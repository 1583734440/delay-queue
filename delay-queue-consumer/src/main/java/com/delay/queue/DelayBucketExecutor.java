package com.delay.queue;

import com.alibaba.fastjson.JSON;
import com.delay.queue.constants.RedisConstants;
import com.delay.queue.disruptor.DisruptorEvent;
import com.delay.queue.disruptor.DisruptorManager;
import com.delay.queue.strategy.DelayTopicStrategy;
import com.delay.queue.strategy.DelayTopicStrategyFactory;
import com.delay.queue.util.StringUtil;
import com.delay.queue.domain.DelayQueueJob;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description TODO
 * @Date 2021/4/11 10:52
 * @Author FU
 */

@Component
@Scope("prototype")
public class DelayBucketExecutor {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    DelayTopicStrategyFactory delayTopicStrategyFactory;

    @Autowired
    DisruptorManager disruptorManager;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private String delayBucketKey = "";

    public void run(String delayBucketKey) {
        this.delayBucketKey = delayBucketKey;
        while (true) {
            try {
                lock.lock();
                String queueId = getQueueId(delayBucketKey);
                while (StringUtil.isBlank(queueId)) {
                    condition.await();
                    queueId = getQueueId(delayBucketKey);
                }
                DelayQueueJob delayQueueJob = getDelayQueueJob(queueId);
                if (delayQueueJob == null) {
                    stringRedisTemplate.opsForZSet().remove(delayBucketKey, queueId);
                    continue;
                }
                long delayTime = delayQueueJob.getDelayTime();
                long now = System.currentTimeMillis();
                if (delayTime > now) {
                    condition.await((delayTime - now), TimeUnit.MILLISECONDS);
                    continue;
                }
                boolean consumeResult = consume(delayQueueJob);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    private boolean consume(DelayQueueJob delayQueueJob) {
        stringRedisTemplate.opsForZSet().remove(delayBucketKey, delayQueueJob.getQueueId());
        stringRedisTemplate.opsForHash().delete(RedisConstants.DELAY_QUEUE_JOBPOOL_KEY, delayQueueJob.getQueueId());
        // TODO 消息处理
        Disruptor<DisruptorEvent<DelayQueueJob>> disruptor = disruptorManager.getDisruptor();
        RingBuffer<DisruptorEvent<DelayQueueJob>> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent(new EventTranslatorOneArg<DisruptorEvent<DelayQueueJob>, Object>() {
            @Override
            public void translateTo(DisruptorEvent<DelayQueueJob> delayQueueJobDisruptorEvent, long l, Object o) {
                delayQueueJobDisruptorEvent.setT(delayQueueJob);
            }
        }, delayQueueJob);
        return true;
    }

    private DelayQueueJob getDelayQueueJob(String queueId) {
        Object o = stringRedisTemplate.opsForHash().get(RedisConstants.DELAY_QUEUE_JOBPOOL_KEY, queueId);
        if (o != null) {
            return JSON.parseObject(o.toString(), DelayQueueJob.class);
        }
        return null;
    }

    private String getQueueId(String delayBucketKey) {
        Set<String> range = stringRedisTemplate.opsForZSet().range(delayBucketKey, 0, 0);
        if (!CollectionUtils.isEmpty(range)) {
            return range.iterator().next();
        }
        return "";
    }

    public void signal() {
        lock.lock();
        condition.signal();
        lock.unlock();
    }
}

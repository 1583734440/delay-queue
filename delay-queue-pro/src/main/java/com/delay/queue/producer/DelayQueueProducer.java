package com.delay.queue.producer;

import com.alibaba.fastjson.JSON;
import com.delay.queue.constants.RedisConstants;
import com.delay.queue.DelayBucketHandler;
import com.delay.queue.StartupInit;
import com.delay.queue.api.DelayQueueProducerApi;
import com.delay.queue.domain.DelayQueueJob;
import com.delay.queue.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class DelayQueueProducer implements DelayQueueProducerApi {

    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean produceDelayMessage(DelayQueueJob delayQueueJob) {
        /*
          设置延迟时间为消息创建时间+延迟时长
         */
        String queueId = delayQueueJob.getQueueId();
        long delayTime = delayQueueJob.getCreateTime() + delayQueueJob.getDelayTime();
        delayQueueJob.setDelayTime(delayTime);
        /*
         * 将延迟消息添加到Jobpool中
         */
        stringRedisTemplate.opsForHash().put(RedisConstants.DELAY_QUEUE_JOBPOOL_KEY, queueId, JSON.toJSONString(delayQueueJob));
        /*
         * 将消息添加至Delay Bucket，用于延迟扫描
         */
        String delayBucketKey = StringUtil.getDelayBucketKey(queueId);
        stringRedisTemplate.opsForZSet().add(delayBucketKey, queueId, delayTime);
        /*
         * 唤醒延迟消息触发处理
         */
        DelayBucketHandler delayBucketHandler = StartupInit.delayBucketHandlers.get(delayBucketKey);
        delayBucketHandler.getDelayBucketExecutor().signal();
        return true;
    }
}

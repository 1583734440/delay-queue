package com.delay.queue.strategy.impl;

import com.delay.queue.domain.DelayQueueJob;
import com.delay.queue.domain.RedisDelayTopic;
import com.delay.queue.strategy.DelayTopicStrategy;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Date 2021/4/11 14:25
 * @Author FU
 */
@Component
@RedisDelayTopic(topic = "order")
public class OrderCancelConsumer implements DelayTopicStrategy {
    @Override
    public void consume(DelayQueueJob delayQueueJob) {
            System.out.println(Thread.currentThread().getName() + " 订单取消处理: " + delayQueueJob.getMessage());
    }
}

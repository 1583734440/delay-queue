package com.delay.queue.strategy;

import com.delay.queue.domain.DelayQueueJob;

/**
 * @Description TODO
 * @Date 2021/4/11 14:23
 * @Author FU
 */

public interface DelayTopicStrategy {

    public void consume(DelayQueueJob delayQueueJob);
}

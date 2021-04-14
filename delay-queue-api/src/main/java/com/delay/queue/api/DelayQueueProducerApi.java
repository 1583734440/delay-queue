package com.delay.queue.api;

import com.delay.queue.domain.DelayQueueJob;

public interface DelayQueueProducerApi {
    /**
     * 延迟消息生产
     * @param delayQueueJob 信息
     * @return boolean
     */
    public boolean produceDelayMessage(DelayQueueJob delayQueueJob);
}

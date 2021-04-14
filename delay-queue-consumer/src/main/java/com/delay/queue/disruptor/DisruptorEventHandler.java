package com.delay.queue.disruptor;

import com.delay.queue.DelayQueueExecutor;
import com.delay.queue.domain.DelayQueueJob;
import com.delay.queue.strategy.DelayTopicStrategy;
import com.delay.queue.strategy.DelayTopicStrategyFactory;
import com.lmax.disruptor.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Date 2021/4/12 14:52
 * @Author FU
 */
@Component
public class DisruptorEventHandler implements EventHandler<DisruptorEvent<DelayQueueJob>> {
    @Autowired
    private DelayQueueExecutor delayQueueExecutor;

    @Override
    public void onEvent(DisruptorEvent<DelayQueueJob> delayQueueJobDisruptorEvent, long l, boolean b) throws Exception {
        DelayQueueJob delayQueueJob = delayQueueJobDisruptorEvent.getT();
        delayQueueExecutor.consume(delayQueueJob);
    }
}

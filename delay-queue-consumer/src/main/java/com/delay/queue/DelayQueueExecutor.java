package com.delay.queue;

import com.delay.queue.domain.DelayQueueJob;
import com.delay.queue.strategy.DelayTopicStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Date 2021/4/14 15:16
 * @Author FU
 */
@Component
public class DelayQueueExecutor {
    @Autowired
    private DelayTopicStrategyFactory delayTopicStrategyFactory;

    @Async("daleyQueueConsumerExecutor")
    public void consume(DelayQueueJob delayQueueJob){
        delayTopicStrategyFactory.getStrategy(delayQueueJob.getTopic()).consume(delayQueueJob);
    }
}

package com.delay.queue.disruptor;

import com.delay.queue.domain.DelayQueueJob;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;

/**
 * @Description TODO
 * @Date 2021/4/13 9:12
 * @Author FU
 */
@Component
@Order(0)
public class DisruptorManager implements CommandLineRunner {
    public static void main(String[] args) {
//        int[] nums1 = new int[]{1,2,3,1};
    }

    @Autowired
    private DisruptorEventHandler disruptorEventHandler;

    private static final int RING_BUFFER_SIZE = 1024 * 1024;

    private Disruptor<DisruptorEvent<DelayQueueJob>> disruptor;

    @Override
    public void run(String... args) throws Exception {
        disruptor = new Disruptor<DisruptorEvent<DelayQueueJob>>(new DisruptorEventFactory<>(), RING_BUFFER_SIZE,
                Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());
        disruptor.handleEventsWith(disruptorEventHandler);
        disruptor.start();

    }

    @PreDestroy
    private void close() {
        if (disruptor != null) {
            disruptor.shutdown();
        }
    }

    public Disruptor<DisruptorEvent<DelayQueueJob>> getDisruptor() {
        return disruptor;
    }

    public void setDisruptor(Disruptor<DisruptorEvent<DelayQueueJob>> disruptor) {
        this.disruptor = disruptor;
    }

}

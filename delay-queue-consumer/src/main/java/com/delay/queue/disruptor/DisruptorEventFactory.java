package com.delay.queue.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @Description TODO
 * @Date 2021/4/12 14:48
 * @Author FU
 */
public class DisruptorEventFactory<T> implements EventFactory<DisruptorEvent<T>> {
    @Override
    public DisruptorEvent<T> newInstance() {
        return new DisruptorEvent<T>();
    }
}

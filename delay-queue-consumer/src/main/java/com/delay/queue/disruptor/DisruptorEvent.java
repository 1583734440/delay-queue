package com.delay.queue.disruptor;

import lombok.Data;

/**
 * @Description TODO
 * @Date 2021/4/12 14:47
 * @Author FU
 */
@Data
public class DisruptorEvent<T> {
    private T t;
}

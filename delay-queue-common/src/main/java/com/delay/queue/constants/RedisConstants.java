package com.delay.queue.constants;

/**
 * @Description TODO
 * @Date 2021/4/7 16:56
 * @Author FU
 */
public interface RedisConstants {
    /**
     * DELAY BUCKEY 桶名称
     */
    public static final String DELAY_QUEUE_BUCKET_PREFIX = "DELAY_BUCKET_KEY_";
    /**
     * 桶得数量 用于增加延迟消息消费得吞吐
     */
    public static final int DELAY_BUCKET_NUM = 16;
    /**
     * JOBPOOL key
     */
    public static final String DELAY_QUEUE_JOBPOOL_KEY = "DELAY_QUEUE_JOBPOOL";

    /**
     * Redis Lock
     */
    public static final String LOCK = "LOCK_";
}

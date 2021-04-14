package com.delay.queue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.IntConsumer;

/**
 * @Description TODO
 * @Date 2021/4/14 14:23
 * @Author FU
 */
@EnableAsync
@Configuration
public class ThreadPoolConfig {

    @Bean("daleyQueueConsumerExecutor")
    public Executor daleyQueueExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(8);
        // 设置最大线程
        executor.setMaxPoolSize(10);
        // 设置缓冲队列大小
        executor.setQueueCapacity(100);
        // 设置线程空闲时间， 60S
        executor.setKeepAliveSeconds(60);
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("daleyQueueExecutor_");
        return executor;
    }

}

package com.delay.queue.producer.tasktest;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Date 2021/4/8 10:58
 * @Author FU
 */
@Component
public class Task {
    @Async("taskExecutor")
    public void doTaskOne(){
        System.out.println("开始任务");
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("结束任务");
    }
}

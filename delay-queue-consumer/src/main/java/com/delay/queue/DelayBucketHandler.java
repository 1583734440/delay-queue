package com.delay.queue;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Date 2021/4/11 11:23
 * @Author FU
 */
@Data
@Component
@Scope("prototype")
public class DelayBucketHandler implements Runnable {

    @Autowired
    private DelayBucketExecutor delayBucketExecutor;

    private String delayBucketKey;

    @Override
    public void run() {
        delayBucketExecutor.run(delayBucketKey);
    }
}

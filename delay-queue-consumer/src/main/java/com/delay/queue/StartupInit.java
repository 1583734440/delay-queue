package com.delay.queue;

import com.delay.queue.constants.RedisConstants;
import com.delay.queue.util.ApplicationContextProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description TODO
 * @Date 2021/4/11 11:31
 * @Author FU
 */
@Component
@Order(1)
public class StartupInit implements CommandLineRunner {

    private ExecutorService executorService =
            Executors.newFixedThreadPool(RedisConstants.DELAY_BUCKET_NUM);

    public static Map<String, DelayBucketHandler> delayBucketHandlers = new
            HashMap<String, DelayBucketHandler>();

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < RedisConstants.DELAY_BUCKET_NUM; i++) {
            DelayBucketHandler delayBucketHandler = ApplicationContextProvider.getBean("delayBucketHandler", DelayBucketHandler.class);
            String delayBucketKey = RedisConstants.DELAY_QUEUE_BUCKET_PREFIX + i;
            delayBucketHandler.setDelayBucketKey(delayBucketKey);
            delayBucketHandlers.put(delayBucketKey, delayBucketHandler);
            executorService.execute(delayBucketHandler);
        }
    }
}

package com.delay.queue.controller;

import com.delay.queue.api.DelayQueueProducerApi;
import com.delay.queue.domain.DelayQueueJob;
import com.delay.queue.producer.tasktest.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping
public class IndexController {
    @Resource
    private DelayQueueProducerApi delayQueueProducerApi;

    @Resource
    private Task task;

    @GetMapping("task")
    public String testTask(){
        task.doTaskOne();
        return "success";
    }


    @RequestMapping
    public String index() {
        log.debug("进入 index 页面！");
        log.info("进入 index 页面！");
        log.warn("进入 index 页面！");
        log.error("进入 index 页面！");
        return "success";
    }

    @RequestMapping("/send")
    public String send() {
        long now = System.currentTimeMillis();
        DelayQueueJob delayQueueJob = DelayQueueJob.builder().
                queueId(UUID.randomUUID().toString().replace("-", ""))
                .message("订单取消！")
                .topic("order")
                .delayTime(0)
                .createTime(now)
                .build();
        delayQueueProducerApi.produceDelayMessage(delayQueueJob);
//
        DelayQueueJob delayQueueJob2 = DelayQueueJob.builder().
                queueId(UUID.randomUUID().toString().replace("-", ""))
                .message("领取优惠券！")
                .topic("login")
                .delayTime(0)
                .createTime(now)
                .build();
        delayQueueProducerApi.produceDelayMessage(delayQueueJob2);
        return "success";
    }
}

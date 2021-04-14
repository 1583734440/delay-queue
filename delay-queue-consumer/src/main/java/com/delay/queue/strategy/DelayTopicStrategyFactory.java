package com.delay.queue.strategy;

import com.delay.queue.domain.RedisDelayTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Date 2021/4/11 14:49
 * @Author FU
 */
@Component
public class DelayTopicStrategyFactory {
    @Autowired
    private List<DelayTopicStrategy> delayTopicStrategies;

    private Map<String, DelayTopicStrategy> strategyMap = new HashMap<>();

    public DelayTopicStrategy getStrategy(String topic) {
        return strategyMap.get(topic);
    }

    @PostConstruct
    public void strategyCache() {
        if (!CollectionUtils.isEmpty(delayTopicStrategies)) {
            for (DelayTopicStrategy strategy : delayTopicStrategies) {
                RedisDelayTopic redisDelayTopic = strategy.getClass().getAnnotation(RedisDelayTopic.class);
                strategyMap.put(redisDelayTopic.topic(), strategy);
            }
        }
    }
}

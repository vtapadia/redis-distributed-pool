package com.vtapadia.playground.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Bean(destroyMethod="shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("localhost:6379");
        return Redisson.create(config);
    }

    @Bean
    public RLiveObjectService liveObjectService(RedissonClient client) {
        return client.getLiveObjectService();
    }
}

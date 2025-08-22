package com.matridx.igams.common.redis;

import com.matridx.springboot.util.base.StringUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Value("${spring.redis.host:}")
    private String host;
    @Value("${spring.redis.port:0}")
    private int port;
    @Value("${spring.redis.redission.watchdogTimeout:10}")
    private int watchdogTimeout;
    @Value("${spring.redis.password:}")
    private String password;
    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.setLockWatchdogTimeout(watchdogTimeout * 1000L);
        config.useSingleServer().setAddress("redis://"+host+":"+port);
        if(StringUtil.isNotBlank(password)){
            config.useSingleServer().setPassword(password);
        }
        return Redisson.create(config);
    }
}


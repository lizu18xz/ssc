package com.fayayo.config;

import com.fayayo.bean.RedisParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * @author dalizu on 2019/9/27.
 * @version v1.0
 * @desc
 */
@Configuration
@ConditionalOnProperty(name = "system.redis-enable",havingValue = "true")
@AutoConfigureAfter(ParamConfiguration.class)
public class RedisConfiguration {


    @Autowired
    ParamConfiguration paramConfiguration;


    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        RedisParam redisParam = paramConfiguration.getRedisParam();
        //相比jedis线程安全
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisParam.getHost(),
                redisParam.getPort());
        return lettuceConnectionFactory;
    }
}

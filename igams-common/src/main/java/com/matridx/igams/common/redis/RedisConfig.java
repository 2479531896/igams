package com.matridx.igams.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
	@Value("${spring.redis.host:}")
    private String host;
    @Value("${spring.redis.port:0}")
    private int port;
    @Value("${spring.redis.timeout:0}")
    private int timeout;
    @Value("${spring.redis.password:}")
    private String password;
    @Value("${spring.redis.url:}")
    private String url;
    private final Logger log = LoggerFactory.getLogger(RedisConfig.class);
    /*@Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory){
        StringRedisTemplate template = new StringRedisTemplate(factory);
        return template;
    }*/
    
    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		log.error("host:"+host);
		log.error("port:"+port);
		log.error("timeout:"+timeout);
		log.error("password:"+password);

    	RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
    	template.setConnectionFactory(redisConnectionFactory);
    	Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    	ObjectMapper om = new ObjectMapper();
    	om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
    	om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    	jackson2JsonRedisSerializer.setObjectMapper(om);
    	StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    	// key采用String的序列化方式
    	template.setKeySerializer(stringRedisSerializer);
    	// hash的key也采用String的序列化方式
    	template.setHashKeySerializer(stringRedisSerializer);
    	// value序列化方式采用jackson
    	template.setValueSerializer(jackson2JsonRedisSerializer);
    	// hash的value序列化方式采用jackson
    	template.setHashValueSerializer(jackson2JsonRedisSerializer);
    	template.afterPropertiesSet();
    	return template;
    }
    
}

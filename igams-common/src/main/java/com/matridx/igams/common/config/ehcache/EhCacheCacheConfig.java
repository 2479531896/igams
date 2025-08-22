package com.matridx.igams.common.config.ehcache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;  
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.Objects;

@Configuration  
public class EhCacheCacheConfig {
    @Value("${spring.cache.ehcache.config:classpath:config/ehcache/ehcache.xml}")
    private String config;
    @Bean  
    public CacheManager ehCacheCacheManager() {
        return new EhCacheCacheManager(Objects.requireNonNull(ehcacheCacheManager().getObject()));
    }  
  
    @Bean  
    public EhCacheManagerFactoryBean ehcacheCacheManager() {  
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();  
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource(config.split(":")[1]));
        return cacheManagerFactoryBean;  
    }  
}
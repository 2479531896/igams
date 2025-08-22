package com.matridx.igams.common.config.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author WYX
 * @version 1.0
 * {@code @className} AopConfig
 * {@code @description} TODO
 * {@code @date} 13:11 2023/3/21
 **/
@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.matridx.igams")
public class AopConfig {
}

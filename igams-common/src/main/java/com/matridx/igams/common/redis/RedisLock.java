package com.matridx.igams.common.redis;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author WYX
 * @version 1.0
 * @className RedisLock
 * @description TODO 自定义注解实现Redis锁
 * @date 13:05 2023/3/21
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {
    /**
     * 锁key前缀
     */
    String key() default "Matridx";

    /**
     * 锁value
     */
    String value() default "matridx";

    /**
     * 超时时间
     */
    long timeout() default 10;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}

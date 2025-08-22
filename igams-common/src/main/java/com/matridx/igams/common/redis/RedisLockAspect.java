package com.matridx.igams.common.redis;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
/**
 * @author WYX
 * @version 1.0
 * @className RedisLockAspect
 * @description TODO AOP与自定义注解结合实现锁
 * @date 13:08 2023/3/21
 **/
@Component
@Aspect
public class RedisLockAspect {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    RedissonClient redissonClient;

    @Autowired(required = false)
    private final Logger log = LoggerFactory.getLogger(RedisLockAspect.class);

    //配置切点
    @Pointcut("@annotation(redisLock)")
    public void cutPoint(RedisLock redisLock){
    }

    /**
     * @description joinPoint(连接点)
     * @param joinPoint
     * @return Object
     */
    @Around("cutPoint(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint,RedisLock redisLock) throws Throwable{
        log.error("线程{}，进入切面", Thread.currentThread().getName());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取目标类名方法名
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String key = className + methodName +":" + redisLock.value();
        //Boolean isSuccess = this.tryLock(key,redisLock.value(),redisLock.timeout(),redisLock.timeUnit());
        //公平锁 先进先出   性能会差一点
        RLock fairLock = redissonClient.getFairLock(key);
        //获取锁  不加参数  watchDog 自动锁续期直到方法执行完   假设服务器宕机 锁会在watchDog的超时时间后释放（10秒）
        fairLock.lock();
        //获取到锁
        Object result;
        try {
            //执行
            result = joinPoint.proceed();
        }finally {
            //解锁
            fairLock.unlock();
        }
        return result;
        //如果获取到锁
       /* if (!ObjectUtils.isEmpty(isSuccess) && isSuccess.equals(Boolean.TRUE)) {
            log.error("RedisLockAspect tryLock key = [{}]", key);
            Object result;
            try {
                //执行
                result = joinPoint.proceed();
            }finally {
                //解锁
                this.unLock(key);
            }
            return result;
        }else {
            log.error("RedisLockAspect tryLock fail, key = [{}]", key);
            throw new BusinessException("系统繁忙,请稍后");
        }*/
    }
    /*
     * 获取锁
     */
  /*  public Boolean tryLock(String key, String value, long timeout, TimeUnit timeUnit) {
        Boolean isSuccess = null;
        try {
            //自旋十秒获取锁
            long endTime = System.currentTimeMillis() + timeout * 1000;
            do {
                //若有指定key返回false 无返回true 指定过期时间避免死锁
                isSuccess = redisUtil.setIfAbsent(key, value, timeout+1, timeUnit);
                //自旋指定时间 一个不满足一直自旋
            } while (System.currentTimeMillis() - endTime < 0 && (ObjectUtils.isEmpty(isSuccess) || !isSuccess.equals(Boolean.TRUE)));
        }catch (Exception e){
            log.error("RedisLockAspect getLock 异常!! error:{}", e.getMessage());
        }
        return isSuccess;
    }*/
    /*
     * 释放锁
     */
   /* private void unLock(String key) {
        try {
            redisUtil.del(key);
        } catch (Exception e) {
            log.error("RedisLockAspect unLock failure!! error:{}", e.getMessage());
        }
    }*/
}

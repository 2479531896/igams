package com.matridx.igams.common.util.sseemitter;



import com.matridx.igams.common.enums.GlobalParmEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 首页的推送工具类
 */

public class SseEmitterAllServer {
	
	private static final Logger logger = LoggerFactory.getLogger(SseEmitterAllServer.class);
    static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    /**
     * 当前连接数
     */

    private static final Map<String,AtomicInteger>  countMap = new HashMap<>();
    private static final Map<String,Map<String, SseEmitter>> sseEmitterMapAll = new HashMap<>();
    static {
        sseEmitterMapAll.put(GlobalParmEnum.SAMPLE_COLLECTION.getCode(),new ConcurrentHashMap<>());
        countMap.put(GlobalParmEnum.SAMPLE_COLLECTION.getCode(),new AtomicInteger(0));
        sseEmitterMapAll.put(GlobalParmEnum.SPECIMEN_EXCEPTION.getCode(),new ConcurrentHashMap<>());
        countMap.put(GlobalParmEnum.SPECIMEN_EXCEPTION.getCode(),new AtomicInteger(0));

    }

    /**
     * 创建用户连接并返回 SseEmitter
     *
     * @param userId 用户ID
     * @return SseEmitter
     */
    public static SseEmitter connect(String userId,String type) {

        logger.error("Sse 准备创建新的连接，当前用户：{} 类型：{}", userId,type);
        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        Map<String, SseEmitter> sseEmitterMap =  sseEmitterMapAll.get(type);
        if (null == sseEmitterMap){
            return null;
        }
        AtomicInteger count = countMap.get(type);
        if (null != sseEmitterMap.get(userId)){
            removeUser(userId,type);
        }
        logger.error("Sse 检查完成，准备创建新的连接，当前用户数据：{} ",count.intValue());
        SseEmitter sseEmitter = new SseEmitter(45*60*1000L);
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(userId,type));
        sseEmitter.onError(errorCallBack(userId,type));
        sseEmitter.onTimeout(timeoutCallBack(userId,type));
        sseEmitterMap.put(userId, sseEmitter);
        // 数量+1
        count.getAndIncrement();
        logger.error("Sse 创建新的连接，当前用户：{}", userId);
        return sseEmitter;
    }
    /**
     * 给指定用户发送信息
     */
    public static void sendMessage(String userId, String message,String type) {
    	logger.error("Sse 推送消息给用户：{} 消息内容：{}  消息类型：type", userId,message,type);
        Map<String, SseEmitter> sseEmitterMap =  sseEmitterMapAll.get(type);
        if (null == sseEmitterMap){
        	logger.error("Sse 推送消息给用户：未找到相应连接");
            return;
        }
        if (sseEmitterMap.containsKey(userId)) {
            try {
                // sseEmitterMap.get(userId).send(message, MediaType.APPLICATION_JSON);
                sseEmitterMap.get(userId).send(message);
            	logger.error("Sse 推送消息给用户：推送完成");
            } catch (Exception e) {
                logger.error("Sse 用户[{}]推送异常:{}", userId, e.getMessage());
                removeUser(userId,type);
            }
        }
    }

    /**
     * 移除用户连接,并关闭连接
     */
    public static void removeUser(String userId,String type) {
        logger.error("removeUser Sse 准备移除用户：{}", userId);
        Map<String, SseEmitter> sseEmitterMap =  sseEmitterMapAll.get(type);
        //移除用户前先关闭连接
        try{
            SseEmitter sseEmitter = sseEmitterMap.get(userId);
            sseEmitter.complete();
        } catch (Exception e) {
            logger.error("removeUser Sse 用户[{}]关闭异常:{}", userId, e.getMessage());
        }
        logger.error("removeUser Sse 移除后用户数：{}", sseEmitterMap.size());
    }

    /**
     * 移除用户连接，只移除连接，不关闭连接
     */
    public static void removeUserFromMap(String userId,String type) {
        logger.error("removeUserFromMap Sse 移除用户：{}", userId);
        Map<String, SseEmitter> sseEmitterMap =  sseEmitterMapAll.get(type);
        AtomicInteger count = countMap.get(type);
        //移除用户前先关闭连接
        try{
            sseEmitterMap.remove(userId);
        } catch (Exception e) {
            logger.error("removeUserFromMap Sse 用户[{}]关闭异常:{}", userId, e.getMessage());
        }
        // 数量-1
        if (count.get() > 0)
            count.getAndDecrement();
        logger.error("removeUserFromMap Sse 移除后用户数：{}", sseEmitterMap.size());
    }

    /**
     * 获取当前连接信息
     */
    public static List<String> getIds(String type) {
        Map<String, SseEmitter> sseEmitterMap =  sseEmitterMapAll.get(type);
        return new ArrayList<>(sseEmitterMap.keySet());
    }

    /**
     * 获取当前连接信息
     */
    public static SseEmitter getEmitter(String type,String key) {
        Map<String, SseEmitter> sseEmitterMap =  sseEmitterMapAll.get(type);
        return sseEmitterMap.get(key);
    }

    /**
     * 获取当前连接数量
     */
    public static int getUserCount(String type) {
        AtomicInteger count = countMap.get(type);
        return count.intValue();
    }

    private static Runnable completionCallBack(String userId,String type) {
        return () -> {
            logger.error("Sse 结束连接：{}", userId);
            removeUserFromMap(userId,type);
        };
    }

    private static Runnable timeoutCallBack(String userId,String type) {
        return () -> {
            logger.error("Sse 连接超时：{}", userId);
            removeUserFromMap(userId,type);
        };
    }

    private static Consumer<Throwable> errorCallBack(String userId,String type) {
        return throwable -> {
            logger.error("Sse 连接异常：{}", userId);
            removeUserFromMap(userId,type);
        };
    }
    /**
     * 群发所有人
     */
    public static void batchSendMessage(String wsInfo,String type) {
        logger.error("Sse 群发消息batchSendMessage，消息内容：{} 类型：{}", wsInfo,type);
        Map<String, SseEmitter> sseEmitterMap =  sseEmitterMapAll.get(type);
        //AtomicInteger count = countMap.get(type);
        sseEmitterMap.forEach((k, v) -> {
            Runnable newRunnable = new Runnable() {
                public void run() {
                    try {
                        v.send(wsInfo, MediaType.APPLICATION_JSON);
                    } catch (IOException e) {
                        logger.error("用户[{}]推送异常:{}", k, e.getMessage());
                        removeUser(k,type);
                    }
                }
            };
            cachedThreadPool.execute(newRunnable);

        });
    }

}

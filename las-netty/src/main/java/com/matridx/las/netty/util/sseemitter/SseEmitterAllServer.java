package com.matridx.las.netty.util.sseemitter;

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

import com.matridx.las.netty.enums.GlobalParmEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.websocket.server.ServerEndpoint;

/**
 * 首页的推送工具类
 */
@ServerEndpoint("webSocket/input")
public class SseEmitterAllServer{
	
	private static final Logger logger = LoggerFactory.getLogger(SseEmitterAllServer.class);
    static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    /**
     * 当前连接数
     */

    private static Map<String,AtomicInteger>  countMap = new HashMap<>();
    private static Map<String,Map<String, SseEmitter>> sseEmitterMapAll = new HashMap<>();
    static {
        sseEmitterMapAll.put(GlobalParmEnum.HTML_TYPE_ALL.getCode(),new ConcurrentHashMap<>());
        sseEmitterMapAll.put(GlobalParmEnum.HTML_TYPE_INPUT.getCode(),new ConcurrentHashMap<>());
        sseEmitterMapAll.put(GlobalParmEnum.HTML_TYPE_CHN.getCode(),new ConcurrentHashMap<>());
        countMap.put(GlobalParmEnum.HTML_TYPE_ALL.getCode(),new AtomicInteger(0));
        countMap.put(GlobalParmEnum.HTML_TYPE_INPUT.getCode(),new AtomicInteger(0));
        countMap.put(GlobalParmEnum.HTML_TYPE_CHN.getCode(),new AtomicInteger(0));

    }

    /**
     * 创建用户连接并返回 SseEmitter
     *
     * @param userId 用户ID
     * @return SseEmitter
     */
    public static SseEmitter connect(String userId,String type) {
        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        Map<String, SseEmitter> sseEmitterMap =  sseEmitterMapAll.get(type);
        AtomicInteger count = countMap.get(type);
        SseEmitter sseEmitter = new SseEmitter(0L);
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(userId,type));
        sseEmitter.onError(errorCallBack(userId,type));
        sseEmitter.onTimeout(timeoutCallBack(userId,type));
        sseEmitterMap.put(userId, sseEmitter);
        // 数量+1
        count.getAndIncrement();
        logger.info("创建新的all连接，当前用户：{}", userId);
        return sseEmitter;
    }
    /**
     * 给指定用户发送信息
     */
    public static void sendMessage(String userId, String message,String type) {
        Map<String, SseEmitter> sseEmitterMap =  sseEmitterMapAll.get(type);
        if (sseEmitterMap.containsKey(userId)) {
            try {
                // sseEmitterMap.get(userId).send(message, MediaType.APPLICATION_JSON);
                sseEmitterMap.get(userId).send(message);
            } catch (IOException e) {
                logger.error("用户[{}]推送异常:{}", userId, e.getMessage());
                removeUser(userId,type);
            }
        }
    }
    /**
     * 移除用户连接
     */
    public static void removeUser(String userId,String type) {
        Map<String, SseEmitter> sseEmitterMap =  sseEmitterMapAll.get(type);
        AtomicInteger count = countMap.get(type);
        sseEmitterMap.remove(userId);
        // 数量-1
        count.getAndDecrement();
        logger.info("移除用户：{}", userId);
        logger.error("移除用户：{}", userId);
        logger.error("移除用户111：{}", sseEmitterMap.size());
    }

    /**
     * 获取当前连接信息
     */
    public static List<String> getIds(String type) {
        Map<String, SseEmitter> sseEmitterMap =  sseEmitterMapAll.get(type);
        return new ArrayList<>(sseEmitterMap.keySet());
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
            logger.info("结束连接：{}", userId);
            removeUser(userId,type);
        };
    }

    private static Runnable timeoutCallBack(String userId,String type) {
        return () -> {
            logger.info("连接超时：{}", userId);
            removeUser(userId,type);
        };
    }

    private static Consumer<Throwable> errorCallBack(String userId,String type) {
        return throwable -> {
            logger.info("连接异常：{}", userId);
            removeUser(userId,type);
        };
    }
    /**
     * 群发所有人
     */
    public static void batchSendMessage(String wsInfo,String type) {
        Map<String, SseEmitter> sseEmitterMap =  sseEmitterMapAll.get(type);
        AtomicInteger count = countMap.get(type);
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

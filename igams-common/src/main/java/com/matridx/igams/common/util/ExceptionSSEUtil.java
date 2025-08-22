package com.matridx.igams.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.SjycDto;
import com.matridx.igams.common.enums.GlobalParmEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ISjyctzService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;


@Component
public class ExceptionSSEUtil {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    ISjyctzService sjyctzService;

    @Autowired(required = false)
    private AmqpTemplate amqpTempl;

    private static ExceptionSSEUtil exceptionSSEUtil;

    @PostConstruct
    public void init() {
        exceptionSSEUtil = this;
        exceptionSSEUtil.redisUtil = this.redisUtil;
        exceptionSSEUtil.amqpTempl = this.amqpTempl;
    }

    private static final Logger logger = LoggerFactory.getLogger(ExceptionSSEUtil.class);
    static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    /**
     * 当前连接数
     */

    private static final Map<String, AtomicInteger> countMap = new HashMap<>();
    private static final Map<String, Map<String, SseEmitter>> sseEmitterMapAll = new HashMap<>();


    /**
     * 创建用户连接并返回 SseEmitter
     *
     * @param userId 用户ID
     * @return SseEmitter
     */
    public static SseEmitter connect(String userId, String key)  {

        //logger.error("Sse 准备创建新的连接，当前用户：{} 类型：{}", userId, key);
        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        Map<String, SseEmitter> sseEmitterMap = sseEmitterMapAll.get(userId) == null ? new ConcurrentHashMap<>() : sseEmitterMapAll.get(userId);

        AtomicInteger count = countMap.get(userId) == null ? new AtomicInteger(0) : countMap.get(userId);

        logger.error("Sse 检查完成，准备创建新的连接，当前用户数据：{} ，当前用户：{} 类型：{}", count.intValue(),userId, key);
        SseEmitter sseEmitter = new SseEmitter(45*60*1000L);
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(userId, key));
        sseEmitter.onError(errorCallBack(userId, key));
        sseEmitter.onTimeout(timeoutCallBack(userId, key));
        sseEmitterMap.put(key, sseEmitter);
        sseEmitterMapAll.put(userId, sseEmitterMap);
        // 数量+1
        count.getAndIncrement();
        if (countMap.get(userId) == null) {
            countMap.put(userId, count);
        }

        try {
            /**
             * 用来触发source.onopen
             */
            Map<String,String>conMap=new HashMap<>();
            conMap.put("msg","start");
            sseEmitter.send(JSONObject.toJSONString(conMap));
        } catch (IOException e) {
            logger.error("Sse 创建新的连接发送消息，当前用户：{},连接key:{}", userId, key);
            e.printStackTrace();
        }
        //logger.error("Sse 创建新的连接，当前用户：{},连接key:{}", userId, key);
        return sseEmitter;
    }

    /**
     * 移除用户连接
     */
    public static void removeUser(String userId, String key) {
        //logger.error("Sse 移除用户：{}", userId);
        Map<String, SseEmitter> sseEmitterMap = sseEmitterMapAll.get(userId);
        AtomicInteger count = countMap.get(userId);
        sseEmitterMap.remove(key);
        // 数量-1
        count.getAndDecrement();
        logger.error("Sse 移除后用户数：{} 移除用户id：{}", sseEmitterMap.size(),userId);
    }


    private static Runnable completionCallBack(String userId, String key) {
        return () -> {
            logger.error("Sse 结束连接：{}", userId);
            removeUser(userId, key);
        };
    }

    private static Runnable timeoutCallBack(String userId, String key) {
        return () -> {
            logger.error("Sse 连接超时：{}", userId);
            removeUser(userId, key);
        };
    }

    private static Consumer<Throwable> errorCallBack(String userId, String key) {
        return throwable -> {
            logger.error("Sse 连接异常：{}", userId);
            removeUser(userId, key);
        };
    }


    /**
     * 给指定用户下所有连接发送消息
     */
    public static void batchSendMessage(String wsInfo, String userid) {

        Map<String, SseEmitter> sseEmitterMap = sseEmitterMapAll.get(userid);
        if (sseEmitterMap == null) {
            logger.error("当前人员没有长连接{}", userid);
            return;
        }
        //AtomicInteger count = countMap.get(type);
        sseEmitterMap.forEach((k, v) -> {
            try {
                logger.error("Sse 群发消息batchSendMessage，消息内容：{} 用户：{}", wsInfo, userid);

                JSONObject json = JSONObject.parseObject(wsInfo);
                json.put("key", k);
                sseEmitterMap.get(k).send(json.toJSONString());
                //v.send(json.toJSONString(), MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                logger.error("用户[{}]推送异常:{}", k, e.getMessage());
                removeUser(userid, k);
            }
            // cachedThreadPool.execute(newRunnable);

        });

    }

    /**
     * 查看后redis数据操作
     *
     * @param yhid
     * @param ycid
     */
    public void viewExceptionMessage(String yhid, String ycid) {
        if (StringUtil.isNotBlank(yhid)) {
            Object exceptionConectMap_redis = redisUtil.get("EXCEPTION_CONNECT:" + yhid);
            if (exceptionConectMap_redis != null) {
                JSONObject exceptionConectMap = JSON.parseObject(exceptionConectMap_redis.toString());
                JSONObject exceptionlist = JSON.parseObject(exceptionConectMap.get("exceptionlist").toString());
                if (exceptionlist.get(ycid) != null) {
                    JSONObject exceptionMap = JSON.parseObject(exceptionlist.get(ycid).toString());
                    exceptionConectMap.put("unreadcount", Integer.parseInt(exceptionConectMap.get("unreadcount").toString()) - Integer.parseInt(exceptionMap.get("ex_unreadcnt").toString()));
                    exceptionlist.remove(ycid);
                    exceptionConectMap.put("exceptionlist", exceptionlist);
                    if ((Integer.parseInt(exceptionConectMap.get("unreadcount").toString()) - Integer.parseInt(exceptionMap.get("ex_unreadcnt").toString())) == 0) {
                        redisUtil.del("EXCEPTION_CONNECT:" + yhid);

                    } else {
                        redisUtil.set("EXCEPTION_CONNECT:" + yhid, JSONObject.toJSONString(exceptionConectMap), -1);
                    }
                }
                //exceptionConectMap_redis = redisUtil.get("EXCEPTION_CONNECT:" + yhid);
                //batchSendMessage(exceptionConectMap_redis.toString(), yhid);
            }

        }
    }

    /***
     * 查看已经结束的异常
     * @param yhid
     * @param ycid
     */
    public void viewFinishException(String yhid, String ycid){
        if (StringUtil.isNotBlank(yhid)) {
            Object exceptionConectMap_redis = redisUtil.get("EXCEPTION_CONNECT:" + yhid);
            if (exceptionConectMap_redis != null) {
                JSONObject exceptionConectMap = JSON.parseObject(exceptionConectMap_redis.toString());
                JSONObject exceptionlist = JSON.parseObject(exceptionConectMap.get("exceptionlist").toString());
                if (exceptionlist.get(ycid) != null) {
                    JSONObject exceptionMap = JSON.parseObject(exceptionlist.get(ycid).toString());
                    exceptionConectMap.put("finreadcount", Integer.parseInt(exceptionConectMap.get("finreadcount").toString()) - Integer.parseInt(exceptionMap.get("ex_unreadcnt").toString()));
                    exceptionlist.remove(ycid);
                    exceptionConectMap.put("exceptionlist", exceptionlist);
                    redisUtil.set("EXCEPTION_CONNECT:" + yhid, JSONObject.toJSONString(exceptionConectMap), -1);
                }
                //exceptionConectMap_redis = redisUtil.get("EXCEPTION_CONNECT:" + yhid);
                //batchSendMessage(exceptionConectMap_redis.toString(), yhid);
            }

        }
    }

    /**
     * 结束异常的操作
     * @param ycid
     */
    public  void finishException(String ycid){
        if (StringUtil.isNotBlank(ycid)) {
            Set<String> set = redisUtil.getKeys("EXCEPTION_CONNECT:*");
            for (String key : set) {
                Object exceptionConectMap_redis = redisUtil.get(key);
                if (exceptionConectMap_redis != null) {
                    JSONObject exceptionConectMap = JSON.parseObject(exceptionConectMap_redis.toString());
                    JSONObject exceptionlist = JSON.parseObject(exceptionConectMap.get("exceptionlist").toString());
                    if (exceptionlist!=null&&exceptionlist.get(ycid) != null) {
                        JSONObject exceptionMap = JSON.parseObject(exceptionlist.get(ycid).toString());
                        exceptionMap.put("sfjs","1");
                        exceptionConectMap.put("unreadcount", Integer.parseInt(exceptionConectMap.get("unreadcount").toString()) - Integer.parseInt(exceptionMap.get("ex_unreadcnt").toString()));
                        exceptionConectMap.put("finreadcount", Integer.parseInt(exceptionConectMap.get("finreadcount")==null?"0":exceptionConectMap.get("finreadcount").toString()) +Integer.parseInt(exceptionMap.get("ex_unreadcnt").toString()));
                        exceptionConectMap.put("exceptionlist", exceptionlist);
                        exceptionlist.put(ycid,exceptionMap);
                        redisUtil.set(key, JSONObject.toJSONString(exceptionConectMap), -1);
                        List<SjycDto> list=new ArrayList<>();
                        SjycDto sjycDto = new SjycDto();
                        String newKey=key.replace("EXCEPTION_CONNECT:","");
                        sjycDto.setRyid(newKey);
                        sjycDto.setYcid(ycid);
                        list.add(sjycDto);
                        sendByList(list, ycid);
                    }
                }
            }
        }
    }
    /**
     * 根号人员发送消息
     *
     * @param list
     * @param ycid
     */
    public void sendByList(List<SjycDto>list, String ycid) {
        if (list!=null&&!list.isEmpty()) {
            for (SjycDto sjycDto : list) {

                Object hget = redisUtil.get(  GlobalParmEnum.SPECIMEN_EXCEPTION.getCode()+":"+sjycDto.getRyid());
                if(hget!=null){
                    Map<String,Object> result = JSON.parseObject((String) hget);
                    result.put("userid",sjycDto.getRyid());
                    result.put("ycid",ycid);
                    Object exceptionConectMap_redis = redisUtil.get("EXCEPTION_CONNECT:" + sjycDto.getRyid());
                    String wsInfo=exceptionConectMap_redis.toString();
                    result.put("wsInfo",wsInfo);
                    result.put("type","sendMsg_exception");
                    amqpTempl.convertAndSend("wechat.exchange", MqType.SSE_SENDMSG+result.get("ssekey"),JSONObject.toJSONString(result));
                }
//                Object exceptionConectMap_redis = redisUtil.get("EXCEPTION_CONNECT:" + sjycDto.getRyid());
//                if (exceptionConectMap_redis != null) {
//                    batchSendMessage(exceptionConectMap_redis.toString(), sjycDto.getRyid());
//                }

            }
        }
    }

    /***
     *判断当前人员是否是第一条消息
     * @param userid
     * @param ycid
     * @return
     */
    public boolean isSendDD(String userid,String ycid){
        boolean flag=false;
        Object exceptionConectMap_redis = redisUtil.get("EXCEPTION_CONNECT:" + userid);
        if(exceptionConectMap_redis!=null){
            JSONObject exceptionConectMap = JSON.parseObject(exceptionConectMap_redis.toString());
            JSONObject exceptionlist = JSON.parseObject(exceptionConectMap.get("exceptionlist").toString());
            if (exceptionlist.get(ycid) != null) {
                JSONObject exceptionMap = JSON.parseObject(exceptionlist.get(ycid).toString());
                if("1".equals(exceptionMap.get("ex_unreadcnt").toString())){
                    flag=true;
                }
            }
        }
        return  flag;
    }
    /**
     * redis添加需要通知人员
     *
     * @param list
     * @param ycid
     */
    public void addExceptionMessage(List<SjycDto> list, String ycid) {
        if (list!=null&&!list.isEmpty()) {
            for (SjycDto sjycDto : list) {

                Object exceptionConectMap_redis = redisUtil.get("EXCEPTION_CONNECT:" + sjycDto.getRyid());
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String dateTime = time.format(date);
                if (exceptionConectMap_redis == null) {
                    JSONObject exceptionConectMap = new JSONObject();
                    exceptionConectMap.put("unreadcount", 1);
                    exceptionConectMap.put("lastUpdateTime", dateTime);
                    Map<String, Object> exceptionMap = new HashMap<>();
                    exceptionMap.put("ex_unreadcnt", 1);
                    exceptionMap.put("sfjs", 0);
                    exceptionMap.put("lastUpdateTime", dateTime);
                    Map<String, Object> exceptionlist = new HashMap<>();
                    exceptionlist.put(ycid, exceptionMap);
                    exceptionConectMap.put("exceptionlist", exceptionlist);
                    redisUtil.set("EXCEPTION_CONNECT:" +  sjycDto.getRyid(), JSONObject.toJSONString(exceptionConectMap), -1);

                } else {
                    JSONObject exceptionConectMap = JSON.parseObject(exceptionConectMap_redis.toString());

                    exceptionConectMap.put("lastUpdateTime", dateTime);
                    JSONObject exceptionlist = JSON.parseObject(exceptionConectMap.get("exceptionlist").toString());
                    if (exceptionlist.get(ycid) != null) {
                        JSONObject exceptionMap = JSON.parseObject(exceptionlist.get(ycid).toString());
                        exceptionMap.put("ex_unreadcnt", Integer.parseInt(exceptionMap.get("ex_unreadcnt").toString()) + 1);
                        exceptionMap.put("lastUpdateTime", dateTime);
                        exceptionlist.put(ycid, exceptionMap);
                        exceptionConectMap.put("exceptionlist", exceptionlist);
                    } else {
                        Map<String, Object> exceptionMap = new HashMap<>();
                        exceptionMap.put("ex_unreadcnt", 1);
                        exceptionMap.put("sfjs", 0);
                        exceptionMap.put("lastUpdateTime", dateTime);
                        exceptionlist.put(ycid, exceptionMap);
                        exceptionConectMap.put("exceptionlist", exceptionlist);
                    }
                    // 1.通过keySet
                    int num = 0;
                    for (String key : exceptionlist.keySet()) {
                        if (key.equals(ycid)) {
                            JSONObject exceptionMap = JSON.parseObject(JSONObject.toJSONString(exceptionlist.get(key)));
                            if(exceptionMap.get("sfjs") != null && "0".equals(exceptionMap.get("sfjs").toString())){
                                num += Integer.parseInt(exceptionMap.get("ex_unreadcnt").toString());
                            }
                        } else {
                            JSONObject exceptionMap = JSON.parseObject(exceptionlist.get(key).toString());
                            if(exceptionMap.get("sfjs") != null && "0".equals(exceptionMap.get("sfjs").toString())){
                                num += Integer.parseInt(exceptionMap.get("ex_unreadcnt").toString());
                            }

                        }

                    }
                    exceptionConectMap.put("unreadcount", num);


                    redisUtil.set("EXCEPTION_CONNECT:" +  sjycDto.getRyid(), JSONObject.toJSONString(exceptionConectMap), -1);
                }


            }
        }

        sendByList(list, ycid);

    }
}

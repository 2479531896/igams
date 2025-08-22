package com.matridx.server.wechat.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.redis.RedisUtil;


import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component
public class ExceptionWechartUtil {

    @Autowired
    private RedisUtil redisUtil;


    private static ExceptionWechartUtil exceptionWechartUtil;

    @PostConstruct
    public void init() {
        exceptionWechartUtil = this;
        exceptionWechartUtil.redisUtil = this.redisUtil;
    }

    private static final Logger logger = LoggerFactory.getLogger(ExceptionWechartUtil.class);
    static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();


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
                    exceptionConectMap.put("unreadcount", Integer.valueOf(exceptionConectMap.get("unreadcount").toString()) - Integer.valueOf(exceptionMap.get("ex_unreadcnt").toString()));
                    exceptionlist.remove(ycid);
                    exceptionConectMap.put("exceptionlist", exceptionlist);
                    if ((Integer.valueOf(exceptionConectMap.get("unreadcount").toString()) - Integer.valueOf(exceptionMap.get("ex_unreadcnt").toString())) == 0) {
                        redisUtil.del("EXCEPTION_CONNECT:" + yhid);

                    } else {
                        redisUtil.set("EXCEPTION_CONNECT:" + yhid, JSONObject.toJSONString(exceptionConectMap), -1);
                    }
                }
            }

        }
    }


    /**
     * redis添加需要通知人员
     *
     * @param yhid
     * @param ycid
     */
    public void addExceptionMessage(String yhid, String ycid) {
        Object exceptionConectMap_redis = redisUtil.get("EXCEPTION_CONNECT:" + yhid);
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dateTime = time.format(date);
        if (exceptionConectMap_redis == null) {
            JSONObject exceptionConectMap = new JSONObject();
            exceptionConectMap.put("unreadcount", 1);
            exceptionConectMap.put("lastUpdateTime", dateTime);
            Map<String, Object> exceptionMap = new HashMap<>();
            exceptionMap.put("ex_unreadcnt", 1);
            exceptionMap.put("lastUpdateTime", dateTime);
            Map<String, Object> exceptionlist = new HashMap<>();
            exceptionlist.put(ycid, exceptionMap);
            exceptionConectMap.put("exceptionlist", exceptionlist);
            redisUtil.set("EXCEPTION_CONNECT:" + yhid, JSONObject.toJSONString(exceptionConectMap), -1);
        } else {
            JSONObject exceptionConectMap = JSON.parseObject(exceptionConectMap_redis.toString());

            exceptionConectMap.put("lastUpdateTime", dateTime);
            JSONObject exceptionlist = JSON.parseObject(exceptionConectMap.get("exceptionlist").toString());
            if (exceptionlist.get(ycid) != null) {
                JSONObject exceptionMap = JSON.parseObject(exceptionlist.get(ycid).toString());
                exceptionMap.put("ex_unreadcnt", Integer.valueOf(exceptionMap.get("ex_unreadcnt").toString()) + 1);
                exceptionMap.put("lastUpdateTime", dateTime);
                exceptionlist.put(ycid, exceptionMap);
                exceptionConectMap.put("exceptionlist", exceptionlist);
            } else {
                Map<String, Object> exceptionMap = new HashMap<>();
                exceptionMap.put("ex_unreadcnt", 1);
                exceptionMap.put("lastUpdateTime", dateTime);
                exceptionlist.put(ycid, exceptionMap);
                exceptionConectMap.put("exceptionlist", exceptionlist);
            }
            // 1.通过keySet
            int num = 0;
            for (String key : exceptionlist.keySet()) {
                if (key.equals(ycid)) {
                    JSONObject exceptionMap = JSON.parseObject(JSONObject.toJSONString(exceptionlist.get(key)));
                    num += Integer.valueOf(exceptionMap.get("ex_unreadcnt").toString());
                } else {
                    JSONObject exceptionMap = JSON.parseObject(exceptionlist.get(key).toString());
                    num += Integer.valueOf(exceptionMap.get("ex_unreadcnt").toString());
                }

            }
            exceptionConectMap.put("unreadcount", num);


            redisUtil.set("EXCEPTION_CONNECT:" + yhid, JSONObject.toJSONString(exceptionConectMap), -1);
        }


    }
}

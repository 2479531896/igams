package com.matridx.igams.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.util.ExceptionSSEUtil;
import com.matridx.igams.common.util.MqType;
import com.matridx.igams.common.util.sseemitter.SseEmitterAllServer;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/sseEmit")
public class SseEmitterController extends BaseController{

    @Autowired
    RedisUtil redisUtil;
    @Autowired(required=false)
    private AmqpTemplate amqpTempl;
    @Value("${matridx.rabbit.ssekey:}")
    private String ssekey ;
    @Autowired
    ExceptionSSEUtil exceptionSSEUtil;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }
	 /**
     * 用于创建连接
     */
	@RequestMapping("/connect/pagedataOA")
    @ResponseBody
    public SseEmitter pagedataOA(String key) {
        Object hget = redisUtil.get( key);
        if (hget!=null){
            Map<String,Object> result = JSON.parseObject((String) hget);
            result.put("ssekey",ssekey);
            redisUtil.del(key);
            redisUtil.set(key,JSON.toJSONString(result),5000);
        }
        return SseEmitterAllServer.connect(key, key.split(":")[0]);
    }

    /**
     * 用于创建连接
     */
    @RequestMapping("/connect/pagedataExceptionRedis")
    @ResponseBody
    public SseEmitter pagedataExceptionRedis(String key, HttpServletRequest request) {
        User user=getLoginInfo(request);
        Object hget = redisUtil.get( key);
        Map<String, Object> result;
        if (hget!=null){
            result = JSON.parseObject((String) hget);
            result.put("ssekey",ssekey);
            redisUtil.del(key);
        }else{
            result = new HashMap<>();
            result.put("ssekey",ssekey);
        }
        redisUtil.set(key,JSON.toJSONString(result),5000);
        return exceptionSSEUtil.connect(user.getYhid(),key);
    }

    /**
     * 关闭连接
     */
    @RequestMapping("/connect/pagedataCloseOARedis")
    @ResponseBody
    public Map<String,Object> pagedataCloseOARedis(String key,HttpServletRequest request) {
        User user=getLoginInfo(request);
        Map<String,Object> map = new HashMap<>();
        Object hget = redisUtil.get( key);
        if (hget!=null){
            Map<String,Object> result = JSON.parseObject((String) hget);
            result.put("key",key);
            result.put("userid",user.getYhid());
            result.put("type","ljgb_exception");
            amqpTempl.convertAndSend("wechat.exchange", MqType.SSE_SENDMSG+result.get("ssekey"), JSONObject.toJSONString(result));
        }
        map.put("status","success");
        return map;
    }
    /**
     * 关闭连接
     */
    @RequestMapping("/connect/pagedataCloseOA")
    @ResponseBody
    public Map<String,Object> pagedataCloseOA(String key) {
        Map<String,Object> map = new HashMap<>();
        Object hget = redisUtil.get( key);
        if (hget!=null){
            Map<String,Object> result = JSON.parseObject((String) hget);
            result.put("key",key);
            result.put("type","ljgb");
            amqpTempl.convertAndSend("wechat.exchange", MqType.SSE_SENDMSG+result.get("ssekey"), JSONObject.toJSONString(result));
        }
        map.put("status","success");
        return map;
    }

}

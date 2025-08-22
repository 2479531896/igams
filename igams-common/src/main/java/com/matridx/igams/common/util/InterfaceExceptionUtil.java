package com.matridx.igams.common.util;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.*;

@Configuration
public class InterfaceExceptionUtil {

    private Logger log = LoggerFactory.getLogger(InterfaceExceptionUtil.class);
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private DingTalkUtil dingTalkUtil;
    @Autowired
    private IDdxxglService ddxxglService;
    @Autowired
    private IXxglService xxglService;

    public void comResetMechanismByThread(Map<String, String> params){
        Thread thread = new Thread() {
            @Override
            public void run() {
                comResetMechanism(params);
            }
        };
        thread.start();
    }

    /**
     * 接口异常重试机制
     * @param params service，method,resetcnt,resettime,key ，其中service，method，key是必填项
     *            service：服务名
     *            method：方法名
     *            resetcnt：重试次数  默认 5次
     *            resettime：重试时间间隔  默认  3,10,30,60,120
     *            resetRemindcnt：重试提醒次数  默认 第最后一次
     *            key：重试次数的redis key的业务标识，可以采用参数的前128位
     */
    public Map<String, String> comResetMechanism(Map<String, String> params){
        Map<String, String> resultMap = new HashMap<>();

        int current_reset_cnt = 0;
        try {
            //前期参数检查
            Map<String, String> t_resultMap = preParamsCheck(params);
            if (t_resultMap != null) {
                return t_resultMap;
            }
            //获取重试间隔时间
            int resetcnt = Integer.parseInt(params.get("resetcnt"));
            List<String> times = StringUtil.splitString(params.get("resettime"));
            int resetRemindcnt = Integer.parseInt(params.get("resetRemindcnt"));
            //确认是否曾经调用过，是否存在错误次数
            Object obj = redisUtil.get("IFRESET:" + params.get("key"));
            // 从redis中获取重试次数
            if (obj != null) {
                Map<String,String> t_params = JSONObject.parseObject((String) obj, Map.class);
                String s_cnt = t_params.get("current_reset_cnt");
                if(StringUtil.isNotBlank(s_cnt))
                    current_reset_cnt = Integer.parseInt(t_params.get("current_reset_cnt"));
            }
            // 更新重试次数
            current_reset_cnt = current_reset_cnt + 1;
            // 重试次数达到最大值，则不再重试
            if (current_reset_cnt > resetcnt) {
                resultMap.put("status", "fail");
                resultMap.put("errorCode", "000099");
                resultMap.put("errorMessage", "重试次数已超过最大值");
                return resultMap;
            }
            //达到提醒次数，则进行提醒，提醒人员从钉钉消息里获取
            if (current_reset_cnt >= resetRemindcnt) {
                String xxnr = xxglService.getMsg("ICOMM_JK00001",params.get("ifuser"),params.get("ifname"),params.get("ifmsg"));
                List<DdxxglDto> ddxxList = ddxxglService.selectByDdxxlx("INTERFACE_EXCEPTION");
                if(ddxxList!=null&&!ddxxList.isEmpty()) {
                    for (DdxxglDto ddxxglDto : ddxxList) {
                        if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
                            dingTalkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), "通知", xxnr);
                        }
                    }
                }
            }

            String servicename = params.get("service");
            String methodname = params.get("method");
            //开启定时任务，定时执行重试
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    log.error("开始执行定时任务" + servicename);
                    Object serviceInstance = ServiceFactory.getService(servicename);//获取方法所在class
                    if(serviceInstance == null){
                        log.error("回调类设置不正确。回调类为：" + servicename);
                        return;
                    }
                    Method method;//获取执行方法
                    try {
                        method = serviceInstance.getClass().getMethod(methodname, Map.class);
                        //执行方法
                        @SuppressWarnings("unchecked")
                        Map<String, Object> map = (Map<String, Object>) method.invoke(serviceInstance, params);

                        log.error("重试机制执行结果为：" + JSONObject.toJSONString(map));
                    } catch (Exception e) {
                        log.error("回调方法执行异常。异常信息为：" + e.getMessage());
                    }
                }
            };

            // 设定任务在几分钟后执行一次
            timer.schedule(task, Integer.parseInt(times.get(current_reset_cnt-1))* 60 * 1000);
            //把当前信息保存在redis里，用于重试时使用,期限为24小时current_reset_cnt
            params.put("current_reset_cnt", String.valueOf(current_reset_cnt));
            redisUtil.set("IFRESET:" + params.get("key"), JSONObject.toJSONString(params),60 * 60 *24);
            resultMap.put("status", "success");
            resultMap.put("errorCode", "0");
        }catch (Exception e){
            params.put("current_reset_cnt", String.valueOf(current_reset_cnt));
            redisUtil.set("IFRESET:" + params.get("key"), JSONObject.toJSONString(params),60 * 60 *24);
            resultMap.put("status", "fail");
            resultMap.put("errorCode", "000001");
            resultMap.put("errorMessage", "重试机制出现异常。异常信息为" + e.getMessage());
            log.error("重试机制出现异常。异常信息为" + e.getMessage());
        }

        return resultMap;
    }

    /**
     * 前期参数检查,对必要参数进行检查，必要参数未传递则返回错误信息
     * @param params
     * @return
     */
    private Map<String, String> preParamsCheck(Map<String, String> params){
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("status", "fail");
        if (StringUtil.isBlank(params.get("service"))){
            resultMap.put("errorCode", "000001");
            resultMap.put("errorMessage", "重试方法信息未传递");
            log.error("重试类的信息未传递。");
            return resultMap;
        }
        if (StringUtil.isBlank(params.get("method"))){
            resultMap.put("errorCode", "000001");
            resultMap.put("errorMessage", "重试方法信息未传递");
            log.error("重试方法的信息未传递。");
            return resultMap;
        }
        if (StringUtil.isBlank(params.get("key"))){
            resultMap.put("errorCode", "000001");
            resultMap.put("errorMessage", "重试业务标识信息未传递");
            log.error("重试业务标识的信息未传递。");
            return resultMap;
        }
        //重试次数
        if (StringUtil.isBlank(params.get("resetcnt"))){
            params.put("resetcnt", "5");
        }
        //重试间隔
        if (StringUtil.isBlank(params.get("resettime"))){
            params.put("resettime", "3,10,30,60,120");
        }
        //重试提醒次数
        if (StringUtil.isBlank(params.get("resetRemindcnt"))){
            params.put("resetRemindcnt", "5");
        }
        //接口用户名称
        if (StringUtil.isBlank(params.get("ifuser"))){
            params.put("ifuser", "");
        }
        //接口名称
        if (StringUtil.isBlank(params.get("ifname"))){
            params.put("ifname", "");
        }
        //接口名称
        if (StringUtil.isBlank(params.get("ifmsg"))){
            params.put("ifmsg", "");
        }
        try {
            int resetcnt = Integer.parseInt(params.get("resetcnt"));
            List<String> times = StringUtil.splitString(params.get("resettime"));
            int resetRemindcnt = Integer.parseInt(params.get("resetRemindcnt"));
            //检测重试时间间隔是否为数字
            for (String time : times){
                if(!StringUtil.isNumeric(time)){
                    resultMap.put("errorCode", "000001");
                    resultMap.put("errorMessage", "重试时间间隔设置不正确" + params.get("resettime"));
                    log.error("重试时间间隔设置不正确,需要为数字，单位为分钟" + params.get("resettime"));
                    return resultMap;
                }
            }
            if(resetcnt > times.size()){
                params.put("resetcnt", String.valueOf(times.size()));
            }
            if(resetRemindcnt > times.size()){
                params.put("resetRemindcnt", String.valueOf(times.size()));
            }
        }catch (Exception e){
            resultMap.put("errorCode", "000001");
            resultMap.put("errorMessage", "重试次数设置不正确" + params.get("resetcnt"));
            log.error("重试次数设置不正确" + params.get("resetcnt"));
            return resultMap;
        }
        return null;
    }
}

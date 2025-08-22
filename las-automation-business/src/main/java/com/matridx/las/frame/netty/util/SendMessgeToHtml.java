package com.matridx.las.frame.netty.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.matridx.las.frame.connect.util.CommonChannelUtil;
import com.matridx.las.frame.netty.enums.*;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SendMessgeToHtml {

    private static final Logger logger = LoggerFactory.getLogger(SendMessgeToHtml.class);
    private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public static void pushMessage(String commd) {
        Runnable newRunnable = new Runnable() {
            public void run() {
                pushMessageUtil(commd);
            }
        };
        cachedThreadPool.execute(newRunnable);
    }

    public static void pushMessageUtil(String commd) {
        JSONObject jsonObject1 = new JSONObject();
        //是不是需要转意
        List<String> list = new ArrayList<>();
        if (StringUtil.isNotBlank(commd)) {
            String[] commds = commd.split(",");
            list = java.util.Arrays.asList(commds);
        }
        if (StringUtil.isBlank(commd) || list.contains(MaterialTypeEnum.MATERIAL_CUBICS.getCode())) {
            

        }
        if (StringUtil.isBlank(commd) || list.contains(MaterialTypeEnum.MATERIAL_AUTO.getCode())) {
            
        }
        if (StringUtil.isBlank(commd) || list.contains(MaterialTypeEnum.MATERIAL_PCR.getCode())) {
            
        }
        if (StringUtil.isBlank(commd) || list.contains(MaterialTypeEnum.MATERIAL_CMH.getCode())) {
            

        }
        if (StringUtil.isBlank(commd) || list.contains(MaterialTypeEnum.MATERIAL_SEQ.getCode())) {
            

        }
        if (StringUtil.isBlank(commd) || list.contains(MaterialTypeEnum.MATERIAL_FMA.getCode())) {
            
        }
        if (StringUtil.isBlank(commd) || list.contains(MaterialTypeEnum.MATERIAL_OCP.getCode())) {
            

        }
        if (StringUtil.isBlank(commd) || list.contains(MaterialTypeEnum.MATERIAL_AGV.getCode())) {
            

        }
        jsonObject1.put("systemState",CommonChannelUtil.getSystemState());
        
        logger.info(JSONObject.toJSONString(jsonObject1, SerializerFeature.WriteMapNullValue));

    }
    /**
     * 推送托盘信息到首页
     */
    public static void putTraySample() {
        JSONObject jsonObject = new JSONObject();
        logger.info(jsonObject.toString());
    }

    public static void procedureErr(String msg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isProcedureErr", "true");
        jsonObject.put("errmsg", msg);
        logger.info(jsonObject.toString());
    }

    /**
     * 区分不同仪器的不同属性
     * @param map
     * @param qyMap

     * @return
     */
    /*private static  JSONArray putProToMe(Map<String,String> map,Map<String,String> qyMap){
        JSONArray sxJ = new JSONArray();
        for(String ky:qyMap.keySet()){
            JSONObject object1 = new JSONObject();
            if(GlobalParmEnum.YQ_PROPERTY_COUNT.getCode().equals(ky)){
                object1.put("sxmc", qyMap.get(ky));
                object1.put("sxz",map.get("count"));
            }
            if(GlobalParmEnum.YQ_PROPERTY_TIME.getCode().equals(ky)){
                object1.put("sxmc", qyMap.get(ky));
                object1.put("sxz",map.get("time"));
            }
            if(GlobalParmEnum.YQ_PROPERTY_CHANNEL.getCode().equals(ky)){
                object1.put("sxmc", qyMap.get(ky));
                object1.put("sxz",map.get("channel"));                        }
            if(!object1.isEmpty()){
                sxJ.add(object1);
            }
        }
        return  sxJ;
    }*/
}

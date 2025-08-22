package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.wechat.service.svcinterface.ISendMessageService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 郭祥杰
 * @date :
 */
@Service
public class SendMessageServiceImpl implements ISendMessageService {
    @Autowired
    private IDdxxglService ddxxglService;
    @Autowired
    private IXxglService xxglService;
    @Autowired
    private DingTalkUtil talkUtil;

    /**
     * @Description: 发送异常消息
     * @param message
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2025/4/27 17:00
     */
    @Override
    public Map<String, Object> sendMessage(String message) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String,Object> map = JSON.parseObject(message,new TypeReference<Map<String, Object>>(){});
        List<DdxxglDto> ddxxgldtolist = ddxxglService.selectByDdxxlx(DingMessageType.DOWNLOADREPORT_ANOMALY_NOTIFICATION.getCode());
        if(map.get("state")!=null && !CollectionUtils.isEmpty(ddxxgldtolist)){
            if("fail".equals(map.get("state").toString())){
                for (DdxxglDto ddxxglDto:ddxxgldtolist){
                    talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(),xxglService.getMsg("ICOMM_ER00002"), StringUtil.replaceMsg(xxglService.getMsg("ICOMM_ER00001"),
                            map.get("ybbh")!=null?map.get("ybbh").toString():"",map.get("zsxm")!=null?map.get("zsxm").toString():"" ,
                            map.get("db")!=null?map.get("db").toString():"" ,map.get("errorNum")!=null?map.get("errorNum").toString():"" , DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                }
            }
            if("success".equals(map.get("state").toString())){
                for (DdxxglDto ddxxglDto:ddxxgldtolist){
                    talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(),xxglService.getMsg("ICOMM_ER00004"),StringUtil.replaceMsg(xxglService.getMsg("ICOMM_ER00003"),
                            map.get("ybbh")!=null?map.get("ybbh").toString():"",map.get("zsxm")!=null?map.get("zsxm").toString():"" ,
                            map.get("db")!=null?map.get("db").toString():"" ,map.get("errorNum")!=null?map.get("errorNum").toString():"" ,DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                }
            }
        }
        resultMap.put("state","success");
        resultMap.put("message","发送成功!");
        return resultMap;
    }
}

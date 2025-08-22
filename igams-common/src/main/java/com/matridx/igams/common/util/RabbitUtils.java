package com.matridx.igams.common.util;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RabbitUtils {
    @Autowired(required=false)
    private AmqpTemplate t_amqpTempl;

    @Autowired
    private IXtszService t_xtszService;
    @PostConstruct
    public void init(){
        xtszService = this.t_xtszService;
        amqpTempl = this.t_amqpTempl;
    }
    private static IXtszService xtszService;
    private static AmqpTemplate amqpTempl;
    /*
     * 发送同步物料消息
     */
    public static void sendSyncWlRabbitMsg(String msg,String routingKey){
        XtszDto xtszDto=xtszService.selectById("rabbit.send.materiel");
        if(xtszDto!=null){
            String szz=xtszDto.getSzz();
            if(StringUtil.isNotBlank(szz)){
                String[] szzArr=szz.split(",");
                for(String szzStr:szzArr){
                    String[] hzArr=szzStr.split(":");
                    amqpTempl.convertAndSend("sys.igams", hzArr[0]+routingKey+ hzArr[1], msg);
                }
            }
        }
    }
    /*
       发送唯一消息，用于防止重复消费
     */
    public static void convertAndSendUniqueMsg(String exchange, String routingKey, String msg){
        //13位时间戳+消息内容
        amqpTempl.convertAndSend(exchange, routingKey, System.currentTimeMillis() +msg);
    }
}

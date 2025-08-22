package com.matridx.igams.web.config.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingtalkrobot_1_0.Client;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTOHeaders;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTORequest;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTOResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.web.dao.entities.WbcxDto;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.service.impl.XtyhServiceImpl;
import com.matridx.igams.web.service.svcinterface.IWbcxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aliyun.teautil.Common.empty;

@Component
public class CommonRabbitMqReceiveTt {
    private final Logger log = LoggerFactory.getLogger(CommonRabbitMqReceiveTt.class);

    @Autowired
    XtyhServiceImpl xtyhService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    DingTalkUtil dingTalkUtil;
    @Autowired
    IXtszService xtszService;
    @Autowired
    IDdxxglService ddxxglService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IWbcxService wbcxService;
    @Value("${matridx.dingtalk.miniappkey:}")
    private String DING_APP_KEY;
    @Value("${matridx.dingtalk.miniappsecret:}")
    private String DING_APP_SECRET;
    @Value("${matridx.dingtalk.miniappid:}")
    private String DING_APP_ID;
    @Value("${matridx.dingtalk.cropid:}")
    private String DING_CRORP_ID;
    @Value("${matridx.dingtalk.agentid:}")
    private String DING_AGENT_ID;
    @Autowired(required=false)
    AmqpTemplate amqpTempl;

    @Value("${matridx.rabbit.preflg:}")
    private String preRabbitFlg;
    @Value("${matridx.rabbit.flg:}")
    private String rabbitFlg;

    @RabbitListener(queues = CommonRabbitMqConfigTt.DELAY_QUEUE, containerFactory="defaultFactory")
    public void receiveMessage(String message){
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) JSON.parse(message);
        String yhid= map.get("yhid");
        XtyhDto xtyhDto = new XtyhDto();
        xtyhDto.setYhid(yhid);
        xtyhDto.setXgry(map.get("xgry"));
        boolean result = xtyhService.updateSfsd(xtyhDto);
        if (result){
            XtyhDto xtyh = xtyhService.getDto(xtyhDto);
        	User user = redisUtil.hugetDto("Users", xtyh.getYhm());
        	if(user != null) {
        		user.setSfsd("0");
                redisUtil.hset("Users",user.getYhm(),JSON.toJSONString(user),-1);
        	}else {
                User redisUser = new User();
                redisUser.setYhm(xtyh.getYhm());
                redisUser.setZsxm(xtyh.getZsxm());
    			redisUser.setSfsd(xtyh.getSfsd());
    			redisUser.setSdtime(xtyh.getSdtime());
    			redisUser.setJstime(xtyh.getJstime());
    			//redisUser.setJsids(xtyh.getJsids());
    			redisUser.setYhid(xtyh.getYhid());
    			redisUser.setDqjs(xtyh.getDqjs());
    			redisUser.setDqjsdm(xtyh.getDqjsdm());
    			redisUser.setDqjsmc(xtyh.getDqjsmc());
    			redisUser.setDdid(xtyh.getDdid());
    			redisUser.setJgid(xtyh.getJgid());
    			redisUser.setMm(xtyh.getMm());
    			redisUser.setMmxgsj(xtyh.getMmxgsj());
                redisUser.setGwmc(xtyh.getGwmc());
                redisUser.setDdtxlj(xtyh.getDdtxlj());
                redisUser.setEmail(xtyh.getEmail());
                redisUser.setUnionid(xtyh.getUnionid());
                redisUser.setWbcxid(xtyh.getWbcxid());
				redisUser.setLoginFlg("0");
                redisUtil.hset("Users",redisUser.getYhm(),JSON.toJSONString(redisUser),-1);
        	}
        }
    }

    /**
     * 发送钉钉消息
     * @param str
     */
    @RabbitListener(queues = ("${matridx.rabbit.preflg:}Send.message.dispose"), containerFactory="defaultFactory")
    public void sendMessageDispose(String str) {
        log.error("sendMessage Rabbit:"+str);
        //获取当前已发数量
        Object numObject = redisUtil.get("maxSendNum");
        Integer num = 10000;
        if (null == numObject){
            XtszDto xtszDto = xtszService.selectById("maxSendNum");
            if (null == xtszDto || StringUtil.isBlank(xtszDto.getSzz())){
                redisUtil.set( "maxSendNum","10000",-1);
            }else{
                redisUtil.set( "maxSendNum",xtszDto.getSzz(),-1);
                num = Integer.parseInt(xtszDto.getSzz());
            }
        }else{
            num = Integer.parseInt(String.valueOf(numObject));
        }
        String date = DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd");
        Object object = redisUtil.get(date + "_CurrentQuantity");
        Integer currentQuantity = 0;
        if (null == object){
            redisUtil.set(date + "_CurrentQuantity",0,86400);
        }else{
            currentQuantity = Integer.parseInt(String.valueOf(object));
        }
        currentQuantity ++;
        if (num.equals(currentQuantity)){
            List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(DingMessageType.SEND_MESSAGE_TYPE.getCode());
            if (!CollectionUtils.isEmpty(ddxxglDtos)){
                String title = xxglService.getMsg("DING_SEND001");
                String DING_SEND002 = xxglService.getMsg("DING_SEND002");
                for (DdxxglDto ddxxglDto : ddxxglDtos) {
                    if(StringUtil.isNotBlank(ddxxglDto.getDdid())&&StringUtil.isNotBlank(ddxxglDto.getYhid())){
                        String msgcontent = StringUtil.replaceMsg(DING_SEND002,String.valueOf(currentQuantity),DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
                        String info = "{\n" +
                                "        'title': '" + title + "',\n" +
                                "        'text': '" + msgcontent + "',\n" +
                                "    }";
                        try {
                            sendMessage(ddxxglDto.getYhid(),ddxxglDto.getDdid(),"sampleMarkdown",info);

                        }catch (Exception e){
                            amqpTempl.convertAndSend("dd_delay_exchange", preRabbitFlg+"dd_delay_key"+rabbitFlg, str, new MessagePostProcessor() {
                                @Override
                                public Message postProcessMessage(Message message) throws AmqpException {
                                    message.getMessageProperties().setExpiration("5000");
                                    return message;
                                }
                            });
                        }
                    }
                }
            }
        }else if(num > currentQuantity){
            Map<String, Object> map = (Map<String, Object>) JSON.parse(str); 
            if(map.get("yhm") == null || "".equals(String.valueOf(map.get("yhm")))) {
            	log.error("sendMessage Rabbit error: no yhm");
            	return;
            }
            String userId = map.get("userId")==null?"":map.get("userId").toString();
            String yhm = map.get("yhm")==null?"":map.get("yhm").toString();
            String msgKey = map.get("msgKey")==null?"":map.get("msgKey").toString();
            String msgParam = map.get("msgParam")==null?"":map.get("msgParam").toString();
            try {
                sendMessage(yhm,userId,msgKey,msgParam);

            }catch (Exception e){
                amqpTempl.convertAndSend("dd_delay_exchange", preRabbitFlg+"dd_delay_key"+rabbitFlg, str, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        message.getMessageProperties().setExpiration("10000");
                        return message;
                    }
                });
            }
        }
        redisUtil.set(date + "_CurrentQuantity",currentQuantity,86400);

    }

    public void sendMessage(String yhm,String userId,String msgKey,String msgParam) throws Exception {
        BatchSendOTOHeaders batchSendOTOHeaders = new BatchSendOTOHeaders();
        BatchSendOTORequest batchSendOTORequest = new BatchSendOTORequest();
        User user = redisUtil.hugetDto("Users", yhm);
        Client client = null;
        try {
            log.error("sendWorkMessage Robot+UserInfo: "+yhm+"----"+userId +"----"+msgKey+"----"+msgParam );
            DBEncrypt db = new DBEncrypt();
            client = dingTalkUtil.createClient();
            if (client == null) {
                log.error("client 异常，未获取到客户端!");
            }
            String accessToken = null;
            String robotName = null;//数据库appname=>appname
            String robotAppkey = null;//数据库appid=>appkey
            String robotAppsecret = null;//数据库appsecret=>appsecret
            String robotCorpid = null;//数据库corpid=>企业corpid
            String robotAgentid = null;//数据库agentid=>企业agentid
            String robotAppId = null;//数据库miniappid=>小程序appid
            if (user == null){
                user = xtyhService.getYhInfo(yhm);
            }
            if(user == null || StringUtil.isBlank(user.getDdid()) ||"1".equals(user.getSfsd())||"1".equals(user.getScbj())) {
            	log.error("因没有用户相应的ddid或者用户已经锁定，此消息暂不发送! " + user.getYhid() + " sd=" + user.getSfsd() + " sc=" + user.getScbj());
            	return;
            }
            Map<String,String> robotDingtalk = new HashMap<>();
            if (StringUtil.isNotBlank(user.getWbcxid())){
                if ("1".equals(user.getSfsd())||"1".equals(user.getScbj())){
                    log.error(user.getYhm()+"已锁定或删除不进行操作,消息内容为："+msgParam);
                    return;
                }
                //若用户存在且用户的外部程序id存在，则获取外部程序的信息
                Object wbcxInfo = redisUtil.hget("robotDingtalk", user.getWbcxid());
                if (wbcxInfo!=null){
                    //若外部程序信息存在，则获取外部程序信息
                    //将外部程序信息转换为map
                    robotDingtalk = (Map<String, String>)wbcxInfo;
                    accessToken = robotDingtalk.get("accessToken");
                    robotAppkey = robotDingtalk.get("robotAppkey");
                    robotAppsecret = robotDingtalk.get("robotAppsecret");
                    robotCorpid = robotDingtalk.get("robotCorpid");
                    robotAgentid = robotDingtalk.get("robotAgentid");
                    robotAppId = robotDingtalk.get("robotAppid");
                    robotName = robotDingtalk.get("robotName");

                }
                if (StringUtil.isAnyBlank(accessToken,robotAppkey,robotAppsecret,robotCorpid,robotAgentid,robotAppId)){
                    //若外部程序信息中有为空的，则从数据库重新set全部
                	WbcxDto tWbcxDto = new WbcxDto();
                	tWbcxDto.setLx("DINGDING");
                    log.error("CommonRabbitMqReceiveTt sendMessage Error--lwj--");
                    List<WbcxDto> wbcxDtoList=wbcxService.getDtoList(tWbcxDto);
                    if(!CollectionUtils.isEmpty(wbcxDtoList)){
                        for(WbcxDto wbcxDto:wbcxDtoList){
                            accessToken = dingTalkUtil.getToken(wbcxDto.getAppid(),wbcxDto.getSecret());
                            robotDingtalk.put("accessToken",accessToken);
                            robotDingtalk.put("robotAppkey",wbcxDto.getAppid());
                            robotDingtalk.put("robotAppsecret",wbcxDto.getSecret());
                            robotDingtalk.put("robotCorpid",wbcxDto.getCropid());
                            robotDingtalk.put("robotAgentid",wbcxDto.getAgentid());
                            robotDingtalk.put("robotAppid",wbcxDto.getMiniappid());
                            robotDingtalk.put("robotName",wbcxDto.getWbcxmc());
                            redisUtil.hset("robotDingtalk",wbcxDto.getWbcxid(),robotDingtalk,5400L);
                        }
                    }
                    //因为重新获取token，所以变量需要重新设置
                    wbcxInfo = redisUtil.hget("robotDingtalk", user.getWbcxid());
                    if (wbcxInfo!=null){
                        //若外部程序信息存在，则获取外部程序信息
                        //将外部程序信息转换为map
                        robotDingtalk = (Map<String, String>)wbcxInfo;
                        accessToken = robotDingtalk.get("accessToken");
                        robotAppkey = robotDingtalk.get("robotAppkey");
                        robotAppsecret = robotDingtalk.get("robotAppsecret");
                        robotCorpid = robotDingtalk.get("robotCorpid");
                        robotAgentid = robotDingtalk.get("robotAgentid");
                        robotAppId = robotDingtalk.get("robotAppid");
                        robotName = robotDingtalk.get("robotName");

                    }
                }
                
            }else{
                robotAppkey = DING_APP_KEY;
                robotAppsecret = DING_APP_SECRET;
                robotCorpid = DING_CRORP_ID;
                robotAgentid = DING_AGENT_ID;
                robotAppId = DING_APP_ID;
                accessToken = dingTalkUtil.getToken(robotAppkey,robotAppsecret);
            }
            try {
            	log.error("sendWorkMessage Robot+RobotInfo: " + (StringUtil.isNotBlank(robotName)?robotName:"系统默认名称") + "-" + db.dCode(robotAppkey) + "-" + db.dCode(robotAppsecret) + "-" + db.dCode(robotCorpid) + "-" + db.dCode(robotAgentid) + "-" + db.dCode(robotAppId));
            }catch(Exception e) {
            	log.error("输出发送的消息时出错,消息内容为："+e.getMessage());
            }
            log.error("accessToken:"+accessToken);
            JSONObject jsonObject = JSONObject.parseObject(msgParam);
            if (jsonObject!=null && jsonObject.getString("actionURL") !=null){
                String actionURL = jsonObject.getString("actionURL");
                int i = actionURL.indexOf("page=/");
                if (i>-1){
                    if (i > 0){
                        actionURL = actionURL.substring(i);
                    }
                    String actionPreURL = "dingtalk://dingtalkclient/action/open_micro_app?miniAppId=«miniAppId»&agentId=«agentId»&pVersion=1&packageType=1&corpId=«corpId»&";
                    actionPreURL = actionPreURL.replace("«miniAppId»",db.dCode(robotAppId)).replace("«agentId»",db.dCode(robotAgentid)).replace("«corpId»",db.dCode(robotCorpid));
                    actionURL = actionPreURL + actionURL;
                    log.error("actionURL====="+actionURL);
                    jsonObject.put("actionURL",actionURL);
                    msgParam = jsonObject.toJSONString();
                }
            }
            batchSendOTOHeaders.xAcsDingtalkAccessToken = accessToken;
            batchSendOTORequest.setRobotCode(db.dCode(robotAppkey))
                    .setUserIds(Collections.singletonList(StringUtil.isNotBlank(user.getDdid()) ? user.getDdid() : userId))
                    .setMsgKey(msgKey)
                    .setMsgParam(msgParam);
            BatchSendOTOResponse batchSendOTOResponse = client.batchSendOTOWithOptions(batchSendOTORequest, batchSendOTOHeaders, new RuntimeOptions());
            log.error("sendWorkMessage Robot+Body:" + JSON.toJSONString(batchSendOTOResponse.getBody()) + "  userid:" + (StringUtil.isNotBlank(user.getDdid())?user.getDdid():userId));
        } catch (TeaException err) {
            log.error(err.getMessage());
            if (!empty(err.code) && !empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.error("sendWorkMessage Robot+code3:" + err.code);
                log.error("sendWorkMessage Robot+message3:" + err.message);
                if("Forbidden.AccessDenied.QpsLimitForApi".equals(err.code)) {
                    throw new Exception("钉钉峰值过高错峰延迟队列发送");
//					try {
//						Thread.sleep(2000);
//						BatchSendOTOResponse batchSendOTOResponse = client.batchSendOTOWithOptions(batchSendOTORequest, batchSendOTOHeaders, new RuntimeOptions());
//	                    log.error("sendWorkMessage_re Robot+Body:" + JSON.toJSONString(batchSendOTOResponse.getBody()) + "  userid:" + (StringUtil.isNotBlank(user.getDdid())?user.getDdid():userId));
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						log.error("sendWorkMessage_re error:" + e.getMessage());
//                        throw new Exception("第二次发送定的消息错误");
//
//					}
                }
            }
        } catch (Exception _err) {
            log.error(_err.getMessage());
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!empty(err.code) && !empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.error("sendWorkMessage Robot+code4:" + err.code);
                log.error("sendWorkMessage Robot+message4:" + err.message);
            }
        }
    }
}

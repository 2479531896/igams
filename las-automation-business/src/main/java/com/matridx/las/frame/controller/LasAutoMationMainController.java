package com.matridx.las.frame.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.las.frame.connect.channel.command.FrameModel;
import com.matridx.las.frame.connect.channel.service.NettyChannelService;
import com.matridx.las.frame.connect.svcinterface.IConnectService;
import com.matridx.las.frame.connect.svcinterface.IHttpService;
import com.matridx.las.frame.connect.util.CommonChannelUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
/*
    主服务器接收分服务器同步信息请求
 */
@Controller
@RequestMapping("/autows")
public class LasAutoMationMainController{
    Logger log = LoggerFactory.getLogger(LasAutoMationMainController.class);
    //分服务器上线信息同步到主服务器
    @RequestMapping("/mainChannelActive")
    @ResponseBody
    public void mainChannelActive(@RequestBody Map<String,String> map){
        String netty_area = map.get("netty_area");
        String nettyName = map.get("netty_name");
        String clientIp = map.get("clientIp");

        log.info("mainChannelActive--nettyName：{} netty_area：{} clientIp：{}",nettyName,netty_area,clientIp);
        if(StringUtil.isNotBlank(netty_area)){
            boolean result =  CommonChannelUtil.addChannel(netty_area,clientIp,new NettyChannelService());
            if(result) {
                log.info("mainChannelActive--同步成功！");
            }else{
                log.error("mainChannelActive--同步失败！");
            }
        }
    }
    /**
     * 将分服务器的设备同步到主服务器
     */
    @RequestMapping("/mainRegisterChannels")
    @ResponseBody
    public void mainRegisterChannels(@RequestBody Map<String,String> map){
        String channelServiceStr = map.get("channelservice");
        log.info("mainChannelActive--channelserviceStr：{}",channelServiceStr);
        if(StringUtil.isNotBlank(channelServiceStr)){
        	JSONObject jsobject = JSON.parseObject(channelServiceStr);
            IHttpService service = CommonChannelUtil.parseJsonToGetIHttpservice(jsobject);
            CommonChannelUtil.syncMainChannels(service);
        }
    }
    
    /**
     * 分服务器更改状态后同步更改主服务器
     */
    @RequestMapping("/updateMainChannelStatus")
    @ResponseBody
    public void updateMainChannelStatus(@RequestBody Map<String,String> map){
        String frameModelStr = map.get("frameModel");
        String Status = map.get("Status");
        log.info("updateMainChannelStatus--sendModelStr：{} Status:{}",frameModelStr,Status);
        if(StringUtil.isNotBlank(frameModelStr)&&StringUtil.isNotBlank(Status)){
            FrameModel frameModel = JSON.parseObject(frameModelStr, FrameModel.class);
            IConnectService channelService = CommonChannelUtil.getChannelByFrame(frameModel);
            if (channelService != null) {
            	channelService.getChannelModel().setStatus(Status);
            }else {
                log.error("updateMainChannelStatus--已注册的channel为空！");
            }

        }
    }
    //将分服务器的信息从主服务器去除
    @RequestMapping("/mainChannelInactive")
    @ResponseBody
    public void mainChannelInactive(@RequestBody Map<String,String> map){
        String nettyArea = map.get("netty_area");
        String clientIp = map.get("clientIp");
        String protocol = map.get("protocol");
        String commanddeviceid = map.get("commanddeviceid");
        log.info("mainChannelInactive--netty_area：{},clientIp：{}",nettyArea,clientIp);
        if(StringUtil.isNotBlank(nettyArea)){
            boolean result =  CommonChannelUtil.removeMainChannel(nettyArea,clientIp,protocol,commanddeviceid);
            if(result) {
                log.info("mainChannelInactive--同步成功！");
            }else{
                log.error("mainChannelInactive--同步失败！");
            }
        }
    }
    //定时任务将分服务器的所有信息同步至主服务器
    @RequestMapping("/syncAllChannelToMain")
    @ResponseBody
    public void syncAllChannelToMain(@RequestBody Map<String,String> map){
        String nettyArea = map.get("netty_area");
        String areaMapStr = map.get("areaMap");
        log.info("syncAllChannelToMain--nettyArea: {} areaMap：{}",nettyArea,areaMapStr);
        if(StringUtil.isNotBlank(nettyArea)&&StringUtil.isNotBlank(areaMapStr)){
            @SuppressWarnings("unchecked")
            Map<String, List<IHttpService>> areaMap = JSON.parseObject(areaMapStr, Map.class);
            CommonChannelUtil.syncAllChannelToMain(areaMap,nettyArea);
        }
    }

}

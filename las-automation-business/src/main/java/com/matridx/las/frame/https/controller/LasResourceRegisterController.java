package com.matridx.las.frame.https.controller;

import com.matridx.las.frame.connect.channel.command.BaseCommand;
import com.matridx.las.frame.connect.svcinterface.IConnectService;
import com.matridx.las.frame.connect.svcinterface.IHttpService;
import com.matridx.las.frame.connect.util.ChannelModel;
import com.matridx.las.frame.connect.util.CommonChannelUtil;
import com.matridx.las.frame.connect.util.ConnectUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 设备主动通过http请求访问注册
 */
@Controller
@RequestMapping("/autows")
public class LasResourceRegisterController {

    @Value("${matridx.netty.area:01}")
    private String netty_area;
    @Value("${matridx.netty.bridgingflg:false}")
    private boolean bridgingflg;
    @Value("${matridx.netty.name:}")
    private String netty_name;
    @Autowired
    BaseCommand baseCommand;

    private Logger log = LoggerFactory.getLogger(LasResourceRegisterController.class);

    @RequestMapping("/register")
    @ResponseBody
    public Map<String, Object> resourceRegister(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        try {
            String address = request.getParameter("address");//ip+port
            String deviceid = request.getParameter("deviceid");//设备ID
            String protocol = request.getParameter("protocol");//协议
            if(StringUtil.isBlank(deviceid))
            	deviceid = "10";
            //规范设备ID规则
            deviceid = StringUtil.leftPad(deviceid, 5, '0');
            map.put("address", address);
            //状态确认地址
            map.put("ztqrdz", request.getParameter("ztqrdz"));
            //是否由服务器确认设备情况
            map.put("confirmflg", request.getParameter("confirmflg"));
            //区域
            map.put("netty_area", netty_area);
            //是否桥接
            map.put("bridgingflg", bridgingflg);
            //获取对应协议的Service
            ChannelModel channelModel = new ChannelModel();
            channelModel.setProtocol(protocol);
            IHttpService service = CommonChannelUtil.getServiceByChannelModel(channelModel);
            //是否桥接
            map.put("protocol", protocol);
            map.put("deviceid", deviceid);
            service.init(map);//初始化service
            
            CommonChannelUtil.handleChannel(address, service);
            if(!CommonChannelUtil.protocols.containsKey(protocol)) {
            	CommonChannelUtil.addProtocol(protocol, service);
            }
            //测试http 信息联通
            //String[] params = new String[1];
            //params[0] = "0";
            //baseCommand.sendByCmdModel("3", "OpenOrCloseDrawer", params);
            //params[0] = "{\"code\":\"Q01D23500001B\",\"word\":\"YB7833XS\",\"project\":\"D\",\"qrcode\":\"Q01D23500001B\"}";
            //baseCommand.sendByCmdModel("5", "print", params);
            
            map.put("status","success");
            map.put("msg", "注册成功!");
        }catch(Exception e){
            map.put("status","fail");
            map.put("msg","注册失败! 异常:"+e.getMessage());
        }
        return map;
    }
    /*
        注销
     */
    @RequestMapping("/logOff")
    @ResponseBody
    public Map<String, Object> logOff(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        try {
            String address = request.getParameter("address");
            String deviceid = request.getParameter("deviceid");
            String protocol = request.getParameter("protocol");
            IConnectService channelService = CommonChannelUtil.removeChannel(netty_area, protocol + deviceid, address);
            if (channelService!=null){
                map.put("status","success");
                map.put("msg", "注销成功!");
                //如果桥接，则发送信息到主服务器上
                if(bridgingflg) {
                    Map<String, String> paramMap= new HashMap<>();
                    paramMap.put("netty_name", StringUtil.isNotBlank(netty_name)?netty_name:netty_area);
                    paramMap.put("netty_area", netty_area);
                    paramMap.put("clientIp", address);
                    paramMap.put("commanddeviceid", channelService.getChannelModel().getCommanddeviceid());
                    paramMap.put("protocol", channelService.getChannelModel().getProtocol());
                    //发送信息到主服务器
                    ConnectUtil.mainChannelInactive(paramMap);
                }
            }else {
                map.put("status","fail");
                map.put("msg", "未找到对应设备!");
            }
        }catch(Exception e){
            map.put("status","fail");
            map.put("msg",e.getMessage());
        }
        return map;
    }

    /**
     * 前端获取命令并在service进行命令解析转换成http请求，请求相应仪器接口执行操作
     * @param req
     * @return
     */
    @RequestMapping("/execute")
    @ResponseBody
    public Map<String,String> executeCommand(HttpServletRequest req){
        Map<String,String> map=new HashMap<>();
        try {
//            String jsonString = "{\"code\":\"A01D2023011B\",\"sign\":\"1\",\"num\":\"1\",\"flg\":\"2\",\"qrcode\":\"A01D2023011B\",\"project\":\"YB01\",\"rownum\":\"3\"}";  ;
            String jsonString = req.getParameter("jsonString");
            log.error("jsonString:" + jsonString);
            String[] param=new String[1];
            param[0] = jsonString;
            baseCommand.sendByCmdModel("7","Print", param);
        }catch(Exception e){
            log.error("参数格式错误!");
        }
        return map;
    }
}

package com.matridx.las.home.controller;

import com.matridx.las.netty.enums.GlobalParmEnum;
import com.matridx.las.netty.util.SendMessgeToHtml;
import com.matridx.las.netty.util.sseemitter.SseEmitterAllServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.matridx.igams.common.controller.BaseController;

@Controller
@RequestMapping("/sseEmit")
public class SseEmitterController extends BaseController{
	 /**
     * 用于创建连接
     */
	@RequestMapping("/connect/input/{userId}")
    @ResponseBody
    public SseEmitter connectInput(@PathVariable String userId) {
        SseEmitter sseEmitter = SseEmitterAllServer.connect(userId,GlobalParmEnum.HTML_TYPE_INPUT.getCode());
        SendMessgeToHtml.putTraySample();
	    return sseEmitter;
    }
    /**
     * 创建首页连接
     */
    @RequestMapping("/connect/all/{userId}")
    @ResponseBody
    public SseEmitter connectAll(@PathVariable String userId) {
        SseEmitter sseEmitter = SseEmitterAllServer.connect(userId, GlobalParmEnum.HTML_TYPE_ALL.getCode());
        SendMessgeToHtml.pushMessage(null);
        return sseEmitter;
    }

    /**
     * 创建设通道高置界面连接
     */
    @RequestMapping("/connect/Chn/{userId}")
    @ResponseBody
    public SseEmitter connectChn(@PathVariable String userId) {
        SseEmitter sseEmitter = SseEmitterAllServer.connect(userId,GlobalParmEnum.HTML_TYPE_CHN.getCode());
        //SendMessgeToHtml.pushMessage(null);

        return sseEmitter;
    }

    @RequestMapping("/connect/close/{userId}")
    @ResponseBody
    public String connectClose(@PathVariable String userId) {
        SseEmitterAllServer.removeUser(userId,GlobalParmEnum.HTML_TYPE_ALL.getCode());
        return "";
    }






}

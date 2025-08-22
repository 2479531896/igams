package com.matridx.las.home.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.las.home.dao.entities.SysszDto;
import com.matridx.las.home.dao.entities.YqxxDto;
import com.matridx.las.home.service.svcinterface.*;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.FrameModelToHtml;
import com.matridx.las.netty.dao.entities.FrontMaterialModel;
import com.matridx.las.netty.dao.entities.JkywzszDto;
import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.enums.MedicalRobotProcessEnum;
import com.matridx.las.netty.enums.RobotStatesEnum;
import com.matridx.las.netty.global.CubsParmGlobal;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.service.svcinterface.IJkywzszService;
import com.matridx.las.netty.service.svcinterface.IYqztxxService;
import com.matridx.las.netty.service.svcinterface.IYsybxxService;
import com.matridx.las.netty.service.svcinterface.StopAndTimeoutService;
import com.matridx.las.netty.util.SendMessgeToHtml;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/globalprocess")
public class GlobalprocessController extends BaseController {
    @Autowired
    private IGlobalprocessService globalprocessService;

    /**
     * 查询所有的系统的流程运行过程中相关的数据
     */
    @RequestMapping("/process/processIndex")
    @ResponseBody
    public Map<String, Object> processIndex(HttpServletRequest request) {
        return globalprocessService.showGlobalprocessDate();
    }

    /**
     * 保存InstrumentUsedList相关信息
     */
    @RequestMapping("/process/saveInstrumentUsedList")
    @ResponseBody
    public Map<String, Object> saveInstrumentUsedList(@RequestBody List<Map<String,String>> list) {
        System.out.println(list);
        return globalprocessService.saveInstrumentUsedList(list);
    }

    /**
     * 保存空闲队列相关信息
     */
    @RequestMapping("/process/saveFreeQueue")
    @ResponseBody
    public Map<String, Object> saveQueueList(@RequestBody List<Map<String,String>> list) {
        return globalprocessService.saveFreeQueue(list);
    }

    /**
     * 发送消息
     */
    @RequestMapping("/process/sendCmd")
    @ResponseBody
    public Map<String, Object> sendCmd(String id, String threadName) {
        return globalprocessService.sendMessageBythreadname(id, threadName);
    }

    /**
     * 删除此消息
     */
    @RequestMapping("/process/removeCmd")
    @ResponseBody
    public Map<String, Object> removeCmd(String id, String threadName, String mlid, HttpServletRequest request) {
        return globalprocessService.removeCmd(id, threadName, mlid);
    }


    /**
     * 查询所有的系统的流程运行过程中相关的数据
     */
    @RequestMapping("/process/modQueueIndex")
    @ResponseBody
    public Map<String, Object> modQueueIndex(String id, String threadName, String type, HttpServletRequest request) {

        return globalprocessService.modQueueIndex(id, threadName, type);
    }

    /**
     * 修改此命令队列
     */
    @RequestMapping("/process/modCmdDic")
    @ResponseBody
    public Map<String, Object> modCmdDic(@RequestBody FrameModelToHtml frameModelToHtml, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map = globalprocessService.modCmdDic(frameModelToHtml);
        return map;
    }

    /**
     * 新增此命令队列
     */
    @RequestMapping("/process/addCmdDic")
    @ResponseBody
    public Map<String, Object> addCmdDic(@RequestBody FrameModelToHtml frameModelToHtml) {
        Map<String, Object> map = new HashMap<>();
        frameModelToHtml.setCommanddeviceid(frameModelToHtml.getCommand()+frameModelToHtml.getDeviceID());
        map = globalprocessService.addCmdDic(frameModelToHtml);
        return map;
    }

    /**
     * 删除事件队列
     */
    @RequestMapping("/process/removeEventDic")
    @ResponseBody
    public Map<String, Object> removeEventDic(String id, String threadName, String lcid) {

        return globalprocessService.removeEventDic(id, threadName, lcid);
    }

    /**
     * 修改此事件队列
     */
    @RequestMapping("/process/modEventDic")
    @ResponseBody
    public Map<String, Object> modEventDic(@RequestBody SjlcDto sjlcDto) {
        String id = sjlcDto.getId();
        String threadName = sjlcDto.getThreadName();
        return globalprocessService.modEventDic(id, threadName, sjlcDto);
    }
    /**
     * 修改此事件队列
     */
    @RequestMapping("/process/addEventDic")
    @ResponseBody
    public Map<String, Object> addEventDic(@RequestBody SjlcDto sjlcDto) {
        String id = sjlcDto.getId();
        String threadName = sjlcDto.getThreadName();
        return globalprocessService.addEventDic(id, threadName, sjlcDto);
    }
    /**
     * 重新发送事件
     */
    @RequestMapping("/process/sendEventDic")
    @ResponseBody
    public Map<String, Object> sendEventDic(String id, String threadName,String command,String deviceid   ) {
        return globalprocessService.sendEventDicBythreadname(id, threadName,command,deviceid);
    }
}

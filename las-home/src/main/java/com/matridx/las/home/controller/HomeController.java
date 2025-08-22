package com.matridx.las.home.controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.las.home.dao.entities.YqxxInfoDto;
import com.matridx.las.home.service.svcinterface.*;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.enums.*;
import com.matridx.las.netty.global.CubsParmGlobal;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.material.AutoDesktopGlobal;
import com.matridx.las.netty.global.material.FrontMaterialAreaGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.service.svcinterface.*;
import com.matridx.las.netty.util.CommonChannelUtil;
import com.matridx.las.netty.util.RedisSetAndGetUtil;
import com.matridx.las.netty.util.SendMessgeToHtml;
//import com.sf.csim.express.service.HttpClientUtil;
import com.matridx.las.netty.util.snapshot.SnapShotManagementGlobal;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.las.home.dao.entities.SysszDto;
import com.matridx.las.home.dao.entities.YqxxDto;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/lashome")
public class HomeController extends BaseController {

    @Autowired
    ISysszService sysszService;

    @Autowired
    IYqxxService yqxxService;

    @Autowired
    IXxglService xxglService;

    @Autowired
    IJcsjService jcsjService;

    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    private IYqxxInfoService yqxxInfoService;
    @Autowired
    private IJkywzszService jkywzszService;
    @Autowired
    private MaterialScienceInitService materialScienceInitService;

    @Autowired
    ICommonService commonService;
    @Autowired
    private IYqztxxService yqztxxService;
    @Autowired
    private IYsybxxService ysybxxService;
    @Autowired
    private StopAndTimeoutService stopAndTimeoutService;
    @Value("${matridx.sysid:}")
    private String sysid;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisSetAndGetUtil redisSetAndGetUtil;
    @Autowired
	private IDdxxglService DdxxglService;
    @Autowired
    private  RedisSetAndGetUtil setAndGetUtil;
    @Autowired
    private SnapShotManagementGlobal snapShotManagementGlobal;
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * 实验室主页面
     *
     * @return
     */
    @RequestMapping(value = "/home/homePage")
    public ModelAndView homePage() {
        ModelAndView mav = new ModelAndView("lashome/home/home_page");
        materialScienceInitService.initMaterial();
        SysszDto sysszDto = sysszService.isExistLab();
        // 查询仪器信息
        List<YqxxDto> yqxxDtos = yqxxService.getListByLab(sysszDto);
        mav.addObject("yqxxDtos", yqxxDtos);
        mav.addObject("sysszDto", sysszDto);
        mav.addObject("log", "websocket测试！");
        return mav;
    }


    @RequestMapping(value = "/home/websocketTest")
    public ModelAndView websocketTest() {
        ModelAndView mav = new ModelAndView("lashome/choice/choice_las");
        mav.addObject("log", "实验室选择页面");
        return mav;
    }

    /**
     * 实验室设置页面
     *
     * @return
     */
    @RequestMapping(value = "/home/homeSet")
    public ModelAndView homeSet() {
        ModelAndView mav = new ModelAndView("lashome/home/home_set");
        // 若实验室不存在则创建
        SysszDto sysszDto = sysszService.isExistLab();
        // 查询仪器信息
        List<YqxxDto> yqxxDtos = yqxxService.getListByLab(sysszDto);
        mav.addObject("yqxxDtos", yqxxDtos);
        mav.addObject("sysszDto", sysszDto);
        return mav;
    }

    /**
     * 获取已使用的仪器信息
     *
     * @param yqxxDto
     * @return
     */
    @RequestMapping(value = "/home/getDeviceList")
    @ResponseBody
    public Map<String, Object> getDeviceList(YqxxDto yqxxDto) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<YqxxDto> yqxxDtos = yqxxService.getDtoList(yqxxDto);
        map.put("status", (yqxxDtos != null && yqxxDtos.size() > 0) ? "success" : "fail");
        map.put("yqxxDtos", yqxxDtos);
        return map;
    }

    /**
     * 实验室信息修改页面
     *
     * @return
     */
    @RequestMapping(value = "/home/labConfigView")
    public ModelAndView labConfigView(SysszDto sysszDto) {
        ModelAndView mav = new ModelAndView("lashome/home/lab_config");
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECTION_UNIT});
        // 检测单位
        mav.addObject("unitlist", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));
        // 查询实验室信息
        sysszDto = sysszService.isExistLab();
        sysszDto.setYwlx(BusTypeEnum.IMP_LABORATORY_BG.getCode());
        // 查询附件
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_LABORATORY_BG.getCode());
        fjcfbDto.setYwid(sysszDto.getSysid());
        List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("fjcfbDtos", t_fjcfbDtos);
        mav.addObject("sysszDto", sysszDto);
        return mav;
    }

    /**
     * 实验室信息修改保存
     *
     * @param sysszDto
     * @return
     */
    @RequestMapping(value = "/home/labConfig")
    @ResponseBody
    public Map<String, Object> labConfig(SysszDto sysszDto) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean isSuccess = sysszService.labConfig(sysszDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 仪器设备新增页面
     *
     * @return
     */
    @RequestMapping(value = "/home/addDeviceView")
    public ModelAndView addDeviceView(YqxxDto yqxxDto) {
        ModelAndView mav = new ModelAndView("lashome/home/device_edit");
        yqxxDto.setFormAction("add");
        yqxxDto.setYwlx(BusTypeEnum.IMP_DEVICE_BG.getCode());
        yqxxDto.setSybj("0");
        mav.addObject("yqxxDto", yqxxDto);
        return mav;
    }

    /**
     * 仪器设备新增保存
     *
     * @param yqxxDto
     * @return
     */
    @RequestMapping(value = "/home/addSaveDevice")
    @ResponseBody
    public Map<String, Object> addSaveDevice(YqxxDto yqxxDto) {
        Map<String, Object> map = new HashMap<String, Object>();
        //获取用户信息
        User user = getLoginInfo();
        yqxxDto.setLrry(user.getYhid());
        boolean isSuccess = yqxxService.addSaveDevice(yqxxDto);
        map.put("yqxxDto", yqxxDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 仪器设备编辑页面
     *
     * @return
     */
    @RequestMapping(value = "/home/modDeviceView")
    public ModelAndView modDeviceView(YqxxDto yqxxDto) {
        ModelAndView mav = new ModelAndView("lashome/home/device_edit");
        YqxxDto t_yqxxDto = yqxxService.getDtoById(yqxxDto.getYqid());
        t_yqxxDto.setFormAction("mod");
        t_yqxxDto.setYwlx(BusTypeEnum.IMP_DEVICE_BG.getCode());
        // 查询附件
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_DEVICE_BG.getCode());
        fjcfbDto.setYwid(t_yqxxDto.getYqid());
        List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("fjcfbDtos", t_fjcfbDtos);
        mav.addObject("yqxxDto", t_yqxxDto);
        return mav;
    }

    /**
     * 仪器设备编辑保存
     *
     * @param yqxxDto
     * @return
     */
    @RequestMapping(value = "/home/modSaveDevice")
    @ResponseBody
    public Map<String, Object> modSaveDevice(YqxxDto yqxxDto) {
        Map<String, Object> map = new HashMap<String, Object>();
        //获取用户信息
        User user = getLoginInfo();
        yqxxDto.setXgry(user.getYhid());
        boolean isSuccess = yqxxService.modSaveDevice(yqxxDto);
        map.put("yqxxDto", yqxxDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 删除仪器信息
     *
     * @param yqxxDto
     * @return
     */
    @RequestMapping(value = "/home/delDevice")
    @ResponseBody
    public Map<String, Object> delDevice(YqxxDto yqxxDto) {
        Map<String, Object> map = new HashMap<String, Object>();
        //获取用户信息
        User user = getLoginInfo();
        yqxxDto.setScry(user.getYhid());
        yqxxDto.setScbj("1");
        boolean isSuccess = yqxxService.delete(yqxxDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 保存仪器设置信息
     *
     * @param yqxxDto
     * @return
     */
    @RequestMapping(value = "/home/updateDeviceInfo")
    @ResponseBody
    public Map<String, Object> updateDeviceInfo(YqxxDto yqxxDto) {
        Map<String, Object> map = new HashMap<String, Object>();
        //获取用户信息
        User user = getLoginInfo();
        yqxxDto.setXgry(user.getYhid());
        boolean isSuccess = yqxxService.update(yqxxDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    @RequestMapping(value = "/home/testNettyMsg")
    @ResponseBody
    public Map<String, Object> testNettyMsg(String msg, String type, String access_token, String ref) {
        Map<String, Object> map = commonService.testMessage(type);
        return map;
    }

    @RequestMapping(value = "/home/closeNettyMessage")
    @ResponseBody
    public Map<String, Object> closeNettyMessage(String msg, String type) {
        Map<String, Object> map = commonService.closeNettyMessage(type);
        return map;
    }



    /**
     * 仪器线上状态页面
     *
     * @return
     */
    @RequestMapping(value = "/home/showYqZtView")
    @ResponseBody
    public void showYqZtView() {
        //推送到前端，所有信息
        SendMessgeToHtml.pushMessage(null);
    }

    /**
     * 暂停 04， 继续 05，停止 06
     * type
     * @return
     */
    @RequestMapping(value = "/home/pauseCmd")
    @ResponseBody
    public Map<String, Object> pause(String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "success");
        map.put("message", "操作成功");
        try {
            if("04".equals(type)) {
                if (!GlobalrouteEnum.SYSTEM_STATE_SUSPEND.getCode().equals(CommonChannelUtil.getSystemState())) {
                    CommonChannelUtil.setSystemState(GlobalrouteEnum.SYSTEM_STATE_SUSPEND.getCode());
                    SendMessgeToHtml.pushMessage(null);
                } else {
                    map.put("status", "false");
                    map.put("message", "已经是暂停状态");
                }
            }else if("05".equals(type)) {
                if(GlobalrouteEnum.SYSTEM_STATE_SUSPEND.getCode().equals(CommonChannelUtil.getSystemState())) {
                    CommonChannelUtil.setSystemState(GlobalrouteEnum.SYSTEM_STATE_WROK.getCode());
                    SendMessgeToHtml.pushMessage(null);
                }else{
                    map.put("status", "false");
                    map.put("message", "不是暂停的状态，不能继续工作");
                }
            }else if("06".equals(type)) {
                if(GlobalrouteEnum.SYSTEM_STATE_WROK.getCode().equals(CommonChannelUtil.getSystemState())) {
                    //停止的话，需要将消息停止发送
                    boolean result = snapShotManagementGlobal.stopSystem();
                    if(!result){
                        map.put("status", "false");
                        map.put("message", "暂停操作失败");
                        return  map;
                    }
                    CommonChannelUtil.setSystemState(GlobalrouteEnum.SYSTEM_STATE_FREE.getCode());
                    SendMessgeToHtml.pushMessage(null);
                }else{
                    map.put("status", "false");
                    map.put("message", "不是工作中的状态，不能停止工作");
                }
            }
        } catch (Exception e) {
            map.put("status", "false");
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 继续
     *
     * @return
     */
    @RequestMapping(value = "/home/continueCmd")
    @ResponseBody
    public Map<String, Object> continueCmd() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "true");
        map.put("message", "success");
        try {

        } catch (Exception e) {
            map.put("status", "false");
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * pcr开始
     */
    @RequestMapping("/home/startPcr")
    @ResponseBody
    public Map<String, Object> startPcr(HttpServletRequest request) {
        //TODO
        //需要文库id
        //查询封装出所有托盘的和托盘里样本的信息
        Map<String, Object> map = new HashMap<>();
        map.put("status", "true");
        map.put("message", "success");
        try {

            FrameModel frameModel = InstrumentStateGlobal.getPcrdoStartMap("");
            new SendBaseCommand().sendEventFlowlist(MedicalRobotProcessEnum.MEDICAL_ROBOT_PLACE_CEHICLE.getCode(), frameModel);
        } catch (Exception e) {
            map.put("status", "false");
            map.put("message", e.getMessage());
        }


        return map;
    }

    /**
     * auto开始
     */
    @RequestMapping("/home/startAuto")
    @ResponseBody
    public Map<String, Object> startAuto(HttpServletRequest request) {
        //TODO
        //需要文库id
        //查询封装出所有托盘的和托盘里样本的信息
        Map<String, Object> map = new HashMap<>();
        map.put("status", "true");
        map.put("message", "success");
        try {
            if (!CubsParmGlobal.getIsStartAuto()) {
                Map<String,EpVehicleModel> deskMap_wk=setAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_WZ.getCode());
                EpVehicleModel epVehicleModel_wk=deskMap_wk.get(RedisStorageEnum.EP_NAME_WK.getCode());
                if(epVehicleModel_wk==null||epVehicleModel_wk.isIsnull()){
                    map.put("status", "false");
                    map.put("message","文库EP管载具不在");
                    return map;
                }
                if(epVehicleModel_wk.getEpList()==null||epVehicleModel_wk.getEpList().size()<1){
                    map.put("status", "false");
                    map.put("message","没有样本不能执行开始");
                    return map;
                }
                //将系统设置为工作中
               // CommonChannelUtil.setSystemState(GlobalrouteEnum.SYSTEM_STATE_WROK.getCode());
                new SendBaseCommand().sendEventFlowlist("206", null);
                CommonChannelUtil.setSystemState(GlobalrouteEnum.SYSTEM_STATE_WROK.getCode());
                CubsParmGlobal.setIsStartAuto(true);
                CubsParmGlobal.setIsAutoWork1(true);
            }else{
                map.put("status", "false");
                map.put("message","已经开始流程，不能重复开始");
            }

        } catch (Exception e) {
            map.put("status", "false");
            map.put("message", e.getMessage());
        }


        return map;
    }

    /**
     * 保存样本
     *
     * @param ysybxxDto
     * @return
     */
    @RequestMapping(value = "/home/saveSample")
    @ResponseBody
    public Map<String, Object> saveSample(YsybxxDto ysybxxDto) {
        Map<String, Object> map = new HashMap<>();
        if (ysybxxDto != null) {
            //将托盘里的样本
            map = ysybxxService.saveSample(ysybxxDto);
        }
        return map;
    }

    /**
     * 点击执行
     *
     * @param request
     * @return
     */
    @RequestMapping("/home/implementSample")
    @ResponseBody
    public Map<String, Object> implementSample(HttpServletRequest request) {
        //将托盘里的样本
        Map<String, Object> map = ysybxxService.implementSample();
        return map;
    }

    /**
     * 录入界面
     */
    @RequestMapping("/home/showInputInterface")
    @ResponseBody
    public Map<String, Object> showInputInterface(HttpServletRequest request) {
        //查询封装出所有托盘的和托盘里样本的信息
        Map<String, Object> map = ysybxxService.getTraySample();
        return map;
    }

    /**
     * 暂停或者停止
     */
    @RequestMapping("/home/stopOrTimeout")
    @ResponseBody
    public Map<String, Object> stopOrTimeout(String type, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        boolean result = stopAndTimeoutService.stopAndTimeOut(type);
        map.put("status", result ? "success" : "fail");
        map.put("message", result ? "操作成功" : "操作失败");
        return map;
    }

    /**
     * 传给前端通道
     */
    @RequestMapping("/home/cubsChannelSetup")
    @ResponseBody
    public Map<String, Object> cubsChannelSetup(HttpServletRequest request) {
        return yqxxInfoService.getJkywzxxList();

    }

    /**
     * 暂停或者停止
     */
    @RequestMapping("/home/saveChannelSetup")
    @ResponseBody
    public Map<String, Object> saveChannelSetup(JkywzszDto jkywzszDto, HttpServletRequest request) {
        return jkywzszService.saveChannelSetup(jkywzszDto);

    }

    /**
     * 初始化
     */
    @RequestMapping("/home/initializationAll")
    @ResponseBody
    public Map<String, Object> initializationAll(HttpServletRequest request) {
        return materialScienceInitService.initMaterial();

    }

    /**
     * 获取需要手动补充的物料区的信息
     *
     * @return
     */
    @RequestMapping("/home/getWlqList")
    @ResponseBody
    public String getWlqList() {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<Object, Object> redisMap = redisUtil.values("fma");
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            //前物料区
            for (Object key : redisMap.keySet()) {
                if (key.toString().contains("fma")) {
                    if (key.toString().equals("fma_tray")) {
//					JSONArray jsonArray= JSON.parseArray(redisMap.get(key).toString());
//					for(Object obj:jsonArray){
//						Map<String,Object>objMap=(Map<String,Object>)JSON.parse(obj.toString());
//						List<AgvsyybpModel> list1=(List<AgvsyybpModel>)objMap.get("boxList");
//						objMap.put("num",list1==null?0:list1.size());
//						objMap.put("key",key);
//						objMap.put("maxNum",GlobalParmEnum.CUBIS_CHANNEL_NUM);
//						mapList.add(objMap);
//					}
                    } else if (key.toString().contains("fma_gunhead")) {
                        Map<String, Object> objMap = (Map<String, Object>) JSON.parse(redisMap.get(key).toString());
                        List<String> list1 = (List<String>) objMap.get("gunhead");
                        objMap.put("num", list1 == null ? 0 : list1.size());
                        objMap.put("key", key);
                        objMap.put("maxNum", GlobalParmEnum.GUN_TOTAL_NUM.getCode());
                        mapList.add(objMap);
                    } else if (key.toString().contains("fma_ep")) {
                        Map<String, Object> objMap = (Map<String, Object>) JSON.parse(redisMap.get(key).toString());
                        List<AgvEpModel> list1 = (List<AgvEpModel>) objMap.get("epList");
                        objMap.put("num", list1 == null ? 0 : list1.size());
                        objMap.put("key", key);
                        objMap.put("maxNum", GlobalParmEnum.EP_TOTAL_NUM.getCode());
                        mapList.add(objMap);
                    } else {
                        Map<String, Object> map1 = JSON.parseObject(redisMap.get(key).toString());
                        map1.put("key", key);
                        mapList.add(map1);
                    }
                } else {
                    map.put(key.toString(), redisMap.get(key));
                }
            }
            map.put("key", "fma");
            redisMap = redisUtil.values("wzEpMap");
            for (Object key : redisMap.keySet()) {
                Map<String, Object> objMap = (Map<String, Object>) JSON.parse(redisMap.get(key).toString());
                if (AreaEnum.FMA_AREA.getCode().equals(objMap.get("qy"))) {
                    List<AgvEpModel> list1 = (List<AgvEpModel>) objMap.get("epList");
                    objMap.put("num", list1 == null ? 0 : list1.size());
                    objMap.put("key", key);
                    objMap.put("maxNum", GlobalParmEnum.EP_TOTAL_NUM.getCode());
                    mapList.add(objMap);
                }
            }
            map.put("list", mapList);
            list.add(map);
            map = new HashMap<>();
            mapList = new ArrayList<>();
            //后物料区
            redisMap = redisUtil.values("auto_desk");
            for (Object key : redisMap.keySet()) {
                if (key.toString().contains("name")) {
                    map.put(key.toString(), redisMap.get(key));
                } else if (key.toString().contains("deviceid")) {
                    map.put(key.toString(), redisMap.get(key));
                } else if (key.toString().contains("lx")) {
                    map.put(key.toString(), redisMap.get(key));
                } else if (key.toString().contains("ep")) {
                    Map<String, Object> objMap = (Map<String, Object>) JSON.parse(redisMap.get(key).toString());
                    List<AgvEpModel> list1 = (List<AgvEpModel>) objMap.get("epList");
                    objMap.put("num", list1 == null ? 0 : list1.size());
                    objMap.put("key", key);
                    objMap.put("maxNum", GlobalParmEnum.EP_TOTAL_NUM.getCode());
                    mapList.add(objMap);
                } else if (key.toString().contains("auto_octalianpipe")) {
                    Map<String, Object> objMap = (Map<String, Object>) JSON.parse(redisMap.get(key).toString());
                    objMap.put("key", key);
                    objMap.put("maxNum", GlobalParmEnum.OC_TOTAL_NUM.getCode());
                    mapList.add(objMap);
                }
            }
            map.put("key", "auto_desk");
            redisMap = redisUtil.values("wzEpMap");
            for (Object key : redisMap.keySet()) {
                Map<String, Object> objMap = (Map<String, Object>) JSON.parse(redisMap.get(key).toString());
                if (AreaEnum.AUTO_DESK_AREA.getCode().equals(objMap.get("qy"))) {
                    List<AgvEpModel> list1 = (List<AgvEpModel>) objMap.get("epList");
                    objMap.put("num", list1 == null ? 0 : list1.size());
                    objMap.put("key", key);
                    objMap.put("maxNum", GlobalParmEnum.EP_TOTAL_NUM.getCode());
                    mapList.add(objMap);
                }
            }
            map.put("list", mapList);
            list.add(map);
            map = new HashMap<>();
            mapList = new ArrayList<>();
            //配置仪中的物料
            redisMap = redisUtil.values("auto");
            for (Object key : redisMap.keySet()) {
                Map<String, Object> map1 = JSON.parseObject(redisMap.get(key).toString());
                map1.put("key", key);
                OctalianpipeListModel octalianpipeList = JSONObject.parseObject(map1.get("octalianpipeList").toString(), OctalianpipeListModel.class);
                map1.put("num", octalianpipeList.getNum());
                map1.put("maxNum", GlobalParmEnum.OC_TOTAL_NUM.getCode());
                mapList.add(map1);
            }
            map.put("name", "配置仪");
            map.put("key", "auto");
            map.put("list", mapList);
            list.add(map);
            map = new HashMap<>();
            mapList = new ArrayList<>();
            //压盖机中的物料
            redisMap = redisUtil.values("chm");
            for (Object key : redisMap.keySet()) {
                Map<String, Object> map1 = JSON.parseObject(redisMap.get(key).toString());
                map1.put("num", map1.get("chmgNum"));
                map1.put("key", key);
                map1.put("maxNum", GlobalParmEnum.COVER_TOTAL_NUM.getCode());
                mapList.add(map1);
            }
            map.put("name", "压盖机");
            map.put("key", "chm");
            map.put("list", mapList);
            list.add(map);
        } catch (Exception e) {
            map.put("status", "fail");
            logger.error(e.getMessage());
        }

        return JSON.toJSONString(list, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 补充单个的物料
     *
     * @param f_key
     * @param z_key
     * @param num
     * @return
     */
    @RequestMapping("/home/saveRedisByKey")
    @ResponseBody
    public Map<String, Object> saveRedisByKey(String f_key, String z_key, String num) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "success");
        try {
            if (z_key.contains("ep")) {
                EpVehicleModel epVehicleModel = redisSetAndGetUtil.getWzEpMap(z_key);
                ;
                epVehicleModel.setEpNm(num);
                List<AgvEpModel> list2 = new ArrayList<>();
                for (int j = 0; j < Integer.valueOf(num); j++) {
                    AgvEpModel agvEpModel = new AgvEpModel();
                    agvEpModel.setBlank(true);
                    int xzb = (int) Math.floor(j / 8) + 1;
                    int yzb = j % 8;
                    if (yzb == 0) {
                        yzb = 8;
                    }
                    agvEpModel.setXzb(xzb + "");
                    agvEpModel.setYzb(yzb + "");
                    list2.add(agvEpModel);
                }
                epVehicleModel.setEpList(list2);
                redisSetAndGetUtil.setWzEpMap(z_key, epVehicleModel);
                //redisUtil.hsetNoTime(f_key, z_key,JSONObject.toJSONString(epVehicleModel, SerializerFeature.WriteMapNullValue));
            } else if (z_key.contains("fma_gunhead")) {
                GunVehicleModel gunVehicleModel = redisSetAndGetUtil.getGunhead(f_key, z_key);
                ;
                gunVehicleModel.setGunNm(num);
                //枪头装满
                List<String> list = new ArrayList<>();
                for (int i = 0; i < Integer.valueOf(num); i++) {
                    list.add(i + "");
                }
                gunVehicleModel.setGunhead(list);
                redisUtil.hsetNoTime(f_key, z_key, JSONObject.toJSONString(gunVehicleModel, SerializerFeature.WriteMapNullValue));
            } else if (f_key.equals("auto")) {
                Object ob = redisSetAndGetUtil.getInstrument(f_key, z_key);
                if (ob != null) {
                    AutoMaterialModel autoMaterial = JSONObject.parseObject(ob.toString(), AutoMaterialModel.class);
                    OctalianpipeListModel octalianpipeList = new OctalianpipeListModel();
//					Map<String, List<OctalYbxxxModel>> map1 = new HashMap<>();
//					octalianpipeList.setMap(map1);
                    octalianpipeList.setNum(Integer.valueOf(num));
                    autoMaterial.setOctalianpipeList(octalianpipeList);
                    InstrumentMaterialGlobal.setAutoMaterial(autoMaterial, false);
                }
            } else if (f_key.equals("auto_desk")) {
                Object ob = redisSetAndGetUtil.getInstrument(f_key, z_key);
                if (ob != null) {
                    OctalianpipeListModel octalianpipeList = JSONObject.parseObject(ob.toString(), OctalianpipeListModel.class);
//					Map<String, List<OctalYbxxxModel>> map1 = new HashMap<>();
//					octalianpipeList.setMap(map1);
                    octalianpipeList.setNum(Integer.valueOf(num));
                    redisUtil.hsetNoTime(f_key, z_key, JSONObject.toJSONString(octalianpipeList, SerializerFeature.WriteMapNullValue));
                }
            } else if (f_key.contains("chm")) {
                Object ob = redisSetAndGetUtil.getInstrument(f_key, z_key);
                if (ob != null) {
                    ChmMaterialModel chmgMaterialModel = JSONObject.parseObject(ob.toString(), ChmMaterialModel.class);
                    chmgMaterialModel.setChmgNum(Integer.valueOf(num));
                    InstrumentMaterialGlobal.setChmMaterial(chmgMaterialModel, false);
                }


            }
        } catch (Exception e) {
            map.put("status", "fail");
            logger.error(e.getMessage());
        }
        return map;
    }
    /**
     * 一键补满
     * @return
     */
    @RequestMapping("/home/saveAllRedis")
    @ResponseBody
    public Map<String, Object> saveAllRedis() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "success");
        try {
            materialScienceInitService.initWlqMaterial();
        } catch (Exception e) {
            map.put("status", "fail");
            logger.error(e.getMessage());
        }
        return map;
    }
    /**
     * 保持样本信息
     *
     * @param list
     * @return
     */
    @ResponseBody
    @RequestMapping("/home/saveSampleList")
    public Map<String, Object> saveSample(@RequestBody List<YsybxxDto> list) {
        Map<String, Object> map = new HashMap<>();
        if (CubsParmGlobal.getIsStartAuto()) {
            map.put("status", "false");
            map.put("message","已经开始流程，不能添加样本");
            return map;
        }
        //需要保存的托盘核算和文库
        Map<String,EpVehicleModel> deskMap_wk=setAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_WZ.getCode());
        EpVehicleModel epVehicleModel_wk=deskMap_wk.get(RedisStorageEnum.EP_NAME_WK.getCode());
        if(epVehicleModel_wk!=null&&!epVehicleModel_wk.isIsnull()&&epVehicleModel_wk.getEpList()!=null){
            EpVehicleModel epVehicleModel_hs=deskMap_wk.get(RedisStorageEnum.EP_NAME_HS.getCode());
            //如果核酸板子没清空，让他先清空
            if(epVehicleModel_wk.getEpList().size()==0&&epVehicleModel_hs.getEpList().size()>0){
                map.put("status", "fail");
                map.put("message", "核酸EP载具未清空，请先点击右上角的清空按钮再来操作");
                return  map;
            }
            return ysybxxService.saveSampleList(list);
        }else {
            map.put("status", "fail");
            map.put("message", "EP管载具不存在");
        }
        return map;
    }
	/**
	 *发送钉钉消息
	 * @return
	 */
	public Map<String,Object> sendMessage(){
		Map<String,Object> resmap = new HashMap<>();
		String xxlx="";
		//对外发送钉钉消息接口
		String url="https://medlab.matridx.com/ws/pathogen/sendExternalMessageNew";
		//根据消息类型获取人员
		List<DdxxglDto> ddxxgldtolist = DdxxglService.selectByDdxxlx(xxlx);
		Map<String,Object> map=new HashMap<>();
		String ids="";
		if(CollectionUtils.isNotEmpty(ddxxgldtolist)) {
			//获取title
			String XX_HEAD = ddxxgldtolist.get(0).getDdxxmc();
			//这里是遍历用户
			for(DdxxglDto ddxxglDto :ddxxgldtolist) {
				//yhm为工号 拼接ids
				if(StringUtil.isNotBlank(ddxxglDto.getYhm())) {
					if(StringUtil.isNotBlank(ids)){
						ids+=ddxxglDto.getYhm();
					}else{
						ids+=","+ddxxglDto.getYhm();
					}
				}
			}
			map.put("ids",ids);
			map.put("title",XX_HEAD);
			map.put("message","发送数据");
		}
		/*HttpClientUtil httpClientUtil=new HttpClientUtil();
		//返回消息 errorCode信息说明 status请求状态
		String result= httpClientUtil.post(url,"",JSON.toJSONString(map));
		JSONObject jsonObject=JSONObject.parseObject(result);
		resmap.put("status", jsonObject.get("status").toString());
		resmap.put("errorCode", jsonObject.get("errorCode").toString());*/
		return resmap;
	}

    /**
     * 清空核酸信息
     *
     * @return
     */
    @RequestMapping("/home/clearHs")
    @ResponseBody
    public Map<String, Object> clearHs() {
        Map<String, Object> map = new HashMap<>();

        try {
        Map<String,EpVehicleModel> deskMap_wk=setAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_WZ.getCode());
        EpVehicleModel epVehicleModel_hs=deskMap_wk.get(RedisStorageEnum.EP_NAME_HS.getCode());
        if(epVehicleModel_hs!=null&&epVehicleModel_hs.getEpList()!=null){
            epVehicleModel_hs.getEpList().clear();
            setAndGetUtil.setWzEpMap(RedisStorageEnum.EP_NAME_HS.getCode(),epVehicleModel_hs);
        }
            map.put("status", "true");
            map.put("message", "success");

        } catch (Exception e) {
            map.put("status", "false");
            logger.error(e.getMessage());
        }
        return map;
    }
    /**
     * 获取样本信息
     *
     * @return
     */
    @RequestMapping("/home/getSampleList")
    @ResponseBody
    public Map<String, Object> getSampleList() {
        //需要保存的托盘核算和文库

        Map<String, Object> map = new HashMap<>();
        map.put("status", "success");
        Map<String, EpVehicleModel> deskMap_wk = redisSetAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_WZ.getCode());
        EpVehicleModel epVehicleModel_wk = deskMap_wk.get(RedisStorageEnum.EP_NAME_WK.getCode());
        try {
            map.put("list", epVehicleModel_wk.getEpList());
        } catch (Exception e) {
            map.put("status", "fail");
            logger.error(e.getMessage());
        }
        return map;
    }
    /**
     * pcr枪头补充
     */
    @RequestMapping("/home/fillupAuto")
    @ResponseBody
    public Map<String, Object> fillupAuto(String deviceid,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "true");
        map.put("message", "success");
        try {
            if(StringUtil.isNotBlank(deviceid)){
                materialScienceInitService.fillupAuto(deviceid);
            }else{
                map.put("status", "false");
                map.put("message", "deviceid为空");
            }
          } catch (Exception e) {
            map.put("status", "false");
            map.put("message", e.getMessage());
        }


        return map;
    }

}

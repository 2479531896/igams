package com.matridx.las.netty.util.snapshot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.enums.YesNotEnum;
import com.matridx.igams.common.redis.RedisStreamUtil;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.las.netty.channel.command.BaseCommand;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.IObserver;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.channel.server.handler.*;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.global.CommandParmGlobal;
import com.matridx.las.netty.global.CubsParmGlobal;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.global.RobotManagementGlobal;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.material.AutoDesktopGlobal;
import com.matridx.las.netty.global.material.FrontMaterialAreaGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.util.ChannelModel;
import com.matridx.las.netty.util.CommonChannelUtil;
import com.matridx.las.netty.util.RedisSetAndGetUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 快照复制和导出
 *
 * @author DELL
 */
@Service
public class SnapShotManagementGlobal {
    private static Logger log = LoggerFactory.getLogger(InstrumentStateGlobal.class);
    @Autowired
    private AvgProtocolHandler avgProtocolHandler;
    @Autowired
    private AutoProtocolHandler autoProtocolHandler;
    @Autowired
    private CubicsProtocolHandler cubicsProtocolHandler;
    @Autowired
    private PcrProtocolHandler pcrProtocolHandler;
    @Autowired
    private CmhProtocolHandler cmhProtocolHandler;
    @Autowired
    private SeqProtocolHandler seqProtocolHandler;
    @Autowired
    private RedisStreamUtil redisStreamUtil;
    @Autowired
    private RedisSetAndGetUtil redisSetAndGetUtil;
    @Autowired
    private RedisUtil redisUtil;



    public static boolean setAllParmtoPro() {
        try {
            JSONObject jsonObject = new JSONObject();
            //将前物料区得所有信息备注
            JSONObject fma = new JSONObject();
            //托盘
            fma.put("fma_tray", FrontMaterialAreaGlobal.getFma_tray());
            //枪头
            fma.put("fma_gunhead1", FrontMaterialAreaGlobal.getFma_gunhead1());
            fma.put("fma_gunhead2", FrontMaterialAreaGlobal.getFma_gunhead2());
            //ep管
            fma.put("fma_ep1", FrontMaterialAreaGlobal.getFma_ep1());
            fma.put("fma_ep2", FrontMaterialAreaGlobal.getFma_ep2());
            fma.put("fma_ep3", FrontMaterialAreaGlobal.getFma_ep3());
            fma.put("lx", FrontMaterialAreaGlobal.getLx());
            fma.put("deviceid", FrontMaterialAreaGlobal.getDeviceid());
            jsonObject.put("fma", fma);
            //机器人身上
            JSONObject agv = new JSONObject();
            agv.put("agv_tray1", AgvDesktopGlobal.getAgv_tray1());
            agv.put("agv_tray2", AgvDesktopGlobal.getAgv_tray2());
            agv.put("agv_gunhead", AgvDesktopGlobal.getAgv_gunhead());
            agv.put("agv_ep1", AgvDesktopGlobal.getAgv_ep1());
            agv.put("agv_ep2", AgvDesktopGlobal.getAgv_ep2());
            agv.put("agv_ep3", AgvDesktopGlobal.getAgv_ep3());
            agv.put("agv_octalianpipe", AgvDesktopGlobal.getAgv_octalianpipe());
            agv.put("state", AgvDesktopGlobal.getState());
            agv.put("clampingNum", AgvDesktopGlobal.getClampingNum());
            agv.put("deviceid", AgvDesktopGlobal.getDeviceid());
            agv.put("lx", AgvDesktopGlobal.getLx());
            jsonObject.put("agv", agv);
            //前物料区
            JSONObject auto_desk = new JSONObject();
            auto_desk.put("auto_octalianpipe1", AutoDesktopGlobal.getauto_octalianpipe1());
            auto_desk.put("auto_octalianpipe2", AutoDesktopGlobal.getauto_octalianpipe2());
            auto_desk.put("ad_ep1", AutoDesktopGlobal.getAd_ep1());
            jsonObject.put("auto_desk", auto_desk);
            //建库仪
            jsonObject.put("cubics", InstrumentMaterialGlobal.getCubsMaterialList());
            //配置仪
            jsonObject.put("auto", InstrumentMaterialGlobal.getAutoMaterialList());
            //加盖机
            jsonObject.put("chm", InstrumentMaterialGlobal.getChmMaterialList());
            //加该机盖
            //pcr
            jsonObject.put("pcr", InstrumentMaterialGlobal.getPcrMaterialList());
            //测序仪
            jsonObject.put("seq", InstrumentMaterialGlobal.getSeqMaterialList());
            //被占用得仪器得绑定得map
            //jsonObject.put("instrumentUsedList",InstrumentStateGlobal.getinstrumentUsedListAll());
            //放入空闲仪器得队列 instrumentstateGlobal
            //空闲仪器
            //jsonObject.put("instrumentQueues",InstrumentStateGlobal.getInstrumentQueues());
            //完成的
            //jsonObject.put("instrumentFinMap",InstrumentStateGlobal.getInstrumentFinMapAll());
            //文库开始信息
            //jsonObject.put("libaryInfMap",InstrumentStateGlobal.getLibaryInfMapAll());
            //手动导入excel文件后执行开始需要用到的
            jsonObject.put("pcrdoStartMap", InstrumentStateGlobal.getPcrdoStartMapAll());
            //CommandParmGlobal公用方法
            jsonObject.put("isSuspend", CommandParmGlobal.getIsSuspend());
            jsonObject.put("listFrameModel", CommandParmGlobal.getListFrameModel());
            jsonObject.put("mapDeviceid", CommandParmGlobal.getMapDeviceid());
            //CubsParmGlobal公用变量
            //jsonObject.put("cubQueues", CubsParmGlobal.getCubQueues());
            jsonObject.put("cubFinMap", CubsParmGlobal.getCubFinMapAll());
            jsonObject.put("havaUpCubis", CubsParmGlobal.getHavaUpCubis());
            jsonObject.put("cubFinQueues", CubsParmGlobal.getCubFinQueues());
            //jsonObject.put("iscubFin",CubsParmGlobal.isIscubFin());
            //jsonObject.put("freeCub",CubsParmGlobal.getFreeCub());
            jsonObject.put("lockObj", CubsParmGlobal.lockObj);
            jsonObject.put("isStartAuto", CubsParmGlobal.getIsStartAuto());
            jsonObject.put("isWorklc", CubsParmGlobal.getIisIsWorklc());
            jsonObject.put("isAutoWork1", CubsParmGlobal.getIsIsAutoWork1());
            jsonObject.put("isAutoWork", CubsParmGlobal.getIsIsAutoWork());
            //机器人类
            jsonObject.put("robotState", AgvDesktopGlobal.getState());
            jsonObject.put("remainingTasks", RobotManagementGlobal.getRemainingTasks());
            //jsonObject.put("robotQueue",RobotManagementGlobal.getQueueFromRedis());
            jsonObject.put("electric", RobotManagementGlobal.getElectric());
            //写入text
            SnapShotGloabalUtil.updateProperties("json", jsonObject.toJSONString());

        } catch (Exception e) {
            log.error(e.getMessage());
            return false;

        }
        return true;
    }

    /**
     * 将所有的信息放入全局变量
     *
     * @return
     */
    public static boolean getAllParmtoProToGlobal() {
        try {
            String json = SnapShotGloabalUtil.inputFile();
            if (StringUtil.isNotBlank(json)) {
                JSONObject jsonObjectAll = JSONObject.parseObject(json);

                /************************前料区放好**********************/
                if (jsonObjectAll.get("fma") != null) {
                    JSONObject fma = JSONObject.parseObject(jsonObjectAll.get("fma").toString());
                    Object fma_tray = fma.get("fma_tray");
                    if (fma_tray != null) {
                        List<TrayModel> avs = JSON.parseArray(fma_tray.toString(), TrayModel.class);
                        FrontMaterialAreaGlobal.setFma_tray(avs, false);
                    }
                    Object fma_gunhead1 = fma.get("fma_gunhead1");
                    if (fma_gunhead1 != null) {
                        GunVehicleModel gunVehicleModel = JSONObject.parseObject(fma_gunhead1.toString(), GunVehicleModel.class);
                        FrontMaterialAreaGlobal.setFma_gunhead1(gunVehicleModel, false);
                    }

                    Object fma_gunhead2 = fma.get("fma_gunhead2");
                    if (fma_gunhead2 != null) {
                        GunVehicleModel gunVehicleModel = JSONObject.parseObject(fma_gunhead2.toString(), GunVehicleModel.class);
                        FrontMaterialAreaGlobal.setFma_gunhead2(gunVehicleModel, false);
                    }
                    Object fma_ep1 = fma.get("fma_ep1");
                    Object fma_ep2 = fma.get("fma_ep2");
                    Object fma_ep3 = fma.get("fma_ep3");
                    if (fma_ep1 != null) {
                        EpVehicleModel epVehicleModel = JSONObject.parseObject(fma_ep1.toString(), EpVehicleModel.class);
                        FrontMaterialAreaGlobal.setFma_ep1(epVehicleModel, false);
                    }
                    if (fma_ep2 != null) {
                        EpVehicleModel epVehicleModel = JSONObject.parseObject(fma_ep2.toString(), EpVehicleModel.class);
                        FrontMaterialAreaGlobal.setFma_ep2(epVehicleModel, false);
                    }
                    if (fma_ep3 != null) {
                        EpVehicleModel epVehicleModel = JSONObject.parseObject(fma_ep3.toString(), EpVehicleModel.class);
                        FrontMaterialAreaGlobal.setFma_ep3(epVehicleModel, false);
                    }
                    Object lxFma = fma.get("lx");
                    if (lxFma != null) {
                        FrontMaterialAreaGlobal.setLx(lxFma.toString());
                    } else {
                        FrontMaterialAreaGlobal.setLx("");
                    }
                    Object deviceidFma = fma.get("deviceid");
                    if (deviceidFma != null) {
                        FrontMaterialAreaGlobal.setDeviceid(deviceidFma.toString());
                    } else {
                        FrontMaterialAreaGlobal.setDeviceid("");
                    }
                }
                /***************************agv台上的物料信息***************************************/
                if (jsonObjectAll.get("agv") != null) {
                    JSONObject agv = JSONObject.parseObject(jsonObjectAll.get("agv").toString());
                    Object agv_tray1 = agv.get("agv_tray1");
                    if (agv_tray1 != null) {
                        Map<String, Object> map = (Map<String, Object>) JSONObject.parse(agv_tray1.toString());
                        Map<String, YsybxxDto> map1 = new HashMap<String, YsybxxDto>();
                        for (String key : map.keySet()) {
                            JSONObject jsonObject1 = (JSONObject) map.get(key);
                            YsybxxDto av = JSONObject.toJavaObject(jsonObject1, YsybxxDto.class);
                            map1.put(key, av);
                        }
                        AgvDesktopGlobal.setAgv_tray1(map1, false);
                    }
                    Object agv_tray2 = agv.get("agv_tray2");
                    if (agv_tray2 != null) {
                        Map<String, Object> map = (Map<String, Object>) JSONObject.parse(agv_tray2.toString());
                        Map<String, YsybxxDto> map1 = new HashMap<String, YsybxxDto>();
                        for (String key : map.keySet()) {
                            JSONObject jsonObject1 = (JSONObject) map.get(key);
                            YsybxxDto av = JSONObject.toJavaObject(jsonObject1, YsybxxDto.class);
                            map1.put(key, av);
                        }
                        AgvDesktopGlobal.setAgv_tray2(map1, true);
                    }
                    Object agv_gunhead = agv.get("agv_gunhead");
                    if (agv_gunhead != null) {
                        GunVehicleModel gunVehicleModel = JSONObject.parseObject(agv_gunhead.toString(), GunVehicleModel.class);
                        AgvDesktopGlobal.setAgv_gunhead(gunVehicleModel, true);
                    }
                    Object agv_ep1 = agv.get("agv_ep1");
                    Object agv_ep2 = agv.get("agv_ep2");
                    Object agv_ep3 = agv.get("agv_ep3");
                    if (agv_ep1 != null) {
                        EpVehicleModel epVehicleModel = JSONObject.parseObject(agv_ep1.toString(), EpVehicleModel.class);
                        AgvDesktopGlobal.setAgv_ep1(epVehicleModel, true);
                    }
                    if (agv_ep2 != null) {
                        EpVehicleModel epVehicleModel = JSONObject.parseObject(agv_ep2.toString(), EpVehicleModel.class);
                        AgvDesktopGlobal.setAgv_ep2(epVehicleModel, true);
                    }
                    if (agv_ep3 != null) {
                        EpVehicleModel epVehicleModel = JSONObject.parseObject(agv_ep3.toString(), EpVehicleModel.class);
                        AgvDesktopGlobal.setAgv_ep3(epVehicleModel, true);
                    }
                    Object agv_octalianpipe = agv.get("agv_octalianpipe");
                    if (agv_octalianpipe != null) {
                        OctalianpipeListModel octalianpipeListModel = JSON.parseObject(agv_octalianpipe.toString(), OctalianpipeListModel.class);
                        AgvDesktopGlobal.setAgv_octalianpipe(octalianpipeListModel, true);
                    }
                    Object lxAgv = agv.get("lx");
                    if (lxAgv != null) {
                        AgvDesktopGlobal.setLx(lxAgv.toString());
                    } else {
                        AgvDesktopGlobal.setLx("");
                    }
                    Object deviceidAgv = agv.get("deviceid");
                    if (deviceidAgv != null) {
                        AgvDesktopGlobal.setDeviceid(deviceidAgv.toString());
                    } else {
                        AgvDesktopGlobal.setDeviceid("");
                    }
                    Object clampingNumAgv = agv.get("clampingNum");
                    if (clampingNumAgv != null) {
                        AgvDesktopGlobal.setClampingNum(clampingNumAgv.toString());
                    } else {
                        AgvDesktopGlobal.setClampingNum("");
                    }
                    Object stateAgv = agv.get("state");
                    if (stateAgv != null) {
                        AgvDesktopGlobal.setState(stateAgv.toString());
                    } else {
                        AgvDesktopGlobal.setState("");
                    }
                }

                /***************************建库仪后物料区***************************************/
                if (jsonObjectAll.get("auto_desk") != null) {
                    JSONObject auto_desk = JSONObject.parseObject(jsonObjectAll.get("auto_desk").toString());
                    Object auto_octalianpipe1 = auto_desk.get("auto_octalianpipe1");
                    if (auto_octalianpipe1 != null) {
                        OctalianpipeListModel octalianpipeListModel = JSON.parseObject(auto_octalianpipe1.toString(), OctalianpipeListModel.class);
                        AutoDesktopGlobal.setauto_octalianpipe1(octalianpipeListModel, false);
                    }
                    Object auto_octalianpipe2 = auto_desk.get("auto_octalianpipe2");
                    if (auto_octalianpipe2 != null) {
                        OctalianpipeListModel octalianpipeListModel = JSON.parseObject(auto_octalianpipe2.toString(), OctalianpipeListModel.class);
                        AutoDesktopGlobal.setauto_octalianpipe2(octalianpipeListModel, false);
                    }
                    Object ad_ep1 = auto_desk.get("ad_ep1");
                    if (ad_ep1 != null) {
                        EpVehicleModel epVehicleModel = JSONObject.parseObject(ad_ep1.toString(), EpVehicleModel.class);
                        AutoDesktopGlobal.setAd_ep1(epVehicleModel, false);
                    }
                }
                /****************************建库仪***************************************/
                if (jsonObjectAll.get("cubics") != null) {
                    JSONArray jsonArray = JSONObject.parseArray(jsonObjectAll.get("cubics").toString());
                    for (Object ob : jsonArray) {
                        if (ob != null) {
                            CubsMaterialModel cubsMaterial = JSONObject.parseObject(ob.toString(), CubsMaterialModel.class);
                            InstrumentMaterialGlobal.setCubsMaterial(cubsMaterial, true);
                        }
                    }
                }
                /****************************配置仪***************************************/
                if (jsonObjectAll.get("auto") != null) {
                    JSONArray jsonArray = JSONObject.parseArray(jsonObjectAll.get("auto").toString());
                    for (Object ob : jsonArray) {
                        if (ob != null) {
                            AutoMaterialModel j = JSON.parseObject(ob.toString(), AutoMaterialModel.class);
                            InstrumentMaterialGlobal.setAutoMaterial(j, true);
                        }
                    }
                }
                /****************************加该机***************************************/
                if (jsonObjectAll.get("chm") != null) {
                    JSONArray jsonArray = JSONObject.parseArray(jsonObjectAll.get("chm").toString());
                    for (Object ob : jsonArray) {
                        if (ob != null) {
                            ChmMaterialModel chmMaterial = JSONObject.parseObject(ob.toString(), ChmMaterialModel.class);
                            InstrumentMaterialGlobal.setChmMaterial(chmMaterial, true);
                        }
                    }
                }

                /****************************定量仪***************************************/
                if (jsonObjectAll.get("pcr") != null) {
                    JSONArray jsonArray = JSONObject.parseArray(jsonObjectAll.get("pcr").toString());
                    for (Object ob : jsonArray) {
                        if (ob != null) {
                            PcrMaterialModel j = JSON.parseObject(ob.toString(), PcrMaterialModel.class);
                            InstrumentMaterialGlobal.setPcrMaterial(j, true);
                        }
                    }
                }
                /****************************测序仪***************************************/
                if (jsonObjectAll.get("seq") != null) {
                    JSONArray jsonArray = JSONObject.parseArray(jsonObjectAll.get("seq").toString());
                    for (Object ob : jsonArray) {
                        if (ob != null) {
                            SeqMaterialModel j = JSON.parseObject(ob.toString(), SeqMaterialModel.class);
                            InstrumentMaterialGlobal.setSeqMaterial(j, true);
                        }
                    }
                }
                /****************************被占用得仪器得绑定得map***************************************/
                if (jsonObjectAll.get("instrumentUsedList") != null) {
                    List<Map<String, String>> listObjectSec = JSONArray.parseObject(jsonObjectAll.get("instrumentUsedList").toString(), List.class);
                    //InstrumentStateGlobal.setinstrumentUsedListAll(listObjectSec);
                } else {
                    //InstrumentStateGlobal.setinstrumentUsedListAll(null);
                }
                /****************************空闲仪器***************************************/
                if (jsonObjectAll.get("instrumentQueues") != null) {
                    Map<String, BlockingQueue<String>> listObjectSec = JSONArray.parseObject(jsonObjectAll.get("instrumentQueues").toString(), Map.class);
                    //	InstrumentStateGlobal.setInstrumentQueues(listObjectSec);
                } else {
                    //	InstrumentStateGlobal.setInstrumentQueues(null);
                }
                /****************************空闲仪器***************************************/

            }


		/*
			jsonObject.put("instrumentUsedList",InstrumentStateGlobal.getinstrumentUsedListAll());
			//放入空闲仪器得队列 instrumentstateGlobal
			//空闲仪器
			jsonObject.put("instrumentQueues",InstrumentStateGlobal.getInstrumentQueues());
			//完成的
			jsonObject.put("instrumentFinMap",InstrumentStateGlobal.getInstrumentFinMapAll());
			//文库开始信息
			jsonObject.put("libaryInfMap",InstrumentStateGlobal.getLibaryInfMapAll());
			//手动导入excel文件后执行开始需要用到的
			jsonObject.put("pcrdoStartMap",InstrumentStateGlobal.getPcrdoStartMapAll());
			//CommandParmGlobal公用方法
			jsonObject.put("isSuspend", CommandParmGlobal.getIsSuspend());
			jsonObject.put("listFrameModel",CommandParmGlobal.getListFrameModel());
			jsonObject.put("mapDeviceid",CommandParmGlobal.getMapDeviceid());
			//CubsParmGlobal公用变量
			jsonObject.put("cubQueues", CubsParmGlobal.getCubQueues());
			jsonObject.put("cubFinMap",CubsParmGlobal.getCubFinMapAll());
			jsonObject.put("havaUpCubis",CubsParmGlobal.getHavaUpCubis());
			jsonObject.put("cubFinQueues",CubsParmGlobal.getCubFinQueues());
			jsonObject.put("iscubFin",CubsParmGlobal.isIscubFin());
			jsonObject.put("freeCub",CubsParmGlobal.getFreeCub());
			jsonObject.put("lockObj",CubsParmGlobal.lockObj);
			jsonObject.put("isStartAuto",CubsParmGlobal.getIsStartAuto());
			jsonObject.put("isWorklc",CubsParmGlobal.getIisIsWorklc());
			jsonObject.put("isAutoWork1",CubsParmGlobal.getIsIsAutoWork1());
			jsonObject.put("isAutoWork",CubsParmGlobal.getIsIsAutoWork());
			//机器人类
			jsonObject.put("robotState", RobotManagementGlobal.getRobotState());
			jsonObject.put("remainingTasks",RobotManagementGlobal.getRemainingTasks());
			jsonObject.put("robotQueue",RobotManagementGlobal.getRobotQueue());
			jsonObject.put("electric",RobotManagementGlobal.getElectric());*/
            //写入text

        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    //获取当前的的运行的已发送过的命令流程，未发送的，map等信息
    public List<Map<String, Object>> getAllCommand() {
        //将对象里的队列放出来
        List<BaseCommandPropertyModel> list = new ArrayList<>();
        //转变为从新加的管理类里面修改
        List<BaseCommand> baseCommandList = CommonChannelUtil.getLcBaseCommandList();
        //已经获取到所有正在发送的信息
        //封装所有的map
        saveBasecommandList(baseCommandList, list);
        //封装成可供表格显示的
        List<Map<String, Object>> list1 = new ArrayList<>();
        for (BaseCommandPropertyModel baseCommandPropertyModel : list) {
            Map<String, BlockingQueue<SjlcDto>> eventDic = baseCommandPropertyModel.getEventDic();
            Map<String, BlockingQueue<SjlcDto>> haveSendeventDic = baseCommandPropertyModel.getHaveSendeventDic();
            Map<String, BlockingQueue<FrameModel>> cmdDic = baseCommandPropertyModel.getCmdDic();
            Map<String, BlockingQueue<FrameModel>> haveSendcmdDic = baseCommandPropertyModel.getHaveSendcmdDic();
            if (eventDic != null && eventDic.size() > 0) {
                for (String thr : eventDic.keySet()) {
                    if (StringUtil.isNotBlank(thr)) {
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("id", baseCommandPropertyModel.getUnid());
                        map1.put("threadName", thr);
                        if (eventDic.get(thr) != null) {
                            map1.put("eventDicNum", eventDic.get(thr).size());
                        } else {
                            map1.put("eventDicNum", 0);
                        }
                        if (haveSendeventDic.get(thr) != null) {
                            map1.put("haveSendeventDicNum", haveSendeventDic.get(thr).size());
                        } else {
                            map1.put("haveSendeventDicNum", 0);
                        }
                        if (baseCommandPropertyModel.getThisSjlcDto() != null) {
                            map1.put("lclx", baseCommandPropertyModel.getThisSjlcDto().getLclx());
                        }
                        if (cmdDic.get(thr) != null) {
                            map1.put("cmdDicNum", cmdDic.get(thr).size());
                        } else {
                            map1.put("cmdDicNum", 0);
                        }
                        if (haveSendcmdDic.get(thr) != null) {
                            map1.put("haveSendcmdDicNum", haveSendcmdDic.get(thr).size());
                        } else {
                            map1.put("haveSendcmdDicNum", 0);
                        }
                        list1.add(map1);
                    }
                }
            }
        }


        return list1;


    }

    public boolean saveBasecommandList(List<BaseCommand> baseCommandList, List<BaseCommandPropertyModel> list) {
        for (BaseCommand baseCommand : baseCommandList) {
            BaseCommandPropertyModel baseCommandPropertyModel = new BaseCommandPropertyModel();
            baseCommandPropertyModel.setCmdDic(baseCommand.getCmdDic());
            baseCommandPropertyModel.setEventDic(baseCommand.getEventDic());
            baseCommandPropertyModel.setExcutingCmdDic(baseCommand.getExcutingCmdDic());
            baseCommandPropertyModel.setThisSjlcDto(baseCommand.getThisSjlcDto());
            //baseCommandPropertyModel.setCommand(command);
            //baseCommandPropertyModel.setDeviceid(a);
            baseCommandPropertyModel.setHaveSendcmdDic(baseCommand.getHaveSendcmdDic());
            baseCommandPropertyModel.setHaveSendeventDic(baseCommand.getHaveSendeventDic());
            baseCommandPropertyModel.setUnid(baseCommand.getUnid());
            list.add(baseCommandPropertyModel);
        }
        return true;
    }

    public boolean sendMessageAgain(FrameModel frameModel, String id) {
        try {
            List<BaseCommand> list = CommonChannelUtil.getLcBaseCommandList();
            BaseCommand baseCommand = null;
            for (BaseCommand baseC : list) {
                if (id.equals(baseC.getUnid())) {
                    baseCommand = baseC;
                    break;
                }
            }
            if (baseCommand != null) {
                // 发送消息
                baseCommand.sendByCmdModel(frameModel.getCommand(), frameModel.getDeviceID(), frameModel.getMsgType(), frameModel.getCmd(),
                        frameModel.getCmdParam(), frameModel.getCallFunc(), frameModel.isSync(),
                        frameModel.isLastSync(), frameModel.getThreadName(), frameModel.getFirstTimeout(),
                        frameModel.getSecondTimeout(), frameModel.getMlid(), frameModel.getYqUsedMap(), true, frameModel.getHowTimes() + 1, frameModel.getLcid(), frameModel.getPassId(), frameModel.getBz());

            } else {
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 取对象里得队列直接发送接下来得命令
     *
     * @param threadName
     * @param id
     * @return
     */
    public boolean sendMessageBythreadname(String threadName, String id) {
        try {
            List<BaseCommand> list = CommonChannelUtil.getLcBaseCommandList();
            BaseCommand baseCommand = null;
            for (BaseCommand baseC : list) {
                if (id.equals(baseC.getUnid())) {
                    baseCommand = baseC;
                    break;
                }
            }
            if (baseCommand != null) {
                //发消息前要清除excutingCmdDic中注册信息
                FrameModel msgModel = baseCommand.getExcutingCmdDicFrameModelByThreadName(threadName);
                //还要清除观察者里的信息
                if (msgModel != null) {
                    String deviceId = "";
                    if (Command.CUBICS.toString().equals(msgModel.getCommand()) && StringUtil.isNotBlank(msgModel.getPassId())) {
                        deviceId = msgModel.getCommanddeviceid() + msgModel.getPassId();
                    } else {
                        deviceId = msgModel.getCommanddeviceid();
                    }
                    //获取观察者
                    ChannelModel channelModel = CommonChannelUtil.getRegisterChannels().get(msgModel.getCommanddeviceid());
                    MatridxProtocolHandler<?> handler = CommonChannelUtil.protocols.get(channelModel.getProtocol());
                    Map<String, List<IObserver>> cmdobservers = handler.getCmdobservers();
                    if (cmdobservers.containsKey(deviceId)
                            && cmdobservers.get(deviceId).size() > 0) {
                        List<IObserver> obList = cmdobservers.get(deviceId);
                        obList.remove(0);
                    }
                }
                baseCommand.removeExcutingCmdDicByThreadName(threadName);
                // 发送消息
                //通过未发送得队列找到下一个要发送得命令
                Map<String, BlockingQueue<FrameModel>> cmdDic = baseCommand.getCmdDic();
                if (cmdDic != null) {
                    BlockingQueue<FrameModel> frameModels = cmdDic.get(threadName);
                    if (frameModels != null && frameModels.size() > 0) {
                        FrameModel frameModel = frameModels.take();
                        //发送时，最后一条发送
                        baseCommand.sendByCmdModel(frameModel.getCommand(), frameModel.getDeviceID(), frameModel.getMsgType(), frameModel.getCmd(),
                                frameModel.getCmdParam(), frameModel.getCallFunc(), frameModel.isSync(),
                                true, frameModel.getThreadName(), frameModel.getFirstTimeout(),
                                frameModel.getSecondTimeout(), frameModel.getMlid(), frameModel.getYqUsedMap(), true, frameModel.getHowTimes() + 1, frameModel.getLcid(), frameModel.getPassId(), frameModel.getBz());
                    }
                }

            } else {
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 直接发送事件
     *
     * @param threadName
     * @param id
     * @return
     */
    public boolean sendEvenBythreadname(String threadName, String id,String command,String deviceid1) {
        try {
            List<BaseCommand> list = CommonChannelUtil.getLcBaseCommandList();
            BaseCommand baseCommand = null;
            for (BaseCommand baseC : list) {
                if (id.equals(baseC.getUnid())) {
                    baseCommand = baseC;
                    break;
                }
            }
            if (baseCommand != null) {
                //发消息前要清除excutingCmdDic中注册信息
                FrameModel msgModel = baseCommand.getExcutingCmdDicFrameModelByThreadName(threadName);
                //还要清除观察者里的信息
                if (msgModel != null) {
                    String deviceId = "";
                    if (Command.CUBICS.toString().equals(msgModel.getCommand()) && StringUtil.isNotBlank(msgModel.getPassId())) {
                        deviceId = msgModel.getCommanddeviceid() + msgModel.getPassId();
                    } else {
                        deviceId = msgModel.getCommanddeviceid();
                    }
                    //获取观察者
                    MatridxProtocolHandler<?> handler = CommonChannelUtil.protocols.get(msgModel.getCommand());
                    Map<String, List<IObserver>> cmdobservers = handler.getCmdobservers();
                    if (cmdobservers.containsKey(deviceId)
                            && cmdobservers.get(deviceId).size() > 0) {
                        List<IObserver> obList = cmdobservers.get(deviceId);
                        obList.remove(0);
                    }
                }
                baseCommand.removeExcutingCmdDicByThreadName(threadName);
                baseCommand.getCmdDic().remove(threadName);
                baseCommand.getHaveSendcmdDic().remove(threadName);
                Map<String, BlockingQueue<SjlcDto>> eventDic = baseCommand.getEventDic();
                BlockingQueue<SjlcDto> t_eventList = eventDic.get(threadName);
                SjlcDto sjlcDtoLast = t_eventList.take();
                Map<String, BlockingQueue<SjlcDto>> haveSendeventDic = baseCommand.getHaveSendeventDic();
                BlockingQueue<SjlcDto> t_haveSendeventList = haveSendeventDic.get(threadName);
                if (t_haveSendeventList == null) {
                    t_haveSendeventList = new LinkedBlockingQueue<SjlcDto>();
                    haveSendeventDic.put(threadName, t_haveSendeventList);
                    log.info("已执行执行事件队列里的信息threadName：" + threadName + "事件数量：" + haveSendeventDic.size());
                }
                t_haveSendeventList.add(sjlcDtoLast);
                baseCommand.setThisSjlcDto(sjlcDtoLast);
                //获取map
                Map<String, String> ma = null;
                //先获取流程中的仪器值
                if (StringUtil.isNotBlank(deviceid1) && StringUtil.isNotBlank(command)) {
                    ma = InstrumentStateGlobal.getInstrumentUsedListMap(command, deviceid1);
                }
                //剔除掉需要新获取的
                if (YesNotEnum.YES.getCode().equals(sjlcDtoLast.getSfxjqr()) && YesNotEnum.YES.getCode().equals(sjlcDtoLast.getSfzyjqr())) {
                    if (ma != null) {
                        String deviceid = ma.get(Command.AGV.toString());
                        if (StringUtil.isNotBlank(deviceid)) {
                            InstrumentStateGlobal.removeInstrumentUsedList_Map(ma, Command.AGV.toString(), deviceid);
                            InstrumentStateGlobal.putInstrumentQueuesToRedis(Command.AGV.toString(), deviceid);
                        }
                    }
                }
                if (YesNotEnum.YES.getCode().equals(sjlcDtoLast.getSfxyq()) && YesNotEnum.YES.getCode().equals(sjlcDtoLast.getSfzhyg()) && StringUtil.isNotBlank(sjlcDtoLast.getZyyqlx())) {
                    if (ma != null) {
                        String zylx = sjlcDtoLast.getZyyqlx();
                        String deviceid = ma.get(zylx);
                        if (StringUtil.isNotBlank(deviceid)) {
                            InstrumentStateGlobal.removeInstrumentUsedList_Map(ma, zylx, deviceid);
                            InstrumentStateGlobal.putInstrumentQueuesToRedis(zylx, deviceid);
                        }
                    }
                }
                //调用发送命令的方法
                baseCommand.sendCommandListThread(true, sjlcDtoLast, threadName, ma);
                log.info("开始执行事件队列里的信息threadName：" + threadName + "事件数量：" + t_eventList.size());


            } else {
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 获取空闲仪器队列
     *
     * @param type
     * @return
     */
    public List<String> getQueceRange(String type) {
        List<String> listnew = new ArrayList();
        try {
            List<MapRecord<String, Object, Object>> list = redisStreamUtil.range(type);
            if (list != null && list.size() > 0) {
                for (MapRecord<String, Object, Object> mapRecord : list) {
                    Map<Object, Object> map = mapRecord.getValue();
                    for (Object o : map.keySet()) {
                        if (o != null) {
                            listnew.add(o.toString());
                            break;
                        }
                    }

                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return listnew;
        }
        return listnew;
    }

    /**
     * 获取流程ma
     *
     * @return
     */
    public List<Object> getInstrumentUsedListAll() {
        List<Object> listnew = new ArrayList();
        try {
            listnew = redisUtil.lGet("InstrumentUsedList");
        } catch (Exception e) {
            log.error(e.getMessage());
            return listnew;
        }
        return listnew;
    }

    /**
     * 获取流程ma
     *
     * @return
     */
    public boolean delInstrumentUsedListAll() {

        try {
            redisSetAndGetUtil.delStream("InstrumentUsedList");

        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    public Map<String, Object> getCmdAndEvenListBythreadName(String id, String threadName) {
        Map<String, Object> mapResult = new HashMap<>();
        BaseCommand baseCommand = null;
        List<BaseCommand> baseCommandList = CommonChannelUtil.getLcBaseCommandList();
        for (BaseCommand b : baseCommandList) {
            if (id.equals(b.getUnid())) {
                baseCommand = b;
                break;
            }
        }
        List<SjlcDto> sjlcDtoList = new ArrayList<>();
        List<SjlcDto> havesjlcDtoList = new ArrayList<>();
        List<FrameModelToHtml> havecmdList = new ArrayList<>();
        List<FrameModelToHtml> cmdList = new ArrayList<>();

        if (baseCommand != null) {
            BlockingQueue<SjlcDto> even = baseCommand.getEventDic().get(threadName);
            if (even != null) {
                for (SjlcDto s : even) {
                    s.setZt("未发送");
                    sjlcDtoList.add(s);
                }
            }
            BlockingQueue<SjlcDto> haveEven = baseCommand.getHaveSendeventDic().get(threadName);
            if (haveEven != null) {
                for (SjlcDto s : haveEven) {
                    s.setZt("已发送");
                    havesjlcDtoList.add(s);
                }
            }
            BlockingQueue<FrameModel> havecmdblockingQueue = baseCommand.getHaveSendcmdDic().get(threadName);
            if (havecmdblockingQueue != null) {
                for (FrameModel f : havecmdblockingQueue) {
                    FrameModelToHtml frameModelToHtml = setFrameModelToFrameModelHtml(f);
                    //传入回调
                    frameModelToHtml.setCallFunc(getClassNameCallback(f));
                    frameModelToHtml.setZt("已发送");
                    havecmdList.add(frameModelToHtml);
                }
            }
            BlockingQueue<FrameModel> cmdblockingQueue = baseCommand.getCmdDic().get(threadName);
            if (cmdblockingQueue != null) {
                for (FrameModel f : cmdblockingQueue) {
                    FrameModelToHtml frameModelToHtml = setFrameModelToFrameModelHtml(f);
                    //传入回调
                    frameModelToHtml.setCallFunc(getClassNameCallback(f));
                    frameModelToHtml.setZt("未发送");
                    cmdList.add(frameModelToHtml);
                }
            }
        }
        //	map1.put("eventDic",eventDic.get(thr));
        //	map1.put("haveSendeventDic",haveSendeventDic.get(thr));
        //	map1.put("cmdDic",cmdDic.get(thr));
        //	map1.put("haveSendcmdDic",haveSendcmdDic.get(thr));

        mapResult.put("cmdDic", cmdList);
        mapResult.put("eventDic", sjlcDtoList);
        mapResult.put("haveSendcmdDic", havecmdList);
        mapResult.put("haveSendeventDic", havesjlcDtoList);
        return mapResult;
    }

    public FrameModelToHtml setFrameModelToFrameModelHtml(FrameModel frameModel) {
        FrameModelToHtml frameModelToHtml = new FrameModelToHtml();
        BeanUtils.copyProperties(frameModel, frameModelToHtml);
        return frameModelToHtml;
    }

    public String getClassNameCallback(FrameModel frameModel) {
        String callback = "";
        if (frameModel.getCallFunc() != null) {
            String classname = frameModel.getCallFunc().getClass().getName();
            String[] classnames = classname.split("\\.");
            if (classnames.length > 0) {
                callback = classnames[classnames.length - 1];
                if (callback.length() > 1) {
                    callback = callback.substring(0, 1).toLowerCase() + callback.substring(1);
                } else {
                    callback = callback.toLowerCase();
                }
            }

        }
        return callback;
    }

    /**
     * 停止整个系统
     * @return
     */
    public boolean stopSystem() {
        //清空观察者
        Map<String, MatridxProtocolHandler<?>> map =  CommonChannelUtil.getProtocols();
        for(String key:map.keySet()){
            MatridxProtocolHandler matridxProtocolHandler = map.get(key);
            matridxProtocolHandler.getCmdobservers().clear();
            matridxProtocolHandler.getcmdThreadNames().clear();
        }
        //清除base里面未发送的消息和事件
        List<BaseCommand> baseCommandList = CommonChannelUtil.getLcBaseCommandList();
        if(baseCommandList!=null&&baseCommandList.size()>0){
            for(BaseCommand baseCommand:baseCommandList){
                //清空命令
                baseCommand.getCmdDic().clear();
                //清空事件
                baseCommand.getEventDic().clear();
                //清空
                baseCommand.getExcutingCmdDic().clear();
            }
            baseCommandList.clear();
        }

        return true;
    }

}

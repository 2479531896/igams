package com.matridx.las.netty.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.enums.*;
import com.matridx.las.netty.global.material.FrontMaterialAreaGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.util.sseemitter.SseEmitterAllServer;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            JSONArray cubics = InstrumentMaterialGlobal.getCubsMaterialList();

            //需要封装成需要的格式
            if (cubics != null && cubics.size() > 0) {
                JSONObject cjson = new JSONObject();
                Map<String, JSONArray> map = new HashMap<String, JSONArray>();

                for (int i = 0; i < cubics.size(); i++) {
                    CubsMaterialModel cubsMaterialModel = (CubsMaterialModel) cubics.get(i);
                    JSONArray ob = map.get(cubsMaterialModel.getCommanddeviceid());
                    if (ob != null && ob.size() > 0) {
                        ob.add(cubsMaterialModel);
                    } else {
                        JSONArray ob1 = new JSONArray();
                        ob1.add(cubsMaterialModel);
                        map.put(cubsMaterialModel.getCommanddeviceid(), ob1);
                    }
                }
                //再将map封装成jsonArray
                JSONArray jsonArray = new JSONArray();
                for (String key : map.keySet()) {
                    JSONObject object = new JSONObject();
                    object.put("commanddeviceid", key);
                    JSONArray ja = map.get(key);
                    //正在工作中的建库仪通道数量
                    int count = 0;
                    Map<String,String> qyMap = new HashMap<>();
                    for (Object jo : ja) {
                        if (jo != null) {
                            CubsMaterialModel cubsMaterialModel = (CubsMaterialModel) jo;
                            qyMap = cubsMaterialModel.getSendSxType();
                            if (InstrumentStatusEnum.STATE_WORK.getCode().equals(cubsMaterialModel.getState())||InstrumentStatusEnum.STATE_FREE.getCode().equals(cubsMaterialModel.getState())) {
                                count++;
                            }
                        }

                    }
                    //属性值推送
                    Map<String,String> map1 =new HashMap<>();
                    map1.put("channel",count+"");
                    JSONArray jsonArray1 = putProToMe(map1,qyMap);
                    object.put("sxList",jsonArray1);
                    object.put("list", ja);
                    jsonArray.add(object);
                }
                jsonObject1.put("cubics", jsonArray);

            } else {
                jsonObject1.put("cubics", cubics);
            }

        }
        if (StringUtil.isBlank(commd) || list.contains(MaterialTypeEnum.MATERIAL_AUTO.getCode())) {
            JSONArray autos = InstrumentMaterialGlobal.getAutoMaterialList();
            //将map封装成数组
            for (int i = 0; i < autos.size(); i++) {
                AutoMaterialModel a = (AutoMaterialModel) autos.get(i);
                if (a.getSampleEpList() != null) {
                    a.setSampleEpListCount(a.getSampleEpList().size() + "");
                }
                //属性值推送
                Map<String,String> map1 =new HashMap<>();
                map1.put("count", a.getSampleEpListCount());
                map1.put("channel", "");
                map1.put("time", a.getStartTime());
                JSONArray jsonArray1 = putProToMe(map1,a.getSendSxType());
                a.setSxList(jsonArray1);
                JSONArray jsonArray = new JSONArray();
                if (a.getOctalianpipeList() != null && a.getOctalianpipeList().getMap() != null) {
                    changeMapToJsonarry(a.getOctalianpipeList());
                }
            }

            jsonObject1.put("autos", autos);
        }
        if (StringUtil.isBlank(commd) || list.contains(MaterialTypeEnum.MATERIAL_PCR.getCode())) {
            JSONArray pcrs = InstrumentMaterialGlobal.getPcrMaterialList();
            for (int i = 0; i < pcrs.size(); i++) {
                PcrMaterialModel a = (PcrMaterialModel) pcrs.get(i);
                JSONArray jsonArray = new JSONArray();
                if (a.getOctalianpipeList() != null && a.getOctalianpipeList().getMap() != null) {
                    changeMapToJsonarry(a.getOctalianpipeList());
                    a.setOctalianpipeListCount(a.getOctalianpipeList().getToSendMap().size() + "");
                }
                //属性值推送
                Map<String,String> map1 =new HashMap<>();
                map1.put("count", a.getOctalianpipeListCount());
                map1.put("channel", "");
                map1.put("time", a.getStartTime());
                JSONArray jsonArray1 = putProToMe(map1,a.getSendSxType());
                a.setSxList(jsonArray1);
            }
            jsonObject1.put("pcrs", pcrs);
        }
        if (StringUtil.isBlank(commd) || list.contains(MaterialTypeEnum.MATERIAL_CMH.getCode())) {
            JSONArray chms = InstrumentMaterialGlobal.getChmMaterialList();
            for (int i = 0; i < chms.size(); i++) {
                ChmMaterialModel a = (ChmMaterialModel) chms.get(i);
                JSONArray jsonArray = new JSONArray();
                if (a.getMap() != null) {
                    for (String m : a.getMap().keySet()) {
                        List<OctalYbxxxModel> lo = a.getMap().get(m);
                        if (lo != null && lo.size() > 0) {
                            for (OctalYbxxxModel o : lo) {
                                o.setBh(m);
                                OctalYbxxxModel octalYbxxxModel = JSONObject.parseObject(JSONObject.toJSONString(o), OctalYbxxxModel.class);
                                jsonArray.add(octalYbxxxModel);
                            }
                        }
                    }
                }
                //属性值推送
                Map<String,String> map1 =new HashMap<>();
                map1.put("count", jsonArray.size() + "");
                map1.put("channel", "");
                map1.put("time", a.getStartTime());
                JSONArray jsonArray1 = putProToMe(map1,a.getSendSxType());
                a.setSxList(jsonArray1);
                a.setToSendMap(jsonArray);
                a.setOctalianpipeListCount(jsonArray.size() + "");
            }
            jsonObject1.put("chms", chms);

        }
        if (StringUtil.isBlank(commd) || list.contains(MaterialTypeEnum.MATERIAL_SEQ.getCode())) {
            JSONArray seqs = InstrumentMaterialGlobal.getSeqMaterialList();
            for (int i = 0; i < seqs.size(); i++) {
                SeqMaterialModel a = (SeqMaterialModel) seqs.get(i);
                JSONArray jsonArray = new JSONArray();
                if (a.getOctalianpipeList() != null && a.getOctalianpipeList().getMap() != null) {
                    changeMapToJsonarry(a.getOctalianpipeList());
                    a.setOctalianpipeListCount(a.getOctalianpipeList().getToSendMap().size() + "");
                }
                //属性值推送
                Map<String,String> map1 =new HashMap<>();
                map1.put("count", a.getOctalianpipeListCount() + "");
                map1.put("channel", "");
                map1.put("time", a.getStartTime());
                JSONArray jsonArray1 = putProToMe(map1,a.getSendSxType());
                a.setSxList(jsonArray1);
            }
            jsonObject1.put("seqs", seqs);

        }
        if (StringUtil.isBlank(commd) || list.contains(MaterialTypeEnum.MATERIAL_FMA.getCode())) {
            Map<Object, Object> fma = InstrumentMaterialGlobal.getFrontMaterialList();
            JSONObject jsonObject = new JSONObject();
            if (fma != null) {
                jsonObject.put("lx", fma.get("lx"));
                jsonObject.put("deviceid", fma.get("deviceid"));
                jsonObject.put("commanddeviceid", fma.get("commanddeviceid"));
                JSONArray gunArray = new JSONArray();
                JSONArray trayArray = new JSONArray();
                JSONArray epArray = new JSONArray();
                //推送Ep管的，获取wzList
                Map<String, EpVehicleModel> epVehicleModels = InstrumentMaterialGlobal.getWzEpMap(RedisStorageEnum.EP_MAP_WZ.getCode());
                if (epVehicleModels != null && epVehicleModels.size() > 0) {
                    for (String key : epVehicleModels.keySet()) {
                        EpVehicleModel epVehicleModel = epVehicleModels.get(key);
                        if (epVehicleModel.getQy() != null && epVehicleModel.getQy().equals(fma.get("qy"))) {
                            if (epVehicleModel.getEpList() == null) {
                                epVehicleModel.setZt("3");
                                epVehicleModel.setCountEpList("0");
                            } else {
                                epVehicleModel.setCountEpList(epVehicleModel.getEpList().size() + "");
                                epVehicleModel.setZt("0");
                            }
                            //属性值推送
                            Map<String,String> map1 =new HashMap<>();
                            map1.put("count", epVehicleModel.getCountEpList() + "");
                            map1.put("channel", "");
                            map1.put("time", "");
                            JSONArray jsonArray1 = putProToMe(map1,epVehicleModel.getSendSxType());
                            epVehicleModel.setSxList(jsonArray1);
                            epArray.add(epVehicleModel);
                        }
                    }
                }
                jsonObject.put("fma_ep", epArray);
                for (Object ob : fma.keySet()) {
                    if (ob != null && ob.toString().contains("fma_gunhead")) {
                        GunVehicleModel fma_gunhead = JSONObject.parseObject(fma.get(ob).toString(), GunVehicleModel.class);
                        if (fma_gunhead.getGunhead() == null) {
                            fma_gunhead.setZt("3");
                            fma_gunhead.setGunheadSize("0");
                        } else {
                            fma_gunhead.setZt("0");
                            fma_gunhead.setGunheadSize(fma_gunhead.getGunhead().size() + "");
                        }
                        //属性值推送
                        Map<String,String> map1 =new HashMap<>();
                        map1.put("count", fma_gunhead.getGunheadSize() + "");
                        map1.put("channel", "");
                        map1.put("time", "");
                        JSONArray jsonArray1 = putProToMe(map1,fma_gunhead.getSendSxType());
                        fma_gunhead.setSxList(jsonArray1);
                        gunArray.add(fma_gunhead);
                    }
                    jsonObject.put("fma_gunhead", gunArray);
                }
                if (fma.get("fma_tray") != null) {
                    List<TrayModel> fma_traynew = JSONObject.parseObject(fma.get("fma_tray").toString(), List.class);
                    List<TrayModel> fma_tray = getSerTrayList(fma_traynew);
                    jsonObject.put("fma_tray", fma_tray);
                } else {
                    jsonObject.put("fma_tray", "");
                }
            }
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(jsonObject);
            jsonObject1.put("fma", jsonArray);
        }
        if (StringUtil.isBlank(commd) || list.contains(MaterialTypeEnum.MATERIAL_OCP.getCode())) {
            Map<Object, Object> auto = InstrumentMaterialGlobal.getOCMaterialList();
            JSONObject jsonObject = new JSONObject();
            if (auto != null) {
                jsonObject.put("lx", auto.get("lx"));
                jsonObject.put("deviceid", auto.get("deviceid"));
                jsonObject.put("commanddeviceid", auto.get("commanddeviceid"));
                JSONArray autoDeskArray = new JSONArray();
                if (auto.get("auto_octalianpipe1") != null) {
                    OctalianpipeListModel ocp_octalianpipe = JSONObject.parseObject(auto.get("auto_octalianpipe1").toString(), OctalianpipeListModel.class);
                    changeMapToJsonarry(ocp_octalianpipe);
                    if (ocp_octalianpipe.getMap() == null) {
                        ocp_octalianpipe.setZt("3");
                        ocp_octalianpipe.setCountOctal("0");
                    } else {
                        ocp_octalianpipe.setCountOctal(ocp_octalianpipe.getToSendMap().size() + "");
                        ocp_octalianpipe.setZt("0");
                    }
                    //属性值推送
                    Map<String,String> map1 =new HashMap<>();
                    map1.put("count", ocp_octalianpipe.getCountOctal() + "");
                    map1.put("channel", "");
                    map1.put("time", "");
                    JSONArray jsonArray1 = putProToMe(map1,ocp_octalianpipe.getSendSxType());
                    ocp_octalianpipe.setSxList(jsonArray1);
                    autoDeskArray.add(ocp_octalianpipe);
                }
                if (auto.get("auto_octalianpipe2") != null) {
                    OctalianpipeListModel ocp_octalianpipe = JSONObject.parseObject(auto.get("auto_octalianpipe2").toString(), OctalianpipeListModel.class);
                    changeMapToJsonarry(ocp_octalianpipe);
                    if (ocp_octalianpipe.getMap() == null) {
                        ocp_octalianpipe.setZt("3");
                        ocp_octalianpipe.setCountOctal("0");
                    } else {
                        ocp_octalianpipe.setCountOctal(ocp_octalianpipe.getToSendMap().size() + "");
                        ocp_octalianpipe.setZt("0");
                    }
                    //属性值推送
                    Map<String,String> map1 =new HashMap<>();
                    map1.put("count", ocp_octalianpipe.getCountOctal() + "");
                    map1.put("channel", "");
                    map1.put("time", "");
                    JSONArray jsonArray1 = putProToMe(map1,ocp_octalianpipe.getSendSxType());
                    ocp_octalianpipe.setSxList(jsonArray1);
                    autoDeskArray.add(ocp_octalianpipe);
                }
                jsonObject.put("auto_desk_ocp", autoDeskArray);
                Map<String, EpVehicleModel> epVehicleModels = InstrumentMaterialGlobal.getWzEpMap(RedisStorageEnum.EP_MAP_WZ.getCode());
                JSONArray epArray = new JSONArray();
                if (epVehicleModels != null && epVehicleModels.size() > 0) {
                    for (String key : epVehicleModels.keySet()) {
                        EpVehicleModel epVehicleModel = epVehicleModels.get(key);
                        if (epVehicleModel.getQy() != null && epVehicleModel.getQy().equals(auto.get("qy"))) {
                            if (epVehicleModel.getEpList() == null) {
                                epVehicleModel.setZt("3");
                                epVehicleModel.setCountEpList("0");
                            } else {
                                epVehicleModel.setZt("0");
                                epVehicleModel.setCountEpList(epVehicleModel.getEpList().size() + "");
                            }
                            //属性值推送
                            Map<String,String> map1 =new HashMap<>();
                            map1.put("count", epVehicleModel.getCountEpList() + "");
                            map1.put("channel", "");
                            map1.put("time", "");
                            JSONArray jsonArray1 = putProToMe(map1,epVehicleModel.getSendSxType());
                            epVehicleModel.setSxList(jsonArray1);
                            epArray.add(epVehicleModel);
                        }
                    }

                }
                jsonObject.put("auto_desk_ep", epArray);

            }
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(jsonObject);
            jsonObject1.put("auto_desk", jsonArray);

        }
        if (StringUtil.isBlank(commd) || list.contains(MaterialTypeEnum.MATERIAL_AGV.getCode())) {
            Map<Object, Object> agv = InstrumentMaterialGlobal.getAgvMaterialList();
            JSONObject jsonObject = new JSONObject();
            if (agv != null) {
                jsonObject.put("lx", agv.get("lx"));
                jsonObject.put("deviceid", agv.get("deviceid"));
                jsonObject.put("commanddeviceid", agv.get("commanddeviceid"));
                jsonObject.put("position", agv.get("position"));
                jsonObject.put("state", agv.get("state"));
                jsonObject.put("clampingNum", agv.get("clampingNum"));
                JSONArray ocpArray = new JSONArray();
                JSONArray epArray = new JSONArray();
                JSONArray gunArray = new JSONArray();
                JSONArray trayArray = new JSONArray();
                if (agv.get("agv_octalianpipe") != null) {
                    OctalianpipeListModel agv_octalianpipe = JSONObject.parseObject(agv.get("agv_octalianpipe").toString(), OctalianpipeListModel.class);
                    changeMapToJsonarry(agv_octalianpipe);
                    if (agv_octalianpipe != null) {
                        agv_octalianpipe.setCountOctal(agv_octalianpipe.getToSendMap().size() + "");
                    }
                    //属性值推送
                    Map<String,String> map1 =new HashMap<>();
                    map1.put("count", agv_octalianpipe.getCountOctal() + "");
                    map1.put("channel", "");
                    map1.put("time", "");
                    JSONArray jsonArray1 = putProToMe(map1,agv_octalianpipe.getSendSxType());
                    agv_octalianpipe.setSxList(jsonArray1);
                    ocpArray.add(agv_octalianpipe);
                }
                jsonObject.put("agv_octalianpipe", ocpArray);
                if (agv.get("agv_tray1") != null) {
                    Map<String, JSONObject> agv_tray1 = JSONObject.parseObject(agv.get("agv_tray1").toString(), Map.class);
                    TrayModel trayModel1 = new TrayModel();
                    if (agv_tray1 != null) {
                        trayModel1.setTpbh("1");
                        List<YsybxxDto> agvsyybpModels = new ArrayList<>();
                        for (String a : agv_tray1.keySet()) {
                            YsybxxDto agvsyybpModel = JSONObject.parseObject(agv_tray1.get(a).toJSONString(), YsybxxDto.class);
                            agvsyybpModels.add(agvsyybpModel);
                        }
                        trayModel1.setBoxList(agvsyybpModels);
                        trayModel1.setCountTray(trayModel1.getBoxList().size() + "");
                        //属性值推送
                        Map<String,String> map1 =new HashMap<>();
                        map1.put("count", trayModel1.getCountTray() + "");
                        map1.put("channel", "");
                        map1.put("time", "");
                        JSONArray jsonArray1 = putProToMe(map1,trayModel1.getSendSxType());
                        trayModel1.setSxList(jsonArray1);
                        trayArray.add(trayModel1);
                    }
                }
                if (agv.get("agv_tray2") != null) {
                    Map<String, JSONObject> agv_tray2 = JSONObject.parseObject(agv.get("agv_tray2").toString(), Map.class);
                    TrayModel trayModel2 = new TrayModel();
                    if (agv_tray2 != null) {
                        trayModel2.setTpbh("1");
                        List<YsybxxDto> agvsyybpModels = new ArrayList<>();
                        for (String a : agv_tray2.keySet()) {
                            YsybxxDto agvsyybpModel = JSONObject.parseObject(agv_tray2.get(a).toJSONString(), YsybxxDto.class);
                            agvsyybpModels.add(agvsyybpModel);
                        }
                        trayModel2.setBoxList(agvsyybpModels);
                        trayModel2.setCountTray(trayModel2.getBoxList().size() + "");
                        //属性值推送
                        Map<String,String> map1 =new HashMap<>();
                        map1.put("count", trayModel2.getCountTray() + "");
                        map1.put("channel", "");
                        map1.put("time", "");
                        JSONArray jsonArray1 = putProToMe(map1,trayModel2.getSendSxType());
                        trayModel2.setSxList(jsonArray1);
                        trayArray.add(trayModel2);
                    }
                }
                jsonObject.put("agv_tray", trayArray);

                if (agv.get("agv_gunhead") != null) {
                    GunVehicleModel agv_gunhead = JSONObject.parseObject(agv.get("agv_gunhead").toString(), GunVehicleModel.class);
                    if (agv_gunhead.getGunhead() != null) {
                        agv_gunhead.setGunheadSize(agv_gunhead.getGunhead().size() + "");
                    } else {
                        agv_gunhead.setGunheadSize("0");
                    }
                    //属性值推送
                    Map<String,String> map1 =new HashMap<>();
                    map1.put("count", agv_gunhead.getGunheadSize() + "");
                    map1.put("channel", "");
                    map1.put("time", "");
                    JSONArray jsonArray1 = putProToMe(map1,agv_gunhead.getSendSxType());
                    agv_gunhead.setSxList(jsonArray1);
                    gunArray.add(agv_gunhead);
                }
                jsonObject.put("agv_gunhead", gunArray);
                //推送Ep管的，获取wzList
                Map<String, EpVehicleModel> epVehicleModels = InstrumentMaterialGlobal.getDeskEpMap(RedisStorageEnum.EP_MAP_DESK.getCode());
                if (epVehicleModels != null && epVehicleModels.size() > 0) {
                    for (String key : epVehicleModels.keySet()) {
                        EpVehicleModel epVehicleModel = epVehicleModels.get(key);
                        if (epVehicleModel.getEpList() == null) {
                            epVehicleModel.setZt("3");
                            epVehicleModel.setCountEpList("0");
                        } else {
                            epVehicleModel.setZt("0");
                            epVehicleModel.setCountEpList(epVehicleModel.getEpList().size() + "");
                        }  //属性值推送
                        Map<String,String> map1 =new HashMap<>();
                        map1.put("count", epVehicleModel.getCountEpList() + "");
                        map1.put("channel", "");
                        map1.put("time", "");
                        JSONArray jsonArray1 = putProToMe(map1,epVehicleModel.getSendSxType());
                        epVehicleModel.setSxList(jsonArray1);

                        epArray.add(epVehicleModel);
                    }
                }
                jsonObject.put("agv_ep", epArray);

            }
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(jsonObject);
            jsonObject1.put("agv", jsonArray);

        }
        jsonObject1.put("systemState",CommonChannelUtil.getSystemState());
        SseEmitterAllServer.batchSendMessage(JSONObject.toJSONString(jsonObject1, SerializerFeature.WriteMapNullValue), GlobalParmEnum.HTML_TYPE_ALL.getCode());
        logger.info(JSONObject.toJSONString(jsonObject1, SerializerFeature.WriteMapNullValue));

    }
    /**
     * 推送托盘信息到首页
     */
    public static void putTraySample() {
        JSONObject jsonObject = new JSONObject();
        //查询redis的数据
        List<TrayModel> list1 = FrontMaterialAreaGlobal.getFma_tray();

        if (list1 == null && list1.size() < 1) {
            logger.error("未找到托盘信息，请准本好托盘");
        }
        List<TrayModel> list = getSerTrayList(list1);
        JSONArray tpinfo = new JSONArray();
        for (TrayModel trayModel : list) {
            JSONArray ybs = new JSONArray();
            JSONObject tp = new JSONObject();
            String readOnlyTray = "false";
            List<String> agvsyyS = new ArrayList<>();
            if (trayModel.getBoxList() != null && trayModel.getBoxList().size() > 0) {
                for (YsybxxDto ag : trayModel.getBoxList()) {
                    if (ag != null && StringUtil.isNotBlank(ag.getTpnwzxh())) {
                        agvsyyS.add(ag.getTpnwzxh());
                        if (!VehicleStatusEnum.VEHICLE_CARDBOX_SAVE.equals(ag.getYbzt())) {
                            readOnlyTray = "true";
                        }
                        JSONObject yb = new JSONObject();
                        yb.put("cassettePosition", ag.getTpnwzxh());
                        yb.put("ysybid", ag.getYsybid());
                        yb.put("nbbh", ag.getNbbh());
                        yb.put("tcsjph", ag.getTcsjph());
                        yb.put("zbf", ag.getZbf());
                        yb.put("jkysjph", ag.getJkysjph());
                        yb.put("jth", ag.getJth());
                        ybs.add(yb);
                    }
                }
            }
            //先将样本放入
            for (int j = 1; j < 5; j++) {
                if (agvsyyS.contains(j + "")) {
                    continue;
                }
                JSONObject yb = new JSONObject();
                yb.put("cassettePosition", j);
                ybs.add(yb);
            }
            tp.put("global_status", readOnlyTray);
            tp.put("traynum", trayModel.getTpbh());
            tp.put("yb", ybs);
            tpinfo.add(tp);
        }
        jsonObject.put("tpinfo", tpinfo);

        SseEmitterAllServer.batchSendMessage(jsonObject.toJSONString(), GlobalParmEnum.HTML_TYPE_INPUT.getCode());
        logger.info(jsonObject.toString());
    }

    public static void procedureErr(String msg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isProcedureErr", "true");
        jsonObject.put("errmsg", msg);
        SseEmitterAllServer.batchSendMessage(jsonObject.toJSONString(), GlobalParmEnum.HTML_TYPE_ALL.getCode());
        logger.info(jsonObject.toString());
    }
    public static void changeMapToJsonarry(OctalianpipeListModel octalianpipeList) {
        //将map封装成数组
        JSONArray jsonArray = new JSONArray();
        if (octalianpipeList.getMap() != null) {
            for (String m : octalianpipeList.getMap().keySet()) {
                List<OctalYbxxxModel> lo = octalianpipeList.getMap().get(m);
                if (lo != null && lo.size() > 0) {
                    for (OctalYbxxxModel o : lo) {
                        o.setBh(m);
                        OctalYbxxxModel octalYbxxxModel = JSONObject.parseObject(JSONObject.toJSONString(o), OctalYbxxxModel.class);
                        jsonArray.add(octalYbxxxModel);
                    }
                }
            }
        }
        octalianpipeList.setToSendMap(jsonArray);
    }

    //推送时给托盘排序
    public static List<TrayModel> getSerTrayList(List<TrayModel> fma_traynew) {
        //托盘排序
        List<TrayModel> fma_tray = new ArrayList<>();
        for (Object tray1 : fma_traynew) {
            TrayModel tray = JSONObject.parseObject(JSONObject.toJSONString(tray1), TrayModel.class);
            if (tray.getBoxList() != null) {
                tray.setCountTray(tray.getBoxList().size() + "");
                //属性值推送
                Map<String,String> map1 =new HashMap<>();
                map1.put("count", tray.getCountTray() + "");
                map1.put("channel", "");
                map1.put("time", "");
                JSONArray jsonArray1 = putProToMe(map1,tray.getSendSxType());
                tray.setSxList(jsonArray1);

            }
            if (fma_tray.size() > 0 && StringUtil.isNotBlank(tray.getWz())) {
                int wz = Integer.parseInt(tray.getWz());
                for (int i = 0; i < fma_tray.size(); i++) {
                    TrayModel trayModel = fma_tray.get(i);
                    if (StringUtil.isNotBlank(trayModel.getWz())) {
                        int wzold = Integer.parseInt(trayModel.getWz());
                        if (wz < wzold) {
                            fma_tray.add(i, tray);
                            break;
                        }
                        if (i + 1 == fma_tray.size()) {
                            fma_tray.add(tray);
                            break;
                        }
                    } else {
                        fma_tray.add(i, tray);
                        break;
                    }

                }
            } else {
                fma_tray.add(tray);
            }

        }
        return fma_tray;
    }

    /**
     * 区分不同仪器的不同属性
     * @param map
     * @param qyMap

     * @return
     */
    private static  JSONArray putProToMe(Map<String,String> map,Map<String,String> qyMap){
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
    }
}

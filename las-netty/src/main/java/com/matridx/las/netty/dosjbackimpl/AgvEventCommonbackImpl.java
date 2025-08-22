package com.matridx.las.netty.dosjbackimpl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.enums.YesNotEnum;
import com.matridx.igams.common.redis.RedisStreamUtil;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.dosjback.EventCommonback;
import com.matridx.las.netty.enums.*;
import com.matridx.las.netty.global.CubsParmGlobal;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.material.AutoDesktopGlobal;
import com.matridx.las.netty.global.material.FrontMaterialAreaGlobal;

import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.service.svcinterface.IWkmxService;
import com.matridx.las.netty.service.svcinterface.IYblcsjService;
import com.matridx.las.netty.util.ChangeEpUtil;

import com.matridx.las.netty.util.GetWLNumUtil;
import com.matridx.las.netty.util.SendMessgeToHtml;
import com.matridx.las.netty.util.sseemitter.SseEmitterAllServer;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 事件回调处理
 */
@Service
public class AgvEventCommonbackImpl implements EventCommonback {
	static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    @Autowired
    private IYblcsjService yblcsjService;
    @Autowired
    private IWkmxService wkmxService;
    @Autowired
    private  RedisStreamUtil streamUtil;
    @Autowired
    private GetWLNumUtil getWLNumUtil;

    @Override
    public JSONObject handBack(SjlcDto sjlcDto, FrameModel frameModel, Map<String,String> ma) {
        JSONObject resJsonObject =new JSONObject();
        FrameModel nextModel = null;
        try {
            log.info(String.format("机器人事件回调，回调事件为%s,回调命令%s",sjlcDto.getSjid(),frameModel.getCmd()));
            //事件回调处理类型
            String callbackType = sjlcDto.getSjhtcllx();
            YblcsjDto yblcsjDto =new YblcsjDto();
            SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );

            resJsonObject.put("res","true");
            resJsonObject.put("special","false");


            SendBaseCommand sendBaseCommand = new SendBaseCommand();

            OctalianpipeListModel octalianpipeListModelNew = new OctalianpipeListModel();

            if(StringUtils.isNoneBlank(callbackType)) {

                switch(callbackType) {
                    case "sfksAuto":
                        Map<String,Object> checkMap=getWLNumUtil.getWlNum("startAuto","");
                        if (!Boolean.valueOf(checkMap.get("state").toString())){
                            getWLNumUtil.sendMessage(checkMap.get("msg").toString());
                            SendMessgeToHtml.procedureErr(checkMap.get("msg").toString());
                            InstrumentMaterialGlobal.getAutoBlockQueue().take();
                        }
                        break;
                    case "sfksCmh":
                        String deviceId_cmh =ma.get(sjlcDto.getZyyqlx());
                        Map<String,Object> checkMap_cmh=getWLNumUtil.getWlNum("startCmh",deviceId_cmh);
                        if (!Boolean.valueOf(checkMap_cmh.get("state").toString())){
                            getWLNumUtil.sendMessage(checkMap_cmh.get("msg").toString());
                            InstrumentMaterialGlobal.getAutoBlockQueue().take();
                        }
                        break;
                    //放置机器人上的八连管载具
                    case "takeEpClaw":
                        InstrumentMaterialGlobal.setEpClaw("");
                        break;
                    case "dropEpClaw":
                        InstrumentMaterialGlobal.setEpClaw("");
                        InstrumentMaterialGlobal.setAvgQy("");
                        break;
                    case "commChange_drop":
                        InstrumentMaterialGlobal.setEpClaw(GoodsEnum.EP_EIGHT_CLAW.getCode());
                        List<String> list=new ArrayList<>();
                        List<String> agvList=new ArrayList<>();
                        String param ;
                        String agvParam ;
                        if(StringUtil.isNotBlank(sjlcDto.getParam())){
                            param=sjlcDto.getParam();
                            list=Arrays.asList(param.split(","));
                        }
                        if(StringUtil.isNotBlank(sjlcDto.getAgvparam())){
                            agvParam=sjlcDto.getAgvparam();
                            agvList=Arrays.asList(agvParam.split(","));
                        }
                        Map<String,Object>map=ChangeEpUtil.changeEpCommon_drop(list,agvList);
                        if(map!=null){
                            InstrumentMaterialGlobal.setAvgQy(map.get("qy").toString());
                        }

                        //如果是清空机器人台面，则会删除拿下去的样本信息
                        if(list.size()==0&&agvList.size()==0){
                            map.put("clear","true");
                        }
                        ChangeEpUtil.changeRedis_drop(map);
                        break;
                    case "commNull_drop":
                        InstrumentMaterialGlobal.setEpClaw(GoodsEnum.EP_EIGHT_CLAW.getCode());
                        Map<String,Object>map1=ChangeEpUtil.changeEpCommon_drop(null,null);
                        InstrumentMaterialGlobal.setAvgQy(map1.get("qy").toString());
                        ChangeEpUtil.changeRedis_drop(map1);
                        break;
                    case "commChange_take":
                        InstrumentMaterialGlobal.setEpClaw(GoodsEnum.EP_EIGHT_CLAW.getCode());
                        List<String> list1=null;
                        String param1;
                        if(StringUtil.isNotBlank(sjlcDto.getParam())){
                            param1=sjlcDto.getParam();
                            list1=Arrays.asList(param1.split(","));
                        }
                        String agvParam1;
                        List<String> agvList1=new ArrayList<>();
                        if(StringUtil.isNotBlank(sjlcDto.getAgvparam())){
                            agvParam1=sjlcDto.getAgvparam();
                            agvList1=Arrays.asList(agvParam1.split(","));
                        }
                        Map<String,Object>map2=ChangeEpUtil.changeEpCommon_take(list1,agvList1);
                        InstrumentMaterialGlobal.setAvgQy(map2.get("qy").toString());
                        ChangeEpUtil.changeRedis_take(map2);
                        break;
                    case "eight_agv":
                        OctalianpipeListModel octalianpipeListModel = AgvDesktopGlobal.getAgv_octalianpipe();
                        OctalianpipeListModel octalianpipeListModel1 = AutoDesktopGlobal.getauto_octalianpipe1();
                        if (null == octalianpipeListModel1.getMap()){
                            octalianpipeListModel.setOctbNum(0);
                            AutoDesktopGlobal.setauto_octalianpipe1(octalianpipeListModel,true);
                            octalianpipeListModelNew.setMap(null);
                            octalianpipeListModelNew.setNum(0);
                            AgvDesktopGlobal.setAgv_octalianpipe(octalianpipeListModelNew,true);
                        }
                        break;
                    //放置机器人上的八连管载具
                    case "eight2_agv":
                        OctalianpipeListModel octalianpipe = AgvDesktopGlobal.getAgv_octalianpipe();
                        OctalianpipeListModel octalianpipeListModel2 = AutoDesktopGlobal.getauto_octalianpipe2();
                        if (null == octalianpipeListModel2.getMap()){
                            AutoDesktopGlobal.setauto_octalianpipe2(octalianpipe,true);
                            octalianpipeListModelNew.setMap(null);
                            AgvDesktopGlobal.setAgv_octalianpipe(octalianpipeListModelNew,true);
                        }
                        break;
                    //放置机器人上的八连管载具
                    case "ep_agv":
                        EpVehicleModel epVehicleModel_q = FrontMaterialAreaGlobal.getFma_ep3();
                        List<AgvEpModel> maps2 = epVehicleModel_q.getEpList() ;
                        if (null == maps2){
                            EpVehicleModel epVehicleModel1 = AgvDesktopGlobal.getAgv_ep3();
                            epVehicleModel_q.setEpList(epVehicleModel1.getEpList());
                            FrontMaterialAreaGlobal.setFma_ep3(epVehicleModel_q,true);
                            epVehicleModel1.setEpList(null);
                            AgvDesktopGlobal.setAgv_ep3(epVehicleModel1,true);
                        }
                        break;
                    //放置机器人上的八连管载具
                    case "ep2_agv":
                        EpVehicleModel epVehicleModel = FrontMaterialAreaGlobal.getFma_ep2();
                        List<AgvEpModel> maps = epVehicleModel.getEpList() ;
                        if (null == maps){
                            EpVehicleModel epVehicleModel1 = AgvDesktopGlobal.getAgv_ep2();
                            epVehicleModel.setEpList(epVehicleModel1.getEpList());
                            FrontMaterialAreaGlobal.setFma_ep2(epVehicleModel,true);
                            epVehicleModel1.setEpList(null);
                            AgvDesktopGlobal.setAgv_ep2(epVehicleModel1,true);
                            FrameModel recModel = InstrumentStateGlobal.getInstrumentFinMap(ma.get(Command.AUTO.toString()));
                            sendBaseCommand.sendEventFlowlist(MedicalRobotProcessEnum.MEDICAL_ROBOT_PCR_START_TWO.getCode(),recModel);
                        }
                        break;
                    case "seq_agv":

                        break;
                    case "seq_auto":

                        break;
                    case "changeEP":
                        //操作redis AGV EP管载具
                        //机器人台面上的ep载具放到前物料区
                        EpVehicleModel epVehicleModel_3 = FrontMaterialAreaGlobal.getFma_ep3();

                        EpVehicleModel epVehicleModelA3 = AgvDesktopGlobal.getAgv_ep3();

                        epVehicleModelA3.setEpList(epVehicleModel_3.getEpList());




                        //建库仪后端物料放置到机器人台面上

                        //建库仪后端ep管载具置为null

                        AgvDesktopGlobal.setAgv_ep3(epVehicleModelA3,true);

                        epVehicleModel_3.setEpList(null);
                        FrontMaterialAreaGlobal.setFma_ep3(epVehicleModel_3,true);

                        EpVehicleModel epVehicleModel_2 = FrontMaterialAreaGlobal.getFma_ep2();
                        EpVehicleModel epVehicleModelA2 = AgvDesktopGlobal.getAgv_ep2();
                        epVehicleModelA2.setEpList(epVehicleModel_2.getEpList());
                        AgvDesktopGlobal.setAgv_ep2(epVehicleModelA2,true);
                        epVehicleModel_2.setEpList(null);
                        FrontMaterialAreaGlobal.setFma_ep2(epVehicleModel_2,true);

                /*    EpVehicleModel epVehicleModel_1 = FrontMaterialAreaGlobal.getFma_ep1();
                    EpVehicleModel epVehicleModelA1 = AgvDesktopGlobal.getAgv_ep1();
                    epVehicleModel_1.setEpList(epVehicleModelA1.getEpList());
                    FrontMaterialAreaGlobal.setFma_ep1(epVehicleModel_1,true);
                    epVehicleModelA1.setEpList(null);
                    AgvDesktopGlobal.setAgv_ep1(epVehicleModelA1,true);*/
                        break;
                    case "dosetHsEp":
                        EpVehicleModel hs1=AgvDesktopGlobal.getAgv_ep2();
                        EpVehicleModel hs2=FrontMaterialAreaGlobal.getFma_ep2();
                        hs2.setEpList(hs1.getEpList());
                        FrontMaterialAreaGlobal.setFma_ep2(hs2,true);
                        hs1.setEpList(null);
                        AgvDesktopGlobal.setAgv_ep2(hs1,true);
                        break;
                    case "setHsEp":
//                    EpVehicleModel hs1=AgvDesktopGlobal.getAgv_ep2();
//                    EpVehicleModel hs2=FrontMaterialAreaGlobal.getFma_ep2();
//                    hs2.setEpList(hs1.getEpList());
//                    FrontMaterialAreaGlobal.setFma_ep2(hs2);
//                    hs1.setEpList(null);
//                    AgvDesktopGlobal.setAgv_ep2(hs1);

                        CubsParmGlobal.setIsWorklc(false);
                        CubsParmGlobal.setIsAutoWork(true);
                        if(CubsParmGlobal.getCubFinQueuesSize()>0){
                            FrameModel startModel= CubsParmGlobal.getCubFinQueues();
                            sendBaseCommand.sendEventFlowlist("203", startModel);

                        }

                        break;
                    case "setHsEp1":
//                    EpVehicleModel hs_1=AgvDesktopGlobal.getAgv_ep2();
//                    EpVehicleModel hs_2=FrontMaterialAreaGlobal.getFma_ep2();
//                    hs_2.setEpList(hs_1.getEpList());
//                    hs_1.setEpList(null);
//                    AgvDesktopGlobal.setAgv_ep2(hs_1);
//                    FrontMaterialAreaGlobal.setFma_ep2(hs_2);
                        CubsParmGlobal.setIsStartAuto(false);
                        CubsParmGlobal.setIsWorklc(false);
                        CubsParmGlobal.setIsAutoWork1(false);
                        break;
                    case "getMoudel":
                        //释放建库仪
                        CubsParmGlobal.delQubinsFinQueue();
                        //CubsParmGlobal.cubFinQueues.poll();
                        String deviceId=ma.get(Command.CUBICS.toString());

                        Lock lock =new ReentrantLock();
                        try{
                            lock.lock();
                            if(CubsParmGlobal.getCubFinQueuesSize()>0){
                                nextModel= CubsParmGlobal.getCubFinQueues();
                            }

                        }catch (Exception e){
                            resJsonObject.put("res","false");
                            e.printStackTrace();
                        }finally {
                            lock.unlock();// 必须在finally中释放
                        }
                        //从建库仪中取出样本 操作redis
                       //InstrumentMaterialGlobal instrumentMaterialGlobal =new InstrumentMaterialGlobal();
                        //建库仪完成样本信息更新
                        CubsMaterialModel cubsMaterialModel =new CubsMaterialModel();
                        cubsMaterialModel.setPassId(deviceId);
                        cubsMaterialModel=InstrumentMaterialGlobal.getCubsMaterial(cubsMaterialModel);
                        String sample = "";
                        String ysybid = "";
                        if(cubsMaterialModel!=null){
                            ysybid =  cubsMaterialModel.getYsybid();
                            sample =  cubsMaterialModel.getSample();
                            cubsMaterialModel.setEpLeft(YesNotEnum.NOT.getValue());
                            cubsMaterialModel.setEpRight(YesNotEnum.NOT.getValue());
                            cubsMaterialModel.setGunheadLeft(YesNotEnum.NOT.getValue());
                            cubsMaterialModel.setGunheadRight(YesNotEnum.NOT.getValue());
                            cubsMaterialModel.setState(InstrumentStatusEnum.STATE_FREE.getCode());
                            cubsMaterialModel.setSample(null);
                            cubsMaterialModel.setYsybid(null);
                            InstrumentMaterialGlobal.setCubsMaterial(cubsMaterialModel,true);
                        }
                        //InstrumentStateGlobal.changeInstrumentState(frameModel.getCommand(),deviceId, InstrumentStatusEnum.STATE_FREE.getCode());

                        //桌面托盘数据更新

                        //EpVehicleModel _epVehicleModel_3 = AgvDesktopGlobal.getAgv_ep3();
                        //EpVehicleModel _epVehicleModel_2 = AgvDesktopGlobal.getAgv_ep2();
                        EpVehicleModel _epVehicleModel_3=ChangeEpUtil.getMap("ep3");
                        EpVehicleModel _epVehicleModel_2=ChangeEpUtil.getMap("ep2");
                        List<AgvEpModel> listMap3=null;
                        List<AgvEpModel> listMap2=null;
                        if(_epVehicleModel_3!=null){
                            listMap3=_epVehicleModel_3.getEpList()==null?new ArrayList<>():_epVehicleModel_3.getEpList();
                        }
                        if(_epVehicleModel_2!=null){
                            listMap2=_epVehicleModel_2.getEpList()==null?new ArrayList<>():_epVehicleModel_2.getEpList();
                        }
                        AgvEpModel agvEpModel = new AgvEpModel();
                        //FrameModel resModel=CubsParmGlobal.getCubFinMap(ma.get(Command.CUBICS.toString()));
                        //String msgInfo=resModel.getMsgInfo();
                        //msgInfo=msgInfo.replace("COMPLETE"," ").replace(resModel.getFrameID()," ");
                        //String[] msgArr=msgInfo.split(" ");
                        //JSONArray jsonArray = JSONObject.parseArray(msgInfo);
                        //JSONObject jsonObject =JSONObject.parseObject(jsonArray.get(0).toString());
                        //String ybbh = jsonObject.get("SampleName").toString();
                        if(listMap2!=null){
                            _epVehicleModel_2.setEpList(setList(listMap2,sample,ysybid));
                            //AgvDesktopGlobal.setAgv_ep2(_epVehicleModel_2,true);
                            ChangeEpUtil.setMap(_epVehicleModel_2,"ep2");
                        }

                        if(listMap3!=null){
                       /* map.put((listMap3.size()+1)+"",ybbh);
                        listMap3.add(map);*/
                            _epVehicleModel_3.setEpList(setList(listMap3,sample,ysybid));
                            //AgvDesktopGlobal.setAgv_ep3(_epVehicleModel_3,true);
                            ChangeEpUtil.setMap(_epVehicleModel_3,"ep3");
                        }
                        //更新yblcsj表中的数据，机器人拿出样本的时间
                        yblcsjDto.setJqrncybsj(sdf.format(new Date()) );
                        yblcsjDto.setYsybid(ysybid);
                        yblcsjService.updateYblcsjById(yblcsjDto);

                        //删除旧ma，从新获取ma
                        if(nextModel!=null){
                            String agvDeviceId=ma.get(Command.AGV.toString());
                            String cubisdeviceid = ma.get(Command.CUBICS.toString());
                            InstrumentStateGlobal.removeInstrumentUsetMap(ma);
                            //删除后将建库仪放入队列
                            if(StringUtils.isNotBlank(cubisdeviceid)){
                                InstrumentStateGlobal.putInstrumentQueuesToRedis(Command.CUBICS.toString(),cubisdeviceid);
                            }
                            ma=InstrumentStateGlobal.getInstrumentUsedListMap(nextModel.getCommand(),nextModel.getPassId());
                            //在这里添加判断是否有超过数量
                            InstrumentStateGlobal.setInstrumentUsedList_Map(ma,Command.AGV.toString(),agvDeviceId);
                            ma.put(Command.AGV.toString(),agvDeviceId);
                            resJsonObject.put("special","true");
                        }else{
                            //判断机器人有没有满
                            EpVehicleModel epVehicleModel1 = ChangeEpUtil.getMap("ep3");
                            List<AgvEpModel> listMap= null;
                            if(epVehicleModel1!=null){
                                listMap = epVehicleModel1.getEpList();
                            }

                            if((listMap!=null&&listMap.size()>=Integer.parseInt(GlobalParmEnum.AUTO_START_NUM.getCode()))) {
                                //判断有没有空闲的配置仪，没有的话，就释放机器人
                                List<MapRecord<String, Object, Object>> listjq= streamUtil.range("AutoGroup");
                                if(listjq==null||listjq.size()==0){

                                    String agvDeviceId=ma.get(Command.AGV.toString());
                                    if(StringUtils.isNotBlank(agvDeviceId)) {
                                        ma.remove(Command.AGV.toString());
                                        InstrumentStateGlobal.removeInstrumentUsedList_Map(ma, Command.AGV.toString(), agvDeviceId);
                                        //放入空闲队列
                                        InstrumentStateGlobal.putInstrumentQueuesToRedis(Command.AGV.toString(),agvDeviceId);
                                    }

                                }

                            }
                        }

                        //放入到agv托盘redis中
                        //放入到agv托盘redis中
                        break;
                    case "setAuto":
                        //操作redis AGV EP管载具
                        //配置仪器放料后机器人台面的文库载具清空
                        EpVehicleModel epModel= ChangeEpUtil.getMap("ep3");
                        //List<AgvEpModel>mapList = AgvDesktopGlobal.getAgv_ep3().getEpList();
                        //设置仪器状态配置仪开始 redis数据更新
                        AutoMaterialModel autoMaterialModel = new AutoMaterialModel();
                        autoMaterialModel.setDeviceid(ma.get(Command.AUTO.toString()));
                        autoMaterialModel=InstrumentMaterialGlobal.getAutoMaterial(autoMaterialModel);
                        if(autoMaterialModel!=null){
                            if(epModel!=null){
                                autoMaterialModel.setSampleEpList(epModel.getEpList());
                            }
                            autoMaterialModel.setState(InstrumentStatusEnum.STATE_WORK.getCode());
                            InstrumentMaterialGlobal.setAutoMaterial(autoMaterialModel,true);
                        }
                        if(epModel!=null){
                            //放入配置仪后保存文库明细
                            wkmxService.saveWkmx(epModel.getEpList(),sjlcDto,ma);
                            //更新EP管
                            epModel.setEpList(null);
                            epModel.setIsnull(true);
                            epModel.setCsdm("");
                            ChangeEpUtil.setMap(epModel,"ep3");
                        }
                        CubsParmGlobal.setIsAutoWork(true);
                        break;
                    case "setAuto1":
                        //操作redis AGV EP管载具
                        //配置仪器放料后机器人台面的文库载具清空
                        EpVehicleModel epModel1= ChangeEpUtil.getMap("ep3");
                        //List<AgvEpModel>mapList = AgvDesktopGlobal.getAgv_ep3().getEpList();
                        //设置仪器状态配置仪开始 Auto redis数据更新
                        AutoMaterialModel autoMaterialModel1 = new AutoMaterialModel();
                        autoMaterialModel1.setDeviceid(ma.get(Command.AUTO.toString()));
                        autoMaterialModel1=InstrumentMaterialGlobal.getAutoMaterial(autoMaterialModel1);
                        if(autoMaterialModel1!=null){
                            if(epModel1!=null){
                                autoMaterialModel1.setSampleEpList(epModel1.getEpList());
                            }
                            autoMaterialModel1.setState(InstrumentStatusEnum.STATE_WORK.getCode());
                            InstrumentMaterialGlobal.setAutoMaterial(autoMaterialModel1,true);
                        }
                        if(epModel1!=null){
                            wkmxService.saveWkmx(epModel1.getEpList(),sjlcDto,ma);
                            epModel1.setEpList(null);
                            epModel1.setIsnull(true);
                            epModel1.setCsdm("");
                            ChangeEpUtil.setMap(epModel1,"ep3");
                        }
                        CubsParmGlobal.setIsAutoWork1(true);
                        break;
                    //开始建库仪流程
                    case "startJky":
                        //获取全局变量
                        Map<String , YsybxxDto> agv_tray1 = AgvDesktopGlobal.getAgv_tray1();
                        Map<String , YsybxxDto> agv_tray2 = AgvDesktopGlobal.getAgv_tray2();
                        boolean falg = true;
                        //判断AGV平台是否存在样本
                        if((agv_tray1!=null && agv_tray1.size()>0) || (agv_tray2!=null && agv_tray2.size()>0)) {
                            //判断是否存在空闲仪器
                            Long len = CubsParmGlobal.getCubquenLenth();
                            if(len!=null && len>0) {
                                //存在，发送放入建库仪命令
                                falg = false;
                                sendBaseCommand.sendEventFlowlist(MedicalRobotProcessEnum.MEDICAL_ROBOT_PUTMATERIAL_TOCUBICS.getCode(), frameModel);
                            }
                        }
                        if(falg) { //如果没有接下来得流程，就释放机器人
                            String agvDeviceId=ma.get(Command.AGV.toString());
                            if(StringUtils.isNotBlank(agvDeviceId)) {
                                ma.remove(Command.AGV.toString());
                                InstrumentStateGlobal.removeInstrumentUsedList_Map(ma, Command.AGV.toString(), agvDeviceId);
                                //放入空闲队列
                                InstrumentStateGlobal.changeInstrumentState(Command.AGV.toString(),agvDeviceId, RobotStatesEnum.ROBOT_ONLINE.getCode());
                                InstrumentStateGlobal.putInstrumentQueuesToRedis(Command.AGV.toString(),agvDeviceId);
                            }
                        }
                        break;

                    //开始建库仪流程
                    case "startJkyOne":
                        EpVehicleModel epVehicleModel1 = AgvDesktopGlobal.getAgv_ep1();
                        EpVehicleModel epVehicleModela2 = AgvDesktopGlobal.getAgv_ep2();
                        List<AgvEpModel> ep_vehicle1 = epVehicleModel1.getEpList();
                        List<AgvEpModel> ep_vehicle2 = epVehicleModela2.getEpList();
                        if(ep_vehicle1!=null && ep_vehicle1.size()>0 && ep_vehicle2!=null && ep_vehicle2.size()>0) {
                            //获取全局变量
                            Map<String, YsybxxDto> agv_tray1_t = AgvDesktopGlobal.getAgv_tray1();
                            Map<String, YsybxxDto> agv_tray2_t = AgvDesktopGlobal.getAgv_tray2();
                            //判断AGV平台是否存在样本
                            if ((agv_tray1_t != null && agv_tray1_t.size() > 0) || (agv_tray2_t != null && agv_tray2_t.size() > 0)) {
                                //判断是否存在空闲仪器
                                Long len = CubsParmGlobal.getCubquenLenth();
                                if(len!=null && len>0) {
                                    //存在，发送放入建库仪命令
                                    sendBaseCommand.sendEventFlowlist(MedicalRobotProcessEnum.MEDICAL_ROBOT_PUTMATERIAL_TOCUBICS.getCode(), frameModel);
                                }
                            }
                        }
                        break;

                    //判断是否归还机器人
                    case "sfghjqr":
                        //处理ma
                        Map<String,String> newMa = new HashMap<>();
                        //创建新的机器人map
                        newMa.put(Command.AGV.toString(), ma.get(Command.AGV.toString()));
                        //移除机器人id
                        InstrumentStateGlobal.removeInstrumentUsedList_Map(ma,Command.AGV.toString(),ma.get(Command.AGV.toString()));
                        //放入全局变量
                        InstrumentStateGlobal.setInstrumentUsedList(newMa);
                        break;
                    //是否执行将载具放置AGV平台流程
                    case "placeVehicle":
                        //获取全局变量
                        List<TrayModel> fma_tray = FrontMaterialAreaGlobal.getFma_tray();
                        if(fma_tray!=null && fma_tray.size()>0) {
                            for (TrayModel trayModel : fma_tray) {
                                //判断是否存在待放置托盘
                                if("1".equals(trayModel.getZt())) {
                                    //存在执行流程
                                    sendBaseCommand.sendEventFlowlist(MedicalRobotProcessEnum.MEDICAL_ROBOT_PLACE_CEHICLE.getCode(), null);
                                    break;
                                }
                            }

                        }
                        break;
                    case "isReturnTray":
                        //判断AGV是否还存在
                        Map<String, YsybxxDto> agv1 = AgvDesktopGlobal.getAgv_tray1();
                        Map<String, YsybxxDto> agv2 = AgvDesktopGlobal.getAgv_tray2();
                        if((agv1!=null && agv1.size()>0) || ( agv2 != null && agv2.size()>0)) {
                            Long len_t = CubsParmGlobal.getCubquenLenth();
                            if(len_t!=null && len_t>0) {
                                sendBaseCommand.sendEventFlowlist(MedicalRobotProcessEnum.MEDICAL_ROBOT_PUTMATERIAL_TOCUBICS.getCode(), frameModel);
                                break;
                            }else {
                                //没有空闲仪器，释放机器人
                                String agvDeviceId=ma.get(Command.AGV.toString());
                                if(StringUtils.isNotBlank(agvDeviceId)) {
                                    ma.remove(Command.AGV.toString());
                                    InstrumentStateGlobal.removeInstrumentUsedList_Map(ma, Command.AGV.toString(), agvDeviceId);
                                    //放入空闲队列
                                    InstrumentStateGlobal.changeInstrumentState(Command.AGV.toString(),agvDeviceId, RobotStatesEnum.ROBOT_ONLINE.getCode());
                                    InstrumentStateGlobal.putInstrumentQueuesToRedis(Command.AGV.toString(),agvDeviceId);
                                }
                                sendBaseCommand.sendEventFlowlist(MedicalRobotProcessEnum.MEDICAL_ROBOT_PUTMATERIAL_TOCUBICS.getCode(), null);
                                break;
                            }

                        }else {//如果没有物料，释放机器人
                            String agvDeviceId=ma.get(Command.AGV.toString());
                            if(StringUtils.isNotBlank(agvDeviceId)) {
                                ma.remove(Command.AGV.toString());
                                InstrumentStateGlobal.removeInstrumentUsedList_Map(ma, Command.AGV.toString(), agvDeviceId);
                                //放入空闲队列
                                InstrumentStateGlobal.changeInstrumentState(Command.AGV.toString(),agvDeviceId, RobotStatesEnum.ROBOT_ONLINE.getCode());
                                InstrumentStateGlobal.putInstrumentQueuesToRedis(Command.AGV.toString(),agvDeviceId);
                            }
                        }
                        break;
                }
            }else{
                log.info(sjlcDto.getSjid());
            }
        }catch (Exception e){
            log.error(String.format("AGV事件回调事件检查错误%s", e.getMessage()));
            log.error(String.format("出错流程类型id%s", sjlcDto.getLclx()));
            log.error(String.format("出错事件id%s", sjlcDto.getSjid()));
        }

        resJsonObject.put("model",nextModel);
        return resJsonObject;
    }

    //根据数量生产auto需要的位置
//    private int[] getXY(int i) {
//        int[] xy = new int[2];
//        xy[0] = (int) Math.ceil(Double.valueOf(i) / 8);
//        xy[1] = (i % 8) == 0 ? 8 : (i % 8);
//        return xy;
//    }
    
//    private List<String> putGunHeadVehicle(){
//    	List<String> agv_gunhead = new ArrayList<String>();
//    	for(int i = 0; i <40 ; i++) {
//    		agv_gunhead.add(String.valueOf(i));
//    	}
//    	return agv_gunhead;
//    }

    /**
     * EP管放入数据
     * @param listMap Ep管List
     * @param sample 样本编号
     * @param ysybid 原始样本id
     * @return 返回Ep管List
     */
    public List<AgvEpModel> setList(List<AgvEpModel>listMap,String sample,String ysybid){
        AgvEpModel agvEpModel1 = new AgvEpModel();
        agvEpModel1.setBlank(false);
        int m =  listMap.size();
        int xzb = (int) Math.floor(m/8)+1;
        int yzb = (m+1)%8;
        if(yzb==0){
            yzb = 8;
        }
        agvEpModel1.setXzb(xzb+"");
        agvEpModel1.setYzb(yzb+"");
        agvEpModel1.setNbbh(sample);
        agvEpModel1.setYsybid(ysybid);
        listMap.add(agvEpModel1);
        return listMap;
    }
}

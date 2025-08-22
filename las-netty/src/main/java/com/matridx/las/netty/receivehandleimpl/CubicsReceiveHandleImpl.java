package com.matridx.las.netty.receivehandleimpl;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.fastjson.JSON;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.enums.GlobalParmEnum;
import com.matridx.las.netty.enums.GoodsEnum;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.material.FrontMaterialAreaGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.service.svcinterface.*;
import com.matridx.las.netty.util.ChangeEpUtil;
import net.sf.json.JsonConfig;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.channel.domain.CommandDetails;
import com.matridx.las.netty.channel.server.handler.MatridxProtocolHandler;
import com.matridx.las.netty.global.CubsParmGlobal;

@Service
public class CubicsReceiveHandleImpl extends BaseReceiveHandleImpl {

    @Autowired
    private IJkywzszService jqrcsszService;
    @Autowired
    private IWkmxService wkmxService;
    @Autowired
    private IWkglService wkglService;
    @Value("${matridx.jcdw:}")
    private String jcdw;
    private String epsize = GlobalParmEnum.EP_TOTAL_NUM.getCode();


    private Logger log = LoggerFactory.getLogger(MatridxProtocolHandler.class);
    int i = 1;

    private String tdh;

    public boolean receiveHandle(FrameModel recModel) {
        log.info(String.format("建库仪接收消息，命令为%s,设备id%s",recModel.getCmd(),recModel.getDeviceID()));
        // 建库仪完成工作后主动给机器发消息，如果是成功，则启动下一个小步骤
        if (recModel.getMsgType().equals(CommandDetails.CUBICS_COMPLETE)&&recModel.getProcessStage().equals(CommandDetails.CUBICS_PROCESSSTAGE_EMPTY)) {
/*            Map<String,String> map=new HashMap<>();

             CubsMaterialModel cubsMaterialModel =new CubsMaterialModel();
             cubsMaterialModel.setPassId(recModel.getPassId());
             cubsMaterialModel.setYsybid(com.matridx.springboot.util.base.StringUtil.generateUUID());
       if(recModel.getPassId().equals("2")){
                cubsMaterialModel.setWzszid("7406B6A4DA004998ABE3310CB409B630");
                cubsMaterialModel.setYsybid("D55E0C92240F499DAF49607FE5E262E7");
                cubsMaterialModel.setSample("X123444");
            }else if(recModel.getPassId().equals("3")){
                cubsMaterialModel.setWzszid("A716D997804845449BD4EA6CD1A6B0B4");
                cubsMaterialModel.setSample("X123");
                cubsMaterialModel.setYsybid("37691D7B95B24B7C8022C30B6A8CF163");
            }else if(recModel.getPassId().equals("4")){
                cubsMaterialModel.setWzszid("FC3E970DF11C437E85A4DA9A2B7C3DBA");
                cubsMaterialModel.setSample("X12355");
                cubsMaterialModel.setYsybid("022366C438AC415E8D1748F1424E41F8");
            }
            map.put("1","2");
         InstrumentStateGlobal.setInstrumentUsedList(map);
         InstrumentMaterialGlobal.setCubsMaterial(cubsMaterialModel,true);*/
           CubsParmGlobal.setCubFinMap(recModel.getPassId(),recModel);
            Lock lock =new ReentrantLock();
            try{
                lock.lock();
                if(CubsParmGlobal.getCubFinQueuesSize()>0&&CubsParmGlobal.getIisIsWorklc()){
                    //CubsParmGlobal.cubFinQueues.add(recModel);
                    CubsParmGlobal.setCubFinQueues(recModel);
                    return true;
                }else{
                    CubsParmGlobal.setCubFinQueues(recModel);
                    //CubsParmGlobal.cubFinQueues.add(recModel);
                    SendBaseCommand sendBaseCommand = new SendBaseCommand();
                    CubsParmGlobal.setIsWorklc(true);
                    sendBaseCommand.sendEventFlowlist("203", recModel);
                }

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();// 必须在finally中释放
            }


        }
        //默认建库仪通道添加

        if (recModel.getMsgType().equals(CommandDetails.CUBICS_DEFAULT)) {
            String[] parm = recModel.getCmdParam();
            if (parm != null && parm.length > 0) {
                //JSONArray jsonArray = JSON.parseArray(parm[0]);
                //net.sf.json.JSONArray.toList(net.sf.json.JSONArray.fromObject(jsonArray),new CubisUpParamModel(),new JsonConfig());
                //改为josnArayy
                if (StringUtil.isNotBlank(parm[0])) {
                    List<CubisUpParamModel> list = (List<CubisUpParamModel>)JSON.parseArray(parm[0],CubisUpParamModel.class);
                    //先检查下是否有重复得通道号
                    List<String> listCus = new ArrayList<>();
                    if(list!=null&&list.size()>0) {
                        for(CubisUpParamModel c:list) {
                            if(StringUtil.isBlank(c.getCubsChannel())){
                                log.error("存在不符合格式得通道数据:"+list.toString());
                                return false ;
                            }
                            if (listCus.contains(c.getCubsChannel())) {
                                listCus.add(c.getCubsChannel());
                                log.error("存在重复得通道号");
                                return false ;
                            } else {
                                listCus.add(c.getCubsChannel());
                            }
                        }
                        //CubsParmGlobal.putListCubQueues(list, recModel.getDeviceID());
                        CubsParmGlobal.putListCubQueuesToRedis(list, recModel.getDeviceID());
                    }
                }
            }
        }
        //建库仪通道上报
        if (recModel.getMsgType().equals(CommandDetails.CUBICS_STATUS)) {
            String[] parm = recModel.getCmdParam();
            if (parm != null && parm.length > 0) {
                //改为josnArayy
                if (StringUtil.isNotBlank(parm[0])) {
                    List<CubisUpParamModel> list = (List<CubisUpParamModel>)JSON.parseArray(parm[0], CubisUpParamModel.class);
                    CubsParmGlobal.setListCubQueues(list, recModel.getDeviceID());
                }
            }
        }
        log.info("建库仪接受消息：" + recModel.getMsgInfo());
        return true;
    }

    public String[] dropClaw(MlpzDto mlpzDto, SjlcDto sjlcDto, Map<String,String> ma){
        String[] strArr = new String[2];
        String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
        switch (param){
            case "takeChangeEp_agv":
                strArr[0]= GoodsEnum.EP_EIGHT_CLAW.getCode();
                break;
        }
        return strArr;
    }

    public String[] commChange(MlpzDto mlpzDto, SjlcDto sjlcDto, Map<String,String> ma){
        String[] strArr = new String[2];
        String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
        switch (param){
            case "takeChangeEp":
                strArr[0]=GoodsEnum.EP_EIGHT_CLAW.getCode();
                break;
        }
        return strArr;
    }
    // 机器人移动到某个位置
    public String[] moveMethod(MlpzDto mlpzDto, SjlcDto sjlcDto, Map<String,String> ma) {
        String[] strArr = new String[2];
        String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
        switch (param) {
            // 去前料区
            case "setFeed_agv":
                strArr[0] = GoodsEnum.QWLQ.getCode();
                break;
            case "goCubics_agv":
                String deviceID = ma.get(Command.CUBICS.toString());
                strArr[0] = getjkypcwz(deviceID);
                break;
            // 去前建库仪后端
            case "goAuto_agv":
                strArr[0] = GoodsEnum.HWLQ.getCode();
                break;

        }
        return strArr;
    }

    // 拍照方法
    public String[] picMethod(MlpzDto mlpzDto, SjlcDto sjlcDto, Map<String,String> ma) {
        String[] strArr = new String[2];
        String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
        switch (param) {
            // 1-卡盒载具来料拍照位置
            case "setFeed_agv":
                strArr[0] = GoodsEnum.QWLQ.getCode();
                break;
            case "goCubics_agv":
                String deviceID = ma.get(Command.CUBICS.toString());
                strArr[0] = getjkypcwz(deviceID);
                strArr[1] = GoodsEnum.MOVE_PIC.getCode();
                break;
            // 去前建库仪后端
            case "goAuto_agv":
                strArr[0] = "26";
        }
        return strArr;
    }

    // 过渡点
    public String[] setTransition(MlpzDto mlpzDto, SjlcDto sjlcDto, Map<String,String> ma) {
        String[] strArr = new String[2];
        String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
        switch (param) {
            // 1-卡盒载具来料工作台过渡点
            case "setFeed_agv":
                strArr[0] =GoodsEnum.QWLQ.getCode();
                break;
            case "goCubics_agv":
                String deviceID = ma.get(Command.CUBICS.toString());
                //strArr[0] = getgdd(deviceID);
                break;
            // 去前建库仪后端
            case "goAuto_agv":
                strArr[0] = "4";
        }
        return strArr;
    }

    // 更换ep管方法
    public String[] changePe(MlpzDto mlpzDto, SjlcDto sjlcDto, Map<String,String> ma) {
        String[] strArr = new String[2];
        String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
        switch (param) {
            // 1-卡盒载具来料拍照位置
            case "setEp_agv":
                strArr[0] = "5";// 从redis中获取
                break;
            // 1-卡盒载具来料拍照位置
            case "getEp_agv":
                strArr[0] = "6";// 从redis中获取
                break;

        }
        return strArr;
    }

    // 建库仪开门方法
    public String[] openDoor(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
        String[] strArr = new String[2];
        // 根据通道号获取对应位置
        String deviceID = ma.get(Command.CUBICS.toString());
        strArr[0] = getjkypcwz(deviceID);
        return strArr;
    }

    // 建库仪关门方法
    public String[] closeDoor(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
        String[] strArr = new String[2];
        // 根据通道号获取对应位置
        String deviceID = ma.get(Command.CUBICS.toString());
        strArr[0] = getjkypcwz(deviceID);
        return strArr;
    }

    public String[] clearCubis(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
        String[] strArr = new String[2];
        Map<String,String> map = new HashMap<>();
        // 根据通道号获取对应位置
        String deviceID = ma.get(Command.CUBICS.toString());
        map.put("PreparationMethod", ""); //制备法
        map.put("SampleName", ""); //内部编号
        map.put("ReagentBox", ""); //提纯试剂编号
        map.put("BuildReagent", ""); //建库试剂编号
        map.put("Biomarker", ""); //接头号
        map.put("CubsChannel",deviceID);
        strArr[0]=JSON.toJSONString(map);
        return strArr;
    }
    // 建库仪取料方法
    public String[] goTran(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
        String[] strArr = new String[2];
        String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx())? sjlcDto.getParamlx() : mlpzDto.getParamlx();
        String deviceID = ma.get(Command.CUBICS.toString());
        switch (param) {
            // 建库仪放料及开关门工作位置
            case "goTran_agv_1":
                strArr[0] = getjkywz(deviceID);
                break;
            // 建库仪开关门拍照位置
            case "pic_agv_1":
                strArr[0] = getjkywz(deviceID);
                strArr[1] = GoodsEnum.TAKE_PIC.getCode();
                break;
            // 建库仪取放料拍照位置
            case "pic_agv_2":
                strArr[0] = getjkypcwz(deviceID);
                break;
            // 左侧枪头
            case "getLeftTip_agv":
                strArr[0] = getjkywz(deviceID);
                strArr[1] = GoodsEnum.LEFT_TIP.getCode();
                break;
            // 左侧枪头
            case "getRightTip_agv":
                strArr[0] = getjkywz(deviceID);
                strArr[1] =  GoodsEnum.RIGHT_TIP.getCode();
                break;
            // 右侧ep管
            case "getRightEP_agv":
                strArr[0] = getjkywz(deviceID);
                strArr[1] = GoodsEnum.RIGHT_EP.getCode();
                break;
            // 左侧ep管
            case "getLeftEP_agv":
                strArr[0] = getjkywz(deviceID);
                strArr[1] = GoodsEnum.LEFT_EP.getCode();
                break;
            // 放入到台面载具 4-1#EP管载具
            case "setVehicle1_agv":
                EpVehicleModel ep2Model =ChangeEpUtil.getMap("ep2");
                strArr[0] = ep2Model.getWz();
                List<AgvEpModel> listMap2= ep2Model.getEpList();
                if(listMap2!=null){
                    strArr[1] = (Integer.valueOf(epsize)-listMap2.size()) + "";// 从redis中获取
                }else{
                    strArr[1] = Integer.valueOf(epsize)+"";
                }

                break;
            // 放入到台面载具 5-2#EP管载具
            case "setVehicle2_agv":
                EpVehicleModel ep3Model=ChangeEpUtil.getMap("ep3");
                strArr[0] = ep3Model.getWz();
                List<AgvEpModel> listMap3= ep3Model.getEpList();
                if(listMap3!=null) {
                    strArr[1] = (Integer.valueOf(epsize)-listMap3.size()) + "";// 从redis中获取
                }else {
                    strArr[1] = Integer.valueOf(epsize)+"";
                }
                break;
            // 5卡盒
            case "getCardBox_agv":
                strArr[0] = getjkywz(deviceID);
                strArr[1] = GoodsEnum.BOX_CIBS.getCode();
                break;
        }

        return strArr;
    }

    // 配置仪放料方法
    public String[] setAuto(MlpzDto mlpzDto, SjlcDto sjlcDto, Map<String,String> ma) {
        String[] strArr = new String[2];
        String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
        switch (param) {
            // 机器人放载具到建库仪后端台面
            case "epIndex_agv":
                strArr[0] = "4";// 从redis中获取
                break;
        }
        return strArr;
    }

    // 配置仪开始
    public JSONObject startAuto(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
        JSONObject jsonObject = new JSONObject();
        String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx())? sjlcDto.getParamlx() : mlpzDto.getParamlx();
        String deviceID = ma.get(mlpzDto.getXylx());
        Map<String,Object> wkMap=InstrumentStateGlobal.getLibaryInfMap(deviceID);
        WkmxDto dto =new WkmxDto();
        dto.setWkid(wkMap.get("wkid").toString());
        switch (param) {
            //清空返回信息

            case "autoStartOne":

                List<Map<String,String>>mapList=new ArrayList<>();
                List<WkmxDto> wklist=wkmxService.getWkmxList(dto);
                for(WkmxDto wkDto:wklist){
                    Map<String,String> map=new HashMap<>();
                    map.put("wz",wkDto.getWkxzb()+","+wkDto.getWkyzb());
                    map.put("ybbh",wkDto.getNbbh());
                    map.put("jcdw",jcdw);
                    mapList.add(map);
                }
//                EpVehicleModel model=AgvDesktopGlobal.getAgv_ep3();
//                model.setEpList(null);
//                AgvDesktopGlobal.setAgv_ep3(model);
                String back = "djcsy:1,wkid:" + wkMap.get("wkid").toString() + ",ispcr:1"  + ",djc:1";
                jsonObject.put("backdata", back);
                jsonObject.put("result", mapList);

                break;

        }
        return jsonObject;
    }

    /***
     * 根据通道号获取取放料
     * @param tdh
     * @return
     */
    public String getjkywz(String tdh){
        String wz="";
        JkywzszDto dto2=jqrcsszService.queryById(tdh);
        if(dto2!=null){
            wz=dto2.getJkywzbh();
        }
        return wz;
    }

    /***
     * 根据通道号获取开关门拍照位置
     * @param tdh
     * @return
     */
    public String getjkypcwz(String tdh){
        String wz="";
        JkywzszDto dto2=jqrcsszService.queryById(tdh);
        if(dto2!=null){
            wz=dto2.getJkykgmwzbh();
        }
        return wz;
    }


    
    // 建库仪开始方法
    public String[] cubicsStart(MlpzDto mlpzDto, SjlcDto sjlcDto, Map<String,String> ma) {
        String[] strArr = new String[2];
        String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
        switch (param) {
            case "cubicsStart":
       			//获取当前仪器管道id
    			String gdid_t= ma.get(Command.CUBICS.toString());
    			Map<String,String> map = new HashMap<String,String>();
    			map.put("CubsChannel", gdid_t); //通道号
    			Map<String , YsybxxDto> agv_tray1 = AgvDesktopGlobal.getAgv_tray1();
    			Map<String , YsybxxDto> agv_tray2 = AgvDesktopGlobal.getAgv_tray2();
				Map<String, YsybxxDto> agv_tray = new HashMap<String, YsybxxDto>();
				if(agv_tray1!=null && agv_tray1.size()>0) {
					agv_tray.putAll(agv_tray1);
				}
				if(agv_tray2!=null && agv_tray2.size()>0) {
					agv_tray.putAll(agv_tray2);
				}				
    			if(agv_tray!=null && agv_tray.size()>0) {
    				for (Entry<String, YsybxxDto> entry : agv_tray.entrySet()) {
						//如果存在
						if(StringUtil.isNotBlank(entry.getKey())) {
							//判断当前样本状态
							if("1".equals(entry.getValue().getYbzt())) {
								map.put("PreparationMethod", entry.getValue().getZbf()); //制备法
								map.put("SampleName", entry.getValue().getNbbh()); //内部编号
								map.put("ReagentBox", entry.getValue().getTcsjph()); //提纯试剂编号
								map.put("BuildReagent", entry.getValue().getJkysjph()); //建库试剂编号
								map.put("Biomarker", entry.getValue().getJth()); //接头号
							}
						}
			        }
    			}  			
                strArr[0] = JSON.toJSONString(map);
                break;
            case "cubicsStart2":
            	Map<String,String> map2 = new HashMap<String,String>();
            	map2.put("IsPrint", "true");
            	map2.put("PreparationMethod", "RNA"); //制备法
            	map2.put("SampleName", "Test"); //内部编号
            	map2.put("SubDeviceAdd", ""); //提纯试剂编号
            	map2.put("BuildReagent", "333"); //建库试剂编号
            	map2.put("CubsChannel", ma.get("1")); //通道号
            	strArr[0] = JSON.toJSONString(map2);
            	break;
            case "cubicsStart3":
                Map<String,String> map3 = new HashMap<String,String>();
                map3.put("IsPrint", "true");
                map3.put("PreparationMethod", "RNA"); //制备法
                map3.put("SampleName", "Test"); //内部编号
                map3.put("SubDeviceAdd", ""); //提纯试剂编号
                map3.put("BuildReagent", "333"); //建库试剂编号
                map3.put("CubsChannel", ma.get("1")); //通道号
                map3.put("IsPrint","true");
                strArr[0] = JSON.toJSONString(map3);
                break;

        }
        return strArr;
    }
}

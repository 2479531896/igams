package com.matridx.las.netty.receivehandleimpl;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.dosjbackimpl.EventFlowBack;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.material.AutoDesktopGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.service.svcinterface.IWkmxService;
import com.matridx.las.netty.service.svcinterface.IYblcsjService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.enums.MedicalRobotProcessEnum;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class AutoReceiveHandleImpl extends BaseReceiveHandleImpl {

    @Autowired
    private IYblcsjService yblcsjService;
    @Autowired
    private IWkmxService wkmxService;


    @Transactional //开始
    public boolean receiveHandle(FrameModel recModel) {

        JSONObject rejsonObject = JSONObject.parseObject(recModel.getCmdParam()[0]);
//		recModel.getCmdParam()[0] = "{\"Date\":\"2022/3/10 9:43:28\",\"Action\":\"Finish_2\",\"Result\": [{\"wz\":\"1,1\",\"ybbh\":\"123456\"}],\"backdata\":\"djcsy:2,wkid:5684421958C34FF194F60A05D967B603,ispcr:1,djc:2\"}";
//		recModel.getCmdParam()[0] = "{\"Date\":\"2022/3/10 9:43:28\",\"Action\":\"Finish_2\",\"Result\": [{\"wz\":\"1,1\",\"ybbh\":\"123456\"},{\"wz\":\"2,1\",\"ybbh\":\"123456\"},{\"wz\":\"3,1\",\"ybbh\":\"123456\"}],\"backdata\":\"djcsy:2,wkid:5684421958C34FF194F60A05D967B603,ispcr:1,djc:2\"}";
//		recModel.getCmdParam()[0] = "{\"Date\":\"2022/3/10 9:43:28\",\"Action\":\"Finish_1\",\"Result\": [{\"xwz\":\"1,1\",\"ywz\":\"1,1\",\"ybbh\":\"123456\"}],\"backdata\":\"djcsy:1,wkid:5684421958C34FF194F60A05D967B603,ispcr:1,djc:1\"}";

        // 取出数据
        handleStateMesg(recModel);
        if (rejsonObject.get("Action").toString().contains("Finish")) {
//			Map<String,String> map_hc = new HashMap<>();
//			map_hc.put("2",recModel.getDeviceID());
//			//map.put("2","001");
//			InstrumentStateGlobal.setInstrumentUsedList(map_hc);
//			Map<String,Object> map_pcr = new HashMap<>();
//			InstrumentStateGlobal.setLibaryInfMap("3",map_pcr);
            JSONObject backJson = setBackData(rejsonObject.get("backdata").toString()); // 先保存状态和动作流程
//			JSONObject backJson = setBackData("djcsy:2,wkid:FD6C48F2ABF34732ABFD14A1FEBACBCD,ispcr:1,djc:2"); // 先保存状态和动作流程
//			JSONObject backJson = setBackData("djcsy:1,wkid:FD6C48F2ABF34732ABFD14A1FEBACBCD,ispcr:1,djc:1"); // 先保存状态和动作流程

            // 配液仪器第一次完成配置,调用
            if (backJson.get("djcsy").toString().equals("1") || backJson.get("djcsy").toString().equals("2")) {
                if (rejsonObject.get("Action").toString().equals(InstrumentStatusEnum.AUTO_FINISH_ONE.getCode()) || rejsonObject.get("Action").toString().equals(InstrumentStatusEnum.AUTO_FINISH_TWO.getCode())) {
                    //将接受到的信息放到redis种
                    InstrumentStateGlobal.setInstrumentFinMap(recModel.getDeviceID(), recModel);
                    //处理压盖机内部八连管数量及数据
                    JSONArray jsonArray = JSONArray.parseArray(rejsonObject.get("Result").toString());
//					JSONArray jsonArray = JSONArray.parseArray("[{\"wz\":\"1,1\",\"ybbh\":\"123456\"}]");
//					JSONArray jsonArray = JSONArray.parseArray("[{\"wz\":\"1,1\",\"ybbh\":\"123456\"},{\"wz\":\"2,1\",\"ybbh\":\"123456\"},{\"wz\":\"3,1\",\"ybbh\":\"123456\"}]");
//					JSONArray jsonArray = JSONArray.parseArray("[{\"xwz\":\"1,1\",\"ywz\":\"1,1\",\"ybbh\":\"123456\"}]");
                    List<OctalYbxxxModel> octalYbxxxModels = new ArrayList<>();
                    List<WkmxDto> wkmxDtos = new ArrayList<>();
//					List<Map> maps = new ArrayList<>();
                    for (Object jo : jsonArray) {
                        if (jo != null) {
//							Map<String,String> map = new HashMap<>();
                            JSONObject jso = JSONObject.parseObject(jo.toString());
//							map.put("x",jso.get("wz").toString().split(",")[0]);
//							map.put("y",jso.get("wz").toString().split(",")[1]);
//							map.put("ybbh",jso.get("ybbh").toString());
//							map.put("yy",jso.get("yy").toString());
//							map.put("xsy",jso.get("xsy").toString());
//							map.put("jcdw",jso.get("jcdw").toString());
//							maps.add(map);
                            OctalYbxxxModel octalYbxxxModel = new OctalYbxxxModel();
                            if (backJson.get("djcsy").toString().equals("1")) {
                                octalYbxxxModel.setBlgxzb(jso.get("xwz").toString().split(",")[0]);
                                octalYbxxxModel.setBlgyzb(jso.get("xwz").toString().split(",")[1]);
                            } else {
                                octalYbxxxModel.setBlgxzb(jso.get("wz").toString().split(",")[0]);
                                octalYbxxxModel.setBlgyzb(jso.get("wz").toString().split(",")[1]);
                            }

                            octalYbxxxModel.setNbbh(jso.get("ybbh").toString());
                            octalYbxxxModels.add(octalYbxxxModel);
                            if (StringUtil.isNotBlank(backJson.get("wkid").toString()) && !"null".equals(backJson.get("wkid").toString()) && backJson.get("djcsy").toString().equals("1")) {
                                WkmxDto wkmxDto = new WkmxDto();
                                wkmxDto.setWkid(backJson.get("wkid").toString());
                                wkmxDto.setNbbh(octalYbxxxModel.getNbbh());
                                wkmxDto.setBlgxzb(octalYbxxxModel.getBlgxzb());
                                wkmxDto.setBlgyzb(octalYbxxxModel.getBlgyzb());
                                wkmxDtos.add(wkmxDto);
                            }
                        }
                    }
                    if (StringUtil.isNotBlank(backJson.get("wkid").toString()) && !"null".equals(backJson.get("wkid").toString())) {
                        WkmxDto wkmxDto = new WkmxDto();
                        wkmxDto.setWkid(backJson.get("wkid").toString());
                        wkmxDto.setNbbh(JSONObject.parseObject(jsonArray.get(0).toString()).get("ybbh").toString());
                        wkmxDto = wkmxService.getDaoInfo(wkmxDto);
                        if (null != wkmxDto && StringUtil.isNotBlank(wkmxDto.getYsybid())) {
                            YblcsjDto yblcsjDto = new YblcsjDto();
                            yblcsjDto.setYsybid(wkmxDto.getYsybid());
                            SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
                            boolean success;
                            if (backJson.get("djcsy").toString().equals("1")) {
                                yblcsjDto.setDycpyjssj(sdf.format(new Date()));
                                success = wkmxService.updateListByWkidAndNbbh(wkmxDtos);
                                if (!success) {
                                    log.error("修改文库明细PCR坐标失败！");
                                }
                            } else {
                                yblcsjDto.setDecpyjssj(sdf.format(new Date()));
                            }
                            success = yblcsjService.updateYblcsjById(yblcsjDto);
                            if (!success) {
                                log.error("样本实验流程配置仪结束时间修改失败！");
                            }
                        }
                    }
                    Map<String, List<OctalYbxxxModel>> newMap = new HashMap<>();
                    Map<String, List<OctalYbxxxModel>> map = octalYbxxxModels.stream().collect(Collectors.groupingBy(OctalYbxxxModel::getBlgxzb));
//                    Iterator<Map.Entry<String, List<OctalYbxxxModel>>> entries = map.entrySet().iterator();
//                    while (entries.hasNext()) {
//                        Map.Entry<String, List<OctalYbxxxModel>> entry = entries.next();
//                        List<OctalYbxxxModel> dtoList = entry.getValue();
//                        newMap.put(String.valueOf(Integer.parseInt(entry.getKey()) * 2), dtoList);
//                    }
                    for (Map.Entry<String, List<OctalYbxxxModel>> entry : map.entrySet()) {
                        List<OctalYbxxxModel> dtoList = entry.getValue();
                        newMap.put(String.valueOf(Integer.parseInt(entry.getKey()) * 2), dtoList);
                    }


                    OctalianpipeListModel octalianpipeListModel = new OctalianpipeListModel();
                    octalianpipeListModel.setOctNum(newMap.size());
                    octalianpipeListModel.setOctbNum(newMap.size());
                    if (backJson.get("djcsy").toString().equals("1")) {
                        octalianpipeListModel.setOctNum(octalianpipeListModel.getOctNum() + 1);
                        octalianpipeListModel.setOctbNum(octalianpipeListModel.getOctbNum() + 1);
                        newMap.put(String.valueOf(octalianpipeListModel.getOctNum() * 2 - 1), new ArrayList<>());
                    }
//					octalianpipeListModel.setOctalList(octalYbxxxModels);
                    octalianpipeListModel.setMap(newMap);
                    AutoMaterialModel autoMaterialModel = new AutoMaterialModel();
                    autoMaterialModel.setDeviceid(recModel.getDeviceID());
                    AutoMaterialModel autoMaterialModel1 = InstrumentMaterialGlobal.getAutoMaterial(autoMaterialModel);
                    octalianpipeListModel.setNum(autoMaterialModel1.getOctalianpipeList().getNum() - newMap.size());
                    autoMaterialModel1.setOctalianpipeList(octalianpipeListModel);
                    autoMaterialModel1.setStartTime("");
                    InstrumentMaterialGlobal.setAutoMaterial(autoMaterialModel1, true);

                    //添加阻隔，如果队列里还有建库仪，则等待

                    Map<String, String> ma = null;
                    if (Command.CUBICS.toString().equals(recModel.getCommand())) {
                        ma = InstrumentStateGlobal.getInstrumentUsedListMap(Command.CUBICS.toString(), recModel.getPassId());
                    } else {
                        ma = InstrumentStateGlobal.getInstrumentUsedListMap(recModel.getCommand(), recModel.getDeviceID());
                    }
                    new SendBaseCommand().sendEventFlowlist(MedicalRobotProcessEnum.MEDICAL_ROBOT_FROM_AUTO_AND_CMH.getCode(), recModel);

//					JSONObject jsonObject = new JSONObject();
//					Map<String, String> ma = InstrumentStateGlobal.getInstrumentUsedListMap(Command.AUTO.toString(),
//							recModel.getDeviceID());
//					String back = "djcsy:1,wkid:" + backJson.get("wkid") + ",ispcr:" + backJson.get("ispcr") + ",djc:1";
//					jsonObject.put("yqAndJqrUsedMap", ma);
//					JSONObject backJsonObject = new JSONObject();
//					backJsonObject.put("backdata", back);
//					jsonObject.put("startparam", backJsonObject);
//					List<FsgcmlDto> list = fsgcmlService
//							.getFsgcmlDtosByType(MedicalRobotProcessEnum.MEDICAL_ROBOT_MOVEMACHINETOCLOSING.getCode());
//					// 调用发送接口
//					SendBaseCommand sendBaseCommand = new SendBaseCommand();
//					sendBaseCommand.sendEventFlowlist(MedicalRobotProcessEnum.MEDICAL_ROBOT_MOVETOLIBRARY.getCode(),null);
                    //	sendBaseCommand.sendCmdSModelByProcessThread(true, list, jsonObject);
                } else {
                    // todo
                }
            }
//			// 第二次配液完成
//			if (backJson.get("djcsy").toString().equals("2")) {
//				if (rejsonObject.get("Action").toString().equals(InstrumentStatusEnum.AUTO_FINISH_TWO.getCode())) {
//					JSONObject jsonObject = new JSONObject();
//					Map<String, String> ma = InstrumentStateGlobal.getInstrumentUsedListMap(Command.AUTO.toString(),
//							recModel.getDeviceID());
//					if (ma != null && StringUtil.isNotBlank(ma.get(Command.CMH.toString()))) {
//						jsonObject.put("yqDeviceID", ma.get(Command.CMH.toString()));
//						jsonObject.put("yqAndJqrUsedMap", ma);
//						String back = "djcsy:2,wkid:" + backJson.get("wkid") + ",ispcr:" + backJson.get("ispcr") + ",djc:2";
//						JSONObject backJsonObject = new JSONObject();
//						backJsonObject.put("backdata", back);
//						jsonObject.put("startparam", backJsonObject);
//						// 第二次到压盖机
//						List<FsgcmlDto> list = fsgcmlService.getFsgcmlDtosByType(
//								MedicalRobotProcessEnum.MEDICAL_ROBOT_MOVEMACHINETOCLOSINGSECEND.getCode());
//						// 调用发送接口
//						SendBaseCommand sendBaseCommand = new SendBaseCommand();
//						//sendBaseCommand.sendCmdSModelByProcessThread(true, list, jsonObject);
//					} else {
//						// todo
//					}
//				} else {
//					// todo
//				}
//			}
            // 第三次配液完成
            if (backJson.get("djcsy").toString().equals("3")) {
                if (rejsonObject.get("Action").toString().equals(InstrumentStatusEnum.AUTO_FINISH_THREE.getCode())) {
                    JSONObject jsonObject = new JSONObject();
                    Map<String, String> ma = InstrumentStateGlobal.getInstrumentUsedListMap(Command.AUTO.toString(),
                            recModel.getDeviceID());
                    AutoMaterialModel startModel = new AutoMaterialModel();
                    startModel.setDeviceid(recModel.getDeviceID());
                    AutoMaterialModel startModel1 = InstrumentMaterialGlobal.getAutoMaterial(startModel);
                    startModel1.setState(InstrumentStatusEnum.STATE_FREE.getCode());
                    startModel1.setStartTime("");
                    InstrumentMaterialGlobal.setAutoMaterial(startModel1, true);
                    if (ma != null && StringUtil.isNotBlank(ma.get(Command.AUTO.toString()))) {
                        jsonObject.put("yqAndJqrUsedMap", ma);
                        String back = "djcsy:3,wkid:" + backJson.get("wkid") + ",ispcr:" + backJson.get("ispcr") + ",djc:3";
                        JSONObject backJsonObject = new JSONObject();
                        backJsonObject.put("backdata", back);
                        jsonObject.put("startparam", backJsonObject);
                        // 调用发送接口
                        SendBaseCommand sendBaseCommand = new SendBaseCommand();
                        //这里释放掉配置仪，测试用//todo
                        String deviceid =  recModel.getDeviceID();
                        InstrumentStateGlobal.changeInstrumentState(Command.AUTO.toString(),deviceid, InstrumentStatusEnum.STATE_FREE.getCode());
                        //yqztxxService.updateStYqztxx(yqid, InstrumentStatusEnum.STATE_FREE.getCode());
                        //InstrumentStateGlobal.putInstrumentQueues(sjlcDto.getGhyqlx(), ma.get(sjlcDto.getGhyqlx()));
                        InstrumentStateGlobal.removeInstrumentUsedList_Map(ma,Command.AUTO.toString(),deviceid);
                        InstrumentStateGlobal.putInstrumentQueuesToRedis(Command.AUTO.toString(),deviceid);
                        ma.remove(Command.AUTO.toString());
                        //sendBaseCommand.sendCmdSModelByProcessThread(true, list, jsonObject);
                    } else {
                        // todo
                    }
                }
            }
        }
        return true;
    }

    // 释放八连管方法
    public String[] dropEight(MlpzDto mlpzDto, SjlcDto sjlcDto, Map<String, String> ma) {
        String[] strArr = new String[2];
        String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
        switch (param) {
            // 从1-6中获取
            case "dropAuto_agv":
                strArr[0] = "2";
                AutoMaterialModel autoMaterialModel = new AutoMaterialModel();
                autoMaterialModel.setDeviceid(ma.get(Command.AUTO.toString()));
                AutoMaterialModel autoMaterialModel1 = InstrumentMaterialGlobal.getAutoMaterial(autoMaterialModel);
                if (autoMaterialModel1.getOctalianpipeList() != null) {
                    autoMaterialModel1.getOctalianpipeList().setOctNum(autoMaterialModel1.getOctalianpipeList().getOctNum() + 1);
                    strArr[1] = String.valueOf(autoMaterialModel1.getOctalianpipeList().getOctNum() * 2);
                } else {
                    OctalianpipeListModel octalianpipeListModel1 = new OctalianpipeListModel();
                    octalianpipeListModel1.setOctNum(octalianpipeListModel1.getOctNum() + 1);
                    strArr[1] = String.valueOf(octalianpipeListModel1.getOctNum() * 2);
                }
                //存入配置仪
                InstrumentMaterialGlobal.setAutoMaterial(autoMaterialModel1, true);
                break;
        }
        return strArr;
    }

    public String[] move(MlpzDto mlpzDto, SjlcDto sjlcDto, Map<String, String> ma) {
        String deviceID = ma.get(Command.AUTO.toString());
        String[] strArr = new String[2];
        String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
        switch (param) {
            // 从1-6中获取
            case "goAuto_agv":
                strArr[0] = InstrumentMaterialGlobal.getInstrumentWz(deviceID,Command.AUTO.toString());
                break;
        }
        return strArr;
    }

    public String[] pic(MlpzDto mlpzDto, SjlcDto sjlcDto, Map<String, String> ma) {
        String deviceID = ma.get(Command.AUTO.toString());
        String[] strArr = new String[2];
        String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
        switch (param) {
            // 从1-6中获取
            case "picAuto_agv":
                strArr[0] = InstrumentMaterialGlobal.getInstrumentWz(deviceID,Command.AUTO.toString());
                break;
        }
        return strArr;
    }

    // 抓取八连管方法
    public String[] takeEight(MlpzDto mlpzDto, SjlcDto sjlcDto, Map<String, String> ma) {
        String deviceID = ma.get(Command.AUTO.toString());
        String[] strArr = new String[2];
        String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
        switch (param) {
            // 从1-6中获取
            case "takeAuto_agv":
                AutoMaterialModel autoMaterialModel = new AutoMaterialModel();
                autoMaterialModel.setDeviceid(deviceID);
                AutoMaterialModel autoMaterialModel1 = InstrumentMaterialGlobal.getAutoMaterial(autoMaterialModel);
                if(autoMaterialModel1!=null){
                    strArr[0] = "2";
                    strArr[1] = String.valueOf(autoMaterialModel1.getOctalianpipeList().getOctNum() * 2);
                }
                break;
        }
        return strArr;
    }

    // 抓取载具方法
    public String[] takeVehicles(MlpzDto mlpzDto, SjlcDto sjlcDto, Map<String, String> ma) {
       //String deviceID = ma.get(mlpzDto.getXylx());
       String[] strArr = new String[2];
       String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
        switch (param) {
            // 八连管载具
           case "takeEight_agv":
                strArr[0] = "2";
               break;
            // EP管载具
           case "takeEp_agv":
                strArr[0] = "1";
                break;
			// 从1-6中获取
			case "takeWl_agv":
				OctalianpipeListModel octalianpipeListModel = AutoDesktopGlobal.getauto_octalianpipe2();
				if (null != octalianpipeListModel){
					strArr[0] = "4";
                    AutoDesktopGlobal.setauto_octalianpipe2(null,false);
					AgvDesktopGlobal.setAgv_octalianpipe(octalianpipeListModel,false);
				}
				break;
       }
       return strArr;
    }


//    public boolean saveWkxx(AutoResultModel mAutoResultModel) {
//
//        return true;
//    }

//    /***
//     * 修改事件流程auto开始时间
//     * @param index 第几次实验
//     * @return 返回结果
//     */
//    private boolean updateAutoStartTime(String index) {
//
//        YblcsjDto yblcsjDto = new YblcsjDto();
//        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
//        yblcsjDto.setYsybid("");
//        if (index.equals("1")) {
//            yblcsjDto.setJkykssj(sdf.format(new Date()));
//        } else if (index.equals("2")) {
//            yblcsjDto.setDecpykssj(sdf.format(new Date()));
//        }
//
//        //更新yblcsj表中的数据，配置仪开始时间
//        return yblcsjService.updateYblcsjById(yblcsjDto);
//    }
}

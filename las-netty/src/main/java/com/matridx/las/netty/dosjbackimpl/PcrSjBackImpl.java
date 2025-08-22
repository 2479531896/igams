package com.matridx.las.netty.dosjbackimpl;

import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.dosjback.EventCommonback;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.enums.MedicalRobotProcessEnum;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.service.svcinterface.IWkmxService;
import com.matridx.las.netty.service.svcinterface.IYblcsjService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.FrameModel;


@Service
public class PcrSjBackImpl implements EventCommonback {
	@Autowired
	private IWkmxService wkmxService;
	@Autowired
	private IYblcsjService yblcsjService;

	@Override
	public JSONObject handBack(SjlcDto sjlcDto, FrameModel frameModel, Map<String,String> ma) {
		JSONObject jsonObject = new JSONObject();
		try {
			log.info(String.format("pcr事件回调，回调事件为%s,回调命令%s",sjlcDto.getSjid(),frameModel.getCmd()));
			// TODO Auto-generated method stub
			String callbackType = sjlcDto.getSjhtcllx();
			String deviceId = ma.get(sjlcDto.getZyyqlx());
			String autoDeviceId = ma.get("2");

			jsonObject.put("res", "true");
			jsonObject.put("special", "false");
			if (StringUtils.isNoneBlank(callbackType)) {
				switch (callbackType) {
					//释放八连管到PCR
					case "eightMatter":
						if ("OK".equals(frameModel.getCmd())) {
							PcrMaterialModel pcrMaterialModel = new PcrMaterialModel();
							pcrMaterialModel.setDeviceid(deviceId);
							PcrMaterialModel pcrMaterialModel1 = InstrumentMaterialGlobal.getPcrMaterial(pcrMaterialModel);
							OctalianpipeListModel octalianpipeListModel = AgvDesktopGlobal.getAgv_octalianpipe();
							if(pcrMaterialModel1!=null){
								if (null != pcrMaterialModel1.getOctalianpipeList()&&null != pcrMaterialModel1.getOctalianpipeList().getMap()){
									if (null != octalianpipeListModel && octalianpipeListModel.getOctgNum() > 0){
										pcrMaterialModel1.getOctalianpipeList().getMap().put(String.valueOf(octalianpipeListModel.getOctgNum()*2-1),octalianpipeListModel.getMap().get(String.valueOf(octalianpipeListModel.getOctgNum()*2-1)));
										pcrMaterialModel1.getOctalianpipeList().setOctNum(pcrMaterialModel1.getOctalianpipeList().getOctNum()+1);
										pcrMaterialModel1.getOctalianpipeList().setOctgNum(pcrMaterialModel1.getOctalianpipeList().getOctgNum()+1);
//								pcrMaterialModel1.getOctalianpipeList().setOctalList(octalianpipeListModel.getOctalList());
										InstrumentMaterialGlobal.setPcrMaterial(pcrMaterialModel1,true);
										octalianpipeListModel.getMap().remove(String.valueOf(octalianpipeListModel.getOctgNum()*2-1));
										octalianpipeListModel.setOctgNum(octalianpipeListModel.getOctgNum() -1);
//								octalianpipeListModel.setOctbNum(octalianpipeListModel.getOctbNum() +1);
										AgvDesktopGlobal.setAgv_octalianpipe(octalianpipeListModel,true);
									}
								}else{
									if (null != octalianpipeListModel && octalianpipeListModel.getOctgNum() > 0){
										Map<String,List<OctalYbxxxModel>> map = new HashMap<>();
										map.put(String.valueOf(octalianpipeListModel.getOctgNum()*2-1),octalianpipeListModel.getMap().get(String.valueOf(octalianpipeListModel.getOctgNum()*2-1)));
										OctalianpipeListModel octalianpipeListModel1 = new OctalianpipeListModel();
										octalianpipeListModel1.setOctNum(1);
										octalianpipeListModel1.setOctgNum(1);
										octalianpipeListModel1.setMap(map);
//								octalianpipeListModel1.setOctalList(octalianpipeListModel.getOctalList());
										pcrMaterialModel1.setOctalianpipeList(octalianpipeListModel1);
										InstrumentMaterialGlobal.setPcrMaterial(pcrMaterialModel1,true);
										octalianpipeListModel.getMap().remove(String.valueOf(octalianpipeListModel.getOctgNum()*2-1));
										octalianpipeListModel.setOctgNum(octalianpipeListModel.getOctgNum() -1);
//								octalianpipeListModel.setOctbNum(octalianpipeListModel.getOctbNum() +1);
										AgvDesktopGlobal.setAgv_octalianpipe(octalianpipeListModel,true);
									}
								}
							}
						} else {
							jsonObject.put("res", "false");
						}
						break;
					//此处判断配置仪是否开关门成功
					case "openAndClose":
						if ("OK".equals(frameModel.getCmd())) {
							break;
						} else {
							jsonObject.put("res", "false");
						}
						break;
					case "dropEight":
						PcrMaterialModel pcrMaterialModel = new PcrMaterialModel();
						pcrMaterialModel.setDeviceid(ma.get(Command.PCR.toString()));
						PcrMaterialModel model = InstrumentMaterialGlobal.getPcrMaterial(pcrMaterialModel);
						if (model!=null&&model.getOctalianpipeList().getOctNum()>0) {
							OctalianpipeListModel octalianpipeListModel = model.getOctalianpipeList();
							Map<String,List<OctalYbxxxModel>>_map=octalianpipeListModel.getMap();
							_map.remove((model.getOctalianpipeList().getOctNum()*2-1)+"");
							octalianpipeListModel.setMap(_map);
							octalianpipeListModel.setOctNum(octalianpipeListModel.getOctNum()-1);
							octalianpipeListModel.setOctgNum(octalianpipeListModel.getOctgNum()-1);
							model.setOctalianpipeList(octalianpipeListModel);
							InstrumentMaterialGlobal.setPcrMaterial(model,true);
						}else  if(model!=null&&model.getOctalianpipeList().getOctNum()==0){
							OctalianpipeListModel octalianpipeListModel = model.getOctalianpipeList();
							octalianpipeListModel.setMap(null);
							octalianpipeListModel.setOctNum(0);
							octalianpipeListModel.setOctgNum(0);
							model.setOctalianpipeList(octalianpipeListModel);
							InstrumentMaterialGlobal.setPcrMaterial(model,true);
						}
						break;
					case "startPcr":
						if ("OK".equals(frameModel.getCmd())) {
							FrameModel recModel = InstrumentStateGlobal.getInstrumentFinMap(autoDeviceId);
							PcrMaterialModel startModel = new PcrMaterialModel();
							startModel.setDeviceid(deviceId);
							PcrMaterialModel model1 = InstrumentMaterialGlobal.getPcrMaterial(startModel);
							if(model1!=null){
								model1.setState(InstrumentStatusEnum.STATE_WORK.getCode());
								InstrumentMaterialGlobal.setPcrMaterial(model1,true);
							}
							JSONObject json = JSONObject.parseObject(recModel.getCmdParam()[0]);
							if (json.get("Action").toString().contains("Finish")) {
								JSONObject backJson = setBackData(json.get("backdata").toString()); // 先保存状态和动作流程
								// 配液仪器第一次完成配置,调用
								if (backJson.get("djcsy").toString().equals("2")) {
//								EpVehicleModel agv_ep3 = agvDesktopGlobal.getAgv_ep3();
									SendBaseCommand sendBaseCommand = new SendBaseCommand();
//								if (null == agv_ep3.getEpList()){
									sendBaseCommand.sendEventFlowlist(MedicalRobotProcessEnum.MEDICAL_ROBOT_PCR_START_TWO.getCode(),recModel);
//								} else{
//									sendBaseCommand.sendEventFlowlist(MedicalRobotProcessEnum.MEDICAL_ROBOT_AGV_EP_Q.getCode(),recModel);
//								}
								}
//							else if (backJson.get("djcsy").toString().equals("2")){
//								SendBaseCommand sendBaseCommand = new SendBaseCommand();
//								sendBaseCommand.sendEventFlowlist(MedicalRobotProcessEnum.MEDICAL_ROBOT_PCR_START_TWO.getCode(),recModel);
//							}
								if (StringUtil.isNotBlank(backJson.get("wkid").toString()) && !"null".equals(backJson.get("wkid").toString())){
									JSONArray jsonArray = JSONArray.parseArray(json.get("Result").toString());
									WkmxDto wkmxDto = new WkmxDto();
									wkmxDto.setWkid(backJson.get("wkid").toString());
									wkmxDto.setNbbh(JSONObject.parseObject(jsonArray.get(0).toString()).get("ybbh").toString());
									wkmxDto = wkmxService.getDaoInfo(wkmxDto);
									if (null != wkmxDto && StringUtil.isNotBlank(wkmxDto.getYsybid())){
										YblcsjDto yblcsjDto = new YblcsjDto();
										yblcsjDto.setYsybid(wkmxDto.getYsybid());
										SimpleDateFormat sdf = new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
										boolean success;
										if(backJson.get("djcsy").toString().equals("1")){
											yblcsjDto.setDycdlykssj(sdf.format(new Date()) );
										}else{
											yblcsjDto.setDecdlykssj(sdf.format(new Date()) );
										}
										success = yblcsjService.updateYblcsjById(yblcsjDto);
										if (!success){
											log.error("样本实验流程PCR开始时间修改失败！");
										}
									}
								}
							}
						}else {
							jsonObject.put("res", "false");
						}
						break;
				}
			}

		}catch (Exception e){
			log.error(String.format("PCR事件回调事件检查错误%s", e.getMessage()));
			log.error(String.format("出错流程类型id%s", sjlcDto.getLclx()));
			log.error(String.format("出错事件id%s", sjlcDto.getSjid()));
		}
		return jsonObject;
	}

	public JSONObject setBackData(String backdata) {
		JSONObject jsonObject = new JSONObject();
		String[] bs = backdata.split(",");
		for (String s : bs) {
			if (StringUtil.isNotBlank(s)) {
				String[] baks = s.split(":");
				if (baks.length > 1) {
					jsonObject.put(baks[0], baks[1]);

				}
			}
		}
		return jsonObject;
	}

}

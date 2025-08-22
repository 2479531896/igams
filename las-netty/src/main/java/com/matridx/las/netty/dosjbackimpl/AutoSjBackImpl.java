package com.matridx.las.netty.dosjbackimpl;


import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.dosjback.EventCommonback;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.material.AutoDesktopGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.service.svcinterface.IYblcsjService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.FrameModel;

@Service
public class AutoSjBackImpl implements EventCommonback {
	@Autowired
	private IYblcsjService yblcsjService;

	@Override
	public JSONObject handBack(SjlcDto sjlcDto, FrameModel frameModel, Map<String,String> ma) {
		JSONObject jsonObject = new JSONObject();
		try {
			log.info(String.format("建库仪事件回调，回调事件为%s,回调命令%s",sjlcDto.getSjid(),frameModel.getCmd()));
			// TODO Auto-generated method stub
			String hdlx = sjlcDto.getSjhtcllx();
			String deviceId=ma.get(sjlcDto.getZyyqlx());

			jsonObject.put("res","true");
			jsonObject.put("special","false");

			if(StringUtils.isNoneBlank(hdlx)) {
				switch (hdlx) {
					//放新的八连管到配置仪
					case "dropEight":
						if("OK".equals(frameModel.getCmd())){
							OctalianpipeListModel octalianpipeListModel = AgvDesktopGlobal.getAgv_octalianpipe();
							AutoMaterialModel autoMaterialModel = new AutoMaterialModel();
							autoMaterialModel.setDeviceid(deviceId);
							AutoMaterialModel autoMaterialModel1 = InstrumentMaterialGlobal.getAutoMaterial(autoMaterialModel);
							if(autoMaterialModel1!=null){
								if (autoMaterialModel1.getOctalianpipeList()!= null ){
									autoMaterialModel1.getOctalianpipeList().setNum(autoMaterialModel1.getOctalianpipeList().getNum()+1);
									InstrumentMaterialGlobal.setAutoMaterial(autoMaterialModel1,true);
								}else{
									OctalianpipeListModel octalianpipeListModel1 = new OctalianpipeListModel();
									octalianpipeListModel1.setNum(octalianpipeListModel1.getNum()+1);
									octalianpipeListModel1.setMap(new HashMap<>());
									autoMaterialModel.setOctalianpipeList(octalianpipeListModel1);
									InstrumentMaterialGlobal.setAutoMaterial(autoMaterialModel,true);
								}
							}

							octalianpipeListModel.setNum(octalianpipeListModel.getNum() -1);
							octalianpipeListModel.setOctbNum(octalianpipeListModel.getOctbNum() -1);
							AgvDesktopGlobal.setAgv_octalianpipe(octalianpipeListModel,true);
						}else{
							jsonObject.put("res","false");
						}

						break;
					//从配置仪物料区取干净的八连管载具到机器人
					case "changeEight":
						if("OK".equals(frameModel.getCmd())){
							OctalianpipeListModel octalianpipe = AutoDesktopGlobal.getauto_octalianpipe1();
							if (null != octalianpipe.getMap()){
								AgvDesktopGlobal.setAgv_octalianpipe(octalianpipe,true);
								octalianpipe.setMap(null);
								octalianpipe.setNum(0);
								AutoDesktopGlobal.setauto_octalianpipe1(octalianpipe,true);
							}
						}else{
							jsonObject.put("res","false");
						}

						break;
					//从配置仪取八连管到机器人身上
					case "eightMatter":
						if("OK".equals(frameModel.getCmd())){
							AutoMaterialModel autoMaterialModel = new AutoMaterialModel();
							autoMaterialModel.setDeviceid(deviceId);
							AutoMaterialModel autoMaterialModel1 = InstrumentMaterialGlobal.getAutoMaterial(autoMaterialModel);
							if(autoMaterialModel1!=null){
								OctalianpipeListModel octalianpipeListModel = autoMaterialModel1.getOctalianpipeList();
								OctalianpipeListModel octalianpipeListModel1 = AgvDesktopGlobal.getAgv_octalianpipe();
								if (null != octalianpipeListModel1.getMap()){
									octalianpipeListModel1.setOctNum(octalianpipeListModel1.getOctNum()+1);
									octalianpipeListModel1.setOctbNum(octalianpipeListModel1.getOctbNum()+1);
//							octalianpipeListModel1.setOctalList(octalianpipeListModel.getOctalList());
									octalianpipeListModel1.getMap().put(String.valueOf(octalianpipeListModel.getOctNum()*2-1),octalianpipeListModel.getMap().get(String.valueOf(octalianpipeListModel.getOctNum()*2)));
									AgvDesktopGlobal.setAgv_octalianpipe(octalianpipeListModel1,true);
								}
								octalianpipeListModel.getMap().remove(String.valueOf(octalianpipeListModel.getOctNum()*2));
								octalianpipeListModel.setOctNum(octalianpipeListModel.getOctNum()-1);
								octalianpipeListModel.setOctbNum(octalianpipeListModel.getOctbNum()-1);
								autoMaterialModel1.setOctalianpipeList(octalianpipeListModel);
								InstrumentMaterialGlobal.setAutoMaterial(autoMaterialModel1,true);
							}
							break;
						}else{
							jsonObject.put("res","false");
						}
						//此处判断配置仪是否开门成功
					case "autoOpenOrClose":
						if("OK".equals(frameModel.getCmd())){
							jsonObject.put("res","true");
						}else{
							jsonObject.put("res","false");
						}
						break;
					case "autoInit":
						if("OK".equals(frameModel.getCmd())){
							jsonObject.put("res","true");
						}else{
							jsonObject.put("res","false");
						}
						break;
					case "startAuto" :
						//设置状态
						if("OK".equals(frameModel.getCmd())){

							InstrumentStateGlobal.changeInstrumentState(frameModel.getCommand(),deviceId, InstrumentStatusEnum.STATE_WORK.getCode());
							String deviceid=ma.get(Command.AUTO.toString());
							Map<String,Object>libMap = InstrumentStateGlobal.getLibaryInfMap(deviceid);
							//yblcsj的配置仪开始时间
							yblcsjService.updateYblcsjByList(libMap,"1","pzyks");
							//处理ma
							Map<String,String> newMa = new HashMap<>();
							//创建新的机器人map
							newMa.put(Command.AGV.toString(), ma.get(Command.AGV.toString()));
							newMa.put(Command.CUBICS.toString(), ma.get(Command.CUBICS.toString()));
							//移除机器人id
							//CubsParmGlobal.putCubQueuesFromRedis(ma.get(Command.CUBICS.toString()));
							InstrumentStateGlobal.removeInstrumentUsedList_Map(ma,Command.AGV.toString(),ma.get(Command.AGV.toString()));
							ma.remove(Command.AGV.toString());
							InstrumentStateGlobal.removeInstrumentUsedList_Map(ma,Command.CUBICS.toString(),ma.get(Command.CUBICS.toString()));
							ma.remove(Command.CUBICS.toString());
							//放入全局变量
							InstrumentStateGlobal.setInstrumentUsedList(newMa);
							jsonObject.put("ma", newMa);

						}else{
							jsonObject.put("res","false");
						}
						break;
					case "startAuto_1" :
						//设置状态
						if("OK".equals(frameModel.getCmd())){

							InstrumentStateGlobal.changeInstrumentState(frameModel.getCommand(),deviceId, InstrumentStatusEnum.STATE_WORK.getCode());
							String deviceid=ma.get(Command.AUTO.toString());
							Map<String,Object>libMap = InstrumentStateGlobal.getLibaryInfMap(deviceid);
							//yblcsj的配置仪开始时间
							yblcsjService.updateYblcsjByList(libMap,"1","pzyks");

						}else{
							jsonObject.put("res","false");
						}
						break;
					case "startAuto2":
						String deviceid=ma.get(Command.AUTO.toString());
						Map<String,Object>libMap = InstrumentStateGlobal.getLibaryInfMap(deviceid);
						yblcsjService.updateYblcsjByList(libMap,"2","pzyks");
						InstrumentStateGlobal.setLibaryInfMap(ma.get(Command.AUTO.toString()),libMap);
						break;
					case "startAuto3":
						String deviceid1=ma.get(Command.AUTO.toString());
						InstrumentStateGlobal.setLibaryInfMap(deviceid1,null);
						break;
				}
			}




		}catch (Exception e){
			log.error(String.format("AUTO事件回调事件检查错误%s", e.getMessage()));
			log.error(String.format("出错流程类型id%s", sjlcDto.getLclx()));
			log.error(String.format("出错事件id%s", sjlcDto.getSjid()));
		}
		return jsonObject;
	}

}

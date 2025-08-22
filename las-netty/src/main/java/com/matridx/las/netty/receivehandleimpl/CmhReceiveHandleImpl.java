package com.matridx.las.netty.receivehandleimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.enums.MedicalRobotProcessEnum;

@Service
public class CmhReceiveHandleImpl extends BaseReceiveHandleImpl {



	@Autowired
	private AgvDesktopGlobal agvDesktopGlobal;
	@Autowired
	private InstrumentMaterialGlobal instrumentMaterialGlobal;

	public boolean receiveHandle(FrameModel recModel) {
		log.info(String.format("压盖机接收消息，命令为%s,设备id%s",recModel.getCmd(),recModel.getDeviceID()));


//		JSONObject rejsonObject = JSONObject.parseObject(recModel.getCmdParam()[0]);
//		// 取出数据
//		String backdata = rejsonObject.get("backdata").toString();
//		JSONObject backJson = setBackData(backdata);
//		// 压盖机器第一次完成配置,调用
//		if (recModel.getCmd().equals("RESULT") && backJson.get("djcsy").toString().equals("1")) {
//			JSONObject jsonObject = new JSONObject();
//			Map<String, String> ma = InstrumentStateGlobal.getInstrumentUsedListMap(Command.CMH.toString(), recModel.getDeviceID());
//			jsonObject.put("yqAndJqrUsedMap", ma);
//			// String back =
//			// "backdata:'djcsy:1,wkid:"+backJson.get("wkid")+",ispcr:"+backJson.get("ispcr");
//			// 封装传入荧光定量一的参数
//			JSONObject jtPcr = makeParamTopcar(backJson, "1");
//			jsonObject.put("startparam", jtPcr);
//			List<FsgcmlDto> list = fsgcmlService
//					.getFsgcmlDtosByType(MedicalRobotProcessEnum.MEDICAL_ROBOT_MOVECLOSINGTOPCR.getCode());
//			// 调用发送接口
//			SendBaseCommand sendBaseCommand = new SendBaseCommand();
//			//sendBaseCommand.sendCmdSModelByProcessThread(true, list, jsonObject);
//		}
//		// 压盖机器第二次完成配置,调用
//		if (backJson.get("djcsy").toString().equals("2")) {
//			JSONObject jsonObject = new JSONObject();
//			Map<String, String> ma = InstrumentStateGlobal.getInstrumentUsedListMap(Command.CMH.toString(), recModel.getDeviceID());
//			if (ma != null && StringUtil.isNotBlank(ma.get(Command.PCR.toString()))) {
//				jsonObject.put("yqDeviceID", ma.get(Command.PCR.toString()));
//				jsonObject.put("yqAndJqrUsedMap", ma);
//				JSONObject jtPcr = makeParamTopcar(backJson, "2");
//				jsonObject.put("startparam", jtPcr);
//				List<FsgcmlDto> list = fsgcmlService
//						.getFsgcmlDtosByType(MedicalRobotProcessEnum.MEDICAL_ROBOT_MOVECLOSINGTOPCRSECEND.getCode());
//				// 调用发送接口
//				SendBaseCommand sendBaseCommand = new SendBaseCommand();
//				//sendBaseCommand.sendCmdSModelByProcessThread(true, list, jsonObject);
//			} else {
//				// todo
//			}
//
//		}
		return true;
	}



	// 释放八连管方法
	public String[] dropEight(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(mlpzDto.getXylx());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		ChmMaterialModel chmMaterialModel = new ChmMaterialModel();
		switch (param) {
			// 从1-6中获取
			case "dropCmh_agv":
				strArr[0] = "1";
				chmMaterialModel.setDeviceid(ma.get(Command.CMH.toString()));
				ChmMaterialModel chmMaterialModel1 = instrumentMaterialGlobal.getChmsMaterial(chmMaterialModel);
				if (null != chmMaterialModel1.getMap()){
					chmMaterialModel1.setOctNum(chmMaterialModel1.getOctNum()+1);
					strArr[1] = String.valueOf(chmMaterialModel1.getOctNum() + 50);
				}else{
					chmMaterialModel.setOctNum(chmMaterialModel.getOctNum()+1);
					strArr[1] = String.valueOf(chmMaterialModel.getOctNum() + 50);
				}

				break;
			// 释放八连管盖
			case "dropCmhG_agv":
				strArr[0] = "1";
				chmMaterialModel.setDeviceid(ma.get(Command.CMH.toString()));
				ChmMaterialModel chmMaterialModel2 = instrumentMaterialGlobal.getChmsMaterial(chmMaterialModel);
				if (null != chmMaterialModel2) {
					chmMaterialModel2.setOctgNum(chmMaterialModel2.getOctgNum()+1);
					strArr[1] =  String.valueOf(chmMaterialModel2.getOctgNum() + 52);
				}
				break;
		}
		return strArr;
	}

	// 抓取八连管方法
	public String[] takeEight(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(sjlcDto.getZyyqlx());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 从1-6中获取
			case "takeCmh_agv":
				strArr[0] = sjlcDto.getZyyqlx();
				ChmMaterialModel chmMaterialModel = new ChmMaterialModel();
				chmMaterialModel.setDeviceid(ma.get(Command.CMH.toString()));
				ChmMaterialModel chmMaterialModel1 = instrumentMaterialGlobal.getChmsMaterial(chmMaterialModel);
				if (chmMaterialModel1 != null){
					strArr[1] = String.valueOf(chmMaterialModel1.getOctgNum() + 50);
				}
				break;
			// 拿八连管盖
			case "takeCmhG_agv":
				strArr[0] = sjlcDto.getZyyqlx();
				ChmMaterialModel chmMaterialModel2 = new ChmMaterialModel();
				chmMaterialModel2.setDeviceid(ma.get(Command.CMH.toString()));
				ChmMaterialModel chmMaterialModel3 = InstrumentMaterialGlobal.getChmsMaterial(chmMaterialModel2);
				if (null!= chmMaterialModel3){
					strArr[1] = chmMaterialModel3.getChmgNum().toString();
				}else{
					strArr[1] = chmMaterialModel2.getOctNum().toString();
				}
				break;
		}
		return strArr;
	}

	// cmh开始
	public String[] startCmh(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(mlpzDto.getXylx());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 从1-6中获取
			case "cmhStartParam":
				strArr[0] = "6";
				strArr[1] = "6";
				break;
		}
		return strArr;
	}

}
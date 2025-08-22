package com.matridx.las.netty.receivehandleimpl;

import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.enums.GoodsEnum;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.service.svcinterface.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.WkmxPcrModel;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.error.ExceptionHandlingUtil;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class PcrReceiveHandleImpl extends BaseReceiveHandleImpl {
	@Autowired
	private IDlysymxService dlysymxService;

	@Autowired
	private IYqdzxxService yqdzxxService;
//	@Autowired
//	private IWksyService wksyService;
	@Value("${matridx.jcdw:}")
	private String jcdw;
	//private boolean cmd_exec_async;
	@Autowired
	private IYblcsjService yblcsjService;
	@Autowired
	private AgvDesktopGlobal agvDesktopGlobal;
	@Autowired
	private InstrumentMaterialGlobal instrumentMaterialGlobal;
	@Autowired
	private IWkmxService wkmxService;
	@Autowired
	private IWkglService wkglService;
	@Autowired
	private IJcsjService jcsjService;

	public boolean receiveHandle(FrameModel recModel) throws ExceptionHandlingUtil {
		log.info(String.format("机器pcr人接收消息，命令为%s,设备id%s",recModel.getCmd(),recModel.getDeviceID()));
		/*
		 * if (recModel.getCmd().equals("STATE")) { AutoResultModel mAutoResultModel =
		 * JSONObject.parseObject(recModel.getCmdParam()[0], AutoResultModel.class); //
		 * 取出数据 //JSONObject backJson = setBackData(backdata);
		 * handleStateMesg(recModel); }
		 */

	/*	if(pcrMaterialModel1!=null){
			pcrMaterialModel1.setState(InstrumentStatusEnum.STATE_FREE.getCode());
			//pcrMaterialModel1.setOctalianpipeList(null);
			instrumentMaterialGlobal.setPcrMaterial(pcrMaterialModel1,true);
		}*/

		Map<String,Object> libMap=InstrumentStateGlobal.getLibaryInfMap(recModel.getDeviceID());
		if (recModel.getCmd().equals("STATE ")) {
			JSONObject rejsonObject = JSONObject.parseObject(recModel.getCmdParam()[0]);
			YqdzxxDto yqdzxxDto = new YqdzxxDto();
			yqdzxxDto.setDzid(StringUtil.generateUUID());
			yqdzxxDto.setYqid(recModel.getDeviceID());
			yqdzxxDto.setYqlx(recModel.getCommand());
			yqdzxxDto.setSbcs(recModel.getCmdParam()[0]);
			yqdzxxDto.setSbsj(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			log.info("保存动作：" + yqdzxxDto.getSbcs());
			yqdzxxService.insert(yqdzxxDto);
		}
		// 接收定量仪
		// 荧光定量仪器第一次完成配置,调用
		if (recModel.getCmd().equals("RESULT")) {
			synchronized (PcrMaterialModel.class) {

				PcrMaterialModel pcrMaterialModel = new PcrMaterialModel();
				pcrMaterialModel.setDeviceid(recModel.getDeviceID());
				PcrMaterialModel pcrMaterialModelN = InstrumentMaterialGlobal.getPcrMaterial(pcrMaterialModel);
				if(pcrMaterialModelN!=null) {
					pcrMaterialModelN.setStartTime("");
					InstrumentMaterialGlobal.setPcrMaterial(pcrMaterialModelN, true);
				}
			}
			log.info("修改pcr开始时间结束");
			Map<String,String>finMap=new HashMap<>();
			InstrumentStateGlobal.setInstrumentFinMap(recModel.getDeviceID(),recModel);
			Map<String, String> ma = InstrumentStateGlobal.getInstrumentUsedListMap(Command.PCR.toString(), recModel.getDeviceID());
			String[] parm = recModel.getCmdParam();
			String jsonString = parm[0];
			WkmxPcrModel wkmxPcrModel = JSONObject.parseObject(jsonString, WkmxPcrModel.class);
			JSONObject jsonO = setBackData(wkmxPcrModel.getBackdata());
			if (jsonO.get("djcsy") != null)
				wkmxPcrModel.setDjcsy(jsonO.get("djcsy").toString());
			if (jsonO.get("ispcr") != null)
				wkmxPcrModel.setIspcr(jsonO.get("ispcr").toString());
			if (jsonO.get("wkid") != null){
				//InstrumentStateGlobal.setLibaryInfMap(recModel.getDeviceID(),jsonO.get("wkid").toString());
				wkmxPcrModel.setWkid(jsonO.get("wkid").toString());
			}
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb(BasicDataTypeEnum.DETECTION_UNIT.getCode());
			jcsjDto.setCsdm(recModel.getDeviceID().substring(0,2));
			jcsjDto =  jcsjService.getByAndCsdm(jcsjDto);
			wkmxPcrModel.setJcdwtype(recModel.getDeviceID().substring(0,2));
			if (jcsjDto!=null &&StringUtil.isNotBlank(jcsjDto.getCsmc())){
				wkmxPcrModel.setJcdw(jcsjDto.getCsmc());
			}else if(jcsjDto==null&&jsonO.get("jcdw") != null){
				wkmxPcrModel.setJcdw(jsonO.get("jcdw").toString());
			}else{
				wkmxPcrModel.setJcdw(jcdw);
			}

			if (ma != null)
				wkmxPcrModel.setPzyqid(ma.get(Command.AUTO.toString()));
			if (wkmxPcrModel.getDjcsy() != null && wkmxPcrModel.getDjcsy().equals("1")) {
				// 保存数据到数据库等待文本导出
				//保存ma


			}

			// 荧光定量仪器第二次完成配置,调用
			if (wkmxPcrModel.getDjcsy() != null && wkmxPcrModel.getDjcsy().equals("2") && jsonO.get("wkid") != null) {
				// 更新定量仪结果
				JSONObject jsonObject = new JSONObject();
				// 获取配置仪器，配置仪要用之前的配置仪
				if (ma != null && StringUtils.isNotBlank(ma.get(Command.AUTO.toString()))) {
					jsonObject.put("yqAndJqrUsedMap", ma);
					String back = "djcsy:2,wkid:" + jsonO.get("wkid") + ",ispcr:2";
					JSONObject backJsonObject = new JSONObject();
					// 获取传给配置仪的参数
					WkglDto wsDto = new WkglDto();
					wsDto.setWkid(jsonO.get("wkid").toString());
					WkglDto wsyDto = wkglService.getWkglDtoBywkid(wsDto);
					JSONObject jback = new JSONObject();
					jback.put("tj", wsyDto.getZjhcytj());
					jback.put("phy", wsyDto.getPhhhy());
					jback.put("bxy", wsyDto.getBxy());
					backJsonObject.put("result", jback);
					backJsonObject.put("backdata", back);
					jsonObject.put("startparam", backJsonObject);
//					// 移动空管到配置仪
//					List<FsgcmlDto> list = fsgcmlService
//							.getFsgcmlDtosByType(MedicalRobotProcessEnum.MEDICAL_ROBOT_MOVETODISPENSERTHIRD.getCode());
//					// 调用发送接口
//					SendBaseCommand sendBaseCommand = new SendBaseCommand();
//					// sendBaseCommand.sendCmdSModelByProcessThread(true, list, jsonObject);
				} else {

				}

			}
			dlysymxService.saveDlysymx(JSONObject.toJSONString(wkmxPcrModel));
			if(libMap!=null) {
				yblcsjService.updateYblcsjByList(libMap, wkmxPcrModel.getDjcsy(), "pcrjs");
				InstrumentStateGlobal.setPcrdoStartMap(jsonO.get("wkid").toString(),recModel);
				new SendBaseCommand().sendEventFlowlist("204", recModel);
			}
			new SendBaseCommand().sendLibrary(wkmxPcrModel);

		}
		return true;
	}

	// 移动方法
	public String[] moveMethod(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String>map) {
		String[] strArr = new String[2];
		// 根据通道号获取对应位置
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 6-荧光定量仪空间
			case "setPcr_agv":
				strArr[0] = InstrumentMaterialGlobal.getInstrumentWz(map.get(Command.PCR.toString()),Command.PCR.toString());
				break;

		}

		return strArr;
	}

	// 拍照方法
	public String[] picMethod(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String>map) {
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 28-荧光定量仪拍照位置
			case "setPcr_agv":
				strArr[0] = InstrumentMaterialGlobal.getInstrumentWz(map.get(Command.PCR.toString()),Command.PCR.toString());
				break;
		}
		return strArr;
	}

	// 取夹爪方法
	public String[] takeClaw(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String>map) {
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 6-八联管物料夹爪
			case "takePcrClaw_agv":
				strArr[0] = GoodsEnum.EIGHT_CLAW.getCode();
				break;
		}
		return strArr;
	}

	// 放夹爪方法
	public String[] dropClaw(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String>map) {
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 6-八联管物料夹爪
			case "dropPcrClaw_agv":
				strArr[0] =  GoodsEnum.EIGHT_CLAW.getCode();
				break;
		}
		return strArr;
	}

	// 抓取八连管方法
	public String[] takeEight(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String>map) {
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 从1-6中获取
			case "takePcr_agv":
				PcrMaterialModel pcrMaterialModel = new PcrMaterialModel();
				pcrMaterialModel.setDeviceid(map.get(sjlcDto.getZyyqlx()));
				PcrMaterialModel pcrMaterialModel1 = instrumentMaterialGlobal.getPcrMaterial(pcrMaterialModel);
				OctalianpipeListModel octalianpipeListModel = pcrMaterialModel1.getOctalianpipeList();
//				if (null != pcrMaterialModel1){
//					if (null != octalianpipeListModel && octalianpipeListModel.getOctNum() > 0){
//						strArr[0] = String.valueOf(octalianpipeListModel.getOctgNum()*2-1);
//					}
//				}else{
				if (null != octalianpipeListModel && octalianpipeListModel.getOctgNum() > 0){
					strArr[0] = String.valueOf(octalianpipeListModel.getOctNum()*2-1);
				}
//				}
				break;
		}
		return strArr;
	}
	// 释放八连管到PCR方法
	public String[] dropEight(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 从1-6中获取
			case "dropPcr_agv":
//				PcrMaterialModel pcrMaterialModel = new PcrMaterialModel();
//				pcrMaterialModel.setDeviceid(sjlcDto.getZyyqlx());
//				PcrMaterialModel pcrMaterialModel1 = instrumentMaterialGlobal.getPcrMaterial(pcrMaterialModel);
				OctalianpipeListModel octalianpipeListModel = agvDesktopGlobal.getAgv_octalianpipe();
//				if (null != pcrMaterialModel1){
//					if (null != octalianpipeListModel && octalianpipeListModel.getOctNum() > 0){
//						strArr[0] = String.valueOf(octalianpipeListModel.getOctgNum()*2-1);
//					}
//				}else{
					if (null != octalianpipeListModel && octalianpipeListModel.getOctgNum() > 0){
						strArr[0] = String.valueOf(octalianpipeListModel.getOctgNum()*2-1);
					}
//				}
				break;
		}
		return strArr;
	}


	// 放入到AGV平台载具的
	public String[] dropMatter(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String>map) {
		String[] strArr = new String[2];
		PcrMaterialModel pcrMaterialModel = new PcrMaterialModel();
		pcrMaterialModel.setDeviceid(map.get(Command.PCR.toString()));
		PcrMaterialModel model = instrumentMaterialGlobal.getPcrMaterial(pcrMaterialModel);
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 6八连管载具
			case "dropPcr_agv":
				strArr[0] = "6";
				strArr[1] = model.getOctalianpipeList().getOctNum()*2-1+"";// 从redis中获取
				break;
		}
		return strArr;
	}

	// 操作夹爪
	public String[] operationClaw(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String>map) {
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 6八连管载具 6-八联管物料夹爪
			case "openClaw_agv":
				strArr[0] = "6";
				strArr[1] = "6";// 从redis中获取
				break;
			case "closeClaw_agv":
				strArr[0] = "6";
				strArr[1] = "6";// 从redis中获取
				break;
		}
		return strArr;
	}

	// 配置仪开始
	public JSONObject startAuto(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		JSONObject jsonObject = new JSONObject();
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		String wkid= "";
		if(param.equals("autoStartSecond")){
			String deviceid =  ma.get(Command.AUTO.toString());
			wkid =InstrumentStateGlobal.getWkidByAutoId(deviceid);
		}else{
			String deviceID = ma.get(Command.AUTO.toString());
			wkid=  InstrumentStateGlobal.getLibaryInfMap(deviceID).get("wkid")+"";
		}

		WkmxDto dto =new WkmxDto();
		dto.setWkid(wkid);
		//dto.setNbbh("11111");
		String back="";
		String index ="";
		WkglDto wsDto = new WkglDto();
		wsDto.setWkid(wkid);
		//wsDto.setWkid("1111");
		wsDto = wkglService.getWkglDtoBywkid(wsDto);
		JSONObject jback = new JSONObject();
		List<WkmxDto> wklist=wkmxService.getWkmxList(dto);

		switch (param) {
			// 配置仪第二次
			case "autoStartSecond":
				back = "djcsy:2,wkid:" + wkid + ",ispcr:0"  + ",djc:2";
				//back = "djcsy:2,wkid:1111,ispcr:0"  + ",djc:2";
				index="1";
				List<Map<String,String>>mapList=new ArrayList<>();
				for(WkmxDto wkDto:wklist){
					Map<String,String> map=new HashMap<>();
					map.put("ybbh",wkDto.getNbbh());
					map.put("wz",wkDto.getWkxzb()+","+wkDto.getWkyzb());
					map.put("yy",wkDto.getWkyyjytj());
					map.put("xsy",wkDto.getWkxsyjytj());
					mapList.add(map);
				}
				jsonObject.put("result", mapList);/***
			 //	 * 修改样本时间流程配置仪完成时间
			 //	 * @param index 第几次实验完成
			 //	 * @return
			 //	 */
//	private boolean updatePcrEndTime(String index) {
//
//		YblcsjDto yblcsjDto =new YblcsjDto();
//		yblcsjDto.setYsybid("");
//		SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
//		yblcsjDto.setYsybid("");
//		if(index.equals("1")) {
//			yblcsjDto.setDycdljssj(sdf.format(new Date()) );
//		}else if(index.equals("2")){
//			yblcsjDto.setDecdlyjssj(sdf.format(new Date()) );
//		}
//
//		return yblcsjService.updateYblcsjById(yblcsjDto);
//	}

				break;
			// 配置仪第三次
			case "autoStartThread":
				jback.put("tj", wsDto.getZjhcytj());
				jback.put("phy", wsDto.getPhhhy());
				jback.put("bxy", wsDto.getBxy());
				back = "djcsy:3,wkid:" + wkid + ",ispcr:2"  + ",djc:3";
				//back = "djcsy:3,wkid:1111,ispcr:2"  + ",djc:3";
				index="2";
				jsonObject.put("result", jback);
				break;


		}


		jsonObject.put("backdata", back);

		return jsonObject;
	}


	// PCR开始
	public JSONObject startPcr(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {

		FrameModel recModel = InstrumentStateGlobal.getInstrumentFinMap(ma.get("2"));
		Map<String,Object> libMap =InstrumentStateGlobal.getLibaryInfMap(ma.get("2"));
		String deviceId=ma.get(Command.AUTO.toString());
		InstrumentStateGlobal.setLibaryInfMap(deviceId,libMap);
		JSONObject jsonObject = JSONObject.parseObject(recModel.getCmdParam()[0]);
		JSONArray oldArr = JSONObject.parseArray(jsonObject.getString("Result"));
		List<Map<String,Object>>list=new ArrayList<>();
		for(int i=0;i<oldArr.size();i++){
			JSONObject arrObj = (JSONObject)oldArr.get(i);
			Map<String,Object> map=new HashMap<>();
			if(arrObj.get("xwz")!=null){
				map.put("wz",arrObj.get("xwz"));
			}else{
				map.put("wz",arrObj.get("wz"));
			}
			map.put("ybbh",arrObj.get("ybbh"));
			map.put("jcdw",arrObj.get("乌鲁木齐实验室"));
			list.add(map);
		}
		JSONObject newJob =new JSONObject();
		newJob.put("backdata",jsonObject.get("backdata"));
		newJob.put("result",list);
		return newJob;
	}

}

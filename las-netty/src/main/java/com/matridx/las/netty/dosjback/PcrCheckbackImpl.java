package com.matridx.las.netty.dosjback;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.WkmxPcrModel;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.dao.entities.SjlcDto;

import java.util.Map;

@Service
public class PcrCheckbackImpl implements EventCheckskipback {

	@Override
	public boolean checkSkip(SjlcDto sjlcDto, FrameModel frameModel, Map<String,String> map) {
		log.info(String.format("pcr事件检查，检查事件为%s",sjlcDto.getSjid()));
		String xhpdlx = sjlcDto.getChecklx();
		boolean flag = false;
		try {
			String pcrDeviceId= map.get(Command.AUTO.toString());
			FrameModel resModel= InstrumentStateGlobal.getInstrumentFinMap(pcrDeviceId);
			String[] parm=resModel.getCmdParam();
			String jsonString = parm[0];
			WkmxPcrModel wkmxPcrModel = JSONObject.parseObject(jsonString, WkmxPcrModel.class);
			JSONObject jsonO = setBackData(wkmxPcrModel.getBackdata());
			JSONObject json = JSONObject.parseObject(InstrumentStateGlobal.getInstrumentFinMap(map.get(Command.AUTO.toString())).getCmdParam()[0]);
			JSONObject backJson = setBackData(json.get("backdata").toString()); // 先保存状态和动作流程
			if (StringUtils.isNoneBlank(xhpdlx)) {
				switch (xhpdlx) {
					//此处判断Pcr开始时间执行不执行
					case "startPcr1":
						if (backJson.get("djcsy").toString().equals("1")) {
							flag = true;
						}
						break;
					//此处判断Pcr开始时间执行不执行
					case "startPcr2":
						if (backJson.get("djcsy").toString().equals("2")) {
							flag = true;
						}
						break;
					//此处判断Pcr是否开始成功
					case "startPcr":
						if ("OK".equals(frameModel.getCmd())) {
							flag = true;
						}
						break;
					// 开关门返回true
					case "openAndClose":
						flag = true;
						//frameModel.getMsgInfo();
						break;
					//判断配置仪第几次完成
					case "checkAutoNum1":
						if (jsonO.get("djcsy") != null){
							if("1".equals(jsonO.get("djcsy").toString())){
								flag = true;
							}
						}
						break;
					//判断配置仪第几次完成
					case "checkAutoNum2":
						if (jsonO.get("djcsy") != null){
							if("2".equals(jsonO.get("djcsy").toString())){
								flag = true;
							}
						}
						break;
				}
			}
		}catch (Exception e){
			log.error(String.format("Pcr事件检查错误%s", e.getMessage()));
			log.error(String.format("出错流程类型id%s", sjlcDto.getLclx()));
			log.error(String.format("出错事件id%s", sjlcDto.getSjid()));
		}

		return flag;
	}

	public JSONObject setBackData(String backdata) {
		JSONObject jsonObject = new JSONObject();
		String[] bs = backdata.split(",");
		for (String s : bs) {
			if (StringUtil.isNotBlank(s)) {
				String[] baks = s.split(":");
				//baks != null &&
				if ( baks.length > 1) {
					jsonObject.put(baks[0], baks[1]);

				}
			}
		}
		return jsonObject;
	}
}

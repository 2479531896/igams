package com.matridx.las.netty.dosjbackimpl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.dosjback.EventCommonback;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CmhSjBackImpl implements EventCommonback {

	@Override
	public JSONObject handBack(SjlcDto sjlcDto, FrameModel frameModel, Map<String,String> ma) {
		JSONObject jsonObject = new JSONObject();
		try {
			log.info(String.format("压盖机事件回调，回调事件为%s,回调命令%s",sjlcDto.getSjid(),frameModel.getCmd()));
			// TODO Auto-generated method stub
			String hdlx = sjlcDto.getSjhtcllx();
			String deviceID=ma.get(sjlcDto.getZyyqlx());

			jsonObject.put("res","true");
			jsonObject.put("special","false");
			ChmMaterialModel chmMaterialModel = new ChmMaterialModel();
			if(StringUtils.isNoneBlank(hdlx)) {
				switch (hdlx) {
					//从机器人取八连管到压盖机回调
					case "eightMatter":
						if("OK".equals(frameModel.getCmd())){

							jsonObject.put("res","true");
							chmMaterialModel.setDeviceid(deviceID);
							ChmMaterialModel chmMaterialModel1 = InstrumentMaterialGlobal.getChmsMaterial(chmMaterialModel);
							OctalianpipeListModel octalianpipeListModel = AgvDesktopGlobal.getAgv_octalianpipe();
							if(chmMaterialModel1!=null){
								if (null != chmMaterialModel1.getMap()){
									if (null != octalianpipeListModel.getMap()){
										chmMaterialModel1.setOctNum(chmMaterialModel1.getOctNum()+1);
										chmMaterialModel1.getMap().put(chmMaterialModel1.getOctNum().toString(),octalianpipeListModel.getMap().get(String.valueOf(octalianpipeListModel.getOctNum()*2-1)));
										InstrumentMaterialGlobal.setChmMaterial(chmMaterialModel1,true);
										octalianpipeListModel.getMap().remove(String.valueOf(octalianpipeListModel.getOctNum()*2-1));
										octalianpipeListModel.setOctNum(octalianpipeListModel.getOctNum()-1);
										AgvDesktopGlobal.setAgv_octalianpipe(octalianpipeListModel,true);
									}
								}else{
									if (null != octalianpipeListModel.getMap()){
										Map<String, List<OctalYbxxxModel>> map = new HashMap<>();
										chmMaterialModel1.setOctNum(chmMaterialModel1.getOctNum()+1);
										map.put(chmMaterialModel1.getOctNum().toString(),octalianpipeListModel.getMap().get(String.valueOf(octalianpipeListModel.getOctNum()*2-1)));
										chmMaterialModel1.setMap(map);
										InstrumentMaterialGlobal.setChmMaterial(chmMaterialModel1,true);
										octalianpipeListModel.getMap().remove(String.valueOf(octalianpipeListModel.getOctNum()*2-1));
										octalianpipeListModel.setOctNum(octalianpipeListModel.getOctNum()-1);
										AgvDesktopGlobal.setAgv_octalianpipe(octalianpipeListModel,true);
									}
								}
							}
						}else {
							jsonObject.put("res","false");
						}
						break;
					//此处判断配置仪是否开门成功
					case "startCmh":
						if("OK".equals(frameModel.getCmd())){
							chmMaterialModel.setDeviceid(deviceID);
							ChmMaterialModel chmMaterialModel1 = InstrumentMaterialGlobal.getChmsMaterial(chmMaterialModel);
							if (chmMaterialModel1!=null){
								chmMaterialModel1.setState(InstrumentStatusEnum.STATE_WORK.getCode());
								InstrumentMaterialGlobal.setChmMaterial(chmMaterialModel1,true);
							}
							jsonObject.put("res","true");
						}else {
							jsonObject.put("res","false");
						}
						break;
				}
			}



		}catch (Exception e){
			log.error(String.format("CMH事件回调事件检查错误%s", e.getMessage()));
			log.error(String.format("出错流程类型id%s", sjlcDto.getLclx()));
			log.error(String.format("出错事件id%s", sjlcDto.getSjid()));
		}
		return jsonObject;

	}

}

package com.matridx.las.home.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matridx.las.home.service.svcinterface.IGlobalprocessService;
import com.matridx.las.home.service.svcinterface.IYqxxInfoService;
import com.matridx.las.netty.channel.command.BaseCommand;
import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.las.netty.enums.RobotStatesEnum;
import com.matridx.las.netty.global.*;
import com.matridx.las.netty.service.svcinterface.*;
import com.matridx.las.netty.util.SendMessgeToHtml;
import com.matridx.las.netty.util.snapshot.SnapShotGloabalUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.las.home.service.svcinterface.ICommonService;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.ICallBack;
import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.FsgcmlDto;
import com.matridx.las.netty.util.CommonChannelUtil;

@Service
public class CommonService implements ICommonService, ICallBack {
	@Value("${MedtlRTPcr.url:}")
	private String pcrUrl;
	@Autowired
	private IYqztxxService yqztxxService;

	@Autowired
	private IWksyService wksyService;

	@Autowired
	private MaterialScienceInitServiceImpl materialScienceInitService;
	@Autowired
	private IYqxxInfoService yqxxInfoService;
	@Autowired
	private IJkywzszService jkywzszService;
	@Autowired
	private IGlobalprocessService globalprocessService;
	@Override
	public boolean callfunc(int times, FrameModel recModel, FrameModel sendModel, SjlcDto sjlcDto) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Map<String, Object> testMessage(String type) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jsObject = new JSONObject();
		SendBaseCommand sendBaseCommand = new SendBaseCommand();
		List<FsgcmlDto> list = null;
		boolean result = true;
		if (type.equals("1")) {
			/*try {
				SnapShotGloabalUtil.outputFile("","snapshot.properties");
				SnapShotGloabalUtil.updateProperties("","snapshot.properties");
				SnapShotGloabalUtil.inputFile();
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		//	Map<String,Object> map1= globalprocessService.showGlobalprocessDate();

			/*JkywzszDto jkywzszDto = new JkywzszDto();
			jkywzszDto.setWzszid("2CB7604A36F24F22893E94D67488733E");
			jkywzszDto.setJkytdh("01");
			jkywzszService.saveChannelSetup(jkywzszDto);
			yqxxInfoService.getJkywzxxList();
			materialScienceInitService.initMaterial();
			SendMessgeToHtml.pushMessage(null);*/
			/*CubsMaterialModel cubsMaterialModel = new CubsMaterialModel();
			cubsMaterialModel.setPassId("1212");
			CubsMaterialModel xs2 =  InstrumentMaterialGlobal.getCubsMaterial(cubsMaterialModel);
			CubsMaterialModel cubsMaterialModel1 = new CubsMaterialModel();
			cubsMaterialModel.setPassId("12121");
			CubsMaterialModel xs = InstrumentMaterialGlobal.getCubsMaterial(cubsMaterialModel1);

			SendMessgeToHtml.pushMessage(null);*/
		} else {
		//	SendMessgeToHtml.pushStateMessage(null);
		/*	new BaseCommand().sendByCmdModel("5", "5005", "OP", "OPOP",
					null, null, true,
					true, "123", 1000,
					1000, "12", null, false, 1, null,"");*/
			//SseEmitterServer.sendMessage("test","121212");
		}
		if (result) {
			map.put("status", "success");
		} else {
			map.put("status", "fail");
		}
		return map;
	}
	
	@Override
	public Map<String, Object> closeNettyMessage(String type) {
		//释放相应的仪器
		String yqid = CommonChannelUtil.getDeviceIDByCommand(type);
		//将仪器放入等待区
		if(type.equals(Command.AGV.toString())) {
			InstrumentStateGlobal.changeInstrumentState(Command.AGV.toString(),yqid, RobotStatesEnum.ROBOT_ONLINE.getCode());
			//map也移除

		}else {
			Map<String, String> mp =  InstrumentStateGlobal.getInstrumentUsedListMap(type,yqid);
			if(mp!=null) {
				InstrumentStateGlobal.removeInstrumentUsetMap(mp);
			}
		}
		InstrumentStateGlobal.putInstrumentQueuesToRedis(Command.AGV.toString(),yqid);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "success");
		return map;
	}

	public void startPre(){
		new BaseCommand() .sendByNewThread(new FrameModel());
	}

}

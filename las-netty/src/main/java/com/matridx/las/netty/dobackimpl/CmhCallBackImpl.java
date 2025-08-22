package com.matridx.las.netty.dobackimpl;

import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.channel.domain.CommandDetails;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.enums.MedicalRobotProcessEnum;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.service.svcinterface.IMlpzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.springboot.util.base.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CmhCallBackImpl extends BaseCallBackImpl  {


	@Autowired
	private IMlpzService mlpzService  ;


	/**
	 * 回调函数回调处理,启动停止，连接等
	 * 
	 * @param times     返回次数：1 第一次 2 第二次
	 * @param recModel  返回的帧信息
	 * @param sendModel 发送的帧信息
	 * @return 返回结果
	 */

	public boolean callfunc(int times, FrameModel recModel, FrameModel sendModel,SjlcDto sjlcDto) {
		try {
			if(CommandDetails.CMH_START.equals(recModel.getMsgType())){
				synchronized (ChmMaterialModel.class) {
					SimpleDateFormat format = new SimpleDateFormat("HH:mm");
					ChmMaterialModel chmMaterialModel = new ChmMaterialModel();
					chmMaterialModel.setDeviceid(recModel.getDeviceID());
					ChmMaterialModel chmMaterialModelN =InstrumentMaterialGlobal.getChmsMaterial(chmMaterialModel);
					//chmMaterialModelN.setStartTime(format.format(new Date()));
					chmMaterialModelN.setState(InstrumentStatusEnum.STATE_FREE.getCode());
					chmMaterialModelN.setStartTime("");
					InstrumentMaterialGlobal.setChmMaterial(chmMaterialModelN, true);
				}
				OctalianpipeListModel octalianpipeListModel = AgvDesktopGlobal.getAgv_octalianpipe();
				SendBaseCommand sendBaseCommand = new SendBaseCommand();
				if (null != octalianpipeListModel && octalianpipeListModel.getOctNum() > 0){
					sendBaseCommand.sendEventFlowlist(MedicalRobotProcessEnum.MEDICAL_ROBOT_CMH.getCode(),recModel);
				}else{
					sendBaseCommand.sendEventFlowlist(MedicalRobotProcessEnum.MEDICAL_ROBOT_FROM_CMH_AND_PCR.getCode(),recModel);
				}
			}
			if (StringUtil.isNotBlank(sendModel.getMlid())) {
				// 根据返回信息处理
				MlpzDto mlpzDto = new MlpzDto();
				mlpzDto.setMlid(sendModel.getMlid());
				mlpzDto = mlpzService.getDtoById(sendModel.getMlid());
				//String deviceID = sjlcDto.getZyyqlx();
				if (StringUtil.isNotBlank(mlpzDto.getMethodname()) && StringUtil.isNotBlank(mlpzDto.getParamlx())){
					if ("takeEight".equals(mlpzDto.getMethodname())){
						switch (mlpzDto.getParamlx()) {
							// 机器人抓取八连管盖
							case "takeCmhG_agv":
								ChmMaterialModel chmGMaterialModel = new ChmMaterialModel();
								chmGMaterialModel.setDeviceid(InstrumentStateGlobal.getInstrumentUsedListMap(recModel.getCommand(),recModel.getDeviceID()).get(Command.CMH.toString()));
								ChmMaterialModel chmGMaterialModel1 = InstrumentMaterialGlobal.getChmsMaterial(chmGMaterialModel);
								if(chmGMaterialModel1!=null){
									chmGMaterialModel1.setChmgNum(chmGMaterialModel1.getChmgNum()-1);
									InstrumentMaterialGlobal.setChmMaterial(chmGMaterialModel1,true);
								}
								break;
							// 机器人抓取八连管
							case "takeCmh_agv":
								ChmMaterialModel chmMaterialModel = new ChmMaterialModel();
								chmMaterialModel.setDeviceid(InstrumentStateGlobal.getInstrumentUsedListMap(recModel.getCommand(),recModel.getDeviceID()).get(Command.CMH.toString()));
								ChmMaterialModel chmMaterialModel1 = InstrumentMaterialGlobal.getChmsMaterial(chmMaterialModel);
								if(chmMaterialModel1!=null){
									chmMaterialModel1.setOctgNum(chmMaterialModel1.getOctgNum() -1);
									chmMaterialModel1.setOctNum(chmMaterialModel1.getOctNum() -1);
									InstrumentMaterialGlobal.setChmMaterial(chmMaterialModel1,true);
								}

								break;
						}
					}

					if ("dropEight".equals(mlpzDto.getMethodname())){
						switch (mlpzDto.getParamlx()) {
							// 机器人释放八连管盖
							case "dropCmhG_agv":
								ChmMaterialModel chmMaterialModel = new ChmMaterialModel();
								chmMaterialModel.setDeviceid(InstrumentStateGlobal.getInstrumentUsedListMap(recModel.getCommand(),recModel.getDeviceID()).get(Command.CMH.toString()));
								ChmMaterialModel chmMaterialModel2 = InstrumentMaterialGlobal.getChmsMaterial(chmMaterialModel);
								if(chmMaterialModel2!=null){
									chmMaterialModel2.setOctgNum(chmMaterialModel2.getOctgNum()+1);
									InstrumentMaterialGlobal.setChmMaterial(chmMaterialModel2,true);
								}
								break;
						}
					}

				}
			}
		}catch (Exception e){
			log.error(String.format("CMH命令回调检查错误%s", e.getMessage()));
			log.error(String.format("出错流程类型id%s", sjlcDto.getLclx()));
			log.error(String.format("出错事件id%s", sjlcDto.getSjid()));
		}

		return true;
	}

}
package com.matridx.las.netty.dobackimpl;

import com.matridx.las.netty.channel.domain.CommandDetails;
import com.matridx.las.netty.dao.entities.AutoMaterialModel;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.dao.entities.SjlcDto;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AutoCallBackImpl extends BaseCallBackImpl{

	/**
     * pcr回调函数回调处理,启动停止，连接等
     * @param times 返回次数：1 第一次  2 第二次
     * @param recModel 返回的帧信息
     * @param sendModel 发送的帧信息
     * @return 返回结果
     */
	//BaseCommand baseCommand = new BaseCommand();//命令回调
    public boolean callfunc(int times,FrameModel recModel, FrameModel sendModel,SjlcDto sjlcDto) {
    	try {
			// 获取命令对象
//			FsgcmlDto fsgcmlDto = new FsgcmlDto();
//			if (StringUtil.isNotBlank(sendModel.getMlid())) {
//				//fsgcmlDto = fsgcmlService.getDtoById(sendModel.getMlid());
//			}
			if(CommandDetails.AUTO_FIRST.equals(recModel.getMsgType())||CommandDetails.AUTO_SECOND.equals(recModel.getMsgType())||CommandDetails.AUTO_SECOND.equals(recModel.getMsgType())){
				synchronized (AutoMaterialModel.class) {
					SimpleDateFormat format = new SimpleDateFormat("HH:mm");
					AutoMaterialModel autoMaterialModel = new AutoMaterialModel();
					autoMaterialModel.setDeviceid(recModel.getDeviceID());
					AutoMaterialModel autoMaterialModelN =InstrumentMaterialGlobal.getAutoMaterial(autoMaterialModel);
					autoMaterialModelN.setStartTime(format.format(new Date()));
					InstrumentMaterialGlobal. setAutoMaterial(autoMaterialModelN, true);
				}
			}
			return recModel.getCmd().equals("OK");
		}catch (Exception e){
			log.error(String.format("AUTO命令回调检查错误%s", e.getMessage()));
			log.error(String.format("出错流程类型id%s", sjlcDto.getLclx()));
			log.error(String.format("出错事件id%s", sjlcDto.getSjid()));
		}
		return true;
    }
	
}

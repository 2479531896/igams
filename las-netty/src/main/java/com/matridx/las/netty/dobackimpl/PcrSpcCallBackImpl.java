package com.matridx.las.netty.dobackimpl;

import com.matridx.las.netty.channel.domain.CommandDetails;
import com.matridx.las.netty.dao.entities.ChmMaterialModel;
import com.matridx.las.netty.dao.entities.PcrMaterialModel;
import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.dao.entities.FsgcmlDto;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PcrSpcCallBackImpl extends BaseCallBackImpl{

	/**
     * pcr回调函数回调处理,启动停止，连接等
     * @param times 返回次数：1 第一次  2 第二次
     * @param recModel 返回的帧信息
     * @param sendModel 发送的帧信息
     * @return 返回结果
     */
	//BaseCommand baseCommand = new BaseCommand();
    public boolean callfunc(int times, FrameModel recModel, FrameModel sendModel, SjlcDto sjlcDto) {
    	try {
			if(CommandDetails.PCR_START.equals(recModel.getMsgType())){
				synchronized (PcrMaterialModel.class) {
					SimpleDateFormat format = new SimpleDateFormat("HH:mm");
					PcrMaterialModel pcrMaterialModel = new PcrMaterialModel();
					pcrMaterialModel.setDeviceid(recModel.getDeviceID());
					PcrMaterialModel pcrMaterialModelN = InstrumentMaterialGlobal.getPcrMaterial(pcrMaterialModel);
					pcrMaterialModelN.setStartTime(format.format(new Date()));
					InstrumentMaterialGlobal.setPcrMaterial(pcrMaterialModelN, true);
				}
			}
			// 获取命令对象
			//FsgcmlDto fsgcmlDto = new FsgcmlDto();
//		if (StringUtil.isNotBlank(sendModel.getMlid())) {
//			fsgcmlDto = fsgcmlService.getDtoById(sendModel.getMlid());
//		}
			//加个判断，如果是连接操作返回false，需要继续发送链接操作，直到返回成功,后面说不要这个了，先留在这里
			/*
			 * if(recModel.getMsgType().equals("CN")&&recModel.getCmdParam()!=null&&recModel
			 * .getCmdParam().length>0) { JSONObject jObject =
			 * JSONObject.parseObject(recModel.getCmdParam()[0]);
			 * //找出发送的baseCommand和threadName，用同样的发出去
			 * if(sendModel.getSendBaseCommand()!=null&&sendModel.getSendThreadName()!=null)
			 * { if(jObject.get("IsConnect").equals("false")){
			 * sendModel.getSendBaseCommand().sendByCmdModel(Command.PCR.toString(),
			 * sendModel.getDeviceID(), sendModel.getMsgType(), sendModel.getCmd(),
			 * sendModel.getCmdParam(), sendModel.getCallFunc(), sendModel.isSync(), true,
			 * sendModel.getSendThreadName(),10000,
			 * 60000,sendModel.getGcid(),sendModel.getYqUsedMap(),true); } } }
			 */
		}catch (Exception e){
			log.error(String.format("Pcr命令回调检查错误%s", e.getMessage()));
			log.error(String.format("出错流程类型id%s", sjlcDto.getLclx()));
			log.error(String.format("出错事件id%s", sjlcDto.getSjid()));
		}

		return true;
//		if(recModel.getCmd().equals("OK")) {
//			return doBackFunc(recModel, sendModel, fsgcmlDto);
//
//		}else {
//			return false;
//		}
    }
	
}

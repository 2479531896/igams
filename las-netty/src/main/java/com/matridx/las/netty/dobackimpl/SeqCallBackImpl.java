package com.matridx.las.netty.dobackimpl;

import com.matridx.las.netty.channel.domain.CommandDetails;
import com.matridx.las.netty.dao.entities.PcrMaterialModel;
import com.matridx.las.netty.dao.entities.SeqMaterialModel;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.BaseCommand;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.dao.entities.FsgcmlDto;
import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.springboot.util.base.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SeqCallBackImpl extends BaseCallBackImpl{

	/**
     * pcr回调函数回调处理,启动停止，连接等
     * @param times 返回次数：1 第一次  2 第二次
     * @param recModel 返回的帧信息
     * @param sendModel 发送的帧信息
     * @return 返回结果
     */
	BaseCommand baseCommand = new BaseCommand();
    public boolean callfunc(int times,FrameModel recModel, FrameModel sendModel,SjlcDto sjlcDto) {
    	try {
			if(CommandDetails.SEQ_FIRST.equals(recModel.getMsgType())){
				synchronized (PcrMaterialModel.class) {
					SimpleDateFormat format = new SimpleDateFormat("HH:mm");
					SeqMaterialModel seqMaterialModel = new SeqMaterialModel();
					seqMaterialModel.setDeviceid(recModel.getDeviceID());
					SeqMaterialModel seqMaterialModelN = InstrumentMaterialGlobal.getSeqMaterial(seqMaterialModel);
					seqMaterialModelN.setStartTime(format.format(new Date()));
					InstrumentMaterialGlobal.setSeqMaterial(seqMaterialModelN, false);
				}
			}
			// 获取命令对象
			FsgcmlDto fsgcmlDto = new FsgcmlDto();
			if (StringUtil.isNotBlank(sendModel.getMlid())) {
				//TODO
			}
			if(recModel.getCmd().equals("OK")) {
				return doBackFunc(recModel, sendModel, fsgcmlDto);

			}else {
				return false;
			}
		}catch (Exception e){
			log.error(String.format("Seq命令回调检查错误%s", e.getMessage()));
			log.error(String.format("出错流程类型id%s", sjlcDto.getLclx()));
			log.error(String.format("出错事件id%s", sjlcDto.getSjid()));
		}
		return true;
    }
	
}

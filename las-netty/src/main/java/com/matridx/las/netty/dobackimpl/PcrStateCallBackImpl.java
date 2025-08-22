package com.matridx.las.netty.dobackimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.BaseCommand;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.dao.entities.FsgcmlDto;
import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class PcrStateCallBackImpl extends BaseCallBackImpl {


	/**
	 * pcr回调函数回调处理,获取状态，获取数据等
	 * 
	 * @param times     返回次数：1 第一次 2 第二次
	 * @param recModel  返回的帧信息
	 * @param sendModel 发送的帧信息
	 * @return 返回结果
	 */
	//BaseCommand baseCommand = new BaseCommand();

	public boolean callfunc(int times, FrameModel recModel, FrameModel sendModel,SjlcDto sjlcDto) {

		try {
			// 获取命令对象
			FsgcmlDto fsgcmlDto = new FsgcmlDto();
			if (StringUtil.isNotBlank(sendModel.getMlid())) {
					//ToDo
			}
			return doBackFunc(recModel, sendModel, fsgcmlDto);
		}catch (Exception e){
			log.error(String.format("PcrState命令回调检查错误%s", e.getMessage()));
			log.error(String.format("出错流程类型id%s", sjlcDto.getLclx()));
			log.error(String.format("出错事件id%s", sjlcDto.getSjid()));
		}
		return  true;
	}
}

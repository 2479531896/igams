package com.matridx.las.netty.error;

import com.matridx.las.netty.channel.command.BaseCommand;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.domain.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.global.RobotManagementGlobal;
import com.matridx.las.netty.global.InstrumentStateGlobal;

/**
 * 处理异常状态
 * 
 * @author DELL
 *
 */
public class ExceptionHandlingUtil extends Exception {
	private Logger log = LoggerFactory.getLogger(SendBaseCommand.class);

	public ExceptionHandlingUtil(JSONObject jsonObject) {
		// super(jsonObject.get("msg").toString());
		handlingAfterException(jsonObject);
	}

	public void handlingAfterException(JSONObject jsonObject) {
		// 先将仪器清空
		if (jsonObject.get("msg") != null)
			log.error(jsonObject.get("msg").toString());
		if (jsonObject.get("deviceid") != null)
			InstrumentStateGlobal.putInstrumentQueuesToRedis(jsonObject.get("commd").toString(), jsonObject.get("deviceid").toString());
		if (jsonObject.get("jqrid") != null)
			InstrumentStateGlobal.putInstrumentQueuesToRedis(Command.AGV.toString(), jsonObject.get("deviceid").toString());
	}
	public void sendMessageAgain(FrameModel frameModel){
		//重新发送消息三次
		if(frameModel.getHowTimes()<4){
			BaseCommand  baseCommand = frameModel.getSendBaseCommand();
			if(baseCommand!=null){
				frameModel.setHowTimes(frameModel.getHowTimes()+1);
				frameModel.setFirstSend(true);
				baseCommand.sendByNewThread(frameModel);
			}else{
				//发送钉钉消息给相应的人员
			}
		}
	}
}

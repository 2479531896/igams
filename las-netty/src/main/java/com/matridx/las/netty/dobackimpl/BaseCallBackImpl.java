package com.matridx.las.netty.dobackimpl;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.ICallBack;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.FsgcmlDto;
import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.global.RobotManagementGlobal;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.service.svcinterface.IYqztxxService;
import com.matridx.las.netty.util.CommonChannelUtil;
import com.matridx.las.netty.util.SpringUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseCallBackImpl implements ICallBack {
	
	ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	Logger log = LoggerFactory.getLogger(BaseCallBackImpl.class);
	/**
	 * 初始化机器人和仪器
	 */
	public void initYqxx( String robotid, String[] comm,Map<String,String> ma) {
		if (StringUtil.isNotBlank(robotid)) {
			//去充电了也要将其从map中移除
			//新启一个线程
			if(ma!=null) {
				ma.remove(Command.AGV.toString());
			}
//			Runnable newRunnable = new Runnable() {
//				public void run() {
//					RobotManagementGlobal.isNextTaskAndElectricQuantity(robotid);
//				}
//			};
			//更换为lambad表达式
			Runnable newRunnable = () -> RobotManagementGlobal.isNextTaskAndElectricQuantity(robotid);
			cachedThreadPool.execute(newRunnable);
		}
		if (comm!=null&&comm.length>0&&ma!=null) {
			for (String k:comm) {
			//释放仪器时，将仪器移除历史记录map
			String id = ma.get(k);
			if(StringUtil.isNotBlank(id)) {
				ma.remove(k);
			}
			InstrumentStateGlobal.changeInstrumentState(k,id, InstrumentStatusEnum.STATE_FREE.getCode());
			//yqztxxService.updateStYqztxx(yqid, InstrumentStatusEnum.STATE_FREE.getCode());
			InstrumentStateGlobal.putInstrumentQueuesToRedis(k, id);
			}
		}
		//空的移除
		if(ma!=null&&ma.isEmpty()) {
			InstrumentStateGlobal.removeInstrumentUsetMap(ma);
		}

	}

	/**
	 * 处理返回信息
	 * 
	 * @param recModel 返回命令
	 * @param sendModel 发送命令
	 * @return 返回结果
	 */
	public boolean doBackFunc(FrameModel recModel, FrameModel sendModel, FsgcmlDto fsgcmlDto) {
		IYqztxxService yqztxxService = (IYqztxxService) SpringUtil.getBean("yqztxxServiceImpl");
		if(!recModel.getCommand().equals(Command.AGV.toString())) {
			InstrumentStateGlobal.changeInstrumentState(recModel.getCommand(),recModel.getDeviceID(), fsgcmlDto.getJszt());
			yqztxxService.updateStYqztxx(recModel.getDeviceID(), fsgcmlDto.getJszt());
		}
		// 判断这个命令结束后需要释放机器人 和释放仪器
		//获取流程的map
		Map<String, String> ma = InstrumentStateGlobal.getInstrumentUsedListMap(recModel.getCommand(), recModel.getDeviceID());
		boolean isFreeYq = fsgcmlDto.getSfjsghyq().equals("1");
		boolean isFreeJqr = fsgcmlDto.getSfjsghjqr().equals("1");
		String robotid = "";
		if (isFreeJqr) {
			robotid = CommonChannelUtil.getDeviceIDByCommand(Command.AGV.toString());
		}
		String[] comm = {};
		if (isFreeYq) {
			comm = fsgcmlDto.getGhyqlx().split(",");
		}
		// 这里不能释放仪器，过程为完成
		initYqxx( robotid,comm, ma);

		return true;
	}


	@Override
	public boolean callfunc(int times, FrameModel recModel, FrameModel sendModel, SjlcDto sjlcDto) {
		return false;
	}
}
package com.matridx.las.netty.channel.command;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.global.CommandParmGlobal;
import com.matridx.las.netty.service.svcinterface.ISjlcService;
import com.matridx.las.netty.util.CommonChannelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.JkdymxDto;
import com.matridx.igams.common.dao.entities.WkmxPcrModel;
import com.matridx.igams.common.enums.InvokingChildTypeEnum;
import com.matridx.igams.common.enums.InvokingTypeEnum;
import com.matridx.igams.common.util.HttpUtil;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.channel.domain.CommandDetails;
import com.matridx.las.netty.channel.server.handler.PcrProtocolHandler;

import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.util.SpringUtil;

public class SendBaseCommand extends BaseCommand {

	private Logger log = LoggerFactory.getLogger(SendBaseCommand.class);

	/**
	 * 发送给机器人，查看电量
	 *
	 * @param command
	 * @param deviceID
	 * @param msgType
	 * @param cmdCode
	 * @param param
	 * @param mapBack
	 * @return
	 */
	public boolean getRobotElectricQuantity(Byte command, String deviceID, String msgType, String cmdCode,
											JSONObject param, ICallBack mapBack) {
		JSONObject jsObject = new JSONObject();
		String[] parm = {jsObject.toJSONString()};
		String threadName = Thread.currentThread().getName();
		System.out.print(threadName);
		boolean result = this.sendByCmdModel(Command.AGV.toString(), deviceID, CommandDetails.AVG_GE, "GE", parm,
				mapBack, false, false);
		return result;
	}

	/**
	 * 发送给机器人让他去充电
	 *
	 * @param command
	 * @param deviceID
	 * @param msgType
	 * @param cmdCode
	 * @param param
	 * @param mapBack
	 * @return
	 */
	public boolean goRobotElectricQuantity(Byte command, String deviceID, String msgType, String cmdCode,
										   JSONObject param, ICallBack mapBack) {
		JSONObject jsObject = new JSONObject();
		String[] parm = {jsObject.toJSONString()};
		String threadName = Thread.currentThread().getName();
		System.out.print(threadName);
		boolean result = this.sendByCmdModel(Command.AGV.toString(), deviceID, CommandDetails.AVG_GO, "GO", parm,
				mapBack, false, false);
		return result;
	}

	/**
	 * 发送给机器人停止充电
	 *
	 * @param deviceID
	 * @param mapBack
	 * @param isSyn
	 * @return
	 */
	public boolean stopRobotElectricQuantity(String deviceID, ICallBack mapBack, boolean isSyn) {
		JSONObject jsObject = new JSONObject();
		String[] parm = {jsObject.toJSONString()};
		boolean result = this.sendByCmdModel(Command.AGV.toString(), deviceID, CommandDetails.AVG_ST, "ST", parm,
				mapBack, isSyn, true, Thread.currentThread().getName());
		return result;
	}

	/**
	 * 对接PCR的字段，发送给pcr
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean sendLibrary(WkmxPcrModel wkmxPcrModel) {
		if (wkmxPcrModel != null) {
			PcrProtocolHandler pcrProtocolHandler = (PcrProtocolHandler) SpringUtil.getBean("pcrProtocolHandler");
			String pcrResult = JSON.toJSONString(wkmxPcrModel);
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			JkdymxDto jkdymxDto = new JkdymxDto();
			//发送信息
			String result2 = null;
			String url = "";
			if ("0".equals(wkmxPcrModel.getIspcr())) {
				url = pcrProtocolHandler.pcrUrlDate;

			} else {
				url = pcrProtocolHandler.wkUrlDate;
			}
			try {
				jkdymxDto.setLxqf("send");
				jkdymxDto.setDysj(dateFm.format(new Date()));
				//jkdymxDto.setYwid(inspinfo.getYbbh());
				jkdymxDto.setNr(pcrResult);
				jkdymxDto.setDydz(url);
				jkdymxDto.setDyfl(InvokingTypeEnum.INVOKING_WKMX.getCode());
				jkdymxDto.setDyzfl(InvokingChildTypeEnum.INVOKING_CHILD_PCR_RESULT.getCode());
				jkdymxDto.setSfcg("2");
				log.info("pcr结果开始上报到OA系统："+pcrResult);
				log.info("pcr结果上报url："+url );
				result2 = HttpUtil.post(url, "", pcrResult);
				log.info(result2);
			} catch (Exception e) {
				log.error(e.getMessage());
				return false;

			}

		}

		return true;
	}






	/**
	 * 将事件放入队列
	 * @param flowType
	 * @param frameModel
	 * @return
	 */
	public  boolean sendEventFlowlist(String flowType, FrameModel frameModel){
		if(InstrumentStatusEnum.STATE_PAUSE.getCode().equals(CommandParmGlobal.getIsSuspend())){
			log.error("系统已暂停，不能继续操作");
			return false;
		}
		ISjlcService eventFlowService = (ISjlcService) SpringUtil.getBean("sjlcServiceImpl");
		SjlcDto sjlcDto = new SjlcDto();
		sjlcDto.setLclx(flowType);
		List<SjlcDto> list = eventFlowService.getSjlcList(sjlcDto);
		Map<String,String> ma = null;
		//先获取流程中的仪器值
		if(frameModel!=null) {
			if (Command.CUBICS.toString().equals(frameModel.getCommand())) {
				ma = InstrumentStateGlobal.getInstrumentUsedListMap(Command.CUBICS.toString(), frameModel.getPassId());
			} else {
				ma = InstrumentStateGlobal.getInstrumentUsedListMap(frameModel.getCommand(), frameModel.getDeviceID());
			}
		}
		if(ma==null){
			ma = new HashMap<String,String>();
			InstrumentStateGlobal.setInstrumentUsedList(ma);
		}
		if(list==null||list.size()==0){
			log.error("根据流程类型："+ flowType +",未能找到事件");
			return  false;
		}
        String threadName = Thread.currentThread().getName();
		//开始前先清除掉同线程名称已发送的命令和流程
		BlockingQueue<SjlcDto> t_haveSendeventList = getHaveSendeventDic().get(threadName);
		getHaveSendeventDic().remove(threadName);
		getHaveSendcmdDic().remove(threadName);
		//将流程对像放入整体记录list里面
		CommonChannelUtil.addLcBaseCommandList(this);
		for(SjlcDto sj:list){
			try {
				putEventToQueue(sj,threadName,ma);
			} catch (InterruptedException e) {
				log.error(e.getMessage());

			}
		}

		return true;
	}
}

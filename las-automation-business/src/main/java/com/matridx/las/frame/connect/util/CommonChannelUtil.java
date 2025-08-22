package com.matridx.las.frame.connect.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.las.frame.connect.channel.service.*;
import com.matridx.las.frame.connect.enums.ChannelStatusEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.matridx.las.frame.connect.channel.command.BaseCommand;
import com.matridx.las.frame.connect.channel.command.FrameModel;
import com.matridx.las.frame.connect.channel.command.IObserver;
import com.matridx.las.frame.connect.channel.domain.Command;
import com.matridx.las.frame.connect.enums.GlobalrouteEnum;
import com.matridx.las.frame.connect.svcinterface.IConnectService;
import com.matridx.las.frame.connect.svcinterface.IHttpService;
import com.matridx.springboot.util.base.StringUtil;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class CommonChannelUtil {

	private static Logger log = LoggerFactory.getLogger(CommonChannelUtil.class);

	// 暂停标记
	private static String systemState="0";


	public static String getSystemState() {
		return systemState;
	}

	public static void setSystemState(String systemState) {
		CommonChannelUtil.systemState = systemState;
	}
	// 锁对象
	private static Object object = new Object();
	// 通道管理(已注册) 第一层为 area  第二层为 设备类型   第三层为设备清单
	private static Map<String, Map<String, List<IHttpService>>> registerChannels = new HashMap<String,Map<String, List<IHttpService>>>();
	// 建库仪已上线的通到
	private static Map<String, List<String>> registerCubics = new HashMap<String, List<String>>();
	// 通道管理(未注册)
	private static Map<String, IHttpService> unRegisterChannels = new HashMap<String, IHttpService>();
	// 协议类信息
	public static Map<String, IConnectService> protocols = new HashMap<String, IConnectService>();
	
	//添加流程管理存的是basecommand队对像,即每启动一个流程就放进来，流程结束清除
	private static List<BaseCommand> lcBaseCommandList = new ArrayList<>();
//	//读取到的帧队列(一次返回)
//	protected static Map<String,BlockingQueue<FrameModel>> recvQueues = new HashMap<>();
//	//读取到的帧队列(二次返回)
//	protected static Map<String,BlockingQueue<FrameModel>> secRecvQueues = new HashMap<>();
	// 系统区域设置
	private static String netty_area;
	@Value("${matridx.netty.area:01}")
	public void setNettyArea(String netty_area) {
		CommonChannelUtil.netty_area = netty_area;
	}
	// 是否桥接

	private static boolean bridgingflg = false;
	@Value("${matridx.netty.bridgingflg:false}")
	public void setBridgingflg(boolean bridgingflg) {
		CommonChannelUtil.bridgingflg = bridgingflg;
	}
	@Autowired
	private IXtszService t_xtszService;//不可用
	private static IXtszService xtszService;
	@PostConstruct
	public void initXtszService(){
		xtszService = this.t_xtszService;
	}
	/**
	 * 新增协议信息处理类
	 *
	 * @param type	1、2、3 ...
	 * @param handler
	 */
	public static void addProtocol(String type, IConnectService handler) {
		protocols.put(type, handler);
	}
	public static Map<String, IConnectService> getProtocols() {
		return protocols;
	}
	/**
	 * commend 设备上线时，把相应的通道记录到 未注册列表里
	 *
	 * @param incoming
	 * @return
	 */
	public static boolean addChannel(String area,String key,IHttpService incoming) {
		// 判断，加入到 list 中
		synchronized (object) {
			// 如果未包含，
			if (!unRegisterChannels.containsKey(area + ":" + key)) {
				unRegisterChannels.put(area + ":" + key, incoming);
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * commend 处理分发上线的通道，注明通道的deviceID和 协议类型
	 *
	 * @param key  IP
	 * @param nettychannel  回调类
	 * @return
	 */
	public static boolean handleChannel(String key,IHttpService nettychannel) {
		// 判断，加入到 list 中
		synchronized (object) {
			String area  = nettychannel.getChannelModel().getArea();
			IHttpService unChnChannel = unRegisterChannels.get(area + ":" + key);

			// 如果未包含，日志里进行警告
			if (unChnChannel == null) {
				//registerChannels.put(recModel.getCommanddeviceid(), channelModel);
				log.error("存在未连接却直接注册的情况 Commanddeviceid：" + nettychannel.getChannelModel().getCommanddeviceid());
			} else {
				unRegisterChannels.remove(area + ":" + key);
			}
			
			resetRegisterChannels(nettychannel);
		}
		return true;
	}

	/**
	 * commend 设备下线后 ，删除相应的通道记录
	 *
	 * @param area 区域
	 * @param key ip
	 * @param service 服务
	 * @return
	 */
	public static IConnectService removeChannel(String area,String key,IHttpService service) {
		// 判断，从 list 中移除
		synchronized (object) {
			unRegisterChannels.remove(area + ":" + key);
			Map<String, List<IHttpService>> areaMap = registerChannels.get(area);
			if(areaMap == null)
				return null;
			for (String command : areaMap.keySet()) {
				List<IHttpService> deviceList = areaMap.get(command);
				if(deviceList == null || deviceList.size() ==0)
					return null;
				Iterator<IHttpService> iterator = deviceList.iterator();
				while (iterator.hasNext()) {
					IHttpService model = iterator.next();
					if (service.getChannel() == model.getChannel()) {
						iterator.remove();
						// 同时注销仪器的状态和队列
						//如果是建库仪则需要将通道全部下线
						if (Command.CUBICS.toString().equals(model.getChannelModel().getProtocol())) {
							//List<String> cubsList = getCubsList(model.getDeviceId());
							//注销时移除所有的建库仪通道
							registerCubics.remove(model.getChannelModel().getCommanddeviceid());
							
						}
						return model;
					}
				}
			}
		}

		return null;
	}
	/**
	 * commend 设备注销 http
	 */
	public static IConnectService removeChannel(String area,String commanddeviceid,String key) {
		// 判断，从 list 中移除
		synchronized (object) {
			unRegisterChannels.remove(area + ":" + key);
			Map<String, List<IHttpService>> areaMap = registerChannels.get(area);
			if(areaMap == null)
				return null;
			for (String command : areaMap.keySet()) {
				List<IHttpService> deviceList = areaMap.get(command);
				if (!CollectionUtils.isEmpty(deviceList)) {
					Iterator<IHttpService> iterator = deviceList.iterator();
					while (iterator.hasNext()) {
						IHttpService model = iterator.next();
						if (model.getChannelModel().getCommanddeviceid().equals(commanddeviceid)) {
							iterator.remove();
							// 同时注销仪器的状态和队列
							//如果是建库仪则需要将通道全部下线
							if (Command.CUBICS.toString().equals(model.getChannelModel().getProtocol())) {
								//List<String> cubsList = getCubsList(model.getDeviceId());
								//注销时移除所有的建库仪通道
								registerCubics.remove(model.getChannelModel().getCommanddeviceid());

							}
							return model;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 重新整理已注册的信息Map
	 * @param channelservice
	 */
	private static void resetRegisterChannels(IHttpService channelservice) {
		
		if (bridgingflg){
			Map<String, String> paramMap= new HashMap<>();
			paramMap.put("channelservice", JSON.toJSONString(channelservice));
			//发送信息到主服务器
			ConnectUtil.mainRegisterChannels(paramMap);
		}
		
		addToRegisterChannels(channelservice);
	}
	
	/**
	 * 把通道信息注册到队列中
	 * @param channelservice
	 * @return
	 */
	private static boolean addToRegisterChannels(IHttpService channelservice) {
		//每个服务器的区域是不一样的
		Map<String, List<IHttpService>> areaMap= registerChannels.get(channelservice.getChannelModel().getArea());
		//Map 里不存在该区域时候
		if(areaMap == null) {
			areaMap = new HashMap<String, List<IHttpService>>();
			List<IHttpService> deviceList = new ArrayList<IHttpService>();
			deviceList.add(channelservice);
			areaMap.put(channelservice.getChannelModel().getProtocol(), deviceList);
			registerChannels.put(channelservice.getChannelModel().getArea(), areaMap);
			return true;
		}
		List<IHttpService> deviceList = areaMap.get(channelservice.getChannelModel().getProtocol());
		//区域Map里不存在该协议时候
		if(deviceList == null) {
			deviceList = new ArrayList<IHttpService>();
			deviceList.add(channelservice);
			areaMap.put(channelservice.getChannelModel().getProtocol(), deviceList);
			registerChannels.put(channelservice.getChannelModel().getArea(), areaMap);
			return true;
		}
		//该协议的List里已经存在该设备时，直接返回
		for(int i=0;i<deviceList.size();i++) {
			IConnectService device = deviceList.get(i);
			if(device.getChannelModel().getCommanddeviceid().equals(channelservice.getChannelModel().getCommanddeviceid())) {
				//重新注册后重置错误次数 并且设置状态为空闲
				device.getChannelModel().setStatus(ChannelStatusEnum.FREE.getCode());
				device.getChannelModel().setErrorCount(0);
				return true;
			}
		}
		deviceList.add(channelservice);
		return true;
	}
	
	/**
	 * 根据接收信息确认之前注册的Map是否已经存在,需要用到区域area,协议command,协议设备Commanddeviceid
	 * @param recModel
	 * @return
	 */
	public static IHttpService getChannelByFrame(FrameModel recModel) {
		Map<String, List<IHttpService>> areaMap = registerChannels.get(recModel.getArea());
		if(areaMap==null)
			return null;
		
		List<IHttpService> deviceList = areaMap.get(recModel.getCommand());
		if(deviceList == null)
			return null;
		
		for(int i=0;i<deviceList.size();i++) {
			IHttpService device = deviceList.get(i);
			if( StringUtil.isNotBlank(recModel.getDeviceID())) {
				//如果设备状态为错误则不使用该设备 返回null 或者是注销接口 则不考虑状态是否错误(LO为注销命令)
				if(device.getChannelModel().getCommanddeviceid().equals(recModel.getCommanddeviceid())&&
						!ChannelStatusEnum.ERROR.getCode().equals(device.getChannelModel().getStatus())){
					return device;
				}
			}else {
				//是否空闲
				if (ChannelStatusEnum.FREE.getCode().equals(device.getChannelModel().getStatus())){
					return device;
				}
			}
		}
	/*	if(!recModel.isAppoint()&&deviceList.size() > 0)
		{
			return deviceList.get(0);
		}*/
		
		return null;
	}

	/**
	 * 准备发送消息，根据Model 整理发送信息，获取发送通道。注册相应的回调队列。
	 * 并根据设置发送命令信息和状态到主服务器上
	 * 之后调用方法发送命令
	 * @param sendModel
	 */
	public static Map<String, Object> sendMessage(FrameModel sendModel) {
		try {
			while (GlobalrouteEnum.SYSTEM_STATE_SUSPEND.getCode().equals(CommonChannelUtil.getSystemState())) {
				Thread.sleep(100);
			}
		} catch (Exception e) {

		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "fail");

		if (sendModel == null)
			return map;

		String params = "";
		String[] cmdParam = sendModel.getCmdParam();
		if (cmdParam != null && cmdParam.length > 0) {
			for (int i = 0; i < cmdParam.length; i++) {
				if (StringUtil.isNotBlank(cmdParam[i])) {
					params += " " + cmdParam[i];
				}
			}
			if (StringUtil.isBlank(params)) {
				params = "";
			}
			sendModel.setMsgInfo(params);
		}
		IHttpService channelservice = getChannelByFrame(sendModel);

		if (channelservice == null) {
			log.error("已注册的channel为空或状态为错误！");
			map.put("status", "fail");
			return map;
		}
		
		//根据返回的通道信息，设置发送Model的类型，尤其是内部的设备ID等，需要用空闲设备来填充
		setFrameByChannelMode(channelservice,sendModel);
		//创建帧ID
		generateFrameID(sendModel);
		Map<String, BlockingQueue<FrameModel>> recvQueuesN = new HashMap<>();
		if (protocols.get(sendModel.getCommand())!=null){
			recvQueuesN = protocols.get(sendModel.getCommand()).getRecvQueues();
		}
		Map<String, BlockingQueue<FrameModel>> secRecvQueuesN = new HashMap<>();
		if (protocols.get(sendModel.getCommand())!=null){
			secRecvQueuesN = protocols.get(sendModel.getCommand()).getSecRecvQueues();
		}
		// 发送消息之前确认第一次返回的队列是否为空，如果为空，则设置空队列
		String rec_frameId = CommonChannelUtil.getFrameIdFromSend(sendModel);

		BlockingQueue<FrameModel> recvQueue = recvQueuesN.get(rec_frameId);
		if (recvQueue == null) {
			recvQueue = new LinkedBlockingQueue<FrameModel>();
			recvQueuesN.put(rec_frameId, recvQueue);
		}
		// 发送消息之前确认第二次返回的队列是否为空，如果为空，则设置空队列
		BlockingQueue<FrameModel> secRecvQueue = secRecvQueuesN.get(rec_frameId);
		if (secRecvQueue == null) {
			secRecvQueue = new LinkedBlockingQueue<FrameModel>();
			secRecvQueuesN.put(rec_frameId, secRecvQueue);
		}
		//判断若调用的service为HTTP请求的则发送命令后将设备状态设置为空闲
		channelservice.getChannelModel().setStatus(ChannelStatusEnum.BUSY.getCode());
		
		if (bridgingflg&&!(channelservice instanceof HttpService)){
			//发送信息到主服务器
			Map<String, String> paramMap= new HashMap<>();
			sendModel.setCommanddeviceid(channelservice.getChannelModel().getCommanddeviceid());
			paramMap.put("frameModel", JSON.toJSONString(sendModel));
			paramMap.put("Status", ChannelStatusEnum.BUSY.getCode());
			ConnectUtil.updateMainChannelStatus(paramMap);
		}
		boolean isSuccess = sendMessgeByChannel(channelservice, sendModel, params);
		log.info(sendModel.getCommand() + "&" + sendModel.getFrameID() + " " + sendModel.getCmd() + params);
		map.put("status", isSuccess ? "success" : "fail");
		return map;
	}
	
	/**
	 * 根据获取到的通道信息发送命令
	 * @param channelservice
	 * @param frameModel
	 * @param params
	 * @return
	 */
	public static boolean sendMessgeByChannel(IHttpService channelservice,FrameModel frameModel,String params){
		boolean isSuccess = false;
		try {
			isSuccess = channelservice.sendMessage(frameModel,
					frameModel.getCommand() + "&" + frameModel.getFrameID() + " " + frameModel.getCmd() + params);
		}catch (Exception e){
			log.error("发送消息出现异常!!! e:{}",e.getMessage());
		}
		//如果错误 更新错误次数
		if(!isSuccess){
			//默认5次
			int maxErrorCount = 5;
			channelservice.getChannelModel().setErrorCount(channelservice.getChannelModel().getErrorCount()+1);
			XtszDto xtszDto = xtszService.getDtoById(GlobalString.CHANNEL_ERROR_MAX_COUNT);
			if(xtszDto!=null) {
				maxErrorCount = Integer.parseInt(xtszDto.getSzz());
			}
			//如果超过最大错误次数
			if (channelservice.getChannelModel().getErrorCount()>=maxErrorCount){
				channelservice.getChannelModel().setStatus(ChannelStatusEnum.ERROR.getCode());
				if (bridgingflg){
					//发送信息到主服务器
					Map<String, String> paramMap= new HashMap<>();
					paramMap.put("frameModel", JSON.toJSONString(frameModel));
					paramMap.put("Status", ChannelStatusEnum.ERROR.getCode());
					ConnectUtil.updateMainChannelStatus(paramMap);
				}
			}
		}
		return isSuccess;
	}
	/**
	 * 发送netty消息公用方法
	 *
	 * @param channel
	 * @param sendModel
	 */
	public static Map<String, Object> sendMessageByChannel(Channel channel, FrameModel sendModel) {
		try {
			//如果系统暂停状态，则命令进入等待状态
			while (GlobalrouteEnum.SYSTEM_STATE_SUSPEND.getCode().equals(CommonChannelUtil.getSystemState())) {
				Thread.sleep(100);
			}
		} catch (Exception e) {
			log.error("系统暂停出现问题:" + e.getMessage());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "fail");
		if (sendModel == null)
			return map;
		String params = "";
		String[] cmdParam = sendModel.getCmdParam();
		if (cmdParam != null && cmdParam.length > 0) {
			for (int i = 0; i < cmdParam.length; i++) {
				params += " " + cmdParam[i];
			}
			// msgInfo.substring(1);
			sendModel.setMsgInfo(params);
		}

		generateFrameID(sendModel);
		channel.writeAndFlush(
				sendModel.getCommand() + "&" + sendModel.getFrameID() + " " + sendModel.getCmd() + params);
		System.err.println(sendModel.getCommand() + "&" + sendModel.getFrameID() + " " + sendModel.getCmd() + params);
		log.info(sendModel.getCommand() + "&" + sendModel.getFrameID() + " " + sendModel.getCmd() + params);

		map.put("status", "success");
		return map;
	}

	/**
	 * command 根据发送消息设置帧ID
	 *
	 * @param sendModel
	 * @return
	 */
	public static boolean generateFrameID(FrameModel sendModel) {
		String deviceId = sendModel.getDeviceID();
		StringBuffer sBuffer = new StringBuffer();
		if (sendModel.isSendType())
			sBuffer.append("S");
		else {
			sBuffer.append("R");
		}
		sBuffer.append(StringUtil.leftPad(deviceId, 5,'0'));
		String msgType = sendModel.getMsgType();
		if (StringUtil.isBlank(msgType))
			msgType = "00";
		sBuffer.append(msgType);
		if (sendModel.isSyncHope()) {
			sBuffer.append("A");
		} else {
			sBuffer.append("S");
		}

		sendModel.setFrameID(sBuffer.toString());
		return true;
	}
	
	/**
	 * 根据返回的设备通道信息设置帧的内容（设备ID等）
	 * @param channeService
	 * @param sendModel
	 */
	private static void setFrameByChannelMode(IConnectService channeService,FrameModel sendModel) {
		if(channeService == null || sendModel == null)
			return;
		sendModel.setDeviceID(channeService.getChannelModel().getDeviceId());
		sendModel.setCommanddeviceid(channeService.getChannelModel().getCommanddeviceid());
	}

	/**
	 * 注册观察者
	 *
	 * @param t_cmdModel
	 * @param threadName
	 * @param observer
	 */
	public static void registerCmdObserver(FrameModel t_cmdModel, String threadName, IObserver observer) {
		
		IConnectService channelModel = getChannelByFrame(t_cmdModel);
		if (t_cmdModel.getCommanddeviceid() == null) {
			log.info(String.format("仪器" + t_cmdModel.getCommanddeviceid() + "为null"));
		}
		IConnectService handler = CommonChannelUtil.protocols.get(channelModel.getChannelModel().getProtocol());
		if (t_cmdModel.getCommand().equals(Command.CUBICS.toString()) && StringUtil.isNotBlank(t_cmdModel.getPassId())) {
			handler.registerCmdObserver(threadName, t_cmdModel.getCommanddeviceid() + t_cmdModel.getPassId(), observer);
		} else {
			handler.registerCmdObserver(threadName, t_cmdModel.getCommanddeviceid(), observer);
		}
	}

	/**
	 * 从发送消息里获取返回的帧ID
	 *
	 * @param sendModel
	 * @return
	 */
	public static String getFrameIdFromSend(FrameModel sendModel) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("R");
		sBuffer.append(StringUtil.leftPad(sendModel.getDeviceID(), 5, '0'));
		String msgType = sendModel.getMsgType();
		if (StringUtil.isBlank(msgType))
			msgType = "00";
		sBuffer.append(msgType);
		if (sendModel.isSyncHope()) {
			sBuffer.append("A");
		} else {
			sBuffer.append("S");
		}

		return sBuffer.toString();
	}

	/**
	 * 从已注册的通道中获取指定的设备id
	 *
	 * @param
	 * @return
	 */
	/*public static String getDeviceIDByCommand(String area,String commd) {
		
		Map<String, List<ChannelModel>> areaMap = registerChannels.get(area);
		if(areaMap==null)
			return null;
		
		List<ChannelModel> deviceList = areaMap.get(commd);
		// 获取设备id
		for (ChannelModel device : deviceList) {
			if(InstrumentStatusEnum.STATE_FREE.getCode().equals(device.getStatus())) {
				return device.getDeviceId();
			}
		}
		return null;
	}
*/

	public static boolean removeCubs(String device, String cubsid) {
		synchronized (registerCubics) {
			//统一加上协议号
			String cdevice = Command.CUBICS.toString() + device;
			List<String> l = CommonChannelUtil.registerCubics.get(cdevice);
			if (l == null) {
				l = new ArrayList<String>();
				return true;
			}
			l.remove(cubsid);
			return true;
		}
	}

	//获取建库仪通道
	public static List<String> getCubsList(String device) {
		//统一加上协议号
		String cdevice = Command.CUBICS.toString() + device;
		return CommonChannelUtil.registerCubics.get(cdevice);
	}

	//通过建库仪通道号，获取建库仪id
	public static String getDeviceIDByCubsChannel(String cusId) {
		// 获取设备id
		for (String cdevice : registerCubics.keySet()) {
			// 判断是这个协议的设备
			List<String> list = registerCubics.get(cdevice);
			if (list != null && list.size() > 0) {
				for (String cs : list) {
					if (cusId.equals(cs)) {
						return cdevice.substring(1, cdevice.length());
					}

				}
			}
		}
		return "";
	}

	public static List<BaseCommand> getLcBaseCommandList() {
		return lcBaseCommandList;
	}

	public static void setLcBaseCommandList(List<BaseCommand> lcBaseCommandList) {

		CommonChannelUtil.lcBaseCommandList = lcBaseCommandList;
	}

	public static void addLcBaseCommandList(BaseCommand baseCommand) {
		lcBaseCommandList.add(baseCommand);
	}

	public static void removeLcBaseCommandList(BaseCommand baseCommand) {
		lcBaseCommandList.remove(baseCommand);

	}

	public static Map<String,Map<String, List<IHttpService>>> getRegisterChannels() {
		return registerChannels;
	}

	/**
	 * 将分服务器的信息同步到主服务器
	 * @param channelService
	 */
	public static void syncMainChannels(IHttpService channelService) {
		//如果同一个区域
		if (netty_area.equals(channelService.getChannelModel().getArea())){
			return;
		}
		
		synchronized (object) {
			String key = channelService.getChannelModel().getArea()+":" + channelService.getChannelModel().getAddress();
			unRegisterChannels.remove(key);
			
			addToRegisterChannels(channelService);
		}
	}
	
	/**
	 * 将分服务器的所有信息同步至主服务器
	 * @param areaMap
	 * @param nettyArea
	 */
	public static void syncAllChannelToMain(Map<String, List<IHttpService>> areaMap, String nettyArea) {
		
		if (MapUtils.isNotEmpty(areaMap)&&!netty_area.equals(nettyArea)){
			//先删除
			registerChannels.remove(nettyArea);
			
			for (String protocol : areaMap.keySet()) {
				List<IHttpService> deviceList = areaMap.get(protocol);
				if(!CollectionUtils.isEmpty(deviceList)) {
					//重新解析Ihttpservice
					List<IHttpService> newDeciceList = new ArrayList<>();
					List<JSONObject> jsonObjects = JSON.parseArray(JSON.toJSONString(deviceList), JSONObject.class);
					for (JSONObject deviceObj : jsonObjects) {
						newDeciceList.add(parseJsonToGetIHttpservice(deviceObj));
					}
					areaMap.put(protocol,newDeciceList);
					//该协议的List里存在该设备时，同步删除 通道管理(未注册) 内设备
					for (IConnectService device : newDeciceList) {
						if (device != null && StringUtil.isNotBlank(device.getChannelModel().getCommanddeviceid())) {
							String key = nettyArea + ":" + device.getChannelModel().getAddress();
							unRegisterChannels.remove(key);
						}
					}
				}
			}
			registerChannels.put(nettyArea,areaMap);
		}
	}
	/*
		解析JSON获取IHttpservice
	 */
	public static IHttpService parseJsonToGetIHttpservice(JSONObject deviceObj) {
		String str_object = deviceObj.getString("channelModel");
		ChannelModel channelModel = JSON.parseObject(str_object, ChannelModel.class);
		IHttpService service = getServiceByChannelModel(channelModel);
		service.setChannelModel(channelModel);
		return service;
	}
	/*
		通过channelModel获取对应Service
	 */
	@NotNull
	public static IHttpService getServiceByChannelModel(ChannelModel channelModel) {
		IHttpService service = null;
		switch (channelModel.getProtocol()) {
			case "1"://Cubics
				service = new NemoService();
				break;
			case "2"://SMAP
				service = new SmapService();
				break;
			case "3"://天隆
				service = new HttpPcrService();
				break;
			case "4"://测序仪
				service = new HttpPrintService();
				break;
			case "5"://打印机
				service = new HttpPrintService();
				break;
			case "6"://nemo
				service = new NemoService();
				break;
			case "7"://nemo
				service = new PrintService();
				break;
			default://默认
				service = new DefaultService();
				break;
		}
		return service;
	}
	/**
	 * commend 设备下线后 ，删除相应的主服务器信息
	 *
	 */
	public static boolean removeMainChannel(String area,String ip,String protocol,String commanddeviceid) {
		// 判断，从 list 中移除
		synchronized (object) {
			unRegisterChannels.remove(area + ":" + ip);
			
			if(StringUtil.isNotBlank(commanddeviceid)) {
				Map<String, List<IHttpService>> areaMap = registerChannels.get(area);
				if(areaMap ==null)
					return true;
				
				List<IHttpService> deviceList = areaMap.get(protocol);
				for (IHttpService model : deviceList) {
					if(model!=null && model.getChannelModel().getCommanddeviceid().equals(commanddeviceid)) {
						deviceList.remove(model);
						return true;
					}
				}
			}
		}
		return true;
	}
}

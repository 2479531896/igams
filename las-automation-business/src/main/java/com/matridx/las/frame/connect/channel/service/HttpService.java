package com.matridx.las.frame.connect.channel.service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.matridx.las.frame.connect.channel.command.FrameModel;
import com.matridx.las.frame.connect.channel.command.IObserver;
import com.matridx.las.frame.connect.channel.domain.Command;
import com.matridx.las.frame.connect.enums.ChannelStatusEnum;
import com.matridx.las.frame.connect.svcinterface.IConnectService;
import com.matridx.las.frame.connect.svcinterface.IHttpService;
import com.matridx.las.frame.connect.util.ChannelModel;
import com.matridx.las.frame.connect.util.CommonChannelUtil;
import com.matridx.las.frame.connect.util.ConnectUtil;
import com.matridx.springboot.util.base.StringUtil;

public class HttpService extends BaseService implements IHttpService{

	private static Logger log = LoggerFactory.getLogger(HttpService.class);
	//设备调用地址
	protected String address;
	//设备确认地址
	protected String confirm_address;
	//是否需要确认设备状态
	protected boolean confirmflg;
	
	@Override
	public boolean sendMessage(FrameModel sendModel,String info) {
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(info) || StringUtil.isBlank(address))
			return false;
		String[] s_spliStrings = info.split(" ");
		if(s_spliStrings.length < 2)
			return false;
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		
		String urlString =  (address.startsWith("http")?"":"http://") + address + (address.endsWith("/")?"":"/") + s_spliStrings[1];
		
		for(int i=2;i<s_spliStrings.length;i++) {
			if(i==2)
				urlString = urlString + "?";
			else
				urlString = urlString + "&";
			urlString = urlString + "pa"+i + "=" + s_spliStrings[i];
			//paramMap.add("pa"+i, s_spliStrings[i]);
		}
		//PCR这边采用的是 http://ip/code?参数 code 为dll方法 
		String resultString = ConnectUtil.sendConnectRequest(urlString ,paramMap,String.class);
		if(resultString==null) {
			return false;
		}
		Type type = new TypeToken<Map<String,String>>() {}.getType();
		Map<String,String> m_result = JSON.parseObject(resultString, type);
		
		String rec_frameId = CommonChannelUtil.getFrameIdFromSend(sendModel);
		//设置返回的frameid
		m_result.put("frameid", rec_frameId);
		//设置返回的协议
		m_result.put("command", sendModel.getCommand());
		
		return recvBackCmdByString(m_result);
	}

	@Override
	public Object getChannel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean init(Map<String, Object> map) {
		// TODO Auto-generated method stub
		confirm_address=map.get("ztqrdz")==null?"":String.valueOf(map.get("ztqrdz"));
		address = map.get("address")==null?"":String.valueOf(map.get("address"));
		confirmflg= StringUtil.isNotObjectBank(map.get("confirmflg")) && Boolean.parseBoolean(String.valueOf(map.get("confirmflg")));
		String netty_area = map.get("netty_area")==null?"":String.valueOf(map.get("netty_area"));
		boolean bridgingflg = StringUtil.isNotObjectBank(map.get("bridgingflg"))?(Boolean) map.get("bridgingflg"):false;
		String deviceid = map.get("deviceid")==null?"":String.valueOf(map.get("deviceid"));
		String protocol = map.get("protocol")==null?"":String.valueOf(map.get("protocol"));
		log.error("HttpService-init:{}",JSON.toJSONString(map));
		ChannelModel channelModel = new ChannelModel();
		channelModel.setCommanddeviceid(protocol + deviceid);
		channelModel.setArea(netty_area);
		channelModel.setDeviceId(deviceid);
		channelModel.setBridgingflg(bridgingflg);
		channelModel.setProtocol(protocol);
		channelModel.setAddress(address);
		channelModel.setStatus(ChannelStatusEnum.FREE.getCode());
        
        super.setChannelModel(channelModel);
        
		return true;
	}
	
	/**
	 * 根据接收到返回JSON信息，翻译成Model后
	 * 组合成类似于Netty的两次返回信息。
	 * @param params
	 * @return
	 */
	protected boolean recvBackCmdByString(Map<String, String> params) {
		FrameModel msgModel = analysisMsg(params);
		return recvBackCmdByModel(msgModel);
	}
	
	/**
	 * 根据Model组合成类似于Netty的两次返回信息。
	 * @param msgModel
	 */
	protected boolean recvBackCmdByModel(FrameModel msgModel) {
		try {
			String msginfo = msgModel.getMsgInfo();
			// 生成立即回调帧
			log.info("立即回调!!RTR");
			// 立即回调帧都是为同步命令
			// 没有观测者的，消息存放到队列中，当前有观察者的情况下
			if (!this.cmdobservers.containsKey(msgModel.getDeviceID())
					|| this.cmdobservers.get(msgModel.getDeviceID()).size() == 0) {
				msgModel.setCmd("RTR");
				msgModel.setMsgInfo("");
				putRecvQueue(msgModel);
			}
			
			msgModel.setCmd("");
			msgModel.setMsgInfo(msginfo);
			// 二次回调
			log.info("二次回调!!");
			// 先调用各个协议的共同处理
			putSecondRecCmd(msgModel);
			// 然后确认是否存在观察者，有则异步调用观察者
			//如果是建库仪，则加上通道号
			String deviceId  = "";
			if(Command.CUBICS.toString().equals(msgModel.getCommand())&&StringUtil.isNotBlank(msgModel.getPassId())){
				deviceId = msgModel.getCommanddeviceid()+msgModel.getPassId();
			}else{
				deviceId = msgModel.getCommanddeviceid();
			}
			IConnectService channelService = CommonChannelUtil.getChannelByFrame(msgModel);
			if (channelService!=null) {
				channelService.getChannelModel().setStatus(ChannelStatusEnum.FREE.getCode());
				if (channelService.getChannelModel().isBridgingflg()){
					//发送信息到主服务器
					Map<String, String> paramMap= new HashMap<>();
					paramMap.put("frameModel", JSON.toJSONString(msgModel));
					paramMap.put("Status", ChannelStatusEnum.FREE.getCode());
					ConnectUtil.updateMainChannelStatus(paramMap);
				}
			}else {
				log.error("recvBackCmd--已注册的channel为空！deviceId={}", deviceId);
				return false;
			}
			if (this.cmdobservers.containsKey(deviceId)
					&& this.cmdobservers.get(deviceId).size() > 0) {
				List<IObserver> obList = this.cmdobservers.get(deviceId);
				String deviceIdnew = deviceId;
	
				for (int i = obList.size() - 1; i >= 0;i--) {
					IObserver t_observer = obList.get(i);
					Runnable newRunnable = new Runnable() {
						public void run() {
							Map<String, Object> result = new HashMap<String, Object>();
							result.put("threadName", cmdThreadNames.get(deviceIdnew));
							result.put("frame", msgModel);
							t_observer.onReceive(result);
						}
					};
					cachedThreadPool.execute(newRunnable);
					// 执行就删除观察者
					obList.remove(i);
				}
	
			} else {
				// 消息存放到一次返回队列中
				putRecvQueue(msgModel);
				// 消息存放到二次返回队列中
				putSecRecvQueue(msgModel);
			}
		}catch(Exception e) {
			log.error(e.getMessage());
		}
		return true;
	}
	
	/**
	 * Comment 解析消息，组成Model类
	 * 1&S00100RZS RZ	   Command+ "&" + FrameID + " " + Cmd + params
	 * @param params
	 * @return
	 */
	private FrameModel analysisMsg(Map<String, String> params) {
		FrameModel frameModel = new FrameModel();
		try {
			String frameid = params.get("frameid");
			frameModel.setFrameID(frameid);
	
			// 是否为发送消息
			String sendType = frameid.substring(0, 1);
			frameModel.setSendType("S".equals(sendType));
			// 设备ID
			String deviceId = frameid.substring(1, 6);
			frameModel.setDeviceID(deviceId);
			// 消息类型
			String messageType = frameid.substring(6, 8);
			frameModel.setMsgType(messageType);
			// 是否期望异步执行反馈
			String ansyHope = frameid.substring(8, 9);
			
			frameModel.setSyncHope("A".equals(ansyHope));
			// 区域
			frameModel.setArea(getChannelModel().getArea());
			// 是否桥接
			frameModel.setBridgingflg(getChannelModel().isBridgingflg());
			// 协议
			frameModel.setCommand(params.get("command"));
			// 协议+设备
			frameModel.setCommanddeviceid(params.get("command") + deviceId);
			// 整体的消息(截取掉ID)
			String info = params.get("msg");
			
			frameModel.setMsgInfo(info);
			frameModel.setAppoint(true);
			// 组装命令参数
			// 入宫参数是json格式，就不能用空格来弄
			//frameModel.setCmdParam(params.get("cmdParams"));
		}catch(Exception e) {
			log.error(e.getMessage());
			return null;
		}
		return frameModel;
	}
	
	/**
	 * comment 推送信息到队列中(立即回调帧队列)
	 * 立即回调帧
	 * @param msgModel
	 * @return
	 * @throws Exception
	 */
	public boolean putRecvQueue(FrameModel msgModel) throws Exception {
		Map<String, BlockingQueue<FrameModel>> recvQueuesN = CommonChannelUtil.protocols
				.get(msgModel.getCommand()).getRecvQueues();
		String frameId = CommonChannelUtil.getFrameIdFromSend(msgModel);
		BlockingQueue<FrameModel> recvQueue = recvQueuesN.get(frameId);
		if (recvQueue == null) {
			recvQueue = new LinkedBlockingQueue<FrameModel>();
		}
		recvQueue.put(msgModel);
		Map<String, BlockingQueue<FrameModel>> recvQueues= CommonChannelUtil.protocols.get(msgModel.getCommand()).getRecvQueues();
		recvQueues.put(frameId, recvQueue);
		return true;
	}
	

	/**
	 * comment 推送信息到队列中(二次回调帧队列)
	 * 
	 * @param msgModel
	 * @return
	 * @throws Exception
	 */
	public boolean putSecRecvQueue(FrameModel msgModel) throws Exception {
		Map<String, BlockingQueue<FrameModel>> secRecvQueuesN = CommonChannelUtil.protocols
				.get(msgModel.getCommand()).getSecRecvQueues();
		String frameId = CommonChannelUtil.getFrameIdFromSend(msgModel);
		BlockingQueue<FrameModel> secRecvQueue = secRecvQueuesN.get(frameId);
		if (secRecvQueue == null) {
			secRecvQueue = new LinkedBlockingQueue<FrameModel>();
		}
		secRecvQueue.put(msgModel);
		secRecvQueuesN.put(frameId, secRecvQueue);
		return true;
	}
	
	/**
	 * 把相应信息放到
	 * 二次回调帧
	 * @param recModel
	 * @return
	 */
	protected String putSecondRecCmd(FrameModel recModel) {
		return "";
	}
}

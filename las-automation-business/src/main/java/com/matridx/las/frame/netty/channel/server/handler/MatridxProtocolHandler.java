package com.matridx.las.frame.netty.channel.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.redis.RedisStreamUtil;
import com.matridx.las.frame.connect.enums.ChannelStatusEnum;
import com.matridx.las.frame.connect.util.ChannelModel;
import com.matridx.las.frame.connect.util.ConnectUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.matridx.las.frame.connect.channel.command.FrameModel;
import com.matridx.las.frame.connect.channel.command.IObserver;
import com.matridx.las.frame.connect.channel.domain.Command;
import com.matridx.las.frame.connect.channel.domain.Packet;
import com.matridx.las.frame.connect.channel.service.NettyChannelService;
import com.matridx.las.frame.connect.global.InstrumentStateGlobal;
import com.matridx.las.frame.connect.svcinterface.IConnectService;
import com.matridx.las.frame.connect.util.CommonChannelUtil;
import com.matridx.springboot.util.base.StringUtil;

/**
 * comment 协议定义 ID：S 001 00 1 comment 第一位为 是否发送 S：代表发送
 * R：代表返回。第二位到第六位为设备ID，字母也可以，但限制为5位 comment 第七位到第八位代表 命令类型，字母也可以限制为2位
 * 。最后一位代表此命令是 A异步发送的，还是 S同步发送的
 * 例：S001ZZ01A cmd信息：  001ZZ是设备ID。 01为命令类型 。A 异步发送
 * 底层发送内容为 1&S001ZZ01A CMF P1 P2 或者 1&S00100RZS RZ	   Command+ "&" + FrameID + " " + Cmd + params
 * 
 * 公司主协议，仪器的上/下线处理交由DefaultProtocolHandler进行，对内容处理则由主协议处理
 * 
 * @author linwu
 *
 * @param <T>
 */
public abstract class MatridxProtocolHandler<T> extends BaseProtocolHandler<T> implements IConnectService {

	private Logger log = LoggerFactory.getLogger(MatridxProtocolHandler.class);

	// 返回的帧队列(一次返回)
	private Map<String, BlockingQueue<FrameModel>> recvQueues = new HashMap<>();
	// 返回的帧队列(二次返回)
	private Map<String, BlockingQueue<FrameModel>> secRecvQueues = new HashMap<>();
	//接收到消息后的执行线程
	ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	// 主要用于异步使用
	protected Map<String, List<IObserver>> cmdobservers = null;
	// 为防止同一个class，同一个ID，再上一帧还未完成返回时就发送下一个命令，特增加执行中命令字典进行确认
	protected Map<String, FrameModel> cmdExcutingDic = null;
	// 观察者线程名称
	protected Map<String, String> cmdThreadNames = null;
	// 是否长期注册不删除
	protected boolean longTermFlg = false;
	
	// 命令是否强制异步执行
	@Value("${matridx.netty.cmd_exec_async:true}")
	private boolean cmd_exec_async;
	// 系统区域设置
	@Value("${matridx.netty.area:01}")
	private String netty_area;
	// 是否桥接
	@Value("${matridx.netty.bridgingflg:false}")
	private boolean bridgingflg;

	@Autowired
	RedisStreamUtil redisStreamUtil;

	public MatridxProtocolHandler() {
		this.cmdobservers = new HashMap<>();
		this.cmdThreadNames = new HashMap<>();
		this.cmdExcutingDic = new HashMap<>();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收消息的处理器："
				+ this.getClass().getName());
		log.error(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收消息的处理器："
				+ this.getClass().getName());
		if (msg instanceof Packet) {
			Packet packet = (Packet) msg;
			String msgInfo = packet.getInfo();
			log.error("MatridxProtocolHandler消息内容：" + msgInfo);

			// 信息为空则返回
			if (StringUtil.isBlank(msgInfo)) {
				// toDo
				return;
			}
			FrameModel msgModel = null;
			msgModel = analysisMsg(msgInfo,2);
			
			if(msgModel==null)
				return;
			
			if(packet.getCommand()!=null) {
				msgModel.setCommand(String.valueOf(packet.getCommand()));
				String devid = msgModel.getDeviceID();
				msgModel.setCommanddeviceid(String.valueOf(packet.getCommand())+devid);
			}
			Channel incoming = ctx.channel();
			// 判断第一位，true代表主动发起的命令。 false 则为返回信息
			if (msgModel.isSendType()) {
				// 接收到命令后的处理
				recvCmd(incoming, msgModel);
			}
			// false 为命令执行返回信息
			else {
				// 接收到返回帧后的处理
				recvBackCmd(msgModel);
			}
		}
	}

	/**
	 * 接收到命令，则需要先返回立即回调帧，然后执行相应的命令，最后返回结果帧
	 * 
	 * @param incoming
	 * @param msgModel
	 * @return
	 */
	protected boolean recvCmd(Channel incoming, FrameModel msgModel) {
		// 发送立即返回帧
 		FrameModel recModel = new FrameModel();
		recModel.setDeviceID(msgModel.getDeviceID());
		recModel.setCommand(msgModel.getCommand());
		recModel.setSendType(false);
		recModel.setMsgInfo("RTR");
		recModel.setCmd("RTR");
		recModel.setMsgType(msgModel.getMsgType());
		recModel.setSyncHope(false);
		// 立即回调帧 为了保证发送消息的机器能得 到相应的返回信息，故直接采用原有的通道
		CommonChannelUtil.sendMessageByChannel(incoming, recModel);

		if (cmd_exec_async || msgModel.isSyncHope()) {

			Runnable newRunnable = new Runnable() {
				public void run() {
					cmdHandle(incoming, msgModel, recModel);
				}
			};
			cachedThreadPool.execute(newRunnable);
		} else {
			cmdHandle(incoming, msgModel, recModel);
		}

		return true;
	}

	/**
	 * 接收到外部命令后执行相应命令，并根据结果返回相应的信息给外部
	 * @param incoming 通道信息
	 * @param msgModel 接收到的信息
	 * @param recModel 用于返回发送的信息
	 * @return
	 */
	private boolean cmdHandle(Channel incoming, FrameModel msgModel, FrameModel recModel) {
		String result = analysisCmd(incoming, msgModel);
		recModel.setMsgInfo(result);
		if("Success".equals(result)) {
			recModel.setCmd("OK");
		}else {
			recModel.setCmd("ERR");
		}
		// 二次返回
		recModel.setMsgInfo("命令二次返回完成!!");
		// 为了保证发送消息的机器能得到相应的返回信息，故直接采用原有的通道
		// CommonChannelUtil.sendMessage(recModel);
		CommonChannelUtil.sendMessageByChannel(incoming, recModel);
		return true;
	}

	/**
	 * 处理返回帧
	 * 
	 * @param msgModel
	 * @return
	 */
	protected boolean recvBackCmd(FrameModel msgModel) throws Exception {
		// 立即回调帧
		if (msgModel.getMsgInfo().contains("RTR")) {
			log.info("立即回调!!");
			// 立即回调帧都是为同步命令
			// 消息存放到队列中
			if (this.cmdobservers.containsKey(msgModel.getDeviceID())
					&& this.cmdobservers.get(msgModel.getDeviceID()).size() > 0) {
				//firstRecCmd() 有观测者的当前暂时直接忽略
				return true;
			} else {
				putRecvQueue(msgModel);
			}

		} else {
			// 二次回调
			log.info("二次回调!!");
			// 先调用各个协议的共同处理
			secondRecCmd(msgModel);
			// 然后确认是否存在观察者，有则异步调用观察者
			//如果是建库仪，则加上通道号
			String deviceId  = "";
			if(Command.CUBICS.toString().equals(msgModel.getCommand())&&StringUtil.isNotBlank(msgModel.getPassId())){
				deviceId = msgModel.getCommanddeviceid()+msgModel.getPassId();
			}else{
				deviceId = msgModel.getCommanddeviceid();
			}
			IConnectService connectService = CommonChannelUtil.getChannelByFrame(msgModel);
			if (connectService!=null) {
				connectService.getChannelModel().setStatus(ChannelStatusEnum.FREE.getCode());
				if (bridgingflg){
					//发送信息到主服务器
					Map<String, String> paramMap= new HashMap<>();
					paramMap.put("frameModel", JSON.toJSONString(msgModel));
					paramMap.put("Status", ChannelStatusEnum.FREE.getCode());
					ConnectUtil.updateMainChannelStatus(paramMap);
				}
			}else {
				log.error("recvBackCmd--已注册的channel为空！deviceId={}", deviceId);
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
				// 消息存放到二次返回队列中
				putSecRecvQueue(msgModel);
			}
		}
		return true;
	}

	/**
	 * comment 推送信息到队列中(立即回调帧队列)
	 * 
	 * @param msgModel
	 * @return
	 * @throws Exception
	 */
	public boolean putRecvQueue(FrameModel msgModel) throws Exception {
		Map<String, BlockingQueue<FrameModel>> recvQueuesN = CommonChannelUtil.protocols
				.get(msgModel.getCommand()).getRecvQueues();
		BlockingQueue<FrameModel> recvQueue = recvQueuesN.get(msgModel.getFrameID());
		if (recvQueue == null) {
			recvQueue = new LinkedBlockingQueue<FrameModel>();
		}
		recvQueue.put(msgModel);
		recvQueues.put(msgModel.getFrameID(), recvQueue);
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
		BlockingQueue<FrameModel> secRecvQueue = secRecvQueuesN.get(msgModel.getFrameID());
		if (secRecvQueue == null) {
			secRecvQueue = new LinkedBlockingQueue<FrameModel>();
		}
		secRecvQueue.put(msgModel);
		secRecvQueuesN.put(msgModel.getFrameID(), secRecvQueue);
		return true;
	}

	/**
	 * Comment 针对指定DeviceID的所有帧ID注册观察者,主要针对命令级别，用于异步使用,默认执行后就删除
	 * 
	 * @param threadName 线程名称
	 * @param deviceId   设备ID
	 * @param observer   观察者
	 * @return
	 */
	public boolean registerCmdObserver(String threadName, String deviceId, IObserver observer) {
		return registerCmdObserver(threadName, deviceId, observer, false);
	}

	/**
	 * Comment 针对指定DeviceID的所有帧ID注册观察者,主要针对命令级别，用于异步使用
	 * 
	 * @param threadName 线程名称
	 * @param observer   观察者
	 * @param isLongTerm true：执行后不删除，false 执行后删除
	 * @return
	 */
	public boolean registerCmdObserver(String threadName, String deviceId, IObserver observer, boolean isLongTerm) {
		// 如果包含某个Key，就直接跳过
		if (this.cmdobservers.containsKey(deviceId)) {
			List<IObserver> obList = this.cmdobservers.get(deviceId);
			this.cmdThreadNames.put(deviceId, threadName);
			obList.add(observer);
		} else {
			List<IObserver> obList = new ArrayList<IObserver>();
			obList.add(observer);
			this.cmdobservers.put(deviceId, obList);
			this.cmdThreadNames.put(deviceId, threadName);
		}
		longTermFlg = isLongTerm;
		return true;
	}

	/**
	 * Comment 删除本协议（已指定设备ID）的所有帧ID注册观察者,主要针对命令级别，用于异步使用
	 * 
	 * @return
	 */
	public boolean unRegisterCmdObserver(String deviceId) {
		// 如果包含某个Key，就直接跳过
		if (this.cmdobservers.containsKey(deviceId)) {
			this.cmdobservers.remove(deviceId);
			this.cmdThreadNames.remove(deviceId);
		}
		return true;
	}

	/**
	 * 根据命令要求执行相应功能，基础类这边执行注册命令，相应其他命令分散到相应类里执行
	 * @param incoming
	 * @param recModel
	 * @return
	 */
	protected String analysisCmd(Channel incoming, FrameModel recModel) {
		// 判断是否为启动后的注册命令
		if ("RZ".equals(recModel.getMsgType())) {
			
			NettyChannelService nettychannel = new NettyChannelService();
	        nettychannel.setChannel(incoming);

			InetSocketAddress ipSocket = (InetSocketAddress)incoming.remoteAddress();
			String key = ipSocket.getAddress().getHostAddress();
			

			ChannelModel channelModel = new ChannelModel();
			channelModel.setProtocol(recModel.getCommand());
			channelModel.setAddress(key);
			channelModel.setDeviceId(recModel.getDeviceID());
			channelModel.setCommanddeviceid(recModel.getCommanddeviceid());
			// 区域
			channelModel.setArea(recModel.getArea());
			channelModel.setStatus(ChannelStatusEnum.FREE.getCode());
			channelModel.setBridgingflg(bridgingflg);
			
			nettychannel.setChannelModel(channelModel);
			
			boolean result = CommonChannelUtil.handleChannel(key, nettychannel);
			if (result) {
				// 为了防止重新上线时，仪器已在忙碌中，则先不将其放入队列中
				Map<String,String> ma = InstrumentStateGlobal.getInstrumentUsedListMap(recModel.getCommand(), recModel.getDeviceID());
				if (ma == null) {
					// 注册成功后，将仪器放入空闲的队列中并修改状态
					//InstrumentStateGlobal.changeInstrumentState(recModel.getCommand(),recModel.getDeviceID(), InstrumentStatusEnum.STATE_FREE.getCode());
					//InstrumentStateGlobal.putInstrumentQueuesToRedis(recModel.getCommand(), recModel.getDeviceID());
				} else {//如果是忙碌状态，改为忙碌
					//InstrumentStateGlobal.changeInstrumentState(recModel.getCommand(),recModel.getDeviceID(), InstrumentStatusEnum.STATE_WORK.getCode());
				}
				// 注册成功通知
				log.error(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " MatridxProtocolHandler:"
						+ incoming.remoteAddress() + " 注册成功！");
				return "Success";
			} else {
				// 注册失败通知
				// frameModel.setMsgInfo("Fail！！");
				log.error(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " MatridxProtocolHandler:"
						+ incoming.remoteAddress() + " 注册失败！");
				return "Fail";
			}
		}

		return excuteCmd(recModel);
	}

	/**
	 * Comment 解析消息，组成Model类
	 * 1&S00100RZS RZ	   Command+ "&" + FrameID + " " + Cmd + params
	 * @param msgInfo
	 * @return
	 */
	private FrameModel analysisMsg(String msgInfo,int length) {
		// 信息长度不够，则返回
		//\\s+ 标识匹配任何空白字符，包括空格、换页、换行、回车、制表、垂直制表符。Java需要转义，所以两个斜杠
		String[] infos = msgInfo.split("\\s+");
		if (infos.length <= 1) {
			log.error(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " MatridxProtocolHandler:"
					+ infos + ",参数不符合规范,信息长度未达到两个及以上，以空格分割");
			return null;
		}
		String frameid = infos[0];
		// 如果为非约定好的ID长度，则返回
		if (StringUtil.isBlank(frameid) || frameid.length() != 7+length) {
			log.error(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " MatridxProtocolHandler:"
					+ infos + ",参数不符合规范，ID长度必须符合" + ( 7+length));
			return null;
		}

		FrameModel frameModel = new FrameModel();
		try {
			frameModel.setFrameID(frameid);
	
			// 是否为发送消息
			String sendType = frameid.substring(0, 1);
			frameModel.setSendType("S".equals(sendType));
			// 设备ID
			String deviceId = frameid.substring(1, 4+length);
			frameModel.setDeviceID(deviceId);
			// 消息类型
			String messageType = frameid.substring(4+length, 6+length);
			frameModel.setMsgType(messageType);
			// 是否期望异步执行反馈
			String ansyHope = frameid.substring(6+length, 7+length);
			frameModel.setSyncHope("A".equals(ansyHope));
			// 区域
			frameModel.setArea(netty_area);
			// 是否桥接
			frameModel.setBridgingflg(bridgingflg);
	
			// 整体的消息(截取掉ID)
			String info = msgInfo.substring(8+length);
			// 命令关键字
			frameModel.setCmd(infos[1]);
			frameModel.setMsgInfo(msgInfo);
			
			frameModel.setAppoint(true);
			// 组装命令参数
			// 入宫参数是json格式，就不能用空格来弄
			if (infos.length > 2) {
				if (infos[2].startsWith("{")) {
					String[] cmdParams = new String[1];
					cmdParams[0] = info.substring(info.indexOf("{"));
					frameModel.setCmdParam(cmdParams);
					JSONObject jsonObject = JSONObject.parseObject(cmdParams[0]);
					if(jsonObject!=null&&jsonObject.get("CubsChannel")!=null&&StringUtil.isNotBlank(jsonObject.get("CubsChannel").toString())){
						frameModel.setPassId(String.valueOf(jsonObject.get("CubsChannel")));
					}
				}else if(infos[2].startsWith("[")){
					String[] cmdParams = new String[1];
					cmdParams[0] = info.substring(info.indexOf("["));
					frameModel.setCmdParam(cmdParams);
					JSONArray jsonArray = JSON.parseArray(cmdParams[0]);
					if(jsonArray!=null&&jsonArray.size()==1){
						JSONObject js = jsonArray.getJSONObject(0);
						if(js!=null&&js.get("CubsChannel")!=null&&StringUtil.isNotBlank(js.get("CubsChannel").toString()))
							frameModel.setPassId(String.valueOf(js.get("CubsChannel")));
						frameModel.setProcessStage(String.valueOf(js.get("ProcessStage")));
					}
				}else {
					String[] cmdParams = new String[infos.length - 2];
					for (int i = 2; i < infos.length; i++) {
						cmdParams[i - 2] = infos[i];
					}
					frameModel.setCmdParam(cmdParams);
				}
			}
		}catch(Exception e) {
			log.error(e.getMessage());
			return null;
		}
		return frameModel;
	}

	/**
	 * 获取观察者
	 * @return
	 */
	public  Map<String, List<IObserver>> getCmdobservers(){
		return  cmdobservers;
	}
	/**
	 * 获取观察者的线程
	 */
	public Map<String, String> getcmdThreadNames(){
		return cmdThreadNames;
	}
	
	/**
	 * 获取第一次返回信息的所有Map的队列
	 * @return
	 */
	public Map<String, BlockingQueue<FrameModel>> getRecvQueues(){
		return recvQueues;
	}
	
	/**
	 * 获取第二次返回信息的所有Map的队列
	 * @return
	 */
	public Map<String, BlockingQueue<FrameModel>> getSecRecvQueues(){
		return secRecvQueues;
	}
	

	public ChannelModel getChannelModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 *
	 * @param recModel
	 * @return
	 */
	protected abstract String firstRecCmd(FrameModel recModel);

	/**
	 *
	 * @param recModel
	 * @return
	 */
	protected abstract String secondRecCmd(FrameModel recModel);

	/**
	 *
	 * @param recModel
	 * @return
	 */
	protected abstract String excuteCmd(FrameModel recModel);

}

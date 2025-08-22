package com.matridx.las.frame.connect.channel.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.matridx.las.frame.connect.channel.command.FrameModel;
import com.matridx.las.frame.connect.channel.command.IObserver;
import com.matridx.las.frame.connect.svcinterface.IConnectService;
import com.matridx.las.frame.connect.util.ChannelModel;

public class BaseService implements IConnectService{
	/**
	 * 通道信息
	 */
	private ChannelModel channelModel;
	// 返回的帧队列(一次返回)
	private Map<String, BlockingQueue<FrameModel>> recvQueues = new HashMap<>();
	// 返回的帧队列(二次返回)
	private Map<String, BlockingQueue<FrameModel>> secRecvQueues = new HashMap<>();
	// 主要用于异步使用
	protected Map<String, List<IObserver>> cmdobservers = null;
	// 为防止同一个class，同一个ID，再上一帧还未完成返回时就发送下一个命令，特增加执行中命令字典进行确认
	protected Map<String, FrameModel> cmdExcutingDic = null;
	// 观察者线程名称
	protected Map<String, String> cmdThreadNames = null;
	// 是否长期注册不删除
	protected boolean longTermFlg = false;
	//接收到消息后的执行线程
	protected ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		
	public BaseService() {
		// TODO Auto-generated constructor stub
		this.cmdobservers = new HashMap<>();
		this.cmdThreadNames = new HashMap<>();
		this.cmdExcutingDic = new HashMap<>();
	}
	
	@Override
	public Map<String, BlockingQueue<FrameModel>> getRecvQueues() {
		// TODO Auto-generated method stub
		return recvQueues;
	}

	@Override
	public Map<String, BlockingQueue<FrameModel>> getSecRecvQueues() {
		// TODO Auto-generated method stub
		return secRecvQueues;
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

	public ChannelModel getChannelModel() {
		return channelModel;
	}

	public void setChannelModel(ChannelModel channelModel) {
		this.channelModel = channelModel;
	}

}

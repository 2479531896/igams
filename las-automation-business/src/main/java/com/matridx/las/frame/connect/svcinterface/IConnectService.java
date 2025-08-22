package com.matridx.las.frame.connect.svcinterface;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.matridx.las.frame.connect.channel.command.FrameModel;
import com.matridx.las.frame.connect.channel.command.IObserver;
import com.matridx.las.frame.connect.util.ChannelModel;

/**
 * Netty的协议类，主要用于公司规定的协议类，被MatridxProtocolHandler 所继承。
 * 处理类似于TCP/IP的协议
 */
public interface IConnectService {
	/**
	 * 获取通道信息
	 * @return
	 */
	ChannelModel getChannelModel();
	
	/**
	 * 获取第一次返回信息的所有Map的队列
	 * @return
	 */
	Map<String, BlockingQueue<FrameModel>> getRecvQueues();
	
	/**
	 * 获取第二次返回信息的所有Map的队列
	 * @return
	 */
	Map<String, BlockingQueue<FrameModel>> getSecRecvQueues();
	
	/**
	 * 注册观察者
	 * @param threadName
	 * @param commanddeviceid
	 * @param observer
	 * @return
	 */
	boolean registerCmdObserver(String threadName, String commanddeviceid,IObserver observer);
}

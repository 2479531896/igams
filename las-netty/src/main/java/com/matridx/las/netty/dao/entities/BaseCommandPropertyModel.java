package com.matridx.las.netty.dao.entities;

import com.matridx.las.netty.channel.command.FrameModel;
import org.apache.ibatis.type.Alias;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * 封装一些baseCommand属性方便传
 */
@Alias(value = "BaseCommandPropertyModel")
public class BaseCommandPropertyModel {
	// 异步命令队列
	private Map<String, BlockingQueue<FrameModel>> cmdDic = new HashMap<String, BlockingQueue<FrameModel>>();
	// 异步事件队列
	private Map<String, BlockingQueue<SjlcDto>> eventDic = new HashMap<String, BlockingQueue<SjlcDto>>();
	// 执行中的异步命令信息，用于保存正在处理的命令信息，方便执行回调方法
	private Map<String, FrameModel> excutingCmdDic = new HashMap<String, FrameModel>();
	//记录正在被发消息的事件
	private SjlcDto thisSjlcDto;
	//此时注册的仪器id
	private String deviceid;
	//此时注册的协议类型
	private String command;
	//用来记录是那个对像
	private String unid;
	//已经发送命令
	private Map<String, BlockingQueue<FrameModel>> haveSendcmdDic = new HashMap<String, BlockingQueue<FrameModel>>();
	//已经发送的事件
	private Map<String, BlockingQueue<SjlcDto>> haveSendeventDic = new HashMap<String, BlockingQueue<SjlcDto>>();

	public Map<String, BlockingQueue<FrameModel>> getCmdDic() {
		return cmdDic;
	}

	public void setCmdDic(Map<String, BlockingQueue<FrameModel>> cmdDic) {
		this.cmdDic = cmdDic;
	}

	public Map<String, BlockingQueue<SjlcDto>> getEventDic() {
		return eventDic;
	}

	public void setEventDic(Map<String, BlockingQueue<SjlcDto>> eventDic) {
		this.eventDic = eventDic;
	}

	public Map<String, FrameModel> getExcutingCmdDic() {
		return excutingCmdDic;
	}

	public void setExcutingCmdDic(Map<String, FrameModel> excutingCmdDic) {
		this.excutingCmdDic = excutingCmdDic;
	}

	public SjlcDto getThisSjlcDto() {
		return thisSjlcDto;
	}

	public void setThisSjlcDto(SjlcDto thisSjlcDto) {
		this.thisSjlcDto = thisSjlcDto;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Map<String, BlockingQueue<FrameModel>> getHaveSendcmdDic() {
		return haveSendcmdDic;
	}

	public void setHaveSendcmdDic(Map<String, BlockingQueue<FrameModel>> haveSendcmdDic) {
		this.haveSendcmdDic = haveSendcmdDic;
	}

	public Map<String, BlockingQueue<SjlcDto>> getHaveSendeventDic() {
		return haveSendeventDic;
	}

	public void setHaveSendeventDic(Map<String, BlockingQueue<SjlcDto>> haveSendeventDic) {
		this.haveSendeventDic = haveSendeventDic;
	}

	public String getUnid() {
		return unid;
	}

	public void setUnid(String unid) {
		this.unid = unid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}

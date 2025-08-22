package com.matridx.las.netty.channel.command;

import java.util.HashMap;
import java.util.Map;

public class FrameModel {
    //帧ID
    private String frameID;
    //发送类型  1&S001RZS RZ 里的第一个S 代表发送
    private boolean sendType;
	//设备ID 1&S001RZS RZ里的 001
    private String deviceID;
	//协议标识+设备ID
	private String commanddeviceid;
	//协议标识
    private String command;
	//命令类型   RZ 启动注册 帧格式：1&S001RZS RZ 里的001后面的RZ
    private String msgType;
    //1&S001RZS 里的最后的S，正常是A 和S之分  A代表异步  S代表同步
	//是否期望异步执行反馈，就是对方接收到命令后是否开启线程执行。接收端在这个基础上，自己还可自定义是否为异步。但对方发送为异步则一定为异步
    private boolean syncHope;
    //信息
    private String msgInfo;
    //命令关键字
    private String cmd;
    //命令参数
    private String[] cmdParam;
    //设备名称，主要用于xml里查找相应设备信息使用
    private String deviceName;
	//是否异步命令
    private boolean sync;
	//是否为异步最后一个命令
    private boolean lastSync;
	//接收命令处理是否为异步
    private boolean receiveSync;
    //回调方法
    private ICallBack callFunc;
	//第一次返回的超时时间
    private int firstTimeout = 30000;
    //第二次返回的超时时间
    private int secondTimeout = 300000;
    //需要关闭的线程名称
    private String threadName = null;
    //记录用过的仪器和机器人
    private Map<String, String> yqUsedMap= new HashMap<String, String>();
    //命令id
    private String mlid ;
    //当需要重复调用发送命令，这里记录下basecommand
    private BaseCommand sendBaseCommand;
    //发送这个消息的线程名称
    private String sendThreadName;
    //用于插队发送
    private boolean isFirstSend=false;
    //这是第几次发送消息，用于失败时重复发送此消息
	private Integer howTimes = 0;
	//流程id
	private String lcid;
	//建库仪的通道号
	private String passId;
	//建库仪状态
	private String processStage;
	//备注
	private String bz;

	public FrameModel() {
    	
    }
    
    public FrameModel(String cmd,String[] params,boolean sync,boolean lastSync,ICallBack func,String threadName) {
    	this.cmd = cmd;
    	this.cmdParam = params;
    	this.sync = sync;
    	this.lastSync = lastSync;
    	this.callFunc = func;
    	this.threadName= threadName;
    }
    
    public FrameModel(String command, String deviceID, String msgType, String cmd,String[] params,boolean sync,boolean lastSync,ICallBack func,String threadName,
    		int firstTimeout,int secondTimeout,String mlid,Map<String, String> map,boolean isFirstSend,Integer howTimes,String lcid,String passId,String bz) {
    	this.command = command;
    	this.deviceID = deviceID;
    	this.commanddeviceid = command+deviceID;
    	this.msgType = msgType;
    	this.cmd = cmd;
    	this.cmdParam = params;
    	this.sync = sync;
    	this.lastSync = lastSync;
    	this.callFunc = func;
    	this.threadName= threadName;
    	this.firstTimeout = firstTimeout;
    	this.secondTimeout = secondTimeout;
    	this.mlid = mlid;
    	this.yqUsedMap = map;
    	this.isFirstSend = isFirstSend;
    	this.howTimes = howTimes;
    	this.lcid = lcid;
    	this.passId = passId;
    	this.bz = bz;
    }
    
    /**
     * comment 命令是否执行错误，现主要用于异步命令线程使用
     */
    private boolean isError = false;

	public String getCommanddeviceid() {
		return commanddeviceid;
	}
	public void setCommanddeviceid(String commanddeviceid) {
		this.commanddeviceid = commanddeviceid;
	}
	public String getFrameID() {
		return frameID;
	}
	public void setFrameID(String frameID) {
		this.frameID = frameID;
	}
	public String getMsgInfo() {
		return msgInfo;
	}
	public void setMsgInfo(String msgInfo) {
		this.msgInfo = msgInfo;
	}
	public boolean isSendType() {
		return sendType;
	}
	public void setSendType(boolean sendType) {
		this.sendType = sendType;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public boolean isSync() {
		return sync;
	}
	public void setSync(boolean sync) {
		this.sync = sync;
	}
	public boolean isLastSync() {
		return lastSync;
	}
	public void setLastSync(boolean lastSync) {
		this.lastSync = lastSync;
	}
	public boolean isReceiveSync() {
		return receiveSync;
	}
	public void setReceiveSync(boolean receiveSync) {
		this.receiveSync = receiveSync;
	}
	public ICallBack getCallFunc() {
		return callFunc;
	}
	public void setCallFunc(ICallBack callFunc) {
		this.callFunc = callFunc;
	}
	public int getFirstTimeout() {
		return firstTimeout;
	}
	public void setFirstTimeout(int firstTimeout) {
		this.firstTimeout = firstTimeout;
	}
	public int getSecondTimeout() {
		return secondTimeout;
	}
	public void setSecondTimeout(int secondTimeout) {
		this.secondTimeout = secondTimeout;
	}
	public String getThreadName() {
		return threadName;
	}
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	public boolean isError() {
		return isError;
	}
	public void setError(boolean isError) {
		this.isError = isError;
	}
	public boolean isSyncHope() {
		return syncHope;
	}
	public void setSyncHope(boolean syncHope) {
		this.syncHope = syncHope;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String[] getCmdParam() {
		return cmdParam;
	}
	public void setCmdParam(String[] cmdParam) {
		this.cmdParam = cmdParam;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}

	public String getMlid() {
		return mlid;
	}

	public void setMlid(String mlid) {
		this.mlid = mlid;
	}

	public Map<String, String> getYqUsedMap() {
		return yqUsedMap;
	}

	public void setYqUsedMap(Map<String, String> yqUsedMap) {
		this.yqUsedMap = yqUsedMap;
	}

	public BaseCommand getSendBaseCommand() {
		return sendBaseCommand;
	}

	public void setSendBaseCommand(BaseCommand sendBaseCommand) {
		this.sendBaseCommand = sendBaseCommand;
	}

	public String getSendThreadName() {
		return sendThreadName;
	}

	public void setSendThreadName(String sendThreadName) {
		this.sendThreadName = sendThreadName;
	}

	public boolean isFirstSend() {
		return isFirstSend;
	}

	public void setFirstSend(boolean isFirstSend) {
		this.isFirstSend = isFirstSend;
	}

	public void setHowTimes(Integer howTimes) {
		this.howTimes = howTimes;
	}

	public Integer getHowTimes() {
		return howTimes;
	}

	public String getLcid() {
		return lcid;
	}

	public void setLcid(String lcid) {
		this.lcid = lcid;
	}

	public String getPassId() {
		return passId;
	}

	public void setPassId(String passId) {
		this.passId = passId;
	}

	public String getProcessStage() {
		return processStage;
	}

	public void setProcessStage(String processStage) {
		this.processStage = processStage;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
}

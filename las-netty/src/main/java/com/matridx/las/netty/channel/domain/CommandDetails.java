package com.matridx.las.netty.channel.domain;

public interface CommandDetails {

	
	//cubis各个动作的命令类型
	String CUBICS_START="ON";//开始
	String CUBICS_STOP="OF";//中断
	String CUBICS_CLEAR="CE";//清除
	String CUBICS_DEFAULT="DE";//默认上报通道信息
	String CUBICS_STATUS="SL";//通道信息查询/上报状态
	String CUBICS_COMPLETE="CP";//完成
	String CUBICS_PROCESSSTAGE_EMPTY="Empty";
	String CUBICS_PROCESSSTAGE_IDEL="Idle";
	String CUBICS_PROCESSSTAGE_ERROR="Error";
	String CUBICS_PROCESSSTAGE_COMPLETE="Complete";
	String CUBICS_PROCESSSTAGE_PROCESSING="Processing";
	//
	String PCR_CONNECT="CN";
	String PCR_STATE="ST";
	String PCR_START="SR";
	String PCR_COMPLETE="CP";
	String PCR_FIRST="FI";
	String PCR_SECOND="SC";
	//机器人命令类型
	String AVG_GE="GE";//获取电池电量
	String AVG_GO="GO";//去充电
	String AVG_ST="ST";//停止充电
	String AGV_DC="DC";//放夹爪
	String AGV_TC="TC";//拿夹爪
	//配液仪的命令
	String AUTO_FIRST = "SR";
	String AUTO_SECOND = "PO";
	String AUTO_THIRD = "TH";

	//压盖机的命令
	String CMH_START="CM";

	//测序仪的命令
	String SEQ_FIRST = "SR";


	
	

}

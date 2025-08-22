package com.matridx.las.frame.connect.channel.domain;

public interface Command {

	Byte REGISTER = 9; // 注册channel协议
	Byte DEFAULT = 0;   //默认普通文本协议
	Byte CUBICS = 1;   //Cubics协议,用作建库仪
	Byte SMAP = 2;   //SMAP协议
	Byte PCR = 3;   //荧光检测PCR协议
	Byte SEQ = 4;   //测序仪协议
	Byte PRINT = 5;   //打印机
	Byte NEMO = 6;   //nemo
	//Byte AGV = 5;   //机器人协议


}

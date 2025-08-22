package com.matridx.las.frame.connect.channel.domain;

public interface CommandType {

	Byte REGISTER = 9; // 注册channel协议
	Byte DEFAULT = 0;   //默认普通文本协议
	Byte CUBICS = 1;   //Cubics协议
	Byte AUTO = 2;   //Auto自动配液协议
	Byte PCR = 3;   //荧光检测PCR协议
	Byte SEQ = 4;   //测序仪协议
	Byte AVG = 5;   //机器人协议
	Byte CMH = 6;   //压盖机协议

}

package com.matridx.las.netty.global;

import com.matridx.igams.common.enums.YesNotEnum;
import com.matridx.las.netty.channel.command.FrameModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 用来记录当前正在发送的命令，流程等信息
 */
public class CommandParmGlobal {

	private static Logger log = LoggerFactory.getLogger(CommandParmGlobal.class);
	private static String isSuspend = YesNotEnum.NOT.getCode();//是否暂停
	private static List<FrameModel> listFrameModel ;//要发送的信息
	private static  Map<String,List<String>> mapDeviceid;//被停掉的仪器

	public static String getIsSuspend() {
		return isSuspend;
	}

	public static void setIsSuspend(String isSuspend) {
		CommandParmGlobal.isSuspend = isSuspend;
	}

	public static List<FrameModel> getListFrameModel() {
		return listFrameModel;
	}

	public static void setListFrameModel(List<FrameModel> listFrameModel) {
		CommandParmGlobal.listFrameModel = listFrameModel;
	}

	public static Map<String, List<String>> getMapDeviceid() {
		return mapDeviceid;
	}

	public static void setMapDeviceid(Map<String, List<String>> mapDeviceid) {
		CommandParmGlobal.mapDeviceid = mapDeviceid;
	}
}

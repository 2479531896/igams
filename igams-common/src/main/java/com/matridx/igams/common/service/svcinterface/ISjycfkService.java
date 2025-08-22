package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.SjycfkDto;
import com.matridx.igams.common.dao.entities.SjycfkModel;

import java.util.List;

public interface ISjycfkService extends BaseBasicService<SjycfkDto, SjycfkModel>{

	/**
	 * 根据异常ID获取送检异常反馈信息
	 */
	List<SjycfkDto> getDtoByYcid(SjycfkDto sjycfkDto);
	
	
	/**
	 * 获取该送检信息下的所有异常的评论数
	 */
	List<SjycfkDto> getExceptionPls(SjycfkDto sjycfkDto);
	
	/**
	 * 小程序获取异常反馈信息，父反馈ID为空 
	 */
	List<SjycfkDto> getMiniDtoByYcid(SjycfkDto sjycfkDto);
	
	/**
	 * 小程序根据根ID获取异常反馈信息
	 */
	List<SjycfkDto> getDtosByGid(SjycfkDto sjycfkDto);
	
	/**
	 * 根据fkid查找该评论下的所有子评论
	 */
	List<SjycfkDto> getZplByFkid(SjycfkDto sjycfkDto);

	 void sendDingMessage(SjycfkDto sjycfkDto);
	 void sendMessageFromOA(SjycfkDto sjycfkDto);
	/**
	 * 发送多文件至微信服务器
	 */
	 boolean sendFilesToAli(List<FjcfbModel> fjcfbModels);
}

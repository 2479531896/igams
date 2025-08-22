package com.matridx.server.wechat.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjycfkWechatDto;
import com.matridx.server.wechat.dao.entities.SjycfkWechatModel;

import java.util.List;

public interface ISjycfkWechatService extends BaseBasicService<SjycfkWechatDto, SjycfkWechatModel>{

	/**
	 * 根据异常ID获取送检异常反馈信息
	 * @param sjycfkDto
	 * @return
	 */
	List<SjycfkWechatDto> getDtoByYcid(SjycfkWechatDto sjycfkDto);
	
	
	/**
	 * 获取该送检信息下的所有异常的评论数
	 * @param sjycfkDto
	 * @return
	 */
	List<SjycfkWechatDto> getExceptionPls(SjycfkWechatDto sjycfkDto);
	
	/**
	 * 小程序获取异常反馈信息，父反馈ID为空 
	 * @param sjycfkDto
	 * @return
	 */
	List<SjycfkWechatDto> getMiniDtoByYcid(SjycfkWechatDto sjycfkDto);
	
	/**
	 * 小程序根据根ID获取异常反馈信息
	 * @param sjycfkDto
	 * @return
	 */
	List<SjycfkWechatDto> getDtosByGid(SjycfkWechatDto sjycfkDto);
	
	/**
	 * 根据fkid查找该评论下的所有子评论
	 * @param sjycfkDto
	 * @return
	 */
	List<SjycfkWechatDto> getZplByFkid(SjycfkWechatDto sjycfkDto);

	void sendDingMessage(SjycfkWechatDto sjycfkDto);
	void sendMessageFromOA(SjycfkWechatDto sjycfkDto, User user);
}

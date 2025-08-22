package com.matridx.server.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjycfkWechatDto;
import com.matridx.server.wechat.dao.entities.SjycfkWechatModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISjycfkWechatDao extends BaseBasicDao<SjycfkWechatDto, SjycfkWechatModel> {

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

	public void sendDingMessage(SjycfkWechatDto sjycfkDto);
}

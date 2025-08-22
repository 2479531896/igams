package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.server.wechat.dao.entities.SjhbxxDto;
import com.matridx.server.wechat.dao.entities.SjhbxxModel;

@Mapper
public interface ISjhbxxDao extends BaseBasicDao<SjhbxxDto, SjhbxxModel>{

	/**
	 * 保存本地删除信息至微信服务器
	 * @param sjhbxxDto
	 * @throws BusinessException 
	 */
	boolean deletepartner(SjhbxxDto sjhbxxDto);


	/**
	 * 通过hbid查询 合作伙伴
	 * @param list
	 * @return
	 */
	List<String> getHbmcByHbid(List<String> list);

	/**
	 * 查询伙伴为'无'的用户信息
	 * @return
	 */
    List<SjhbxxDto> getXtyhByHbmc();
	/**
	 * 启用
	 * @param sjhbxxDto
	 * @return
	 */
	boolean enablepartner(SjhbxxDto sjhbxxDto);

	/**
	 * 停用
	 * @param sjhbxxDto
	 * @return
	 */
	boolean disablepartner(SjhbxxDto sjhbxxDto);
	/*
		86页面修改同步85
	 */
    int updatePageEvent(SjhbxxDto sjhbxxDto);
}

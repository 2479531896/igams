package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjqxDto;
import com.matridx.server.wechat.dao.entities.SjqxModel;

@Mapper
public interface ISjqxDao extends BaseBasicDao<SjqxDto, SjqxModel>{

	/**
	 * 根据微信ID删除权限信息
	 * @param wxid
	 * @return
	 */
	int deleteByWxid(String wxid);

	/**
	 * 批量新增权限信息
	 * @param sjqxDtos
	 * @return
	 */
	int insertBySjqxDtos(List<SjqxDto> sjqxDtos);

}

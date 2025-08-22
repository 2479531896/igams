package com.matridx.igams.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjysxxDto;
import com.matridx.igams.wechat.dao.entities.SjysxxModel;

@Mapper
public interface ISjysxxDao extends BaseBasicDao<SjysxxDto, SjysxxModel>{

	/**
	 * 根据送检信息新增医生信息
	 * @param sjxxDto
	 * @return
	 */
	int insertBySjxxDto(SjxxDto sjxxDto);

	/**
	 * 根据送检医生查询医生信息
	 * @param sjxxDto
	 * @return
	 */
	List<SjysxxDto> selectSjysxxDtoBySjys(SjxxDto sjxxDto);

}

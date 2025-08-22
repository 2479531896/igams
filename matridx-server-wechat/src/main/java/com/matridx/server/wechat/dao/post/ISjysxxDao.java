package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.SjysxxDto;
import com.matridx.server.wechat.dao.entities.SjysxxModel;

@Mapper
public interface ISjysxxDao extends BaseBasicDao<SjysxxDto, SjysxxModel>{

	/**
	 * 根据送检医生查询医生信息
	 * @param sjxxDto
	 * @return
	 */
	List<SjysxxDto> selectBySjys(SjxxDto sjxxDto);

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

	/**
	 * 获取送检医生列表
	 * @param sjysxxDto
	 * @return
	 */
	List<SjysxxDto> getSjysxxDtos(SjysxxDto sjysxxDto);
	
	/**
	 *树形结构查询送检医生
	 * @param sjysxxDto
	 * @return
	 */
	List<SjysxxDto> selectTreeSjysxx(SjysxxDto sjysxxDto);
	
	/**
	 * 查询条数
	 * @param sjysxxDto
	 * @return
	 */
	int getCountByWxid(SjysxxDto sjysxxDto);

}

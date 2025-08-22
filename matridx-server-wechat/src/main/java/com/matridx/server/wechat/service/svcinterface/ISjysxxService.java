package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.SjysxxDto;
import com.matridx.server.wechat.dao.entities.SjysxxModel;

public interface ISjysxxService extends BaseBasicService<SjysxxDto, SjysxxModel>{

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
	boolean insertBySjxxDto(SjxxDto sjxxDto);

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
	
	/**
	 * 获取录入人员list
	 * @param wxid
	 * @param ddid
	 * @return
	 */
	List<String> getLrrylist(String wxid,String ddid);

}

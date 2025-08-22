package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjxxjgDto;
import com.matridx.server.wechat.dao.entities.SjxxjgModel;

public interface ISjxxjgService extends BaseBasicService<SjxxjgDto, SjxxjgModel>{

	/**
	 * 同步送检详细审核结果
	 * @param sjxxjgDtos
	 * @throws BusinessException 
	 */
	void receiveDetailedInspection(List<SjxxjgDto> sjxxjgDtos) throws BusinessException;

	
	/**
	 * 根据送检ID查询fjdid为null的详细信息
	 * @param sjxxjgDto
	 * @return
	 */
	List<SjxxjgDto> getxxjgByFjdidIsNull(SjxxjgDto sjxxjgDto);
	/**
	 * 根据送检ID查询Species下详细结果
	 * @param list
	 * @return
	 */
	List<SjxxjgDto> getxxInSpecies(List<SjxxjgDto> list);
	
	/**
	 * 根据送检ID查询Genus下详细结果
	 * @param list
	 * @return
	 */
	List<SjxxjgDto> getxxInGenus(List<SjxxjgDto> list);
	
	/**
	 * 根据检测类型计算D,R,C下详细结果总和
	 * @param sjxxjgDto
	 * @return
	 */
	List<SjxxjgDto> getJclxCount(SjxxjgDto sjxxjgDto);


}

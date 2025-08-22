package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjxxjgDto;
import com.matridx.igams.wechat.dao.entities.YhsjxxjgDto;
import com.matridx.igams.wechat.dao.entities.YhsjxxjgModel;

import java.util.List;

public interface IYhsjxxjgService extends BaseBasicService<YhsjxxjgDto, YhsjxxjgModel>{

	/**
	 * 批量删除用户送检详细审核结果
	 * @param yhsjxxjgDto
	 * @return
	 */
	boolean deleteByYhsjxxjgDto(YhsjxxjgDto yhsjxxjgDto);

	/**
	 * 批量新增用户送检详细审核结果
	 * @param yhsjxxjgDtos
	 * @return
	 */
	boolean insertByYhjxxjgDtos(List<YhsjxxjgDto> yhsjxxjgDtos);

	/**
	 * 医生修改送检详细结果信息
	 * @param yhsjxxjgDto
	 * @return
	 */
	boolean modAnalysis(YhsjxxjgDto yhsjxxjgDto);
	
	/**
	 * 查询详细结果信息
	 * @param sjxxjgDto
	 * @return
	 */
	List<SjxxjgDto> getTreeAnalysis(SjxxjgDto sjxxjgDto);

	/**
	 * 获取详细结果信息
	 * @param sjxxjgDto
	 * @return
	 */
	List<SjxxjgDto> getAnalysis(SjxxjgDto sjxxjgDto);

}

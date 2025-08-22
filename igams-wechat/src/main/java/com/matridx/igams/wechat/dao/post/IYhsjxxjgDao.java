package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjxxjgDto;
import com.matridx.igams.wechat.dao.entities.YhsjxxjgDto;
import com.matridx.igams.wechat.dao.entities.YhsjxxjgModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IYhsjxxjgDao extends BaseBasicDao<YhsjxxjgDto, YhsjxxjgModel>{

	/**
	 * 批量删除用户送检详细审核结果
	 * @param yhsjxxjgDto
	 * @return
	 */
	int deleteByYhsjxxjgDto(YhsjxxjgDto yhsjxxjgDto);

	/**
	 * 根据用户ID和送检ID查询信息
	 * @param yhsjxxjgDto
	 * @return
	 */
	List<YhsjxxjgDto> selectByYhidAndSjid(YhsjxxjgDto yhsjxxjgDto);

	/**
	 * 批量新增用户详细结果
	 * @param yhsjxxjgDtos
	 * @return
	 */
	boolean insertByYhsjxxjgDtos(List<YhsjxxjgDto> yhsjxxjgDtos);

	/**
	 * 查询详细结果信息
	 * @param sjxxjgDto
	 * @return
	 */
	List<SjxxjgDto> getTreeAnalysis(SjxxjgDto sjxxjgDto);

	/**
	 * 根据送检ID和用户ID查询fjdid为null的详细信息
	 * @param sjxxjgDto
	 * @return
	 */
	List<SjxxjgDto> getDtoByFjdidIsNull(SjxxjgDto sjxxjgDto);

	/**
	 * 查询分类信息
	 * @param sjxxjglist
	 * @return
	 */
	List<SjxxjgDto> getSpeciesInfo(List<SjxxjgDto> sjxxjglist);

}

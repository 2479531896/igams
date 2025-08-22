package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjnyxDto;
import com.matridx.igams.wechat.dao.entities.SjnyxModel;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISjnyxDao extends BaseBasicDao<SjnyxDto, SjnyxModel>{

	/**
	 * 批量新增送检耐药性信息
	 * @param sjnyxDtos
	 * @return
	 */
	boolean insertBysjnyxDtos(List<SjnyxDto> sjnyxDtos);

	/**
	 * 根据送检ID查询耐药信息
	 * @param sjxxDto
	 * @return
	 */
	List<SjnyxDto> getNyxBySjid(SjxxDto sjxxDto);

	/**
	 * 根据Wkbh查询耐药信息
	 * @param sjnyxDto
	 * @return
	 */
	List<SjnyxDto> getNyxByWkbh(SjnyxDto sjnyxDto);

	/**
	 * 根据送检ID查询耐药信息
	 * @param sjxxDto
	 * @return
	 */
	List<SjwzxxDto> getNyxMapBySjid(SjxxDto sjxxDto);

	/**
	 * 根据送检ID查询耐药信息，采用送检耐药性列表
	 * @param sjxxDto
	 * @return
	 */
	List<SjnyxDto> getNyxListBySjid(SjxxDto sjxxDto);

	/**
	 * 根据送检ID查询耐药信息，按照药品进行分类
	 * @param sjxxDto
	 * @return
	 */
	List<SjnyxDto> getNyxYpListBySjid(SjxxDto sjxxDto);

	/**
	 * 根据Dto删除耐药信息
	 * @param sjnyxDto
	 * @return
	 */
	int deleteBySjnyxDto(SjnyxDto sjnyxDto);
	
	/**
	 * 根据送检id查询耐药基因说明
	 * @param sjxxDto
	 * @return
	 */
	List<SjnyxDto> getExplanationForWord(SjxxDto sjxxDto);

	/**
	 * 根据sjids和jclx批量删除耐药性
	 * @param sjnyxDto
	 * @return
	 */
	boolean deleteBySjidsAndJclx(SjnyxDto sjnyxDto);

	List<Map<String,Object>> getNyxMapList(SjxxDto sjxxDto);

	boolean updateBySjnyxDtos(List<SjnyxDto> sjnyxDtos);

	List<SjnyxDto> getNyxBySjidAndJclx(SjxxDto sjxxDto);

	List<SjnyxDto> getSamePositionNy(SjnyxDto sjnyxDto);
}

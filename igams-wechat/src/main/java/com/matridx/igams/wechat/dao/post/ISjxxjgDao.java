package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjxxjgDto;
import com.matridx.igams.wechat.dao.entities.SjxxjgModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISjxxjgDao extends BaseBasicDao<SjxxjgDto, SjxxjgModel>{

	/**
	 * 批量删除送检详细审核结果
	 * @param sjxxjgDto
	 * @return
	 */
	int deleteBySjxxjgDto(SjxxjgDto sjxxjgDto);

	/**
	 * 批量新增送检详细审核结果
	 * @param sjxxjgDtos
	 * @return
	 */
	boolean insertBySjxxjgDtos(List<SjxxjgDto> sjxxjgDtos);

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
	
	/**
	 * 报告模板疑似背景微生物数据 
	 * @param sjxxjgDto
	 * @return
	 */
	List<SjxxjgDto> selectBackground(SjxxjgDto sjxxjgDto);

	/**
	 * 查询详细结果信息
	 * @param sjxxjgDto
	 * @return
	 */
	List<SjxxjgDto> getTreeAnalysis(SjxxjgDto sjxxjgDto);

	/**
	 * 根据送检ID查询详细结果信息
	 * @param sjid
	 * @return
	 */
	List<SjxxjgDto> selectBySjid(String sjid);

	/**
	 * 获取检测类型信息
	 * @param sjxxjgDto
	 * @return
	 */
	List<SjxxjgDto> getJclxInfo(SjxxjgDto sjxxjgDto);
	/**
	 * 获取导出数据
	 * @param sjxxjgDto
	 * @return
	 */
	List<SjxxjgDto> getListByIds(SjxxjgDto sjxxjgDto);
	/**
	 * 获取导出数据
	 * @param sjxxjgDto
	 * @return
	 */
	List<SjxxjgDto> getDetectionData(SjxxjgDto sjxxjgDto);


	List<Map<String,Object>> getInfoList(SjxxjgDto sjxxjgDto);
	
}

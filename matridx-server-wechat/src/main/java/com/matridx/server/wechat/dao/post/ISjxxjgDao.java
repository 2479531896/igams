package com.matridx.server.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjxxjgDto;
import com.matridx.server.wechat.dao.entities.SjxxjgModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISjxxjgDao extends BaseBasicDao<SjxxjgDto, SjxxjgModel>{

	/**
	 * 批量保存送检详细审核结果信息
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
	 * 批量删除送检详细审核结果
	 * @param sjxxjgDto
	 * @return
	 */
	int deleteBySjxxjgDto(SjxxjgDto sjxxjgDto);
	
	/**
	 * 根据检测类型计算D,R,C下详细结果总和
	 * @param sjxxjgDto
	 * @return
	 */
	List<SjxxjgDto> getJclxCount(SjxxjgDto sjxxjgDto);
	
}

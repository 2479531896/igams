package com.matridx.server.wechat.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjjcxmDto;
import com.matridx.server.wechat.dao.entities.SjjcxmModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;

@Mapper
public interface ISjjcxmDao extends BaseBasicDao<SjjcxmDto, SjjcxmModel>{

	/**
	 * 根据送检信息新增检测项目
	 * @param sjjcxmDtos
	 * @return
	 */
	int insertSjjcxmDtos(List<SjjcxmDto> sjjcxmDtos);
	/**
	 * 根据送检信息更新检测项目
	 * @param sjjcxmDtos
	 * @return
	 */
	int updateSjjcxmDtos(List<SjjcxmDto> sjjcxmDtos);

	/**
	 * 根据送检ID删除检测项目
	 * @param sjxxDto
	 */
	int deleteBySjxx(SjxxDto sjxxDto);


	/**
	 * 根据项目管理id修改
	 * @param list
	 * @return
	 */
	int updateListNew(List<SjjcxmDto> list);
	
	/**
	 * 获取检测项目的String清单
	 * @param sjjcxmDtos
	 * @return
	 */
	public List<String> getStringList(SjjcxmDto sjjcxmDtos);

	/**
	 * 获取检测子项目的String清单
	 * @param sjjcxmDto
	 * @return
	 */
	public List<String> getJczxmString(SjjcxmDto sjjcxmDto);

	/**
	 * 根据送检ID查询检测项目
	 * @param sjxxDto
	 * @return
	 */
	List<SjjcxmDto> selectJcxmBySjid(SjxxDto sjxxDto);


	/**
	 * 根据sjid获取送检检测项目以及对应项目收费标准
	 * @param sjid
	 * @return
	 */
	List<Map<String,Object>> getDetectionPayInfo(String sjid);

	/**
	 * 还原数据
	 * @param sjjcxmDtos
	 * @return
	 */
	int revertData(List<SjjcxmDto> sjjcxmDtos);
}

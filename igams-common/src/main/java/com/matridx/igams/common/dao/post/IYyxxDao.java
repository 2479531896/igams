package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.YyxxDto;
import com.matridx.igams.common.dao.entities.YyxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IYyxxDao extends BaseBasicDao<YyxxDto, YyxxModel>{
	/**
	 * 查询医院信息列表
	 */
	List<YyxxDto> getPageYyxxDtoList(YyxxDto yyxxDto);
	
	/**
	 * 根据搜索条件获取导出条数
	 */
	 int getCountForSearchExp(YyxxDto yyxxDto);
	
	/**
	 * 从数据库分页获取导出数据
	 */
	 List<YyxxDto> getListForSearchExp(YyxxDto yyxxDto);
	
	/**
	 * 从数据库分页获取导出数据
	 */
	 List<YyxxDto> getListForSelectExp(YyxxDto yyxxDto);
	
	/**
	 * 预输入查询送检单位信息
	 */
	 List<YyxxDto> selectHospitalName(YyxxDto yyxxDto);
	/**
	 * 根据医院名称查找医院
	 */
	 List<YyxxDto> queryByDwmc(YyxxDto yyxxDto);

	/**
	 * 查询其它医院
	 */
	List<YyxxDto> selectOther();
	/**
	 * 根据医院LISid查询医院信息
	 */
	List<YyxxDto> selectYyxxByLisId(YyxxDto yyxxDto);
	/*
    	通过送检id获取医院信息
 	*/
	YyxxDto getYyxxBySjid(String sjid);

	/**
	 * @Description: 根据医院名称、医院简称、医院其他名称查询医院信息
	 * @param yyxxDto
	 * @return java.util.List<com.matridx.igams.common.dao.entities.YyxxDto>
	 * @Author: 郭祥杰
	 * @Date: 2025/7/25 9:23
	 */
	List<YyxxDto> queryAllByMc(YyxxDto yyxxDto);
}

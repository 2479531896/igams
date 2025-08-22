package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.YyxxDto;
import com.matridx.igams.common.dao.entities.YyxxModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;

public interface IYyxxService extends BaseBasicService<YyxxDto, YyxxModel>{
	/**
	 * 获取所走数据
	 */
	List<YyxxDto> getPageYyxxDtoList(YyxxDto yyxxDto);

	/**
	 * 根据搜索条件获取导出条数
	 */
	 int getCountForSearchExp(YyxxDto yyxxDto,Map<String, Object> params);
	
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

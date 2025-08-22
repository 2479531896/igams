package com.matridx.igams.common.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.YyxxDto;
import com.matridx.igams.common.dao.entities.YyxxModel;
import com.matridx.igams.common.dao.post.IYyxxDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IYyxxService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class YyxxServiceImpl extends BaseBasicServiceImpl<YyxxDto, YyxxModel, IYyxxDao> implements IYyxxService{
	
	/**
	 * 新增医院信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(YyxxDto yyxxDto){
		if(StringUtil.isBlank(yyxxDto.getDwid())) {
			yyxxDto.setDwid(StringUtil.generateUUID());
		}
		int result = dao.insert(yyxxDto);
		return result != 0;
	}
	
	/**
	 * 获得全部医院信息
	 */
	@Override
	public List<YyxxDto> getPageYyxxDtoList(YyxxDto yyxxDto) {
		return dao.getPageYyxxDtoList(yyxxDto);
	}

	/**
	 * 导出
	 */
	@Override
	public int getCountForSearchExp(YyxxDto yyxxDto, Map<String, Object> params) {
		return dao.getCountForSearchExp(yyxxDto);
	}
	/**
	 * 根据搜索条件获取导出信息
	 */
	public List<YyxxDto> getListForSearchExp(Map<String,Object> params){
		YyxxDto yyxxDto = (YyxxDto)params.get("entryData");
		queryJoinFlagExport(params,yyxxDto);
		return dao.getListForSearchExp(yyxxDto);
	}
	
	/**
	 * 根据选择信息获取导出信息
	 */
	public List<YyxxDto> getListForSelectExp(Map<String,Object> params){
		YyxxDto yyxxDto = (YyxxDto)params.get("entryData");
		queryJoinFlagExport(params,yyxxDto);
		return dao.getListForSelectExp(yyxxDto);
	}
	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String,Object> params,YyxxDto yyxxDto){
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuilder sqlParam = new StringBuilder();
		for(DcszDto dcszDto:choseList){
			if(dcszDto == null || dcszDto.getDczd() == null)
				continue;
		
			sqlParam.append(",");
			if(StringUtil.isNotBlank(dcszDto.getSqlzd())){
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		String sqlcs=sqlParam.toString();
		yyxxDto.setSqlParam(sqlcs);
	}
	/**
	 * 预输入查询送检单位信息
	 */
	@Override
	public List<YyxxDto> selectHospitalName(YyxxDto yyxxDto) {
		return dao.selectHospitalName(yyxxDto);
	}

	@Override
	public List<YyxxDto> queryByDwmc(YyxxDto yyxxDto) {
		return dao.queryByDwmc(yyxxDto);
	}

	@Override
	public List<YyxxDto> selectOther() {
		// TODO Auto-generated method stub
		return dao.selectOther();
	}
	/**
	 * 根据医院LISid查询医院信息
	 */
	public List<YyxxDto> selectYyxxByLisId(YyxxDto yyxxDto){
		return dao.selectYyxxByLisId(yyxxDto);
	}

	@Override
	public YyxxDto getYyxxBySjid(String sjid) {
		return dao.getYyxxBySjid(sjid);
	}

	/**
	 * @Description: 根据医院名称、医院简称、医院其他名称查询医院信息
	 * @param yyxxDto
	 * @return java.util.List<com.matridx.igams.common.dao.entities.YyxxDto>
	 * @Author: 郭祥杰
	 * @Date: 2025/7/25 9:24
	 */
	@Override
	public List<YyxxDto> queryAllByMc(YyxxDto yyxxDto) {
		return dao.queryAllByMc(yyxxDto);
	}
}

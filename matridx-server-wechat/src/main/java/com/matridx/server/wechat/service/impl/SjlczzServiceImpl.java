package com.matridx.server.wechat.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.SjlczzDto;
import com.matridx.server.wechat.dao.entities.SjlczzModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.post.ISjlczzDao;
import com.matridx.server.wechat.service.svcinterface.ISjlczzService;

@Service
public class SjlczzServiceImpl extends BaseBasicServiceImpl<SjlczzDto, SjlczzModel, ISjlczzDao> implements ISjlczzService{

	/**
	 * 根据送检信息新增临床症状
	 * @param sjxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertBySjxx(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		List<String> lczzs = sjxxDto.getLczzs();
		if(lczzs != null && lczzs.size() > 0){
			dao.deleteBySjxx(sjxxDto);
			List<SjlczzDto> sjlczzDtos = new ArrayList<SjlczzDto>();
			for (int i = 0; i < lczzs.size(); i++) {
				SjlczzDto sjlczzDto = new SjlczzDto();
				sjlczzDto.setSjid(sjxxDto.getSjid());
				sjlczzDto.setXh(String.valueOf(i+1));
				sjlczzDto.setZz(lczzs.get(i));
				sjlczzDtos.add(sjlczzDto);
			}
			int result = dao.insertSjlczzDtos(sjlczzDtos);
			if(result == 0)
				return false;
		}
		return true;
	}

	/**
	 * 根据送检信息修改临床症状
	 * @param sjxxDto
	 * @return
	 */
	/*
	 * @Override public boolean updateBySjxx(SjxxDto sjxxDto) { // TODO
	 * Auto-generated method stub //先删除再新增 boolean isSuccess =
	 * dao.deleteBySjxx(sjxxDto); if(!isSuccess) return false; List<String> lczzs =
	 * sjxxDto.getLczzs(); if(lczzs != null && lczzs.size() > 0){ List<SjlczzDto>
	 * sjlczzDtos = new ArrayList<SjlczzDto>(); for (int i = 0; i < lczzs.size();
	 * i++) { SjlczzDto sjlczzDto = new SjlczzDto();
	 * sjlczzDto.setSjid(sjxxDto.getSjid()); sjlczzDto.setXh(String.valueOf(i+1));
	 * sjlczzDto.setZz(lczzs.get(i)); sjlczzDtos.add(sjlczzDto); } int result =
	 * dao.insertSjlczzDtos(sjlczzDtos); if(result == 0) return false; } return
	 * true; }
	 */
	
	/**
	 * 获取检测项目的String清单
	 * @param sjjcxmDtos
	 * @return
	 */
	public List<String> getStringList(SjlczzDto sjlczzDto){
		return dao.getStringList(sjlczzDto);
	}

	/**
	 * 根据送检ID查询送检临床症状
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjlczzDto> selectLczzBySjid(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.selectLczzBySjid(sjxxDto);
	}

	/**
	 * 根据送检信息新增临床症状
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean insertBySjxxDto(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		dao.deleteBySjxx(sjxxDto);
		List<SjlczzDto> sjlczzDtos = sjxxDto.getSjlczzs();
		if(sjlczzDtos != null && sjlczzDtos.size() > 0){
			for (int i = 0; i < sjlczzDtos.size(); i++) {
				sjlczzDtos.get(i).setSjid(sjxxDto.getSjid());
				sjlczzDtos.get(i).setXh(String.valueOf(i+1));
			}
			int result = dao.insertSjlczzDtos(sjlczzDtos);
			if(result == 0)
				return false;
		}
		return true;
	}
}

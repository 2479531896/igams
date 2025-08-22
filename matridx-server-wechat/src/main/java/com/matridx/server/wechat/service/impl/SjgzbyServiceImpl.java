package com.matridx.server.wechat.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.SjgzbyDto;
import com.matridx.server.wechat.dao.entities.SjgzbyModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.post.ISjgzbyDao;
import com.matridx.server.wechat.service.svcinterface.ISjgzbyService;

@Service
public class SjgzbyServiceImpl extends BaseBasicServiceImpl<SjgzbyDto, SjgzbyModel, ISjgzbyDao> implements ISjgzbyService{
	/**
	 * 根据送检信息新增关注病原
	 * @param sjxxDto
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int insertBySjxx(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		List<String> bys = sjxxDto.getBys();
		if(bys != null && bys.size() > 0){
			//先删除再新增
			dao.deleteBySjxx(sjxxDto);
            List<SjgzbyDto> sjgzbyDtos = new ArrayList<SjgzbyDto>();
            for (int i = 0; i < bys.size(); i++) {
            	SjgzbyDto sjgzbyDto = new SjgzbyDto();
            	sjgzbyDto.setSjid(sjxxDto.getSjid());
            	sjgzbyDto.setBy(bys.get(i));
            	sjgzbyDtos.add(sjgzbyDto);
			}
			int result = dao.insertSjgzbyDtos(sjgzbyDtos);
			return result;
		}
		return 0;
	}
	
	/**
	 * 根据页面的输入列表登录数据库
	 * @param sjgzbyDtos
	 * @return
	 */
	/*
	 * public int updateBySjxx(SjxxDto sjxxDto) { //先删除再新增
	 * dao.deleteBySjxx(sjxxDto); List<String> bys = sjxxDto.getBys(); if(bys !=
	 * null && bys.size() > 0){ List<SjgzbyDto> sjgzbyDtos = new
	 * ArrayList<SjgzbyDto>(); for (int i = 0; i < bys.size(); i++) { SjgzbyDto
	 * sjgzbyDto = new SjgzbyDto(); sjgzbyDto.setSjid(sjxxDto.getSjid());
	 * sjgzbyDto.setBy(bys.get(i)); sjgzbyDtos.add(sjgzbyDto); } int result =
	 * dao.insertSjgzbyDtos(sjgzbyDtos); return result; } return 0; }
	 */
	
	/**
	 * 获取检测项目的String清单
	 * @param sjjcxmDtos
	 * @return
	 */
	public List<String> getStringList(SjgzbyDto sjgzbyDto){
		return dao.getStringList(sjgzbyDto);
	}

	/**
	 * 根据送检信息新增关注病原
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public int insertBySjxxDto(SjxxDto sjxxDto) {
		//先删除再新增
		dao.deleteBySjxx(sjxxDto);
		// TODO Auto-generated method stub
		List<SjgzbyDto> sjgzbyDtos = sjxxDto.getSjgzbys();
		if(sjgzbyDtos != null && sjgzbyDtos.size() > 0){
            for (int i = 0; i < sjgzbyDtos.size(); i++) {
            	sjgzbyDtos.get(i).setSjid(sjxxDto.getSjid());
			}
			int result = dao.insertSjgzbyDtos(sjgzbyDtos);
			return result;
		}
		return 0;
	}
}

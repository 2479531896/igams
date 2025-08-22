package com.matridx.igams.experiment.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.JdmbDto;
import com.matridx.igams.experiment.dao.entities.MbglDto;
import com.matridx.igams.experiment.dao.entities.MbglModel;
import com.matridx.igams.experiment.dao.post.IMbglDao;
import com.matridx.igams.experiment.service.svcinterface.IJdmbService;
import com.matridx.igams.experiment.service.svcinterface.IMbglService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class MbglServiceImpl extends BaseBasicServiceImpl<MbglDto, MbglModel, IMbglDao> implements IMbglService{

	@Autowired
	IJdmbService jdmbService;
	
	/** 
	 * 插入模板信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(MbglDto mbglDto){
			mbglDto.setMbid(StringUtil.generateUUID());
			int result = dao.insert(mbglDto);
		return result != 0;
	}

	/**
	 * 新增模板
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addSaveTemplate(MbglDto mbglDto) {
		// TODO Auto-generated method stub
		boolean result = insertDto(mbglDto);
		if(!result)
			return false;
		//查询默认模板阶段并插入
		List<JdmbDto> jdmbDtos = jdmbService.selectDefaultStage();
		if(jdmbDtos != null && jdmbDtos.size() > 0){
			result = jdmbService.insertByMbid(jdmbDtos);
		}
		if(!result)
			return false;
		//添加默认阶段模板
		List<JdmbDto> mrjdmcs=jdmbService.selectmrjdmb();
		if(mrjdmcs!=null && mrjdmcs.size()>0) {
			for(int i=1;i<=3;i++) {
				JdmbDto mejdmc=mrjdmcs.get(i-1);
				String jdmc=String.valueOf(mejdmc.getJdmc());
				JdmbDto jdmbDto=new JdmbDto();
				jdmbDto.setMbid(mbglDto.getMbid());
				jdmbDto.setJdid(StringUtil.generateUUID());
				jdmbDto.setJdmc(jdmc);
				jdmbDto.setLrry(mbglDto.getLrry());
				jdmbDto.setXh(String.valueOf(i));
				jdmbService.insertdefaultJdmb(jdmbDto);
			}
		}
		
		return true;
	}
	
	/**
	 * 查询所有模板
	 */
	@Override
	public List<MbglDto> getAllMb(MbglDto mbglDto){
		return dao.getAllMb(mbglDto);
	}
	
	/**
	 * 根据模板ID查询模板
	 */
	@Override
	public MbglDto getModById(MbglDto mbglDto) {
		return dao.getModById(mbglDto);
	}
	
	/**
	 * 通过模板名称查询是否存在名称相同的模板(若存在则不能保存模板)
	 */
	public boolean selectMbidByMbmc(MbglDto mbglDto) {
		return dao.selectMbidByMbmc(mbglDto) == null;
	}
}

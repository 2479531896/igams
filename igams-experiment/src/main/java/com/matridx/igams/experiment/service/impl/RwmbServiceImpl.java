package com.matridx.igams.experiment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.experiment.dao.entities.RwmbDto;
import com.matridx.igams.experiment.dao.entities.RwmbModel;
import com.matridx.igams.experiment.dao.post.IRwmbDao;
import com.matridx.igams.experiment.service.svcinterface.IRwmbService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class RwmbServiceImpl extends BaseBasicServiceImpl<RwmbDto, RwmbModel, IRwmbDao> implements IRwmbService{

	@Autowired
	IFjcfbService fjcfbService;
	
	/** 
	 * 插入任务模板信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(RwmbDto rwmbDto){
		rwmbDto.setRwmbid(StringUtil.generateUUID());
		int rwxh=dao.getrwmbxh(rwmbDto);
		rwxh++;
		rwmbDto.setXh(rwxh+"");
		int result = dao.insert(rwmbDto);
		return result != 0;
	}
	/**
	 * 删除当前任务
	 */
	@Override
	public boolean updatescbj(RwmbDto rwmbDto){
		// TODO Auto-generated method stub
		return dao.updatescbj(rwmbDto);
	}
	
	/**
	 * 任务模板重新排序
	 */
	@Override
	public boolean taskSort(RwmbDto rwmbDto) {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(rwmbDto.getJdid()) && StringUtil.isNotBlank(rwmbDto.getRwmbid())){
			int result = dao.updateJdByRwmbid(rwmbDto);
			if(result == 0)
				return false;
		}
		int result = dao.taskSort(rwmbDto);
		return result != 0;
	}
	
	/**
	 * 新增模板任务
	 */
	@Override
	public boolean addSaveTask(RwmbDto rwmbDto) {
		// TODO Auto-generated method stub
		boolean result = insertDto(rwmbDto);
		if(!result)
			return false;
		//文件复制到正式文件夹，插入信息至正式表
		if(rwmbDto.getFjids()!=null && rwmbDto.getFjids().size() > 0){
			for (int i = 0; i < rwmbDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(rwmbDto.getFjids().get(i),rwmbDto.getRwmbid());
				if(!saveFile)
					return false;
			}
		}
		return true;
	}
	
	/**
	 * 更新模板任务
	 */
	@Override
	public boolean updateTaskTemplate(RwmbDto rwmbDto) {
		// TODO Auto-generated method stub
		boolean result = update(rwmbDto);
		if(!result)
			return false;
		//文件复制到正式文件夹，插入信息至正式表
		if(rwmbDto.getFjids()!=null && rwmbDto.getFjids().size() > 0){
			for (int i = 0; i < rwmbDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(rwmbDto.getFjids().get(i),rwmbDto.getRwmbid());
				if(!saveFile)
					return false;
			}
		}
		return true;
	}
	
}

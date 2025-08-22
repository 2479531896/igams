package com.matridx.igams.dmp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.dmp.dao.entities.ZytgfDto;
import com.matridx.igams.dmp.dao.entities.ZytgfModel;
import com.matridx.igams.dmp.dao.post.IZytgfDao;
import com.matridx.igams.dmp.service.svcinterface.IZytgfService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class ZytgfServiceImpl extends BaseBasicServiceImpl<ZytgfDto, ZytgfModel, IZytgfDao> implements IZytgfService{

	/**
	 * 获取资源提供方信息
	 * @return
	 */
	@Override
	public List<ZytgfDto> getZytgfDtoList() {
		// TODO Auto-generated method stub
		return dao.getZytgfDtoList();
	}

	/**
	 * 新增资源提供方
	 * @param zytgfDto
	 * @return
	 */
	@Override
	public boolean addSaveProvider(ZytgfDto zytgfDto) {
		// TODO Auto-generated method stub
		return insertDto(zytgfDto);
	}

	/** 
	 * 插入提供方信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(ZytgfDto zytgfDto){
		zytgfDto.setTgfid(StringUtil.generateUUID());
		int result = dao.insert(zytgfDto);
        return result != 0;
    }
}

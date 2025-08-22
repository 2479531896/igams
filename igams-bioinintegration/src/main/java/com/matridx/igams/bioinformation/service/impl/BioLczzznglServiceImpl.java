package com.matridx.igams.bioinformation.service.impl;


import com.matridx.igams.bioinformation.dao.entities.BioLczzznglDto;
import com.matridx.igams.bioinformation.dao.entities.BioLczzznglModel;
import com.matridx.igams.bioinformation.dao.post.IBioLczzznglDao;
import com.matridx.igams.bioinformation.service.svcinterface.IBioLczzznglService;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BioLczzznglServiceImpl extends BaseBasicServiceImpl<BioLczzznglDto, BioLczzznglModel, IBioLczzznglDao> implements IBioLczzznglService {

	/**
	 * 通过指南ids查询List
	 */
	public List<BioLczzznglDto> getDtoListByIds(BioLczzznglDto lczzznglDto){
		return dao.getDtoListByIds(lczzznglDto);
	}

	/**
	 * 插入
	 */
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(BioLczzznglDto bioLczzznglDto){
		bioLczzznglDto.setZnid(StringUtil.generateUUID());
		int result = dao.insert(bioLczzznglDto);
		return result != 0;
	}

	/**
	 * 修改
	 */
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateDto(BioLczzznglDto bioLczzznglDto){
		int result = dao.update(bioLczzznglDto);
		return result != 0;
	}

	/**
	 * 删除
	 */
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteDto(BioLczzznglDto bioLczzznglDto){
		int result = dao.delete(bioLczzznglDto);
		return result != 0;
	}
}

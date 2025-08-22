package com.matridx.igams.common.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.SyxxDto;
import com.matridx.igams.common.dao.entities.SyxxModel;
import com.matridx.igams.common.dao.post.ISyxxDao;
import com.matridx.igams.common.service.svcinterface.ISyxxService;

@Service
public class SyxxServiceImpl extends BaseBasicServiceImpl<SyxxDto, SyxxModel, ISyxxDao> implements ISyxxService{

	/**
	 * 根据文件类型查询水印信息
	 */
	@Override
	public SyxxDto getDtoByWjlb(String wjlb) {
		// TODO Auto-generated method stub
		return dao.getDtoByWjlb(wjlb);
	}
}

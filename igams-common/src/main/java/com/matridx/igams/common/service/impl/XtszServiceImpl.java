package com.matridx.igams.common.service.impl;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.entities.XtszModel;
import com.matridx.igams.common.dao.post.IXtszDao;
import com.matridx.igams.common.service.svcinterface.IXtszService;

@Service
public class XtszServiceImpl extends BaseBasicServiceImpl<XtszDto, XtszModel, IXtszDao> implements IXtszService{
	@Autowired
	RedisUtil redisUtil;
	/**
	 * 新增或修改系统设置
	 */
	@Override
	public boolean insertOrUpdateXtsz(XtszDto xtszDto) {
		// TODO Auto-generated method stub
		return dao.insertOrUpdateXtsz(xtszDto);
	}

	/**
	 * 根据ID查询系统设置信息
	 */
	@Override
	public XtszDto selectById(String szlb) {
		// TODO Auto-generated method stub
		return dao.selectById(szlb);
	}

	/**
	 * 根据设置类别模糊查询系统设置信息 
	 */
	public List<XtszDto> getObscureDto(String szlb){
		return dao.getObscureDto(szlb);
	}

	/**
	 * 查找系统设置表中的所有数据
	 */
	@Override
	public List<XtszDto> getXtszList() {
		return dao.getXtszList();
	}
}

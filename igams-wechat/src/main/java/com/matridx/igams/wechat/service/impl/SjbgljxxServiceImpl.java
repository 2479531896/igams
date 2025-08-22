package com.matridx.igams.wechat.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjbgljxxDto;
import com.matridx.igams.wechat.dao.entities.SjbgljxxModel;
import com.matridx.igams.wechat.dao.post.ISjbgljxxDao;
import com.matridx.igams.wechat.service.svcinterface.ISjbgljxxService;

@Service
public class SjbgljxxServiceImpl extends BaseBasicServiceImpl<SjbgljxxDto, SjbgljxxModel, ISjbgljxxDao> implements ISjbgljxxService{

	/**
	 * 批量新增
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertList(List<SjbgljxxDto> sjbgljxxDtos) {
		return dao.insertList(sjbgljxxDtos)>0;
	}

}

package com.matridx.igams.wechat.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjkjxxDto;
import com.matridx.igams.wechat.dao.entities.SjkjxxModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.post.ISjkjxxDao;
import com.matridx.igams.wechat.service.svcinterface.ISjkjxxService;

@Service
public class SjkjxxServiceImpl extends BaseBasicServiceImpl<SjkjxxDto, SjkjxxModel, ISjkjxxDao> implements ISjkjxxService{

	/**
	 * 根据送检信息新增送检快捷信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean inserBySjxxDto(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		int result = dao.inserBySjxxDto(sjxxDto);
        return result != 0;
    }
}

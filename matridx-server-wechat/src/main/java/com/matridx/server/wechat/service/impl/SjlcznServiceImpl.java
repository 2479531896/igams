package com.matridx.server.wechat.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.SjlcznDto;
import com.matridx.server.wechat.dao.entities.SjlcznModel;
import com.matridx.server.wechat.dao.post.ISjlcznDao;
import com.matridx.server.wechat.service.svcinterface.ISjlcznService;

@Service
public class SjlcznServiceImpl extends BaseBasicServiceImpl<SjlcznDto, SjlcznModel, ISjlcznDao> implements ISjlcznService{

	/**
	 * 添加送检临床指南(MQ同步)
	 * @param sjlcznDto
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertSjlczn(SjlcznDto sjlcznDto) {
		if(sjlcznDto.getIds()!=null && sjlcznDto.getIds().size()>0) {
			dao.delete(sjlcznDto);
			int result=dao.insert(sjlcznDto);
			if(result >=0) 
				return true;
			return false;
		}
		return true;
	}
}

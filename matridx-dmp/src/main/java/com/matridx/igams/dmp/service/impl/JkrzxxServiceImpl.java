package com.matridx.igams.dmp.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.dmp.dao.entities.JkrzxxDto;
import com.matridx.igams.dmp.dao.entities.JkrzxxModel;
import com.matridx.igams.dmp.dao.post.IJkrzxxDao;
import com.matridx.igams.dmp.service.svcinterface.IJkrzxxService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class JkrzxxServiceImpl extends BaseBasicServiceImpl<JkrzxxDto, JkrzxxModel, IJkrzxxDao> implements IJkrzxxService{

	/** 
	 * 插入注册用户信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(JkrzxxDto jkrzxxDto){
		jkrzxxDto.setRzid(StringUtil.generateUUID());
		int result = dao.insert(jkrzxxDto);
        return result != 0;
    }
}

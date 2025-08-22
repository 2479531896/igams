package com.matridx.igams.dmp.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.dmp.dao.entities.LjfxxDto;
import com.matridx.igams.dmp.dao.entities.LjfxxModel;
import com.matridx.igams.dmp.dao.entities.ZyxxDto;
import com.matridx.igams.dmp.dao.post.ILjfxxDao;
import com.matridx.igams.dmp.service.svcinterface.ILjfxxService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class LjfxxServiceImpl extends BaseBasicServiceImpl<LjfxxDto, LjfxxModel, ILjfxxDao> implements ILjfxxService{

	/**
	 * 根据资源信息Dto新增连接方信息
	 * @param zyxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertLjfxxDtoByZyxxDto(ZyxxDto zyxxDto) {
		// TODO Auto-generated method stub
		zyxxDto.setLjid(StringUtil.generateUUID());
		int result = dao.insertLjfxxDtoByZyxxDto(zyxxDto);
        return result != 0;
    }

}

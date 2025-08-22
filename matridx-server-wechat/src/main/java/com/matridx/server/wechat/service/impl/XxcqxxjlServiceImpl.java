package com.matridx.server.wechat.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.XxcqxxjlDto;
import com.matridx.server.wechat.dao.entities.XxcqxxjlModel;
import com.matridx.server.wechat.dao.post.IXxcqxxjlDao;
import com.matridx.server.wechat.service.svcinterface.IXxcqxxjlService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class XxcqxxjlServiceImpl extends BaseBasicServiceImpl<XxcqxxjlDto, XxcqxxjlModel, IXxcqxxjlDao> implements IXxcqxxjlService{

	/**
	 * 插入学习记录到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(XxcqxxjlDto xxcqxxjlDto){
		xxcqxxjlDto.setJlid(StringUtil.generateUUID());
		int result = dao.insert(xxcqxxjlDto);
        return result != 0;
    }

	/**
	 * 获取最近3条记录正确数量
	 * @param xxcqxxjlDto
	 * @return
	 */
	@Override
	public List<XxcqxxjlDto> getRecentCorrect(XxcqxxjlDto xxcqxxjlDto) {
		// TODO Auto-generated method stub
		return dao.getRecentCorrect(xxcqxxjlDto);
	}
}

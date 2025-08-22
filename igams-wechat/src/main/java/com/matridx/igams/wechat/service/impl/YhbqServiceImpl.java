package com.matridx.igams.wechat.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.YhbqDto;
import com.matridx.igams.wechat.dao.entities.YhbqModel;
import com.matridx.igams.wechat.dao.post.IYhbqDao;
import com.matridx.igams.wechat.service.svcinterface.IYhbqService;

@Service
public class YhbqServiceImpl extends BaseBasicServiceImpl<YhbqDto, YhbqModel, IYhbqDao> implements IYhbqService{

	/**
	 * 保存标签下用户信息
	 * @param yhbqDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertByTags(YhbqDto yhbqDto) {
		// TODO Auto-generated method stub
		dao.deleteByBqid(yhbqDto);
		boolean result = dao.insertByBqid(yhbqDto);
        return result;
    }

}

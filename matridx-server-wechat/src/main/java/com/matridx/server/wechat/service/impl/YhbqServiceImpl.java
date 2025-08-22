package com.matridx.server.wechat.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.WxyhDto;
import com.matridx.server.wechat.dao.entities.YhbqDto;
import com.matridx.server.wechat.dao.entities.YhbqModel;
import com.matridx.server.wechat.dao.post.IYhbqDao;
import com.matridx.server.wechat.service.svcinterface.IYhbqService;

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
        return dao.insertByBqid(yhbqDto);
    }

	/**
	 * 批量新增用户标签
	 * @param wxyhDto
	 * @return
	 */
	@Override
	public boolean insertByWxids(WxyhDto wxyhDto) {
		// TODO Auto-generated method stub
		int result = dao.insertByWxids(wxyhDto);
        return result != 0;
    }

	/**
	 * 批量删除用户标签
	 * @param wxyhDto
	 * @return
	 */
	@Override
	public boolean deleteByWxids(WxyhDto wxyhDto) {
		// TODO Auto-generated method stub
		int result = dao.deleteByWxids(wxyhDto);
        return result != 0;
    }

}

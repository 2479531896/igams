package com.matridx.igams.common.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.LbcxszDto;
import com.matridx.igams.common.dao.entities.LbcxtjszDto;
import com.matridx.igams.common.dao.entities.LbcxtjszModel;
import com.matridx.igams.common.dao.post.ILbcxtjszDao;
import com.matridx.igams.common.service.svcinterface.ILbcxtjszService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class LbcxtjszServiceImpl extends BaseBasicServiceImpl<LbcxtjszDto, LbcxtjszModel, ILbcxtjszDao> implements ILbcxtjszService{

    /**
     * 插入列表查询条件设置信息
     */
	@Override
	public boolean insertByLbcxsz(LbcxszDto lbcxszDto) {
		// TODO Auto-generated method stub
		String xh = StringUtil.generateUUID();
		lbcxszDto.setXh(xh);
		int result = dao.insertByLbcxsz(lbcxszDto);
		return result > 0;
	}

	@Override
	public boolean updateByLbcxsz(LbcxszDto lbcxszDto) {
		int result = dao.updateByLbcxsz(lbcxszDto);
		return result > 0;
	}

	@Override
	public boolean upateLbcxtjszScbj(LbcxszDto lbcxszDto) {
		int result = dao.upateLbcxtjszScbj(lbcxszDto);
		return result > 0;
	}
}

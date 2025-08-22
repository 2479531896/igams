package com.matridx.igams.wechat.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjzmjgDto;
import com.matridx.igams.wechat.dao.entities.SjzmjgModel;
import com.matridx.igams.wechat.dao.post.ISjzmjgDao;
import com.matridx.igams.wechat.service.svcinterface.ISjzmjgService;

@Service
public class SjzmjgServiceImpl extends BaseBasicServiceImpl<SjzmjgDto, SjzmjgModel, ISjzmjgDao> implements ISjzmjgService{
	
	/**
	 * list新增送检自免结果
	 * @param list
	 * @return
	 */
	@Override
	public boolean insertSjzmjg(List<SjzmjgDto> list) {
		// TODO Auto-generated method stub
		return dao.insertSjzmjg(list);
	}

	/**
	 * 根据送检ID删除送检自免结果
	 * @param sjzmjgDto
	 * @return
	 */
	@Override
	public boolean deleteSjzmjg(SjzmjgDto sjzmjgDto) {
		// TODO Auto-generated method stub
		return dao.deleteSjzmjg(sjzmjgDto);
	}

}

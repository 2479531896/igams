package com.matridx.igams.experiment.service.impl;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.QclmxDto;
import com.matridx.igams.experiment.dao.entities.QclmxModel;
import com.matridx.igams.experiment.dao.post.IQclmxDao;
import com.matridx.igams.experiment.service.svcinterface.IQclmxService;

@Service
public class QclmxServiceImpl extends BaseBasicServiceImpl<QclmxDto, QclmxModel, IQclmxDao> implements IQclmxService{
	/**
	 * 查询当前最大的序号
	 */
	@Override
	public int getMaxXh(String qclid)
	{
		// TODO Auto-generated method stub
		return dao.getMaxXh(qclid);
	}
	
	/**
	 * 通过内部编号查询送检id
	 */
	@Override
	public String getSjidByNbbh(String nbbh)
	{
		// TODO Auto-generated method stub
		return dao.getSjidByNbbh(nbbh);
	}

}

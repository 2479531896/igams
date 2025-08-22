package com.matridx.las.netty.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.las.netty.dao.entities.WksyDto;
import com.matridx.las.netty.dao.entities.WksyModel;
import com.matridx.las.netty.dao.post.IWksyDao;
import com.matridx.las.netty.service.svcinterface.IWksyService;

@Service
public class WksyServiceImpl extends BaseBasicServiceImpl<WksyDto, WksyModel, IWksyDao> implements IWksyService{
	/**
	 * 定量仪保存
	 */
	@Override
	public boolean saveDlysy(WksyDto wksyDto) {
		int re = dao.saveDlysy(wksyDto);
		return true;
	}
	/**
	 * 修改
	 */
	@Override
	public boolean updateDlysyDto(WksyDto wksyDto) {
		int re = dao.updateDlysyDto(wksyDto);
		return true;
	}
	
	public WksyDto getWksyDtoBywkid(WksyDto wksyDto) {
		
		return  dao.getWksyDtoBywkid(wksyDto);
	}
}

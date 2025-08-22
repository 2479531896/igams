package com.matridx.igams.common.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.ShlbDto;
import com.matridx.igams.common.dao.entities.ShlbModel;
import com.matridx.igams.common.dao.post.IShlbDao;
import com.matridx.igams.common.service.svcinterface.IShlbService;

@Service
public class ShlbServiceImpl extends BaseBasicServiceImpl<ShlbDto, ShlbModel, IShlbDao> implements IShlbService{

	
    /**
     * 插入审核类别信息
     */
    @Override
    public boolean insert(ShlbModel shlbModel){
    	String shlb = shlbModel.getShlb();
    	shlbModel.setShlb(shlb);
    	int result = dao.insert(shlbModel);
    	return result > 0;
    }
    
	/**
	 * 根据审核类别获取审核类别信息
	 */
	public ShlbDto getShlbxxByShlb(ShlbDto shlbDto) {
		return dao.getShlbxxByShlb(shlbDto);
	}
	
	/**
	 * 根据ids查询审核类别
	 */
	public List<ShlbDto> getShlbxxByIds(ShlbDto shlbDto) {
		return dao.getShlbxxByIds(shlbDto);
	}
	
	/**
	 * 获取审核类别的所有信息
	 */
	@Override
	public List<ShlbDto> getShlbAllData() {
		// TODO Auto-generated method stub
		return dao.getShlbAllData();
	}

	@Override
	public List<ShlbDto> getShlbForHomePage() {
		return dao.getShlbForHomePage();
	}
}

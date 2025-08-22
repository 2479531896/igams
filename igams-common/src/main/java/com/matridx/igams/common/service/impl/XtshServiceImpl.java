package com.matridx.igams.common.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.dao.entities.XtshModel;
import com.matridx.igams.common.dao.post.IXtshDao;
import com.matridx.igams.common.service.svcinterface.IXtshService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class XtshServiceImpl extends BaseBasicServiceImpl<XtshDto, XtshModel, IXtshDao> implements IXtshService{
	
    /**
     * 插入审核信息
     */
    @Override
    public boolean insert(XtshModel xtshModel){
    	String shid = StringUtil.generateUUID();
    	xtshModel.setShid(shid);
    	int result = dao.insert(xtshModel);
    	return result > 0;
    }
	/**
	 * 插入审核信息
	 */
	public boolean insertXtsh(XtshDto xtshDto){
		int result = dao.insert(xtshDto);
		return result > 0;
	}
	/**
	 * 根据ids获取系统审核信息
	 */
    @Override
	public List<XtshDto> getXtshByIds(XtshDto xtshDto){
    	return dao.getXtshByIds(xtshDto);
    }

    /**
     * 到货审核延期列表
     */
	@Override
	public List<XtshDto> getPagedDaoHuoSpyq(XtshDto xtshDto) {
		// TODO Auto-generated method stub
		return dao.getPagedDaoHuoSpyq(xtshDto);
	}

	/**
	 * 查看某条到货审批延期信息
	 */
	@Override
	public XtshDto getDHspyqByShid(XtshDto xtshDto) {
		// TODO Auto-generated method stub
		return dao.getDHspyqByShid(xtshDto);
	}

	@Override
	public List<XtshDto> getGwDtoList(XtshDto xtshDto) {
		return dao.getGwDtoList(xtshDto);
	}
}

package com.matridx.server.wechat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.server.wechat.dao.entities.ZdyszDto;
import com.matridx.server.wechat.dao.entities.ZdyszModel;
import com.matridx.server.wechat.dao.post.IZdyszDao;
import com.matridx.server.wechat.service.svcinterface.IZdyszService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class ZdyszServiceImpl extends BaseBasicServiceImpl<ZdyszDto, ZdyszModel, IZdyszDao> implements IZdyszService{

	@Autowired
	IFjcfbService fjcfbService;
	/**
	 * 插入自定义设置信息
	 * @param zdyfadto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(ZdyszDto zdyszdto){
		zdyszdto.setZdyid(StringUtil.generateUUID());
		int result = dao.insert(zdyszdto);
		if(result == 0)
			return false;
		return true;
	}
	/**
	 * 根据wxid查询该用户保存的自定义设置方案
	 * @param zdyfadto
	 * @return
	 */
	@Override
	public List<ZdyszDto> getZdyProject(ZdyszDto zdyszdto){
		return dao.getZdyProject(zdyszdto);
	}
	
	/**
	 * 设置应用方案
	 * @param list
	 * @return
	 */
	@Override
	public boolean setUseProject(List<ZdyszDto> list) {
		return dao.setUseProject(list);
	}
	
	/**
	 * 新增自定义方案并保存图片至正式表
	 * @param fjcfbDto
	 * @param zdyfadto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addProject(FjcfbDto fjcfbDto,ZdyszDto zdyszdto) {
		boolean result=insertDto(zdyszdto);
		if(!result)
			return false;
		boolean saveimg=fjcfbService.save2RealFile(fjcfbDto.getFjid(), zdyszdto.getZdyid());
		if(!saveimg)
			return false;
		return true;
	}
	
	/**
	 * 添加默认方案
	 * @param zdyfadto
	 * @return
	 */
	public boolean insertMrfa(ZdyszDto zdyszdto) {
		List<ZdyszDto> zdyfalist=getZdyProject(zdyszdto);
		if(zdyfalist!=null && zdyfalist.size()>0) {
			zdyszdto.setZdyid(zdyfalist.get(0).getZdyid());//未启用多个方案的情况下只存在默认方案
			return true;
		}else {
			boolean result=insertDto(zdyszdto);
			return result;
		}
	}
}

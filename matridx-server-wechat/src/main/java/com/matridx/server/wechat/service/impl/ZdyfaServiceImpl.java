package com.matridx.server.wechat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.server.wechat.dao.entities.ZdyfaDto;
import com.matridx.server.wechat.dao.entities.ZdyfaModel;
import com.matridx.server.wechat.dao.post.IZdyfaDao;
import com.matridx.server.wechat.service.svcinterface.IZdyfaService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class ZdyfaServiceImpl extends BaseBasicServiceImpl<ZdyfaDto, ZdyfaModel, IZdyfaDao> implements IZdyfaService{

	@Autowired
	IFjcfbService fjcfbService;
	/**
	 * 插入自定义方案信息
	 * @param zdyszdto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(ZdyfaDto zdyfadto){
		zdyfadto.setFaid(StringUtil.generateUUID());
		int result = dao.insert(zdyfadto);
		if(result == 0)
			return false;
		return true;
	}
	
	/**
	 * 根据wxid查询该用户保存的自定义方案
	 * @param zdyszdto
	 * @return
	 */
	@Override
	public List<ZdyfaDto> getZdyProject(ZdyfaDto zdyfadto){
		return dao.getZdyProject(zdyfadto);
	}
	
	/**
	 * 设置应用方案
	 * @param list
	 * @return
	 */
	@Override
	public boolean setUseProject(List<ZdyfaDto> list) {
		return dao.setUseProject(list);
	}
	/**
	 * 添加默认方案
	 * @param zdyszdto
	 * @return
	 */
	public boolean insertMrfa(ZdyfaDto zdyfadto) {
		List<ZdyfaDto> zdyfalist = getZdyProject(zdyfadto);
		if(zdyfalist!=null && zdyfalist.size()>0) {
			zdyfadto.setFaid(zdyfalist.get(0).getFaid());//未启用多个方案的情况下只存在默认方案
			return true;
		}else {
			boolean result = insertDto(zdyfadto);
			return result;
		}
	}
	
	/**
	 * 新增自定义方案并保存图片至正式表
	 * @param fjcfbDto
	 * @param zdyszdto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addProject(FjcfbDto fjcfbDto,ZdyfaDto zdyfadto) {
		boolean result=insertDto(zdyfadto);
		if(!result)
			return false;
		boolean saveimg=fjcfbService.save2RealFile(fjcfbDto.getFjid(), zdyfadto.getFaid());
		if(!saveimg)
			return false;
		return true;
	}
	
	/**
	 * 根据faid和fasfyy查询已应用的方案信息
	 * @param zdyfadto
	 * @return
	 */
	public ZdyfaDto getYyyFa(ZdyfaDto zdyfadto) {
		return dao.getYyyFa(zdyfadto);
	}
}

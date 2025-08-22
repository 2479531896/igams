package com.matridx.igams.production.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.WjssdwDto;
import com.matridx.igams.production.dao.entities.WjssdwModel;
import com.matridx.igams.production.dao.post.IWjssdwDao;
import com.matridx.igams.production.service.svcinterface.IWjssdwService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WjssdwServiceImpl extends BaseBasicServiceImpl<WjssdwDto, WjssdwModel, IWjssdwDao> implements IWjssdwService{

	/**
	 * 根据文件id查询所属单位信息
	 */
	public WjssdwDto getWjssdwByWjid(WjssdwDto wjssdwDto) {
		return dao.getWjssdwByWjid(wjssdwDto);
	}
	
	/**
	 * 根据文件ids删除所属单位信息
	 */
	public boolean deleteWjssdw(WjssdwDto wjssdwDto) {
		return dao.deleteWjssdw(wjssdwDto);
	}
	
	/**
	 * 根据文件ID更新文件所属信息
	 */
	public void updateWjssdw(WjssdwDto wjssdwDto) {
		//先删除再添加
		WjssdwDto t_wjssdw = new WjssdwDto();
		t_wjssdw.setIds(wjssdwDto.getWjid());
		dao.deleteWjssdw(t_wjssdw);
		wjssdwDto.setXh("1");
		dao.insert(wjssdwDto);
    }

	/**
	 * 分组获取机构信息
	 
	 */
	public List<WjssdwDto> getListGroup(){
		return dao.getListGroup();
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDtoList(List<WjssdwDto> wjssdwDtoList) {
		return dao.insertDtoList(wjssdwDtoList);
	}
}

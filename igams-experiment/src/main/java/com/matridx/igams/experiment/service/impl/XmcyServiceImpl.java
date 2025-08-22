package com.matridx.igams.experiment.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.XmcyDto;
import com.matridx.igams.experiment.dao.entities.XmcyModel;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmjdrwDto;
import com.matridx.igams.experiment.dao.post.IXmcyDao;
import com.matridx.igams.experiment.dao.post.IXmglDao;
import com.matridx.igams.experiment.service.svcinterface.IXmcyService;

@Service
public class XmcyServiceImpl extends BaseBasicServiceImpl<XmcyDto, XmcyModel, IXmcyDao> implements IXmcyService{

	@Autowired
	IXmglDao xmglDao;
	
	/**
	 * 查询项目已有成员
	 */
	@Override
	public List<XmcyDto> selectSelMember(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		return dao.selectSelMember(xmglDto);
	}

	/**
	 * 查询项目未选用户
	 */
	@Override
	public List<XmcyDto> selectUnSelMember(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		return dao.selectUnSelMember(xmglDto);
	}

	/**
	 * 将子项目的成员更新至父项目
	 */
	@Override
	public boolean addMemberToFxm(XmglDto z_xmglDto) {
		// TODO Auto-generated method stub
		dao.addMemberToFxm(z_xmglDto);
		return true;
	}

	/**
	 * 查询用户列表
	 */
	@Override
	public List<String> selectYhidGroup(List<String> xmids) {
		// TODO Auto-generated method stub
		return dao.selectYhidGroup(xmids);
	}

	/**
	 * 根据用户ID更新项目成员
	 */
	@Override
	public boolean updateMemberByYhids(XmglDto f_xmglDto) {
		// TODO Auto-generated method stub
		if(f_xmglDto.getIds() != null && f_xmglDto.getIds().size() > 0){
			boolean isSuccess = dao.deleteXmcyByXmid(f_xmglDto);
			if(!isSuccess)
				return false;
			int result = dao.insertXmcyByYhids(f_xmglDto);
			return result != 0;
		}
		return true;
	}

	/**
	 * 项目成员编辑保存
	 */
	@Override
	public boolean editSaveMember(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		dao.deleteXmcyByXmid(xmglDto);
		List<String> yhids = Arrays.asList(xmglDto.getYhids().split(","));
		xmglDto.setIds(yhids);
		int result = dao.insertXmcyByYhids(xmglDto);
		if(result == 0)
			return false;
		List<XmglDto> xmglDtos = xmglDao.selectProjectStatus(xmglDto);
		if(xmglDtos != null && xmglDtos.size() > 0){
			for (XmglDto dto : xmglDtos) {
				if (!xmglDto.getXmid().equals(dto.getXmid())) {
					//查询项目用户
					List<String> t_yhids = dao.selectYhidsByXmid(dto);
					List<String> s_yhids = dao.selectSelYhids(dto);
					if (s_yhids.containsAll(t_yhids) && t_yhids.containsAll(s_yhids)) {
						break;
					}
					if (t_yhids.size() > 0) {
						dao.deleteXmcyByXmid(dto);
						dto.setIds(t_yhids);
						result = dao.insertXmcyByYhids(dto);
						if (result == 0)
							return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 根据yhids添加项目成员
	 */
	public int insertXmcyByYhids(XmglDto xmglDto) {
		return dao.insertXmcyByYhids(xmglDto);
	}
	
	/**
	 * 根据xmid删除项目成员
	 */
	public boolean deleteXmcyByXmid(XmglDto xmglDto) {
		return dao.deleteXmcyByXmid(xmglDto);
	}
	
	/**
	 * 根据项目id查询项目成员
	 */
	@Override
	public List<XmcyDto> getxmcyByxmid(XmjdrwDto xmjdrwDto){
		// TODO Auto-generated method stub
		return dao.getxmcyByxmid(xmjdrwDto);
	}
}

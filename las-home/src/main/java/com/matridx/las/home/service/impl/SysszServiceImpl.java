package com.matridx.las.home.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.las.home.dao.post.ISysszDao;
import com.matridx.las.home.service.svcinterface.ISysszService;
import com.matridx.las.home.dao.entities.SysszDto;
import com.matridx.las.home.dao.entities.SysszModel;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class SysszServiceImpl extends BaseBasicServiceImpl<SysszDto, SysszModel, ISysszDao> implements ISysszService{

	@Autowired
	IFjcfbService fjcfbService;
	
	/**
	 * 新增实验室信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(SysszDto sysszDto){
		sysszDto.setSysid(StringUtil.generateUUID());
		int result = dao.insert(sysszDto);
		if(result == 0)
			return false;
		return true;
	}
	
	/**
	 * 若实验室不存在则创建
	 * @return
	 */
	@Override
	public SysszDto isExistLab() {
		// TODO Auto-generated method stub
		SysszDto sysszDto = new SysszDto();
		List<SysszDto> sysszDtos = dao.getDtoList(sysszDto);
		if(sysszDtos == null || sysszDtos.size() == 0) {
			// 新增实验室
			insertDto(sysszDto);
			return sysszDto;
		}
		return sysszDtos.get(0);
	}

	/**
	 * 修改实验室信息
	 * @param sysszDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean labConfig(SysszDto sysszDto) {
		// TODO Auto-generated method stub
		int result = dao.update(sysszDto);
		if(result == 0)
			return false;
		// 文件复制到正式文件夹，插入信息至正式表(只取第一个)
		if(sysszDto.getFjids() != null && sysszDto.getFjids().size() > 0) {
			// 删除原有文件
			FjcfbDto fjcfbDto=new FjcfbDto();
			fjcfbDto.setYwlx(sysszDto.getYwlx());
			fjcfbDto.setYwid(sysszDto.getSysid());
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			if (fjcfbDtos != null && fjcfbDtos.size() > 0){
				for (int i = 0; i < fjcfbDtos.size(); i++){
					fjcfbService.delFile(fjcfbDtos.get(i));
				}
			}
			boolean saveFile = fjcfbService.save2RealFile(sysszDto.getFjids().get(0), sysszDto.getSysid() );
			if (!saveFile)
				return false;
		}
		return true;
	}

}

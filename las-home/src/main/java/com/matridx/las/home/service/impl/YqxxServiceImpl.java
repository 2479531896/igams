package com.matridx.las.home.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.las.home.service.svcinterface.IYqxxService;
import com.matridx.las.home.dao.entities.SysszDto;
import com.matridx.las.home.dao.entities.YqxxDto;
import com.matridx.las.home.dao.entities.YqxxModel;
import com.matridx.las.home.dao.post.IYqxxDao;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class YqxxServiceImpl extends BaseBasicServiceImpl<YqxxDto, YqxxModel, IYqxxDao> implements IYqxxService{

	@Autowired
	IFjcfbService fjcfbService;
	
	/**
	 * 新增仪器信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(YqxxDto yqxxDto){
		yqxxDto.setYqid(StringUtil.generateUUID());
		int result = dao.insert(yqxxDto);
		if(result == 0)
			return false;
		return true;
	}
	
	/**
	 * 新增仪器信息保存
	 * @param yqxxDto
	 * @return
	 */
	@Override
	public boolean addSaveDevice(YqxxDto yqxxDto) {
		// TODO Auto-generated method stub
		boolean result = insertDto(yqxxDto);
		if(!result)
			return false;
		// 文件复制到正式文件夹，插入信息至正式表(只取第一个)
		if(yqxxDto.getFjids() != null && yqxxDto.getFjids().size() > 0) {
			boolean saveFile = fjcfbService.save2RealFile(yqxxDto.getFjids().get(0), yqxxDto.getYqid());
			if (!saveFile)
				return false;
		}
		return true;
	}

	/**
	 * 根据实验室查询仪器信息
	 * @param sysszDto
	 * @return
	 */
	@Override
	public List<YqxxDto> getListByLab(SysszDto sysszDto) {
		// TODO Auto-generated method stub
		YqxxDto yqxxDto = new YqxxDto();
		yqxxDto.setSysid(sysszDto.getSysid());
		return dao.getDtoList(yqxxDto);
	}

	/**
	 * 修改仪器信息
	 * @param yqxxDto
	 * @return
	 */
	@Override
	public boolean modSaveDevice(YqxxDto yqxxDto) {
		// TODO Auto-generated method stub
		int result = dao.update(yqxxDto);
		if(result == 0)
			return false;
		// 文件复制到正式文件夹，插入信息至正式表(只取第一个)
		if(yqxxDto.getFjids() != null && yqxxDto.getFjids().size() > 0) {
			// 删除原有文件
			FjcfbDto fjcfbDto=new FjcfbDto();
			fjcfbDto.setYwlx(yqxxDto.getYwlx());
			fjcfbDto.setYwid(yqxxDto.getYqid());
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			if (fjcfbDtos != null && fjcfbDtos.size() > 0){
				for (int i = 0; i < fjcfbDtos.size(); i++){
					fjcfbService.delFile(fjcfbDtos.get(i));
				}
			}
			boolean saveFile = fjcfbService.save2RealFile(yqxxDto.getFjids().get(0), yqxxDto.getYqid());
			if (!saveFile)
				return false;
		}
		return true;
	}

}

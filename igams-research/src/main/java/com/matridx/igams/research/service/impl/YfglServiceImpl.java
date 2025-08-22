package com.matridx.igams.research.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.research.dao.entities.YfglDto;
import com.matridx.igams.research.dao.entities.YfglModel;
import com.matridx.igams.research.dao.post.IYfglDao;
import com.matridx.igams.research.service.svcinterface.IYfglService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class YfglServiceImpl extends BaseBasicServiceImpl<YfglDto, YfglModel, IYfglDao> implements IYfglService{

	@Autowired
	IFjcfbService fjcfbService;
	
	/**
	 * 新增研发信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addSaveResearch(YfglDto yfglDto) {
		// TODO Auto-generated method stub
		insertDto(yfglDto);
		//文件复制到正式文件夹，插入信息至正式表
		if(yfglDto.getFjids()!=null && !yfglDto.getFjids().isEmpty()){
			for (int i = 0; i < yfglDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(yfglDto.getFjids().get(i),yfglDto.getYfid());
				if(!saveFile)
					return false;
			}
		}
		return true;
	}
	
	/** 
	 * 插入研发信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(YfglDto yfglDto){
		yfglDto.setYfid(StringUtil.generateUUID());
		int result = dao.insert(yfglDto);
        return result != 0;
    }
	
	/**
	 * 根据研发ID查询附件表信息
	 */
	@Override
	public List<FjcfbDto> selectFjcfbDtoByYfid(String yfid) {
		// TODO Auto-generated method stub
		List<FjcfbDto> fjcfbDtos= dao.selectFjcfbDtoByYfid(yfid);
        for (FjcfbDto fjcfbDto : fjcfbDtos) {
            String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
            fjcfbDto.setWjmhz(wjmhz);
        }
		return fjcfbDtos;
	}

}

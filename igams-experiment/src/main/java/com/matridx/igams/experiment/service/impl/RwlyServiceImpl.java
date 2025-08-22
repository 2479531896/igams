package com.matridx.igams.experiment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.TaskMassageTypeEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.experiment.dao.entities.RwlyDto;
import com.matridx.igams.experiment.dao.entities.RwlyModel;
import com.matridx.igams.experiment.dao.post.IRwlyDao;
import com.matridx.igams.experiment.service.svcinterface.IRwlyService;

@Service
public class RwlyServiceImpl extends BaseBasicServiceImpl<RwlyDto, RwlyModel, IRwlyDao> implements IRwlyService{
	
	@Autowired
	private IFjcfbService fjcfbService;
	/** 
	 * 插入任务留言到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(RwlyDto rwlyDto){
		rwlyDto.setLylx(TaskMassageTypeEnum.PERSON_MASSAGE.getCode());
		int result = dao.insert(rwlyDto);
		if(result == 0)
			return false;
		if(rwlyDto.getFjids()!=null && rwlyDto.getFjids().size() > 0){
			for (int i = 0; i < rwlyDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(rwlyDto.getFjids().get(i),rwlyDto.getLyid());
				if(!saveFile)
					return false;
			}
			//附件排序
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(rwlyDto.getLyid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_MESSAGE.getCode());
			fjcfbService.fileSort(fjcfbDto);
		}
		return true;
	}
}

package com.matridx.igams.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.dao.entities.GzlcglDto;
import com.matridx.igams.common.dao.entities.GzlcglModel;
import com.matridx.igams.common.dao.post.IGzlcglDao;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGzlcglService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class GzlcglServiceImpl extends BaseBasicServiceImpl<GzlcglDto, GzlcglModel, IGzlcglDao> implements IGzlcglService{
	@Autowired
	IFjcfbService fjcfbService;
	/**
	 * 根据工作管理信息插入工作流程
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertBygzglDto(GzglDto gzglDto) {
		// TODO Auto-generated method stub
		GzlcglDto gzlcglDto = new GzlcglDto();
		gzlcglDto.setSqr(gzglDto.getXgry());
		gzlcglDto.setGzid(gzglDto.getGzid());
		gzlcglDto.setLrry(gzglDto.getXgry());
		return insertDto(gzlcglDto);
	}

	/** 
	 * 插入工作流程到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(GzlcglDto gzlcglDto){
		gzlcglDto.setLcid(StringUtil.generateUUID());
		int result = dao.insert(gzlcglDto);
		return result != 0;
	}

	/**
	 * 根据工作ID查询流程信息
	 */
	@Override
	public List<GzlcglDto> getDtoListByGzid(String gzid) {
		// TODO Auto-generated method stub
		return dao.getDtoListByGzid(gzid);
	}

	/**
	 * 根据确认任务结果新增工作流程
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertGzlcxxByQrrw(GzglDto gzglDto) {
		// TODO Auto-generated method stub
		gzglDto.setLcid(StringUtil.generateUUID());
		int result = dao.insertGzlcxxByQrrw(gzglDto);
		if(result == 0)
			return false;
		//上传附件
		return insertConfirmFile(gzglDto);
	}

	/**
	 * 确认时 添加附件
	 */
	public boolean insertConfirmFile(GzglDto gzglDto) {
		if(!gzglDto.getFjids().isEmpty()){
			for (int i = 0; i < gzglDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(gzglDto.getFjids().get(i),gzglDto.getLcid());
				if(!saveFile)
					return false;
			}
			//附件排序
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(gzglDto.getLcid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_CONFIRM.getCode());
			fjcfbService.fileSort(fjcfbDto);
		}
		return true;
		}
	/**
	 * 批量任务确认新增流程
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertGzlcxxByQrrws(GzglDto gzglDto) {
		// TODO Auto-generated method stub
		List<String> ids = gzglDto.getIds();
		if(ids!=null && !ids.isEmpty()){
			List<GzlcglDto> gzlcglDtos = new ArrayList<>();
			for (String id : ids) {
				GzlcglDto gzlcglDto = new GzlcglDto();
				gzlcglDto.setLcid(StringUtil.generateUUID());
				gzlcglDto.setGzid(id);
				gzlcglDto.setSftg(gzglDto.getSftg());
				gzlcglDto.setShyj(gzglDto.getShyj());
				gzlcglDto.setLrry(gzglDto.getLrry());
				gzlcglDtos.add(gzlcglDto);
			}
			int result = dao.insertGzlcxxByQrrws(gzlcglDtos);
			return result != 0;
		}
		return true;
	}

	@Override
	public List<GzlcglDto> getPagedTaskConfirmedList(GzlcglDto gzlcglDto) {
		return dao.getPagedTaskConfirmedList(gzlcglDto);
	}
}

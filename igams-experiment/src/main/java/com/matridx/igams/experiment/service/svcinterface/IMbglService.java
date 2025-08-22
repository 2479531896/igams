package com.matridx.igams.experiment.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.MbglDto;
import com.matridx.igams.experiment.dao.entities.MbglModel;

public interface IMbglService extends BaseBasicService<MbglDto, MbglModel>{

	/**
	 * 新增模板
	 */
	boolean addSaveTemplate(MbglDto mbglDto);

	/**
	 * 查询所有模板
	 */
	List<MbglDto> getAllMb(MbglDto mbglDto);
	
	/**
	 * 根据模板ID查询模板
	 */
	MbglDto getModById(MbglDto mbglDto);
	

	/**
	 * 通过模板名称查询是否存在名称相同的模板(若存在则不能保存模板)
	 */
	boolean selectMbidByMbmc(MbglDto mbglDto);
}

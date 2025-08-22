package com.matridx.igams.experiment.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.JdmbDto;
import com.matridx.igams.experiment.dao.entities.JdmbModel;
import com.matridx.igams.experiment.dao.entities.MbglDto;

public interface IJdmbService extends BaseBasicService<JdmbDto, JdmbModel>{

	/**
	 * 查询默认阶段模板信息
	 */
	List<JdmbDto> selectDefaultStage();

	/**
	 * 根据模板ID新增阶段模板
	 */
	boolean insertByMbid(List<JdmbDto> jdmbDtos);
	
	/**
	 * 根据当前模板id查询所有阶段
	 */
	List<JdmbDto> selectStageByID(MbglDto mbglDto);
	
	/**
	 * 修改阶段名称
	 */
	boolean updatejdmc(JdmbDto jdmbDto);
	
	/**
	 * 查询默认阶段模板
	 */
	List<JdmbDto> selectmrjdmb();
	
	/**
	 * 添加对应模板id的默认阶段模板
	 */
	boolean insertdefaultJdmb(JdmbDto jdmbDto);
	
	/**
	 * 阶段模板排序
	 */
	boolean stageSort(JdmbDto jdmbDto);

	/**
	 * 根据模板ID查询阶段模板信息
	 */
	List<JdmbDto> selectJdmbByMbid(String mbid);

}

package com.matridx.igams.detection.molecule.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxmModel;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;

import java.util.List;
import java.util.Map;


public interface IFzjcxmPJService extends BaseBasicService<FzjcxmDto, FzjcxmModel>{
	/**

	/**
     * 根据分子检测id查询有关的检测项目信息
     */
     List<FzjcxmDto> getDtoListByFzjcid(String fzjcid);

	/**
	 * 根据分子检测id查询有关的检测项目信息
	 */
	 List<FzjcxmDto> getXmGroupList(FzjcxmDto fzjcxmDto);

	/**
	 * 根据分子检测id查询分子检测基本信息
	 */
	 List<Map<String,Object>> getFzjcxxInfoForGenerateReport(FzjcxmDto fzjcxmDto);
	 int insertListJcxmAndjczxm(List<FzjcxmDto> list);

	/**
	 * 跟进分子检测ID获取检测项目信息
	 * @param fzjcxxDto
	 * @return
	 */
	List<FzjcxmDto> getJcxmListAndBgByFzjcid(FzjcxxDto fzjcxxDto);
	/**
	 * 处理并生成报告
	 */
    boolean dealAndGenerateReport(FzjcxmDto fzjcxmDto) throws BusinessException;
	/*
		通过分子项目id删除分子检测项目
	 */
	void delFzjcxmByIds(FzjcxmDto fzjcxmDto);
	/*
		删除普检分子检测项目
	 */
	void delFzjcxmByFzjc(FzjcxmDto fzjcxmDto);

	boolean updateFzjcxmDtos(List<FzjcxmDto> fzjcxmDtos);

	List<FzjcxmDto> getJcxmListByFzjcid(String fzjcid);
}

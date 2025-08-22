package com.matridx.igams.detection.molecule.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxmModel;


public interface IFzjcxmService extends BaseBasicService<FzjcxmDto, FzjcxmModel>{
	/**
 	 * 检测项目明细
     */
	List<Map<String,String>> generateReportList(String fzxmid);

     List<FzjcxmDto> getDtoListByFzjcid(FzjcxmDto fzjcxmDto);

	/**
	 * 根据分子检测id查询有关的检测项目信息
	 */
	 List<FzjcxmDto> getFzjcxmxxList(FzjcxmDto fzjcxmDto);

	/**
	 * 增加分子检测项目
	 */
	boolean insertFzjcxmDto(FzjcxmDto fzjcxmDto);

	/**
	 * 根据分子检测信息的主键id删除关联的分子检测项目
	 */
	boolean delFzjcxmByFzjcid(FzjcxmDto fzjcxmDto);

	/**
	 * 根据分子检测信息的主键id删除关联的分子检测项目
	 */
	int deleteFzjcxmByFzjcid(String fzjcid);
}

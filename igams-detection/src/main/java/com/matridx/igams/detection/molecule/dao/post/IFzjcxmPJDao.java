package com.matridx.igams.detection.molecule.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxmModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface IFzjcxmPJDao extends BaseBasicDao<FzjcxmDto, FzjcxmModel>{
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

	List<FzjcxmDto> getJcxmListByFzjcid(String fzjcid);
	/*
		修改状态
	 */
	void updateZt(FzjcxmDto fzjcxmDto);
	/*
    通过分子项目id删除分子检测项目
 	*/
	void delFzjcxmByIds(FzjcxmDto fzjcxmDto);
	/*
		通过分子检测id删除分子检测项目
 	*/
	void delFzjcxmByFzjc(FzjcxmDto fzjcxmDto);

	boolean updateFzjcxmDtos(List<FzjcxmDto> fzjcxmDtos);
}

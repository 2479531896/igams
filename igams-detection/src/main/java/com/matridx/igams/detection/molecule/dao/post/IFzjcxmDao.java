package com.matridx.igams.detection.molecule.dao.post;

import com.matridx.igams.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxmModel;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;


@Mapper
public interface IFzjcxmDao extends BaseBasicDao<FzjcxmDto, FzjcxmModel>{
	/**
 	 * 检测项目明细
     */
	List<Map<String,String>> generateReportList(String fzxmid);

    List<FzjcxmDto> getDtoListByFzjcid(FzjcxmDto fzjcxmDto);
    int deleteFzjcxmByFzjcid(String id);

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
	 * 成组插入分子检测项目
	 */
	boolean insertList(List<FzjcxmDto> fzjcxmDto);
}

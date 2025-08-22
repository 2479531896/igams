package com.matridx.server.detection.molecule.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.detection.molecule.dao.entities.FzjcjgDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxmModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IFzjcxmDao extends BaseBasicDao<FzjcxmDto, FzjcxmModel>{

	/**
	 * 增加分子检测结果
	 * @param fzjcjgDto
	 */
	void insertDto(FzjcjgDto fzjcjgDto);

	/**
	 * 根据分子检测信息的主键id删除关联的分子检测项目
	 * @param fzjcid
	 */
	void deleteFzjcxmByFzjcid(String fzjcid);

	/**
	 * 增加分子检测项目
	 * @param fzjcxmDto
	 * @return
	 */
	boolean insertFzjcxmDto(FzjcxmDto fzjcxmDto);

	/**
	 * 根据分子检测信息的主键id删除关联的分子检测项目
	 * @param fzjcxmDto
	 */
	boolean delFzjcxmByFzjcid(FzjcxmDto fzjcxmDto);
}

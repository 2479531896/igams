package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.storehouse.dao.entities.LlcglDto;
import com.matridx.igams.storehouse.dao.entities.LlcglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ILlcglDao extends BaseBasicDao<LlcglDto, LlcglModel>{

	/**
	 * 领料车列表
	 * @param llcglDto
	 * @return
	 */
	List<LlcglDto> getLlcDtoList(LlcglDto llcglDto);
	boolean insertLlxxList(List<QgmxDto> list);
	boolean deleteLlxxlist(List<QgmxDto> list);

	boolean insertLlxxList(LlcglDto llcglDto);

	boolean insetLlcglDtos(LlcglDto llcglDto);
}

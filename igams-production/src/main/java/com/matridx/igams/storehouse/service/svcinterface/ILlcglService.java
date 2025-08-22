package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.storehouse.dao.entities.LlcglDto;
import com.matridx.igams.storehouse.dao.entities.LlcglModel;

import java.util.List;

public interface ILlcglService extends BaseBasicService<LlcglDto, LlcglModel>{

	/**
	 * 领料车列表
	 * @param llcglDto
	 * @return
	 */
    List<LlcglDto> getLlcDtoList(LlcglDto llcglDto);
	/**
	 * 批量插入领料车
	 */
	boolean insertLlxxList(List<QgmxDto> list);
	boolean deleteLlxxlist(List<QgmxDto> list);

	/**
	 * @Description: 批量新增
	 * @param llcglDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/7/8 17:12
	 */
	boolean insetLlcglDtos(LlcglDto llcglDto);

	/**
	 * @Description: 批量修改
	 * @param llcglDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/7/8 17:12
	 */
	boolean delLlcglDtos(LlcglDto llcglDto);
}


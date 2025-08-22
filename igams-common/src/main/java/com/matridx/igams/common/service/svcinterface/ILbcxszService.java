package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

import com.matridx.igams.common.dao.entities.LbcxszDto;
import com.matridx.igams.common.dao.entities.LbcxszModel;

public interface ILbcxszService extends BaseBasicService<LbcxszDto, LbcxszModel>{

	/**
	 * 删除列表
	 */
	boolean deleteDto(LbcxszDto lbcxszDto);

	/**
	 * 获取操作代码同时根据操作代码名称标识操作代码信息
	 */
	List<LbcxszDto> getAllCzdmAndChecked(LbcxszDto lbcxszDto);

}

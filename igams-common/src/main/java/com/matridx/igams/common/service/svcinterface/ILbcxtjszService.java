package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.LbcxszDto;
import com.matridx.igams.common.dao.entities.LbcxtjszDto;
import com.matridx.igams.common.dao.entities.LbcxtjszModel;

public interface ILbcxtjszService extends BaseBasicService<LbcxtjszDto, LbcxtjszModel>{

	/**
	 * 插入查询条件设置信息
	 */
	 boolean insertByLbcxsz(LbcxszDto lbcxszDto);

	/**
	 * 修改查询条件设置信息
	 */
	 boolean updateByLbcxsz(LbcxszDto lbcxszDto);

	/**
	 * 更新列表查询条件设置的删除状态
	 */
	 boolean upateLbcxtjszScbj(LbcxszDto lbcxszDto);

}

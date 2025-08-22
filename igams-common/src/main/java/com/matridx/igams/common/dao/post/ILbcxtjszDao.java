package com.matridx.igams.common.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.LbcxszDto;
import com.matridx.igams.common.dao.entities.LbcxtjszDto;
import com.matridx.igams.common.dao.entities.LbcxtjszModel;

@Mapper
public interface ILbcxtjszDao extends BaseBasicDao<LbcxtjszDto, LbcxtjszModel>{

	/**
	 * 插入列表查询条件设置信息
	 * lbcxszDto
	 * 
	 */
	 int insertByLbcxsz(LbcxszDto lbcxszDto);

	/**
	 * 修改列表查询条件设置信息
	 * lbcxszDto
	 * 
	 */
	 int updateByLbcxsz(LbcxszDto lbcxszDto);

	/**
	 * 更新列表查询条件设置的删除状态
	 * lbcxszDto
	 */
	 int upateLbcxtjszScbj(LbcxszDto lbcxszDto);

}

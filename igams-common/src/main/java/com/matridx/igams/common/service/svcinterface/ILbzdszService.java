package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.dao.entities.LbcxszDto;
import com.matridx.igams.common.dao.entities.LbzdszDto;
import com.matridx.igams.common.dao.entities.LbzdszModel;

public interface ILbzdszService extends BaseBasicService<LbzdszDto, LbzdszModel>{

	/**
	 * 插入字段设置信息
	 */
	 boolean insertByLbcxsz(LbcxszDto lbcxszDto);

	/**
	 * 批量插入
	 */
	 int insertByLbcxszlist(List<LbzdszDto>list);
	/**
	 * 修改字段设置信息
	 */
	 boolean updateByLbcxsz(LbcxszDto lbcxszDto);

	/**
	 * 删除查询字段设置信息
	 */
	 boolean deleteLbzdszb(LbcxszDto lbcxszDto);

	/**
	 * 根据ywid删除
	 */
	 boolean deleteLbzdszbyywid(LbzdszDto lbzdszDto);
	/**
	 * 根据用户信息获取用户未选择的字段信息
	 */
	 List<LbzdszDto> getWaitList(LbzdszDto lbzdszDto);
	
	/**
	 * 查出共有多少种ywid在lbzdsz表中
	 */
	 List<String> getYwmc();
	
	/**
	 * 通过ywid 查询导出字段
	 */
	 List<LbzdszDto> getDczd(String ywid);
	/*
	* 列表显示字段权限
	* */
	 List<LbzdszDto> getXszdQx(Map<String,String> map);
}

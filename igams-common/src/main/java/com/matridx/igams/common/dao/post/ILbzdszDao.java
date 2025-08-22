package com.matridx.igams.common.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.LbcxszDto;
import com.matridx.igams.common.dao.entities.LbzdszDto;
import com.matridx.igams.common.dao.entities.LbzdszModel;

@Mapper
public interface ILbzdszDao extends BaseBasicDao<LbzdszDto, LbzdszModel>{

	/**
	 * 新增列表字段设置信息
	 * lbcxszDto
	 * 
	 */
	 int insertByLbcxsz(LbcxszDto lbcxszDto);

	/**
	 * 批量设置
	 * list
	 * 
	 */
	 int insertByLbcxszlist(List<LbzdszDto>list);
	/**
	 * 修改列表字段设置信息
	 * lbcxszDto
	 * 
	 */
	 int updateByLbcxsz(LbcxszDto lbcxszDto);

	/**
	 * 删除查询字段设置信息
	 * lbcxszDto
	 * 
	 */
	 int deleteLbzdszb(LbcxszDto lbcxszDto);

	/**
	 * 根据业务id删除
	 * lbzdszDto
	 * 
	 */
	 int deleteLbzdszbyywid(LbzdszDto lbzdszDto);
	/**
	 * 根据用户信息获取用户未选择的字段信息
	 * lbzdszDto
	 * 
	 */
	 List<LbzdszDto> getWaitList(LbzdszDto lbzdszDto);
	
	/**
	 * 查出共有多少种ywid在lbzdsz表中
	 * 
	 */
	 List<String> getYwmc();
	
	/**
	 * 通过ywid 查询导出字段
	 * ywid
	 * 
	 */
	 List<LbzdszDto> getDczd(String ywid);

	List<LbzdszDto> getXszdQx(Map<String,String> map);
}

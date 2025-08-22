package com.matridx.igams.hrm.dao.post;


import java.util.List;

import com.matridx.igams.hrm.dao.entities.YhcqtzDto;
import com.matridx.igams.hrm.dao.entities.YhcqtzModel;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;

@Mapper
public interface IYhcqtzDao extends BaseBasicDao<YhcqtzDto, YhcqtzModel>{

	/**
	 * 根据yhid获取用户出勤信息
	 */
	YhcqtzDto getCqxxByYhid(YhcqtzDto yhcqtzDto);


	
	/**
	 * 根据ids删除用户出勤信息
	 */
	boolean delYhcqtzxx(YhcqtzDto yhcqtzDto);

	
	/**
	 * 更新出勤退勤信息
	 */
	boolean updateCqAndTq(List<YhcqtzDto> list);

	/**
	 * 更新出勤退勤信息
	 */
	boolean updateSj(YhcqtzDto yhcqtzDto);
	
	
	/**
	 * 获取用户出勤信息list(不分页)
	 */
	List<YhcqtzDto> getYhcqList(YhcqtzDto yhcqtzDto);
	
	/**
	 * 修改时根据原用户id删除用户出勤信息
	 */
	boolean delByYyhid(YhcqtzDto yhcqtzDto);



}

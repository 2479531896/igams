package com.matridx.igams.common.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.DdxxglModel;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IDdxxglDao extends BaseBasicDao<DdxxglDto, DdxxglModel>{

	/**
	 * 根据钉钉消息类型查询用户信息
	 */
	List<DdxxglDto> selectByDdxxlx(String ddxxlx);
	
	/**
	 * 查询系统用户
	 */
	List<DdxxglDto> getXtyh();

	/**
	 * 根据部门ID查询审批流程
	 */
	List<DdxxglDto> getProcessByJgid(@Param("jgid") String jgid,@Param("splx") String splx);

	/**
	 * 根据审批类型查询钉钉审批机构
	 */
	List<DdxxglDto> getDingtalkAuditDep(String splx);
	
	/**
	 * 根据审批ID获取钉钉审批流程信息
	 */
	DdxxglDto getProcessBySpid(String spid);

	/**
	 * 根据钉钉消息类型查找对应的用户
	 */
	List<DdxxglDto> getDdidByDdxxlx(DdxxglDto ddxxglDto);
}

package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.JkdymxDto;
import com.matridx.igams.common.dao.entities.JkdymxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IJkdymxDao extends BaseBasicDao<JkdymxDto, JkdymxModel>{

	/**
	 * 根据业务ID查询调用信息(报告)
	 */
	List<JkdymxDto> getListByYwid(String ywid);

	/**
	 * 根据dyid查询调用信息(报告)
	 */
	List<JkdymxDto> getPageJkdymxDtoList(JkdymxDto jkdymxDto);

	List<String> getSearchItems(String key);

	/**
	 * 查询调用信息(报告)
	 * @param jkdymxDto
	 * @return
	 */
    List<JkdymxDto> selectReportInfo(JkdymxDto jkdymxDto);
}

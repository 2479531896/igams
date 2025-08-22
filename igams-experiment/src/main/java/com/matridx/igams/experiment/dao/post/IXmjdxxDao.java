package com.matridx.igams.experiment.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmjdrwDto;
import com.matridx.igams.experiment.dao.entities.XmjdxxDto;
import com.matridx.igams.experiment.dao.entities.XmjdxxModel;

@Mapper
public interface IXmjdxxDao extends BaseBasicDao<XmjdxxDto, XmjdxxModel>{

	/**
	 * 根据项目ID查询阶段列表
	 */
	List<XmjdxxDto> selectStageByXmid(XmglDto xmglDto);


	/**
	 * 根据项目ID查询最小时间
	 */
	String selectMinTimeByXmid(XmglDto xmglDto);
	
	/**
	 * 查询最大的xh
	 */
	int getXh(XmjdxxDto xmjdxxDto);

	/**
	 * 项目阶段排序
	 */
	int stageSort(List<XmjdxxDto> xmjdxxDtos);
	
	/**
	 * 根据frwid查询子任务所在阶段
	 */
	List<XmjdxxDto> getJdsByFrwid(XmjdrwDto xmjdrwDto);

	/**
	 * 批量新增阶段
	 */
	int insertByDtos(List<XmjdxxDto> xmjdDtos);

	/**
	 * 查询序号后面的阶段
	 */
	List<XmjdxxDto> selectStageByXh(XmjdxxDto xmjdxxDto);

	/**
	 * 批量修改阶段日期
	 */
	int updateByDtos(List<XmjdxxDto> xmjdxxDtos);
}

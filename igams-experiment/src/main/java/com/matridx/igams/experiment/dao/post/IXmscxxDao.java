package com.matridx.igams.experiment.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmscxxDto;
import com.matridx.igams.experiment.dao.entities.XmscxxModel;

@Mapper
public interface IXmscxxDao extends BaseBasicDao<XmscxxDto, XmscxxModel>{

	/**
	 * 获取删除项目列表
	 */
	List<XmglDto> getRecoverDtoList(XmglDto xmglDto);

	/**
	 * 根据项目ID删除数据
	 */
	int deleteByXmglDto(XmglDto xmglDto);

}

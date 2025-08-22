package com.matridx.igams.experiment.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmscxxDto;
import com.matridx.igams.experiment.dao.entities.XmscxxModel;

public interface IXmscxxService extends BaseBasicService<XmscxxDto, XmscxxModel>{

	/**
	 * 将项目删除信息加入到删除信息表
	 */
	boolean insertByXmglDto(XmglDto xmglDto);
	
	/**
	 * 获取删除项目列表
	 */
	List<XmglDto> getRecoverDtoList(XmglDto xmglDto);

	/**
	 * 根据项目ID删除数据
	 */
	boolean deleteByXmglDto(XmglDto xmglDto);

}

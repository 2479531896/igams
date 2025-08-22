package com.matridx.igams.experiment.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.QclglDto;
import com.matridx.igams.experiment.dao.entities.QclglModel;

@Mapper
public interface IQclglDao extends BaseBasicDao<QclglDto, QclglModel>{
	
	/**
	 * 根据检测单位和日期确定xh
	 */
	int getXh(QclglDto qclglDto);
}

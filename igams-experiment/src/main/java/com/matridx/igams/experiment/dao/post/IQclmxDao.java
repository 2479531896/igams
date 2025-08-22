package com.matridx.igams.experiment.dao.post;

import org.apache.ibatis.annotations.Mapper;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.QclmxDto;
import com.matridx.igams.experiment.dao.entities.QclmxModel;

@Mapper
public interface IQclmxDao extends BaseBasicDao<QclmxDto, QclmxModel>{

	/**
	 * 查询当前最大的序号
	 */
	int getMaxXh(String qclid);
	
	/**
	 * 通过内部编号查询送检id
	 */
	String getSjidByNbbh(String nbbh);
}

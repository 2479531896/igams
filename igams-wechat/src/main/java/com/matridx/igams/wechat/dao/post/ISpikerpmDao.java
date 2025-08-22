package com.matridx.igams.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SpikerpmDto;
import com.matridx.igams.wechat.dao.entities.SpikerpmModel;

@Mapper
public interface ISpikerpmDao extends BaseBasicDao<SpikerpmDto, SpikerpmModel>{
	
	/**
	 * 根据检测项目和标本类型获取阈值信息
	 * @param spikerpmDto
	 * @return
	 */
    SpikerpmDto getDtoByJcxmAndYblx(SpikerpmDto spikerpmDto);
}

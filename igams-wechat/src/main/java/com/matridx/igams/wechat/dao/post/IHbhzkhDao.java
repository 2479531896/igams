package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.HbhzkhDto;
import com.matridx.igams.wechat.dao.entities.HbhzkhModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IHbhzkhDao extends BaseBasicDao<HbhzkhDto, HbhzkhModel>{

	/**
	 * 批量插入
	 * @param list
	 * @return
	 */
	Boolean insertList(List<HbhzkhDto> list);
}

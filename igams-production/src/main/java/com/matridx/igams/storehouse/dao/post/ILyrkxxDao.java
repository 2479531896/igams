package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.LyrkxxDto;
import com.matridx.igams.storehouse.dao.entities.LyrkxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ILyrkxxDao extends BaseBasicDao<LyrkxxDto, LyrkxxModel>{

	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
	boolean insertList(List<LyrkxxDto> list);

}

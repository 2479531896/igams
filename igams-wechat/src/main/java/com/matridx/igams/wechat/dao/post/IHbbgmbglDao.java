package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.HbbgmbglDto;
import com.matridx.igams.wechat.dao.entities.HbbgmbglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IHbbgmbglDao extends BaseBasicDao<HbbgmbglDto, HbbgmbglModel> {
	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
    boolean insertList(List<HbbgmbglDto> list);

}

package com.matridx.server.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.HbsfbzDto;
import com.matridx.server.wechat.dao.entities.HbsfbzModel;

import java.util.List;

@Mapper
public interface IHbsfbzDao extends BaseBasicDao<HbsfbzDto, HbsfbzModel>{
	/**
	 * 获取默认收费标准
	 * @param hbsfbzDto
	 * @return
	 */
    HbsfbzDto getDefaultDto(HbsfbzDto hbsfbzDto);

	/**
	 * 添加收费标准
	 * @param hbsfbzDto
	 * @return
	 */
    boolean insertsfbz(List<HbsfbzDto> hbsfbzDto);
	/**
	 * 批量修改
	 * @param list
	 * @return
	 */
	Boolean updateList(List<HbsfbzDto> list);

    boolean batchModSfbz(List<HbsfbzDto> list);
}

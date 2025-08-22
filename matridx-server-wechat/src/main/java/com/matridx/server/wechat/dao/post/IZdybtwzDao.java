package com.matridx.server.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.ZdybtwzDto;
import com.matridx.server.wechat.dao.entities.ZdybtwzModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IZdybtwzDao extends BaseBasicDao<ZdybtwzDto, ZdybtwzModel>{

	/**
	 * 根据方案ID删除信息
	 * @param faid
	 * @return
	 */
	int deleteByFaid(String faid);

	/**
	 * 批量新增标题位置信息
	 * @param zdybtwzDtos
	 * @return
	 */
	int insertByZdybtwzDtos(List<ZdybtwzDto> zdybtwzDtos);

	/**
	 * 根据方案id查询当前已应用方案的自定义位置信息
	 * @param zdybtwzDto
	 * @return
	 */
    List<ZdybtwzDto> getZdybtwzByFaid(ZdybtwzDto zdybtwzDto);
}

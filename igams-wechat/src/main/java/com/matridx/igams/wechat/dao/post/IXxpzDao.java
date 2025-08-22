package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.XxpzDto;
import com.matridx.igams.wechat.dao.entities.XxpzModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IXxpzDao extends BaseBasicDao<XxpzDto, XxpzModel>{

	/**
	 * 获取信息配置
	 * @param xxpzDto
	 * @return
	 */
    List<XxpzDto> getXxpzList(XxpzDto xxpzDto);
}

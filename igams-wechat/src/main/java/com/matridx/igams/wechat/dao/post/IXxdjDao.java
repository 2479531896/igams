package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.XxdjDto;
import com.matridx.igams.wechat.dao.entities.XxdjModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IXxdjDao extends BaseBasicDao<XxdjDto, XxdjModel>{
	
	/**
	 * 从数据库分页获取导出送检信息数据
	 * @param xxdjDto
	 * @return
	 */
    List<XxdjDto> getListForSearchExp(XxdjDto xxdjDto);
	
	/**
	 * 根据搜索条件获取导出条数
	 * @param xxdjDto
	 * @return
	 */
    int getCountForSearchExp(XxdjDto xxdjDto);
	
	
}

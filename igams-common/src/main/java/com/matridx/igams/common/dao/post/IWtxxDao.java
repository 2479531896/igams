package com.matridx.igams.common.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.WtxxDto;
import com.matridx.igams.common.dao.entities.WtxxModel;

@Mapper
public interface IWtxxDao extends BaseBasicDao<WtxxDto, WtxxModel>{
	/**
	 * 获取有效的指定的委托人
	 */
	 User getWtr(Map<String,Object> param);
	
	/**
	 * 根据给定用户ID和角色信息查询其所有资源对应权限限定标记
	 */
	 List<Map<String, Object>> listQxxdbjByYh(@Param("yhid")String yhid, @Param("jsIds")List<String> jsIds);
}

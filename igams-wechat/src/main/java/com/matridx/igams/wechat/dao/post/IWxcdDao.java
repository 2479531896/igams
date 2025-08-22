package com.matridx.igams.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.WxcdDto;
import com.matridx.igams.wechat.dao.entities.WxcdModel;

@Mapper
public interface IWxcdDao extends BaseBasicDao<WxcdDto, WxcdModel>{

	/**
	 * 查询微信菜单信息
	 * @return
	 */
	List<WxcdDto> getWxcdTreeList(WxcdDto wxcdDto);

	/**
	 * 获取数据包含个性类型
	 * @return
	 */
	List<String> getGxlxList();

	/**
	 * 删除菜单及子菜单
	 * @param wxcdDto
	 * @return
	 */
	boolean deleteByCdid(WxcdDto wxcdDto);

}

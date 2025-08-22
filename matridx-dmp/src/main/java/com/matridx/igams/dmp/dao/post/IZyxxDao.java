package com.matridx.igams.dmp.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.dmp.dao.entities.RzqxDto;
import com.matridx.igams.dmp.dao.entities.ZyxxDto;
import com.matridx.igams.dmp.dao.entities.ZyxxModel;

@Mapper
public interface IZyxxDao extends BaseBasicDao<ZyxxDto, ZyxxModel>{

	/**
	 * 获取权限资源列表
	 * @param rzid
	 * @return
	 */
	List<ZyxxDto> getZyxxTreeList(String rzid);

	/**
	 * 根据认证ID更新权限删除状态
	 * @param zyxxDto
	 */
	void updateRzqx(ZyxxDto zyxxDto);

	/**
	 * 批量新增权限
	 * @param rzqxDtos
	 * @return
	 */
	int batchSaveRzqx(List<RzqxDto> rzqxDtos);
	
	/**
	 * 根据用户代码和资源代码，获取用户授权信息
	 * @param zyxxDto
	 * @return
	 */
	public List<ZyxxDto> getAuthList(ZyxxDto zyxxDto);

}

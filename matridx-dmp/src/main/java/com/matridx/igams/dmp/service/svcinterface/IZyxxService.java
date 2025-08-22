package com.matridx.igams.dmp.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.dmp.dao.entities.ZyxxDto;
import com.matridx.igams.dmp.dao.entities.ZyxxModel;

public interface IZyxxService extends BaseBasicService<ZyxxDto, ZyxxModel>{

	/**
	 * 获取权限资源列表
	 * @param rzid
	 * @return
	 */
	List<ZyxxDto> getZyxxTreeList(String rzid);

	/**
	 * 组装用户权限资源列表树
	 * @param zyxxList
	 * @param jSONDATA
	 * @return
	 */
	String installTree(List<ZyxxDto> zyxxList, String jSONDATA);

	/**
	 * 保存用户权限信息
	 * @param zyxxDto
	 * @return
	 * @throws BusinessException 
	 */
	boolean saveCompetenceArray(ZyxxDto zyxxDto) throws BusinessException;

	/**
	 * 数据新增保存
	 * @param zyxxDto
	 * @return
	 */
	boolean addSaveDataPlat(ZyxxDto zyxxDto);

	/**
	 * 获取数据库枚举类型
	 * @return
	 */
	List<String> getSjklx();

	/**
	 * 获取调用方式枚举类型
	 * @return
	 */
	List<String> getDyfs();

	/**
	 * 获取请求方式枚举类型
	 * @return
	 */
	List<String> getQqfs();

	/**
	 * 根据用户代码和资源代码，获取用户授权信息
	 * @param zyxxDto
	 * @return
	 */
    List<ZyxxDto> getAuthList(ZyxxDto zyxxDto);
}

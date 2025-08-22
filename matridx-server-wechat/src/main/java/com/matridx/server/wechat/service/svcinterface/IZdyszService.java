package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.ZdyszDto;
import com.matridx.server.wechat.dao.entities.ZdyszModel;

public interface IZdyszService extends BaseBasicService<ZdyszDto, ZdyszModel>{

	/**
	 * 根据wxid查询该用户保存的自定义设置方案
	 * @param zdyszdto
	 * @return
	 */
	List<ZdyszDto> getZdyProject(ZdyszDto zdyszdto);
	
	/**
	 * 设置应用方案
	 * @param list
	 * @return
	 */
	boolean setUseProject(List<ZdyszDto> list);
	
	/**
	 * 新增自定义方案并保存图片至正式表
	 * @param fjcfbDto
	 * @param zdyszdto
	 * @return
	 */
    boolean addProject(FjcfbDto fjcfbDto, ZdyszDto zdyszdto);
	
	/**
	 * 添加默认方案
	 * @param zdyszdto
	 * @return
	 */
    boolean insertMrfa(ZdyszDto zdyszdto);
}

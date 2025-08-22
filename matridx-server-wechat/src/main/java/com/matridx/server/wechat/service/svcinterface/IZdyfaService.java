package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.ZdyfaDto;
import com.matridx.server.wechat.dao.entities.ZdyfaModel;

public interface IZdyfaService extends BaseBasicService<ZdyfaDto, ZdyfaModel>{

	/**
	 * 根据wxid查询该用户保存的自定义方案
	 * @param zdyfadto
	 * @return
	 */
    List<ZdyfaDto> getZdyProject(ZdyfaDto zdyfadto);
	
	/**
	 * 添加默认方案
	 * @param zdyfadto
	 * @return
	 */
    boolean insertMrfa(ZdyfaDto zdyfadto);
	
	/**
	 * 设置应用方案
	 * @param list
	 * @return
	 */
    boolean setUseProject(List<ZdyfaDto> list);
	
	/**
	 * 新增自定义方案并保存图片至正式表
	 * @param fjcfbDto
	 * @param zdyfadto
	 * @return
	 */
    boolean addProject(FjcfbDto fjcfbDto, ZdyfaDto zdyfadto);
	
	/**
	 * 根据faid和fasfyy查询已应用的方案信息
	 * @param zdyfadto
	 * @return
	 */
    ZdyfaDto getYyyFa(ZdyfaDto zdyfadto);
}

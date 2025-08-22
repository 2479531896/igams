package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.server.wechat.dao.entities.ZdyfaDto;
import com.matridx.server.wechat.dao.entities.ZdyfaModel;

@Mapper
public interface IZdyfaDao extends BaseBasicDao<ZdyfaDto, ZdyfaModel>{

	/**
	 * 根据wxid查询该用户保存的自定义方案
	 * @param zdyfadto
	 * @return
	 */
    List<ZdyfaDto> getZdyProject(ZdyfaDto zdyfadto);
	
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

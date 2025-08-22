package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.TyszDto;
import com.matridx.igams.common.dao.entities.TyszModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ITyszDao extends BaseBasicDao<TyszDto, TyszModel>{

    /**
     * 获取资源菜单信息
     * tyszDto
     * 
     */
     List<TyszDto> getMenuList(TyszDto tyszDto);

    /**
     * 获取资源按钮信息
     * tyszDto
     * 
     */
     List<TyszDto> getButtonList(TyszDto tyszDto);
}

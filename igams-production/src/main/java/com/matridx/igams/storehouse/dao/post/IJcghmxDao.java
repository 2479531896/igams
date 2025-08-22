package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.JcghmxDto;
import com.matridx.igams.storehouse.dao.entities.JcghmxModel;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IJcghmxDao extends BaseBasicDao<JcghmxDto, JcghmxModel>{

    /**
     * 通过jyxxid获取信息
     * @param
     * @return
     */
    List<JcghmxDto> getListByJyxxid(JcghmxDto jcghmxDto);
    /**
     * 通过ckid分组获取信息
     * @param
     * @return
     */
    List<JcghmxDto> getDtoListGroupByCk(JcghmxDto jcghmxDto);
    /**
     * 批量新增
     * @param jcghmxDtos
     * @return
     */
    boolean insertList(List<JcghmxDto> jcghmxDtos);

}

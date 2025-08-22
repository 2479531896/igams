package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.JcjymxDto;
import com.matridx.igams.storehouse.dao.entities.JcjymxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IJcjymxDao extends BaseBasicDao<JcjymxDto, JcjymxModel>{
    /**
     * 根据仓库分组查询
     */
    List<JcjymxDto> getDtoListGroupByCk(JcjymxDto jcjymxDto);

    /**
     * 根据借出借用信息id分组查询
     */
    List<JcjymxDto> getDtoListGroupByXx(JcjymxDto jcjymxDto);

    List<JcjymxDto> getDtoListGroupBy(JcjymxDto jcjymxDto);

    boolean deleteByIds(List<String> ids);

    List<JcjymxDto> getScphAndSlByJyxxid(String jyxxid);

    int updateList(List<JcjymxDto> list);
}

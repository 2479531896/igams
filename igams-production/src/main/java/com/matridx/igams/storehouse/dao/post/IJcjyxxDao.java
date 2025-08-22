package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.JcjyxxDto;
import com.matridx.igams.storehouse.dao.entities.JcjyxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IJcjyxxDao extends BaseBasicDao<JcjyxxDto, JcjyxxModel>{
    /**
     * 查询列表信息
     */
    List<JcjyxxDto> getDtoListInfo(JcjyxxDto jcjyxxDto);

    boolean deleteByIds(List<String> ids);

    List<JcjyxxDto> getDtoListGroupByJyxx(JcjyxxDto jcjyxxDto);
}

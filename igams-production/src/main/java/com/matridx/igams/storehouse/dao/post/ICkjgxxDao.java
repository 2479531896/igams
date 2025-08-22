package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.CkjgxxDto;
import com.matridx.igams.storehouse.dao.entities.CkjgxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICkjgxxDao extends BaseBasicDao<CkjgxxDto, CkjgxxModel>{

    /**
     * 批量新增
     */
    boolean insertList(List<CkjgxxDto> list);

}

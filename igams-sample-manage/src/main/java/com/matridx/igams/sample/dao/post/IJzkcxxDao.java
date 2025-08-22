package com.matridx.igams.sample.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.sample.dao.entities.JzkcxxDto;
import com.matridx.igams.sample.dao.entities.JzkcxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * {@code @author:JYK}
 */
@Mapper
public interface IJzkcxxDao extends BaseBasicDao<JzkcxxDto, JzkcxxModel> {

    /**
     * 批量更新
     */
    int updateList(List<JzkcxxDto> list);
}

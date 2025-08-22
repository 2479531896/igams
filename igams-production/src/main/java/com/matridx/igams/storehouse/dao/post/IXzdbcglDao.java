package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.XzdbcglDto;
import com.matridx.igams.storehouse.dao.entities.XzdbcglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IXzdbcglDao extends BaseBasicDao<XzdbcglDto, XzdbcglModel> {
    /**
     * 批量删除
     * @param list
     * @return
     */
    Boolean deleteList(List<XzdbcglDto> list);
}

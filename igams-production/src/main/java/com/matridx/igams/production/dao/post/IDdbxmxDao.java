package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;

import com.matridx.igams.production.dao.entities.DdbxmxDto;
import com.matridx.igams.production.dao.entities.DdbxmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IDdbxmxDao extends BaseBasicDao<DdbxmxDto, DdbxmxModel> {
    /**
     * 批量更新
     */
    int updateList(List<DdbxmxDto> list);
    /**
     * 批量新增
     */
    int insertList(List<DdbxmxDto> list);
}

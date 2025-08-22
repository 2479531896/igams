package com.matridx.igams.sample.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.sample.dao.entities.JzllmxDto;
import com.matridx.igams.sample.dao.entities.JzllmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * {@code @author:JYK}
 */
@Mapper
public interface IJzllmxDao extends BaseBasicDao<JzllmxDto, JzllmxModel> {
    /**
     * 根据llid获取数据
     */
    List<JzllmxDto> getDtoListByllid(String id);
    /**
     * 批量新增
     */
    boolean insertList(List<JzllmxDto> jzllmxDtos);
    /**
     * 批量更新
     */
    boolean updateList(List<JzllmxDto> list);
}

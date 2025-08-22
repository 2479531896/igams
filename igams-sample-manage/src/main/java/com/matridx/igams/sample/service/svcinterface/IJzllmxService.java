package com.matridx.igams.sample.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.sample.dao.entities.JzllmxDto;
import com.matridx.igams.sample.dao.entities.JzllmxModel;

import java.util.List;

/**
 * {@code @author:JYK}
 */
public interface IJzllmxService extends BaseBasicService<JzllmxDto, JzllmxModel> {
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

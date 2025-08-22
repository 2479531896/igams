package com.matridx.igams.sample.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.sample.dao.entities.JzkcxxDto;
import com.matridx.igams.sample.dao.entities.JzkcxxModel;

import java.util.List;

/**
 * {@code @author:JYK}
 */
public interface IJzkcxxService extends BaseBasicService<JzkcxxDto, JzkcxxModel> {
    /**
     * 批量更新
     */
    int updateList(List<JzkcxxDto> list);

}

package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.XzdbglDto;
import com.matridx.igams.storehouse.dao.entities.XzdbglModel;

import java.util.List;

/**
 * @author:JYK
 */
public interface IXzdbglService extends BaseBasicService<XzdbglDto, XzdbglModel> {
    /**
     * 批量新增
     * @return
     */
    boolean insertList(List<XzdbglDto> list);

}

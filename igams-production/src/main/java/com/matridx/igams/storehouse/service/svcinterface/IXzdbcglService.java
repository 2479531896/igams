package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.XzdbcglDto;
import com.matridx.igams.storehouse.dao.entities.XzdbcglModel;

import java.util.List;

/**
 * @author:JYK
 */
public interface IXzdbcglService extends BaseBasicService<XzdbcglDto, XzdbcglModel> {
//    deleteList

    /**
     * 批量删除
     * @param list
     * @return
     */
    Boolean deleteList(List<XzdbcglDto> list);
}

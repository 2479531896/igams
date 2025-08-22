package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.XzdbglDto;
import com.matridx.igams.storehouse.dao.entities.XzdbglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IXzdbglDao extends BaseBasicDao<XzdbglDto, XzdbglModel> {
    /**
     * 根据货物名称货物货物标准查询
     * @param xzdbglDto
     * @return
     */
    XzdbglDto getDtoByHwmcAndHwbz(XzdbglDto xzdbglDto);
    /**
     * 批量新增
     * @return
     */
    boolean insertList(List<XzdbglDto> list);
}

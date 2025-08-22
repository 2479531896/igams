package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.XzdbcglDto;
import com.matridx.igams.storehouse.dao.entities.XzdbglDto;
import com.matridx.igams.storehouse.dao.entities.XzkcxxDto;
import com.matridx.igams.storehouse.dao.entities.XzkcxxModel;

import java.util.List;

public interface IXzkcxxService extends BaseBasicService<XzkcxxDto, XzkcxxModel>{
    /**
     * 批量新增
     */
    boolean insertList(List<XzkcxxDto> list);
    /**
     * 批量修改
     */
    boolean updateList(List<XzkcxxDto> list);
    /**
     * 批量查询
     */
    List<XzkcxxDto> getDtoByXzkcids(XzdbcglDto xzdbcglDto);
    /**
     * 根据货物名称货物货物标准库位查询
     * @return
     */
    XzkcxxDto getDtoByHwmcAndHwbz(XzkcxxDto xzkcxxDto);
    /**
     * 批量更新
     */
    boolean updateListByIds(List<XzkcxxDto> list);
    /**
     * 批量更新
     */
    boolean updateListKcl(List<XzkcxxDto> list);

    /**
     * 获取行政库存id
     * @return
     */
    List<XzkcxxDto> getXzkcid(XzkcxxDto xzkcxxDto);
    /**
     * 操作调拨车
     */
    boolean czDbc(XzdbglDto xzdbglDto, User user)throws BusinessException;
    /**
     * 调拨操作
     */
    boolean Dbcz(XzdbglDto xzdbglDto,XzkcxxDto xzkcxxDto, User user)throws BusinessException;
    /**
     * 库存维护
     */
    boolean updateAqkc(XzkcxxDto xzkcxxDto);
    /**
     * 批量查询库存信息getXzkcxxs
     */
    List<XzkcxxDto> getXzkcxxs(List<XzkcxxDto> list);

    void updateXzllDtos(List<XzkcxxDto> list);
    /**
     * 修改库存量
     */
    void updateListKclById(List<XzkcxxDto> list);
}

package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.XzrkmxDto;
import com.matridx.igams.storehouse.dao.entities.XzrkmxModel;

import java.util.List;

public interface IXzrkmxService extends BaseBasicService<XzrkmxDto, XzrkmxModel>{
    /**
     * 行政库存列表
     * @param xzrkmxDto
     * @return
     */
    List<XzrkmxDto> getPagedDtoAdministrationStockList(XzrkmxDto xzrkmxDto);
    /**
     * 行政库存 基本信息
     * @param xzrkmxDto
     * @return
     */
    XzrkmxDto getJbxxByXzrkmxid(XzrkmxDto xzrkmxDto);
    /**
     * 修改 行政库存明细
     * @param xzrkmxDto
     * @return
     */
    boolean updateAdministrationStockByXzrkmxid(XzrkmxDto xzrkmxDto);

    /**
     * 根据xzrkmxid获取qgmxids
     * @param xzrkmxDto
     * @return
     */
    List<XzrkmxDto> getQgmxidsByXzrkmxids(XzrkmxDto xzrkmxDto);

    /**
     * 删除 行政库存明细
     * @param xzrkmxDto
     * @return
     */
    boolean delAdministrationStockByXzrkmxid(XzrkmxDto xzrkmxDto);

    /**
     * 批量保存入库明细信息
     */
    boolean insertList(List<XzrkmxDto> xzrkmxDtos);

    /**
     * 获取已经入库的数量
     */
    XzrkmxDto getrksl(String qgmxid);
    /**
     * 获取行政库存数据
     * @param xzrkid
     * @return
     */
    List<XzrkmxDto> getXzkcList(String xzrkid);

    List<XzrkmxDto> getDtoListByXzrkid(String xzrkid);
    /**
     * 批量更新
     * @param list
     * @return
     */
    boolean updateList(List<XzrkmxDto> list);
    List<XzrkmxDto> getDtoListByDto(XzrkmxDto xzrkmxDto);
}

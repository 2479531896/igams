package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.XzllmxDto;
import com.matridx.igams.storehouse.dao.entities.XzllmxModel;

import java.util.List;

public interface IXzllmxService extends BaseBasicService<XzllmxDto, XzllmxModel>{

    /**
     * 获取行政领料明细信息
     * @param xzllid
     * @return
     */
    List<XzllmxDto> getDtoXzllmxListByXzllid(String xzllid);

    /**
     * 批量保存入库明细信息
     */
    boolean insertList(List<XzllmxDto> xzllmxDtos);
    /**
     * 批量更新入库明细信息
     */
    boolean updateList(List<XzllmxDto> xzllmxDtos);
    /**
     * 删除入库明细信息
     */
    boolean delXzllmxByXzllid(XzllmxDto xzllmxDto);
    /**
     * 根据行政库存id获取领料信息
     * @param xzkcid
     * @return
     */
    List<XzllmxDto> getDtoXzllmxListByXzkcid(String xzkcid);
}

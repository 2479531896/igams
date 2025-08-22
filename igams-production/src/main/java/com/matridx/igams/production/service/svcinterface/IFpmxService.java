package com.matridx.igams.production.service.svcinterface;


import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.FpmxDto;
import com.matridx.igams.production.dao.entities.FpmxModel;

import java.util.List;


public interface IFpmxService extends BaseBasicService<FpmxDto, FpmxModel> {

    /**
     * 多条添加发票明细
     */
    boolean insertList(List<FpmxDto> list);
    /**
     * 根据发票ID删除记录
     */
    void deleteByFpid(FpmxDto fpmxDto);
    /**
     * 根据合同明细ID查找总数量
     */
    String getSumById(String htmxid);
    /**
     * 查找入库信息
     */
    List<FpmxDto> getListForRk(FpmxDto fpmxDto);
    /**
     * 查找入库信息
     */
    List<FpmxDto> getRkListByFp(FpmxDto fpmxDto);
    /**
     * 通过htfkid查找信息
     */
    List<FpmxDto> getListByHtfkid(FpmxDto fpmxDto);
    /**
     * 通过htmxid查找信息
     */
    List<FpmxDto> getListByHtmxid(FpmxDto fpmxDto);
    /**
     * 查找货物信息
     */
    List<FpmxDto> getListForHw(FpmxDto fpmxDto);
    /**
     * 通过htid分组查询总金额
     */
    List<FpmxDto> getSumGroupByHtid(FpmxDto fpmxDto);
    /**
     * 获取明细
     */
    List<FpmxDto> getInvoiceList(FpmxDto fpmxDto);
}

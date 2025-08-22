package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.HtfktxDto;
import com.matridx.igams.production.dao.entities.HtfktxModel;

import java.util.List;

public interface IHtfktxService extends BaseBasicService<HtfktxDto, HtfktxModel>{

    /**
     * 获取付款提醒信息
     */
    List<HtfktxDto> getListByHtid(HtfktxDto htfktxDto);

    /**
     * 保存合同付款提醒信息
     */
    boolean saveContractPayRemind(HtfktxDto htfktxDto,List<HtfktxDto> htfkDtos);

    /**
     * 删除合同付款提醒数据
     */
    boolean delPendingPayment(HtfktxDto htfktxDto);

    /**
     * 更新付款提醒信息
     */
    boolean updateHtfkid(HtfktxDto htfktxDto);

    /**
     * 新增合同付款提醒信息
     */
    void insertDtoList(List<HtfktxDto> htfktxDtos);
    /**
     * 更新htfkid为null
     */
    boolean updateHtfkidToNull(HtfktxDto htfktxDto);
    /**
     * 根据htid删除
     */
    void deleteByHtid(HtfktxDto htfktxDto);
}

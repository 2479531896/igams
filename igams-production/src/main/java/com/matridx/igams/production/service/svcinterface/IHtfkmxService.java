package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.HtfkmxDto;
import com.matridx.igams.production.dao.entities.HtfkmxModel;

import java.util.List;
import java.util.Map;

public interface IHtfkmxService extends BaseBasicService<HtfkmxDto, HtfkmxModel>{

    /**
     * 获取相关合同付款情况
     */
    List<HtfkmxDto> getHtfkqkMessage(List<HtfkmxDto> list);

    /**
     * 新增合同付款明细
     */
    boolean insertDtoList(List<HtfkmxDto> list);
    /**
     * 批量更新明细
     */
    int updateList(List<HtfkmxDto> htfkmxDtos);

    /**
     * 根据合同ID获取合同付款信息
     */
    List<HtfkmxDto> getListByHtid(String htid);

    /**
     * 更新合同付款中金额信息
     */
    boolean updateContractFkzje(List<HtfkmxDto> htfkmxDtos);
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(HtfkmxDto htfkmxDto, Map<String, Object> params);

    /**
     * 获取合同钉钉实例id
     */
    List<HtfkmxDto> queryHtddslid(HtfkmxDto htfkmxDto);

    /**
     * 分组查询付款金额
     */
    List<HtfkmxDto> getListGroupByHt(HtfkmxDto htfkmxDto);
}

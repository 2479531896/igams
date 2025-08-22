package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.BfplszDto;
import com.matridx.igams.wechat.dao.entities.KhzyfpDto;
import com.matridx.igams.wechat.dao.entities.KhzyfpModel;

import java.util.List;

public interface IKhzyfpService extends BaseBasicService<KhzyfpDto, KhzyfpModel> {

    /**
     * 客户列表
     */
    List<KhzyfpDto> getPagedCustomerList(KhzyfpDto khzyfpDto);

    /**
     * 拜访记录列表(分页)
     */
    List<KhzyfpDto> getPagedVisitRecordsList(KhzyfpDto khzyfpDto);
    /**
     * 合并
     */
    boolean mergeList(List<KhzyfpDto> list);
    /**
     * 批量新增
     */
    boolean insertList(List<KhzyfpDto> list);
    /**
     * 资源分配保存
     */
    boolean resourceDispenseSaveVisitingObject(KhzyfpDto khzyfpDto);
    /*
        通过用户名删除资源分配信息
     */
    boolean deleteByYhms(List<String> yhms);
}

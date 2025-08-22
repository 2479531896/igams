package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.SbpdDto;
import com.matridx.igams.production.dao.entities.SbpdModel;

import java.util.List;
import java.util.Map;

public interface ISbpdService extends BaseBasicService<SbpdDto, SbpdModel>{

    /**
     * 获取部门
     */
    List<Map<String,String>> getDepartments();

    /**
     * 审核列表
     */
    List<SbpdDto> getPagedAuditEquipmentInventory(SbpdDto sbpdDto);
    /**
     * 删除
     */
    boolean delEquipmentInventory(SbpdDto sbpdDto);
    /**
     * 新增
     */
    boolean addEquipmentInventory(SbpdDto sbpdDto);
    /**
     * 修改
     */
    boolean modEquipmentInventory(SbpdDto sbpdDto);

}

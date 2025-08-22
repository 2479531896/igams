package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.SbpdDto;
import com.matridx.igams.production.dao.entities.SbpdModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISbpdDao extends BaseBasicDao<SbpdDto, SbpdModel>{

    /**
     * 获取部门
     */
    List<Map<String,String>> getDepartments();
    /**
     * 获取审核列表
     */
    List<SbpdDto> getPagedAuditEquipmentInventory(SbpdDto sbpdDto);
    /**
     * 审核列表
     */
    List<SbpdDto> getAuditListEquipmentInventory(List<SbpdDto> list);
    /**
     * 批量新增
     */
    boolean insertList(List<SbpdDto> list);

}

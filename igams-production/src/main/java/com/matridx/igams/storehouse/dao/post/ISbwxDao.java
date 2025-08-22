package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.SbwxDto;
import com.matridx.igams.storehouse.dao.entities.SbwxModel;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ISbwxDao extends BaseBasicDao<SbwxDto, SbwxModel> {
    /**
     * @param wxbyDto
     * @description 钉钉审批撤回修改ddslid为null
     */
    void updateDdslidToNull(SbwxDto wxbyDto);
    /**
     * @description 通过钉钉实例i获取维修保养数据
     * @param ddslid
     * @return 
     */
    SbwxDto getDtoByDdslid(String ddslid);
    /**
     * @description 获取审批人角色信息
     * @param yhid
     * @return
     */
    List<SbwxDto> getSprjsBySprid(String yhid);
    /**
     * @description 获取审核数据
     * @param sbwxDto
     * @return
     */
    List<SbwxDto> getPagedAuditUpkeepDevice(SbwxDto sbwxDto);
    /**
     * @description 获取审核数据List
     * @param t_sbyzList
     * @return
     */
    List<SbwxDto> getAuditListUpkeepDevice(List<SbwxDto> t_sbyzList);
    /**
     * @description 获取所有使用部门
     */
    List<SbwxDto> getDepartmentList();
    /**
     * @description 获取所有管理人员
     */
    List<SbwxDto> getGlryList();
    /**
     * @description 获取流水号
     * @param prefix
     * @return
     */
    String getSbwxSerial(String prefix);
}

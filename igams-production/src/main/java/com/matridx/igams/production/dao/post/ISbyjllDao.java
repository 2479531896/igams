package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.SbyjllDto;
import com.matridx.igams.production.dao.entities.SbyjllModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface ISbyjllDao extends BaseBasicDao<SbyjllDto, SbyjllModel> {
    /**
     * 设置钉钉实例ID为null
     */
    void updateDdslidToNull(SbyjllDto sbyjllDto);
    /**
     * 根据钉钉实例ID获取在钉钉进行领料申请的审批信息
     */
    SbyjllDto getDtoByDdslid(String ddspid);
    /**
     * 根据审批人用户ID获取角色信息
     */
    List<SbyjllDto> getSprjsBySprid(String sprid);
    /**
     * 设备移交履历的原使用部门和现使用部门
     */
    List<SbyjllDto> selectOldOrNewDepartments(SbyjllDto sbyjllDto);
    /**
     * 设备移交审核列表
     */
    List<SbyjllDto> getPagedAuditDeviceTurnOver(SbyjllDto sbyjllDto);
    List<SbyjllDto> getAuditDeviceTurnOver(List<SbyjllDto> sbyjllDtos);
    /**
     * 批量新增设备移交记录
     */
    void insertSbyjllDtos(List<SbyjllDto> sbyjllDtos);
    /**
     * 设备查看履历
     */
    List<SbyjllDto> getSyjllDtos(SbyjllDto sbyjllDto);
}

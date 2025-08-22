package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.SbbfDto;
import com.matridx.igams.production.dao.entities.SbbfModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface ISbbfDao extends BaseBasicDao<SbbfDto, SbbfModel> {
    void updateDdslidToNull(SbbfDto sbbfDto);
    /**
     * 根据钉钉实例ID获取在钉钉进行领料申请的审批信息
     */
    SbbfDto getDtoByDdslid(String ddspid);
    /**
     * 根据审批人用户ID获取角色信息
     */
    List<SbbfDto> getSprjsBySprid(String sprid);
    /**
     * 获取部门
     */
    List<SbbfDto> getDepartmentList();
    /**
     * 获取管理人员
     */
    List<SbbfDto> getLeadList();
    /**
     * 设备报废审核列表
     */
    List<SbbfDto> getPagedAuditDeviceScrap(SbbfDto sbbfDto);
    /**
     * 设备报废审核列表
     */
    List<SbbfDto> getAuditDeviceScrap(List<SbbfDto> list);

}

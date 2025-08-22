package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.CpxqjhDto;
import com.matridx.igams.production.dao.entities.CpxqjhModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICpxqjhDao extends BaseBasicDao<CpxqjhDto, CpxqjhModel> {

    /**
     * 获取需求单号
     */
    String getXqdhSerial(String str);

    /**
     * 设置钉钉实例ID为null
     */
    void updateDdslidToNull(CpxqjhDto cpxqjhDto);

    /**
     * 获取审核列表
     */
    List<CpxqjhDto> getPagedAuditDevice(CpxqjhDto cpxqjhDto);


    /**
     * 审核列表
     */
    List<CpxqjhDto> getAuditListDevice(List<CpxqjhDto> list);

    /**
     * 根据钉钉实例ID获取在钉钉进行领料申请的审批信息
     */
    CpxqjhDto getDtoByDdslid(String ddspid);

    /**
     * 获取钉钉审批人信息
     */
    CpxqjhDto getSprxxByDdid(String ddid);

    /**
     * 根据审批人用户ID获取角色信息
     */
    List<CpxqjhDto> getSprjsBySprid(String sprid);

    /**
     * 删除
     */
    Integer deleteByIds(CpxqjhDto cpxqjhDto);
    /**
     * 获取货物信息
     */
    List<CpxqjhDto> getHwxxByWlid(CpxqjhDto cpxqjhDto);
    /**
     * 需求计划列表
     */
    List<CpxqjhDto> getPagedXqjhDtoList(CpxqjhDto cpxqjhDto);
    /**
     * @description 获取需求数量
     */
    CpxqjhDto getXqslByCpxqid(String sczlmxid);
    /**
     * @description 获取入库数量
     */
    CpxqjhDto getRkslByCpxqid(String cpxqid);
    /**
     * @description 修改入库状态
     */
    boolean updateRkzt(CpxqjhDto cpxqjhDto);
}

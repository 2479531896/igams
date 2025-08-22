package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.FksqDto;
import com.matridx.igams.wechat.dao.entities.FksqModel;
import com.matridx.igams.wechat.dao.entities.SwyszkDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IFksqDao extends BaseBasicDao<FksqDto, FksqModel>{

    /**
     * 设置钉钉实例ID为null
     * @param
     * @return
     */
    boolean updateDdslidToNull(FksqDto fksqDto);

    /**
     *根据ddslid获取信息
     * @param ddslid
     * @return
     */
    FksqDto getDtoByDdslid(String ddslid);
    /**
     * 根据审批人用户ID获取审批人角色信息
     * @param sprid
     * @return
     */
    List<FksqDto> getSprjsBySprid(String sprid);

    /**
     * 获取审核列表
     * @param fksqDto
     * @return
     */
    List<FksqDto> getPagedAuditPaymentApplication(FksqDto fksqDto);
    /**
     * 审核列表
     * @param list
     * @return
     */
    List<FksqDto> getAuditListPaymentApplication(List<FksqDto> list);
    /**
     * 获取应收账款更新数据
     */
    List<SwyszkDto> getUpdateData(List<String> ids);

}

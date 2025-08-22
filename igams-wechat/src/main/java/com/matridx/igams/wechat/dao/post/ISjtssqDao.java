package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.wechat.dao.entities.SjtssqDto;
import com.matridx.igams.wechat.dao.entities.SjtssqModel;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ISjtssqDao extends BaseBasicDao<SjtssqDto, SjtssqModel> {

    /**
     * 获取审核列表
     * @param sjtssqDto
     * @return
     */
    List<SjtssqDto> getPagedAuditApplication(SjtssqDto sjtssqDto);
    /**
     * 审核列表
     * @param list
     * @return
     */
    List<SjtssqDto> getAuditListApplication(List<SjtssqDto> list);
    /**
     * 更改处理状态
     * @param sjtssqDto
     * @return
     */
    boolean updateClbj(SjtssqDto sjtssqDto);
    /**
     * 验证项目重复
     * @param sjtssqDto
     * @return
     */
    SjtssqDto verification(SjtssqDto sjtssqDto);

    /**
     * 更新钉钉实例ID
     * @param sjtssqDto
     * @return
     */
    boolean updateDdslid(SjtssqDto sjtssqDto);

    /**
     * 根据钉钉实例ID获取特殊申请信息
     * @param sjtssqDto
     * @return
     */
    SjtssqDto getDtoByDdslid(SjtssqDto sjtssqDto);

    /**
     * 根据钉钉ID获取审批人信息
     * @param user
     * @return
     */
    SjtssqDto getSprxxByDdid(User user);

    /**
     * 根据审批人ID获取审批人角色信息
     * @param sprid
     * @return
     */
    List<SjtssqDto> getSprjsBySprid(String sprid);

    /**
     * 将钉钉实例ID置为空
     * @param sjtssqDto
     * @return
     */
    boolean updateDdslidToNull(SjtssqDto sjtssqDto);
    /**
     * 获取申请项目字符串
     * @param sjtssqDto
     * @return
     */
    String getSqxms(SjtssqDto sjtssqDto);

    /**
     * 根据搜索条件获取导出条数
     * @param sjtssqDto
     * @return
     */
    int getCountForSearchExp(SjtssqDto sjtssqDto);

    /**
     * 从数据库分页获取导出数据
     * @param sjtssqDto
     * @return
     */
    List<SjtssqDto> getListForSearchExp(SjtssqDto sjtssqDto);

    /**
     * 从数据库分页获取导出数据
     * @param sjtssqDto
     * @return
     */
    List<SjtssqDto> getListForSelectExp(SjtssqDto sjtssqDto);
}

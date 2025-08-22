package com.matridx.igams.storehouse.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.XzllglDto;
import com.matridx.igams.storehouse.dao.entities.XzllglModel;

import java.util.List;

@Mapper
public interface IXzllglDao extends BaseBasicDao<XzllglDto, XzllglModel>{
    /**
     * 行政领料审核列表
     * @param xzllglDto
     * @return
     */

    List<XzllglDto> getPagedAuditXzllgl(XzllglDto xzllglDto);
    /**
     * 行政领料审核数据
     */
    List<XzllglDto> getAuditXzllglList(List<XzllglDto> list);
    /**
     * 获取领料列表
     * @param
     * @return
     */
    List<XzllglDto> getPagedDtoListReceiveAdministrationMateriel(XzllglDto xzllglDto);

    /**
     * 根据行政领料id获取行政领料信息
     * @param xzllglDto
     * @return
     */
    XzllglDto getDtoReceiveAdministrationMaterielByXzllid(XzllglDto xzllglDto);

    /**
     * 删除行政领料信息
     *
     * @param xzllglDto
     */
    void delReceiveAdministrationMateriel(XzllglDto xzllglDto);
    /**
     * 修改保存行政领料信息
     * @param xzllglDto
     * @return
     */
    boolean modReceiveAdminMateriel(XzllglDto xzllglDto);

    /**
     * 校验领料单号是否重复
     * @param
     * @return
     */
    XzllglDto getDtoByLldh(XzllglDto xzllglDto);


    /**
     * 根据行政领料ids获取审核通过行政领料详细信息
     * @param
     * @return
     */
    List<XzllglDto> getReceiveAdministrationMaterielListByXzllids(XzllglDto xzllglDto);

    /**
     * 查询已有流水号
     * @param prefix
     * @return
     */
    String getDjhSerial(String prefix);
    /**
     * 将钉钉实例ID置为空
     *
     * @param xzllglDto
     */
    void updateDdslidToNull(XzllglDto xzllglDto);
    /**
     * 通过钉钉审批实例ID查找信息
     * @param
     * @return
     */
    XzllglDto getDtoByDdslid(String ddslid);
    /**
     * 获取审批人信息
     * @param
     * @return
     */
    XzllglDto getSprxxByDdid(String ddid);
    /**
     * 获取审批人角色
     * @param
     * @return
     */
    List<XzllglDto> getSprjsBySprid(String sprid);
    /**
     * 根据搜索条件获取导出条数
     * @return
     */
    int getCountForSearchExp(XzllglDto xzllglDto);

    /**
     * 从数据库分页获取导出数据
     * @return
     */
    List<XzllglDto> getListForSearchExp(XzllglDto xzllglDto);

    /**
     * 从数据库分页获取导出数据
     * @return
     */
    List<XzllglDto> getListForSelectExp(XzllglDto xzllglDto);
}

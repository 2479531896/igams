package com.matridx.igams.storehouse.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;

import com.matridx.igams.storehouse.dao.entities.XzllglDto;
import com.matridx.igams.storehouse.dao.entities.XzllglModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IXzllglService extends BaseBasicService<XzllglDto, XzllglModel>{

    List<XzllglDto> getPagedAuditXzllgl(XzllglDto xzllglDto);

    /**
     * 获取领料列表
     * @param
     * @return
     */
    List<XzllglDto> getPagedDtoListReceiveAdministrationMateriel(XzllglDto xzllglDto);

    /**
     * 自动生成领料单号
     * @param
     * @return
     */
    String generateDjh();

    /**
     * 校验领料单号是否重复
     * @param
     * @return
     */
    XzllglDto getDtoByLldh(XzllglDto xzllglDto);

    /**
     * 保存领料信息
     * @param
     * @return
     * @throws BusinessException
     */
    boolean addSaveXzPicking(XzllglDto xzllglDto) throws BusinessException;


    /**
     * 根据行政领料id获取行政领料信息
     * @param xzllglDto
     * @return
     */
    XzllglDto getDtoReceiveAdministrationMaterielByXzllid(XzllglDto xzllglDto);

    /**
     * 修改保存行政领料信息
     * @param xzllglDto
     * @return
     */
    boolean modReceiveAdminMateriel(XzllglDto xzllglDto) throws BusinessException;
    /**
     * 出库保存行政领料信息
     * @param xzllglDto
     * @return
     */
    boolean releaseSaveReceiveAdminMateriel(XzllglDto xzllglDto) throws BusinessException;
    /**
     * 删除行政领料信息
     * @param xzllglDto
     * @return
     */
    boolean delReceiveAdministrationMateriel(XzllglDto xzllglDto);

    /**
     * 根据行政领料ids获取审核通过行政领料详细信息
     * @param
     * @return
     */
    List<XzllglDto> getReceiveAdministrationMaterielListByXzllids(XzllglDto xzllglDto);

    /**
     * 新增保存行政领料信息
     * @param xzllglDto
     * @return
     */
    boolean addReceiveAdminMateriel(XzllglDto xzllglDto) throws BusinessException;
    /**
     * 钉钉审批回调
     * @return
     */
    boolean callbackXzllglAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;
}

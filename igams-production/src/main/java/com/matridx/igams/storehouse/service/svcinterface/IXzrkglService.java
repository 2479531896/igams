package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.XzrkglDto;
import com.matridx.igams.storehouse.dao.entities.XzrkglModel;

import java.util.List;


public interface IXzrkglService extends BaseBasicService<XzrkglDto, XzrkglModel>{
    /**
     * 行政入库审核列表
     * @param xzrkglDto
     * @return
     */
    List<XzrkglDto> getPagedAuditPutInAdmin(XzrkglDto xzrkglDto);


    /**
     * 自动生成领料单号
     * @param
     * @return
     */
    String generateDjh(XzrkglDto xzrkglDto);

    /**
     * 更具入库单号查询
     * @param
     * @return
     */
    XzrkglDto getDtoByRkdh(XzrkglDto xzrkglDto);

    /**
     * 保存入库信息
     * @param
     * @return
     * @throws BusinessException
     */
    boolean addSaveInStore(XzrkglDto xzrkglDto) throws BusinessException;

    XzrkglDto getDtoByXzrkid(String xzrkid);

    /**
     * 其他入库信息
     * @param
     * @return
     * @throws BusinessException
     */
    boolean addSaveInStoreOther(XzrkglDto xzrkglDto) throws BusinessException;
    /**
     * 删除入库信息
     * @param
     * @return
     * @throws BusinessException
     */
    boolean delAdministrationStorage(XzrkglDto xzrkglDto) throws BusinessException;
    /**
     * 修改请购入库保存入库信息
     * @param
     * @return
     * @throws BusinessException
     */
    boolean modSaveAdministrationStorage(XzrkglDto xzrkglDto) throws BusinessException;
    /**
     * 修改其他保存入库信息
     * @param
     * @return
     * @throws BusinessException
     */
    boolean modSaveAdministrationOtherStorage(XzrkglDto xzrkglDto) throws BusinessException;
}

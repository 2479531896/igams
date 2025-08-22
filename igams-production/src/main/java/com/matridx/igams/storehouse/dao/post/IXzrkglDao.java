package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.exception.BusinessException;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.XzrkglDto;
import com.matridx.igams.storehouse.dao.entities.XzrkglModel;

import java.util.List;

@Mapper
public interface IXzrkglDao extends BaseBasicDao<XzrkglDto, XzrkglModel>{
    /**
     * 获取审核列表
     * @param xzrkglDto
     * @return
     */
    List<XzrkglDto> getPagedAuditPutInAdmin(XzrkglDto xzrkglDto);

    /**
     * 查询已有流水号
     * @param prefix
     * @return
     */
    String getDjhSerial(String prefix);

    /**
     * 更具入库单号查询
     * @param
     * @return
     */
    XzrkglDto getDtoByRkdh(XzrkglDto xzrkglDto);
 /**
     * 审核列表
     * @param list
     * @return
     */
    List<XzrkglDto> getAuditListPutInAdmin(List<XzrkglDto> list);

    XzrkglDto getDtoByXzrkid(String xzrkid);

    List<XzrkglDto> getDtoByXzrkids(XzrkglDto xzrkglDto);
    /**
     * 修改
     * @param xzrkglDto
     * @return
     */
    boolean updatePageEvent(XzrkglDto xzrkglDto);
    /**
     * 修改其他保存入库信息
     * @param
     * @return
     */
    boolean modSaveAdministrationOtherStorage(XzrkglDto xzrkglDto);
}
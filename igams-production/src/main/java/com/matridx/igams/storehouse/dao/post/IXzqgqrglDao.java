package com.matridx.igams.storehouse.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.XzqgqrglDto;
import com.matridx.igams.storehouse.dao.entities.XzqgqrglModel;

import java.util.List;

@Mapper
public interface IXzqgqrglDao extends BaseBasicDao<XzqgqrglDto, XzqgqrglModel>{

    /**
     * 获取最大单号
     * @param prefix
     * @return
     */
    String getDjhSerial(String prefix);
	 /**
     * 修改行政请购确认单
     * @param xzqgqrglDto
     * @return
     */
     boolean modSavePurchaseConfirm(XzqgqrglDto xzqgqrglDto);

      /**
     * 获取行政请购确认审核的ID列表
     * @param xzqgqrglDto
     * @return
     */
      List<XzqgqrglDto> getPagedAuditIdList(XzqgqrglDto xzqgqrglDto);

    /**
     * 根据行政请购确认审核的ID列表获取审核列表详细信息
     * @param xzqgqrglDtos
     * @return
     */
    List<XzqgqrglDto> getAuditListByIds(List<XzqgqrglDto> xzqgqrglDtos);

    /**
     * 更新行政请购确认管理钉钉实例ID
     * @param xzqgqrglDto
     */
    void updateDdslid(XzqgqrglDto xzqgqrglDto);

    /**
     * 根据钉钉实例ID获取确认信息
     * @param xzqgqrglDto
     * @return
     */
    XzqgqrglDto getDtoByDdslid(XzqgqrglDto xzqgqrglDto);

    /**
     * 根据yhid获取审批人角色信息
     * @param yhid
     * @return
     */
    List<XzqgqrglDto> getSprjsBySprid(String yhid);

    /**
     * 根据qrids获取行政请购确认信息
     * @param xzqgqrglDto
     * @return
     */
    List<XzqgqrglDto> getDtoListByIds(XzqgqrglDto xzqgqrglDto);

    /**
     * 更新确认单付款完成标记
     * @param xzqgqrglDto
     * @return
     */
    boolean updateFkwcbj(XzqgqrglDto xzqgqrglDto);
}

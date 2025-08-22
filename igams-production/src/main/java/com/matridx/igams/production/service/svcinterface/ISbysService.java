package com.matridx.igams.production.service.svcinterface;



import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.SbysDto;
import com.matridx.igams.production.dao.entities.SbysModel;

import java.util.List;


public interface ISbysService extends BaseBasicService<SbysDto, SbysModel>{

    /**
     * 废弃按钮
     */
    boolean discard(SbysDto sbysDto);

    /**
     * 审核列表
     */
    List<SbysDto> getPagedAuditDevice(SbysDto sbysDto);
    /**
     * 生成设备验收单号
     */
    String getSbysdh(String str);

    List<String> getDtoByGdzcbh(SbysDto sbysDto);
    /**
     * 新设备验收列表
     */
    List<SbysDto> getPagedEquipmentList(SbysDto sbysDto);
    /**
     * 获取列表中的部门
     */
    List<SbysDto> getDepartmentList();
    /**
     * 新增修改获取信息
     */
    SbysDto getSbysDtoById(String id);
    /**
     * 修改方法
     */
    boolean updateAndSave(SbysDto sbysDto) throws BusinessException;
    List<SbysDto> getDtoEquipmentList(SbysDto sbysDto);
    boolean updateList(List<SbysDto> list);
    /**
     * 报废或售出
     */
    boolean scrapOrSoldSaveEquipmentAcceptance(SbysDto sbysDto) throws BusinessException;
    /**
     * 闲置
     */
    boolean inactiveSaveEquipmentAcceptance(SbysDto sbysDto) throws BusinessException;
	/**
     * @description 设备验收修改页面数据
     * @return boolean
     */
    boolean updatePageEvent(SbysDto sbysDto);
    /**
     * 计量列表获取数据
     */
    List<SbysDto> getPagedMeteringList(SbysDto sbysDto);
    /**
     * @description 修改台账信息
     */
    void updateTzs(List<SbysDto> sbysDtos);
    /**
     * @description 修改台账信息
     */
    void updateTzsWithNull(List<SbysDto> sbysDtos);
    /**
     * @description 修改设备状态
     */
    void updateSbzts(SbysDto sbysDto);
    List<SbysDto> getSbysDtoList(SbysDto sbysDto);
	 /**
     * @description 获取计量信息
      */
    List<SbysDto> getPagedDeviceInfoForJl(SbysDto sbysDto);
    /**
     * @description 获取验证信息
     */
    List<SbysDto> getPagedDeviceInfoYZ(SbysDto sbysDto);
	/**
     * @description 通过wlid修改设备验收
     */
    void updateForWlid(SbysDto sbysDto);
    /**
     * @description 批量修改状态
     */
    void updateListZt(List<SbysDto> sbysDtos);
}

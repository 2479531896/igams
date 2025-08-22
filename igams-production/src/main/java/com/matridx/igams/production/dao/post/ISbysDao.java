package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.SbysDto;
import com.matridx.igams.production.dao.entities.SbysModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISbysDao extends BaseBasicDao<SbysDto, SbysModel>{

    /**
     * 废弃按钮
     */
    boolean discard(SbysDto sbysDto);

    /**
     * 获取审核列表
     */
    List<SbysDto> getPagedAuditDevice(SbysDto sbysDto);
    /**
     * 审核列表
     */
    List<SbysDto> getAuditListDevice(List<SbysDto> list);
    /**
     * 生成设备验收单号
     */
    String getSbysdh(String str);

    List<String> getDtoByGdzcbh(SbysDto sbysDto);
    /**
     * 新设备验收列表
     */
    List<SbysDto> getPagedEquipmentList(SbysDto sbysDto);
    List<SbysDto> getPagedMinidataEquipmentList(SbysDto sbysDto);
    /**
     * 获取列表中的部门
     */
    List<SbysDto> getDepartmentList();
    /**
     * 新增修改获取信息
     */
    SbysDto getSbysDtoById(String id);
    List<SbysDto> getDtoEquipmentList(SbysDto sbysDto);
    boolean updateList(List<SbysDto> list);
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(SbysDto sbysDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<SbysDto> getListForSearchExp(SbysDto sbysDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<SbysDto> getListForSelectExp(SbysDto sbysDto);
    boolean scrapOrSold(SbysDto sbysDto);
    /**
     * 计量列表获取数据
     */
    List<SbysDto> getPagedMeteringList(SbysDto sbysDto);
	 /**
     * @description 设备验收修改页面数据
     */
    boolean updatePageEvent(SbysDto sbysDto);
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
    /**
     * @description 获取记录编号流水号
     */
    String getSbysSerial(String prefix);
	/**
     * @description 获取计量信息
     */
    List<SbysDto> getPagedDeviceInfoForJl(SbysDto sbysDto);
    /**
     * @description 获取验证信息
     */
    List<SbysDto> getPagedDeviceInfoYZ(SbysDto sbysDto);
    List<SbysDto> getSbysDtoList(SbysDto sbysDto);
	 /**
     * @description 通过wlid修改设备验收
     */
    void updateForWlid(SbysDto sbysDto);
	/**
     * 查询部门管理员
     */
    List<SbysDto> selectAdministrators(SbysDto sbysDto);
    /**
     * 查询部门负责人
     */
    List<SbysDto> selectLeaders(SbysDto sbysDto);
    /**
     * @description 批量修改状态
     */
    void updateListZt(List<SbysDto> sbysDtos);
    /**
     * 盘点状态置为空
     */
    boolean updatePdztToNull(SbysDto sbysDto);
    /**
     * 查询盘点数据
     */
    List<SbysDto> getListForInventory(SbysDto sbysDto);
    /**
     * 更新盘点状态
     */
    boolean updateForInventory(List<SbysDto> sbysDtos);
}

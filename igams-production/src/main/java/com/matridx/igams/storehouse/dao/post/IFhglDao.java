package com.matridx.igams.storehouse.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.FhglDto;
import com.matridx.igams.storehouse.dao.entities.FhglModel;

import java.util.List;

@Mapper
public interface IFhglDao extends BaseBasicDao<FhglDto, FhglModel>{
    /**
     * 发货列表
     */
    List<FhglDto> getPagedDtoList(FhglDto fhglDto);
    /**
     * 获取单条数据
     */
    FhglDto getDtoByid(FhglDto fhglDto);
    
    /**
	 * 获取可退货列表 
	 * @param fhglDto
	 * @return
	 */
    List<FhglDto> getPagedDtoKThList(FhglDto fhglDto);
	
	/**
     * 获取发货单号
     * @param
     * @return
     */
    String getFhdhSerial(String str);

    /**
     * 发货列表
     */
    List<FhglDto> getDtoListByFhdh(FhglDto fhglDto);

    /**
     * 获取审核列表
     * @param
     * @return
     */
    List<FhglDto> getPagedAuditDevice(FhglDto fhglDto);

    /**
     * 审核列表
     * @param
     * @return
     */
    List<FhglDto> getAuditListDevice(List<FhglDto> list);

    boolean updateWlxx(FhglDto fhglDto);
    /**
     * @description 获取销售订单的发货状态
     * @param fhglDto
     * @return
     */
    List<FhglDto> getFhztByXsids(FhglDto fhglDto);
    /*
        根据条件获取要插入的到款信息
     */
    List<FhglDto> getFhAutoDkjl(FhglDto fhglDto);

    /**
     * 根据搜索条件获取导出条数
     * @return
     */
    int getCountForSearchExp(FhglDto fhglDto);

    /**
     * 从数据库分页获取导出数据
     * @return
     */
    List<FhglDto> getListForSearchExp(FhglDto fhglDto);

    /**
     * 从数据库分页获取导出数据
     * @return
     */
    List<FhglDto> getListForSelectExp(FhglDto fhglDto);
}

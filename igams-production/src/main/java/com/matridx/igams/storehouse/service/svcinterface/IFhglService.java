package com.matridx.igams.storehouse.service.svcinterface;

import java.util.List;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.FhglDto;
import com.matridx.igams.storehouse.dao.entities.FhglModel;

public interface IFhglService extends BaseBasicService<FhglDto, FhglModel>{
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
     String getFhglFhdhh();
  
    /**
   	 * 生成单号
   	 * @param
   	 * @return
   	 */
    String GenerateNumber();
	
    /**
     * 判断发货单是否重复
     * @param
     * @return
     */
    boolean isFhdhRepeatU8(FhglDto fhglDto);

	/**
	 * 判断发货单是否重复
	 * @param
	 * @return
	 */
    List<FhglDto> isFhdhRepeat(FhglDto fhglDto);
    /**
     * 判断发货单是否重复
	 * @param
     * @return
     */
    boolean shipFhAddSave(FhglDto fhglDto) throws BusinessException;

	/**
	 * 发货修改保存
	 * @param
	 * @return
	 */
    boolean shipFhModSave(FhglDto fhglDto) throws BusinessException;

	/**
	 * 判断发货单是否重复
	 *
	 * @param
	 */
    void shipFhDelSave(FhglDto fhglDto) throws BusinessException;

	/**
	 * 审核列表
	 * @param
	 * @return
	 */
    List<FhglDto> getPagedDtoFhxxList(FhglDto fhglDto);

	/**
	 * 退货单是否重复
	 * @param
	 * @return
	 */
    boolean thSave(FhglDto fhglDto) throws BusinessException;

	/**
	 * 审核列表
	 * @param
	 * @return
	 */
    List<FhglDto> getPagedAuditDevice(FhglDto fhglDto);
	/**
	 * 快递维护
	 */
    boolean expressMaintenanceFh(FhglDto fhglDto);

    boolean updateWlxx(FhglDto fhglDto);
}

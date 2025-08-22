package com.matridx.igams.storehouse.service.svcinterface;


import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.CpjgDto;
import com.matridx.igams.storehouse.dao.entities.CpjgModel;


public interface ICpjgService extends BaseBasicService<CpjgDto, CpjgModel>{

    /**
     * 新增保存
     */
    boolean saveAddProductInfo(CpjgDto cpjgDto) throws BusinessException;

    /**
     * 修改保存
     */
    boolean saveModProductInfo(CpjgDto cpjgDto) throws BusinessException;

    /**
     * 删除保存
     */
    boolean saveDelProductInfo(CpjgDto cpjgDto) throws BusinessException;
}

package com.matridx.bioinformation.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.bioinformation.dao.entities.JxglDto;
import com.matridx.bioinformation.dao.entities.JxglModel;

public interface IJxglService extends BaseBasicService<JxglDto, JxglModel>{
    /**
     * 删除应用和应用明细
     */
    Boolean updateJxInfo(JxglDto jxglDto) throws BusinessException;

    /**
     * 新增应用和应用明细
     */
    String insertJxInfo(JxglDto jxglDto) throws BusinessException;

    /**
     * 修改应用和应用明细
     */
    Boolean saveJxInfo(JxglDto jxglDto) throws BusinessException;

}

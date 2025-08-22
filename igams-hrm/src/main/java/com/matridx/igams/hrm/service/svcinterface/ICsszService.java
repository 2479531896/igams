package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.CsszDto;
import com.matridx.igams.hrm.dao.entities.CsszModel;

/**
 * @author WYX
 * @version 1.0
 * @className ICsszService
 * @description TODO
 * @date 16:50 2023/1/9
 **/
public interface ICsszService extends BaseBasicService<CsszDto, CsszModel> {
    /**
     * @description 新增保存初始设置信息
     */
    boolean addSaveInitSetting(CsszDto csszDto) throws BusinessException;
    /**
     * @description 修改保存初始设置信息
     */
    boolean modSaveInitSetting(CsszDto csszDto) throws BusinessException;

    /**
     * 根据模板类型获取初始设置数据
     */
    CsszDto getDtoByKhlx(String khlx);
}

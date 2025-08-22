package com.matridx.igams.hrm.service.svcinterface;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.TgxxDto;
import com.matridx.igams.hrm.dao.entities.TgxxModel;
public interface ITgxxService extends BaseBasicService<TgxxDto, TgxxModel> {
    /**
     * @description 调岗审核回调
     * @param processInstanceId processCode
     * @return boolean
     */
    boolean aduitJobAdjustmentCallback(String processInstanceId, String processCode,String wbcxid) throws BusinessException;
}

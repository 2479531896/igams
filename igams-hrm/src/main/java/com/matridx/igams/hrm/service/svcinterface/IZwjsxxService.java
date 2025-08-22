package com.matridx.igams.hrm.service.svcinterface;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.ZwjsxxDto;
import com.matridx.igams.hrm.dao.entities.ZwjsxxModel;

public interface IZwjsxxService extends BaseBasicService<ZwjsxxDto, ZwjsxxModel> {
    /**
     * 职位晋升审核回调
     */
    boolean aduitPostPromotionCallback(String processInstanceId,String processCode,String wbcxid) throws BusinessException;
}

package com.matridx.igams.production.service.svcinterface;


import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.DdbxglDto;
import com.matridx.igams.production.dao.entities.DdbxglModel;

/**
 * @author:JYK
 */
public interface IDdbxglService extends BaseBasicService<DdbxglDto, DdbxglModel> {
    /**
     * 执行钉钉审批回调
     */
    boolean reimburseCallback(String data, String processInstanceId,String processCode);
}

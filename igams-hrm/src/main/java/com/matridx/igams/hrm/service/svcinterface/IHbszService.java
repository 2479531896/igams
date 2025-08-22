package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.HbszDto;
import com.matridx.igams.hrm.dao.entities.HbszModel;

import java.util.Map;

/**
 * @author:JYK
 */
public interface IHbszService extends BaseBasicService<HbszDto, HbszModel> {
    /**
     * @description 新增保存红包设置信息
     */
    boolean addSaveRedpacketsetting(HbszDto hbszDto) throws BusinessException;
    /**
     * @description 修改保存红包设置信息
     */
    boolean modSaveRedpacketsetting(HbszDto hbszDto) throws BusinessException;
    /**
     * @description 删除红包设置信息
     */
    boolean delRedpacketsetting(HbszDto hbszDto) throws BusinessException;
    /**
     * @description 处理红包信息
     */
    Map<String, Object> processRedpacketInfo(HbszDto hbszDto) throws BusinessException;
}

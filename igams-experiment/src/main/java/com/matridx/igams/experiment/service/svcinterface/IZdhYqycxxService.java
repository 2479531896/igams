package com.matridx.igams.experiment.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.ZdhYqycxxDto;
import com.matridx.igams.experiment.dao.entities.ZdhYqycxxModel;

/**
 * @author : 郭祥杰
 * @date :
 */
public interface IZdhYqycxxService extends BaseBasicService<ZdhYqycxxDto, ZdhYqycxxModel> {
    /**
     * @Description: 仪器异常上报
     * @param jsonString
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/19 11:28
     */
    boolean exceptionReporting(String jsonString) throws BusinessException;
}

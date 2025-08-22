package com.matridx.igams.experiment.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.ZdhJkwdjlDto;
import com.matridx.igams.experiment.dao.entities.ZdhJkwdjlModel;

/**
 * @author : 郭祥杰
 * @date :
 */
public interface IZdhJkwdjlService extends BaseBasicService<ZdhJkwdjlDto, ZdhJkwdjlModel> {
    /**
     * @Description: 建库仪温度上报接口
     * @param jsonString
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/7 16:43
     */
    public boolean temperatureReporting(String jsonString) throws BusinessException;
}

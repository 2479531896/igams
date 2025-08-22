package com.matridx.igams.experiment.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.ZdhYbxxDto;
import com.matridx.igams.experiment.dao.entities.ZdhYbxxModel;

/**
 * @author : 郭祥杰
 * @date :
 */
public interface IZdhYbxxService extends BaseBasicService<ZdhYbxxDto, ZdhYbxxModel> {
    /**
     * @Description: 更新样本状态
     * @param jsonString
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/6 15:26
     */
    public boolean statusReporting(String jsonString) throws BusinessException;

}

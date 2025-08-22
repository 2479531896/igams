package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.SbfyDto;
import com.matridx.igams.production.dao.entities.SbfyModel;

import java.util.Map;

/**
 * @author : 郭祥杰
 * @date :
 */
public interface ISbfyService  extends BaseBasicService<SbfyDto, SbfyModel> {
    /**
     * @Description: 设备费用维护
     * @param sbfyDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/11/5 17:05
     */
    Map<String,Object> querySbfyList(SbfyDto sbfyDto);

    /**
     * @Description: 费用维护新增
     * @param sbfyDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/11/6 13:52
     */
    boolean insertSbfyDto(SbfyDto sbfyDto);
}

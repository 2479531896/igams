package com.matridx.bioinformation.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.bioinformation.dao.entities.RwglDto;
import com.matridx.bioinformation.dao.entities.RwglModel;

import java.util.List;

public interface IRwglService extends BaseBasicService<RwglDto, RwglModel>{
    /**
     * 新增任务
     * @param rwglDto
     * @return
     */
    Boolean addTaskInfo(RwglDto rwglDto) throws Exception;

    /**
     * 修改任务
     * @param rwglDto
     * @return
     */
    Boolean modTaskInfo(RwglDto rwglDto) throws BusinessException;

    /**
     * 获取任务列表
     * @param rwglDto
     * @return
     */
    List<RwglDto> getInfoList(RwglDto rwglDto);

    /**
     * 操作任务
     * @param rwglDto
     * @return
     */
    Boolean operateTaskInfo(RwglDto rwglDto) throws Exception;

    /**
     * 修改任务状态
     * @param rwglDtoList
     * @return
     */
    Boolean updateList(List<RwglDto> rwglDtoList);

    /**
     * 获取容器
     * @param strings
     * @return
     */
    List<RwglDto> getEndDtoList(List<String> strings);
}

package com.matridx.igams.experiment.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.SysjglDto;
import com.matridx.igams.experiment.dao.entities.SysjglModel;

import java.util.List;
import java.util.Map;

public interface ISysjglService extends BaseBasicService<SysjglDto, SysjglModel>{
    /**
     * @Description: 批量新增
     * @param jsonStr
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2025/5/12 10:43
     */
    boolean insertSysjglList(String jsonStr,String type);

    /**
     * @Description: 批量新增
     * @param sysjglDtos
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2025/5/12 17:04
     */
    boolean insertSysjglDtos(List<SysjglDto> sysjglDtos);

    /**
     * @Description: 送检查看
     * @param sysjglDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2025/5/13 9:38
     */
    Map<String,Object> getSysjxxxMap(SysjglDto sysjglDto);

    /**
     * @Description: 根据ids删除
     * @param sysjglDto
     * @return boolean
     */
    boolean deleteByIds(SysjglDto sysjglDto);
}

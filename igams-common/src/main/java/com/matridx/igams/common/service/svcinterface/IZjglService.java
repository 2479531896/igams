package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.ZjglDto;
import com.matridx.igams.common.dao.entities.ZjglModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;

public interface IZjglService extends BaseBasicService<ZjglDto, ZjglModel>{
    /**
     * @description 获取所有组件
     */
    List<ZjglDto> getAllComponent();
    /**
     * @Description: 上传工具类
     * @param zjglDto
     * @return Map<String,Object>
     * @Author: 郭祥杰
     * @Date: 2025/4/22 15:03
     */
    Map<String,Object> insertTool(ZjglDto zjglDto);

    /**
     * @Description: 上传工具附件
     * @param zjglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2025/4/29 11:21
     */
    boolean insertToolFile(ZjglDto zjglDto,List<ZjglDto> zjglDtoList);

    /**
     * @Description: 删除工具
     * @param zjglDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2025/4/29 16:16
     */
    Map<String,Object> delTool(ZjglDto zjglDto);

}

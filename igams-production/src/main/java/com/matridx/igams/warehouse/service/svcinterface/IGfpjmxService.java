package com.matridx.igams.warehouse.service.svcinterface;


import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.warehouse.dao.entities.GfpjmxDto;
import com.matridx.igams.warehouse.dao.entities.GfpjmxModel;

import java.util.List;
import java.util.Map;

public interface IGfpjmxService extends BaseBasicService<GfpjmxDto, GfpjmxModel> {
    /**
     * @Description: 供方评价明细新增
     * @param gfpjmxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/26 13:55
     */
    boolean insetGfpjmxDtoList(List<GfpjmxDto> gfpjmxDtoList);

    /**
     * @Description:  供方评价明细修改
     * @param gfpjmxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/26 14:29
     */
    boolean updateGfyzmxDtoList(List<GfpjmxDto> gfpjmxDtoList);

    /**
     * @Description: 获取审核意见
     * @param gfpjid
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @Author: 郭祥杰
     * @Date: 2024/7/1 15:22
     */
    Map<String,String> queryShxxMap(String gfpjid);
}

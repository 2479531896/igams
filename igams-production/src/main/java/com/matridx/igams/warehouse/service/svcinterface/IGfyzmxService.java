package com.matridx.igams.warehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.warehouse.dao.entities.GfyzmxDto;
import com.matridx.igams.warehouse.dao.entities.GfyzmxModel;

import java.util.List;


public interface IGfyzmxService extends BaseBasicService<GfyzmxDto, GfyzmxModel> {
    /**
     * @Description: 供方验证明细列表
     * @param gfyzmxDto
     * @return java.util.List<com.matridx.igams.warehouse.dao.entities.GfyzmxDto>
     * @Author: 郭祥杰
     * @Date: 2024/6/17 13:31
     */
    List<GfyzmxDto> getListByGfyzid(GfyzmxDto gfyzmxDto);

    /**
     * @Description: 批量新增
     * @param gfyzmxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/18 16:06
     */
    boolean insertGfyzmxDtoList(List<GfyzmxDto> gfyzmxDtoList);

    /**
     * @Description: 批量修改
     * @param gfyzmxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/19 17:33
     */
    boolean updateGfyzmxDtos(List<GfyzmxDto> gfyzmxDtoList);
}

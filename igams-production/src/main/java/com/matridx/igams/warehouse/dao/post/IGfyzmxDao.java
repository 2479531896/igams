package com.matridx.igams.warehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.warehouse.dao.entities.GfyzmxDto;
import com.matridx.igams.warehouse.dao.entities.GfyzmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author jld
 */
@Mapper
public interface IGfyzmxDao extends BaseBasicDao<GfyzmxDto, GfyzmxModel> {
    /**
     * @Description: 获取供方验证明细
     * @param gfyzmxDto
     * @return java.util.List<com.matridx.igams.warehouse.dao.entities.GfyzmxDto>
     * @Author: 郭祥杰
     * @Date: 2024/6/17 13:33
     */
    List<GfyzmxDto> getListByGfyzid(GfyzmxDto gfyzmxDto);

    /**
     * @Description: 批量新增
     * @param gfyzmxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/18 16:08
     */
    boolean insertGfyzmxDtoList(List<GfyzmxDto> gfyzmxDtoList);

    /**
     * @Description: 批量修改
     * @param gfyzmxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/19 17:27
     */
    boolean updateGfyzmxDtos(List<GfyzmxDto> gfyzmxDtoList);
}

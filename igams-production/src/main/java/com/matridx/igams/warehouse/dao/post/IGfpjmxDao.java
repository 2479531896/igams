package com.matridx.igams.warehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.warehouse.dao.entities.GfpjmxDto;
import com.matridx.igams.warehouse.dao.entities.GfpjmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author jld
 */
@Mapper
public interface IGfpjmxDao extends BaseBasicDao<GfpjmxDto, GfpjmxModel> {
    /**
     * @Description: 新增供方评价明细
     * @param gfpjmxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/26 13:57
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
}

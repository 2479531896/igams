package com.matridx.igams.warehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.warehouse.dao.entities.GfjxmxDto;
import com.matridx.igams.warehouse.dao.entities.GfjxmxModel;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
public interface IGfjxmxService extends BaseBasicService<GfjxmxDto, GfjxmxModel> {

    /**
     * @Description: 查询评估项目
     * @param
     * @return java.util.List<com.matridx.igams.warehouse.dao.entities.GfjxmxDto>
     * @Author: 郭祥杰
     * @Date: 2024/7/3 14:16
     */
    List<GfjxmxDto> queryDtoList();

    /**
     * @Description: 批量新增
     * @param gfjxmxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/7/3 17:32
     */
    boolean insertGfjxmxDtos(List<GfjxmxDto> gfjxmxDtoList);

    /**
     * @Description: 批量更新
     * @param gfjxmxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/7/4 13:54
     */
    boolean updateGfjxmxDtos(List<GfjxmxDto> gfjxmxDtoList);
}

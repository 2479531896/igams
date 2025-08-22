package com.matridx.igams.warehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.warehouse.dao.entities.GfjxmxDto;
import com.matridx.igams.warehouse.dao.entities.GfjxmxModel;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
public interface IGfjxmxDao extends BaseBasicDao<GfjxmxDto, GfjxmxModel> {
    /**
     * @Description: 获取供方项目
     * @param
     * @return java.util.List<com.matridx.igams.warehouse.dao.entities.GfjxmxDto>
     * @Author: 郭祥杰
     * @Date: 2024/7/3 14:17
     */
    List<GfjxmxDto> queryDtoList();

    /**
     * @Description: 批量新增
     * @param gfjxmxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/7/3 17:34
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

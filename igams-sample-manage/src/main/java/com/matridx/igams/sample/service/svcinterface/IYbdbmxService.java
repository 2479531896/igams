package com.matridx.igams.sample.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.sample.dao.entities.YbdbmxDto;
import com.matridx.igams.sample.dao.entities.YbdbmxModel;

import java.util.List;

public interface IYbdbmxService extends BaseBasicService<YbdbmxDto, YbdbmxModel> {

    /**
     * 获取样本调拨明细List
     * @param ybdbmxDto
     * @return
     */
    List<YbdbmxDto> getDbmxDtos(YbdbmxDto ybdbmxDto);
    /**
     * 检查是否可删除
     * @param ybdbmxDto
     * @return
     */
    List<String> checkCanDelete(YbdbmxDto ybdbmxDto);
    /*
        修改样本调拨明细
     */
    boolean updateYbdbmxDtos(List<YbdbmxDto> ybdbmxDtos);

    /**
     * @Description: 批量修改
     * @param ybdbmxDtos
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/8/1 17:08
     */
    boolean updateList(List<YbdbmxDto> ybdbmxDtos);
}

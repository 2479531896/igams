package com.matridx.igams.sample.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.sample.dao.entities.YbdbmxDto;
import com.matridx.igams.sample.dao.entities.YbdbmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IYbdbmxDao extends BaseBasicDao<YbdbmxDto, YbdbmxModel> {


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
     * @Description: 批量更新
     * @param ybdbmxDtos
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/8/1 17:10
     */
    boolean updateList(List<YbdbmxDto> ybdbmxDtos);
}

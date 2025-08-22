package com.matridx.igams.experiment.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.ZdhJkwdjlDto;
import com.matridx.igams.experiment.dao.entities.ZdhJkwdjlModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
@Mapper
public interface IZdhJkwdjlDao extends BaseBasicDao<ZdhJkwdjlDto, ZdhJkwdjlModel> {
    /**
     * @Description: 批量新增建库温度记录
     * @param jkwdjlDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/7 16:55
     */
    boolean insertJkwdjlList(List<ZdhJkwdjlDto> jkwdjlDtoList);
}

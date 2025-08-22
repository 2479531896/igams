package com.matridx.igams.experiment.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.ZdhYqycxxDto;
import com.matridx.igams.experiment.dao.entities.ZdhYqycxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
@Mapper
public interface IZdhYqycxxDao extends BaseBasicDao<ZdhYqycxxDto, ZdhYqycxxModel> {
    /**
     * @Description: 批量新增仪器异常
     * @param zdhYqycxxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/19 11:39
     */
    boolean insertYqycDtoLsit(List<ZdhYqycxxDto> zdhYqycxxDtoList);
}

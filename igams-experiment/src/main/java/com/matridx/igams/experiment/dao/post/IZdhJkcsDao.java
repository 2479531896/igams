package com.matridx.igams.experiment.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.ZdhJkcsDto;
import com.matridx.igams.experiment.dao.entities.ZdhJkcsModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
@Mapper
public interface IZdhJkcsDao extends BaseBasicDao<ZdhJkcsDto, ZdhJkcsModel> {
    /**
     * @Description: 批量新增接口参数
     * @param jkcsDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/11 9:23
     */
    boolean insertJkcsList(List<ZdhJkcsDto> jkcsDtoList);
    /**
     * @Description: 批量新增接口参数
     * @param jkcsDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/19 14:11
     */
    boolean insertJkcsDtos(List<ZdhJkcsDto> jkcsDtoList);
}

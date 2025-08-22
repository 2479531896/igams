package com.matridx.igams.experiment.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.ZdhJkcsDto;
import com.matridx.igams.experiment.dao.entities.ZdhJkcsModel;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
public interface IZdhJkcsService extends BaseBasicService<ZdhJkcsDto, ZdhJkcsModel> {
    /**
     * @Description: 批量新增接口参数
     * @param jkcsDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/11 9:22
     */
    boolean insertJkcsList(List<ZdhJkcsDto> jkcsDtoList);
    /**
     * @Description: 批量新增接口参数
     * @param jkcsDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/19 14:24
     */
    boolean insertJkcsDtos(List<ZdhJkcsDto> jkcsDtoList);
}

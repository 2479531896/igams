package com.matridx.igams.web.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.YhzcyDto;
import com.matridx.igams.web.dao.entities.YhzcyModel;

import java.util.List;

public interface IYhzcyService extends BaseBasicService<YhzcyDto, YhzcyModel>{

    /**
     * 用户组添加成员
     * @param yhzcyDto
     * @return
     */
    boolean toYhzcySelected(YhzcyDto yhzcyDto);

    /**
     * 用户组去除成员
     * @param yhzcyDto
     * @return
     */
    boolean toYhzcyOptional(YhzcyDto yhzcyDto);

    /**
     * 删除组下的所有成员
     * @param ids
     * @return
     */
    boolean deleteByIds(List<String> ids);
}

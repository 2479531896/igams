package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.SjyctzDto;
import com.matridx.igams.common.dao.entities.SjyctzModel;

import java.util.List;

public interface ISjyctzService extends BaseBasicService<SjyctzDto, SjyctzModel>{

    /* 获取用户列表 */
    List<SjyctzDto> getYhjsList(SjyctzDto sjyctzDto);

    /**
     * 批量新增
     */
    boolean insertList(List<SjyctzDto> list);

}

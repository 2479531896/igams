package com.matridx.igams.hrm.service.svcinterface;


import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.PxqxszDto;
import com.matridx.igams.hrm.dao.entities.PxqxszModel;

import java.util.List;

public interface IPxqxszService extends BaseBasicService<PxqxszDto, PxqxszModel> {

    /**
     * 查询机构列表(未选择)
     */
    List<PxqxszDto> getPagedUnSelectJgList(PxqxszDto pxqxszDto);
    /**
     * 查询机构列表(已选择)
     */
    List<PxqxszDto> getPagedSelectedJgList(PxqxszDto pxqxszDto);
    /**
     * 批量新增
     */
    boolean insertList(List<PxqxszDto> list);
    /**
     * 定时任务
     */
    List<PxqxszDto> getBeforeDayList(String day);
}

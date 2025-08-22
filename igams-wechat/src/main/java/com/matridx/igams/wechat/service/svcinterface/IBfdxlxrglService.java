package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.BfdxlxrglDto;
import com.matridx.igams.wechat.dao.entities.BfdxlxrglModel;

import java.util.List;

public interface IBfdxlxrglService extends BaseBasicService<BfdxlxrglDto, BfdxlxrglModel> {

    /**
     * 批量新增
     */
    boolean insertList(List<BfdxlxrglDto> list);
    /**
     * 批量修改
     */
    boolean updateList(List<BfdxlxrglDto> list);
    /**
     * 合并
     */
    boolean mergeList(List<BfdxlxrglDto> list);

}

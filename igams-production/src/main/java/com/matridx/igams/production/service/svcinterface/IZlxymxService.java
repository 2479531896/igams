package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.ZlxymxDto;
import com.matridx.igams.production.dao.entities.ZlxymxModel;

import java.util.List;

/**
 * @author:JYK
 */
public interface IZlxymxService extends BaseBasicService<ZlxymxDto, ZlxymxModel> {

    /**
     * 修改状态
     */
    boolean updateZt(ZlxymxDto zlxymxDto);

    /**
     * 批量新增
     */
    boolean insertList(List<ZlxymxDto> list);

    /**
     * 直接删除
     */
    void deleteInfo(ZlxymxDto zlxymxDto);
}

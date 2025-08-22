package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.TsclqkDto;
import com.matridx.igams.common.dao.entities.TsclqkModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface ITsclqkService extends BaseBasicService<TsclqkDto, TsclqkModel> {

    /**
     * 批量新增
     */
    boolean insertList(List<TsclqkDto> list);
    /**
     * 批量还原
     */
    boolean revertData(TsclqkDto tsclqkDto);


    List<TsclqkDto> getPagedListByBm(TsclqkDto tsclqkDto);

    /**
     *  处理
     **/
    boolean disposalSaveQualityComplaints(TsclqkDto tsclqkDto);
    /**
     * 获取部门集合
     */
    List<TsclqkDto> getDepartmentList(TsclqkDto tsclqkDto);
    /**
     * 批量修改
     */
    boolean updateForConclusion(List<TsclqkDto>list);

}

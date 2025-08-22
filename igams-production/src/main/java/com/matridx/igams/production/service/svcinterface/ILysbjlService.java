package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.*;

import java.util.List;
import java.util.Map;


/**
 * @author:JYK
 */
public interface ILysbjlService extends BaseBasicService<LysbjlDto, LysbjlModel> {
    //留样设备记录保存
    boolean saveRecordRetention(LysbjlDto lysbjlDto);
    /**
     * 获取留样设备group数据
     */
    List<LysbjlDto> getDtoListGroup(LysbjlDto lysbjlDto);
    /**
     * 获取留样设备统计数据
     */
    Map<String, Object> getStatisticsRetention(LysbjlDto lysbjlDto);
}

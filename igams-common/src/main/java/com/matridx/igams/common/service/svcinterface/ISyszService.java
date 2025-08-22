package com.matridx.igams.common.service.svcinterface;
import com.matridx.igams.common.dao.entities.DbszDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.SyszDto;
import com.matridx.igams.common.dao.entities.SyszModel;

import java.util.List;
import java.util.Map;

public interface ISyszService extends BaseBasicService<SyszDto, SyszModel>{
    /**
     * 获取首页要展示的页面
     */
    List<SyszDto> getHomePage(SyszDto syszDto);
    /**
     * 获取首页要展示的页面数据
     */
    void getHomePageData(List<SyszDto> syszDtos, Map<String, Object> map,SyszDto syszDto);
    /**
     * 获取任务统计
     */
    Map<String, Object> getHomePageTaskStatis(SyszDto syszDto);
    /*
        保存首页设置
     */
    boolean homePageSaveSetting(SyszDto syszDto) throws BusinessException;
    /*
        获取审核任务代办
     */
    Map<String, Object> getHomePageAuditTaskWaiting(SyszDto syszDto, List<DbszDto> dbszDtos);
}

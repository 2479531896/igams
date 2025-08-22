package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.NdpxDto;
import com.matridx.igams.hrm.dao.entities.NdpxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface INdpxDao extends BaseBasicDao<NdpxDto, NdpxModel>{

    /**
     * 获取筛选数据
     */
    List<String> getFilterData();

    /**
     * 获取工作管理数据
     */
    List<NdpxDto> getTaskInfo(NdpxDto ndpxDto);
    /**
     * 从数据库分页获取导出数据
     */
    List<NdpxDto> getListForSearchExp(NdpxDto ndpxDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<NdpxDto> getListForSelectExp(NdpxDto ndpxDto);
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(NdpxDto ndpxDto);
    /*
        获取年度培训提醒数据
     */
    List<NdpxDto> getRemindAnnualTraining(NdpxDto ndpxDto);
    /*
        修改是否发送
     */
    void updateSffs(NdpxDto ndpxDto);
}

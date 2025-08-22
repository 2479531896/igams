package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.SbpdmxDto;
import com.matridx.igams.production.dao.entities.SbpdmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISbpdmxDao extends BaseBasicDao<SbpdmxDto, SbpdmxModel>{
    /**
     * 批量新增
     */
    boolean insertList(List<SbpdmxDto> list);
    /**
     * 批量修改
     */
    boolean updateList(List<SbpdmxDto> list);
    /*
        导出条数
     */
    int getCountForSearchExp(SbpdmxDto sbpdmxDto);
    /*
        搜索导出
     */
    List<SbpdmxDto> getListForSearchExp(SbpdmxDto sbpdmxDto);
    /*
        选择导出
     */
    List<SbpdmxDto> getListForSelectExp(SbpdmxDto sbpdmxDto);
}

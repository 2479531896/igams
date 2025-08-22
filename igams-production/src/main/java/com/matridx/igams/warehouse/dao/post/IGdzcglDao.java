package com.matridx.igams.warehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.warehouse.dao.entities.GdzcglDto;
import com.matridx.igams.warehouse.dao.entities.GdzcglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IGdzcglDao extends BaseBasicDao<GdzcglDto, GdzcglModel> {
    /**
     * 从数据库分页获取导出数据
     * @param gdzcglDto
     
     */
    List<GdzcglDto> getListForSearchExp(GdzcglDto gdzcglDto);

    /**
     * 从数据库分页获取导出数据
     * @param gdzcglDto
     
     */
    List<GdzcglDto> getListForSelectExp(GdzcglDto gdzcglDto);
    /**
     * 根据搜索条件获取导出条数
     * @param gdzcglDto
     
     */
    int getCountForSearchExp(GdzcglDto gdzcglDto);
}

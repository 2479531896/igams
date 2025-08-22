package com.matridx.igams.hrm.dao.post;



import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.GrksmxDto;
import com.matridx.igams.hrm.dao.entities.GrksmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IGrksmxDao extends BaseBasicDao<GrksmxDto, GrksmxModel> {

    /**
     * 获取个人考试明细
     */
    List<GrksmxDto> getListByGrksid(GrksmxDto grksmxDto);
    /**
     * 从数据库分页获取导出数据
     */
    List<GrksmxDto> getListForSelectExp(GrksmxDto grksmxDto);
    /**
     * 从数据库分页获取导出数据
     */
    List<GrksmxDto> getListForSelect(GrksmxDto grksmxDto);
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(GrksmxDto grksmxDto);
    /**
     * 从数据库分页获取导出数据
     */
    List<GrksmxDto> getListForSearchExp(GrksmxDto grksmxDto);
    /**
     * 从数据库分页获取导出数据
     */
    List<GrksmxDto> getListForSearch(GrksmxDto grksmxDto);

    /**
     * 根据搜索条件获取导出条数(销售)
     */
    int getCountForSellSearchExp(GrksmxDto grksmxDto);
    /**
     * 从数据库分页获取导出数据(销售)
     */
    List<GrksmxDto> getListForSellSearchExp(GrksmxDto grksmxDto);


    /**
     * 新增考试信息
     */
    boolean insertList(List<GrksmxDto> list);
    /**
     * 获取个人考试明细
     */
    List<GrksmxDto> getListByKsIds(List<String> ids);
    /**
     * 更新分数
     */
    boolean updateGrksmxDtos(List<GrksmxDto> upGrksmxDtos);
}

package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.GrksmxDto;
import com.matridx.igams.hrm.dao.entities.GrksmxModel;


import java.util.List;
import java.util.Map;

public interface IGrksmxService extends BaseBasicService<GrksmxDto, GrksmxModel> {

    /**
     * 获取个人考试明细
     * @param grksmxDto
     * @return
     */
    List<GrksmxDto> getListByGrksid(GrksmxDto grksmxDto);
    /**
     * 从数据库分页获取导出数据
     * @param params
     * @return
     */
    List<GrksmxDto> getListForSelectExp(Map<String, Object> params);
    /**
     * 根据搜索条件获取导出条数
     * @param params
     * @return
     */
    int getCountForSearchExp(GrksmxDto grksmxDto, Map<String, Object> params);
    /**
     * 从数据库分页获取导出数据
     * @param params
     * @return
     */
    List<GrksmxDto> getListForSearchExp(Map<String, Object> params);
    /**
     * 新增考试信息
     *
     * @param list
     */
    void insertList(List<GrksmxDto> list);
    /**
     * 根据搜索条件获取导出条数(销售)
     * @param params
     * @return
     */
    int getCountForSellSearchExp(GrksmxDto grksmxDto, Map<String, Object> params);
    /**
     * 从数据库分页获取导出数据(销售)
     * @param params
     * @return
     */
    List<GrksmxDto> getListForSellSearchExp(Map<String, Object> params);
    /**
     * 获取个人考试明细
     * @param ids
     * @return
     */
    List<GrksmxDto> getListByKsIds(List<String> ids);
    /**
     * 更新分数
     * @param upGrksmxDtos
     * @return
     */
    boolean updateGrksmxDtos(List<GrksmxDto> upGrksmxDtos);
}

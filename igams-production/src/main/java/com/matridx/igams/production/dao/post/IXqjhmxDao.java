package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.XqjhmxDto;
import com.matridx.igams.production.dao.entities.XqjhmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IXqjhmxDao extends BaseBasicDao<XqjhmxDto, XqjhmxModel> {
    /**
     * 批量新增需求明细信息
     */
    Integer insertList(List<XqjhmxDto> xqjhmxDtoList);

    /**
     * 批量修改需求明细信息
     */
    Integer updateList(List<XqjhmxDto> xqjhmxDtoList);


    /**
     * 批量修改需求明细信息
     */
    Integer updateDtoList(List<XqjhmxDto> xqjhmxDtoList);
    /**
     * 删除
     */
    Integer deleteByCpxqids(XqjhmxDto xqjhmxDto);
    /**
     * 批量更新生产状态
     */
    boolean updateListSczt(List<XqjhmxDto> list);
    /**
     * 需求物料列表sql
     */
    List<XqjhmxDto>getPagedACMaterials(XqjhmxDto xqjhmxDto);
    XqjhmxDto getACMaterialDto(XqjhmxDto xqjhmxDto);
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(XqjhmxDto xqjhmxDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<XqjhmxDto> getListForSearchExp(XqjhmxDto xqjhmxDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<XqjhmxDto> getListForSelectExp(XqjhmxDto xqjhmxDto);

    /**
     * 根据ID查询
     */
    List<XqjhmxDto> getDtoListById(XqjhmxDto xqjhmxDto);
}

package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.XsmxDto;
import com.matridx.igams.storehouse.dao.entities.XsmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IXsmxDao extends BaseBasicDao<XsmxDto, XsmxModel>{
    /**
     * 废弃按钮
     *
     * @param xsmxDto
     */
    void discard(XsmxDto xsmxDto);
    /**
     * 批量新增
     * @param list
     * @return
     */
    boolean insertList(List<XsmxDto> list);
    /**
     * 根据Id查询信息
     * @param xsmxDto
     * @return
     */
    List<XsmxDto> getListById(XsmxDto xsmxDto);

    /**
     * 根据Id更新信息
     * @param
     * @return
     */
    Integer updateXsmxList(List<XsmxDto> xsmxDtos);
    /**
     * 批量更新
     * @param list
     * @return
     */
    int updateList(List<XsmxDto> list);
    /**
     * 物料编码转义
     */
    String escapeWlbm(XsmxDto xsmxDto);
    /**
     * 产品类型转义
     */
    String escapecplx(XsmxDto xsmxDto);

    /**
     * 销售明细列表
     * @param xsmxDto
     * @return
     */

    List<XsmxDto> getPagedDtoListDetails(XsmxDto xsmxDto);

    /**
     * 从数据库分页获取导出数据
     * @param xsmxDto
     * @return
     */
    List<XsmxDto> getListForSearchExp(XsmxDto xsmxDto);

    /**
     * 从数据库分页获取导出数据
     * @param xsmxDto
     * @return
     */
    List<XsmxDto> getListForSelectExp(XsmxDto xsmxDto);
    /**
     * 根据搜索条件获取导出条数
     * @param xsmxDto
     * @return
     */
    int getCountForSearchExp(XsmxDto xsmxDto);
    /**
     * 从数据库分页获取导出数据
     * @param xsmxDto
     * @return
     */
    List<XsmxDto> getListForReportSearchExp(XsmxDto xsmxDto);
    /**
     * 根据搜索条件获取导出条数
     * @param xsmxDto
     * @return
     */
    int getCountForReportSearchExp(XsmxDto xsmxDto);
    /**
     * 批量更新生产状态
     */
    boolean updateListSczt(List<XsmxDto> list);
    /*
        获取销售到款信息
     */
    List<XsmxDto> getDkxxByXsid(XsmxDto xsmxDto);
    /*
        获取销售单下销售明细
    */
    List<XsmxDto> getXsmxByXs(XsmxDto xsmxDto);
    /*
        修改到款信息
    */
    boolean updateDkxx(XsmxDto xsmxDto);
    /*
        获取到款信息group
    */
    List<XsmxDto> getDkxxGroup(XsmxDto xsmxDto);
    /*
    获取到款金额group
 */
    List<XsmxDto> getAllDkjeGroupXs(XsmxDto xsmxDto);
}

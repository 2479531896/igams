package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.XzdbcglDto;
import com.matridx.igams.storehouse.dao.entities.XzkcxxDto;
import com.matridx.igams.storehouse.dao.entities.XzkcxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IXzkcxxDao extends BaseBasicDao<XzkcxxDto, XzkcxxModel>{

    /**
     * 批量新增
     */
    boolean insertList(List<XzkcxxDto> list);
    /**
     * 批量修改
     */
    boolean updateList(List<XzkcxxDto> list);
    /**
     * 批量查询
     */
    List<XzkcxxDto> getDtoByXzkcids(XzdbcglDto xzdbcglDto);
    /**
     * 根据货物名称货物货物标准库位查询
     * @return
     */
    XzkcxxDto getDtoByHwmcAndHwbz(XzkcxxDto xzkcxxDto);
    /**
     * 批量更新
     */
    boolean updateListByIds(List<XzkcxxDto> list);

    /**
     * 批量库存量
     */
    boolean updateListKcl(List<XzkcxxDto> list);
    /**
     * 获取行政库存id
     * @return
     */
    List<XzkcxxDto> getXzkcid(XzkcxxDto xzkcxxDto);
    /**
     * 库存维护
     */
    boolean updateAqkc(XzkcxxDto xzkcxxDto);
    /**
     * 批量查询库存信息getXzkcxxs
     */
    List<XzkcxxDto> getXzkcxxs(List<XzkcxxDto> list);
    /**
     * 根据搜索条件获取导出条数
     * @param xzkcxxDto
     * @return
     */
    int getCountForSearchExp(XzkcxxDto xzkcxxDto);

    /**
     * 从数据库分页获取导出数据
     * @param xzkcxxDto
     * @return
     */
    List<XzkcxxDto> getListForSearchExp(XzkcxxDto xzkcxxDto);

    /**
     * 从数据库分页获取导出数据
     * @return
     */
    List<XzkcxxDto> getListForSelectExp(XzkcxxDto xzkcxxDto);

    void updateXzllDtos(List<XzkcxxDto> xzkcxxDtos);
    /**
     * 修改库存量
     */
    void updateListKclById(List<XzkcxxDto> list);
}

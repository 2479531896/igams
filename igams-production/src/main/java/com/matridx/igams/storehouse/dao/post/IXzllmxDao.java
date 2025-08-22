package com.matridx.igams.storehouse.dao.post;


import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.XzllmxDto;
import com.matridx.igams.storehouse.dao.entities.XzllmxModel;

import java.util.List;

@Mapper
public interface IXzllmxDao extends BaseBasicDao<XzllmxDto, XzllmxModel>{
    /**
     * 获取行政领料明细信息
     * @param xzllid
     * @return
     */
    List<XzllmxDto> getDtoXzllmxListByXzllid(String xzllid);

    /**
     * 根据行政库存id获取领料信息
     * @param xzkcid
     * @return
     */
    List<XzllmxDto> getDtoXzllmxListByXzkcid(String xzkcid);
    /**
     * 批量保存入库明细信息
     */
    boolean insertList(List<XzllmxDto> xzllmxDtos);
    /**
     * 批量修改入库明细信息
     */
    boolean updateList(List<XzllmxDto> xzllmxDtos);
    /**
     * 删除入库明细信息
     */
    boolean delXzllmxByXzllid(XzllmxDto xzllmxDto);

}

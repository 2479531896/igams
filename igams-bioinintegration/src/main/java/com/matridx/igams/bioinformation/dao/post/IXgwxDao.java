package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.bioinformation.dao.entities.XgwxDto;
import com.matridx.igams.bioinformation.dao.entities.XgwxModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IXgwxDao extends BaseBasicDao<XgwxDto, XgwxModel> {

    /**
     * 根据taxids查询list
     */
    List<XgwxDto> getDtoListByIds(XgwxDto xgwxDto);
    /**
     * 查询list
     */
    List<XgwxDto> getXgwxList(XgwxDto xgwxDto);
    /**
     * 批量更新
     */
    int updateList(List<XgwxDto> list);
    /**
     * 批量更新参考文献管理表
     */
    int updateCkwxgl(List<XgwxDto> list);

}

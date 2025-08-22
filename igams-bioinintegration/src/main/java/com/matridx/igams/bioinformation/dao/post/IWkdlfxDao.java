package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.bioinformation.dao.entities.WkdlfxDto;
import com.matridx.igams.bioinformation.dao.entities.WkdlfxModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IWkdlfxDao extends BaseBasicDao<WkdlfxDto, WkdlfxModel> {
    /**
     * 批量新增
     */
    boolean insertList(List<WkdlfxDto> list);

}

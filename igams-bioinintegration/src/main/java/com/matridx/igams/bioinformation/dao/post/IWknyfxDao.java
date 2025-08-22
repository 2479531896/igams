package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.bioinformation.dao.entities.WknyfxDto;
import com.matridx.igams.bioinformation.dao.entities.WknyfxModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IWknyfxDao extends BaseBasicDao<WknyfxDto, WknyfxModel> {
    /**
     * 批量新增
     */
    boolean insertList(List<WknyfxDto> list);

}

package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.bioinformation.dao.entities.WklcznDto;
import com.matridx.igams.bioinformation.dao.entities.WklcznModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IWklcznDao extends BaseBasicDao<WklcznDto, WklcznModel> {
    /**
     * 批量新增
     */
    boolean insertList(List<WklcznDto> list);

}

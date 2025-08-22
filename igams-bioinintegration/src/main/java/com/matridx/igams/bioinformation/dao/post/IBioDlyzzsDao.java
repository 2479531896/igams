package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.bioinformation.dao.entities.BioDlyzzsDto;
import com.matridx.igams.bioinformation.dao.entities.BioDlyzzsModel;

import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IBioDlyzzsDao extends BaseBasicDao<BioDlyzzsDto, BioDlyzzsModel> {

    /**
     * 批量更新
     */
    int updateList(List<BioDlyzzsDto> list);
}

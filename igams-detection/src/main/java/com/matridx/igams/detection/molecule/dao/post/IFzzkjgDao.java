package com.matridx.igams.detection.molecule.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.detection.molecule.dao.entities.FzzkjgDto;
import com.matridx.igams.detection.molecule.dao.entities.FzzkjgModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IFzzkjgDao extends BaseBasicDao<FzzkjgDto, FzzkjgModel>{

    /**
     * 批量插入
     */
    boolean insertList(List<FzzkjgDto> list);
}

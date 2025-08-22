package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.GcjlDto;
import com.matridx.igams.production.dao.entities.GcjlModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IGcjlDao extends BaseBasicDao<GcjlDto, GcjlModel> {
    /**
     * 观察记录
     */
    List<GcjlDto> getDtolistGcSampleStock(GcjlDto gcjlDto);

    boolean insertGcjlDtos(List<GcjlDto> gcjlDtos);
}

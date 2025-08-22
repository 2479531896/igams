package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.bioinformation.dao.entities.BioLczzznglDto;
import com.matridx.igams.bioinformation.dao.entities.BioLczzznglModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IBioLczzznglDao extends BaseBasicDao<BioLczzznglDto, BioLczzznglModel> {

    /**
     * 根据znids查询list
     */
    List<BioLczzznglDto> getDtoListByIds(BioLczzznglDto lczzznglDto);
}

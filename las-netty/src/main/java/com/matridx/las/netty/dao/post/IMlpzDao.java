package com.matridx.las.netty.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.las.netty.dao.entities.MlpzDto;
import com.matridx.las.netty.dao.entities.MlpzModel;

import java.util.List;

@Mapper
public interface IMlpzDao extends BaseBasicDao<MlpzDto, MlpzModel>{
    public List<MlpzDto> getMlpzList(MlpzDto mlpzDto);
}

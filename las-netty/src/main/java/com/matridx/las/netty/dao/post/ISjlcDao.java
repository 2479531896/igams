package com.matridx.las.netty.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.las.netty.dao.entities.SjlcModel;

import java.util.List;

@Mapper
public interface ISjlcDao extends BaseBasicDao<SjlcDto, SjlcModel>{
    public List<SjlcDto> getSjlcList(SjlcDto sjlcDto);
}

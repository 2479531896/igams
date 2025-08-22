package com.matridx.las.netty.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.las.netty.dao.entities.YsybxxDto;
import com.matridx.las.netty.dao.entities.YsybxxModel;

import java.util.List;

@Mapper
public interface IYsybxxDao extends BaseBasicDao<YsybxxDto, YsybxxModel>{

    int updateList(List<YsybxxDto> list);

    int insertList(List<YsybxxDto> list);

}

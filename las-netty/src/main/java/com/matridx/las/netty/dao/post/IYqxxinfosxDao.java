package com.matridx.las.netty.dao.post;

import com.matridx.las.netty.dao.entities.JkywzszDto;
import com.matridx.las.netty.dao.entities.YqxxinfosxDto;
import com.matridx.las.netty.dao.entities.YqxxinfosxModel;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;

import java.util.List;

@Mapper
public interface IYqxxinfosxDao extends BaseBasicDao<YqxxinfosxDto, YqxxinfosxModel>{
    public int deleteAll();
    int updateList(List<YqxxinfosxDto> list);
    int insertList(List<YqxxinfosxDto> list);
    public List<YqxxinfosxDto> getYqxxinfoSxList(YqxxinfosxDto yqxxinfosxDto);
}

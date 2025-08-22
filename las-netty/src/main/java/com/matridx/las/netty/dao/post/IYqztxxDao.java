package com.matridx.las.netty.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.las.netty.dao.entities.YqztxxDto;
import com.matridx.las.netty.dao.entities.YqztxxModel;

@Mapper
public interface IYqztxxDao extends BaseBasicDao<YqztxxDto, YqztxxModel>{
   public void updateStYqztxx(YqztxxDto yqztxxDto);
   public List<Map<String, String>> getAllYqztList() ;
}

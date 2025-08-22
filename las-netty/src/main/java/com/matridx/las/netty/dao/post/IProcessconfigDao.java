package com.matridx.las.netty.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.las.netty.dao.entities.ProcessconfigDto;
import com.matridx.las.netty.dao.entities.ProcessconfigModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IProcessconfigDao extends BaseBasicDao<ProcessconfigDto, ProcessconfigModel>{
    public List<ProcessconfigDto> getDtoByCsid(String csid);

}

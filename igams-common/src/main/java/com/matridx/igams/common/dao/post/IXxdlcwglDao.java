package com.matridx.igams.common.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.XxdlcwglDto;
import com.matridx.igams.common.dao.entities.XxdlcwglModel;

@Mapper
public interface IXxdlcwglDao extends BaseBasicDao<XxdlcwglDto, XxdlcwglModel>{
     int insert(XxdlcwglDto xxdlcwglDto);

    int deleteByDate(String date);
}

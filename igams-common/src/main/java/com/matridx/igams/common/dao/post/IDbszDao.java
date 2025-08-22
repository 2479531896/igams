package com.matridx.igams.common.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.DbszDto;
import com.matridx.igams.common.dao.entities.DbszModel;

import java.util.List;

@Mapper
public interface IDbszDao extends BaseBasicDao<DbszDto, DbszModel>{
    /*
    获取个人代办设置
 */
    List<DbszDto> getPersonDtoList(DbszDto dbszDto);
    /*
        保存代办设置
     */
    boolean insertDbszDtos(List<DbszDto> dbszDtos);
}

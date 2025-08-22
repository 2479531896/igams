package com.matridx.igams.web.dao.matridxsql;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author : 郭祥杰
 * @date :
 */
@Mapper
public interface ICommonInsertAiDao {
    int insertResult(String cxdm);
    int delResult(String cxdm);
}

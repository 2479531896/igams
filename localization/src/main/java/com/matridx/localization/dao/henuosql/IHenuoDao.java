package com.matridx.localization.dao.henuosql;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author : 郭祥杰
 * @date :
 */
@Mapper
public interface IHenuoDao {
    String getHospitalName(@Param("barcode")String barcode);
}

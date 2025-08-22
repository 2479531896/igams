package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HY_DZ_BorrowOutBackDao extends BaseBasicDao<HY_DZ_BorrowOutBackDto, HY_DZ_BorrowOutBackModel>{

    /**
     * 判断单号是否存在
     */
    HY_DZ_BorrowOutBackDto queryByCode(HY_DZ_BorrowOutBackDto hy_dz_borrowOutBackDto);

    /**
     * 获取单号
     */
    List<HY_DZ_BorrowOutBackDto> getcCode(HY_DZ_BorrowOutBackDto hy_dz_borrowOutBackDto);

}

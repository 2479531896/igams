package com.matridx.igams.production.dao.matridxsql;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.HY_DZ_BorrowOutBacksDto;
import com.matridx.igams.production.dao.entities.HY_DZ_BorrowOutBacksModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HY_DZ_BorrowOutBacksDao extends BaseBasicDao<HY_DZ_BorrowOutBacksDto, HY_DZ_BorrowOutBacksModel> {

    /**
     * 判断单号是否存在
     */
    HY_DZ_BorrowOutBacksDto queryByCode(HY_DZ_BorrowOutBacksDto hy_dz_borrowOutBacksDto);

    /**
     * 获取单号
     */
    List<HY_DZ_BorrowOutBacksDto> getcCode(HY_DZ_BorrowOutBacksDto hy_dz_borrowOutBacksDto);
    int insert2(HY_DZ_BorrowOutBacksDto hyDzBorrowOutBacksDto);
}

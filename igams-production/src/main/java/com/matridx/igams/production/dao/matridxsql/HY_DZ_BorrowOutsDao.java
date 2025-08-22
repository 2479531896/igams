package com.matridx.igams.production.dao.matridxsql;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.HY_DZ_BorrowOutsDto;
import com.matridx.igams.production.dao.entities.HY_DZ_BorrowOutsModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HY_DZ_BorrowOutsDao extends BaseBasicDao<HY_DZ_BorrowOutsDto, HY_DZ_BorrowOutsModel> {

    /**
     * 判断单号是否存在
     */
    HY_DZ_BorrowOutsDto queryByCode(HY_DZ_BorrowOutsDto hy_dz_borrowOutsDto);

    /**
     * 获取单号
     */
    List<HY_DZ_BorrowOutsDto> getcCode(HY_DZ_BorrowOutsDto hy_dz_borrowOutsDto);

    boolean updateList(List<HY_DZ_BorrowOutsDto> list);
}

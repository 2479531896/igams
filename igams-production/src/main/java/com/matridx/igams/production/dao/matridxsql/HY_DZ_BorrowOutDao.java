package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.HY_DZ_BorrowOutDto;
import com.matridx.igams.production.dao.entities.HY_DZ_BorrowOutModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HY_DZ_BorrowOutDao extends BaseBasicDao<HY_DZ_BorrowOutDto, HY_DZ_BorrowOutModel>{

    /**
     * 判断单号是否存在
     */
    HY_DZ_BorrowOutDto queryByCode(HY_DZ_BorrowOutDto hy_dz_borrowOutDto);

    /**
     * 获取单号
     */
    List<HY_DZ_BorrowOutDto> getcCode(HY_DZ_BorrowOutDto hy_dz_borrowOutDto);

}

package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.OM_MODetailsDto;
import com.matridx.igams.production.dao.entities.OM_MODetailsModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface OM_MODetailsDao extends BaseBasicDao<OM_MODetailsDto, OM_MODetailsModel> {
    /**
     * 获取序号最大值
     */
    Integer getXhMax(OM_MODetailsDto om_moDetailsDto);


    int updateRkxx(List<OM_MODetailsDto> list);
    /*
            批量插入
        * */
    int insertList(List<OM_MODetailsDto> om_moDetailsDtos);
    /*
        修改多
     */
    int updates(OM_MODetailsDto omMoDetailsDto);
}

package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.OM_MOMaterialsDto;
import com.matridx.igams.production.dao.entities.OM_MOMaterialsModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface OM_MOMaterialsDao extends BaseBasicDao<OM_MOMaterialsDto, OM_MOMaterialsModel> {
    int insertList(List<OM_MOMaterialsDto> om_moMaterialsDtos);
    /**
     * 批量更新信息
     */
    int updateList(List<OM_MOMaterialsDto> om_moMaterialsDtos);

}


package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.SO_SODetailsDto;
import com.matridx.igams.production.dao.entities.SO_SODetailsModel;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;
@Mapper
public interface SO_SODetailsDao extends BaseBasicDao<SO_SODetailsDto, SO_SODetailsModel> {

    List<SO_SODetailsDto> getSO_SODetailsInfo(SO_SODetailsDto so_soDetailsDto);

    /**
     * 批量修改销售单明细
     */
    int updateListDtos(List<SO_SODetailsDto> sO_SODetailsDtos);

    /**
     * 批量插入销售单明细
     */
    int insertList(List<SO_SODetailsDto> sO_SODetailsDtos);

    int updateSoListDtos(List<SO_SODetailsDto> sO_soDetailsDtos);

    SO_SODetailsDto getIrownoByiSOsID(SO_SODetailsDto sO_soDetails_ino);
}
 
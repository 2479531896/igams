package com.matridx.igams.production.dao.matridxsql;
import java.util.List;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.SO_SOMainDto;
import com.matridx.igams.production.dao.entities.SO_SOMainModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SO_SOMainDao extends BaseBasicDao<SO_SOMainDto, SO_SOMainModel> {


    /**
     * 通过ID获取SO_SOMainInfo
     */
    SO_SOMainDto getSO_SOMainInfoByID(SO_SOMainDto so_soMainDto);

    /**
     * 批量修改数量
     */
    int updateList(List<SO_SOMainDto> sO_SOMainDtos);

    /**
     * 得到总条数
     */
    Integer getToTalNumber();

    /**
     * 获取单号
     */
    List<SO_SOMainDto> getcSOCodeRd(SO_SOMainDto so_soMainDto);

    /**
     * 获取单号
     */
    SO_SOMainDto getcSOCode(SO_SOMainDto so_soMainDto);
}

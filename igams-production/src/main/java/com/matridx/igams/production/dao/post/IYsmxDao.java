package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.YsmxDto;
import com.matridx.igams.production.dao.entities.YsmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IYsmxDao extends BaseBasicDao<YsmxDto, YsmxModel>{

    /**
     * 批量新增
     */
    boolean insertList(List<YsmxDto> list);
    /**
     * 删除废弃数据
     */
    boolean delObsoleteData(YsmxDto ysmxDto);

}

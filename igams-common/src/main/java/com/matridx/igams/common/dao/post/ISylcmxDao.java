package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.SylcmxDto;
import com.matridx.igams.common.dao.entities.SylcmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISylcmxDao extends BaseBasicDao<SylcmxDto, SylcmxModel>{

    /**
     * 批量新增
     */
    boolean insertList(List<SylcmxDto> list);
    /**
     * 删除废弃数据
     */
    boolean delObsoleteData(SylcmxDto sylcmxDto);

}

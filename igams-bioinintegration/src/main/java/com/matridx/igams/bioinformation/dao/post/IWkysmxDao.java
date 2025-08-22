package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.bioinformation.dao.entities.WkysmxDto;
import com.matridx.igams.bioinformation.dao.entities.WkysmxModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IWkysmxDao extends BaseBasicDao<WkysmxDto, WkysmxModel> {
    /**
     * 批量新增
     */
    boolean insertList(List<WkysmxDto> list);

    List<WkysmxDto> getList(WkysmxDto wkysmxDto);
}

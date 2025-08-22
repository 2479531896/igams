package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.bioinformation.dao.entities.ZsxxDto;
import com.matridx.igams.bioinformation.dao.entities.ZsxxModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IZsxxDao extends BaseBasicDao<ZsxxDto, ZsxxModel> {

    /**
     * 批量更新
     */
    int updateList(List<ZsxxDto> list);

}

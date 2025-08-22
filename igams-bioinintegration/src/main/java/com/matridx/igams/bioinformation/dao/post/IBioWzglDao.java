package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.bioinformation.dao.entities.*;
import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IBioWzglDao extends BaseBasicDao<BioWzglDto, BioWzglModel> {

    /**
     * 根据wzid/中文名称/英文名称 查找物种信息
     */
    BioWzglDto getWzxxByMc(BioWzglDto wzglDto);

    /**
     * 获取物种List
     */
    List<BioWzglDto> getWzList(BioWzglDto wzglDto);

    /**
     * 更新wzfl
     */
    boolean updateWzflByFid(List<BioWzglDto>list);

    /**
     * insert or update
     */
    boolean updateList(List<BioWzglDto>list);

}

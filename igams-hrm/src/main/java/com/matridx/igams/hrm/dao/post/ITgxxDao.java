package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.TgxxDto;
import com.matridx.igams.hrm.dao.entities.TgxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ITgxxDao extends BaseBasicDao<TgxxDto, TgxxModel> {
    /**
     * @description 批量插入调岗信息信息
     */
    boolean insertTgxxDtos(List<TgxxDto> tgxxDtos);
}

package com.matridx.igams.production.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.XpxxDto;
import com.matridx.igams.production.dao.entities.XpxxModel;

import java.util.List;

@Mapper
public interface IXpxxDao extends BaseBasicDao<XpxxDto, XpxxModel>{

    List<XpxxDto> getUnUpdateChips(XpxxDto xpxxDto);

    int updateChipQcScore(List<XpxxDto> updateList);
}

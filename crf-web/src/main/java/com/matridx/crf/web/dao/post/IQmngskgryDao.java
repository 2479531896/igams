package com.matridx.crf.web.dao.post;

import com.matridx.crf.web.dao.entities.QmngshzxxDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.crf.web.dao.entities.QmngskgryDto;
import com.matridx.crf.web.dao.entities.QmngskgryModel;

@Mapper
public interface IQmngskgryDao extends BaseBasicDao<QmngskgryDto, QmngskgryModel>{
    public boolean delDtoList(QmngskgryDto qmngskgryDto);

}

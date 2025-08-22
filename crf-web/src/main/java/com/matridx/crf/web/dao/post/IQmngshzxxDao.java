package com.matridx.crf.web.dao.post;

import com.matridx.crf.web.dao.entities.QmngsdmxqDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.crf.web.dao.entities.QmngshzxxDto;
import com.matridx.crf.web.dao.entities.QmngshzxxModel;

import java.util.List;
import java.util.Map;

@Mapper
public interface IQmngshzxxDao extends BaseBasicDao<QmngshzxxDto, QmngshzxxModel>{
    public List<Map<String,String>> getHospitailList(String dqjs);
    public boolean delDto(QmngshzxxDto qmngshzxxDto);
}

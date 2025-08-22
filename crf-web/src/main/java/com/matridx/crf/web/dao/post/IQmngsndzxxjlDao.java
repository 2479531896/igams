package com.matridx.crf.web.dao.post;

import com.matridx.crf.web.dao.entities.HbpdrxxjlDto;
import com.matridx.crf.web.dao.entities.QmngshzxxDto;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.crf.web.dao.entities.QmngsndzxxjlDto;
import com.matridx.crf.web.dao.entities.QmngsndzxxjlModel;

@Mapper
public interface IQmngsndzxxjlDao extends BaseBasicDao<QmngsndzxxjlDto, QmngsndzxxjlModel>{
    public boolean delDto(QmngshzxxDto qmngshzxxDto);
    public boolean delDtoList(QmngsndzxxjlDto qmngsndzxxjlDto);
    public List<QmngsndzxxjlDto> queryById(QmngsndzxxjlDto qmngsndzxxjlDto);
    /**
     * 从数据库分页获取导出数据
     * @param qmngsndzxxjlDto
     * @return
     */
    public List<QmngsndzxxjlDto> getListForSelectExp(QmngsndzxxjlDto qmngsndzxxjlDto);
}

package com.matridx.crf.web.dao.post;

import com.matridx.crf.web.dao.entities.NdzxxDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.crf.web.dao.entities.HbpdrxxjlDto;
import com.matridx.crf.web.dao.entities.HbpdrxxjlModel;

import java.util.List;

@Mapper
public interface IHbpdrxxjlDao extends BaseBasicDao<HbpdrxxjlDto, HbpdrxxjlModel>{

    /**
     * 根据患者ID删除记录
     * @param hbpdrxxjlDto
     * @return
     */
    public boolean deleteByHzid(HbpdrxxjlDto hbpdrxxjlDto);
	
	/**
     * 通过HBP患者ID获取HBP记录信息
     * @param hbpdrxxjlDto
     * @return
     */
    List<HbpdrxxjlDto> getDtoListByhzid(HbpdrxxjlDto hbpdrxxjlDto);
    /**
     * 从数据库分页获取导出数据
     * @param hbpdrxxjlDto
     * @return
     */
    public List<HbpdrxxjlDto> getListForSelectExp(HbpdrxxjlDto hbpdrxxjlDto);

}

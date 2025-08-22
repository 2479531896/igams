package com.matridx.crf.web.dao.post;

import com.matridx.crf.web.dao.entities.QmngsndzxxjlDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.crf.web.dao.entities.QmngsshDto;
import com.matridx.crf.web.dao.entities.QmngsshModel;

import java.util.List;
import java.util.Map;

@Mapper
public interface IQmngsshDao extends BaseBasicDao<QmngsshDto, QmngsshModel>{
    public List<QmngsshDto> getSjz(String qmngsndzjlid);
    public int deleteByNdz(QmngsndzxxjlDto ndz);
    public boolean insertSh(List<QmngsshDto> dmxqs);
    public List<Map<String, String>> getSjzName(String qmngsndzjlid);
    /**
   	 * 获取生化配置
   	 * @return
   	 */
	public List<QmngsshDto> queryShpz(String hzid);
}

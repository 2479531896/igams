package com.matridx.crf.web.dao.post;

import com.matridx.crf.web.dao.entities.*;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;

import java.util.List;
import java.util.Map;

@Mapper
public interface IQmngsdmxqDao extends BaseBasicDao<QmngsdmxqDto, QmngsdmxqModel>{
    public List<QmngsdmxqDto> getSjz(String qmngsndzjlid);
    public int deleteByNdz(QmngsndzxxjlDto ndz);
    public boolean insertDmxq(List<QmngsdmxqDto> dmxqs);
    public List<Map<String, String>> getSjzName(String qmngsndzjlid);
    /**
  	 * 获取动脉血气
  	 * @return
  	 */
    public List<QmngsdmxqDto> queryDmxq(String hzid);
}

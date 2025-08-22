package com.matridx.crf.web.dao.post;

import com.matridx.crf.web.dao.entities.*;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;

import java.util.List;
import java.util.Map;

@Mapper
public interface IQmngsyzzbDao extends BaseBasicDao<QmngsyzzbDto, QmngsyzzbModel>{
    public List<QmngsyzzbDto> getSjz(String ndzjlid);
    public int deleteByNdz(QmngsndzxxjlDto ndz);
    public boolean insertYzzb(List<QmngsyzzbDto> dmxqs);
    public List<Map<String, String>> getSjzName(String ndzjlid);
    /**
   	 * 获炎症指标
   	 * @return
   	 */
    public List<QmngsyzzbDto> queryYzzb(String hzid);
}

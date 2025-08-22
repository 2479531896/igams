package com.matridx.crf.web.dao.post;

import com.matridx.crf.web.dao.entities.*;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;

import java.util.List;
import java.util.Map;

@Mapper
public interface IQmngsxcgDao extends BaseBasicDao<QmngsxcgDto, QmngsxcgModel>{
    public List<QmngsxcgDto> getSjz(String ndzjlid);
    public int deleteByNdz(QmngsndzxxjlDto ndz);
    public boolean insertXcg(List<QmngsxcgDto> xcgs);
    public List<Map<String, String>> getSjzName(String ndzjlid);
    /**
   	 * 获取血常规
   	 * @return
   	 */
    public List<QmngsxcgDto> queryXcg(String hzid);
}

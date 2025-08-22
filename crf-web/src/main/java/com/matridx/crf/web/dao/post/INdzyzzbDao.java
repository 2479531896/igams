package com.matridx.crf.web.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.crf.web.dao.entities.NdzyzzbDto;
import com.matridx.crf.web.dao.entities.NdzyzzbModel;

@Mapper
public interface INdzyzzbDao extends BaseBasicDao<NdzyzzbDto, NdzyzzbModel>{
	public List<NdzyzzbDto> getSjz(String ndzjlid);
	public int deleteByNdz(NdzxxjlDto ndz);
	public boolean insertYzzb(List<NdzyzzbDto> dmxqs);
	public List<Map<String, String>> getSjzName(String ndzjlid);

}

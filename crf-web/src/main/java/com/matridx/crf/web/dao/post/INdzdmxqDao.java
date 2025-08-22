package com.matridx.crf.web.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.crf.web.dao.entities.NdzdmxqDto;
import com.matridx.crf.web.dao.entities.NdzdmxqModel;
import com.matridx.crf.web.dao.entities.NdzxxjlDto;

@Mapper
public interface INdzdmxqDao extends BaseBasicDao<NdzdmxqDto, NdzdmxqModel>{
	public List<NdzdmxqDto> getSjz(String ndzjlid);
	public int deleteByNdz(NdzxxjlDto ndz);
	public boolean insertDmxq(List<NdzdmxqDto> dmxqs);
	public List<Map<String, String>> getSjzName(String ndzjlid);

}

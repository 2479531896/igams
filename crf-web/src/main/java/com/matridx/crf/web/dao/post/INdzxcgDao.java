package com.matridx.crf.web.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.crf.web.dao.entities.NdzxcgDto;
import com.matridx.crf.web.dao.entities.NdzxcgModel;
import com.matridx.crf.web.dao.entities.NdzxxjlDto;

@Mapper
public interface INdzxcgDao extends BaseBasicDao<NdzxcgDto, NdzxcgModel>{
	public List<NdzxcgDto> getSjz(String ndzjlid);
	public int deleteByNdz(NdzxxjlDto ndz);
	public boolean insertXcg(List<NdzxcgDto> xcgs);
	public List<Map<String, String>> getSjzName(String ndzjlid);

}

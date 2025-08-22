package com.matridx.crf.web.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.crf.web.dao.entities.NdzshDto;
import com.matridx.crf.web.dao.entities.NdzshModel;

@Mapper
public interface INdzshDao extends BaseBasicDao<NdzshDto, NdzshModel>{
	public List<NdzshDto> getSjz(String ndzjlid);
	public int deleteByNdz(NdzxxjlDto ndz);
	public boolean insertSh(List<NdzshDto> shs);
	public List<Map<String, String>> getSjzName(String ndzjlid);

}

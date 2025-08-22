package com.matridx.crf.web.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.crf.web.dao.entities.NdzdmxqDto;
import com.matridx.crf.web.dao.entities.NdzshDto;
import com.matridx.crf.web.dao.entities.NdzshModel;

public interface INdzshService extends BaseBasicService<NdzshDto, NdzshModel>{
	public List<NdzshDto> getSjz(String ndzjlid);
	public void saveSh(List<NdzshDto> dmxqs,NdzxxjlDto ndz);
}

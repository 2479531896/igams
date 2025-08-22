package com.matridx.crf.web.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.crf.web.dao.entities.NdzxcgDto;
import com.matridx.crf.web.dao.entities.NdzxcgModel;

public interface INdzxcgService extends BaseBasicService<NdzxcgDto, NdzxcgModel>{
	public List<NdzxcgDto> getSjz(String ndzjlid);
	public void saveXcg(List<NdzxcgDto> dmxqs,NdzxxjlDto ndz);
}

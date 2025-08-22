package com.matridx.crf.web.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.crf.web.dao.entities.NdzyzzbDto;
import com.matridx.crf.web.dao.entities.NdzyzzbModel;

public interface INdzyzzbService extends BaseBasicService<NdzyzzbDto, NdzyzzbModel>{
	public List<NdzyzzbDto> getSjz(String ndzjlid);
	public void saveYzzb(List<NdzyzzbDto> dmxqs,NdzxxjlDto ndz);
}

package com.matridx.crf.web.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.crf.web.dao.entities.NdzdmxqDto;
import com.matridx.crf.web.dao.entities.NdzdmxqModel;

public interface INdzdmxqService extends BaseBasicService<NdzdmxqDto, NdzdmxqModel>{
	public List<NdzdmxqDto> getSjz(String ndzjlid);
	public void saveDmxq(List<NdzdmxqDto> dmxqs,NdzxxjlDto ndz);
}

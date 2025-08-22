package com.matridx.crf.web.service.svcinterface;

import com.matridx.crf.web.dao.entities.*;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface IQmngsdmxqService extends BaseBasicService<QmngsdmxqDto, QmngsdmxqModel>{
    public List<QmngsdmxqDto> getSjz(String ndzjlid);
    public void saveDmxq(List<QmngsdmxqDto> dmxqs, QmngsndzxxjlDto ndz);
    
    /**
	 * 获取动脉血气
	 * @return
	 */
    public List<QmngsdmxqDto> queryDmxq(String hzid);
}

package com.matridx.crf.web.service.svcinterface;

import com.matridx.crf.web.dao.entities.*;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface IQmngsxcgService extends BaseBasicService<QmngsxcgDto, QmngsxcgModel>{
    public List<QmngsxcgDto> getSjz(String ndzjlid);
    public void saveXcg(List<QmngsxcgDto> dmxqs, QmngsndzxxjlDto ndz);
    /**
   	 * 获取血常规
   	 * @return
   	 */
    public List<QmngsxcgDto> queryXcg(String hzid);
}

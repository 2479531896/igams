package com.matridx.crf.web.service.svcinterface;

import com.matridx.crf.web.dao.entities.*;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface IQmngsshService extends BaseBasicService<QmngsshDto, QmngsshModel>{
    public List<QmngsshDto> getSjz(String ndzjlid);
    public void saveSh(List<QmngsshDto> dmxqs, QmngsndzxxjlDto ndz);
    /**
   	 * 获取生化配置
   	 * @return
   	 */
    public List<QmngsshDto> queryShpz(String hzid);
}

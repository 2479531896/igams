package com.matridx.crf.web.service.svcinterface;

import com.matridx.crf.web.dao.entities.*;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface IQmngsyzzbService extends BaseBasicService<QmngsyzzbDto, QmngsyzzbModel>{
    public List<QmngsyzzbDto> getSjz(String ndzjlid);
    public void saveYzzb(List<QmngsyzzbDto> dmxqs, QmngsndzxxjlDto ndz);
    /**
   	 * 获炎症指标
   	 * @return
   	 */
    public List<QmngsyzzbDto> queryYzzb(String hzid);
}

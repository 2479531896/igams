package com.matridx.crf.web.service.svcinterface;

import com.matridx.crf.web.dao.entities.BeanQmngsndzxxFroms;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.crf.web.dao.entities.QmngshzxxDto;
import com.matridx.crf.web.dao.entities.QmngshzxxModel;

import java.util.List;
import java.util.Map;

public interface IQmngshzxxService extends BaseBasicService<QmngshzxxDto, QmngshzxxModel>{
    public List<Map<String,String>> getHospitailList(String dqjs);
    public boolean saveQmngsNdz(QmngshzxxDto qmngshzxxDto, BeanQmngsndzxxFroms beanQmngsndzxxFroms);
    public boolean updateQmngshzxx(QmngshzxxDto qmngshzxxDto,BeanQmngsndzxxFroms beanQmngsndzxxFroms);
	/* qmngs患者信息查看
	 * 
	 * @return
	 */
	public QmngshzxxDto queryById(String id);
	public boolean delQmngsHzxx(String qmngshzid,String userid);
}

package com.matridx.crf.web.service.svcinterface;

import com.matridx.crf.web.dao.entities.BeanHbpxxForms;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.crf.web.dao.entities.HbphzxxDto;
import com.matridx.crf.web.dao.entities.HbphzxxModel;

public interface IHbphzxxService extends BaseBasicService<HbphzxxDto, HbphzxxModel>{

    boolean insertHbpHzxx(HbphzxxDto hbphzxxDto, BeanHbpxxForms beanHbpxxForms);

    boolean updateHbpHzxx(HbphzxxDto hbphzxxDto, BeanHbpxxForms beanHbpxxForms);
}

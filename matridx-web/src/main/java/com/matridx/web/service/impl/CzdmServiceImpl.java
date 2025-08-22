package com.matridx.web.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.web.dao.entities.CzdmDto;
import com.matridx.web.dao.entities.CzdmModel;
import com.matridx.web.dao.post.ICzdmDao;
import com.matridx.web.service.svcinterface.ICzdmService;

@Service
public class CzdmServiceImpl extends BaseBasicServiceImpl<CzdmDto, CzdmModel, ICzdmDao> implements ICzdmService{

}

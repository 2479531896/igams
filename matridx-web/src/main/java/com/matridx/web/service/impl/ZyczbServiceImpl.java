package com.matridx.web.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.web.dao.entities.ZyczbDto;
import com.matridx.web.dao.entities.ZyczbModel;
import com.matridx.web.dao.post.IZyczbDao;
import com.matridx.web.service.svcinterface.IZyczbService;

@Service
public class ZyczbServiceImpl extends BaseBasicServiceImpl<ZyczbDto, ZyczbModel, IZyczbDao> implements IZyczbService{

}

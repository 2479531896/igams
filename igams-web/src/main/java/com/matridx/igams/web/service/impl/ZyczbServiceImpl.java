package com.matridx.igams.web.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.web.dao.entities.ZyczbDto;
import com.matridx.igams.web.dao.entities.ZyczbModel;
import com.matridx.igams.web.dao.post.IZyczbDao;
import com.matridx.igams.web.service.svcinterface.IZyczbService;

@Service
public class ZyczbServiceImpl extends BaseBasicServiceImpl<ZyczbDto, ZyczbModel, IZyczbDao> implements IZyczbService{

}

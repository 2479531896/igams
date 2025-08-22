package com.matridx.web.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.web.dao.entities.JszyczbDto;
import com.matridx.web.dao.entities.JszyczbModel;
import com.matridx.web.dao.post.IJszyczbDao;
import com.matridx.web.service.svcinterface.IJszyczbService;

@Service
public class JszyczbServiceImpl extends BaseBasicServiceImpl<JszyczbDto, JszyczbModel, IJszyczbDao> implements IJszyczbService{

}

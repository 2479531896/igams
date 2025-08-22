package com.matridx.igams.common.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.WtzyDto;
import com.matridx.igams.common.dao.entities.WtzyModel;
import com.matridx.igams.common.dao.post.IWtzyDao;
import com.matridx.igams.common.service.svcinterface.IWtzyService;

@Service
public class WtzyServiceImpl extends BaseBasicServiceImpl<WtzyDto, WtzyModel, IWtzyDao> implements IWtzyService{

}

package com.matridx.igams.web.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.web.dao.entities.WbzyczDto;
import com.matridx.igams.web.dao.entities.WbzyczModel;
import com.matridx.igams.web.dao.post.IWbzyczDao;
import com.matridx.igams.web.service.svcinterface.IWbzyczService;

@Service
public class WbzyczServiceImpl extends BaseBasicServiceImpl<WbzyczDto, WbzyczModel, IWbzyczDao> implements IWbzyczService{

}

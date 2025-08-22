package com.matridx.igams.common.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.GrdcszDto;
import com.matridx.igams.common.dao.entities.GrdcszModel;
import com.matridx.igams.common.dao.post.IGrdcszDao;
import com.matridx.igams.common.service.svcinterface.IGrdcszService;

@Service
public class GrdcszServiceImpl extends BaseBasicServiceImpl<GrdcszDto, GrdcszModel, IGrdcszDao> implements IGrdcszService{

}

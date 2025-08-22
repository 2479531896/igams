package com.matridx.igams.common.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.CydcszDto;
import com.matridx.igams.common.dao.entities.CydcszModel;
import com.matridx.igams.common.dao.post.ICydcszDao;
import com.matridx.igams.common.service.svcinterface.ICydcszService;

@Service
public class CydcszServiceImpl extends BaseBasicServiceImpl<CydcszDto, CydcszModel, ICydcszDao> implements ICydcszService{

}

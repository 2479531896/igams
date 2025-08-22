package com.matridx.las.netty.service.impl;

import com.matridx.las.netty.dao.entities.MlsjDto;
import com.matridx.las.netty.dao.entities.MlsjModel;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.las.netty.dao.post.IMlsjDao;
import com.matridx.las.netty.service.svcinterface.IMlsjService;

@Service
public class MlsjServiceImpl extends BaseBasicServiceImpl<MlsjDto, MlsjModel, IMlsjDao> implements IMlsjService{

}

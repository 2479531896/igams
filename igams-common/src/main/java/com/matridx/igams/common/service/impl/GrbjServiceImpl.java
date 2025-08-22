package com.matridx.igams.common.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.GrbjDto;
import com.matridx.igams.common.dao.entities.GrbjModel;
import com.matridx.igams.common.dao.post.IGrbjDao;
import com.matridx.igams.common.service.svcinterface.IGrbjService;

import java.util.List;

@Service
public class GrbjServiceImpl extends BaseBasicServiceImpl<GrbjDto, GrbjModel, IGrbjDao> implements IGrbjService{

}

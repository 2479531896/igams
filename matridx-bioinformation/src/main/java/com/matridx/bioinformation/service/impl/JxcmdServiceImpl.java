package com.matridx.bioinformation.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.bioinformation.dao.entities.JxcmdDto;
import com.matridx.bioinformation.dao.entities.JxcmdModel;
import com.matridx.bioinformation.dao.post.IJxcmdDao;
import com.matridx.bioinformation.service.svcinterface.IJxcmdService;

@Service
public class JxcmdServiceImpl extends BaseBasicServiceImpl<JxcmdDto, JxcmdModel, IJxcmdDao> implements IJxcmdService {

}

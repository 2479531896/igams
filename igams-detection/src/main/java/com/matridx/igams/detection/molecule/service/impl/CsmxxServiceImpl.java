package com.matridx.igams.detection.molecule.service.impl;

import com.matridx.igams.detection.molecule.dao.entities.CsmxxDto;
import com.matridx.igams.detection.molecule.dao.entities.CsmxxModel;
import com.matridx.igams.detection.molecule.dao.post.ICsmxxDao;
import com.matridx.igams.detection.molecule.service.svcinterface.ICsmxxService;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;

@Service
public class CsmxxServiceImpl extends BaseBasicServiceImpl<CsmxxDto, CsmxxModel, ICsmxxDao> implements ICsmxxService {

}

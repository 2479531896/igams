package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.PdjlDto;
import com.matridx.igams.production.dao.entities.PdjlModel;
import com.matridx.igams.production.dao.post.IPdjlDao;
import com.matridx.igams.production.service.svcinterface.IPdjlService;
import org.springframework.stereotype.Service;

/**
 * @author:JYK
 */
@Service
public class PdjlServiceImpl extends BaseBasicServiceImpl<PdjlDto, PdjlModel, IPdjlDao> implements IPdjlService {
}

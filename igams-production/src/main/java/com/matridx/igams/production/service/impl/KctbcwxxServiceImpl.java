package com.matridx.igams.production.service.impl;


import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.KctbcwxxDto;
import com.matridx.igams.production.dao.entities.KctbcwxxModel;
import com.matridx.igams.production.dao.post.IKctbcwxxDao;
import com.matridx.igams.production.service.svcinterface.IKctbcwxxService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KctbcwxxServiceImpl extends BaseBasicServiceImpl<KctbcwxxDto, KctbcwxxModel, IKctbcwxxDao> implements IKctbcwxxService {
    @Override
    public boolean insertKctbcwxxList(List<KctbcwxxDto> list) {
        return dao.insertKctbcwxxList(list)!=0;
    }
}

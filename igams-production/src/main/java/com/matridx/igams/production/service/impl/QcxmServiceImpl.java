package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.QcxmDto;
import com.matridx.igams.production.dao.entities.QcxmModel;
import com.matridx.igams.production.dao.post.IQcxmDao;
import com.matridx.igams.production.service.svcinterface.IQcxmService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QcxmServiceImpl extends BaseBasicServiceImpl<QcxmDto, QcxmModel, IQcxmDao> implements IQcxmService {

    @Override
    public boolean insertList(List<QcxmDto> list) {
        return dao.insertList(list);
    }

    @Override
    public boolean delAbandonedData(QcxmDto qcxmDto) {
        return dao.delAbandonedData(qcxmDto);
    }
}

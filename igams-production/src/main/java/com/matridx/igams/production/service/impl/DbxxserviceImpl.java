package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.DbxxDto;
import com.matridx.igams.production.dao.entities.DbxxModel;
import com.matridx.igams.production.dao.post.IDbxxDao;
import com.matridx.igams.production.service.svcinterface.IDbxxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
@Service
public class DbxxserviceImpl extends BaseBasicServiceImpl<DbxxDto, DbxxModel, IDbxxDao> implements IDbxxService {
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertList(List<DbxxDto> dbxxDtoList) {
        return dao.insertList(dbxxDtoList)>0;
    }
}

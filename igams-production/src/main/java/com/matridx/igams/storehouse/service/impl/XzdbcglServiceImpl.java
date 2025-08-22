package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.XzdbcglDto;
import com.matridx.igams.storehouse.dao.entities.XzdbcglModel;
import com.matridx.igams.storehouse.dao.post.IXzdbcglDao;
import com.matridx.igams.storehouse.service.svcinterface.IXzdbcglService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:JYK
 */
@Service
public class XzdbcglServiceImpl extends BaseBasicServiceImpl<XzdbcglDto, XzdbcglModel, IXzdbcglDao> implements IXzdbcglService {
    @Override
    public Boolean deleteList(List<XzdbcglDto> list) {
        return dao.deleteList(list);
    }
}

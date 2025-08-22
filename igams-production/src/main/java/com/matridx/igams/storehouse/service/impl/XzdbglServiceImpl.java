package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.XzdbglDto;
import com.matridx.igams.storehouse.dao.entities.XzdbglModel;
import com.matridx.igams.storehouse.dao.post.IXzdbglDao;
import com.matridx.igams.storehouse.service.svcinterface.IXzdbglService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:JYK
 */
@Service
public class XzdbglServiceImpl extends BaseBasicServiceImpl<XzdbglDto, XzdbglModel, IXzdbglDao> implements IXzdbglService {

    @Override
    public boolean insertList(List<XzdbglDto> list) {
        return dao.insertList(list);
    }
}

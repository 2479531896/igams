package com.matridx.igams.hrm.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.hrm.dao.entities.YhqjxxDto;
import com.matridx.igams.hrm.dao.entities.YhqjxxModel;
import com.matridx.igams.hrm.dao.post.IYhqjxxDao;
import com.matridx.igams.hrm.service.svcinterface.IYhqjxxService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:JYK
 */
@Service
public class YhqjxxServiceImpl extends BaseBasicServiceImpl<YhqjxxDto, YhqjxxModel, IYhqjxxDao> implements IYhqjxxService {
    @Override
    public YhqjxxDto selectDtoByDdslid(YhqjxxDto yhqjxxDto) {
        return dao.selectDtoByDdslid(yhqjxxDto);
    }

    @Override
    public List<YhqjxxDto> getDtoListGroupYhAndQjlx(YhqjxxDto yhqjxxDto) {
        return dao.getDtoListGroupYhAndQjlx(yhqjxxDto);
    }

}

package com.matridx.igams.web.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.web.dao.entities.YhzcyDto;
import com.matridx.igams.web.dao.entities.YhzcyModel;
import com.matridx.igams.web.dao.post.IYhzcyDao;
import com.matridx.igams.web.service.svcinterface.IYhzcyService;

import java.util.List;

@Service
public class YhzcyServiceImpl extends BaseBasicServiceImpl<YhzcyDto, YhzcyModel, IYhzcyDao> implements IYhzcyService{

    /**
     * 用户组添加成员
     * @param yhzcyDto
     * @return
     */
    @Override
    public boolean toYhzcySelected(YhzcyDto yhzcyDto) {
        return dao.toYhzcySelected(yhzcyDto);
    }

    /**
     * 用户组去除成员
     * @param yhzcyDto
     * @return
     */
    @Override
    public boolean toYhzcyOptional(YhzcyDto yhzcyDto) {
        return dao.toYhzcyOptional(yhzcyDto);
    }

    /**
     * 删除组下的所有成员
     * @param ids
     * @return
     */
    @Override
    public boolean deleteByIds(List<String> ids) {
        return dao.deleteByIds( ids);
    }
}

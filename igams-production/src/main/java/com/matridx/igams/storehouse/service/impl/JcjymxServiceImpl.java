package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.dao.post.IJcjymxDao;
import com.matridx.igams.storehouse.service.svcinterface.IJcjymxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JcjymxServiceImpl extends BaseBasicServiceImpl<JcjymxDto, JcjymxModel, IJcjymxDao> implements IJcjymxService {


    @Override
    public List<JcjymxDto> getDtoListGroupByCk(JcjymxDto jcjymxDto) {
        return dao.getDtoListGroupByCk(jcjymxDto);
    }

    @Override
    public List<JcjymxDto> getDtoListGroupByXx(JcjymxDto jcjymxDto) {
        return dao.getDtoListGroupByXx(jcjymxDto);
    }

    @Override
    public boolean deleteByIds(List<String> ids) {
        return dao.deleteByIds(ids);
    }

    @Override
    public List<JcjymxDto> getScphAndSlByJyxxid(String jyxxid) {
        return dao.getScphAndSlByJyxxid(jyxxid);
    }

    @Override
    public List<JcjymxDto> getDtoListGroupBy(JcjymxDto jcjymxDto) {
        return dao.getDtoListGroupBy(jcjymxDto);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateList(List<JcjymxDto> jcjymxDtos) {
        return dao.updateList(jcjymxDtos)>0;
    }
}

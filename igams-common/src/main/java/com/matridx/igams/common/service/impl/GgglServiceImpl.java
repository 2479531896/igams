package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.GgglDto;
import com.matridx.igams.common.dao.entities.GgglModel;
import com.matridx.igams.common.dao.post.IGgglDao;
import com.matridx.igams.common.service.svcinterface.IGgglService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GgglServiceImpl extends BaseBasicServiceImpl<GgglDto, GgglModel, IGgglDao> implements IGgglService{
    @Autowired
    IFjcfbService fjcfbService;
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveNotice(GgglDto ggglDto) throws BusinessException {
        ggglDto.setGgid(StringUtil.generateUUID());
        insert(ggglDto);
        if(!CollectionUtils.isEmpty(ggglDto.getFjids())) {
            for (int i = 0; i < ggglDto.getFjids().size(); i++) {
                boolean save2RealFile = fjcfbService.save2RealFile(ggglDto.getFjids().get(i), ggglDto.getGgid());
                if (!save2RealFile){
                    throw new BusinessException("msg","保存公告附件失败！");
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveNotice(GgglDto ggglDto) throws BusinessException {
        update(ggglDto);
        if(!CollectionUtils.isEmpty(ggglDto.getFjids())) {
            for (int i = 0; i < ggglDto.getFjids().size(); i++) {
                boolean save2RealFile = fjcfbService.save2RealFile(ggglDto.getFjids().get(i), ggglDto.getGgid());
                if (!save2RealFile){
                    throw new BusinessException("msg","保存公告附件失败！");
                }
            }
        }
        return true;
    }

}

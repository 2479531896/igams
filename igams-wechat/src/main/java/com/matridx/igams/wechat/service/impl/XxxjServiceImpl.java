package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.wechat.dao.entities.XxxjDto;
import com.matridx.igams.wechat.dao.entities.XxxjModel;
import com.matridx.igams.wechat.dao.post.IXxxjDao;
import com.matridx.igams.wechat.service.svcinterface.IXxxjService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class XxxjServiceImpl extends BaseBasicServiceImpl<XxxjDto, XxxjModel, IXxxjDao> implements IXxxjService {

    @Autowired
    private IFjcfbService fjcfbService;
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deleteById(XxxjDto xxxjDto) {
        return dao.deleteById(xxxjDto)>0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateDtoById(XxxjDto xxxjDto) {
        return dao.updateDtoById(xxxjDto)>0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean progressSaveXxxj(XxxjDto xxxjDto) {
        if(StringUtil.isNotBlank(xxxjDto.getXjid())){
            dao.updateDtoById(xxxjDto);
        }else{
            xxxjDto.setXjid(StringUtil.generateUUID());
            dao.insertDto(xxxjDto);
        }
        //附件复制到正式文件夹
        if(xxxjDto.getFjids()!=null && !xxxjDto.getFjids().isEmpty()){
            for (int i = 0; i < xxxjDto.getFjids().size(); i++) {
                boolean saveFile = fjcfbService.save2RealFile(xxxjDto.getFjids().get(i),xxxjDto.getXjid());
                if(!saveFile)
                    return false;
            }
        }
        return true;
    }
}

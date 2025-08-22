package com.matridx.igams.hrm.service.impl;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.hrm.dao.entities.BcglDto;
import com.matridx.igams.hrm.dao.entities.BtglDto;
import com.matridx.igams.hrm.dao.entities.BtglModel;
import com.matridx.igams.hrm.dao.post.IBtglDao;
import com.matridx.igams.hrm.service.svcinterface.IBcglService;
import com.matridx.igams.hrm.service.svcinterface.IBtglService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BtglServiceImpl extends BaseBasicServiceImpl<BtglDto, BtglModel, IBtglDao> implements IBtglService {
    @Autowired
    IBcglService bcglService;

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveSubsidys(BtglDto btglDto, User user) {
        if (StringUtil.isNotBlank(btglDto.getBtglid())){
            BtglDto btglDto_t=new BtglDto();
            btglDto_t.setBtglid(btglDto.getBtglid());
            btglDto.setIds(btglDto.getBtglid());
            btglDto.setScry(user.getYhid());
            delete(btglDto);
            btglDto.setBtglid(StringUtil.generateUUID());
            btglDto.setLrry(user.getYhid());
            insert(btglDto);
            BcglDto bcglDto=new BcglDto();
            bcglDto.setBtglid(btglDto_t.getBtglid());
            List<BcglDto> bcglDto_ts=bcglService.getDtoList(bcglDto);
            if (!CollectionUtils.isEmpty(bcglDto_ts)){
                for (BcglDto bcglDto_t : bcglDto_ts) {
                    bcglDto.setIds(bcglDto_t.getBcglid());
                }
            }
            bcglDto.setBtglid(btglDto.getBtglid());
            bcglDto.setXgry(user.getXgry());
            bcglDto.setXgry(user.getYhid());
            bcglService.update(bcglDto);
        }else {
            btglDto.setLrry(user.getYhid());
            btglDto.setBtglid(StringUtil.generateUUID());
            btglDto.setLrry(user.getYhid());
            insert(btglDto);
        }
        return true;
    }
}

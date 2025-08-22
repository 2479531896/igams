package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.DzglDto;
import com.matridx.igams.wechat.dao.entities.DzglModel;
import com.matridx.igams.wechat.dao.post.IDzglDao;
import com.matridx.igams.wechat.service.svcinterface.IDzglService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class DzglServiceImpl extends BaseBasicServiceImpl<DzglDto, DzglModel,IDzglDao> implements IDzglService{
    /**
     * 根据ID获取地址
     * @param ryid
     * @return
     */
    public List<DzglDto> getListByUser(String ryid){return dao.getListByUser(ryid);}

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertDto(DzglDto dzglDto){
        int result = dao.insert(dzglDto);
        return result != 0;
    }


    public void cleardefault(String ryid){
        dao.cleardefault(ryid);
    }
    public boolean changedefault(DzglDto dzglDto){
        return dao.changedefault(dzglDto);
    }


}

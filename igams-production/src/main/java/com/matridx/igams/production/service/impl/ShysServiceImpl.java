package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.ShysDto;
import com.matridx.igams.production.dao.entities.ShysModel;
import com.matridx.igams.production.dao.post.IShysDao;
import com.matridx.igams.production.service.svcinterface.IShysService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
@Service
public class ShysServiceImpl extends BaseBasicServiceImpl<ShysDto, ShysModel, IShysDao> implements IShysService {

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertDtoList(List<ShysDto> shysDtoList) {
        return dao.insertDtoList(shysDtoList)>0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateDispose(ShysDto shysDto) {
        if(StringUtil.isBlank(shysDto.getCszt())){
            shysDto.setCszt("1");
        }
        if(StringUtil.isBlank(shysDto.getCsbz())){
            shysDto.setCsbz("");
        }
        return dao.updateDispose(shysDto)>0;
    }
}

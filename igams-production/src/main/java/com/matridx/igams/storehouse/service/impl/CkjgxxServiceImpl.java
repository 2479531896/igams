package com.matridx.igams.storehouse.service.impl;


import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.CkjgxxDto;
import com.matridx.igams.storehouse.dao.entities.CkjgxxModel;
import com.matridx.igams.storehouse.dao.post.ICkjgxxDao;
import com.matridx.igams.storehouse.service.svcinterface.ICkjgxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CkjgxxServiceImpl extends BaseBasicServiceImpl<CkjgxxDto, CkjgxxModel, ICkjgxxDao> implements ICkjgxxService {

    /**
     * 权限设置保存
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean batchpermitSaveCkxx(CkjgxxDto ckjgxxDto) {
        List<CkjgxxDto> list=(List<CkjgxxDto>) JSON.parseArray(ckjgxxDto.getCkjgxx_json(),CkjgxxDto.class);
        dao.delete(ckjgxxDto);
        if(list!=null&&!list.isEmpty()){
            for(CkjgxxDto dto:list){
                dto.setCkjgid(StringUtil.generateUUID());
                dto.setCkid(ckjgxxDto.getCkid());
            }
            boolean inserted = dao.insertList(list);
            if(!inserted){
                return false;
            }
        }
        return true;
    }
}

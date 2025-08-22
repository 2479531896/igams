package com.matridx.igams.web.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.web.dao.entities.JszyczbDto;
import com.matridx.igams.web.dao.entities.JszyczbModel;
import com.matridx.igams.web.dao.post.IJszyczbDao;
import com.matridx.igams.web.service.svcinterface.IJszyczbService;

import java.util.List;

@Service
public class JszyczbServiceImpl extends BaseBasicServiceImpl<JszyczbDto, JszyczbModel, IJszyczbDao> implements IJszyczbService{

    /**
     * 根据角色ID获取该角色全部菜单权限
     * @param jsid
     * @return
     */
    public List<JszyczbDto> getListById(String jsid){
        return dao.getListById(jsid);
    }

}

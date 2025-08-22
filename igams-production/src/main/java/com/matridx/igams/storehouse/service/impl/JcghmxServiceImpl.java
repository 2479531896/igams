package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.JcghmxDto;
import com.matridx.igams.storehouse.dao.entities.JcghmxModel;
import com.matridx.igams.storehouse.dao.post.IJcghmxDao;
import com.matridx.igams.storehouse.service.svcinterface.IJcghmxService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class JcghmxServiceImpl extends BaseBasicServiceImpl<JcghmxDto, JcghmxModel, IJcghmxDao> implements IJcghmxService {

    /**
     * 通过jyxxid获取信息
     * @param
     * @return
     */
    public List<JcghmxDto> getListByJyxxid(JcghmxDto jcghmxDto){
        return dao.getListByJyxxid(jcghmxDto);
    }

    /**
     * 通过ckid分组获取信息
     * @param
     * @return
     */
    public List<JcghmxDto> getDtoListGroupByCk(JcghmxDto jcghmxDto){
        return dao.getDtoListGroupByCk(jcghmxDto);
    }

    /**
     * 批量新增
     * @param jcghmxDtos
     * @return
     */
    public boolean insertList(List<JcghmxDto> jcghmxDtos){
        return dao.insertList(jcghmxDtos);
    }

}

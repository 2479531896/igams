package com.matridx.igams.wechat.service.impl;


import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.JcsqmxDto;
import com.matridx.igams.wechat.dao.entities.JcsqmxModel;
import com.matridx.igams.wechat.dao.post.IJcsqmxDao;
import com.matridx.igams.wechat.service.svcinterface.IJcsqmxService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JcsqmxServiceImpl extends BaseBasicServiceImpl<JcsqmxDto, JcsqmxModel, IJcsqmxDao> implements IJcsqmxService {

    /**
     * 批量新增
     * @param list
     * @return
     */
    public boolean insertList(List<JcsqmxDto> list){
        return dao.insertList(list);
    }

    /**
     * 验证标本
     * @param jcsqmxDto
     * @return
     */
    public List<JcsqmxDto> verifySamples(JcsqmxDto jcsqmxDto){
        return dao.verifySamples(jcsqmxDto);
    }
}

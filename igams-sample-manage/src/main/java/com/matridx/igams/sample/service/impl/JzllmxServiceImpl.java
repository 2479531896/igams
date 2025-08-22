package com.matridx.igams.sample.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.sample.dao.entities.JzllmxDto;
import com.matridx.igams.sample.dao.entities.JzllmxModel;
import com.matridx.igams.sample.dao.post.IJzllmxDao;
import com.matridx.igams.sample.service.svcinterface.IJzllmxService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * {@code @author:JYK}
 */
@Service
public class JzllmxServiceImpl extends BaseBasicServiceImpl<JzllmxDto, JzllmxModel, IJzllmxDao> implements IJzllmxService {
    @Override
    public List<JzllmxDto> getDtoListByllid(String id) {
        return dao.getDtoListByllid(id);
    }

    /**
     * 批量新增
     */
    public boolean insertList(List<JzllmxDto> jzllmxDtos){
        return dao.insertList(jzllmxDtos);
    }

    /**
     * 批量更新
     */
    public boolean updateList(List<JzllmxDto> list){
        return dao.updateList(list);
    }
}

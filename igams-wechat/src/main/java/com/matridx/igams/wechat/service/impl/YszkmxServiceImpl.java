package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.YszkmxDto;
import com.matridx.igams.wechat.dao.entities.YszkmxModel;
import com.matridx.igams.wechat.dao.post.IYszkmxDao;
import com.matridx.igams.wechat.service.svcinterface.IYszkmxService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class YszkmxServiceImpl extends BaseBasicServiceImpl<YszkmxDto, YszkmxModel, IYszkmxDao> implements IYszkmxService {

    @Override
    public int insertList(List<YszkmxDto> list) {
        return dao.insertList(list);
    }

    /**
     * 查询对账明细
     * @param params
     * @return
     */
    public List<Map<String,Object>> getDtoListOptimize(Map<String, Object> params){
        return dao.getDtoListOptimize(params);
    }
}

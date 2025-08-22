package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.DdbxmxDto;
import com.matridx.igams.production.dao.entities.DdbxmxModel;
import com.matridx.igams.production.dao.post.IDdbxmxDao;
import com.matridx.igams.production.service.svcinterface.IDdbxmxService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:JYK
 */
@Service
public class DdbxmxServiceImpl extends BaseBasicServiceImpl<DdbxmxDto, DdbxmxModel, IDdbxmxDao> implements IDdbxmxService {

    /**
     * 批量更新
     */
    public int updateList(List<DdbxmxDto> list){
        return dao.updateList(list);
    }

    /**
     * 批量新增
     */
    public int insertList(List<DdbxmxDto> list){
        return dao.insertList(list);
    }
}

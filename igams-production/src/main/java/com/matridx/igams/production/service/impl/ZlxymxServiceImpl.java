package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.ZlxymxDto;
import com.matridx.igams.production.dao.entities.ZlxymxModel;
import com.matridx.igams.production.dao.post.IZlxymxDao;
import com.matridx.igams.production.service.svcinterface.IZlxymxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author:JYK
 */
@Service
public class ZlxymxServiceImpl extends BaseBasicServiceImpl<ZlxymxDto, ZlxymxModel, IZlxymxDao> implements IZlxymxService {

    /**
     * 修改状态
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateZt(ZlxymxDto zlxymxDto){
        return dao.updateZt(zlxymxDto);
    }

    /**
     * 批量新增
     */
    public boolean insertList(List<ZlxymxDto> list){
        return dao.insertList(list);
    }

    /**
     * 直接删除
     */
    public void deleteInfo(ZlxymxDto zlxymxDto){
        dao.deleteInfo(zlxymxDto);
    }
}

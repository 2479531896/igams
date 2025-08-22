package com.matridx.igams.sample.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.sample.dao.entities.YbdbmxDto;
import com.matridx.igams.sample.dao.entities.YbdbmxModel;
import com.matridx.igams.sample.dao.post.IYbdbmxDao;
import com.matridx.igams.sample.service.svcinterface.IYbdbmxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class YbdbmxSerivceImpl extends BaseBasicServiceImpl<YbdbmxDto, YbdbmxModel, IYbdbmxDao> implements IYbdbmxService {

    /**
     * 获取样本调拨明细List
     * @param ybdbmxDto
     * @return
     */
    @Override
    public List<YbdbmxDto> getDbmxDtos(YbdbmxDto ybdbmxDto) {
        return dao.getDbmxDtos(ybdbmxDto);
    }

    /**
     * 检查是否可删除
     * @param ybdbmxDto
     * @return
     */
    @Override
    public List<String> checkCanDelete(YbdbmxDto ybdbmxDto){
        return dao.checkCanDelete(ybdbmxDto);
    }

    @Override
    public boolean updateYbdbmxDtos(List<YbdbmxDto> ybdbmxDtos) {
        return dao.updateYbdbmxDtos(ybdbmxDtos);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateList(List<YbdbmxDto> ybdbmxDtos) {
        return dao.updateList(ybdbmxDtos);
    }
}

package com.matridx.igams.warehouse.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.warehouse.dao.entities.GfjxmxDto;
import com.matridx.igams.warehouse.dao.entities.GfjxmxModel;
import com.matridx.igams.warehouse.dao.post.IGfjxmxDao;
import com.matridx.igams.warehouse.service.svcinterface.IGfjxmxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
@Service
public class GfjxmxServiceImpl extends BaseBasicServiceImpl<GfjxmxDto, GfjxmxModel, IGfjxmxDao> implements IGfjxmxService {
    @Override
    public List<GfjxmxDto> queryDtoList() {
        return dao.queryDtoList();
    }

    /**
     * @Description: 批量新增
     * @param gfjxmxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/7/3 17:33
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertGfjxmxDtos(List<GfjxmxDto> gfjxmxDtoList) {
        return dao.insertGfjxmxDtos(gfjxmxDtoList);
    }

    /**
     * @Description: 批量更新
     * @param gfjxmxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/7/4 13:54
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateGfjxmxDtos(List<GfjxmxDto> gfjxmxDtoList) {
        return dao.updateGfjxmxDtos(gfjxmxDtoList);
    }
}

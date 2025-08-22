package com.matridx.igams.warehouse.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.warehouse.dao.entities.GfyzmxDto;
import com.matridx.igams.warehouse.dao.entities.GfyzmxModel;
import com.matridx.igams.warehouse.dao.post.IGfyzmxDao;
import com.matridx.igams.warehouse.service.svcinterface.IGfyzmxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GfyzmxServiceImpl extends BaseBasicServiceImpl<GfyzmxDto, GfyzmxModel, IGfyzmxDao> implements IGfyzmxService {
    /**
     * @Description: 供方验证明细列表
     * @param gfyzmxDto
     * @return java.util.List<com.matridx.igams.warehouse.dao.entities.GfyzmxDto>
     * @Author: 郭祥杰
     * @Date: 2024/6/17 13:31
     */
    @Override
    public List<GfyzmxDto> getListByGfyzid(GfyzmxDto gfyzmxDto) {
        return dao.getListByGfyzid(gfyzmxDto);
    }

    /**
     * @Description: 批量新增
     * @param gfyzmxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/18 16:07
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertGfyzmxDtoList(List<GfyzmxDto> gfyzmxDtoList) {
        return dao.insertGfyzmxDtoList(gfyzmxDtoList);
    }

    /**
     * @Description: 批量修改
     * @param gfyzmxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/19 17:33
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateGfyzmxDtos(List<GfyzmxDto> gfyzmxDtoList) {
        return dao.updateGfyzmxDtos(gfyzmxDtoList);
    }
}

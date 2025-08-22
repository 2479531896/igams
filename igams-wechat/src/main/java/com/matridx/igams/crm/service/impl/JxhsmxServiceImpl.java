package com.matridx.igams.crm.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.crm.dao.entities.JxhsmxDto;
import com.matridx.igams.crm.dao.entities.JxhsmxModel;
import com.matridx.igams.crm.dao.post.IJxhsmxDao;
import com.matridx.igams.crm.service.svcinterface.IJxhsmxService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JxhsmxServiceImpl extends BaseBasicServiceImpl<JxhsmxDto, JxhsmxModel, IJxhsmxDao> implements IJxhsmxService{

    /**
     * 批量新增绩效核算明细
     * @param jxhsmxDtos
     * @return
     */
    @Override
    public boolean insertDtos(List<JxhsmxDto> jxhsmxDtos){
        int i = dao.insertDtos(jxhsmxDtos);
        return i == jxhsmxDtos.size();
    }

    /**
     * 批量删除绩效核算明细
     * @param jxhsmxDtos
     * @return
     */
    @Override
    public boolean deleteDtos(List<JxhsmxDto> jxhsmxDtos) {
        int i =  dao.deleteDtos(jxhsmxDtos);
        return i == jxhsmxDtos.size();
    }
    /**
     * 批量更新绩效核算明细
     * @param jxhsmxDtos
     * @return
     */
    @Override
    public boolean updateDtos(List<JxhsmxDto> jxhsmxDtos) {
        int i = dao.updateDtos(jxhsmxDtos);
        return i == jxhsmxDtos.size();
    }

}

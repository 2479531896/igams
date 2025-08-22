package com.matridx.igams.experiment.service.impl;


import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.KzglDto;
import com.matridx.igams.experiment.dao.entities.KzglModel;
import com.matridx.igams.experiment.dao.entities.KzmxDto;
import com.matridx.igams.experiment.dao.post.IKzglDao;
import com.matridx.igams.experiment.service.svcinterface.IKzglService;
import com.matridx.igams.experiment.service.svcinterface.IKzmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class KzglServiceImpl extends BaseBasicServiceImpl<KzglDto, KzglModel, IKzglDao> implements IKzglService {
    @Autowired
    private IKzmxService kzmxService;

    /**
     * 获取检测单位限制
     */
    public List<Map<String,String>> getJsjcdwByjsid(String jsid){
        return dao.getJsjcdwByjsid(jsid);
    }

    /**
     * 查询当天已建立的扩增数量
     */
    public KzglDto getCountByDay(KzglDto kzglDto){
        return dao.getCountByDay(kzglDto);
    }

    /**
     * 根据扩增名称查询扩增信息
     */
    public KzglDto getDtoByKzmc(KzglDto kzglDto){
        return dao.getDtoByKzmc(kzglDto);
    }

    /**
     * 删除
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deleteDto(KzglDto kzglDto){
        KzmxDto kzmxDto=new KzmxDto();
        kzmxDto.setScry(kzglDto.getScry());
        kzmxDto.setIds(kzglDto.getIds());
        KzmxDto kzmxDto_t=new KzmxDto();
        kzmxDto_t.setIds(kzglDto.getIds());
        List<KzmxDto> dtoList = kzmxService.getDtoList(kzmxDto_t);
        List<String> syglids=new ArrayList<>();
        if(dtoList!=null&&dtoList.size()>0){
            for(KzmxDto dto:dtoList){
                if(StringUtil.isNotBlank(dto.getSyglid())){
                    syglids.add(dto.getSyglid());
                }
            }
        }
        if(syglids.size() > 0){
            kzmxDto_t.setIds(syglids);
            kzmxService.removeKzsjByIds(kzmxDto_t);
        }
        boolean delete = kzmxService.delete(kzmxDto);
        if(!delete){
            return false;
        }
        delete = dao.delete(kzglDto)>0;
        return delete;
    }
}

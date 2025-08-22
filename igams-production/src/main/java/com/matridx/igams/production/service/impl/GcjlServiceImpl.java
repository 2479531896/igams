package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.GcjlDto;
import com.matridx.igams.production.dao.entities.GcjlModel;
import com.matridx.igams.production.dao.post.IGcjlDao;
import com.matridx.igams.production.service.svcinterface.IGcjlService;
import com.matridx.igams.storehouse.dao.entities.LykcxxDto;
import com.matridx.igams.storehouse.service.svcinterface.ILykcxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:JYK
 */
@Service
public class GcjlServiceImpl extends BaseBasicServiceImpl<GcjlDto, GcjlModel, IGcjlDao> implements IGcjlService {
    @Autowired
    ILykcxxService lykcxxService;
    @Override
    public List<GcjlDto> getDtolistGcSampleStock(GcjlDto gcjlDto) {
        return dao.getDtolistGcSampleStock(gcjlDto);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean inspectionrecordsSavePage(GcjlDto gcjlDto) throws BusinessException {
        List<GcjlDto> addGcjlDtos = new ArrayList<>();
        for (String lykcid : gcjlDto.getIds()) {
            GcjlDto gcjlDto_add = new GcjlDto();
            gcjlDto_add.setGcjlid(StringUtil.generateUUID());
            gcjlDto_add.setLrry(gcjlDto.getLrry());
            gcjlDto_add.setLykcid(lykcid);
            gcjlDto_add.setGcyq(gcjlDto.getGcyq());
            gcjlDto_add.setGcjg(gcjlDto.getGcjg());
            gcjlDto_add.setBz(gcjlDto.getBz());
            addGcjlDtos.add(gcjlDto_add);
        }
        boolean isSuccess = dao.insertGcjlDtos(addGcjlDtos);
        if (!isSuccess){
            throw new BusinessException("msg", "保存观察记录失败!");
        }
        LykcxxDto lykcxxDto = new LykcxxDto();
        lykcxxDto.setIds(gcjlDto.getIds());
        lykcxxDto.setScgcrq(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
        isSuccess = lykcxxService.updateScgcrq(lykcxxDto);
        if (!isSuccess){
            throw new BusinessException("msg", "修改上次观察日期失败!");
        }
        return true;
    }
}

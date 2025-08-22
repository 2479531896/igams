package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.dao.entities.TsclqkDto;
import com.matridx.igams.common.dao.entities.TsclqkModel;
import com.matridx.igams.common.dao.entities.ZltsglDto;
import com.matridx.igams.common.dao.post.ITsclqkDao;
import com.matridx.igams.common.dao.post.IZltsglDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.ITsclqkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TsclqkServiceImpl extends BaseBasicServiceImpl<TsclqkDto, TsclqkModel, ITsclqkDao> implements ITsclqkService {

    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    IZltsglDao zltsglDao;

    @Override
    public boolean insertList(List<TsclqkDto> list) {
        return dao.insertList(list);
    }

    @Override
    public boolean revertData(TsclqkDto tsclqkDto) {
        return dao.revertData(tsclqkDto);
    }

    @Override
    public List<TsclqkDto> getPagedListByBm(TsclqkDto tsclqkDto) {
        return dao.getPagedListByBm(tsclqkDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean disposalSaveQualityComplaints(TsclqkDto tsclqkDto) {
        boolean update = dao.dealData(tsclqkDto);
        if(!update){
            return false;
        }
        if(tsclqkDto.getFjids()!=null && !tsclqkDto.getFjids().isEmpty()){
            for (int i = 0; i < tsclqkDto.getFjids().size(); i++) {
                boolean saveFile = fjcfbService.save2RealFile(tsclqkDto.getFjids().get(i),tsclqkDto.getTsclqkid());
                if(!saveFile)
                    return false;
            }
        }
        ZltsglDto zltsglDto=new ZltsglDto();
        zltsglDto.setXgry(tsclqkDto.getClry());
        zltsglDto.setZltsid(tsclqkDto.getZltsid());
        int result = zltsglDao.update(zltsglDto);
        if(result==0){
            return false;
        }
        return true;
    }

    /**
     * 获取部门集合
     */
    @Override
    public List<TsclqkDto> getDepartmentList(TsclqkDto tsclqkDto){
        return dao.getDepartmentList(tsclqkDto);
    }

    /**
     * 批量修改
     */
    @Override
    public boolean updateForConclusion(List<TsclqkDto> list){
        return dao.updateForConclusion(list);
    }
}

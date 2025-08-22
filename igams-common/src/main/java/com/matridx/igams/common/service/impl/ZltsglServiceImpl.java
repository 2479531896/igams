package com.matridx.igams.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.SjycDto;
import com.matridx.igams.common.dao.entities.TsclqkDto;
import com.matridx.igams.common.dao.entities.ZltsglDto;
import com.matridx.igams.common.dao.entities.ZltsglModel;
import com.matridx.igams.common.dao.post.IZltsglDao;
import com.matridx.igams.common.enums.QualityComplaintsEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.ISjycService;
import com.matridx.igams.common.service.svcinterface.ITsclqkService;
import com.matridx.igams.common.service.svcinterface.IZltsglService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ZltsglServiceImpl extends BaseBasicServiceImpl<ZltsglDto, ZltsglModel, IZltsglDao> implements IZltsglService {
    @Autowired
    private ITsclqkService tsclqkService;
    @Autowired
    private ISjycService sjycService;
    @Autowired
    IFjcfbService fjcfbService;


    @Override
    public ZltsglDto getInspectionInfo(ZltsglDto zltsglDto) {
        return dao.getInspectionInfo(zltsglDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addSaveQualityComplaints(ZltsglDto zltsglDto) {
        zltsglDto.setZt(StatusEnum.CHECK_NO.getCode());
        zltsglDto.setZltsid(StringUtil.generateUUID());
        if(zltsglDto.getFjids()!=null && !zltsglDto.getFjids().isEmpty()){
            for (int i = 0; i < zltsglDto.getFjids().size(); i++) {
                boolean saveFile = fjcfbService.save2RealFile(zltsglDto.getFjids().get(i),zltsglDto.getZltsid());
                if(!saveFile)
                   return false;
            }
        }
        List<String> bms = zltsglDto.getBms();
        if(bms!=null&&!bms.isEmpty()){
            zltsglDto.setXdcs(String.valueOf(bms.size()));
            List<TsclqkDto> tsclqkDtos=new ArrayList<>();
            for(String bmid:bms){
                TsclqkDto tsclqkDto=new TsclqkDto();
                tsclqkDto.setTsclqkid(StringUtil.generateUUID());
                tsclqkDto.setZltsid(zltsglDto.getZltsid());
                tsclqkDto.setBmid(bmid);
                tsclqkDto.setLx("1");
                tsclqkDto.setLrry(zltsglDto.getLrry());
                tsclqkDtos.add(tsclqkDto);
            }
            boolean insertList = tsclqkService.insertList(tsclqkDtos);
            if(!insertList){
                return false;
            }
        }
        if(QualityComplaintsEnum.COMPLAINTS_EXCEPTION.getCode().equals(zltsglDto.getYwlx())){
            SjycDto sjycDto=new SjycDto();
            sjycDto.setTsbj("1");
            sjycDto.setIds(zltsglDto.getYwid());
            sjycDto.setXgry(zltsglDto.getLrry());
            boolean updateDto = sjycService.updateTsbj(sjycDto);
            if(!updateDto){
                return false;
            }
        }
        int insert = dao.insert(zltsglDto);
        if(insert==0){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean modSaveQualityComplaints(ZltsglDto zltsglDto) {
        if(zltsglDto.getFjids()!=null && !zltsglDto.getFjids().isEmpty()){
            for (int i = 0; i < zltsglDto.getFjids().size(); i++) {
                boolean saveFile = fjcfbService.save2RealFile(zltsglDto.getFjids().get(i),zltsglDto.getZltsid());
                if(!saveFile)
                    return false;
            }
        }
        TsclqkDto tsclqkDto=new TsclqkDto();
        tsclqkDto.setZltsid(zltsglDto.getZltsid());
        List<TsclqkDto> dtoList = tsclqkService.getDtoList(tsclqkDto);
        tsclqkDto.setScry(zltsglDto.getXgry());
        tsclqkDto.setIds(zltsglDto.getZltsid());
        tsclqkService.delete(tsclqkDto);
        List<String> ids = new ArrayList<>();
        List<String> bms = zltsglDto.getBms();
        if(bms!=null&&!bms.isEmpty()){
            List<TsclqkDto> tsclqkDtos=new ArrayList<>();
            for(String bmid:bms){
                boolean isFind=false;
                if(dtoList!=null&&!dtoList.isEmpty()){
                    for(TsclqkDto dto:dtoList){
                        if(bmid.equals(dto.getBmid())){
                            ids.add(dto.getTsclqkid());
                            isFind=true;
                            break;
                        }
                    }
                }
                if(!isFind){
                    TsclqkDto tsclqkDto_t=new TsclqkDto();
                    tsclqkDto_t.setTsclqkid(StringUtil.generateUUID());
                    tsclqkDto_t.setZltsid(zltsglDto.getZltsid());
                    tsclqkDto_t.setBmid(bmid);
                    tsclqkDto_t.setLx("1");
                    tsclqkDto_t.setLrry(zltsglDto.getXgry());
                    tsclqkDtos.add(tsclqkDto_t);
                }
            }
            if(!tsclqkDtos.isEmpty()){
                boolean insertList = tsclqkService.insertList(tsclqkDtos);
                if(!insertList){
                    return false;
                }
            }
            if(!ids.isEmpty()){
                tsclqkDto.setIds(ids);
                tsclqkDto.setXgry(zltsglDto.getXgry());
                boolean revertData = tsclqkService.revertData(tsclqkDto);
                if(!revertData){
                    return false;
                }
            }
        }
        int update = dao.update(zltsglDto);
        if(update==0){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delQualityComplaints(ZltsglDto zltsglDto) {
        int delete = dao.delete(zltsglDto);
        if(delete==0){
            return false;
        }
        TsclqkDto tsclqkDto=new TsclqkDto();
        tsclqkDto.setIds(zltsglDto.getIds());
        tsclqkDto.setScry(zltsglDto.getScry());
        tsclqkService.delete(tsclqkDto);
        //根据ywids获取其他关联的质量投诉，判断每个被删除的投诉所关联的异常是否有其他关联的投诉，若没有则将tsbj更新为0
        zltsglDto.setYwlx(QualityComplaintsEnum.COMPLAINTS_EXCEPTION.getCode());
        List<ZltsglDto> dtoList = dao.getDtoList(zltsglDto);
        List<String> ids=new ArrayList<>();
        if(dtoList!=null&&!dtoList.isEmpty()){
            List<String> ywids = zltsglDto.getYwids();
            for(String ywid:ywids){
                boolean isFind=false;
                for(ZltsglDto dto:dtoList){
                    if(ywid.equals(dto.getYwid())){
                        isFind=true;
                        break;
                    }
                }
                if(!isFind){
                    ids.add(ywid);
                }
            }
        }else{
            ids.addAll(zltsglDto.getYwids());
        }
        if(!ids.isEmpty()){
            SjycDto sjycDto=new SjycDto();
            sjycDto.setTsbj("0");
            sjycDto.setIds(ids);
            sjycDto.setXgry(zltsglDto.getLrry());
            sjycService.updateTsbj(sjycDto);
        }
        return true;
    }

    @Override
    public List<ZltsglDto> getItemSelectionInfo(ZltsglDto zltsglDto) {
        return dao.getItemSelectionInfo(zltsglDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean concludeSaveQualityComplaints(ZltsglDto zltsglDto) {
        TsclqkDto tsclqkDto=new TsclqkDto();
        tsclqkDto.setScry(zltsglDto.getXgry());
        tsclqkDto.setIds(zltsglDto.getZltsid());
        String[] strArray = {"0", "2"};
        tsclqkDto.setLxs(strArray);
        tsclqkService.delete(tsclqkDto);
        List<TsclqkDto> list = JSON.parseArray(zltsglDto.getClqk_json(), TsclqkDto.class);
        if(!CollectionUtils.isEmpty(list)){
            List<TsclqkDto> insertList=new ArrayList<>();
            List<TsclqkDto> updateList=new ArrayList<>();
            for(TsclqkDto dto:list){
                if(StringUtil.isNotBlank(dto.getTsclqkid())){
                    dto.setXgry(zltsglDto.getXgry());
                    updateList.add(dto);
                }else{
                    dto.setTsclqkid(StringUtil.generateUUID());
                    dto.setZltsid(zltsglDto.getZltsid());
                    dto.setLrry(zltsglDto.getXgry());
                    insertList.add(dto);
                }

            }
            if(!CollectionUtils.isEmpty(insertList)){
                boolean result = tsclqkService.insertList(insertList);
                if(!result){
                    return false;
                }
            }
            if(!CollectionUtils.isEmpty(updateList)){
                boolean result = tsclqkService.updateForConclusion(updateList);
                if(!result){
                    return false;
                }
            }
        }
        boolean conclude = dao.conclude(zltsglDto);
        if(!conclude){
            return false;
        }
        return true;
    }
}

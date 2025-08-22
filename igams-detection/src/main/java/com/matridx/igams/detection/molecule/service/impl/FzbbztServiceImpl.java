package com.matridx.igams.detection.molecule.service.impl;

import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.detection.molecule.dao.entities.FzbbztDto;
import com.matridx.igams.detection.molecule.dao.entities.FzbbztModel;
import com.matridx.igams.detection.molecule.dao.post.IFzbbztDao;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzbbztService;

import java.util.ArrayList;
import java.util.List;

@Service
public class FzbbztServiceImpl extends BaseBasicServiceImpl<FzbbztDto, FzbbztModel, IFzbbztDao> implements IFzbbztService{

    /**
     * 新增普检标本状态
     */
    @Override
    public boolean insertFzbbzt(FzjcxxDto fzjcxxDto) {
        dao.deleteByFzjcid(fzjcxxDto.getFzjcid());
        List<String> bbzts=fzjcxxDto.getPjbbzts();
        if(bbzts!=null && bbzts.size()>0) {
            List<FzbbztDto> fzbbztDtoList=new ArrayList<>();
            for (int i = 0; i < bbzts.size(); i++){
                FzbbztDto fzbbztDto=new FzbbztDto();
                fzbbztDto.setFzjcid(fzjcxxDto.getFzjcid());
                fzbbztDto.setXh(String.valueOf(i+1));
                fzbbztDto.setZt(bbzts.get(i));
                fzbbztDtoList.add(fzbbztDto);
            }
            dao.insertFzbbzt(fzbbztDtoList);
        }
        return true;
    }
}

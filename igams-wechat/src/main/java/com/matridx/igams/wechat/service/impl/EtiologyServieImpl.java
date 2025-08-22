package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.EtiologyDto;
import com.matridx.igams.wechat.dao.entities.EtiologyModel;
import com.matridx.igams.wechat.dao.post.IEtiologyDao;
import com.matridx.igams.wechat.service.svcinterface.IEtiologyServie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
public class EtiologyServieImpl extends BaseBasicServiceImpl<EtiologyDto, EtiologyModel, IEtiologyDao> implements IEtiologyServie {

    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void insertEtiology(Map<String,Object>map){
            String type=map.get("type").toString();

            EtiologyDto dto=new EtiologyDto();
            dto.setLx(type);
            dao.delBylx(dto);
            if("MONTH_ETIOLOGY".equals(type)){
                String zqkssj=map.get("zqkssj").toString();
                String zqjssj=map.get("zqjssj").toString();
                dto.setZqkssj(zqkssj);
                dto.setZqjssj(zqjssj);
                dao.insert(dto);
                EtiologyDto dto1=new EtiologyDto();
                dto.setLx("TOTAL_SD_MEAN");
                dao.delBylx(dto1);
                dao.insertList(dto);
            }else if("DAY_ETIOLOGY".equals(type)){
                dao.insertByDay(dto);
            }
    }


    @Override
    public List<EtiologyDto> getYjByCxlx(EtiologyDto etiologyDto) {
        return dao.getYjByCxlx(etiologyDto);
    }
}

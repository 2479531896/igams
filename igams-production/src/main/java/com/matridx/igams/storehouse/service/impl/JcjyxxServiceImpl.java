package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.entities.JcjymxDto;
import com.matridx.igams.storehouse.dao.entities.JcjyxxDto;
import com.matridx.igams.storehouse.dao.entities.JcjyxxModel;
import com.matridx.igams.storehouse.dao.post.IJcjyxxDao;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IJcjymxService;
import com.matridx.igams.storehouse.service.svcinterface.IJcjyxxService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class JcjyxxServiceImpl extends BaseBasicServiceImpl<JcjyxxDto, JcjyxxModel, IJcjyxxDao> implements IJcjyxxService {

    @Autowired
    private IHwxxService hwxxService;
    @Autowired
    private IJcjymxService jcjymxService;
    @Override
    public List<JcjyxxDto> getDtoListInfo(JcjyxxDto jcjyxxDto) {
        return dao.getDtoListInfo(jcjyxxDto);
    }

    @Override
    public List<JcjyxxDto> getDtoListInfoAndMx(JcjyxxDto jcjyxxDto) {
        List<JcjyxxDto> jcjyxxDtos =  dao.getDtoListInfo(jcjyxxDto);
        JcjymxDto jcjymxDto = new JcjymxDto();
        jcjymxDto.setJcjyid(jcjyxxDto.getJcjyid());
        List<JcjymxDto> jcjymxDtos = jcjymxService.getDtoList(jcjymxDto);
        for (JcjyxxDto jcjyxxDto1:jcjyxxDtos) {
            Double total = 0.00;
            HwxxDto hwxxDto = new HwxxDto();
            hwxxDto.setWlid(jcjyxxDto1.getWlid());
            hwxxDto.setCkqxlx(jcjyxxDto1.getCkqxlx());
            hwxxDto.setCkid(jcjyxxDto1.getCkid());
            List<HwxxDto> hwxxDtos=hwxxService.queryHWxx(hwxxDto,"1");
            List<HwxxDto> hwxxDtoList = new ArrayList<>();
            if(!CollectionUtils.isEmpty(jcjymxDtos)) {
                for (JcjymxDto jcjymxDto1:jcjymxDtos) {
                    if (jcjymxDto1.getJyxxid().equals(jcjyxxDto1.getJyxxid())){
                        if(!CollectionUtils.isEmpty(hwxxDtos)) {
                            boolean bo = true;
                            for (HwxxDto hwxxDto1:hwxxDtos) {
                                if (jcjymxDto1.getHwid().equals(hwxxDto1.getHwid())){
                                    hwxxDto1.setKlsl(String.valueOf(Double.parseDouble(hwxxDto1.getKlsl()) + Double.parseDouble(jcjymxDto1.getJysl())));
                                    hwxxDto1.setYxsl(jcjymxDto1.getJysl());
                                    hwxxDto1.setQyxsl(jcjymxDto1.getJysl());
                                    total += Double.parseDouble(jcjymxDto1.getJysl());
                                    bo = false;
                                }
                            }
                            if (bo){
                                HwxxDto dto = hwxxService.getDtoById(jcjymxDto1.getHwid());
                                dto.setKlsl(String.valueOf(Double.parseDouble(dto.getKlsl()) + Double.parseDouble(jcjymxDto1.getJysl())));
                                dto.setYxsl(jcjymxDto1.getJysl());
                                dto.setQyxsl(jcjymxDto1.getJysl());
                                total += Double.parseDouble(jcjymxDto1.getJysl());
                                hwxxDtos.add(dto);
                            }
                        }else{
                            HwxxDto dto = hwxxService.getDtoById(jcjymxDto1.getHwid());
                            dto.setKlsl(String.valueOf(Double.parseDouble(dto.getKlsl()) + Double.parseDouble(jcjymxDto1.getJysl())));
                            dto.setYxsl(jcjymxDto1.getJysl());
                            dto.setQyxsl(jcjymxDto1.getJysl());
                            total += Double.parseDouble(jcjymxDto1.getJysl());
                            hwxxDtoList.add(dto);
                        }
                    }

                }

            }
            jcjyxxDto1.setZyxsl(String.valueOf(total));
            jcjyxxDto1.setHwxxDtos(hwxxDtos);
            jcjyxxDto1.getHwxxDtos().addAll(hwxxDtoList);
        }
        return jcjyxxDtos;
    }

    @Override
    public boolean deleteByIds(List<String> ids) {
        return dao.deleteByIds(ids);
    }

    @Override
    public List<JcjyxxDto> getDtoListGroupByJyxx(JcjyxxDto jcjyxxDto) {
        return dao.getDtoListGroupByJyxx(jcjyxxDto);
    }
}

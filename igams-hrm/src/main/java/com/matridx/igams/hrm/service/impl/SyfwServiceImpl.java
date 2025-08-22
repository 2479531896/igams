package com.matridx.igams.hrm.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.hrm.dao.entities.SyfwDto;
import com.matridx.igams.hrm.dao.entities.SyfwModel;
import com.matridx.igams.hrm.dao.post.ISyfwDao;
import com.matridx.igams.hrm.service.svcinterface.ISyfwService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author:JYK
 */
@Service
public class SyfwServiceImpl extends BaseBasicServiceImpl<SyfwDto, SyfwModel, ISyfwDao> implements ISyfwService {

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveSyfw(SyfwDto syfwDto) throws BusinessException {
        if (StringUtil.isNotBlank(syfwDto.getSyfw_json())){
            List<SyfwDto> syfwDtos = JSON.parseArray(syfwDto.getSyfw_json(), SyfwDto.class);
            if (!CollectionUtils.isEmpty(syfwDtos)){
                SyfwDto syfwDto_s = new SyfwDto();
                syfwDto_s.setJxmbid(syfwDto.getJxmbid());
                List<SyfwDto> dtoList = dao.getDtoListByJxmbid(syfwDto_s);
                List<SyfwDto> lsList = new ArrayList<>(syfwDtos);
                if (!CollectionUtils.isEmpty(dtoList)){
                    Iterator<SyfwDto> iterator = syfwDtos.iterator();
                    //剔除已有的
                    while (iterator.hasNext()){
                        SyfwDto next = iterator.next();
                        for (SyfwDto dto : dtoList) {
                            if (next.getYhid().equals(dto.getYhid())){
                                iterator.remove();
                            }
                        }
                    }
                    Iterator<SyfwDto> yIterator = dtoList.iterator();
                    while (yIterator.hasNext()){
                        SyfwDto next = yIterator.next();
                        for (SyfwDto dto : lsList) {
                            if (next.getYhid().equals(dto.getYhid())){
                                yIterator.remove();
                            }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(syfwDtos)){
                    for (SyfwDto dto : syfwDtos) {
                        dto.setJxmbid(syfwDto.getJxmbid());
                        dto.setSyfwid(StringUtil.generateUUID());
                        dto.setZt("0");
                    }
                    boolean isSuccess = dao.insertSyfwDtos(syfwDtos);
                    if (!isSuccess){
                        throw new BusinessException("msg","保存适用范围信息失败!");
                    }
                }
                if (!CollectionUtils.isEmpty(dtoList)){
                    SyfwDto syfwDto_del = new SyfwDto();
                    syfwDto_del.setJxmbid(syfwDto.getJxmbid());
                    List<String> yhids = new ArrayList<>();
                    for (SyfwDto dto : dtoList) {
                        yhids.add(dto.getYhid());
                    }
                    syfwDto_del.setYhids(yhids);
                    boolean isSuccess = dao.deleteByYhids(syfwDto_del);
                    if (!isSuccess){
                        throw new BusinessException("msg","删除适用范围信息失败!");
                    }
                }
            }else {
                boolean isSuccess = deleteById(syfwDto.getJxmbid());
                if (!isSuccess){
                    throw new BusinessException("msg","删除适用范围失败!");
                }
            }
        }else {
            boolean isSuccess = deleteById(syfwDto.getJxmbid());
            if (!isSuccess){
                throw new BusinessException("msg","删除适用范围失败!");
            }
        }
        return true;
    }

    @Override
    public boolean insertSyfwDtos(List<SyfwDto> syfwDtos) {
        return dao.insertSyfwDtos(syfwDtos);
    }

    @Override
    public boolean updateSyfwDtos(List<SyfwDto> syfwDtos) {
        return dao.updateSyfwDtos(syfwDtos);
    }

    @Override
    public List<SyfwDto> getDtoListByJxmbid(SyfwDto syfwDto) {
        return dao.getDtoListByJxmbid(syfwDto);
    }

    @Override
    public List<SyfwDto> getPagedDtoListByJxmbid(SyfwDto syfwDto) {
        return dao.getPagedDtoListByJxmbid(syfwDto);
    }

    @Override
    public void deleteByJxmbids(SyfwDto syfwDto) {
        dao.deleteByJxmbids(syfwDto);
    }
}

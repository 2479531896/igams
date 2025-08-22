package com.matridx.crf.web.service.impl;

import com.matridx.crf.web.dao.entities.*;
import com.matridx.crf.web.dao.post.IHbpdrxxjlDao;
import com.matridx.crf.web.service.svcinterface.IHbpdrxxjlService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.crf.web.dao.post.IHbphzxxDao;
import com.matridx.crf.web.service.svcinterface.IHbphzxxService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;

@Service
public class HbphzxxServiceImpl extends BaseBasicServiceImpl<HbphzxxDto, HbphzxxModel, IHbphzxxDao> implements IHbphzxxService{

    private Logger log = LoggerFactory.getLogger(QmngshzxxServiceImpl.class);
    @Autowired
    IHbpdrxxjlDao hbpdrxxjlDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public boolean insertHbpHzxx(HbphzxxDto hbphzxxDto, BeanHbpxxForms beanHbpxxForms) {
//        try {
            boolean result = false;
            //先保存Dto
            hbphzxxDto.setScbj("0");
            hbphzxxDto.setHbphzxxid(StringUtil.generateUUID());
            //保存记录信息
            HbpdrxxjlDto one = beanHbpxxForms.getHbpdrxxjlDtoOne();
            HbpdrxxjlDto two = beanHbpxxForms.getHbpdrxxjlDtoTwo();
            HbpdrxxjlDto three = beanHbpxxForms.getHbpdrxxjlDtoThree();
            HbpdrxxjlDto four = beanHbpxxForms.getHbpdrxxjlDtoFour();
            HbpdrxxjlDto five = beanHbpxxForms.getHbpdrxxjlDtoFive();
            if(StringUtil.isBlank(one.getHbpjlid())){
                one.setHbpjlid(StringUtil.generateUUID());
                one.setJldjt("1");
                one.setHbphzxxid(hbphzxxDto.getHbphzxxid());
                hbpdrxxjlDao.insertDto(one);
            }
            if(StringUtil.isBlank(two.getHbpjlid())){
                two.setHbpjlid(StringUtil.generateUUID());
                two.setJldjt("2");
                two.setHbphzxxid(hbphzxxDto.getHbphzxxid());
                hbpdrxxjlDao.insertDto(two);
            }
            if(StringUtil.isBlank(three.getHbpjlid())){
                three.setHbpjlid(StringUtil.generateUUID());
                three.setJldjt("3");
                three.setHbphzxxid(hbphzxxDto.getHbphzxxid());
                hbpdrxxjlDao.insertDto(three);
            }
            if(StringUtil.isBlank(four.getHbpjlid())){
                four.setHbpjlid(StringUtil.generateUUID());
                four.setJldjt("4");
                four.setHbphzxxid(hbphzxxDto.getHbphzxxid());
                hbpdrxxjlDao.insertDto(four);
            }
            if(StringUtil.isBlank(five.getHbpjlid())){
                five.setHbpjlid(StringUtil.generateUUID());
                five.setJldjt("5");
                five.setHbphzxxid(hbphzxxDto.getHbphzxxid());
                hbpdrxxjlDao.insertDto(five);
            }
            dao.insert(hbphzxxDto);
            return true;
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return false;
//        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public boolean updateHbpHzxx(HbphzxxDto hbphzxxDto, BeanHbpxxForms beanHbpxxForms) {
        HbpdrxxjlDto one = beanHbpxxForms.getHbpdrxxjlDtoOne();
        HbpdrxxjlDto two = beanHbpxxForms.getHbpdrxxjlDtoTwo();
        HbpdrxxjlDto three = beanHbpxxForms.getHbpdrxxjlDtoThree();
        HbpdrxxjlDto four = beanHbpxxForms.getHbpdrxxjlDtoFour();
        HbpdrxxjlDto five = beanHbpxxForms.getHbpdrxxjlDtoFive();
        if (one != null){
            one.setJldjt("1");
            one.setHbphzxxid(hbphzxxDto.getHbphzxxid());
            hbpdrxxjlDao.updateDto(one);
        }
        if (two != null){
            two.setJldjt("2");
            two.setHbphzxxid(hbphzxxDto.getHbphzxxid());
            hbpdrxxjlDao.updateDto(two);
        }
        if (three != null){
            three.setJldjt("3");
            three.setHbphzxxid(hbphzxxDto.getHbphzxxid());
            hbpdrxxjlDao.updateDto(three);
        }
        if (four != null){
            four.setJldjt("4");
            four.setHbphzxxid(hbphzxxDto.getHbphzxxid());
            hbpdrxxjlDao.updateDto(four);
        }
        if (five != null){
            five.setJldjt("5");
            five.setHbphzxxid(hbphzxxDto.getHbphzxxid());
            hbpdrxxjlDao.updateDto(five);
        }
        dao.updateDto(hbphzxxDto);
        return true;
    }

}

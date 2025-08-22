package com.matridx.bioinformation.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.bioinformation.dao.entities.*;
import com.matridx.bioinformation.service.svcinterface.IJxcmdService;
import com.matridx.bioinformation.service.svcinterface.IRwglService;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.bioinformation.dao.post.IJxglDao;
import com.matridx.bioinformation.service.svcinterface.IJxglService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JxglServiceImpl extends BaseBasicServiceImpl<JxglDto, JxglModel, IJxglDao> implements IJxglService{
    @Autowired
    IJxcmdService jxcmdService;
    @Autowired
    IRwglService rwglService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean updateJxInfo(JxglDto jxglDto) throws BusinessException {
        RwglDto rwglDto = new RwglDto();
        rwglDto.setJxid(jxglDto.getJxid());
        List<RwglDto> infoList = rwglService.getInfoList(rwglDto);
        if (infoList != null && infoList.size()>0) {
            throw new BusinessException("msg","该应用下存在任务，不允许删除！");
        }
        boolean success = dao.updateDto(jxglDto)!=0;
        if (!success){
            throw new BusinessException("msg","应用管理删除失败！");
        }
        JxcmdDto jxcmdDto = new JxcmdDto();
        jxcmdDto.setYwid(jxglDto.getJxid());
        jxcmdDto.setScry(jxglDto.getScry());
        jxcmdService.updateDto(jxcmdDto);
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String insertJxInfo(JxglDto jxglDto) throws BusinessException {
        JxglDto jxglDto1 = dao.getDto(jxglDto);
        if (jxglDto1 != null){
            throw new BusinessException("mes","应用名重复！");
        }
        jxglDto.setJxid(StringUtil.generateUUID());
//        if (StringUtil.isNotBlank(jxglDto.getWjlj())){
//            try {
//                jxglDto.setWjlj( URLEncoder.encode(jxglDto.getWjlj(),"utf-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
        boolean success = dao.insertDto(jxglDto)!=0;
        if (!success){
            throw new BusinessException("mes","应用新增失败！");
        }
        List<JxcmdModel> jxcmdModels =  (List<JxcmdModel>)JSONObject.parseArray(jxglDto.getArgs(), JxcmdModel.class);
        if (null != jxcmdModels && jxcmdModels.size() >0){
            for (JxcmdModel jxcmdModel:jxcmdModels) {
                jxcmdModel.setJxcmdid(StringUtil.generateUUID());
                jxcmdModel.setYwid(jxglDto.getJxid());
                jxcmdModel.setLrry(jxglDto.getLrry());
            }
            success = jxcmdService.batchInsert(jxcmdModels);
            if (!success){
                throw new BusinessException("mes","应用明细新增失败！");
            }
        }

        return jxglDto.getJxid();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean saveJxInfo(JxglDto jxglDto) throws BusinessException {
//        if (StringUtil.isNotBlank(jxglDto.getWjlj())){
//            try {
//                jxglDto.setWjlj( URLEncoder.encode(jxglDto.getWjlj(),"utf-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
        boolean success = dao.update(jxglDto)!=0;
        if (!success){
            throw new BusinessException("mes","应用管理修改失败！");
        }

        JxcmdDto jxcmdDto = new JxcmdDto();
        jxcmdDto.setYwid(jxglDto.getJxid());
        jxcmdDto.setScry(jxglDto.getXgry());
        jxcmdService.updateDto(jxcmdDto);
        List<JxcmdModel> jxcmdModels =  (List<JxcmdModel>)JSONObject.parseArray(jxglDto.getArgs(), JxcmdModel.class);
        if (null != jxcmdModels && jxcmdModels.size() >0){
            for (JxcmdModel model:jxcmdModels) {
                model.setJxcmdid(StringUtil.generateUUID());
                model.setYwid(jxglDto.getJxid());
                model.setLrry(jxglDto.getXgry());
            }
            success = jxcmdService.batchInsert(jxcmdModels);
            if (!success){
                throw new BusinessException("mes","应用明细修改失败！");
            }
        }
        return true;
    }
}

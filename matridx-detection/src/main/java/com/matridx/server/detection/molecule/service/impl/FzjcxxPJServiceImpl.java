package com.matridx.server.detection.molecule.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.detection.molecule.dao.entities.FzbbztDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcjgDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxxModel;
import com.matridx.server.detection.molecule.dao.post.IFzjcxxPJDao;
import com.matridx.server.detection.molecule.enums.DetMQTypeEnum;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcjgPJService;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxmPJService;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxxPJService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FzjcxxPJServiceImpl extends BaseBasicServiceImpl<FzjcxxDto, FzjcxxModel, IFzjcxxPJDao> implements IFzjcxxPJService {
    @Autowired
    private IFzjcxmPJService fzjcxmPJService;
    @Autowired(required = false)
    private AmqpTemplate amqpTempl;
    @Autowired
    IFzjcjgPJService fzjcjgPJService;
    /**
     * 列表查询
     * @param map
     * @return
     */
    public List<FzjcxxDto> getListWithMap(Map<String, Object> map){
        return dao.getListWithMap(map);
    }

    /**
     * 保存普检信息
     * @param fzjcxxDto
     * @return
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveFzjcxx(FzjcxxDto fzjcxxDto) throws BusinessException {
        fzjcxxDto.setBbzbh(fzjcxxDto.getYbbh());
        fzjcxxDto.setWybh(fzjcxxDto.getYbbh());
        if (StringUtil.isNotBlank(fzjcxxDto.getFzjcid())){
            //修改保存
            List<FzjcxxDto> existByYbbh = dao.getExistByYbbh(fzjcxxDto);
            if (existByYbbh.size()>0){
                throw new BusinessException("该样本编号已存在！");
            }
            FzjcxxDto dto = dao.getDto(fzjcxxDto);
            if (dto!=null && StringUtil.isNotBlank(dto.getJssj())){
                throw new BusinessException("该样本已被接收，不可修改！");
            }
            int result = dao.saveEditDto(fzjcxxDto);
            if (result<=0){
                return false;
            }
            List<FzjcxmDto> fzjcxmDtos = (List<FzjcxmDto>) JSON.parseArray(fzjcxxDto.getJcxm(), FzjcxmDto.class);
            List<FzjcxmDto> list = new ArrayList<>();
            for (FzjcxmDto fzjcxmDto : fzjcxmDtos) {
                fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
                if (StringUtil.isNotBlank(fzjcxmDto.getFzxmid())){
                    list.add(fzjcxmDto);
                }else {
                    fzjcxmDto.setFzxmid(StringUtil.generateUUID());
                    list.add(fzjcxmDto);
                }
            }
            //先删除向目标，再新增项目信息
            FzjcxmDto fzjcxmDto = new FzjcxmDto();
            fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
            fzjcxmPJService.delFzjcxmByFzjcid(fzjcxmDto);
            fzjcxmPJService.insertList(list);
            fzjcxxDto.setJcxm(JSON.toJSONString(list));
        }else {
            //新增保存
            fzjcxxDto.setFzjcid(StringUtil.generateUUID());
            List<FzjcxxDto> existByYbbh = dao.getExistByYbbh(fzjcxxDto);
            if (existByYbbh.size()>0){
                throw new BusinessException("该样本编号已存在！");
            }
            fzjcxxDto.setLrry(fzjcxxDto.getWxid());
            int result = dao.insertPjDto(fzjcxxDto);
            if (result<=0){
                return false;
            }
            List<FzjcxmDto> fzjcxmDtos=(List<FzjcxmDto>) JSON.parseArray(fzjcxxDto.getJcxm(), FzjcxmDto.class);
            for (FzjcxmDto fzjcxmDto : fzjcxmDtos) {
                fzjcxmDto.setFzxmid(StringUtil.generateUUID());
                fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
                fzjcxmDto.setLrry(fzjcxxDto.getWxid());
            }
            fzjcxmPJService.insertList(fzjcxmDtos);
            fzjcxxDto.setJcxm(JSON.toJSONString(fzjcxmDtos));
        }

        //发送rabbit
        amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.NORMAL_INSPECT_TOOA.getCode(), RabbitEnum.WEPJ_EDI.getCode()+ JSONObject.toJSONString(fzjcxxDto));
        return true;
    }

    /**
     * 获取普检信息
     * @param fzjcxxDto
     * @return
     */
    public FzjcxxDto getPjDto(FzjcxxDto fzjcxxDto){
        return dao.getPjDto(fzjcxxDto);
    }

    /**
     * 删除普检信息
     * @param fzjcxxDto
     * @return
     */
    public boolean delGeneralInspection(FzjcxxDto fzjcxxDto){
        boolean isSuccess = dao.delGeneralInspection(fzjcxxDto);
        if (isSuccess){
            //发送rabbit
            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.NORMAL_INSPECT_TOOA.getCode(), RabbitEnum.WEPJ_DEL.getCode()+ JSONObject.toJSONString(fzjcxxDto));
        }
        return isSuccess;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public void addOrModSaveToWechat(FzjcxxDto fzjcxxDto) {
        FzjcxxDto fzjcxxDto_sel = dao.getDtoById(fzjcxxDto.getFzjcid());
        //修改
        if (fzjcxxDto_sel!=null){
            dao.saveEditDto(fzjcxxDto);
            insertFzbbzt(fzjcxxDto);
            if (!CollectionUtils.isEmpty(fzjcxxDto.getDelIds())){
                FzjcxmDto fzjcxmDto_del = new FzjcxmDto();
                fzjcxmDto_del.setScry(fzjcxxDto.getXgry());
                fzjcxmDto_del.setIds(fzjcxxDto.getDelIds());
                fzjcxmPJService.delFzjcxmByIds(fzjcxmDto_del);
            }
            if (!CollectionUtils.isEmpty(fzjcxxDto.getAddFzjcxmDtos())){
                fzjcxmPJService.insertList(fzjcxxDto.getAddFzjcxmDtos());
            }
        }else {
            //新增
            dao.insertPjDto(fzjcxxDto);
            insertFzbbzt(fzjcxxDto);
            if (!CollectionUtils.isEmpty(fzjcxxDto.getAddFzjcxmDtos())){
                fzjcxmPJService.insertList(fzjcxxDto.getAddFzjcxmDtos());
            }
        }
    }

    @Override
    public void updateSyzt(FzjcxxDto fzjcxxDto) {
        dao.updateSyzt(fzjcxxDto);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public void acceptSaveSamplePJ(FzjcxxDto fzjcxxDto) {
        //更新分子检测信息
        dao.saveFzjcxxInfo(fzjcxxDto);
        //更新分子检测标本信息
        insertFzbbzt(fzjcxxDto);
    }



    /**
     * 新增普检标本状态
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertFzbbzt(FzjcxxDto fzjcxxDto) {
        dao.deleteByFzjcid(fzjcxxDto.getFzjcid());
        List<String> pjbbzts = fzjcxxDto.getPjbbzts();
        if(!CollectionUtils.isEmpty(pjbbzts)) {
            List<FzbbztDto> fzbbztDtoList=new ArrayList<>();
            for (int i = 0; i < pjbbzts.size(); i++){
                FzbbztDto fzbbztDto=new FzbbztDto();
                fzbbztDto.setFzjcid(fzjcxxDto.getFzjcid());
                fzbbztDto.setXh(String.valueOf(i+1));
                fzbbztDto.setZt(pjbbzts.get(i));
                fzbbztDtoList.add(fzbbztDto);
            }
            dao.insertFzbbzt(fzbbztDtoList);
        }
        return true;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public void delPJDetection(FzjcxxDto fzjcxxDto) {
        dao.delPJDetection(fzjcxxDto);
        FzjcxmDto fzjcxmDto = new FzjcxmDto();
        fzjcxmDto.setIds(fzjcxxDto.getIds());
        fzjcxmDto.setScry(fzjcxxDto.getScry());
        fzjcxmPJService.delFzjcxmByFzjc(fzjcxmDto);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public void resultModSavePJ(FzjcjgDto fzjcjgDto) {
        fzjcjgPJService.delDtoListByFzxmid(fzjcjgDto);
        if (!CollectionUtils.isEmpty(fzjcjgDto.getJglist())){
            fzjcjgPJService.insertList(fzjcjgDto.getJglist());
        }
        //修改igams_fzjcxx表检测结果
        if (!CollectionUtils.isEmpty(fzjcjgDto.getFzjcxxmcs())){
            dao.updateJcjgmc(fzjcjgDto.getFzjcxxmcs());
        }
    }

    @Override
    public void updatePjBgrqAndBgwcs(FzjcxxDto fzjcxxDto) {
        dao.updatePjBgrqAndBgwcs(fzjcxxDto);
    }


    /**
     * 获取个人报告
     */
    @Override
    public List<FjcfbDto> getReport(FjcfbDto fjcfbDto){
        return dao.getReport(fjcfbDto);
    }
}

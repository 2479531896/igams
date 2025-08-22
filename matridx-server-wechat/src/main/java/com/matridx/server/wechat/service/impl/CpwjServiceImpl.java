package com.matridx.server.wechat.service.impl;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.server.wechat.dao.entities.CpwjDto;
import com.matridx.server.wechat.dao.entities.CpwjModel;
import com.matridx.server.wechat.dao.post.ICpwjDao;
import com.matridx.server.wechat.service.svcinterface.ICpwjService;
import com.matridx.springboot.util.qrcode.QrCodeUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CpwjServiceImpl extends BaseBasicServiceImpl<CpwjDto, CpwjModel, ICpwjDao> implements ICpwjService {
    @Autowired
    IFjcfbService fjcfbService;
    @Value("${matridx.fileupload.prefix}")
    private String prefix;
    @Value("${matridx.fileupload.tempPath}")
    private String tempFilePath;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    private Logger log = LoggerFactory.getLogger(CpwjServiceImpl.class);
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addSaveProductManual(CpwjDto cpwjDto) throws BusinessException {
        if (CollectionUtils.isEmpty(cpwjDto.getFjids())){
            throw new BusinessException("msg", "请上传产品说明书附件!");
        }
        boolean isSuccess;
        cpwjDto.setCpwjid(StringUtil.generateUUID());
        cpwjDto.setSfgk("1");
        CpwjDto cpwjDto_sel = new CpwjDto();
        cpwjDto_sel.setCpdm(cpwjDto.getCpdm());
        cpwjDto_sel.setBbh(cpwjDto.getBbh());
        CpwjDto cpwjDto_res = dao.getDtoByBbhAndCpdm(cpwjDto_sel);
        if (cpwjDto_res!=null){
            cpwjDto.setXh(String.valueOf(Integer.parseInt(cpwjDto_res.getXh())+1));
            CpwjDto cpwjDto_mod = new CpwjDto();
            cpwjDto_mod.setCpdm(cpwjDto.getCpdm());
            cpwjDto_mod.setBbh(cpwjDto.getBbh());
            cpwjDto_mod.setSfgk("0");
            isSuccess = dao.updateByBbhAndCpdm(cpwjDto_mod);
            if (!isSuccess){
                throw new BusinessException("msg", "修改产品说明书失败!");
            }
        }else {
            cpwjDto.setXh("1");
        }
        isSuccess = insert(cpwjDto);
        if (!isSuccess){
            throw new BusinessException("msg", "新增产品说明书失败!");
        }
        DBEncrypt bpe = new DBEncrypt();
        String fileName = cpwjDto.getCpdm()+"("+cpwjDto.getBbh()+")"+System.currentTimeMillis() + ".jpg";
        String path = prefix + tempFilePath + BusTypeEnum.IMP_PRODUCT_MANUAL_QRCODE.getCode();
        String url = bpe.dCode(applicationurl)+"/ws/productManual/viewProductManual/"+cpwjDto.getCpdm()+"/"+cpwjDto.getBbh();
        log.error("说明书访问路径："+url);
        String filePath = QrCodeUtil.createQrCode(url, path, fileName);
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setFjid(StringUtil.generateUUID());
        fjcfbDto.setYwid(cpwjDto.getCpwjid());
        fjcfbDto.setXh("1");
        fjcfbDto.setYwlx(BusTypeEnum.IMP_PRODUCT_MANUAL_QRCODE.getCode());
        fjcfbDto.setWjm(fileName);
        fjcfbDto.setWjlj(bpe.eCode(filePath));
        fjcfbDto.setFwjlj(bpe.eCode(path));
        fjcfbDto.setFwjm(bpe.eCode(fileName));
        fjcfbDto.setZhbj("0");
        fjcfbDto.setLrry(cpwjDto.getLrry());
        isSuccess = fjcfbService.insert(fjcfbDto);
        if(!isSuccess) {
            throw new BusinessException("msg", "保存二维码附件失败!");
        }
        for (int i = 0; i < cpwjDto.getFjids().size(); i++) {
            isSuccess = fjcfbService.save2RealFile(cpwjDto.getFjids().get(i),cpwjDto.getCpwjid());
            if(!isSuccess) {
                throw new BusinessException("msg", "保存附件失败!");
            }
        }
        return isSuccess;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean modSaveProductManual(CpwjDto cpwjDto) throws BusinessException {
        if (CollectionUtils.isEmpty(cpwjDto.getFjids())){
            throw new BusinessException("msg", "请上传产品说明书附件!");
        }else {
            for (int i = 0; i < cpwjDto.getFjids().size(); i++) {
                fjcfbService.save2RealFile(cpwjDto.getFjids().get(i), cpwjDto.getCpwjid());
            }
        }
        boolean isSuccess = dao.updateCpwjDto(cpwjDto);
        if(!isSuccess) {
            throw new BusinessException("msg", "修改产品说明书失败!");
        }
        return true;
    }

    @Override
    public CpwjDto getDtoByBbhAndCpdm(CpwjDto cpwjDto) {
        return dao.getDtoByBbhAndCpdm(cpwjDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delProductManual(CpwjDto cpwjDto) throws BusinessException {
        List<CpwjDto> dtoList = getDtoList(cpwjDto);
        List<CpwjDto> modCpwjDtos = new ArrayList<>();
        for (CpwjDto dto : dtoList) {
            if ("1".equals(dto.getSfgk())){
                CpwjDto cpwjDto_mod = new CpwjDto();
                cpwjDto_mod.setBbh(dto.getBbh());
                cpwjDto_mod.setCpdm(dto.getCpdm());
                cpwjDto_mod.setCpwjid(dto.getCpwjid());
                cpwjDto_mod.setXgry(cpwjDto.getScry());
                modCpwjDtos.add(cpwjDto_mod);
            }
        }
        if (!CollectionUtils.isEmpty(modCpwjDtos)){
            dao.updateGkByBbhAndCpdm(modCpwjDtos);
        }
        boolean isSuccess = delete(cpwjDto);
        if(!isSuccess) {
            throw new BusinessException("msg", "删除产品说明书失败!");
        }
        return true;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean publicornotProductManual(CpwjDto cpwjDto) throws BusinessException {
        CpwjDto dtoById = dao.getDtoById(cpwjDto.getCpwjid());
        if ("1".equals(dtoById.getSfgk())){
            List<CpwjDto> modCpwjDtos = new ArrayList<>();
            CpwjDto cpwjDto_mod = new CpwjDto();
            cpwjDto_mod.setBbh(dtoById.getBbh());
            cpwjDto_mod.setCpdm(dtoById.getCpdm());
            cpwjDto_mod.setCpwjid(dtoById.getCpwjid());
            cpwjDto_mod.setXgry(cpwjDto.getXgry());
            modCpwjDtos.add(cpwjDto_mod);
            dao.updateGkByBbhAndCpdm(modCpwjDtos);

            cpwjDto.setSfgk("0");
        }else {
            CpwjDto cpwjDto_mod = new CpwjDto();
            cpwjDto_mod.setCpdm(dtoById.getCpdm());
            cpwjDto_mod.setBbh(dtoById.getBbh());
            cpwjDto_mod.setXgry(cpwjDto.getXgry());
            cpwjDto_mod.setSfgk("0");
            dao.updateByBbhAndCpdm(cpwjDto_mod);

            cpwjDto.setSfgk("1");
        }
        boolean isSuccess = update(cpwjDto);
        if(!isSuccess) {
            throw new BusinessException("msg", "修改产品说明书失败!");
        }
        return true;
    }
}

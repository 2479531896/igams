package com.matridx.igams.hrm.service.impl;


import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.service.svcinterface.IGzglService;
import com.matridx.igams.hrm.dao.entities.*;
import com.matridx.igams.hrm.dao.post.ITkglDao;
import com.matridx.igams.hrm.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class TkglServiceImpl extends BaseBasicServiceImpl<TkglDto, TkglModel, ITkglDao> implements ITkglService , IFileImport {
    private final Logger log = LoggerFactory.getLogger(PxglServiceImpl.class);
    @Autowired
    IKsglService ksglService;
    @Autowired
    IKsmxService ksmxService;
    @Autowired
    IGrksmxService grksmxService;
    @Autowired
    IGrksglService grksglService;
    @Autowired
    IGzglService gzglService;
    @Override
    public boolean existCheck(String fieldName, String value) {
        return false;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages) throws BusinessException {
        TkglDto tkglDto = (TkglDto) baseModel;
        TkglDto tkglDto_t = getDto(tkglDto);
        if(tkglDto_t!=null){
            tkglDto.setLrry(user.getYhid());
            tkglDto.setTkid(tkglDto_t.getTkid());
        }else{
            log.error("未找到名为   "+tkglDto.getTkmc()+"   的题库，请先新增题库再导入题目！");
            throw new BusinessException("msg", "未找到名为   "+tkglDto.getTkmc()+"   的题库，请先新增题库再导入题目！");
        }
        KsglDto ksglDto = new KsglDto();
        ksglDto.setTkid(tkglDto.getTkid());
        ksglDto.setXh(tkglDto.getXh());
        if("单选题".equals(tkglDto.getTmlx())){
            ksglDto.setTmlx("SELECT");
        }else if("多选题".equals(tkglDto.getTmlx())){
            ksglDto.setTmlx("MULTIPLE");
        }else if("简答题".equals(tkglDto.getTmlx())){
            ksglDto.setTmlx("EXPLAIN");
        }else if("判断题".equals(tkglDto.getTmlx())){
            ksglDto.setTmlx("JUDGE");
        }else if("填空题".equals(tkglDto.getTmlx())){
            ksglDto.setTmlx("GAP");
        }
        ksglDto.setTmnr(tkglDto.getTmnr());
        if (StringUtil.isBlank(ksglDto.getTmnr())){
            throw new BusinessException("msg","题目内容不能为空");
        }
        if (StringUtil.isBlank(tkglDto.getDa())&&!"GAP".equals(ksglDto.getTmlx())&&!"EXPLAIN".equals(ksglDto.getTmlx())){
            throw new BusinessException("msg",tkglDto.getTmnr()+"答案不能为空");
        }
        ksglDto.setDa(tkglDto.getDa());
        ksglDto.setDajx(tkglDto.getDajx());
        ksglDto.setLrry(user.getYhid());
        int count = ksglService.getCount(ksglDto);
        if(count==0){
            ksglDto.setKsid(StringUtil.generateUUID());
            ksglService.insert(ksglDto);
            if(StringUtil.isNotBlank(tkglDto.getXxA())){
                KsmxDto ksmxDto=new KsmxDto();
                ksmxDto.setKsid(ksglDto.getKsid());
                ksmxDto.setXxdm("A");
                ksmxDto.setXxnr(tkglDto.getXxA());
                ksmxDto.setLrry(user.getYhid());
                ksmxDto.setKsmxid(StringUtil.generateUUID());
                ksmxService.insert(ksmxDto);
            }else if (StringUtil.isBlank(tkglDto.getXxA())&&!"GAP".equals(ksglDto.getTmlx())&&!"EXPLAIN".equals(ksglDto.getTmlx())){
               throw new BusinessException("msg",tkglDto.getTmnr()+"选项A不能为空");
            }
            if(StringUtil.isNotBlank(tkglDto.getXxB())){
                KsmxDto ksmxDto=new KsmxDto();
                ksmxDto.setKsid(ksglDto.getKsid());
                ksmxDto.setXxdm("B");
                ksmxDto.setXxnr(tkglDto.getXxB());
                ksmxDto.setLrry(user.getYhid());
                ksmxDto.setKsmxid(StringUtil.generateUUID());
                ksmxService.insert(ksmxDto);
            }
            if(StringUtil.isNotBlank(tkglDto.getXxC())){
                KsmxDto ksmxDto=new KsmxDto();
                ksmxDto.setKsid(ksglDto.getKsid());
                ksmxDto.setXxdm("C");
                ksmxDto.setXxnr(tkglDto.getXxC());
                ksmxDto.setLrry(user.getYhid());
                ksmxDto.setKsmxid(StringUtil.generateUUID());
                ksmxService.insert(ksmxDto);
            }
            if(StringUtil.isNotBlank(tkglDto.getXxD())){
                KsmxDto ksmxDto=new KsmxDto();
                ksmxDto.setKsid(ksglDto.getKsid());
                ksmxDto.setXxdm("D");
                ksmxDto.setXxnr(tkglDto.getXxD());
                ksmxDto.setLrry(user.getYhid());
                ksmxDto.setKsmxid(StringUtil.generateUUID());
                ksmxService.insert(ksmxDto);
            }
            if(StringUtil.isNotBlank(tkglDto.getXxE())){
                KsmxDto ksmxDto=new KsmxDto();
                ksmxDto.setKsid(ksglDto.getKsid());
                ksmxDto.setXxdm("E");
                ksmxDto.setXxnr(tkglDto.getXxE());
                ksmxDto.setLrry(user.getYhid());
                ksmxDto.setKsmxid(StringUtil.generateUUID());
                ksmxService.insert(ksmxDto);
            }
            if(StringUtil.isNotBlank(tkglDto.getXxF())){
                KsmxDto ksmxDto=new KsmxDto();
                ksmxDto.setKsid(ksglDto.getKsid());
                ksmxDto.setXxdm("F");
                ksmxDto.setXxnr(tkglDto.getXxF());
                ksmxDto.setLrry(user.getYhid());
                ksmxDto.setKsmxid(StringUtil.generateUUID());
                ksmxService.insert(ksmxDto);
            }
            if(StringUtil.isNotBlank(tkglDto.getXxG())){
                KsmxDto ksmxDto=new KsmxDto();
                ksmxDto.setKsid(ksglDto.getKsid());
                ksmxDto.setXxdm("G");
                ksmxDto.setXxnr(tkglDto.getXxG());
                ksmxDto.setLrry(user.getYhid());
                ksmxDto.setKsmxid(StringUtil.generateUUID());
                ksmxService.insert(ksmxDto);
            }
            if(StringUtil.isNotBlank(tkglDto.getXxH())){
                KsmxDto ksmxDto=new KsmxDto();
                ksmxDto.setKsid(ksglDto.getKsid());
                ksmxDto.setXxdm("H");
                ksmxDto.setXxnr(tkglDto.getXxH());
                ksmxDto.setLrry(user.getYhid());
                ksmxDto.setKsmxid(StringUtil.generateUUID());
                ksmxService.insert(ksmxDto);
            }
        }
        return true;
    }

    @Override
    public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
        return null;
    }

    @Override
    public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
        return false;
    }

    @Override
    public boolean checkDefined(List<Map<String, String>> defined) {
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateTK(TkglDto tkglDto) throws BusinessException {
        int update = dao.update(tkglDto);
        if (update<1){
            throw new BusinessException("msg","修改失败");
        }
        List<KsglDto> tmmxlist= JSON.parseArray(tkglDto.getTmmx_json(), KsglDto.class);
        log.error("修改题库明细"+ tkglDto.getTmmx_json());
        KsmxDto ksmxDto_t=new KsmxDto();
        ksmxDto_t.setTkid(tkglDto.getTkid());
        ksmxService.deleteByTkid(ksmxDto_t);
        KsglDto ksglDto=new KsglDto();
        ksglDto.setTkid(tkglDto.getTkid());
        ksglService.deleteByTkid(ksglDto);
        if(!CollectionUtils.isEmpty(tmmxlist)){
            List<KsglDto> ksglDtos=new ArrayList<>();
            List<KsmxDto> ksmxDtos=new ArrayList<>();
            for(int i=0;i<tmmxlist.size();i++){
                if(StringUtil.isNotBlank(tmmxlist.get(i).getKsid())){
                    tmmxlist.get(i).setTkid(tkglDto.getTkid());
                    ksglDtos.add(tmmxlist.get(i));
                }else{
                    tmmxlist.get(i).setTkid(tkglDto.getTkid());
                    tmmxlist.get(i).setKsid(StringUtil.generateUUID());
                    tmmxlist.get(i).setXh(String.valueOf(i+1));
                    ksglDtos.add(tmmxlist.get(i));
                }
                if(StringUtil.isNotBlank(tmmxlist.get(i).getXxA())){
                    KsmxDto ksmxDto=new KsmxDto();
                    ksmxDto.setKsid(tmmxlist.get(i).getKsid());
                    ksmxDto.setXxdm("A");
                    ksmxDto.setXxnr(tmmxlist.get(i).getXxA());
                    ksmxDto.setLrry(tkglDto.getXgry());
                    ksmxDto.setKsmxid(StringUtil.generateUUID());
                    ksmxDtos.add(ksmxDto);
                }
                if(StringUtil.isNotBlank(tmmxlist.get(i).getXxB())){
                    KsmxDto ksmxDto=new KsmxDto();
                    ksmxDto.setKsid(tmmxlist.get(i).getKsid());
                    ksmxDto.setXxdm("B");
                    ksmxDto.setXxnr(tmmxlist.get(i).getXxB());
                    ksmxDto.setLrry(tkglDto.getXgry());
                    ksmxDto.setKsmxid(StringUtil.generateUUID());
                    ksmxDtos.add(ksmxDto);
                }
                if(StringUtil.isNotBlank(tmmxlist.get(i).getXxC())){
                    KsmxDto ksmxDto=new KsmxDto();
                    ksmxDto.setKsid(tmmxlist.get(i).getKsid());
                    ksmxDto.setXxdm("C");
                    ksmxDto.setXxnr(tmmxlist.get(i).getXxC());
                    ksmxDto.setLrry(tkglDto.getXgry());
                    ksmxDto.setKsmxid(StringUtil.generateUUID());
                    ksmxDtos.add(ksmxDto);
                }
                if(StringUtil.isNotBlank(tmmxlist.get(i).getXxD())){
                    KsmxDto ksmxDto=new KsmxDto();
                    ksmxDto.setKsid(tmmxlist.get(i).getKsid());
                    ksmxDto.setXxdm("D");
                    ksmxDto.setXxnr(tmmxlist.get(i).getXxD());
                    ksmxDto.setLrry(tkglDto.getXgry());
                    ksmxDto.setKsmxid(StringUtil.generateUUID());
                    ksmxDtos.add(ksmxDto);
                }
                if(StringUtil.isNotBlank(tmmxlist.get(i).getXxE())){
                    KsmxDto ksmxDto=new KsmxDto();
                    ksmxDto.setKsid(tmmxlist.get(i).getKsid());
                    ksmxDto.setXxdm("E");
                    ksmxDto.setXxnr(tmmxlist.get(i).getXxE());
                    ksmxDto.setLrry(tkglDto.getXgry());
                    ksmxDto.setKsmxid(StringUtil.generateUUID());
                    ksmxDtos.add(ksmxDto);
                }
                if(StringUtil.isNotBlank(tmmxlist.get(i).getXxF())){
                    KsmxDto ksmxDto=new KsmxDto();
                    ksmxDto.setKsid(tmmxlist.get(i).getKsid());
                    ksmxDto.setXxdm("F");
                    ksmxDto.setXxnr(tmmxlist.get(i).getXxF());
                    ksmxDto.setLrry(tkglDto.getXgry());
                    ksmxDto.setKsmxid(StringUtil.generateUUID());
                    ksmxDtos.add(ksmxDto);
                }
                if(StringUtil.isNotBlank(tmmxlist.get(i).getXxG())){
                    KsmxDto ksmxDto=new KsmxDto();
                    ksmxDto.setKsid(tmmxlist.get(i).getKsid());
                    ksmxDto.setXxdm("G");
                    ksmxDto.setXxnr(tmmxlist.get(i).getXxG());
                    ksmxDto.setLrry(tkglDto.getXgry());
                    ksmxDto.setKsmxid(StringUtil.generateUUID());
                    ksmxDtos.add(ksmxDto);
                }
                if(StringUtil.isNotBlank(tmmxlist.get(i).getXxH())){
                    KsmxDto ksmxDto=new KsmxDto();
                    ksmxDto.setKsid(tmmxlist.get(i).getKsid());
                    ksmxDto.setXxdm("H");
                    ksmxDto.setXxnr(tmmxlist.get(i).getXxH());
                    ksmxDto.setLrry(tkglDto.getXgry());
                    ksmxDto.setKsmxid(StringUtil.generateUUID());
                    ksmxDtos.add(ksmxDto);
                }
            }
            if(!CollectionUtils.isEmpty(ksglDtos)){
                boolean gl = ksglService.insertList(ksglDtos);
                if (!gl){
                    throw new BusinessException("msg","修改失败");
                }
            }
            if(!CollectionUtils.isEmpty(ksmxDtos)){
                boolean mx = ksmxService.insertList(ksmxDtos);
                if (!mx){
                    throw new BusinessException("msg","修改失败");
                }
            }
            if ("1".equals(tkglDto.getSfgx())){
                List<KsglDto> xgqtkList = JSON.parseArray(tkglDto.getXgqtk(), KsglDto.class);
                boolean flag = false;
                List<String> ids = new ArrayList<>();
                for (KsglDto dto : xgqtkList) {
                    for (KsglDto tmmx : tmmxlist) {
                        if (dto.getKsid().equals(tmmx.getKsid())){
                            if (!dto.getDa().equals(tmmx.getDa())){
                                dto.setDa(tmmx.getDa());
                                ids.add(dto.getKsid());
                                flag = true;
                            }
                        }
                    }
                }
                if (flag){
                    List<GrksmxDto> grksmxDtos =grksmxService.getListByKsIds(ids);
                    List<GrksmxDto> upGrksmxDtos = new ArrayList<>();
                    List<GrksglDto> upGrksglDtos = new ArrayList<>();
                    List<GzglDto> upGzglDtos = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(grksmxDtos)){
                        List<String> list = new ArrayList<>();
                        for (GrksmxDto grksmx : grksmxDtos) {
                            int zf = 0;
                            String grksid = grksmx.getGrksid();
                            if (!list.contains(grksid)) {
                                list.add(grksid);
                                for (GrksmxDto grksmxDto : grksmxDtos) {
                                    if (grksid.equals(grksmxDto.getGrksid())) {
                                        for (KsglDto dto : xgqtkList) {
                                            if (dto.getKsid().equals(grksmxDto.getKsid())) {
                                                if ("SELECT".equals(grksmxDto.getTmlx()) || "MULTIPLE".equals(grksmxDto.getTmlx()) || "JUDGE".equals(grksmxDto.getTmlx())) {
                                                    String[] split = dto.getDa().split(",");
                                                    String[] option = grksmxDto.getDtjg().split(",");
                                                    if (split.length == option.length) {
                                                        for (String s : option) {
                                                            if (dto.getDa().contains(s)) {
                                                                flag = true;
                                                            } else {
                                                                flag = false;
                                                                break;
                                                            }
                                                        }
                                                        if (flag) {
                                                            zf = zf + Integer.parseInt(grksmxDto.getFs());
                                                            GrksmxDto grksmxDto_t = new GrksmxDto();
                                                            grksmxDto_t.setDf(grksmxDto.getFs());
                                                            grksmxDto_t.setGrksmxid(grksmxDto.getGrksmxid());
                                                            upGrksmxDtos.add(grksmxDto_t);
                                                        } else {
                                                            GrksmxDto grksmxDto_t = new GrksmxDto();
                                                            grksmxDto_t.setDf("0");
                                                            grksmxDto_t.setGrksmxid(grksmxDto.getGrksmxid());
                                                            upGrksmxDtos.add(grksmxDto_t);
                                                        }
                                                    } else {
                                                        GrksmxDto grksmxDto_t = new GrksmxDto();
                                                        grksmxDto_t.setDf("0");
                                                        grksmxDto_t.setGrksmxid(grksmxDto.getGrksmxid());
                                                        upGrksmxDtos.add(grksmxDto_t);
                                                    }
                                                } else {
                                                    zf = zf + Integer.parseInt(grksmxDto.getDf());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            GrksglDto grksglDto = new GrksglDto();
                            grksglDto.setGrksid(grksid);
                            grksglDto.setZf(String.valueOf(zf));
                            GzglDto gzglDto = new GzglDto();
                            gzglDto.setGzid(grksmx.getGzid());
                            int tgfs = Integer.parseInt(grksmx.getTgfs());
                            if (zf >= tgfs) {
                                gzglDto.setZt("80");
                                gzglDto.setTgbj("1");
                            } else {
                                gzglDto.setZt("00");
                                gzglDto.setTgbj("0");
                            }
                            upGzglDtos.add(gzglDto);
                            upGrksglDtos.add(grksglDto);

                        }
                        boolean updateGrksglDtos = grksglService.updateGrksglDtos(upGrksglDtos);
                        if (!updateGrksglDtos){
                            throw new BusinessException("msg","更新分数失败！");
                        }
                        if (!CollectionUtils.isEmpty(upGrksmxDtos)){
                            boolean updateGrksmxDtos = grksmxService.updateGrksmxDtos(upGrksmxDtos);
                            if (!updateGrksmxDtos){
                                throw new BusinessException("msg","更新分数失败！");
                            }
                        }
                        boolean updateGzglDtos = gzglService.updateGzglDtos(upGzglDtos);
                        if (!updateGzglDtos){
                            throw new BusinessException("msg","更新分数失败！");
                        }
                    }
                }
            }
        }
        return true;
    }
}

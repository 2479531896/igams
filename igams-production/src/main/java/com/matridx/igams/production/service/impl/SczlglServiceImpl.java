package com.matridx.igams.production.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.production.dao.entities.*;
import com.matridx.igams.production.dao.post.ISczlglDao;
import com.matridx.igams.production.service.svcinterface.ISczlglService;
import com.matridx.igams.production.service.svcinterface.ISczlmxService;
import com.matridx.igams.production.service.svcinterface.IXqjhmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SczlglServiceImpl extends BaseBasicServiceImpl<SczlglDto, SczlglModel, ISczlglDao> implements ISczlglService, IAuditService {
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;

    @Autowired
    ICommonService commonService;
    @Autowired
    ISczlmxService sczlmxService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IXqjhmxService xqjhmxService;
    @Autowired
    RedisUtil redisUtil;
    private final Logger log = LoggerFactory.getLogger(SczlglServiceImpl.class);

    @Override
    public String getZldh() {
        String yearLast = new SimpleDateFormat("yyyyMMdd", Locale.CHINESE).format(new Date().getTime());
        String prefix = "PO-" + yearLast+"-";
        // 查询流水号
        String serial = dao.getZldhSerial(prefix);
        return prefix + serial;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public List<String> produceAddSave(SczlglDto sczlglDto,XqjhmxDto xqjhmxDto_t) throws BusinessException {
        if ("0".equals(sczlglDto.getSfsc())){
            if(StringUtil.isNotBlank(sczlglDto.getYhm())){
                talkUtil.sendWorkMessage(sczlglDto.getYhm(),
                        sczlglDto.getSqr(),
                        xxglService.getMsg("ICOMM_DSC001"),xxglService.getMsg("ICOMM_DSC002",xqjhmxDto_t.getXqdh(),xqjhmxDto_t.getXqrq(),xqjhmxDto_t.getWlbm(),xqjhmxDto_t.getWlmc(),xqjhmxDto_t.getSl(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
            }
        }
        sczlglDto.setZt("00");
        Boolean success = dao.insert(sczlglDto)!=0;
        if (!success){
            throw new BusinessException("msg","新增生产指令失败！");
        }
        //文件复制到正式文件夹，插入信息至正式表
        if(!CollectionUtils.isEmpty(sczlglDto.getFjids())) {
            for (int i = 0; i < sczlglDto.getFjids().size(); i++) {
                boolean saveFile = fjcfbService.save2RealFile(sczlglDto.getFjids().get(i),sczlglDto.getSczlid());
                if(!saveFile)
                    throw new BusinessException("msg","生产指令附件保存失败!");
            }
        }
        List<String> ywids = new ArrayList<>();
        List<SczlmxDto> sczlmxDtos = JSON.parseArray(sczlglDto.getSczlmx_json(), SczlmxDto.class);
        if(!CollectionUtils.isEmpty(sczlmxDtos)) {
            for (SczlmxDto sczlmxDto : sczlmxDtos) {
                sczlmxDto.setLrry(sczlglDto.getLrry());
                sczlmxDto.setSczlid(sczlglDto.getSczlid());
                sczlmxDto.setSczlmxid(StringUtil.generateUUID());
                ywids.add(sczlmxDto.getSczlmxid());
            }
            success = sczlmxService.insertList(sczlmxDtos);
            if (!success){
                throw new BusinessException("msg","新增生产指令明细信息失败！");
            }
        }else{
            throw new BusinessException("msg","明细计划信息为空！");
        }
        if (StringUtil.isNotBlank(sczlmxDtos.get(0).getXqjhmxid())){
            XqjhmxDto xqjhmxDto = new XqjhmxDto();
            xqjhmxDto.setScsl(sczlglDto.getJhcl());
            xqjhmxDto.setScslbj("0");
            xqjhmxDto.setXqjhmxid(sczlmxDtos.get(0).getXqjhmxid());
            success = xqjhmxService.update(xqjhmxDto);
            if (!success)
                throw new BusinessException("msg","修改需求生产数量失败！");
        }
        return ywids;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public List<String> produceModSave(SczlglDto sczlglDto) throws BusinessException {
        Boolean success = dao.update(sczlglDto)!=0;
        if (!success){
            throw new BusinessException("msg","修改生产指令失败！");
        }
        //文件复制到正式文件夹，插入信息至正式表
        if(!CollectionUtils.isEmpty(sczlglDto.getFjids())) {
            for (int i = 0; i < sczlglDto.getFjids().size(); i++) {
                boolean saveFile = fjcfbService.save2RealFile(sczlglDto.getFjids().get(i),sczlglDto.getSczlid());
                if(!saveFile)
                    throw new BusinessException("msg","生产指令附件保存失败!");
            }
        }
        List<String> ywids = new ArrayList<>();
        List<SczlmxDto> sczlmxDtos = JSON.parseArray(sczlglDto.getSczlmx_json(), SczlmxDto.class);

        if (StringUtil.isBlank(sczlglDto.getCzbs())){
            SczlmxDto dto = new SczlmxDto();
            dto.setScry(sczlglDto.getXgry());
            dto.setScbj("1");
            dto.setSczlmxid(sczlglDto.getSczlmxid());
            success = sczlmxService.delete(dto);
            if (!success){
                throw new BusinessException("msg","新修改生产指令明细信息失败！");
            }
            if(!CollectionUtils.isEmpty(sczlmxDtos)) {
                for (SczlmxDto sczlmxDto : sczlmxDtos) {
                    sczlmxDto.setLrry(sczlglDto.getXgry());
                    sczlmxDto.setSczlid(sczlglDto.getSczlid());
                    sczlmxDto.setSczlmxid(StringUtil.generateUUID());
                    ywids.add(sczlmxDto.getSczlmxid());
                }
                success = sczlmxService.insertList(sczlmxDtos);
                if (!success){
                    throw new BusinessException("msg","新增生产指令明细信息失败！");
                }
            }else{
                throw new BusinessException("msg","明细计划信息为空！");
            }
        }else{
            if(!CollectionUtils.isEmpty(sczlmxDtos)) {
                for (SczlmxDto sczlmxDto : sczlmxDtos) {
                    sczlmxDto.setXgry(sczlglDto.getXgry());
                    ywids.add(sczlmxDto.getSczlmxid());
                }
                sczlmxService.updateList(sczlmxDtos);
            }else{
                throw new BusinessException("msg","明细计划信息为空！");
            }
        }
        if (StringUtil.isNotBlank(sczlglDto.getXqjhmxid())){
            XqjhmxDto xqjhmxDto = new XqjhmxDto();
            xqjhmxDto.setScsl(sczlglDto.getBysl());
            xqjhmxDto.setScslbj("0");
            xqjhmxDto.setXqjhmxid(sczlglDto.getXqjhmxid());
            success = xqjhmxService.update(xqjhmxDto);
            if (!success)
                throw new BusinessException("msg","修改需求生产数量失败！");
        }
        return ywids;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Boolean produceDel(SczlglDto sczlglDto) throws BusinessException {
        Boolean success;
       List<SczlmxDto> sczlmxDtos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(sczlglDto.getIds())) {
            SczlmxDto dto = new SczlmxDto();
            dto.setIds(sczlglDto.getIds());
            List<SczlmxDto> dtoList = sczlmxService.getDtoList(dto);
            List<XqjhmxDto> xqjhmxDtos = new ArrayList<>();
            for (SczlmxDto sczlmxDto : dtoList) {
                if (StringUtil.isNotBlank(sczlmxDto.getXqjhmxid())){
                    XqjhmxDto xqjhmxDto = new XqjhmxDto();
                    xqjhmxDto.setScsl(sczlmxDto.getScsl());
                    xqjhmxDto.setScslbj("1");
                    xqjhmxDto.setXqjhmxid(sczlmxDto.getXqjhmxid());
                    xqjhmxDtos.add(xqjhmxDto);
                }
            }
            if (!CollectionUtils.isEmpty(xqjhmxDtos)){
                success = xqjhmxService.updateDtoList(xqjhmxDtos);
                if (!success)
                    throw new BusinessException("msg","修改需求生产数量失败！");
            }
            for (String id : sczlglDto.getIds()) {
                SczlmxDto sczlmxDto = new SczlmxDto();
                sczlmxDto.setSczlmxid(id);
                sczlmxDto.setScry(sczlglDto.getScry());
                sczlmxDto.setScbj("1");
                sczlmxDtos.add(sczlmxDto);
            }
            success=  sczlmxService.updateList(sczlmxDtos);
            if (!success)
                throw new BusinessException("msg","删除明细信息失败！");
        }else{
            throw new BusinessException("msg","为获取到ids！");
        }
        return true;
    }

    @Override
    public List<SczlglDto> getPagedDtoList(SczlglDto sczlglDto){
        List<SczlglDto> pagedDtoList = dao.getPagedDtoList(sczlglDto);
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.AUDIT_TYPE.getCode());
        for (JcsjDto jcsjDto : jcsjDtos) {
            try {
                shgcService.addShgcxxByYwid(pagedDtoList, jcsjDto.getCskz1(), "zt", "sczlmxid",
                        new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                log.error(e.getMessage());
            }
        }

        return pagedDtoList;
    }

    @Override
    public List<SczlglDto> getPagedModelDtoList(SczlglDto sczlglDto){
        return dao.getPagedModelDtoList(sczlglDto);
    }

    @Override
    public List<SczlglDto> getPagedAuditDevice(SczlglDto sczlglDto) {
        // 获取人员ID和履历号
        List<SczlglDto> sczlglDtos= dao.getPagedAuditDevice(sczlglDto);
        if (CollectionUtils.isEmpty(sczlglDtos))
            return sczlglDtos;
        List<SczlglDto> sczlglDtoList = dao.getAuditListDevice(sczlglDtos);
        commonService.setSqrxm(sczlglDtoList);
        return sczlglDtoList;
    }

    @Override
    public Map<String, Object> getHjyProgress(Map<String, Object> map) {
        Map<String,Object> returnmap=new HashMap<>();
        Map<String,Object> hjymap=new HashMap<>();
        Map<String,Object> hjymap_t=new HashMap<>();
        hjymap.put("wlbms",map.get("hjywlbms"));
        hjymap.put("nfs",map.get("hjynfs"));
        hjymap.put("yfs",map.get("hjyyfs"));
        List<Map<String, Object>> hjymaplist= dao.getHjyProgress(hjymap);
        List<Map<String, Object>> hjymaplist_xq=new ArrayList<>();
        List<Map<String, Object>> hjymaplist_sc=new ArrayList<>();
        List<Map<String, Object>> hjymaplist_ck=new ArrayList<>();
        for (Map<String, Object> map1:hjymaplist){
            if (map1.get("lx").equals("需求人份")){
                hjymaplist_xq.add(map1);
            }else if (map1.get("lx").equals("生产人份")){
                hjymaplist_sc.add(map1);
            }else if (map1.get("lx").equals("出库人份")){
                hjymaplist_ck.add(map1);
            }
        }
        hjymap_t.put("hjymaplist_xq",hjymaplist_xq);
        hjymap_t.put("hjymaplist_sc",hjymaplist_sc);
        hjymap_t.put("hjymaplist_ck",hjymaplist_ck);
        returnmap.put("hjymap",hjymap_t);
        Map<String,Object> xlmap=new HashMap<>();
        Map<String,Object> xlmap_t=new HashMap<>();
        xlmap.put("wlbms",map.get("xlwlbms"));
        xlmap.put("nfs",map.get("xlnfs"));
        xlmap.put("yfs",map.get("xlyfs"));
        List<Map<String, Object>>xlmaplist= dao.getHjyProgress(xlmap);
        List<Map<String, Object>> xlmaplist_xq=new ArrayList<>();
        List<Map<String, Object>> xlmaplist_sc=new ArrayList<>();
        List<Map<String, Object>> xlmaplist_ck=new ArrayList<>();
        for (Map<String, Object> map1:xlmaplist){
            if (map1.get("lx").equals("需求人份")){
                xlmaplist_xq.add(map1);
            }else if (map1.get("lx").equals("生产人份")){
                xlmaplist_sc.add(map1);
            }else if (map1.get("lx").equals("出库人份")){
                xlmaplist_ck.add(map1);
            }
        }
        xlmap_t.put("xlmaplist_xq",xlmaplist_xq);
        xlmap_t.put("xlmaplist_sc",xlmaplist_sc);
        xlmap_t.put("xlmaplist_ck",xlmaplist_ck);
        returnmap.put("xlmap",xlmap_t);
        Map<String,Object> threemap=new HashMap<>();
        Map<String,Object> threemap_t=new HashMap<>();
        threemap.put("wlbms",map.get("threewlbms"));
        threemap.put("nfs",map.get("threenfs"));
        threemap.put("yfs",map.get("threeyfs"));
        List<Map<String, Object>>threemaplist= dao.getHjyProgress(threemap);
        List<Map<String, Object>> threemaplist_xq=new ArrayList<>();
        List<Map<String, Object>> threemaplist_sc=new ArrayList<>();
        List<Map<String, Object>> threemaplist_ck=new ArrayList<>();
        for (Map<String, Object> map1:threemaplist){
            if (map1.get("lx").equals("需求人份")){
                threemaplist_xq.add(map1);
            }else if (map1.get("lx").equals("生产人份")){
                threemaplist_sc.add(map1);
            }else if (map1.get("lx").equals("出库人份")){
                threemaplist_ck.add(map1);
            }
        }
        threemap_t.put("threemaplist_xq",threemaplist_xq);
        threemap_t.put("threemaplist_sc",threemaplist_sc);
        threemap_t.put("threemaplist_ck",threemaplist_ck);
        returnmap.put("threemap",threemap_t);
        return returnmap;
    }

    @Override
    public List<Map<String, Object>> selectDingTalkXq(Map<String, Object> map) {
        return dao.selectDingTalkXq(map);
    }

    @Override
    public List<Map<String, Object>> selectDingTalkSc(Map<String, Object> map) {
        return dao.selectDingTalkSc(map);
    }

    @Override
    public List<Map<String, Object>> selectDingTalkCk(Map<String, Object> map) {
        return dao.selectDingTalkCk(map);
    }

    @Override
    public List<Map<String, Object>> selectDingTalkKc(Map<String, Object> map) {
        return dao.selectDingTalkKc(map);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        SczlglDto sczlglDto = (SczlglDto) baseModel;
        sczlglDto.setXgry(operator.getYhid());
        if (StringUtil.isBlank(sczlglDto.getDdbj())){
            if("1".equals(sczlglDto.getXsbj())) {
                produceModAudit(sczlglDto);
            }else {
                produceModAudit(sczlglDto);
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void produceModAudit(SczlglDto sczlglDto) throws BusinessException {
        //文件复制到正式文件夹，插入信息至正式表
        if (StringUtil.isNotBlank(sczlglDto.getCzbs())){
            if(!CollectionUtils.isEmpty(sczlglDto.getFjids())) {
                for (int i = 0; i < sczlglDto.getFjids().size(); i++) {
                    boolean saveFile = fjcfbService.save2RealFile(sczlglDto.getFjids().get(i),sczlglDto.getSczlmxid());
                    if(!saveFile)
                        throw new BusinessException("msg","生产指令附件保存失败!");
                }
            }
        }else{
            for (String id : sczlglDto.getIds()) {
                if(!CollectionUtils.isEmpty(sczlglDto.getFjids())) {
                    for (int i = 0; i < sczlglDto.getFjids().size(); i++) {
                        boolean saveFile = fjcfbService.batchRealFile(sczlglDto.getFjids().get(i),id,StringUtil.generateUUID());
                        if(!saveFile)
                            throw new BusinessException("msg","生产指令附件保存失败!");
                    }
                }
            }
        }
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
//        String token = talkUtil.getToken();
        String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
        String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
        String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
        String ICOMM_SH00092 = xxglService.getMsg("ICOMM_SH00092");
        for (ShgcDto shgcDto : shgcList) {
            SczlmxDto sczlmxDto = new SczlmxDto();
            sczlmxDto.setSczlmxid(shgcDto.getYwid());
            sczlmxDto.setXgry(operator.getYhid());

            SczlglDto sczlglDto = new SczlglDto();
            sczlglDto.setSczlmxid(shgcDto.getYwid());
            SczlglDto dto = dao.getDto(sczlglDto);
            shgcDto.setSqbm(sczlglDto.getSsjg());
            List<SpgwcyDto> spgwcyDtos = commonService.siftJgList(shgcDto.getSpgwcyDtos(), sczlglDto.getSsjg());

            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                // 由于合同提交时直接发起了钉钉审批，所以审核不通过时直接采用钉钉的审批拒绝消息即可
                sczlmxDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(), spgwcyDto.getYhid(), spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00026,
                                    xxglService.getMsg("ICOMM_SH00093", operator.getZsxm(), shgcDto.getShlbmc(),
                                            dto.getZldh(), dto.getCplxmc(), dto.getWlmc(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                //生成规则：P-年-月日-流水号
                String prefix = "P"+"-"+DateUtils.getCustomFomratCurrentDate("yyyy")+"-"+DateUtils.getCustomFomratCurrentDate("MMdd")+"-";
                String serial = sczlmxService.getProgressSerial(prefix);
                sczlmxDto.setJlbh(prefix+serial);
                sczlmxDto.setZt(StatusEnum.CHECK_PASS.getCode());
//                if (AuditTypeEnum.AUDIT_YQ_PRODUCE.getCode().equals(shgcDto.getShlb())){
                    SczlglDto sczlglDto1 = new SczlglDto();
                    sczlglDto1.setZt(StatusEnum.CHECK_PASS.getCode());
                    sczlglDto1.setSczlid(dto.getSczlid());
                    dao.update(sczlglDto1);
                    SczlmxDto sczlmxDto1=new SczlmxDto();
                    sczlmxDto1.setSczlid(sczlglDto1.getSczlid());
                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date=new Date();
                    sczlmxDto1.setShsj(dateformat.format(date));
                    sczlmxService.updateDto(sczlmxDto1);
//                }
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(), spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00006, StringUtil.replaceMsg(ICOMM_SH00092,
                                    operator.getZsxm(), shgcDto.getShlbmc(), dto.getZldh(), dto.getCplxmc(), dto.getWlmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }

            }else {
                sczlmxDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                String ICOMM_SH00091 = xxglService.getMsg("ICOMM_SH00091");
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00003, StringUtil.replaceMsg(ICOMM_SH00091,
                                        operator.getZsxm(), shgcDto.getShlbmc(), dto.getZldh(), dto.getCplxmc(), dto.getWlmc(),
                                        DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }

                //发送钉钉消息--取消审核人员
                if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())){
                    int size = shgcDto.getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",shgcDto.getShlbmc() ,dto.getZldh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            sczlmxService.update(sczlmxDto);
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        SczlmxDto sczlmxDto = new SczlmxDto();
        if (auditParam.isCancelOpe()) {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                String sczlmxid = shgcDto.getYwid();
                sczlmxDto.setXgry(operator.getYhid());
                sczlmxDto.setSczlmxid(sczlmxid);
                sczlmxDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
            }
        } else {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                String sczlmxid = shgcDto.getYwid();
                sczlmxDto.setXgry(operator.getYhid());
                sczlmxDto.setSczlmxid(sczlmxid);
                sczlmxDto.setZt(StatusEnum.CHECK_NO.getCode());
            }
        }
        boolean success = sczlmxService.update(sczlmxDto);
        if (!success){
            throw new BusinessException("msg","修改明细状态失败！");
        }
        return true;
    }

    @Override
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
        return null;
    }

    @Override
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
        Map<String, Object> map =new HashMap<>();
        @SuppressWarnings("unchecked")
        List<String> ids = (List<String>)param.get("ywids");
        SczlmxDto sczlmxDto = new SczlmxDto();
        sczlmxDto.setIds(ids);
        List<SczlmxDto> dtoList = sczlmxService.getDtoList(sczlmxDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(SczlmxDto dto:dtoList){
                list.add(dto.getSczlmxid());
            }
        }
        map.put("list",list);
        return map;
    }

    /**
     * 导出
     */
    public int getCountForSearchExp(SczlglDto sczlglDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(sczlglDto);
    }

    /**
     * 根据搜索条件获取导出信息
     */
    public List<SczlglDto> getListForSearchExp(Map<String, Object> params) {
        SczlglDto sczlglDto = (SczlglDto) params.get("entryData");
        queryJoinFlagExport(params, sczlglDto);
        return dao.getListForSearchExp(sczlglDto);
    }

    /**
     * 根据选择信息获取导出信息
     */
    public List<SczlglDto> getListForSelectExp(Map<String, Object> params) {
        SczlglDto sczlglDto = (SczlglDto) params.get("entryData");
        queryJoinFlagExport(params, sczlglDto);
        return dao.getListForSelectExp(sczlglDto);
    }
    private void queryJoinFlagExport(Map<String, Object> params, SczlglDto sczlglDto) {
        @SuppressWarnings("unchecked")
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
        for (DcszDto dcszDto : choseList) {
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        String sqlcs = sqlParam.toString();
        sczlglDto.setSqlParam(sqlcs);
    }
}

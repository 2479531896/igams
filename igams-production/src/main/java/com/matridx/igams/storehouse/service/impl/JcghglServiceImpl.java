package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.DeviceStateEnum;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.GoodsStatusEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.SbysDto;
import com.matridx.igams.production.dao.matridxsql.VoucherHistoryDao;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.production.service.svcinterface.ISbysService;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.dao.post.IHwxxDao;
import com.matridx.igams.storehouse.dao.post.IJcghglDao;
import com.matridx.igams.storehouse.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class JcghglServiceImpl extends BaseBasicServiceImpl<JcghglDto, JcghglModel, IJcghglDao> implements IJcghglService , IAuditService {

    @Autowired
    IShgcService shgcService;
    @Autowired
    IJcghmxService jcghmxService;
    @Autowired
    VoucherHistoryDao voucherHistoryDao;
    @Autowired
    ICommonService commonService;
    @Autowired
    IHwxxService hwxxService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IRdRecordService rdRecordService;
    @Autowired
    IHwxxDao hwxxDao;
    @Autowired
    ICkhwxxService ckhwxxService;
    @Autowired
    ISbysService sbysService;
    @Autowired
    IDhxxService dhxxService;
    @Autowired
    IDdxxglService ddxxglService;
    @Autowired
    IXtszService xtszService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    private final Logger log = LoggerFactory.getLogger(JcghglServiceImpl.class);

    /**
     * 列表数据
     * @param jcghglDto
     * @return
     */
    @Override
    public List<JcghglDto> getPagedDtoList(JcghglDto jcghglDto){
        List<JcghglDto> list = dao.getPagedDtoList(jcghglDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REPAID.getCode(), "zt", "jcghid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    @Override
    public List<JcghglDto> getPagedAuditRepaid(JcghglDto jcghglDto) {
        // 获取人员ID和履历号
        List<JcghglDto> list = dao.getPagedAuditRepaid(jcghglDto);
        if (CollectionUtils.isEmpty(list))
            return list;
        List<JcghglDto> sqList = dao.getRepaidAuditList(list);
        commonService.setSqrxm(sqList);
        return sqList;
    }

    /**
     * 根据归还单号查询
     * @param
     * @return
     */
    public List<JcghglDto> getDtoByGhdh(String ghdh){
        return dao.getDtoByGhdh(ghdh);
    }
    /**
     * 自动生成归还单号
     * @param jcghglDto
     * @return
     */
    public String generateGhdh(JcghglDto jcghglDto){
        String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
        String prefix = "GH" + "-" + date + "-";
        // 查询流水号
        String serial = dao.getGhdhSerial(prefix);
        return prefix + serial;
    }
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        JcghglDto jcghglDto = (JcghglDto) baseModel;
        jcghglDto.setXgry(operator.getYhid());
        return modSaveRepaid(jcghglDto);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
//        String token = talkUtil.getToken();
        String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
        String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
        String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
        String ICOMM_GH00002 = xxglService.getMsg("ICOMM_GH00002");
        String ICOMM_GH00003 = xxglService.getMsg("ICOMM_GH00003");
        for (ShgcDto shgcDto : shgcList) {
            JcghglDto jcghglDto = new JcghglDto();
            jcghglDto.setJcghid(shgcDto.getYwid());

            jcghglDto.setXgry(operator.getYhid());
            JcghglDto jcghglDto_t = getDtoById(jcghglDto.getJcghid());
            shgcDto.setSqbm(jcghglDto_t.getBm());
            List<SpgwcyDto> spgwcyDtos = commonService.siftJgList(shgcDto.getSpgwcyDtos(), jcghglDto_t.getBm());
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                // 由于合同提交时直接发起了钉钉审批，所以审核不通过时直接采用钉钉的审批拒绝消息即可
                jcghglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), ICOMM_SH00026, StringUtil.replaceMsg(ICOMM_GH00002,
                                            operator.getZsxm(), shgcDto.getShlbmc(), jcghglDto_t.getGhdh(), jcghglDto_t.getBmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                JcghmxDto jcghmxDto=new JcghmxDto();
                jcghmxDto.setJcghid(jcghglDto_t.getJcghid());
                List<JcghmxDto> dtoList = jcghmxService.getDtoList(jcghmxDto);
                if(!CollectionUtils.isEmpty(dtoList)) {
                    List<HwxxDto> hwxxDtos = new ArrayList<>();
                    //获取设备验收id
                    List<String> sbysids = new ArrayList<>();
                    for (JcghmxDto jcghmxDto_t : dtoList) {
                        if ("3".equals(jcghmxDto_t.getLbcskz1()) && StringUtil.isNotBlank(jcghmxDto_t.getSbysid())) {
                            sbysids.add(jcghmxDto_t.getSbysid());
                        }
                        HwxxDto hwxxDto = new HwxxDto();
                        hwxxDto.setHwid(jcghmxDto_t.getHwid());
                        hwxxDto.setKcl(jcghmxDto_t.getGhsl());
                        hwxxDto.setZt(GoodsStatusEnum.GODDS_SCRAP.getCode());
                        hwxxDtos.add(hwxxDto);
                    }
                    int sum = hwxxDao.updateHwxxDtos(hwxxDtos);
                    if (sum <= 0)
                        throw new BusinessException("更新货物信息失败！");
                    //修改设备验收的设备状态
                    if (!CollectionUtils.isEmpty(sbysids)) {
                        SbysDto sbysDto = new SbysDto();
                        sbysDto.setSbzt(DeviceStateEnum.INSTOCK.getCode());
                        sbysDto.setIds(sbysids);
                        sbysDto.setXgry(operator.getYhid());
                        XtszDto xtszDto = xtszService.getDtoById("default.device.location");
                        if(xtszDto!=null && StringUtil.isNotBlank(xtszDto.getSzz())){
                            sbysDto.setSydd(xtszDto.getSzz());
                        }
                        sbysService.updateSbzts(sbysDto);
                        List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.DEVICE_JCGH.getCode());
                        try{
                            if (!CollectionUtils.isEmpty(ddxxglDtolist)) {
                                String ICOMM_GHCS001 = xxglService.getMsg("ICOMM_GHCS001");
                                String ICOMM_GHCS002 = xxglService.getMsg("ICOMM_GHCS002");
                                for (DdxxglDto ddxxglDto : ddxxglDtolist) {
                                    if (StringUtil.isNotBlank(ddxxglDto.getYhm())){
                                        String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/borrowing/returnlistView/returnlistView&jcghid="+jcghglDto_t.getJcghid(), StandardCharsets.UTF_8);

                                        try {
                                            internalbtn = internalbtn+ URLEncoder.encode("?userid="+ddxxglDto.getDdid()+"&urlPrefix="+urlPrefix+"&username="+ddxxglDto.getZsxm()+"&wbfw=1", StandardCharsets.UTF_8);
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                        log.error("internalbtn:"+internalbtn);
                                        List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                                        OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                                        btnJsonList.setTitle("小程序");
                                        btnJsonList.setActionUrl(internalbtn);
                                        btnJsonLists.add(btnJsonList);
                                        talkUtil.sendCardDyxxMessageThread(shgcDto.getShlb(),ddxxglDto.getYhid(),ddxxglDto.getYhm(),
                                                ddxxglDto.getDdid(),
                                                ICOMM_GHCS001,StringUtil.replaceMsg(ICOMM_GHCS002,
                                                        jcghglDto_t.getGhdh(),jcghglDto_t.getDwmc(),
                                                        jcghglDto_t.getGhrq()),
                                                btnJsonLists,"1");
                                    }
                                }
                            }
                        }catch(Exception e){
                            log.error(e.getMessage());
                        }
                    }
                    List<CkhwxxDto> modCkhwxxDtos = new ArrayList<>();
                    List<CkhwxxDto> addCkhwxxDtos = new ArrayList<>();
                    for (JcghmxDto jcghmxDtoX : dtoList) {
                        CkhwxxDto ckhwxxDtoSel = new CkhwxxDto();
                        ckhwxxDtoSel.setCkid(jcghmxDtoX.getCkid());
                        ckhwxxDtoSel.setWlid(jcghmxDtoX.getWlid());
                        CkhwxxDto ckhwxxDto = ckhwxxService.getDtoByWlidAndCkid(ckhwxxDtoSel);
                        if (ckhwxxDto != null) {
                            double kcl = Double.parseDouble(StringUtil.isNotBlank(ckhwxxDto.getKcl())?ckhwxxDto.getKcl():"0");
                            double ghsl = Double.parseDouble(jcghmxDtoX.getGhsl());
                            CkhwxxDto modCkhwxxDto = new CkhwxxDto();
                            modCkhwxxDto.setKcl(String.valueOf(kcl + ghsl));
                            modCkhwxxDto.setCkhwid(ckhwxxDto.getCkhwid());
                            modCkhwxxDtos.add(modCkhwxxDto);
                        } else {
                            CkhwxxDto ckhwxxDtoT = new CkhwxxDto();
                            ckhwxxDtoT.setCkhwid(StringUtil.generateUUID());
                            ckhwxxDtoT.setWlid(jcghmxDtoX.getWlid());
                            ckhwxxDtoT.setKcl(jcghmxDtoX.getGhsl());
                            ckhwxxDtoT.setCkid(jcghmxDtoX.getCkid());
                            ckhwxxDtoT.setYds("0");
                            addCkhwxxDtos.add(ckhwxxDtoT);
                        }
                    }
                    boolean result = true;
                    if(!CollectionUtils.isEmpty(modCkhwxxDtos)){
                        result = ckhwxxService.updateCkhwxxs(modCkhwxxDtos);
                        if(!result){
                            throw new BusinessException("更新仓库货物信息失败!");
                        }
                    }
                    if(!CollectionUtils.isEmpty(addCkhwxxDtos)){
                        result = ckhwxxService.insertCkhwxxs(addCkhwxxDtos);
                        if(!result){
                            throw new BusinessException("新增仓库货物信息失败!");
                        }
                    }
                }
                jcghglDto.setZt(StatusEnum.CHECK_PASS.getCode());
                jcghglDto_t.setZsxm(operator.getZsxm());
                jcghglDto_t.setXgry(jcghglDto.getXgry());
                if (!rdRecordService.determine_Entry(jcghglDto_t.getGhrq())){
                    throw new BusinessException("ICOM99019","该月份已结账，不允许再录入U8数据，请修改单据日期!");
                }
                Map<String, Object> map = rdRecordService.addU8GhData(jcghglDto_t);
                JcghglDto JcghglDto_x = (JcghglDto) map.get("jcghglDto");
                jcghglDto.setGlid(JcghglDto_x.getGlid());
                jcghglDto.setU8ghdh(JcghglDto_x.getU8ghdh());
                String qydh=generteQydh(jcghglDto_t.getGhrq());
                jcghglDto.setQydh(qydh);
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(), spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), ICOMM_SH00006, StringUtil.replaceMsg(ICOMM_GH00003,
                                            operator.getZsxm(), shgcDto.getShlbmc(), jcghglDto_t.getGhdh(), jcghglDto_t.getBmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }else {
                jcghglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                String ICOMM_GH00001 = xxglService.getMsg("ICOMM_GH00001");
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                        spgwcyDto.getYhid(), ICOMM_SH00003, StringUtil.replaceMsg(ICOMM_GH00001,
                                                operator.getZsxm(), shgcDto.getShlbmc(), jcghglDto_t.getGhdh(),
                                                jcghglDto_t.getBmmc(),
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
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
                            		shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00004",operator.getZsxm(),shgcDto.getShlbmc() ,jcghglDto_t.getGhdh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(jcghglDto);
        }

        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        // TODO Auto-generated method stub
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        if (auditParam.isCancelOpe()) {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                String jcghid = shgcDto.getYwid();
                JcghglDto jcghglDto = new JcghglDto();
                jcghglDto.setXgry(operator.getYhid());
                jcghglDto.setJcghid(jcghid);
                jcghglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                update(jcghglDto);
            }
        } else {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                String jcghid = shgcDto.getYwid();
                JcghglDto jcghglDto = new JcghglDto();
                jcghglDto.setXgry(operator.getYhid());
                jcghglDto.setJcghid(jcghid);
                jcghglDto.setZt(StatusEnum.CHECK_NO.getCode());
                update(jcghglDto);
            }
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
        JcghglDto jcghglDto = new JcghglDto();
        jcghglDto.setIds(ids);
        List<JcghglDto> dtoList = dao.getDtoList(jcghglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(JcghglDto dto:dtoList){
                list.add(dto.getJcghid());
            }
        }
        map.put("list",list);
        return map;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean saveRepaidBorrowing(JcghglDto jcghglDto) throws BusinessException {
        List<JcghglDto> jcghglDtos = dao.getDtoByGhdh(jcghglDto.getGhdh());
        if(!CollectionUtils.isEmpty(jcghglDtos)) {
            throw new BusinessException("该归还单号已存在，请更新归还单号！");
        }
        boolean success = dao.insert(jcghglDto) !=0 ;
        if (!success)
            throw new BusinessException("保存主表信息失败！");
        List<JcghmxDto> jcghmxDtos= JSON.parseArray(jcghglDto.getGhmx_json(),JcghmxDto.class);
        List<HwxxDto> hwxxDtos=new ArrayList<>();
        for (JcghmxDto jcghmxDto:jcghmxDtos) {
            HwxxDto hwxxDto=new HwxxDto();
            hwxxDto.setHwid(StringUtil.generateUUID());
            HwxxDto hwxxDto_t = hwxxService.getDtoById(jcghmxDto.getHwid());
            hwxxDto.setWlid(jcghmxDto.getWlid());
            hwxxDto.setScph(hwxxDto_t.getScph());
            hwxxDto.setScrq(hwxxDto_t.getScrq());
            hwxxDto.setYxq(hwxxDto_t.getYxq());
            hwxxDto.setSl(jcghmxDto.getGhsl());
//            hwxxDto.setKcl(jcghmxDto.getGhsl());
            hwxxDto.setCkid(jcghmxDto.getCkid());
            hwxxDto.setKwbh(jcghmxDto.getKwbh());
            hwxxDto.setZsh(jcghmxDto.getZsh());
            hwxxDto.setSbysid(jcghmxDto.getSbysid());
            hwxxDto.setZt(GoodsStatusEnum.REPAID_STORE.getCode());
            hwxxDtos.add(hwxxDto);
            jcghmxDto.setGhmxid(StringUtil.generateUUID());
            jcghmxDto.setJcghid(jcghglDto.getJcghid());
            jcghmxDto.setLrry(jcghglDto.getLrry());
            jcghmxDto.setHwid(hwxxDto.getHwid());

        }
        success =  hwxxDao.insertHwxxList(hwxxDtos);
        if (!success)
            throw new BusinessException("保存货物信息失败！");
        success = jcghmxService.insertList(jcghmxDtos);
        if (!success)
            throw new BusinessException("保存借出归还信息失败！");

        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean modSaveRepaid(JcghglDto jcghglDto) throws BusinessException {
        boolean success = dao.update(jcghglDto) != 0;
        if (!success)
            throw new BusinessException("更新主表信息失败！");

        success = jcghmxService.deleteById(jcghglDto.getJcghid());
        if (!success)
            throw new BusinessException("删除原有明细数据失败！");
        List<JcghmxDto> jcghmxDtos= JSON.parseArray(jcghglDto.getGhmx_json(),JcghmxDto.class);
        List<HwxxDto> hwxxDtos=new ArrayList<>();
        if(!CollectionUtils.isEmpty(jcghmxDtos)) {
            for (JcghmxDto dto:jcghmxDtos) {
                HwxxDto hwxxDto=new HwxxDto();
                hwxxDto.setHwid(dto.getHwid());
                hwxxDto.setCkid(dto.getCkid());
                hwxxDto.setKwbh(dto.getKwbh());
                hwxxDto.setSl(dto.getGhsl());
                hwxxDtos.add(hwxxDto);
                dto.setGhmxid(StringUtil.generateUUID());
                dto.setJcghid(jcghglDto.getJcghid());
                dto.setLrry(jcghglDto.getXgry());
            }
        }
        int sum = hwxxDao.updateHwxxDtos(hwxxDtos);
        if (sum<=0)
            throw new BusinessException("更新货物信息失败！");
        success = jcghmxService.insertList(jcghmxDtos);
        if (!success)
            throw new BusinessException("保存借出归还信息失败！");
        return true;
    }

    /**
     * 废弃
     * @param jcghglDto
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean discard(JcghglDto jcghglDto) {
        boolean isSuccess;
        JcghmxDto jcghmxDto=new JcghmxDto();
        jcghmxDto.setIds(jcghglDto.getIds());
        List<JcghmxDto> dtoList = jcghmxService.getDtoList(jcghmxDto);
        //删除归还明细
        jcghmxDto.setScry(jcghglDto.getScry());
        jcghmxService.delete(jcghmxDto);
        //删除hwxx表
        if(!CollectionUtils.isEmpty(dtoList)){
            //获取设备验收id
            List<String> sbysids = new ArrayList<>();
            List<String> hwids=new ArrayList<>();
            for(JcghmxDto dto:dtoList){
                if ("3".equals(dto.getLbcskz1())){
                    sbysids.add(dto.getSbysid());
                }
                hwids.add(dto.getHwid());
            }
            //修改设备验收的设备状态
            if (!CollectionUtils.isEmpty(sbysids)){
                SbysDto sbysDto = new SbysDto();
                sbysDto.setSbzt(DeviceStateEnum.PUT.getCode());
                sbysDto.setIds(sbysids);
                sbysDto.setXgry(jcghglDto.getScry());
                sbysService.updateSbzts(sbysDto);
            }
            HwxxDto hwxxDto = new HwxxDto();
            hwxxDto.setIds(hwids);
            hwxxDto.setScry(jcghglDto.getScry());
            hwxxService.deleteHwxxDtos(hwxxDto);
        }
        //删除归还主表
        isSuccess=delete(jcghglDto);
        return isSuccess;
    }


    /**
     * 删除
     * @param jcghglDto
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deleteDto(JcghglDto jcghglDto) {
        boolean isSuccess=false;
        List<String> ids=jcghglDto.getIds();
        List<String> tgids=jcghglDto.getTgids();
        //执行未审核通过的删除操作
        if(!CollectionUtils.isEmpty(ids)) {
            JcghmxDto jcghmxDto=new JcghmxDto();
            jcghmxDto.setIds(ids);
            List<JcghmxDto> dtoList = jcghmxService.getDtoList(jcghmxDto);
            //删除归还明细
            jcghmxDto.setScry(jcghglDto.getScry());
            jcghmxService.delete(jcghmxDto);
            //删除hwxx表
            if(!CollectionUtils.isEmpty(dtoList)){
                List<String> hwids=new ArrayList<>();
                //获取设备验收id
                List<String> sbysids = new ArrayList<>();
                for(JcghmxDto dto:dtoList){
                    if (StringUtil.isNotBlank(dto.getSbysid())){
                        sbysids.add(dto.getSbysid());
                    }
                    hwids.add(dto.getHwid());
                }
                //修改设备验收的设备状态
                if (!CollectionUtils.isEmpty(sbysids)){
                    SbysDto sbysDto = new SbysDto();
                    sbysDto.setSbzt(DeviceStateEnum.PUT.getCode());
                    sbysDto.setIds(sbysids);
                    sbysDto.setXgry(jcghglDto.getScry());
                    sbysService.updateSbzts(sbysDto);
                }
                HwxxDto hwxxDto = new HwxxDto();
                hwxxDto.setIds(hwids);
                hwxxDto.setScry(jcghglDto.getScry());
                hwxxService.deleteHwxxDtos(hwxxDto);
            }
            //删除归还主表
            isSuccess=delete(jcghglDto);
        }
        //执行审核通过的删除操作
        if(!CollectionUtils.isEmpty(tgids)) {
            JcghmxDto jcghmxDto=new JcghmxDto();
            jcghmxDto.setIds(tgids);
            List<JcghmxDto> dtoList = jcghmxService.getDtoList(jcghmxDto);
            //删除归还明细
            jcghmxDto.setScry(jcghglDto.getScry());
            jcghmxService.delete(jcghmxDto);
            if(!CollectionUtils.isEmpty(dtoList)){
                List<String> hwids=new ArrayList<>();
                for(JcghmxDto dto:dtoList){
                    BigDecimal sl=new BigDecimal(dto.getSl());
                    BigDecimal kcl=new BigDecimal(dto.getKcl());
                    //判断hwxx表的数量和库存量是否相等，不相等不允许删除
                    if(sl.compareTo(kcl)!=0){
                        return false;
                    }else{
                        //相等的话则继续执行删除操作
                        hwids.add(dto.getHwid());
                        //修改ckhwxx的库存量
                        CkhwxxDto ckhwxxDto_sel = new CkhwxxDto();
                        ckhwxxDto_sel.setCkid(dto.getCkid());
                        ckhwxxDto_sel.setWlid(dto.getWlid());
                        CkhwxxDto ckhwxxDto = ckhwxxService.getDtoByWlidAndCkid(ckhwxxDto_sel);
                        BigDecimal ckkcl=new BigDecimal(ckhwxxDto.getKcl());
                        BigDecimal ghsl=new BigDecimal(dto.getGhsl());
                        ckhwxxDto.setKcl(String.valueOf(ckkcl.subtract(ghsl)));
                        ckhwxxService.updateCkhwxx(ckhwxxDto);
                    }
                }
                HwxxDto hwxxDto = new HwxxDto();
                hwxxDto.setIds(hwids);
                hwxxDto.setScry(jcghglDto.getScry());
                hwxxService.deleteHwxxDtos(hwxxDto);
            }
            //删除归还主表
            jcghglDto.setIds(tgids);
            isSuccess=delete(jcghglDto);
        }
        return isSuccess;
    }

    /**
     * 请验单号
     */
    public String generteQydh(String date) {
        // 生成规则: IT-2021-0823-01    部门参数扩展-年-月日-流水号(两位) 。
        String[] split = date.split("-");
        date=split[0]+"-"+split[1]+split[2];
        String prefix = "L-" + date + "-";
        //查询流水号
        String serial = dao.generteQydh(prefix);
        return prefix+serial;
    }
}

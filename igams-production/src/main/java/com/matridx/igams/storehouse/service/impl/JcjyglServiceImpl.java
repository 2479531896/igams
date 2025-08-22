package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSON;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.DeviceStateEnum;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.production.dao.entities.SbysDto;
import com.matridx.igams.production.dao.entities.VoucherHistoryDto;
import com.matridx.igams.production.dao.matridxsql.VoucherHistoryDao;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.production.service.svcinterface.ISbysService;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.dao.post.IJcjyglDao;
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


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JcjyglServiceImpl extends BaseBasicServiceImpl<JcjyglDto, JcjyglModel, IJcjyglDao> implements IJcjyglService,IAuditService {

    @Autowired
    private IJcjyxxService jcjyxxService;

    @Autowired
    private ICkhwxxService ckhwxxService;

    @Autowired
    private IRdRecordService rdRecordService;

    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;

    @Autowired
    DingTalkUtil talkUtil;

    @Autowired
    IXxglService xxglService;

    @Autowired
    ICommonService commonService;

    @Autowired
    private IJccglService jccglService;

    @Autowired
    IShgcService shgcService;

    @Autowired
    IJcjymxService jcjymxService;

    @Autowired
    IJcsjService jcsjService;

    @Autowired
    IHwxxService hwxxService;

    @Autowired
    ICkglService ckglService;

    @Autowired
    ICkmxService ckmxService;

    @Autowired
    VoucherHistoryDao voucherHistoryDao;

    @Autowired
    IDdxxglService ddxxglService;

    @Autowired
    ISbysService sbysService;

    private final Logger log = LoggerFactory.getLogger(JcjyglServiceImpl.class);

    @Override
    public String generateJydh(JcjyglDto jcjyglDto) {
//        // 生成规则: LL-20201022-01 LL-年份日期-流水号 。
//        String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
//        String prefix = "JY" + "-" + date + "-";
//        // 查询流水号
//        String serial = dao.getJydhSerial(prefix);
//        return prefix + serial;
        String jydh;
        VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
        voucherHistoryDto.setCardNumber("HYJCGH001");
        //获取最大流水号
        VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
        if(voucherHistoryDto_t!=null) {
            String serialnum;
            String serial;
            List<JcjyglDto> jcjyglDtos;
            int i = 1;
            do {
                serialnum = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber()) + i);
                serial = "0000000000" + serialnum;
                serial = serial.substring(serial.length() - 10);
                jcjyglDtos = dao.getDtoByJydh(serial);
                i+=1;
            }
            while (jcjyglDtos !=null && jcjyglDtos.size()>0);
            jydh = serial;
        }else {
            voucherHistoryDto.setcNumber("1");
            voucherHistoryDto.setbEmpty("0");
            jydh = "0000000001";
            voucherHistoryDao.insert(voucherHistoryDto);

        }
        return jydh;

    }

    @Override
    public String generateJydhOA(JcjyglDto jcjyglDto) {
        // 生成规则: LL-20201022-01 LL-年份日期-流水号 。
        String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
        String prefix = "JY" + "-" + date + "-";
        // 查询流水号
        String serial = dao.getJydhSerial(prefix);
        return prefix + serial;
    }

    /**
     * 获取列表信息
     */
    @Override
    public List<JcjyglDto> getPagedDtoList(JcjyglDto jcjyglDto){
        List<JcjyglDto> list = dao.getPagedDtoList(jcjyglDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_BORROWING.getCode(), "zt", "jcjyid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean saveAddBorrowing(JcjyglDto jcjyglDto) throws BusinessException {
        List<JcjyglDto> jcjyglDtos = dao.getDtoByJydh(jcjyglDto.getJydh());
        if(!CollectionUtils.isEmpty(jcjyglDtos)) {
            throw new BusinessException("该借用单号已存在，请更新借用单号！");
        }
//        VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
//        voucherHistoryDto.setCardNumber("HYJCGH001");
//        //获取最大流水号
//        VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
//        if(voucherHistoryDto_t!=null) {
//
//            voucherHistoryDto_t.setcNumber(jcjyglDto.getJydh());
//            //更新最大单号值
//            Boolean success = voucherHistoryDao.update(voucherHistoryDto_t)!=0;
//            if (!success)
//                throw new BusinessException("保存借用单号信息失败！");
//        }else {
//            voucherHistoryDto.setcNumber("1");
//            voucherHistoryDto.setbEmpty("0");
//            Boolean success = voucherHistoryDao.insert(voucherHistoryDto)!=0;
//            if (!success)
//                throw new BusinessException("保存借用单号信息失败！");
//
//        }
        jcjyglDto.setHtbh(jcjyglDto.getHtid());
        boolean success = dao.insert(jcjyglDto) != 0;
        if (!success)
            throw new BusinessException("保存主表信息失败！");
        List<JcjyxxDto> jcjyxxDtos= JSON.parseArray(jcjyglDto.getJymx_json(),JcjyxxDto.class);
        List<CkhwxxDto> ckhwxxDtos = new ArrayList<>();
        List<JccglDto> jccglDtos = new ArrayList<>();
        for (JcjyxxDto jcjyxxDto:jcjyxxDtos) {
            jcjyxxDto.setJyxxid(StringUtil.generateUUID());
            jcjyxxDto.setJcjyid(jcjyglDto.getJcjyid());
            jcjyxxDto.setLrry(jcjyglDto.getLrry());
            CkhwxxDto ckhwxxDto = new CkhwxxDto();
            ckhwxxDto.setCkhwid(jcjyxxDto.getCkhwid());
            CkhwxxDto ckhwxxDto1 =  ckhwxxService.getJbxxByCkhwid(ckhwxxDto);
            if (StringUtil.isBlank(ckhwxxDto1.getYds())){
                ckhwxxDto1.setYds("0");
            }
            double yds = Double.parseDouble(ckhwxxDto1.getYds()) + Double.parseDouble(jcjyxxDto.getJcsl());
            ckhwxxDto.setYds(Double.toString(yds));
            ckhwxxDtos.add(ckhwxxDto);
            JccglDto jccglDto = new JccglDto();
            jccglDto.setRyid(jcjyxxDto.getLrry());
            jccglDto.setCkhwid(jcjyxxDto.getCkhwid());
            jccglDtos.add(jccglDto);
            success = jcjyxxService.insert(jcjyxxDto);
            if (!success)
                throw new BusinessException("保存借出借用信息失败！");
        }
        success = ckhwxxService.updateCkhwxxs(ckhwxxDtos);
        if (!success)
            throw new BusinessException("更新预定数失败！");
        JccglDto jccglDto = new JccglDto();
        jccglDto.setRyid(jcjyglDto.getLrry());
        List<JccglDto> jccDtoList = jccglService.getJccDtoList(jccglDto);
        StringBuilder ids = new StringBuilder();
        if(!CollectionUtils.isEmpty(jccDtoList)) {
            for (JccglDto jccgl:jccDtoList) {
                for (JccglDto jcc:jccglDtos) {
                    if (jccgl.getCkhwid().equals(jcc.getCkhwid())) {
                        if (ids.length()==0){
                            ids.append(jccgl.getCkhwid());
                        }else{
                            ids.append(",").append(jccgl.getCkhwid());
                        }

                    }
                }

            }
            jccglDto.setIds(ids.toString());
            success = jccglService.delete(jccglDto);
            if (!success)
                throw new BusinessException("借出车更新失败！");
        }

        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delBorrowing(JcjyglDto jcjyglDto) throws BusinessException {
        List<JcjyglDto> dtoList = dao.getDtoListByIds(jcjyglDto);
        JcjyxxDto jcjyxxDto = new JcjyxxDto();
        jcjyxxDto.setIds(jcjyglDto.getIds());
        List<JcjyxxDto> groupByJyxx = jcjyxxService.getDtoListGroupByJyxx(jcjyxxDto);
        List<CkhwxxDto> updataCkhwxxDtos = new ArrayList<>();
        List<HwxxDto> updataHwxxDtos = new ArrayList<>();
        //获取设备hwid
        List<SbysDto> sbysDtos = new ArrayList<>();
        JcjymxDto jcjymxDto1 = new JcjymxDto();
        jcjymxDto1.setIds(jcjyglDto.getIds());
        List<JcjymxDto> jcjymxDtos = jcjymxService.getDtoList(jcjymxDto1);
        if (!CollectionUtils.isEmpty(jcjymxDtos)){
            for (JcjymxDto jcjymxDto_t : jcjymxDtos) {
                if ("3".equals(jcjymxDto_t.getLbcskz1())){
                    SbysDto sbysDto = new SbysDto();
                    sbysDto.setTzzt("0");
                    sbysDto.setSbzt(DeviceStateEnum.LEAVE.getCode());
                    sbysDto.setSbysid(jcjymxDto_t.getSbysid());
                    sbysDto.setYsbzt(jcjymxDto_t.getSbzt());
                    sbysDto.setXgry(jcjyglDto.getScry());
                    sbysDtos.add(sbysDto);
                }
            }
            //修改设备验收的台账
            if (!CollectionUtils.isEmpty(sbysDtos)){
                sbysService.updateTzs(sbysDtos);
            }
        }
        if(!CollectionUtils.isEmpty(groupByJyxx)) {
            for (JcjyxxDto jcjyxxDto1 : groupByJyxx) {
                CkhwxxDto ckhwxxDto = new CkhwxxDto();
                ckhwxxDto.setCkhwid(jcjyxxDto1.getCkhwid());
                //审核通过，库存量+借出量
                if (StatusEnum.CHECK_PASS.getCode().equals(jcjyxxDto1.getZt())){
                    ckhwxxDto.setKcl(jcjyxxDto1.getJysl());
                    ckhwxxDto.setKclbj("1");
                    updataCkhwxxDtos.add(ckhwxxDto);
                }else{
                    //未通过，删除预定数
                    ckhwxxDto.setYds(jcjyxxDto1.getJcsl());
                    ckhwxxDto.setYdsbj("0");
                    updataCkhwxxDtos.add(ckhwxxDto);
                }
            }
        }

        if (!CollectionUtils.isEmpty(dtoList)){
            boolean flag = false;
            for (JcjyglDto dto : dtoList) {
                //审核通过，修改ckhwxx得库存量，库存量=库存量-借出信息得借出数量，修改hwxx得库存量，库存量=库存量-明细得借用数量
                if (StatusEnum.CHECK_PASS.getCode().equals(dto.getZt())){
                    if (StringUtil.isNotBlank(dto.getHwid())){
                        HwxxDto hwxxDto = new HwxxDto();
                        hwxxDto.setHwid(dto.getHwid());
                        hwxxDto.setAddkcl(dto.getJysl());
                        updataHwxxDtos.add(hwxxDto);
                        flag = true;
                    }
                //审核未通过得单子，修改ckhwxx得yds,yds=yds-借出信息表得借出数量，修改hwxx得yds,yds=yds-借出明细得借出数量，修改借出信息表删除标记和借出明细表删除标记。
                }else {
                    if (StringUtil.isNotBlank(dto.getHwid())) {
                        HwxxDto hwxxDto = new HwxxDto();
                        hwxxDto.setHwid(dto.getHwid());
                        hwxxDto.setYds(dto.getJysl());
                        hwxxDto.setYdsbj("0");
                        updataHwxxDtos.add(hwxxDto);
                        flag = true;
                    }
                }
            }
            if (!CollectionUtils.isEmpty(updataCkhwxxDtos)){
                boolean isSuccess = ckhwxxService.updateByCkhwid(updataCkhwxxDtos);
                if (!isSuccess){
                    throw new BusinessException("更新预定数或库存量失败！");
                }
            }
            if (!CollectionUtils.isEmpty(updataHwxxDtos)){
                boolean isSuccess = hwxxService.updateHwxxDtos(updataHwxxDtos);
                if (!isSuccess){
                    throw new BusinessException("更新预定数或库存量失败！");
                }
            }
            boolean deletexx = jcjyxxService.deleteByIds(jcjyglDto.getIds());
            if (!deletexx){
                throw new BusinessException("删除借出借用信息失败！");
            }
            if (flag){
                boolean isSuccess = jcjymxService.deleteByIds(jcjyglDto.getIds());
                if (!isSuccess){
                    throw new BusinessException("删除借出借用明细失败！");
                }
            }
        }
        boolean isSuccess = dao.deleteByIds(jcjyglDto);
        if (!isSuccess){
            throw new BusinessException("删除失败！");
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean modSaveBorrowing(JcjyglDto jcjyglDto) throws BusinessException {
        JcjyglDto jcjyglDto_old = dao.getDtoById(jcjyglDto.getJcjyid());
        List<JcjyglDto> jcjyglDtos = dao.getDtoByJydh(jcjyglDto_old.getJydh());
        if (null != jcjyglDtos && jcjyglDtos.size()>1){
            throw new BusinessException("该借用单号已存在，请更新借用单号！");
        }
        jcjyglDto.setHtbh(jcjyglDto.getHtid());
        boolean success = dao.update(jcjyglDto) != 0;
        if (!success)
            throw new BusinessException("更新主表信息失败！");
        JcjyxxDto jcjyxxDto1 = new JcjyxxDto();
        jcjyxxDto1.setJcjyid(jcjyglDto.getJcjyid());
        List<JcjyxxDto> jcjyxxDtoList = jcjyxxService.getDtoListInfo(jcjyxxDto1);
        List<JcjyxxDto> jcjyxxDtos= JSON.parseArray(jcjyglDto.getJymx_json(),JcjyxxDto.class);
        List<CkhwxxDto> ckhwxxDtos = new ArrayList<>();
        List<JcjyxxDto> jcjyxxDtos_mod = new ArrayList<>();
        List<JcjyxxDto> jcjyxxDtos_add;
        List<JcjyxxDto> jcjyxxDtos_del;
        List<JcjyxxDto> jcjyxxDtos_before = new ArrayList<>();
        if(!CollectionUtils.isEmpty(jcjyxxDtos)) {
            for (JcjyxxDto jcjyxxDto:jcjyxxDtos) {
                for (JcjyxxDto jcjyxx:jcjyxxDtoList) {
                    if (StringUtil.isNotBlank(jcjyxxDto.getJyxxid())){
                        if (jcjyxxDto.getJyxxid().equals(jcjyxx.getJyxxid())){
                            jcjyxxDto.setXgry(jcjyglDto.getXgry());
                            CkhwxxDto ckhwxxDto = new CkhwxxDto();
                            ckhwxxDto.setCkhwid(jcjyxxDto.getCkhwid());
                            CkhwxxDto ckhwxxDto1 =  ckhwxxService.getJbxxByCkhwid(ckhwxxDto);
                            if (StringUtil.isBlank(ckhwxxDto1.getYds())){
                                ckhwxxDto1.setYds("0");
                            }
                            double yds = Double.parseDouble(ckhwxxDto1.getYds()) - Double.parseDouble(jcjyxx.getJcsl()) + Double.parseDouble(jcjyxxDto.getJcsl());
                            ckhwxxDto.setYds(Double.toString(yds));
                            ckhwxxDtos.add(ckhwxxDto);
                            jcjyxxDtos_mod.add(jcjyxxDto);
                            jcjyxxDtos_before.add(jcjyxx);
                            success = jcjyxxService.update(jcjyxxDto);
                            if (!success)
                                throw new BusinessException("更新借出借用信息失败！");
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(jcjyxxDtos_mod)){
                for (JcjyxxDto jcjyxxDto:jcjyxxDtos_mod) {
                    jcjyxxDtos.remove(jcjyxxDto);
                }
            }
            if (!CollectionUtils.isEmpty(jcjyxxDtos_before)){
                for (JcjyxxDto jcjyxxDto:jcjyxxDtos_before) {
                    jcjyxxDtoList.remove(jcjyxxDto);
                }
            }
            jcjyxxDtos_add = jcjyxxDtos;
            if (!CollectionUtils.isEmpty(jcjyxxDtos_add)){
                for (JcjyxxDto jcjyxxDto:jcjyxxDtos_add) {
                    jcjyxxDto.setJyxxid(StringUtil.generateUUID());
                    jcjyxxDto.setJcjyid(jcjyglDto.getJcjyid());
                    jcjyxxDto.setLrry(jcjyglDto.getXgry());
                    CkhwxxDto ckhwxxDto = new CkhwxxDto();
                    ckhwxxDto.setCkhwid(jcjyxxDto.getCkhwid());
                    CkhwxxDto ckhwxxDto1 =  ckhwxxService.getJbxxByCkhwid(ckhwxxDto);
                    if (StringUtil.isBlank(ckhwxxDto1.getYds())){
                        ckhwxxDto1.setYds("0");
                    }
                    double yds = Double.parseDouble(ckhwxxDto1.getYds()) + Double.parseDouble(jcjyxxDto.getJcsl());
                    ckhwxxDto.setYds(Double.toString(yds));
                    ckhwxxDtos.add(ckhwxxDto);
                    success = jcjyxxService.insert(jcjyxxDto);
                    if (!success)
                        throw new BusinessException("更新借出借用信息失败！");
                }

            }
            jcjyxxDtos_del = jcjyxxDtoList;
            if (!CollectionUtils.isEmpty(jcjyxxDtos_del)){
                for (JcjyxxDto jcjyxxDto:jcjyxxDtos_del) {
                    jcjyxxDto.setScry(jcjyglDto.getXgry());
                    CkhwxxDto ckhwxxDto = new CkhwxxDto();
                    ckhwxxDto.setCkhwid(jcjyxxDto.getCkhwid());
                    CkhwxxDto ckhwxxDto1 =  ckhwxxService.getJbxxByCkhwid(ckhwxxDto);
                    if (StringUtil.isBlank(ckhwxxDto1.getYds())){
                        ckhwxxDto1.setYds("0");
                    }
                    double yds = Double.parseDouble(ckhwxxDto1.getYds()) - Double.parseDouble(jcjyxxDto.getJcsl());
                    ckhwxxDto.setYds(Double.toString(yds));
                    ckhwxxDtos.add(ckhwxxDto);
                    success = jcjyxxService.delete(jcjyxxDto);
                    if (!success)
                        throw new BusinessException("更新借出借用信息失败！");
                }
            }
            if (!CollectionUtils.isEmpty(ckhwxxDtos)){
                success = ckhwxxService.updateCkhwxxs(ckhwxxDtos);
                if (!success)
                    throw new BusinessException("更新预定数失败！");
            }
        }
        return true;
    }

    @Override
    public List<JcjyglDto> getPagedAuditJcjyglDto(JcjyglDto jcjyglDto) {
        // 获取人员ID和履历号
        List<JcjyglDto> list = dao.getPagedAuditJcjyglDto(jcjyglDto);
        if (CollectionUtils.isEmpty(list))
            return list;
        List<JcjyglDto> sqList = dao.getAuditListJcjyglDto(list);
        commonService.setSqrxm(sqList);
        return sqList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam)
            throws BusinessException {
        JcjyglDto jcjyglDto = (JcjyglDto) baseModel;
        jcjyglDto.setXgry(operator.getYhid());
        boolean isSuccess = true;
        if (StringUtil.isBlank(jcjyglDto.getDdbj())){
            if("1".equals(jcjyglDto.getXsbj())) {
                isSuccess = modSaveBorrowing(jcjyglDto);
            }else {
                isSuccess = seniorModSaveBorrowing(jcjyglDto);
            }
        }
        return isSuccess;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean seniorModSaveBorrowing(JcjyglDto jcjyglDto) throws BusinessException {
        JcjyglDto jcjyglDto_old = dao.getDtoById(jcjyglDto.getJcjyid());
        List<JcjyglDto> jcjyglDtos = dao.getDtoByJydh(jcjyglDto_old.getJydh());
        JcjymxDto jcjymxDto1 = new JcjymxDto();
        jcjymxDto1.setJcjyid(jcjyglDto.getJcjyid());
        List<JcjymxDto> jcjymxDtoList = jcjymxService.getDtoList(jcjymxDto1);
        if (null != jcjyglDtos && jcjyglDtos.size()>1){
            throw new BusinessException("该借用单号已存在，请更新借用单号！");
        }
//        VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
//        voucherHistoryDto.setCardNumber("HYJCGH001");
//        //获取最大流水号
//        VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
//        if(voucherHistoryDto_t!=null) {
//            if (!String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber()) + 1).equals(String.valueOf(Integer.parseInt(jcjyglDto.getJydh())))){
//                throw new BusinessException("借用单号发生错误！");
//            }
//        }else {
//            voucherHistoryDto.setcNumber("1");
//            voucherHistoryDto.setbEmpty("0");
//            Boolean success = voucherHistoryDao.insert(voucherHistoryDto)!=0;
//            if (!success)
//                throw new BusinessException("保存借用单号信息失败！");
//
//        }
        jcjyglDto.setHtbh(jcjyglDto.getHtid());
        boolean success = dao.update(jcjyglDto) != 0;
        if (!success)
            throw new BusinessException("更新主表信息失败！");
        List<JcjymxDto> jcjymxDtos= JSON.parseArray(jcjyglDto.getJymx_json(),JcjymxDto.class);
        List<HwxxDto> hwxxDtos = new ArrayList<>();
        List<HwxxDto> hwxxDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(jcjymxDtoList) && !CollectionUtils.isEmpty(jcjymxDtos)) {
            for (JcjymxDto jcjymxDto2 : jcjymxDtoList) {
                HwxxDto hwxxDto1 = new HwxxDto();
                hwxxDto1.setHwid(jcjymxDto2.getHwid());
                hwxxDto1.setYds(jcjymxDto2.getJysl());
                hwxxDto1.setYdsbj("0");
                hwxxDtoList.add(hwxxDto1);
            }
            success = hwxxService.updateHwxxDtos(hwxxDtoList);
            if (!success)
                throw new BusinessException("更新预定数失败！");
            success = jcjymxService.delete(jcjymxDtoList.get(0));
            if (!success)
                throw new BusinessException("更新明细表信息失败！");
        }
        if(!CollectionUtils.isEmpty(jcjymxDtos)) {
            for (JcjymxDto jcjymxDto:jcjymxDtos) {
                jcjymxDto.setLrry(jcjyglDto.getXgry());
                jcjymxDto.setJymxid(StringUtil.generateUUID());
                HwxxDto hwxxDto = hwxxService.getDtoById(jcjymxDto.getHwid());
                HwxxDto hwxxDto1 = new HwxxDto();
                hwxxDto1.setHwid(jcjymxDto.getHwid());
                hwxxDto1.setXgry(jcjyglDto.getXgry());
                if (null != hwxxDto){
                    hwxxDto1.setYds(jcjymxDto.getJysl());
                    hwxxDto1.setYdsbj("1");
                    hwxxDtos.add(hwxxDto1);
                }
                jcjymxService.insert(jcjymxDto);
            }
            success = hwxxService.updateHwxxDtos(hwxxDtos);
            if (!success)
                throw new BusinessException("更新预定数失败！");
        }
        return true;
    }

    @SuppressWarnings("unchecked")
	@Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
            throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
//        String token = talkUtil.getToken();
        String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
        String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
        String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
        String ICOMM_SH00082 = xxglService.getMsg("ICOMM_SH00082");
        for (ShgcDto shgcDto : shgcList) {
            JcjyglDto jcjyglDto = new JcjyglDto();
            jcjyglDto.setJcjyid(shgcDto.getYwid());
            jcjyglDto.setShry(operator.getYhid());
            SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datetime = tempDate.format(new java.util.Date());
            jcjyglDto.setShrq(datetime);
            jcjyglDto.setXgry(operator.getYhid());
            JcjyglDto jcjyglDto1 = getDtoById(jcjyglDto.getJcjyid());
            shgcDto.setSqbm(jcjyglDto1.getBm());
            List<SpgwcyDto> spgwcyDtos = commonService.siftJgList(shgcDto.getSpgwcyDtos(), jcjyglDto1.getBm());
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                // 由于合同提交时直接发起了钉钉审批，所以审核不通过时直接采用钉钉的审批拒绝消息即可
                jcjyglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), ICOMM_SH00026,
                                    xxglService.getMsg("ICOMM_SH00108", operator.getZsxm(), shgcDto.getShlbmc(),
                                            jcjyglDto1.getJydh(), jcjyglDto1.getBm(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {

                jcjyglDto.setZt(StatusEnum.CHECK_PASS.getCode());
                jcjyglDto1.setZsxm(operator.getZsxm());
                jcjyglDto1.setXgry(jcjyglDto.getXgry());
                if (!rdRecordService.determine_Entry(jcjyglDto1.getJyrq())){
                    throw new BusinessException("ICOM99019","该月份已结账，不允许再录入U8数据，请修改单据日期!");
                }
                Map<String, Object> map = rdRecordService.addU8JyData(jcjyglDto1);
                JcjyglDto jcjyglDto2 = (JcjyglDto) map.get("jcjyglDto");
                List<HwxxDto> hwxxDtos=(List<HwxxDto>)map.get("hwxxDtos");
                //List<CkhwxxDto> ckhwxxDtos=(List<CkhwxxDto>)map.get("ckhwxxDtos");
                List<CkglDto> ckglDtos=(List<CkglDto>)map.get("ckglDtos");
                List<CkmxDto> ckmxDtos=(List<CkmxDto>)map.get("ckmxDtos");
                List<JcjymxDto> jcjymxDtosT=(List<JcjymxDto>)map.get("jcjymxDtos");
                boolean success = hwxxService.updateHwInfos(hwxxDtos);
                if (!success)
                    throw new BusinessException("msg", "更新货物信息失败！");
//                success = ckhwxxService.updateCkhwxxs(ckhwxxDtos);
//                if (!success)
//                    throw new BusinessException("msg", "更新仓库货物信息失败！");
                success = ckglService.insertCkgls(ckglDtos);
                if (!success){
                    throw new BusinessException("msg", "添加出库数据出错！");
                }
                success = ckmxService.insertckmxs(ckmxDtos);
                if (!success){
                    throw new BusinessException("msg", "添加出库明细数据出错！");
                }
                if(!CollectionUtils.isEmpty(jcjymxDtosT)){
                    success = jcjymxService.updateList(jcjymxDtosT);
                    if(!success){
                        throw new BusinessException("msg","更新借用明细U8关联id失败");
                    }
                }
                jcjyglDto.setGlid(jcjyglDto2.getGlid());
                jcjyglDto.setU8jydh(jcjyglDto2.getU8jydh());
                JcjymxDto jcjymxDto1 = new JcjymxDto();
                jcjymxDto1.setJcjyid(jcjyglDto.getJcjyid());
                List<JcjymxDto> jcjymxDtoList = jcjymxService.getDtoListGroupByXx(jcjymxDto1);
                JcjyxxDto jcjyxxDto = new JcjyxxDto();
                jcjyxxDto.setIds(jcjyglDto.getJcjyid());
                List<JcjyxxDto> jcjyxxDtoList = jcjyxxService.getDtoListGroupByJyxx(jcjyxxDto);
                List<CkhwxxDto> ckhwxxDtoList = new ArrayList<>();
                for (JcjyxxDto dto : jcjyxxDtoList) {
                    CkhwxxDto ckhwxxDto = new CkhwxxDto();
                    ckhwxxDto.setCkhwid(dto.getCkhwid());
                    ckhwxxDto.setYds(dto.getJcsl());
                    ckhwxxDto.setYdsbj("0");
                    ckhwxxDto.setKcl(dto.getJysl());
                    ckhwxxDto.setKclbj("0");
                    ckhwxxDtoList.add(ckhwxxDto);
                }

                success = ckhwxxService.updateByCkhwid(ckhwxxDtoList);
                if (!success)
                    throw new BusinessException("msg", "更新仓库货物信息预定数失败！");
                //



                List<JcjymxDto> dtoList = jcjymxService.getDtoList(jcjymxDto1);
                List<SbysDto> sbysDtos = new ArrayList<>();
                boolean flag = false;
                for (JcjymxDto jcjymxDto : dtoList) {
                    if ("3".equals(jcjymxDto.getLbcskz1())){
                        flag = true;
                        SbysDto sbysDto = new SbysDto();
                        sbysDto.setTzzt("1");
                        sbysDto.setSbzt(DeviceStateEnum.PUT.getCode());
                        sbysDto.setYsbzt(jcjymxDto.getSbzt());
                        sbysDto.setSbysid(jcjymxDto.getSbysid());
                        sbysDto.setXgry(operator.getYhid());
                        sbysDtos.add(sbysDto);
                    }
                }
                //修改设备验收的台账
                if (!CollectionUtils.isEmpty(sbysDtos)){
                    sbysService.updateTzs(sbysDtos);
                }
                if (flag){
                    List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.DEVICE_JCJY.getCode());
                    try{
                        if (!CollectionUtils.isEmpty(ddxxglDtolist)) {
                            String ICOMM_SH00103 = xxglService.getMsg("ICOMM_SH00103");
                            String ICOMM_SH00104 = xxglService.getMsg("ICOMM_SH00104");
                            for (DdxxglDto ddxxglDto : ddxxglDtolist) {
                                if (StringUtil.isNotBlank(ddxxglDto.getYhm())){
                                    String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/borrowing/borrowinglistView/borrowinglistView&jcjyid="+jcjyglDto1.getJcjyid(), StandardCharsets.UTF_8);

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
                                            ICOMM_SH00103,StringUtil.replaceMsg(ICOMM_SH00104,
                                                    jcjyglDto1.getJydh(),jcjyglDto1.getDwmc(),
                                                    jcjyglDto1.getLxr(), jcjyglDto1.getLxfs()),
                                            btnJsonLists,"1");
                                }
                            }
                        }
                    }catch(Exception e){
                        log.error(e.getMessage());
                    }
                }
                if (!CollectionUtils.isEmpty(spgwcyDtos) ) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(), spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00006, StringUtil.replaceMsg(ICOMM_SH00082,
                                    operator.getZsxm(), shgcDto.getShlbmc(), jcjyglDto1.getJydh(), jcjyglDto1.getBmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }

            }else {
                jcjyglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                String ICOMM_SH00081 = xxglService.getMsg("ICOMM_SH00081");
                // 发送钉钉消息
                // if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                //     try {
                //         int size = spgwcyDtos.size();
                //         for (int i = 0; i < size; i++) {
                //             if (StringUtil.isNotBlank(spgwcyDtos.get(i).getDdid())) {
                //                 if(StringUtil.isNotBlank(spgwcyDtos.get(i).getDdid())){
                //                     talkUtil.sendWorkMessage("null", spgwcyDtos.get(i).getDdid(),ICOMM_SH00003,StringUtil.replaceMsg(ICOMM_SH00081,
                //                             operator.getZsxm(),shgcDto.getShlbmc() ,jcjyglDto1.getJydh(),
                //                             jcjyglDto1.getBmmc(),
                //                             DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                //                 }
                //             }
                //         }
                //     } catch (Exception e) {
                //         log.error(e.getMessage());
                //     }
                // }
                if (!CollectionUtils.isEmpty(spgwcyDtos) ) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                //获取下一步审核人用户名
                                //小程序访问
                                @SuppressWarnings("deprecation")
                                String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/auditpage/jyjcgoods/jyjcgoods&type=1&ywzd=jcjyid&ywid=" + jcjyglDto1.getJcjyid() + "&auditType=" + shgcDto.getAuditType() + "&urlPrefix=" + urlPrefix,StandardCharsets.UTF_8);
                                List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                                OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                                btnJsonList.setTitle("小程序");
                                btnJsonList.setActionUrl(internalbtn);
                                btnJsonLists.add(btnJsonList);
                                talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00003, StringUtil.replaceMsg(ICOMM_SH00081,
                                        operator.getZsxm(), shgcDto.getShlbmc(), jcjyglDto1.getJydh(),
                                        jcjyglDto1.getBmmc(),
                                        DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
                            }
                        }
                    } catch (Exception e) {
                        throw new BusinessException("msg",e.getMessage());
                    }
                }

                if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())){
                    //发送钉钉消息--取消审核人员
                    int size = shgcDto.getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(), shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,jcjyglDto1.getJydh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(jcjyglDto);
        }

        return true;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
            throws BusinessException {
        // TODO Auto-generated method stub
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        if (auditParam.isCancelOpe()) {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                String jcjyid = shgcDto.getYwid();
                JcjyglDto jcjyglDto = new JcjyglDto();
                jcjyglDto.setXgry(operator.getYhid());
                jcjyglDto.setJcjyid(jcjyid);
                jcjyglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                modSaveBorrowing(jcjyglDto);
            }
        } else {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                String jcjyid = shgcDto.getYwid();
                JcjyglDto jcjyglDto = new JcjyglDto();
                jcjyglDto.setXgry(operator.getYhid());
                jcjyglDto.setJcjyid(jcjyid);
                jcjyglDto.setZt(StatusEnum.CHECK_NO.getCode());
                boolean success = dao.update(jcjyglDto) != 0;
                if (!success)
                    throw new BusinessException("更新主表信息失败！");
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
        JcjyglDto jcjyglDto = new JcjyglDto();
        jcjyglDto.setIds(ids);
        List<JcjyglDto> dtoList = dao.getDtoListByIds(jcjyglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(JcjyglDto dto:dtoList){
                list.add(dto.getJcjyid());
            }
        }
        map.put("list",list);
        return map;
    }

    @Override
    public List<JcjyglDto> getJcjyWithKh() {
        return dao.getJcjyWithKh();
    }

    @Override
    public List<JcjyglDto> getJcjyxxByDwid(JcjyglDto jcjyglDto) {
        return dao.getJcjyxxByDwid(jcjyglDto);
    }

    /**
     * 导出
     *
     * @param jcjyglDto
     
     */
    @Override
    public int getCountForSearchExp(JcjyglDto jcjyglDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(jcjyglDto);
    }
    /**
     * 根据搜索条件获取导出信息
     *

     */
    public List<JcjyglDto> getListForSearchExp(Map<String, Object> params) {
        JcjyglDto jcjyglDto = (JcjyglDto) params.get("entryData");
        queryJoinFlagExport(params, jcjyglDto);
        return dao.getListForSearchExp(jcjyglDto);
    }

    /**
     * 根据选择信息获取导出信息
     *

     */
    public List<JcjyglDto> getListForSelectExp(Map<String, Object> params) {
        JcjyglDto jcjyglDto = (JcjyglDto) params.get("entryData");
        queryJoinFlagExport(params, jcjyglDto);
        return dao.getListForSelectExp(jcjyglDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, JcjyglDto jcjyglDto) {
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
        jcjyglDto.setSqlParam(sqlcs);
    }

    @Override
    public boolean updateWlxx(JcjyglDto jcjyglDto) {
        return dao.updateWlxx(jcjyglDto);
    }

    @Override
    public void updateFzrByYfzr(JcjyglDto jcjyglDto) {
        dao.updateFzrByYfzr(jcjyglDto);
    }
}

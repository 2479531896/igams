package com.matridx.igams.hrm.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.hrm.dao.entities.GrjxDto;
import com.matridx.igams.hrm.dao.entities.GrjxModel;
import com.matridx.igams.hrm.dao.entities.GrjxmxDto;
import com.matridx.igams.hrm.dao.entities.JxmbDto;
import com.matridx.igams.hrm.dao.entities.JxmbmxDto;
import com.matridx.igams.hrm.dao.entities.MbszDto;
import com.matridx.igams.hrm.dao.entities.QzszDto;
import com.matridx.igams.hrm.dao.entities.SyfwDto;
import com.matridx.igams.hrm.dao.entities.YghmcDto;
import com.matridx.igams.hrm.dao.post.IGrjxDao;
import com.matridx.igams.hrm.service.svcinterface.IGrjxService;
import com.matridx.igams.hrm.service.svcinterface.IGrjxmxService;
import com.matridx.igams.hrm.service.svcinterface.IJxmbService;
import com.matridx.igams.hrm.service.svcinterface.IJxmbmxService;
import com.matridx.igams.hrm.service.svcinterface.IMbszService;
import com.matridx.igams.hrm.service.svcinterface.IQzszService;
import com.matridx.igams.hrm.service.svcinterface.ISyfwService;
import com.matridx.igams.hrm.service.svcinterface.IYghmcService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:JYK
 */
@Service
public class GrjxServiceImpl extends BaseBasicServiceImpl<GrjxDto, GrjxModel, IGrjxDao> implements IGrjxService, IAuditService {
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ICommonService commonService;
    @Autowired
    ISyfwService syfwService;
    @Autowired
    IMbszService mbszService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IJxmbService jxmbService;
    @Autowired
    IGrjxmxService grjxmxService;
    @Autowired
    IJxmbmxService jxmbmxService;
    @Autowired
    IQzszService qzszService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IYghmcService yghmcService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    private final Logger log = LoggerFactory.getLogger(GrjxServiceImpl.class);
    /**
     * @description 发放绩效按钮

     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean distributePerformance(JxmbDto jxmbDto) throws BusinessException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //如果是批量发放
        List<SyfwDto> syfwDtoList;
        if ("1".equals(jxmbDto.getBatchFlag())){
            SyfwDto syfwDto=new SyfwDto();
            syfwDto.setJxmbid(jxmbDto.getJxmbid());
            syfwDtoList = syfwService.getDtoList(syfwDto);
        }else {
            syfwDtoList = JSON.parseArray(jxmbDto.getSyfw_json(), SyfwDto.class);
        }
        JxmbDto jxmbDto_sel = jxmbService.getDtoById(jxmbDto.getJxmbid());
        if (!CollectionUtils.isEmpty(syfwDtoList)){
            List<GrjxDto> addGrjxDtos = new ArrayList<>();
            GrjxDto grjxDto_y = new GrjxDto();
            grjxDto_y.setZqkssj(jxmbDto.getZqkssj());
            grjxDto_y.setZqjssj(jxmbDto.getZqjssj());
            grjxDto_y.setMbszid(jxmbDto_sel.getMbszid());
            grjxDto_y.setJxmbid(jxmbDto_sel.getJxmbid());
            //通过模板设置和周期获取已发放的人员
            List<GrjxDto> dtoList = dao.getDtoList(grjxDto_y);
            if (!CollectionUtils.isEmpty(dtoList)){
                //去除已经发放的
                for (GrjxDto dto : dtoList) {
                    syfwDtoList.removeIf(e->dto.getYhid().equals(e.getYhid()));
                }
            }
            if (!CollectionUtils.isEmpty(syfwDtoList)){
                //需要发送绩效确认消息人员
                List<GrjxDto> confirmGrjxDtos = new ArrayList<>();
                List<SyfwDto> addSyfwDtos = new ArrayList<>();
                List<SyfwDto> modSyfwDtos = new ArrayList<>();
                SyfwDto syfwDto_s = new SyfwDto();
                syfwDto_s.setJxmbid(jxmbDto_sel.getJxmbid());
                List<SyfwDto> syfwDtos = syfwService.getDtoListByJxmbid(syfwDto_s);
                JxmbmxDto jxmbmxDto = new JxmbmxDto();
                jxmbmxDto.setJxmbid(jxmbDto_sel.getJxmbid());
                //获取对应绩效模板明细
                List<JxmbmxDto> jxmbmxDtos = jxmbmxService.getDtoList(jxmbmxDto);
                //增加个人绩效明细
                List<GrjxmxDto> grjxmxDtos = new ArrayList<>();
                if (!"delay".equals(jxmbDto.getCheckflag())){
                    long now = new Date().getTime();
                    Calendar calendar = Calendar.getInstance();
                    Calendar yxqcalendar = Calendar.getInstance();
                    try {
                        calendar.setTime(DateUtils.parse(jxmbDto_sel.getSxrq()));
                        yxqcalendar.setTime(DateUtils.parse(jxmbDto_sel.getYxq()));
                        yxqcalendar.add(Calendar.DATE,1);
                    } catch (Exception e) {
                        throw new BusinessException("msg","解析绩效目标生效日期或失效日期失败!");
                    }
                    long yxq = yxqcalendar.getTime().getTime();
                    if (yxq<=now){
                        throw new BusinessException("msg","该绩效模板已失效!");
                    }
                    long kssj = calendar.getTime().getTime();
                    if (kssj>now){
                        throw new BusinessException("msg","该绩效模板还未生效!");
                    }
                }
                Date zqkssjDate = DateUtils.parseDate("yyyy-MM-dd", jxmbDto.getZqkssj());
                Date zqjssjDate = DateUtils.parseDate("yyyy-MM-dd", jxmbDto.getZqjssj());
                Calendar getjzrq = Calendar.getInstance();
                //本月发放
                if ("0".equals(jxmbDto_sel.getFfkhy())){
                    getjzrq.setTime(zqkssjDate);
                    getjzrq.add(Calendar.DATE,Integer.parseInt(jxmbDto_sel.getXffrq()) );
                    //次月发放
                }else if ("1".equals(jxmbDto_sel.getFfkhy())){
                    getjzrq.setTime(zqjssjDate);
                    getjzrq.add(Calendar.DATE,Integer.parseInt(jxmbDto_sel.getXffrq()) );
                }
                getjzrq.add(Calendar.DATE,-1);
                String ffrq = format.format(getjzrq.getTime().getTime());
                getjzrq.add(Calendar.DATE,Integer.parseInt(jxmbDto_sel.getXjzrq()) );
                String jzrq = format.format(getjzrq.getTime().getTime());
                MbszDto mbszDto = new MbszDto();
                mbszDto.setYffrq(ffrq);
                mbszDto.setXgry(jxmbDto.getLrry());
                mbszDto.setMbszid(jxmbDto_sel.getMbszid());
                boolean isSuccess = mbszService.update(mbszDto);
                if (!isSuccess){
                    throw new BusinessException("msg","修改模板设置的已发放日期失败！");
                }
                //绩效发放通知人员
                List<Map<String,String>> jxfftzrys = new ArrayList<>();
                QzszDto qzszDto = new QzszDto();
                qzszDto.setJxmbid(jxmbDto_sel.getJxmbid());
                qzszDto.setMbszid(jxmbDto_sel.getMbszid());
                QzszDto dtoByJxmbid =  qzszService.getDtoByJxmbidAndMbszidWithNull(qzszDto);
                for (SyfwDto syfwDto_ff : syfwDtoList) {
                    GrjxDto grjxDto_add = new GrjxDto();
                    String grjxid = StringUtil.generateUUID();
                    grjxDto_add.setGrjxid(grjxid);
                    grjxDto_add.setMbszid(jxmbDto_sel.getMbszid());
                    grjxDto_add.setJxmbid(jxmbDto_sel.getJxmbid());
                    grjxDto_add.setZqkssj(jxmbDto.getZqkssj());
                    grjxDto_add.setZqjssj(jxmbDto.getZqjssj());
                    grjxDto_add.setBm(jxmbDto_sel.getJgid());
                    grjxDto_add.setYhid(syfwDto_ff.getYhid());
                    grjxDto_add.setYhm(syfwDto_ff.getYhm());
                    grjxDto_add.setNf(DateUtils.getCustomFomratCurrentDate("yyyy"));
                    grjxDto_add.setYf(DateUtils.getCustomFomratCurrentDate("MM"));
                    grjxDto_add.setZt(StatusEnum.CHECK_NO.getCode());
                    grjxDto_add.setSfjs("0");
                    grjxDto_add.setLrry(jxmbDto.getLrry());
                    if ("1".equals(jxmbDto_sel.getMbzlxcskz1())){
                        grjxDto_add.setQrzt("0");
                        grjxDto_add.setZdrymc(jxmbDto_sel.getLrrymc());
                        grjxDto_add.setSyrq(jxmbDto_sel.getSyrq());
                        grjxDto_add.setMbmc(jxmbDto_sel.getMbmc());
                        confirmGrjxDtos.add(grjxDto_add);
                    }else {
                        grjxDto_add.setQrzt("1");
                    }
                    Map<String, String> map = new HashMap<>();
                    map.put("yhm",grjxDto_add.getYhm());
                    map.put("yhid",grjxDto_add.getYhid());
                    map.put("bt",jxmbDto.getZqkssj()+"-"+jxmbDto.getZqjssj()+jxmbDto_sel.getMbmc()+"已发放");
                    map.put("khmc",jxmbDto_sel.getMbmc());
                    map.put("khzq",jxmbDto_sel.getKhzqmc());
                    map.put("jzsj",jzrq);
                    //通知人员
                    jxfftzrys.add(map);
                    if (!CollectionUtils.isEmpty(jxmbmxDtos)){
                        for (JxmbmxDto jxmbmxDto_t : jxmbmxDtos) {
                            GrjxmxDto grjxmxDto = new GrjxmxDto();
                            grjxmxDto.setGrjxid(grjxid);
                            if (dtoByJxmbid!=null){
                                grjxmxDto.setQz(dtoByJxmbid.getQz());
                            }
                            grjxmxDto.setGrjxmxid(StringUtil.generateUUID());
                            grjxmxDto.setJxmbid(jxmbDto_sel.getJxmbid());
                            grjxmxDto.setJxmbmxid(jxmbmxDto_t.getJxmbmxid());
                            grjxmxDto.setLrry(jxmbDto.getLrry());
                            grjxmxDtos.add(grjxmxDto);
                        }
                    }
                    //未发放的数据    有适用范围修改状态为已发放   没适用范围新增适用范围且状态为已发放
                    boolean flag = true;
                    if (!CollectionUtils.isEmpty(syfwDtos)){
                        for (SyfwDto syfwDto : syfwDtos) {
                            if (syfwDto.getYhid().equals(grjxDto_add.getYhid())){
                                flag = false;
                                //有适用范围修改状态为已发放
                                syfwDto.setZt("1");
                                modSyfwDtos.add(syfwDto);
                                break;
                            }
                        }
                    }
                    //没适用范围新增适用范围且状态为已发放
                    if (flag){
                        SyfwDto syfwDto = new SyfwDto();
                        syfwDto.setYhid(grjxDto_add.getYhid());
                        syfwDto.setJxmbid(grjxDto_add.getJxmbid());
                        syfwDto.setSyfwid(StringUtil.generateUUID());
                        syfwDto.setZt("1");
                        addSyfwDtos.add(syfwDto);
                    }
                    addGrjxDtos.add(grjxDto_add);
                }
                if (!CollectionUtils.isEmpty(addSyfwDtos)){
                    isSuccess = syfwService.insertSyfwDtos(addSyfwDtos);
                    if (!isSuccess){
                        throw new BusinessException("msg","新增适用范围信息失败!");
                    }
                }
                if (!CollectionUtils.isEmpty(modSyfwDtos)){
                    isSuccess = syfwService.updateSyfwDtos(modSyfwDtos);
                    if (!isSuccess){
                        throw new BusinessException("msg","修改适用范围信息失败!");
                    }
                }
                if (!CollectionUtils.isEmpty(addGrjxDtos)) {
                    isSuccess = dao.insertGrjxDtos(addGrjxDtos);
                    if (!isSuccess) {
                        throw new BusinessException("msg", "新增个人绩效信息失败!");
                    }
                }
                if (!CollectionUtils.isEmpty(grjxmxDtos)){
                    isSuccess = grjxmxService.insertGrjxmxDtos(grjxmxDtos);
                    if (!isSuccess){
                        throw new BusinessException("msg","新增个人绩效明细失败！");
                    }
                }
                if (!CollectionUtils.isEmpty(jxfftzrys)) {
                    String bt = xxglService.getMsg("ICOMM_JXTZ00001");
                    String nrid = "ICOMM_JXTZ00004";
                    isSuccess = commonSendJxMsg(jxfftzrys, bt, nrid, "0");
                    if (!isSuccess) {
                        log.error("发送绩效通知人员消息失败！--jxfftzrys"+JSON.toJSONString(jxfftzrys));
                    }
                }
                if (!CollectionUtils.isEmpty(confirmGrjxDtos)) {
                  sendJxConfirmMsg(confirmGrjxDtos);
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean batchDistributePerformance(JxmbDto jxmbDto) throws BusinessException {
        for (String id : jxmbDto.getIds()) {
            jxmbDto.setJxmbid(id);
            distributePerformance(jxmbDto);
        }
        return true;
    }
    /* @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean distributePerformance(GrjxDto grjxDto) throws Exception {
        if (StringUtil.isNotBlank(grjxDto.getSyfw_json())){
            List<GrjxDto> grjxDtos = JSON.parseArray(grjxDto.getSyfw_json(), GrjxDto.class);
            if (!CollectionUtils.isEmpty(grjxDtos)){
                GrjxDto grjxDto_y = new GrjxDto();
                String zqsj = DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd");
                grjxDto_y.setZqkssj(zqsj);
                grjxDto_y.setZqjssj(zqsj);
                grjxDto_y.setMbszid(grjxDto.getMbszid());
                grjxDto_y.setJxmbid(grjxDto.getJxmbid());
                //通过模板设置和周期获取已发放的人员
                List<GrjxDto> dtoList = dao.getDtoList(grjxDto_y);
                if (!CollectionUtils.isEmpty(dtoList)){
                    Iterator<GrjxDto> iterator = grjxDtos.iterator();
                    //去除已经发放的
                    while (iterator.hasNext()){
                        GrjxDto next = iterator.next();
                        for (GrjxDto dto : dtoList) {
                            if (dto.getYhid().equals(next.getYhid())){
                                iterator.remove();
                            }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(grjxDtos)){
                    //需要发送绩效确认消息人员
                    List<GrjxDto> confirmGrjxDtos = new ArrayList<>();
                    List<SyfwDto> addSyfwDtos = new ArrayList<>();
                    List<SyfwDto> modSyfwDtos = new ArrayList<>();
                    SyfwDto syfwDto_s = new SyfwDto();
                    syfwDto_s.setJxmbid(grjxDto.getJxmbid());
                    List<SyfwDto> syfwDtos = syfwService.getDtoListByJxmbid(syfwDto_s);
                    JxmbmxDto jxmbmxDto = new JxmbmxDto();
                    jxmbmxDto.setJxmbid(grjxDto.getJxmbid());
                    //获取对应绩效模板明细
                    List<JxmbmxDto> jxmbmxDtos = jxmbmxService.getDtoList(jxmbmxDto);
                    //增加个人绩效明细
                    List<GrjxmxDto> grjxmxDtos = new ArrayList<>();
                    JxmbDto dtoById = jxmbService.getDtoById(grjxDto.getJxmbid());
                    String ffrq = dtoById.getFfrq();
                    String jzrq = dtoById.getJzrq();
                    MbszDto mbszDto = new MbszDto();
                    mbszDto.setYffrq(ffrq);
                    mbszDto.setMbszid(dtoById.getMbszid());
                    boolean isSuccess = mbszService.update(mbszDto);
                    if (!isSuccess){
                        throw new BusinessException("msg","修改模板设置的已发放日期失败！");
                    }
                    //绩效发放通知人员
                    List<Map<String,String>> jxfftzrys = new ArrayList<>();
                    QzszDto qzszDto = new QzszDto();
                    qzszDto.setJxmbid(grjxDto.getJxmbid());
                    qzszDto.setMbszid(grjxDto.getMbszid());
                    QzszDto dtoByJxmbid =  qzszService.getDtoByJxmbidAndMbszidWithNull(qzszDto);
                    for (GrjxDto dto : grjxDtos) {
                        String grjxid = StringUtil.generateUUID();
                        dto.setGrjxid(grjxid);
                        dto.setMbszid(grjxDto.getMbszid());
                        dto.setJxmbid(grjxDto.getJxmbid());
                        dto.setZqkssj(dtoById.getSxrq());
                        dto.setZqjssj(dtoById.getYxq());
                        dto.setBm(grjxDto.getBm());
                        dto.setNf(DateUtils.getCustomFomratCurrentDate("yyyy"));
                        dto.setYf(DateUtils.getCustomFomratCurrentDate("MM"));
                        dto.setZt(StatusEnum.CHECK_NO.getCode());
                        dto.setSfjs("0");
                        dto.setLrry(grjxDto.getLrry());
                        if ("1".equals(dtoById.getMbzlxcskz1())){
                            dto.setQrzt("0");
                            dto.setZdrymc(dtoById.getLrrymc());
                            dto.setSyrq(dtoById.getSyrq());
                            dto.setMbmc(dtoById.getMbmc());
                            confirmGrjxDtos.add(dto);
                        }else {
                            dto.setQrzt("1");
                        }
                        Map<String, String> map = new HashMap<>();
                        map.put("yhm",dto.getYhm());
                        map.put("yhid",dto.getYhid());
                        map.put("bt",dtoById.getSxrq()+"-"+dtoById.getYxq()+dtoById.getMbmc()+"已发放");
                        map.put("khmc",dtoById.getMbmc());
                        map.put("khzq",dtoById.getKhzqmc());
                        map.put("jzsj",jzrq);
                        //通知人员
                        jxfftzrys.add(map);
                        if (!CollectionUtils.isEmpty(jxmbmxDtos)){
                            for (JxmbmxDto jxmbmxDto_t : jxmbmxDtos) {
                                GrjxmxDto grjxmxDto = new GrjxmxDto();
                                grjxmxDto.setGrjxid(grjxid);
                                if (dtoByJxmbid!=null){
                                    grjxmxDto.setQz(dtoByJxmbid.getQz());
                                }
                                grjxmxDto.setGrjxmxid(StringUtil.generateUUID());
                                grjxmxDto.setJxmbid(grjxDto.getJxmbid());
                                grjxmxDto.setJxmbmxid(jxmbmxDto_t.getJxmbmxid());
                                grjxmxDto.setLrry(grjxDto.getLrry());
                                grjxmxDtos.add(grjxmxDto);
                            }
                        }
                        //未发放的数据    有适用范围修改状态为已发放   没适用范围新增适用范围且状态为已发放
                        boolean flag = true;
                        if (!CollectionUtils.isEmpty(syfwDtos)){
                            for (SyfwDto syfwDto : syfwDtos) {
                                if (syfwDto.getYhid().equals(dto.getYhid())){
                                    flag = false;
                                    //有适用范围修改状态为已发放
                                    syfwDto.setZt("1");
                                    modSyfwDtos.add(syfwDto);
                                    break;
                                }
                            }
                        }
                        //没适用范围新增适用范围且状态为已发放
                        if (flag){
                            SyfwDto syfwDto = new SyfwDto();
                            syfwDto.setYhid(dto.getYhid());
                            syfwDto.setJxmbid(grjxDto.getJxmbid());
                            syfwDto.setSyfwid(StringUtil.generateUUID());
                            syfwDto.setZt("1");
                            addSyfwDtos.add(syfwDto);
                        }
                    }
                    if (!CollectionUtils.isEmpty(addSyfwDtos)){
                        isSuccess = syfwService.insertSyfwDtos(addSyfwDtos);
                        if (!isSuccess){
                            throw new BusinessException("msg","新增适用范围信息失败!");
                        }
                    }
                    if (!CollectionUtils.isEmpty(modSyfwDtos)){
                        isSuccess = syfwService.updateSyfwDtos(modSyfwDtos);
                        if (!isSuccess){
                            throw new BusinessException("msg","修改适用范围信息失败!");
                        }
                    }
                    if (!CollectionUtils.isEmpty(grjxDtos)) {
                        isSuccess = dao.insertGrjxDtos(grjxDtos);
                        if (!isSuccess) {
                            throw new BusinessException("msg", "新增个人绩效信息失败!");
                        }
                    }
                    if (!CollectionUtils.isEmpty(grjxmxDtos)){
                        isSuccess = grjxmxService.insertGrjxmxDtos(grjxmxDtos);
                        if (!isSuccess){
                            throw new BusinessException("msg","新增个人绩效明细失败！");
                        }
                    }
                    if (!CollectionUtils.isEmpty(jxfftzrys)) {
                        String bt = xxglService.getMsg("ICOMM_JXTZ00001");
                        String nrid = "ICOMM_JXTZ00004";
                        isSuccess = commonSendJxMsg(jxfftzrys, bt, nrid, "0");
                        if (!isSuccess) {
                            log.error("发送绩效通知人员消息失败！--jxfftzrys"+JSON.toJSONString(jxfftzrys));
                        }
                    }
                    if (!CollectionUtils.isEmpty(confirmGrjxDtos)) {
                        sendJxConfirmMsg(confirmGrjxDtos);
                    }
                }
            }
        }
        return true;
    }*/

    @Override
    public GrjxDto getLastPerformance(GrjxDto grjxDto) {
        return dao.getLastPerformance(grjxDto);
    }

    @Override
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        GrjxDto grjxDto=(GrjxDto)baseModel;
        grjxDto.setXgry(operator.getYhid());
        ShgcDto shgcDto = auditParam.getShgcDto();
        ShxxDto shxx = shgcDto.getShxx();
        grjxDto.setGwid(shxx.getGwid());
        return auditSavePerformance(grjxDto);
    }

    @Override
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList==null||shgcList.isEmpty()){
            return true;
        }
        String ICOMM_GRJX00001 = xxglService.getMsg("ICOMM_GRJX00001");
        String ICOMM_GRJX00003 = xxglService.getMsg("ICOMM_GRJX00003");
        String ICOMM_GRJX00005 = xxglService.getMsg("ICOMM_GRJX00005");
        for (ShgcDto shgcDto : shgcList) {
            GrjxDto grjxDto = new GrjxDto();
            grjxDto.setGrjxid(shgcDto.getYwid());
            grjxDto.setXgry(operator.getYhid());
            GrjxDto grjxDto_t = getDtoById(grjxDto.getGrjxid());
            shgcDto.setSqbm(grjxDto_t.getJgid());
            List<SpgwcyDto> spgwcyDtos = commonService.siftJgList(shgcDto.getSpgwcyDtos(),grjxDto_t.getJgid());
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                String ICOMM_GRJX00004 = xxglService.getMsg("ICOMM_GRJX00004");
                grjxDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_GRJX00003,
                                    StringUtil.replaceMsg(ICOMM_GRJX00004, operator.getZsxm(), shgcDto.getShlbmc(), grjxDto_t.getMbmc(), grjxDto_t.getZqkssj() + "~" + grjxDto_t.getZqjssj(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过  //评分结束
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                String ICOMM_GRJX00006 = xxglService.getMsg("ICOMM_GRJX00006");
                grjxDto.setZt(StatusEnum.CHECK_PASS.getCode());
                grjxDto.setSfjs("1");
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    int size = spgwcyDtos.size();
                    for (int i = 0; i < size; i++) {
                        if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),spgwcyDtos.get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_GRJX00005,
                                    StringUtil. replaceMsg(ICOMM_GRJX00006, operator.getZsxm(),shgcDto.getShlbmc(),grjxDto_t.getMbmc(),grjxDto_t.getZqkssj()+"~"+grjxDto_t.getZqjssj(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                if ("1".equals(grjxDto_t.getJxtzsj())){
                    YghmcDto dtoById = yghmcService.getDtoByYhId(operator.getYhid());
                    if (dtoById!=null&&StringUtil.isNotBlank(dtoById.getZszg())){
                        String ICOMM_JXTZ00006 = xxglService.getMsg("ICOMM_JXTZ00006");
                        String bt = grjxDto_t.getXm()+"的绩效评分结束!";
                        talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),dtoById.getYhid(),dtoById.getYhm(), dtoById.getYhid(), bt,
                                StringUtil. replaceMsg(ICOMM_JXTZ00006, bt,grjxDto_t.getMbmc(),grjxDto_t.getZqkssj()+"~"+grjxDto_t.getZqjssj(), grjxDto_t.getKhzf(),DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss")));
                    }
                }
            }else {
                String ICOMM_GRJX00002 = xxglService.getMsg("ICOMM_GRJX00002");
                GrjxmxDto grjxmxDto = new GrjxmxDto();
                grjxmxDto.setLcxh(shgcDto.getXlcxh());
                grjxmxDto.setGrjxid(grjxDto_t.getGrjxid());
                grjxmxDto.setJxshid(grjxDto_t.getJxshid());
                List<GrjxmxDto> grjxmxDtos =  grjxmxService.getDtoListByGwid(grjxmxDto);
               //有没有绩效信息 没有对应岗位信息新增
                if (CollectionUtils.isEmpty(grjxmxDtos)) {
                    ShlcDto shlcDto = new ShlcDto();
                    shlcDto.setShid(grjxDto_t.getJxshid());
                    shlcDto.setJxmbid(grjxDto_t.getJxmbid());
                    shlcDto.setMbszid(grjxDto_t.getMbszid());
                    List<ShlcDto> dtoListById = shlcService.getDtoListById(shlcDto);
                    if (!CollectionUtils.isEmpty(dtoListById)) {
                        JxmbmxDto jxmbmxDto = new JxmbmxDto();
                        jxmbmxDto.setJxmbid(grjxDto_t.getJxmbid());
                        //获取对应绩效模板明细
                        List<JxmbmxDto> jxmbmxDtos = jxmbmxService.getDtoList(jxmbmxDto);
                        List<GrjxmxDto> addGrjxmxDtos = new ArrayList<>();
                        if (!CollectionUtils.isEmpty(jxmbmxDtos)) {
                            for (ShlcDto dto : dtoListById) {
                                if (dto.getLcxh().equals(shgcDto.getXlcxh())){
                                    for (JxmbmxDto jxmbmxDto_t : jxmbmxDtos) {
                                        GrjxmxDto grjxmxDto_t = new GrjxmxDto();
                                        grjxmxDto_t.setGrjxid(grjxDto_t.getGrjxid());
                                        grjxmxDto_t.setGrjxmxid(StringUtil.generateUUID());
                                        grjxmxDto_t.setJxmbid(grjxDto_t.getJxmbid());
                                        grjxmxDto_t.setLrry(grjxDto.getXgry());
                                        grjxmxDto_t.setGwid(dto.getGwid());
                                        grjxmxDto_t.setQz(dto.getQz());
                                        grjxmxDto_t.setJxmbmxid(jxmbmxDto_t.getJxmbmxid());
                                        addGrjxmxDtos.add(grjxmxDto_t);
                                    }
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(addGrjxmxDtos)) {
                            boolean isSuccess = grjxmxService.insertGrjxmxDtos(addGrjxmxDtos);
                            if (!isSuccess) {
                                throw new BusinessException("msg", "新增个人绩效明细信息失败！");
                            }
                        }
                    }
                }
                grjxDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/JXList/jxshPage/jxshPage&grjxid=" + grjxDto_t.getGrjxid() + "&shlb" + AuditTypeEnum.AUDIT_PERFORMANCE.getCode() + "&urlPrefix=" + urlPrefix + "&wbfw=1", StandardCharsets.UTF_8);
                                List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                                OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                                btnJsonList.setTitle("小程序");
                                btnJsonList.setActionUrl(internalbtn);
                                btnJsonLists.add(btnJsonList);
                                talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_GRJX00001, StringUtil.replaceMsg(ICOMM_GRJX00002,
                                        operator.getZsxm(), shgcDto.getShlbmc(), grjxDto_t.getMbmc(), grjxDto_t.getZqkssj() + "~" + grjxDto_t.getZqjssj(),
                                        DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
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
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005", operator.getZsxm(),shgcDto.getShlbmc(),grjxDto_t.getMbmc(),
                                    DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(grjxDto);
        }
        return true;
    }

    @Override
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String grjxid = shgcDto.getYwid();
                GrjxDto grjxDto = new GrjxDto();
                grjxDto.setXgry(operator.getYhid());
                grjxDto.setGrjxid(grjxid);
                grjxDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(grjxDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String grjxid = shgcDto.getYwid();
                GrjxDto grjxDto = new GrjxDto();
                grjxDto.setXgry(operator.getYhid());
                grjxDto.setGrjxid(grjxid);
                grjxDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(grjxDto);
            }
        }
        return true;
    }

    @Override
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
        Map<String, Object> map =new HashMap<>();
        List<String> ids = (List<String>)param.get("ywids");
        GrjxDto grjxDto = new GrjxDto();
        grjxDto.setIds(ids);
        List<GrjxDto> dtoList = dao.getDtoListByIds(grjxDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(GrjxDto dto:dtoList){
                list.add(dto.getGrjxid());
            }
        }
        map.put("list",list);
        return map;
    }

    /**
     * 导出
     */
    public int getCountForSearchExp(GrjxDto grjxDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(grjxDto);
    }

    /**
     * 根据搜索条件获取导出信息
     */
    public List<GrjxDto> getListForSearchExp(Map<String, Object> params) {
        GrjxDto grjxDto = (GrjxDto) params.get("entryData");
        queryJoinFlagExport(params, grjxDto);
        return dao.getListForSearchExp(grjxDto);
    }
    /**
     * 详细导出
     */
    public int getCountForSearchExpXX(GrjxDto grjxDto, Map<String, Object> params) {
        grjxDto.setDcFlag("1");
        return dao.getCountForSearchExp(grjxDto);
    }

    /**
     * 根据搜索条件获取详细导出信息
     */
    public List<GrjxDto> getListForSearchExpXX(Map<String, Object> params) {
        GrjxDto grjxDto = (GrjxDto) params.get("entryData");
        grjxDto.setDcFlag("1");
        queryJoinFlagExport(params, grjxDto);
        return dao.getListForSearchExp(grjxDto);
    }
    /**
     * 根据选择信息获取详细导出信息
     */
    public List<GrjxDto> getListForSelectExpXX(Map<String, Object> params) {
        GrjxDto grjxDto = (GrjxDto) params.get("entryData");
        grjxDto.setDcFlag("1");
        queryJoinFlagExport(params, grjxDto);
        return dao.getListForSelectExp(grjxDto);
    }

    /**
     * 根据选择信息获取导出信息
     */
    public List<GrjxDto> getListForSelectExp(Map<String, Object> params) {
        GrjxDto grjxDto = (GrjxDto) params.get("entryData");
        queryJoinFlagExport(params, grjxDto);
        return dao.getListForSelectExp(grjxDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, GrjxDto grjxDto) {
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
        grjxDto.setSqlParam(sqlcs);
    }
    /*
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void taskDistributePerformance() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //获取所有绩效模板
        JxmbDto jxmbDto = new JxmbDto();
        jxmbDto.setZt(StatusEnum.CHECK_PASS.getCode());
        jxmbDto.setSxrq(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
        jxmbDto.setYxq(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
        List<JxmbDto> dtoList = jxmbService.getDtoList(jxmbDto);
        //需要发送绩效确认消息人员
        List<GrjxDto> confirmGrjxDtos = new ArrayList<>();
        //增加个人绩效
        List<GrjxDto> addGrjxDtos = new ArrayList<>();
        //修改绩效是否结束
        List<GrjxDto> modGrjxDtos = new ArrayList<>();
        //增加个人绩效明细
        List<GrjxmxDto> addGrjxmxDtos = new ArrayList<>();
        //修改适用范围 状态
        List<SyfwDto> modSyfwDtos = new ArrayList<>();
        //修改模板设置 已发放日期
        List<MbszDto> modMbszDtos = new ArrayList<>();
        //绩效发放通知人员
        List<Map<String,String>> jxfftzrys = new ArrayList<>();
        //绩效提醒自动提交为1的人员
        List<Map<String,String>> jxtxonetzrys = new ArrayList<>();
        //绩效提醒自动提交为0的人员
        List<Map<String,String>> jxtxtwotzrys = new ArrayList<>();
        //自动提交绩效通知人员
        List<Map<String,String>> zdtjjxtzrys = new ArrayList<>();
        //自动提交绩效业务信息
        List<GrjxDto> zdtjYwxx = new ArrayList<>();
        //需要提交的个人绩效对应模板ids
        List<String> xtjjxmbids = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dtoList)){
            for (JxmbDto dto : dtoList) {
                Calendar calendar = Calendar.getInstance();
                Date date = new Date();
                calendar.setTime(date);
                //当前时间
                String nowDateStr = format.format(calendar.getTime());
                SyfwDto syfwDto = new SyfwDto();
                //模板设置的mbid就是jxmbid
                syfwDto.setJxmbid(dto.getJxmbid());
                List<SyfwDto> syfwDtos = syfwService.getDtoListByJxmbid(syfwDto);
                //周期时间不为空也就是满足发放条件
                if (nowDateStr.equals(dto.getFfrq())&&StringUtil.isNotBlank(dto.getSxrq()) && StringUtil.isNotBlank(dto.getYxq())) {
                    JxmbmxDto jxmbmxDto = new JxmbmxDto();
                    jxmbmxDto.setJxmbid(dto.getJxmbid());
                    //获取对应绩效模板明细
                    List<JxmbmxDto> jxmbmxDtos = jxmbmxService.getDtoList(jxmbmxDto);
                    //更新dto已发放日期
                    dto.setYffrq(nowDateStr);
                    //更新数据库已发放日期
                    MbszDto mbszDto_mod = new MbszDto();
                    mbszDto_mod.setMbszid(dto.getMbszid());
                    mbszDto_mod.setYffrq(nowDateStr);
                    modMbszDtos.add(mbszDto_mod);
                    QzszDto qzszDto = new QzszDto();
                    qzszDto.setJxmbid(dto.getJxmbid());
                    qzszDto.setMbszid(dto.getMbszid());
                    QzszDto dtoByJxmbid =  qzszService.getDtoByJxmbidAndMbszidWithNull(qzszDto);
                    String jzsj = dto.getJzrq();
                    if (!CollectionUtils.isEmpty(syfwDtos)) {
                        for (SyfwDto syfwDto_t : syfwDtos) {
                            //未发放绩效
                            if ("0".equals(syfwDto_t.getZt())) {
                                //更新状态为已发放 下面绩效提醒也用到
                                syfwDto_t.setZt("1");
                                modSyfwDtos.add(syfwDto_t);
                                Map<String, String> map = new HashMap<>();
                                map.put("yhm",syfwDto_t.getYhm());
                                map.put("yhid",syfwDto_t.getYhid());
                                map.put("bt",dto.getSxrq()+"-"+dto.getYxq()+dto.getMbmc()+"已发放");
                                map.put("khmc",dto.getMbmc());
                                map.put("khzq",dto.getKhzqmc());
                                map.put("jzsj",jzsj);
                                //通知人员
                                jxfftzrys.add(map);
                                //个人绩效
                                GrjxDto grjxDto = new GrjxDto();
                                String grjxid = StringUtil.generateUUID();
                                grjxDto.setGrjxid(grjxid);
                                grjxDto.setMbszid(dto.getMbszid());
                                grjxDto.setJxmbid(dto.getJxmbid());
                                grjxDto.setZqkssj(dto.getSxrq());
                                grjxDto.setZqjssj(dto.getYxq());
                                grjxDto.setYhid(syfwDto_t.getYhid());
                                grjxDto.setBm(syfwDto_t.getBm());
                                grjxDto.setNf(DateUtils.getCustomFomratCurrentDate("yyyy"));
                                grjxDto.setYf(DateUtils.getCustomFomratCurrentDate("MM"));
                                grjxDto.setZt(StatusEnum.CHECK_NO.getCode());
                                grjxDto.setLrry("admin");
                                grjxDto.setSfjs("0");
                                grjxDto.setYhm(syfwDto_t.getYhm());
                                if ("1".equals(dto.getMbzlxcskz1())){
                                    grjxDto.setZdrymc(dto.getLrrymc());
                                    grjxDto.setSyrq(dto.getSyrq());
                                    grjxDto.setMbmc(dto.getMbmc());
                                    grjxDto.setQrzt("0");
                                    confirmGrjxDtos.add(grjxDto);
                                }else {
                                    grjxDto.setQrzt("1");
                                }
                                addGrjxDtos.add(grjxDto);
                                if (!CollectionUtils.isEmpty(jxmbmxDtos)){
                                    for (JxmbmxDto jxmbmxDto_t : jxmbmxDtos) {
                                        GrjxmxDto grjxmxDto = new GrjxmxDto();
                                        grjxmxDto.setGrjxid(grjxid);
                                        grjxmxDto.setGrjxmxid(StringUtil.generateUUID());
                                        grjxmxDto.setJxmbid(dto.getJxmbid());
                                        grjxmxDto.setJxmbmxid(jxmbmxDto_t.getJxmbmxid());
                                        grjxmxDto.setLrry("admin");
                                        if (dtoByJxmbid!=null){
                                            grjxmxDto.setQz(dtoByJxmbid.getQz());
                                        }
                                        addGrjxmxDtos.add(grjxmxDto);
                                    }
                                }
                            }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(syfwDtos)&&StringUtil.isNotBlank(dto.getYffrq())) {
                    //判断当前时间是否等于，如果等于，发送绩效提醒消息给用户
                    //自动将未提交的基绩效提交至审批流程时间
                    String zdtjjx = dto.getJzrq();
                    //发送绩效提醒消息时间
                    String jxtxsj = dto.getJxtx();
                    for (SyfwDto syfwDto_tx : syfwDtos) {
                        if (nowDateStr.equals(jxtxsj)) {
                            //提醒状态为已发放的人员
                            if ("1".equals(syfwDto_tx.getZt())) {
                                if ("1".equals(dto.getZdtj())) {
                                    Map<String, String> map = new HashMap<>();
                                    map.put("yhm",syfwDto_tx.getYhm());
                                    map.put("yhid",syfwDto_tx.getYhid());
                                    map.put("bt","您有绩效考核即将过期，请尽快提交!");
                                    map.put("khmc",dto.getMbmc());
                                    map.put("khzq",dto.getKhzqmc());
                                    map.put("jzsj",zdtjjx+"到达截至日期将自动提交，请尽快填写自评信息!");
                                    jxtxonetzrys.add(map);
                                } else {
                                    Map<String, String> map = new HashMap<>();
                                    map.put("yhm",syfwDto_tx.getYhm());
                                    map.put("yhid",syfwDto_tx.getYhid());
                                    map.put("bt","您有绩效考核即将过期，请尽快提交!");
                                    map.put("khmc",dto.getMbmc());
                                    map.put("khzq",dto.getKhzqmc());
                                    map.put("jzsj",zdtjjx);
                                    jxtxtwotzrys.add(map);
                                }
                            }
                        }
                        //自动将未提交的基绩效提交至审批流程
                        if (nowDateStr.equals(zdtjjx)&&"1".equals(dto.getZdtj())) {
                            //需要提交的个人绩效对应模板ids
                            if (!xtjjxmbids.contains(dto.getMbszid())){
                                xtjjxmbids.add(dto.getMbszid());
                                GrjxDto grjxDto = new GrjxDto();
                                grjxDto.setMbszid(dto.getMbszid());
                                grjxDto.setZt(StatusEnum.CHECK_NO.getCode());
                                //获取需要自动提交的数据
                                List<GrjxDto> grjxDtos = dao.getNeedSubmitByMbid(grjxDto);
                                if (!CollectionUtils.isEmpty(grjxDtos)){
                                    for (GrjxDto grjxDto_s : grjxDtos) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("yhm",grjxDto_s.getYhm());
                                        map.put("yhid",grjxDto_s.getYhid());
                                        map.put("bt","您有绩效已自动提交!");
                                        map.put("khmc",dto.getMbmc());
                                        map.put("khzq",dto.getKhzqmc());
                                        map.put("jzsj",zdtjjx);
                                        map.put("tjsj",zdtjjx);
                                        zdtjjxtzrys.add(map);
                                        zdtjYwxx.add(grjxDto_s);
                                    }
                                }
                            }
                        }
                        //绩效截止 修改适用范围状态为未发放
                        if (nowDateStr.equals(zdtjjx)){
                            syfwDto_tx.setZt("0");
                            modSyfwDtos.add(syfwDto_tx);
                        }
                    }
                }
            }
        }
        //自动提交绩效
        if (!CollectionUtils.isEmpty(zdtjYwxx)){
            for (GrjxDto grjxDto_s : zdtjYwxx) {
                ShgcDto shgcDto_t = new ShgcDto();
                shgcDto_t.setExtend_1("[\""+grjxDto_s.getGrjxid()+"\"]");
                shgcDto_t.setShlb(AuditTypeEnum.AUDIT_PERFORMANCE.getCode());
                shgcDto_t.setShid(grjxDto_s.getJxshid());
                User user = new User();
                user.setYhid(grjxDto_s.getYhid());
                user.setZsxm(grjxDto_s.getZsxm());
                shgcService.checkAndCommit(shgcDto_t,user);
                //个人绩效截止 修改是否结束为1
                GrjxDto grjxDto_mod = new GrjxDto();
                grjxDto_mod.setGrjxid(grjxDto_s.getGrjxid());
                grjxDto_mod.setSfjs("1");
                modGrjxDtos.add(grjxDto_mod);
            }
        }
        if (!CollectionUtils.isEmpty(addGrjxDtos)){
            boolean isSuccess = dao.insertGrjxDtos(addGrjxDtos);
            if (!isSuccess){
                throw new BusinessException("msg","新增个人绩效失败！");
            }
        }
        if (!CollectionUtils.isEmpty(addGrjxmxDtos)){
            boolean isSuccess = grjxmxService.insertGrjxmxDtos(addGrjxmxDtos);
            if (!isSuccess){
                throw new BusinessException("msg","新增个人绩效明细失败！");
            }
        }
        if (!CollectionUtils.isEmpty(modSyfwDtos)){
            boolean isSuccess = syfwService.updateSyfwDtos(modSyfwDtos);
            if (!isSuccess){
                throw new BusinessException("msg","修改适用范围状态失败！");
            }
        }
        if (!CollectionUtils.isEmpty(modMbszDtos)){
            boolean isSuccess = mbszService.updateMbszDtos(modMbszDtos);
            if (!isSuccess){
                throw new BusinessException("msg","修改模板设置已发放时间失败！");
            }
        }
        if (!CollectionUtils.isEmpty(modGrjxDtos)){
            boolean isSuccess = dao.updateGrjxDtos(modGrjxDtos);
            if (!isSuccess){
                throw new BusinessException("msg","修改个人绩效是否结束失败！");
            }
        }
        boolean isSuccess = false;
        if (!CollectionUtils.isEmpty(confirmGrjxDtos)){
            sendJxConfirmMsg(confirmGrjxDtos);
        }
        if (!CollectionUtils.isEmpty(jxfftzrys)) {
            String bt = xxglService.getMsg("ICOMM_JXTZ00001");
            String nrid = "ICOMM_JXTZ00004";
            isSuccess = commonSendJxMsg(jxfftzrys, bt, nrid, "0");
            if (!isSuccess) {
                log.error("发送绩效通知人员消息失败！--jxfftzrys="+JSON.toJSONString(jxfftzrys));
            }
        }
        if (!CollectionUtils.isEmpty(jxtxonetzrys)) {
            String bt = xxglService.getMsg("ICOMM_JXTZ00002");
            String nrid = "ICOMM_JXTZ00004";
            isSuccess = commonSendJxMsg(jxtxonetzrys, bt, nrid, "0");
            if (!isSuccess) {
                log.error("发送绩效过期自动提交提醒消息失败！--jxtxonetzrys="+JSON.toJSONString(jxtxonetzrys));
            }
        }
        if (!CollectionUtils.isEmpty(jxtxtwotzrys)) {
            String bt = xxglService.getMsg("ICOMM_JXTZ00002");
            String nrid = "ICOMM_JXTZ00004";
            isSuccess = commonSendJxMsg(jxtxtwotzrys, bt, nrid, "0");
            if (!isSuccess) {
                log.error("发送绩效过期不自动提交提醒消息失败！--jxtxtwotzrys="+JSON.toJSONString(jxtxtwotzrys));
            }
        }
        if (!CollectionUtils.isEmpty(zdtjjxtzrys)) {
            String bt = xxglService.getMsg("ICOMM_JXTZ00003");
            String nrid = "ICOMM_JXTZ00005";
            isSuccess = commonSendJxMsg(zdtjjxtzrys, bt, nrid, "1");
            if (!isSuccess) {
                log.error("发送绩效自动提交消息失败！--zdtjjxtzrys="+JSON.toJSONString(zdtjjxtzrys));
            }
        }
    }*/
    /**
     * @description 定时发放绩效
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void taskDistributePerformance() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        //将String转LocalDateTime
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //获取所有绩效模板
        JxmbDto jxmbDto = new JxmbDto();
        jxmbDto.setZt(StatusEnum.CHECK_PASS.getCode());
        jxmbDto.setSxrq(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
        jxmbDto.setJzyxq(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
        List<JxmbDto> dtoList = jxmbService.getDtoList(jxmbDto);
        //需要发送绩效确认消息人员
        List<GrjxDto> confirmGrjxDtos = new ArrayList<>();
        //增加个人绩效
        List<GrjxDto> addGrjxDtos = new ArrayList<>();
        //修改绩效是否结束
        List<GrjxDto> modGrjxDtos = new ArrayList<>();
        //增加个人绩效明细
        List<GrjxmxDto> addGrjxmxDtos = new ArrayList<>();
        //修改适用范围 状态
        List<SyfwDto> modSyfwDtos = new ArrayList<>();
        //修改模板设置 已发放日期
        List<MbszDto> modMbszDtos = new ArrayList<>();
        //绩效发放通知人员
        List<Map<String,String>> jxfftzrys = new ArrayList<>();
        //绩效提醒自动提交为1的人员
        List<Map<String,String>> jxtxonetzrys = new ArrayList<>();
        //绩效提醒自动提交为0的人员
        List<Map<String,String>> jxtxtwotzrys = new ArrayList<>();
        //自动提交绩效通知人员
        List<Map<String,String>> zdtjjxtzrys = new ArrayList<>();
        //自动提交绩效业务信息
        List<GrjxDto> zdtjYwxx = new ArrayList<>();
        //需要提交的个人绩效对应模板ids
        List<String> xtjjxmbids = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dtoList)){
            for (JxmbDto dto : dtoList) {
                Calendar calendar = Calendar.getInstance();
                Date date = new Date();
                calendar.setTime(date);
                //当前时间
                String nowDateStr = format.format(calendar.getTime());
                //本周第几天 根据我国习惯周一为第一天 减一
                int weekDay = calendar.get(Calendar.DAY_OF_WEEK)-1;
                //本月第几天
                int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
                //本年第几天
                int yearDay = calendar.get(Calendar.DAY_OF_YEAR);
                LocalDate sxrq = LocalDate.parse(DateUtils.getCustomFomratCurrentDate("yyyy")+"-01-01", df);
                //判断相差多少天 相差几年，相差几月，相差几周
                long years = ChronoUnit.YEARS.between(sxrq, today);
                long months = ChronoUnit.MONTHS.between(sxrq, today);
                long weeks = ChronoUnit.WEEKS.between(sxrq, today);
                int ffrq = Integer.parseInt(dto.getXffrq());
                int khzqcskz2 = Integer.parseInt(dto.getKhzqcskz2());
                int ys = 0;
                //如果发放日期为负数 日期差加一去求余
                if (ffrq < 0) {
                    years++;
                    months++;
                    weeks++;
                }
                //处理发放日期为负的数据
                Calendar calendartwo = Calendar.getInstance();
                calendartwo.setTime(DateUtils.parse(DateUtils.getCustomFomratCurrentDate("yyyy")+"-01-01"));
                //相差除以周期数 取余数
                if ("WW".equals(dto.getKhzqcskz1())) {
                    //由于一年当中第一天不一定是周一，取周一
                    while (calendartwo.get(Calendar.DAY_OF_WEEK)-1!=1){
                        calendartwo.add(Calendar.DATE,1);
                    }
                    calendartwo.add(Calendar.DATE, new BigDecimal(weeks).multiply(new BigDecimal(7)).intValue());
                    ys = new BigDecimal(weeks).divideAndRemainder(new BigDecimal(khzqcskz2))[1].intValue();
                } else if ("YY".equals(dto.getKhzqcskz1())) {
                    calendartwo.add(Calendar.YEAR, (int) years);
                    ys = new BigDecimal(years).divideAndRemainder(new BigDecimal(khzqcskz2))[1].intValue();
                } else if ("MM".equals(dto.getKhzqcskz1())) {
                    calendartwo.add(Calendar.MONTH, (int) months);
                    ys = new BigDecimal(months).divideAndRemainder(new BigDecimal(khzqcskz2))[1].intValue();
                } else {
                    log.error("taskDistributePerformance---考核周期参数扩展1不合法！---" + JSON.toJSONString(dto));
                }
                String zqkssj = null;
                String zqjssj = null;
                int khzqweek = new BigDecimal(khzqcskz2).multiply(new BigDecimal(7)).intValue();
                //余数为0 表示在发放周期 ，再判断发放日期是否满足发放条件
                if (ys == 0) {
                    //本月 这一周期后几天发放(发放日期为正数)这一周期前几天发放(发放日期为负数)
                    if ("0".equals(dto.getFfkhy())) {
                        //是否满足发放条件
                        if (ffrq > 0) {
                            if ("WW".equals(dto.getKhzqcskz1())) {
                                //本周第几天和发放日期相等，满足发放条件
                                if (ffrq == weekDay) {
                                    calendar.add(Calendar.DATE, -ffrq+1);
                                    zqkssj = format.format(calendar.getTime());
                                    calendar.add(Calendar.DATE,  khzqweek);
                                    zqjssj = format.format(calendar.getTime());
                                }
                            } else if ("YY".equals(dto.getKhzqcskz1())) {
                                //本年第几天和发放日期相等，满足发放条件
                                if (ffrq == yearDay) {
                                    calendar.add(Calendar.DATE, -ffrq+1);
                                    zqkssj = format.format(calendar.getTime());
                                    calendar.add(Calendar.YEAR, khzqcskz2);
                                    zqjssj = format.format(calendar.getTime());
                                }
                            } else if ("MM".equals(dto.getKhzqcskz1())) {
                                //本月第几天和发放日期相等，满足发放条件
                                if (ffrq == monthDay) {
                                    calendar.add(Calendar.DATE, -ffrq+1);
                                    zqkssj = format.format(calendar.getTime());
                                    calendar.add(Calendar.MONTH, khzqcskz2);
                                    zqjssj = format.format(calendar.getTime());
                                }
                            }
                        } else {
                            calendartwo.add(Calendar.DATE, ffrq);
                            if (nowDateStr.equals(format.format(calendartwo.getTime()))) {
                                calendartwo.add(Calendar.DATE, -ffrq);
                                //设置周期结束时间
                                zqjssj = format.format(calendartwo.getTime());
                                if ("WW".equals(dto.getKhzqcskz1())) {
                                    //往前推为周期开始时间
                                    calendartwo.add(Calendar.DATE, -khzqweek);
                                    zqkssj = format.format(calendartwo.getTime());
                                } else if ("YY".equals(dto.getKhzqcskz1())) {
                                    calendartwo.add(Calendar.YEAR, -khzqcskz2);
                                    zqkssj = format.format(calendartwo.getTime());
                                } else if ("MM".equals(dto.getKhzqcskz1())) {
                                    calendartwo.add(Calendar.MONTH, -khzqcskz2);
                                    zqkssj = format.format(calendartwo.getTime());
                                }
                            }
                        }
                        //次月 下一周期后几天时发放(发放日期为正数)下一周期前几天时发放(发放日期为负数)
                    } else if ("1".equals(dto.getFfkhy())) {
                        if (ffrq > 0) {
                            if ("WW".equals(dto.getKhzqcskz1())) {
                                //次周期周第几天和发放日期相等，满足发放条件
                                if (ffrq == weekDay) {
                                    calendar.add(Calendar.DATE, -ffrq+1);
                                    //当前日期减去发放日期为周期结束时间
                                    zqjssj = format.format(calendar.getTime());
                                    //往前推一个周期为周期开始时间
                                    calendar.add(Calendar.DATE, -khzqweek);
                                    zqkssj = format.format(calendar.getTime());
                                }
                            } else if ("YY".equals(dto.getKhzqcskz1())) {
                                //次周期年第几天和发放日期相等，满足发放条件
                                if (ffrq == yearDay) {
                                    calendar.add(Calendar.DATE, -ffrq+1);
                                    //当前日期减去发放日期为周期结束时间
                                    zqjssj = format.format(calendar.getTime());
                                    //往前推一个周期为周期开始时间
                                    calendar.add(Calendar.YEAR, -khzqcskz2);
                                    zqkssj = format.format(calendar.getTime());
                                }
                            } else if ("MM".equals(dto.getKhzqcskz1())) {
                                //次周期月第几天和发放日期相等，满足发放条件
                                if (ffrq == monthDay) {
                                    calendar.add(Calendar.DATE, -ffrq+1);
                                    //当前日期减去发放日期为周期结束时间
                                    zqjssj = format.format(calendar.getTime());
                                    //往前推一个周期为周期开始时间
                                    calendar.add(Calendar.MONTH, -khzqcskz2);
                                    zqkssj = format.format(calendar.getTime());
                                }
                            }
                        } else if (ffrq < 0) {
                            //发放日期为负数
                            calendartwo.add(Calendar.DATE, ffrq-1);
                            if (nowDateStr.equals(format.format(calendartwo.getTime()))) {
                                calendartwo.add(Calendar.DATE, -ffrq);
                                //设置周期结束时间
                                zqjssj = format.format(calendartwo.getTime());
                                if ("WW".equals(dto.getKhzqcskz1())) {
                                    //往前推为周期开始时间
                                    calendartwo.add(Calendar.DATE, -khzqweek);
                                    zqjssj = format.format(calendartwo.getTime());
                                    calendartwo.add(Calendar.DATE, -khzqweek);
                                    zqkssj = format.format(calendartwo.getTime());
                                } else if ("YY".equals(dto.getKhzqcskz1())) {
                                    calendartwo.add(Calendar.YEAR, -khzqcskz2);
                                    zqjssj = format.format(calendartwo.getTime());
                                    calendartwo.add(Calendar.YEAR, -khzqcskz2);
                                    zqkssj = format.format(calendartwo.getTime());
                                } else if ("MM".equals(dto.getKhzqcskz1())) {
                                    calendartwo.add(Calendar.MONTH, -khzqcskz2);
                                    zqjssj = format.format(calendartwo.getTime());
                                    calendartwo.add(Calendar.MONTH, -khzqcskz2);
                                    zqkssj = format.format(calendartwo.getTime());
                                }
                            }
                        }
                    } else {
                        log.error("taskDistributePerformance---发放考核月不合法！----" + JSON.toJSONString(dto));
                    }
                }
                SyfwDto syfwDto = new SyfwDto();
                //模板设置的mbid就是jxmbid
                syfwDto.setJxmbid(dto.getJxmbid());
                List<SyfwDto> syfwDtos = syfwService.getDtoListByJxmbid(syfwDto);
                //周期时间不为空也就是满足发放条件
                if (StringUtil.isNotBlank(zqkssj) && StringUtil.isNotBlank(zqjssj)) {
                    //往前推一天
                    Calendar instance = Calendar.getInstance();
                    instance.setTime(DateUtils.parse(zqjssj));
                    instance.add(Calendar.DATE,-1);
                    zqjssj = format.format(instance.getTime());
                    JxmbmxDto jxmbmxDto = new JxmbmxDto();
                    jxmbmxDto.setJxmbid(dto.getJxmbid());
                    //获取对应绩效模板明细
                    List<JxmbmxDto> jxmbmxDtos = jxmbmxService.getDtoList(jxmbmxDto);
                    //更新dto已发放日期
                    dto.setYffrq(nowDateStr);
                    //更新数据库已发放日期
                    MbszDto mbszDto_mod = new MbszDto();
                    mbszDto_mod.setMbszid(dto.getMbszid());
                    mbszDto_mod.setYffrq(nowDateStr);
                    modMbszDtos.add(mbszDto_mod);
                    Calendar jzrqCalendar = Calendar.getInstance();
                    jzrqCalendar.setTime(new Date());
                    jzrqCalendar.add(Calendar.DATE,Integer.parseInt(dto.getXjzrq()));
                    QzszDto qzszDto = new QzszDto();
                    qzszDto.setJxmbid(dto.getJxmbid());
                    qzszDto.setMbszid(dto.getMbszid());
                    QzszDto dtoByJxmbid =  qzszService.getDtoByJxmbidAndMbszidWithNull(qzszDto);
                    String jzsj = format.format(jzrqCalendar.getTime());
                    if (!CollectionUtils.isEmpty(syfwDtos)) {
                        for (SyfwDto syfwDto_t : syfwDtos) {
                            //未发放绩效
                            if ("0".equals(syfwDto_t.getZt())) {
                                //更新状态为已发放 下面绩效提醒也用到
                                syfwDto_t.setZt("1");
                                modSyfwDtos.add(syfwDto_t);
                                Map<String, String> map = new HashMap<>();
                                map.put("yhm",syfwDto_t.getYhm());
                                map.put("yhid",syfwDto_t.getYhid());
                                map.put("bt",zqkssj+"-"+zqjssj+dto.getMbmc()+"已发放");
                                map.put("khmc",dto.getMbmc());
                                map.put("khzq",dto.getKhzqmc());
                                map.put("jzsj",jzsj);
                                //通知人员
                                jxfftzrys.add(map);
                                //个人绩效
                                GrjxDto grjxDto = new GrjxDto();
                                String grjxid = StringUtil.generateUUID();
                                grjxDto.setGrjxid(grjxid);
                                grjxDto.setMbszid(dto.getMbszid());
                                grjxDto.setJxmbid(dto.getJxmbid());
                                grjxDto.setZqkssj(zqkssj);
                                grjxDto.setZqjssj(zqjssj);
                                grjxDto.setYhid(syfwDto_t.getYhid());
                                grjxDto.setBm(syfwDto_t.getBm());
                                grjxDto.setNf(DateUtils.getCustomFomratCurrentDate("yyyy"));
                                grjxDto.setYf(DateUtils.getCustomFomratCurrentDate("MM"));
                                grjxDto.setZt(StatusEnum.CHECK_NO.getCode());
                                grjxDto.setLrry("admin");
                                grjxDto.setSfjs("0");
                                grjxDto.setYhm(syfwDto_t.getYhm());
                                if ("1".equals(dto.getMbzlxcskz1())){
                                    grjxDto.setZdrymc(dto.getLrrymc());
                                    grjxDto.setSyrq(dto.getSyrq());
                                    grjxDto.setMbmc(dto.getMbmc());
                                    grjxDto.setQrzt("0");
                                    confirmGrjxDtos.add(grjxDto);
                                }else {
                                    grjxDto.setQrzt("1");
                                }
                                addGrjxDtos.add(grjxDto);
                                if (!CollectionUtils.isEmpty(jxmbmxDtos)){
                                    for (JxmbmxDto jxmbmxDto_t : jxmbmxDtos) {
                                        GrjxmxDto grjxmxDto = new GrjxmxDto();
                                        grjxmxDto.setGrjxid(grjxid);
                                        grjxmxDto.setGrjxmxid(StringUtil.generateUUID());
                                        grjxmxDto.setJxmbid(dto.getJxmbid());
                                        grjxmxDto.setJxmbmxid(jxmbmxDto_t.getJxmbmxid());
                                        grjxmxDto.setLrry("admin");
                                        if (dtoByJxmbid!=null){
                                            grjxmxDto.setQz(dtoByJxmbid.getQz());
                                        }
                                        addGrjxmxDtos.add(grjxmxDto);
                                    }
                                }
                            }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(syfwDtos)&&StringUtil.isNotBlank(dto.getYffrq())) {
                    //处理发送绩效提醒时间
                    Calendar calendarthree = Calendar.getInstance();
                    //判断当前时间是否等于，已发放日期+截至日期-绩效提醒的时间，如果等于，发送绩效提醒消息给用户
                    calendarthree.setTime(DateUtils.parse(dto.getYffrq()));
                    calendarthree.add(Calendar.DATE, Integer.parseInt(dto.getXjzrq()));
                    //自动将未提交的基绩效提交至审批流程时间
                    String zdtjjx = format.format(calendarthree.getTime());
                    calendarthree.add(Calendar.DATE, -Integer.parseInt(dto.getXjxtx()));
                    //发送绩效提醒消息时间
                    String jxtxsj = format.format(calendarthree.getTime());
                    if (nowDateStr.equals(jxtxsj)) {
                        GrjxDto grjxDto_remind = new GrjxDto();
                        grjxDto_remind.setMbszid(dto.getMbszid());
                        List<String> zts = new ArrayList<>();
                        zts.add(StatusEnum.CHECK_NO.getCode());
                        zts.add(StatusEnum.CHECK_UNPASS.getCode());
                        grjxDto_remind.setZts(zts);
                        List<GrjxDto> needRemindGrjxDtos =  dao.getNeedRemindByMbid(grjxDto_remind);
                        if (!CollectionUtils.isEmpty(needRemindGrjxDtos)){
                            for (GrjxDto needRemindGrjxDto : needRemindGrjxDtos) {
                                if ("1".equals(dto.getZdtj())) {
                                    Map<String, String> map = new HashMap<>();
                                    map.put("yhm",needRemindGrjxDto.getYhm());
                                    map.put("yhid",needRemindGrjxDto.getYhid());
                                    map.put("bt","您有绩效考核即将过期，请尽快提交!");
                                    map.put("khmc",dto.getMbmc());
                                    map.put("khzq",dto.getKhzqmc());
                                    map.put("jzsj",zdtjjx+"到达截至日期将自动提交，请尽快填写自评信息!");
                                    jxtxonetzrys.add(map);
                                } else {
                                    Map<String, String> map = new HashMap<>();
                                    map.put("yhm",needRemindGrjxDto.getYhm());
                                    map.put("yhid",needRemindGrjxDto.getYhid());
                                    map.put("bt","您有绩效考核即将过期，请尽快提交!");
                                    map.put("khmc",dto.getMbmc());
                                    map.put("khzq",dto.getKhzqmc());
                                    map.put("jzsj",zdtjjx);
                                    jxtxtwotzrys.add(map);
                                }
                            }
                        }
                    }
                    for (SyfwDto syfwDto_tx : syfwDtos) {
                        //自动将未提交的基绩效提交至审批流程
                        if (nowDateStr.equals(zdtjjx)&&"1".equals(dto.getZdtj())) {
                            //需要提交的个人绩效对应模板ids
                            if (!xtjjxmbids.contains(dto.getMbszid())){
                                xtjjxmbids.add(dto.getMbszid());
                                GrjxDto grjxDto = new GrjxDto();
                                grjxDto.setMbszid(dto.getMbszid());
                                List<String> zts = new ArrayList<>();
                                zts.add(StatusEnum.CHECK_NO.getCode());
                                zts.add(StatusEnum.CHECK_UNPASS.getCode());
                                grjxDto.setZts(zts);
                                //获取需要自动提交的数据
                                List<GrjxDto> grjxDtos = dao.getNeedSubmitByMbid(grjxDto);
                                if (!CollectionUtils.isEmpty(grjxDtos)){
                                    for (GrjxDto grjxDto_s : grjxDtos) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("yhm",grjxDto_s.getYhm());
                                        map.put("yhid",grjxDto_s.getYhid());
                                        map.put("bt","您有绩效已自动提交!");
                                        map.put("khmc",dto.getMbmc());
                                        map.put("khzq",dto.getKhzqmc());
                                        map.put("jzsj",zdtjjx);
                                        map.put("tjsj",zdtjjx);
                                        zdtjjxtzrys.add(map);
                                        zdtjYwxx.add(grjxDto_s);
                                    }
                                }
                            }
                        }
                        //绩效截止 修改适用范围状态为未发放
                        if (nowDateStr.equals(zdtjjx)){
                            syfwDto_tx.setZt("0");
                            modSyfwDtos.add(syfwDto_tx);
                        }
                    }
                }
            }
        }
        //自动提交绩效
        if (!CollectionUtils.isEmpty(zdtjYwxx)){
            for (GrjxDto grjxDto_s : zdtjYwxx) {
                ShgcDto shgcDto_t = new ShgcDto();
                shgcDto_t.setExtend_1("[\""+grjxDto_s.getGrjxid()+"\"]");
                shgcDto_t.setShlb(AuditTypeEnum.AUDIT_PERFORMANCE.getCode());
                shgcDto_t.setShid(grjxDto_s.getJxshid());
                User user = new User();
                user.setYhid(grjxDto_s.getYhid());
                user.setZsxm(grjxDto_s.getZsxm());
                shgcService.checkAndCommit(shgcDto_t,user);
                //个人绩效截止 修改是否结束为1
                GrjxDto grjxDto_mod = new GrjxDto();
                grjxDto_mod.setGrjxid(grjxDto_s.getGrjxid());
                grjxDto_mod.setSfjs("1");
                modGrjxDtos.add(grjxDto_mod);
            }
        }
        if (!CollectionUtils.isEmpty(addGrjxDtos)){
            boolean isSuccess = dao.insertGrjxDtos(addGrjxDtos);
            if (!isSuccess){
                throw new BusinessException("msg","新增个人绩效失败！");
            }
        }
        if (!CollectionUtils.isEmpty(addGrjxmxDtos)){
            boolean isSuccess = grjxmxService.insertGrjxmxDtos(addGrjxmxDtos);
            if (!isSuccess){
                throw new BusinessException("msg","新增个人绩效明细失败！");
            }
        }
        if (!CollectionUtils.isEmpty(modSyfwDtos)){
            boolean isSuccess = syfwService.updateSyfwDtos(modSyfwDtos);
            if (!isSuccess){
                throw new BusinessException("msg","修改适用范围状态失败！");
            }
        }
        if (!CollectionUtils.isEmpty(modMbszDtos)){
            boolean isSuccess = mbszService.updateMbszDtos(modMbszDtos);
            if (!isSuccess){
                throw new BusinessException("msg","修改模板设置已发放时间失败！");
            }
        }
        if (!CollectionUtils.isEmpty(modGrjxDtos)){
            boolean isSuccess = dao.updateGrjxDtos(modGrjxDtos);
            if (!isSuccess){
                throw new BusinessException("msg","修改个人绩效是否结束失败！");
            }
        }
        boolean isSuccess;
        if (!CollectionUtils.isEmpty(confirmGrjxDtos)){
           sendJxConfirmMsg(confirmGrjxDtos);
        }
        if (!CollectionUtils.isEmpty(jxfftzrys)) {
            String bt = xxglService.getMsg("ICOMM_JXTZ00001");
            String nrid = "ICOMM_JXTZ00004";
            isSuccess = commonSendJxMsg(jxfftzrys, bt, nrid, "0");
            if (!isSuccess) {
                log.error("发送绩效通知人员消息失败！--jxfftzrys="+JSON.toJSONString(jxfftzrys));
            }
        }
        if (!CollectionUtils.isEmpty(jxtxonetzrys)) {
            String bt = xxglService.getMsg("ICOMM_JXTZ00002");
            String nrid = "ICOMM_JXTZ00004";
            isSuccess = commonSendJxMsg(jxtxonetzrys, bt, nrid, "0");
            if (!isSuccess) {
                log.error("发送绩效过期自动提交提醒消息失败！--jxtxonetzrys="+JSON.toJSONString(jxtxonetzrys));
            }
        }
        if (!CollectionUtils.isEmpty(jxtxtwotzrys)) {
            String bt = xxglService.getMsg("ICOMM_JXTZ00002");
            String nrid = "ICOMM_JXTZ00004";
            isSuccess = commonSendJxMsg(jxtxtwotzrys, bt, nrid, "0");
            if (!isSuccess) {
                log.error("发送绩效过期不自动提交提醒消息失败！--jxtxtwotzrys="+JSON.toJSONString(jxtxtwotzrys));
            }
        }
        if (!CollectionUtils.isEmpty(zdtjjxtzrys)) {
            String bt = xxglService.getMsg("ICOMM_JXTZ00003");
            String nrid = "ICOMM_JXTZ00005";
            isSuccess = commonSendJxMsg(zdtjjxtzrys, bt, nrid, "1");
            if (!isSuccess) {
                log.error("发送绩效自动提交消息失败！--zdtjjxtzrys="+JSON.toJSONString(zdtjjxtzrys));
            }
        }
    }

    private void sendJxConfirmMsg(List<GrjxDto> grjxDtos) {
        String ICOMM_JXTX00004 = xxglService.getMsg("ICOMM_JXTX00004");
        String ICOMM_JXTX00005 = xxglService.getMsg("ICOMM_JXTX00005");
        try {
            for (GrjxDto grjxDto : grjxDtos) {
                String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/JXList/TargetConfirmation/TargetConfirmation&grjxid="+grjxDto.getGrjxid()+"&jxmbid="+grjxDto.getJxmbid()+"&urlPrefix="+urlPrefix+"&wbfw=1", StandardCharsets.UTF_8);
                List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                btnJsonList.setTitle("小程序");
                btnJsonList.setActionUrl(internalbtn);
                btnJsonLists.add(btnJsonList);
                talkUtil.sendCardMessage(grjxDto.getYhm(), grjxDto.getYhid(),ICOMM_JXTX00004,StringUtil.replaceMsg(ICOMM_JXTX00005,
                        grjxDto.getMbmc(),grjxDto.getZqkssj()+"~"+grjxDto.getZqjssj(),
                        grjxDto.getZdrymc(),grjxDto.getSyrq()),btnJsonLists,"1");
            }
        }catch (Exception e){
            log.error("sendJxConfirmMsg----发送绩效确认消息失败,grjxDtos="+JSON.toJSONString(grjxDtos));
        }
    }

    private boolean commonSendJxMsg(List<Map<String, String>> tzrys, String bt, String nrid,String tjbj) {
        for (Map<String, String> tzry : tzrys) {
            if (StringUtil.isNotBlank(tzry.get("yhm"))){
                if ("1".equals(tjbj)){
                    talkUtil.sendWorkMessage(tzry.get("yhm"), tzry.get("yhid"), bt,
                            xxglService.getMsg(nrid, tzry.get("bt"),tzry.get("khmc"),tzry.get("khzq"),tzry.get("jzsj"),tzry.get("tjsj")));
                }else {
                    talkUtil.sendWorkMessage(tzry.get("yhm"), tzry.get("yhid"), bt,
                            xxglService.getMsg(nrid, tzry.get("bt"),tzry.get("khmc"),tzry.get("khzq"),tzry.get("jzsj")));
                }
            }
        }
        return true;
    }

    /**
     * 获取待办数据
     */
    public List<GrjxDto> getToDoList(GrjxDto grjxDto){
        return dao.getToDoList(grjxDto);
    }

    /**
     * 审核列表数据（不分页）
     */
    public List<GrjxDto> getAuditListData(GrjxDto grjxDto){
        // 获取人员ID和履历号
        List<GrjxDto> t_List= dao.getPagedAuditData(grjxDto);

        if (CollectionUtils.isEmpty(t_List))
            return t_List;

        List<GrjxDto> sqList = dao.getAuditList(t_List);

        commonService.setSqrxm(sqList);

        return sqList;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean discardPerformance(GrjxDto grjxDto) throws BusinessException{
        boolean isSuccess;
        grjxDto.setScbj("2");
        isSuccess = update(grjxDto);
        if (!isSuccess){
            throw new BusinessException("msg","废弃个人绩效失败！");
        }
        GrjxmxDto grjxmxDto = new GrjxmxDto();
        grjxmxDto.setScbj("2");
        grjxmxDto.setXgry(grjxDto.getXgry());
        grjxmxDto.setGrjxid(grjxDto.getGrjxid());
        isSuccess = grjxmxService.discard(grjxmxDto);
        if (!isSuccess){
            throw new BusinessException("msg","废弃个人绩效明细失败！");
        }
        return isSuccess;
    }
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean auditSavePerformance(GrjxDto grjxDto) throws BusinessException{
        boolean isSuccess;
        String pf_json = grjxDto.getPf_json();
        if (StringUtil.isNotBlank(pf_json)){
            List<GrjxmxDto> grjxmxDtos = JSON.parseArray(pf_json, GrjxmxDto.class);
            if (!CollectionUtils.isEmpty(grjxmxDtos)){
                for (GrjxmxDto grjxmxDto : grjxmxDtos) {
                    grjxmxDto.setXgry(grjxDto.getXgry());
                    grjxmxDto.setGrjxid(grjxDto.getGrjxid());
                    grjxmxDto.setFs(grjxmxDto.getJxfs());
                    grjxmxDto.setShr(grjxDto.getXgry());
                    grjxmxDto.setJxzf(grjxDto.getJxzf());
                    grjxmxDto.setGwid(grjxDto.getGwid());
                }
                isSuccess = grjxmxService.updateGrjxmxDtosWithGwid(grjxmxDtos);
                if (!isSuccess){
                    throw new BusinessException("msg","修改个人绩效明细信息失败！");
                }
            }
        }
        GrjxmxDto grjxmxDto = new GrjxmxDto();
        grjxmxDto.setGrjxid(grjxDto.getGrjxid());
        boolean isExist = false;
        String khzf = "0";
        List<GrjxmxDto> grjxmxDtos = grjxmxService.getDtoList(grjxmxDto);
        if(!CollectionUtils.isEmpty(grjxmxDtos)){
            for(GrjxmxDto dto:grjxmxDtos) {
                if(StringUtil.isNotBlank(dto.getFs()) && !dto.getFs().matches("-?\\d+(\\.\\d+)?")){
                    isExist = true;
                    break;
                }
            }
        }
        if(!isExist) {
            khzf = grjxmxService.getSumScore(grjxmxDto);
        }
        grjxDto.setKhzf(khzf);
        isSuccess = update(grjxDto);
        if (!isSuccess){
            throw new BusinessException("msg","修改个人绩效信息失败！");
        }
        return isSuccess;
    }
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSavePerformance(GrjxDto grjxDto) throws BusinessException{
        if ("auditSave".equals(grjxDto.getFlag())){
            return auditSavePerformance(grjxDto);
        }
        boolean isSuccess;
        boolean isExist = false;
        String pf_json = grjxDto.getPf_json();
        if (StringUtil.isNotBlank(pf_json)){
            List<GrjxmxDto> grjxmxDtos = JSON.parseArray(pf_json, GrjxmxDto.class);
            if (!CollectionUtils.isEmpty(grjxmxDtos)){
                for (GrjxmxDto grjxmxDto : grjxmxDtos) {
                    grjxmxDto.setXgry(grjxDto.getXgry());
                    grjxmxDto.setGrjxid(grjxDto.getGrjxid());
                    grjxmxDto.setFs(grjxmxDto.getJxfs());
                    grjxmxDto.setJxzf(grjxDto.getJxzf());
                    if(!grjxmxDto.getFs().matches("-?\\d+(\\.\\d+)?")){
                        isExist = true;
                    }
                }
                isSuccess = grjxmxService.updateGrjxmxDtosWithNull(grjxmxDtos);
                if (!isSuccess){
                    throw new BusinessException("msg","修改个人绩效明细信息失败！");
                }
            }
        }
        GrjxmxDto grjxmxDto = new GrjxmxDto();
        grjxmxDto.setGrjxid(grjxDto.getGrjxid());
        String khzf = "0";
        List<GrjxmxDto> grjxmxDtos = grjxmxService.getDtoList(grjxmxDto);
        if(!CollectionUtils.isEmpty(grjxmxDtos)){
            for(GrjxmxDto dto:grjxmxDtos) {
                if(StringUtil.isNotBlank(dto.getFs()) && !dto.getFs().matches("-?\\d+(\\.\\d+)?")){
                    isExist = true;
                    break;
                }
            }
        }
        if(!isExist) {
            khzf = grjxmxService.getSumScore(grjxmxDto);
        }
        grjxDto.setKhzf(khzf);
        if(!grjxDto.getJxzf().matches("-?\\d+(\\.\\d+)?")){
            grjxDto.setJxzf("0");
        }
        isSuccess = update(grjxDto);
        if (!isSuccess){
            throw new BusinessException("msg","修改个人绩效信息失败！");
        }
        return isSuccess;
    }

    @Override
    public List<String> getGrjxYears() {
        return dao.getGrjxYears();
    }

    @Override
    public List<Map<String,String>> getGrjxBms() {
        return dao.getGrjxBms();
    }

    @Override
    public boolean confirmPerformance(GrjxDto gxjxDto) {
        gxjxDto.setQrzt("1");
        return update(gxjxDto);
    }

    @Override
    public List<GrjxDto> getPagedListPerformanceConfirm(GrjxDto grjxDto) {
        return dao.getPagedListPerformanceConfirm(grjxDto);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean releaseRecallPerformance(GrjxDto grjxDto) throws BusinessException {
        if (!CollectionUtils.isEmpty(grjxDto.getIds())){
            List<GrjxDto> sendMsgs;
            if ("1".equals(grjxDto.getChbj())){
                sendMsgs = dao.getDtoListByIds(grjxDto);
                //ids是grjxid
                boolean isSuccess = delete(grjxDto);
                if (!isSuccess){
                    throw new BusinessException("msg","发放撤回个人绩效失败！");
                }
                GrjxmxDto grjxmxDto = new GrjxmxDto();
                grjxmxDto.setScry(grjxDto.getScry());
                grjxmxDto.setIds(grjxDto.getIds());
                isSuccess = grjxmxService.delete(grjxmxDto);
                if (!isSuccess){
                    throw new BusinessException("msg","发放撤回个人绩效明细失败！");
                }
            }else {
               //ids 是用户id
                sendMsgs = dao.getDtoListByYhIds(grjxDto);
                if (!CollectionUtils.isEmpty(sendMsgs)){
                    List<String> ids = sendMsgs.stream().map(GrjxDto::getGrjxid).filter(StringUtil::isNotBlank).collect(Collectors.toList());
                    grjxDto.setIds(ids);
                    boolean isSuccess = delete(grjxDto);
                    if (!isSuccess){
                        throw new BusinessException("msg","发放撤回个人绩效失败！");
                    }
                    GrjxmxDto grjxmxDto = new GrjxmxDto();
                    grjxmxDto.setIds(ids);
                    grjxmxDto.setScry(grjxDto.getScry());
                    isSuccess = grjxmxService.delete(grjxmxDto);
                    if (!isSuccess){
                        throw new BusinessException("msg","发放撤回个人绩效明细失败！");
                    }
                }
            }
            if (!CollectionUtils.isEmpty(sendMsgs)){
                for (GrjxDto sendMsg : sendMsgs) {
                    String ICOMM_JXTX00006 = xxglService.getMsg("ICOMM_JXTX00006");
                    String ICOMM_JXTX00007 = xxglService.getMsg("ICOMM_JXTX00007");
                    talkUtil.sendWorkMessage(sendMsg.getYhm(), sendMsg.getYhid(), ICOMM_JXTX00006,
                            StringUtil.replaceMsg(ICOMM_JXTX00007,
                                    sendMsg.getMbmc(),sendMsg.getZqkssj()+"~"+sendMsg.getZqjssj(),
                                    sendMsg.getZdrymc(),sendMsg.getSyrq()));
                }
            }
        }
        return true;
    }

    @Override
    public Map<String,Object> getPerformanceStatistics(YghmcDto yghmcDto){
        return dao.getPerformanceStatistics(yghmcDto);
    }
    @Override
    public List<Map<String, Object>> getPerformanceStatisticsView(YghmcDto yghmcDto){
        return dao.getPerformanceStatisticsView(yghmcDto);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delByIds(GrjxDto grjxDto) throws BusinessException{
        boolean flag =true;
        GrjxmxDto grjxmxDto = new GrjxmxDto();
        grjxmxDto.setIds(grjxDto.getIds());
        grjxmxDto.setScry(grjxDto.getScry());
        flag=grjxmxService.delete(grjxmxDto);
        if (!flag){
            throw new BusinessException("msg","删除明细失败");
        }
        flag=dao.delete(grjxDto)>0;
        if (!flag){
            throw new BusinessException("msg","删除绩效失败");
        }
        return true;
    }
    /*
        绩效目标审核通过后 校验是否要直接发放绩效
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean checkDistributePerformance(JxmbDto jxmbDto) throws BusinessException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        //生效日期
        Calendar sxrqCalendar = Calendar.getInstance();
        try {
            sxrqCalendar.setTime(DateUtils.parse(jxmbDto.getSxrq()));
        } catch (Exception e) {
            throw new BusinessException("日期转换失败!");
        }
        //发放日期
        int xffrq = Integer.parseInt(jxmbDto.getXffrq());
        //考核周期
        int khzqcskz2 = Integer.parseInt(jxmbDto.getKhzqcskz2());
        int khzqweek = new BigDecimal(khzqcskz2).multiply(new BigDecimal(7)).intValue();
        String zqkssj = null;
        String zqjssj = null;
        //本月
        if ("0".equals(jxmbDto.getFfkhy())){
            //判断 发放日期>0 并且 当前日期  >=  生效日期 + 发放日期
            if (xffrq>0){
                sxrqCalendar.add(Calendar.DATE,xffrq-1);
                if (date.getTime()>=sxrqCalendar.getTime().getTime()){
                    zqkssj = jxmbDto.getSxrq();
                    //往后推一个周期  计算周期结束时间
                    if ("WW".equals(jxmbDto.getKhzqcskz1())) {
                        sxrqCalendar.add(Calendar.DATE, khzqweek);
                    } else if ("YY".equals(jxmbDto.getKhzqcskz1())) {
                        sxrqCalendar.add(Calendar.YEAR, khzqcskz2);
                    } else if ("MM".equals(jxmbDto.getKhzqcskz1())) {
                        sxrqCalendar.add(Calendar.MONTH, khzqcskz2);
                    }
                    sxrqCalendar.add(Calendar.DATE,-xffrq);
                    zqjssj = format.format(sxrqCalendar.getTime());
                }
            //判断 发放日期<0 并且
            } else if (xffrq<0){
                //往后推一个周期  再加发放日期（负数）
                if ("WW".equals(jxmbDto.getKhzqcskz1())) {
                    sxrqCalendar.add(Calendar.DATE, khzqweek);
                } else if ("YY".equals(jxmbDto.getKhzqcskz1())) {
                    sxrqCalendar.add(Calendar.YEAR, khzqcskz2);
                } else if ("MM".equals(jxmbDto.getKhzqcskz1())) {
                    sxrqCalendar.add(Calendar.MONTH, khzqcskz2);
                }
                sxrqCalendar.add(Calendar.DATE, xffrq);
                //判断 当前日期 > 往后推一个周期  再加发放日期（负数）
                if (date.getTime()>=sxrqCalendar.getTime().getTime()){
                    zqkssj = jxmbDto.getSxrq();
                    sxrqCalendar.add(Calendar.DATE, -xffrq-1);
                    zqjssj = format.format(sxrqCalendar.getTime());
                }
            }
         //次月
        }else if ("1".equals(jxmbDto.getFfkhy())) {
            if (xffrq > 0) {
                sxrqCalendar.add(Calendar.DATE, xffrq - 1);
                //判断 发放日期>0 并且 当前日期  >  生效日期 + 发放日期
                if (date.getTime() >= sxrqCalendar.getTime().getTime()) {
                    //往前推一个周期  计算周期开始时间
                    if ("WW".equals(jxmbDto.getKhzqcskz1())) {
                        sxrqCalendar.add(Calendar.DATE, -khzqweek);
                    } else if ("YY".equals(jxmbDto.getKhzqcskz1())) {
                        sxrqCalendar.add(Calendar.YEAR, -khzqcskz2);
                    } else if ("MM".equals(jxmbDto.getKhzqcskz1())) {
                        sxrqCalendar.add(Calendar.MONTH, -khzqcskz2);
                    }
                    sxrqCalendar.add(Calendar.DATE, -xffrq + 1);
                    zqkssj = format.format(sxrqCalendar.getTime());
                    //往后推一个周期  计算周期结束时间
                    if ("WW".equals(jxmbDto.getKhzqcskz1())) {
                        sxrqCalendar.add(Calendar.DATE, khzqweek);
                    } else if ("YY".equals(jxmbDto.getKhzqcskz1())) {
                        sxrqCalendar.add(Calendar.YEAR, khzqcskz2);
                    } else if ("MM".equals(jxmbDto.getKhzqcskz1())) {
                        sxrqCalendar.add(Calendar.MONTH, khzqcskz2);
                    }
                    sxrqCalendar.add(Calendar.DATE, -1);
                    zqjssj = format.format(sxrqCalendar.getTime());
                }
            } else if (xffrq < 0){
                //往后推一个周期  再加发放日期（负数）
                if ("WW".equals(jxmbDto.getKhzqcskz1())) {
                    sxrqCalendar.add(Calendar.DATE, khzqweek);
                } else if ("YY".equals(jxmbDto.getKhzqcskz1())) {
                    sxrqCalendar.add(Calendar.YEAR, khzqcskz2);
                } else if ("MM".equals(jxmbDto.getKhzqcskz1())) {
                    sxrqCalendar.add(Calendar.MONTH, khzqcskz2);
                }
                sxrqCalendar.add(Calendar.DATE, xffrq);
                if (date.getTime()>=sxrqCalendar.getTime().getTime()){
                    //往前推一个周期  计算周期结束时间
                    if ("WW".equals(jxmbDto.getKhzqcskz1())) {
                        sxrqCalendar.add(Calendar.DATE, -khzqweek);
                    } else if ("YY".equals(jxmbDto.getKhzqcskz1())) {
                        sxrqCalendar.add(Calendar.YEAR, -khzqcskz2);
                    } else if ("MM".equals(jxmbDto.getKhzqcskz1())) {
                        sxrqCalendar.add(Calendar.MONTH, -khzqcskz2);
                    }
                    sxrqCalendar.add(Calendar.DATE, -xffrq+1);
                    zqjssj = format.format(sxrqCalendar.getTime());
                    //往前推一个周期  计算周期结束时间
                    if ("WW".equals(jxmbDto.getKhzqcskz1())) {
                        sxrqCalendar.add(Calendar.DATE, -khzqweek);
                    } else if ("YY".equals(jxmbDto.getKhzqcskz1())) {
                        sxrqCalendar.add(Calendar.YEAR, -khzqcskz2);
                    } else if ("MM".equals(jxmbDto.getKhzqcskz1())) {
                        sxrqCalendar.add(Calendar.MONTH, -khzqcskz2);
                    }
                    sxrqCalendar.add(Calendar.DATE, 1);
                    zqkssj = format.format(sxrqCalendar.getTime());
                }
            }
        }
        if (StringUtil.isNotBlank(zqkssj)&&StringUtil.isNotBlank(zqjssj)){
            jxmbDto.setZqkssj(zqkssj);
            jxmbDto.setZqjssj(zqjssj);
            jxmbDto.setIds(jxmbDto.getJxmbid());
            jxmbDto.setBatchFlag("1");
            jxmbDto.setCheckflag("delay");
            distributePerformance(jxmbDto);
        }
        return true;
    }
}

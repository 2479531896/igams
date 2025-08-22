package com.matridx.igams.hrm.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiAttendanceVacationQuotaUpdateRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiAttendanceVacationQuotaListResponse;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.RedisCommonKeyEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IUserService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.hrm.dao.entities.JqffjlDto;
import com.matridx.igams.hrm.dao.entities.JqffjlModel;
import com.matridx.igams.hrm.dao.entities.JqffszDto;
import com.matridx.igams.hrm.dao.entities.JqxzDto;
import com.matridx.igams.hrm.dao.entities.YhjqDto;
import com.matridx.igams.hrm.dao.entities.YhqjxxDto;
import com.matridx.igams.hrm.dao.post.IJqffjlDao;
import com.matridx.igams.hrm.service.svcinterface.IJqffjlService;
import com.matridx.igams.hrm.service.svcinterface.IJqffszService;
import com.matridx.igams.hrm.service.svcinterface.IJqxzService;
import com.matridx.igams.hrm.service.svcinterface.IYhjqService;
import com.matridx.igams.hrm.service.svcinterface.IYhqjxxService;
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
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:JYK
 */
@Service
public class JqffjlServiceImpl extends BaseBasicServiceImpl<JqffjlDto, JqffjlModel, IJqffjlDao> implements IJqffjlService {
    @Autowired
    DingTalkUtil dingTalkUtil;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IUserService userService;
    @Autowired
    IYhjqService yhjqService;
    @Autowired
    IJqffszService jqffszService;
    @Autowired
    IJqxzService jqxzService;
    @Autowired
    IYhqjxxService yhqjxxService;
    @Autowired
    IDdxxglService ddxxglService;
    @Autowired
    IXxglService xxglService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    private final Logger log = LoggerFactory.getLogger(JqffjlServiceImpl.class);
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addSaveVacationRecord(JqffjlDto jqffjlDto) throws BusinessException {
        String glyddid =  String.valueOf(redisUtil.get(RedisCommonKeyEnum.VACATION_ADMIN.getKey()));
        if (StringUtil.isBlank(glyddid)){
            throw new BusinessException("msg","请配置管理员钉钉id");
        }
        jqffjlDto.setCzrddid(glyddid);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        JqffjlDto jqffjlDto_sel = new JqffjlDto();
        jqffjlDto_sel.setYhids(jqffjlDto.getYhids());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        jqffjlDto_sel.setKssj(format.format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        jqffjlDto_sel.setYxq(format.format(calendar.getTime()));
        jqffjlDto_sel.setJqlx(jqffjlDto.getJqlx());
        List<JqffjlDto> list = dao.getDtoListByKsAndYxqAndYhAndJqlx(jqffjlDto_sel);
        if (!CollectionUtils.isEmpty(list)){
            StringBuilder msg = new StringBuilder("本月已存在数据</br>");
            for (JqffjlDto dto : list) {
                msg.append(dto.getZsxm()).append("：").append(dto.getJqlxmc()).append("</br>").append("(").append(dto.getKssj()).append("-").append(dto.getYxq()).append(")</br>");
            }
            msg.append("请确认是否继续发放!");
            throw new BusinessException("msg",msg.toString(),"addOne");
        }
        return confirmSaveOne(jqffjlDto);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean confirmSaveOne(JqffjlDto jqffjlDto) throws BusinessException {
        String token = dingTalkUtil.getDingTokenByUserId(jqffjlDto.getYhm());
        JcsjDto qjlx = this.getQjlx(jqffjlDto.getJqlx());
        List<String> yhids = jqffjlDto.getYhids();
        UserDto userDto = new UserDto();
        userDto.setIds(yhids);
        StringBuilder msg = new StringBuilder();
        List<UserDto> listByIds = userService.getListByIds(userDto);
        List<String> ddids = listByIds.stream().map(UserDto::getDdid).distinct().collect(Collectors.toList());
        jqffjlDto.setDdids(ddids);
        List<OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo> oapiLeaveQuotaUserListVos = dingTalkUtil.queryHolidayBalance(token, qjlx.getCskz2(), jqffjlDto.getCzrddid(), StringUtil.join(ddids, ","));
        //如果空指针 有可能操作人没权限
        for (OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo oapiLeaveQuotaUserListVo : oapiLeaveQuotaUserListVos) {
           if (!CollectionUtils.isEmpty(oapiLeaveQuotaUserListVo.getLeaveQuotas())){
               for (OapiAttendanceVacationQuotaListResponse.Leavequotas leaveQuota : oapiLeaveQuotaUserListVo.getLeaveQuotas()) {
                   YhjqDto yhjqDto = new YhjqDto();
                   yhjqDto.setDdid(leaveQuota.getUserid());
                   yhjqDto.setNd(leaveQuota.getQuotaCycle());
                   yhjqDto.setJqlx(jqffjlDto.getJqlx());
                   YhjqDto yhjqDto_Sel = yhjqService.getDtoByDdcs(yhjqDto);
                   String jqze = yhjqDto_Sel==null?"0":yhjqDto_Sel.getJqze();
                   String syed = yhjqDto_Sel==null?"0":yhjqDto_Sel.getSyed();
                   String dw = "0".equals(jqffjlDto.getDw())?"小时":"天";
                   String zsxm;
                   if (yhjqDto_Sel==null){
                       UserDto userDto_sel = new UserDto();
                       userDto_sel.setDdid(leaveQuota.getUserid());
                       UserDto yhByDdid = userService.getYhByDdid(userDto_sel);
                       zsxm = yhByDdid.getZsxm();
                   }else {
                       zsxm = yhjqDto_Sel.getZsxm();
                   }
                   Long quotaNumPerHour = leaveQuota.getQuotaNumPerHour();
                   Long usedNumPerHour = leaveQuota.getUsedNumPerHour();
                   Long quotaNumPerDay = leaveQuota.getQuotaNumPerDay();
                   Long usedNumPerDay = leaveQuota.getUsedNumPerDay();
                   if (quotaNumPerHour!=null){
                       compareToDDAndOA(msg,leaveQuota.getQuotaCycle(),jqze,syed,dw,zsxm,quotaNumPerHour,usedNumPerHour);
                   }else if (quotaNumPerDay!=null){
                       compareToDDAndOA(msg, leaveQuota.getQuotaCycle(), jqze, syed, dw, zsxm, quotaNumPerDay, usedNumPerDay);
                   }
               }
           }
        }
        jqffjlDto.setOapiLeaveQuotaUserListVos(oapiLeaveQuotaUserListVos);
        if (msg.length()>0){
            msg.append("钉钉假期额度和OA假期额度不一致，请确定是否继续发放？");
            throw new BusinessException("msg",msg.toString(),"addTwo");
        }
        return confirmSaveTwo(jqffjlDto);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean confirmSaveTwo(JqffjlDto jqffjlDto) throws BusinessException {
        //新增假期发放记录
        List<JqffjlDto> addJqffjlDtos = new ArrayList<>();
        //修改用户假期
        List<YhjqDto> modYhjqDtos = new ArrayList<>();
        //新增用户假期
        List<YhjqDto> addYhjqDtos = new ArrayList<>();
        //更新钉钉用户假期
        List<OapiAttendanceVacationQuotaUpdateRequest.LeaveQuotas> leaveQuotasList = new ArrayList<>();
        //没有钉钉假期数据人员
        List<String> kjqddids = new ArrayList<>(jqffjlDto.getDdids());
        //获取年度
        String nd = jqffjlDto.getKssj().substring(0, 4);
        //发放时长
        BigDecimal sc = new BigDecimal(jqffjlDto.getSc());
        //假期类型
        JcsjDto qjlx = this.getQjlx(jqffjlDto.getJqlx());
        //有钉钉假期额度信息处理
        for (String ddid : jqffjlDto.getDdids()) {
            List<OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo> oapiLeaveQuotaUserListVos = jqffjlDto.getOapiLeaveQuotaUserListVos();
            for (OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo oapiLeaveQuotaUserListVo : oapiLeaveQuotaUserListVos) {
                if (!CollectionUtils.isEmpty(oapiLeaveQuotaUserListVo.getLeaveQuotas())) {
                    for (OapiAttendanceVacationQuotaListResponse.Leavequotas leaveQuota : oapiLeaveQuotaUserListVo.getLeaveQuotas()) {
                        if (ddid.equals(leaveQuota.getUserid())&&nd.equals(leaveQuota.getQuotaCycle())) {
                            //有钉钉假期数据就删除
                            kjqddids.removeIf(s -> s.equals(ddid));
                            YhjqDto yhjqDto = new YhjqDto();
                            yhjqDto.setDdid(leaveQuota.getUserid());
                            yhjqDto.setNd(leaveQuota.getQuotaCycle());
                            yhjqDto.setJqlx(jqffjlDto.getJqlx());
                            YhjqDto yhjqDto_Sel = yhjqService.getDtoByDdcs(yhjqDto);
                            BigDecimal quotaNum = new BigDecimal(0);
                            BigDecimal usedNum = new BigDecimal(0);
                            if (leaveQuota.getQuotaNumPerHour() != null) {
                                quotaNum = new BigDecimal(leaveQuota.getQuotaNumPerHour()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                                usedNum = new BigDecimal(leaveQuota.getUsedNumPerHour()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                            } else if (leaveQuota.getQuotaNumPerDay() != null) {
                                quotaNum = new BigDecimal(leaveQuota.getQuotaNumPerDay()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                                usedNum = new BigDecimal(leaveQuota.getUsedNumPerDay()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                            }
                            BigDecimal ddsyed = quotaNum.subtract(usedNum).add(sc);
                            //假期发放记录
                            JqffjlDto jqffjlDto_add = new JqffjlDto();
                            jqffjlDto_add.setFfjlid(StringUtil.generateUUID());
                            jqffjlDto_add.setLrry(jqffjlDto.getLrry());
                            jqffjlDto_add.setJqlx(jqffjlDto.getJqlx());
                            jqffjlDto_add.setKssj(jqffjlDto.getKssj());
                            jqffjlDto_add.setYxq(jqffjlDto.getYxq());
                            jqffjlDto_add.setNd(nd);
                            jqffjlDto_add.setFffs(jqffjlDto.getFffs());
                            jqffjlDto_add.setDw(jqffjlDto.getDw());
                            jqffjlDto_add.setSc(jqffjlDto.getSc());
                            //如果查到的钉钉假期余额数据不为空：钉钉假期总额为接口查到的总额+发放时长，钉钉已用额度为查到的钉钉已用额度，钉钉剩余额度为查到的钉钉假期总额-钉钉已用额度+发放时长。
                            jqffjlDto_add.setDdze(String.valueOf(quotaNum.add(sc)));
                            jqffjlDto_add.setDdyyed(String.valueOf(usedNum));
                            jqffjlDto_add.setDdsyed(String.valueOf(ddsyed));
                            if (yhjqDto_Sel != null) {
                                //如果查到的OA假期余额数据不为空：假期总额为查到的OA假期额度+发放时长，已用额度为查到的OA已用额度，剩余额度为接收到的参数+发放时长，
                                BigDecimal oajqze = new BigDecimal(yhjqDto_Sel.getJqze());
                                BigDecimal oasyed = new BigDecimal(yhjqDto_Sel.getSyed());
                                jqffjlDto_add.setJeze(String.valueOf(oajqze.add(sc)));
                                jqffjlDto_add.setYyed(yhjqDto_Sel.getYyed());
                                jqffjlDto_add.setSyed(String.valueOf(oasyed.add(sc)));
                                jqffjlDto_add.setYhjqid(yhjqDto_Sel.getYhjqid());
                                jqffjlDto_add.setYhid(yhjqDto_Sel.getYhid());
                                //如果OA用户假期数据不为空（根据假期类型，用户id,年度判断 修正:直接通过主键id修改）： 修改igams_yhjq表数据，假期总额=假期总额+发放时长，已用额度=原已用额度，剩余额度=假期剩余额度+发放时长
                                YhjqDto yhjqDto_mod = new YhjqDto();
                                yhjqDto_mod.setYhjqid(yhjqDto_Sel.getYhjqid());
                                yhjqDto_mod.setKssj(jqffjlDto.getKssj());
                                yhjqDto_mod.setYxq(jqffjlDto.getYxq());
                                yhjqDto_mod.setJqze(String.valueOf(oajqze.add(sc)));
                                yhjqDto_mod.setSyed(String.valueOf(oasyed.add(sc)));
                                //钉钉总额度=钉钉查到的总额度+发放时长，钉钉已用额度=钉钉查到的已用额度，钉钉剩余额度=钉钉查到的剩余额度+发放时长。
                                yhjqDto_mod.setDdze(String.valueOf(quotaNum.add(sc)));
                                yhjqDto_mod.setDdsyed(String.valueOf(ddsyed));
                                yhjqDto_mod.setDdyyed(String.valueOf(usedNum));
                                modYhjqDtos.add(yhjqDto_mod);
                            } else {
                                //处理OA假期数据空的用户假期数据
                                YhjqDto yhjqDto_add = this.dealEmptyOAData(jqffjlDto, addYhjqDtos, nd, sc, leaveQuota.getUserid());
                                //处理OA假期数据空的 假期发放记录
                                jqffjlDto_add.setYhid(yhjqDto_add.getYhid());
                                jqffjlDto_add.setJeze(yhjqDto_add.getJqze());
                                jqffjlDto_add.setYyed(yhjqDto_add.getYyed());
                                jqffjlDto_add.setSyed(yhjqDto_add.getSyed());
                                jqffjlDto_add.setYhjqid(yhjqDto_add.getYhjqid());
                            }
                            addJqffjlDtos.add(jqffjlDto_add);
                            OapiAttendanceVacationQuotaUpdateRequest.LeaveQuotas leaveQuotas = new OapiAttendanceVacationQuotaUpdateRequest.LeaveQuotas();
                            leaveQuotas.setLeaveCode(qjlx.getCskz2());
                            leaveQuotas.setQuotaCycle(nd);
                            leaveQuotas.setStartTime(DateUtils.parseDate("yyyy-MM-dd",jqffjlDto.getKssj()).getTime());
                            leaveQuotas.setEndTime(DateUtils.parseDate("yyyy-MM-dd",jqffjlDto.getYxq()).getTime());
                            BigDecimal DDquotaNum = quotaNum.multiply(new BigDecimal(100)).add(sc.multiply(new BigDecimal(100)));
                            if (leaveQuota.getQuotaNumPerHour()!=null){
                                leaveQuotas.setQuotaNumPerHour(DDquotaNum.longValue());
                            }else {
                                leaveQuotas.setQuotaNumPerDay(DDquotaNum.longValue());
                            }
                            leaveQuotas.setReason("OA手动发放");
                            leaveQuotas.setUserid(ddid);
                            leaveQuotasList.add(leaveQuotas);
                        }
                    }
                }
            }
        }
        //没有钉钉假期额度信息处理
        for (String kjqddid : kjqddids) {
            //新增假期发放记录
            JqffjlDto jqffjlDto_add = new JqffjlDto();
            YhjqDto yhjqDto = new YhjqDto();
            yhjqDto.setDdid(kjqddid);
            yhjqDto.setNd(nd);
            yhjqDto.setJqlx(jqffjlDto.getJqlx());
            YhjqDto yhjqDto_Sel = yhjqService.getDtoByDdcs(yhjqDto);
            if (yhjqDto_Sel!=null){
                jqffjlDto_add.setYhjqid(yhjqDto_Sel.getYhjqid());
                jqffjlDto_add.setYhid(yhjqDto_Sel.getYhid());
                //如果查到的OA假期余额数据不为空：假期总额为查到的OA假期额度+发放时长，已用额度为查到的OA已用额度，剩余额度为接收到的参数+发放时长，
                BigDecimal oajqze = new BigDecimal(yhjqDto_Sel.getJqze());
                BigDecimal oasyed = new BigDecimal(yhjqDto_Sel.getSyed());
                jqffjlDto_add.setJeze(String.valueOf(oajqze.add(sc)));
                jqffjlDto_add.setYyed(yhjqDto_Sel.getYyed());
                jqffjlDto_add.setSyed(String.valueOf(oasyed.add(sc)));
                //如果OA用户假期数据不为空（根据假期类型，用户id,年度判断 修正:直接通过主键id修改）： 修改igams_yhjq表数据，假期总额=假期总额+发放时长，已用额度=原已用额度，剩余额度=假期剩余额度+发放时长
                YhjqDto yhjqDto_mod = new YhjqDto();
                yhjqDto_mod.setYhjqid(yhjqDto_Sel.getYhjqid());
                yhjqDto_mod.setKssj(jqffjlDto.getKssj());
                yhjqDto_mod.setYxq(jqffjlDto.getYxq());
                yhjqDto_mod.setJqze(String.valueOf(oajqze.add(sc)));
                yhjqDto_mod.setSyed(String.valueOf(oasyed.add(sc)));
                //钉钉总额度=钉钉查到的总额度+发放时长，钉钉已用额度=钉钉查到的已用额度，钉钉剩余额度=钉钉查到的剩余额度+发放时长。
                yhjqDto_mod.setDdze(jqffjlDto.getSc());
                yhjqDto_mod.setDdsyed(jqffjlDto.getSc());
                yhjqDto_mod.setDdyyed("0");
                modYhjqDtos.add(yhjqDto_mod);
            }else {
                //处理OA假期数据空的用户假期数据
                YhjqDto yhjqDto_add = this.dealEmptyOAData(jqffjlDto, addYhjqDtos, nd, sc, kjqddid);
                jqffjlDto_add.setYhjqid(yhjqDto_add.getYhjqid());
                jqffjlDto_add.setYhid(yhjqDto_add.getYhid());
                //如果查到的OA假期余额数据为空：假期总额=发放时长，已用额度=0，剩余额度=发放时长，
                jqffjlDto_add.setJeze(jqffjlDto.getSc());
                jqffjlDto_add.setYyed("0");
                jqffjlDto_add.setSyed(jqffjlDto.getSc());
            }
            jqffjlDto_add.setFfjlid(StringUtil.generateUUID());
            jqffjlDto_add.setFffs(jqffjlDto.getFffs());
            jqffjlDto_add.setJqlx(jqffjlDto.getJqlx());
            jqffjlDto_add.setKssj(jqffjlDto.getKssj());
            jqffjlDto_add.setYxq(jqffjlDto.getYxq());
            jqffjlDto_add.setNd(nd);
            jqffjlDto_add.setFffs(jqffjlDto.getFffs());
            jqffjlDto_add.setDw(jqffjlDto.getDw());
            jqffjlDto_add.setSc(jqffjlDto.getSc());
            // 如果查到的钉钉假期额度数据为空：钉钉假期总额=发放时长，钉钉已用额度=0，钉钉剩余额度=发放时长。
            jqffjlDto_add.setDdze(jqffjlDto.getSc());
            jqffjlDto_add.setDdyyed("0");
            jqffjlDto_add.setDdsyed(jqffjlDto.getSc());
            jqffjlDto_add.setLrry(jqffjlDto.getLrry());
            addJqffjlDtos.add(jqffjlDto_add);
            //更新钉钉假期数据
            OapiAttendanceVacationQuotaUpdateRequest.LeaveQuotas leaveQuotas = new OapiAttendanceVacationQuotaUpdateRequest.LeaveQuotas();
            leaveQuotas.setLeaveCode(qjlx.getCskz2());
            leaveQuotas.setQuotaCycle(nd);
            leaveQuotas.setStartTime(DateUtils.parseDate("yyyy-MM-dd",jqffjlDto.getKssj()).getTime());
            leaveQuotas.setEndTime(DateUtils.parseDate("yyyy-MM-dd",jqffjlDto.getYxq()).getTime());
            BigDecimal DDquotaNum = sc.multiply(new BigDecimal(100));
            if ("0".equals(jqffjlDto.getDw())){
                leaveQuotas.setQuotaNumPerHour(DDquotaNum.longValue());
            }else {
                leaveQuotas.setQuotaNumPerDay(DDquotaNum.longValue());
            }
            leaveQuotas.setReason("OA手动发放");
            leaveQuotas.setUserid(kjqddid);
            leaveQuotasList.add(leaveQuotas);
        }
        if (!CollectionUtils.isEmpty(addJqffjlDtos)){
            boolean isSuccess = dao.insertJqffjlDtos(addJqffjlDtos);
            if (!isSuccess){
                throw new BusinessException("msg","新增OA假期发放记录失败！");
            }
        }
        if (!CollectionUtils.isEmpty(addYhjqDtos)){
            boolean isSuccess = yhjqService.insertYhjqDtos(addYhjqDtos);
            if (!isSuccess){
                throw new BusinessException("msg","新增OA用户假期失败！");
            }
        }
        if (!CollectionUtils.isEmpty(modYhjqDtos)){
            boolean isSuccess = yhjqService.updateYhjqDtos(modYhjqDtos);
            if (!isSuccess){
                throw new BusinessException("msg","修改OA用户假期失败！");
            }
        }
        boolean isSuccess = dingTalkUtil.updateVacationQuota(leaveQuotasList, jqffjlDto.getCzrddid(),dingTalkUtil.getDingTokenByUserId(jqffjlDto.getYhm()));
        if (!isSuccess){
            throw new BusinessException("msg","同步更新钉钉假期额度失败，请联系管理员！");
        }
        return true;
    }

    private YhjqDto dealEmptyOAData(JqffjlDto jqffjlDto, List<YhjqDto> addYhjqDtos, String nd, BigDecimal sc, String kjqddid) {
        //如果OA用户假期数据为空：新增igams_yhjq表数据，假期总额=发放时长，已用额度=0，剩余额度=发放时长，年度=开始时间的年度，
        //有效期为发放记录的有效期，开始时间为发放记录的开始时间，钉钉总额=发放时长，钉钉已用额度=0，钉钉剩余额度=发放时长。
        UserDto userDto = new UserDto();
        userDto.setDdid(kjqddid);
        UserDto yhByDdid = userService.getYhByDdid(userDto);
        YhjqDto yhjqDto_add = new YhjqDto();
        yhjqDto_add.setYhjqid(StringUtil.generateUUID());
        yhjqDto_add.setDw(jqffjlDto.getDw());
        yhjqDto_add.setYhid(yhByDdid.getYhid());
        yhjqDto_add.setJqlx(jqffjlDto.getJqlx());
        yhjqDto_add.setJqze(String.valueOf(sc));
        yhjqDto_add.setYyed("0");
        yhjqDto_add.setSyed(String.valueOf(sc));
        yhjqDto_add.setNd(nd);
        yhjqDto_add.setYxq(jqffjlDto.getYxq());
        yhjqDto_add.setKssj(jqffjlDto.getKssj());
        yhjqDto_add.setDdze(String.valueOf(sc));
        yhjqDto_add.setDdyyed("0");
        yhjqDto_add.setDdsyed(String.valueOf(sc));
        addYhjqDtos.add(yhjqDto_add);
        return yhjqDto_add;
    }

    private void compareToDDAndOA(StringBuilder msg, String nd, String jqze, String syed, String dw, String zsxm, Long quotaNumPer, Long usedNumPer) {
        BigDecimal quotaNum = new BigDecimal(quotaNumPer).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        BigDecimal usedNum = new BigDecimal(usedNumPer).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        BigDecimal ddsyed = quotaNum.subtract(usedNum);
        if (ddsyed.compareTo(new BigDecimal(syed))!=0||quotaNum.compareTo(new BigDecimal(jqze))!=0){
            msg.append(zsxm).append(" 年度：").append(nd).append("</br>");
            msg.append("钉钉假期总额：").append(quotaNum).append(dw).append("</br>");
            msg.append("钉钉假期剩余额度：").append(ddsyed).append(dw).append("</br>");
            msg.append("OA假期总额：").append(jqze).append(dw).append("</br>");
            msg.append("OA假期剩余额度：").append(syed).append(dw).append("</br>");
        }
    }
    public JcsjDto getQjlx(String csid) {
        JcsjDto qjlx = null;
        List<JcsjDto> jqlxs = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.HOLIDAY_TYPE.getCode());//假期类型
        for (JcsjDto jqlx : jqlxs) {
            if (jqlx.getCsid().equals(csid)){
                qjlx = jqlx;
                break;
            }
        }
        return qjlx;
    }

    @Override
    public List<String> getAllNd() {
        return dao.getAllNd();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delVacationRecord(JqffjlDto jqffjlDto) throws BusinessException {
        String glyddid =  String.valueOf(redisUtil.get(RedisCommonKeyEnum.VACATION_ADMIN.getKey()));
        if (StringUtil.isBlank(glyddid)){
            throw new BusinessException("msg","请配置管理员钉钉id");
        }
        jqffjlDto.setCzrddid(glyddid);
        String token = dingTalkUtil.getDingTokenByUserId(jqffjlDto.getYhm());
        // 根据用户id,假期类型，年度判断钉钉假期总额和剩余额度是否和OA假期的总额和剩余额度一致
        YhjqDto yhjqDto_t = new YhjqDto();
        yhjqDto_t.setIds(jqffjlDto.getYhjqids());
        List<YhjqDto> dtoList = yhjqService.getDtoList(yhjqDto_t);
        StringBuilder msg = new StringBuilder();
        List<OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo> oapiLeaveQuotaUserListVoArrayList = new ArrayList<>();
        for (YhjqDto dto : dtoList) {
            if (StringUtil.isNotBlank(dto.getYeqksj())) {
                throw new BusinessException("msg", dto.getZsxm()+"的"+dto.getNd()+"年度的假期余额已经清空不允许删除！");
            }
        }
        for (YhjqDto dto : dtoList) {
            String dw = "0".equals(dto.getDw())?"小时":"天";
            List<OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo> oapiLeaveQuotaUserListVos = dingTalkUtil.queryHolidayBalance(token, dto.getJqlxcskz2(), jqffjlDto.getCzrddid(), dto.getDdid());
            for (OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo oapiLeaveQuotaUserListVo : oapiLeaveQuotaUserListVos) {
                if (!CollectionUtils.isEmpty(oapiLeaveQuotaUserListVo.getLeaveQuotas())){
                    for (OapiAttendanceVacationQuotaListResponse.Leavequotas leaveQuota : oapiLeaveQuotaUserListVo.getLeaveQuotas()) {
                        if (dto.getNd().equals(leaveQuota.getQuotaCycle())&&dto.getDdid().equals(leaveQuota.getUserid())){
                            Long quotaNumPerHour = leaveQuota.getQuotaNumPerHour();
                            Long usedNumPerHour = leaveQuota.getUsedNumPerHour();
                            Long quotaNumPerDay = leaveQuota.getQuotaNumPerDay();
                            Long usedNumPerDay = leaveQuota.getUsedNumPerDay();
                            if (quotaNumPerHour!=null){
                                compareToDDAndOA(msg,leaveQuota.getQuotaCycle(),dto.getJqze(),dto.getSyed(),dw,dto.getZsxm(),quotaNumPerHour,usedNumPerHour);
                            }else if (quotaNumPerDay!=null){
                                compareToDDAndOA(msg, leaveQuota.getQuotaCycle(),dto.getJqze(),dto.getSyed(),dw,dto.getZsxm(), quotaNumPerDay, usedNumPerDay);
                            }
                        }
                    }
                } else {
                    compareToDDAndOA(msg,dto.getNd(),dto.getJqze(),dto.getSyed(),dw,dto.getZsxm(),0L,0L);
                }
            }
            oapiLeaveQuotaUserListVoArrayList.addAll(oapiLeaveQuotaUserListVos);
        }
        jqffjlDto.setOapiLeaveQuotaUserListVos(oapiLeaveQuotaUserListVoArrayList);
        if (msg.length()>0){
            msg.append("钉钉假期额度和OA假期额度不一致，请确定是否继续更新？");
            throw new BusinessException("msg",msg.toString(),"delOne");
        }
        return confirmDelVacationRecord(jqffjlDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean confirmDelVacationRecord(JqffjlDto jqffjlDto) throws BusinessException {
        List<JqffjlDto> jqffjlDtos = dao.getJqffjlDtosGroupByYhjq(jqffjlDto);
        //错误信息
        StringBuilder msg = new StringBuilder();
        //更新钉钉用户假期
        List<OapiAttendanceVacationQuotaUpdateRequest.LeaveQuotas> leaveQuotasList = new ArrayList<>();
        //修改用户假期
        List<YhjqDto> modYhjqDtos = new ArrayList<>();
        for (JqffjlDto dto : jqffjlDtos) {
            BigDecimal sc = new BigDecimal(dto.getSc());
            BigDecimal yhjqze = new BigDecimal(dto.getYhjqze());
            BigDecimal yhyyed = new BigDecimal(dto.getYhyyed());
            BigDecimal yhsyed = new BigDecimal(dto.getYhsyed());
            BigDecimal yhddze = new BigDecimal(dto.getYhddze());
            BigDecimal yhddsyed = new BigDecimal(dto.getYhddsyed());
            if (yhjqze.subtract(yhyyed).compareTo(sc)<0){
                msg.append("用户名：").append(dto.getYhm()).append(": 年度：").append(dto.getNd()).append("</br>");
                msg.append("假期类型:").append(dto.getJqlxmc()).append("OA剩余额度小于删除的发放时长</br>");
            }
            YhjqDto yhjqDto_mod = new YhjqDto();
            yhjqDto_mod.setYhjqid(dto.getYhjqid());
            yhjqDto_mod.setJqze(String.valueOf(yhjqze.subtract(sc)));
            yhjqDto_mod.setSyed(String.valueOf(yhsyed.subtract(sc)));
            yhjqDto_mod.setDdze(String.valueOf(yhddze.subtract(sc)));
            yhjqDto_mod.setDdsyed(String.valueOf(yhddsyed.subtract(sc)));
            modYhjqDtos.add(yhjqDto_mod);
            for (OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo oapiLeaveQuotaUserListVo : jqffjlDto.getOapiLeaveQuotaUserListVos()) {
                if (!CollectionUtils.isEmpty(oapiLeaveQuotaUserListVo.getLeaveQuotas())){
                    for (OapiAttendanceVacationQuotaListResponse.Leavequotas leaveQuota : oapiLeaveQuotaUserListVo.getLeaveQuotas()) {
                        if(dto.getDdid().equals(leaveQuota.getUserid())&&dto.getNd().equals(leaveQuota.getQuotaCycle())){
                            BigDecimal quotaNum = new BigDecimal(0);
                            BigDecimal usedNum = new BigDecimal(0);
                            BigDecimal ddsc = sc.multiply(new BigDecimal(100));
                            if (leaveQuota.getQuotaNumPerHour() != null) {
                                quotaNum = new BigDecimal(leaveQuota.getQuotaNumPerHour());
                                usedNum = new BigDecimal(leaveQuota.getUsedNumPerHour());
                            } else if (leaveQuota.getQuotaNumPerDay() != null) {
                                quotaNum = new BigDecimal(leaveQuota.getQuotaNumPerDay());
                                usedNum = new BigDecimal(leaveQuota.getUsedNumPerDay());
                            }
                            BigDecimal ddsyed = quotaNum.subtract(usedNum);
                            if (ddsyed.compareTo(ddsc)<0){
                                msg.append("用户名：").append(dto.getYhm()).append(": 年度：").append(dto.getNd()).append("</br>");
                                msg.append("假期类型:").append(dto.getJqlxmc()).append("钉钉剩余额度小于删除的发放时长</br>");
                            }
                            //更新钉钉假期数据
                            OapiAttendanceVacationQuotaUpdateRequest.LeaveQuotas leaveQuotas = new OapiAttendanceVacationQuotaUpdateRequest.LeaveQuotas();
                            leaveQuotas.setLeaveCode(dto.getJqlxcskz2());
                            leaveQuotas.setQuotaCycle(dto.getNd());
                            leaveQuotas.setStartTime(DateUtils.parseDate("yyyy-MM-dd",dto.getYhjqkssj()).getTime());
                            leaveQuotas.setEndTime(DateUtils.parseDate("yyyy-MM-dd",dto.getYhjqjssj()).getTime());
                            if (leaveQuota.getQuotaNumPerHour()!=null){
                                BigDecimal ddze = new BigDecimal(leaveQuota.getQuotaNumPerHour()).subtract(ddsc);
                                leaveQuotas.setQuotaNumPerHour(ddze.longValue());
                            }else {
                                BigDecimal ddze = new BigDecimal(leaveQuota.getQuotaNumPerDay()).subtract(ddsc);
                                leaveQuotas.setQuotaNumPerDay(ddze.longValue());
                            }
                            leaveQuotas.setReason("OA删除假期记录调整钉钉假期");
                            leaveQuotas.setUserid(dto.getDdid());
                            leaveQuotasList.add(leaveQuotas);
                        }
                    }
                }else {
                    msg.append("用户名：").append(dto.getYhm()).append(": 年度：").append(dto.getNd()).append("</br>");
                    msg.append("假期类型:").append(dto.getJqlxmc()).append("钉钉额度为空</br>");
                }
            }
        }
        if (msg.length()>0){
            msg.append("不允许删除！");
            throw new BusinessException("msg",msg.toString());
        }
        boolean isSuccess = delete(jqffjlDto);
        if (!isSuccess){
            throw new BusinessException("msg","删除假期发放记录失败！");
        }
        isSuccess= yhjqService.updateYhjqDtos(modYhjqDtos);
        if (!isSuccess){
            throw new BusinessException("msg","修改用户假期额度失败！");
        }
        isSuccess = dingTalkUtil.updateVacationQuota(leaveQuotasList, jqffjlDto.getCzrddid(),dingTalkUtil.getDingTokenByUserId(jqffjlDto.getYhm()));
        if (!isSuccess){
            throw new BusinessException("msg","同步更新钉钉假期额度失败，请联系管理员！");
        }
        return true;
    }
    /**
     *  定时发放假期
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void taskDistributeVacation(Map<String,String> map) throws BusinessException{
        String glyddid =  String.valueOf(redisUtil.get(RedisCommonKeyEnum.VACATION_ADMIN.getKey()));
        if (StringUtil.isBlank(glyddid)){
            throw new BusinessException("msg","请配置管理员钉钉id");
        }
        String wbcxid = map.get("wbcxid");
        if (StringUtil.isBlank(wbcxid)){
            throw new BusinessException("msg","请配置外部程序id");
        }
        //默认校验 校验是否发放 超过指定异常发放额度的数据
        boolean sfjy = true;
        //默认9小时
        BigDecimal yced = new BigDecimal(9);
        //是否校验
        String sfjyStr = map.get("sfjy");
        if (StringUtil.isNotBlank(sfjyStr)){
            sfjy = Boolean.parseBoolean(sfjyStr);
        }
        //异常额度
        String ycedStr = map.get("yced");
        if (StringUtil.isNotBlank(ycedStr)){
            yced = new BigDecimal(ycedStr);
        }
        String token = dingTalkUtil.getToken(wbcxid);
        //OA和钉钉不一致消息
        boolean sffs = false;
        Calendar nowCal = Calendar.getInstance();
        //今年总天数
        BigDecimal dnzts = new BigDecimal(nowCal.getActualMaximum(Calendar.DAY_OF_YEAR));
        String nowStr = DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd");
        //将String转LocalDateTime
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //年
        String dqnf = String.valueOf(nowCal.get(Calendar.YEAR));
        //获取没有发放过的假期发放设置
        List<JqffszDto> jqffszDtos = jqffszService.getListWithDistribute(new JqffszDto());
        //修改用户假期
        List<YhjqDto> modYhjqDtos = new ArrayList<>();
        //新增用户假期
        List<YhjqDto> addYhjqDtos = new ArrayList<>();
        //新增假期发放记录
        List<JqffjlDto> addJqffjlDtos = new ArrayList<>();
        //修改假期发放设置的累计额度
        List<JqffszDto> modJqffszDtos = new ArrayList<>();
        //异常发放记录
        List<Map<String,String>> ycFfjl = new ArrayList<>();
        //更新钉钉用户假期
        List<OapiAttendanceVacationQuotaUpdateRequest.LeaveQuotas> leaveQuotasList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(jqffszDtos)){
            List<String> ffszids = jqffszDtos.stream().map(JqffszDto::getFfszid).distinct().collect(Collectors.toList());
            //假期设置包含的用户
            List<String> yhids = jqffszDtos.stream().map(JqffszDto::getYhid).distinct().collect(Collectors.toList());
            JqxzDto jqxzDto_sel = new JqxzDto();
            jqxzDto_sel.setFfszids(ffszids);
            //各个用户的请假信息
            List<YhqjxxDto> qjxxs = new ArrayList<>();
            //获取假期发放限制
            List<JqxzDto> jqxzDtos = jqxzService.getDtoList(jqxzDto_sel);
            if (!CollectionUtils.isEmpty(jqxzDtos)){
                //获取需要限制的假期类型
                List<String> jqlxs = jqxzDtos.stream().map(JqxzDto::getJqlx).distinct().collect(Collectors.toList());
                YhqjxxDto yhqjxxDto = new YhqjxxDto();
                yhqjxxDto.setYhids(yhids);
                yhqjxxDto.setQjlxs(jqlxs);
                yhqjxxDto.setNd(dqnf);
                yhqjxxDto.setZt("80");
                qjxxs = yhqjxxService.getDtoListGroupYhAndQjlx(yhqjxxDto);
            }
            //判断是否符合发放规则
            for (JqffszDto jqffszDto : jqffszDtos) {
                boolean sffjq = true;
                if (!CollectionUtils.isEmpty(jqxzDtos)){
                    List<JqxzDto> thisJqxzs = jqxzDtos.stream().filter(e -> e.getFfszid().equals(jqffszDto.getFfszid())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(thisJqxzs)){
                        for (JqxzDto thisJqxz : thisJqxzs) {
                            BigDecimal xzsc = new BigDecimal(thisJqxz.getSc());
                            if (!CollectionUtils.isEmpty(qjxxs)){
                                for (YhqjxxDto qjxx : qjxxs) {
                                    //假期限制是假期发放设置的子集直接拿假期发放设置的yhid  由于假期信息是所有数据 所以加上yhid和jqlx判断
                                    //判断假期限制表的假期类型在当前年份请假的总时长，是否大于假期限制表的时长，如果大于，不发放额度，如果不大于，发放额度。
                                    if (jqffszDto.getYhid().equals(qjxx.getYhid())&&
                                            thisJqxz.getJqlx().equals(qjxx.getQjlx())&&
                                            xzsc.compareTo(new BigDecimal(qjxx.getQjsc()))<0){
                                        sffjq = false;
                                        break;
                                    }
                                }
                            }
                            //有一个限制不满足直接停止
                            if (!sffjq){
                                break;
                            }
                        }
                    }
                }
                //不满足发放条件跳过循环
                if (sffjq) {
                    //发放额度
                    BigDecimal ffed;
                    //假期发放设置时长
                    BigDecimal ffszsc = new BigDecimal(jqffszDto.getSc());
                    //累计额度
                    BigDecimal ljed = new BigDecimal(jqffszDto.getLjed());
                    //年限转移额度
                    BigDecimal nxzyed = new BigDecimal(jqffszDto.getNxzyed());
                    //开始时间年份
                    String kssjnf = jqffszDto.getKssj().substring(0, 4);
                    //当前日期
                    LocalDate dqrq = LocalDate.parse(nowStr, df);
                    // 判断开始时间是否为当年，如果是，计算开始时间到当前年份12月31号的天数
                    if (dqnf.equals(kssjnf)) {
                        LocalDate kssj = LocalDate.parse(jqffszDto.getKssj(), df);
                        //当前日期-开始日期
                        long days = ChronoUnit.DAYS.between(kssj,dqrq)+1;
                        //((当前日期-开始日期)/当年总天数*时长)+ 年限转移额度- 累计额度 = 发放额度
                        //舍去制，截断操作，后面所有数字直接去除。结果会向原点方向对齐。  RoundingMode.DOWN
                        ffed = new BigDecimal(days).divide(dnzts, 4, RoundingMode.DOWN).multiply(ffszsc).add(nxzyed).subtract(ljed).setScale(2, RoundingMode.DOWN);
                    } else {
                        // 如果开始时间不是当前年份：
                        //当年第一天
                        LocalDate dyt = LocalDate.parse(dqnf+"-01-01", df);
                        //当前日期-当年第一天
                        long days = ChronoUnit.DAYS.between(dyt,dqrq)+1;
                        //((当前日期-当年第一天)/当年总天数*时长)+ 年限转移额度- 累计额度 = 发放额度
                        //舍去制，截断操作，后面所有数字直接去除。结果会向原点方向对齐。  RoundingMode.DOWN
                        ffed = new BigDecimal(days).divide(dnzts, 4, RoundingMode.DOWN).multiply(ffszsc).add(nxzyed).subtract(ljed).setScale(2, RoundingMode.DOWN);
                    }
                    //如果校验异常发放额度
                    if (sfjy){
                        //如果发放额度大于异常额度
                        if (ffed.compareTo(yced)>0){
                            Map<String, String> ycjl = new HashMap<>();
                            ycjl.put("yhm",jqffszDto.getYhm());
                            ycjl.put("zsxm",jqffszDto.getZsxm());
                            ycjl.put("yced",String.valueOf(ffed));
                            ycFfjl.add(ycjl);
                            log.error("定时任务异常假期额度：人员{},假期发放设置信息：{}",JSON.toJSONString(ycjl),JSON.toJSONString(jqffszDto));
                            //跳过循环不发放假期
                            continue;
                        }
                    }
                    boolean sfff = false;
                    //如果是今年最后一天 把所有没发的直接发放
                    if (nowStr.equals(dqnf+"-12-31")){
                        sfff = true;
                    }else {
                        //舍去制，截断操作，后面所有数字直接去除。结果会向原点方向对齐。  RoundingMode.DOWN 重要的事情说三遍
                        // (发放额度) / 8
                        ffed = ffed.divide(new BigDecimal(8),0,RoundingMode.DOWN);
                        //如果满8的倍数就发放
                        if (ffed.compareTo(new BigDecimal(1))>=0){
                            ffed = ffed.multiply(new BigDecimal(8));
                            sfff = true;
                            //修改累计额度 累计额度 = 发放额度+之前的累计额度
                            JqffszDto jqffszDto_mod = new JqffszDto();
                            jqffszDto_mod.setFfszid(jqffszDto.getFfszid());
                            jqffszDto_mod.setLjed(String.valueOf(ffed));
                            modJqffszDtos.add(jqffszDto_mod);
                        }
                    }
                    //是否发放
                    if (sfff) {
                        //根据用户id,假期类型，年度，有效期判断用户假期表数据是否存在，
                        YhjqDto yhjqDto = new YhjqDto();
                        yhjqDto.setDdid(jqffszDto.getDdid());
                        yhjqDto.setNd(dqnf);
                        yhjqDto.setJqlx(jqffszDto.getJqlx());
                        YhjqDto yhjqDto_Sel = yhjqService.getDtoByDdcs(yhjqDto);
                        //OA假期额度
                        BigDecimal oajqze = new BigDecimal(0);
                        if (yhjqDto_Sel != null) {
                            oajqze = new BigDecimal(yhjqDto_Sel.getJqze());
                        }
                        //有效期
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(DateUtils.parseDate("yyyy-MM-dd", dqnf + "-" + jqffszDto.getJzrq()));
                        if ("1".equals(jqffszDto.getJzlx())) {
                            calendar.add(Calendar.YEAR, 1);
                        }
                        BigDecimal quotaNum = new BigDecimal(0);
                        BigDecimal usedNum = new BigDecimal(0);
                        //获取钉钉额度信息
                        List<OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo> oapiLeaveQuotaUserListVos = dingTalkUtil.queryHolidayBalance(token, jqffszDto.getJqlxcskz2(), glyddid, jqffszDto.getDdid());
                        for (OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo oapiLeaveQuotaUserListVo : oapiLeaveQuotaUserListVos) {
                            if (!CollectionUtils.isEmpty(oapiLeaveQuotaUserListVo.getLeaveQuotas())) {
                                for (OapiAttendanceVacationQuotaListResponse.Leavequotas leaveQuota : oapiLeaveQuotaUserListVo.getLeaveQuotas()) {
                                    if (dqnf.equals(leaveQuota.getQuotaCycle())) {
                                        if (leaveQuota.getQuotaNumPerHour() != null) {
                                            quotaNum = new BigDecimal(leaveQuota.getQuotaNumPerHour()).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
                                            usedNum = new BigDecimal(leaveQuota.getUsedNumPerHour()).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
                                        } else if (leaveQuota.getQuotaNumPerDay() != null) {
                                            quotaNum = new BigDecimal(leaveQuota.getQuotaNumPerDay()).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
                                            usedNum = new BigDecimal(leaveQuota.getUsedNumPerDay()).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
                                        }
                                        if (quotaNum.compareTo(oajqze) != 0) {
                                            sffs = true;
                                        }
                                    }
                                }
                            } else {
                                sffs = true;
                            }
                        }
                        //新增假期发放记录
                        JqffjlDto jqffjlDto_add = new JqffjlDto();
                        //如果存在：
                        if (yhjqDto_Sel != null) {
                            BigDecimal jqze = new BigDecimal(yhjqDto_Sel.getJqze());
                            BigDecimal syed = new BigDecimal(yhjqDto_Sel.getSyed());
                            // 更新假期总额和剩余额度，假期总额=原假期总额+发放额度，剩余额度=原剩余额度+发放额度，还有下面查出来的钉钉假期总额，钉钉已用额度和钉钉剩余额度。
                            YhjqDto yhjqDto_mod = new YhjqDto();
                            yhjqDto_mod.setYhjqid(yhjqDto_Sel.getYhjqid());
                            yhjqDto_mod.setJqze(String.valueOf(jqze.add(ffed)));
                            yhjqDto_mod.setSyed(String.valueOf(syed.add(ffed)));
                            yhjqDto_mod.setDdze(String.valueOf(quotaNum.add(ffed)));
                            yhjqDto_mod.setDdsyed(String.valueOf(quotaNum.subtract(usedNum).add(ffed)));
                            yhjqDto_mod.setDdyyed(String.valueOf(usedNum));
                            modYhjqDtos.add(yhjqDto_mod);
                            jqffjlDto_add.setYhjqid(yhjqDto_Sel.getYhjqid());
                            jqffjlDto_add.setYhid(yhjqDto_Sel.getYhid());
                            //如果查到的OA假期余额数据不为空：假期总额为查到的OA假期额度+发放时长，已用额度为查到的OA已用额度，剩余额度为接收到的参数+发放时长，
                            BigDecimal oasyed = new BigDecimal(yhjqDto_Sel.getSyed());
                            jqffjlDto_add.setJeze(String.valueOf(oajqze.add(ffed)));
                            jqffjlDto_add.setYyed(yhjqDto_Sel.getYyed());
                            jqffjlDto_add.setSyed(String.valueOf(oasyed.add(ffed)));
                        } else {
                            //如果不存在：
                            //新增用户假期表数据，假期总额=发放额度，剩余额度=发放额度，已用额度=0，有效期=假期发放设置的截止类型+截止日期。
                            YhjqDto yhjqDto_add = new YhjqDto();
                            yhjqDto_add.setYhjqid(StringUtil.generateUUID());
                            yhjqDto_add.setDw(jqffszDto.getDw());
                            yhjqDto_add.setYhid(jqffszDto.getYhid());
                            yhjqDto_add.setJqlx(jqffszDto.getJqlx());
                            yhjqDto_add.setJqze(String.valueOf(ffed));
                            yhjqDto_add.setYyed("0");
                            yhjqDto_add.setSyed(String.valueOf(ffed));
                            yhjqDto_add.setNd(dqnf);
                            yhjqDto_add.setYxq(DateUtils.format(calendar.getTime(), "yyyy-MM-dd"));
                            yhjqDto_add.setKssj(jqffszDto.getKssj());
                            yhjqDto_add.setDdze(String.valueOf(ffed));
                            yhjqDto_add.setDdyyed("0");
                            yhjqDto_add.setDdsyed(String.valueOf(ffed));
                            addYhjqDtos.add(yhjqDto_add);
                            jqffjlDto_add.setYhjqid(yhjqDto_add.getYhjqid());
                            jqffjlDto_add.setYhid(yhjqDto_add.getYhid());
                            //如果查到的OA假期余额数据为空：假期总额=发放时长，已用额度=0，剩余额度=发放时长，
                            jqffjlDto_add.setJeze(String.valueOf(ffed));
                            jqffjlDto_add.setYyed("0");
                            jqffjlDto_add.setSyed(String.valueOf(ffed));
                        }

                        jqffjlDto_add.setFfjlid(StringUtil.generateUUID());
                        jqffjlDto_add.setFffs("0");
                        jqffjlDto_add.setJqlx(jqffszDto.getJqlx());
                        jqffjlDto_add.setKssj(nowStr);
                        jqffjlDto_add.setYxq(DateUtils.format(calendar.getTime(), "yyyy-MM-dd"));
                        jqffjlDto_add.setNd(dqnf);
                        jqffjlDto_add.setDw(jqffszDto.getDw());
                        jqffjlDto_add.setSc(String.valueOf(ffed));
                        // 如果查到的钉钉假期额度数据为空：钉钉假期总额=发放时长，钉钉已用额度=0，钉钉剩余额度=发放时长。
                        jqffjlDto_add.setDdze(String.valueOf(quotaNum.add(ffed)));
                        jqffjlDto_add.setDdyyed("0");
                        jqffjlDto_add.setDdsyed(String.valueOf(quotaNum.subtract(usedNum).add(ffed)));
                        jqffjlDto_add.setLrry("admin");
                        //自动发放要存入发放设置id 避免重复发放
                        jqffjlDto_add.setFfszid(jqffszDto.getFfszid());
                        addJqffjlDtos.add(jqffjlDto_add);
                        //更新钉钉假期数据
                        OapiAttendanceVacationQuotaUpdateRequest.LeaveQuotas leaveQuotas = new OapiAttendanceVacationQuotaUpdateRequest.LeaveQuotas();
                        leaveQuotas.setLeaveCode(jqffszDto.getJqlxcskz2());
                        leaveQuotas.setQuotaCycle(dqnf);
                        leaveQuotas.setStartTime(DateUtils.parseDate("yyyy-MM-dd", nowStr).getTime());
                        leaveQuotas.setEndTime(calendar.getTime().getTime());
                        BigDecimal ddffed = ffed.multiply(new BigDecimal(100));
                        BigDecimal ddzed = quotaNum.multiply(new BigDecimal(100));
                        if ("0".equals(jqffszDto.getDw())) {
                            leaveQuotas.setQuotaNumPerHour(ddffed.add(ddzed).longValue());
                        } else {
                            leaveQuotas.setQuotaNumPerDay(ddffed.add(ddzed).longValue());
                        }
                        leaveQuotas.setReason("定时自动发放假期额度");
                        leaveQuotas.setUserid(jqffszDto.getDdid());
                        leaveQuotasList.add(leaveQuotas);
                    }else {
                        log.error("taskDistributeVacation-不满足发放条件(额度不足8小时),发放设置id为：{}",jqffszDto.getFfszid());
                    }
                } else {
                    log.error("taskDistributeVacation-不满足发放条件(发放限制),发放设置id为："+jqffszDto.getFfszid());
                }
            }
        }
        if (!CollectionUtils.isEmpty(addJqffjlDtos)){
            boolean isSuccess = dao.insertJqffjlDtos(addJqffjlDtos);
            if (!isSuccess){
                throw new BusinessException("msg","新增OA假期发放记录失败！");
            }
        }
        if (!CollectionUtils.isEmpty(addYhjqDtos)){
            boolean isSuccess = yhjqService.insertYhjqDtos(addYhjqDtos);
            if (!isSuccess){
                throw new BusinessException("msg","新增OA用户假期失败！");
            }
        }
        if (!CollectionUtils.isEmpty(modYhjqDtos)){
            boolean isSuccess = yhjqService.updateYhjqDtos(modYhjqDtos);
            if (!isSuccess){
                throw new BusinessException("msg","修改OA用户假期失败！");
            }
        }
        if (!CollectionUtils.isEmpty(modJqffszDtos)){
            boolean isSuccess = jqffszService.updateLjeds(modJqffszDtos);
            if (!isSuccess){
                throw new BusinessException("msg","修改假期发放设置累计额度失败！");
            }
        }
        //如果是当年最后一天 清空ljed(累计额度),nxzyed(年限转移额度)
        if (nowStr.equals(dqnf+"-12-31")){
            boolean isSuccess = jqffszService.updateToNull(new JqffszDto());
            if (!isSuccess){
                throw new BusinessException("msg","清空累计额度和年限转移额度失败！");
            }
        }
        if (!CollectionUtils.isEmpty(leaveQuotasList)){
            boolean isSuccess = dingTalkUtil.updateVacationQuota(leaveQuotasList, glyddid,token);
            if (!isSuccess){
                throw new BusinessException("msg","同步更新钉钉假期额度失败，请联系管理员！");
            }
        }
        if (!CollectionUtils.isEmpty(ycFfjl)){
            List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.VACATION_LEFT.getCode());
            if (!CollectionUtils.isEmpty(ddxxglDtolist)){
                String ICOMM_JQ00005 = xxglService.getMsg("ICOMM_JQ00005");
                String ICOMM_JQ00006 = xxglService.getMsg("ICOMM_JQ00006");
                if (StringUtil.isNotBlank(ICOMM_JQ00005)&&StringUtil.isNotBlank(ICOMM_JQ00006)){
                    String ycxx = ycFfjl.stream().map(e -> "</br>"+e.get("yhm") + e.get("zsxm")+",额度："+e.get("yced")).collect(Collectors.joining());
                    for (DdxxglDto ddxxglDto : ddxxglDtolist) {
                        dingTalkUtil.sendWorkMessage(ddxxglDto.getYhm(),
                                ddxxglDto.getYhid(), ICOMM_JQ00005, StringUtil.replaceMsg(ICOMM_JQ00006, ycxx));
                    }
                }else {
                    log.error("VACATION_LEFT-未配置钉钉消息ICOMM_JQ00005或ICOMM_JQ00006");
                }
            }
        }
        if (sffs){
            List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.VACATION_LEFT.getCode());
            if (!CollectionUtils.isEmpty(ddxxglDtolist)){
                String ICOMM_JQ00003 = xxglService.getMsg("ICOMM_JQ00003");
                String ICOMM_JQ00004 = xxglService.getMsg("ICOMM_JQ00004");
                if (StringUtil.isNotBlank(ICOMM_JQ00003)&&StringUtil.isNotBlank(ICOMM_JQ00004)){
                    for (DdxxglDto ddxxglDto : ddxxglDtolist) {
                        // 内网访问
                        String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/vacation/diffentWithDDAndOA/diffentWithDDAndOA?urlPrefix="+urlPrefix+"&wbfw=1", StandardCharsets.UTF_8);
                        //外网访问
                        List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                        OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                        btnJsonList.setTitle("查看");
                        btnJsonList.setActionUrl(internalbtn);
                        btnJsonLists.add(btnJsonList);
                        dingTalkUtil.sendCardMessage(ddxxglDto.getYhm(),ddxxglDto.getDdid(),ICOMM_JQ00003,ICOMM_JQ00004,
                                btnJsonLists,"1");
                    }
                }else {
                    log.error("VACATION_LEFT-未配置钉钉消息ICOMM_JQ00003或ICOMM_JQ00004");
                }
            }
        }
    }
}

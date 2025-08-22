package com.matridx.igams.hrm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.google.common.collect.Lists;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.enums.ApproveStatusEnum;
import com.matridx.igams.common.enums.AttendanceEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IUserService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.HolidayUtil;
import com.matridx.igams.common.util.HolidayVo;
import com.matridx.igams.hrm.dao.entities.BcglDto;
import com.matridx.igams.hrm.dao.entities.KqqjxxDto;
import com.matridx.igams.hrm.dao.entities.YghmcDto;
import com.matridx.igams.hrm.dao.entities.YhkqxxDto;
import com.matridx.igams.hrm.dao.entities.YhkqxxModel;
import com.matridx.igams.hrm.dao.post.IYhkqxxDao;
import com.matridx.igams.hrm.service.svcinterface.IBcglService;
import com.matridx.igams.hrm.service.svcinterface.IKqqjxxService;
import com.matridx.igams.hrm.service.svcinterface.IYghmcService;
import com.dingtalk.api.response.OapiAttendanceGetcolumnvalResponse.ColumnValForTopVo;
import com.dingtalk.api.response.OapiAttendanceGetcolumnvalResponse.ColumnDayAndVal;
import com.matridx.igams.hrm.service.svcinterface.IYhcqtzService;
import com.matridx.igams.hrm.service.svcinterface.IYhkqxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.taobao.api.ApiException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class YhkqxxServiceImpl extends BaseBasicServiceImpl<YhkqxxDto, YhkqxxModel, IYhkqxxDao> implements IYhkqxxService {
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IYhcqtzService yhcqtzService;
    @Autowired
    IUserService userService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IKqqjxxService kqqjxxService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    DingTalkUtil dingTalkUtil;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IXtszService xtszService;
    @Autowired
    IYghmcService yghmcService;
    @Autowired
    IBcglService bcglService;
    @Autowired
    HolidayUtil holidayUtil;
    private final Logger logger = LoggerFactory.getLogger(YhkqxxServiceImpl.class);
    /**
     * 获取打卡情况以及保存
     */
    public void updateAttendance(Map<String,String> map) throws BusinessException {
        //flg为0，更新四天前的考勤信息
        //flg为1，更新上个月的考勤信息
        //flg为2，更新本月的考勤信息
        String flg = map.get("flg");
        String wbcxid = map.get("wbcxid");
        if (StringUtil.isBlank(wbcxid)){
            throw new BusinessException("msg","请配置外部程序id");
        }
        getAttendanceRecords(flg,wbcxid);
    }

    /**
     * 获取考勤情况以及保存
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void getAttendanceRecords(String flg,String wbcxid) {
        List<BcglDto> bcglDtos = bcglService.getDtoList(new BcglDto());
        List<YhkqxxDto> allYhkqxxDtos = new ArrayList<>();
        String token = dingTalkUtil.getToken(wbcxid);
        UserDto xtyhDto_y = new UserDto();
        xtyhDto_y.setWbcxid(wbcxid);
        List<UserDto> xtyhDtos = userService.getAllUserList(xtyhDto_y);//查找的是不包括删除标记为1的所有数据
        //只取在职的
        List<UserDto> zzXtyhDtos = xtyhDtos.stream().filter((e) -> !"1".equals(e.getSfsd())).collect(Collectors.toList());
        List<List<UserDto>> partition = Lists.partition(zzXtyhDtos, 50);
        for (List<UserDto> dtos : partition) {
            List<YhkqxxDto> yhkqxxDtos = new ArrayList<>();
            List<String> ids = dtos.stream().map(UserDto::getDdid).filter(StringUtil::isNotBlank).collect(Collectors.toList());
            //只支持查询7天数据
            switch (flg) {
                case "0": {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, -3);
                    String startdate = sdf.format(calendar.getTime());
                    calendar.add(Calendar.DATE, +1);
                    String enddate = sdf.format(calendar.getTime());
                    allYhkqxxDtos.addAll(getDingAttendence(ids, startdate, enddate, token, xtyhDtos, yhkqxxDtos, bcglDtos));
                    break;
                }
                case "1": {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                    Calendar calendar = Calendar.getInstance();
                    String startdate;
                    String enddate = null;
                    for (int time = 0; time < 5; time++) {
                        if (time == 0) {
                            calendar.add(Calendar.MONTH, -1);
                            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                            startdate = sdf.format(calendar.getTime());
                        } else {
                            startdate = enddate;
                        }
                        calendar.add(Calendar.DATE, +7);
                        enddate = sdf.format(calendar.getTime());
                        allYhkqxxDtos.addAll(getDingAttendence(ids, startdate, enddate, token, xtyhDtos, yhkqxxDtos, bcglDtos));
                    }
                    break;
                }
                case "2": {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                    Calendar calendar = Calendar.getInstance();
                    String startdate;
                    String enddate = null;
                    for (int time = 0; time < 5; time++) {
                        if (time == 0) {
                            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                            startdate = sdf.format(calendar.getTime());
                        } else {
                            startdate = enddate;
                        }
                        calendar.add(Calendar.DATE, +7);
                        enddate = sdf.format(calendar.getTime());
                        allYhkqxxDtos.addAll(getDingAttendence(ids, startdate, enddate, token, xtyhDtos, yhkqxxDtos, bcglDtos));
                    }
                    break;
                }
            }
        }
        //离职人员考勤数据(离职离今日30天人员)
        YghmcDto yghmcDto = new YghmcDto();
        yghmcDto.setWbcxid(wbcxid);
        Calendar instance = Calendar.getInstance();
        yghmcDto.setLzrqend(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
        instance.add(Calendar.DATE,-30);
        yghmcDto.setLzrqstart(DateUtils.getCustomFomratCurrentDate(instance.getTime(),"yyyy-MM-dd"));
        List<YghmcDto> yghmcDtos = yghmcService.getLzryByRq(yghmcDto);
        if (!CollectionUtils.isEmpty(yghmcDtos)) {
            Iterator<YghmcDto> iterator = yghmcDtos.iterator();
            while (iterator.hasNext()) {
                YghmcDto next = iterator.next();
                //去除重复人员
                for (UserDto xtyhDto : zzXtyhDtos) {
                    if (xtyhDto.getDdid().equals(next.getDdid())) {
                        iterator.remove();
                    }
                }
            }
            if (!CollectionUtils.isEmpty(yghmcDtos)) {
                List<List<YghmcDto>> lzpartition = Lists.partition(yghmcDtos, 50);
                for (List<YghmcDto> yghmcDtoList : lzpartition) {
                    List<YhkqxxDto> yhkqxxDtos = new ArrayList<>();
                    List<String> ids = yghmcDtoList.stream().map(YghmcDto::getDdid).filter(StringUtil::isNotBlank).collect(Collectors.toList());
                    logger.error("getAttendanceRecords-------离职ddids="+JSON.toJSONString(ids));
                    switch (flg) {
                        case "0": {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.DATE, -3);
                            String startdate = sdf.format(calendar.getTime());
                            calendar.add(Calendar.DATE, +1);
                            String enddate = sdf.format(calendar.getTime());
                            allYhkqxxDtos.addAll(getDingAttendence(ids, startdate, enddate, token, xtyhDtos, yhkqxxDtos, bcglDtos));
                            break;
                        }
                        case "1": {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                            Calendar calendar = Calendar.getInstance();
                            String startdate;
                            String enddate = null;
                            for (int time = 0; time < 5; time++) {
                                if (time == 0) {
                                    calendar.add(Calendar.MONTH, -1);
                                    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                                    startdate = sdf.format(calendar.getTime());
                                } else {
                                    startdate = enddate;
                                }
                                calendar.add(Calendar.DATE, +7);
                                enddate = sdf.format(calendar.getTime());
                                allYhkqxxDtos.addAll(getDingAttendence(ids, startdate, enddate, token, xtyhDtos, yhkqxxDtos, bcglDtos));
                            }
                            break;
                        }
                        case "2": {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                            Calendar calendar = Calendar.getInstance();
                            String startdate;
                            String enddate = null;
                            for (int time = 0; time < 5; time++) {
                                if (time == 0) {
                                    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                                    startdate = sdf.format(calendar.getTime());
                                } else {
                                    startdate = enddate;
                                }
                                calendar.add(Calendar.DATE, +7);
                                enddate = sdf.format(calendar.getTime());
                                allYhkqxxDtos.addAll(getDingAttendence(ids, startdate, enddate, token, xtyhDtos, yhkqxxDtos, bcglDtos));
                            }
                            break;
                        }
                    }
                }
            }
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Calendar qjcale = Calendar.getInstance();
        KqqjxxDto kqqjxxDto = new KqqjxxDto();
        kqqjxxDto.setRqend(DateUtils.getCustomFomratCurrentDate(qjcale.getTime(),"yyyy-MM-dd"));
        if ("0".equals(flg)) {
            //往前推四天
            qjcale.add(Calendar.DATE,-4);
            kqqjxxDto.setRqstart(DateUtils.getCustomFomratCurrentDate(qjcale.getTime(),"yyyy-MM-dd"));
        }else if ("1".equals(flg)){
            //上月一号到现在
            qjcale.add(Calendar.MONTH, -1);
            qjcale.set(Calendar.DAY_OF_MONTH, qjcale.getActualMinimum(Calendar.DAY_OF_MONTH));
            kqqjxxDto.setRqstart(DateUtils.getCustomFomratCurrentDate(qjcale.getTime(),"yyyy-MM-dd"));
        }else if ("2".equals(flg)){
            //本月一号到现在
            qjcale.set(Calendar.DAY_OF_MONTH, qjcale.getActualMinimum(Calendar.DAY_OF_MONTH));
            kqqjxxDto.setRqstart(DateUtils.getCustomFomratCurrentDate(qjcale.getTime(),"yyyy-MM-dd"));
        }
        //将list转为map 减少遍历
        Map<String, String> kqqjxxDtosMap = new HashMap<>();
        List<KqqjxxDto> kqqjxxDtos = kqqjxxService.getDtoListForKsAndJs(kqqjxxDto);
        if (!CollectionUtils.isEmpty(kqqjxxDtos)){
            kqqjxxDtosMap = kqqjxxDtos.stream().collect(Collectors.toMap(KqqjxxDto::getYhrq, KqqjxxDto::getQjzsc, (key1, key2) -> key2));
        }
        //将list转为map 减少遍历
        Map<String, BcglDto> bcglDtosMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(bcglDtos)){
            bcglDtosMap = bcglDtos.stream().collect(Collectors.toMap(BcglDto::getBcid, Function.identity(), (key1, key2) -> key2));
        }
        for (YhkqxxDto allYhkqxxDto : allYhkqxxDtos) {
            //获取补贴信息
            getSubsidies(df, kqqjxxDtosMap, bcglDtosMap, allYhkqxxDto);
        }
        //新增或修改
        if (!CollectionUtils.isEmpty(allYhkqxxDtos)){
            List<List<YhkqxxDto>> partition_add = Lists.partition(allYhkqxxDtos, 500);
            for (List<YhkqxxDto> yhkqxxDtos : partition_add) {
                dao.insertOrUpdateYhRecords(yhkqxxDtos);
            }
        }
    }
    //获取补贴
    private void getSubsidies(DateTimeFormatter df, Map<String, String> kqqjxxDtosMap, Map<String, BcglDto> bcglDtosMap, YhkqxxDto allYhkqxxDto) {
        String qjzsc = null;
        if (!MapUtils.isEmpty(kqqjxxDtosMap)){
            qjzsc = kqqjxxDtosMap.get(allYhkqxxDto.getYhid() + allYhkqxxDto.getRq());
        }
        if (StringUtil.isNotBlank(allYhkqxxDto.getCqsj())&&StringUtil.isNotBlank(allYhkqxxDto.getTqsj())){
            LocalDateTime tqsj = LocalDateTime.parse(allYhkqxxDto.getTqsj(), df);
            LocalDateTime cqsj = LocalDateTime.parse(allYhkqxxDto.getCqsj(), df);
            long rqc = ChronoUnit.MINUTES.between(cqsj, tqsj);//分钟
            //计算考勤工作时长
            BigDecimal kqgzscDecimal = new BigDecimal(rqc).divide(new BigDecimal(60), 1, RoundingMode.HALF_UP);//小时保留一位小数
            if (StringUtil.isNotBlank(qjzsc)){
                kqgzscDecimal = kqgzscDecimal.subtract(new BigDecimal(qjzsc)).setScale(1, RoundingMode.HALF_UP);
            }
            allYhkqxxDto.setGzsc(String.valueOf(kqgzscDecimal));
        }

        //计算补贴工作时长及补贴金额
        if (!MapUtils.isEmpty(bcglDtosMap)&&StringUtil.isNotBlank(allYhkqxxDto.getTqsj())&&StringUtil.isNotBlank(allYhkqxxDto.getBcid())){
            BcglDto bcglDto = bcglDtosMap.get(allYhkqxxDto.getBcid());
            if (bcglDto!=null&&StringUtil.isNotBlank(bcglDto.getBtglid())){
                String rq = allYhkqxxDto.getRq();
                Calendar instance = Calendar.getInstance();
                try {
                    instance.setTime(DateUtils.parse(rq));
                    instance.add(Calendar.DATE,1);
                } catch (Exception e) {
                    logger.error("getAttendanceRecords日期转换失败！！"+JSON.toJSONString(allYhkqxxDto));
                }
                //次日
                String cr = DateUtils.getCustomFomratCurrentDate(instance.getTime(), "yyyy-MM-dd");
                //保证以下日期格式为 yyyy-MM-dd HH:mm:ss 否则会报错
                allYhkqxxDto.setBcglid(bcglDto.getBcglid());
                allYhkqxxDto.setBtglid(bcglDto.getBtglid());
                LocalDateTime tqsj = LocalDateTime.parse(allYhkqxxDto.getTqsj(), df);
                LocalDateTime bccqsj = LocalDateTime.parse(rq+" "+bcglDto.getSbdksj(), df);
                long rqc = ChronoUnit.MINUTES.between(bccqsj, tqsj);//分钟
                BigDecimal bcgzsc = new BigDecimal(rqc).divide(new BigDecimal(60), 1, RoundingMode.HALF_UP);//小时保留一位小数
                //减去请假时长
                if (StringUtil.isNotBlank(qjzsc)){
                    bcgzsc = bcgzsc.subtract(new BigDecimal(qjzsc)).setScale(1, RoundingMode.HALF_UP);
                }
                int flag = bcgzsc.compareTo(new BigDecimal(bcglDto.getBzsc()));
                LocalDateTime bzsj;
                LocalDateTime dzkssj;
                //1：今日 0：次日
                //标准时间
                if ("0".equals(bcglDto.getBzjrcr())){
                    bzsj = LocalDateTime.parse(cr+" "+bcglDto.getBzsj(), df);//标准时间 次日
                }else {
                    bzsj = LocalDateTime.parse(rq+" "+bcglDto.getBzsj(), df);//标准时间 今日
                }
                //递增开始时间
                if ("0".equals(bcglDto.getDzjrcr())){
                    dzkssj = LocalDateTime.parse(cr+" "+bcglDto.getDzkssj(), df);//递增开始时间 次日
                }else {
                    dzkssj = LocalDateTime.parse(rq+" "+bcglDto.getDzkssj(), df);//递增开始时间 今日
                }
                //工作时长大于或等于标准时长
                if (flag>=0){
                    int tq_bz = tqsj.compareTo(bzsj);
                    //判断下班打卡时间是否大于标准时间
                    if (tq_bz>=0){
                        //判断下班打卡时间是否大于递增开始时间
                        int tq_dz = tqsj.compareTo(dzkssj);
                        if (tq_dz>0){
                            long tq_dz_sc = ChronoUnit.MINUTES.between(dzkssj, tqsj);//分钟
                            //向下取整 3.5 为 3
                            BigDecimal dzsc = new BigDecimal(tq_dz_sc).divide(new BigDecimal(60), 0, RoundingMode.DOWN);//小时
                            // 如果递增时长为0，补贴金额为标准金额。
                            if (dzsc.compareTo(new BigDecimal("0"))==0){
                                allYhkqxxDto.setBtje(bcglDto.getBzje());
                            }else {
                                // 计算递增金额，递增金额=递增时长/递增间隔(取整数)*递增金额。  递增时长/递增间隔 向下取整
                                BigDecimal dzje = dzsc.divide(new BigDecimal(bcglDto.getDzjg()), 0, RoundingMode.DOWN).multiply(new BigDecimal(bcglDto.getDzje()));
                                // 计算补贴总金额，递增金额+标准金额。
                                BigDecimal btzje = dzje.add(new BigDecimal(bcglDto.getBzje()));
                                // 判断补贴总金额是否大于封顶金额，如果大于，补贴金额=封顶金额
                                int sffd = btzje.compareTo(new BigDecimal(bcglDto.getFdje()));
                                //判断补贴总金额是否大于封顶金额，如果大于，补贴金额=封顶金额
                                if (sffd>0){
                                    allYhkqxxDto.setBtje(bcglDto.getFdje());
                                }else {
                                    allYhkqxxDto.setBtje(String.valueOf(btzje));
                                }
                            }
                        }else {
                            // 如果小于等于，补贴金额为标准金额
                            allYhkqxxDto.setBtje(bcglDto.getBzje());
                        }
                    }else {
                        //如果小于，没有补贴金额
                        allYhkqxxDto.setBtje("0");
                    }
                //工作时长小于标准时长
                }else{
                    allYhkqxxDto.setBtje("0");
                }

            }else {
                logger.error("getAttendanceRecords没有对应补贴信息或信息错误--班次id为"+ allYhkqxxDto.getBcid()+"---bcglDto="+JSON.toJSONString(bcglDto));
            }
        }
    }

    public List<YhkqxxDto> getDingAttendence(List<String> ids,String startdate,String enddate,String token,List<UserDto> xtyhDtos,List<YhkqxxDto> yhkqxxDtos,List<BcglDto> bcglDtos){
        List<YhkqxxDto> thislist = new ArrayList<>();
        Map<String, Object> checkinMap = talkUtil.checkinListRecord(token,ids,startdate,enddate);
        OapiAttendanceListRecordResponse rsp = (OapiAttendanceListRecordResponse) checkinMap.get("list");
        List<OapiAttendanceListRecordResponse.Recordresult> recordresult = rsp.getRecordresult();
        if (!CollectionUtils.isEmpty(recordresult)) {
            for (OapiAttendanceListRecordResponse.Recordresult recordresult_t : recordresult) {
                YhkqxxDto yhkqxxDto = new YhkqxxDto();
                if (recordresult_t.getClassId()!=null){
                    yhkqxxDto.setBcid(String.valueOf(recordresult_t.getClassId()));
                }
                //审批实例关联id
                yhkqxxDto.setProcInstId(recordresult_t.getProcInstId());
                yhkqxxDto.setKqid(StringUtil.generateUUID());
                for (UserDto xtyhDto :  xtyhDtos) {
                    if (recordresult_t.getUserId().equals(xtyhDto.getDdid())){
                        yhkqxxDto.setYhid(xtyhDto.getYhid());
                        yhkqxxDto.setWbcxid(xtyhDto.getWbcxid());
                        break;
                    }
                }
                if (StringUtil.isBlank(yhkqxxDto.getYhid())){
                    yhkqxxDto.setYhid("");
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat simpleDateFormat_t = new SimpleDateFormat("yyyy-MM-dd");
                if (recordresult_t.getTimeResult() != null) {
                    yhkqxxDto.setCqzt(this.getCqzt(recordresult_t.getTimeResult()));
                }
                if (recordresult_t.getCheckType() != null) {
                    if (recordresult_t.getCheckType().equals("OnDuty")) {
                        yhkqxxDto.setCqsj(simpleDateFormat.format(recordresult_t.getUserCheckTime()));
                        yhkqxxDto.setRq(simpleDateFormat_t.format(recordresult_t.getWorkDate()));
                    } else if (recordresult_t.getCheckType().equals("OffDuty")) {
                        yhkqxxDto.setTqsj(simpleDateFormat.format(recordresult_t.getUserCheckTime()));
                        yhkqxxDto.setRq(simpleDateFormat_t.format(recordresult_t.getWorkDate()));
                    }
                } else {
                    continue;
                }
                boolean isHave = false;
                for (YhkqxxDto dto : yhkqxxDtos) {
                    //如果有该天数据
                    if (yhkqxxDto.getRq().equals(dto.getRq())&&yhkqxxDto.getYhid().equals(dto.getYhid())){
                        if (recordresult_t.getCheckType().equals("OnDuty")) {
                            dto.setCqsj(yhkqxxDto.getCqsj());
                        }else if (recordresult_t.getCheckType().equals("OffDuty")) {
                            dto.setTqsj(yhkqxxDto.getTqsj());
                        }
                        isHave = true;
                    }
                }
                //没有则加入
                if (!isHave){
                    thislist.add(yhkqxxDto);
                    //判断用
                    yhkqxxDtos.add(yhkqxxDto);
                }
            }
        }
        for (YhkqxxDto yhkqxxDto : thislist) {
            //未排班 有下班打卡 审批实例id不为空 确定为加班审批或出差加班
            if (StringUtil.isBlank(yhkqxxDto.getBcid())&&StringUtil.isNotBlank(yhkqxxDto.getTqsj())&&StringUtil.isNotBlank(yhkqxxDto.getProcInstId())){
                long tqsj = DateUtils.parseDate("HH:mm:ss", yhkqxxDto.getTqsj().substring(10, 19)).getTime();
                long ere = DateUtils.parseDate("HH:mm:ss", "22:00:00").getTime();
                long bdb = DateUtils.parseDate("HH:mm:ss", "12:00:00").getTime();
                //退勤时间在 晚八点后才可能有补贴（或者12点之前 凌晨下班，不考虑12点以后下班（默认为异常数据）） 以此约束钉钉接口调用次数
                if (tqsj>=ere||tqsj<=bdb) {
                    GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult approveInfo = dingTalkUtil.getApproveInfo(token, yhkqxxDto.getProcInstId());
                    //审批完成的
                    if (ApproveStatusEnum.COMPLETED.code.equals(approveInfo.getStatus()) && "agree".equals(approveInfo.getResult())) {
                        //获取表单的值
                        List<GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues> formComponentValues = approveInfo.getFormComponentValues();
                        for (GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues formComponentValue : formComponentValues) {
                            if (StringUtil.isNotBlank(formComponentValue.getName()))
                                switch (formComponentValue.getName()) {
                                    case "开始时间":
                                        yhkqxxDto.setJbkssj(formComponentValue.getValue());
                                        break;
                                    case "结束时间":
                                        yhkqxxDto.setJbjssj(formComponentValue.getValue());
                                        break;
                                    case "班次":
                                        yhkqxxDto.setBcmc(StringUtil.isNotBlank(formComponentValue.getValue()) ? formComponentValue.getValue().split("-")[0] : null);
                                        break;
                                }
                        }
                        if (StringUtil.isNotBlank(yhkqxxDto.getBcmc())) {
                            for (BcglDto bcglDto : bcglDtos) {
                                if (yhkqxxDto.getBcmc().equals(bcglDto.getBcmc())) {
                                    yhkqxxDto.setBcid(bcglDto.getBcid());
                                    break;
                                }
                            }
                        }
                        //往来数据或没按规则填写班次数据
                        if (StringUtil.isBlank(yhkqxxDto.getBcmc()) && StringUtil.isNotBlank(yhkqxxDto.getCqsj())) {
                            //通过打卡时间 就近原则获取班次信息
                            BcglDto bcglDto = bcglService.getBcByCqsj(yhkqxxDto.getCqsj().substring(10, 19));
                            if (bcglDto != null) {
                                yhkqxxDto.setBcid(bcglDto.getBcid());
                                yhkqxxDto.setBcmc(bcglDto.getBcmc());
                            }
                        }
                    }
                }
            }
        }
        return thislist;
    }
    //更新考勤按钮
    // public boolean updateCheckSave(XtyhDto xtyhDto_t){
    //     try {
           /* if(xtyhDto_t.getYhid()!=null){
                List<String> ids = new ArrayList<>();
                ids.add(xtyhDto_t.getDdid());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                String sdate = xtyhDto_t.getStartdate();
                String edate = xtyhDto_t.getEnddate();
                Date starttime = sdf.parse(sdate);
                Date endtime = sdf.parse(edate);
                calendar.setTime(endtime);
                int endday = calendar.get(Calendar.DAY_OF_YEAR);//一年中的第几天
                calendar.setTime(starttime);
                int startday = calendar.get(Calendar.DAY_OF_YEAR);//一年中的第几天
                int day = endday - startday;
                int sum = 0;
                if (day % 7 != 0) {
                    sum = day / 7;
                } else {
                    sum = day / 7;
                    sum += 1;
                }
                sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                String startdate = sdf.format(starttime);
                String enddate = sdf.format(endtime);
                String token = dingTalkUtil.getDingTokenByUserId(xtyhDto_t.getYhm());
                List<YhkqxxDto> allYhkqxxDtos = new ArrayList<>();
                for (int time = 0; time < sum; time++) {
                    if (time == 0) {
                        calendar.add(Calendar.DATE, +7);
                        enddate = sdf.format(calendar.getTime());
                    } else {
                        startdate = enddate;
                        calendar.add(Calendar.DATE, +7);
                        enddate = sdf.format(calendar.getTime());
                    }
                    allYhkqxxDtos.addAll(getDingAttendence(ids, startdate, enddate, token, new ArrayList<>(),new ArrayList<>(),new ArrayList<>()));
                }
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                for (YhkqxxDto allYhkqxxDto : allYhkqxxDtos) {
                    if (StringUtil.isNotBlank(allYhkqxxDto.getCqsj())&&StringUtil.isNotBlank(allYhkqxxDto.getTqsj())){
                        LocalDate tqsj = LocalDate.parse(allYhkqxxDto.getTqsj(), df);
                        LocalDate cqsj = LocalDate.parse(allYhkqxxDto.getCqsj(), df);
                        long rqc = ChronoUnit.HOURS.between(cqsj, tqsj);
                        allYhkqxxDto.setGzsc(String.valueOf(rqc));
                    }
                    allYhkqxxDto.setYhid(xtyhDto_t.getYhid());
                    allYhkqxxDto.setWbcxid(xtyhDto_t.getWbcxid());
                }
                //新增或修改
                if (!CollectionUtils.isEmpty(allYhkqxxDtos)){
                    List<List<YhkqxxDto>> partition = Lists.partition(allYhkqxxDtos, 500);
                    for (List<YhkqxxDto> yhkqxxDtos : partition) {
                        dao.insertOrUpdateYhRecords(yhkqxxDtos);
                    }
                }
            }*/

            //不开放 更新所有用户考勤信息
            // else{
            //     List<XtyhDto> yhlist = xtyhService.getDtoList();
            //     int i;
            //     int count;
            //     if (yhlist.size() % 50 == 0) {
            //         count = yhlist.size() / 50;
            //     } else {
            //         count = yhlist.size() / 50;
            //         count += 1;
            //     }
            //     for (int j = 0; j < count; j++) {
            //         List<String> ids = new ArrayList<>();
            //         if (j == count - 1) {
            //             for (i = j * 50; i < yhlist.size(); i++) {
            //                 String ddid = yhlist.get(i).getDdid();
            //                 ids.add(ddid);
            //             }
            //         } else {
            //             for (i = j * 50; i < (j + 1) * 50; i++) {
            //                 String ddid = yhlist.get(i).getDdid();
            //                 ids.add(ddid);
            //             }
            //         }
            //         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //         Calendar calendar = Calendar.getInstance();
            //         String sdate = xtyhDto_t.getStartdate();
            //         String edate = xtyhDto_t.getEnddate();
            //         Date starttime = sdf.parse(sdate);
            //         Date endtime = sdf.parse(edate);
            //         calendar.setTime(endtime);
            //         int endday = calendar.get(Calendar.DAY_OF_YEAR);//一年中的第几天
            //         calendar.setTime(starttime);
            //         int startday = calendar.get(Calendar.DAY_OF_YEAR);//一年中的第几天
            //         int day = endday - startday;
            //         int sum = 0;
            //         if (day % 7 != 0) {
            //             sum = day / 7;
            //         } else {
            //             sum = day / 7;
            //             sum += 1;
            //         }
            //         sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            //         String startdate = sdf.format(starttime);
            //         String enddate = sdf.format(endtime);
            //         for (int time = 0; time < sum; time++) {
            //             if (time == 0) {
            //                 calendar.add(Calendar.DATE, +7);
            //                 enddate = sdf.format(calendar.getTime());
            //             } else {
            //                 startdate = enddate;
            //                 calendar.add(Calendar.DATE, +7);
            //                 enddate = sdf.format(calendar.getTime());
            //             }
            //             getDingAttendence(ids,startdate,enddate,allUserList);
            //         }
            //     }
            // }
    //     }catch (Exception e){
    //         // TODO Auto-generated catch block
    //         return false;
    //     }
    //     return true;
    // }

    /**
     * 定时更新请假信息
     */
    public void updateLeaveStatus(Map<String,String> map) throws BusinessException {
        //flg为0，更新四天前的考勤信息
        //flg为1，更新上个月的考勤信息
        //flg为2，更新本月的考勤信息
        String flg = map.get("flg");
        String wbcxid = map.get("wbcxid");
        if (StringUtil.isBlank(wbcxid)){
            throw new BusinessException("msg","请配置外部程序id");
        }
        updateTimingLeaveStatus(flg,wbcxid);
    }
    /**
     * 定时更新请假信息
     */
    public void updateTimingLeaveStatus(String flg,String wbcxid) throws BusinessException {
        switch (flg) {
            case "1": {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                long startsj = calendar.getTime().getTime();
                calendar.add(Calendar.DATE, +30);
                long endsj = calendar.getTime().getTime();
                getLeaveStatus(startsj, endsj, wbcxid);
                break;
            }
            case "2": {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                long startsj = calendar.getTime().getTime();
                calendar.add(Calendar.DATE, +30);
                long endsj = calendar.getTime().getTime();
                getLeaveStatus(startsj, endsj, wbcxid);
                break;
            }
            case "0": {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -3);
                long startsj = calendar.getTime().getTime();
                calendar.add(Calendar.DATE, +1);
                long endsj = calendar.getTime().getTime();
                getLeaveStatus(startsj, endsj, wbcxid);
                break;
            }
        }
    }


    public void getLeaveStatus(Long startsj,Long endsj,String wbcxid) throws BusinessException {
        JcsjDto jcsjDto_dd = new JcsjDto();
        jcsjDto_dd.setCsdm("HOLIDAY");
        jcsjDto_dd.setJclb("DINGTALK_AUDTI_CALLBACK_TYPE");
        jcsjDto_dd = jcsjService.getDtoByCsdmAndJclb(jcsjDto_dd);
        if(StringUtil.isBlank(jcsjDto_dd.getCsmc()) || StringUtil.isBlank(jcsjDto_dd.getCskz1())) {
            throw new BusinessException("msg","请设置假期回调的钉钉审批回调类型基础数据！");
        }
        String token = talkUtil.getToken(wbcxid);
        String processCode;
        Long nextCursor = 0L;
        List<String> list;
        processCode = jcsjDto_dd.getCskz1();
        Date date = new Date();
        date.setTime(startsj);
        String rqstart = DateUtils.getCustomFomratCurrentDate(date, "yyyy-MM-dd");
        date.setTime(endsj);
        String rqend = DateUtils.getCustomFomratCurrentDate(date, "yyyy-MM-dd");
        KqqjxxDto kqqjxxDto_sel = new KqqjxxDto();
        kqqjxxDto_sel.setRqstart(rqstart);
        kqqjxxDto_sel.setRqend(rqend);
        List<KqqjxxDto> allKqqjxxDtos = kqqjxxService.getDtoList(kqqjxxDto_sel);
        int spdsl = 0;
        while (nextCursor != null) {
            OapiProcessinstanceListidsResponse rsp_lb = getApprovalInstance(processCode, startsj, endsj, token, nextCursor);
            list = rsp_lb.getResult().getList();
            if (rsp_lb.getResult().getNextCursor() != null) {
                nextCursor = rsp_lb.getResult().getNextCursor();
            } else {
                nextCursor = null;
            }
            spdsl = spdsl + (list!=null?list.size():0);
            logger.error("--------------------------审批单数量" + spdsl);
            if (!CollectionUtils.isEmpty(allKqqjxxDtos) && !CollectionUtils.isEmpty(list)) {
                for (KqqjxxDto allKqqjxxDto : allKqqjxxDtos) {
                    //如果数据库有该审批信息不再去查，节省接口调用次数
                    list.removeIf(next -> next.equals(allKqqjxxDto.getSpid()));
                }
            }
            if (!CollectionUtils.isEmpty(list)){
                for (String s : list) {
                    DingTalkClient client_t = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/get");
                    OapiProcessinstanceGetRequest req_t = new OapiProcessinstanceGetRequest();
                    req_t.setProcessInstanceId(s);
                    OapiProcessinstanceGetResponse rsp_t = null;
                    try {
                        rsp_t = client_t.execute(req_t, token);
                    } catch (ApiException e) {
                        logger.error(e.getMessage());
                    }
                    if (rsp_t==null){
                        logger.error("getLeaveStatus--获取请假审批单信息失败！+{}",s);
                        continue;
                    }
                    OapiProcessinstanceGetResponse.ProcessInstanceTopVo processInstance = rsp_t.getProcessInstance();
                    String status = processInstance.getStatus();
                    String result = processInstance.getResult();
                    if ("COMPLETED".equals(status) && "agree".equals(result)) {
                        KqqjxxDto kqqjxxDto = new KqqjxxDto();
                        kqqjxxDto.setSpid(s);
                        String body = rsp_t.getBody();
                        JSONObject jsonObject = JSON.parseObject(body);
                        JSONObject obj = jsonObject.getJSONObject("process_instance");
                        UserDto xtyhDto = new UserDto();
                        xtyhDto.setDdid(obj.getString("originator_userid"));
                        UserDto xtyhDto1 = userService.getYhByDdid(xtyhDto);
                        if (xtyhDto1 != null) {
                            kqqjxxDto.setYhid(xtyhDto1.getYhid());
                        } else {
                            logger.error("getLeaveStatus--获取yhid,ddid=" + xtyhDto.getDdid() + "--spid=" + s);
                            continue;
                        }
                        JSONArray jsonArray = obj.getJSONArray("form_component_values");
                        String value = jsonArray.getJSONObject(0).getString("value");
                        JSONArray jsonArray_t = JSON.parseArray(value);
                        if (jsonArray_t.getString(0).contains("下午") || jsonArray_t.getString(0).contains("上午")
                        ) {
                            if (jsonArray_t.getString(0).contains("上午")) {
                                kqqjxxDto.setQjkssj(jsonArray_t.getString(0).substring(0, 11) + "08:30:00");
                                kqqjxxDto.setRq(jsonArray_t.getString(0).substring(0, 10));
                            } else if (jsonArray_t.getString(0).contains("下午")) {
                                kqqjxxDto.setQjkssj(jsonArray_t.getString(0).substring(0, 11) + "13:00:00");
                                kqqjxxDto.setRq(jsonArray_t.getString(0).substring(0, 10));
                            }
                            if (jsonArray_t.getString(1).contains("上午")) {
                                kqqjxxDto.setQjjssj(jsonArray_t.getString(1).substring(0, 11) + "12:00:00");
                            } else if (jsonArray_t.getString(1).contains("下午")) {
                                kqqjxxDto.setQjjssj(jsonArray_t.getString(1).substring(0, 11) + "17:30:00");
                            }
                        } else {
                            kqqjxxDto.setQjkssj(jsonArray_t.getString(0));
                            kqqjxxDto.setQjjssj(jsonArray_t.getString(1));
                            kqqjxxDto.setRq(new SimpleDateFormat("yyyy-MM-dd").format(jsonArray_t.getDate(0)));
                        }
                        if ("hour".equals(jsonArray_t.getString(3))) {
                            kqqjxxDto.setQjsc(jsonArray_t.getString(2) + "小时");
                        } else if ("day".equals(jsonArray_t.getString(3))) {
                            kqqjxxDto.setQjsc(jsonArray_t.getString(2) + "天");
                        } else if ("halfDay".equals(jsonArray_t.getString(3))) {
                            double d = jsonArray_t.getDouble(2) * 8;
                            kqqjxxDto.setQjsc(d + "小时");
                        }
                        kqqjxxDto.setQjlx(this.getQjlx(jsonArray_t.getString(4)));
                        //避免审批单日期超出导致报错
                        KqqjxxDto qjxxdto = kqqjxxService.getSpid(kqqjxxDto);
                        if (qjxxdto == null) {
                            YhkqxxDto yhkqxxDto = new YhkqxxDto();
                            yhkqxxDto.setRq(kqqjxxDto.getRq());
                            yhkqxxDto.setYhid(kqqjxxDto.getYhid());
                            yhkqxxDto.setQjkssj(kqqjxxDto.getQjkssj());
                            yhkqxxDto.setQjjssj(kqqjxxDto.getQjjssj());
                            yhkqxxDto.setQjsc(kqqjxxDto.getQjsc());
                            yhkqxxDto.setQjlx(kqqjxxDto.getQjlx());
                            YhkqxxDto kqxxdto = getKqid(yhkqxxDto);
                            if (kqxxdto != null) {
                                if (StringUtil.isBlank(kqxxdto.getQjkssj())) {
                                    yhkqxxDto.setKqid(kqxxdto.getKqid());
                                    if (StringUtil.isNotBlank(kqxxdto.getGzsc())) {
                                        double v = Double.parseDouble(kqxxdto.getGzsc()) - Double.parseDouble(kqqjxxDto.getQjsc().replace("小时", ""));
                                        yhkqxxDto.setGzsc(String.valueOf(v));
                                    }
                                    updateSj(yhkqxxDto);
                                }
                            } else {
                                yhkqxxDto.setKqid(StringUtil.generateUUID());
                                insert(yhkqxxDto);
                            }
                            kqqjxxService.insert(kqqjxxDto);
                        }
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }
    /*//定时更新加班信息
    public void updateWorkOvertime(Map<String,String> map) {
        //flg为0，更新四天前的考勤信息
        //flg为1，更新上个月的考勤信息
        //flg为2，更新本月的考勤信息
        String flg = map.get("flg");
        updateTimingWorkOvertime(flg);
    }

    //定时更新加班信息
    public void updateTimingWorkOvertime(String flg){
        if(flg.equals("1")){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            long startsj = calendar.getTime().getTime();
            calendar.add(Calendar.DATE, +35);
            long endsj = calendar.getTime().getTime();
            getWorkOvertime(startsj,endsj);
        }else if(flg.equals("2")){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            long startsj = calendar.getTime().getTime();
            calendar.add(Calendar.DATE, +35);
            long endsj = calendar.getTime().getTime();
            getWorkOvertime(startsj,endsj);
        }else if(flg.equals("0")){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -3);
            long startsj = calendar.getTime().getTime();
            calendar.add(Calendar.DATE, +1);
            long endsj = calendar.getTime().getTime();
            getWorkOvertime(startsj,endsj);
        }
    }

    public void getWorkOvertime(Long startsj,Long endsj){
        WbcxDto wbcxDto = new WbcxDto();
        wbcxDto.setLx(CharacterEnum.DINGDING.getCode());
        wbcxDto.setSftb("1");
        List<WbcxDto> wbcxDtos = wbcxService.getDtoList(wbcxDto);
        for (WbcxDto dto : wbcxDtos) {
            String token = talkUtil.getToken(dto.getWbcxid());
            JcsjDto jcsjDto_dd = new JcsjDto();
            jcsjDto_dd.setCsdm("E");
            jcsjDto_dd.setJclb("DINGTALK_AUDTI_CALLBACK_TYPE");
            jcsjDto_dd = jcsjService.getDtoByCsdmAndJclb(jcsjDto_dd);
            if (StringUtil.isBlank(jcsjDto_dd.getCsmc()) || StringUtil.isBlank(jcsjDto_dd.getCskz1())) {
                logger.error("getLeaveStatus--请设置加班的钉钉审批回调类型基础数据！");
                return;
            }
            List<YhkqxxDto> yhkqxxDtos = new ArrayList<>();
            SimpleDateFormat formatRq = new SimpleDateFormat("yyyy-MM-dd");
            List<String> approveList = talkUtil.getApproveList(token, jcsjDto_dd.getCskz1(), startsj, endsj, null);
            if (!CollectionUtils.isEmpty(approveList)) {
                for (String processInstanceId : approveList) {
                    GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult approveInfo = talkUtil.getApproveInfo(token, processInstanceId);
                    //审批完成的
                    if (ApproveStatusEnum.COMPLETED.code.equals(approveInfo.getStatus()) && "agree".equals(approveInfo.getResult())) {
                        YhkqxxDto yhkqxxDto = new YhkqxxDto();
                        yhkqxxDto.setKqid(StringUtil.generateUUID());
                        yhkqxxDto.setWbcxid(dto.getWbcxid());
                        yhkqxxDto.setRq(formatRq.format(DateUtils.parseDate("yyyy-MM-dd",approveInfo.getCreateTime())));
                        yhkqxxDto.setDdid(approveInfo.getOriginatorUserId());
                        XtyhDto xtyhDto = new XtyhDto();
                        xtyhDto.setDdid(approveInfo.getOriginatorUserId());
                        XtyhDto xtyhDto1 = xtyhService.getYhid(xtyhDto);
                        if (xtyhDto1 != null) {
                            yhkqxxDto.setYhid(xtyhDto1.getYhid());
                        } else {
                            logger.error("getLeaveStatus--获取yhid,ddid=" + xtyhDto.getDdid());
                            continue;
                        }
                        //获取表单的值
                        List<GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues> formComponentValues = approveInfo.getFormComponentValues();
                        for (GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues formComponentValue : formComponentValues) {
                            if (StringUtil.isNotBlank(formComponentValue.getName()))
                                switch (formComponentValue.getName()){
                                    case "开始时间": yhkqxxDto.setJbkssj(formComponentValue.getValue());break;
                                    case "结束时间": yhkqxxDto.setJbjssj(formComponentValue.getValue());break;
                                    case "班次": yhkqxxDto.setBcmc(StringUtil.isNotBlank(formComponentValue.getValue())?formComponentValue.getValue().split("-")[0]:null);break;
                                }
                        }
                        yhkqxxDtos.add(yhkqxxDto);
                    }
                }
            }
            dao.insertOrUpdateYhWorkOvertime(yhkqxxDtos);
        }
    }*/

    /**
     * 从数据库分页获取导出数据
     */
    public List<YhkqxxDto> getListForSelectExp(Map<String, Object> params){
        YhkqxxDto yhkqxxDto = (YhkqxxDto) params.get("entryData");
        queryJoinFlagExport(params,yhkqxxDto);
        return dao.getListForSelectExp(yhkqxxDto);

    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, YhkqxxDto yhkqxxDto)
    {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
        for (DcszDto dcszDto : choseList)
        {
            if(dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if(StringUtil.isNotBlank(dcszDto.getSqlzd())){
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        yhkqxxDto.setSqlParam(sqlParam.toString());
    }


    /**
     * 根据搜索条件获取导出条数
     */
    public int getCountForSearchExp(YhkqxxDto yhkqxxDto,Map<String,Object> params){
        return dao.getCountForSearchExp(yhkqxxDto);
    }
    /**
     * 根据搜索条件获取导出信息
     */
    public List<YhkqxxDto> getListForSearchExp(Map<String, Object> params) {
        YhkqxxDto yhkqxxDto = (YhkqxxDto) params.get("entryData");
        queryJoinFlagExport(params, yhkqxxDto);
        return dao.getListForSearchExp(yhkqxxDto);
    }
    /**
     * 根据yhid和日期获取主键id
     */
    public YhkqxxDto getKqid(YhkqxxDto yhkqxxDto){
        return dao.getKqid(yhkqxxDto);
    }
    /**
     * 更新出勤退勤信息
     */
    public boolean updateSj(YhkqxxDto yhkqxxDto){
        return dao.updateSj(yhkqxxDto);
    }

    /**
     * 根据kqid获取用户出勤信息
     */
    public YhkqxxDto getKqxxByKqid(YhkqxxDto yhkqxxDto){
        return dao.getKqxxByKqid(yhkqxxDto);
    }
    /**
     * 根据ids删除信息
     */
    public boolean delKqxx(YhkqxxDto yhkqxxDto){
        return dao.delKqxx(yhkqxxDto);
    }
    /**
     * 根据kqid获取信息
     */
    public YhkqxxDto getByKqid(String kqid){
        return dao.getByKqid(kqid);
    }
    /**
     * 获取审批实例ID列表
     */
    public  OapiProcessinstanceListidsResponse getApprovalInstance(String processCode,Long startsj,Long endsj,String token,Long nextCursor){
        DingTalkClient client_lb = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/listids");
        OapiProcessinstanceListidsRequest req_lb = new OapiProcessinstanceListidsRequest();
        req_lb.setProcessCode(processCode);
        req_lb.setStartTime(startsj);
        req_lb.setEndTime(endsj);
        req_lb.setSize(20L);
        req_lb.setCursor(nextCursor);
        OapiProcessinstanceListidsResponse rsp_lb =null;
        try {
            rsp_lb = client_lb.execute(req_lb, token);
        } catch (ApiException e) {
            logger.error("getApprovalInstance错误--"+e.getMessage());
        }
        return rsp_lb;
    }
    /**
     * @description 同步考勤信息
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void syncAttendanceInfo(Map<String,String> map) throws BusinessException, InterruptedException {
        String wbcxid = map.get("wbcxid");
        if (StringUtil.isBlank(wbcxid)){
            throw new BusinessException("msg","请配置外部程序id");
        }
        String flg = map.get("flg");
        Date dateStart = null;
        Date dateEnd = null;
        //flg为0，更新四天前的考勤信息
        //flg为1，更新上个月的考勤信息
        //flg为2，更新本月的考勤信息
        Calendar calendar = Calendar.getInstance();
        switch (flg) {
            case "1":
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                dateStart = calendar.getTime();
                calendar.add(Calendar.DATE, +30);
                dateEnd = calendar.getTime();
                break;
            case "2":
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                dateStart = calendar.getTime();
                calendar.add(Calendar.DATE, +30);
                dateEnd = calendar.getTime();
                break;
            case "0":
                calendar.add(Calendar.DATE, -3);
                dateStart = calendar.getTime();
                calendar.add(Calendar.DATE, +1);
                dateEnd = calendar.getTime();
                break;
        }
        //获取休息日
        Map<String, HolidayVo> holidayMap = holidayUtil.getAllHolidayByYear(DateUtils.getCustomFomratCurrentDate(dateStart, "yyyy"));
        List<YhkqxxDto> allYhkqxxDtos = new ArrayList<>();
        UserDto xtyhDto_y = new UserDto();
        xtyhDto_y.setWbcxid(wbcxid);
        List<UserDto> xtyhDtos = userService.getAllUserList(xtyhDto_y);//查找的是不包括删除标记为1的所有数据
        String token = dingTalkUtil.getToken(wbcxid);
        List<String> ddids = new ArrayList<>();
        for (UserDto xtyhDto : xtyhDtos) {
            if (!"1".equals(xtyhDto.getSfsd())&&StringUtil.isNotBlank(xtyhDto.getDdid())){
                ddids.add(xtyhDto.getDdid());
            }
        }
        ddids = ddids.stream().distinct().collect(Collectors.toList());
        //在职人员
        for (String ddid : ddids) {
            logger.error("syncAttendanceInfo-------同步考勤信息，ddid="+ddid);
            List<OapiAttendanceGetcolumnvalResponse.ColumnValForTopVo> columnVals = dingTalkUtil.getColumnVal(token, StringUtil.join(getColumnIdList(), ","), ddid, dateStart,dateEnd);
            String yhid = "";
            for (UserDto xtyhDto : xtyhDtos) {
                if (ddid.equals(xtyhDto.getDdid())) {
                    yhid = xtyhDto.getYhid();
                    break;
                }
            }
            List<YhkqxxDto> yhkqxxDtos = this.getYhkqxxDtos(wbcxid, columnVals, yhid,holidayMap);
            List<OapiAttendanceGetleavetimebynamesResponse.ColumnValForTopVo> leaveColumnVal = dingTalkUtil.getLeaveColumnVal(token, StringUtil.join(getLeaveNames(), ","), ddid, dateStart, dateEnd);
            this.getLeaveYhkqxxDtos(wbcxid, leaveColumnVal, yhid,yhkqxxDtos,holidayMap);
            allYhkqxxDtos.addAll(yhkqxxDtos);
            Thread.sleep(10);
        }
        //离职人员考勤数据
        YghmcDto yghmcDto = new YghmcDto();
        yghmcDto.setWbcxid(wbcxid);
        Calendar instance = Calendar.getInstance();
        yghmcDto.setLzrqend(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
        instance.add(Calendar.DATE,-30);
        yghmcDto.setLzrqstart(DateUtils.getCustomFomratCurrentDate(instance.getTime(),"yyyy-MM-dd"));
        List<YghmcDto> yghmcDtos = yghmcService.getLzryByRq(yghmcDto);
        if (!CollectionUtils.isEmpty(yghmcDtos)) {
            //去除重复人员
            for (String ddid : ddids) {
                yghmcDtos.removeIf(next -> ddid.equals(next.getDdid()));
            }
            if (!CollectionUtils.isEmpty(yghmcDtos)) {
                //离职人员
                for (YghmcDto yghmc : yghmcDtos) {
                    logger.error("syncAttendanceInfo-------同步考勤信息，离职ddid="+yghmc.getDdid());
                    List<ColumnValForTopVo> columnVals = dingTalkUtil.getColumnVal(token, StringUtil.join(getColumnIdList(), ","), yghmc.getDdid(), dateStart, dateEnd);
                    List<YhkqxxDto> yhkqxxDtos = this.getYhkqxxDtos(wbcxid, columnVals, yghmc.getYhid(),holidayMap);
                    List<OapiAttendanceGetleavetimebynamesResponse.ColumnValForTopVo> leaveColumnVal = dingTalkUtil.getLeaveColumnVal(token, StringUtil.join(getLeaveNames(), ","), yghmc.getDdid(), dateStart, dateEnd);
                    this.getLeaveYhkqxxDtos(wbcxid, leaveColumnVal, yghmc.getYhid(),yhkqxxDtos,holidayMap);
                    allYhkqxxDtos.addAll(yhkqxxDtos);
                    Thread.sleep(10);
                }
            }
        }
        //新增或修改
        if (!CollectionUtils.isEmpty(allYhkqxxDtos)){
            List<List<YhkqxxDto>> partition = Lists.partition(allYhkqxxDtos, 500);
            for (List<YhkqxxDto> yhkqxxDtos : partition) {
                dao.insertOrUpdateYhkqxxDtos(yhkqxxDtos);
            }
        }
    }
    private void getLeaveYhkqxxDtos(String wbcxid, List<OapiAttendanceGetleavetimebynamesResponse.ColumnValForTopVo> leaveColumnVal, String yhid, List<YhkqxxDto> yhkqxxDtos,Map<String,HolidayVo> holidayMap) {
        for (OapiAttendanceGetleavetimebynamesResponse.ColumnValForTopVo columnValForTopVo : leaveColumnVal) {
            String name = columnValForTopVo.getColumnvo().getName();
            switch (name) {
                case "事假":
                    this.setLeaveVale(columnValForTopVo, yhkqxxDtos, "事假", wbcxid, yhid,holidayMap);//事假
                    break;
                case "病假":
                    this.setLeaveVale(columnValForTopVo, yhkqxxDtos, "病假", wbcxid, yhid,holidayMap);//病假
                    break;
                case "年假":
                    this.setLeaveVale(columnValForTopVo, yhkqxxDtos, "年假", wbcxid, yhid,holidayMap);//年假
                    break;
                case "调休":
                    this.setLeaveVale(columnValForTopVo, yhkqxxDtos, "调休", wbcxid, yhid,holidayMap);//调休
                    break;
                case "婚假":
                    this.setLeaveVale(columnValForTopVo, yhkqxxDtos, "婚假", wbcxid, yhid,holidayMap);//婚假
                    break;
                case "产假":
                    this.setLeaveVale(columnValForTopVo, yhkqxxDtos, "产假", wbcxid, yhid,holidayMap);//产假
                    break;
                case "护理假":
                    this.setLeaveVale(columnValForTopVo, yhkqxxDtos, "护理假", wbcxid, yhid,holidayMap);//护理假
                    break;
                case "产检假":
                    this.setLeaveVale(columnValForTopVo, yhkqxxDtos, "产检假", wbcxid, yhid,holidayMap);//产检假
                    break;
                case "丧假":
                    this.setLeaveVale(columnValForTopVo, yhkqxxDtos, "丧假", wbcxid, yhid,holidayMap);//丧假
                    break;
                case "育儿假":
                    this.setLeaveVale(columnValForTopVo, yhkqxxDtos, "育儿假", wbcxid, yhid,holidayMap);//育儿假
                    break;
                case "独生子女陪护父母假":
                    this.setLeaveVale(columnValForTopVo, yhkqxxDtos, "独生子女陪护父母假", wbcxid, yhid,holidayMap);//独生子女陪护父母假
                    break;
            }
        }
    }

    private void setLeaveVale(OapiAttendanceGetleavetimebynamesResponse.ColumnValForTopVo columnValForTopVo, List<YhkqxxDto> yhkqxxDtos, String methodName, String wbcxid, String yhid,Map<String,HolidayVo> holidayMap) {
        for (OapiAttendanceGetleavetimebynamesResponse.ColumnDayAndVal columnVals_val : columnValForTopVo.getColumnvals()) {
            String date = DateUtils.getCustomFomratCurrentDate(columnVals_val.getDate(),"yyyy-MM-dd");
            String value = columnVals_val.getValue();
            for (YhkqxxDto yhkqxxDto : yhkqxxDtos) {
                //如果有该日期数据 修改
                if (date.equals(yhkqxxDto.getRq())){
                    this.setYhkqxx(methodName, yhkqxxDto, value,holidayMap);
                }
            }
        }
    }
    @NotNull
    private List<YhkqxxDto> getYhkqxxDtos(String wbcxid, List<ColumnValForTopVo> columnVals, String yhid, Map<String, HolidayVo> holidayMap) {
        //用户考勤信息
        List<YhkqxxDto> yhkqxxDtos = new ArrayList<>();
        for (ColumnValForTopVo columnVal : columnVals) {
            List<ColumnDayAndVal> columnVals_vals = columnVal.getColumnVals();
            AttendanceEnum attendanceEnum = AttendanceEnum.getById(columnVal.getColumnVo().getId());
            switch (attendanceEnum) {
                case SHOULD_ATTENDANCE_DAYS:
                    this.setVale(columnVals_vals, yhkqxxDtos, "ycqts", wbcxid, yhid,holidayMap);//应出勤天数
                    break;
                case ATTENDANCE_DAYS:
                    this.setVale(columnVals_vals, yhkqxxDtos, "cqts", wbcxid, yhid,holidayMap);//实际出勤天数
                    break;
                case ATTENDANCE_REST_DAYS:
                    this.setVale(columnVals_vals, yhkqxxDtos, "xxts", wbcxid, yhid,holidayMap);//休息天数
                    break;
                case LATE_TIMES:
                    this.setVale(columnVals_vals, yhkqxxDtos, "cdcs", wbcxid, yhid,holidayMap);//迟到天数
                    break;
                case LATE_MINUTE:
                    this.setVale(columnVals_vals, yhkqxxDtos, "cdsc", wbcxid, yhid,holidayMap);//迟到时长
                    break;
                case LEAVE_EARLY_TIMES:
                    this.setVale(columnVals_vals, yhkqxxDtos, "ztcs", wbcxid, yhid,holidayMap);//早退次数
                    break;
                case LEAVE_EARLY_MINUTE:
                    this.setVale(columnVals_vals, yhkqxxDtos, "ztsc", wbcxid, yhid,holidayMap);//早退时长
                    break;
                case ON_WORK_LACK_CARD_TIMES:
                    this.setVale(columnVals_vals, yhkqxxDtos, "sbqkcs", wbcxid, yhid,holidayMap);//上班缺卡次数
                    break;
                case OFF_WORK_LACK_CARD_TIMES:
                    this.setVale(columnVals_vals, yhkqxxDtos, "xbqkcs", wbcxid, yhid,holidayMap);//下班缺卡次数
                    break;
                case ABSENTEEISM_DAYS:
                    this.setVale(columnVals_vals, yhkqxxDtos, "kgts", wbcxid, yhid,holidayMap);//旷工次数
                    break;
                case BUSINESS_TRIP_TIME:
                    this.setVale(columnVals_vals, yhkqxxDtos, "ccsc", wbcxid, yhid,holidayMap);//出差时长
                    break;
                case OUT_TIME:
                    this.setVale(columnVals_vals, yhkqxxDtos, "wcsc", wbcxid, yhid,holidayMap);//外出时长
                    break;
                case OVERTIME_DURATION:
                    this.setVale(columnVals_vals, yhkqxxDtos, "jbzsc", wbcxid, yhid,holidayMap);//加班总时长
                    break;
                case OVERTIME_工作日加班:
                    this.setVale(columnVals_vals, yhkqxxDtos, "gzrjb", wbcxid, yhid,holidayMap);//工作日加班
                    break;
                case OVERTIME_休息日加班:
                    this.setVale(columnVals_vals, yhkqxxDtos, "xxrjb", wbcxid, yhid,holidayMap);//休息日加班
                    break;
                case OVERTIME_节假日加班:
                    this.setVale(columnVals_vals, yhkqxxDtos, "jjrjb", wbcxid, yhid,holidayMap);//节假日加班
                    break;
                case ATTEND_RESULT:
                    this.setVale(columnVals_vals, yhkqxxDtos, "kqjg", wbcxid, yhid,holidayMap);//考勤结果
                    break;
            }
        }
        return yhkqxxDtos;
    }

    public String getQjlx(String csmc) {
        String qjlx = "";
        List<JcsjDto> jqlxs = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.HOLIDAY_TYPE.getCode());//假期类型
        for (JcsjDto jqlx : jqlxs) {
            if (jqlx.getCsmc().equals(csmc)){
                qjlx = jqlx.getCsid();
                break;
            }
        }
        return StringUtil.isNotBlank(qjlx)?qjlx:csmc;
    }
    public String getCqzt(String csdm) {
        String cqzt = "";
        List<JcsjDto> cqzts = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.ATTENDANCE_STATUS.getCode());//出勤状态
        for (JcsjDto cq : cqzts) {
            if (cq.getCsdm().equals(csdm)){
                cqzt = cq.getCsid();
                break;
            }
        }
        return StringUtil.isNotBlank(cqzt)?cqzt:csdm;
    }
    private void setVale(List<ColumnDayAndVal> columnVals_vals, List<YhkqxxDto> yhkqxxDtos,String methodName,String wbcxid,String yhid,Map<String, HolidayVo> holidayMap) {
        for (ColumnDayAndVal columnVals_val : columnVals_vals) {
            String date = DateUtils.getCustomFomratCurrentDate(columnVals_val.getDate(),"yyyy-MM-dd");
            String value = columnVals_val.getValue();
            boolean isHave = false;
            for (YhkqxxDto yhkqxxDto : yhkqxxDtos) {
                //如果有该日期数据 修改
                if (date.equals(yhkqxxDto.getRq())){
                    isHave = true;
                    this.setYhkqxx(methodName, yhkqxxDto, value,holidayMap);
                }
            }
            //如果没有该日期数据 新增
            if (!isHave){
                YhkqxxDto yhkqxxDto = new YhkqxxDto();
                yhkqxxDto.setKqid(StringUtil.generateUUID());
                yhkqxxDto.setRq(date);
                yhkqxxDto.setWbcxid(wbcxid);
                yhkqxxDto.setYhid(yhid);
                this.setYhkqxx(methodName, yhkqxxDto, value,holidayMap);
                yhkqxxDtos.add(yhkqxxDto);
            }
        }
    }

    private void setYhkqxx(String methodName, YhkqxxDto yhkqxxDto, String value,Map<String, HolidayVo> holidayMap) {
        switch (methodName){
            case "ycqts":
                yhkqxxDto.setYcqts(value);//应出勤天数
                break;
            case "cqts":
                if (MapUtils.isNotEmpty(holidayMap)){
                    HolidayVo holidayVo = holidayMap.get(yhkqxxDto.getRq());
                    //如果是休息日不计算出勤天数
                    if (holidayVo!=null&&("1".equals(holidayVo.getStatus())||"2".equals(holidayVo.getStatus()))){
                        value = "0";
                    }
                }
                yhkqxxDto.setCqts(value);//实际出勤天数
                break;
            case "xxts":
                yhkqxxDto.setXxts(value);//休息天数
                break;
            case "cdcs":
                yhkqxxDto.setCdcs(value);//迟到天数
                break;
            case "cdsc":
                yhkqxxDto.setCdsc(value);//迟到时长
                break;
            case "ztcs":
                yhkqxxDto.setZtcs(value);//早退次数
                break;
            case "ztsc":
                yhkqxxDto.setZtsc(value);//早退时长
                break;
            case "sbqkcs":
                yhkqxxDto.setSbqkcs(value);//上班缺卡次数
                break;
            case "xbqkcs":
                yhkqxxDto.setXbqkcs(value);//下班缺卡次数
                break;
            case "kgts":
                yhkqxxDto.setKgts(value);//旷工次数
                break;
            case "ccsc":
                yhkqxxDto.setCcsc(value);//出差时长
                break;
            case "wcsc":
                yhkqxxDto.setWcsc(value);//外出时长
                break;
            case "jbzsc":
                yhkqxxDto.setJbzsc(value);//加班总时长
                break;
            case "gzrjb":
                yhkqxxDto.setGzrjb(value);//工作日加班
                break;
            case "xxrjb":
                yhkqxxDto.setXxrjb(value);//休息日加班
                break;
            case "jjrjb":
                yhkqxxDto.setJjrjb(value);//节假日加班
                break;
            case "kqjg":
                yhkqxxDto.setKqjg(value);//考勤结果
                break;
            case "事假":
                yhkqxxDto.setSjxs(value);//事假
                break;
            case "病假":
                yhkqxxDto.setBjxs(value);//病假
                break;
            case "年假":
                yhkqxxDto.setNjxs(value);//年假
                break;
            case "调休":
                yhkqxxDto.setTxxs(value);//调休
                break;
            case "婚假":
                yhkqxxDto.setHjts(value);//婚假
                break;
            case "产假":
                if (MapUtils.isNotEmpty(holidayMap)){
                    HolidayVo holidayVo = holidayMap.get(yhkqxxDto.getRq());
                    //如果是休息日不计算产检假，防止出勤天数-产假天数为负
                    if (holidayVo!=null&&("1".equals(holidayVo.getStatus())||"2".equals(holidayVo.getStatus()))){
                        value = "0";
                    }
                }
                yhkqxxDto.setCjts(value);//产假
                break;
            case "护理假":
                yhkqxxDto.setHljts(value);//护理假
                break;
            case "产检假":
                yhkqxxDto.setCjjts(value);//产检假
                break;
            case "丧假":
                yhkqxxDto.setSjts(value);//丧假
                break;
            case "育儿假":
                yhkqxxDto.setYejts(value);//育儿假
                break;
            case "独生子女陪护父母假":
                yhkqxxDto.setDsznphfmjts(value);//独生子女陪护父母假
                break;
        }
    }
    public List<String> getLeaveNames(){
        List<String> leaveNames = new ArrayList<>();
        leaveNames.add("事假");
        leaveNames.add("病假");
        leaveNames.add("年假");
        leaveNames.add("调休");
        leaveNames.add("婚假");
        leaveNames.add("产假");
        leaveNames.add("护理假");
        leaveNames.add("产检假");
        leaveNames.add("丧假");
        leaveNames.add("育儿假");
        leaveNames.add("独生子女陪护父母假");
        return leaveNames;
    }
    //将应出勤天数，实际出勤天数，休息天数，迟到天数，迟到时长，早退次数，早退时长，上班缺卡次数，下班缺卡次数，旷工次数，出差时长，外出时长，加班总时长，工作日加班，休息日加班，节假日加班，考勤结果同步进用户考勤表。
    public List<Long> getColumnIdList(){
        List<Long> ids = new ArrayList<>();
        ids.add(AttendanceEnum.SHOULD_ATTENDANCE_DAYS.id);//将应出勤天数
        ids.add(AttendanceEnum.ATTENDANCE_DAYS.id);//实际出勤天数
        ids.add(AttendanceEnum.ATTENDANCE_REST_DAYS.id);//休息天数
        ids.add(AttendanceEnum.LATE_TIMES.id);//迟到天数
        ids.add(AttendanceEnum.LATE_MINUTE.id);//迟到时长
        ids.add(AttendanceEnum.LEAVE_EARLY_TIMES.id);//早退次数
        ids.add(AttendanceEnum.LEAVE_EARLY_MINUTE.id);//早退时长
        ids.add(AttendanceEnum.ON_WORK_LACK_CARD_TIMES.id);//上班缺卡次数
        ids.add(AttendanceEnum.OFF_WORK_LACK_CARD_TIMES.id);//下班缺卡次数
        ids.add(AttendanceEnum.ABSENTEEISM_DAYS.id);//旷工次数
        ids.add(AttendanceEnum.BUSINESS_TRIP_TIME.id);//出差时长
        ids.add(AttendanceEnum.OUT_TIME.id);//外出时长
        ids.add(AttendanceEnum.OVERTIME_DURATION.id);//加班总时长
        ids.add(AttendanceEnum.OVERTIME_工作日加班.id);//工作日加班
        ids.add(AttendanceEnum.OVERTIME_休息日加班.id);//休息日加班
        ids.add(AttendanceEnum.OVERTIME_节假日加班.id);//节假日加班
        ids.add(AttendanceEnum.ATTEND_RESULT.id);//考勤结果
        return ids;
    }
    /**
     * @description 同步考勤组信息
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void syncAttendanceGroups(Map<String,String> map) throws InterruptedException, BusinessException {
        String nowdate = DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd");
        String wbcxid = map.get("wbcxid");
        if (StringUtil.isBlank(wbcxid)){
            throw new BusinessException("msg","请配置外部程序id");
        }

        String token = dingTalkUtil.getToken(wbcxid);
        Map<Long,String> groups = new HashMap<>();
        List<OapiAttendanceGetsimplegroupsResponse.AtGroupForTopVo> simpleGroups = dingTalkUtil.getSimpleGroups(token);
        for (OapiAttendanceGetsimplegroupsResponse.AtGroupForTopVo simpleGroup : simpleGroups) {
            groups.put(simpleGroup.getGroupId(),simpleGroup.getGroupName()+"@"+simpleGroup.getType()+"@"+simpleGroup.getOwnerUserId());
        }
        logger.error("syncAttendanceGroups考勤组--"+JSON.toJSONString(groups));
        UserDto xtyhDto_y = new UserDto();
        xtyhDto_y.setWbcxid(wbcxid);
        List<UserDto> xtyhDtos = userService.getAllUserList(xtyhDto_y);//查找的是不包括删除标记为1的所有数据
        List<YhkqxxDto> yhkqxxDtos = new ArrayList<>();
        for (Long groupId : groups.keySet()) {
            String[] split = groups.get(groupId).split("@");
            List<OapiAttendanceGroupMemberListResponse.TopGroupMemberVo> memberList = dingTalkUtil.getMemberList(token, split[2], groupId);
            for (OapiAttendanceGroupMemberListResponse.TopGroupMemberVo topGroupMemberVo : memberList) {
                if ("0".equals(topGroupMemberVo.getType())){
                    boolean isHave = false;
                    for (YhkqxxDto yhkqxxDto : yhkqxxDtos) {
                        if (yhkqxxDto.getDdid().equals(topGroupMemberVo.getMemberId())){
                            isHave = true;
                            break;
                        }
                    }
                    //由于数据问题    （同一个人在两个考勤组里）如果已经有该人员数据，不继续新增
                    if (!isHave){
                        YhkqxxDto yhkqxxDto = new YhkqxxDto();
                        yhkqxxDto.setKqid(StringUtil.generateUUID());
                        yhkqxxDto.setRq(nowdate);
                        yhkqxxDto.setDdid(topGroupMemberVo.getMemberId());
                        yhkqxxDto.setKqz(split[0]);
                        yhkqxxDto.setKqlx(split[1]);
                        yhkqxxDto.setWbcxid(wbcxid);
                        yhkqxxDtos.add(yhkqxxDto);
                    }
                }else if ("1".equals(topGroupMemberVo.getType())){
                    List<OapiV2UserListResponse.ListUserResponse> allUsersV2ByKeyAndSecret = dingTalkUtil.getAllUsersV2(token, Long.parseLong(topGroupMemberVo.getMemberId()));
                    if (!CollectionUtils.isEmpty(allUsersV2ByKeyAndSecret)){
                        for (OapiV2UserListResponse.ListUserResponse userResponse : allUsersV2ByKeyAndSecret) {
                            boolean isHave = false;
                            for (YhkqxxDto yhkqxxDto : yhkqxxDtos) {
                                if (yhkqxxDto.getDdid().equals(userResponse.getUserid())){
                                    isHave = true;
                                    break;
                                }
                            }
                            //由于数据问题    （同一个人在两个考勤组里）
                            if (!isHave) {
                                YhkqxxDto yhkqxxDto = new YhkqxxDto();
                                yhkqxxDto.setKqid(StringUtil.generateUUID());
                                yhkqxxDto.setRq(nowdate);
                                yhkqxxDto.setDdid(userResponse.getUserid());
                                yhkqxxDto.setKqz(split[0]);
                                yhkqxxDto.setKqlx(split[1]);
                                yhkqxxDto.setWbcxid(wbcxid);
                                yhkqxxDtos.add(yhkqxxDto);
                            }
                        }
                    }else {
                        logger.error("syncAttendanceGroups未找到对应部门成员信息--ID="+topGroupMemberVo.getMemberId());
                    }

                }
            }
            logger.error("syncAttendanceGroups---考勤组"+split[0]);
            Thread.sleep(50);
        }
        for (UserDto xtyhDto : xtyhDtos) {
            for (YhkqxxDto yhkqxxDto : yhkqxxDtos) {
                if (xtyhDto.getDdid().equals(yhkqxxDto.getDdid())){
                    yhkqxxDto.setYhid(xtyhDto.getYhid());
                }
            }
        }
        //新增或修改
        if (!CollectionUtils.isEmpty(yhkqxxDtos)){
            List<List<YhkqxxDto>> partition = Lists.partition(yhkqxxDtos, 500);
            for (List<YhkqxxDto> yhkqxxDtos_add : partition) {
                dao.insertOrUpdateList(yhkqxxDtos_add);
            }
        }

    }
    @Override
    public List<YhkqxxDto> getPagedStatis(YhkqxxDto yhkqxxDto) {
        return dao.getPagedStatis(yhkqxxDto);
    }
    @Override
    public List<YhkqxxDto> getDtoListByYh(YhkqxxDto yhkqxxDto) {
        return dao.getDtoListByYh(yhkqxxDto);
    }

    @Override
    public YhkqxxDto getDtoByYhAndRq(YhkqxxDto yhkqxxDto) {
        return dao.getDtoByYhAndRq(yhkqxxDto);
    }

    @Override
    public List<YhkqxxDto> getPagedSubsidy(YhkqxxDto yhkqxxDto) {
        return dao.getPagedSubsidy(yhkqxxDto);
    }
    /**
     * 考勤统计导出
     */
    public int getAttCountForSearchExp(YhkqxxDto yhkqxxDto, Map<String, Object> params) {
        return dao.getAttCountForSearchExp(yhkqxxDto);
    }
    /**
     * 根据搜索条件获取导出信息
     */
    public List<YhkqxxDto> getAttListForSearchExp(Map<String, Object> params) {
        YhkqxxDto yhkqxxDto = (YhkqxxDto) params.get("entryData");
        queryJoinFlagExport(params, yhkqxxDto);
        return dao.getAttListForSearchExp(yhkqxxDto);
    }
    /**
     * 从数据库分页获取导出数据
     */
    public List<YhkqxxDto> getAttListForSelectExp(Map<String, Object> params) {
        YhkqxxDto yhkqxxDto = (YhkqxxDto) params.get("entryData");
        queryJoinFlagExport(params, yhkqxxDto);
        return dao.getAttListForSelectExp(yhkqxxDto);
    }

    @Override
    public boolean insertOrUpdateYhWorkOvertime(List<YhkqxxDto> yhkqxxDtos) {
        return dao.insertOrUpdateYhWorkOvertime(yhkqxxDtos);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean updateCallBackAskForLeave(GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult approveInfo,HttpServletRequest request) {
        KqqjxxDto kqqjxxDto = new KqqjxxDto();
        kqqjxxDto.setWbcxid(request.getParameter("wbcxid"));
        UserDto xtyhDto = new UserDto();
        xtyhDto.setDdid(approveInfo.getOriginatorUserId());
        UserDto xtyhDto1 = userService.getYhByDdid(xtyhDto);
        kqqjxxDto.setYhid(xtyhDto1.getYhid());
        List<GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues> formComponentValues = approveInfo.getFormComponentValues();
        GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues formComponentValue = formComponentValues.get(0);
        List<String> strings = JSON.parseArray(formComponentValue.getValue(), String.class);
        if (strings.get(0).contains("下午") || strings.get(0).contains("上午")) {
            if (strings.get(0).contains("上午")) {
                kqqjxxDto.setQjkssj(strings.get(0).substring(0, 11) + "08:30:00");
                kqqjxxDto.setRq(strings.get(0).substring(0, 10));
            } else if (strings.get(0).contains("下午")) {
                kqqjxxDto.setQjkssj(strings.get(0).substring(0, 11) + "13:00:00");
                kqqjxxDto.setRq(strings.get(0).substring(0, 10));
            }
            if (strings.get(1).contains("上午")) {
                kqqjxxDto.setQjjssj(strings.get(1).substring(0, 11) + "12:00:00");
            } else if (strings.get(1).contains("下午")) {
                kqqjxxDto.setQjjssj(strings.get(1).substring(0, 11) + "17:30:00");
            }
        } else {
            kqqjxxDto.setQjkssj(strings.get(0));
            kqqjxxDto.setQjjssj(strings.get(1));
            Date date = DateUtils.parseDate("yyyy-MM-dd HH:mm",strings.get(0));
            kqqjxxDto.setRq(new SimpleDateFormat("yyyy-MM-dd").format(date));
        }
        kqqjxxDto.setQjsc(strings.get(2) + strings.get(3));
        kqqjxxDto.setQjlx(getQjlx(strings.get(4)));
        kqqjxxDto.setSpid(request.getParameter("processInstanceId"));
        kqqjxxDto.setYhid(xtyhDto1.getYhid());
        KqqjxxDto qjxxdto = kqqjxxService.getSpid(kqqjxxDto);
        if (qjxxdto == null) {
            YhkqxxDto yhkqxxDto = new YhkqxxDto();
            yhkqxxDto.setRq(kqqjxxDto.getRq());
            yhkqxxDto.setYhid(kqqjxxDto.getYhid());
            yhkqxxDto.setQjkssj(kqqjxxDto.getQjkssj());
            yhkqxxDto.setQjjssj(kqqjxxDto.getQjjssj());
            yhkqxxDto.setQjsc(kqqjxxDto.getQjsc());
            yhkqxxDto.setQjlx(kqqjxxDto.getQjlx());
            YhkqxxDto kqxxdto = getKqid(yhkqxxDto);
            if (kqxxdto != null) {
                if (StringUtil.isBlank(kqxxdto.getQjkssj())) {
                    yhkqxxDto.setKqid(kqxxdto.getKqid());
                    if (StringUtil.isNotBlank(kqxxdto.getGzsc())) {
                        double v = Double.parseDouble(kqxxdto.getGzsc()) - Double.parseDouble(kqqjxxDto.getQjsc().replace("小时", ""));
                        yhkqxxDto.setGzsc(String.valueOf(v));
                    }
                    updateSj(yhkqxxDto);
                }
            } else {
                yhkqxxDto.setKqid(StringUtil.generateUUID());
                insert(yhkqxxDto);
            }
             kqqjxxService.insert(kqqjxxDto);
        }
        return true;
    }

    /**
     * 考勤统计导出
     */
    public int getDetailsCountForSearchExp(YhkqxxDto yhkqxxDto, Map<String, Object> params) {
        return dao.getDetailsCountForSearchExp(yhkqxxDto);
    }
    /**
     * 根据搜索条件获取导出信息
     */
    public List<YhkqxxDto> getDetailsListForSearchExp(Map<String, Object> params) {
        YhkqxxDto yhkqxxDto = (YhkqxxDto) params.get("entryData");
        queryJoinFlagExport(params, yhkqxxDto);
        return dao.getDetailsListForSearchExp(yhkqxxDto);
    }
    /**
     * 从数据库分页获取导出数据
     */
    public List<YhkqxxDto> getDetailsListForSelectExp(Map<String, Object> params) {
        YhkqxxDto yhkqxxDto = (YhkqxxDto) params.get("entryData");
        queryJoinFlagExport(params, yhkqxxDto);
        return dao.getDetailsListForSelectExp(yhkqxxDto);
    }
}

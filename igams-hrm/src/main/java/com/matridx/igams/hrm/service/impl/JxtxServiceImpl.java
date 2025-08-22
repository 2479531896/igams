package com.matridx.igams.hrm.service.impl;
import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.hrm.dao.entities.JxmbDto;
import com.matridx.igams.hrm.dao.entities.JxtxDto;
import com.matridx.igams.hrm.dao.entities.JxtxModel;
import com.matridx.igams.hrm.dao.entities.JxtxmxDto;
import com.matridx.igams.hrm.dao.post.IJxtxDao;
import com.matridx.igams.hrm.service.svcinterface.IJxmbService;
import com.matridx.igams.hrm.service.svcinterface.IJxtxService;
import com.matridx.igams.hrm.service.svcinterface.IJxtxmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;


/**
 * @author WYX
 * @version 1.0
 * @className JxtxServiceImpl
 **/
@Service
public class JxtxServiceImpl extends BaseBasicServiceImpl<JxtxDto, JxtxModel, IJxtxDao> implements IJxtxService {

    @Autowired
    IJxtxmxService jxtxmxService;
    @Autowired
    IJxmbService jxmbService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    private final Logger log = LoggerFactory.getLogger(GrjxServiceImpl.class);
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSavePerformanceReminder(JxtxDto jxtxDto) throws BusinessException {
        String jxtxid = StringUtil.generateUUID();
        jxtxDto.setJxtxid(jxtxid);
        insert(jxtxDto);
        List<JxtxmxDto> jxtxmxDtos = JSON.parseArray(jxtxDto.getJxtxmx_json(), JxtxmxDto.class);
        if (!CollectionUtils.isEmpty(jxtxmxDtos)){
            for (JxtxmxDto jxtxmxDto : jxtxmxDtos) {
                jxtxmxDto.setJxtxid(jxtxid);
                jxtxmxDto.setJxtxmxid(StringUtil.generateUUID());
                jxtxmxDto.setLrry(jxtxDto.getLrry());
            }
           boolean isSuccess = jxtxmxService.insertJxtxmxDtos(jxtxmxDtos);
            if (!isSuccess){
                throw new BusinessException("msg","新增绩效提醒明细失败！");
            }
        }
        return true;
    }
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSavePerformanceReminder(JxtxDto jxtxDto) throws BusinessException {
        update(jxtxDto);
        List<JxtxmxDto> jxtxmxDtos = JSON.parseArray(jxtxDto.getJxtxmx_json(), JxtxmxDto.class);
        List<JxtxmxDto> modJxtxmxDtos = new ArrayList<>();
        List<JxtxmxDto> addJxtxmxDtos = new ArrayList<>();
        JxtxmxDto jxtxmxDto_sel = new JxtxmxDto();
        jxtxmxDto_sel.setJxtxid(jxtxDto.getJxtxid());
        List<JxtxmxDto> dtoList = jxtxmxService.getDtoList(jxtxmxDto_sel);
        if (!CollectionUtils.isEmpty(jxtxmxDtos)){
            for (JxtxmxDto jxtxmxDto : jxtxmxDtos) {
                //剩下的就是删除的
                dtoList.removeIf(next -> next.getJxtxmxid().equals(jxtxmxDto.getJxtxmxid()));
                if (StringUtil.isBlank(jxtxmxDto.getJxtxmxid())){
                    jxtxmxDto.setJxtxid(jxtxDto.getJxtxid());
                    jxtxmxDto.setJxtxmxid(StringUtil.generateUUID());
                    jxtxmxDto.setLrry(jxtxDto.getXgry());
                    addJxtxmxDtos.add(jxtxmxDto);
                }else {
                    jxtxmxDto.setXgry(jxtxDto.getXgry());
                    modJxtxmxDtos.add(jxtxmxDto);
                }
            }
            if (!CollectionUtils.isEmpty(addJxtxmxDtos)){
                boolean isSuccess = jxtxmxService.insertJxtxmxDtos(addJxtxmxDtos);
                if (!isSuccess){
                    throw new BusinessException("msg","新增绩效提醒明细失败！");
                }
            }
            if (!CollectionUtils.isEmpty(modJxtxmxDtos)){
                boolean isSuccess = jxtxmxService.updateJxtxmxDtos(modJxtxmxDtos);
                if (!isSuccess){
                    throw new BusinessException("msg","修改绩效提醒明细失败！");
                }
            }
            if (!CollectionUtils.isEmpty(dtoList)){
                JxtxmxDto jxtxmxDto = new JxtxmxDto();
                List<String> ids = new ArrayList<>();
                for (JxtxmxDto dto : dtoList) {
                    ids.add(dto.getJxtxmxid());
                }
                jxtxmxDto.setIds(ids);
                jxtxmxDto.setScry(jxtxDto.getScry());
                boolean isSuccess = jxtxmxService.delete(jxtxmxDto);
                if (!isSuccess){
                    throw new BusinessException("msg","删除绩效提醒明细失败！");
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delPerformanceReminder(JxtxDto jxtxDto) throws BusinessException {
        dao.delete(jxtxDto);
        JxtxmxDto jxtxmxDto = new JxtxmxDto();
        jxtxmxDto.setIds(jxtxDto.getIds());
        jxtxmxDto.setScry(jxtxDto.getScry());
        boolean isSuccess = jxtxmxService.deleteByJxtxid(jxtxmxDto);
        if (!isSuccess){
            throw new BusinessException("msg","删除绩效提醒明细失败！");
        }
        return true;
    }

    /**
     * @description 定时绩效提醒
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void taskPerformanceReminder() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        //将String转LocalDateTime
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //获取所有绩效提醒信息
        JxtxmxDto jxtxmxDto = new JxtxmxDto();
        //需要查询是否在指定周期创建模板人员
        List<JxmbDto> jxmbDtos = new ArrayList<>();
        List<JxtxmxDto> dtoList = jxtxmxService.getDtoList(jxtxmxDto);
        if (!CollectionUtils.isEmpty(dtoList)){
            for (JxtxmxDto dto : dtoList) {
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
                int txrq = Integer.parseInt(dto.getTxrq());
                int khzqcskz2 = Integer.parseInt(dto.getKhzqcskz2());
                int ys = 0;
                //如果提醒日期为负数 日期差加一去求余
                if (txrq < 0) {
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
                    log.error("taskPerformanceReminder---考核周期参数扩展1不合法！---" + JSON.toJSONString(dto));
                }
                String zqkssj = null;
                String zqjssj = null;
                int khzqweek = new BigDecimal(khzqcskz2).multiply(new BigDecimal(7)).intValue();
                //余数为0 表示在发放周期 ，再判断发放日期是否满足提醒条件
                if (ys == 0) {
                    //本月 这一周期后几天提醒(发放日期为正数)这一周期前几天提醒(发放日期为负数)
                    if ("0".equals(dto.getTxy())) {
                        //是否满足提醒条件
                        if (txrq > 0) {
                            if ("WW".equals(dto.getKhzqcskz1())) {
                                //本周第几天和提醒日期相等，满足发放条件
                                if (txrq == weekDay) {
                                    calendar.add(Calendar.DATE, -txrq+1);
                                    zqkssj = format.format(calendar.getTime());
                                    calendar.add(Calendar.DATE,  khzqweek);
                                    zqjssj = format.format(calendar.getTime());
                                }
                            } else if ("YY".equals(dto.getKhzqcskz1())) {
                                //本年第几天和提醒日期相等，满足发放条件
                                if (txrq == yearDay) {
                                    calendar.add(Calendar.DATE, -txrq+1);
                                    zqkssj = format.format(calendar.getTime());
                                    calendar.add(Calendar.YEAR, khzqcskz2);
                                    zqjssj = format.format(calendar.getTime());
                                }
                            } else if ("MM".equals(dto.getKhzqcskz1())) {
                                //本月第几天和提醒日期相等，满足发放条件
                                if (txrq == monthDay) {
                                    calendar.add(Calendar.DATE, -txrq+1);
                                    zqkssj = format.format(calendar.getTime());
                                    calendar.add(Calendar.MONTH, khzqcskz2);
                                    zqjssj = format.format(calendar.getTime());
                                }
                            }
                        } else {
                            calendartwo.add(Calendar.DATE, txrq);
                            if (nowDateStr.equals(format.format(calendartwo.getTime()))) {
                                calendartwo.add(Calendar.DATE, -txrq);
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
                        //次月 下一周期后几天时提醒(提醒日期为正数)下一周期前几天时提醒(提醒日期为负数)
                    } else if ("1".equals(dto.getTxy())) {
                        if (txrq > 0) {
                            if ("WW".equals(dto.getKhzqcskz1())) {
                                //次周期周第几天和提醒日期相等，满足提醒条件
                                if (txrq == weekDay) {
                                    calendar.add(Calendar.DATE, -txrq+1);
                                    //当前日期减去发放日期为周期结束时间
                                    zqjssj = format.format(calendar.getTime());
                                    //往前推一个周期为周期开始时间
                                    calendar.add(Calendar.DATE, -khzqweek);
                                    zqkssj = format.format(calendar.getTime());
                                }
                            } else if ("YY".equals(dto.getKhzqcskz1())) {
                                //次周期年第几天和提醒日期相等，满足提醒条件
                                if (txrq == yearDay) {
                                    calendar.add(Calendar.DATE, -txrq+1);
                                    //当前日期减去提醒日期为周期结束时间
                                    zqjssj = format.format(calendar.getTime());
                                    //往前推一个周期为周期开始时间
                                    calendar.add(Calendar.YEAR, -khzqcskz2);
                                    zqkssj = format.format(calendar.getTime());
                                }
                            } else if ("MM".equals(dto.getKhzqcskz1())) {
                                //次周期月第几天和提醒日期相等，满足提醒条件
                                if (txrq == monthDay) {
                                    calendar.add(Calendar.DATE, -txrq+1);
                                    //当前日期减去提醒日期为周期结束时间
                                    zqjssj = format.format(calendar.getTime());
                                    //往前推一个周期为周期开始时间
                                    calendar.add(Calendar.MONTH, -khzqcskz2);
                                    zqkssj = format.format(calendar.getTime());
                                }
                            }
                        } else if (txrq < 0) {
                            //提醒日期为负数
                            calendartwo.add(Calendar.DATE, txrq);
                            if (nowDateStr.equals(format.format(calendartwo.getTime()))) {
                                calendartwo.add(Calendar.DATE, -txrq);
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
                        log.error("taskPerformanceReminder---发放考核月不合法！----" + JSON.toJSONString(dto));
                    }
                }
                //周期时间不为空也就是满足提醒条件
                if (StringUtil.isNotBlank(zqkssj) && StringUtil.isNotBlank(zqjssj)) {
                    //往前推一天
                    Calendar instance = Calendar.getInstance();
                    instance.setTime(DateUtils.parse(zqjssj));
                    instance.add(Calendar.DATE,-1);
                    zqjssj = format.format(instance.getTime());
                    //组装
                    JxmbDto jxmbDto = new JxmbDto();
                    jxmbDto.setRyid(dto.getRyid());
                    jxmbDto.setTxjb(dto.getTxjb());
                    jxmbDto.setYhm(dto.getYhm());
                    jxmbDto.setZqkssj(zqkssj);
                    jxmbDto.setZqjssj(zqjssj);
                    jxmbDtos.add(jxmbDto);
                }
            }
        }
        if (!CollectionUtils.isEmpty(jxmbDtos)){
            //获取在指定日期创建模板人员
            List<JxmbDto> txrys = jxmbService.getCreatByRq(jxmbDtos);
            if (!CollectionUtils.isEmpty(txrys)){
                for (JxmbDto jxmbDto : txrys) {
                    //剩下的就是要提醒没创建模板的人
                    jxmbDtos.removeIf(next -> jxmbDto.getRyid().equals(next.getRyid()) && jxmbDto.getTxjb().equals(next.getTxjb()));
                }
            }
            if (!CollectionUtils.isEmpty(jxmbDtos)){
                //去重
                jxmbDtos = jxmbDtos.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(f -> f.getRyid()+f.getTxjb()))), ArrayList::new));
                String ICOMM_JXTX00001 = xxglService.getMsg("ICOMM_JXTX00001");
                for (JxmbDto jxmbDto : jxmbDtos) {
                    if ("1".equals(jxmbDto.getTxjb())){
                        talkUtil.sendWorkMessage(jxmbDto.getYhm(), jxmbDto.getRyid(), ICOMM_JXTX00001,
                                xxglService.getMsg("ICOMM_JXTX00002", jxmbDto.getZqkssj(),jxmbDto.getZqjssj()));
                    }else {
                        talkUtil.sendWorkMessage(jxmbDto.getYhm(), jxmbDto.getRyid(), ICOMM_JXTX00001,
                                xxglService.getMsg("ICOMM_JXTX00003", jxmbDto.getZqkssj(),jxmbDto.getZqjssj()));
                    }
                }
            }
        }
    }

}

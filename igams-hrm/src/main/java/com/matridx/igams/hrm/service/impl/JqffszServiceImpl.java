package com.matridx.igams.hrm.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.hrm.dao.entities.JqffszDto;
import com.matridx.igams.hrm.dao.entities.JqffszModel;
import com.matridx.igams.hrm.dao.entities.JqxzDto;
import com.matridx.igams.hrm.dao.entities.YghmcDto;
import com.matridx.igams.hrm.dao.post.IJqffszDao;
import com.matridx.igams.hrm.service.svcinterface.IJqffszService;
import com.matridx.igams.hrm.service.svcinterface.IJqxzService;
import com.matridx.igams.hrm.service.svcinterface.IYghmcService;
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
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author:JYK
 */
@Service
public class JqffszServiceImpl extends BaseBasicServiceImpl<JqffszDto, JqffszModel, IJqffszDao> implements IJqffszService {
    private final Logger log = LoggerFactory.getLogger(JqffszServiceImpl.class);
    @Autowired
    IJqxzService jqxzService;
    @Autowired
    IYghmcService yghmcService;
    @Autowired
    RedisUtil redisUtil;
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addSaveVacationGrant(JqffszDto jqffszDto) throws BusinessException {
        String yhids=jqffszDto.getYhids();
        String[] split = yhids.split(",");
        List<JqffszDto> jqffszDtos=new ArrayList<>();
        for (String s : split) {
            JqffszDto jqffszDto1 = new JqffszDto();
            jqffszDto1.setFfszid(StringUtil.generateUUID());
            jqffszDto1.setJzrq(jqffszDto.getJzrq());
            jqffszDto1.setJzlx(jqffszDto.getJzlx());
            jqffszDto1.setLrry(jqffszDto.getLrry());
            jqffszDto1.setKssj(jqffszDto.getKssj());
            jqffszDto1.setJssj(jqffszDto.getJssj());
            jqffszDto1.setDw(jqffszDto.getDw());
            jqffszDto1.setSc(jqffszDto.getSc());
            jqffszDto1.setYhid(s);
            jqffszDto1.setJqlx(jqffszDto.getJqlx());
            jqffszDtos.add(jqffszDto1);
        }
        if (!CollectionUtils.isEmpty(jqffszDtos)){
            List<JqffszDto> jqffszDtoList=dao.selectDtoByList(jqffszDtos);
            if (!CollectionUtils.isEmpty(jqffszDtoList))  {
                StringBuilder message=new StringBuilder();
                for (JqffszDto dto : jqffszDtoList) {
                    if (StringUtil.isNotBlank(dto.getZsxm()) && StringUtil.isNotBlank(dto.getJqlxmc())) {
                        message.append(dto.getZsxm()).append("已存在").append(dto.getJqlxmc()).append("假期类型的假期发放,设置不允许重复添加！").append("</br>");
                    }
                }
                throw new BusinessException("msg",message.toString());
            }
        }
        if (!dao.insertList(jqffszDtos)){
            throw new BusinessException("msg","新增假期设置失败!");
        }
        List<JqxzDto> jqxzDtoList= JSON.parseArray(jqffszDto.getJqxz_json(), JqxzDto.class);
        List<JqxzDto> jqxzDtos=new ArrayList<>();
        if (StringUtil.isNotBlank(jqxzDtoList.get(0).getJqlx())) {
            for (JqxzDto dto : jqxzDtoList) {
                for (JqffszDto value : jqffszDtos) {
                    JqxzDto jqxzDto = new JqxzDto();
                    jqxzDto.setFfszid(value.getFfszid());
                    jqxzDto.setJqlx(dto.getJqlx());
                    jqxzDto.setSc(dto.getSc());
                    jqxzDto.setJqxzid(StringUtil.generateUUID());
                    jqxzDtos.add(jqxzDto);
                }
            }
            if (!jqxzService.insertList(jqxzDtos)){
                throw new BusinessException("msg","新增假期限制失败!");
            }
        }
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void regularHolidayDistribution(Map<String, String> map) {
        List<JcsjDto> jqlxlist=redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.HOLIDAY_TYPE.getCode());//假期类型
        JqffszDto jqffszDto=new JqffszDto();
        try {
            for (JcsjDto jcsjDto:jqlxlist){
                if ("杰毅年假".equals(jcsjDto.getCsmc())){
                    jqffszDto.setJqlx(jcsjDto.getCsid());
                    jqffszDto.setDw(jcsjDto.getCskz1());
                    break;
                }
            }
            if (StringUtil.isNotBlank(jqffszDto.getJqlx())){
                YghmcDto yghmcDto=new YghmcDto();
                yghmcDto.setJqxz("1");
                Calendar calendar=Calendar.getInstance();
                List<YghmcDto> yghmcDtoList=yghmcService.getDtoList(yghmcDto);
                Map<String, YghmcDto> mapStream = new ConcurrentHashMap<>();
                yghmcDtoList = yghmcDtoList.stream()
                        // 通过 map 对数据进行去重，map 只用在这里进行去重  防止员工花名册出现问题影响到这里
                        .filter(yghmcDtoStream -> mapStream.put(
                                yghmcDtoStream.getYhid()+yghmcDtoStream.getGznx()+yghmcDtoStream.getZzrq()+yghmcDtoStream.getCjgzsj(),
                                yghmcDtoStream) == null)
                        .collect(Collectors.toList());
                List<JqffszDto> jqffszDtos=new ArrayList<>();
                for (YghmcDto yghmcDto1 : yghmcDtoList){
                    JqffszDto jqffszDto1 = new JqffszDto();
                    jqffszDto1.setJqlx(jqffszDto.getJqlx());
                    jqffszDto1.setYhid(yghmcDto1.getYhid());
                    jqffszDto1.setDw(jqffszDto.getDw());
                    jqffszDto1.setJzrq(map.get("jzrq"));
                    jqffszDto1.setJzlx(map.get("jzlx"));
                    Date date=new SimpleDateFormat("yyyy-MM-dd").parse(yghmcDto1.getCjgzsj());
                    calendar.setTime(date);
                    if (Integer.parseInt(yghmcDto1.getGznx())>=20){
                        jqffszDto1.setSc("120");
                        calendar.add(Calendar.YEAR,15);
                    }else if (Integer.parseInt(yghmcDto1.getGznx())>=10&&Integer.parseInt(yghmcDto1.getGznx())<20){
                        jqffszDto1.setSc("80");
                        calendar.add(Calendar.YEAR,10);
                    }else if (Integer.parseInt(yghmcDto1.getGznx())>=1&&Integer.parseInt(yghmcDto1.getGznx())<10){
                        jqffszDto1.setSc("40");
                        calendar.add(Calendar.YEAR,1);
                    }
                    Date date1=calendar.getTime();
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    String detime=simpleDateFormat.format(date1);
                    if (detime.compareTo(yghmcDto1.getZzrq())>=0){
                        jqffszDto1.setKssj(detime);
                    }else {
                        jqffszDto1.setKssj(yghmcDto1.getZzrq());
                    }
                    jqffszDtos.add(jqffszDto1);
                }
                List<JqffszDto> jqffszDtos1=dao.selectDtosByYhIdAndJqlx(jqffszDtos);
                List<JqffszDto> jqffdzDtosAdds=new ArrayList<>();
                List<String> delFfszIds=new ArrayList<>();
                for (JqffszDto jqffszDto1:jqffszDtos1){
                    if (StringUtil.isBlank(jqffszDto1.getFfszid())){
                        jqffszDto1.setFfszid(StringUtil.generateUUID());
                        jqffdzDtosAdds.add(jqffszDto1);
                    }else {
                        if (!jqffszDto1.getKssj().equals(jqffszDto1.getKssj_t())){
                            delFfszIds.add(jqffszDto1.getFfszid());
                            jqffszDto1.setFfszid(StringUtil.generateUUID());
                            jqffdzDtosAdds.add(jqffszDto1);
                        }
                    }
                }

                if (!CollectionUtils.isEmpty(jqffdzDtosAdds)){
                    //当前年份
                    Calendar nowCal = Calendar.getInstance();
                    String dqnf = String.valueOf(nowCal.get(Calendar.YEAR));
                    //今年总天数
                    BigDecimal dnzts = new BigDecimal(nowCal.getActualMaximum(Calendar.DAY_OF_YEAR));
                    String nowStr = DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd");
                    //将String转LocalDateTime
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    for (JqffszDto jqffdzDtosAdd : jqffdzDtosAdds) {
                        //如果之前有假期发放设置的数据
                        if (StringUtil.isNotBlank(jqffdzDtosAdd.getKssj_t())){
                            //已发放的累计额度
                            BigDecimal ljed = new BigDecimal(jqffdzDtosAdd.getLjed_t());
                            BigDecimal nxzyed;
                            //开始时间年份
                            String kssjnf = jqffdzDtosAdd.getKssj_t().substring(0, 4);
                            LocalDate dqrq = LocalDate.parse(nowStr, df);
                            // 判断开始时间是否为当年 基本不可能
                            if (dqnf.equals(kssjnf)){
                                LocalDate kssj = LocalDate.parse(jqffdzDtosAdd.getKssj_t(), df);
                                //当前日期-开始日期
                                long days = ChronoUnit.DAYS.between(kssj,dqrq)+1;
                                //((当前日期-开始日期)/当年总天数*时长)- 累计额度 = 年限转移额度
                                nxzyed = new BigDecimal(days).divide(dnzts, 4, RoundingMode.DOWN).multiply(new BigDecimal(jqffdzDtosAdd.getSc_t())).subtract(ljed).setScale(2, RoundingMode.DOWN);
                            }else {
                                //当年第一天
                                LocalDate dyt = LocalDate.parse(dqnf+"-01-01", df);
                                //当前日期-当年第一天
                                long days = ChronoUnit.DAYS.between(dyt,dqrq)+1;
                                //((当前日期-当年第一天)/当年总天数*时长)- 累计额度 = 年限转移额度
                                nxzyed = new BigDecimal(days).divide(dnzts, 4, RoundingMode.DOWN).multiply(new BigDecimal(jqffdzDtosAdd.getSc_t())).subtract(ljed).setScale(2, RoundingMode.DOWN);
                            }
                            jqffdzDtosAdd.setNxzyed(String.valueOf(nxzyed));
                        }
                    }
                    dao.insertList(jqffdzDtosAdds);
                }
                if (!CollectionUtils.isEmpty(delFfszIds)){
                    JqffszDto jqffszDto1=new JqffszDto();
                    jqffszDto1.setIds(delFfszIds);
                    jqffszDto1.setScbj("2");
                    dao.update(jqffszDto1);
                }
            }

        }catch (Exception e){
            log.error(e.toString());
        }

    }

    @Override
    public List<JqffszDto> getListWithDistribute(JqffszDto jqffszDto) {
        return dao.getListWithDistribute(jqffszDto);
    }

    @Override
    public boolean updateToNull(JqffszDto jqffszDto) {
        return dao.updateToNull(jqffszDto);
    }

    @Override
    public boolean updateLjeds(List<JqffszDto> jqffszDtos) {
        return dao.updateLjeds(jqffszDtos);
    }
}

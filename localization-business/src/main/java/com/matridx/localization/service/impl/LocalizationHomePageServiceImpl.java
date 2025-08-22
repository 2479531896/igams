package com.matridx.localization.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.CommonWSController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.localization.config.RabbitMqLocal;
import com.matridx.localization.dao.entities.LocalizationStatisticsDto;
import com.matridx.localization.dao.post.ILocalizationHomePageDao;
import com.matridx.localization.service.svcinterface.ILocalizationHomePageService;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.encrypt.Encrypt;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
public class LocalizationHomePageServiceImpl implements ILocalizationHomePageService {
    @Autowired
    ILocalizationHomePageDao dao;
    @Autowired
    ICommonService commonService;
    @Autowired
    RedisUtil redisUtil_t;


    @Autowired
    private RabbitMqLocal localRabbitMq;
    private static RedisUtil redisUtil;

    @Value("${matridx.wechat.serverapplicationurl:}")
    private String serverapplicationurl;

    @Value("${spring.rabbitmq.report.virtualHost:}")
    private String virtualHost;

    @Value("${matridx.wechat.organ:}")
    private String organ;

    @Value("${matridx.wechat.word:}")
    private String word;
    @PostConstruct
    public void init() {
        redisUtil = redisUtil_t;
    }
    private final Logger log = LoggerFactory.getLogger(LocalizationHomePageServiceImpl.class);
    /*
     * 年龄与性别分布
     */
    @Override
    public List<Map<String, Object>> getAgeRangeAndGender(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException {
        LocalizationStatisticsDto clone = localizationStatisticsDto.clone();
        setCommonParams(clone,"year");
        return dao.getAgeRangeAndGender(clone);
    }

    /*
     * 样本类型分布
     */
    @Override
    public List<Map<String, Object>> getSampleTypeTotal(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException {
        LocalizationStatisticsDto clone = localizationStatisticsDto.clone();
        setCommonParams(clone,"year");
        return dao.getSampleTypeTotal(clone);
    }
    //科室分布
    @Override
    public List<Map<String, Object>> getInspectionDepartment(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException {
        LocalizationStatisticsDto clone = localizationStatisticsDto.clone();
        setCommonParams(clone,"year");
        return dao.getInspectionDepartment(clone);
    }
    /*
     * 微生物较上周变化情况
     */
    @Override
    public List<Map<String, Object>> getMicrobeChange(LocalizationStatisticsDto localizationStatisticsDto) {
        return dao.getMicrobeChange(localizationStatisticsDto);
    }
    /*
       本周样本数变化
     */
    @Override
    public List<Map<String, Object>> getSampleCountChange(LocalizationStatisticsDto localizationStatisticsDto) {
        return dao.getSampleCountChange(localizationStatisticsDto);
    }
    /*
        送检报告阳性率
     */
    @Override
    public List<Map<String, Object>> getPositiveRate(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException {
        LocalizationStatisticsDto clone = localizationStatisticsDto.clone();
        setCommonParams(clone,"week");
        return dao.getPositiveRate(clone);
    }
    /*
        耐药基因检测结果
     */
    @Override
    public List<Map<String, Object>> getDrugResistGeneCount(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException {
        LocalizationStatisticsDto clone = localizationStatisticsDto.clone();
        setCommonParams(clone,"year");
        return dao.getDrugResistGeneCount(clone);
    }
    /*
        检出微生物信息
     */
    @Override
    public List<Map<String, Object>> getMicrobeInfo(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException {
        LocalizationStatisticsDto clone = localizationStatisticsDto.clone();
        setCommonParams(clone,"year");
        return dao.getMicrobeInfo(clone);
    }
    /*
        样本总数、报告总数、阳性样本数
     */
    @Override
    public Map<String, Object> getSampleCountAndReportCountAndPositiveCount(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException {
        LocalizationStatisticsDto clone = localizationStatisticsDto.clone();
        setCommonParams(clone,"day");
        return dao.getSampleCountAndReportCountAndPositiveCount(clone);
    }
    /*
        样本总数、报告总数、阳性样本数(不限制伙伴、单位)
     */
    @Override
    public Map<String, Object> getSampleCountAndReportCountAndPositiveCountMapWhole(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException {
        LocalizationStatisticsDto clone = localizationStatisticsDto.clone();
        setCommonParams(clone,"day");
        return dao.getSampleCountAndReportCountAndPositiveCountMapWhole(clone);
    }
    /*
       年龄段的检出微生物占比
     */
    @Override
    public List<Map<String, Object>> getMicrobeInfoForAgeRange(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException {
        LocalizationStatisticsDto clone = localizationStatisticsDto.clone();
        setCommonParams(clone,"year");
        return dao.getMicrobeInfoForAgeRange(clone);
    }
    /*
      检出这个微生物的样本类型的分布
    */
    @Override
    public List<Map<String, Object>> getSampleTypeTotalForMicrobe(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException {
        LocalizationStatisticsDto clone = localizationStatisticsDto.clone();
        setCommonParams(clone,"year");
        return dao.getSampleTypeTotalForMicrobe(clone);
    }
    /*
      指定样本类型的检出微生物检出趋势
    */
    @Override
    public List<Map<String, Object>> microbeTrendForSampleType(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException {
        LocalizationStatisticsDto clone = localizationStatisticsDto.clone();
        setCommonParams(clone,"year");
        List<Map<String, Object>> microbeTrendForSampleType = dao.microbeTrendForSampleType(clone);
        List<Map<String, Object>> allMicrobeForSampleType = dao.allMicrobeForSampleType(clone);
        if(!CollectionUtils.isEmpty(microbeTrendForSampleType)){
            for (Map<String, Object> map : microbeTrendForSampleType) {
                for (Map<String, Object> allMap : allMicrobeForSampleType) {
                    if (map.get("rq").equals(allMap.get("rq"))){
                        map.put("wznum", allMap.get("wznum"));
                        break;
                    }
                }
            }
        }
        return microbeTrendForSampleType;
    }
    /*
      各个年龄段的指定耐药基因的检出趋势
     */
    @Override
    public List<Map<String, Object>> ageRangeTrendForDrugResistGene(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException {
        LocalizationStatisticsDto clone = localizationStatisticsDto.clone();
        setCommonParams(clone,"year");
        return dao.ageRangeTrendForDrugResistGene(clone);
    }

    private void setCommonParams(LocalizationStatisticsDto clone, String initTjlx) throws BusinessException {
        Calendar calendar = clone.getCalendar();
        if (clone.isInit()){
            getCommonParams(clone, initTjlx, calendar);
        }else{
            getCommonParams(clone, clone.getTjlx(), calendar);
        }
    }

    private static void getCommonParams(LocalizationStatisticsDto clone, String tjlx, Calendar calendar) {
        String nowDateStr = DateUtils.getCustomFomratCurrentDate(calendar.getTime(), "yyyy-MM-dd");
        switch (tjlx){
            case "year":
                clone.setYear(nowDateStr);
                break;
            case "month":
                clone.setMonth(nowDateStr);
                break;
            case "week":
                //week特殊处理 sql会自动获取哪一周
                clone.setTjlx("week");
                clone.setWeek(nowDateStr);
                break;
            case "day":
                clone.setDay(nowDateStr);
                break;
        }
        if(!CollectionUtils.isEmpty(clone.getJcdwdms())) {
            List<JcsjDto> jcdwDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
            clone.setJcdws(new ArrayList<>());
            jcdwDtos.forEach(jcdwDto -> {
                if (clone.getJcdwdms().contains(jcdwDto.getCsdm())) {
                    clone.getJcdws().add(jcdwDto.getCsid());
                }
            });
        }
    }

    public void dealLocalHospitalInspect(String str) {
        try {
            String text = JSON.parse(str) + organ + word;
            String sign = Encrypt.encrypt(text, "SHA1");
            RestTemplate restTemplate=new RestTemplate();
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("organ", organ);
            params.add("sign",sign);
            params.add("jsonStr", JSON.parse(str));
            restTemplate.postForObject(serverapplicationurl+"/ws/inspect/receiveInspectInfo",params, Map.class);
        }catch (Exception e){
            //若抛出异常则将该消息重新放入队列中
            log.error("消息处理失败，重新入队。内容：{},{}", str, e);
            try {
                Connection connection = null;
                Channel channelReport = null;
                ConnectionFactory connectionFactory = new ConnectionFactory();
                connectionFactory.setHost(localRabbitMq.getHost());
                connectionFactory.setUsername(localRabbitMq.getUsername());
                connectionFactory.setPassword(localRabbitMq.getPassword());
                connectionFactory.setPort(Integer.parseInt(localRabbitMq.getPort()));
                connectionFactory.setVirtualHost(virtualHost);
                // 设置连接超时时间为5秒
                connectionFactory.setConnectionTimeout(5000); // 单位是毫秒
                connection = connectionFactory.newConnection();
                channelReport = connection.createChannel();
                // 声明一个队列，如果队列不存在会被创建
                channelReport.queueDeclare("matridx.local.inspect.enter", true, false, false, null);

                // 发布消息到队列中
                channelReport.basicPublish("local.exchange", "matridx.local.inspect.enter", null, str.getBytes());
            }catch (Exception e1){
                log.error("reportRabbit 获取连接IO异常或发送消息异常");
            }
//            // 添加重试逻辑（需要注入RabbitTemplate）
//            amqpTempl.convertAndSend(
//                    "local.exchange",  // 实际交换机名称
//                    "matridx.local.inspect.enter",    // 实际路由键
//                    str);
        }
    }




    /**
     * @Description: 发送消息
     * @param str
     * @return void
     * @Author: 郭祥杰
     * @Date: 2025/4/27 14:42
     */
    @Override
    public void sendMessage(String str) {
        try {
            RestTemplate restTemplate=new RestTemplate();
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("message",str);
            restTemplate.postForObject(serverapplicationurl+"/ws/inspect/sendMessage", params, Map.class);
            commonService.logErrorNumMap(str,"errorMessage","消息发送成功!错误次数为:",true);
        }catch (Exception e) {
            try {
                String jsonStr = commonService.logErrorNumMap(str,"errorMessage","rabbit重发:"+e+str,true);
                Connection connection = null;
                Channel channelReport = null;
                ConnectionFactory connectionFactory = new ConnectionFactory();
                connectionFactory.setHost(localRabbitMq.getHost());
                connectionFactory.setUsername(localRabbitMq.getUsername());
                connectionFactory.setPassword(localRabbitMq.getPassword());
                connectionFactory.setPort(Integer.parseInt(localRabbitMq.getPort()));
                connectionFactory.setVirtualHost(virtualHost);
                // 设置连接超时时间为5秒
                connectionFactory.setConnectionTimeout(5000); // 单位是毫秒
                connection = connectionFactory.newConnection();
                channelReport = connection.createChannel();
                // 声明一个队列，如果队列不存在会被创建
                channelReport.queueDeclare("matridx.inspect.report.sendMessage", true, false, false, null);

                // 发布消息到队列中
                channelReport.basicPublish("local.exchange", "matridx.inspect.report.sendMessage", null, jsonStr.getBytes());
            }catch (Exception e1){
                log.error("reportRabbit 获取连接IO异常或发送消息异常");
            }

        }
    }
}

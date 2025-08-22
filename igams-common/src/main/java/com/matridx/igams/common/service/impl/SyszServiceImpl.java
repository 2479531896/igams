package com.matridx.igams.common.service.impl;
import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.RedisCommonKeyEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.post.ISyszDao;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SyszServiceImpl extends BaseBasicServiceImpl<SyszDto, SyszModel, ISyszDao> implements ISyszService{
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IShgcService shgcService;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Autowired
    IGzglService gzglService;
    @Autowired
    IDbszService dbszService;
    @Autowired
    IGrbjService grbjService;
    @Autowired
    IGgglService ggglService;
    private final Logger log = LoggerFactory.getLogger(SyszServiceImpl.class);
    /**
     * 获取首页要展示的页面
     */
    @Override
    public List<SyszDto> getHomePage(SyszDto syszDto) {
        Object persionPageObj = redisUtil.hget(RedisCommonKeyEnum.HOME_PAGE_PERSION_PAGE.getKey(), syszDto.getYhid());
        if (persionPageObj!=null){
            return JSON.parseArray(String.valueOf(persionPageObj),SyszDto.class);
        }
        List<SyszDto> syszDtos = dao.getHomePage(syszDto);
        if (CollectionUtils.isEmpty(syszDtos)){
            Object defaultObj = redisUtil.hget(RedisCommonKeyEnum.HOME_PAGE_PERSION_PAGE.getKey(), "default");
            if(defaultObj!=null) {
                syszDtos = JSON.parseArray(String.valueOf(defaultObj),SyszDto.class);
            }else {
                SyszDto syszDtoDefault = new SyszDto();
                syszDtoDefault.setYhid("default");
                syszDtos = dao.getHomePage(syszDtoDefault);
            }
        }
        return syszDtos;
    }
    /**
     *  获取首页要展示的页面数据
     */
    @Override
    public void getHomePageData(List<SyszDto> syszDtos, Map<String, Object> map,SyszDto syszDto) {
        if (!CollectionUtils.isEmpty(syszDtos)){
            Map<String, List<SyszDto>> listMap = syszDtos.stream().collect(Collectors.groupingBy(e->StringUtil.isBlank(e.getFbsbj())?"":e.getFbsbj()));
            for (String fbsbj : listMap.keySet()) {
                List<SyszDto> dtoList = listMap.get(fbsbj);
                List<String> zjqfs = dtoList.stream().map(SyszDto::getZjqf).distinct().collect(Collectors.toList());
                for (String zjqf : zjqfs) {
                    switch (zjqf) {
                        //工作任务统计
                        case "TaskStatis" : getHomePageDataForTaskStatis(syszDto,map,fbsbj);break;
                        //审核任务代办
                        case "AuditTaskWaiting" : getHomePageDataForAuditTaskWaiting(syszDto,map,fbsbj);break;
                        //公告
                        case "Notice" : getHomePageDataForNotice(map);break;
                        //笔记
                        case "Note" : getHomePageDataForNote(syszDto,map);break;
                        //日程
                        case "Schedule" : getHomePageDataForSchedule(syszDto,map);break;
                    }
                }
            }
        }
    }
    /*
      获取日程
    */
    private void getHomePageDataForSchedule(SyszDto syszDto, Map<String, Object> map) {
        GrbjDto grbjDto = new GrbjDto();
        grbjDto.setRqM(DateUtils.getCustomFomratCurrentDate("yyyy-MM"));
        grbjDto.setYhid(syszDto.getYhid());
        grbjDto.setTbrc("1");
        List<GrbjDto> grbjDtos = grbjService.getDtoList(grbjDto);
        map.put("Schedule",grbjDtos);
    }

    /*
       获取个人笔记
     */
    private void getHomePageDataForNote(SyszDto syszDto, Map<String, Object> map) {
        GrbjDto grbjDto = new GrbjDto();
        grbjDto.setYhid(syszDto.getYhid());
        grbjDto.setSortName("rq");
        grbjDto.setSortOrder("DESC");
        grbjDto.setPageSize(10);
        grbjDto.setPageNumber(1);
        List<GrbjDto> grbjDtos = grbjService.getPagedDtoList(grbjDto);
        Map<String, Object> hashMap = new HashMap<>();
        map.put("total", grbjDto.getTotalNumber());
        hashMap.put("rows",grbjDtos);
        map.put("Note",hashMap);
    }

    /*
      获取公告
     */
    private void getHomePageDataForNotice(Map<String, Object> map) {
        GgglDto ggglDto = new GgglDto();
        ggglDto.setSortName("rq");
        ggglDto.setSortOrder("DESC");
        ggglDto.setPageSize(10);
        ggglDto.setPageNumber(1);
        ggglDto.setSfjz("0");
        List<GgglDto> ggglDtos = ggglService.getPagedDtoList(ggglDto);
        Map<String, Object> hashMap = new HashMap<>();
        map.put("total", ggglDto.getTotalNumber());
        hashMap.put("rows",ggglDtos);
        map.put("Notice",hashMap);
    }

    /**
     *  获取任务完成状态
     */
    private void getHomePageDataForTaskStatis(SyszDto syszDto, Map<String, Object> map ,String fbsbj) {
        try {
            if (StringUtil.isNotBlank(fbsbj)){
                MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                paramMap.add("access_token", syszDto.getAccess_token());
                paramMap.add("syszDto", syszDto);
                RestTemplate restTemplate = new RestTemplate();
                @SuppressWarnings("unchecked")
                Map<String, Object> taskMap = restTemplate.postForObject( applicationurl+fbsbj+"/homePage/homePage/pagedataGetHomePageTaskStatis",paramMap, Map.class);
                map.put("TaskStatus"+fbsbj,taskMap);
            }else {
                Map<String, Object> homePageTaskStatis = this.getHomePageTaskStatis(syszDto);
                map.put("TaskStatus",homePageTaskStatis);
            }
        }catch (Exception e){
            log.error("getHomePageDataForTaskStatis--错误信息：{},参数：{}",e.getMessage(),fbsbj+JSON.toJSONString(syszDto));
        }
    }
    /**
     *  获取任务代办
     */
    private void getHomePageDataForAuditTaskWaiting(SyszDto syszDto, Map<String, Object> map ,String fbsbj) {
        try {
            DbszDto dbszDto = new DbszDto();
            dbszDto.setYhid(syszDto.getYhid());
            dbszDto.setFbsbj(fbsbj);
            List<DbszDto> dbszDtos = this.getPersonAuditTaskWaiting(dbszDto);
            if (StringUtil.isNotBlank(fbsbj)){
                MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                paramMap.add("access_token", syszDto.getAccess_token());
                paramMap.add("syszDto", syszDto);
                paramMap.add("dbszDtos", dbszDtos);
                RestTemplate restTemplate = new RestTemplate();
                @SuppressWarnings("unchecked")
                Map<String, Object> taskWaitingMap = restTemplate.postForObject( applicationurl+fbsbj+"/homePage/homePage/pagedataGetHomePageAuditTaskWaiting",paramMap, Map.class);
                map.put("AuditTaskWaiting"+fbsbj,taskWaitingMap);
            }else {
                map.put("AuditTaskWaiting",this.getHomePageAuditTaskWaiting(syszDto,dbszDtos));
            }
        }catch (Exception e){
            log.error("getHomePageDataForAuditTaskWaiting--错误信息：{},参数：{}",e.getMessage(),fbsbj+JSON.toJSONString(syszDto));
        }
    }
    /**
     *  获取审核任务代办
     */
    public Map<String,Object> getHomePageAuditTaskWaiting(SyszDto syszDto,List<DbszDto> dbszDtos){
        Map<String, Object> map = new HashMap<>();
        List<Map<String,Object>> auditTaskWaitingCounts = new ArrayList<>();
        List<ShgcDto> rows = new ArrayList<>();
        int totalNumber = 0;
        if (!CollectionUtils.isEmpty(dbszDtos)){
            ShgcDto shgcDto = new ShgcDto();
            shgcDto.setYhid(syszDto.getYhid());
            shgcDto.setDbszDtos(dbszDtos);
            auditTaskWaitingCounts = shgcService.getAuditTaskWaitingCount(shgcDto);
            List<String> shlbs = dbszDtos.stream().map(DbszDto::getShlb).collect(Collectors.toList());
            shgcDto.setShlbs(shlbs);
            shgcDto.setSortName("shgc.sqsj");
            shgcDto.setSortOrder("DESC");
            shgcDto.setPageSize(10);
            shgcDto.setPageNumber(1);
            rows = shgcService.getPagedAuditTaskWaitingList(shgcDto);
            totalNumber = shgcDto.getTotalNumber();
        }
        map.put("auditTaskWaitingCounts",auditTaskWaitingCounts);
        map.put("rows",rows);
        map.put("total",totalNumber);
        return map;
    }
    /*
        获取个人代办审核类别
     */
    private List<DbszDto> getPersonAuditTaskWaiting(DbszDto dbszDto) {
        Object persionAuditTaskWaiting = redisUtil.hget(RedisCommonKeyEnum.HOME_PAGE_AUDIT_TASK_WAITING.getKey(), dbszDto.getYhid()+dbszDto.getFbsbj());
        if (persionAuditTaskWaiting!=null){
            return JSON.parseArray(String.valueOf(persionAuditTaskWaiting), DbszDto.class);
        }
        List<DbszDto> dbszDtos = dbszService.getPersonDtoList(dbszDto);
        if (CollectionUtils.isEmpty(dbszDtos)){
            dbszDtos = new ArrayList<>();
        }
        redisUtil.hset(RedisCommonKeyEnum.HOME_PAGE_AUDIT_TASK_WAITING.getKey(),dbszDto.getYhid()+dbszDto.getFbsbj(),JSON.toJSONString(dbszDtos),-1);
        return dbszDtos;
    }

    /**
     *  获取工作任务统计
     */
    public Map<String,Object> getHomePageTaskStatis(SyszDto syszDto){
        GzglDto gzglDto = new GzglDto();
        gzglDto.setYhid(syszDto.getYhid());
        return gzglService.getHomePageTaskStatis(gzglDto);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean homePageSaveSetting(SyszDto syszDto) throws BusinessException {
        if (StringUtil.isNotBlank(syszDto.getSysz_json())){
            List<SyszDto> syszDtos = JSON.parseArray(syszDto.getSysz_json(), SyszDto.class);
            if (!CollectionUtils.isEmpty(syszDtos)){
                delete(syszDto);
                for (SyszDto dto : syszDtos) {
                    dto.setSyszid(StringUtil.generateUUID());
                    dto.setYhid(syszDto.getYhid());
                }
               boolean isSuccess = dao.insertSyszDtos(syszDtos);
               if (!isSuccess){
                   throw new BusinessException("msg","保存首页设置失败！");
               }
               redisUtil.hdel(RedisCommonKeyEnum.HOME_PAGE_PERSION_PAGE.getKey(),syszDto.getYhid());
            }
        }
        return true;
    }
}

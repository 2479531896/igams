package com.matridx.igams.common.service.impl;
import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.ShlbDto;
import com.matridx.igams.common.enums.RedisCommonKeyEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IShlbService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.DbszDto;
import com.matridx.igams.common.dao.entities.DbszModel;
import com.matridx.igams.common.dao.post.IDbszDao;
import com.matridx.igams.common.service.svcinterface.IDbszService;
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

@Service
public class DbszServiceImpl extends BaseBasicServiceImpl<DbszDto, DbszModel, IDbszDao> implements IDbszService{
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Autowired
    IShlbService shlbService;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public List<DbszDto> getPersonDtoList(DbszDto dbszDto) {
        return dao.getPersonDtoList(dbszDto);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> auditTaskWaitingSetting(DbszDto dbszDto) {
        Map<String, Object> map = new HashMap<>();
        List<DbszDto> Select = this.getPersonDtoList(dbszDto);
        map.put("Select",Select);
        List<ShlbDto> shlbDtos;
        if (StringUtil.isNotBlank(dbszDto.getFbsbj())){
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("access_token", dbszDto.getAccess_token());
            RestTemplate restTemplate = new RestTemplate();
            String shlbDtosStr = JSON.toJSONString(restTemplate.postForObject(applicationurl + dbszDto.getFbsbj() + "/homePage/homePage/pagedataGetAllAuditType", paramMap, List.class));
            shlbDtos = JSON.parseArray(shlbDtosStr, ShlbDto.class);
        }else {
            shlbDtos = this.getAllAuditType();
        }
        if (!CollectionUtils.isEmpty(shlbDtos)){
            if (!CollectionUtils.isEmpty(Select)){
                for (DbszDto dto : Select) {
                    for (ShlbDto shlbDto : shlbDtos) {
                        if (shlbDto.getShlb().equals(dto.getShlb())){
                            dto.setZylj(shlbDto.getZylj());
                            dto.setShlbbm(shlbDto.getShlbbm());
                        }
                    }
                    shlbDtos.removeIf(e -> e.getShlb().equals(dto.getShlb()));
                }
            }
            map.put("UnSelect",shlbDtos);
        }else {
            map.put("UnSelect",new ArrayList<>());
        }
        return map;
    }
    public List<ShlbDto> getAllAuditType(){
        return shlbService.getShlbForHomePage();
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean auditTaskWaitingSaveSetting(DbszDto dbszDto) throws BusinessException {
        delete(dbszDto);
        if (StringUtil.isNotBlank(dbszDto.getDbsz_json())){
            List<DbszDto> dbszDtos = JSON.parseArray(dbszDto.getDbsz_json(), DbszDto.class);
            if (!CollectionUtils.isEmpty(dbszDtos)){
                for (DbszDto dto : dbszDtos) {
                    dto.setDbszid(StringUtil.generateUUID());
                    dto.setYhid(dbszDto.getYhid());
                }
               boolean isSuccess = dao.insertDbszDtos(dbszDtos);
                if (!isSuccess){
                    throw new BusinessException("msg","保存代办设置失败！");
                }
            }
        }
        redisUtil.hdel(RedisCommonKeyEnum.HOME_PAGE_AUDIT_TASK_WAITING.getKey(),dbszDto.getYhid()+dbszDto.getFbsbj());
        return true;
    }

}

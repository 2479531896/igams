package com.matridx.igams.hrm.service.impl;
import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.enums.RedisCommonKeyEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.hrm.dao.entities.JpmxDto;
import com.matridx.igams.hrm.dao.entities.JpmxModel;
import com.matridx.igams.hrm.dao.post.IJpmxDao;
import com.matridx.igams.hrm.service.svcinterface.IJpmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JpmxServiceImpl extends BaseBasicServiceImpl<JpmxDto, JpmxModel, IJpmxDao> implements IJpmxService {
    @Autowired
    RedisUtil redisUtil;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public List<JpmxDto> getLotteryInfos(JpmxDto jpmxDto) {
        Object jpmxObj = redisUtil.get(RedisCommonKeyEnum.AWARD_JPMX.getKey()+jpmxDto.getJpglid());
        List<JpmxDto> jpmxDtos;
        if (jpmxObj!=null){
            jpmxDtos = JSON.parseArray(String.valueOf(jpmxObj),JpmxDto.class);
        }else {
            jpmxDtos = dao.getLotteryInfos(jpmxDto);
            if (!CollectionUtils.isEmpty(jpmxDtos)){
                DBEncrypt dbEncrypt = new DBEncrypt();
                for (JpmxDto dto : jpmxDtos) {
                    if (StringUtil.isNotBlank(dto.getFjid())&&StringUtil.isNotBlank(dto.getWjlj())){
                        redisUtil.hsetStoreImage(RedisCommonKeyEnum.REDIS_FILE.getKey(),dto.getFjid(),new File(dbEncrypt.dCode(dto.getWjlj())),jpmxDtos.get(0).getRqc()*24*60*60);
                        dto.setWjlj(applicationurl+urlPrefix+"/ws/file/pictureShowRedis?ywlx=IMP_AWARD_MANAGEMENT&fjid="+dto.getFjid());
                    }
                }
                //过期时间 结束日期-开始日期 加一天
                redisUtil.set(RedisCommonKeyEnum.AWARD_JPMX.getKey()+jpmxDto.getJpglid(),JSON.toJSONString(jpmxDtos),jpmxDtos.get(0).getRqc()*24*60*60);
            }
        }
        return jpmxDtos;
    }

    @Override
    public boolean updateSysl(JpmxDto jpmxDto) {
        return dao.updateSysl(jpmxDto);
    }
    @Override
    public boolean insertList(List<JpmxDto> list) {
        return dao.insertList(list);
    }

    @Override
    public boolean updateList(List<JpmxDto> list) {
        return dao.updateList(list);
    }
}

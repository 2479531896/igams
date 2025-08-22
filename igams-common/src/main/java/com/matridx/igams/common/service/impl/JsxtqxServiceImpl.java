package com.matridx.igams.common.service.impl;
import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JsxtqxDto;
import com.matridx.igams.common.dao.entities.JsxtqxModel;
import com.matridx.igams.common.dao.post.IJsxtqxDao;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IJsxtqxService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class JsxtqxServiceImpl extends BaseBasicServiceImpl<JsxtqxDto, JsxtqxModel, IJsxtqxDao> implements IJsxtqxService{
    @Autowired
    RedisUtil redisUtil;
    /**
     * 新增角色系统权限
     * @param jsxtqxDto
     * @return
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertJsxtqxDto(JsxtqxDto jsxtqxDto) {
        dao.deleteById(jsxtqxDto.getJsid());
        List<JsxtqxDto> list= new ArrayList<>();
        if(!CollectionUtils.isEmpty(jsxtqxDto.getJsxtqxDtos())) {
            for (int i = 0; i < jsxtqxDto.getJsxtqxDtos().size(); i++){
                if(StringUtil.isNotBlank(jsxtqxDto.getJsxtqxDtos().get(i).getXtid())) {
                    JsxtqxDto jsxtqxDto_add = new JsxtqxDto();
                    jsxtqxDto_add.setJsid(jsxtqxDto.getJsid());
                    jsxtqxDto_add.setXtid(jsxtqxDto.getJsxtqxDtos().get(i).getXtid());
                    //系统权限这边设置系统的参数代码
                    JcsjDto jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.SYSTEM_CODE.getCode(), jsxtqxDto.getJsxtqxDtos().get(i).getXtid());
                    if(jcsjDto!=null) {
                    	jsxtqxDto_add.setJsdm(jcsjDto.getCsdm());
                    }
                    jsxtqxDto_add.setJsmc(jsxtqxDto.getJsmc());
                    list.add(jsxtqxDto_add);
                }
            }
            dao.insertJsxtqx(list);
        }
        redisUtil.hset("Users_Xtqx",jsxtqxDto.getJsid(), JSON.toJSONString(list),-1);
        return true;
    }
}

package com.matridx.igams.hrm.service.impl;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.hrm.dao.entities.JkdaDto;
import com.matridx.igams.hrm.dao.entities.JkdaModel;
import com.matridx.igams.hrm.dao.entities.YghmcDto;
import com.matridx.igams.hrm.dao.post.IJkdaDao;
import com.matridx.igams.hrm.service.svcinterface.IJkdaService;
import com.matridx.igams.hrm.service.svcinterface.IYghmcService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author : 郭祥杰
 * @date :
 */
@Service
public class JkdaServiceImpl extends BaseBasicServiceImpl<JkdaDto, JkdaModel, IJkdaDao> implements IJkdaService, IFileImport {
    @Autowired
    IYghmcService yghmcService;
    private final Logger log = LoggerFactory.getLogger(JkdaServiceImpl.class);

    @Override
    public boolean existCheck(String fieldName, String value) {
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages){
        try{
            JkdaDto jkdaDto = (JkdaDto)baseModel;
            jkdaDto.setJkdaid(StringUtil.generateUUID());
            jkdaDto.setLrry(user.getYhid());
            YghmcDto yghmcDto = new YghmcDto();
            yghmcDto.setSfz(jkdaDto.getSfz());
            yghmcDto = yghmcService.getDto(yghmcDto);
            if(yghmcDto!=null){
                jkdaDto.setYghmcid(yghmcDto.getYghmcid());
                jkdaDto.setBm(yghmcDto.getJgmc());
                jkdaDto.setGwmc(yghmcDto.getGwmc());
            }else{
                throw new BusinessException("未找到用户！身份证号："+jkdaDto.getSfz());
            }
            dao.insert(jkdaDto);
        }catch(Exception e){
            log.error(e.getMessage());
        }
        return true;
    }

    @Override
    public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
        return null;
    }

    @Override
    public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
        return false;
    }

    @Override
    public boolean checkDefined(List<Map<String, String>> defined) {
        return true;
    }
}

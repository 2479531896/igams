package com.matridx.igams.wechat.service.impl;


import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.wechat.dao.entities.WzzkDto;
import com.matridx.igams.wechat.dao.entities.WzzkModel;
import com.matridx.igams.wechat.dao.post.IWzzkDao;
import com.matridx.igams.wechat.service.svcinterface.IWzzkService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WzzkServiceImpl extends BaseBasicServiceImpl<WzzkDto, WzzkModel, IWzzkDao> implements IWzzkService, IFileImport {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertOrUpdateList(List<WzzkDto> list) {
        return dao.insertOrUpdateList(list);
    }

    @Override
    public List<WzzkDto> getNcStatistics(WzzkDto wzzkDto) {
        return dao.getNcStatistics(wzzkDto);
    }

    @Override
    public List<WzzkDto> getNcPcTable(WzzkDto wzzkDto) {
        return dao.getNcPcTable(wzzkDto);
    }

    @Override
    public List<WzzkDto> getPcStatistics(WzzkDto wzzkDto) {
        return dao.getPcStatistics(wzzkDto);
    }


    @Override
    public boolean existCheck(String fieldName, String value) {
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages) throws BusinessException {

        List<JcsjDto> jcxmList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
        WzzkDto wzzkDto=(WzzkDto)baseModel;
        String jcdwmc=wzzkDto.getJcdw();
        Optional<JcsjDto> optional=jcxmList.stream().filter(e->e.getCsmc().equals(jcdwmc)).findFirst();
        List<WzzkDto>wzzkDtoList=new ArrayList<>();
        if (optional.isPresent()){
            wzzkDto.setZkid(StringUtil.generateUUID());
            wzzkDto.setJcdwid(optional.get().getCsid());
            wzzkDto.setScbj("0");
            wzzkDtoList.add(wzzkDto);
            dao.insertOrUpdateList(wzzkDtoList);
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

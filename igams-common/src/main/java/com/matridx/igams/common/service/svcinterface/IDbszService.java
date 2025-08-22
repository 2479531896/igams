package com.matridx.igams.common.service.svcinterface;
import com.matridx.igams.common.dao.entities.ShlbDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.DbszDto;
import com.matridx.igams.common.dao.entities.DbszModel;
import java.util.List;
import java.util.Map;

public interface IDbszService extends BaseBasicService<DbszDto, DbszModel>{
    /*
        获取个人代办设置
     */
    List<DbszDto> getPersonDtoList(DbszDto dbszDto);
    /*
       获取代办设置页面数据
    */
    Map<String, Object> auditTaskWaitingSetting(DbszDto dbszDto);
    /*
        代办设置保存
     */
    boolean auditTaskWaitingSaveSetting(DbszDto dbszDto) throws BusinessException;
    /*
        获取所有审核类别
     */
    List<ShlbDto> getAllAuditType();
}

package com.matridx.igams.warehouse.service.impl;

import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;

import com.matridx.igams.production.dao.matridxsql.Fa_ObjectsDao;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.warehouse.dao.entities.GdzcglDto;
import com.matridx.igams.warehouse.dao.entities.GdzcglModel;
import com.matridx.igams.warehouse.dao.post.IGdzcglDao;
import com.matridx.igams.warehouse.service.svcinterface.IGdzcglService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class GdzcglServiceImpl extends BaseBasicServiceImpl<GdzcglDto, GdzcglModel, IGdzcglDao> implements IGdzcglService {

    @Autowired
    Fa_ObjectsDao fa_objectsDao;
    @Autowired
    IRdRecordService rdRecordService;
    private final Logger log = LoggerFactory.getLogger(GdzcglServiceImpl.class);
    /**
     * 新增
     * @param gdzcglDto
     
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertDto(GdzcglDto gdzcglDto) {
        gdzcglDto.setGdzcid(StringUtil.generateUUID());
        boolean isSuccess = insert(gdzcglDto);
        try {
            isSuccess =rdRecordService.addU8Asset(gdzcglDto);
        } catch (BusinessException e) {
            log.error(e.getMessage());
        }
        return isSuccess;
    }

    /**
     * 选中导出
     *
     * @param params
     
     */
    public List<GdzcglDto> getListForSelectExp(Map<String, Object> params)
    {
        GdzcglDto gdzcglDto = (GdzcglDto) params.get("entryData");
        queryJoinFlagExport(params, gdzcglDto);
        return dao.getListForSelectExp(gdzcglDto);
    }

    /**
     * 根据搜索条件获取导出条数
     *
     * @param gdzcglDto
     
     */
    public int getCountForSearchExp(GdzcglDto gdzcglDto,Map<String, Object> params) {
        return dao.getCountForSearchExp(gdzcglDto);
    }

    /**
     * 根据搜索条件分页获取导出信息
     * @param params
     
     */
    public List<GdzcglDto> getListForSearchExp(Map<String,Object> params){
        GdzcglDto gdzcglDto = (GdzcglDto)params.get("entryData");
        queryJoinFlagExport(params,gdzcglDto);
        return dao.getListForSearchExp(gdzcglDto);
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, GdzcglDto gdzcglDto) {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
        for (DcszDto dcszDto : choseList) {
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        String sqlcs = sqlParam.toString();
        gdzcglDto.setSqlParam(sqlcs);
    }

    /**
     * 删除
     * @param gdzcglDto
     
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean deleteDto(GdzcglDto gdzcglDto){
        return delete(gdzcglDto);
    }
}

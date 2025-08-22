package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.SbpdmxDto;
import com.matridx.igams.production.dao.entities.SbpdmxModel;
import com.matridx.igams.production.dao.post.ISbpdmxDao;
import com.matridx.igams.production.service.svcinterface.ISbpdmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SbpdmxServiceImpl extends BaseBasicServiceImpl<SbpdmxDto, SbpdmxModel, ISbpdmxDao> implements ISbpdmxService {

    @Override
    public boolean insertList(List<SbpdmxDto> list) {
        return dao.insertList(list);
    }

    @Override
    public boolean updateList(List<SbpdmxDto> list) {
        return dao.updateList(list);
    }
    /**
     * 根据搜索条件获取导出条数
     */
    public int getCountForSearchExp(SbpdmxDto sbpdmxDto, Map<String, Object> params){
        return dao.getCountForSearchExp(sbpdmxDto);
    }

    /**
     * 根据搜索条件分页获取导出信息
     */
    public List<SbpdmxDto> getListForSearchExp(Map<String,Object> params){
        SbpdmxDto sbpdmxDto = (SbpdmxDto)params.get("entryData");
        queryJoinFlagExport(params,sbpdmxDto);
        return dao.getListForSearchExp(sbpdmxDto);
    }

    /**
     * 根据选择信息获取导出信息
     */
    public List<SbpdmxDto> getListForSelectExp(Map<String,Object> params){
        SbpdmxDto sbpdmxDto = (SbpdmxDto)params.get("entryData");
        queryJoinFlagExport(params,sbpdmxDto);
        return dao.getListForSelectExp(sbpdmxDto);
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, SbpdmxDto sbpdmxDto) {
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
        sbpdmxDto.setSqlParam(sqlcs);
    }
}

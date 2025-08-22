package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.DkjejlDto;
import com.matridx.igams.storehouse.dao.entities.DkjejlModel;
import com.matridx.igams.storehouse.dao.entities.XsglDto;
import com.matridx.igams.storehouse.dao.entities.XsmxDto;
import com.matridx.igams.storehouse.dao.post.IDkjejlDao;
import com.matridx.igams.storehouse.service.svcinterface.IDkjejlService;
import com.matridx.igams.storehouse.service.svcinterface.IXsglService;
import com.matridx.igams.storehouse.service.svcinterface.IXsmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:JYK
 */
@Service
public class DkjejlServiceImpl extends BaseBasicServiceImpl<DkjejlDto, DkjejlModel, IDkjejlDao> implements IDkjejlService {
    @Autowired
    IXsmxService xsmxService;
    @Autowired
    IXsglService xsglService;
    /**
     * 到款记录保存
     * @param dkjejlDto
     * @param user
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean ArriveMoneySaveMoneyHos(DkjejlDto dkjejlDto, User user) throws BusinessException {
        boolean isSuccess  = false;
        long dkyL = 0L;
        String dky = null;
        List<DkjejlDto> dkjejlDtos = JSON.parseArray(dkjejlDto.getDkjlmx_json(), DkjejlDto.class);
        if (!CollectionUtils.isEmpty(dkjejlDtos)){
            List<DkjejlDto> addDkjejlDtos = new ArrayList<>();
            DkjejlDto dkjejlDto_sel = new DkjejlDto();
            dkjejlDto_sel.setYwid(dkjejlDto.getYwid());
            List<DkjejlDto> dtoList = dao.getDtoList(dkjejlDto);
            if (!CollectionUtils.isEmpty(dtoList)){
                for (DkjejlDto dto : dkjejlDtos) {
                    //剩下是要删除的
                    dtoList.removeIf(e->e.getDkjlid().equals(dto.getDkjlid()));
                }
            }
            for (DkjejlDto dto : dkjejlDtos) {
                //新增的
                if (StringUtil.isBlank(dto.getDkjlid())){
                    dto.setDkjlid(StringUtil.generateUUID());
                    dto.setLrry(user.getYhid());
                    addDkjejlDtos.add(dto);
                }
                if (StringUtil.isBlank(dto.getFywid())){
                    dto.setFywid(null);
                }
                if (StringUtil.isNotBlank(dto.getDky())&&DateUtils.parseDate("yyyy-MM-dd",dto.getDky()).getTime()>dkyL){
                    dky = dto.getDky();
                }
            }
            if (!CollectionUtils.isEmpty(addDkjejlDtos)){
                isSuccess = dao.insertList(addDkjejlDtos);
                if (!isSuccess){
                    throw new BusinessException("msg","新增到款记录失败！");
                }
            }
            if (!CollectionUtils.isEmpty(dtoList)){
                List<String> ids = dtoList.stream().map(DkjejlModel::getDkjlid).distinct().collect(Collectors.toList());
                DkjejlDto dkjejlDto_del = new DkjejlDto();
                dkjejlDto_del.setIds(ids);
                dkjejlDto_del.setScry(user.getYhid());
                isSuccess = delete(dkjejlDto_del);
                if (!isSuccess){
                    throw new BusinessException("msg","删除到款记录失败！");
                }
            }
        }else {
            DkjejlDto dkjejlDto_del = new DkjejlDto();
            dkjejlDto_del.setYwid(dkjejlDto.getYwid());
            dkjejlDto_del.setFywid(dkjejlDto.getFywid());
            dkjejlDto_del.setScry(user.getYhid());
            dao.deleteByYwid(dkjejlDto_del);
        }
        String xsmxid;
        //获取到款总金额维护进销售的 dkje 和 ysk
        DkjejlDto dkjejlDto_sel = new DkjejlDto();
        //如果为销售到款维护
        if ("0".equals(dkjejlDto.getYwlx())){
            xsmxid = dkjejlDto.getYwid();
        }else {
            xsmxid = dkjejlDto.getFywid();
        }
        dkjejlDto_sel.setIds(xsmxid);
        //获取该销售明细的总到款金额
        DkjejlDto dto = dao.getAllDkje(dkjejlDto_sel);
        //获取该销售单的总到款金额
        XsmxDto xsmxDto_all = new XsmxDto();
        xsmxDto_all.setXsid(dkjejlDto.getXsid());
        List<XsmxDto> xsmxDtos = xsmxService.getXsmxByXs(xsmxDto_all);
        List<String> xsmxids = xsmxDtos.stream().map(XsmxDto::getXsmxid).collect(Collectors.toList());
        DkjejlDto dkjeDto_all = new DkjejlDto();
        dkjeDto_all.setIds(xsmxids);
        //销售单的总到款金额
        DkjejlDto dto_all = dao.getAllDkje(dkjeDto_all);
        XsglDto xsglDto_up = new XsglDto();
        xsglDto_up.setXsid(dkjejlDto.getXsid());
        xsglDto_up.setYsk(dto_all.getDkje());
        isSuccess = xsglService.updateYsk(xsglDto_up);
        if (!isSuccess){
            throw new BusinessException("msg","修改销售单到款信息失败！");
        }
        XsmxDto xsmxDto_up = new XsmxDto();
        xsmxDto_up.setXsmxid(xsmxid);
        xsmxDto_up.setDkje(dto.getDkje());
        xsmxDto_up.setDky(dky);
        isSuccess = xsmxService.updateDkxx(xsmxDto_up);
        if (!isSuccess){
            throw new BusinessException("msg","修改销售明细到款信息失败！");
        }
        return true;
    }

    /**
     * 到款列表选中导出
     * @param params
     * @return
     */
    public List<DkjejlDto> getListForSelectExp(Map<String, Object> params){
        DkjejlDto dkjejlDto = (DkjejlDto) params.get("entryData");
        queryJoinFlagExport(params,dkjejlDto);
        return dao.getListForSelectExp(dkjejlDto);
    }

    /**
     * 到款列表根据搜索条件获取导出条数
     * @return
     */
    public int getCountForSearchExp(DkjejlDto dkjejlDto,Map<String,Object> params){
        return dao.getCountForSearchExp(dkjejlDto);
    }

    /**
     * 到款列表根据搜索条件分页获取导出信息
     * @return
     */
    public List<DkjejlDto> getListForSearchExp(Map<String,Object> params){
        DkjejlDto dkjejlDto = (DkjejlDto)params.get("entryData");
        queryJoinFlagExport(params,dkjejlDto);
        return dao.getListForSearchExp(dkjejlDto);
    }
    /**
     * 未到款列表选中导出
     * @param params
     * @return
     */
    public List<DkjejlDto> getNotListForSelectExp(Map<String, Object> params){
        DkjejlDto dkjejlDto = (DkjejlDto) params.get("entryData");
        queryJoinFlagExport(params,dkjejlDto);
        return dao.getNotListForSelectExp(dkjejlDto);
    }

    /**
     * 未到款列表根据搜索条件获取导出条数
     * @return
     */
    public int getNotCountForSearchExp(DkjejlDto dkjejlDto,Map<String,Object> params){
        return dao.getNotCountForSearchExp(dkjejlDto);
    }

    /**
     * 未到款列表根据搜索条件分页获取导出信息
     * @return
     */
    public List<DkjejlDto> getNotListForSearchExp(Map<String,Object> params){
        DkjejlDto dkjejlDto = (DkjejlDto)params.get("entryData");
        queryJoinFlagExport(params,dkjejlDto);
        return dao.getNotListForSearchExp(dkjejlDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, DkjejlDto dkjejlDto){
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
        for (DcszDto dcszDto : choseList){
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;
            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd()))
            {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        dkjejlDto.setSqlParam(sqlParam.toString());
    }
    @Override
    public void insertList(List<DkjejlDto> list) {
        dao.insertList(list);
    }

    @Override
    public void deleteByYwids(DkjejlDto dkjejlDto) {
        dao.deleteByYwids(dkjejlDto);
    }

    @Override
    public List<DkjejlDto> getPagedNotDtoList(DkjejlDto dkjejlDto) {
        return dao.getPagedNotDtoList(dkjejlDto);
    }

    @Override
    public DkjejlDto getNotPaymentReceivedDto(DkjejlDto dkjejlDto) {
        return dao.getNotPaymentReceivedDto(dkjejlDto);
    }
}

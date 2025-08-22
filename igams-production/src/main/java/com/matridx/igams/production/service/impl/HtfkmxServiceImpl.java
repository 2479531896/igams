package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.HtfkmxDto;
import com.matridx.igams.production.dao.entities.HtfkmxModel;
import com.matridx.igams.production.dao.post.IHtfkmxDao;
import com.matridx.igams.production.service.svcinterface.IHtfkmxService;

import java.util.List;
import java.util.Map;

@Service
public class HtfkmxServiceImpl extends BaseBasicServiceImpl<HtfkmxDto, HtfkmxModel, IHtfkmxDao> implements IHtfkmxService{

    @Autowired
    IShgcService shgcService;
    private final Logger log = LoggerFactory.getLogger(HtfkmxServiceImpl.class);

    /**
     * 合同付款列表（查询审核状态）
     */
    @Override
    public List<HtfkmxDto> getPagedDtoList(HtfkmxDto htfkmxDto) {
        List<HtfkmxDto> list = dao.getPagedDtoList(htfkmxDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_CONTRACT_PAYMENT.getCode(), "zt", "htfkid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    /**
     * 获取相关合同付款情况
     */
    public List<HtfkmxDto> getHtfkqkMessage(List<HtfkmxDto> list){
        return dao.getHtfkqkMessage(list);
    }

    /**
     * 新增合同付款明细
     */
    public boolean insertDtoList(List<HtfkmxDto> list){
        return dao.insertDtoList(list);
    }
    /**
     * 批量更新明细
     */
    public int updateList(List<HtfkmxDto> htfkmxDtos){
        return dao.updateList(htfkmxDtos);
    }

    /**
     * 根据合同ID获取合同付款信息
     */
    public List<HtfkmxDto> getListByHtid(String htid){
        return dao.getListByHtid(htid);
    }

    /**
     * 更新合同付款中金额信息
     */
    public boolean updateContractFkzje(List<HtfkmxDto> htfkmxDtos){
        return dao.updateContractFkzje(htfkmxDtos);
    }

    @Override
    public int getCountForSearchExp(HtfkmxDto htfkmxDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(htfkmxDto);
    }

    @Override
    public List<HtfkmxDto> queryHtddslid(HtfkmxDto htfkmxDto) {
        return dao.queryHtddslid(htfkmxDto);
    }

    @Override
    public List<HtfkmxDto> getListGroupByHt(HtfkmxDto htfkmxDto) {
        return dao.getListGroupByHt(htfkmxDto);
    }


    /**
     * 根据搜索条件获取导出信息
     */
    public List<HtfkmxDto> getListForSearchExp(Map<String, Object> params) {
        HtfkmxDto htfkmxDto = (HtfkmxDto) params.get("entryData");
        queryJoinFlagExport(params, htfkmxDto);
        return dao.getListForSearchExp(htfkmxDto);
    }

    /**
     * 根据选择信息获取导出信息
     */
    public List<HtfkmxDto> getListForSelectExp(Map<String, Object> params) {
        HtfkmxDto htfkmxDto = (HtfkmxDto) params.get("entryData");
        queryJoinFlagExport(params, htfkmxDto);
        return dao.getListForSelectExp(htfkmxDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, HtfkmxDto htfkmxDto) {
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
        htfkmxDto.setSqlParam(sqlcs);
    }
}

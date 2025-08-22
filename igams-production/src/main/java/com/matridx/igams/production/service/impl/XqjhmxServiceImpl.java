package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.XqjhmxDto;
import com.matridx.igams.production.dao.entities.XqjhmxModel;
import com.matridx.igams.production.dao.post.IXqjhmxDao;
import com.matridx.igams.production.service.svcinterface.IXqjhmxService;
import com.matridx.igams.storehouse.dao.entities.XsmxDto;
import com.matridx.igams.storehouse.dao.post.IXsmxDao;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class XqjhmxServiceImpl extends BaseBasicServiceImpl<XqjhmxDto, XqjhmxModel, IXqjhmxDao> implements IXqjhmxService {
    @Autowired
    private IXsmxDao xsmxDao;
    @Override
    public Boolean insertList(List<XqjhmxDto> xqjhmxDtoList) {
        return dao.insertList(xqjhmxDtoList)!=0;
    }

    @Override
    public Boolean updateList(List<XqjhmxDto> xqjhmxDtoList) {
        return dao.updateList(xqjhmxDtoList)!=0;
    }
    @Override
    public Boolean updateDtoList(List<XqjhmxDto> xqjhmxDtoList) {
        return dao.updateDtoList(xqjhmxDtoList)!=0;
    }

    @Override
    public boolean deleteByCpxqids(XqjhmxDto xqjhmxDto) {
        return dao.deleteByCpxqids(xqjhmxDto)!=0;
    }
    /**
     * 修改生产状态
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean modSaveChoose(XqjhmxDto xqjhmxDto) throws BusinessException {
        List<XqjhmxDto> xqjhmxDtoList=new ArrayList<>();
        List<XsmxDto> xsmxDtoList=new ArrayList<>();
        if (!CollectionUtils.isEmpty(xqjhmxDto.getIds())){
            for (int i=0;i<xqjhmxDto.getLxs().size();i++){
                if ("xs".equals(xqjhmxDto.getLxs().get(i))){
                    XsmxDto xsmxDto=new XsmxDto();
                    xsmxDto.setXsmxid(xqjhmxDto.getIds().get(i));
                    if ("0".equals(xqjhmxDto.getSczts().get(i))){
                        xsmxDto.setSczt("1");
                    }else {
                        xsmxDto.setSczt("0");
                    }
                    xsmxDto.setXgry(xqjhmxDto.getXgry());
                    xsmxDtoList.add(xsmxDto);
                }else {
                    XqjhmxDto xqjhmxDto1=new XqjhmxDto();
                    xqjhmxDto1.setXqjhmxid(xqjhmxDto.getIds().get(i));
                    if ("0".equals(xqjhmxDto.getSczts().get(i))){
                        xqjhmxDto1.setSfsc("1");
                    }else {
                        xqjhmxDto1.setSfsc("0");
                    }
                    xqjhmxDto1.setXgry(xqjhmxDto1.getXgry());
                    xqjhmxDtoList.add(xqjhmxDto1);

                }
            }
        }
        if (!CollectionUtils.isEmpty(xqjhmxDtoList)){
            if (!dao.updateListSczt(xqjhmxDtoList))
                throw new BusinessException("msg","修改需求明细生产状态失败!");
        }
        if (!CollectionUtils.isEmpty(xsmxDtoList)){
            if (!xsmxDao.updateListSczt(xsmxDtoList))
                throw new BusinessException("msg","修改销售明细生产状态失败!");

        }
        return true;
    }

    @Override
    public List<XqjhmxDto> getPagedACMaterials(XqjhmxDto xqjhmxDto) {
        return dao.getPagedACMaterials(xqjhmxDto);
    }

    @Override
    public XqjhmxDto getACMaterialDto(XqjhmxDto xqjhmxDto) {
        return dao.getACMaterialDto(xqjhmxDto);
    }

    @Override
    public List<XqjhmxDto> getDtoListById(XqjhmxDto xqjhmxDto) {
        return dao.getDtoListById(xqjhmxDto);
    }

    /**
     * 导出
     */
    public int getCountForSearchExp(XqjhmxDto xqjhmxDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(xqjhmxDto);
    }

    /**
     * 根据搜索条件获取导出信息
     */
    public List<XqjhmxDto> getListForSearchExp(Map<String, Object> params) {
        XqjhmxDto xqjhmxDto = (XqjhmxDto) params.get("entryData");
        queryJoinFlagExport(params, xqjhmxDto);
        return dao.getListForSearchExp(xqjhmxDto);
    }

    /**
     * 根据选择信息获取导出信息
     */
    public List<XqjhmxDto> getListForSelectExp(Map<String, Object> params) {
        XqjhmxDto xqjhmxDto = (XqjhmxDto) params.get("entryData");
        queryJoinFlagExport(params, xqjhmxDto);
        return dao.getListForSelectExp(xqjhmxDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, XqjhmxDto xqjhmxDto) {
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
        xqjhmxDto.setSqlParam(sqlcs);
    }

}

package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.service.svcinterface.IQgglService;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.XzrkmxDto;
import com.matridx.igams.storehouse.dao.entities.XzrkmxModel;
import com.matridx.igams.storehouse.dao.post.IXzrkmxDao;
import com.matridx.igams.storehouse.service.svcinterface.IXzrkmxService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class XzrkmxServiceImpl extends BaseBasicServiceImpl<XzrkmxDto, XzrkmxModel, IXzrkmxDao> implements IXzrkmxService{

    @Autowired
    private IQgglService qgglService;
    @Autowired
    private IQgmxService qgmxService;
    /**
     * 库存列表
     * @param
     * @return
     */
    @Override
    public List<XzrkmxDto> getPagedDtoAdministrationStockList(XzrkmxDto xzrkmxDto) {
        return dao.getPagedDtoAdministrationStockList(xzrkmxDto);
    }

    /**
     * 行政库存 基本信息
     *
     * @param xzrkmxDto
     * @return
     */
    @Override
    public XzrkmxDto getJbxxByXzrkmxid(XzrkmxDto xzrkmxDto) {
        return dao.getJbxxByXzrkmxid(xzrkmxDto);
    }

    /**
     * 修改行政入库明细表
     * @param xzrkmxDto
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateAdministrationStockByXzrkmxid(XzrkmxDto xzrkmxDto) {
        return dao.updateAdministrationStockByXzrkmxid(xzrkmxDto);
    }

    /**
     * 根据xzrkmxid获取qgmxids
     * @param xzrkmxDto
     * @return
     */
    public List<XzrkmxDto> getQgmxidsByXzrkmxids(XzrkmxDto xzrkmxDto){
        return dao.getQgmxidsByXzrkmxids(xzrkmxDto);
    }

    /**
     * 删除行政入库明细表
     * @param xzrkmxDto
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delAdministrationStockByXzrkmxid(XzrkmxDto xzrkmxDto) {
        List<XzrkmxDto> qgmxidsByXzrkmxids = dao.getQgmxidsByXzrkmxids(xzrkmxDto);
        List<String> qgmxids = new ArrayList<>();
        List<String> qgids = new ArrayList<>();
        for (XzrkmxDto qgmxidsByXzrkmxid : qgmxidsByXzrkmxids) {
            qgmxids.add(qgmxidsByXzrkmxid.getQgmxid());
        }
        QgmxDto qgmxDto = new QgmxDto();
        qgmxDto.setXgry(xzrkmxDto.getScry());
        qgmxDto.setIds(qgmxids);
        List<QgmxDto> qgidsByQgmxids = qgmxService.getQgidsByQgmxids(qgmxDto);
        for (QgmxDto qgidsByQgmxid : qgidsByQgmxids) {
            qgids.add(qgidsByQgmxid.getQgid());
        }
        boolean isSuccess = dao.delAdministrationStockByXzrkmxid(xzrkmxDto);
        if (isSuccess){
            isSuccess = qgmxService.delRkslByQgmxid(qgmxDto);
            if (isSuccess) {
                QgglDto qgglDto = new QgglDto();
                qgglDto.setXgry(xzrkmxDto.getScry());
                qgglDto.setIds(qgids);
                qgglDto.setWcbj("0");
                isSuccess = qgglService.updateWcbjs(qgglDto);
            }
        }
        return isSuccess;
    }

    @Override
    public boolean insertList(List<XzrkmxDto> xzrkmxDtos) {
        return dao.insertList(xzrkmxDtos);
    }

    @Override
    public XzrkmxDto getrksl(String qgmxid) {
        return dao.getrksl(qgmxid);
    }

    /**
     * 获取行政库存数据
     * @param xzrkid
     * @return
     */
    public List<XzrkmxDto> getXzkcList(String xzrkid){
        return dao.getXzkcList(xzrkid);
    }

    @Override
    public List<XzrkmxDto> getDtoListByXzrkid(String xzrkid) {
        return dao.getDtoListByXzrkid(xzrkid);
    }
    /**
     * 批量更新
     * @param list
     * @return
     */
    public boolean updateList(List<XzrkmxDto> list){
        return dao.updateList(list);
    }

    @Override
    public List<XzrkmxDto> getDtoListByDto(XzrkmxDto xzrkmxDto) {
        return dao.getDtoListByDto(xzrkmxDto);
    }
}

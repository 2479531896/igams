package com.matridx.igams.storehouse.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.XzqgqrmxDto;
import com.matridx.igams.storehouse.dao.entities.XzqgqrmxModel;
import com.matridx.igams.storehouse.dao.post.IXzqgqrmxDao;
import com.matridx.igams.storehouse.service.svcinterface.IXzqgqrmxService;

import java.util.List;

@Service
public class XzqgqrmxServiceImpl extends BaseBasicServiceImpl<XzqgqrmxDto, XzqgqrmxModel, IXzqgqrmxDao> implements IXzqgqrmxService{

    /**
     * 保存确认明细
     * @param list
     * @return
     */
    public boolean insertDtoList(List<XzqgqrmxDto> list){
        return dao.insertDtoList(list);
    }

    /**
     * 通过qrid删除确认明细
     * @param xzqgqrmxDto
     * @return
     */
    public boolean delDtoByQrid(XzqgqrmxDto xzqgqrmxDto){
        return dao.delDtoByQrid(xzqgqrmxDto);
    }

    /**
     * 删除行政请购确认明细
     *
     * @param xzqgqrmxDtos
     */
    public void delByList(List<XzqgqrmxDto> xzqgqrmxDtos){
        dao.delByList(xzqgqrmxDtos);
    }

    /**
     * 批量更新确认明细
     *
     * @param xzqgqrmxDtos
     */
    public void updateDtoList(List<XzqgqrmxDto> xzqgqrmxDtos){
        dao.updateDtoList(xzqgqrmxDtos);
    }

    /**
     * 获取需要更新确认的请购信息
     * @param xzqgqrmxDto
     * @return
     */
    public List<XzqgqrmxDto> getNeedQrxx(XzqgqrmxDto xzqgqrmxDto){
        return dao.getNeedQrxx(xzqgqrmxDto);
    }

    /**
     * 更新请购单是否确认为1
     * @param xzqgqrmxDtos
     * @return
     */
    public boolean modQgglSfqr(List<XzqgqrmxDto> xzqgqrmxDtos){
        return dao.modQgglSfqr(xzqgqrmxDtos);
    }

    /**
     * 获取需要付款的确认明细信息
     * @param xzqgqrmxDto
     * @return
     */
    public List<XzqgqrmxDto> getNeedFkData(XzqgqrmxDto xzqgqrmxDto){
        return dao.getNeedFkData(xzqgqrmxDto);
    }

    /**
     * 获取付款完成的确认单
     * @param xzqgqrmxDto
     * @return
     */
    public List<XzqgqrmxDto> getFkwcDtoList(XzqgqrmxDto xzqgqrmxDto){
        return dao.getFkwcDtoList(xzqgqrmxDto);
    }

    /**
     * 更新请购明细是否入库信息
     *
     * @param xzqgqrmxDtos
     */
    public void modQgmxsfrk(List<XzqgqrmxDto> xzqgqrmxDtos){
        dao.modQgmxsfrk(xzqgqrmxDtos);
    }

    @Override
    public List<XzqgqrmxDto> getMxlistByQrid(XzqgqrmxDto xzqgqrmxDto) {
        return dao.getMxlistByQrid(xzqgqrmxDto);
    }

    @Override
    public void updateBatchFkbj(List<XzqgqrmxDto> xzqgqrxmlist) {
        dao.updateBatchFkbj(xzqgqrxmlist);
    }

    @Override
    public List<XzqgqrmxDto> getNeedXzqgQrxx(XzqgqrmxDto xzqgqrmxDto) {
        return dao.getNeedXzqgQrxx(xzqgqrmxDto);
    }

    @Override
    public List<String> getFkwcQgidList(List<XzqgqrmxDto> qrmxlist) {
        return dao.getFkwcQgidList(qrmxlist);
    }
}

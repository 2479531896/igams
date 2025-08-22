package com.matridx.igams.storehouse.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.XzqgfkmxDto;
import com.matridx.igams.storehouse.dao.entities.XzqgfkmxModel;
import com.matridx.igams.storehouse.dao.post.IXzqgfkmxDao;
import com.matridx.igams.storehouse.service.svcinterface.IXzqgfkmxService;

import java.util.List;

@Service
public class XzqgfkmxServiceImpl extends BaseBasicServiceImpl<XzqgfkmxDto, XzqgfkmxModel, IXzqgfkmxDao> implements IXzqgfkmxService{

    /**
     * 新增行政请购付款明细
     * @param list
     * @return
     */
    public boolean insertDtoList(List<XzqgfkmxDto> list){
        return dao.insertDtoList(list);
    }

    /**
     * 通过付款ID获取行政请购付款明细数据
     * @param fkid
     * @return
     */
    @Override
    public List<XzqgfkmxDto> getListByFkid(String fkid) {
        return dao.getListByFkid(fkid);
    }

    /**
     * 批量更新付款明细数据
     * @param xzqgfkmxlist
     * @return
     */
    @Override
    public boolean updateBatch(List<XzqgfkmxDto> xzqgfkmxlist) {
        return dao.updateBatch(xzqgfkmxlist);
    }

    @Override
    public void updateBatchFkbj(List<XzqgfkmxDto> xzqgfkmxList) {
        dao.updateBatchFkbj(xzqgfkmxList);
    }

    /**
     * 删除行政请购付款明细
     * @param list
     * @return
     */
    public boolean delDtoList(List<XzqgfkmxDto> list){
        return dao.delDtoList(list);
    }

    /**
     * 获取需要更新付款的请购信息
     * @param xzqgfkmxDto
     * @return
     */
    public List<XzqgfkmxDto> getNeedFkxx(XzqgfkmxDto xzqgfkmxDto){
        return dao.getNeedFkxx(xzqgfkmxDto);
    }

    /**
     * 更新请购信息付款标记
     *
     * @param list
     */
    public void modQgglSffk(List<XzqgfkmxDto> list){
        dao.modQgglSffk(list);
    }
}

package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.XzqgfkmxDto;
import com.matridx.igams.storehouse.dao.entities.XzqgfkmxModel;

import java.util.List;

public interface IXzqgfkmxService extends BaseBasicService<XzqgfkmxDto, XzqgfkmxModel>{

    /**
     * 新增行政请购付款明细
     * @param list
     * @return
     */
    boolean insertDtoList(List<XzqgfkmxDto> list);

    /**
     * 通过付款ID获取行政请购付款明细数据
     * @param fkid
     * @return
     */
    List<XzqgfkmxDto> getListByFkid(String fkid);

    /**
     * 批量更新付款明细数据
     * @param xzqgfkmxlist
     * @return
     */
    boolean updateBatch(List<XzqgfkmxDto> xzqgfkmxlist);

    /**
     * 批量更新付款明细数据付款标记
     * @param xzqgfkmxList
     */
    void updateBatchFkbj(List<XzqgfkmxDto> xzqgfkmxList);

    /**
     * 删除行政请购付款明细
     * @param list
     * @return
     */
    boolean delDtoList(List<XzqgfkmxDto> list);

    /**
     * 获取需要更新付款的请购信息
     * @param xzqgfkmxDto
     * @return
     */
    List<XzqgfkmxDto> getNeedFkxx(XzqgfkmxDto xzqgfkmxDto);

    /**
     * 更新请购信息付款标记
     *
     * @param list
     */
    void modQgglSffk(List<XzqgfkmxDto> list);
}

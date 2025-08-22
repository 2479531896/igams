package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import com.matridx.igams.storehouse.dao.entities.XzqgqrmxDto;
import com.matridx.igams.storehouse.dao.entities.XzqgqrmxModel;

import java.util.List;

public interface IXzqgqrmxService extends BaseBasicService<XzqgqrmxDto, XzqgqrmxModel>{
    /**
     * 保存确认明细
     * @param list
     * @return
     */
    boolean insertDtoList(List<XzqgqrmxDto> list);

    /**
     * 通过qrid删除确认明细
     * @param xzqgqrmxDto
     * @return
     */
    boolean delDtoByQrid(XzqgqrmxDto xzqgqrmxDto);

    /**
     * 删除行政请购确认明细
     *
     * @param xzqgqrmxDtos
     */
    void delByList(List<XzqgqrmxDto> xzqgqrmxDtos);

    /**
     * 批量更新确认明细
     *
     * @param xzqgqrmxDtos
     */
    void updateDtoList(List<XzqgqrmxDto> xzqgqrmxDtos);

    /**
     * 获取需要更新确认的请购信息
     * @param xzqgqrmxDto
     * @return
     */
    List<XzqgqrmxDto> getNeedQrxx(XzqgqrmxDto xzqgqrmxDto);

    /**
     * 更新请购单是否确认为1
     * @param xzqgqrmxDtos
     * @return
     */
    boolean modQgglSfqr(List<XzqgqrmxDto> xzqgqrmxDtos);

    /**
     * 获取需要付款的确认明细信息
     * @param xzqgqrmxDto
     * @return
     */
    List<XzqgqrmxDto> getNeedFkData(XzqgqrmxDto xzqgqrmxDto);

    /**
     * 获取付款完成的确认单
     * @param xzqgqrmxDto
     * @return
     */
    List<XzqgqrmxDto> getFkwcDtoList(XzqgqrmxDto xzqgqrmxDto);

    /**
     * 更新请购明细是否入库信息
     *
     * @param xzqgqrmxDtos
     */
    void modQgmxsfrk(List<XzqgqrmxDto> xzqgqrmxDtos);

    /**
     * 通过确认id查找行政请购确认明细数据
     * @param xzqgqrmxDto
     * @return
     */
    List<XzqgqrmxDto> getMxlistByQrid(XzqgqrmxDto xzqgqrmxDto);

    /**
     * 更新请购明细的付款标记
     * @param xzqgqrxmlist
     */
    void updateBatchFkbj(List<XzqgqrmxDto> xzqgqrxmlist);

    /**
     * 获取需要更新完成标记的请购信息
     * @param xzqgqrmxDto
     * @return
     */
    List<XzqgqrmxDto> getNeedXzqgQrxx(XzqgqrmxDto xzqgqrmxDto);

    /**
     * 找出已经完成付款的请购ID
     * @param qrmxlist
     * @return
     */
    List<String> getFkwcQgidList(List<XzqgqrmxDto> qrmxlist);
}

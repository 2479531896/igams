package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.XsmxDto;
import com.matridx.igams.storehouse.dao.entities.XsmxModel;

import java.util.List;


public interface IXsmxService extends BaseBasicService<XsmxDto, XsmxModel> {

    /**
     * 废弃按钮
     *
     * @param xsmxDto
     */
    void discard(XsmxDto xsmxDto);

    /**
     * 批量新增
     * @param list
     * @return
     */
    boolean insertList(List<XsmxDto> list);
    /**
     * 根据Id查询信息
     * @param xsmxDto
     * @return
     */
    List<XsmxDto> getListById(XsmxDto xsmxDto);

    /**
     * 根据Id更新信息
     * @param xsmxDtos
     * @return
     */
    Boolean updateXsmxList(List<XsmxDto> xsmxDtos);
    /**
     * 批量更新
     * @param list
     * @return
     */
    int updateList(List<XsmxDto> list);
    /**
     * 物料编码转义
     */
    String escapeWlbm(XsmxDto xsmxDto);
    /**
     * 产品类型转义
     */
    String escapecplx(XsmxDto xsmxDto);

    /**
     * 销售明细列表
     * @param xsmxDto
     * @return
     */

    List<XsmxDto> getPagedDtoListDetails(XsmxDto xsmxDto);
    /*
        获取销售到款信息
     */
    List<XsmxDto> getDkxxByXsid(XsmxDto xsmxDto);
    /*
        获取销售单下销售明细
     */
    List<XsmxDto> getXsmxByXs(XsmxDto xsmxDto);
    /*
        修改到款信息
     */
    boolean updateDkxx(XsmxDto xsmxDto);
    /*
        获取到款信息group
     */
    List<XsmxDto> getDkxxGroup(XsmxDto xsmxDto);
    /*
        获取到款金额group
     */
    List<XsmxDto> getAllDkjeGroupXs(XsmxDto xsmxDto);
}

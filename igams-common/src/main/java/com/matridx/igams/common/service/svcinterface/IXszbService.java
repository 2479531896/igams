package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.XszbDto;
import com.matridx.igams.common.dao.entities.XszbModel;

import java.util.List;
import java.util.Map;

public interface IXszbService extends BaseBasicService<XszbDto, XszbModel>{


    List<XszbDto> getXszbQyxx(XszbDto xszbDto);
	Map<String, Object> editSaveSale(XszbDto xszbDto);

    //根据销售子分类查找销售指标数据
    List<String> getDtosByZfl(XszbDto xszbDto);
    /*
     * 获取我的销售指标
     */
    List<XszbDto> getMySalesIndicator(XszbDto xszbDto);
    /*
        获取下属的指标
     */
    List<XszbDto> getSubordinateIndicator(XszbDto xszbDto);
    /*
        确认销售指标
     */
    boolean confirmSalesIndicator(XszbDto xszbDto);
    /*
        保存销售指标
     */
    boolean editSaveSalesIndicator(XszbDto xszbDto) throws BusinessException;
    /*
        获取销售指标清单
     */
    List<XszbDto> getSalesIndicatorList(XszbDto xszbDto);
    /*
        获取销售指标的销售额和目标
     */
    XszbDto getSalesIndicator(XszbDto xszbDto);
}

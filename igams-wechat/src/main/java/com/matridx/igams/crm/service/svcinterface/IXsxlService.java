package com.matridx.igams.crm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.crm.dao.entities.XsxlglDto;
import com.matridx.igams.crm.dao.entities.XsxlglModel;

import java.util.List;
import java.util.Map;

public interface IXsxlService extends BaseBasicService<XsxlglDto, XsxlglModel> {
    /**
     * 获取销量清单信息
     * @param xsxlglDto
     * @return
     */
    public List<Map<String,Object>> getSaleVolume(XsxlglDto xsxlglDto);
    /**
     * 获取未回款账目明细
     * @param xsxlglDto
     * @return
     */
    List<Map<String,Object>> getUnpaidAccounts(XsxlglDto xsxlglDto);
    /**
     * 获取未回款金额
     * @param xsxlglDto
     * @return
     */
    Map<String,Object> getUnpaidAmount(XsxlglDto xsxlglDto);
    /**
     * 获取未回款详情
     * @param xsxlglDto
     * @return
     */
    List<Map<String,Object>> getUnpaidAccountDetail(XsxlglDto xsxlglDto);
}

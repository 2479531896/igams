package com.matridx.igams.crm.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.crm.dao.entities.XsxlglDto;
import com.matridx.igams.crm.dao.entities.XsxlglModel;
import com.matridx.igams.crm.dao.post.IXsxlglDao;
import com.matridx.igams.crm.service.svcinterface.IXsxlService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class XsxlglServiceImpl extends BaseBasicServiceImpl<XsxlglDto, XsxlglModel,IXsxlglDao> implements IXsxlService {
    /**
     * 获取销量清单信息
     * @param xsxlglDto
     * @return
     */
    public List<Map<String,Object>> getSaleVolume(XsxlglDto xsxlglDto){
        return dao.getSaleVolume(xsxlglDto);
    }

    /**
     * 获取未回款账目明细
     * @param xsxlglDto
     * @return
     */
    public List<Map<String,Object>> getUnpaidAccounts(XsxlglDto xsxlglDto){
        return dao.getUnpaidAccounts(xsxlglDto);
    }
    /**
     * 获取未回款金额
     * @param xsxlglDto
     * @return
     */
    public Map<String,Object> getUnpaidAmount(XsxlglDto xsxlglDto){
        return dao.getUnpaidAmount(xsxlglDto);
    }
    /**
     * 获取未回款详情
     * @param xsxlglDto
     * @return
     */
    public List<Map<String,Object>> getUnpaidAccountDetail(XsxlglDto xsxlglDto){
        return dao.getUnpaidAccountDetail(xsxlglDto);
    }
}

package com.matridx.igams.crm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.crm.dao.entities.XsxlglDto;
import com.matridx.igams.crm.dao.entities.XsxlglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IXsxlglDao extends BaseBasicDao<XsxlglDto, XsxlglModel> {

    /**
     * 获取销量清单信息
     * @param xsxlglDto
     * @return
     */
    List<Map<String,Object>> getSaleVolume(XsxlglDto xsxlglDto);
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

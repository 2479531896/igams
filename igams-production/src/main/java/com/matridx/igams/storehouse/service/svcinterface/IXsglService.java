package com.matridx.igams.storehouse.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.FhglDto;
import com.matridx.igams.storehouse.dao.entities.XsglDto;
import com.matridx.igams.storehouse.dao.entities.XsglModel;
import com.matridx.igams.storehouse.dao.entities.XsmxDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


public interface IXsglService extends BaseBasicService<XsglDto, XsglModel>{
    /**
     * 生成oa销售订单号
     * @return
     */
    String getOAxsddh(String str);
    /**
     * 审核列表
     * @param xsglDto
     * @return
     */
    List<XsglDto> getPagedAuditSale(XsglDto xsglDto);
    /**
     * 验证
     * @param xsglDto
     * @return
     */
    XsglDto verify(XsglDto xsglDto);
    /**
     * 根据搜索条件获取导出条数
     * @return
     */
    int getCountForSearchExp(XsglDto xsglDto, Map<String, Object> params);

    List<XsglDto> getXsxxWithKh();

    List<XsglDto> getXsxxByKhid(XsglDto xsglDto);

    boolean salereceiptSavePage(XsglDto xsglDto);
	List<XsglDto> getDtoListByJyId(String jcjyid);
    boolean updateWlxx(XsglDto xsglDto);
    /**
     * 钉钉审批回调
     * @return
     */
    boolean callbackXsglAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;
    /**
     * 负责人设置保存
     */
    boolean stewardsetSaveSale(XsglDto xsglDto);
    /**
     * @description 获取销售订单数据
     * @param xsglDto
     * @return
     */
    List<XsglDto> getPagedSaleData(XsglDto xsglDto);
    /**
     * @param fhglDtos
     * @description 修改发货状态
     */
    void updateFhzt(List<FhglDto> fhglDtos);
    /*
        修改应收款信息
     */
    boolean updateYsk(XsglDto xsglDto);
    /**
     * 删除
     * @param xsglDto
     * @return
     */
    boolean delSale(XsglDto xsglDto) throws BusinessException;
    /*
        批量修改应收款
     */
    void updateYsks(List<XsglDto> xsglDtos);
}

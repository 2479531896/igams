package com.matridx.igams.production.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.ZlxyDto;
import com.matridx.igams.production.dao.entities.ZlxyModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
public interface IZlxyService extends BaseBasicService<ZlxyDto, ZlxyModel> {
    /**
     * 新增保存
     */
    boolean addSaveAgreement(ZlxyDto zlxyDto);

    /**
     * 修改保存
     */
    boolean modSaveAgreement(ZlxyDto zlxyDto);

    /**
     * 删除
     */
    boolean delAgreement(ZlxyDto zlxyDto);
    /**
     * 获取质量协议审核列表
     */
    List<ZlxyDto> getPagedAuditAgreement(ZlxyDto zlxyDto);
    /**
     * 钉钉审批回调
     */
    boolean callbackQualityAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;
    /**
     * 查询数据生成合同
     */
    Map<String,Object> getParamForContract(ZlxyDto zlxyDto);
    /**
     * 双章合同保存
     */
    boolean formalSaveAgreementContract(ZlxyDto zlxyDto);
    /**
     * @description 获取质量协议编号
     */
    String getAgreementContractSerial(String prefix,String num);
    /**
     * @description 校验协议编号重复
     */
    boolean isZlxybhRepeat(String zlxybh, String zlxyid);
}

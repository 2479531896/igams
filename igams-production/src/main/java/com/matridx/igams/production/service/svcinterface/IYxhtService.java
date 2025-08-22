package com.matridx.igams.production.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.YxhtDto;
import com.matridx.igams.production.dao.entities.YxhtModel;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IYxhtService extends BaseBasicService<YxhtDto, YxhtModel>{
    /**
     * @description 合同编号是否重复
     */
    boolean isHtbhRepeat(String htbh, String htid);
    /**
     * @description 新增保存营销合同
     */
    boolean addSaveMarketingContract(YxhtDto yxhtDto) throws BusinessException;
    /**
     * @description 修改保存营销合同
     */
    boolean modSaveMarketingContract(YxhtDto yxhtDto) throws BusinessException;
    /**
     * 查询合同期限
     */
    List<YxhtDto> selectHtqxList();

    boolean callbackMarketingContractAduit(JSONObject json_obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;
    /**
     * @description 营销合同审核列表数据
     */
    List<YxhtDto> getPagedAuditMarketingContract(YxhtDto yxhtDto);
    /**
     * @description 删除营销合同信息
     */
    boolean delMarketingContract(YxhtDto yxhtDto) throws BusinessException;
    /**
     * @description 双章合同保存
     */
    boolean formalSaveMarketingContract(YxhtDto yxhtDto) throws BusinessException;
    /**
     * @description 获取直属领导钉钉信息
     */
    List<YxhtDto> getInfoByZsld(YxhtDto yxhtDto);
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(YxhtDto yxhtDto, Map<String, Object> params);
    /**
     * @description 获取流水号
     */
    String getMarketingContractSerial(String prefix);
}

package com.matridx.igams.wechat.service.svcinterface;


import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SwhtglDto;
import com.matridx.igams.wechat.dao.entities.SwhtglModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * 商务合同管理(IgamsSwhtgl)表服务接口
 *
 * @author makejava
 * @since 2023-04-04 13:52:16
 */
public interface ISwhtglService  extends BaseBasicService<SwhtglDto, SwhtglModel> {

    /**
     * 查询是否重复数据
     */
    List<SwhtglDto> getRepeatDtoList(SwhtglDto swhtglDto);
    /**
     * 根据前缀生成合同编号
     */
    String getContractNumber(String cskz);

    /**
     * 审核列表
     * @param
     * @return
     */
    List<SwhtglDto> getPagedAuditList(SwhtglDto swhtglDto);

    /**
     * 分页查询
     * @param swhtglDto
     * @return
     */
    List<SwhtglDto> getPagedDtoList(SwhtglDto swhtglDto);
    /**
     * 根据客户获取合同信息
     * @param
     * @return
     */
    List<SwhtglDto> getAllInfo(SwhtglDto swhtglDto);
    /**
     * 插入合同信息
     */
    Boolean insertInfo(SwhtglDto swhtglDto) throws BusinessException;

    /**
     * 修改合同信息
     */
    Boolean updateInfo(SwhtglDto swhtglDto) throws BusinessException;

    /**
     * 删除合同信息
     */
    Boolean delInfo(SwhtglDto swhtglDto) throws BusinessException;

    /**
     * 双章合同信息
     */
    Boolean formalSaveContract(SwhtglDto swhtglDto) throws BusinessException;

    /**
     * 终止合同
     */
    Boolean discontinuecontractSave(SwhtglDto swhtglDto) throws BusinessException;

    /**
     * 补充合同
     */
    Boolean replenishcontractSaveContract(SwhtglDto swhtglDto) throws BusinessException;

    /**
     * 根据日期获取合同状态
     */
    String getHtzt(SwhtglDto swhtglDto);

    /**
     * 申请回调
     * @param obj
     * @param request
     * @param t_map
     * @return
     * @throws BusinessException
     */
    boolean callbackContractAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;

    /**
     * 客户合同管理查询合同信息
     * @param
     * @return
     */
    List<SwhtglDto> getKhhtglInfo(SwhtglDto swhtglDto);
    /**
     * 处理合同信息
     */
    Boolean processContract(SwhtglDto swhtglDto);
    /**
     * 修改状态
     */
    boolean updateZt(SwhtglDto swhtglDto);

    /**
     * 更新收费标准
     * @param swhtglDto
     * @return
     */
    boolean updateSfbz(SwhtglDto swhtglDto);
}

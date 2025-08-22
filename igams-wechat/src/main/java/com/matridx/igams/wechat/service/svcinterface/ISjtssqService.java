package com.matridx.igams.wechat.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjtssqDto;
import com.matridx.igams.wechat.dao.entities.SjtssqModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


public interface ISjtssqService extends BaseBasicService<SjtssqDto, SjtssqModel>{

    /**
     * 申请审核列表
     * @param sjtssqDto
     * @return
     */
    List<SjtssqDto> getPagedAuditApplication(SjtssqDto sjtssqDto);

    /**
     * 更改处理状态
     * @param sjtssqDto
     * @return
     */
    boolean updateClbj(SjtssqDto sjtssqDto);

    /**
     * 验证项目重复
     * @param sjtssqDto
     * @return
     */
    SjtssqDto verification(SjtssqDto sjtssqDto);

    /**
     * 特殊申请回调
     * @param obj
     * @param request
     * @param t_map
     * @return
     * @throws BusinessException
     */
    boolean callbackTssqAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;
    /**
     * 驳回保存
     * @param sjtssqDto
     * @return
     */
    boolean rejectSaveApplication(SjtssqDto sjtssqDto, User user);
    /**
     * 获取申请项目字符串
     * @param sjtssqDto
     * @return
     */
    String getSqxms(SjtssqDto sjtssqDto);
    /**
     * 删除
     * @param sjtssqDto
     * @return
     */
    boolean deleteDto(SjtssqDto sjtssqDto);
    /**
     * 新增
     * @param sjtssqDto
     * @return
     */
    Map<String,Object> insertSjtssq(SjtssqDto sjtssqDto);
    /**
     * 根据搜索条件获取导出条数
     * @param sjtssqDto
     * @return
     */
    int getCountForSearchExp(SjtssqDto sjtssqDto, Map<String, Object> params);
}

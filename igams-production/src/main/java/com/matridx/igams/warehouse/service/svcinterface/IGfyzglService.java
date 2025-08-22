package com.matridx.igams.warehouse.service.svcinterface;


import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.warehouse.dao.entities.GfyzglDto;
import com.matridx.igams.warehouse.dao.entities.GfyzglModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author jld
 */
public interface IGfyzglService extends BaseBasicService<GfyzglDto, GfyzglModel> {
    /**
     * @Description: 供方验证列表
     * @param gfyzglDto
     * @return java.util.List<com.matridx.igams.warehouse.dao.entities.GfyzglDto>
     * @Author: 郭祥杰
     * @Date: 2024/6/17 9:27
     */
    List<GfyzglDto> getPagedEvaluation(GfyzglDto gfyzglDto);

    /** 生成记录编号
     * @Description:
     * @param jgid
     * @return java.lang.String
     * @Author: 郭祥杰
     * @Date: 2024/6/17 14:29
     */
    String buildCode(String jgid);

    /**
     * @Description: 新增供方验证
     * @param gfyzglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/18 15:58
     */
    boolean insertEvaluation(GfyzglDto gfyzglDto) throws BusinessException;

    /**
     * @Description: 修改供方验证
     * @param gfyzglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/19 16:34
     */
    boolean updateEvaluation(GfyzglDto gfyzglDto) throws BusinessException;

    /**
     * @Description: 批量删除
     * @param gfyzglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/20 16:36
     */
    boolean delEvaluation(GfyzglDto gfyzglDto) throws BusinessException;

    /**
     * @Description: 钉钉审批回调
     * @param jsonObjectbj
     * @param request
     * @param map
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/21 16:11
     */
    boolean callbackEvaluationAduit(JSONObject jsonObjectbj, HttpServletRequest request, Map<String, Object> map) throws BusinessException;

    /**
     * @Description: 更新是否完成
     * @param gfyzglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/26 15:42
     */
    boolean updateList(GfyzglDto gfyzglDto);

    /**
     * @Description: 查询审核信息
     * @param gfyzid
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @Author: 郭祥杰
     * @Date: 2024/6/28 17:06
     */
    Map<String,String> queryShxxMap(String gfyzid);
}

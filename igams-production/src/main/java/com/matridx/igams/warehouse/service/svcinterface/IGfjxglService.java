package com.matridx.igams.warehouse.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.warehouse.dao.entities.GfjxglDto;
import com.matridx.igams.warehouse.dao.entities.GfjxglModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author : 郭祥杰
 * @date :
 */
public interface IGfjxglService extends BaseBasicService<GfjxglDto, GfjxglModel> {
    /**
     * @Description: 查询列表数据
     * @param gfjxglDto
     * @return java.util.List<com.matridx.igams.warehouse.dao.entities.GfjxglDto>
     * @Author: 郭祥杰
     * @Date: 2024/7/2 17:09
     */
    List<GfjxglDto> getPagedPerformance(GfjxglDto gfjxglDto);

    /**
     * @Description: 生成记录编号
     * @param jgid
     * @return java.lang.String
     * @Author: 郭祥杰
     * @Date: 2024/7/3 8:44
     */
    String buildCode(String jgid);

    /*
     * @Description: 新增保存
     * @param gfjxglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/7/3 17:25
     */
    boolean insertPerpromance(GfjxglDto gfjxglDto) throws BusinessException;

    /**
     * @Description: 更新保存
     * @param gfjxglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/7/4 13:59
     */
    boolean updatePerpromance(GfjxglDto gfjxglDto) throws BusinessException;

    /**
     * @Description: 删除
     * @param gfjxglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/7/4 14:14
     */
    boolean delPerformance(GfjxglDto gfjxglDto) throws BusinessException;

    /**
     * @Description: 审核回调
     * @param object
     * @param request
     * @param map
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/7/4 16:31
     */
    boolean callbackPerformanceAduit(JSONObject object, HttpServletRequest request, Map<String, Object> map) throws BusinessException;

    /**
     * @Description: 查询审核信息
     * @param gfjxid
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @Author: 郭祥杰
     * @Date: 2024/7/5 15:35
     */
    Map<String,String> queryShxxMap(String gfjxid);
}

package com.matridx.igams.warehouse.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.warehouse.dao.entities.GfpjbDto;
import com.matridx.igams.warehouse.dao.entities.GfpjbModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author jld
 */
public interface IGfpjbService extends BaseBasicService<GfpjbDto, GfpjbModel> {
    /**
     * @Description: 获取列表数据
     * @param gfpjbDto
     * @return List<GfpjbDto>
     * @Author: 郭祥杰
     * @Date: 2024/6/25 17:30
     */
    List<GfpjbDto> getPagedGfpjbDto(GfpjbDto gfpjbDto);

    /** 生成记录编号
     * @Description:
     * @param jgid
     * @return java.lang.String
     * @Author: 郭祥杰
     * @Date: 2024/6/25 17:40
     */
    String buildCode(String jgid);

    /**
     * @Description: 新增保存
     * @param gfpjbDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/26 10:16
     */
    boolean insertAppraise(GfpjbDto gfpjbDto) throws BusinessException;

    /**
     * @Description: 供方评价修改保存
     * @param gfpjbDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/26 14:06
     */
    boolean updateAppraise(GfpjbDto gfpjbDto) throws BusinessException;

    /**
     * @Description: 供方评价删除
     * @param gfpjbDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/26 14:54
     */
    boolean delAppraise(GfpjbDto gfpjbDto) throws BusinessException;

    /**
     * @Description: 审核回调
     * @param object
     * @param request
     * @param map
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/26 16:40
     */
    boolean callbackAppraiseAduit(JSONObject object, HttpServletRequest request, Map<String, Object> map) throws BusinessException;

    /**
     * @Description: 根据供方验证id查询
     * @param gfyzid
     * @return com.matridx.igams.warehouse.dao.entities.GfpjbDto
     * @Author: 郭祥杰
     * @Date: 2024/7/15 15:58
     */
    GfpjbDto getDtoByGfyzId(String gfyzid);
}

package com.matridx.igams.hrm.service.svcinterface;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.YhkqxxDto;
import com.matridx.igams.hrm.dao.entities.YhkqxxModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IYhkqxxService extends BaseBasicService<YhkqxxDto, YhkqxxModel> {

    /**
     * 更新出勤退勤信息
     * @param yhkqxxDto
     * @return
     */
    boolean updateSj(YhkqxxDto yhkqxxDto);

    //更新考勤按钮
    // public boolean updateCheckSave(XtyhDto xtyhDto);
    /**
     * 从数据库分页获取导出数据
     */
    List<YhkqxxDto> getListForSelectExp(Map<String, Object> params);
    /**
     * 根据搜索条件获取导出条数
     * @param yhkqxxDto
     * @return
     */
    int getCountForSearchExp(YhkqxxDto yhkqxxDto, Map<String, Object> params);
    /**
     * 根据搜索条件获取导出信息
     *
     * @param params
     * @return
     */
    List<YhkqxxDto> getListForSearchExp(Map<String, Object> params);
    /**
     * 根据yhid和日期获取主键id
     * @param yhkqxxDto
     * @return
     */
    YhkqxxDto getKqid(YhkqxxDto yhkqxxDto);
    /**
     * 根据kqid获取用户出勤信息
     * @param yhkqxxDto
     * @return
     */
    YhkqxxDto getKqxxByKqid(YhkqxxDto yhkqxxDto);
    /**
     * 根据ids删除信息
     * @param yhkqxxDto
     * @return
     */
    boolean delKqxx(YhkqxxDto yhkqxxDto);
    /**
     * 根据kqid获取信息
     * @param kqid
     * @return
     */
    YhkqxxDto getByKqid(String kqid);
    /**
     * @description 用户考勤统计列表
     * @param yhkqxxDto
     * @return
     */
    List<YhkqxxDto> getPagedStatis(YhkqxxDto yhkqxxDto);
    /**
     * @description 个人用户考勤
     * @param yhkqxxDto
     * @return
     */
    List<YhkqxxDto> getDtoListByYh(YhkqxxDto yhkqxxDto);
    /**
     * @description 获取用户考勤统计信息
     * @param yhkqxxDto
     * @return
     */
    YhkqxxDto getDtoByYhAndRq(YhkqxxDto yhkqxxDto);
    /**
     * @description 获取请假类型
     * @param csmc
     * @return
     */
    String getQjlx(String csmc);
    /**
     * @description 获取补贴信息
     * @param yhkqxxDto
     * @return
     */
    List<YhkqxxDto> getPagedSubsidy(YhkqxxDto yhkqxxDto);
    /**
     * @description 批量新增或修改用户考勤信息(加班班次)
     * @param yhkqxxDtos
     * @return boolean
     */
    boolean insertOrUpdateYhWorkOvertime(List<YhkqxxDto> yhkqxxDtos);
    /**
     * 更新请假信息
     */
    boolean updateCallBackAskForLeave(GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult approveInfo,HttpServletRequest request);
}

package com.matridx.igams.hrm.service.svcinterface;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.YhjqDto;
import com.matridx.igams.hrm.dao.entities.YhjqModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author:JYK
 */
public interface IYhjqService extends BaseBasicService<YhjqDto, YhjqModel> {
    /**处理钉钉审批回调
     *
     */
    boolean updateCallBackAskForLeave(GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult approveInfo,HttpServletRequest request);

    /**
     * 撤回或者拒绝时回滚数据
     */
    boolean rollBackHolidays(HttpServletRequest request);
    /**
     * 更新用户请假信息状态
     */
    boolean updateUserHolidaysStatus(HttpServletRequest request);

    /**
     * 销假回调操作
     */
    boolean reportBackHolidays(String mainProcessInstanceId);
    /**
     * 获取用户请假信息
     */
    YhjqDto getDtoByDdcs(YhjqDto yhjqDto);
    /**
     * @description 批量新增用户假期
     */
    boolean insertYhjqDtos(List<YhjqDto> yhjqDtos);
    /**
     * @description 批量修改用户假期
     */
    boolean updateYhjqDtos(List<YhjqDto> yhjqDtos);
}

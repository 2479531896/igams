package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.KqqjxxDto;
import com.matridx.igams.hrm.dao.entities.KqqjxxModel;

import java.util.List;

public interface IKqqjxxService extends BaseBasicService<KqqjxxDto, KqqjxxModel> {

    /**
     * 更新出勤退勤信息
     */
    boolean updateSj(KqqjxxDto kqqjxxDto);
    /**
     * 获取用户请假信息
     */
    KqqjxxDto getSpid(KqqjxxDto kqqjxxDto);
    /**
     * 获取请假信息
     */
    List<KqqjxxDto> viewLeaveNews(KqqjxxDto kqqjxxDto);
    /**
     * 删除信息
     */
    boolean delQjxx(KqqjxxDto kqqjxxDto);
    /**
     * @description 获取请假信息
     */
    List<KqqjxxDto> getDtoListForKsAndJs(KqqjxxDto kqqjxxDto);
}

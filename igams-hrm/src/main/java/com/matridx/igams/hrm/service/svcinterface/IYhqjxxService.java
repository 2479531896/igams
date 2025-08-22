package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.YhqjxxDto;
import com.matridx.igams.hrm.dao.entities.YhqjxxModel;

import java.util.List;

/**
 * @author:JYK
 */
public interface IYhqjxxService extends BaseBasicService<YhqjxxDto, YhqjxxModel> {
    /**
     * 根据钉钉审批实例id获取数据
     */
    YhqjxxDto selectDtoByDdslid(YhqjxxDto yhqjxxDto);
    /**
     * @description 获取用户请假时长信息
     */
    List<YhqjxxDto> getDtoListGroupYhAndQjlx(YhqjxxDto yhqjxxDto);
}

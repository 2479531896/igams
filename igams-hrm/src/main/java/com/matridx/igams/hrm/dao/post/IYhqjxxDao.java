package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.YhqjxxDto;
import com.matridx.igams.hrm.dao.entities.YhqjxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IYhqjxxDao extends BaseBasicDao<YhqjxxDto, YhqjxxModel> {
    /**
     * 根据钉钉审批实例id获取数据
     */
    YhqjxxDto selectDtoByDdslid(YhqjxxDto yhqjxxDto);
    /**
     * @description 获取用户请假时长信息
     */
    List<YhqjxxDto> getDtoListGroupYhAndQjlx(YhqjxxDto yhqjxxDto);
}

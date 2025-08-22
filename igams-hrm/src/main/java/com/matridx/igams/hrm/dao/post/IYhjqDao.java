package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.YhjqDto;
import com.matridx.igams.hrm.dao.entities.YhjqModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IYhjqDao extends BaseBasicDao<YhjqDto, YhjqModel> {
    /**
     * 获取所有用户
     */
    String getAllUserIds();

    /**
     * 批量查询
     */
    List<YhjqDto> selectDtoListByList(List<YhjqDto> list);

    /**
     * 批量更新
     */
    boolean updateList(List<YhjqDto> list);
    /**
     * 根据钉钉审批实例id获取信息
     */
    YhjqDto selectDtoByDdslid(YhjqDto yhjqDto);
    /**
     * 根据假期类型、用户id获取未过期的数据
     */
    List<YhjqDto> selectDtosByJqlxAndYhid(YhjqDto yhjqDto);
    /**
     * 批量更新
     */
    void updateListByList(List<YhjqDto> yhjqDtos);
    /**
     * 批量更新剩余额度
     */
    void updateListSyed(List<YhjqDto> yhjqDtos);
    /**
     * @description 获取用户请假信息
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
    /**
     * @description 批量修改用户假期钉钉余额
     */
    void updateDdyes(List<YhjqDto> yhjqDtos);
}

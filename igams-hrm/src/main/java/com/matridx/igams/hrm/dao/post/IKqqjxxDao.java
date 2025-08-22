package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.KqqjxxDto;
import com.matridx.igams.hrm.dao.entities.KqqjxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IKqqjxxDao  extends BaseBasicDao<KqqjxxDto, KqqjxxModel> {
    /**
     * 更新出勤退勤信息
     */
    boolean updateSj(KqqjxxDto kqqjxxDto);
    /**
     * 删除信息
     */
    boolean delQjxx(KqqjxxDto kqqjxxDto);
    /**
     * 获取用户请假信息
     */
    KqqjxxDto getSpid(KqqjxxDto kqqjxxDto);
    /**
     * 获取请假信息
     */
    List<KqqjxxDto> viewLeaveNews(KqqjxxDto kqqjxxDto);
    /**
     * @description 获取请假信息
     */
    List<KqqjxxDto> getDtoListForKsAndJs(KqqjxxDto kqqjxxDto);
}

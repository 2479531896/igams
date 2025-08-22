package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.ShfkdjDto;
import com.matridx.igams.production.dao.entities.ShfkdjModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IShfkdjDao extends BaseBasicDao<ShfkdjDto, ShfkdjModel> {
    /**
     * 钉钉售后反馈登记列表
     */
    List<ShfkdjDto> getPagedListSaleFeedbackDingTalk(ShfkdjDto shfkdjDto);
    /**
     * 获取处理记录
     */
    List<ShfkdjDto> getCljlList(ShfkdjDto shfkdjDto);

    boolean updateJd(ShfkdjDto shfkdjDto);

    boolean updateFzr(ShfkdjDto shfkdjDto);
}

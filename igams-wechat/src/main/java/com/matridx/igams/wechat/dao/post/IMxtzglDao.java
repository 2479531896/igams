package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.MxtzglDto;
import com.matridx.igams.wechat.dao.entities.MxtzglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IMxtzglDao extends BaseBasicDao<MxtzglDto, MxtzglModel> {

    /**
     * 多条添加通知记录
     * @param list
     * @return
     */
    boolean insertList(List<MxtzglDto> list);

    /**
     * 获取镁信通知信息
     * @param mxtzglDto
     * @return
     */
    List<MxtzglDto> getListById(MxtzglDto mxtzglDto);

}

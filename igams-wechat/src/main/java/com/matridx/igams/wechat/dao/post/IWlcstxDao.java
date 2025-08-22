package com.matridx.igams.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.WlcstxDto;
import com.matridx.igams.wechat.dao.entities.WlcstxModel;

@Mapper
public interface IWlcstxDao extends BaseBasicDao<WlcstxDto, WlcstxModel>{

    /**
     * 新增或修改超时提醒数据
     * @param wlcstxDto
     * @return
     */
    int insertOrUpdateWlcstx(WlcstxDto wlcstxDto);
}

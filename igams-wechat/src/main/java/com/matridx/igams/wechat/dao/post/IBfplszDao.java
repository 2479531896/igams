package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.BfplszDto;
import com.matridx.igams.wechat.dao.entities.BfplszModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IBfplszDao extends BaseBasicDao<BfplszDto, BfplszModel>{

    /**
     * 待拜访列表
     */
    List<BfplszDto> getPagedToBeVisitedList(BfplszDto bfplszDto);

    /**
     * 定时任务更新起始时间
     */
    boolean updateTimedTaskDates();
    /**
     * 定时任务更新拜访标记
     */
    boolean updateTimedTaskVisitMarkers();
}

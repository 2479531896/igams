package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.JpglDto;
import com.matridx.igams.hrm.dao.entities.JpglModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IJpglDao extends BaseBasicDao<JpglDto, JpglModel>{
    /*
        更新是否发送
     */
    void updateSffs(JpglDto jpglDtoUp);
    /*
        获取抽奖信息
     */
    JpglDto getLotteryInfo(String jpglid);
}

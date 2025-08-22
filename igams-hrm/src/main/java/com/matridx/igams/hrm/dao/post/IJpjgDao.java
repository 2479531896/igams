package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.JpjgDto;
import com.matridx.igams.hrm.dao.entities.JpjgModel;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface IJpjgDao extends BaseBasicDao<JpjgDto, JpjgModel>{
    /*
        获取抽奖记录
     */
    List<JpjgDto> getLotteryRecords(JpjgDto jpjgDto);
    /*
        获取个人抽奖记录
     */
    JpjgDto getPersonalLotteryRecord(JpjgDto jpjgDto);
}

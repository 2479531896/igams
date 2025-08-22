package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.JpjgDto;
import com.matridx.igams.hrm.dao.entities.JpjgModel;
import java.util.List;
public interface IJpjgService extends BaseBasicService<JpjgDto, JpjgModel>{
    /*
        获取抽奖记录
     */
    List<JpjgDto> getLotteryRecords(JpjgDto jpjgDto);
    /*
        获取个人抽奖记录
     */
    JpjgDto getPersonalLotteryRecord(JpjgDto jpjgDto);
}

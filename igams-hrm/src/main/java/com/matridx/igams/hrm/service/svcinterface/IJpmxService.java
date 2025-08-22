package com.matridx.igams.hrm.service.svcinterface;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.JpmxDto;
import com.matridx.igams.hrm.dao.entities.JpmxModel;
import java.util.List;

public interface IJpmxService extends BaseBasicService<JpmxDto, JpmxModel>{

    /**
     * 批量保存
     */
    boolean insertList(List<JpmxDto> list);
    /**
     * 批量修改
     */
    boolean updateList(List<JpmxDto> list);
    /*
        奖品明细信息
     */
    List<JpmxDto> getLotteryInfos(JpmxDto jpmxDto);
    /*
        更新剩余数量
    */
    boolean updateSysl(JpmxDto jpmxDto);
}

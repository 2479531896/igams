package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.WzzkDto;
import com.matridx.igams.wechat.dao.entities.WzzkModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IWzzkDao extends BaseBasicDao<WzzkDto, WzzkModel> {

    /**
     * 批量新增或修改
     * @param list
     * @return
     */
    boolean insertOrUpdateList(List<WzzkDto> list);

    List<WzzkDto> getPcStatistics(WzzkDto wzzkDto);

    List<WzzkDto> getNcStatistics(WzzkDto wzzkDto);

    List<WzzkDto> getNcPcTable(WzzkDto wzzkDto);

}

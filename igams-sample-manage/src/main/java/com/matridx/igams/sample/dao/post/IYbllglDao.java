package com.matridx.igams.sample.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.sample.dao.entities.YbllglDto;
import com.matridx.igams.sample.dao.entities.YbllglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IYbllglDao extends BaseBasicDao<YbllglDto, YbllglModel> {
    /**
     * 自动生成领料单号
     */
    String getLldhSerial(String prefix);
    /**
     * 校验领料单号是否重复
     */
    YbllglDto getDtoByLldh(YbllglDto ybllglDto);
    // 查询状态
    YbllglDto getZtById(YbllglDto ybllglDto);

    boolean updateFlr(YbllglDto ybllglDto);
    boolean deleteByLlids(YbllglDto ybllglDto);

    List<YbllglDto> getYbkcidByLlids(YbllglDto ybllglDto);
    /**
     *获取样本领料审核信息
     */
    List<YbllglDto> getPagedAuditPick(YbllglDto ybllglDto);
    /**
     * 获取样本领料审核列表
     */
    List<YbllglDto> getAuditListPick(List<YbllglDto> list);

    List<YbllglDto> getDtoByIds(YbllglDto ybllglDto);

}

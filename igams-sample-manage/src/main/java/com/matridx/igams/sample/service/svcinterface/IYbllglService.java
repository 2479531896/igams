package com.matridx.igams.sample.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.sample.dao.entities.YbllglDto;
import com.matridx.igams.sample.dao.entities.YbllglModel;

import java.util.List;

public interface IYbllglService extends BaseBasicService<YbllglDto, YbllglModel> {

    /**
     * 自动生成领料单号
     */
    String generateLldh(YbllglDto ybllglDto);

    /**
     * 校验领料单号是否重复
     */
    boolean getDtoByLldh(YbllglDto ybllglDto);
    // 查询状态
    YbllglDto getZtById(YbllglDto ybllglDto);

    boolean updateFlr(YbllglDto ybllglDto);

    boolean deleteByLlids(YbllglDto ybllglDto);

    List<YbllglDto> getYbkcidByLlids(YbllglDto ybllglDto);
    /**
     *获取样本领料审核信息
     */
    List<YbllglDto> getPagedAuditPick(YbllglDto ybllglDto);

    List<YbllglDto> getDtoByIds(YbllglDto ybllglDto);
    /**
     * 修改保存
     */
    boolean modSaveYbll(YbllglDto ybllglDto, User user) throws BusinessException;
    /**
     * 样本领料出库
     */
    boolean deliveryYbll(YbllglDto ybllglDto, User user) throws BusinessException;
    /**
     * 删除或废弃样本领料信息
     */
    boolean delYbll(YbllglDto ybllglDto, User user) throws BusinessException;
}

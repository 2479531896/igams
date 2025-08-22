package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.DkjejlDto;
import com.matridx.igams.storehouse.dao.entities.DkjejlModel;

import java.util.List;

/**
 * @author:JYK
 */
public interface IDkjejlService extends BaseBasicService<DkjejlDto, DkjejlModel> {
    /**
     * 到款记录保存
     * @param dkjejlDto
     * @param user
     * @return
     */
    boolean ArriveMoneySaveMoneyHos(DkjejlDto dkjejlDto, User user) throws BusinessException;
    /**
     * 批量插入
     */
    void insertList(List<DkjejlDto> list);
    /*
        批量删除
     */
    void deleteByYwids(DkjejlDto dkjejlDto);
    /*
        获取未到款列表
     */
    List<DkjejlDto> getPagedNotDtoList(DkjejlDto dkjejlDto);
    /*
        未到款列表查看
     */
    DkjejlDto getNotPaymentReceivedDto(DkjejlDto dkjejlDto);
}

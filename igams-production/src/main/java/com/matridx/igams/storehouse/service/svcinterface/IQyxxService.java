package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.QyxxDto;
import com.matridx.igams.storehouse.dao.entities.QyxxModel;

import java.util.List;

/**
 * @author:JYK
 */
public interface IQyxxService extends BaseBasicService<QyxxDto, QyxxModel> {
    /*
    * 取样保存
    * */
    boolean saveSampling(QyxxDto qyxxDto) throws BusinessException;
    /*
     * 取样删除
     * */
    boolean delSampling(QyxxDto qyxxDto) throws BusinessException;
    /*
     * 取样小结
     * */
    boolean updateSummary(QyxxDto qyxxDto)throws BusinessException;
    /**
     * 取样记录
     *
     */
    List<QyxxDto> getDtolistQySampleStock(QyxxDto qyxxDto);
}

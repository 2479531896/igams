package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.QzszDto;
import com.matridx.igams.hrm.dao.entities.QzszModel;

import java.util.List;

/**
 * @author WYX
 * @version 1.0
 * @className IQzszService
 * @description TODO
 * @date 15:50 2023/1/29
 **/
public interface IQzszService extends BaseBasicService<QzszDto, QzszModel> {
    /**
     * @description 批量插入权重设置信息
     */
    boolean insertQzszDtos(List<QzszDto> qzszDtos);
    /**
     * @description 根据绩效模板ids批量删除权重设置信息
     */
    void deleteByJxmbds(QzszDto qzszDto);
    /**
     * @description 通过mbszid获取权重信息
     */
    List<QzszDto> getDtoListByMzbszid(QzszDto qzszDto);
    /**
     * @description 通过绩效mbid获取权重信息
     */
    QzszDto getDtoByJxmbidAndMbszidWithNull(QzszDto qzszDto);
}

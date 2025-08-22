package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.QzszDto;
import com.matridx.igams.hrm.dao.entities.QzszModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author WYX
 * @version 1.0
 * @className IQzszDao
 * @description TODO
 * @date 15:30 2023/1/29
 **/
@Mapper
public interface IQzszDao extends BaseBasicDao<QzszDto, QzszModel> {
    /**
     * @description 批量插入权重设置信息
     */
    boolean insertQzszDtos(List<QzszDto> qzszDtos);
    /**
     * @description 通过mbszid获取权重信息
     */
    List<QzszDto> getDtoListByMzbszid(QzszDto qzszDto);
    /**
     * @description 通过绩效mbid获取权重信息
     */
    QzszDto getDtoByJxmbidAndMbszidWithNull(QzszDto qzszDto);
    /**
     * @description 根据绩效模板ids批量删除权重设置信息
     */
    void deleteByJxmbds(QzszDto qzszDto);
}

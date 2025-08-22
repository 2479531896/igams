package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.QyxxDto;
import com.matridx.igams.storehouse.dao.entities.QyxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IQyxxDao extends BaseBasicDao<QyxxDto, QyxxModel> {

    int deleteList(QyxxDto qyxxDto);

    List<QyxxDto> getQyxxListByIds(QyxxDto qyxxDto);
    /**
     * 取样记录
     *
     */
    List<QyxxDto> getDtolistQySampleStock(QyxxDto qyxxDto);
}

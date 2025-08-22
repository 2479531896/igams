package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.ZlxymxDto;
import com.matridx.igams.production.dao.entities.ZlxymxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IZlxymxDao extends BaseBasicDao<ZlxymxDto, ZlxymxModel> {
    /**
     * 修改状态
     */
    boolean updateZt(ZlxymxDto zlxymxDto);

    /**
     * 批量新增
     */
    boolean insertList(List<ZlxymxDto> list);

    /**
     * 直接删除
     */
    void deleteInfo(ZlxymxDto zlxymxDto);
}

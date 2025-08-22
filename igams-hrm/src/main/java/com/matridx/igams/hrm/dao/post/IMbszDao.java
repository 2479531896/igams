package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.MbszDto;
import com.matridx.igams.hrm.dao.entities.MbszModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IMbszDao extends BaseBasicDao<MbszDto, MbszModel> {
    /**
     * @description 批量修改模板设置信息
     */
    boolean updateMbszDtos(List<MbszDto> mbszDtos);
    /**
     * 根据模板ids删除数据
     *
     */
    boolean deleteBymbid(MbszDto mbszDto);
    /**
     * @description 修改生效日期和有效期
     */
    boolean updateSxrqAndYxq(MbszDto mbszDto);
}

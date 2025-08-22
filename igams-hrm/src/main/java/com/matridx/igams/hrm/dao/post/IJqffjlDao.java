package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.JqffjlDto;
import com.matridx.igams.hrm.dao.entities.JqffjlModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IJqffjlDao extends BaseBasicDao<JqffjlDto, JqffjlModel> {
    /**
     * @description 通过开始时间、有效期、用户、假期类型获取假期发放记录
     */
    List<JqffjlDto> getDtoListByKsAndYxqAndYhAndJqlx(JqffjlDto jqffjlDto);
    /**
     * @description 批量新增用户假期发放记录
     */
    boolean insertJqffjlDtos(List<JqffjlDto> jqffjlDtos);
    /**
     * @description 获取所有年度
     */
    List<String> getAllNd();
    /**
     * @description 获取假期发放记录信息 groupby 用户假期
     */
    List<JqffjlDto> getJqffjlDtosGroupByYhjq(JqffjlDto jqffjlDto);
}

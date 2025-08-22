package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.SbyzDto;
import com.matridx.igams.production.dao.entities.SbyzModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface ISbyzDao extends BaseBasicDao<SbyzDto, SbyzModel> {
    /**
     * 审核列表
     */
    List<SbyzDto> getPagedAuditTesting(SbyzDto sbyzDto);
    /**
     *
     */
    List<SbyzDto> getAuditListTesting(List<SbyzDto> list);
    /**
     * 批量插入
     */
    boolean insertList(List<SbyzDto> list);
    List<SbyzDto> getDepartmentList(SbyzDto sbyzDto);
    /**
     * 获取管理人员
     */
    List<SbyzDto> getGlryList();

}

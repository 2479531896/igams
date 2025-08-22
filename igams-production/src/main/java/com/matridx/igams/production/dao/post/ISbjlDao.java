package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.SbjlDto;
import com.matridx.igams.production.dao.entities.SbjlModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface ISbjlDao extends BaseBasicDao<SbjlDto, SbjlModel> {
    /**
     * 获取列表中的部门
     */
    List<SbjlDto> getDepartmentList();
    /**
     * 获取管理人员
     */
    List<SbjlDto> getGlryList();
    /**
     * 审核列表
     */
    List<SbjlDto> getPagedAuditMetering(SbjlDto sbjlDto);
    /**
     * 审核列表
     */
    List<SbjlDto> getAuditListMetering(List<SbjlDto> list);
    /**
     * 批量插入数据
     */
    boolean insertList(List<SbjlDto> list);
    /**
     * 批量更新数据
     */
    boolean updateList(List<SbjlDto> list);
}

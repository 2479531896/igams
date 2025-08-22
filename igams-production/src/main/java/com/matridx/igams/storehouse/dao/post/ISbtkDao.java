package com.matridx.igams.storehouse.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.SbtkDto;
import com.matridx.igams.storehouse.dao.entities.SbtkModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISbtkDao extends BaseBasicDao<SbtkDto, SbtkModel>{
    /**
     * @description 获取所有使用部门
     */
    List<SbtkDto> getDepartmentList();
    /**
     * @description 获取所有管理人员
     */
    List<SbtkDto> getGlryList();
    /**
     * @description 获取审核数据List
     * @param sbtkDto
     * @return
     */
    List<SbtkDto> getPagedAuditStockreturnDevice(SbtkDto sbtkDto);
    /**
     * @description 获取审核数据List
     * @param t_sbtkList
     * @return
     */
    List<SbtkDto> getAuditListStockreturnDevice(List<SbtkDto> t_sbtkList);
}

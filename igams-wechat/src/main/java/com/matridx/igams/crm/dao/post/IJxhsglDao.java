package com.matridx.igams.crm.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.crm.dao.entities.JxhsglDto;
import com.matridx.igams.crm.dao.entities.JxhsglModel;

import java.util.List;

@Mapper
public interface IJxhsglDao extends BaseBasicDao<JxhsglDto, JxhsglModel>{

    /**
     * 查询重复绩效
     * @param jxhsglDto
     * @return
     */
    JxhsglDto getSameDto(JxhsglDto jxhsglDto);
    /**
     * 更新状态
     * @param jxhsglDto
     * @return
     */
    int updateZt(JxhsglDto jxhsglDto);

    /**
     * 获取审核信息1
     * @param jxhsglDto
     * @return
     */
    List<JxhsglDto> getPagedAuditList(JxhsglDto jxhsglDto);
    /**
     *  获取审核信息2
     * @param tList
     * @return
     */
    List<JxhsglDto> getAuditList(List<JxhsglDto> tList);
}

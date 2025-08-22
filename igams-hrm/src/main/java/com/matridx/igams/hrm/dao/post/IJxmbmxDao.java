package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.JxmbmxDto;
import com.matridx.igams.hrm.dao.entities.JxmbmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IJxmbmxDao extends BaseBasicDao<JxmbmxDto, JxmbmxModel> {

    /**
     * 批量插入模板明细
     */
    boolean insertMbmxList(List<JxmbmxDto> list);

    /**
     * 根据绩效模板id删除绩效模板明细
     */
    boolean deleteByJxmbid(String jxmbid);
}

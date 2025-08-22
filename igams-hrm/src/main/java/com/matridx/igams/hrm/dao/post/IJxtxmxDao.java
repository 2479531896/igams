package com.matridx.igams.hrm.dao.post;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.JxtxmxDto;
import com.matridx.igams.hrm.dao.entities.JxtxmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IJxtxmxDao extends BaseBasicDao<JxtxmxDto, JxtxmxModel> {
    /**
     * @description 新增绩效提醒明细信息
     */
    boolean insertJxtxmxDtos(List<JxtxmxDto> jxtxmxDtos);
    /**
     * @description 修改绩效提醒明细信息
     */
    boolean updateJxtxmxDtos(List<JxtxmxDto> jxtxmxDtos);
    /**
     * @description 删除绩效提醒明细信息
     */
    boolean deleteByJxtxid(JxtxmxDto jxtxmxDto);
}

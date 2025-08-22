package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.FxsjglDto;
import com.matridx.igams.wechat.dao.entities.FxsjglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IFxsjglDao extends BaseBasicDao<FxsjglDto, FxsjglModel>{

    /**
     * 审核信息
     * @param
     * @return
     */
    List<FxsjglDto> getAuditListRecheck(List<FxsjglDto> list);
    /**
     * 审核列表
     * @param
     * @return
     */
    List<FxsjglDto> getPagedAuditList(FxsjglDto fxsjglDto);
    /**
     * 查询角色
     * @param params
     * @return
     */
    List<Map<String,Object>> getRoleList(Map<String, Object> params);
}

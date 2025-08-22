package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.JcsqglDto;
import com.matridx.igams.wechat.dao.entities.JcsqglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IJcsqglDao extends BaseBasicDao<JcsqglDto, JcsqglModel>{

    /**
     * 获取审核列表
     * @param jcsqglDto
     * @return
     */
    List<JcsqglDto> getPagedAuditDetectionApplication(JcsqglDto jcsqglDto);
    /**
     * 审核列表
     * @param list
     * @return
     */
    List<JcsqglDto> getAuditListDetectionApplication(List<JcsqglDto> list);

    /**
     * 设置钉钉实例ID为null
     * @param
     * @return
     */
    boolean updateDdslidToNull(JcsqglDto jcsqglDto);
    /**
     * 根据钉钉实例ID获取在钉钉进行领料申请的审批信息
     * @param
     * @return
     */
    JcsqglDto getDtoByDdslid(String ddspid);
    /**
     * 根据审批人用户ID获取角色信息
     * @param
     * @return
     */
    List<JcsqglDto> getSprjsBySprid(String sprid);

    /**
     * 获取文库编码
     * @param jcsqglDto
     * @return
     */
    List<JcsqglDto> getWkbmsByYbbhs(JcsqglDto jcsqglDto);

    List<Map<String, String>> getWxyhByYhid(String yhid);

}

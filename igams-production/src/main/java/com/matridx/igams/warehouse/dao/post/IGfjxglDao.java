package com.matridx.igams.warehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.warehouse.dao.entities.GfjxglDto;
import com.matridx.igams.warehouse.dao.entities.GfjxglModel;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
public interface IGfjxglDao extends BaseBasicDao<GfjxglDto, GfjxglModel> {
    /**
     * @Description: 生成记录编号
     * @param jlbh
     * @return java.lang.String
     * @Author: 郭祥杰
     * @Date: 2024/7/3 8:47
     */
    String getJlbhSerial(String jlbh);

    /**
     * @Description: 根据审批人查询
     * @param sprid
     * @return com.matridx.igams.warehouse.dao.entities.GfjxglDto
     * @Author: 郭祥杰
     * @Date: 2024/7/4 16:37
     */
    List<GfjxglDto> getSprjsBySprid(String sprid);
}

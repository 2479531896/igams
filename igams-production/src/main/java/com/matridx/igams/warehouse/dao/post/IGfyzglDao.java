package com.matridx.igams.warehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.warehouse.dao.entities.GfyzglDto;
import com.matridx.igams.warehouse.dao.entities.GfyzglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author jld
 */
@Mapper
public interface IGfyzglDao extends BaseBasicDao<GfyzglDto, GfyzglModel> {
    /** 获取流水号
     * @Description:
     * @param jlbh
     * @return java.lang.String
     * @Author: 郭祥杰
     * @Date: 2024/6/17 15:00
     */
    String getJlbhSerial(String jlbh);

    /** 获取审批人信息
     * @Description:
     * @param sprid
     * @return java.util.List<com.matridx.igams.warehouse.dao.entities.GfyzglDto>
     * @Author: 郭祥杰
     * @Date: 2024/6/21 16:34
     */
    List<GfyzglDto> getSprjsBySprid(String sprid);

    /**
     * @Description: 更新是否完成
     * @param gfyzglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/26 15:45
     */
    boolean updateList(GfyzglDto gfyzglDto);
}

package com.matridx.igams.warehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.warehouse.dao.entities.GfpjbDto;
import com.matridx.igams.warehouse.dao.entities.GfpjbModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author jld
 */ 
@Mapper
public interface IGfpjbDao extends BaseBasicDao<GfpjbDto, GfpjbModel> {
    /**
     * @Description: 生成记录编号
     * @param jlbh
     * @return java.lang.String
     * @Author: 郭祥杰
     * @Date: 2024/6/25 17:42
     */
    String getJlbhSerial(String jlbh);

    /**
     * @Description: 获取审批人信息
     * @param sprid
     * @return java.util.List<com.matridx.igams.warehouse.dao.entities.GfpjbDto>
     * @Author: 郭祥杰
     * @Date: 2024/6/26 16:48
     */
    List<GfpjbDto> getSprjsBySprid(String sprid);

    /**
     * @Description: 根据供方验证id查询
     * @param gfyzid
     * @return com.matridx.igams.warehouse.dao.entities.GfpjbDto
     * @Author: 郭祥杰
     * @Date: 2024/7/15 15:57
     */
    GfpjbDto getDtoByGfyzId(String gfyzid);
}

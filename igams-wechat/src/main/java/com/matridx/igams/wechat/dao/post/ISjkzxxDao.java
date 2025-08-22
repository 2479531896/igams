package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjkzxxDto;
import com.matridx.igams.wechat.dao.entities.SjkzxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISjkzxxDao extends BaseBasicDao<SjkzxxDto, SjkzxxModel>{

    /**
     * 查询对账明细
     * @param params
     * @return
     */
    List<Map<String,Object>> getDtoListOptimize(Map<String, Object> params);

    /**
     * 标本实验列表分页
     * @return
     */
    List<SjkzxxDto> getPagedSpecimenExperimentalList(SjkzxxDto sjkzxxDto);

    /**
     * 从数据库分页获取导出数据
     * @param sjkzxxDto
     * @return
     */
    List<SjkzxxDto> getListForSearchExp(SjkzxxDto sjkzxxDto);

    /**
     * 从数据库分页获取导出数据
     * @param sjkzxxDto
     * @return
     */
    List<SjkzxxDto> getListForSelectExp(SjkzxxDto sjkzxxDto);

    /**
     * 根据搜索条件获取导出条数
     * @param sjkzxxDto
     * @return
     */
    int getCountForSearchExp(SjkzxxDto sjkzxxDto);

    SjkzxxDto getSjkzxxBySjid(SjkzxxDto sjkzxxDto);

    /**
     * 获取igams_tswzlx
     * @return
     */
    List<Map<String,String>>getTsWzlx();

}

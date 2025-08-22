package com.matridx.igams.warehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;

import com.matridx.igams.warehouse.dao.entities.FlowingDto;
import com.matridx.igams.warehouse.dao.entities.FlowingModel;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IFlowingDao extends BaseBasicDao<FlowingDto, FlowingModel> {
    /**
     * 获取页面流水信息
     * @param flowingDto
     
     */
    List<FlowingDto> getPageFlowingDtoList(FlowingDto flowingDto);
    /**
     * 根据入库id查询数据
     *
     */

    FlowingDto getRkById(FlowingDto flowingDto);
    /**
     * 根据出库id查询数据
     *
     */
    FlowingDto getCkById(FlowingDto flowingDto);
    /**
     * 根据调出id查询数据
     *
     */
    FlowingDto getDcById(FlowingDto flowingDto);
    /**
     * 根据调入id查询数据
     *
     */
    FlowingDto getDrById(FlowingDto flowingDto);
    /**
     * 根据借出id查询数据
     *
     */
    FlowingDto getJcById(FlowingDto flowingDto);
    /**
     * 根据归还id查询数据
     *
     */
    FlowingDto getGhById(FlowingDto flowingDto);
    /**
     * 根据到货id查询数据
     *
     */
    FlowingDto getDhById(FlowingDto flowingDto);
    /**
     * 根据发货id查询数据
     *
     */
    FlowingDto getFhById(FlowingDto flowingDto);


    /**
     * 从数据库分页获取导出数据
     * @param flowingDto
     
     */
    List<FlowingDto> getListForSearchExp(FlowingDto flowingDto);

    /**
     * 从数据库分页获取导出数据
     * @param flowingDto
     
     */
    List<FlowingDto> getListForSelectExp(FlowingDto flowingDto);
    /**
     * 根据搜索条件获取导出条数
     * @param flowingDto
     
     */
    int getCountForSearchExp(FlowingDto flowingDto);


}

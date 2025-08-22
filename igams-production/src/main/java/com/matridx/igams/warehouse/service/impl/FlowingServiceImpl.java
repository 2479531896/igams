package com.matridx.igams.warehouse.service.impl;

import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.warehouse.dao.entities.FlowingDto;
import com.matridx.igams.warehouse.dao.entities.FlowingModel;
import com.matridx.igams.warehouse.dao.post.IFlowingDao;
import com.matridx.igams.warehouse.service.svcinterface.IFlowingService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
@Service
public class FlowingServiceImpl extends BaseBasicServiceImpl<FlowingDto, FlowingModel, IFlowingDao> implements IFlowingService {
    /**
     * 获取页面数据
     * @param flowingDto
     
     */
    @Override
    public List<FlowingDto> getPageFlowingDtoList(FlowingDto flowingDto) {
        return dao.getPageFlowingDtoList(flowingDto);
    }
    /**
     * 根据入库id查询数据
     *
     */

    @Override
    public FlowingDto getRkById(FlowingDto flowingDto) {
        return dao.getRkById(flowingDto);
    }
    /**
     * 根据出库id查询数据
     *
     */

    @Override
    public FlowingDto getCkById(FlowingDto flowingDto) {
        return dao.getCkById(flowingDto);
    }
    /**
     * 根据调出id查询数据
     *
     */

    @Override
    public FlowingDto getDcById(FlowingDto flowingDto) {
        return dao.getDcById(flowingDto);
    }

    /**
     * 根据调入id查询数据
     *
     */
    @Override
    public FlowingDto getDrById(FlowingDto flowingDto) {
        return dao.getDrById(flowingDto);
    }
    /**
     * 根据借出id查询数据
     *
     */

    @Override
    public FlowingDto getJcById(FlowingDto flowingDto) {
        return dao.getJcById(flowingDto);
    }

    /**
     * 根据归还id查询数据
     *
     */
    @Override
    public FlowingDto getGhById(FlowingDto flowingDto) {
        return dao.getGhById(flowingDto);
    }

    /**
     * 根据到货id查询数据
     *
     */
    @Override
    public FlowingDto getDhById(FlowingDto flowingDto) {
        return dao.getDhById(flowingDto);
    }

    /**
     * 根据发货id查询数据
     *
     */
    @Override
    public FlowingDto getFhById(FlowingDto flowingDto) {
        return dao.getFhById(flowingDto);
    }

    /**
     * 根据选择信息获取导出信息

     */
    public List<FlowingDto> getListForSelectExp(Map<String,Object> params){
        FlowingDto flowingDto = (FlowingDto)params.get("entryData");
        queryJoinFlagExport(params,flowingDto);
        return dao.getListForSelectExp(flowingDto);
    }


    /**
     * 根据搜索条件获取导出条数
     *

     */
    public int getCountForSearchExp(FlowingDto flowingDto,Map<String, Object> params) {
        return dao.getCountForSearchExp(flowingDto);
    }
    /**
     * 根据搜索条件分页获取导出信息
     *

     */

    public List<FlowingDto> getListForSearchExp(Map<String, Object> params)
    {
        FlowingDto flowingDto = (FlowingDto)params.get("entryData");
        queryJoinFlagExport(params,flowingDto);

        return dao.getListForSearchExp(flowingDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String,Object> params,FlowingDto flowingDto){
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
        for(DcszDto dcszDto:choseList){
            if(dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if(StringUtil.isNotBlank(dcszDto.getSqlzd())){
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        String sqlcs=sqlParam.toString();
        flowingDto.setSqlParam(sqlcs);
    }


}

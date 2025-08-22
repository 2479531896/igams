package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.SczlmxDto;
import com.matridx.igams.production.dao.entities.SczlmxModel;
import com.matridx.igams.production.dao.entities.XqjhmxDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISczlmxDao extends BaseBasicDao<SczlmxDto, SczlmxModel> {
    /**
     * 批量新增生产指令明细信息
     */
    Integer insertList(List<SczlmxDto> sczlmxDtos);

    /**
     * 批量跟新生产指令明细信息
     */
    Integer updateList(List<SczlmxDto> sczlmxDtos);

    List<SczlmxDto> getProduceEligibles(SczlmxDto sczlmxDto);

    List<SczlmxDto> getProducePlanStatis(SczlmxDto sczlmxDto);
    /**
     * 生产指令数据
     */
    List<SczlmxDto> getSczlmxDtos(List<XqjhmxDto> list);
    /**
     * 生产指令数据
     */
    List<SczlmxDto> getPagedProduce(SczlmxDto sczlmxDto);
    /**
     * @description 获取流水号
     */
    String getProgressSerial(String prefix);
    /**
     * @description 获取打印单内容
     */
    List<SczlmxDto> getDtoListForPrint(SczlmxDto sczlmxDto);
}

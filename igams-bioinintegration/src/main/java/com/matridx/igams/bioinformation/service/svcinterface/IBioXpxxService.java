package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.BioXpxxDto;
import com.matridx.igams.bioinformation.dao.entities.BioXpxxModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface IBioXpxxService extends BaseBasicService<BioXpxxDto, BioXpxxModel>{

    /**
     * 超找测序仪基础数据范围内的芯片信息
     */
    List<BioXpxxDto> getXpxxByCxylist(List<JcsjDto> cxyChild);
    /**
     * 获取生信审核系统芯片列表
     */
    List <BioXpxxDto> getPagedBioChipList(BioXpxxDto bioXpxxDto);
    /**
     * 获取今日芯片列表芯片列表
     */
    List <BioXpxxDto> getDtoTodayChipList(BioXpxxDto bioXpxxDto);
    /**
     * 根据芯片名查询芯片ID
     */
    String getXpidByXpm(String xpm);
    /**
     * 批量新增
     */
    boolean insertList(List<BioXpxxDto> list);

    boolean   insertDto(BioXpxxDto bioXpxxDto);
}

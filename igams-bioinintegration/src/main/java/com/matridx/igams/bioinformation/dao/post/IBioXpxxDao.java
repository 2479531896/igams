package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.bioinformation.dao.entities.BioXpxxDto;
import com.matridx.igams.bioinformation.dao.entities.BioXpxxModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.JcsjDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IBioXpxxDao extends BaseBasicDao<BioXpxxDto, BioXpxxModel> {

    /***
     * 修改wksjgl 下机时间
     */
    int updateWkgl(List<Map<String,Object> >list);

    /**
     * 修改sjsygl下机时间
     */
    int updateSjsygl(List<Map<String,Object> >list);
    /**
     * 查找某测序仪归属省份范围下测序仪的所有芯片数据
     */
    List<BioXpxxDto> getXpxxByCxylist(List<JcsjDto> cxyChild);
    /**
     * 获取生信审核系统芯片列表
     */
    List<BioXpxxDto> getPagedBioChipList(BioXpxxDto bioXpxxDto);
    /**
     * 获取今日芯片列表芯片列表
     */
    List<BioXpxxDto> getDtoTodayChipList(BioXpxxDto bioXpxxDto);
    /**
     * 根据芯片名所有芯片数据
     */
    List<BioXpxxDto> getXpxxByXpms(List<String> ids);
    /**
     * 批量新增
     */
    boolean insertList(List<BioXpxxDto> list);
    /**
     * 批量修改
     */
    boolean updateList(List<BioXpxxDto> list);
    /**
     * 根据芯片名查询芯片ID
     */
    String getXpidByXpm(String xpm);

    int insertDto(BioXpxxDto bioXpxxDto);
}

package com.matridx.igams.common.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.PcrsyjgDto;
import com.matridx.igams.common.dao.entities.PcrsyjgModel;

@Mapper
public interface IPcrsyjgDao extends BaseBasicDao<PcrsyjgDto, PcrsyjgModel>{
int insertListPcr(List<PcrsyjgDto> list);

    /**
     * 通过角色ID获得角色检测单位信息
     */
    List<Map<String, String>> getJsjcdwByjsid(String dqjs);
}

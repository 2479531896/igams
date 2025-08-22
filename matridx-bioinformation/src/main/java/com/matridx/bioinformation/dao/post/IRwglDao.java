package com.matridx.bioinformation.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.bioinformation.dao.entities.RwglDto;
import com.matridx.bioinformation.dao.entities.RwglModel;

import java.util.List;

@Mapper
public interface IRwglDao extends BaseBasicDao<RwglDto, RwglModel>{

    Integer updateList(List<RwglDto> rwglDtoList);

    List<RwglDto> getOverdueList(RwglDto rwglDto);

    RwglDto getDtoInfo(RwglDto rwglDto);

    /**
     * 获取容器
     * @param strings
     * @return
     */
    List<RwglDto> getEndDtoList(List<String> strings);

    /**
     * 获取任务列表
     * @param rwglDto
     * @return
     */
    List<RwglDto> getInfoList(RwglDto rwglDto);
}

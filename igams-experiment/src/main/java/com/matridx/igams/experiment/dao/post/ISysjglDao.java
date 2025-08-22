package com.matridx.igams.experiment.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.SysjglDto;
import com.matridx.igams.experiment.dao.entities.SysjglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISysjglDao extends BaseBasicDao<SysjglDto, SysjglModel>{
    /**
     * @Description: 实验试剂新增
     * @param list
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2025/5/13 9:40
     */
    boolean insertSysjglDtos(List<SysjglDto> list);

    /**
     * @Description: 根据id查询
     * @param sysjglDto
     * @return java.util.List<com.matridx.igams.experiment.dao.entities.SysjglDto>
     * @Author: 郭祥杰
     * @Date: 2025/5/13 9:40
     */
    List<SysjglDto> getSysjxxxMap(SysjglDto sysjglDto);

    /**
     * @Description: 根据ids删除
     * @param sysjglDto
     * @return boolean
     */
    boolean deleteByIds(SysjglDto sysjglDto);
}

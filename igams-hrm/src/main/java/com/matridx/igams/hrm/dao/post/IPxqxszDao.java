package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;

import com.matridx.igams.hrm.dao.entities.PxqxszDto;
import com.matridx.igams.hrm.dao.entities.PxqxszModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IPxqxszDao extends BaseBasicDao<PxqxszDto, PxqxszModel> {
    /**
     * 查询机构列表(未选择)
     */
    List<PxqxszDto> getPagedUnSelectJgList(PxqxszDto pxqxszDto);
    /**
     * 查询机构列表(已选择)
     */
    List<PxqxszDto> getPagedSelectedJgList(PxqxszDto pxqxszDto);
    /**
     * 批量新增
     */
    boolean insertList(List<PxqxszDto> list);

    /**
     * 定时任务
     */
    List<PxqxszDto> getBeforeDayList(String day);



}

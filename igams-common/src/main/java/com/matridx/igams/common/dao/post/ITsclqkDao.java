package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.TsclqkDto;
import com.matridx.igams.common.dao.entities.TsclqkModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ITsclqkDao extends BaseBasicDao<TsclqkDto, TsclqkModel>{

    /**
     * 批量新增
     */
    boolean insertList(List<TsclqkDto> list);
    /**
     * 批量还原
     */
    boolean revertData(TsclqkDto tsclqkDto);

    List<TsclqkDto> getPagedListByBm(TsclqkDto tsclqkDto);

    /**
     * 处理
     */
    boolean dealData(TsclqkDto tsclqkDto);
    /**
     * 获取部门集合
     */
    List<TsclqkDto> getDepartmentList(TsclqkDto tsclqkDto);
    /**
     * 批量修改
     */
    boolean updateForConclusion(List<TsclqkDto>list);
}

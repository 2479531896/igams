package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.BcglDto;
import com.matridx.igams.hrm.dao.entities.BcglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IBcglDao extends BaseBasicDao<BcglDto, BcglModel>{
    /**
     * 获取数据
     */
    List<BcglDto> getBcglDtoList(BcglDto bcglDto);
    /**
     * 批量新增
     */
    void insertList(List<BcglDto> list);
    /**
     * 批量更新
     */
    void updateList(List<BcglDto> list);
    /**
     * @description 通过出勤时间获取班次信息
     */
    BcglDto getBcByCqsj(String cqsj);
}

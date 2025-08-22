package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.KsglDto;
import com.matridx.igams.hrm.dao.entities.KsglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IKsglDao extends BaseBasicDao<KsglDto, KsglModel> {
    /**
     * 通过tkid获取考试明细
     */
    List<KsglDto> getListQuestions(KsglDto ksglDto);

    /**
     * 通过tkid删除题目
     */
    void deleteByTkid(KsglDto ksglDto);

    /**
     * 获取题目数量
     */
    int getCount(KsglDto ksglDto);

    /**
     * 批量新增
     */
    boolean insertList(List<KsglDto> ksglDtos);
    /*
        获取题库题目
     */
    List<KsglDto> getQuestions(KsglDto ksglDto);

}

package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.GrksglDto;
import com.matridx.igams.hrm.dao.entities.KsglDto;
import com.matridx.igams.hrm.dao.entities.KsglModel;


import java.util.List;
import java.util.Map;

public interface IKsglService extends BaseBasicService<KsglDto, KsglModel> {
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
    /**
     * 随机组题
     */
    List<KsglDto> groupQuestions(int sl, Map<Object, Object> tmlist, List<KsglDto> list_t, GrksglDto grksglDto);
    /*
        获取题库题目
     */
    List<KsglDto> getQuestions(KsglDto ksglDto);
}

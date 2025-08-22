package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjkzxxDto;
import com.matridx.igams.wechat.dao.entities.SjkzxxModel;

import java.util.List;
import java.util.Map;

public interface ISjkzxxService extends BaseBasicService<SjkzxxDto, SjkzxxModel> {


    /**
     * 查询对账明细
     * @param params
     * @return
     */
    List<Map<String,Object>> getDtoListOptimize(Map<String, Object> params);

    /**
     * 标本实验列表分页
     * @return
     */
    List<SjkzxxDto> getPagedSpecimenExperimentalList(SjkzxxDto sjkzxxDto);

    /**
     * 数据合并复制sjkzxx得qtxx
     * @param copy1
     * @param copy2
     * @param user
     */
    void copySjkzxx(SjkzxxDto copy1, SjkzxxDto copy2, User user);

    SjkzxxDto getSjkzxxBySjid(SjkzxxDto sjkzxxDto);

    /**
     * 获取igams_tswzlx
     * @return
     */
    List<Map<String,String>>getTsWzlx();
}

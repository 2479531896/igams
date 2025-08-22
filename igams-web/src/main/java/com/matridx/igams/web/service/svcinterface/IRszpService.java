package com.matridx.igams.web.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.RszpDto;
import com.matridx.igams.web.dao.entities.RszpModel;

import java.util.List;
import java.util.Map;

public interface IRszpService extends BaseBasicService<RszpDto, RszpModel> {

    /**
     * 获取审批id
     * @param rszpDto
     * @return
     */
    RszpDto getSpid(RszpDto rszpDto);
    /**
     * 获取单条记录
     * @param rszpDto
     * @return
     */
    RszpDto getDtoByZpid(RszpDto rszpDto);
    /**
     * 删除按钮
     * @param rszpDto
     * @return
     */
    boolean delRecruitment(RszpDto rszpDto);
    /**
     * 从数据库分页获取导出数据
     * @param params
     * @return
     */
    List<RszpDto> getListForSelectExp(Map<String, Object> params);
    /**
     * 根据搜索条件获取导出条数
     * @param params
     * @return
     */
    int getCountForSearchExp(RszpDto rszpDto, Map<String, Object> params);
    /**
     * 从数据库分页获取导出数据
     * @param params
     * @return
     */
    List<RszpDto> getListForSearchExp(Map<String, Object> params);
}

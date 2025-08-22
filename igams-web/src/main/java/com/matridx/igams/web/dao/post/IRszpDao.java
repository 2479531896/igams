package com.matridx.igams.web.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.RszpDto;
import com.matridx.igams.web.dao.entities.RszpModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IRszpDao  extends BaseBasicDao<RszpDto, RszpModel> {

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
     * @param rszpDto
     * @return
     */
    List<RszpDto> getListForSelectExp(RszpDto rszpDto);
    /**
     * 根据搜索条件获取导出条数
     * @param rszpDto
     * @return
     */
    int getCountForSearchExp(RszpDto rszpDto);
    /**
     * 从数据库分页获取导出数据
     * @param rszpDto
     * @return
     */
    List<RszpDto> getListForSearchExp(RszpDto rszpDto);



}

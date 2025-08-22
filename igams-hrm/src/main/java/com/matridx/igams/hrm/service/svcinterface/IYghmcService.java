package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.YghmcDto;
import com.matridx.igams.hrm.dao.entities.YghmcModel;
import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
public interface IYghmcService extends BaseBasicService<YghmcDto, YghmcModel> {
    /**
     * 批量修改
     */

    boolean updateList(YghmcDto yghmcDto);
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(YghmcDto yghmcDto, Map<String, Object> params);
    /**
     * 获取所有员工
     */
    List<String> getAllYgYhid();
    /**
     * 批量插入员工花名册信息
     */
    void insertYghmcDtos(List<YghmcDto> yghmcDtos);
    /**
     * 批量修改员工花名册信息
     */
    void updateYghmcDtos(List<YghmcDto> yghmcDtos);
    /**
     *  合同到期处理
     */
    boolean processSaveRoster(YghmcDto yghmcDto);
    /**
     * 获取员工的所有部门
     */
    List<YghmcDto> getAllRosterBm(YghmcDto yghmcDto);
    /**
     * 通过花名册信息获取所有员工合同信息
     */
    List<YghmcDto> getAllYghtxxByHmc();
    /**
     * 通过花名册信息获取所有员工离职信息
     */
    List<YghmcDto> getAllYgLzxxByHmc();

    /**
     * 获取高级筛选分类数据
     */
    Map<String,Object> getScreenClassfy();
    /**
     * 获取指定人员下级员工
     */
    List<YghmcDto> getSubordinateEmployee(YghmcDto yghmcDto);
    /**
     * 获取指定人员下级员工
     */
    List<YghmcDto> getUserByYhmOrZsxm(YghmcDto yghmcDto);
    /**
     * 通过用户id获取员工花名册信息
     */
    YghmcDto getDtoByYhId(String yhid);
    /**
     * 通过离职日期获取离职人员
     */
    List<YghmcDto> getLzryByRq(YghmcDto yghmcDto);
    /**
     * 获取所有员工花名册
     */
    List<YghmcDto> getAllList();
    /**
     * 递归获取下级
     */
    void recursiveGetInfo(List<YghmcDto> allList,List<String> ddids,int cj,List<String> ryids);
    /*
        获取花名册信息
     */
    List<YghmcDto> getYghmcDtos(YghmcDto yghmcDto);

    /**
     * @Description: 查询员工花名册信息
     * @param
     * @return com.matridx.igams.hrm.dao.entities.YghmcDto
     * @Author: 郭祥杰
     * @Date: 2024/7/22 15:53
     */
    YghmcDto queryYghmcDto(String yghmcid);
}

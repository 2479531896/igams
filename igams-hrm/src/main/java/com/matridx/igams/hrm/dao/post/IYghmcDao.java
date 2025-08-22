package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.YghmcDto;
import com.matridx.igams.hrm.dao.entities.YghmcModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
@Mapper
public interface IYghmcDao extends BaseBasicDao<YghmcDto, YghmcModel> {
    /**
     * 批量修改
     */
    boolean updateList(YghmcDto yghmcDto);
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(YghmcDto yghmcDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<YghmcDto> getListForSearchExp(YghmcDto yghmcDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<YghmcDto> getListForSelectExp(YghmcDto yghmcDto);

    List<String> getAllYgYhid();

    /**
     * 批量插入员工花名册信息
     */
    void insertYghmcDtos(List<YghmcDto> yghmcDtos);
    /**
     * 批量修改员工花名册信息
     */
    void updateYghmcDtos(List<YghmcDto> yghmcDtos);

    boolean updateListWithDqtx(List<YghmcDto> btxlist);

    boolean updateDqtxByIds(YghmcDto yghmcDto);
    /**
     * 获取员工的所有部门
     */
    List<YghmcDto> getAllRosterBm(YghmcDto yghmcDto);

    List<YghmcDto> getAllYghtxxByHmc();

    List<YghmcDto> getAllYgLzxxByHmc();
    /**
     * 获取高级筛选分类数据
     */
    Map<String,Object> getScreenClassfy();
    /**
     *  获取指定人员下级员工
     */
    List<YghmcDto> getSubordinateEmployee(YghmcDto yghmcDto);
    /**
     * 获取指定人员下级员工
     */
    List<YghmcDto> getUserByYhmOrZsxm(YghmcDto yghmcDto);
    /**
     * @description 通过用户id获取员工花名册信息
     */
    YghmcDto getDtoByYhId(String yhid);
    /**
     * @description 通过离职日期获取离职人员
     */
    List<YghmcDto> getLzryByRq(YghmcDto yghmcDto);
    /**
     * 获取所有员工花名册
     */
    List<YghmcDto> getAllList();
    /*
    获取花名册信息
 */
    List<YghmcDto> getYghmcDtos(YghmcDto yghmcDto);

    /**
     * @Description: 生成记录编号
     * @param jlbh
     * @return java.lang.String
     * @Author: 郭祥杰
     * @Date: 2024/7/22 16:01
     */
    String queryJlbh(String jlbh);
}

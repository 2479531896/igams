package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.DkjejlDto;
import com.matridx.igams.storehouse.dao.entities.DkjejlModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IDkjejlDao extends BaseBasicDao<DkjejlDto, DkjejlModel> {
    /*
        批量插入
     */
    boolean insertList(List<DkjejlDto> dkjejlDtos);
    /*
        通过业务id删除
     */
    void deleteByYwid(DkjejlDto dkjejlDto);
    /*
        获取所属到款总金额
     */
    DkjejlDto getAllDkje(DkjejlDto dkjejlDto);
    /*
        批量删除
    */
    void deleteByYwids(DkjejlDto dkjejlDto);
    /*
        选择导出
     */
    List<DkjejlDto> getListForSelectExp(DkjejlDto dkjejlDto);
    /*
           搜索导出条数
        */
    int getCountForSearchExp(DkjejlDto dkjejlDto);
    /*
           搜索导出
        */
    List<DkjejlDto> getListForSearchExp(DkjejlDto dkjejlDto);
    /*
     获取未到款列表
    */
    List<DkjejlDto> getPagedNotDtoList(DkjejlDto dkjejlDto);
    /*
        未到款列表查看
    */
    DkjejlDto getNotPaymentReceivedDto(DkjejlDto dkjejlDto);
    /**
     * 未到款列表选中导出
     */
    List<DkjejlDto> getNotListForSelectExp(DkjejlDto dkjejlDto);
    /**
     * 未到款列表根据搜索条件获取导出条数
     */
    int getNotCountForSearchExp(DkjejlDto dkjejlDto);
    /**
     * 未到款列表根据搜索条件分页获取导出信息
     */
    List<DkjejlDto> getNotListForSearchExp(DkjejlDto dkjejlDto);
}

package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.BfdxlxrglDto;
import com.matridx.igams.wechat.dao.entities.BfplszDto;
import com.matridx.igams.wechat.dao.entities.KhzyfpDto;
import com.matridx.igams.wechat.dao.entities.KhzyfpModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IKhzyfpDao extends BaseBasicDao<KhzyfpDto, KhzyfpModel>{

    /**
     * 客户列表
     */
    List<KhzyfpDto> getPagedCustomerList(KhzyfpDto khzyfpDto);
    /**
     * 拜访记录列表(分页)
     */
    List<KhzyfpDto> getPagedVisitRecordsList(KhzyfpDto khzyfpDto);
    /**
     * 合并
     */
    boolean mergeList(List<KhzyfpDto> list);
    /**
     * 批量新增
     */
    boolean insertList(List<KhzyfpDto> list);
    /**
     * 删除无用数据
     */
    boolean delUselessData(KhzyfpDto khzyfpDto);
    /**
     * 还原数据
     */
    boolean restoreData(List<KhzyfpDto> list);
    /**
     * 删除无效的拜访频率数据
     */
    boolean delVisitFrequencyData(KhzyfpDto khzyfpDto);
    /*
    通过用户名删除资源分配信息
    */
    boolean deleteByYhms(List<String> yhms);

}

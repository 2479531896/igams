package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.BfdxglDto;
import com.matridx.igams.wechat.dao.entities.BfdxglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IBfdxglDao extends BaseBasicDao<BfdxglDto, BfdxglModel>{

    /**
     * 从数据库分页获取导出异常信息
     */
    List<BfdxglDto> getListForSelectExp(BfdxglDto bfdxglDto);

    /**
     * 查询可以导出的条数
     */
    int getCountForSearchExp(BfdxglDto bfdxglDto);

    /**
     * 从数据库分页获取导出送检异常
     */
    List<BfdxglDto> getListForSearchExp(BfdxglDto bfdxglDto);
    /**
     * 合并
     */
    boolean merge(BfdxglDto bfdxglDto);
    /**
     * 对象编码流水号
     */
    String getCodeSerial(Map<String, Object> map);

    /**
     * 根据dwmc获取拜访对象
     * @param bfdxglDto
     * @return
     */
    List<BfdxglDto> getDtoByDwmc(BfdxglDto bfdxglDto);
}

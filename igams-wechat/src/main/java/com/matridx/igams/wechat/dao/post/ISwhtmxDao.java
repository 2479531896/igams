package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SwhtmxDto;
import com.matridx.igams.wechat.dao.entities.SwhtmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商务合同明细(IgamsSwhtmx)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-04 13:52:16
 */
@Mapper
public interface ISwhtmxDao extends BaseBasicDao<SwhtmxDto, SwhtmxModel> {

    /**
     * 批量插入
     */
    int insertList(List<SwhtmxDto> list);

    /**
     * 获取客户合同明细
     */
    List<SwhtmxDto> getAllList(SwhtmxDto swhtmxDto);

    /**
     * 获取伙伴收费明细
     */
    List<SwhtmxDto> getHbsfInfo(SwhtmxDto swhtmxDto);
    /**
     * 批量修改
     */
    Boolean updateList(List<SwhtmxDto> list);

    int modProjectInfo(SwhtmxDto swhtmxDto);
}


package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.wechat.dao.entities.SysxxDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SysxxModel;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISysxxDao extends BaseBasicDao<SysxxDto, SysxxModel>{

    /**
     * 根据对方实验室名称获取实验室信息
     * @param map
     * @return
     */
    SysxxDto getSysxxDtoByDfsysmc(Map<String,String> map);

    /**
     * 获取列表数据
     */
    List<SysxxDto> getPageSysxxDtoList(SysxxDto sysxxDto);
}

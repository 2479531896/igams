package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.JpmxDto;
import com.matridx.igams.hrm.dao.entities.JpmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IJpmxDao extends BaseBasicDao<JpmxDto, JpmxModel>{

    /**
     * 批量保存
     */
    boolean insertList(List<JpmxDto> list);

    /**
     * 批量修改
     */
    boolean updateList(List<JpmxDto> list);
	/*
        获取抽奖明细信息
     */
    List<JpmxDto> getLotteryInfos(JpmxDto jpmxDto);
    /*
         更新剩余数量
      */
    boolean updateSysl(JpmxDto jpmxDto);

}

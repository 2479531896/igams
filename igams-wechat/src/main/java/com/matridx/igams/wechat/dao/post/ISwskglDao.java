package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.KdxxDto;
import com.matridx.igams.wechat.dao.entities.SwskglDto;
import com.matridx.igams.wechat.dao.entities.SwskglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISwskglDao extends BaseBasicDao<SwskglDto, SwskglModel>{

    /**
     * 获取总金额
     */
    String getTotalAmount(String yszkid);

}

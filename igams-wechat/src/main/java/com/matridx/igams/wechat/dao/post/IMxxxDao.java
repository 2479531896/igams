package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.MxxxDto;
import com.matridx.igams.wechat.dao.entities.MxxxModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMxxxDao extends BaseBasicDao<MxxxDto, MxxxModel> {

    /**
     * 生成订单号
     * @param
     * @return
     */
    String getDdhSerial(String prefix);

    /**
     * 根据订单查询你信息
     */
    MxxxDto getDtoByOrderNo(MxxxDto mxxxDto);

}

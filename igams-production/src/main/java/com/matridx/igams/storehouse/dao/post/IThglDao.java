package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.ThglDto;
import com.matridx.igams.storehouse.dao.entities.ThglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IThglDao  extends BaseBasicDao<ThglDto, ThglModel> {
    /**
     * 自动生成退货单号
     * @param prefix
     * @return
     */
    String getThdhSerial(String prefix);
    /**
     * 校验退货单号是否重复
     * @param thglDto
     * @return
     */
    ThglDto getDtoByThdh(ThglDto thglDto);
    /**
     *获取退货审核信息
     */
    List<ThglDto> getPagedAuditReturnManagement(ThglDto thglDto);
    /**
     * 获取退货审核列表
     * @param list
     * @return
     */
    List<ThglDto> getAuditListReturnManagement(List<ThglDto> list);
}

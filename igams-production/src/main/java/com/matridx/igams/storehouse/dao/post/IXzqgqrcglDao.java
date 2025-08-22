package com.matridx.igams.storehouse.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.XzqgqrcglDto;
import com.matridx.igams.storehouse.dao.entities.XzqgqrcglModel;

import java.util.List;

@Mapper
public interface IXzqgqrcglDao extends BaseBasicDao<XzqgqrcglDto, XzqgqrcglModel>{
    /**
     * 加入确认车
     * @param xzqgqrcglDto
     * @return
     */
    boolean saveConfirmCar(XzqgqrcglDto xzqgqrcglDto);
    /**
     * 删除确认车
     * @param xzqgqrcglDto
     * @return
     */
    boolean delConfirmCar(XzqgqrcglDto xzqgqrcglDto);

    /**
     * 根据人员id获取确认车列表
     * @param xzqgqrcglDto
     * @return
     */
    List<XzqgqrcglDto> getDtoListByRyid(XzqgqrcglDto xzqgqrcglDto);

    /**
     * 获取不在确认车的行政请购未确认信息
     * @param xzqgqrcglDto
     * @return
     */
    List<XzqgqrcglDto> getUnconfirmMsgNotInCar(XzqgqrcglDto xzqgqrcglDto);
}

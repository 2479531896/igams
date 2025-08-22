package com.matridx.igams.storehouse.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.XzqgqrcglDto;
import com.matridx.igams.storehouse.dao.entities.XzqgqrcglModel;
import com.matridx.igams.storehouse.dao.post.IXzqgqrcglDao;
import com.matridx.igams.storehouse.service.svcinterface.IXzqgqrcglService;

import java.util.List;

@Service
public class XzqgqrcglServiceImpl extends BaseBasicServiceImpl<XzqgqrcglDto, XzqgqrcglModel, IXzqgqrcglDao> implements IXzqgqrcglService{
    /**
     * 加入确认车
     * @param xzqgqrcglDto
     * @return
     */
    public boolean saveConfirmCar(XzqgqrcglDto xzqgqrcglDto){
        return dao.saveConfirmCar(xzqgqrcglDto);
    }
    /**
     * 删除确认车
     * @param xzqgqrcglDto
     * @return
     */
    public boolean delConfirmCar(XzqgqrcglDto xzqgqrcglDto){
        return dao.delConfirmCar(xzqgqrcglDto);
    }
    /**
     * 根据人员id获取确认车列表
     * @param xzqgqrcglDto
     * @return
     */
    public List<XzqgqrcglDto> getDtoListByRyid(XzqgqrcglDto xzqgqrcglDto){
        return dao.getDtoListByRyid(xzqgqrcglDto);
    }

    /**
     * 获取不在确认车的行政请购未确认信息
     * @param xzqgqrcglDto
     * @return
     */
    public List<XzqgqrcglDto> getUnconfirmMsgNotInCar(XzqgqrcglDto xzqgqrcglDto){
        return dao.getUnconfirmMsgNotInCar(xzqgqrcglDto);
    }
}

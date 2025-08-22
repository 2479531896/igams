package com.matridx.server.wechat.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.CpwjDto;
import com.matridx.server.wechat.dao.entities.CpwjModel;

public interface ICpwjService extends BaseBasicService<CpwjDto, CpwjModel>{
    /**
     * @description 新增保存产品说明书
     * @param cpwjDto
     * @return
     */
    boolean addSaveProductManual(CpwjDto cpwjDto) throws BusinessException;

    /**
     * @Description: 修改保存
     * @param cpwjDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2025/4/21 10:25
     */
    boolean modSaveProductManual(CpwjDto cpwjDto) throws BusinessException;
    /**
     * @description 通过版本号和产品代码获取产品说明书信息
     * @param cpwjDto
     * @return
     */
    CpwjDto getDtoByBbhAndCpdm(CpwjDto cpwjDto);
    /**
     * @description 删除产品说明书
     * @param cpwjDto
     * @return
     */
    boolean delProductManual(CpwjDto cpwjDto) throws BusinessException;
    /**
     * @description 修改产品说明书是否公开
     * @param cpwjDto
     * @return
     */
    boolean publicornotProductManual(CpwjDto cpwjDto) throws BusinessException;
}

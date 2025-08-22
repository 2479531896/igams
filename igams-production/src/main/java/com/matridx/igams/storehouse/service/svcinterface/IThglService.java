package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.ThglDto;
import com.matridx.igams.storehouse.dao.entities.ThglModel;

import java.util.List;

public interface IThglService extends BaseBasicService<ThglDto, ThglModel> {
    /**
     * 自动生成退货单号
     * @return
     */
    String generateThdh();
    /**
     * 检验退货单号重复
     * @return
     */
    boolean getDtoByThdh(ThglDto thglDto);
    /**
     * 新增保存退货信息
     * @return
     */
    boolean addSaveReturnManagements(ThglDto thglDto) throws BusinessException;
    /**
     * 修改保存退货信息
     * @return
     */
    boolean modSaveReturnManagements(ThglDto thglDto) throws BusinessException;
    /**
     * 删除退货信息
     * @return
     */
    boolean delReturnManagement(ThglDto thglDto) throws BusinessException;

    /**
     * @Description 获取退货审核数据
     * @Param
     * @Return
     */
    List<ThglDto> getPagedAuditReturnManagement(ThglDto thglDto);

}

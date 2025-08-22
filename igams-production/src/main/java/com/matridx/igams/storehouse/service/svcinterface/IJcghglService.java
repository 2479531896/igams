package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.JcghglDto;
import com.matridx.igams.storehouse.dao.entities.JcghglModel;

import java.util.List;


public interface IJcghglService extends BaseBasicService<JcghglDto, JcghglModel>{
    /**
     * 审核列表
     * @param
     * @return
     */
    List<JcghglDto> getPagedAuditRepaid(JcghglDto jcghglDto);
    /**
     * 自动生成归还单号
     * @param jcghglDto
     * @return
     */
    String generateGhdh(JcghglDto jcghglDto);
    /**
     * 根据归还单号查询
     * @param
     * @return
     */
    List<JcghglDto> getDtoByGhdh(String ghdh);

    /**
     * 归还保存
     */
    boolean saveRepaidBorrowing(JcghglDto jcghglDto) throws BusinessException;
    /**
     * 归还修改保存
     */
    boolean modSaveRepaid(JcghglDto jcghglDto) throws BusinessException;
    /**
     * 废弃
     * @param jcghglDto
     * @return
     */
    boolean discard(JcghglDto jcghglDto);
    /**
     * 删除
     * @param jcghglDto
     * @return
     */
    boolean deleteDto(JcghglDto jcghglDto);
}

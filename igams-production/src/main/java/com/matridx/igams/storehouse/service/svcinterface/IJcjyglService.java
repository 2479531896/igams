package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.JcjyglDto;
import com.matridx.igams.storehouse.dao.entities.JcjyglModel;

import java.util.List;
import java.util.Map;


public interface IJcjyglService extends BaseBasicService<JcjyglDto, JcjyglModel>{

    /**
     * 自动生成借用单号
     * @param jcjyglDto
     * @return
     */
    String generateJydh(JcjyglDto jcjyglDto);

    /**
     * 自动生成OA借用单号
     * @param jcjyglDto
     * @return
     */
    String generateJydhOA(JcjyglDto jcjyglDto);

    /**
     * 借用车保存
     */
    boolean saveAddBorrowing(JcjyglDto jcjyglDto) throws BusinessException;

    /**
     * 借用借用删除
     */
    boolean delBorrowing(JcjyglDto jcjyglDto) throws BusinessException;

    /**
     * 修改借出借用信息
     * @param
     * @return
     * @throws BusinessException
     */
    boolean modSaveBorrowing(JcjyglDto jcjyglDto) throws BusinessException;

    /**
     * 审核修改借出借用信息
     * @param
     * @return
     * @throws BusinessException
     */
    boolean seniorModSaveBorrowing(JcjyglDto jcjyglDto) throws BusinessException;

    /**
     * 审核列表
     * @param
     * @return
     */
    List<JcjyglDto> getPagedAuditJcjyglDto(JcjyglDto jcjyglDto);

    /**
     * 获取列表信息
     */
    List<JcjyglDto> getPagedDtoList(JcjyglDto jcjyglDto);

    List<JcjyglDto> getJcjyWithKh();

    List<JcjyglDto> getJcjyxxByDwid(JcjyglDto jcjyglDto);
    /**
     * 根据搜索条件获取导出条数
     * @param jcjyglDto
     * @return
     */
    int getCountForSearchExp(JcjyglDto jcjyglDto, Map<String, Object> params);

    boolean updateWlxx(JcjyglDto jcjyglDto);
    /**
     * 负责人设置保存
     */
    void updateFzrByYfzr(JcjyglDto jcjyglDto);
}

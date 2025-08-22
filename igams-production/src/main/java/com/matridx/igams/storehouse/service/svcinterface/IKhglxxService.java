package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.storehouse.dao.entities.KhglDto;
import com.matridx.igams.storehouse.dao.entities.KhglModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.Map;


public interface IKhglxxService extends BaseBasicService<KhglDto, KhglModel> {

    /**
     * 查询客户编码最大值
     *
     * @param khglDto
     * @return
     */
    Integer countMax(KhglDto khglDto);

    /**
     * 通过ids删除客户管理信息
     *
     * @param khglDto
     * @return
     */
    boolean deleteByKhglids(KhglDto khglDto);

    /**
     * 通过客户代码查询客户管理信息
     *
     * @param khdm
     * @return
     */
    KhglDto getKhglDtoByKhdm(String khdm);

    /**
     * 根据搜索条件获取导出条数
     * @param khglDto
     * @return
     */
    int getCountForSearchExp(KhglDto khglDto, Map<String, Object> params);
    /**
     * 新增保存客户管理信息
     * @param khglDto
     * @return
     */
    boolean insertKhglxx(KhglDto khglDto) throws BusinessException;
    /**
     * 修改保存客户管理信息
     * @param khglDto
     * @return
     */
    boolean updateKhglxx(KhglDto khglDto) throws BusinessException;
    /**
     * 通过客户简称查询客户管理信息
     *
     * @return
     */
    KhglDto getKhglDtoByKhjc(String khjc);
    /**
     * 批量更新
     */
    boolean updateList(KhglDto khglDto);
}

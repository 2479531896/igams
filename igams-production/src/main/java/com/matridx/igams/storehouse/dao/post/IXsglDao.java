package com.matridx.igams.storehouse.dao.post;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.FhglDto;
import com.matridx.igams.storehouse.dao.entities.XsglDto;
import com.matridx.igams.storehouse.dao.entities.XsglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IXsglDao extends BaseBasicDao<XsglDto, XsglModel>{

    /**
     * 生成oa销售订单号
     * @return
     */
    String getOAxsddh(String str);

    /**
     * 废弃按钮
     * @param xsglDto
     * @return
     */
    boolean discard(XsglDto xsglDto);

    /**
     * 获取审核列表
     * @param xsglDto
     * @return
     */
    List<XsglDto> getPagedAuditSale(XsglDto xsglDto);
    /**
     * 审核列表
     * @param list
     * @return
     */
    List<XsglDto> getAuditListSale(List<XsglDto> list);
    /**
     * 验证
     * @param xsglDto
     * @return
     */
    XsglDto verify(XsglDto xsglDto);

    /**
     * 销售列表选中导出
     * @return
     */
    List<XsglDto> getListForSelectExp(XsglDto xsglDto);

    /**
     * 搜索条件分页获取导出信息
     * @return
     */
    List<XsglDto> getListForSearchExp(XsglDto xsglDto);

    /**
     * 根据搜索条件获取导出条数
     * @return
     */
    int getCountForSearchExp(XsglDto xsglDto);

    List<XsglDto> getXsxxWithKh();

    List<XsglDto> getXsxxByKhid(XsglDto xsglDto);
	
    boolean updateWlxx(XsglDto xsglDto);
	
	XsglDto getDtoByJyId(String jcjyid);
    List<XsglDto> getDtoListByJyId(String jcjyid);
    /**
     * 将钉钉实例ID至为空
     *
     * @param xsglDto
     */
    void updateDdslidToNull(XsglDto xsglDto);
    /**
     *根据ddslid获取信息
     * @param ddspid
     * @return
     */
    XsglDto getDtoByDdslid(String ddspid);
    /**
     * 获取钉钉审批人信息
     * @param
     * @return
     */
    XsglDto getSprxxByDdid(String ddid);
    /**
     * 根据审批人用户ID获取角色信息
     * @param
     * @return
     */
    List<XsglDto> getSprjsBySprid(String sprid);
    /**
     * 负责人设置保存
     */
    void updateFzrByYfzr(XsglDto xsglDto);
    /**
     * @description 获取销售数据
     * @param xsglDto
     * @return
     */
    List<XsglDto> getPagedSaleData(XsglDto xsglDto);
    /**
     * @param fhglDtos
     * @description 修改发货状态
     */
    void updateFhzt(List<FhglDto> fhglDtos);
    /*
        修改应收款信息
     */
    boolean updateYsk(XsglDto xsglDto);
    /*
        批量修改应收款
    */
    void updateYsks(List<XsglDto> xsglDtos);
}

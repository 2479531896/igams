package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.ZlxyDto;
import com.matridx.igams.production.dao.entities.ZlxyModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
@Mapper
public interface IZlxyDao extends BaseBasicDao<ZlxyDto, ZlxyModel> {
    /**
     * 修改合同新旧
     */
    void modHtxjByGysid(ZlxyDto zlxyDto);

    /**
     * 获取审核数据
     */
    List<ZlxyDto> getPagedAuditAgreement(ZlxyDto zlxyDto);
    List<ZlxyDto> getAuditListAgreemen(List<ZlxyDto> zlxyDtos);
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(ZlxyDto zlxyDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<ZlxyDto> getListForSearchExp(ZlxyDto zlxyDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<ZlxyDto> getListForSelectExp(ZlxyDto zlxyDto);
    ZlxyDto getDtoByDdslid(ZlxyDto zlxyDto);
    /**
     * 根据审批人用户ID获取角色信息
     */
    List<ZlxyDto> getSprjsBySprid(String sprid);
    /**
     * 将钉钉实例id置为null
     */
    void updateDdslidToNull(ZlxyDto zlxyDto);
    /**
     * 生成合同文档
     */
    Map<String,Object> getContractMap(ZlxyDto zlxyDto);
    /**
     * 获取质量协议明细
     */
    List<Map<String,String>> getContractListMap(ZlxyDto zlxyDto);

    /**
     * 获取最新的数据
     */
    ZlxyDto selectLastDto(String gysid);
    /**
     * 获取供应商ids
     */
    List<String> selectGysids(ZlxyDto zlxyDto);
    List<ZlxyDto> selectZlxyDtos(List<String> list);
    void updateList(List<ZlxyDto> list);
    /**
     * 双章合同保存
     */
    boolean formalSaveAgreementContract(ZlxyDto zlxyDto);
    /**
     * @description 获取质量协议编号
     */
    String getAgreementContractSerial(@Param("prefix") String prefix, @Param("num") String num);
    /**
     *  通过质量协议编号获取信息
     */
    List<ZlxyDto> getListByZlxybh(ZlxyDto zlxyDto);
}

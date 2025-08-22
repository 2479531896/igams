package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.YxhtDto;
import com.matridx.igams.production.dao.entities.YxhtModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IYxhtDao extends BaseBasicDao<YxhtDto, YxhtModel>{
    /**
     * 查询合同期限
     */
    List<YxhtDto> selectHtqxList();
    /**
     *  通过合同编号获取营销合同信息
     */
    List<YxhtDto> getListByHtbh(YxhtDto yxhtDto);
    /**
     * 页面修改
     */
    void updatePageEvent(YxhtDto yxhtDto);
    /**
     *  通过钉钉spslid获取数据
     */
    YxhtDto getDtoByDdslid(String ddslid);
    /**
     *  通过钉钉sprid获取数据
     */
    List<YxhtDto> getSprjsBySprid(String sprid);
    /**
     * 修改ddslid为null
     */
    void updateDdslidToNull(YxhtDto yxhtDto);
    /**
     *获取营销合同审核信息
     */
    List<YxhtDto> getPagedAuditMarketingContract(YxhtDto yxhtDto);
    /**
     *  营销合同审核列表数据
     */
    List<YxhtDto> getAuditListMarketingContract(List<YxhtDto> t_sbyzList);
    /**
     *  获取营销合同关联单号
     */
    List<YxhtDto> getDhByHtIds(YxhtDto yxhtDto);
    /**
     *  获取直属领导钉钉信息
     */
    List<YxhtDto> getInfoByZsld(YxhtDto yxhtDto);
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(YxhtDto yxhtDto);
    /**
     * 从数据库分页获取导出数据
     */
    List<YxhtDto> getListForSearchExp(YxhtDto yxhtDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<YxhtDto> getListForSelectExp(YxhtDto yxhtDto);
    /**
     * @description 获取流水号
     */
    String getMarketingContractSerial(String prefix);
}

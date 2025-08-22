package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.MngsmxjgDto;
import com.matridx.igams.bioinformation.dao.entities.MngsmxjgModel;
import com.matridx.igams.bioinformation.dao.entities.WkcxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface IMngsmxjgService extends BaseBasicService<MngsmxjgDto, MngsmxjgModel> {

    Integer existsTable(String tableName);

    /**
     * 修改物种相关信息
     */
    Integer updateWzxx(MngsmxjgDto mngsmxjgDto);

    /**
     * 修改物种相关信息
     */
    Boolean reviewAudit(List<MngsmxjgDto> mngsmxjgList, WkcxDto wkcxDto, User user) throws BusinessException;

    /**
     * 删除无关联数据
     */
    Integer deleteByNull(MngsmxjgDto mngsmxjgDto);

    /**
     * 分组查询明细信息
     */
    List<MngsmxjgDto> getDtoListGroupBy(MngsmxjgDto mngsmxjgDto);

    /**
     * 分组查询明细信息未匹配
     */
    MngsmxjgDto getDtoNull(MngsmxjgDto mngsmxjgDto);

    /**
     * 批量修改
     */
    Boolean updateListLs(List<MngsmxjgDto> mngsmxjgList);
    /**
     * 查询临时信息
     */
    List<MngsmxjgDto> getDtoListLs(MngsmxjgDto mngsmxjgDto);

    List<MngsmxjgDto> getListInfo(MngsmxjgDto mngsmxjgDto,WkcxDto wkcxDto);
    List<MngsmxjgDto> getReviewInfo(List<MngsmxjgDto> reviewInfo);
    List<MngsmxjgDto> getReviewFilterInfo(MngsmxjgDto mngsmxjgDto,WkcxDto wkcxDto) throws BusinessException ;

    /**
     * 查询审核信息
     */
    List<MngsmxjgDto> getInfo(MngsmxjgDto mngsmxjgDto);
    /**
     * 批量插入明细结果临时表
     */
    boolean batchInsertLs(List<MngsmxjgDto> mngsmxjgList);

    /**
     * 批量修改
     */
    Boolean updateListFz(MngsmxjgDto mngsmxjgDto);

    List<MngsmxjgDto> getGidAndsfdc(MngsmxjgDto mngsmxjgDto);

    Boolean updateGidAndsfdc(List<MngsmxjgDto> list);

    /**
     * 博精图片
     */
    Boolean updateBjtppath(List<MngsmxjgDto> list);
    /**
     * 修改未默认关注的
     */
    boolean updateGzdSetMr(List<MngsmxjgDto> mngsmxjgList);

    /**
     * 查询临时信息
     */
    List<MngsmxjgDto> getDtoListLsCs(MngsmxjgDto mngsmxjgDto);

    /**
     * 批量修改明细结果临时表
     */
    boolean batchUpdateLs(List<MngsmxjgDto> mngsmxjgList);

    /**
     * 根据taxid修改关注度
     */
    boolean updateGzdToAijgList(List<MngsmxjgDto> mngsmxjgList);
    /**
     * 查找出文库结果明细临时表中所有的数据
     */
    List<MngsmxjgDto> getLsAll(MngsmxjgDto mngsmxjgDto);

    /**
     * 全部字段的list数据插入到文库明细结果表中
     */
    boolean insertLsAllToWkjgmx(List<MngsmxjgDto> mngsmxjgDtoList);

    /**
     * 清空文库结果明细临时表中所有的数据
     */
    boolean delLsAll(MngsmxjgDto mngsmxjgDto);

    /**
     * 查询是特检，不是REM的文库数据
     */
    List<MngsmxjgDto> getMxjgByTjAndNotREM(MngsmxjgDto mngsmxjgDto);
    /**
     *  得到微生物列表
     */
    List<MngsmxjgDto> getListbyTaxids(MngsmxjgDto mngsmxjgDto);

    /**
     * 根据关注度查找文库编号并对文库编号去重
     */
    Set<String> getWkbhByGzh(MngsmxjgDto mngsmxjgDto);

    /**
     * 根据物种ID查找文库编号并对文库编号去重
     */
    Set<String> getWkbhByTaxid(MngsmxjgDto mngsmxjg);

    /**
     * 将表中关注度为2，fl为核心库0的数据gzd改为4（疑似）
     */
    boolean updateGzdToFour(MngsmxjgDto dto);

    /**
     * aijg同步到gzd
     */
    boolean updateGzdToAijg(MngsmxjgDto dto);

    /**
     * 根据文库编号bbh得到所有fl和关注度
     */
    List<MngsmxjgDto> getSampleTreeList(MngsmxjgDto dto);

    List<List<MngsmxjgDto>> dealMngsmxInfo(List<MngsmxjgDto> list);

    /**
     * 根据版本号和wkbh 修改fl
     */
    boolean updateListLsFl(MngsmxjgDto dto);

    boolean updateGzdByIds(MngsmxjgDto dto);
    /**
     * 获取合并数据
     */
    List<MngsmxjgDto> getMngsInfo(MngsmxjgDto dto);
    /**
     * AI结果导出
     */
    List<MngsmxjgDto> getListForExp(Map<String, Object> params);
    /**
     * 审核结果导出
     */
    List<MngsmxjgDto> getListForAuditExp(Map<String, Object> params);
    /**
     * 获取高关注数据
     */
    List<MngsmxjgDto> getHighConcernList(MngsmxjgDto mngsmxjgDto);
    /**
     * 获取疑似数据
     */
    List<MngsmxjgDto> getSuspectedList(MngsmxjgDto mngsmxjgDto);
    /**
     * 获取导出数据
     */
    List<MngsmxjgDto> getExportList(MngsmxjgDto mngsmxjgDto);

    /**
     * 背景数据库导出数据
     */
    List<Map<String,String>> backgroundDataBase(MngsmxjgDto mngsmxjgDto);
}

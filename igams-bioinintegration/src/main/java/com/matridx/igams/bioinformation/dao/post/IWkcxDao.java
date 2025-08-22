package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.bioinformation.dao.entities.BioXpxxDto;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.bioinformation.dao.entities.WkcxDto;
import com.matridx.igams.bioinformation.dao.entities.WkcxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IWkcxDao extends BaseBasicDao<WkcxDto, WkcxModel> {
    /**
     * 获取最新的文库测序版本
     */
    WkcxDto getDtoNew(WkcxDto wkcxDto);
    /**
     * 根据内部编码获取list
     */
    WkcxDto getListByNbbm(WkcxDto wkcxDto);

    /**
     * 根据内部编码获取当前样本其他文库
     */
    List<WkcxDto>getByNbbmToList(WkcxDto wkcxDto);
    /**
     * 根据文库编号获取芯片名
     */
    WkcxDto getXpmBywkhb(WkcxDto wkcxDto);
    /**
     * 根据测序开始时间获取测序仪基本信息
     */
    List<Map<String,String>> getCxyInfo(WkcxDto wkcxDto);

    /**
     * 根据子csid取到父基础数据
     */
    List<JcsjDto> getCxyProvinces(List<String> cyxidlist);

    /**
     * 根据父基础数据查找出子基础数据
     */
    List<JcsjDto> getCxyJcsjByFcsid(JcsjDto cxydto);

    /**
     * 根据芯片ID查找文库测序数据
     */
    List<WkcxDto> getWkcxByXpid(BioXpxxDto xp);
    /**
     * 根据芯片IDs查找文库测序数据
     */
    List<WkcxDto> getWkcxByXpids(BioXpxxDto xp);
    /**
     * 得到数据版本表
     */
    List<String> getTableInfo(WkcxDto wkcxDto);


    /**
     * 那文库相关信息
     */
    List<WkcxDto> getLibraryInfo(WkcxDto wkcxDto);

    /**
     * 查找sample列表数据
     */
    List<WkcxDto> getPagedWkcx(WkcxDto wkcxDto);
    /**
     * 查找sample列表数据
     */
    List<WkcxDto> getpageWkcx(WkcxDto wkcxDto);
    /**
     * 报告导出
     */
    WkcxDto getExport(WkcxDto wkcxDto);

    /**
     * 临床文库数和研发文库
     */
    Map<String,Object> getSampleAndLibraryBytime(Map<String,Object>map);

    /**
     * 临床标本数量
     */
    Map<String,Object> getClinicalSampleBytime(Map<String,Object>map);

    /**
     * 研发标本数
     */
    Map<String,Object> getDevelopmentSampleBytime(Map<String,Object>map);

    /**
     * 一段时间中每天文库测序的totalreads数据
     */
    List<Map<String, String>> getSequenceByTime(WkcxDto wkcxDto);

    /**
     * stats报告统计页面--- 报告统计右，统计报告中每种样本类型占比
     */
    List<Map<String, String>> getRatioOfSampleType(WkcxDto wkcxDto);

    /**
     * 查找出文库测序中含有的样本类型
     */
    List<String> getYblxType(WkcxDto wkcxDto);

    /**
     * stats报告统计页面--- 柱状图，统计报告中每种样本类型中四周关注度类型占比
     */
    List<Map<String, String>> getYblxGzd(WkcxDto wkcxDto);

    /**
     * 获取某段时间内的年月份
     */
    List<String> getYearMonthTime(WkcxDto wkcxDto);

    /**
     * 糊查找查mngs明细结果表名
     */
    List<String> getMngsmxjgTableName();

    /**
     * 获取传入时间范围内需要查找的mngs明细表名时间后缀
     */
    List<String> getTableTimeSuffix(Map<String, List<String>> paramMap);
    /**
     * 根据ids查找列表数据
     */
    List<WkcxDto> getListByIds(WkcxDto wkcxDto);
    /**
     * 生信数据同步
     */
    List<WkcxDto> syncData(WkcxDto wkcxDto);
    /**
     * 批量新增
     */
    boolean insertList(List<WkcxDto> list);
    /**
     * 获取分析版本信息
     */
    List<WkcxDto> getAnalysisParams();

    /**
     * 批量更新
     */
    int updateList(List<WkcxDto> list);
    /**
     * 模版结果导出
     */
    List<WkcxDto> getListForExp(WkcxDto wkcxDto);
    /**
     * mngs实验室列表
     */
    List<WkcxDto> getLabList(WkcxDto wkcxDto);

    /**
     * 文库测序数据关联复检申请表
     */
    List<WkcxDto> getpageWkcxLeftFjsq(List<WkcxDto> sampleList);
}

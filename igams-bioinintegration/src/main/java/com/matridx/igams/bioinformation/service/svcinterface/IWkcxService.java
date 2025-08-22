package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.BioXpxxDto;
import com.matridx.igams.bioinformation.dao.entities.WkcxDto;
import com.matridx.igams.bioinformation.dao.entities.WkcxModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;


public interface IWkcxService extends BaseBasicService<WkcxDto, WkcxModel>{
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
     * 根据文库名获取芯片名称
     */
    WkcxDto getXpmBywkhb(WkcxDto wkcxDto);
    /**
     * 获取today的列表数据
     */
    Map<String, Object> getTodayList(User user,BioXpxxDto xpxxDto);
    /**
     * 通过芯片id获取列表
     */
    Map<String, Object> getTodayListByxpid(String chipid);

//    Map<String, Object> getSampleList();
    /**
     * 获取sample的列表数据
     */
    List<WkcxDto> getPagedWkcx(WkcxDto wkcxDto);

    /**
     * 获取sample的列表数据
     */
    List<WkcxDto> getpageWkcx(WkcxDto wkcxDto);

    /**
     * 文库详情
     */
    List<WkcxDto> libraryInfo(WkcxDto wkcxDto);

    /**
     * 数量统计
     */
    Map<String,Object>getQuantityStatistics(WkcxDto wkcxDto);
    /**
     * 报告导出
     */
    WkcxDto getExport(WkcxDto wkcxDto);

    /**
     * 一段时间中每天文库测序的totalreads数据
     */
    List<Map<String, String>> getSequenceByTime(WkcxDto wkcxDto);

    /**
     * stats报告统计页面--- 饼状图报告统计右，统计报告中每种样本类型占比
     */
    List<Map<String, String>> getRatioOfSampleType(WkcxDto wkcxDto);

    /**
     * stats报告统计页面--- 柱状图，统计报告中每种样本类型中四周关注度类型占比
     */
    List<Map<String, String>> getYblxGzd(WkcxDto wkcxDto);

    /**
     * 获取某段时间内的年月份
     */
    List<String> getYearMonthTime(WkcxDto wkcxDto);

    /**
     * 模糊查找查mngs明细结果表名
     */
    List<String> getMngsmxjgTableName();

    /**
     * 获取传入时间范围内需要查找的mngs明细表名时间后缀
     */
    List<String> getTableTimeSuffix(Map<String, List<String>> paramMap);

    /**
     * 保存合并操作
     */
    boolean updateDto(WkcxDto wkcxDto);
    /**
     * 根据ids查找列表数据
     */
    List<WkcxDto> getListByIds(WkcxDto wkcxDto);
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

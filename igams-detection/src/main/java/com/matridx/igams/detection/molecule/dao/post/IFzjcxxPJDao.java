package com.matridx.igams.detection.molecule.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxModel;
import org.apache.ibatis.annotations.Mapper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Mapper
public interface IFzjcxxPJDao extends BaseBasicDao<FzjcxxDto, FzjcxxModel>{
    /**
     * 根据条件下载报告压缩包
     */
     Map<String,Object> reportZipDownload(FzjcxxDto fzjcxxDto, HttpServletRequest request);
    /**
     * 根据选择查询报告信息(pdf结果)
     */
     List<FzjcxxDto> selectDownloadReportByIds(FzjcxxDto fzjcxxDto);
    /**
     * 根据条件查询需要下载的报告信息
     */
     List<FzjcxxDto> selectDownloadReport(FzjcxxDto fzjcxxDto);
    /**
     * 点击实验按钮判断检查项目
     */
     List<FzjcxxDto> checkJcxm(FzjcxxDto fzjcxxDto);
    /**
     * 更新实验状态
     */
     boolean updateSyzt(FzjcxxDto fzjcxxDto);
    /**
     * 获取jcxx
     */
     List<FzjcxxDto> getInfoByNbbh(FzjcxxDto fzjcxxDto);
    /**
     * 根据nbbh修改
     */
     Integer updateInfoByNbbh(FzjcxxDto fzjcxxDto);
     Map<String, Object> generateReport(FzjcxxDto fzjcxxDto);

    /**
     * 根据样本编号获取分子检测数据
     */
    List<FzjcxxDto> getFzjcxxListByYbbh(FzjcxxDto fzjcxxDto);

    /**
     * 普检实验流水号
     */
    String getPjSyhSerial(Map<String, Object> map);

    List<FzjcxxDto> getFzjcxxByNbbh(FzjcxxDto fzjcxxDto);

    String getNbbhSerial(String prefix);

    List<FzjcxxDto> getFzjcxxBySyh(FzjcxxDto fzjcxxDto);

    int saveFzjcxxInfo(FzjcxxDto fzjcxxDto);

    /**
     * 生成标本编号
     */
     String generateFzjcYbbh(String prefix);
    /**
     * 修改分子检测信息
     */
     int saveEditHPV(FzjcxxDto fzjcxxDto);
    /**
     * 删除按钮
     */
	  boolean delHPVDetection(FzjcxxDto fzjcxxDto);

    /**
     * 更新报告日期
     */
	  boolean updatePjBgrq(FzjcxxDto fzjcxxDto);
	  /**
     * 获取普检检测实验列表数据
     */
 List<FzjcxxDto> getPagedDetectPJlab(FzjcxxDto fzjcxxDto);
    /**
     *普检审核列表
     */
     List<FzjcxxDto> getPagedAuditList(FzjcxxDto fzjcxxDto);
     List<FzjcxxDto> getPagedAuditIdList(FzjcxxDto fzjcxxDto);
    /**
     * 根据标本申请审核的ID列表获取审核列表详细信息
     */
     List<FzjcxxDto> getAuditListByIds(List<FzjcxxDto> fzjcxxDtos);
    /**
     * 获取个人word报告
     */
     List<FjcfbDto> getReport(FjcfbDto fjcfbDto);
    /**
     * 更新审核状态
     */
     int updateZt(FzjcxxDto fzjcxxDto);
    /**
     * 更新报告日期
     */
     boolean updateBgrq(FzjcxxDto fzjcxxDto);
    /*通过审核过程中的ywid查找出分子检测信息数据list*/
    List<FzjcxxDto> getFzjcxxListByYwids(List<ShgcDto> shgcList);
    /**
     * 更新发送时间和发送标记
     */
    void updateFssjByIds(List<FzjcxxDto> fzjcxxlistx);

    /**
     * 查询样本编号是不是已经存在
     */
     Integer getCountByybbh(FzjcxxDto fzjcxxDto);
    /**
     * 样本类型转义
     */
     String escapeYblx(FzjcxxDto fzjcxxDto);
    /**
     * 接收人员转义
     */
     String escapeJsry(FzjcxxDto fzjcxxDto);
     String escapeSyry(FzjcxxDto fzjcxxDto);
        /**
         * 检测项目转义
         */
     String escapeJcxm(FzjcxxDto fzjcxxDto);
    /**
     * 检测单位转义
     */
     String escapeJcdw(FzjcxxDto fzjcxxDto);
    /**
     * 科室转义
     */
     String escapeKs(FzjcxxDto fzjcxxDto);
    /**
     * 送检单位转义
     */
     String escapeSjdw(FzjcxxDto fzjcxxDto);

    /**
     * 采集人员转义
     */
     String escapeCjry(FzjcxxDto fzjcxxDto);
    /**
     * 查询实验号
     */
     String escapeSyh(FzjcxxDto fzjcxxDto);

    /**
     * 检测结果的查询
     */
    List<FzjcxxDto> getJg(FzjcxxDto fzjcxxDto);
    /**
     * 拿到科室信息
     */
     List<FzjcxxDto> selectAllKs(FzjcxxDto fzjcxxDto);

    /**
     * 查询标本状态
     */
    List<FzjcxxDto>  getBbzts(FzjcxxDto fzjcxxDto);


    /**
     * 批量更新检测结果
     */
    boolean updateJcjgmc(List<FzjcxxDto> list);
    /**
     * 检测项目转义
     */
     String escapeJczxm(FzjcxxDto fzjcxxDto);
    /**
     * 查询角色检测单位限制
     */
     List<Map<String, String>> getJsjcdwByjsid(String jsid);
    /*
        通过分子检测项目获取分子检测数据
     */
    FzjcxxDto getDtoByFzxm(FzjcxxDto fzjcxxDto);
    /*
        修改报告日期和报告完成数
     */
    boolean updatePjBgrqAndBgwcs(FzjcxxDto fzjcxxDto);
    /**
     * 查询组装内部编码所需要的的数据
     * @param fzjcxxDto
     * @return
     */
    List<Map<String,String>> getconfirmDmInfo(FzjcxxDto fzjcxxDto);

    /**
     * 生成自定义流水号
     * @param map
     * @return
     */
    String getCustomSerial(Map<String,Object> map);
}

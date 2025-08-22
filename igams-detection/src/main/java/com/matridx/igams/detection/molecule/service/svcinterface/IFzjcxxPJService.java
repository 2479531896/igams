package com.matridx.igams.detection.molecule.service.svcinterface;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.detection.molecule.dao.entities.FzjcjgDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IFzjcxxPJService extends BaseBasicService<FzjcxxDto, FzjcxxModel>{
    /**
     * 根据条件下载报告压缩包
     */
     Map<String,Object> reportZipDownload(FzjcxxDto fzjcxxDto, HttpServletRequest request);
    /**
     * 点击实验按钮判断检查项目
     */
     List<FzjcxxDto> checkJcxm(FzjcxxDto fzjcxxDto);
    /**
     * 更新实验状态
     */
     boolean updateSyzt(FzjcxxDto fzjcxxDto);
    /**
     * 检测结果
     */
     FzjcxxDto getInfoByNbbh(FzjcxxDto fzjcxxDto) throws BusinessException;
    /**
     * 根据nbbh修改
     */
     Boolean updateInfoByNbbh(FzjcxxDto fzjcxxDto);
    /**
     * 修改结果保存
     */
     boolean resultModSaveDetectionPJ(FzjcjgDto fzjcjgDto);

    /**
     * 普检检测查找根据标号查找信息
     */
     FzjcxxDto getSampleAcceptInfo(FzjcxxDto fzjcxxDto) throws BusinessException;

    /**
     * 通过实验号获取分子检测信息
     */
    List<FzjcxxDto> getFzjcxxBySyh(FzjcxxDto fzjcxxDto);

    /**
     * 生成实验流水号
     */
    String getPjSyhSerial(FzjcxxDto fzjcxxDto);

    List<FzjcxxDto> getFzjcxxByNbbh(FzjcxxDto fzjcxxDto);

    String getFzjcxxByybbh();

     boolean saveFzjcxxInfo(FzjcxxDto fzjcxxDto) throws BusinessException;

    /**
     * 根据标本编号获取分子检测数据
     */
    List<FzjcxxDto> getFzjcxxListByYbbh(FzjcxxDto fzjcxxDto);
    /**
     * 增加分子物检信息
     */
    boolean insertHPVDetection(FzjcxxDto fzjcxxDto);
    /**
     * 修改分子检测信息
     */
     boolean saveEditHPV(FzjcxxDto fzjcxxDto);
    /**
     * 删除按钮
     */
     boolean delHPVDetection(FzjcxxDto fzjcxxDto);
	 /**
     * 获取普检检测实验列表数据
     */
    List<FzjcxxDto> getPagedDetectPJlab(FzjcxxDto fzjcxxDto);
    /**
     *普检审核列表
     */
     List<FzjcxxDto> getPagedAuditList(FzjcxxDto fzjcxxDto);
    /**
     * 获取个人word报告
     */
     List<FjcfbDto> getReport(FjcfbDto fjcfbDto);
    /*通过审核过程中的ywid查找出分子检测信息数据list*/
    List<FzjcxxDto> getFzjcxxListByYwids(List<ShgcDto> shgcList);

    /**
     * 查询阴性阳性结果
     */
     List<FzjcxxDto> getJg(FzjcxxDto fzjcxxDto);
    /**
     * 拿到科室信息
     */
     List<FzjcxxDto> selectAllKs(FzjcxxDto fzjcxxDto);

    /**
     * 得到标本状态
     */
    FzjcxxDto getBbzts(FzjcxxDto fzjcxxDto);

    /**
     * 生成样本编号
     */
     String generateYbbh(FzjcxxDto fzjcxxDto);

    /**
     * 查询角色检测单位限制
     */
     List<Map<String, String>> getJsjcdwByjsid(String jsid);

    /**
     * rabbit接收保存普检信息
     * @param fzjcxxDto
     * @return
     */
    boolean rabbitSaveFzjcxx(FzjcxxDto fzjcxxDto);
    /*
        修改报告日期和报告完成数
     */
    boolean updatePjBgrqAndBgwcs(FzjcxxDto fzjcxxDto);
    /**
     * 解析甲乙流文件
     */
    void analysisFluFile(Map<String, Object> map) throws IOException;
}
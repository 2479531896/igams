package com.matridx.server.detection.molecule.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.server.detection.molecule.dao.entities.FzbbztDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IFzjcxxPJDao extends BaseBasicDao<FzjcxxDto, FzjcxxModel>{

    /**
     * 列表查询
     * @param map
     * @return
     */
    List<FzjcxxDto> getListWithMap(Map<String, Object> map);

    /**
     * 获取普检信息
     * @param fzjcxxDto
     * @return
     */
    FzjcxxDto getPjDto(FzjcxxDto fzjcxxDto);

    /**
     * 判断样本是否存在
     * @param fzjcxxDto
     * @return
     */
    List<FzjcxxDto> getExistByYbbh(FzjcxxDto fzjcxxDto);

    /**
     * 新增普检信息
     * @param fzjcxxDto
     * @return
     */
    int insertPjDto(FzjcxxDto fzjcxxDto);

    /**
     * 修改分子检测信息
     * @param fzjcxxDto
     * @return
     */
    int saveEditDto(FzjcxxDto fzjcxxDto);

    /**
     * 删除普检信息
     * @param fzjcxxDto
     * @return
     */
    boolean delGeneralInspection(FzjcxxDto fzjcxxDto);
    /*
          普检信息实验保存 86同步至85
       */
    void updateSyzt(FzjcxxDto fzjcxxDto);
    /*
        删除分子检测标本状态
     */
    void deleteByFzjcid(String fzjcid);
    /*
        新增分子检测标本状态
     */
    void insertFzbbzt(List<FzbbztDto> fzbbztDtoList);
    /*
        标本接收保存分子检测信息
     */
    void saveFzjcxxInfo(FzjcxxDto fzjcxxDto);
    /*
      普检信息 删除 保存 86同步至85
   */
    void delPJDetection(FzjcxxDto fzjcxxDto);
    /**
     * 批量更新检测结果
     */
    boolean updateJcjgmc(List<FzjcxxDto> list);
    /**
     * 批量更新审核状态和报告日期
     */
    void updatePjBgrqAndBgwcs(FzjcxxDto fzjcxxDto);
    /**
     * 批量更新审核状态和报告日期
     */
    void updateZtAndBgrq(FzjcxxDto fzjcxxDto);

    /**
     * 获取个人报告
     */
    List<FjcfbDto> getReport(FjcfbDto fjcfbDto);
}

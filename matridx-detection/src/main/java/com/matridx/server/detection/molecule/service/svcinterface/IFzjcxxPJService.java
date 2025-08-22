package com.matridx.server.detection.molecule.service.svcinterface;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.detection.molecule.dao.entities.FzjcjgDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxxModel;

import java.util.List;
import java.util.Map;

public interface IFzjcxxPJService extends BaseBasicService<FzjcxxDto, FzjcxxModel>{

    /**
     * 列表查询
     * @param map
     * @return
     */
    List<FzjcxxDto> getListWithMap(Map<String, Object> map);

    /**
     * 保存普检信息
     * @param fzjcxxDto
     * @return
     */
    boolean addSaveFzjcxx(FzjcxxDto fzjcxxDto) throws BusinessException;

    /**
     * 删除普检信息
     * @param fzjcxxDto
     * @return
     */
    boolean delGeneralInspection(FzjcxxDto fzjcxxDto);

    /**
     * 获取普检信息
     * @param fzjcxxDto
     * @return
     */
    FzjcxxDto getPjDto(FzjcxxDto fzjcxxDto);
    /*
        普检信息新增或修改保存 86同步至85
     */
    void addOrModSaveToWechat(FzjcxxDto fzjcxxDto);
    /*
       普检信息 实验 保存 86同步至85
    */
    void updateSyzt(FzjcxxDto fzjcxxDto);
    /*
       普检信息 标本接收 保存 86同步至85
    */
    void acceptSaveSamplePJ(FzjcxxDto fzjcxxDto);
    /*
          普检信息 删除 保存 86同步至85
       */
    void delPJDetection(FzjcxxDto fzjcxxDto);
    /*
          普检信息 结果修改 保存 86同步至85
       */
    void resultModSavePJ(FzjcjgDto fzjcjgDto);
    /*
          普检信息 审核 保存 86同步至85
       */
    void updatePjBgrqAndBgwcs(FzjcxxDto fzjcxxDto);


    /**
     * 获取个人报告
     */
    List<FjcfbDto> getReport(FjcfbDto fjcfbDto);
}

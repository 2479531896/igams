package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public interface IYxsjxxService extends BaseBasicService<SjxxDto, SjxxModel> {
    /**
     * 从Dto里把数据放到Map里，减少Dto的属性设置，防止JSON出错
     * @param sjxxDto
     * @return
     */
    Map<String,Object> pareMapFromDto(SjxxDto sjxxDto, HttpServletRequest request);

    /**
     * 查询所有送检信息(优化)
     * @param params
     * @return
     */
    List<Map<String,Object>> getDtoListOptimize(Map<String, Object> params);

    /**
     * 压缩下载
     * @param request
     * @return
     */
    void packageZipAndDownload(HttpServletRequest request, HttpServletResponse httpResponse);
    /**
     * 对账单数据
     * @param params
     * @return
     */
    List<Map<String,Object>> getDtoAccountList(Map<String, Object> params);
}

package com.matridx.igams.wechat.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.JcsqglDto;
import com.matridx.igams.wechat.dao.entities.JcsqglModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;


public interface IJcsqglService extends BaseBasicService<JcsqglDto, JcsqglModel> {


    /**
     * 新增
     * @param jcsqglDto
     * @return
     */
    boolean addSaveDetectionApplication(JcsqglDto jcsqglDto) throws BusinessException;

    /**
     * 修改
     * @param jcsqglDto
     * @return
     */
    boolean modSaveDetectionApplication(JcsqglDto jcsqglDto);

    /**
     * 删除
     * @param jcsqglDto
     * @return
     */
    boolean delDetectionApplication(JcsqglDto jcsqglDto);

    /**
     * 审核列表
     * @param jcsqglDto
     * @return
     */
    List<JcsqglDto> getPagedAuditDetectionApplication(JcsqglDto jcsqglDto);
    /**
     * 检出申请回调
     * @param obj
     * @param request
     * @param t_map
     * @return
     * @throws BusinessException
     */
    boolean callbackAuditDetectionApplication(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;
    /**
     * 上传
     * @param jcsqglDto
     * @return
     */
    boolean uploadSaveDetectionApplication(JcsqglDto jcsqglDto);
    /**
     * 获取文库编码
     * @param jcsqglDto
     * @return
     */
    List<JcsqglDto> getWkbmsByYbbhs(JcsqglDto jcsqglDto);
    /**
     * 获取文库编码
     * @param fjid
     * @return
     */
    Map<String, Object> analysisFile(String fjid,String xzbj, User user);

    /**
     * 检出申请回调fastq和物种
     * @param  map
     */
    Map<String,Object> fastqCallBack(Map<String,String>map);

    void pagedataDetectionZipDownload(HttpServletRequest request,HttpServletResponse httpResponse) throws BusinessException, FileNotFoundException;
}

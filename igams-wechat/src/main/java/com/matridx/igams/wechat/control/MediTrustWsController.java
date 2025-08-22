package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.detection.molecule.dao.entities.MxRequestDto;
import com.matridx.igams.detection.molecule.enums.MxRequestEnum;
import com.matridx.igams.wechat.dao.entities.MxxxDto;
import com.matridx.igams.wechat.dao.entities.MxxxModel;
import com.matridx.igams.wechat.service.svcinterface.IMxxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/meditrust")
public class MediTrustWsController extends BaseController {

    @Autowired
    private IMxxxService mxxxService;

    /**
     * 同步创建订单
     * @return
     */
    @RequestMapping("/equityinsurance/createorder")
    @ResponseBody
    public Map<String,Object> createorder(String data) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        MxRequestDto requestDto = JSONObject.parseObject(data, MxRequestDto.class);
        Map<String, Object> map = new HashMap<>();
        MxxxModel mxxxModel = JSONObject.parseObject(requestDto.getRequestData(), MxxxModel.class);
        MxxxDto mxxxDto = new MxxxDto();
        mxxxDto.setMxOrderNo(mxxxModel.getMxOrderNo());
        MxxxDto responseMx = mxxxService.getDtoByOrderNo(mxxxDto);
        boolean success = true;
        if (null == responseMx){
            String orderNo = mxxxService.generateDdh();
            mxxxModel.setOrderNo(orderNo);
            success = mxxxService.insertInto(mxxxModel);
        }else{
            mxxxModel.setOrderNo(responseMx.getOrderNo());
        }
        map.put("requestType","meditrust.equityinsurance.createorder");
        map.put("requestId",requestDto.getRequestId());
        map.put("responseTime",simpleDateFormat.format(new Date()));
        map.put("companyId",requestDto.getCompanyId());
        if (success){
            map.put("resultCode", MxRequestEnum.MX_ReQUEST_INFO_000000.getCode());
            map.put("resultMsg",MxRequestEnum.MX_ReQUEST_INFO_000000.getValue());
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("mxOrderNo",mxxxModel.getMxOrderNo());
            responseData.put("orderNo",mxxxModel.getOrderNo());
            map.put("responseData",responseData);
        }else{
            map.put("resultCode", MxRequestEnum.MX_ReQUEST_INFO_020999.getCode());
            map.put("resultMsg","保存"+MxRequestEnum.MX_ReQUEST_INFO_020999.getValue());
        }
        return map;
    }

    /**
     * 同步订单预约
     * @return
     */
    @RequestMapping("/equityinsurance/makeappointment")
    @ResponseBody
    public Map<String,Object> makeappointment(String data) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        MxRequestDto requestDto = JSONObject.parseObject(data, MxRequestDto.class);
        Map<String, Object> map = new HashMap<>();
        MxxxModel mxxxModel = JSONObject.parseObject(requestDto.getRequestData(), MxxxModel.class);
        mxxxModel.setStatus("0");
        MxxxDto mxxxDto = new MxxxDto();
        mxxxDto.setMxOrderNo(mxxxModel.getMxOrderNo());
        MxxxDto responseMx = mxxxService.getDtoByOrderNo(mxxxDto);
        boolean success = false;
        if (null != responseMx){
            if (StringUtil.isBlank(responseMx.getIdNum())){
                success = mxxxService.updateInfo(mxxxModel);
            }else{
                success = true;
            }

        }
        map.put("requestType","meditrust.equityinsurance.makeappointment");
        map.put("requestId",requestDto.getRequestId());
        map.put("responseTime",simpleDateFormat.format(new Date()));
        map.put("companyId",requestDto.getCompanyId());
        if (success){
            map.put("resultCode", MxRequestEnum.MX_ReQUEST_INFO_000000.getCode());
            map.put("resultMsg",MxRequestEnum.MX_ReQUEST_INFO_000000.getValue());
        }else{
            map.put("resultCode", MxRequestEnum.MX_ReQUEST_INFO_020999.getCode());
            map.put("resultMsg","更新"+MxRequestEnum.MX_ReQUEST_INFO_020999.getValue());
        }
        return map;
    }

    /**
     * 同步检测状态API
     * @return
     */
    @RequestMapping("/equityinsurance/noticestatus")
    @ResponseBody
    public Map<String,Object> noticestatus(MxxxDto mxxxDto) {
        //todo mxxx 查询订单信息
        return new HashMap<>();
    }

    /**
     * 查询订单状态
     * @return
     */
    @RequestMapping("/equityinsurance/chackorder")
    @ResponseBody
    public Map<String,Object> chackorder(String data) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Map<String, Object> map = new HashMap<>();
        MxRequestDto requestDto = JSONObject.parseObject(data, MxRequestDto.class);
        MxxxModel mxxxModel = JSONObject.parseObject(requestDto.getRequestData(), MxxxModel.class);
        MxxxDto mxxxDto = new MxxxDto();
        mxxxDto.setMxOrderNo(mxxxModel.getMxOrderNo());
        mxxxDto.setOrderNo(mxxxModel.getOrderNo());
        MxxxDto responseMx = mxxxService.getDtoByOrderNo(mxxxDto);
        map.put("requestType","meditrust.equityinsurance.chackorder");
        map.put("requestId",requestDto.getRequestId());
        map.put("responseTime",simpleDateFormat.format(new Date()));
        map.put("companyId",requestDto.getCompanyId());
        if (null != responseMx){
            map.put("resultCode", MxRequestEnum.MX_ReQUEST_INFO_000000.getCode());
            map.put("resultMsg",MxRequestEnum.MX_ReQUEST_INFO_000000.getValue());
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("mxOrderNo",StringUtil.isNotBlank(mxxxModel.getMxOrderNo())?mxxxModel.getMxOrderNo():"");
            responseData.put("orderNo",StringUtil.isNotBlank(responseMx.getOrderNo())?responseMx.getOrderNo():"");
            responseData.put("status",StringUtil.isNotBlank(responseMx.getStatus())?responseMx.getStatus():"");
            responseData.put("hasReport",StringUtil.isNotBlank(responseMx.getHasReport())?responseMx.getHasReport():"");
            responseData.put("reportUrl",StringUtil.isNotBlank(responseMx.getReportUrl())?responseMx.getReportUrl():"");
            responseData.put("statusTime",StringUtil.isNotBlank(responseMx.getStatusTime())?responseMx.getStatusTime():"");
            map.put("responseData",responseData);
        }else{
            map.put("resultCode", MxRequestEnum.MX_ReQUEST_INFO_020999.getCode());
            map.put("resultMsg","查询"+MxRequestEnum.MX_ReQUEST_INFO_020999.getValue());
        }
        return map;
    }
}

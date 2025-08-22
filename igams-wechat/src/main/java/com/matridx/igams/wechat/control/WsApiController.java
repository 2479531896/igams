package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JkdymxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.InvokingChildTypeEnum;
import com.matridx.igams.common.enums.InvokingTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJkdymxService;
import com.matridx.igams.wechat.config.ExtendAmqpUtils;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.service.svcinterface.ISjwzxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxTwoService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxWsService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/wsapi")
public class WsApiController extends BaseController {

    @Autowired
    private ISjxxWsService sjxxWsService;

    @Autowired
    private ISjxxService sjxxService;

    @Autowired
    private ISjwzxxService sjwzxxService;

    @Autowired
    IJkdymxService jkdymxService;
    private Logger log = LoggerFactory.getLogger(WbaqyzController.class);

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    ExtendAmqpUtils extendAmqpUtils;
    @Autowired
    ISjxxTwoService sjxxTwoService;

    /**
     * 接收外部送检信息
     * @param request
     * @return
     */
    @RequestMapping(value="/inspect/receiveInspectInfo")
    @ResponseBody
    public Map<String, Object> receiveInspectInfo(HttpServletRequest request, String jsonStr, String organ, String sign){
        Map<String, Object> map = new HashMap<>();
        JkdymxDto jkdymxDto = new JkdymxDto();
        jkdymxDto.setLxqf("recv"); // 类型区分 发送 send;接收recv
        jkdymxDto.setDysj(DateUtils.getCustomFomratCurrentDate(null));
        jkdymxDto.setDydz("/wsapi/inspect/receiveInspectInfo");
        jkdymxDto.setNr(jsonStr);
        jkdymxDto.setQtxx("organ："+(StringUtil.isNotBlank(organ)?organ:"")+",sign："+(StringUtil.isNotBlank(sign)?sign:""));
        jkdymxDto.setDyfl(InvokingTypeEnum.RECEIVE_INSPECTINFO.getCode());
        jkdymxDto.setDyzfl(InvokingChildTypeEnum.RECEIVE_INSPECTINFO_WSAPI.getCode());
        try{
            log.error("接收到送检信息-wsapi："+jsonStr+",organ："+(StringUtil.isNotBlank(organ)?organ:"")+",sign："+(StringUtil.isNotBlank(sign)?sign:""));
            map = sjxxService.receiveInspectInfoMap(request, jsonStr);
            jkdymxDto.setSfcg("1"); // 是否成功  0:失败 1:成功 2:未知
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("errorCode", "189");
            map.put("errorInfo", e.getMsg());
            log.error(e.getMsg()+e.toString());
            jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("errorCode", "199");
            map.put("errorInfo", e.getMessage());
            log.error(e.toString());
            jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
        }
        jkdymxDto.setFhnr(JSONObject.toJSONString(map)); // 返回内容
        jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
        jkdymxService.insertJkdymxDto(jkdymxDto); // 同步新增
        return map;
    }
    /**
     * 接收外部送检信息Plus
     * @param request
     * @return
     */
    @RequestMapping(value="/inspect/receiveInspectInfoPlus")
    @ResponseBody
    public Map<String, Object> receiveInspectInfoPlus(HttpServletRequest request, String jsonStr, String organ, String sign){
        Map<String, Object> map = new HashMap<>();        JkdymxDto jkdymxDto = new JkdymxDto();
        jkdymxDto.setLxqf("recv"); // 类型区分 发送 send;接收recv
        jkdymxDto.setDysj(DateUtils.getCustomFomratCurrentDate(null));
        jkdymxDto.setDydz("/wsapi/inspect/receiveInspectInfoPlus");
        jkdymxDto.setNr(jsonStr);
        jkdymxDto.setQtxx("organ："+(StringUtil.isNotBlank(organ)?organ:"")+",sign："+(StringUtil.isNotBlank(sign)?sign:""));
        jkdymxDto.setDyfl(InvokingTypeEnum.RECEIVE_INSPECTINFO.getCode());
        jkdymxDto.setDyzfl(InvokingChildTypeEnum.RECEIVE_INSPECTINFO_WSAPI_PLUS.getCode());
        try{
            log.error("接收到送检信息plus-wsapi："+jsonStr+",organ："+(StringUtil.isNotBlank(organ)?organ:"")+",sign："+(StringUtil.isNotBlank(sign)?sign:""));
            map = sjxxService.receiveInspectInfoMapPlus(request, jsonStr);
            jkdymxDto.setSfcg("1"); // 是否成功  0:失败 1:成功 2:未知
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("errorCode", "189");
            map.put("errorInfo", e.getMsg());
            log.error(e.getMsg()+e.toString());
            jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("errorCode", "199");
            map.put("errorInfo", e.getMessage());
            log.error(e.toString());
            jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
        }
        jkdymxDto.setFhnr(JSONObject.toJSONString(map)); // 返回内容
        jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
        jkdymxService.insertJkdymxDto(jkdymxDto); // 同步新增
        return map;
    }
    /**
     * 外部获取下载报告地址清单接口
     * @param request
     * @param code
     * @param type
     * @param sign
     * @param response
     * @return
     */
    @RequestMapping(value="/pathogen/externalGetReport")
    @ResponseBody
    public Map<String, Object> externalGetReport(HttpServletRequest request, String organ, String code, String lastcode, String type, String sign, HttpServletResponse response) throws IOException {
        BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        Map<String, Object> map = new HashMap<>();
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);
        try{
            JSONObject jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
            String param= jsonObject.toJSONString();
            if(StringUtils.isNotBlank(param)){
                if(StringUtils.isNotBlank(String.valueOf(jsonObject.get("code"))))
                    code=String.valueOf(jsonObject.get("code"));
                if(StringUtils.isNotBlank(String.valueOf(jsonObject.get("lastcode"))))
                    lastcode=String.valueOf(jsonObject.get("lastcode"));
                if(StringUtils.isNotBlank(String.valueOf(jsonObject.get("type"))))
                    type=String.valueOf(jsonObject.get("type"));

            }
        }catch (Exception e){
            log.error("未获取到JSON格式的参数2!");
            map.put("status", "fail");
            map.put("errorCode","未获取到JSON格式的参数!");
            return  map;
        }
        try{
            SjxxDto sjxxDto = new SjxxDto();
            sjxxDto.setWbbm(code);
            sjxxDto.setLastwbbm(lastcode);
            sjxxDto.setLx(type);
            User user= getLoginInfo(request);
            map = sjxxService.externalGetReport(request, sjxxDto, user.getYhm());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("errorCode", e.getMessage());
            log.error(e.toString());
        }
        return map;
    }

    /**
     * 外部获取下载报告地址清单接口
     * @param request
     * @return
     */
    @RequestMapping(value="/pathogen/externalGetReportList")
    @ResponseBody
    public Map<String,Object> externalGetReportList(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        try {
            User user =getLoginInfo(request);
            List<Map<String, String>> ybInfoList = sjxxWsService.getAndCheckYbxxInfo(request,user,false);
            List<String> ybbhs = ybInfoList.stream().map(item -> item.get("ybbh")).collect(Collectors.toList());
            if (ybInfoList != null && ybInfoList.size() > 0) {
                List<String> ywids = ybInfoList.stream().map(item -> item.get("sjid")).collect(Collectors.toList());
                List<Map<String, Object>> data = sjxxWsService.getJcxmFjInfoByYwids(ywids);
                String limit = "50";
                Object settting = redisUtil.hget("matridx_xtsz", "external.interface.settting");
                if (settting != null) {
                    JSONObject setttingObj = JSONObject.parseObject(String.valueOf(settting));
                    limit = setttingObj.getString("szz");
                }
                if (Integer.parseInt(limit) == ybInfoList.size()){
                    map.put("lastcode",ybbhs.get(ybbhs.size()-1));
                }
                map.put("data", data);
                map.put("status","success");
                map.put("errorCode","0");
            }
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("errorCode", e.getMessage());
        }
        return map;
    }
    /**
     * 外部获取下载报告地址清单接口Plus
     * @param request
     * @return
     */
    @RequestMapping(value="/pathogen/externalGetReportListPlus")
    @ResponseBody
    public Map<String,Object> externalGetReportListPlus(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        try {
            User user =getLoginInfo(request);
            List<Map<String, String>> ybInfoList = sjxxWsService.getAndCheckYbxxInfo(request,user,true);
            if (ybInfoList != null && ybInfoList.size() > 0) {
                List<String> sjbms = ybInfoList.stream().map(item -> item.get("sjbm")).collect(Collectors.toList());
                List<String> ywlxnotlikes = new ArrayList<>();
                ywlxnotlikes.add("WORD");
                List<Map<String, Object>> data = sjxxWsService.getJcxmFjInfoByYwidsPlus(ybInfoList,null,ywlxnotlikes);
                String limit = "50";
                Object settting = redisUtil.hget("matridx_xtsz", "external.interface.settting");
                if (settting != null) {
                    JSONObject setttingObj = JSONObject.parseObject(String.valueOf(settting));
                    limit = setttingObj.getString("szz");
                }
                if (Integer.parseInt(limit) == ybInfoList.size()){
                    map.put("lastcode",sjbms.get(sjbms.size()-1));
                }
                map.put("data", data);
                map.put("status","success");
                map.put("errorCode","0");
            }
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("errorCode", e.getMessage());
        }
        return map;
    }

    /**
     * 文件下载（用于外部编码下载报告使用）
     * @param
     * @param response
     * @return
     */
    @RequestMapping(value="/file/downloadByCode")
    public void downloadByCode(HttpServletRequest request, String organ, String type, String code, String lastcode, String sign, HttpServletResponse response){
        User user =getLoginInfo(request);
        sjxxWsService.downloadByCode(request, user.getYhm(), type, code, lastcode, sign, response);
    }
    /**
     * 文件下载（用于外部编码下载报告使用）Plus
     * @param
     * @param response
     * @return
     */
    @RequestMapping(value="/file/downloadByCodePlus")
    public void downloadByCodePlus(HttpServletRequest request, String organ, String type, String code, String lastcode, String sign, HttpServletResponse response){
        User user =getLoginInfo(request);
        sjxxWsService.downloadByCodePlus(request, user.getYhm(), type, code, lastcode, sign, response);
    }

    /**
     * 文件下载（用于外部接口限时下载报告清单使用）
     * @param fjcfbDto
     * @param response
     * @return
     */
    @RequestMapping(value="/file/limitDownload")
    @ResponseBody
    public String limitDownload(HttpServletRequest request, FjcfbDto fjcfbDto, HttpServletResponse response){
        if( StringUtil.isBlank(fjcfbDto.getFjid())){
            log.error("limitDownload  ----  没有获取到附件ID： " + fjcfbDto.getFjid());
            return null;
        }
        log.error("limitDownload  ----  附件ID： " + fjcfbDto.getFjid());
        FjcfbDto t_fjcfbDto = fjcfbService.getDto(fjcfbDto);
        String wjlj = t_fjcfbDto.getWjlj();
        String wjm = t_fjcfbDto.getWjm();
        DBEncrypt crypt = new DBEncrypt();
        String filePath = crypt.dCode(wjlj);
        sjxxWsService.download(filePath, wjm, response);
        return null;
    }

    @RequestMapping("/pathogen/getPathogenResultByCode")
    @ResponseBody
    public Map<String,Object> getPathogenResultByCode(HttpServletRequest request){
        List<Map<String, String>> ybInfoList;
        Map<String,Object> map = new HashMap<>();
        try {
            User user =getLoginInfo(request);
            ybInfoList = sjxxWsService.getAndCheckYbxxInfo(request,user,false);
        } catch (BusinessException e) {
        	log.error(e.getMessage());
            return map;
        }
        if (ybInfoList != null && ybInfoList.size() > 0) {
            List<String> ybbhs = ybInfoList.stream().map(item -> item.get("ybbh")).collect(Collectors.toList());
            List<Map<String, Object>> resutList = sjwzxxService.getJcjgInfoByCodes(ybbhs);
            map.put("data",resutList);
            String limit = "50";
            Object settting = redisUtil.hget("matridx_xtsz", "external.interface.settting");
            if (settting != null) {
                JSONObject setttingObj = JSONObject.parseObject(String.valueOf(settting));
                limit = setttingObj.getString("szz");
            }
            if (Integer.parseInt(limit) == ybbhs.size()){
                map.put("lastcode",ybbhs.get(ybbhs.size()-1));
            }
        }else {
            map.put("status", "fail");
            map.put("errorCode", "未查询到信息！");
            return map;
        }
        map.put("status","success");
        map.put("errorCode","0");
        return map;

    }
    @RequestMapping(value="/report/sendDownloadReportMsg")
    @ResponseBody
    public void sendDownloadReportMsg(@RequestBody List<Map<String, Object>> maps){
        for (Map<String, Object> map : maps) {
            extendAmqpUtils.sendDownloadReportMsg(map);
        }
    }

    /**
     * 获取申请单信息
     * @param request
     * 	organ:
     * 	db:
     * 	barcode:
     * 	startTime:
     * 	endTime:
     * @return
     */
    @RequestMapping("/getSampleDetails")
    @ResponseBody
    public Map<String, Object> getSampleDetails(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        String organ = user.getYhm();
        String db = request.getParameter("db");
        String barcode = StringUtil.isNotBlank(request.getParameter("barcode"))?request.getParameter("barcode"):"";
        String startTime = StringUtil.isNotBlank(request.getParameter("startTime"))?request.getParameter("startTime"):"";
        String endTime = StringUtil.isNotBlank(request.getParameter("endTime"))?request.getParameter("endTime"):"";
        log.error("委外接口-getApplicationDetail-organ:{},db:{},barcode:{},startTime:{},endTime{}",organ,db,barcode,startTime,endTime);
        try {
            if (StringUtil.isBlank(organ)){
                map.put("status", "fail");
                map.put("errorInfo", "未获取到organ信息!");
                map.put("errorCode", 103);
                log.error("委外接口-未获取到organ信息!");
                return map;
            }
            map = sjxxTwoService.getApplicationDetail(request);
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("errorInfo", e.getMessage());
            map.put("errorCode", -1);
            log.error("委外接口-发送异常信息!" + e.getMessage());
        }
        return map;
    }


    /**
     * 获取申请单信息
     * @param request
     * 	organ:
     * 	db:
     * 	barcode:
     * 	filename:
     * @return
     */
    @RequestMapping("/sendSampleReport")
    @ResponseBody
    public Map<String, Object> sendSampleReport(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        String organ = user.getYhm();
        String db = request.getParameter("db");
        String barcode = request.getParameter("barcode");
        String jcxmcsdm = request.getParameter("jcxmcsdm");
        String filename = request.getParameter("filename");
        if (StringUtil.isAnyBlank(barcode,jcxmcsdm,filename)){
            throw new RuntimeException("缺少参数!");
        }
        String data = StringUtil.isNotBlank(request.getParameter("data"))?request.getParameter("data"):"";
        log.error("委外接口-sendSampleReport-organ:{},db:{},barcode:{},jcxmcsdm:{},data:{},filename:{}",organ,db,barcode,jcxmcsdm,data,filename);
        try {
            return sjxxTwoService.sendSampleReport(request,barcode,jcxmcsdm);
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("errorInfo", e.getMessage());
            map.put("errorCode", -1);
        }
        return map;
    }


    /**
     * 实验室系统备份数据抽查记录 文件下载
     */
    @RequestMapping(value="/file/downloadFileHZMDLPF33R04")
    public String downloadFileHZMDLPF33R04(HttpServletResponse response,HttpServletRequest request){
        String dateStr = request.getParameter("date");
        List<Map<String, Object>> list = sjxxTwoService.getDateSjxxList(dateStr);
        // 静态资源中的文件路径
        String staticFilePath = "templates/imp/HZMDL-PF-33-R04 实验室系统备份数据抽查记录.xls";
        String folderPath = "/matridx/fileupload/temp/HZMDLPF33R04";
        String tempFilePath = "/HZMDL-PF-33-R04 实验室系统备份数据抽查记录【"+dateStr+"】"+ new Date().getTime() +".xls";
        InputStream in = null;
        FileOutputStream out = null;
        try {
            // 读取文件
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(staticFilePath);
            // 将文件复制到临时文件夹中
            File folder = new File(folderPath);
            if (!folder.isDirectory()) {
                folder.mkdirs();
            }
            File file = new File(folderPath + tempFilePath);
            if (!file.exists()){
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            int c;
            byte[] buffer = new byte[1024];
            while((c = in.read(buffer)) != -1){
                for(int i = 0; i < c; i++)
                    out.write(buffer[i]);
            }
            if (file.isFile()) {
                FileInputStream inexcle = null;
                FileOutputStream outexcle = null;
                Workbook workbook = null;
                try {
                    inexcle = new FileInputStream(folderPath + tempFilePath);
                    workbook = WorkbookFactory.create(inexcle);
                    Sheet sheet = workbook.getSheet("Sheet1");
                    if (StringUtil.isNotBlank(request.getParameter("fzr"))){
                        sheet.getRow(1).getCell(1).setCellValue(request.getParameter("fzr"));
                    }
                    if (StringUtil.isNotBlank(request.getParameter("shr"))){
                        sheet.getRow(2).getCell(1).setCellValue(request.getParameter("shr"));
                    }
                    if (StringUtil.isNotBlank(request.getParameter("ccsj"))){
                        sheet.getRow(1).getCell(3).setCellValue(request.getParameter("shr"));
                    }
                    if (StringUtil.isNotBlank(request.getParameter("shsj"))){
                        sheet.getRow(2).getCell(3).setCellValue(request.getParameter("shsj"));
                    }
                    int startRow = 4;
                    for (Map<String, Object> map : list) {
                        String json = JSON.toJSONString(map);
                        sheet.getRow(startRow).getCell(2).setCellValue(json);
                        sheet.getRow(startRow+1).getCell(2).setCellValue(json);
                        startRow = startRow+3;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    outexcle = new FileOutputStream(folderPath + tempFilePath);
                    if (workbook != null) {
                        workbook.write(outexcle);
                    }
                    if (outexcle != null) {
                        outexcle.close();
                    }
                    if (inexcle != null) {
                        inexcle.close();
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        File downloadFile = new File(folderPath+tempFilePath);
        response.setContentLength((int) downloadFile.length());
        String agent = request.getHeader("user-agent");
        //log.error("文件下载 agent=" + agent);
        //指明为下载
        response.setHeader("content-type", "application/octet-stream");
        String fileName = downloadFile.getName();
        if(fileName != null){
            if (agent.contains("iPhone") || agent.contains("Trident")){
                //iphone手机 微信内置浏览器 下载
                if (agent.contains("MicroMessenger")|| agent.contains("micromessenger")){
                    fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
                    response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));// 文件名外的双引号处理firefox的空格截断问题
                }else {
                    //iphone手机 非微信内置浏览器 下载 或 ie浏览器 下载
                    fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
                    response.setHeader("Content-Disposition","attachment;filename*=UTF-8''" + fileName);
                }
            }else {
                fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
                response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));// 文件名外的双引号处理firefox的空格截断问题
            }
        }
        if (fileName != null) {
            log.error("文件下载 准备完成 wjm=" + URLDecoder.decode(fileName, StandardCharsets.UTF_8));
        }
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        InputStream iStream;
        OutputStream os = null; //输出流
        try {
            iStream = new FileInputStream(folderPath+tempFilePath);
            os = response.getOutputStream();
            bis = new BufferedInputStream(iStream);
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer);
                os.flush();
                i = bis.read(buffer);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("文件下载 写文件异常：" + e);
        }
        try {
            if(bis!=null)
                bis.close();
            if(os!=null) {
                os.flush();
                os.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("文件下载 关闭文件流异常：" + e);
            return null;
        }
        return null;
    }
}

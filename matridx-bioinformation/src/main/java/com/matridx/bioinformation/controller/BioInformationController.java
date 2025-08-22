package com.matridx.bioinformation.controller;

import com.matridx.bioinformation.dao.entities.*;
import com.matridx.bioinformation.service.svcinterface.*;
import com.matridx.bioinformation.util.DockerUtil;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.file.upload.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("/bioInformation")
public class BioInformationController extends BaseController {

    @Autowired
    IJxglService jxglService;
    @Autowired
    IJxcmdService jxcmdService;
    @Autowired
    IRwglService rwglService;
    @Autowired
    ILjglService ljglService;
    @Autowired
    DockerUtil dockerUtil;
    @Autowired
    ZipUtil zipUtil;
    @Value("${matridx.operate.filePath:}")
    private String filePath;
    @Value("${matridx.operate.fileFiltering:}")
    private String fileFiltering;

    private Logger log = LoggerFactory.getLogger(BioInformationController.class);

    /**
     * 获取应用列表
     * @return
     */
    @RequestMapping(value ="/getMirrorList")
    @ResponseBody
    public Map<String,Object> getMirrorList(JxglDto jxglDto){
        Map<String,Object> map = new HashMap<>();
        List<JxglDto> jxglDtos = jxglService.getPagedDtoList(jxglDto);
        for (JxglDto dto : jxglDtos) {
            if (StringUtil.isNotBlank(dto.getWjlj())){
                try {
                    dto.setUrl("/ws/file/getFileByPath?wjlj="+URLEncoder.encode(dto.getWjlj(),"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    map.put("status","fail");
                    map.put("requestMessage","图片 URLEncoder.encode 失败");
                    e.printStackTrace();
                    return map;
                }
            }
        }
        map.put("status","success");
        Map<String,Object> requestData = new HashMap<>();
        requestData.put("jxglDtos",jxglDtos);
        requestData.put("total", jxglDto.getTotalNumber());
        map.put("requestData",requestData);
        return map;
    }

    /**
     * 获取应用参数信息
     * @return
     */
    @RequestMapping(value ="/getMirrorInfo")
    @ResponseBody
    public Map<String,Object> getMirrorInfo(JxglDto jxglDto){
        Map<String,Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(jxglDto.getJxid())){
            JxglDto jxglDto1 = jxglService.getDto(jxglDto);
            if (StringUtil.isNotBlank(jxglDto1.getWjlj())){
                try {
                    jxglDto1.setUrl("/ws/file/getFileByPath?wjlj="+URLEncoder.encode(jxglDto1.getWjlj(),"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    map.put("status","fail");
                    map.put("requestMessage","图片 URLEncoder.encode 失败");
                    e.printStackTrace();
                    return map;
                }
            }
            if (jxglDto1 != null && StringUtil.isNotBlank(jxglDto1.getJxid()) && StringUtil.isNotBlank(jxglDto1.getSj())){
                Map<String,Object> requestData = new HashMap<>();
                requestData.put("jxglDto",jxglDto1);
                JxcmdDto jxcmdDto = new JxcmdDto();
                jxcmdDto.setYwid(jxglDto1.getJxid());
                jxcmdDto.setSs("kwargs");
                List<JxcmdDto> kwargs = jxcmdService.getDtoList(jxcmdDto);
                jxcmdDto.setSs("args");
                List<JxcmdDto> args = jxcmdService.getDtoList(jxcmdDto);
                jxcmdDto.setSs("volumslist");
                List<JxcmdDto> volumslist = jxcmdService.getDtoList(jxcmdDto);
                requestData.put("kwargs",kwargs);
                requestData.put("volumslist",volumslist);
                requestData.put("args",args);
                map.put("status","success");
                map.put("requestData",requestData);
            }else{
                map.put("status","fail");
                map.put("requestMessage","未获取到应用数据！");
            }
        }else{
            map.put("status","fail");
            map.put("requestMessage","未获取到应用id！");
        }
        return map;
    }
    /**
     * 删除应用
     * @return
     */
    @RequestMapping(value ="/removeMirror")
    @ResponseBody
    public Map<String,Object> removeMirror(JxglDto jxglDto){
        Map<String,Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(jxglDto.getJxid())){
            User user= getLoginInfo();
            jxglDto.setScry(user.getYhid());
            try {
                boolean success = jxglService.updateJxInfo(jxglDto);
                if (success){
                    map.put("status","success");
                    map.put("requestMessage","删除成功!");
                }else{
                    map.put("status","fail");
                    map.put("requestMessage","删除失败！");
                }
            } catch (BusinessException e) {
                map.put("status","fail");
                map.put("requestMessage",e.getMsg());
            }
        }else{
            map.put("status","fail");
            map.put("requestMessage","未获取到应用id");
        }
        return map;
    }
    /**
     * 新增应用
     * @return
     */
    @RequestMapping(value ="/addMirrorInfo")
    @ResponseBody
    public Map<String,Object> addMirrorInfo(JxglDto jxglDto){
        Map<String,Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(jxglDto.getJxmc())) {
            User user= getLoginInfo();
            jxglDto.setLrry(user.getYhid());
            try {
                String ywid = jxglService.insertJxInfo(jxglDto);
                if (StringUtil.isNotBlank(ywid)){
                    map.put("status","success");
                    map.put("ywid",ywid);
                    map.put("requestMessage","新增成功!");
                }else{
                    map.put("status","fail");
                    map.put("requestMessage","新增失败！");
                }
            } catch (BusinessException e) {
                map.put("status","fail");
                map.put("requestMessage",e.getMsg());
            }
        }else{
            map.put("status","fail");
            map.put("requestMessage","未获取到应用信息！");
        }
        return map;
    }

    /**
     * 修改保存应用
     * @return
     */
    @RequestMapping(value ="/saveMirrorInfo")
    @ResponseBody
    public Map<String,Object> saveMirrorInfo(JxglDto jxglDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(jxglDto.getJxid())){
            User user= getLoginInfo(request);
            jxglDto.setXgry(user.getYhid());
            try {
                boolean success = jxglService.saveJxInfo(jxglDto);
                if (success){
                    map.put("status","success");
                    map.put("requestMessage","修改成功!");
                }else{
                    map.put("status","fail");
                    map.put("requestMessage","修改失败！");
                }
            } catch (BusinessException e) {
                map.put("status","fail");
                map.put("requestMessage",e.getMsg());
            }
        }else{
            map.put("status","fail");
            map.put("requestMessage","未获取到应用信息");
        }
        return map;
    }

    /**
     * 获取任务列表
     * @return
     */
    @RequestMapping(value ="/getTaskList")
    @ResponseBody
    public Map<String,Object> getTaskList(RwglDto rwglDto){
        Map<String,Object> map = new HashMap<>();
        List<RwglDto> rwglDtos = rwglService.getPagedDtoList(rwglDto);
        map.put("status","success");
        map.put("total",rwglDto.getTotalNumber());
        map.put("requestData",rwglDtos);
        return map;
    }

    /**
     * 获取任务详情
     * @return
     */
    @RequestMapping(value ="/getTaskInfo")
    @ResponseBody
    public Map<String,Object> getTaskInfo(RwglDto rwglDto){
        Map<String,Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(rwglDto.getRwid())){
            RwglDto rwglDto1 = rwglService.getDto(rwglDto);
            if (rwglDto1 != null && StringUtil.isNotBlank(rwglDto1.getRwid()) && StringUtil.isNotBlank(rwglDto1.getSj())){
                Map<String,Object> requestData = new HashMap<>();
                requestData.put("rwglDto",rwglDto1);
                JxcmdDto jxcmdDto = new JxcmdDto();
                jxcmdDto.setYwid(rwglDto1.getRwid());
                jxcmdDto.setSs("kwargs");
                List<JxcmdDto> kwargs = jxcmdService.getDtoList(jxcmdDto);
                jxcmdDto.setSs("args");
                List<JxcmdDto> args = jxcmdService.getDtoList(jxcmdDto);
                jxcmdDto.setSs("volumslist");
                List<JxcmdDto> volumslist = jxcmdService.getDtoList(jxcmdDto);
                requestData.put("volumslist",volumslist);
//                try {
//                    List<String> images= dockerUtil.images();
//                    requestData.put("images",images);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    map.put("status","fail");
//                    map.put("requestMessage","未获取到镜像信息！");
//                }
//                JxglDto jxglDto = new JxglDto();
//                List<JxglDto> jxglDtos = jxglService.getDtoList(jxglDto);
//                requestData.put("jxglDtos",jxglDtos);
                requestData.put("args",args);
                requestData.put("kwargs",kwargs);
                map.put("status","success");
                map.put("requestData",requestData);
            }else{
                map.put("status","fail");
                map.put("requestMessage","未获取到容器数据");
            }
        }else{
            map.put("status","fail");
            map.put("requestMessage","未获取到容器id");
        }
        return map;
    }
    /**
     * 获取ImagesList
     * @return
     */
    @RequestMapping(value ="/getImagesList")
    @ResponseBody
    public Map<String,Object> getImagesList(){
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> requestData = new HashMap<>();
        try {
            List<String> images= dockerUtil.images();
            requestData.put("images",images);
        } catch (Exception e) {
            log.error("images:"+e.toString());
            map.put("status","fail");
            map.put("requestMessage","未获取到镜像信息！");
        }
        map.put("status","success");
        map.put("requestData",requestData);
        return map;
    }

    /**
     * 任务新增
     * @return
     */
    @RequestMapping(value ="/addTaskInfo")
    @ResponseBody
    public Map<String,Object> addTaskInfo(RwglDto rwglDto){
        Map<String,Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(rwglDto.getRwmc())){
            User user= getLoginInfo();
            rwglDto.setLrry(user.getYhid());
            try {
                rwglDto.setRwid(StringUtil.generateUUID());
                boolean success = rwglService.addTaskInfo(rwglDto);
                if (success){
                    map.put("status","success");
                    map.put("ywid",rwglDto.getRwid());
                    map.put("requestMessage","新增成功!");
                }else{
                    map.put("status","fail");
                    map.put("requestMessage","新增失败！");
                }
            } catch (Exception e) {
                log.error(e.toString());
                map.put("status","fail");
                map.put("requestMessage",e.getMessage()==null? "新增操作超时!":e.getMessage());
            }
        }else{
            map.put("status","fail");
            map.put("requestMessage","为获取到任务信息！");
        }
        return map;
    }

    /**
     * 任务修改
     * @return
     */
    @RequestMapping(value ="/modTaskInfo")
    @ResponseBody
    public Map<String,Object> modTaskInfo(RwglDto rwglDto){
        Map<String,Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(rwglDto.getRwid())){
            User user= getLoginInfo();
            rwglDto.setXgry(user.getYhid());
            try {
                boolean success = rwglService.modTaskInfo(rwglDto);
                if (success){
                    map.put("status","success");
                    map.put("ywid",rwglDto.getRwid());
                    map.put("requestMessage","修改成功!");
                }else{
                    map.put("status","fail");
                    map.put("requestMessage","修改失败！");
                }
            } catch (BusinessException e) {
                map.put("status","fail");
                map.put("requestMessage","修改失败！");
            }
        }else{
            map.put("status","fail");
            map.put("requestMessage","未获取到任务信息！");
        }
        return map;
    }


    /**
     * 任务操做
     * @return
     */
    @RequestMapping(value ="/operateTaskInfo")
    @ResponseBody
    public Map<String,Object> operateTaskInfo(RwglDto rwglDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user= getLoginInfo(request);
        rwglDto.setXgry(user.getYhid());
        try {
            boolean success = rwglService.operateTaskInfo(rwglDto);
            if (success){
                map.put("status","success");
                map.put("requestMessage","执行成功!");
            }else{
                map.put("status","fail");
                map.put("requestMessage","执行失败！");
            }
        } catch (Exception e) {
            map.put("status","fail");
            map.put("requestMessage",e.getMessage()==null? "操作超时!":e.getMessage());
        }
        return map;
    }
    /**
     * 读取某个文件夹下的所有文件
     * @return
     */
    @RequestMapping(value ="/readFile")
    @ResponseBody
    public Map<String,Object> readFile(String filepath,String czbs){
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> requestData = new HashMap<>();
        if(StringUtil.isNotBlank(filepath)){
            if (StringUtil.isNotBlank(czbs) && "xz".equals(czbs)){
                File file = new File(filePath+filepath);
                log.error("filePath:"+filePath+filepath);
                if (file.isDirectory()) {
                    String[] filelist = file.list();
                    requestData.put("filelist",ljglService.fileList(filelist));
                    map.put("requestData",requestData);
                    map.put("status","success");
                }else{
                    map.put("status","not");
                    map.put("requestMessage","该路径下不存在文件！");
                }
            }else{
                String[] strings = filepath.split("/");
                String newPath = "";

                for (int i = 0; i < strings.length ; i++) {
                    newPath+=strings[i]+"/";
                    File file = new File(filePath+newPath);
                    log.error("filePath:"+filePath+filepath);
                    if (file.isDirectory()) {
                        String[] filelist = file.list();
                        if (i==0){
                            log.error("fileFiltering:"+fileFiltering);
                            String[] split = fileFiltering.split(",");
                            if (null!= filelist && filelist.length>0){
                                List<String> list = new ArrayList<>(Arrays.asList(filelist));
                                for (String s : split) {
                                    if (list.contains(s)) {
                                        list.remove(s);
                                    }
                                }
                                filelist = list.toArray(new String[list.size()]);
                            }
                        }
                        requestData.put("filelist"+i,ljglService.fileList(filelist));
                        requestData.put("status"+i,"success");
                    }else{
                        requestData.put("status"+i,"not");
                        requestData.put("requestMessage"+i,"该路径下不存在文件！");
                    }
                }
                if (strings.length == 0){
                    File file = new File(filePath+"/");
                    log.error("filePath:"+filePath+filepath);
                    if (file.isDirectory()) {
                        String[] filelist = file.list();
                        log.error("fileFiltering:"+fileFiltering);
                        String[] split = fileFiltering.split(",");
                        if (null!= filelist && filelist.length>0){
                            List<String> list = new ArrayList<>(Arrays.asList(filelist));
                            for (String s : split) {
                                if (list.contains(s)) {
                                    list.remove(s);
                                }
                            }
                            filelist = list.toArray(new String[list.size()]);
                        }
                        requestData.put("filelist0",ljglService.fileList(filelist));
                        requestData.put("status0","success");
                    }else{
                        requestData.put("status","not");
                        requestData.put("requestMessage","该路径下不存在文件！");
                    }
                }
                map.put("status","success");
                map.put("requestData",requestData);
            }
        }else{
            File file = new File(filePath+"/");
            log.error("filePath:"+filePath+filepath);
            if (file.isDirectory()) {
                String[] filelist = file.list();
                log.error("fileFiltering:"+fileFiltering);
                String[] split = fileFiltering.split(",");
                if (null!= filelist && filelist.length>0){
                    List<String> list = new ArrayList<>(Arrays.asList(filelist));
                    for (String s : split) {
                        if (list.contains(s)) {
                            list.remove(s);
                        }
                    }
                    filelist = list.toArray(new String[list.size()]);
                }
                requestData.put("filelist0",ljglService.fileList(filelist));
                requestData.put("status0","success");
            }
            map.put("status","success");
            map.put("requestData",requestData);
        }
        return map;
    }


    /**
     * 转换Zip
     * @return
     */
    @RequestMapping(value ="/convertedZip")
    @ResponseBody
    public Map<String,Object> convertedZip(String filepath){
        Map<String,Object> map = new HashMap<>();
        String wjm =StringUtil.generateUUID()+".zip";
        String saveFilePath =filePath+wjm;
        ZipUtil.toZip(filepath,saveFilePath , true);
        map.put("status","success");
        map.put("wjm",wjm);
        return map;
    }

    /**
     * 下载Zip
     * @return
     */
    @RequestMapping(value ="/downloadZip")
    @ResponseBody
    public String downloadZip(String path, HttpServletResponse response, HttpServletRequest request){
        String[] strings = path.split("/");
        String wjm =strings[strings.length-1];
        String saveFilePath = filePath+path;
        log.error("saveFilePath:"+saveFilePath);
        File downloadFile = new File(saveFilePath);
        response.setContentLength((int) downloadFile.length());
        String agent = request.getHeader("user-agent");
        //指明为下载
        response.setHeader("content-type", "application/octet-stream");
        try {
            if(wjm != null){
                if (agent.contains("iPhone") || agent.contains("Trident")){
                    if (agent.contains("MicroMessenger")|| agent.contains("micromessenger")){
                        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", wjm));// 文件名外的双引号处理firefox的空格截断问题
                    }else {
                        wjm = URLEncoder.encode(wjm,"UTF-8");
                        response.setHeader("Content-Disposition","attachment;filename*=UTF-8''" + wjm);
                    }
                }else {
                    wjm = URLEncoder.encode(wjm, "utf-8");
                    response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", wjm));// 文件名外的双引号处理firefox的空格截断问题
                }
            }
        } catch (UnsupportedEncodingException e1) {
            log.error("e1"+e1.toString());
            return "fail";
        }

        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        InputStream iStream = null;
        OutputStream os = null;
        try {
            iStream = new FileInputStream(downloadFile);
            os = response.getOutputStream();
            bis = new BufferedInputStream(iStream);
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer);
                os.flush();
                i = bis.read(buffer);
            }

        } catch (Exception e) {
            log.error(e.toString());
            return "fail";
        }
        try {
            bis.close();
            os.flush();
            os.close();
        } catch (IOException e) {
            log.error(e.toString());
            return "fail";
        }
        return "success";
    }

    /**
     * 创建文件夹
     * @return
     */
    @RequestMapping(value ="/mkdirs")
    @ResponseBody
    public Map<String,Object> mkdirs(String storePath){
        Map<String,Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(storePath) && !"null".equals(storePath)){
            File file = new File(filePath+storePath);
            log.error("storePath:"+storePath);
            if (file.isDirectory()) {
                map.put("status","success");
                map.put("message","文件已经存在!");
                return map;
            }
            file.mkdirs();
            map.put("status","success");
            map.put("message","创建成功!");
            return map;
        }
        map.put("status","fail");
        map.put("message","未获取到创建路径!");
        return map;
    }

    /**
     * 路径新增
     * @return
     */
    @RequestMapping(value ="/bioInformation/addRoute")
    @ResponseBody
    public Map<String,Object> addRoute(LjglDto ljglDto){
        Map<String,Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(ljglDto.getLj()) && StringUtil.isNotBlank(ljglDto.getLjmc())){
            ljglDto.setLjglid(StringUtil.generateUUID());
            if (ljglDto.getLj().indexOf(".") == -1){
                ljglDto.setType("lj");
            }else{
                ljglDto.setType("wj");
            }
            ljglService.insert(ljglDto);
            map.put("status","success");
            map.put("message","保存成功!");
            return map;
        }
        map.put("status","fail");
        map.put("message","保存失败!");
        return map;
    }

    /**
     * 路径详情List
     * @return
     */
    @RequestMapping(value ="/bioInformation/getDtoListRoute")
    @ResponseBody
    public Map<String,Object> getDtoListRoute(LjglDto ljglDto){
        Map<String,Object> map = new HashMap<>();
        List<LjglDto> dtoList = ljglService.getPagedDtoList(ljglDto);
        map.put("total",ljglDto.getTotalNumber());
        map.put("status","success");
        map.put("dtoList",dtoList);
        return map;
    }

    /**
     * 路径详情
     * @return
     */
    @RequestMapping(value ="/bioInformation/viewRoute")
    @ResponseBody
    public Map<String,Object> viewRoute(LjglDto ljglDto){
        Map<String,Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(ljglDto.getLjglid())){
            LjglDto dto = ljglService.getDto(ljglDto);
            map.put("status","success");
            map.put("dto",dto);
            return map;
        }
        map.put("status","fail");
        map.put("message","未获取到数据!");
        return map;
    }

    /**
     * 路径修改
     * @return
     */
    @RequestMapping(value ="/bioInformation/modRoute")
    @ResponseBody
    public Map<String,Object> modRoute(LjglDto ljglDto){
        Map<String,Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(ljglDto.getLjglid())){
            if (StringUtil.isNotBlank(ljglDto.getLj())){
                if (ljglDto.getLj().indexOf(".") == -1){
                    ljglDto.setType("lj");
                }else{
                    ljglDto.setType("wj");
                }
            }else {
                map.put("status","fail");
                map.put("message","路径不能为空！");
                return map;
            }
            boolean  success= ljglService.update(ljglDto);
            if (success){
                map.put("status","success");
                map.put("dto","修改成功");
                return map;
            }
            map.put("status","fail");
            map.put("message","修改失败");
            return map;
        }
        map.put("status","fail");
        map.put("message","未获取到路径!");
        return map;
    }

    /**
     * 路径删除
     * @return
     */
    @RequestMapping(value ="/bioInformation/delRoute")
    @ResponseBody
    public Map<String,Object> delRoute(LjglDto ljglDto){
        Map<String,Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(ljglDto.getLjglid())){
            boolean  success= ljglService.delete(ljglDto);
            if (success){
                map.put("status","success");
                map.put("dto","删除成功");
                return map;
            }
            map.put("status","fail");
            map.put("message","删除失败");
            return map;
        }
        map.put("status","fail");
        map.put("message","未获取到路径!");
        return map;
    }

    /**
     * 刷新线程
     * @return
     */
    @RequestMapping(value ="/bioInformation/flushedThread")
    @ResponseBody
    public Map<String,Object> flushedThread(){
        Map<String,Object> map = new HashMap<>();
        boolean thread = dockerUtil.startSelectDOckerThread();
        if (!thread){
            map.put("status","fail");
            map.put("message","线程正在运行中！");
        }
        map.put("status","success");
        map.put("message","刷新成功！");
        return map;
    }
}

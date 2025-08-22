package com.matridx.igams.bioinformation.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.bioinformation.dao.entities.MngsmxjgDto;
import com.matridx.igams.bioinformation.service.svcinterface.IMngsmxjgService;
import com.matridx.igams.bioinformation.util.DockerUtil;
import com.matridx.igams.bioinformation.util.log2Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pkl")
public class PklController {

    @Autowired
    private IMngsmxjgService mngsmxjgService;
    @Autowired
    private DockerUtil dockerUtil;
    private final Logger log = LoggerFactory.getLogger(PklController.class);

    @RequestMapping("/creat/pagedataPkl")
    @ResponseBody
    public String creatPkl(){
        Map<String,Object> map=new HashMap<>();
        map.put("status","true");
        MngsmxjgDto dto=new MngsmxjgDto();
        List<MngsmxjgDto> mxjgList=mngsmxjgService.getMxjgByTjAndNotREM(dto);
        log2Util.buildPklData("/matridx/python/","pkl.txt","/matridx/python/","/matridx/python/main.py", JSONObject.toJSON(mxjgList).toString());
        return JSON.toJSONString(map);
    }

    @RequestMapping("/creat/pagedataGetQindex")
    @ResponseBody
    public String getQindex(){
        Map<String,Object> map=new HashMap<>();
        map.put("status","true");
        log2Util.GaussianKernelSmoothing("/matridx/python/qindex.py","B",9606+"",29505.19,"/matridx/python");
        return JSON.toJSONString(map);
    }

    @RequestMapping("/creat/pagedataCreatePic")
    @ResponseBody
    public Map<String,Object> createPic(){
        Map<String,Object> map=new HashMap<>();
        map.put("status","true");
            log.error("dockerPath:开始");
            String path = "/matridx/rpic";
            log.error("dockerPath:"+path);
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File dirs = new File(path+"/Hindex");
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
            log.error("dockerPath:"+path);
            try {
                dockerUtil.createContainers("/code/density_plot_Hindex.R","/code/host_index.txt",path,"");
                dockerUtil.createContainers("/code/density_plot_list.R","/code/N"+"_qindex.txt",path,"");
                dockerUtil.createContainers("/code/density_plot_list.R","/code/L"+"_qindex.txt",path,"");
                dockerUtil.createContainers("/code/density_plot_list.R","/code/B"+"_qindex.txt",path,"");
                dockerUtil.createContainers("/code/density_plot_list.R","/code/T"+"_qindex.txt",path,"");
            }catch (Exception e){
                log.error(e.getMessage());
                map.put("status","false");
                map.put("message",e.getMessage());
            }

            log.error("创建docker结束！");
        return map;
    }
}

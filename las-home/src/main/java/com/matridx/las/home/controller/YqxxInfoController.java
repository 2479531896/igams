package com.matridx.las.home.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.las.home.dao.entities.YqxxDto;
import com.matridx.las.home.dao.entities.YqxxInfoDto;
import com.matridx.las.home.service.svcinterface.IYqxxInfoService;
import com.matridx.las.home.service.svcinterface.IYqxxService;
import com.matridx.las.home.service.svcinterface.MaterialScienceInitService;
import com.matridx.las.netty.dao.entities.JkywzszDto;
import com.matridx.las.netty.dao.entities.ProcessconfigDto;
import com.matridx.las.netty.dao.entities.YqxxinfosxDto;
import com.matridx.las.netty.enums.MaterialTypeEnum;
import com.matridx.las.netty.service.svcinterface.IJkywzszService;
import com.matridx.las.netty.service.svcinterface.IProcessconfigService;
import com.matridx.las.netty.service.svcinterface.IYqxxinfosxService;
import com.matridx.las.netty.util.SendMessgeToHtml;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lashome")
public class YqxxInfoController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(YqxxInfoController.class);

    @Autowired
    private IYqxxInfoService yqxxInfoService;
    @Autowired
    private IYqxxService yqxxService;
    @Autowired
    private IJkywzszService jkywzszService;
    @Autowired
    private MaterialScienceInitService materialScienceInitService;
    @Autowired
    private IJcsjService jcsjService;
    @Autowired
    private IYqxxinfosxService yqxxinfosxService;
    @Autowired
    private IProcessconfigService processconfigService;
    /***
     * 添加仪器信息
     * @param list
     * @param request
     * @return
     */
    @RequestMapping("/yqxxinfo/saveyqxxinfo")
    @ResponseBody
    @Transactional
    public Map<String, Object> saveYqxxInfo(@RequestBody List<YqxxInfoDto> list, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            yqxxInfoService.insertYqxxInfo(list, getLoginInfo().getYhid());
            updateRedis(list);
            map.put("status", "true");
            map.put("message", "success");
        } catch (Exception e) {
            map.put("status", "false");
            map.put("message", e.getMessage());
        }
        return map;
    }

    /***
     * 修改仪器信息
     * @param jsonArray
     * @return
     */
    @RequestMapping("/yqxxinfo/updateyqxxinfo")
    @ResponseBody
    @Transactional
    public Map<String, Object> updateYqxxInfo(@RequestBody JSONArray jsonArray) {
        Map<String, Object> map = new HashMap<>();
        List<YqxxInfoDto> list = new ArrayList<>();
        List<YqxxInfoDto> newlist = new ArrayList<>();
        logger.info(jsonArray.toJSONString());
        //转格式
        try {
            for (Object ob : jsonArray) {
                if (ob != null) {
                    Map<String, Object> mapob = (Map) ob;
                    List<JkywzszDto> lt = new ArrayList<>();
                    if (mapob.get("jkywzszDtoList") != null && StringUtil.isNotBlank(mapob.get("jkywzszDtoList").toString())) {
                        if (mapob.get("lx") != null && MaterialTypeEnum.MATERIAL_CUBICS.getCode().equals(mapob.get("lx"))) {
                            lt = JSONArray.parseArray(mapob.get("jkywzszDtoList").toString(), JkywzszDto.class);
                        }
                        mapob.put("jkywzszDtoList", null);
                    }
                    List<YqxxinfosxDto> qx = new ArrayList<>();
                    if(mapob.get("yqxxinfosxDtos") != null && StringUtil.isNotBlank(mapob.get("yqxxinfosxDtos").toString())){
                        qx = JSONArray.parseArray(mapob.get("yqxxinfosxDtos").toString(), YqxxinfosxDto.class);
                    }
                    mapob.put("yqxxinfosxDtos", null);
                    YqxxInfoDto yqxxInfoDto = JSONObject.parseObject(JSONObject.toJSONString(mapob), YqxxInfoDto.class);
                    yqxxInfoDto.setJkywzszDtoList(lt);
                    yqxxInfoDto.setYqxxinfosxDtos(qx);
                    if (StringUtil.isBlank(yqxxInfoDto.getDeviceid())) {
                        map.put("status", "false");
                        map.put("message", "类型为" + MaterialTypeEnum.getValueByCode(yqxxInfoDto.getLx()) + "，仪器名为" + yqxxInfoDto.getName() + "的deviceid为空");
                        return map;
                    }

                    list.add(yqxxInfoDto);
                    if(StringUtil.isBlank(yqxxInfoDto.getYqxxid())){
                        newlist.add(yqxxInfoDto);
                    }
                }
            }
            if (list.size() > 0) {
                //TODO  根据已有id 修改 yqxx下的组件表 在删除
                //删除建库仪的通道设置
                jkywzszService.delJkywzAll();
                //删除属性
                yqxxinfosxService.deleteAll();
                yqxxInfoService.updateBySysId(list.get(0));
                yqxxInfoService.insertYqxxInfo(list, getLoginInfo().getYhid());
            }
            map.put("status", "true");
            map.put("message", "success");
        } catch (Exception e) {
             map.put("status", "false");
            map.put("message", e.getMessage());
        }

        return map;
    }

    /***
     * 新添加的仪器在rieds中更新信息
     * @param list 需要更新到redis的组件或者仪器
     * @return
     */
    private boolean updateRedis( List<YqxxInfoDto> list){
        boolean flag=false;
        if(list.size()>0){
            materialScienceInitService.initRedis(list);
        }

        return flag;
    }

    /***
     * 仪器信息
     * @param dto
     * @return
     */
    @RequestMapping("/yqxxinfo/getYqxxInfoLit")
    @ResponseBody
    public Map<String, Object> getYqxxInfoList(YqxxInfoDto dto) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<YqxxInfoDto> list = yqxxInfoService.getYqxxList(dto);
            YqxxDto yqxxDto = new YqxxDto();
            List<YqxxDto> yqxxList = yqxxService.getDtoList(yqxxDto);
            List<JcsjDto> listJc = jcsjService.getDtoListbyJclb(BasicDataTypeEnum.YQ_PROPERTY_TYPE);
            JSONArray sxlxJarry = new JSONArray();
            for (JcsjDto jcsjDto : listJc) {
                JSONObject sxlx = new JSONObject();
                sxlx.put("value", jcsjDto.getCsid());
                sxlx.put("text", jcsjDto.getCsmc());
                sxlxJarry.add(sxlx);
            }
            map.put("list", list);
            map.put("yqxxList", yqxxList);
            map.put("sxlx",sxlxJarry);
            map.put("status", "true");
            map.put("message", "success");
            //封装下建库仪已上报的通道号
            JSONArray jsonArray1 = jkywzszService.getJkytdAndState();
            map.put("jkytd", jsonArray1);
            //展示流程引导和流程选择
            JSONArray pcJarry = new JSONArray();
            List<JcsjDto> listPc = jcsjService.getDtoListbyJclb(BasicDataTypeEnum.YQ_PROCESS_TYPE);
            for (JcsjDto jcsjDto : listPc) {
                //通过基础数据查找流程需要引导的
                JSONObject pclc = new JSONObject();
                pclc.put("value", jcsjDto.getCsid());
                pclc.put("text", jcsjDto.getCsmc());
                pclc.put("sfmr", jcsjDto.getSfmr());
                List<ProcessconfigDto> pcList = processconfigService.getDtoByCsid(jcsjDto.getCsid());
                pclc.put("pclsit",pcList);
                pcJarry.add(pclc);
            }
            map.put("lcyd",pcJarry);
            JSONArray a = JSONArray.parseArray(JSONObject.toJSONString(list));
            logger.info(a.toJSONString());
        } catch (Exception e) {
            map.put("status", "false");
            map.put("message", e.getMessage());
        }

        return map;
    }
}

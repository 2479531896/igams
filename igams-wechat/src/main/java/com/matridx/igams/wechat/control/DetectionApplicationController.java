package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.wechat.dao.entities.JcsqglDto;
import com.matridx.igams.wechat.dao.entities.JcsqmxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.service.svcinterface.IJcsqglService;
import com.matridx.igams.wechat.service.svcinterface.IJcsqmxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/application")
public class DetectionApplicationController extends BaseController {


    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private IJcsqglService jcsqglService;
    @Autowired
    private IJcsqmxService jcsqmxService;
    @Autowired
    private ISjxxService sjxxService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    ICommonService commonService;



    /**
     * 检出申请列表 页面初始化
     */
    @RequestMapping("/application/pageListDetectionApplication")
    public ModelAndView pageListDetectionApplication(JcsqglDto jcsqglDto) {
        ModelAndView mav=new ModelAndView("wechat/detectionApplication/detectionApplication_list");
        mav.addObject("sqlxlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTIONAPPLICATION_TYPE.getCode()));
        mav.addObject("jcsqglDto",jcsqglDto);
        return mav;
    }

    /**
     * 检出申请列表 钉钉接口
     */
    @RequestMapping("/application/pagedataListInterface")
    @ResponseBody
    public Map<String, Object> pagedataListInterface() {
        Map<String, Object> map = new HashMap<>();
        map.put("sqlxlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTIONAPPLICATION_TYPE.getCode()));
        return map;
    }

    /**
     * 检出申请列表 数据查询
     */
    @RequestMapping("/application/pageGetListDetectionApplication")
    @ResponseBody
    public Map<String, Object> pageGetListDetectionApplication(JcsqglDto jcsqglDto) {
        Map<String, Object> map = new HashMap<>();
        if(StringUtil.isNotBlank(jcsqglDto.getTab_flag())&&"1".equals(jcsqglDto.getTab_flag())){
            User user=getLoginInfo();
            jcsqglDto.setLrry(user.getYhid());
        }
        List<JcsqglDto> list = jcsqglService.getPagedDtoList(jcsqglDto);
        try{
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_DETECTIONAPPLICATION.getCode(), "zt", "sqglid", new String[]{
                    StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
        } catch (BusinessException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        map.put("total",jcsqglDto.getTotalNumber());
        map.put("rows",list);
        return map;
    }

    /**
     * 检出申请列表  查看详情
     * @return
     */
    @RequestMapping("/application/viewDetectionApplication")
    public ModelAndView viewDetectionApplication(JcsqglDto jcsqglDto) {
        ModelAndView mav = new ModelAndView("wechat/detectionApplication/detectionApplication_view");
        JcsqglDto dto = jcsqglService.getDto(jcsqglDto);
        mav.addObject("jcsqglDto", dto);
        JcsqmxDto jcsqmxDto=new JcsqmxDto();
        jcsqmxDto.setSqglid(jcsqglDto.getSqglid());
        List<JcsqmxDto> dtoList = jcsqmxService.getDtoList(jcsqmxDto);
        mav.addObject("jcsqmxDtos", dtoList);
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(jcsqglDto.getSqglid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_DETECTION_APPLICATION_FILE.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        fjcfbDto.setYwlx(BusTypeEnum.IMP_LIBRARY_JCSQ.getCode());
        List<FjcfbDto> jcck_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("jcck_fjcfbDtos",jcck_fjcfbDtos);
        String jcpath= (String) redisUtil.hget(jcsqglDto.getSqglid(),"path");
        if(StringUtil.isNotBlank(jcpath)){
            String jcwjm=jcpath.substring(jcpath.lastIndexOf("/")+1,jcpath.length());
            mav.addObject("jcwjm",jcwjm);
        }else{
            mav.addObject("jcwjm","");
        }

        return mav;
    }

    /**
     * 查看钉钉审批流程
     * @param jcsqglDto
     * @return
     */
    @RequestMapping("/application/pagedataViewDd")
    public ModelAndView pagedataViewDd(JcsqglDto jcsqglDto){
        JcsqglDto dto = jcsqglService.getDto(jcsqglDto);
        ModelAndView mav = new ModelAndView("wechat/detectionApplication/detectionApplication_view_dd");
        User user=getLoginInfo();
        User user_redis = redisUtil.hugetDto("Users", user.getYhm());
        String wbcxid=user_redis.getWbcxid();
        Map<String, String> result = talkUtil.getProcessMessage(dto.getDdslid(),talkUtil.getToken(wbcxid));
        Map<String, String> body=JSON.parseObject(result.get("body"),Map.class);
        Map<String,String> process_instance=JSON.parseObject(JSON.toJSONString(body.get("process_instance")),Map.class);
        List<Map> jsonArray= JSON.parseArray(JSON.toJSONString(process_instance.get("operation_records")),Map.class);
        List<Map<String,String>>mapList=new ArrayList<>();

        for(int i=0;i<jsonArray.size();i++){
            Map<String,String> job =jsonArray.get(i);
            User use1=new User();
            use1.setDdid(job.get("userid"));
            List<User> usersByDdid = commonService.getUserByDdid(use1);
            Map<String,String> resultMap=new HashMap<>();
            resultMap.put("userName",usersByDdid.get(0).getZsxm());
            resultMap.put("ddtxlj",usersByDdid.get(0).getDdtxlj());
            if(i==0){
                resultMap.put("msg",job.get("date")+" 发起申请");
                resultMap.put("remark","");
            }else{
                String str="";
                switch (job.get("operation_result")==null?"":job.get("operation_result")){
                    case "AGREE":
                        str=" 同意";
                        break;
                    case "REFUSE":
                        str=" 拒绝";
                        break;
                    case "NONE":
                        str=" 审批中";
                        break;
                }
                resultMap.put("msg",job.get("date")+str);//是否通用

                resultMap.put("remark",job.get("remark")==null?"":job.get("remark"));//评论
            }
            mapList.add(resultMap);
        }
        List<Map> taskArray= JSON.parseArray(JSON.toJSONString(process_instance.get("tasks")),Map.class);
        for(int i=0;i<taskArray.size();i++){
            Map<String,String> job =taskArray.get(i);
            User use1=new User();
            use1.setDdid(job.get("userid"));
            List<User> usersByDdid = commonService.getUserByDdid(use1);
            Map<String,String> resultMap=new HashMap<>();
            resultMap.put("userName",usersByDdid.get(0).getZsxm());
            resultMap.put("ddtxlj",usersByDdid.get(0).getDdtxlj());
            if("RUNNING".equals(job.get("task_status"))){
                resultMap.put("msg",(job.get("finish_time")==null?"":job.get("finish_time"))+" 处理中");
                resultMap.put("remark","");
                mapList.add(resultMap);
            }
        }
        mav.addObject("ddsl",mapList);
        return mav;
    }
    /**
     * 检出申请列表 钉钉查看详情接口
     */
    @RequestMapping("/application/pagedataViewInterface")
    @ResponseBody
    public Map<String, Object> pagedataViewInterface(JcsqglDto jcsqglDto) {
        Map<String, Object> map = new HashMap<>();
        JcsqglDto dto = jcsqglService.getDto(jcsqglDto);
        map.put("jcsqglDto", dto);
        JcsqmxDto jcsqmxDto=new JcsqmxDto();
        jcsqmxDto.setSqglid(jcsqglDto.getSqglid());
        List<JcsqmxDto> dtoList = jcsqmxService.getDtoList(jcsqmxDto);
        map.put("jcsqmxDtos", dtoList);
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(jcsqglDto.getSqglid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_DETECTION_APPLICATION_FILE.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        map.put("fjcfbDtos",fjcfbDtos);
        User user=getLoginInfo();
        User user_redis = redisUtil.hugetDto("Users", user.getYhm());
        String wbcxid=user_redis.getWbcxid();
        Map<String, String> result = talkUtil.getProcessMessage(dto.getDdslid(),talkUtil.getToken(wbcxid));
        Map<String, String> body=JSON.parseObject(result.get("body"),Map.class);
        Map<String,String> process_instance=JSON.parseObject(JSON.toJSONString(body.get("process_instance")),Map.class);
        List<Map> jsonArray= JSON.parseArray(JSON.toJSONString(process_instance.get("operation_records")),Map.class);
        List<Map<String,String>>mapList=new ArrayList<>();

        for(int i=0;i<jsonArray.size();i++){
            Map<String,String> job =jsonArray.get(i);
            User use1=new User();
            use1.setDdid(job.get("userid"));
            List<User> usersByDdid = commonService.getUserByDdid(use1);
            Map<String,String> resultMap=new HashMap<>();
            resultMap.put("userName",usersByDdid.get(0).getZsxm());
            resultMap.put("ddtxlj",usersByDdid.get(0).getDdtxlj());
            if(i==0){
                resultMap.put("msg",job.get("date")+" 发起申请");
            }else{
                String str="";
                switch (job.get("operation_result")==null?"":job.get("operation_result")){
                    case "AGREE":
                        str=" 同意";
                        break;
                    case "REFUSE":
                        str=" 拒绝";
                        break;
                    case "NONE":
                        str=" 审批中";
                        break;
                }
                resultMap.put("msg",job.get("date")+str);//是否通用

                resultMap.put("remark",job.get("remark")==null?"":job.get("remark"));//评论
            }
            mapList.add(resultMap);
        }

        List<Map> taskArray= JSON.parseArray(JSON.toJSONString(process_instance.get("tasks")),Map.class);
        for(int i=0;i<taskArray.size();i++){
            Map<String,String> job =taskArray.get(i);
            User use1=new User();
            use1.setDdid(job.get("userid"));
            List<User> usersByDdid = commonService.getUserByDdid(use1);
            Map<String,String> resultMap=new HashMap<>();
            resultMap.put("userName",usersByDdid.get(0).getZsxm());
            resultMap.put("ddtxlj",usersByDdid.get(0).getDdtxlj());
            if("RUNNING".equals(job.get("task_status"))){
                resultMap.put("msg",(job.get("finish_time")==null?"":job.get("finish_time"))+" 处理中");
                resultMap.put("remark","");
                mapList.add(resultMap);
            }
        }
        map.put("ddsl",mapList);
        return map;
    }

    /**
     * 检出申请列表  新增(无权限限制)
     * @return
     */
    @RequestMapping("/application/addDetectionApplication")
    public ModelAndView addDetectionApplication(JcsqglDto jcsqglDto) {
        ModelAndView mav = new ModelAndView("wechat/detectionApplication/detectionApplication_add");
        jcsqglDto.setFormAction("addSaveDetectionApplication");
        jcsqglDto.setLx("0");//设置默认选中
        mav.addObject("fjcfbDtos",new ArrayList<>());
        mav.addObject("sqlxlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTIONAPPLICATION_TYPE.getCode()));
        mav.addObject("ywlx",BusTypeEnum.IMP_DETECTION_APPLICATION_FILE.getCode());
        mav.addObject("jcsqglDto",jcsqglDto);
        mav.addObject("xzbj","0");
        return mav;
    }

    /**
     * 检出申请列表  新增(伙伴限制)
     * @return
     */
    @RequestMapping("/application/addOpenDetectionApplication")
    public ModelAndView addOpenDetectionApplication(JcsqglDto jcsqglDto) {
        ModelAndView mav = new ModelAndView("wechat/detectionApplication/detectionApplication_add");
        jcsqglDto.setFormAction("addSaveOpenDetectionApplication");
        jcsqglDto.setLx("0");//设置默认选中
        mav.addObject("fjcfbDtos",new ArrayList<>());
        mav.addObject("sqlxlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTIONAPPLICATION_TYPE.getCode()));
        mav.addObject("ywlx",BusTypeEnum.IMP_DETECTION_APPLICATION_FILE.getCode());
        mav.addObject("jcsqglDto",jcsqglDto);
        mav.addObject("xzbj","1");
        return mav;
    }

    /**
     * 检出申请列表 钉钉新增接口
     */
    @RequestMapping("/application/pagedataAddInterface")
    @ResponseBody
    public Map<String, Object> pagedataAddInterface(JcsqglDto jcsqglDto) {
        Map<String, Object> map = new HashMap<>();
        jcsqglDto.setFormAction("addSaveDetectionApplication");
        map.put("fjcfbDtos",new ArrayList<>());
        map.put("sqlxlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTIONAPPLICATION_TYPE.getCode()));
        map.put("ywlx",BusTypeEnum.IMP_DETECTION_APPLICATION_FILE.getCode());
        map.put("jcsqglDto",jcsqglDto);
        return map;
    }

    /**
     * 获取明细
     */
    @RequestMapping("/application/pagedataGetDetails")
    @ResponseBody
    public Map<String, Object> pagedataGetDetails(JcsqmxDto jcsqmxDto) {
        Map<String, Object> map = new HashMap<>();
        List<JcsqmxDto> list = new ArrayList<>();
        if(StringUtil.isNotBlank(jcsqmxDto.getSqglid())){
            list=jcsqmxService.getDtoList(jcsqmxDto);
            List<String> sjids=new ArrayList<>();
            for(JcsqmxDto dto:list){
                sjids.add(dto.getSjid());
            }
            if(sjids!=null&&sjids.size()>0){
                JcsqglDto jcsqglDto=new JcsqglDto();
                jcsqglDto.setYbbhs(sjids);
                List<JcsqglDto> wkbmsByYbbhs = jcsqglService.getWkbmsByYbbhs(jcsqglDto);
                if(wkbmsByYbbhs!=null&&wkbmsByYbbhs.size()>0){
                    for(JcsqmxDto dto:list){
                        for(JcsqglDto jcsqglDto_t:wkbmsByYbbhs){
                            if(dto.getSjid().equals(jcsqglDto_t.getSjid())){
                                if(StringUtil.isBlank(dto.getWkbh())){
                                    dto.setWkbh(jcsqglDto_t.getWkbm());
                                }else{
                                    dto.setWkbh(dto.getWkbh()+","+jcsqglDto_t.getWkbm());
                                }
                            }
                        }
                    }
                }
            }
        }
        map.put("rows",list);
        return map;
    }

    /**
     * 送检信息列表
     * @param sjxxDto
     * @return
     */

    @RequestMapping(value="/application/pagedataListInspection")
    @ResponseBody
    public Map<String, Object> pageGetListInspection(SjxxDto sjxxDto,HttpServletRequest request){
        User user=getLoginInfo();
        List<SjxxDto> sjxxlist;
        Map<String,Object> params = sjxxService.pareMapFromDto(sjxxDto);
        String s_sjhbfl = request.getParameter("sjhbfls");
        if(StringUtil.isNotBlank(s_sjhbfl)) {
            String[] sjhbfls = s_sjhbfl.split(",");
            for (int i = 0; i < sjhbfls.length; i++){
                sjhbfls[i] = sjhbfls[i].replace("'","");
            }
            params.put("sjhbfls", sjhbfls);
        }
        String kylxs = request.getParameter("kylxs");
        if(StringUtil.isNotBlank(kylxs)) {
            String[] s_kylxs = kylxs.split(",");
            for (int i = 0; i < s_kylxs.length; i++){
                s_kylxs[i] = s_kylxs[i].replace("'","");
            }
            params.put("kylxs", s_kylxs);
        }
        sjxxlist=sjxxService.getDtoListOptimize(params);
        sjxxDto.setTotalNumber((int)params.get("totalNumber"));
        Map<String, Object> map= new HashMap<>();
        //判断是否有实付金额字段权限，有就计算
        if (StringUtil.isNotBlank(sjxxDto.getSfzjezdqx())&&"1".equals(sjxxDto.getSfzjezdqx())){
            SjxxDto sjxxDto_z = sjxxService.getSfzjeAndTkzje(sjxxDto);
            map.put("sfzje", sjxxDto_z.getSfzje());
        }
        if (StringUtil.isNotBlank(sjxxDto.getTkzjezdqx())&&"1".equals(sjxxDto.getTkzjezdqx())){
            SjxxDto sjxxDto_z = sjxxService.getSfzjeAndTkzje(sjxxDto);
            map.put("tkzje", sjxxDto_z.getTkzje());
        }
        map.put("total", sjxxDto.getTotalNumber());
        map.put("rows", sjxxlist);
        //需要筛选钉钉字段的，请调用该方法
        screenClassColumns(request,map);
        return map;
    }

    /**
     * 选择标本列表
     * @return
     */
    @RequestMapping("/application/pagedataChooseSamples")
    public ModelAndView pagedataChooseSamples(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("wechat/detectionApplication/detectionApplication_choose");
        mav.addObject("xzbj",request.getParameter("xzbj"));
        return mav;
    }

    /**
     * 获取标本信息
     */
    @RequestMapping("/application/pagedataGetSamples")
    @ResponseBody
    public Map<String, Object> pagedataGetSamples(SjxxDto sjxxDto) {
        Map<String, Object> map = new HashMap<>();
        List<SjxxDto> sjxxDtos = sjxxService.getDtoList(sjxxDto);
        if(sjxxDtos!=null&&sjxxDtos.size()>0){
            List<String> sjids=new ArrayList<>();
            for(SjxxDto dto:sjxxDtos){
                sjids.add(dto.getSjid());
            }
            if(sjids!=null&&sjids.size()>0){
                JcsqglDto jcsqglDto=new JcsqglDto();
                jcsqglDto.setYbbhs(sjids);
                List<JcsqglDto> wkbmsByYbbhs = jcsqglService.getWkbmsByYbbhs(jcsqglDto);
                if(wkbmsByYbbhs!=null&&wkbmsByYbbhs.size()>0){
                    for(SjxxDto dto:sjxxDtos){
                        for(JcsqglDto jcsqglDto_t:wkbmsByYbbhs){
                            if(dto.getSjid().equals(jcsqglDto_t.getSjid())){
                                if(StringUtil.isBlank(dto.getWkbh())){
                                    dto.setWkbh(jcsqglDto_t.getWkbm());
                                }else{
                                    dto.setWkbh(dto.getWkbh()+","+jcsqglDto_t.getWkbm());
                                }
                            }
                        }
                    }
                }
            }
        }
        map.put("sjxxDtos",sjxxDtos);
        return map;
    }

    /**
     * 验证标本信息
     */
    @RequestMapping("/application/pagedataVerifySamples")
    @ResponseBody
    public Map<String, Object> pagedataVerifySamples(JcsqmxDto jcsqmxDto) {
        Map<String, Object> map = new HashMap<>();
        List<JcsqmxDto> jcsqmxDtos = jcsqmxService.verifySamples(jcsqmxDto);
        if(jcsqmxDtos!=null&&jcsqmxDtos.size()>0){
            map.put("status","false");
        }else{
            map.put("status","true");
        }
        return map;
    }

    /**
     * 检出申请列表 新增保存
     */
    @RequestMapping("/application/addSaveDetectionApplication")
    @ResponseBody
    public Map<String, Object> addSaveDetectionApplication(JcsqglDto jcsqglDto) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo();
        jcsqglDto.setLrry(user.getYhid());
        boolean isSuccess = jcsqglService.addSaveDetectionApplication(jcsqglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        map.put("auditType",AuditTypeEnum.AUDIT_DETECTIONAPPLICATION.getCode());
        map.put("ywid",jcsqglDto.getSqglid());
        return map;
    }

    @RequestMapping("/application/addSaveOpenDetectionApplication")
    @ResponseBody
    public Map<String, Object> addSaveOpenDetectionApplication(JcsqglDto jcsqglDto) throws BusinessException {
        return addSaveDetectionApplication(jcsqglDto);
    }

    /**
     * 压缩下载
     *
     * @param httpResponse
     * @return
     */
    @RequestMapping("/application/pagedataDetectionZipDownload")
    @ResponseBody
    public void pagedataZipDownload(HttpServletRequest request,HttpServletResponse httpResponse) throws FileNotFoundException, BusinessException {
        jcsqglService.pagedataDetectionZipDownload(request,httpResponse);
    }

    /**
     * 检出申请列表  修改
     * @return
     */
    @RequestMapping("/application/modDetectionApplication")
    public ModelAndView modDetectionApplication(JcsqglDto jcsqglDto) {
        ModelAndView mav = new ModelAndView("wechat/detectionApplication/detectionApplication_add");
        jcsqglDto = jcsqglService.getDto(jcsqglDto);
        jcsqglDto.setFormAction("modSaveDetectionApplication");
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(jcsqglDto.getSqglid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_DETECTION_APPLICATION_FILE.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("sqlxlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTIONAPPLICATION_TYPE.getCode()));
        mav.addObject("ywlx",BusTypeEnum.IMP_DETECTION_APPLICATION_FILE.getCode());
        mav.addObject("jcsqglDto",jcsqglDto);
        return mav;
    }

    /**
     * 检出申请列表 钉钉修改接口
     */
    @RequestMapping("/application/pagedataModInterface")
    @ResponseBody
    public Map<String, Object> pagedataModInterface(JcsqglDto jcsqglDto) {
        Map<String, Object> map = new HashMap<>();
        jcsqglDto = jcsqglService.getDto(jcsqglDto);
        jcsqglDto.setFormAction("modSaveDetectionApplication");
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(jcsqglDto.getSqglid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_DETECTION_APPLICATION_FILE.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        map.put("fjcfbDtos",fjcfbDtos);
        map.put("sqlxlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTIONAPPLICATION_TYPE.getCode()));
        map.put("ywlx",BusTypeEnum.IMP_DETECTION_APPLICATION_FILE.getCode());
        map.put("jcsqglDto",jcsqglDto);
        return map;
    }

    /**
     * 检出申请列表 修改保存
     */
    @RequestMapping("/application/modSaveDetectionApplication")
    @ResponseBody
    public Map<String, Object> modSaveDetectionApplication(JcsqglDto jcsqglDto) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo();
        jcsqglDto.setXgry(user.getYhid());
        boolean isSuccess = jcsqglService.modSaveDetectionApplication(jcsqglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        map.put("auditType",AuditTypeEnum.AUDIT_DETECTIONAPPLICATION.getCode());
        map.put("ywid",jcsqglDto.getSqglid());
        return map;
    }

    /**
     * 检出申请列表 删除
     */
    @RequestMapping("/application/delDetectionApplication")
    @ResponseBody
    public Map<String, Object> delDetectionApplication(JcsqglDto jcsqglDto) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo();
        jcsqglDto.setScry(user.getYhid());
        boolean isSuccess = jcsqglService.delDetectionApplication(jcsqglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00003").getXxnr()):xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 	审核列表
     * @return
     */
    @RequestMapping("/application/pageListAuditDetectionApplication")
    public ModelAndView pageListAuditDetectionApplication() {
        ModelAndView mav = new  ModelAndView("wechat/detectionApplication/detectionApplication_auditList");
        mav.addObject("sqlxlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTIONAPPLICATION_TYPE.getCode()));
        return mav;
    }

    /**
     * 	审核列表
     * @param jcsqglDto
     * @return
     */
    @RequestMapping("/application/pageGetListAuditDetectionApplication")
    @ResponseBody
    public Map<String, Object> pageGetListAuditDetectionApplication(JcsqglDto jcsqglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // 附加委托参数
        DataPermission.addWtParam(jcsqglDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(jcsqglDto.getDqshzt())) {
            DataPermission.add(jcsqglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "jcsqgl", "sqglid",
                    AuditTypeEnum.AUDIT_DETECTIONAPPLICATION);
        } else {
            DataPermission.add(jcsqglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "jcsqgl", "sqglid",
                    AuditTypeEnum.AUDIT_DETECTIONAPPLICATION);
        }
        DataPermission.addCurrentUser(jcsqglDto, getLoginInfo(request));
        List<JcsqglDto> listMap = jcsqglService.getPagedAuditDetectionApplication(jcsqglDto);
        map.put("total", jcsqglDto.getTotalNumber());
        map.put("rows", listMap);
        return map;
    }

    /**
     * 上传页面
     * @param jcsqglDto
     * @return
     */
    @RequestMapping(value ="/application/uploadDetectionApplication")
    public ModelAndView uploadDetectionApplication(JcsqglDto jcsqglDto){
        ModelAndView mav = new ModelAndView("wechat/detectionApplication/detectionApplication_upload");
        //获取文件类型
        jcsqglDto.setYwlx(BusTypeEnum.IMP_DETECTION_APPLICATION_FILE.getCode());
        mav.addObject("jcsqglDto", jcsqglDto);
        return mav;
    }


    /**
     * 检出申请列表 上传保存
     */
    @RequestMapping("/application/uploadSaveDetectionApplication")
    @ResponseBody
    public Map<String, Object> uploadSaveDetectionApplication(JcsqglDto jcsqglDto) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo();
        jcsqglDto.setScry(user.getYhid());
        boolean isSuccess = jcsqglService.uploadSaveDetectionApplication(jcsqglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?"上传成功!":"上传失败!");
        return map;
    }

    /**
     * 上传的Excel解析
     * @param fjids
     */
    @RequestMapping(value="/application/pagedataAnalysisFile")
    @ResponseBody
    public Map<String, Object> pagedataAnalysisFile(String fjids,String xzbj) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(fjids) && !"null".equals(fjids)) {
            User user=getLoginInfo();

            map=jcsqglService.analysisFile(fjids,xzbj,user);
        }
        return map;
    }
}

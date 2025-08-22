package com.matridx.crf.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.matridx.crf.web.dao.entities.*;
import com.matridx.crf.web.service.svcinterface.*;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.web.dao.entities.XtjsDto;
import com.matridx.web.service.svcinterface.IXtjsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/crf")
public class CrfController extends BaseController {

    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    private IXxglService xxglservice;

    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    INdzxxService ndzxxService;
    @Autowired
    IJnsjbgjlService jnsjbgjlService;
    @Autowired
    IJnsjhzxxService jnsjhzxxService;
    @Autowired
    IJnsjjcjgService jnsjjcjgService;
    @Autowired
    IJnsjcdizlService jnsjcdizlService;
    @Autowired
    IJnsjcdiqyysService jnsjcdiqyysService;
    @Autowired
    private IXxglService xxglService;
    @Autowired
    IXtjsService xtjsService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 条件查找
     * @param ndzxxDto
     * @return
     */
    @RequestMapping(value ="/casereport/pageListNdz")
    public ModelAndView pageListNdz(NdzxxDto ndzxxDto) {
        ModelAndView mav=new ModelAndView("casereport/dailycasereport/ndz_List");
        mav.addObject("ndzxxDto", ndzxxDto);
        return mav;
    }

    /**
     * 条件查找列表信息
     * @param ndzxxDto
     * @return
     */
    @RequestMapping("/ndzview/getSelectPage")
    @ResponseBody
    public Map<String, Object> NdzPage(NdzxxDto ndzxxDto){
        Map<String, Object> map=new HashMap<String, Object>();
        List<NdzxxDto> ndzxxList=ndzxxService.getPagedDtoList(ndzxxDto);
        map.put("rows", ndzxxList);
        map.put("total", ndzxxDto.getTotalNumber());
        return map;
    }

    /**
     * 查看患者信息页面
     * @param ndzxxDto
     * @return
     */
    @RequestMapping("/dailycasereport/viewHzxx")
    public ModelAndView viewHzxx(NdzxxDto ndzxxDto) {
        ModelAndView mav=new ModelAndView("casereport/dailycasereport/personalData");
        if("第一日".equals(ndzxxDto.getJldjt())){
            ndzxxDto.setJldjt("1");
        }else if("第三日".equals(ndzxxDto.getJldjt())){
            ndzxxDto.setJldjt("3");
        }else if("第五日".equals(ndzxxDto.getJldjt())){
            ndzxxDto.setJldjt("5");
        }else if("第七日".equals(ndzxxDto.getJldjt())){
            ndzxxDto.setJldjt("7");
        }
        NdzxxDto ndzxxDto_hz = ndzxxService.getHzxx(ndzxxDto);
        NdzxxDto ndzxxDto_jl = ndzxxService.getJlxx(ndzxxDto);
        List<NdzdmxqDto> dmxqList = ndzxxService.getDmxq(ndzxxDto);
        List<NdzyzzbDto> yzzbList = ndzxxService.getYzzb(ndzxxDto);
        List<NdzshDto> shList = ndzxxService.getSh(ndzxxDto);
        List<NdzxcgDto> xcgList = ndzxxService.getXcg(ndzxxDto);
        FjcfbDto fjcfbDto = ndzxxService.getFjcfb(ndzxxDto);
        if (ndzxxDto_hz.getJwhbz() != null) {
            JcsjDto jwhbsjsDto = new JcsjDto();
            jwhbsjsDto.setJclb(BasicDataTypeEnum.COMPLICATION.getCode());
            jwhbsjsDto.setIds(ndzxxDto_hz.getJwhbz());
            List<JcsjDto> jwhbzjcsjDtos = jcsjService.selectDetectionUnit(jwhbsjsDto);
            mav.addObject("jwhbzjcsjDtos", jwhbzjcsjDtos);

        }
        if (ndzxxDto_hz.getGrbw() != null) {
            JcsjDto grbwjsDto = new JcsjDto();
            grbwjsDto.setJclb(BasicDataTypeEnum.ANTIBACTERIALHISTORY.getCode());
            grbwjsDto.setIds(ndzxxDto_hz.getGrbw());
            List<JcsjDto> grbwjcsjDtos = jcsjService.selectDetectionUnit(grbwjsDto);
            mav.addObject("grbwjcsjDtos", grbwjcsjDtos);
        }
        mav.addObject("fjcfbDto",fjcfbDto);
        mav.addObject("dmxqList",dmxqList);
        mav.addObject("yzzbList",yzzbList);
        mav.addObject("shList",shList);
        mav.addObject("xcgList",xcgList);
        mav.addObject("ndzxxDto_hz", ndzxxDto_hz);
        mav.addObject("ndzxxDto_jl", ndzxxDto_jl);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 病理报告导入页面
     * @return
     */
    @RequestMapping(value ="/hzxx/importReport")
    public ModelAndView importReport(){
        ModelAndView mav = new ModelAndView("casereport/dailycasereport/report_import");
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT.getCode());
        mav.addObject("fjcfbDto", fjcfbDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 病理报告word导入页面
     * @return
     */
    @RequestMapping(value ="/hzxx/importWordReport")
    public ModelAndView importWordReport(){
        ModelAndView mav = new ModelAndView("casereport/dailycasereport/word_import");
        FjcfbDto fjcfbDto = new FjcfbDto();
        List<NdzxxDto> ssyyList = ndzxxService.getSsyy();
        List<NdzxxDto> yzList = ndzxxService.getYz();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT.getCode());
        mav.addObject("ssyyList", ssyyList);
        mav.addObject("yzList", yzList);
        mav.addObject("fjcfbDto", fjcfbDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 病理报告word导入列表展示页面
     * @return
     */
    @RequestMapping(value ="/hzxx/importReportPage")
    @ResponseBody
    public Map<String, Object> importReportPage(FjcfbDto fjcfbDto){
        List<NdzxxDto> impList = ndzxxService.getImpList(fjcfbDto);
        //Json格式的要求{total:22,rows:{}}
        //构造成Json的格式传递
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("total", impList== null?0:impList.size());
        result.put("rows", impList);
        return result;
    }
    /**
     * 检查导入进度
     * @return
     */
    @RequestMapping(value ="/file/checkImpInfo")
    @ResponseBody
    public Map<String,Object> checkImpInfo(FjcfbDto fjcfbDto){

        //Json格式的要求{total:22,rows:{}}
        //构造成Json的格式传递
        Map<String, Object> msg = ndzxxService.checkImpFileProcess(fjcfbDto.getFjid());

        return msg;
    }

    /**
     * 显示检查结果后，用户点击继续，则进行实际的导入操作
     * @param imp_file
     * @param fjcfbDto
     * @return
     */
    @RequestMapping(value="/file/saveImportRecord")
    @ResponseBody
    public Map<String, Object> saveImportRecord(FjcfbDto fjcfbDto,HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        try{
            User user = getLoginInfo(request);
            fjcfbDto.setLrry(user.getYhid());
            boolean isSuccess = ndzxxService.saveWordReport(fjcfbDto,user);
            result.put("status", isSuccess?"success":"fail");
            result.put("msg", isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
            result.put("fjcfbDto", fjcfbDto);
        } catch (Exception e) {
            result.put("status", "fail");
            result.put("msg", e.getMessage());
        }
        return result;
    }
    /**
     * 所属医院统计
     * @param ndzxxDto
     * @return
     */
    @RequestMapping(value ="/casereport/pageSsyyStatistic")
    public ModelAndView pageSsyyStatistic(NdzxxDto ndzxxDto) {
        ModelAndView mav=new ModelAndView("casereport/ssyy_statistic");
        mav.addObject("ndzxxDto", ndzxxDto);
        return mav;
    }
    /**
     * 统计列表信息
     * @param ndzxxDto
     * @return
     */
    @RequestMapping("/casereport/getSsyyStatistic")
    @ResponseBody
    public Map<String, Object> getSsyyStatistic(NdzxxDto ndzxxDto){
        Map<String, Object> map=new HashMap<String, Object>();
        List<NdzxxDto> ssyy = ndzxxService.getPagedSsyy(ndzxxDto);
        for(int i=0;i<ssyy.size();i++){
            if(ssyy.get(i).getSsyy().equals("所有记录")){
                NdzxxDto ndzxxDto1=new NdzxxDto();
                ndzxxDto1=ssyy.get(0);
                ssyy.set(0,ssyy.get(i));
                ssyy.set(i,ndzxxDto1);
            }
        }
        map.put("rows", ssyy);
        map.put("total", ndzxxDto.getTotalNumber());
        return map;
    }
    /**
     * 上传页面
     * @param
     * @return
     */
    @RequestMapping(value ="/dailycasereport/uploadNdz")
    public ModelAndView uploadNdz(NdzxxDto ndzxxDto){
        ModelAndView mav = new ModelAndView("casereport/dailycasereport/ndz_upload");
        FjcfbDto fjcfbDto = ndzxxService.getFjcfb(ndzxxDto);
        ndzxxDto.setYwlx(BusTypeEnum.IMP_REPORT_HJYZ.getCode());
        mav.addObject("fjcfbDto",fjcfbDto);
        mav.addObject("ndzxxDto", ndzxxDto);
        return mav;
    }
    /**
     * 检验结果导入保存
     * @param ndzxxDto
     * @return
     */
    @RequestMapping(value = "/casereport/uploadNdzSave")
    @ResponseBody
    public Map<String, Object> uploadNdzSave(NdzxxDto ndzxxDto){
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            //获取用户信息
            User user = getLoginInfo();
            ndzxxDto.setLrry(user.getYhid());
            ndzxxDto.setXgry(user.getYhid());
            FjcfbDto fjcfb = ndzxxService.getFjcfb(ndzxxDto);
            if(fjcfb!=null){
                map.put("message","已有报告，如果需要覆盖原有报告，请先删除再上传");
            }else{
                boolean isSuccess = ndzxxService.uploadNdzSave(ndzxxDto);
                map.put("status", isSuccess?"success":"fail");
                map.put("message", isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
            }
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            map.put("status", "fail");
            map.put("message", e.toString());
        }
        return map;
    }

    /**
     * 艰难梭菌
     * @param jnsjbgjlDto
     * @return
     */
    @RequestMapping(value ="/cdiff/pageListJnsj")
    public ModelAndView pageListJnsj(JnsjbgjlDto jnsjbgjlDto) {
        ModelAndView mav=new ModelAndView("casereport/cdiff/jnsj_list");
        return mav;
    }

    /**
     * 艰难梭菌条件查找列表信息
     * @param jnsjbgjlDto
     * @return
     */
    @RequestMapping("/cdiff/listJnsj")
    @ResponseBody
    public Map<String, Object> listJnsj(JnsjbgjlDto jnsjbgjlDto){
        Map<String, Object> map=new HashMap<String, Object>();
        List<JnsjbgjlDto> jnsjList=jnsjbgjlService.getPagedDtoList(jnsjbgjlDto);
        map.put("rows", jnsjList);
        map.put("total", jnsjbgjlDto.getTotalNumber());
        return map;
    }

    /**
     * 艰难梭菌新增修改页面
     * @param jnsjbgjlDto
     * @return
     */
    @RequestMapping(value ="/cdiff/addJnsj")
    public ModelAndView addJnsj(JnsjbgjlDto jnsjbgjlDto) {
        ModelAndView mav=new ModelAndView("casereport/cdiff/jnsj_edit");
        JnsjhzxxDto jnsjhzxx = new JnsjhzxxDto();
        JnsjbgjlDto jnsjbgjl = new JnsjbgjlDto();
        jnsjbgjlDto.setFormAction("insertJnsj");
        String jsid = getLoginInfo().getDqjs();
        Map<String,Object>map=jnsjbgjlService.getDateByBgId(jnsjbgjlDto);
        List<JcsjDto>yysjcsj=jcsjService.getDtoListbyJclb(BasicDataTypeEnum.JNSJCDIZQYYS);
        List<JcsjDto>jnsjks=jcsjService.getDtoListbyJclb(BasicDataTypeEnum.JNSJKS);
        List<JcsjDto> jnsjdw = new ArrayList<>();
        XtjsDto xtjsDto = xtjsService.getDtoById(jsid);
        if ("1".equals(xtjsDto.getDwxdbj())){
            JnsjhzxxDto jnsjhzxxDto = new JnsjhzxxDto();
            jnsjhzxxDto.setDqjs(jsid);
            jnsjdw = jnsjhzxxService.getHospitailList(jnsjhzxxDto);
        }else {
            jnsjdw=jcsjService.getDtoListbyJclb(BasicDataTypeEnum.HOSPITAL);
        }
        //List<JcsjDto>jnsjdw=jcsjService.getDtoListbyJclb(BasicDataTypeEnum.JNSJDW);
        //List<JcsjDto> jnsjdw = jnsjhzxxService.getHospitailList(jnsjhzxxDto);
        mav.addObject("dataMap",map);
        mav.addObject("jnsjks",jnsjks);
        mav.addObject("jnsjdw",jnsjdw);
        mav.addObject("yysjcsj",yysjcsj);
        mav.addObject("jnsjhzxxDto",jnsjhzxx);
        mav.addObject("jnsjbgjlDto",jnsjbgjl);
        mav.addObject("jnsjbgjlDto",jnsjbgjlDto);
        return mav;
    }

    /***
     * 根据办理入组单位获取艰难梭菌报告数量
     * @param jnsjbgjlDto
     * @return
     */
    @RequestMapping("/cdiff/getrzbhsize")
    @ResponseBody
    public Map<String, Object> getRzbhSize(JnsjbgjlDto jnsjbgjlDto){
        Map<String, Object> map=new HashMap<String, Object>();

        //生成入组编号生成规则：艰难梭菌单位基础数据的参数代码 + 四位数字序号
        JcsjDto jc_dw = jcsjService.getDtoById(jnsjbgjlDto.getBlrzdw());
        String prefix = jnsjbgjlService.generateBlrzbh(jc_dw.getCsdm());
        map.put("size",jc_dw.getCsdm() + prefix);
        return map;
    }
    /**
     * 艰难梭菌新增数据库
     * @param formeData
     * @return
     */
    @RequestMapping("/cdiff/insertJnsj")
    @ResponseBody
    public Map<String, Object> insertJnsj(String formeData,HttpServletRequest request){
        Map<String, Object> map=new HashMap<String, Object>();
        JSONObject json=JSONObject.parseObject(formeData);

        json.put("userid",getLoginInfo(request).getYhid());
        boolean isSuccess = jnsjbgjlService.insertJnsj(json);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
                : xxglService.getModelById("ICOM00002").getXxnr());
//        List<JnsjbgjlDto> jnsjList=jnsjbgjlService.getPagedDtoList(jnsjbgjlDto);
//        map.put("rows", jnsjList);
//        map.put("total", jnsjbgjlDto.getTotalNumber());
        return map;
    }

    /**
     * 艰难梭菌新增数据库
     * @param formeData
     * @return
     */
    @RequestMapping("/cdiff/updateJnsj")
    @ResponseBody
    public Map<String, Object> updateJnsj(String formeData,HttpServletRequest request){
        Map<String, Object> map=new HashMap<String, Object>();
        JSONObject json=JSONObject.parseObject(formeData);
        json.put("userid",getLoginInfo(request).getYhid());
        boolean isSuccess = jnsjbgjlService.updateJnsj(json);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
                : xxglService.getModelById("ICOM00002").getXxnr());
//        List<JnsjbgjlDto> jnsjList=jnsjbgjlService.getPagedDtoList(jnsjbgjlDto);
//        map.put("rows", jnsjList);
//        map.put("total", jnsjbgjlDto.getTotalNumber());
        return map;
    }

    /**
     * 艰难梭菌患者信息
     * @param jnsjhzxxDto
     * @return
     */
    @RequestMapping(value ="/cdiff/pageListJnsjHzxx")
    public ModelAndView pageListJnsjHzxx(JnsjhzxxDto jnsjhzxxDto) {
        ModelAndView mav=new ModelAndView("casereport/cdiff/hzxx_list");
        Map<String, List<JcsjDto>> jcsjlist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[] { BasicDataTypeEnum.VISIT_TYPE, BasicDataTypeEnum.CASE_ENROLLMENT_UNIT, BasicDataTypeEnum.DEPARTMENT_TYPE});
        mav.addObject("visitList", jcsjlist.get(BasicDataTypeEnum.VISIT_TYPE.getCode()));// 就诊类型
        mav.addObject("blrzdwList", jcsjlist.get(BasicDataTypeEnum.CASE_ENROLLMENT_UNIT.getCode()));// 病例入组单位
        mav.addObject("ksList", jcsjlist.get(BasicDataTypeEnum.DEPARTMENT_TYPE.getCode()));// 科室
        return mav;
    }

    /**
     *艰难梭菌患者信息条件查找列表
     * @param jnsjhzxxDto
     * @return
     */
    @RequestMapping("/cdiff/listJnsjHzxx")
    @ResponseBody
    public Map<String, Object> listJnsjHzxx(JnsjhzxxDto jnsjhzxxDto){
        List<JnsjhzxxDto> jnsjList= new ArrayList<>();
        XtjsDto xtjsDto = xtjsService.getDtoById(getLoginInfo().getDqjs());
        if ("1".equals(xtjsDto.getDwxdbj())){
            jnsjhzxxDto.setDqjs(getLoginInfo().getDqjs());
            jnsjList=jnsjhzxxService.getPagedDtoList(jnsjhzxxDto);
        }else {
            jnsjList=jnsjhzxxService.getPagedDtoList(jnsjhzxxDto);
        }
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("rows", jnsjList);
        map.put("total", jnsjhzxxDto.getTotalNumber());
        return map;
    }

    /**
     * 艰难梭菌查看信息
     * @param jnsjbgjlDto
     * @return
     */
    @RequestMapping(value ="/cdiff/viewJnsjInfo")
    public ModelAndView viewJnsjInfo(JnsjbgjlDto jnsjbgjlDto,JnsjhzxxDto jnsjhzxxDto) {
        ModelAndView mav=new ModelAndView("casereport/cdiff/hzxx_view");
        jnsjbgjlDto = jnsjbgjlService.getViewDto(jnsjbgjlDto);
        mav.addObject("jnsjbgjlDto",jnsjbgjlDto);
        return mav;
    }

    /**
     * 查询患者信息的艰难梭菌检测结果历史记录
     * @param jnsjjcjgDto
     * @return
     */
    @RequestMapping("/cdiff/getjcjgList")
    @ResponseBody
    public Map<String, Object> getjcjgList(JnsjjcjgDto jnsjjcjgDto){
        List<JnsjjcjgDto> jcjglist = new ArrayList<>();
        jcjglist = jnsjjcjgService.getDtoList(jnsjjcjgDto);
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("rows", jcjglist);
        map.put("total", jnsjjcjgDto.getTotalNumber());
        return map;
    }

    /**
     * 查询患者信息的艰难梭菌治疗用药记录
     * @param jnsjcdizlDto
     * @return
     */
    @RequestMapping("/cdiff/getjnsjCdizlList")
    @ResponseBody
    public Map<String, Object> getjnsjCdizlList(JnsjcdizlDto jnsjcdizlDto){
        List<JnsjcdizlDto> cdizllist = new ArrayList<>();
        cdizllist = jnsjcdizlService.getDtoList(jnsjcdizlDto);
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("rows", cdizllist);
        map.put("total", jnsjcdizlDto.getTotalNumber());
        return map;
    }

    /**
     * 查询患者信息的艰难梭菌治疗用药记录
     * @param jnsjcdiqyysDto
     * @return
     */
    @RequestMapping("/cdiff/getjnsjCdiyylsList")
    @ResponseBody
    public Map<String, Object> getjnsjCdiyylsList(JnsjcdiqyysDto jnsjcdiqyysDto){
        List<JnsjcdiqyysDto> cdizqyyslist = new ArrayList<>();
        cdizqyyslist = jnsjcdiqyysService.getDtoList(jnsjcdiqyysDto);
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("rows", cdizqyyslist);
        map.put("total", jnsjcdiqyysDto.getTotalNumber());
        return map;
    }

    /**
     * 删除艰难梭菌信息记录
     * @param jnsjbgjlDto
     * @return
     */
    @RequestMapping("/cdiff/delJnsjInfo")
    @ResponseBody
    public Map<String, Object> delJnsjInfo(JnsjbgjlDto jnsjbgjlDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<String,Object>();
        User user = getLoginInfo(request);
        jnsjbgjlDto.setScry(user.getYhid());
        boolean isSuccess = jnsjbgjlService.delJnsjInfo(jnsjbgjlDto);
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    //扩展修改页面
    @RequestMapping(value ="/cdiff/extendMod")
    public ModelAndView extendMod(JnsjbgjlDto jnsjbgjlDto,JnsjhzxxDto jnsjhzxxDto) {
        ModelAndView mav=new ModelAndView("casereport/cdiff/hzxx_extendMod");
        jnsjbgjlDto = jnsjbgjlService.getDto(jnsjbgjlDto);
        mav.addObject("jnsjbgjlDto",jnsjbgjlDto);
        return mav;
    }

    //扩展修改保存数据库
    @RequestMapping("/cdiff/extendUpdateJnsj")
    @ResponseBody
    public Map<String, Object> extendUpdateJnsj(JnsjbgjlDto jnsjbgjlDto,HttpServletRequest request){
        Map<String, Object> map=new HashMap<String, Object>();
        jnsjbgjlDto.setXgry(getLoginInfo(request).getYhid());
        boolean isSuccess = jnsjbgjlService.extendUpdateJnsj(jnsjbgjlDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
}

package com.matridx.igams.crm.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IUserService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.crm.dao.entities.JxhsglDto;
import com.matridx.igams.crm.dao.entities.JxhsmxDto;
import com.matridx.igams.crm.service.svcinterface.IJxhsglService;
import com.matridx.igams.crm.service.svcinterface.IJxhsmxService;
import com.matridx.igams.wechat.dao.entities.SwyszkDto;
import com.matridx.igams.wechat.service.svcinterface.ISwyszkService;
import com.matridx.igams.wechat.service.svcinterface.IYhqtxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * PerformanceChecklistController
 * 绩效核对清单controller层
 * @author LittleRedBean
 * @version 1.0
 * @date 2024/12/17 上午9:07
 */

@Controller
@RequestMapping("/performanceCheck")
public class PerformanceChecklistController extends BaseController {

    @Autowired
    private IJxhsglService jxhsglService;
    @Autowired
    private IJxhsmxService jxhsmxService;
    @Autowired
    private ISwyszkService swyszkService;
    @Autowired
    private IXxdyService xxdyService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IXxglService xxglService;
    @Autowired
    private IYhqtxxService yhqtxxService;
    @Autowired
    private IUserService userService;

    @Autowired
    private IXtszService xtszService;

    /**
     * 分页获取绩效核对清单数据
     * @param jxhsglDto
     * @return
     */
    @RequestMapping("/accounting/pagedataListPerformanceCheck")
    @ResponseBody
    public Map<String, Object> pagedataListPerformanceCheck(JxhsglDto jxhsglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
//        super.setCzdmList(request,map);
//        super.setTyszList(request,map);
        List<JxhsglDto> list = jxhsglService.getPagedDtoList(jxhsglDto);
        map.put("total", jxhsglDto.getTotalNumber());
        map.put("rows",list);
        return map;
    }

    /**
     * 小程序获取个人历史绩效
     * @param jxhsglDto
     * @return
     */
    @RequestMapping("/accounting/pagedataPersionPerformance")
    @ResponseBody
    public Map<String, Object> pagedataPersionPerformance(JxhsglDto jxhsglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        List<JxhsglDto> list = jxhsglService.getDtoList(jxhsglDto);
        map.put("historylist",list);
        return map;
    }

    /**
     * 编辑保存(新增修改)
     * @return
     */
    @RequestMapping("/accounting/pagedataEditPerformanceCheck")
    @ResponseBody
    public Map<String,Object> pagedataEditPerformanceCheck(HttpServletRequest request) {
        User user = getLoginInfo();
        Map<String,Object> map = new HashMap<>();
        try {
            JxhsglDto jxhsglDto = JSONObject.parseObject(request.getParameter("jxhsglDto"),JxhsglDto.class);
            map = jxhsglService.editSaveJxhsgl(jxhsglDto,user);
        } catch (BusinessException e) {
            map.put("status","fail");
            map.put("message","保存失败!"+e.getMessage());
        }
        return map;
    }
    /**
     * 删除
     * @param jxhsglDto
     * @return
     */
    @RequestMapping("/accounting/pagedataDelPerformanceCheck")
    @ResponseBody
    public Map<String,Object> pagedataDelPerformanceCheck(JxhsglDto jxhsglDto) {
        User user = getLoginInfo();
        Map<String,Object> map = new HashMap<>();
        try {
            map = jxhsglService.delSaveJxhsgl(jxhsglDto,user);
        } catch (BusinessException e) {
            map.put("status","fail");
            map.put("message","删除失败!"+e.getMessage());
        }
        return map;
    }

    /**
     * 查看、修改初始化数据
     * @param jxhsglDto
     * @return
     */
    @RequestMapping("/accounting/pagedataInfoPerformanceCheck")
    @ResponseBody
    public Map<String,Object> pagedataInfoPerformanceCheck(JxhsglDto jxhsglDto) {
        Map<String,Object> map = new HashMap<>();
        JxhsglDto dto = jxhsglService.getDto(jxhsglDto);
        JxhsmxDto jxhsmxDto = new JxhsmxDto();
        jxhsmxDto.setJxhsglid(jxhsglDto.getJxhsglid());
        List<JxhsmxDto> dtoList = jxhsmxService.getDtoList(jxhsmxDto);
        map.put("jxhsglDto",dto);
        map.put("jxhsmxDtos",dtoList);
        map.put("shlb",AuditTypeEnum.AUDIT_PERFORMANCE_CHECK.getCode());
        return map;
    }

    @RequestMapping("/accounting/pagedataAddPerformanceCheck")
    @ResponseBody
    public Map<String,Object> pagedataAddPerformanceCheck(JxhsglDto jxhsglDto){
        Map<String,Object> map = new HashMap<>();
        XtszDto xtszDto=new XtszDto();//获取税点
        xtszDto.setSzlb("crm.performance.taxpoint");
        xtszDto=xtszService.getDto(xtszDto);
        if(xtszDto!=null && StringUtil.isNotBlank(xtszDto.getSzz())) {
            String sd = xtszDto.getSzz();
            map.put("sd",sd);
        }
        return map;
    }

    /**
     * 设置页面初始化
     * @param jxhsglDto
     * @return
     */
    @RequestMapping("/accounting/pagedataInitSettings")
    @ResponseBody
    public Map<String,Object> pagedataInitSettings(JxhsglDto jxhsglDto) {
        Map<String,Object> map = new HashMap<>();
        List<JcsjDto> sfList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PROVINCE.getCode());
        // 按省份拆分对账单的客户设置
        // 原信息yxx  kh => swkhgl.khid
        // 对应信息dyxx sf => jcsj.csid
        // kzcs1 fzr => xtyh.yhid
        XxdyDto xxdyDto_jxhssz1 = new XxdyDto();
        xxdyDto_jxhssz1.setDylxcsdm("JXHS_CFKHSZ");
        List<Map<String, Object>> xxdyDtos_jxhssz1 = xxdyService.getOriginList(xxdyDto_jxhssz1);
        for (Map<String, Object> xxdy : xxdyDtos_jxhssz1) {
            Optional<JcsjDto> optional = sfList.stream().filter(item -> item.getCsid().equals(xxdy.get("dyxx"))).findFirst();
            if (optional.isPresent()){
                xxdy.put("sfmc",optional.get().getCsmc());
            }
        }
        // 按客户剔除对账单的设置
        // 原信息yxx kh => swkhgl.khid
        // 对应信息dyxx 固定值 => TCKH
        XxdyDto xxdyDto_jxhssz2 = new XxdyDto();
        xxdyDto_jxhssz2.setDylxcsdm("JXHS_TCKHSZ");
        xxdyDto_jxhssz2.setDyxx("TCKH");
        List<Map<String, Object>> xxdyDtos_jxhssz2 = xxdyService.getOriginList(xxdyDto_jxhssz2);
        map.put("list_one",xxdyDtos_jxhssz1);//dyid,yxx,khmc,dyxx,sfmc,kzcs1,fzrmc
        map.put("list_two",xxdyDtos_jxhssz2);//dyid,yxx,khmc
        map.put("sfList",sfList);
        return map;
    }
    /**
     * 设置页面保存
     * @return
     */
    @RequestMapping("/accounting/pagedataSaveSettings")
    @ResponseBody
    public Map<String,Object> pagedataSaveSettings(HttpServletRequest request) {
        User user = getLoginInfo();
        Map<String,Object> map = new HashMap<>();
        try {
            String dataStr = request.getParameter("data");
            Map<String,Object> data = JSON.parseObject(dataStr);
            map = jxhsglService.editSaveSettings(data,user);
        } catch (BusinessException e) {
            map.put("status","fail");
            map.put("message","保存失败!"+e.getMessage());
        }
        return map;
    }
    /**
     * 拉取核算清单
     * @return
     */
    @RequestMapping("/accounting/pagedataPullPerformanceCheck")
    @ResponseBody
    public Map<String,Object> pagedataPullPerformanceCheck(HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
        SwyszkDto swyszkDto = new SwyszkDto();
        swyszkDto.setZdzqstart(request.getParameter("dzzqStart"));
        swyszkDto.setZdzqend(request.getParameter("dzzqEnd"));
        swyszkDto.setDjxs(request.getParameter("fzr"));
        List<SwyszkDto> list = swyszkService.getPullList(swyszkDto);
        list.forEach(item -> {
            item.setJxjsje(new BigDecimal(StringUtil.isNotBlank(item.getJsje())?item.getJsje():"0").subtract(new BigDecimal(StringUtil.isNotBlank(item.getJxje())?item.getJxje():"0")).toString());
        });
        map.put("list",list);
        map.put("status","success");
        return map;
    }

    /**
     * 绩效核算审核列表
     * @param jxhsglDto
     * @return
     */
    @RequestMapping("/accounting/pagedataAuditListPerformanceCheck")
    @ResponseBody
    public Map<String,Object> pagedataAuditListPerformanceCheck(JxhsglDto jxhsglDto,HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
        super.setCzdmList(request,map);
        // 附加委托参数
        DataPermission.addWtParam(jxhsglDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(jxhsglDto.getDqshzt())) {
            DataPermission.add(jxhsglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "jxhsgl", "jxhsglid",
                    AuditTypeEnum.AUDIT_PERFORMANCE_CHECK);
        } else {
            DataPermission.add(jxhsglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "jxhsgl", "jxhsglid",
                    AuditTypeEnum.AUDIT_PERFORMANCE_CHECK);
        }
        DataPermission.addCurrentUser(jxhsglDto, getLoginInfo());
        List<JxhsglDto> listMap = jxhsglService.getPagedAuditList(jxhsglDto);
        map.put("total", jxhsglDto.getTotalNumber());
        map.put("rows", listMap);
        map.put("shlb",AuditTypeEnum.AUDIT_PERFORMANCE_CHECK.getCode());
        return map;
    }

    @RequestMapping("/accounting/pagedataAccountingList")
    @ResponseBody
    public Map<String,Object> beAccountingList(){
        User user=getLoginInfo();
        Map<String,Object> map=new HashMap<>();
        String khrqstart="";
        String khrqend="";
        LocalDate date = LocalDate.now(); // 获取当前日期
        YearMonth yearMonth = YearMonth.from(date); // 从当前日期获取年份和月份
        int month = yearMonth.getMonthValue(); // 获取当前月份
        int year = yearMonth.getYear(); // 获取当前年份
        String dqqsyf="";
        String dqjsyf="";
        String finalyear="";
        // 确定当前月份所属的季度
        int currentQuarter;
        if (month >= 1 && month <= 3) {
            dqqsyf="01";
            dqjsyf="03";
            currentQuarter = 1;
        } else if (month >= 4 && month <= 6) {
            dqqsyf="04";
            dqjsyf="06";
            currentQuarter = 2;
        } else if (month >= 7 && month <= 9) {
            dqqsyf="07";
            dqjsyf="09";
            currentQuarter = 3;
        } else { // month >= 10 && month <= 12
            dqqsyf="10";
            dqjsyf="12";
            currentQuarter = 4;
        }
        // 计算上一个季度
        int previousQuarter = (currentQuarter + 3) % 4; // 使用模运算得到上一个季度
        if (previousQuarter == 0) {
            previousQuarter = 4; // 处理第四季度到第一季度的跨越
        }
        int previousQuarterStartMonth = (previousQuarter - 1) * 3 + 1;
        int previousQuarterEndMonth = previousQuarter * 3;
        int previousQuarterYear = year;
        map.put("dqnf",year);//当前年份
        JxhsglDto jxhsglDto=new JxhsglDto();
        jxhsglDto.setFzr(user.getYhid());
        jxhsglDto.setJxksrq_y(String.valueOf(year));
        List<JxhsglDto> jxhsglDtos=jxhsglService.getDtoList(jxhsglDto);
        map.put("jxhsglDtos",jxhsglDtos);
        if (currentQuarter == 1) {
            previousQuarterYear--; // 从第一季度回退到上一个年度
        }
        finalyear=String.valueOf(previousQuarterYear);
        khrqstart=previousQuarterYear+"-"+previousQuarterStartMonth;
        khrqend=previousQuarterYear+"-"+previousQuarterEndMonth;

        SwyszkDto swyszkDto=new SwyszkDto();
        swyszkDto.setZdzqstart(khrqstart);
        swyszkDto.setZdzqend(khrqend);
        swyszkDto.setDjxs(user.getYhid());
        List<SwyszkDto> dhslist=swyszkService.getAccountingList(swyszkDto);
        if(CollectionUtils.isEmpty(dhslist)){//若上季度已核对完成，则显示本季度
            finalyear=String.valueOf(year);
            swyszkDto.setZdzqstart(year+"-"+dqqsyf);
            swyszkDto.setZdzqend(year+"-"+dqjsyf);
            dhslist=swyszkService.getAccountingList(swyszkDto);
        }
        map.put("list",dhslist);
        map.put("jxksrq",year+"-"+dqqsyf);
        map.put("jxjsrq",year+"-"+dqjsyf);
        //计算销售总额等
        Map<String,String> param=new HashMap<>();
        JcsjDto jcsjDto_zbfl = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SALE_CLASSIFY.getCode()).stream().filter(e -> "SALES_TARGET".equals(e.getCsdm())).findFirst().get();
        JcsjDto zblx_q = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SALE_TYPE.getCode()).stream().filter(e -> "Q".equals(e.getCsdm())).findFirst().get();
        JcsjDto zblx_n = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SALE_TYPE.getCode()).stream().filter(e -> "Q".equals(e.getCsdm())).findFirst().get();
        param.put("zbfl",jcsjDto_zbfl.getCsid());
        param.put("zblx",zblx_q.getCsid());//季度指标
        param.put("djxs",user.getYhid());
        param.put("zdzqstart",swyszkDto.getZdzqstart());
        param.put("zdzqend",swyszkDto.getZdzqend());
        Map<String,String> jd_map=swyszkService.getCheckTarget(param);//季度
        param.put("zblx",zblx_n.getCsid());//年度指标
        param.put("zdzqstart",finalyear+"-01");
        param.put("zdzqend",finalyear+"-12");
        Map<String,String> nd_map=swyszkService.getCheckTarget(param);//年度
        map.put("jd_map",jd_map);
        map.put("nd_map",nd_map);
        return map;
    }

    @RequestMapping(value = "/accounting/pagedataHistoryPerformance")
    @ResponseBody
    public Map<String,Object> getHistoryPerformance(JxhsglDto jxhsglDto){
        Map<String,Object> map=new HashMap<>();
        jxhsglDto.setFzr(jxhsglDto.getYhid());
        List<JxhsglDto> jxhsglDtos=jxhsglService.getDtoList(jxhsglDto);
        map.put("jxhsglDtos",CollectionUtils.isEmpty(jxhsglDtos)?new ArrayList<>():jxhsglDtos);
        return map;
    }

    @RequestMapping(value = "/accounting/pagedataToAccounting")
    @ResponseBody
    public Map<String,String> pagedataToAccounting(HttpServletRequest request,JxhsglDto jxhsglDto){
        User user=getLoginInfo();
        Map<String,String> map=new HashMap<>();
        String list_str=request.getParameter("list");
        List<SwyszkDto> list=JSONObject.parseArray(list_str,SwyszkDto.class);
        boolean isSuccess=jxhsglService.savePerformance(list,user,jxhsglDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr())
                : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    @RequestMapping(value = "/accounting/pagedataPayment")
    @ResponseBody
    public Map<String,Object> pagedataPayment(SwyszkDto swyszkDto){
        Map<String,Object> map=new HashMap<>();
        User user=getLoginInfo();
        swyszkDto.setDjxs(user.getYhid());
        List<SwyszkDto> swyszkDtos=swyszkService.getPullList(swyszkDto);
        BigDecimal jsje=new BigDecimal("0");
        BigDecimal hkje=new BigDecimal("0");
        if(!CollectionUtils.isEmpty(swyszkDtos)){
            for(SwyszkDto swyszkDto1:swyszkDtos){
                jsje=jsje.add(new BigDecimal(StringUtil.isNotBlank(swyszkDto1.getJsje())?swyszkDto1.getJsje():"0"));
                hkje=hkje.add(new BigDecimal(StringUtil.isNotBlank(swyszkDto1.getHkje())?swyszkDto1.getHkje():"0"));
            }
        }
        map.put("jsjesum",String.valueOf(jsje));
        map.put("hkjesum",String.valueOf(hkje));
        map.put("swyszkDtos",swyszkDtos);
        return map;
    }

    @RequestMapping(value = "/accounting/pagedataPerformanceAnalysis")
    @ResponseBody
    public Map<String,Object> pagedataPerformanceAnalysis(SwyszkDto swyszkDto){
        Map<String,Object> map=new HashMap<>();
        Map<String,String> param=new HashMap<>();
        param.put("zdzqstart",swyszkDto.getZdzqstart());
        param.put("zdzqend",swyszkDto.getZdzqend());
        param.put("djxs",swyszkDto.getDjxs());
        List<Map<String,String>> xssk_statics=swyszkService.getSaleReceiptsStatistics(param);
        map.put("xssklist",xssk_statics);
        return map;
    }

    /**
     * 获取下级用户列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/accounting/pagedataJuniorList")
    @ResponseBody
    public Map<String,Object> getJuniorList(HttpServletRequest request){
        User user=getLoginInfo();
        List<String> xjyhs=yhqtxxService.getXjyhList(user.getYhid());
        UserDto userDto=new UserDto();
        userDto.setIds(xjyhs);
        List<UserDto> userList=userService.getListByIds(userDto);
        Map<String,Object> map=new HashMap<>();
        map.put("userList",userList);
        return map;
    }
}

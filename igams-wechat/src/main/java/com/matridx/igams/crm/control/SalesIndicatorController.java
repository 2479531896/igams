package com.matridx.igams.crm.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XszbDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXszbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.crm.dao.entities.XsxlglDto;
import com.matridx.igams.crm.service.svcinterface.IXsxlService;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.IYhqtxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/salesIndicator")
public class SalesIndicatorController extends BaseController {
    @Autowired
    IHbqxService hbqxService;
    @Autowired
    ISjhbxxService sjhbxxService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IXszbService xszbService;
    @Autowired
    IXxglService  xxglService;
    @Autowired
    IXsxlService xsxlService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    ISjxxService sjxxService;
    @Autowired
    IYhqtxxService yhqtxxService;
    /*
        获取销售指标主界面数据
     */
    @RequestMapping("/salesIndicator/minidataGetSalesIndicatorMainInfo")
    @ResponseBody
    public Map<String,Object> minidataGetSalesIndicatorMainInfo(XszbDto xszbDto){
        //获取CRM销售分类
        JcsjDto jcsjDto_zbfl = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SALE_CLASSIFY.getCode()).stream().filter(e -> "SALES_TARGET".equals(e.getCsdm())).findFirst().get();
        xszbDto.setZbfl(jcsjDto_zbfl.getCsid());
        User user = getLoginInfo();
        Map<String,Object> map=new HashMap<>();
        String nowMonth = DateUtils.getCustomFomratCurrentDate("yyyy-MM");
        xszbDto.setYhid(user.getYhid());
        xszbDto.setSfqr("0");
        List<XszbDto> onConfirmIndicator=xszbService.getSalesIndicatorList(xszbDto);
        if(!CollectionUtils.isEmpty(onConfirmIndicator)){
            for(int i=onConfirmIndicator.size()-1;i>=0;i--){//过滤不是发给自己的数据
                if(!user.getYhid().equals(onConfirmIndicator.get(i).getYhid()))
                    onConfirmIndicator.remove(i);
            }
            if(!CollectionUtils.isEmpty(onConfirmIndicator))
                map.put("qrbj","1");//是否有未确认的指标
        }
        xszbDto.setSfqr(null);
        //获取月度销售额和月度销售目标
        xszbDto.setKszq(nowMonth);
        xszbDto.setJszq(nowMonth);
        XszbDto salesIndicatorM =xszbService.getSalesIndicator(xszbDto);
        LocalDate date = LocalDate.now(); // 获取当前日期
        YearMonth yearMonth = YearMonth.from(date); // 从当前日期获取年份和月份
        int month = yearMonth.getMonthValue(); // 获取当前月份
        int year = yearMonth.getYear(); // 获取当前年份
        // 计算当前季度的开始月份
        int startMonth = 0;
        if(month % 3>0){
            startMonth=(month / 3) * 3 + 1;
        }else {
            startMonth=month-2;
        }
        // 计算当前季度的结束月份
        int endMonth = startMonth + 2;
        // 格式化输出
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        // 使用formatter格式化
        String startMonthStr = YearMonth.of(year, startMonth).format(formatter);
        String endMonthStr = YearMonth.of(year, endMonth).format(formatter);
        //获取季度销售额和季度销售目标
        xszbDto.setKszq(startMonthStr);
        xszbDto.setJszq(endMonthStr);
        XszbDto salesIndicatorQ =xszbService.getSalesIndicator(xszbDto);
        XsxlglDto xsxlglDto = new XsxlglDto();
        xsxlglDto.setYhid(user.getYhid());
        xsxlglDto.setRqY(DateUtils.getCustomFomratCurrentDate("yyyy"));
        //获取未回款金额
        Map<String,Object> unpaidAmount = xsxlService.getUnpaidAmount(xsxlglDto);
        map.put("salesIndicatorM",salesIndicatorM);
        map.put("salesIndicatorQ",salesIndicatorQ);
        map.put("unpaidAmount",unpaidAmount);
        return map;
    }
    /*
        确认销售指标接
     */
    @RequestMapping("/salesIndicator/minidataConfirmSalesIndicator")
    @ResponseBody
    public Map<String,Object> minidataConfirmSalesIndicator(XszbDto xszbDto){
        User user = getLoginInfo();
        Map<String,Object> map=new HashMap<>();
        //获取CRM销售分类
        JcsjDto jcsjDto_zbfl = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SALE_CLASSIFY.getCode()).stream().filter(e -> "SALES_TARGET".equals(e.getCsdm())).findFirst().get();
        xszbDto.setZbfl(jcsjDto_zbfl.getCsid());
        xszbDto.setYhid(user.getYhid());
        //确认销售指标
        boolean isSuccess=xszbService.confirmSalesIndicator(xszbDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /*
       获取自己和下属的销售指标
     */
    @RequestMapping("/salesIndicator/minidataGetSalesIndicatorInfo")
    @ResponseBody
    public Map<String,Object> minidataGetSalesIndicatorInfo(XszbDto xszbDto){
        User user = getLoginInfo();
        Map<String,Object> map=new HashMap<>();
        xszbDto.setYhid(user.getYhid());
        //获取CRM销售分类
        JcsjDto jcsjDto_zbfl = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SALE_CLASSIFY.getCode()).stream().filter(e -> "SALES_TARGET".equals(e.getCsdm())).findFirst().get();
        xszbDto.setZbfl(jcsjDto_zbfl.getCsid());
        String nf;
        //我的指标确认显示数据
        List<XszbDto> mySalesIndicator =xszbService.getMySalesIndicator(xszbDto);
        if (StringUtil.isNotBlank(xszbDto.getNf())){
            nf = xszbDto.getNf();
        }else if (!CollectionUtils.isEmpty(mySalesIndicator)){
            nf = mySalesIndicator.get(0).getNf();
        }else{
            nf = DateUtils.getCustomFomratCurrentDate("yyyy");
        }
        xszbDto.setNf(nf);
        //获取下属的指标
        List<XszbDto> subordinateIndicator =xszbService.getSubordinateIndicator(xszbDto);
        map.put("subordinateIndicator",subordinateIndicator);
        map.put("mySalesIndicator",mySalesIndicator);
        map.put("nf",nf);
        return map;
    }
    /*
      获取指定人员的销售指标
    */
    @RequestMapping("/salesIndicator/minidataGetSalesIndicator")
    @ResponseBody
    public Map<String,Object> minidataGetSalesIndicator(XszbDto xszbDto){
        Map<String,Object> map=new HashMap<>();
        //获取CRM销售分类
        JcsjDto jcsjDto_zbfl = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SALE_CLASSIFY.getCode()).stream().filter(e -> "SALES_TARGET".equals(e.getCsdm())).findFirst().get();
        xszbDto.setZbfl(jcsjDto_zbfl.getCsid());
        //我的指标确认显示数据
        List<XszbDto> salesIndicator =xszbService.getMySalesIndicator(xszbDto);
        map.put("salesIndicator",salesIndicator);
        return map;
    }
    /*
      获取自己和下属的销售指标清单
    */
    @RequestMapping("/salesIndicator/minidataGetSalesIndicatorList")
    @ResponseBody
    public Map<String,Object> minidataGetSalesIndicatorList(XszbDto xszbDto){
        Map<String,Object> map=new HashMap<>();
        User user = getLoginInfo();
        xszbDto.setYhid(user.getYhid());
        List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
        if(!CollectionUtils.isEmpty(dwAndbjlist)) {//这里单位限制主要是为了让没有限制的人员看到所有的，有限制的人员只看到自己分配的
            if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
                xszbDto.setDwxzbj("1");
                List<String> xjyhs=yhqtxxService.getXjyhList(user.getYhid());
                xszbDto.setYhids(xjyhs);
            }
        }
        //获取CRM销售分类
        JcsjDto jcsjDto_zbfl = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SALE_CLASSIFY.getCode()).stream().filter(e -> "SALES_TARGET".equals(e.getCsdm())).findFirst().get();
        xszbDto.setZbfl(jcsjDto_zbfl.getCsid());
        //获取自己和下属的销售指标清单
        List<XszbDto> salesIndicatorList =xszbService.getSalesIndicatorList(xszbDto);
        map.put("salesIndicatorList",salesIndicatorList);
        return map;
    }
    /*
     * 保存销售指标
     */
    @RequestMapping("/salesIndicator/minidataSaveSalesIndicator")
    @ResponseBody
    public Map<String,Object> minidataSaveSalesIndicator(XszbDto xszbDto){
        User user=getLoginInfo();
        xszbDto.setLrry(user.getYhid());
        Map<String,Object> map=new HashMap<>();
        boolean isSuccess;
        //保存销售指标
        try {
            isSuccess=xszbService.editSaveSalesIndicator(xszbDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
}

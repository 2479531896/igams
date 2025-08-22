package com.matridx.igams.production.controller;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.production.dao.entities.YsglDto;
import com.matridx.igams.production.service.svcinterface.IYsglService;
import com.matridx.igams.production.service.svcinterface.IYsmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/financialStatistics")
public class FinancialStatisticsController extends BaseController {
    @Autowired
    private IYsglService ysglService;
    @Autowired
    private IYsmxService ysmxService;
    @Autowired
    RedisUtil redisUtil;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }
    private final Logger log = LoggerFactory.getLogger(FinancialStatisticsController.class);


    /**
     * 财务预算统计
     */
    @RequestMapping("/financialStatistics/pagedataFinancialStatistics")
    @ResponseBody
    public Map<String, Object> pagedataFinancialStatistics(YsglDto ysglDto, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        long threadId = Thread.currentThread().getId();
        ysglDto.setThreadId(threadId);
        String nowDate = DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd");
        String year = DateUtils.getCustomFomratCurrentDate("yyyy")+"-01-01~"+ nowDate;
        String month = DateUtils.getCustomFomratCurrentDate("yyyy-MM")+"-01~"+ nowDate;
        if (StringUtil.isBlank(ysglDto.getTjlx())||"year".equals(ysglDto.getTjlx())){
            ysglDto.setTjlx("year");
            ysglDto.setRqStart(year.split("~")[0]);
            ysglDto.setRqEnd(year.split("~")[1]);
        }else {
            ysglDto.setTjlx("month");
            ysglDto.setRqStart(month.split("~")[0]);
            ysglDto.setRqEnd(month.split("~")[1]);
        }
        ysglDto.setNd(DateUtils.getCustomFomratCurrentDate("yyyy"));
        result.put("year", year);
        result.put("month", month);
        User user = getLoginInfo(request);
        try {
            ysglService.InitData(true,true,user,ysglDto);
            //获取预算费用和实际费用
            Map<String, Object> budgetAndActualExpenses = ysglService.getBudgetAndActualExpenses(ysglDto);
            result.put("budgetAndActualExpenses", budgetAndActualExpenses);
            //获取部门实际费用占比
            List<Map<String, Object>> departmentActualExpenses = ysglService.getDepartmentActualExpenses(ysglDto);
            result.put("departmentActualExpenses", departmentActualExpenses);
            //科目实际费用占比
            List<Map<String, Object>> subjectActualExpenses = ysglService.getSubjectActualExpenses(ysglDto);
            result.put("subjectActualExpenses", subjectActualExpenses);
            //部门预算进度
            List<Map<String, Object>> departmentBudgetProgress = ysglService.getDepartmentBudgetProgress(ysglDto);
            result.put("departmentBudgetProgress", departmentBudgetProgress);
            //各科目预算进度
            List<Map<String, Object>> subjectBudgetProgress = ysglService.getSubjectBudgetProgress(ysglDto);
            result.put("subjectBudgetProgress", subjectBudgetProgress);
            //获取科目预算与实际支出明细
            List<Map<String, Object>> subjectExpenseDetail = ysglService.getSubjectExpenseDetail(ysglDto);
            result.put("subjectExpenseDetail", subjectExpenseDetail);
        } catch (BusinessException e) {
            log.error("errorMsg: {}", e.getMessage());
            result.put("errorMsg", e.getMessage());
        }finally {
            ysglService.delInitData(threadId);
        }
        return result;
    }
}

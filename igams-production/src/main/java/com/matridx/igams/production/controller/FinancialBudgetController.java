package com.matridx.igams.production.controller;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.YsglDto;
import com.matridx.igams.production.dao.entities.YsmxDto;
import com.matridx.igams.production.service.svcinterface.IYsglService;
import com.matridx.igams.production.service.svcinterface.IYsmxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/financial")
public class FinancialBudgetController extends BaseController {

    @Autowired
    private IYsglService ysglService;
    @Autowired
    private IYsmxService ysmxService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IXxglService xxglService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }

    /**
     * 财务预算列表
     */
    @RequestMapping("/budget/pageGetListFinancialBudget")
    @ResponseBody
    public Map<String, Object> pageGetListFinancialBudget(YsglDto ysglDto) {
        Map<String, Object> map = new HashMap<>();
        List<YsglDto> list = ysglService.getPagedDtoList(ysglDto);
        map.put("bms", ysglService.getDepartments());
        map.put("nds", ysglService.getYears());
        map.put("total", ysglDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }

    /**
     * 财务预算列表 查看
     */
    @RequestMapping("/budget/viewFinancialBudget")
    @ResponseBody
    public Map<String, Object> viewFinancialBudget(YsglDto ysglDto) {
        Map<String, Object> map = new HashMap<>();
        YsglDto dto = ysglService.getDto(ysglDto);
        YsmxDto ysmxDto=new YsmxDto();
        ysmxDto.setYsglid(ysglDto.getYsglid());
        List<YsmxDto> dtoList = ysmxService.getDtoList(ysmxDto);
        map.put("ysglDto", dto);
        map.put("ysmxDtos", dtoList);
        return map;
    }

    /**
     * 财务预算列表 新增
     */
    @RequestMapping("/budget/addFinancialBudget")
    @ResponseBody
    public Map<String, Object> addFinancialBudget(YsglDto ysglDto) {
        Map<String, Object> map = new HashMap<>();
        List<JcsjDto> xmdls = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.ACCOUNT_SUBJECT_TWO.getCode());
        List<JcsjDto> xmfls = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.ACCOUNT_SUB_SUBJECT_TWO.getCode());
        List<YsmxDto> ysmxDtos=new ArrayList<>();
        if(!xmfls.isEmpty()){
            for(JcsjDto xmfl:xmfls){
                YsmxDto ysmxDto=new YsmxDto();
                ysmxDto.setKmfl(xmfl.getCsid());
                ysmxDto.setKmflmc(xmfl.getCsmc());
                if(!xmfls.isEmpty()){
                    for(JcsjDto xmdl:xmdls){
                        if(xmdl.getCsid().equals(xmfl.getFcsid())){
                            ysmxDto.setKmdl(xmdl.getCsid());
                            ysmxDto.setKmdlmc(xmdl.getCsmc());
                            break;
                        }
                    }
                }
                ysmxDtos.add(ysmxDto);
            }
        }
        map.put("ysglDto", ysglDto);
        map.put("ysmxDtos", ysmxDtos);
        return map;
    }

    /**
     * 财务预算列表 新增保存
     */
    @RequestMapping(value = "/budget/addSaveFinancialBudget")
    @ResponseBody
    public Map<String, Object> addSaveFinancialBudget(YsglDto ysglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        YsglDto dto = ysglService.saveVerification(ysglDto);
        if(dto!=null){
            map.put("status", "fail");
            map.put("message", "当前部门当前年度的财务预算已存在!");
            return map;
        }
        User user = getLoginInfo(request);
        ysglDto.setLrry(user.getYhid());
        boolean isSuccess = ysglService.addSaveFinancialBudget(ysglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 财务预算列表 修改
     */
    @RequestMapping("/budget/modFinancialBudget")
    @ResponseBody
    public Map<String, Object> modFinancialBudget(YsglDto ysglDto) {
        Map<String, Object> map = new HashMap<>();
        YsglDto dto = ysglService.getDto(ysglDto);
        map.put("ysglDto", dto);
        YsmxDto ysmxDto=new YsmxDto();
        ysmxDto.setYsglid(ysglDto.getYsglid());
        List<YsmxDto> ysmxDtos = ysmxService.getDtoList(ysmxDto);
        map.put("ysmxDtos", ysmxDtos);
        return map;
    }

    /**
     * 财务预算列表 修改保存
     */
    @RequestMapping(value = "/budget/modSaveFinancialBudget")
    @ResponseBody
    public Map<String, Object> modSaveFinancialBudget(YsglDto ysglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        YsglDto dto = ysglService.saveVerification(ysglDto);
        if(dto!=null){
            map.put("status", "fail");
            map.put("message", "当前部门当前年度的财务预算已存在!");
            return map;
        }
        User user = getLoginInfo(request);
        ysglDto.setXgry(user.getYhid());
        boolean isSuccess = ysglService.modSaveFinancialBudget(ysglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 财务预算列表 删除
     */
    @RequestMapping(value = "/budget/delFinancialBudget")
    @ResponseBody
    public Map<String, Object> delFinancialBudget(YsglDto ysglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        ysglDto.setScry(user.getYhid());
        boolean isSuccess = ysglService.delFinancialBudget(ysglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00003").getXxnr()):xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 财务预算列表 导入
     */
    @RequestMapping("/budget/importFinancialBudget")
    @ResponseBody
    public Map<String, Object> importFinancialBudget() {
        Map<String, Object> map = new HashMap<>();
        map.put("ywlx", BusTypeEnum.IMP_FINANCIAL_BUDGET.getCode());
        return map;
    }


}

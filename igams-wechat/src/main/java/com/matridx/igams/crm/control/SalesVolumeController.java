package com.matridx.igams.crm.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XszbDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXszbService;
import com.matridx.igams.crm.dao.entities.XsxlglDto;
import com.matridx.igams.crm.service.svcinterface.IXsxlService;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/salesVolum")
public class SalesVolumeController extends BaseController {

    @Autowired
    IHbqxService hbqxService;
    @Autowired
    ISjhbxxService sjhbxxService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IXsxlService xsxlService;
    @Autowired
    IXszbService xszbService;

    @RequestMapping("/salesVolum/pagedataSalesVolum")
    @ResponseBody
    public Map<String,Object> SalesVolumeController(XsxlglDto xsxlglDto){
        XszbDto xszbDto=new XszbDto();
        User user = getLoginInfo();
        Map<String,Object> map=new HashMap<>();
        JcsjDto jcsjDto=new JcsjDto();
        jcsjDto.setJclb(BasicDataTypeEnum.XXDY_TYPE.getCode());
        jcsjDto.setCsdm("XMFL");
        jcsjDto=jcsjService.getDto(jcsjDto);
        xsxlglDto.setDylx(jcsjDto.getCsid());
        if("Month".equals(xsxlglDto.getRqlx())){
            xszbDto.setZblxcsmc("M");
        }else if("Year".equals(xsxlglDto.getRqlx())){
            xszbDto.setZblxcsmc("Y");
        }else if("Quarter".equals(xsxlglDto.getRqlx())){
            xszbDto.setZblxcsmc("Q");
        }

        if("1".equals(user.getDqjsdwxdbj())){
            //判断伙伴权限
            List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
            if (!CollectionUtils.isEmpty(hbqxList)) {
                List<String> hbmcList = sjhbxxService.getHbmcByHbid(hbqxList);
                if (!CollectionUtils.isEmpty(hbmcList)) {
                    xsxlglDto.setSjhbs(hbmcList);
                }
            }
        }
        List<Map<String,Object>> xsslmap=xsxlService.getSaleVolume(xsxlglDto);
        //分组合计数据
        if(!CollectionUtils.isEmpty(xsslmap)) {
            map.put("zxl",dealSalesList(xsslmap));
        }
        //获取销售目标
        xszbDto.setYhid(user.getYhid());
        JcsjDto t_jcsjDto=new JcsjDto();
        t_jcsjDto.setJclb(BasicDataTypeEnum.SALE_CLASSIFY.getCode());
        t_jcsjDto.setCsdm("SALES_TARGET");
        t_jcsjDto=jcsjService.getDto(t_jcsjDto);
        xszbDto.setZbfl(t_jcsjDto.getCsid());
        xszbDto.setKszq(xsxlglDto.getRqstart());
        xszbDto.setJszq(xsxlglDto.getRqend());
        xszbDto.setSfqr("1");
        xszbDto=xszbService.getDto(xszbDto);
        map.put("xszbDto",xszbDto);
        map.put("xsxllist",xsslmap);
        return map;
    }

    public List<Map<String,Object>> dealSalesList(List<Map<String,Object>> xsslmap){
        // 使用 Stream API 对数据进行分组和求和
        List<Map<String, Object>> cssgroup = xsslmap.stream()
                .collect(Collectors.groupingBy(
                        map -> (String) map.get("yxx"), // 按照 "yxx" 字段分组
                        Collectors.reducing(BigDecimal.ZERO,
                                map -> new BigDecimal(String.valueOf(map.get("css"))),
                                BigDecimal::add) // 对每组的 "css" 求和
                ))
                .entrySet().stream()
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("yxx", entry.getKey());
                    result.put("css", entry.getValue());
                    return result;
                })
                .collect(Collectors.toList());
        List<Map<String, Object>> zyfjegroup = xsslmap.stream()
                .collect(Collectors.groupingBy(
                        map -> (String) map.get("yxx"), // 按照 "yxx" 字段分组
                        Collectors.reducing(BigDecimal.ZERO,
                                map -> new BigDecimal(String.valueOf(map.get("zyfje"))),
                                BigDecimal::add) // 对每组的 "css" 求和
                ))
                .entrySet().stream()
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("yxx", entry.getKey());
                    result.put("zyfje", entry.getValue());
                    return result;
                })
                .collect(Collectors.toList());
        // 合并两个列表
        List<Map<String, Object>> mergedList = new ArrayList<>();
        for (Map<String, Object> map1 : cssgroup) {
            String yxx = (String) map1.get("yxx");
            Map<String, Object> mergedMap = new HashMap<>(map1);

            // 从第二个列表中查找对应的"yxx"字段并合并数据
            Optional<Map<String, Object>> optMap2 = zyfjegroup.stream()
                    .filter(map2 -> yxx.equals(map2.get("yxx")))
                    .findFirst();

            if (optMap2.isPresent()) {
                mergedMap.putAll(optMap2.get());
            }

            // 如果第二个列表中没有找到对应的"yxx"，则只保留第一个列表中的数据
            mergedList.add(mergedMap);
        }
        return mergedList;
    }

    @RequestMapping("/salesVolum/pagedataUnpaidAccounts")
    @ResponseBody
    public Map<String,Object> UnpaidAccounts(XsxlglDto xsxlglDto){
        Map<String,Object> map=new HashMap<>();
        List<Map<String,Object>> unpaidAccounts = xsxlService.getUnpaidAccounts(xsxlglDto);
        Map<String,Object> unpaidAmount = xsxlService.getUnpaidAmount(xsxlglDto);
        map.put("unpaidAccounts",unpaidAccounts);
        map.put("unpaidAmount",unpaidAmount);
        return map;
    }

    @RequestMapping("/salesVolum/pagedataUnpaidAccountDetail")
    @ResponseBody
    public Map<String,Object> UnpaidAccountDetail(XsxlglDto xsxlglDto){
        Map<String,Object> map=new HashMap<>();
        List<Map<String,Object>> unpaidAccountDetails = xsxlService.getUnpaidAccountDetail(xsxlglDto);
        Map<String,Object> unpaidAmount = xsxlService.getUnpaidAmount(xsxlglDto);
        map.put("unpaidAccountDetails",unpaidAccountDetails);
        map.put("unpaidAmount",unpaidAmount);
        return map;
    }
}

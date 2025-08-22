package com.matridx.igams.experiment.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.PersonalSettingEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.experiment.dao.entities.KzglDto;
import com.matridx.igams.experiment.dao.entities.KzmxDto;
import com.matridx.igams.experiment.service.svcinterface.IKzglService;
import com.matridx.igams.experiment.service.svcinterface.IKzmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping("/amplification")
public class AmplificationController extends BaseController {
    @Autowired
    private IKzglService kzglService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IGrszService grszService;
    @Autowired
    private IKzmxService kzmxService;
    @Autowired
    IXxglService xxglService;

    /**
     * 跳转扩增列表界面
     */
    @RequestMapping(value = "/amplification/pageListAmplification")
    @ResponseBody
    public ModelAndView pageListAmplification() {
        return new ModelAndView("experiment/amplification/amplification_list");
    }

    /**
     * 获取文库列表
     */
    @RequestMapping("/amplification/pageGetListAmplification")
    @ResponseBody
    public Map<String, Object> pageGetListAmplification(KzglDto kzglDto) {
        User user = getLoginInfo();
        List<KzglDto> kzglDtos = new ArrayList<>();
        List<Map<String, String>> jcdwList = kzglService.getJsjcdwByjsid(user.getDqjs());
        if(jcdwList!=null && jcdwList.size() > 0) {
            if ("1".equals(jcdwList.get(0).get("dwxdbj"))) {
                List<String> strList = new ArrayList<>();
                for (Map<String, String> stringStringMap : jcdwList) {
                    if (stringStringMap.get("jcdw") != null) {
                        strList.add(stringStringMap.get("jcdw"));
                    }
                }
                if (strList.size() > 0) {
                    kzglDto.setJcdwxz(strList);
                    kzglDtos = kzglService.getPagedDtoList(kzglDto);
                }
            } else {
                kzglDtos = kzglService.getPagedDtoList(kzglDto);
            }
        }else {
            kzglDtos = kzglService.getPagedDtoList(kzglDto);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", kzglDto.getTotalNumber());
        map.put("rows", kzglDtos);
        return map;
    }

    /**
     * 跳转新增界面
     */
    @RequestMapping("/amplification/addAmplification")
    @ResponseBody
    public ModelAndView addAmplification(KzmxDto kzmxDto) {
        ModelAndView mav = new ModelAndView("experiment/amplification/amplification_edit");
        User user = getLoginInfo();
        GrszDto grszDto = new GrszDto();
        grszDto.setYhid(user.getYhid());
        grszDto.setSzlbs(new String[]{PersonalSettingEnum.TEST_PLACE.getCode(),PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode(),PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode()});
        Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
        //查个人设置检查单位
        grszDto.setSzlb(PersonalSettingEnum.TEST_PLACE.getCode());
        GrszDto grszxx = grszDtoMap.get(PersonalSettingEnum.TEST_PLACE.getCode());
        if (grszxx != null) {
            kzmxDto.setJcdw(grszxx.getSzz());
        }
        //宏基因组DNA建库试剂盒个人设置
        grszDto.setSzlb(PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode());
        GrszDto grszxx_o = grszDtoMap.get(PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode());
        if(grszxx_o!=null) {
            kzmxDto.setSjph1(grszxx_o.getSzz());
        }
        //逆转录试剂盒个人设置
        grszDto.setSzlb(PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode());
        GrszDto grszxx_t = grszDtoMap.get(PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode());
        if(grszxx_t!=null) {
            kzmxDto.setSjph2(grszxx_t.getSzz());
        }
        KzglDto kzglDto = new KzglDto();
        String date = DateUtils.format(new Date(), "yyyy-MM-dd");
        String t_date = date.replaceAll("-", "");
        kzglDto.setLrsj(date);
        kzglDto = kzglService.getCountByDay(kzglDto);
        String kzmc;
        if (Integer.parseInt(kzglDto.getCount()) < 9 && Integer.parseInt(kzglDto.getCount()) >= 0) {
            kzmc = t_date + "0" + (Integer.parseInt(kzglDto.getCount()) + 1);
        } else {
            kzmc = t_date + (Integer.parseInt(kzglDto.getCount()) + 1);
        }
        kzmxDto.setKzmc(kzmc);
        mav.addObject("jcdwList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));// 检测单位
        kzmxDto.setFormAction("addSaveAmplification");
        mav.addObject("kzmxDto", kzmxDto);
        return mav;
    }

    /**
     * 验证是否存在相同syglid
     */
    @RequestMapping("/amplification/pagedataVerifySame")
    @ResponseBody
    public Map<String, Object> pagedataVerifySame(KzmxDto kzmxDto) {
        Map<String, Object> map = new HashMap<>();
        if(StringUtil.isNotBlank(kzmxDto.getNbbh())&&(kzmxDto.getNbbh().startsWith("NC")||kzmxDto.getNbbh().startsWith("PC"))){
            map.put("status","true");
            return map;
        }
        KzmxDto kzmxDto_t = kzmxService.verifySame(kzmxDto);
        if (kzmxDto_t != null) {
            map.put("status","false");
        } else {
            map.put("status","true");
        }
        return map;
    }


    /**
     * 新增提交保存
     */
    @RequestMapping("/amplification/addSaveAmplification")
    @ResponseBody
    public Map<String, Object> addSaveAmplification(KzmxDto kzmxDto){
        boolean isSuccess;
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        kzmxDto.setLrry(user.getYhid());
        GrszDto grszDto = new GrszDto();
        grszDto.setYhid(user.getYhid());
        grszDto.setSzlbs(new String[]{PersonalSettingEnum.TEST_PLACE.getCode(),PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode(),PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode()});
        Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);

        //检测单位个人设置
        grszDto.setSzlb(PersonalSettingEnum.TEST_PLACE.getCode());
        GrszDto grszxx = grszDtoMap.get(PersonalSettingEnum.TEST_PLACE.getCode());
        if (grszxx == null) {
            grszDto.setSzz(kzmxDto.getJcdw());
            grszDto.setLrry(user.getYhid());
            grszService.insertDto(grszDto);
        } else {
            if (grszxx.getSzz()==null || !grszxx.getSzz().equals(kzmxDto.getJcdw())) {
                grszDto.setSzz(kzmxDto.getJcdw());
                grszService.updateByYhidAndSzlb(grszDto);
            }
        }
        //宏基因组DNA建库试剂盒个人设置
        grszDto.setSzlb(PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode());
        GrszDto grszxx_o = grszDtoMap.get(PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode());
        if (grszxx_o == null) {
            grszDto.setSzz(kzmxDto.getSjph1());
            grszDto.setLrry(user.getYhid());
            grszService.insertDto(grszDto);
        } else {
            if (grszxx_o.getSzz()==null ||!grszxx_o.getSzz().equals(kzmxDto.getSjph1())) {
                grszDto.setSzz(kzmxDto.getSjph1());
                grszService.updateByYhidAndSzlb(grszDto);
            }
        }
        //逆转录试剂盒个人设置
        grszDto.setSzlb(PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode());
        GrszDto grszxx_t = grszDtoMap.get(PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode());
        if (grszxx_t == null) {
            grszDto.setSzz(kzmxDto.getSjph2());
            grszDto.setLrry(user.getYhid());
            grszService.insertDto(grszDto);
        } else {
            if (grszxx_t.getSzz()==null ||!grszxx_t.getSzz().equals(kzmxDto.getSjph2())) {
                grszDto.setSzz(kzmxDto.getSjph2());
                grszService.updateByYhidAndSzlb(grszDto);
            }
        }
        List<String> notexitnbbhs;
        try {
            notexitnbbhs = kzmxService.checkNbbms(kzmxDto);
            KzglDto kzglDto = new KzglDto();
            kzglDto.setKzmc(kzmxDto.getKzmc());
            kzglDto = kzglService.getDtoByKzmc(kzglDto);
            if (kzglDto != null) {
                map.put("status", "fail");
                map.put("message", "扩增名称不允许重复!");
                return map;
            }
            if (notexitnbbhs == null || notexitnbbhs.size() < 1) {
                isSuccess = kzmxService.addSaveAmplification(kzmxDto);
                map.put("status", isSuccess ? "success" : "fail");
                map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr())
                        : xxglService.getModelById("ICOM00002").getXxnr());
                return map;
            } else {
                map.put("status", "caution");
                map.put("message", "保存失败!内部编号:" + String.join(",", notexitnbbhs) + "不存在!是否继续保存!");
                map.put("notexitnbbhs", notexitnbbhs);
                return map;
            }
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message",e.getMsg());
            return map;
        }
    }


    /**
     * 跳转修改界面
     */
    @RequestMapping("/amplification/modAmplification")
    @ResponseBody
    public ModelAndView modAmplification(KzglDto kzglDto) {
        ModelAndView mav = new ModelAndView("experiment/amplification/amplification_edit");
        kzglDto=kzglService.getDto(kzglDto);
        mav.addObject("jcdwList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));// 检测单位
        kzglDto.setFormAction("modSaveAmplification");
        mav.addObject("kzmxDto", kzglDto);
        return mav;
    }

    /**
     * 获取修改界面内容返回给JS处理
     */
    @RequestMapping("/amplification/pagedataGetInfo")
    @ResponseBody
    public List<KzmxDto> pagedataGetInfo(KzmxDto kzmxDto) {
        return kzmxService.getDtoList(kzmxDto);
    }

    /**
     * 修改提交保存
     */
    @RequestMapping("/amplification/modSaveAmplification")
    @ResponseBody
    public Map<String, Object> modSaveLibrary(KzmxDto kzmxDto){
        boolean isSuccess;
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        kzmxDto.setXgry(user.getYhid());
        GrszDto grszDto = new GrszDto();
        grszDto.setYhid(user.getYhid());
        grszDto.setSzlbs(new String[]{PersonalSettingEnum.TEST_PLACE.getCode(),PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode(),PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode()});
        Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);

        grszDto.setSzlb(PersonalSettingEnum.TEST_PLACE.getCode());
        GrszDto grszxx = grszDtoMap.get(PersonalSettingEnum.TEST_PLACE.getCode());
        if (grszxx == null) {
            grszDto.setSzz(kzmxDto.getJcdw());
            grszDto.setLrry(user.getYhid());
            grszService.insertDto(grszDto);
        } else {
            if (grszxx.getSzz()==null  || !grszxx.getSzz().equals(kzmxDto.getJcdw())) {
                grszDto.setSzz(kzmxDto.getJcdw());
                grszService.updateByYhidAndSzlb(grszDto);
            }
        }
        //宏基因组DNA建库试剂盒个人设置
        grszDto.setSzlb(PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode());
        GrszDto grszxx_o = grszDtoMap.get(PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode());
        if (grszxx_o == null) {
            grszDto.setSzz(kzmxDto.getSjph1());
            grszDto.setLrry(user.getYhid());
            grszService.insertDto(grszDto);
        } else {
            if (grszxx_o.getSzz()==null || !grszxx_o.getSzz().equals(kzmxDto.getSjph1())) {
                grszDto.setSzz(kzmxDto.getSjph1());
                grszService.updateByYhidAndSzlb(grszDto);
            }
        }
        //逆转录试剂盒个人设置
        grszDto.setSzlb(PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode());
        GrszDto grszxx_t = grszDtoMap.get(PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode());
        if (grszxx_t == null) {
            grszDto.setSzz(kzmxDto.getSjph2());
            grszDto.setLrry(user.getYhid());
            grszService.insertDto(grszDto);
        } else {
            if (grszxx_t.getSzz()==null || !grszxx_t.getSzz().equals(kzmxDto.getSjph2())) {
                grszDto.setSzz(kzmxDto.getSjph2());
                grszService.updateByYhidAndSzlb(grszDto);
            }
        }
        try {
            KzglDto kzglDto = new KzglDto();
            kzglDto.setKzmc(kzmxDto.getKzmc());
            kzglDto.setKzid(kzmxDto.getKzid());
            kzglDto = kzglService.getDtoByKzmc(kzglDto);
            if (kzglDto != null) {
                map.put("status", "fail");
                map.put("message", "扩增名称不允许重复!");
                return map;
            }
            isSuccess = kzmxService.modSaveAmplification(kzmxDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr())
                    : xxglService.getModelById("ICOM00002").getXxnr());
            return map;
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", e.getMsg());
            return map;
        }
    }

    /**
     * 删除
     */
    @RequestMapping("/amplification/delAmplification")
    @ResponseBody
    public Map<String, Object> delAmplification(KzglDto kzglDto) {
        User user = getLoginInfo();
        kzglDto.setScry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = kzglService.deleteDto(kzglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00003").getXxnr()):xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }


    /**
     * 跳转查看界面
     */
    @RequestMapping("/amplification/viewAmplification")
    @ResponseBody
    public ModelAndView viewAmplification(KzglDto kzglDto) {
        ModelAndView mav = new ModelAndView("experiment/amplification/amplification_view");
        kzglDto=kzglService.getDto(kzglDto);
        mav.addObject("kzglDto", kzglDto);
        return mav;
    }
}

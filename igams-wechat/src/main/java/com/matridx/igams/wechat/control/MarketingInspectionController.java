package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GrlbzdszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.LbzdszDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGrlbzdszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.ILbzdszService;
import com.matridx.igams.common.util.WechatCommonUtils;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.FjsqyyDto;
import com.matridx.igams.wechat.dao.entities.SjbgsmDto;
import com.matridx.igams.wechat.dao.entities.SjdwxxDto;
import com.matridx.igams.wechat.dao.entities.SjjcxmDto;
import com.matridx.igams.wechat.dao.entities.SjkzxxDto;
import com.matridx.igams.wechat.dao.entities.SjnyxDto;
import com.matridx.igams.wechat.dao.entities.SjsyglDto;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxjgDto;
import com.matridx.igams.wechat.dao.entities.SjybztDto;
import com.matridx.igams.wechat.dao.entities.SjzmjgDto;
import com.matridx.igams.wechat.service.svcinterface.IFjsqService;
import com.matridx.igams.wechat.service.svcinterface.IFjsqyyService;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.ISjbgsmService;
import com.matridx.igams.wechat.service.svcinterface.ISjgzbyService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.igams.wechat.service.svcinterface.ISjkzxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjlczzService;
import com.matridx.igams.wechat.service.svcinterface.ISjnyxService;
import com.matridx.igams.wechat.service.svcinterface.ISjqqjcService;
import com.matridx.igams.wechat.service.svcinterface.ISjsyglService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxjgService;
import com.matridx.igams.wechat.service.svcinterface.ISjybztService;
import com.matridx.igams.wechat.service.svcinterface.ISjzmjgService;
import com.matridx.igams.wechat.service.svcinterface.IYxsjxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 营销端检测接口
 */
@Controller
@RequestMapping("/marketingInspection")
public class MarketingInspectionController extends BaseController {

    private Logger log = LoggerFactory.getLogger(MarketingInspectionController.class);

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ILbzdszService lbzdszService;
    @Autowired
    private IJcsjService jcsjService;
    @Autowired
    private ISjjcxmService sjjcxmService;
    @Autowired
    private ISjxxService sjxxService;
    @Autowired
    private IYxsjxxService yxsjxxService;
    @Autowired
    private IGrlbzdszService grlbzdszService;
    @Autowired
    private IHbqxService hbqxService;
    @Autowired
    private ISjhbxxService sjhbxxService;
    @Autowired
    private ISjnyxService sjnyxService;
    @Autowired
    private IFjsqService fjsqService;
    @Autowired
    private ISjxxjgService sjxxjgService;
    @Autowired
    private ISjzmjgService sjzmjgService;
    @Autowired
    private ISjbgsmService sjbgsmService;
    @Autowired
    private ISjybztService sjybztService;
    @Autowired
    private IFjcfbService fjcfbService;
    @Autowired
    private ISjqqjcService sjqqjcService;
    @Autowired
    private ISjgzbyService sjgzbyService;
    @Autowired
    private ISjlczzService sjlczzService;
    @Autowired
    private IFjsqyyService fjsqyyService;
    @Autowired
    WechatCommonUtils wechatCommonUtils;
    @Autowired
    ISjsyglService sjsyglService;
    @Autowired
    private ISjkzxxService sjkzxxService;

    @RequestMapping(value = "/marketingInspection/pageListMarketInspection")
    public ModelAndView pageListMarketInspection() {
        ModelAndView mav = new ModelAndView("wechat/sjxx/yxsjxx_List");
        User user = getLoginInfo();
        List<SjdwxxDto> sjdwxxlist = sjxxService.getSjdw();
        mav.addObject("sjdwxxlist", sjdwxxlist);
        mav.addObject("samplelist", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
        mav.addObject("detectlist", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
        mav.addObject("xsbmList", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.PLATFORM_OWNERSHIP.getCode()));//销售部门
        mav.addObject("ptgsList", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.PLATFORM_OWNERSHIP_ORIGIN.getCode()));//平台归属
        List<JcsjDto> kyxmList = redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.RESEARCH_PROJECT.getCode());
        mav.addObject("kylist", kyxmList);//科研项目
        List<String> kylxList = new ArrayList<>();
        for (JcsjDto kyxm : kyxmList) {
            String kylx = StringUtil.isNotBlank(kyxm.getCskz1()) ? kyxm.getCskz1() : "其它";
            boolean isIn = false;
            for (String kylxmc : kylxList) {
                if (kylxmc.equals(kylx)) {
                    isIn = true;
                    break;
                }
            }
            if (!isIn) {
                kylxList.add(kylx);
            }
        }
        mav.addObject("kylxList", kylxList);//科研项目类型
        List<JcsjDto> fjlxs = redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.RECHECK.getCode());
        List<JcsjDto> fjlxList = new ArrayList<>();
        for (JcsjDto fjlx : fjlxs) {
            if ("1".equals(fjlx.getCskz1())) {
                fjlxList.add(fjlx);
            }
        }
        mav.addObject("fjlxList", fjlxList);//加测复测类型
        mav.addObject("cskz1List", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.FIRST_SJXXKZ.getCode()));
        mav.addObject("cskz2List", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.SECOND_SJXXKZ.getCode()));
        mav.addObject("cskz3List", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.THIRD_SJXXKZ.getCode()));
        mav.addObject("cskz4List", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.FOURTH_SJXXKZ.getCode()));
        mav.addObject("expressage", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
        mav.addObject("stamplist", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.STAMP_TYPE.getCode()));//盖章类型
        mav.addObject("detectionList", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
        mav.addObject("sjhbflList", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.CLASSIFY.getCode()));//合作伙伴分类
        mav.addObject("sjqfList", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));//送检区分
        // 查询合作伙伴
        //List<SjhbxxDto> sjhbxxlist = sjhbxxservice.getDtoList(null);
        //mav.addObject("sjhbxxlist", sjhbxxlist);
        GrlbzdszDto grlbzdszDto = new GrlbzdszDto();
        grlbzdszDto.setYhid(getLoginInfo().getYhid());
        grlbzdszDto.setYwid("MARKETINGINSPECT");
        LbzdszDto lbzdszDto = new LbzdszDto();
        lbzdszDto.setYwid("MARKETINGINSPECT");
        lbzdszDto.setYhid(user.getYhid());
        lbzdszDto.setJsid(user.getDqjs());
        List<LbzdszDto> choseList = grlbzdszService.getChoseList(grlbzdszDto);
        List<LbzdszDto> waitList = lbzdszService.getWaitList(lbzdszDto);
//		List<String> zdList = lbzdszService.getSjxxZdList();//从列表字段设置表中获取默认设置为 1显示、0隐藏、3角色限制、9主键的SQL字段
        String xszdlist = "";
        for (LbzdszDto lbzdszdto : choseList) {
            xszdlist = xszdlist + "," + lbzdszdto.getXszd();
        }
        for (LbzdszDto lbzdszdto : waitList) {
            xszdlist = xszdlist + "," + lbzdszdto.getXszd();
        }
        String limitColumns = "";
        if (StringUtil.isNotBlank(xszdlist)) {
            limitColumns = "{'sjxxDto':'" + xszdlist.substring(1) + "'}";
        }
        //查看角色是否有该字段权限
        Map<String, String> map = new HashMap<>();
        map.put("jsid", user.getDqjs());
        map.put("ywid", "STATISTICS");
        List<LbzdszDto> lbzdszDtos = lbzdszService.getXszdQx(map);
        String xszds = "";
        for (LbzdszDto lbzdszDto_t : lbzdszDtos) {
            xszds = xszds + "," + lbzdszDto_t.getXszd();
        }
        mav.addObject("xszds", xszds);
        mav.addObject("limitColumns", limitColumns);
        mav.addObject("choseList", choseList);
        mav.addObject("waitList", waitList);
        return mav;
    }

    /**
     * 送检信息列表
     *
     * @param sjxxDto
     * @return
     */
    @RequestMapping(value = "/marketingInspection/pageGetListMarketInspection")
    @ResponseBody
    public Map<String, Object> pageGetListMarketInspection(SjxxDto sjxxDto, HttpServletRequest request) {
        User user = getLoginInfo();
        List<Map<String, Object>> sjxxlist;
        //取出检测单位为一个List
        List<String> strList = new ArrayList<>();
        //根据角色查询到角色检测单位信息
        List<Map<String, String>> jcdwList = sjxxService.getJsjcdwByjsid(user.getDqjs());
        //判断检测单位是否为1（单位限制）
        if (jcdwList != null && jcdwList.size() > 0 && "1".equals(jcdwList.get(0).get("dwxdbj"))) {
            for (int i = 0; i < jcdwList.size(); i++) {
                if (jcdwList.get(i).get("jcdw") != null) {
                    strList.add(jcdwList.get(i).get("jcdw"));
                }
            }
            List<String> userids = new ArrayList<>();
            if (user.getYhid() != null) {
                userids.add(user.getYhid());
            }
            if (user.getDdid() != null) {
                userids.add(user.getDdid());
            }
            if (user.getWechatid() != null) {
                userids.add(user.getWechatid());
            }
            if (userids.size() > 0) {
                sjxxDto.setUserids(userids);
            }
            //判断伙伴权限
            List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
            if (hbqxList != null && hbqxList.size() > 0) {
                List<String> hbmcList = sjhbxxService.getHbmcByHbid(hbqxList);
                if (hbmcList != null && hbmcList.size() > 0) {
                    sjxxDto.setSjhbs(hbmcList);
                }
            }
            //如果检测单位不为空，进行查询。
            if (strList != null && strList.size() > 0) {
                sjxxDto.setJcdwxz(strList);
                Map<String, Object> params = yxsjxxService.pareMapFromDto(sjxxDto, request);
                sjxxlist = yxsjxxService.getDtoListOptimize(params);
                sjxxDto.setTotalNumber((int) params.get("totalNumber"));
            } else {
                //如果检测单位为空，直接返回空。没有查看权限
                sjxxlist = new ArrayList<>();
            }
        } else {
            Map<String, Object> params = yxsjxxService.pareMapFromDto(sjxxDto, request);
            sjxxlist = yxsjxxService.getDtoListOptimize(params);
            sjxxDto.setTotalNumber((int) params.get("totalNumber"));
        }

        Map<String, Object> map = new HashMap<>();
        //判断是否有实付金额字段权限，有就计算
        if (StringUtil.isNotBlank(sjxxDto.getSfzjezdqx()) && "1".equals(sjxxDto.getSfzjezdqx())) {
            SjxxDto sjxxDto_z = sjxxService.getSfzjeAndTkzje(sjxxDto);
            map.put("sfzje", sjxxDto_z.getSfzje());
        }
        if (StringUtil.isNotBlank(sjxxDto.getTkzjezdqx()) && "1".equals(sjxxDto.getTkzjezdqx())) {
            SjxxDto sjxxDto_z = sjxxService.getSfzjeAndTkzje(sjxxDto);
            map.put("tkzje", sjxxDto_z.getTkzje());
        }
        map.put("total", sjxxDto.getTotalNumber());
        map.put("rows", sjxxlist);
        //需要筛选钉钉字段的，请调用该方法
        screenClassColumns(request, map);
        return map;
    }

    /**
     * 查看营销统计详情
     *
     * @return
     */
    @RequestMapping(value = "/marketingInspection/viewYxSjxx")
    public ModelAndView viewYxSjxx(HttpServletRequest request) {
        String sjid = request.getParameter("sjid");
        String fjid = request.getParameter("fjid");
        String mavurl = "wechat/list_sjhb/sjxx_ListView";
        if (StringUtil.isNotBlank(fjid)) {
            mavurl = "wechat/recheck/recheck_view";
        }
        ModelAndView mav = new ModelAndView(mavurl);
        if (StringUtil.isNotBlank(fjid)) {
            FjsqDto fjsqDto = new FjsqDto();
            fjsqDto.setFjid(fjid);
            FjsqDto fjsqDtos = fjsqService.getDto(fjsqDto);
            SjybztDto sjybztDto = new SjybztDto();
            sjybztDto.setSjid(fjsqDtos.getSjid());
            String xzbj = "";
            sjybztDto.setYbztCskz1("S");
            List<SjybztDto> sjybztDtos = sjybztService.getDtoList(sjybztDto);
            if (sjybztDtos != null && sjybztDtos.size() > 0) {
                xzbj = "1";
            }
            SjxxDto sjxxDto = new SjxxDto();
            sjxxDto.setSjid(fjsqDtos.getSjid());
            SjxxDto sjxxDtos = sjxxService.getDto(sjxxDto);
            if (sjxxDtos != null) {
                if ("1".equals(sjxxDtos.getYyxxCskz1())) {
                    sjxxDtos.setSjdw(sjxxDtos.getHospitalname() + "-" + sjxxDtos.getSjdwmc());
                } else {
                    sjxxDtos.setSjdw(sjxxDtos.getHospitalname());
                }
                mav.addObject("sjxxDto", sjxxDtos);
            } else {
                mav.addObject("sjxxDto", sjxxDto);
            }
            mav.addObject("fjsqDto", fjsqDtos);
            mav.addObject("xzbj", xzbj);
        } else {
            SjxxDto sjxxDto = new SjxxDto();
            sjxxDto.setSjid(sjid);
            List<SjnyxDto> sjnyx = sjnyxService.getNyxBySjid(sjxxDto);
            SjxxDto sjxxDto2 = sjxxService.getDto(sjxxDto);
            List<SjwzxxDto> sjwzxx = sjxxService.selectWzxxBySjid(sjxxDto2);
            if (sjwzxx != null && sjwzxx.size() > 0) {
                String xpxx = sjwzxx.get(0).getXpxx();//由于一个标本中的物种芯片信息相同，取其一
                mav.addObject("Xpxx", xpxx);
            }

//            if (("Z6").equals(sjxxDto2.getCskz1()) || ("Z12").equals(sjxxDto2.getCskz1()) || ("Z18").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1())) {
            if (("Z").equals(sjxxDto2.getCskz1()) ||("Z6").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1())) {
                SjzmjgDto sjzmjgDto = new SjzmjgDto();
                sjzmjgDto.setSjid(sjxxDto.getSjid());
                List<SjzmjgDto> sjzmList = sjzmjgService.getDtoList(sjzmjgDto);
                mav.addObject("sjzmList", sjzmList);
                mav.addObject("KZCS", sjxxDto2.getCskz1());
                log.error(sjxxDto2.getCskz1());
                log.error(JSON.toJSONString(sjzmList));
            }
            FjcfbDto fjcfbDto = new FjcfbDto();
            fjcfbDto.setYwid(sjxxDto.getSjid());
            fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionPdfYwlxs());

            List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
            mav.addObject("fjcfbDtos", fjcfbDtos);
            fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionWordYwlxs());
            List<FjcfbDto> zhwj = fjcfbService.selectzhpdf(fjcfbDto);
            fjcfbDto.setYwlxs(null);
            fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
            List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
            //收样附件
            fjcfbDto.setYwlxs(null);
            fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION_CONFIRM.getCode());
            List<FjcfbDto> b_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
            SjbgsmDto sjbgsmdto = new SjbgsmDto();
            sjbgsmdto.setSjid(sjxxDto.getSjid());
            List<SjbgsmDto> sjbgsmxx = sjbgsmService.selectSjbgBySjid(sjbgsmdto);

            Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE});
            List<JcsjDto> jcxmlist = jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
            List<JcsjDto> t_jcxmlist = new ArrayList<>();//用于结果页

            if (jcxmlist != null && jcxmlist.size() > 0) {
                for (int i = 0; i < jcxmlist.size(); i++) {

                    boolean sftj = false;//判断对应该检测项目的报告说明和附件是否存在，若其中一个存在添加该项目
                    //查看页面自勉报告不显示问题 // 判断WORD附件 不判断PDF附件  auther:zhanghan  2020/12/22
//                    if (("Z6").equals(sjxxDto2.getCskz1()) || ("Z8").equals(sjxxDto2.getCskz1()) || ("Z12").equals(sjxxDto2.getCskz1()) || ("Z18").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1()) || ("T3").equals(sjxxDto2.getCskz1()) || ("T6").equals(sjxxDto2.getCskz1()) || ("K").equals(sjxxDto2.getCskz1()) || ("X").equals(sjxxDto2.getCskz1()) || ("Y").equals(sjxxDto2.getCskz1()) || ("G").equals(sjxxDto2.getCskz1())) {
                    if (("Z").equals(sjxxDto2.getCskz1()) ||("Z6").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1()) || ("T3").equals(sjxxDto2.getCskz1()) || ("T6").equals(sjxxDto2.getCskz1()) || ("K").equals(sjxxDto2.getCskz1()) || ("X").equals(sjxxDto2.getCskz1()) || ("Y").equals(sjxxDto2.getCskz1()) || ("G").equals(sjxxDto2.getCskz1())) {
                        if (zhwj != null && zhwj.size() > 0) {
                            for (int j = 0; j < zhwj.size(); j++) {
                                if (zhwj.get(j).getYwlx().equals((jcxmlist.get(i).getCskz3() + "_" + jcxmlist.get(i).getCskz1() + "_WORD"))) {
                                    sftj = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!sftj && sjbgsmxx != null && sjbgsmxx.size() > 0) {
                        boolean isFindSub = false;
                        for (int j = 0; j < sjbgsmxx.size(); j++) {
                            if (jcxmlist.get(i).getCsid().equals(sjbgsmxx.get(j).getJcxmid())) {
                                //确认是否为 检测子项目的数据，因为框架不想调整，所以如果是，则直接设置，并进行下一个
                                if(StringUtil.isNotBlank(sjbgsmxx.get(j).getJczlx()) ) {
                                    Object o_sub_jcsj = redisUtil.hget("matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode(), sjbgsmxx.get(j).getJczlx());
                                    if (o_sub_jcsj == null)
                                        continue;
                                    JcsjDto sub_jcsj_dto = JSONObject.parseObject(o_sub_jcsj.toString(), JcsjDto.class);

                                    Object o_jcsj = redisUtil.hget("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode(), sub_jcsj_dto.getFcsid());
                                    if (o_jcsj != null) {
                                        JcsjDto jcsj_dto = JSONObject.parseObject(o_jcsj.toString(), JcsjDto.class);
                                        sub_jcsj_dto.setFcsdm(jcsj_dto.getCsdm());
                                        sub_jcsj_dto.setFcsmc(jcsj_dto.getCsmc());
                                    }

                                    t_jcxmlist.add(sub_jcsj_dto);
                                    isFindSub = true;
                                }else{
                                    sftj=true;
                                    break;
                                }
                            }
                        }
                        if(isFindSub)
                            continue;
                    } else if (!sftj && t_fjcfbDtos != null && t_fjcfbDtos.size() > 0) {
                        String cskz3 = jcxmlist.get(i).getCskz3();
                        String cskz1 = jcxmlist.get(i).getCskz1();
                        for (int j = 0; j < t_fjcfbDtos.size(); j++) {
                            if (t_fjcfbDtos.get(j).getYwlx().equals((cskz3 == null ? "" : cskz3.replace("_ONCO", "") + cskz1 == null ? "" : cskz1))) {
                                sftj = true;
                                break;
                            }
                        }
                    }
                    if (!sftj && fjcfbDtos != null && fjcfbDtos.size() > 0) {
                        String cskz3 = jcxmlist.get(i).getCskz3();
                        String cskz1 = jcxmlist.get(i).getCskz1();
                        for (int j = 0; j < fjcfbDtos.size(); j++) {
                            if (fjcfbDtos.get(j).getYwlx().equals((cskz3 == null ? "" : cskz3.replace("_ONCO", "") + cskz1 == null ? "" : cskz1))|| fjcfbDtos.get(j).getYwlx().equals((cskz3==null?"":cskz3) +"_"+ (cskz1==null?"":cskz1))) {
                                sftj = true;
                                break;
                            }
                        }
                    }
                    if (sftj)
                        t_jcxmlist.add(jcxmlist.get(i));
                }
            }

            SjxxjgDto sjxxjgDto = new SjxxjgDto();
            sjxxjgDto.setSjid(sjxxDto.getSjid());
            List<SjxxjgDto> getJclxCount = sjxxjgService.getJclxCount(sjxxjgDto);
            //查看当前复检申请信息
            FjsqDto fjsqDto = new FjsqDto();
            String[] zts = {StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_PASS.getCode()};
            fjsqDto.setZts(zts);
            fjsqDto.setSjid(sjxxDto2.getSjid());
            List<FjsqDto> fjsqList = fjsqService.getListBySjid(fjsqDto);
            List<SjsyglDto> sjsyglDtos = sjsyglService.getViewDetectData(sjid);
            if(sjsyglDtos!=null&&sjsyglDtos.size()>0){
                for(SjsyglDto dto:sjsyglDtos){
                    if(StringUtil.isNotBlank(dto.getSjsj())){
                        sjxxDto2.setSjsj(dto.getSjsj());
                        break;
                    }
                }
            }
            mav.addObject("sjsyglDtos", sjsyglDtos);
            mav.addObject("fjsqList", fjsqList);
            mav.addObject("SjxxjgList", getJclxCount);
            mav.addObject("SjnyxDto", sjnyx);
            mav.addObject("zhwjpdf", zhwj);
            mav.addObject("sjbgsmList", sjbgsmxx);
            mav.addObject("t_fjcfbDtos", t_fjcfbDtos);
            mav.addObject("sjxxDto", sjxxDto2);
            mav.addObject("Sjwzxx", sjwzxx);
            mav.addObject("jcxmlist", t_jcxmlist);
            mav.addObject("b_fjcfbDtos", b_fjcfbDtos);
        }
        return mav;
    }


    /**
     * 修改操作
     * @param request
     * @return
     */
    @RequestMapping(value = "/marketingInspection/modYxSjxx")
    public ModelAndView modYxSjxx(HttpServletRequest request) {
        String sjid = request.getParameter("sjid");
        String xg_flg = request.getParameter("xg_flg");
        String fjid = request.getParameter("fjid");
        String mavurl = "wechat/sjxx/sjxx_ListSave";
        if (StringUtil.isNotBlank(fjid)) {
            mavurl = "wechat/recheck/recheck_mod";
        }
        ModelAndView mav = new ModelAndView(mavurl);
        if (StringUtil.isNotBlank(fjid)) {
            FjsqDto fjsqDto = fjsqService.getDtoById(fjid);
            FjsqyyDto fjsqyyDto = new FjsqyyDto();
            fjsqyyDto.setFjid(fjid);
            List<FjsqyyDto> fjsqyyDtos = fjsqyyService.getDtoList(fjsqyyDto);
            List<JcsjDto> jc_fjyylist = redisUtil.lgetDto("All_matridx_jcsj:"+BasicDataTypeEnum.RECHECK_REASON.getCode());
            List<JcsjDto> fjyylist = new ArrayList<>();
            if (fjsqyyDtos != null && fjsqyyDtos.size() > 0) {
                for (int i = 0; i < jc_fjyylist.size(); i++) {
                    if (fjsqDto.getLx().equals(jc_fjyylist.get(i).getFcsid())) {
                        fjyylist.add(jc_fjyylist.get(i));
                    }
                    for (FjsqyyDto t_fjsqyyDto : fjsqyyDtos) {
                        if (t_fjsqyyDto.getYy().equals(jc_fjyylist.get(i).getCsid())) {
                            jc_fjyylist.get(i).setChecked("1");//是否选中
                        }
                    }
                }
            }
            mav.addObject("detectlist", redisUtil.lgetDto("All_matridx_jcsj:"+BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
            mav.addObject("detectionList", redisUtil.lgetDto("All_matridx_jcsj:"+BasicDataTypeEnum.DETECTION_UNIT.getCode()));//送检单位
            mav.addObject("fjyylist", fjyylist);//复检原因
            SjxxDto sjxxDto = new SjxxDto();
            sjxxDto.setSjid(fjsqDto.getSjid());
            SjxxDto sjxxDtos = sjxxService.getDto(sjxxDto);
            if (sjxxDtos != null) {
                if ("1".equals(sjxxDtos.getYyxxCskz1())) {
                    sjxxDtos.setSjdw(sjxxDtos.getHospitalname() + "-" + sjxxDtos.getSjdwmc());
                } else {
                    sjxxDtos.setSjdw(sjxxDtos.getHospitalname());
                }
                mav.addObject("sjxxDto", sjxxDtos);
            } else {
                mav.addObject("sjxxDto", sjxxDto);
            }
            mav.addObject("fjsqDto", fjsqDto);
        } else {
            SjxxDto sjxxDto = new SjxxDto();
            sjxxDto.setSjid(sjid);
            sjxxDto.setXg_flg(xg_flg);
            SjxxDto t_sjxxDto = sjxxService.getDtoById(sjxxDto.getSjid());
            List<SjdwxxDto> sjdwxxList = sjxxService.getSjdw();//查询出所有送检单位；
            //t_sjxxDto.setJcxmids(sjjcxmService.getSjjcxm(sjxxDto.getSjid()));//把查询出来的送检项目set到送检信息中去；
            t_sjxxDto.setBys(sjgzbyService.getGzby(sjxxDto.getSjid()));//把查询出来的关注病原set到送检信息中去；
            t_sjxxDto.setLczzs(sjlczzService.getLczz(sjxxDto.getSjid()));//把查询出来的临床症状set到送检信息中去；
            t_sjxxDto.setSjqqjcs(sjqqjcService.getJcz(sjxxDto.getSjid()));//查询前期检测项目；
            t_sjxxDto.setZts(sjybztService.getZtBysjid(sjxxDto.getSjid()));
            t_sjxxDto.setFormAction("modSaveSjxx");
            t_sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
            t_sjxxDto.setXg_flg(sjxxDto.getXg_flg());
            List<FjcfbDto> fjcfbDtos = sjxxService.selectFjByWjid(sjxxDto.getSjid());
            SjkzxxDto sjkzxxDto_t = sjkzxxService.getDtoById(sjxxDto.getSjid());
            if(sjkzxxDto_t!=null){
                mav.addObject("sjkzxxDto", sjkzxxDto_t);
            }else{
                mav.addObject("sjkzxxDto", new SjkzxxDto());
            }
            SjjcxmDto sjjcxmDto = new SjjcxmDto();
            sjjcxmDto.setSjid(sjid);
            List<SjjcxmDto> dtoList = sjjcxmService.getDtoList(sjjcxmDto);
            List<String> jcxmids = new ArrayList<>();
            List<String> jczxmids = new ArrayList<>();
            for (SjjcxmDto dto : dtoList) {
                jcxmids.add(dto.getJcxmid());
                jczxmids.add(dto.getJczxmid());
            }
            mav.addObject("jczxmids", jczxmids);
            t_sjxxDto.setJcxmids(jcxmids);

            if (!CollectionUtils.isEmpty(jcxmids)){
                List<JcsjDto> jczxmlist= new ArrayList<>();
                JcsjDto jcsjDto = new JcsjDto();
                jcsjDto.setFcsidList(jcxmids);
                jczxmlist = jcsjService.getListByFid(jcsjDto);
                mav.addObject("jczxmlist", jczxmlist);
            }

            mav.addObject("dtoList", JSONObject.toJSONString(dtoList));
            mav.addObject("divisionListJson", JSONObject.toJSONString(redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.INSPECTION_DIVISION.getCode())));//送检区分
            mav.addObject("sjxxDto", t_sjxxDto);
            mav.addObject("sjdwxxList", sjdwxxList);
            mav.addObject("clincallist", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.CLINICAL_TYPE.getCode()));//临床症状
            mav.addObject("pathogenylist", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));//病原
            mav.addObject("samplelist", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
            mav.addObject("datectlist", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
            mav.addObject("detectionlist", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.DETECTION_TYPE.getCode()));//前期检测
            mav.addObject("samplestate", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.SAMPLESTATE.getCode()));//标本状态
            mav.addObject("expressage", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
            mav.addObject("decetionlist", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.DETECTION_UNIT.getCode()));
            mav.addObject("self_projectList", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.SELFEXEMPTION_PROJECT.getCode()));//自免项目
            mav.addObject("self_destinationList", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.DETECTION_UNIT.getCode()));//自免目的地
            mav.addObject("divisionList", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));//送检区分
            mav.addObject("invoiceList", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.INVOICE_APPLICATION.getCode()));//开票申请
            mav.addObject("refundlist", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.REFUND_METHOD.getCode()));//退款方式
            mav.addObject("kyxmlist", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.RESEARCH_PROJECT.getCode()));//科研项目

            mav.addObject("fjcfbDtos", fjcfbDtos);
        }
        return mav;
    }


    /**
     * 扩展参数修改
     * @return
     */
    @RequestMapping("/marketingInspection/extendCskzView")
    public ModelAndView extendCskzView(SjxxDto sjxxDto) {
        ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_cskz");
        List<SjxxDto> sjxxDtos=sjxxService.getSjxxCskz(sjxxDto);
        mav.addObject("sjkz1list", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.FIRST_SJXXKZ.getCode()));
        mav.addObject("sjkz2list", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.SECOND_SJXXKZ.getCode()));
        mav.addObject("sjkz3list", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.THIRD_SJXXKZ.getCode()));
        mav.addObject("sjkz4list", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.FOURTH_SJXXKZ.getCode()));
        mav.addObject("ksxxList", sjxxService.getSjdw());
        if(sjxxDtos!=null && sjxxDtos.get(0)!=null) {
            sjxxDto.setCskz1(sjxxDtos.get(0).getCskz1());
            sjxxDto.setCskz2(sjxxDtos.get(0).getCskz2());
            sjxxDto.setCskz3(sjxxDtos.get(0).getCskz3());
            sjxxDto.setCskz4(sjxxDtos.get(0).getCskz4());
            sjxxDto.setCskz5(sjxxDtos.get(0).getCskz5());
            if (sjxxDtos.size()>0){
                boolean isKsSame = true;
                boolean isQtksSame = true;
                String qtks = StringUtil.isNotBlank(sjxxDtos.get(0).getQtks())?sjxxDtos.get(0).getQtks():"";
                String ks = StringUtil.isNotBlank(sjxxDtos.get(0).getKs())?sjxxDtos.get(0).getKs():"";
                for (SjxxDto sjxxDto_t : sjxxDtos) {
                    if (!ks.equals(sjxxDto_t.getKs())){
                        isKsSame = false;
                    }
                    if (!qtks.equals(sjxxDto_t.getQtks())){
                        isQtksSame = false;
                    }
                    if (!isKsSame && !isQtksSame){
                        break;
                    }
                }
                if (isKsSame){
                    sjxxDto.setKs(ks);
                }
                if (isQtksSame){
                    sjxxDto.setQtks(qtks);
                }
            }
        }
        mav.addObject("sjxxDto", sjxxDto);
        return mav;
    }

    /**
     * 导出对账单
     * @return
     */
    @RequestMapping(value = "/marketingInspection/exportaccountYxSjxx")
    public ModelAndView exportaccountYxSjxx() {
        ModelAndView mav = new ModelAndView("wechat/sjxx/yxsjxx_exportAccount");
        mav.addObject("dzzqlist", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.RECONCILIATION_TYPE.getCode()));
        return mav;
    }

    /**
     * 获取查询数据
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/marketingInspection/pagedataSelectQuery")
    @ResponseBody
    public Map<String, Object> pagedataSelectQuery(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("sortName","sj.jsrq");
        params.put("sortOrder","asc");
        params.put("pageSize",200);
        params.put("pageNumber",1);
        params.put("pageStart",0);
        //params.put("hbmc",request.getParameter("hbmc"));
        List<String> hbmcList=new ArrayList<>();
        if(StringUtil.isNotBlank(request.getParameter("hbmc"))){
            String hbmc=request.getParameter("hbmc");
            if(hbmc.indexOf("，")!=-1){
                hbmc=hbmc.replace("，",",");
            }
            String[] hbmcArr=hbmc.split(",");
            for(String _hbmc:hbmcArr){
                if(StringUtil.isNotBlank(_hbmc)){
                    hbmcList.add(_hbmc.trim());
                }
            }
        }
        if(hbmcList!=null&&hbmcList.size()>0){
            params.put("hbmcs",hbmcList);
        }
        List<String> khmcList=new ArrayList<>();
        if(StringUtil.isNotBlank(request.getParameter("khmc"))){
            String khmc=request.getParameter("khmc");
            if(khmc.indexOf("，")!=-1){
                khmc=khmc.replace("，",",");
            }
            String[] khmcArr=khmc.split(",");
            for(String _khmc:khmcArr){
                if(StringUtil.isNotBlank(_khmc)){
                    khmcList.add(_khmc.trim());
                }
            }
        }
        if(khmcList!=null&&khmcList.size()>0){
            params.put("khmcs",khmcList);
        }
        params.put("dzzq",request.getParameter("dzzq"));
        String jsrq = request.getParameter("jsrq");
        params.put("jsyf",jsrq);
        String dzzqdm = request.getParameter("dzzqdm");
        int month=Integer.parseInt(jsrq.split("-")[1]);
        int year=Integer.parseInt(jsrq.split("-")[0]);

        Calendar calendar = Calendar.getInstance();
        // 获取当前年
        int nowYear = calendar.get(Calendar.YEAR);
        // 取当前月
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        // 获取当前日
        int nowDay = calendar.get(Calendar.DATE);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if("NM".equals(dzzqdm)){
            //如果选择的是本年本月，并且没过20号，则取上个月
            if(year==nowYear&&month==nowMonth&&nowDay<20){
                // 上月起始
                Calendar lastMonthFirstDateCal = Calendar.getInstance();
                //这里由于考虑到将3月分标本数据放到4月份去对账，希望4月份对账的时候能够找到3月份没有对账的数据,所以在原有基础再往前推一个月
                lastMonthFirstDateCal.add(Calendar.MONTH, -2);
                lastMonthFirstDateCal.set(Calendar.DAY_OF_MONTH, 1);
                String lastMonthFirstTime = format.format(lastMonthFirstDateCal.getTime());
                // 上月末尾
                Calendar lastMonthEndDateCal = Calendar.getInstance();
                lastMonthEndDateCal.add(Calendar.MONTH, -1);
                lastMonthEndDateCal.set(Calendar.DAY_OF_MONTH, lastMonthEndDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));
                String lastMonthEndTime = format.format(lastMonthEndDateCal.getTime());
                params.put("jsrqstart",lastMonthFirstTime);
                params.put("jsrqend",lastMonthEndTime);
            }else{
                String dateStr = jsrq.replaceAll("-",""); // 指定年月
                LocalDate date = LocalDate.parse(dateStr + "01", DateTimeFormatter.BASIC_ISO_DATE);
                LocalDate dateFirst = date.with(TemporalAdjusters.firstDayOfMonth()); // 指定年月的第一天
                LocalDate dateEnd = date.with(TemporalAdjusters.lastDayOfMonth()); // 指定年月的最后一天
                params.put("jsrqstart",dateFirst.toString());
                params.put("jsrqend",dateEnd.toString());
            }
        }else if("SPECIFIC".equals(dzzqdm)){
            //如果选择的是本年本月，并且没过20号，则取上个月
            if(year==nowYear&&month==nowMonth&&nowDay<20){
                // 上上个月26号
                Calendar monthFirstDateCal = Calendar.getInstance();
                monthFirstDateCal.add(Calendar.MONTH, -2);
                monthFirstDateCal.set(Calendar.DAY_OF_MONTH, 26);
                String monthFirstTime = format.format(monthFirstDateCal.getTime());
                // 上月25号
                Calendar monthEndDateCal = Calendar.getInstance();
                monthEndDateCal.add(Calendar.MONTH, -1);
                monthEndDateCal.set(Calendar.DAY_OF_MONTH, 25);
                String monthEndTime = format.format(monthEndDateCal.getTime());
                params.put("jsrqstart",monthFirstTime);
                params.put("jsrqend",monthEndTime);
            }else{
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(format.parse(jsrq+"-26"));
                    cal.add(Calendar.MONTH, -1);
                    params.put("jsrqstart", format.format(cal.getTime()));
                    params.put("jsrqend",jsrq+"-25");
                }catch (Exception e){
                    log.error("获取上一月月份异常：",e.getMessage());
                }
            }
        }
        List<Map<String, Object>> sjxxlist = yxsjxxService.getDtoAccountList(params);
        map.put("rows", sjxxlist);
        return map;
    }

    /**
     * 压缩下载
     *
     * @param httpResponse
     * @return
     */
    @RequestMapping("/marketingInspection/pagedataZipDownload")
    @ResponseBody
    public void pagedataZipDownload(HttpServletRequest request,HttpServletResponse httpResponse) {
        yxsjxxService.packageZipAndDownload(request,httpResponse);
    }


    /**
     * 项目金额维护
     * @param
     * @return
     */
    @RequestMapping(value="/marketingInspection/paymentProjectAmount")
    public ModelAndView paymentProjectAmount(SjjcxmDto sjjcxmDto){
        ModelAndView mav=new ModelAndView("wechat/sjxx/yxsjxx_amountPayment");
        if (StringUtil.isNotBlank(sjjcxmDto.getSjid())){
            List<SjjcxmDto> allDtoList = sjjcxmService.getAllDtoList(sjjcxmDto);
            List<SjjcxmDto> beforeList = new ArrayList<>();
            List<SjjcxmDto> laterList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(allDtoList)){
                for (SjjcxmDto dto : allDtoList) {
                    if (StringUtil.isNotBlank(dto.getScbj()) && "1".equals( dto.getScbj())  && "是".equals(dto.getSfsf())){
                        beforeList.add(dto);
                    }else if ("0".equals( dto.getScbj()) && "是".equals(dto.getSfsf())){
                        laterList.add(dto);
                    }
                }
            }
            mav.addObject("beforeList",JSONObject.toJSONString(beforeList));
            mav.addObject("sjid",sjjcxmDto.getSjid());
            mav.addObject("laterList",JSONObject.toJSONString(laterList));
        }
        return mav;
    }

    /**
     * 项目金额维护
     * @param
     * @return
     */
    @RequestMapping(value="/marketingInspection/paymentSaveProjectAmount")
    @ResponseBody
    public Map<String, Object> paymentSaveProjectAmount(SjjcxmDto sjjcxmDto){
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        sjjcxmDto.setXgry(user.getYhid());
        try {
            sjxxService.saveProjectAmount(sjjcxmDto);
            map.put("status","success");
            map.put("message","保存成功！");
        } catch (BusinessException e) {
            map.put("status","fail");
            map.put("message",e.getMsg());
        }
        return map;
    }
}

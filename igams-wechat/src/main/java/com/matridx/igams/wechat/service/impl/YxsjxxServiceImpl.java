package com.matridx.igams.wechat.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.KhyhzcbzDto;
import com.matridx.igams.wechat.dao.entities.SjjcxmDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxModel;
import com.matridx.igams.wechat.dao.post.IYxsjxxDao;
import com.matridx.igams.wechat.service.svcinterface.IFjsqService;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.IKhyhzcbzService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.IYxsjxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class YxsjxxServiceImpl extends BaseBasicServiceImpl<SjxxDto, SjxxModel, IYxsjxxDao> implements IYxsjxxService {

    @Autowired
    private IJcsjService jcsjService;
    @Autowired
    private ISjxxService sjxxService;
    @Autowired
    private IHbqxService hbqxService;
    @Autowired
    private ISjhbxxService sjhbxxService;
    @Autowired
    private ISjjcxmService sjjcxmService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private IFjsqService fjsqService;
    @Autowired
    private IKhyhzcbzService khyhzcbzService;

    private Logger log = LoggerFactory.getLogger(YxsjxxServiceImpl.class);

    /**
     * 从Dto里把数据放到Map里，减少Dto的属性设置，防止JSON出错
     *
     * @param sjxxDto
     * @return
     */
    public Map<String, Object> pareMapFromDto(SjxxDto sjxxDto, HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("sjhbs", sjxxDto.getSjhbs());//送检伙伴s 权限
        paramMap.put("dwids", sjxxDto.getDwids());//送检科室 高级筛选
        paramMap.put("yblxs", sjxxDto.getYblxs());//标本类型 高级筛选
        paramMap.put("kylxs", sjxxDto.getKylxs());//科研类型（项目性质） 高级筛选
        paramMap.put("kyxms", sjxxDto.getKyxms());//科研项目（立项编号） 高级筛选
        paramMap.put("jcxms", sjxxDto.getJcxms());//检测项目 高级筛选
        paramMap.put("ptgss", sjxxDto.getPtgss());//平台归属 高级筛选
        paramMap.put("xsbms", sjxxDto.getXsbms());//平台归属 高级筛选
        paramMap.put("sjhbfls", sjxxDto.getSjhbfls());//伙伴分类 高级筛选
        paramMap.put("sjhbzfls", sjxxDto.getSjhbzfls());//伙伴子分类 高级筛选
        paramMap.put("jyjgs", sjxxDto.getJyjgs());//报告结果 高级筛选
        paramMap.put("fkbj", sjxxDto.getFkbj());//是否付款 高级筛选
        paramMap.put("clbj", sjxxDto.getClbj());//处理标记
        paramMap.put("dcbjflg", sjxxDto.getDcbjflg());//导出标记
        paramMap.put("sfsfs", sjxxDto.getSfsfs());//是否收费 高级筛选
        paramMap.put("sfjs", sjxxDto.getSfjs());//是否接收 高级筛选
        paramMap.put("sjkzcs1", sjxxDto.getSjkzcs1());//是否开票 高级筛选
        paramMap.put("dcbj", sjxxDto.getDcbj());//导出标记 模糊查询
        paramMap.put("sfdz", sjxxDto.getSfdz());//是否对账 高级筛选
        paramMap.put("tjmc", request.getParameter("tjmc"));//是否对账 高级筛选
        paramMap.put("yhmc", request.getParameter("yhmc"));//销售负责人
        //是否上传 高级筛选
        String s_sfsc = sjxxDto.getSfsc();
        if (StringUtil.isNotBlank(s_sfsc)) {
            String[] sfscs = s_sfsc.split(",");
            for (int i = 0; i < sfscs.length; i++) {
                sfscs[i] = sfscs[i].replace("'", "");
            }
            paramMap.put("sfsc", sfscs);
        }
        //是否自免检测 高级筛选
        String s_sfzmjc = sjxxDto.getSfzmjc();
        if (StringUtil.isNotBlank(s_sfzmjc)) {
            String[] sjzmjcs = s_sfzmjc.split(",");
            for (int i = 0; i < sjzmjcs.length; i++) {
                sjzmjcs[i] = sjzmjcs[i].replace("'", "");
            }
            paramMap.put("sfzmjc", sjzmjcs);
        }
        paramMap.put("sjkzcs2", sjxxDto.getSjkzcs2());//是否到账 高级筛选
        paramMap.put("sjkzcs4", sjxxDto.getSjkzcs4());//送检扩展4 高级筛选
        paramMap.put("jcdws", sjxxDto.getJcdws());//检测单位 高级筛选
        paramMap.put("kdlxs", sjxxDto.getKdlxs());//快递类型 高级筛选
        paramMap.put("gzlxs", sjxxDto.getGzlxs());//盖章类型 高级筛选
        paramMap.put("sjqfs", sjxxDto.getSjqfs());//送检区分 高级筛选
        paramMap.put("yyzddjs", sjxxDto.getYyzddjs());//医院重点等级 高级筛选
        //复测类型 高级筛选
        String s_fjlxs = request.getParameter("fjlxs");
        if (StringUtil.isNotBlank(s_fjlxs)) {
            String[] fjlxs = s_fjlxs.split(",");
            for (int i = 0; i < fjlxs.length; i++) {
                fjlxs[i] = fjlxs[i].replace("'", "");
            }
            paramMap.put("fjlxs", fjlxs);
        }
        paramMap.put("bgrqstart", sjxxDto.getBgrqstart());//报告日期开始
        paramMap.put("bgrqend", sjxxDto.getBgrqend());//报告日期结束
        paramMap.put("fkrqstart", sjxxDto.getFkrqstart());//付款日期开始
        paramMap.put("fkrqend", sjxxDto.getFkrqend());//付款日期结束
        paramMap.put("syrqstart", sjxxDto.getSyrqstart());//实验日期开始
        paramMap.put("syrqflg", sjxxDto.getSyrqflg());//实验日期标记
        paramMap.put("syrqend", sjxxDto.getSyrqend());//实验日期结束
        paramMap.put("jsrqstart", sjxxDto.getJsrqstart());//接收日期开始
        paramMap.put("jsrqend", sjxxDto.getJsrqend());//接收日期结束
        paramMap.put("all_param", sjxxDto.getAll_param());//查询条件 所有
        paramMap.put("hzxm", sjxxDto.getHzxm());//查询条件 患者姓名
        paramMap.put("db", sjxxDto.getDb());//查询条件 合作伙伴
        paramMap.put("hospitalname", sjxxDto.getHospitalname());//查询条件 医院名称、单位简称
        paramMap.put("cskz5", sjxxDto.getCskz5());//查询条件 发票号码
        paramMap.put("ybbh", sjxxDto.getYbbh());//查询条件 样本编号
        paramMap.put("nbbm", sjxxDto.getNbbm());//查询条件 内部编码
        paramMap.put("sjys", sjxxDto.getSjys());//查询条件 送检医生
        paramMap.put("kyxm", sjxxDto.getKyxm());//查询条件 科研项目（立项编号）
        paramMap.put("yytjmc", sjxxDto.getYytjmc());//查询条件 医院统计名称
        paramMap.put("qtks", sjxxDto.getQtks());//查询条件 报告显示科室
        paramMap.put("ids", sjxxDto.getIds());
        paramMap.put("userids", sjxxDto.getUserids());
        paramMap.put("jcdwxz", sjxxDto.getJcdwxz());
        paramMap.put("sortName", sjxxDto.getSortName());
        paramMap.put("sortGroupName", sjxxDto.getSortName().replace("desc" , "").replace("asc" , ""));
        paramMap.put("sortOrder", sjxxDto.getSortOrder());
        paramMap.put("sortLastName", sjxxDto.getSortLastName());
        paramMap.put("sortGroupLastName", sjxxDto.getSortLastName().replace("desc" , "").replace("asc" , ""));
        paramMap.put("sortLastOrder", sjxxDto.getSortLastOrder());
        paramMap.put("pageSize", sjxxDto.getPageSize());
        paramMap.put("pageNumber", sjxxDto.getPageNumber());
        paramMap.put("pageStart",(sjxxDto.getPageNumber() -1 ) *sjxxDto.getPageSize());
        if(StringUtil.isNotBlank(sjxxDto.getSortName())){
            List<String> list = Arrays.asList(sjxxDto.getSortName().split(","));
            for(String s : list){
                if(StringUtil.isNotBlank(s)){
                    int spaceIndex = s.indexOf(' ');
                    String orderStr = spaceIndex == -1 ? s : s.substring(0, spaceIndex);
                    if("jsje".equals(orderStr)){
                        paramMap.put("jsjeFlag","jsjeFlag");
                    }
                    if("ybbh".equals(orderStr)){
                        paramMap.put("ybbhFlag","ybbhFlag");
                    }
                    if("nbbm".equals(orderStr)){
                        paramMap.put("nbbmFlag","nbbmFlag");
                    }
                    if("hzxm".equals(orderStr)){
                        paramMap.put("hzxmFlag","hzxmFlag");
                    }
                    if("yymc".equals(orderStr)){
                        paramMap.put("yymcFlag","yymcFlag");
                    }
                    if("sjdwmc".equals(orderStr)){
                        paramMap.put("sjdwmcFlag","sjdwmcFlag");
                    }
                    if("yblxmc".equals(orderStr)){
                        paramMap.put("yblxmcFlag","yblxmcFlag");
                    }
                    if("jsrq".equals(orderStr)){
                        paramMap.put("jsrqFlag","jsrqFlag");
                    }
                    if("bgrq".equals(orderStr)){
                        paramMap.put("bgrqFlag","bgrqFlag");
                    }
                    if("sfsf".equals(orderStr)){
                        paramMap.put("sfsfFlag","sfsfFlag");
                    }
                    if("ptgsmc".equals(orderStr)){
                        paramMap.put("ptgsmcFlag","ptgsmcFlag");
                    }
                    if("yhmc".equals(orderStr)){
                        paramMap.put("yhmcFlag","yhmcFlag");
                    }
                    if("db".equals(orderStr)){
                        paramMap.put("dbFlag","dbFlag");
                    }
                    if("xsbmmc".equals(orderStr)){
                        paramMap.put("xsbmmcFlag","xsbmmcFlag");
                    }
                    if("fkrq".equals(orderStr)){
                        paramMap.put("fkrqFlag","fkrqFlag");
                    }
                    if("sfcss".equals(orderStr)){
                        paramMap.put("sfcssFlag","sfcssFlag");
                    }
                    if("lrsj".equals(orderStr)){
                        paramMap.put("lrsjFlag","lrsjFlag");
                    }
                    if("fkje".equals(orderStr)){
                        paramMap.put("fkjeFlag","fkjeFlag");
                    }
                    if("clbj".equals(orderStr)){
                        paramMap.put("clbjFlag","clbjFlag");
                    }
                    if("dzje".equals(orderStr)){
                        paramMap.put("dzjeFlag","dzjeFlag");
                    }
                    if("sfdz".equals(orderStr)){
                        paramMap.put("sfdzFlag","sfdzFlag");
                    }
                    if("dcbj".equals(orderStr)){
                        paramMap.put("dcbjFlag","dcbjFlag");
                    }
                    if("dcbjlx".equals(orderStr)){
                        paramMap.put("dcbjlxFlag","dcbjlxFlag");
                    }
                    if("ztqf".equals(orderStr)){
                        paramMap.put("ztqfFlag","ztqfFlag");
                    }
                    if("sfjs".equals(orderStr)){
                        paramMap.put("sfjsFlag","sfjsFlag");
                    }
                    if("yblx".equals(orderStr)){
                        paramMap.put("yblxFlag","yblxFlag");
                    }
                }
            }
        }
        return paramMap;
    }

    /**
     * 查询所有送检信息(优化)
     *
     * @param params
     * @return
     */
    public List<Map<String, Object>> getDtoListOptimize(Map<String, Object> params) {
        int count = dao.getCountOptimize(params);
        params.put("totalNumber", count);
        List<Map<String, Object>> sjxxlist = dao.getDtoListOptimize(params);
        jcsjService.handleCodeToValue(sjxxlist, new BasicDataTypeEnum[]
                {BasicDataTypeEnum.FIRST_SJXXKZ, BasicDataTypeEnum.SECOND_SJXXKZ, BasicDataTypeEnum.THIRD_SJXXKZ, BasicDataTypeEnum.FOURTH_SJXXKZ,BasicDataTypeEnum.INSPECTION_DIVISION,BasicDataTypeEnum.SD_TYPE,BasicDataTypeEnum.APPLICATION_ITEM,BasicDataTypeEnum.DETECTION_UNIT,
                        BasicDataTypeEnum.DETECT_SUBTYPE,BasicDataTypeEnum.INVOICE_APPLICATION}, new String[]
                {"cskz1:sfkp", "cskz2:sfdz", "cskz3:cskz3", "cskz4:cskz4", "sjqf:sjqfmc", "kdlx:kdlxmc", "sqlx:sqlxmc", "jcdw:jcdwmc",
                        "jczxmid:jczxmmc","kpsq:kpsqmc"});
        return sjxxlist;
    }

    /**
     * 搜索导出条数
     * @param params
     * @return
     */
    public int getCountForSearchExp(Map<String, Object> map,Map<String, Object> params) {
        String jsid = (String) map.get("jsid");
        String yhid = (String) map.get("yhid");
        String ddid = (String) map.get("ddid");
        String wxid = (String) map.get("wxid");
        stringFormatList(map);
        List<Map<String, String>> jcdwList = sjxxService.getJsjcdwByjsid(jsid);
        int count = 0;
        if(jcdwList!=null&&jcdwList.size() > 0){
            if ("1".equals(jcdwList.get(0).get("dwxdbj"))) {
                List<String> strList = new ArrayList<>();
                List<String> userids = new ArrayList<>();
                if (StringUtil.isNotBlank(yhid)) {
                    userids.add(yhid);
                }
                if (StringUtil.isNotBlank(ddid)) {
                    userids.add(ddid);
                }
                if (StringUtil.isNotBlank(wxid)) {
                    userids.add(wxid);
                }
                if (userids.size() > 0) {
                    map.put("userids",userids);
                }
                //判断伙伴权限
                List<String> hbqxList = hbqxService.getHbidByYhid(yhid);
                if (hbqxList != null && hbqxList.size() > 0) {
                    List<String> hbmcList = sjhbxxService.getHbmcByHbid(hbqxList);
                    if (hbmcList != null && hbmcList.size() > 0) {
                        map.put("sjhbs",hbmcList);
                    }
                }
                for (int i = 0; i < jcdwList.size(); i++) {
                    if (jcdwList.get(i).get("jcdw") != null) {
                        strList.add(jcdwList.get(i).get("jcdw"));
                    }
                }
                if (strList != null && strList.size() > 0) {
                    map.put("jcdwxz",strList);
                    count = dao.getCountForSearchExp(map);
                }
            } else {
                count = dao.getCountForSearchExp(map);
            }
        }else {
            count = dao.getCountForSearchExp(map);
        }

        return count;
    }

    /**
     * 搜索导出
     * @param params
     * @return
     */
    public List<Map<String, Object>> getListForSearchExp(Map<String, Object> params) {
        Map<String, Object> map = (Map<String, Object>) params.get("entryData");
        stringFormatList(map);
        queryJoinFlagExport(params, map);
        //查询角色检测单位限定
        List<Map<String, Object>> list;
        @SuppressWarnings("unchecked")
        Map<String, String> otherPars = (Map<String, String>) params.get("otherPars");
        String jsid = otherPars.get("jsid");
        String yhid = (String) map.get("yhid");
        String ddid = (String) map.get("ddid");
        String wxid = (String) map.get("wxid");

        List<Map<String, String>> jcdwList = sjxxService.getJsjcdwByjsid(jsid);
        if(jcdwList!=null&&jcdwList.size() > 0){
            if ("1".equals(jcdwList.get(0).get("dwxdbj"))) {
                List<String> strList = new ArrayList<>();
                List<String> userids = new ArrayList<>();
                if (StringUtil.isNotBlank(yhid)) {
                    userids.add(yhid);
                }
                if (StringUtil.isNotBlank(ddid)) {
                    userids.add(ddid);
                }
                if (StringUtil.isNotBlank(wxid)) {
                    userids.add(wxid);
                }
                if (userids.size() > 0) {
                    map.put("userids",userids);
                }
                //判断伙伴权限
                List<String> hbqxList = hbqxService.getHbidByYhid(yhid);
                if (hbqxList != null && hbqxList.size() > 0) {
                    List<String> hbmcList = sjhbxxService.getHbmcByHbid(hbqxList);
                    if (hbmcList != null && hbmcList.size() > 0) {
                        map.put("sjhbs",hbmcList);
                    }
                }
                for (int i = 0; i < jcdwList.size(); i++) {
                    if (jcdwList.get(i).get("jcdw") != null) {
                        strList.add(jcdwList.get(i).get("jcdw"));
                    }
                }
                if (strList != null && strList.size() > 0) {
                    map.put("jcdwxz",strList);
                    list = dao.getListForSearchExp(map);
                } else {
                    list = new ArrayList<>();
                }
            } else {
                list = dao.getListForSearchExp(map);
            }
        }else {
            list = dao.getListForSearchExp(map);
        }

        return list;
    }

    /**
     * 选中导出
     * @param params
     * @return
     */
    public List<Map<String, Object>> getListForSelectExp(Map<String, Object> params) {
        Map<String, Object> map = (Map<String, Object>) params.get("entryData");
        stringFormatList(map);
        queryJoinFlagExport(params, map);
        if (StringUtil.isNotBlank((String) map.get("selectjson"))) {
            List<String> selectjson = (List<String>) JSONArray.parse(String.valueOf(map.get("selectjson")));
            map.put("selectjson",selectjson);
        }
        return dao.getListForSelectExp(map);
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, Map<String, Object> map) {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuffer sqlParam = new StringBuffer();
        for (DcszDto dcszDto : choseList) {
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;
            if (dcszDto.getDczd().equalsIgnoreCase("KYLX") || dcszDto.getDczd().equalsIgnoreCase("KYXM")) {
                map.put("jc_ky_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("JCDWMC")) {
                map.put("jc_jcdw_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("SWMC") || dcszDto.getDczd().equalsIgnoreCase("HBFL") || dcszDto.getDczd().equalsIgnoreCase("DQXX") || dcszDto.getDczd().equalsIgnoreCase("SF") || dcszDto.getDczd().equalsIgnoreCase("GZLX") || dcszDto.getDczd().equalsIgnoreCase("SFBZ")
                    || dcszDto.getDczd().equalsIgnoreCase("PTGSMC")|| dcszDto.getDczd().equalsIgnoreCase("TJMC")|| dcszDto.getDczd().equalsIgnoreCase("HBYH")) {
                map.put("hbxx_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("HBYH")) {
                map.put("hbyh_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("PTGSMC")) {
                map.put("ptgs_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("XSBMMC")) {
                map.put("xsbm_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("HBFL")) {
                map.put("jc_hbfl_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("DQXX")) {
                map.put("yh_dq_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("SF")) {
                map.put("jc_sf_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("YYTJMC") || dcszDto.getDczd().equalsIgnoreCase("YYMC") || dcszDto.getDczd().equalsIgnoreCase("BGXSDW") || dcszDto.getDczd().equalsIgnoreCase("YYZDDJ")) {
                map.put("yyxx_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("SFKP")) {
                map.put("jc_cskz1_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("SFDZ")) {
                map.put("jc_cskz2_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("SJKZ4")) {
                map.put("jc_cskz4_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("YBLXMC")) {
                map.put("jc_yblx_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("KSMC") || dcszDto.getDczd().equalsIgnoreCase("BGXSKS")) {
                map.put("ks_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("JCXMMC") || dcszDto.getDczd().equalsIgnoreCase("JCZXMMC") || dcszDto.getDczd().equalsIgnoreCase("SFBZ") || dcszDto.getDczd().equalsIgnoreCase("SFCSS") || dcszDto.getDczd().equalsIgnoreCase("ZLCL")) {
                map.put("jcxm_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("SQLX")) {
                map.put("jc_sqlx_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("KDLX")) {
                map.put("jc_kdlx_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("SJQF")) {
                map.put("jc_sjqf_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("FJJCLX")) {
                map.put("jc_fjsqlx_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("GZLX")) {
                map.put("jc_gzlx_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("SFBZ")) {
                map.put("sfbz_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("ZLCL")) {
                map.put("zlcl_flg", "Y");
            }
            if (dcszDto.getDczd().equalsIgnoreCase("jsje")) {
                map.put("jsje_flg", "Y");
            }
            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        map.put("sqlParam",sqlParam.toString());
    }

    private void stringFormatList(Map<String, Object> map){
        Set<String> keys = map.keySet();
        String[] listKeys = new String[]{"dwids","yblxs","kylxs","kyxms","jcxms","sj_jcxms","sjhbfls","sjhbzfls","jyjgs","fkbj","sfsfs","sjkzcs1","sfsc","sfzmjc","sjkzcs2","sjkzcs4","jcdws","sjqfs","yyzddjs","fjlxs","ptgss","xsbms"};
        List<String> gjsxKeys = Arrays.asList(listKeys);
        for (String key : keys) {
            if (gjsxKeys.contains(key)) {
                Object okey = map.get(key);
                if (okey instanceof String) {
                    String value = (String) okey;
                    value = value.replace("'", "");
                    if (StringUtil.isNotBlank(value)) {
                        String[] values = value.split(",");
                        map.put(key, values);
                    }
                }
            }
        }
    }

    /**
     * 压缩下载
     * @param request
     * @return
     */
    public void packageZipAndDownload(HttpServletRequest request,HttpServletResponse httpResponse){
        Map<String, Object> params = new HashMap<>();
        params.put("sortName","khid");
        params.put("sortOrder","asc");
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
        params.put("sortLastName","sj.jsrq");
        params.put("sortLastOrder","asc");
        String dzzq = request.getParameter("dzzq");
        String jsrq = request.getParameter("jsrq");
        String dzzqdm = request.getParameter("dzzqdm");
        String dcbj = request.getParameter("dcbj");
        String sfbc = request.getParameter("sfbc");
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
        String start="";
        String end="";
        if("NM".equals(dzzqdm)){
            //如果选择的是本年本月，并且没过20号，则取上个月
            if(year==nowYear&&month==nowMonth&&nowDay<20){
                // 上月起始
                Calendar lastMonthFirstDateCal = Calendar.getInstance();
                lastMonthFirstDateCal.add(Calendar.MONTH, -1);
                lastMonthFirstDateCal.set(Calendar.DAY_OF_MONTH, 1);
                String lastMonthFirstTime = format.format(lastMonthFirstDateCal.getTime());
                // 上月末尾
                Calendar lastMonthEndDateCal = Calendar.getInstance();
                lastMonthEndDateCal.add(Calendar.MONTH, -1);
                lastMonthEndDateCal.set(Calendar.DAY_OF_MONTH, lastMonthEndDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));
                String lastMonthEndTime = format.format(lastMonthEndDateCal.getTime());
                start=lastMonthFirstTime;
                end=lastMonthEndTime;
                params.put("jsrqstart",lastMonthFirstTime);
                params.put("jsrqend",lastMonthEndTime);
            }else{
                String dateStr = jsrq.replaceAll("-",""); // 指定年月
                LocalDate date = LocalDate.parse(dateStr + "01", DateTimeFormatter.BASIC_ISO_DATE);
                LocalDate dateFirst = date.with(TemporalAdjusters.firstDayOfMonth()); // 指定年月的第一天
                LocalDate dateEnd = date.with(TemporalAdjusters.lastDayOfMonth()); // 指定年月的最后一天
                start=dateFirst.toString();
                end=dateEnd.toString();
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
                start=monthFirstTime;
                end=monthEndTime;
                params.put("jsrqstart",monthFirstTime);
                params.put("jsrqend",monthEndTime);
            }else{
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(format.parse(jsrq+"-26"));
                    cal.add(Calendar.MONTH, -1);
                    start=format.format(cal.getTime());
                    end=jsrq+"-25";
                    params.put("jsrqstart", format.format(cal.getTime()));
                    params.put("jsrqend",jsrq+"-25");
                }catch (Exception e){
                    log.error("获取上一月月份异常：",e.getMessage());
                }
            }
        }
        String startDate=start.split("-")[0]+"年"+start.split("-")[1]+"月"+start.split("-")[2]+"日";
        String endDate=end.split("-")[0]+"年"+end.split("-")[1]+"月"+end.split("-")[2]+"日";
        List<Map<String, Object>> sjxxlist = getDtoAccountList(params);
        //将要压缩的文件塞到一个list里面
        List<Map> fileList = new ArrayList<>();
        List<Map<String, Object>> newList=new ArrayList<>();
        List<String> ids=new ArrayList<>();
        List<String> fjids=new ArrayList<>();
        if(sjxxlist!=null&&sjxxlist.size() > 0){
            Map<String,Object> map=sjxxlist.get(0);
            String fileName=jsrq+"-"+(map.get("flmc")!=null?map.get("flmc").toString():"")+"-"+map.get("gsmc").toString()+"-对账单.xls";
            for(int i=0;i<sjxxlist.size();i++){
                if("1".equals(sfbc)&&StringUtil.isNotBlank(dcbj)){
                    if(ids.size()==1000){
                        SjjcxmDto sjjcxmDto=new SjjcxmDto();
                        sjjcxmDto.setIds(ids);
                        sjjcxmDto.setDcbj(dcbj);
                        sjjcxmService.updateDcbj(sjjcxmDto);
                        ids=new ArrayList<>();
                        if(sjxxlist.get(i).get("xmglid")!=null){
                            ids.add(sjxxlist.get(i).get("xmglid").toString());
                        }
                    }else{
                        if(sjxxlist.get(i).get("xmglid")!=null){
                            ids.add(sjxxlist.get(i).get("xmglid").toString());
                        }
                    }

                    if(fjids.size()==1000){
                        FjsqDto fjsqDto=new FjsqDto();
                        fjsqDto.setIds(fjids);
                        fjsqDto.setDcbj(dcbj);
                        fjsqService.updateDcbj(fjsqDto);
                        fjids=new ArrayList<>();
                        if(sjxxlist.get(i).get("fjid")!=null){
                            fjids.add(sjxxlist.get(i).get("fjid").toString());
                        }
                    }else{
                        if(sjxxlist.get(i).get("fjid")!=null){
                            fjids.add(sjxxlist.get(i).get("fjid").toString());
                        }
                    }
                }
                if(String.valueOf(map.get("khid")).equals(String.valueOf(sjxxlist.get(i).get("khid")))){
                    newList.add(sjxxlist.get(i));
                }else{
                    fileList.add(exportExcel(fileName,newList,startDate,endDate,dzzq));
                    newList=new ArrayList<>();
                    newList.add(sjxxlist.get(i));
                    map=sjxxlist.get(i);
                    fileName=jsrq+"-"+(map.get("flmc")!=null?map.get("flmc").toString():"")+"-"+map.get("gsmc").toString()+"-对账单.xls";
                }
            }
            if("1".equals(sfbc)&&StringUtil.isNotBlank(dcbj)){
                if(ids!=null&&ids.size()>0){
                    SjjcxmDto sjjcxmDto=new SjjcxmDto();
                    sjjcxmDto.setIds(ids);
                    sjjcxmDto.setDcbj(dcbj);
                    sjjcxmService.updateDcbj(sjjcxmDto);
                }

                if(fjids!=null&&fjids.size()>0){
                    FjsqDto fjsqDto=new FjsqDto();
                    fjsqDto.setIds(fjids);
                    fjsqDto.setDcbj(dcbj);
                    fjsqService.updateDcbj(fjsqDto);
                }

            }
            fileList.add(exportExcel(fileName,newList,startDate,endDate,dzzq));
        }
        log.error("压缩包内文件个数为："+fileList.size());
        //压缩多个文件并下载
        zipFiles(fileList,httpResponse);
    }

    //生成excel表格导出到本地
    public Map exportExcel(String fileName,List<Map<String, Object>> list,String startDate,String endDate,String dzzq) {
        //对list进行优惠计算
        Map<String,Object> t_map=dealDiscount(list);
        list=(List<Map<String, Object>>)t_map.get("list");
        String yhzc_str=String.valueOf(t_map.get("yhzc_str"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HSSFWorkbook wb = null;
        Map excelOut = new HashMap();
        try {
            //获取导出对账单系统设置
            Object oncoxtsz=redisUtil.hget("matridx_xtsz","export.statement");
            String xtsz="";
            if(oncoxtsz!=null){
                JSONObject job=JSONObject.parseObject(String.valueOf(oncoxtsz));
                xtsz=job.getString("szz");
            }
            //获取对账字段
            List<JcsjDto> jcsjDtos = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.RECONCILIATION_FIELD));
            if(jcsjDtos!=null&&jcsjDtos.size()>0){
                String dzzd="";
                if(list.get(0).get("dzzd")!=null&&StringUtil.isNotBlank( list.get(0).get("dzzd").toString())){
                    String[] khdzzds = list.get(0).get("dzzd").toString().split(",");
                    for(String khdzzd:khdzzds){

                        for(JcsjDto dto:jcsjDtos){
                            if(khdzzd.equals(dto.getCsid())){
                                dzzd+=","+dto.getCsdm()+"-"+dto.getCsmc();
                                break;
                            }
                        }
                    }
                }else{
                    for(JcsjDto dto:jcsjDtos){
                        if(dzzq.equals(dto.getFcsid())&&"1".equals(dto.getCskz1())){
                            dzzd+=","+dto.getCsdm()+"-"+dto.getCsmc();
                        }
                    }
                }
                if(StringUtil.isNotBlank(dzzd)){
                    dzzd=dzzd.substring(1);
                    //创建工作sheet
                    wb = new HSSFWorkbook();
                    HSSFSheet sheet = wb.createSheet(list.get(0).get("db").toString());
                    // 合并单元格
                    CellRangeAddress region = new CellRangeAddress(0, 0, 0, dzzd.split(",").length+4);
                    sheet.addMergedRegion(region);
                    CellRangeAddress region1 = new CellRangeAddress(1, 1, 0, dzzd.split(",").length+4);
                    sheet.addMergedRegion(region1);
                    CellRangeAddress region2 = new CellRangeAddress(2, 2, 0, dzzd.split(",").length+4);
                    sheet.addMergedRegion(region2);
                    if(StringUtil.isNotBlank(yhzc_str)){
                        CellRangeAddress lastregion = new CellRangeAddress(list.size()+5, list.size()+5, 0, dzzd.split(",").length+4);
                        sheet.addMergedRegion(lastregion);
                    }
                    sheet.setDefaultColumnWidth(10);

                    HSSFCellStyle rowOneStyle = wb.createCellStyle();
                    HSSFFont rowOneFont = wb.createFont();
                    rowOneFont.setFontName("宋体"); //设置字体名称
                    rowOneFont.setFontHeightInPoints((short) 14); //设置字号
                    rowOneFont.setBold(true);
                    rowOneStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
                    rowOneStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
                    rowOneStyle.setFont(rowOneFont);

                    //第一行
                    HSSFRow row0 = sheet.createRow(0);
                    if(StringUtil.isNotBlank(xtsz)){
                        row0.createCell(0).setCellValue(xtsz.split("\\|")[0]);
                    }else{
                        row0.createCell(0).setCellValue("");
                    }
                    row0.getCell(0).setCellStyle(rowOneStyle);

                    HSSFCellStyle rowTwoStyle = wb.createCellStyle();
                    HSSFFont rowTwoFont = wb.createFont();
                    rowTwoFont.setFontName("宋体"); //设置字体名称
                    rowTwoFont.setFontHeightInPoints((short) 12); //设置字号
                    rowTwoStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
                    rowTwoStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
                    rowTwoStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());// 设置背景色
                    rowTwoStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    rowTwoStyle.setFont(rowTwoFont);
                    HSSFRow row1 = sheet.createRow(1);
                    row1.createCell(0).setCellValue("对账周期："+startDate+"-"+endDate);
                    row1.getCell(0).setCellStyle(rowTwoStyle);

                    HSSFCellStyle rowThreeStyle = wb.createCellStyle();
                    HSSFFont rowThreeFont = wb.createFont();
                    rowThreeFont.setFontName("宋体"); //设置字体名称
                    rowThreeFont.setFontHeightInPoints((short) 12); //设置字号
                    rowThreeStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
                    rowThreeStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
                    rowThreeStyle.setFont(rowThreeFont);
                    rowThreeStyle.setWrapText(true);
                    HSSFRow row2 = sheet.createRow(2);
                    row2.setHeightInPoints(78);//行高设置
                    if(StringUtil.isNotBlank(xtsz)&&(xtsz.split("\\|").length>1)){
                        row2.createCell(0).setCellValue(xtsz.split("\\|")[1]);
                    }else{
                        row2.createCell(0).setCellValue("");
                    }
                    row2.getCell(0).setCellStyle(rowThreeStyle);

                    //标题
                    HSSFCellStyle rowFourStyle = wb.createCellStyle();
                    HSSFFont rowFourFont = wb.createFont();
                    rowFourFont.setFontName("华文楷体"); //设置字体名称
                    rowFourFont.setFontHeightInPoints((short) 12); //设置字号
                    rowFourFont.setBold(true);
                    rowFourFont.setColor(IndexedColors.WHITE.getIndex());
                    rowFourStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
                    rowFourStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
                    rowFourStyle.setFont(rowFourFont);
                    rowFourStyle.setWrapText(true);
                    rowFourStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());// 设置背景色
                    rowFourStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    rowFourStyle.setBorderBottom(BorderStyle.THIN);
                    rowFourStyle.setBorderTop(BorderStyle.THIN);
                    rowFourStyle.setBorderLeft(BorderStyle.THIN);
                    rowFourStyle.setBorderRight(BorderStyle.THIN);
                    HSSFRow row3 = sheet.createRow(3);
                    row3.setHeightInPoints(78);//行高设置
                    row3.createCell(0).setCellValue("送检ID");
                    row3.getCell(0).setCellStyle(rowFourStyle);
                    sheet.setColumnHidden(0, true);
                    row3.createCell(1).setCellValue("检测项目ID");
                    row3.getCell(1).setCellStyle(rowFourStyle);
                    sheet.setColumnHidden(1, true);
                    row3.createCell(2).setCellValue("检测子项目ID");
                    row3.getCell(2).setCellStyle(rowFourStyle);
                    sheet.setColumnHidden(2, true);
                    row3.createCell(3).setCellValue("复检ID");
                    row3.getCell(3).setCellStyle(rowFourStyle);
                    sheet.setColumnHidden(3, true);
                    row3.createCell(4).setCellValue("项目管理ID");
                    row3.getCell(4).setCellStyle(rowFourStyle);
                    sheet.setColumnHidden(4, true);

                    int cellNum=5;
                    String[] dzzdSplit = dzzd.split(",");
                    for(String zd:dzzdSplit){
                        row3.createCell(cellNum).setCellValue(zd.split("-")[1]);
                        row3.getCell(cellNum).setCellStyle(rowFourStyle);
                        if("jcxmmc".equals(zd.split("-")[0])){
                            sheet.setColumnWidth(cellNum,6000);
                        }
                        cellNum++;
                    }
                    int rowNum=4;
                    double sfcssSum=0.00;
                    double sfjeSum=0.00;
                    double sfbzSum=0.00;
                    double kdfySum=0.00;
                    double ysfbzSum=0.00;
                    for(Map map:list){
                        HSSFRow row = sheet.createRow(rowNum);
                        row.createCell(0).setCellValue(map.get("sjid")!=null?map.get("sjid").toString():"");
                        row.createCell(1).setCellValue(map.get("jcxmid")!=null?map.get("jcxmid").toString():"");
                        row.createCell(2).setCellValue(map.get("jczxmid")!=null?map.get("jczxmid").toString():"");
                        row.createCell(3).setCellValue(map.get("fjid")!=null?map.get("fjid").toString():"");
                        row.createCell(4).setCellValue(map.get("xmglid")!=null?map.get("xmglid").toString():"");
                        int cell=5;
                        for(String zd:dzzdSplit){
                            if("sfsf".equals(zd.split("-")[0])){
                                row.createCell(cell).setCellValue(map.get("sfsf")!=null?(map.get("sfsf").toString().equals("1")?"是":"否"):"");
                            }else if("sfcss".equals(zd.split("-")[0])){
                                row.createCell(cell).setCellValue(map.get("sfcss")!=null?map.get("sfcss").toString():"");
                                if(map.get("sfcss")!=null&&StringUtil.isNotBlank(map.get("sfcss").toString().replaceAll("-","").replaceAll("/",""))){
                                    sfcssSum+=Double.parseDouble(map.get("sfcss").toString().replaceAll("-","").replaceAll("/",""));
                                }
                            }else if("sfje".equals(zd.split("-")[0])){
                                row.createCell(cell).setCellValue(map.get("sfje")!=null?map.get("sfje").toString():"");
                                if(map.get("sfje")!=null&&StringUtil.isNotBlank(map.get("sfje").toString().replaceAll("-","").replaceAll("/",""))){
                                    sfjeSum+=Double.parseDouble(map.get("sfje").toString().replaceAll("-","").replaceAll("/",""));
                                }
                            }else if("fkje".equals(zd.split("-")[0])){
                                row.createCell(cell).setCellValue(map.get("fkje")!=null?map.get("fkje").toString():"");
                                if(map.get("fkje")!=null&&StringUtil.isNotBlank(map.get("fkje").toString().replaceAll("-","").replaceAll("/",""))){
                                    sfbzSum+=Double.parseDouble(map.get("fkje").toString().replaceAll("-","").replaceAll("/",""));
                                }
                            }else if("yfkje".equals(zd.split("-")[0])){
                                row.createCell(cell).setCellValue(map.get("yfkje")!=null?map.get("yfkje").toString():"");
                                if(map.get("yfkje")!=null&&StringUtil.isNotBlank(map.get("yfkje").toString().replaceAll("-","").replaceAll("/",""))){
                                    ysfbzSum+=Double.parseDouble(map.get("yfkje").toString().replaceAll("-","").replaceAll("/",""));
                                }
                            }else if("kdfy".equals(zd.split("-")[0])){
                                row.createCell(cell).setCellValue(map.get("kdfy")!=null?map.get("kdfy").toString().replaceAll("-","").replaceAll("/",""):"");
                                if(map.get("kdfy")!=null&&StringUtil.isNotBlank(map.get("kdfy").toString().replaceAll("-","").replaceAll("/",""))){
                                    try {
                                        kdfySum+=Double.parseDouble(map.get("kdfy").toString().replaceAll("-","").replaceAll("/",""));
                                    }catch (Exception e){
                                        kdfySum+=Double.parseDouble("0");
                                        log.error("数字转换异常："+e.getMessage()+",替换成0");
                                    }

                                }
                            }else{
                                row.createCell(cell).setCellValue(map.get(zd.split("-")[0])!=null?map.get(zd.split("-")[0]).toString():"");
                            }
                            cell++;
                        }
                        rowNum++;
                    }

                    //优惠政策
                    if(StringUtil.isNotBlank(yhzc_str)){
                        HSSFCellStyle rowLastStyle = wb.createCellStyle();
                        HSSFFont rowLastFont = wb.createFont();
                        rowLastFont.setFontName("宋体"); //设置字体名称
                        rowLastFont.setFontHeightInPoints((short) 12); //设置字号
                        rowLastStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
                        rowLastStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
                        rowLastStyle.setFont(rowLastFont);
                        rowLastStyle.setWrapText(true);
                        HSSFRow rowLast = sheet.createRow(list.size()+5);
                        rowLast.setHeightInPoints(78);//行高设置
                        rowLast.createCell(0).setCellValue("优惠政策:"+"\r\n"+yhzc_str);
                        rowLast.getCell(0).setCellStyle(rowLastStyle);
                    }

                    //合计
                    HSSFCellStyle rowLastStyle = wb.createCellStyle();
                    HSSFFont rowLastFont = wb.createFont();
                    rowLastFont.setFontName("宋体"); //设置字体名称
                    rowLastFont.setFontHeightInPoints((short) 12); //设置字号
                    rowLastFont.setBold(true);
                    rowLastStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());// 设置背景色
                    rowLastStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    rowLastStyle.setBorderBottom(BorderStyle.THIN);
                    rowLastStyle.setBorderTop(BorderStyle.THIN);
                    rowLastStyle.setBorderLeft(BorderStyle.THIN);
                    rowLastStyle.setBorderRight(BorderStyle.THIN);
                    rowLastStyle.setFont(rowLastFont);
                    HSSFRow rowLast = sheet.createRow(list.size()+4);
                    rowLast.createCell(0).setCellValue("合计");
                    rowLast.getCell(0).setCellStyle(rowLastStyle);
                    rowLast.createCell(1).setCellStyle(rowLastStyle);
                    rowLast.createCell(2).setCellStyle(rowLastStyle);
                    rowLast.createCell(3).setCellStyle(rowLastStyle);
                    rowLast.createCell(4).setCellStyle(rowLastStyle);
                    int cellLast=5;
                    for(String zd:dzzdSplit){
                        rowLast.createCell(cellLast);
                        rowLast.getCell(cellLast).setCellStyle(rowLastStyle);
                        if("sfcss".equals(zd.split("-")[0])){
                            rowLast.getCell(cellLast).setCellValue(String.valueOf(sfcssSum));
                        }else if("sfje".equals(zd.split("-")[0])){
                            rowLast.getCell(cellLast).setCellValue(String.valueOf(sfjeSum));
                        }else if("fkje".equals(zd.split("-")[0])){
                            rowLast.getCell(cellLast).setCellValue(String.valueOf(sfbzSum));
                        }else if("yfkje".equals(zd.split("-")[0])){
                            rowLast.getCell(cellLast).setCellValue(String.valueOf(ysfbzSum));
                        }else if("kdfy".equals(zd.split("-")[0])){
                            rowLast.getCell(cellLast).setCellValue(String.valueOf(kdfySum));
                        }
                        cellLast++;
                    }
                }else{
                    log.error("文件名为 "+fileName+" 对账字段关联失败,无法导出！");
                }
            }else{
                log.error("对账字段基础数据获取失败！");
            }
            if (wb != null) {
                // 写入excel文件
                wb.write(out);
                excelOut.put("fileName",fileName);
                excelOut.put("outByte",out.toByteArray());
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("异常：" + e.toString());
        }finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return excelOut;
    }

    public Map<String,Object> dealDiscount(List<Map<String, Object>> list){
        Map<String,Object> t_map=new HashMap<>();
        String khid=String.valueOf(list.get(0).get("khid"));
        int sfcss_sum=0;//总收费测试数
        Double fkje_sum=0.00;//总收费金额
        for(Map<String,Object> map:list){
            map.put("yfkje",map.get("fkje")!=null?Double.parseDouble(String.valueOf(map.get("fkje"))):0.00);//初始化yfkje
            int sfcss=map.get("sfcss")!=null?Integer.valueOf(String.valueOf(map.get("sfcss"))):0;
            Double fkje=map.get("fkje")!=null?Double.parseDouble(String.valueOf(map.get("fkje"))):0.00;
            sfcss_sum += sfcss;
            fkje_sum+=fkje;
        }
        //1.获取项目测试数相关优惠政策
        KhyhzcbzDto khyhzcbzDto=new KhyhzcbzDto();
        khyhzcbzDto.setYhflcskz1("XMCSS");
        khyhzcbzDto.setKhid(khid);
        khyhzcbzDto.setSjlist(list);
        List<KhyhzcbzDto> xmcssDiscountList=khyhzcbzService.getYhzcByXmcss(khyhzcbzDto);
        //2.获取项目金额相关优惠政策
        khyhzcbzDto.setYhflcskz1("XMJE");
        khyhzcbzDto.setKhid(khid);
        List<KhyhzcbzDto> xmjeDiscountList=khyhzcbzService.getYhzcByXmje(khyhzcbzDto);
        //3.获取收费测试数相关优惠政策
        khyhzcbzDto.setYhflcskz1("CSS");
        khyhzcbzDto.setDcsl(String.valueOf(sfcss_sum));
        List<KhyhzcbzDto> sfcssDiscountList=khyhzcbzService.getYhzcByDcsl(khyhzcbzDto);
        //4.获取结算金额相关优惠政策
        khyhzcbzDto.setYhflcskz1("JE");
        khyhzcbzDto.setDcsl(String.valueOf(fkje_sum));
        List<KhyhzcbzDto> jsjeDiscountList=khyhzcbzService.getYhzcByDcsl(khyhzcbzDto);
        String yhzc_str="";//优惠政策说明
        List<KhyhzcbzDto> finallist= Stream.of(xmcssDiscountList,xmjeDiscountList,sfcssDiscountList,jsjeDiscountList)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(finallist)){
            for(int i=1;i<finallist.size()+1;i++){
                yhzc_str=yhzc_str+i+"."+finallist.get(i-1).getYhxm()+":"+finallist.get(i-1).getYhqsfw()+"<"+finallist.get(i-1).getYhyjmc()+"<="+finallist.get(i-1).getYhjsfw()+",优惠"+finallist.get(i-1).getYh()+("MONEY".equals(finallist.get(i-1).getYhxsdm())?"元":"折")+"\r\n";
            }
        }
        //第一步，先计算优惠金额
        khyhzcbzDto.setYhxs_flg("MONEY");
        if(!CollectionUtils.isEmpty(xmcssDiscountList)){
            //若项目测试数优惠不为空，将list进行优惠
            khyhzcbzDto.setYhflcskz1("XMCSS");
            khyhzcbzDto.setYhzclist(xmcssDiscountList);
            list=khyhzcbzService.getFinalListByXmcss(khyhzcbzDto);
        }
        if(!CollectionUtils.isEmpty(xmjeDiscountList)){
            khyhzcbzDto.setSjlist(list);//优惠叠加
            khyhzcbzDto.setYhflcskz1("XMJE");
            khyhzcbzDto.setYhzclist(xmjeDiscountList);
            list=khyhzcbzService.getFinalListByXmcss(khyhzcbzDto);
        }
        if(!CollectionUtils.isEmpty(sfcssDiscountList)){
            khyhzcbzDto.setSjlist(list);//优惠叠加
            khyhzcbzDto.setYhflcskz1("CSS");
            khyhzcbzDto.setYhzclist(sfcssDiscountList);
            list=khyhzcbzService.getFinalListByXmcss(khyhzcbzDto);
        }
        if(!CollectionUtils.isEmpty(jsjeDiscountList)){
            khyhzcbzDto.setSjlist(list);//优惠叠加
            khyhzcbzDto.setYhflcskz1("JE");
            khyhzcbzDto.setYhzclist(jsjeDiscountList);
            list=khyhzcbzService.getFinalListByXmcss(khyhzcbzDto);
        }
        if(!CollectionUtils.isEmpty(list)){//循环将优惠金额放入原结算金额
            for(Map<String,Object> khyhzcbzDto1:list){
                khyhzcbzDto1.put("yfkje",khyhzcbzDto1.get("fkje"));
            }
        }
        //第二步，计算优惠折扣
        khyhzcbzDto.setYhxs_flg("DISCOUNT");
        if(!CollectionUtils.isEmpty(xmcssDiscountList)){
            //若项目测试数优惠不为空，将list进行优惠
            khyhzcbzDto.setSjlist(list);//优惠叠加
            khyhzcbzDto.setYhflcskz1("XMCSS");
            khyhzcbzDto.setYhzclist(xmcssDiscountList);
            list=khyhzcbzService.getFinalListByXmcss(khyhzcbzDto);
        }
        if(!CollectionUtils.isEmpty(xmjeDiscountList)){
            khyhzcbzDto.setSjlist(list);//优惠叠加
            khyhzcbzDto.setYhflcskz1("XMJE");
            khyhzcbzDto.setYhzclist(xmjeDiscountList);
            list=khyhzcbzService.getFinalListByXmcss(khyhzcbzDto);
        }
        if(!CollectionUtils.isEmpty(sfcssDiscountList)){
            khyhzcbzDto.setSjlist(list);//优惠叠加
            khyhzcbzDto.setYhflcskz1("CSS");
            khyhzcbzDto.setYhzclist(sfcssDiscountList);
            list=khyhzcbzService.getFinalListByXmcss(khyhzcbzDto);
        }
        if(!CollectionUtils.isEmpty(jsjeDiscountList)){
            khyhzcbzDto.setSjlist(list);//优惠叠加
            khyhzcbzDto.setYhflcskz1("JE");
            khyhzcbzDto.setYhzclist(jsjeDiscountList);
            list=khyhzcbzService.getFinalListByXmcss(khyhzcbzDto);
        }
        t_map.put("list",list);
        t_map.put("yhzc_str",yhzc_str);
        return t_map;
    }

    //多个文件压缩成压缩包并下载
    public void zipFiles(List<Map> fileList,HttpServletResponse httpResponse) {
        try(ZipOutputStream zipOutputStream = new ZipOutputStream(httpResponse.getOutputStream()); OutputStream out =null) {
            //下载压缩包
            httpResponse.setContentType("application/zip");
            httpResponse.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("对账单.zip", "UTF-8"));
            // 创建 ZipEntry 对象
            for (Map map:fileList){
                if (StringUtil.isNotBlank((String) map.get("fileName"))){
                    ZipEntry zipEntry =  new ZipEntry((String) map.get("fileName"));
                    zipOutputStream.putNextEntry(zipEntry);
                    zipOutputStream.write((byte[]) map.get("outByte"));
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 对账单数据
     * @param params
     * @return
     */
    public List<Map<String,Object>> getDtoAccountList(Map<String, Object> params){
        return dao.getDtoAccountList(params);
    }
}

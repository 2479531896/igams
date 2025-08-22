package com.matridx.igams.bioinformation.controller;

import com.matridx.igams.bioinformation.dao.entities.*;
import com.matridx.igams.bioinformation.service.svcinterface.*;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.YyxxDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IYyxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

@Controller
@RequestMapping("/bioinfo")
public class BioinfoController extends BaseController {
    @Autowired
    private IWkcxService wkcxService;
    @Autowired
    private IWkcxbbService wkcxbbService;
    @Autowired
    private IMngsmxjgService mngsmxjgService;
    @Autowired
    private IDlfxjgService dlfxjgService;
    @Autowired
    private INyfxjgService nyfxjgService;
    @Autowired
    private IYyxxService yyxxService;
    @Autowired
    private IJcsjService jcsjService;
    @Autowired
    private IBioXpxxService xpxxService;
    @Autowired
    private IOtherService otherService;
    @Autowired
    RedisUtil redisUtil;

    private final Logger log = LoggerFactory.getLogger(BioinfoController.class);
    /**
     * 测试使用方法
     */
    @RequestMapping(value="/test/test")
    @ResponseBody
    public boolean jdtest(){
//        Map<String,Object> map = todaylist("xx","xx");
        return true;
    }

    /**
     * 查找today列表
     */
    @RequestMapping("/bioinfo/pagedataToday")
    @ResponseBody
    public Map<String, Object> pagedataToday(BioXpxxDto bioXpxxDto) {
        Map<String, Object> map;
        User user=getLoginInfo();
        map = wkcxService.getTodayList(user,bioXpxxDto);
        return map;
    }

    /**
     * 查找列表
     */
    @RequestMapping("/bioinfo/pagedataTodayByChipid")
    @ResponseBody
    public Map<String, Object> pagedataTodayByChipid(String chipid) {
        Map<String, Object> map;
//        try {
            map = wkcxService.getTodayListByxpid(chipid);
//        } catch (Exception e) {
//            map.put("status", "false");
//            map.put("message", e.getMessage());
//            log.error(e.getMessage());
//        }
        return map;
    }

    /**
     * 查找sample列表数据
     */
    @RequestMapping("/bioinfo/pagedataSample")
    @ResponseBody
    public Map<String, Object> pageListSample(WkcxDto wkcxDto) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo();
        List<Map<String,String>> list=otherService.getJsjcdwByjsid(user.getDqjs());
        List<String> jcdwList=new ArrayList<>();
        List<String> hbmcList=new ArrayList<>();
        if(list!=null&&list.size() > 0){
            if("1".equals(list.get(0).get("dwxdbj"))) {//判断当前角色是否进行了单位限制
                jcdwList= new ArrayList<>();
                for (Map<String, String> stringStringMap : list) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwList.add(stringStringMap.get("jcdw"));
                    }
                }
                //判断伙伴权限
                List<String> hbqxList = otherService.getHbidByYhid(user.getYhid());
                if(hbqxList!=null && hbqxList.size()>0) {
                    hbmcList=otherService.getHbmcByHbid(hbqxList);
                }
            }
        }

//        try {
        if (StringUtil.isNotBlank(wkcxDto.getXpm())){
            BioXpxxDto bioXpxxDto = new BioXpxxDto();
            bioXpxxDto.setXpm(wkcxDto.getXpm());
            bioXpxxDto = xpxxService.getDto(bioXpxxDto);
            if (bioXpxxDto != null && StringUtil.isNotBlank(bioXpxxDto.getXpid())){
                wkcxDto.setXpid(bioXpxxDto.getXpid());
            }
        }
        wkcxDto.setJcdws(jcdwList);
        wkcxDto.setSjhbs(hbmcList);
        List<WkcxDto> sampleList = wkcxService.getPagedWkcx(wkcxDto);
        map.put("total",wkcxDto.getTotalNumber());
        map.put("rows",sampleList);
//        } catch (Exception e) {
//            map.put("status", "false");
//            map.put("message", e.getMessage());
//            log.error(e.getMessage());
//        }
        return map;
    }

    /**
     * 查找review列表数据
     */
    @RequestMapping("/bioinfo/pagedataReview")
    @ResponseBody
    public Map<String, Object> pagedataReview(WkcxDto wkcxDto) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo();
        List<Map<String,String>> list=otherService.getJsjcdwByjsid(user.getDqjs());
        List<String> jcdwList=new ArrayList<>();
        List<String> hbmcList=new ArrayList<>();
        if(list!=null&&list.size() > 0){
            if("1".equals(list.get(0).get("dwxdbj"))) {//判断当前角色是否进行了单位限制
                jcdwList= new ArrayList<>();
                for (Map<String, String> stringStringMap : list) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwList.add(stringStringMap.get("jcdw"));
                    }
                }
                //判断伙伴权限
                List<String> hbqxList = otherService.getHbidByYhid(user.getYhid());
                if(hbqxList!=null && hbqxList.size()>0) {
                    hbmcList=otherService.getHbmcByHbid(hbqxList);
                }
            }
        }
        wkcxDto.setJcdws(jcdwList);
        wkcxDto.setSjhbs(hbmcList);
//        try {
            wkcxDto.setZt("01");
            List<WkcxDto> reviewlist = wkcxService.getPagedWkcx(wkcxDto);
            map.put("total",wkcxDto.getTotalNumber());
            map.put("rows",reviewlist);
//        } catch (Exception e) {
//            map.put("status", "false");
//            map.put("message", e.getMessage());
//            log.error(e.getMessage());
//        }
        return map;
    }

    /**
     * 查找report列表数据
     */
    @RequestMapping("/bioinfo/pagedataReport")
    @ResponseBody
    public Map<String, Object> pagedataReport(WkcxDto wkcxDto) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo();
        List<Map<String,String>> list=otherService.getJsjcdwByjsid(user.getDqjs());
        List<String> jcdwList=new ArrayList<>();
        List<String> hbmcList=new ArrayList<>();
        if(list!=null&&list.size() > 0){
            if("1".equals(list.get(0).get("dwxdbj"))) {//判断当前角色是否进行了单位限制
                jcdwList= new ArrayList<>();
                for (Map<String, String> stringStringMap : list) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwList.add(stringStringMap.get("jcdw"));
                    }
                }
                //判断伙伴权限
                List<String> hbqxList = otherService.getHbidByYhid(user.getYhid());
                if(hbqxList!=null && hbqxList.size()>0) {
                    hbmcList=otherService.getHbmcByHbid(hbqxList);
                }
            }
        }

        wkcxDto.setJcdws(jcdwList);
        wkcxDto.setSjhbs(hbmcList);
//        try {
            List<String> zts = new ArrayList<>();
            zts.add("02");//已审核
            zts.add("03");//发送报告
            wkcxDto.setZts(zts);
            List<WkcxDto> reportlist = wkcxService.getPagedWkcx(wkcxDto);
            map.put("total",wkcxDto.getTotalNumber());
            map.put("rows",reportlist);
//        } catch (Exception e) {
//            map.put("status", "false");
//            map.put("message", e.getMessage());
//            log.error(e.getMessage());
//        }
        return map;
    }

    /**
     * 查找search列表数据
     */
    @RequestMapping("/bioinfo/pagedataSearch")
    @ResponseBody
    public Map<String, Object> pagedataSearch(WkcxDto wkcxDto) {
        Map<String, Object> map = new HashMap<>();
//        try {
            //样本类型：wkcx表关联的sjxx表的yblx字段  √
            //样本ID：wkcx表的wkbh文库编号  √
            //芯片ID： wkcx表关联xpxx表的芯片ID，芯片表的芯片编号
            //送检单位：wkcx关联sjxx，sjxx的sjdw  √
            //送检伙伴：wkcx关联sjxx，sjxx的db字段  √
            //患者姓名：wkcx关联sjxx，sjxx的hzxm字段  √
            //样本状态：传入就是00,10.。。，wkcx的zt字段
            //耐药基因：wkcx关联mngs耐药分析结果，耐药分析结果的耐药基因，根据耐药分析结果里面查到的耐药基因和文库测序id去wkcx查找数据
            //关注度（gzd） ：mngs明细结果关注度
            //物种：根据输入信息wzmc去物种表取物种id即taxid，用taxid取mngs明细结果取文库编号，用文库编号去文库测序表取数据
            //涉及分表，分表规则为每个月使用一张表，表命令规则eg：igams_mngsmxjg_202208，igams_mngsmxjg_202207...
            //获取页面传入的起始时间，求起始时间内的所有年月

            //分表处理，模糊查找出mngs明细结果表名时间后缀，找出搜索时间段内的年月份，得到表名时间后缀在搜索时间段内的年月进行查找
            Map<String,List<String>> paramMap = new HashMap<>();
            List <String> tablenamelist = wkcxService.getMngsmxjgTableName();
            List<String> timelist = wkcxService.getYearMonthTime(wkcxDto);
            paramMap.put("tablenamelist",tablenamelist);
            paramMap.put("timelist",timelist);
            List<String> list = wkcxService.getTableTimeSuffix(paramMap);
//            if (StringUtil.isNotBlank(wkcxDto.getWzmc())){
//                BioWzglDto wzglDto = new BioWzglDto();
//                wzglDto.setWzmc(wkcxDto.getWzmc());
//                wzglDto = wzglService.getWzxxByMc(wzglDto);
//                if (wzglDto != null && StringUtil.isNotBlank(wzglDto.getWzid())){
//                    MngsmxjgDto mngsmxjg = new MngsmxjgDto();
//                    mngsmxjg.setTaxid(wzglDto.getWzid());
//                    mngsmxjg.setTimes(list);
//                    Set<String> wkbhs = mngsmxjgService.getWkbhByTaxid(mngsmxjg);
//                    if (wkbhs != null && wkbhs.size()>0 ){
//                        List<String> wkbhlist = new ArrayList<>(wkbhs);
//                        wkcxDto.setWkbhs(wkbhlist);
//                    }
//                }
//            }
            if (StringUtil.isNotBlank(wkcxDto.getGzd())){
                MngsmxjgDto mngsmxjgDto = new MngsmxjgDto();
                mngsmxjgDto.setTimes(list);
                if ("1".equals(wkcxDto.getGzd())){//重点关注
                    mngsmxjgDto.setAijg("1");
                }else if ("2".equals(wkcxDto.getGzd())){//默认关注
                    mngsmxjgDto.setAijg("2");
                }else if ("3".equals(wkcxDto.getGzd())){//关注
                    mngsmxjgDto.setAijg("3");
                }else if ("4".equals(wkcxDto.getGzd())){//
                    mngsmxjgDto.setAijg("4");
                }else if ("5".equals(wkcxDto.getGzd())){//
                    mngsmxjgDto.setAijg("5");
                }
                Set<String> gzdlist = mngsmxjgService.getWkbhByGzh(mngsmxjgDto);
                if (gzdlist != null && gzdlist.size()>0 ){
                    List<String> wkbhlist = new ArrayList<>(gzdlist);
                    wkcxDto.setWkbhs(wkbhlist);
                }
            }
            if (StringUtil.isNotBlank(wkcxDto.getNyjy())){
                NyfxjgDto nyfxjgDto = new NyfxjgDto();
                nyfxjgDto.setNyjy(wkcxDto.getNyjy());
                List<NyfxjgDto> nyfxjgDtoList = nyfxjgService.getListByNyjy(nyfxjgDto);
                if (nyfxjgDtoList != null && nyfxjgDtoList.size()>0){
                    wkcxDto.setNyfxjglist(nyfxjgDtoList);
                }
            }
            if (StringUtil.isNotBlank(wkcxDto.getXpm())){
                BioXpxxDto bioXpxxDto = new BioXpxxDto();
                bioXpxxDto.setXpm(wkcxDto.getXpm());
                bioXpxxDto = xpxxService.getDto(bioXpxxDto);
                if (bioXpxxDto != null && StringUtil.isNotBlank(bioXpxxDto.getXpid())){
                    wkcxDto.setXpid(bioXpxxDto.getXpid());
                }
            }
            if (StringUtil.isNotBlank(wkcxDto.getYblx())){//传入的yblx是中文名称，转为csid后在set到wkcx的yblx字段
                JcsjDto yblxJcsj = new JcsjDto();
                yblxJcsj.setJclb(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
                yblxJcsj.setCsmc(wkcxDto.getYblx());
                yblxJcsj = jcsjService.getDtoByCsmcAndJclb(yblxJcsj);
                if (StringUtil.isNotBlank(yblxJcsj.getCsid())){
                    wkcxDto.setYblx(yblxJcsj.getCsid());
                }else
                    wkcxDto.setYblx(null);
            }

            if (StringUtil.isNotBlank(wkcxDto.getDwmc())){
                YyxxDto yyxxDto = new YyxxDto();
                yyxxDto.setDwmc(wkcxDto.getDwmc());
                List<YyxxDto> yyxxDtos = yyxxService.queryByDwmc(yyxxDto);
                if (yyxxDtos!=null &&yyxxDtos.size()>0){
                    wkcxDto.setDwid(yyxxDtos.get(0).getDwid());
                }
            }
        User user=getLoginInfo();
        List<Map<String,String>> dwlist=otherService.getJsjcdwByjsid(user.getDqjs());
        List<String> jcdwList=new ArrayList<>();
        List<String> hbmcList=new ArrayList<>();
        if(dwlist!=null&&dwlist.size() > 0){
            if("1".equals(dwlist.get(0).get("dwxdbj"))) {//判断当前角色是否进行了单位限制
                jcdwList= new ArrayList<>();
                for (Map<String, String> stringStringMap : dwlist) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwList.add(stringStringMap.get("jcdw"));
                    }
                }
                //判断伙伴权限
                List<String> hbqxList = otherService.getHbidByYhid(user.getYhid());
                if(hbqxList!=null && hbqxList.size()>0) {
                    hbmcList=otherService.getHbmcByHbid(hbqxList);
                }
            }
        }


        wkcxDto.setJcdws(jcdwList);
        wkcxDto.setSjhbs(hbmcList);
        List<JcsjDto>syslist=jcsjService.getDtoListbyJclb(BasicDataTypeEnum.DETECTION_UNIT);
        List<WkcxDto> sampleList = wkcxService.getPagedWkcx(wkcxDto);
            map.put("total",wkcxDto.getTotalNumber());
            map.put("rows",sampleList);
            map.put("syslist",syslist);
//        } catch (Exception e) {
//            map.put("status", "false");
//            map.put("message", e.getMessage());
//            log.error(e.getMessage());
//        }
        return map;
    }

    //mtresult
    @RequestMapping("/bioinfo/pagedataMtresult")
    @ResponseBody
    public boolean mtresult() {
        boolean res = true;
        try {
            File file = new File("E:\\桌面乱七八糟文件\\整理\\生信审核\\生信提供的文件\\mtresult.json");
            String jsonString = FileUtils.readFileToString(file, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonString);
            List<MngsmxjgDto> mngsmxjgList = new ArrayList<>();
            //获取所有的key
            Iterator<?> keys = jsonObject.keys();
            while(keys.hasNext()){
                String key=keys.next().toString();
                MngsmxjgDto mngsmxjgDto = new MngsmxjgDto();
                mngsmxjgDto.setTaxid(key);
                mngsmxjgDto.setJtxlxx(   String.valueOf(jsonObject.get(key))  );
                mngsmxjgList.add(mngsmxjgDto);
            }
            if(mngsmxjgList.size()>0){
                mngsmxjgService.batchUpdateLs(mngsmxjgList);
            }
        } catch (Exception e){
            log.error(e.getMessage());
        }
        return res;
    }

    //毒力报告结果
    @RequestMapping("/bioinfo/pagedataDlbgjg")
    @ResponseBody
    public boolean dlbgjg(String bbh,String wkbh) {
        boolean res = true;
        User user = getLoginInfo();
        try {
            FileReader fr=new FileReader("E:\\桌面乱七八糟文件\\整理\\生信审核\\生信提供的文件\\毒力报告结果");
            BufferedReader br=new BufferedReader(fr);
            String line;
            String[] arrs;
            List<DlfxjgDto> dlfxjglist = new ArrayList<>();
            while ((line=br.readLine())!=null) {
                if (line.contains("毒力因子") && line.contains("毒力相关类别") && line.contains("毒力基因") && line.contains("比对reads数") && line.contains("VFDB参考物种")){
                    continue;
                }
                arrs=line.replace(" ","").split("\t");
//            System.out.println(arrs[0] + " : " + arrs[1] + " : " + arrs[2]);
                List<String> list= Arrays.asList(arrs);
                JSONObject jsonObject = new JSONObject();
                if (list.size()>0){
                    jsonObject.put("dlyz",list.get(0));
                    jsonObject.put("dlxglb",list.get(1));
                    jsonObject.put("dljy",list.get(2));
                    jsonObject.put("dbreads",list.get(3));
                    jsonObject.put("vfdb",list.get(4));
                }
                WkcxbbDto wkcxbbDto = new WkcxbbDto();
                wkcxbbDto.setBbh(bbh);
                wkcxbbDto.setWkbh(wkbh);
                wkcxbbDto = wkcxbbService.getDtoByBbhAndWkbh(wkcxbbDto);
                DlfxjgDto dlfxjgDto = new DlfxjgDto();
                dlfxjgDto.setDlfxid(StringUtil.generateUUID());
                dlfxjgDto.setBbh(bbh);
                dlfxjgDto.setSfbg("1");
                dlfxjgDto.setNr(String.valueOf(jsonObject));
                if (wkcxbbDto != null && StringUtil.isNotBlank(wkcxbbDto.getWkcxid())){
                    dlfxjgDto.setWkcxid(wkcxbbDto.getWkcxid());
                }
                dlfxjgDto.setLrry(user.getYhid());
                dlfxjglist.add(dlfxjgDto);
            }
            if(dlfxjglist.size()>0){
                dlfxjgService.insertList(dlfxjglist);
            }
            br.close();
            fr.close();
        } catch (Exception e){
            log.error(e.getMessage());
        }
        return res;
    }

    //耐药检测结果统计
    @RequestMapping("/bioinfo/pagedataNyjcjgtj")
    @ResponseBody
    public boolean nyjcjgtj(String bbh,String wkbh) {
        boolean res = true;
        User user = getLoginInfo();
        try {
            FileReader fr = new FileReader("E:\\桌面乱七八糟文件\\整理\\生信审核\\生信提供的文件\\耐药检测结果统计");
            BufferedReader br=new BufferedReader(fr);
            String line;
            String[] arrs;
            List<NyfxjgDto> nyfxjgDtoList = new ArrayList<>();
            Map<String,Integer> resmap = new HashMap<>();
            //耐药类别	耐药家族	重点耐药基因	top5基因	top5基因的检出信息	参考物种	参考物种TaxID	耐药机制
            while ((line=br.readLine())!=null) {
                if (  line.contains("耐药类别") && line.contains("耐药家族") && line.contains("重点耐药基因") && line.contains("top5基因") && line.contains("top5基因的检出信息")
                      && line.contains("参考物种")  && line.contains("参考物种TaxID")  && line.contains("耐药机制") ){
                    continue;
                }
                arrs=line.replace(" ","").split("\t");
                List<String> list= Arrays.asList(arrs);
                JSONObject jsonObject = new JSONObject();

                WkcxbbDto wkcxbbDto = new WkcxbbDto();
                wkcxbbDto.setBbh(bbh);
                wkcxbbDto.setWkbh(wkbh);
                wkcxbbDto = wkcxbbService.getDtoByBbhAndWkbh(wkcxbbDto);
                NyfxjgDto nyfxjgDto = new NyfxjgDto();
                nyfxjgDto.setNyjgid(StringUtil.generateUUID());
                if (wkcxbbDto != null && StringUtil.isNotBlank(wkcxbbDto.getWkcxid())){
                    nyfxjgDto.setWkcxid(wkcxbbDto.getWkcxid());
                }
                if (list.size()>0){
                    jsonObject.put("nylb",list.get(0));
                    jsonObject.put("nyjzu",list.get(1));
                    jsonObject.put("zdnyjy",list.get(2));
                    jsonObject.put("topjy",list.get(3));
                    jsonObject.put("topjydjcxx",list.get(4));
                    jsonObject.put("ckwz",list.get(5));
                    jsonObject.put("ckwztaxid",list.get(6));
                    jsonObject.put("nyjz",list.get(7));
                    nyfxjgDto.setNylb(list.get(0));
                    nyfxjgDto.setNyjzu(list.get(1));
                }
                nyfxjgDto.setNr(String.valueOf(jsonObject));
                nyfxjgDto.setBbh(bbh);
                nyfxjgDto.setSfbg("1");
                nyfxjgDto.setNyjy(String.valueOf(jsonObject.get("topjy")));
                nyfxjgDto.setLrry(user.getYhid());
                if (resmap.containsKey((nyfxjgDto.getNyjzu()))){
                    //map存在该值，即存在重复耐药家族数据，处理以后在放入list中
                    for (int i=0; i<nyfxjgDtoList.size(); i++) {
                        NyfxjgDto dto = nyfxjgDtoList.get(i);
                        if (nyfxjgDto.getNyjzu().equals(dto.getNyjzu())){
                            String nylb = nyfxjgDto.getNylb()+";"+dto.getNylb();
                            nyfxjgDto.setNylb(nylb);
                            JSONObject object = new JSONObject(dto.getNr());
                            object.put("nylb",nylb);
                            nyfxjgDto.setNr(String.valueOf(object));
                            nyfxjgDtoList.set(i,nyfxjgDto);
                        }
                    }
                }else {
                    resmap.put(nyfxjgDto.getNyjzu(),1);
                    nyfxjgDtoList.add(nyfxjgDto);
                }
            }
            if (nyfxjgDtoList.size()>0){
                nyfxjgService.insertList(nyfxjgDtoList);
            }
            br.close();
            fr.close();
        } catch (Exception e){
            log.error(e.getMessage());
        }
        return res;
    }

    //结果ai，out
    @RequestMapping("/bioinfo/pagedataOut")
    @ResponseBody
    public boolean out() {
        boolean res = true;
        try {
            FileReader fr = new FileReader("E:\\桌面乱七八糟文件\\整理\\生信审核\\生信提供的文件\\MDL001-13C2225013N-DNA-C37-20220613.out");
            BufferedReader br=new BufferedReader(fr);
            String line;
            String[] arrs;
            List<MngsmxjgDto> mngsmxjgDtoList = new ArrayList<>();
            //耐药类别	耐药家族	重点耐药基因	top5基因	top5基因的检出信息	参考物种	参考物种TaxID	耐药机制
            while ((line=br.readLine())!=null) {
                if (  line.contains("Sample") && line.contains("Taxid") && line.contains("Decision") && line.contains("Type") && line.contains("Library")
                        && line.contains("reads_count")  && line.contains("rab")  && line.contains("rpm") ){
                    continue;
                }
                arrs=line.replace(" ","").split("\t");
                List<String> list= Arrays.asList(arrs);

                //Sample	Taxid	Decision	Type	Library	reads_count	rab	rpm	Srpm	Qindex	Hindex	Bmp	Tax_rank
                // Chip_rank	History_rank	Score	Cla
                MngsmxjgDto mngsmxjgDto = new MngsmxjgDto();
                if (list.size()>0){
                    mngsmxjgDto.setTaxid(list.get(1));
                    if (StringUtil.isNotBlank(list.get(2))){
                        if ("Hign".equals(list.get(2))){
                            mngsmxjgDto.setAijg("1");
                            mngsmxjgDto.setGzd("1");
                        }else if ("background".equals(list.get(2))){
                            mngsmxjgDto.setAijg("2");
                            mngsmxjgDto.setGzd("2");
                        }else if ("Low".equals(list.get(2))){
                            mngsmxjgDto.setAijg("3");
                            mngsmxjgDto.setGzd("3");
                        }else {
                            mngsmxjgDto.setAijg("5");
                            mngsmxjgDto.setGzd("5");
                        }
                    }else {
                        mngsmxjgDto.setAijg("5");
                        mngsmxjgDto.setGzd("5");
                    }
//                    mngsmxjgDto.setGzd(list.get(2));
                }
                mngsmxjgDtoList.add(mngsmxjgDto);
            }
            if(mngsmxjgDtoList.size()>0){
                mngsmxjgService.batchUpdateLs(mngsmxjgDtoList);
            }
            //修改临时表中gzd字段值为2（代表不关注），且fl为0核心库的则将gzd改为4疑似
            //mngsmxjgService.updateGzdToFour();
            br.close();
            fr.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    //coverage
    @RequestMapping("/bioinfo/pagedataCoverage")
    @ResponseBody
    public boolean coverage(String bbh, String wkbh) {
        boolean res = true;
        try {
            File file = new File("E:\\桌面乱七八糟文件\\整理\\生信审核\\生信提供的文件\\coverage.json");
            String jsonString = FileUtils.readFileToString(file, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonString);
            List<MngsmxjgDto> mngsmxjgList = new ArrayList<>();
            //获取所有的key
            Iterator<?> keys = jsonObject.keys();
            while(keys.hasNext()){
                String key=keys.next().toString();
                MngsmxjgDto mngsmxjgDto = new MngsmxjgDto();
                mngsmxjgDto.setTaxid(key);
                mngsmxjgDto.setFgdxx(   String.valueOf(jsonObject.get(key))  );
                mngsmxjgList.add(mngsmxjgDto);
            }
            if(mngsmxjgList.size()>0){
                mngsmxjgService.batchUpdateLs(mngsmxjgList);
            }
            //查询igams_wkjgmx_ls的结果集添加到igams_wkjgmx中，然后情况igams_wkjgmx_ls表
            MngsmxjgDto mngsmxjgDto = new MngsmxjgDto();
            mngsmxjgDto.setBbh(bbh);
            mngsmxjgDto.setWkbh(wkbh);
            List<MngsmxjgDto> mngsmxjgDtoList = mngsmxjgService.getLsAll(mngsmxjgDto);
            mngsmxjgService.insertLsAllToWkjgmx(mngsmxjgDtoList);//将临时表的数据插入到文库结果明细表
            mngsmxjgService.delLsAll(mngsmxjgDto);//删除临时表的所有数据

        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    /**
     * stats质控统计页面
     */
    @RequestMapping("/stats/pagedataQcStatistics")
    @ResponseBody
    public Map<String,Object> qcStatistics(HttpServletRequest req, WkcxDto wkcxDto) {
        String method = req.getParameter("method");
        Map<String,Object> map = new HashMap<>();
        if("getSequenceByTime".equals(method)){
            //stats第三页质控统计的测序数量
            //starttime endtime，sjxx录入开始和结束时间
            //测序数据量，一段时间中每天文库测序的totalreads数据
            List<Map<String,String>> mapList = wkcxService.getSequenceByTime(wkcxDto);
            map.put("tjdata",mapList);//测序数据量
        }else if ("getRatioOfSampleType".equals(method)){
            //stats报告统计页面饼状图右 统计报告中每种样本类型占比
            List<Map<String,String>> mapList = wkcxService.getRatioOfSampleType(wkcxDto);
            map.put("tjdata",mapList);
        }else if ("getYblxGzd".equals(method)){
            //stats报告统计页面柱状图 统计报告中每种样本类型中四种关注度占比
            List<Map<String,String>> mapList = wkcxService.getYblxGzd(wkcxDto);
            map.put("tjdata",mapList);//测序数据量
        }

        return map;
    }

    /**
     * stats报告统计页面--- 报告统计右，统计报告中每种样本类型占比
     */
    @RequestMapping("/stats/pagedataReportStatistics")
    @ResponseBody
    public Map<String,Object> reportStatistics(WkcxDto wkcxDto) {
        Map<String,Object> map = new HashMap<>();
        List<Map<String,String>> mapList = wkcxService.getRatioOfSampleType(wkcxDto);
        map.put("data",mapList);//测序数据量
        return map;
    }

    /**
     * 查找实验室列表数据
     */
    @RequestMapping("/bioinfo/pagedataLab")
    @ResponseBody
    public Map<String, Object> pagedataLab(WkcxDto wkcxDto) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo();
        List<Map<String,String>> list=otherService.getJsjcdwByjsid(user.getDqjs());
        List<String> jcdwList=new ArrayList<>();
        if(list!=null&&list.size() > 0){
            if("1".equals(list.get(0).get("dwxdbj"))) {//判断当前角色是否进行了单位限制
                jcdwList= new ArrayList<>();
                for (Map<String, String> stringStringMap : list) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwList.add(stringStringMap.get("jcdw"));
                    }
                }
            }
        }

        wkcxDto.setJcdws(jcdwList);
        List<WkcxDto> wkcxDtos = wkcxService.getLabList(wkcxDto);
        List<JcsjDto> jcdws = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
        List<JcsjDto> sjqfs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
        map.put("jcdws",jcdws);
        map.put("sjqfs",sjqfs);
        map.put("total",wkcxDtos.size());
        map.put("rows",wkcxDtos);
        return map;
    }

    /**
     * 实验室列表  查看
     */
    @RequestMapping("/bioinfo/pagedataViewLab")
    @ResponseBody
    public Map<String, Object> pagedataViewLab(WkcxDto wkcxDto) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo();
        List<Map<String,String>> list=otherService.getJsjcdwByjsid(user.getDqjs());
        List<String> jcdwList=new ArrayList<>();
        List<String> hbmcList=new ArrayList<>();
        if(list!=null&&list.size() > 0){
            if("1".equals(list.get(0).get("dwxdbj"))) {//判断当前角色是否进行了单位限制
                jcdwList= new ArrayList<>();
                for (Map<String, String> stringStringMap : list) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwList.add(stringStringMap.get("jcdw"));
                    }
                }
                //判断伙伴权限
                List<String> hbqxList = otherService.getHbidByYhid(user.getYhid());
                if(hbqxList!=null && hbqxList.size()>0) {
                    hbmcList=otherService.getHbmcByHbid(hbqxList);
                }
            }
        }

        wkcxDto.setJcdws(jcdwList);
        wkcxDto.setSjhbs(hbmcList);
        List<WkcxDto> wkcxDtos = wkcxService.getDtoList(wkcxDto);
        map.put("rows",wkcxDtos);
        return map;
    }

}

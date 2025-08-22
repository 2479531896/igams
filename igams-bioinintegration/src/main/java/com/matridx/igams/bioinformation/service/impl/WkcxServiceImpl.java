package com.matridx.igams.bioinformation.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.bioinformation.dao.entities.BioXpxxDto;
import com.matridx.igams.bioinformation.dao.entities.WkcxDto;
import com.matridx.igams.bioinformation.dao.entities.WkcxModel;
import com.matridx.igams.bioinformation.dao.post.IMngsmxjgDao;
import com.matridx.igams.bioinformation.dao.post.IWkcxDao;
import com.matridx.igams.bioinformation.dao.post.IWkcxbbDao;
import com.matridx.igams.bioinformation.service.svcinterface.IBioXpxxService;
import com.matridx.igams.bioinformation.service.svcinterface.IOtherService;
import com.matridx.igams.bioinformation.service.svcinterface.IWkcxService;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WkcxServiceImpl extends BaseBasicServiceImpl<WkcxDto, WkcxModel, IWkcxDao> implements IWkcxService {
    private final Logger log = LoggerFactory.getLogger(WkcxServiceImpl.class);

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IBioXpxxService xpxxService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IWkcxbbDao wkcxbbDao;
    @Autowired
    IMngsmxjgDao mngsmxjgDao;
    @Autowired
    private IOtherService otherService;

    @Override
    public WkcxDto getDtoNew(WkcxDto wkcxDto) {
        return dao.getDtoNew(wkcxDto);
    }

    @Override
    public WkcxDto getListByNbbm(WkcxDto wkcxDto) {
        return dao.getListByNbbm(wkcxDto);
    }

    @Override
    public List<WkcxDto> getByNbbmToList(WkcxDto wkcxDto) {
        return dao.getByNbbmToList(wkcxDto);
    }


    @Override
    public WkcxDto getXpmBywkhb(WkcxDto wkcxDto) {
        return dao.getXpmBywkhb(wkcxDto);
    }

    /**
     * 获取today的列表数据
     */
    @Override
    public Map<String, Object> getTodayList(User user,BioXpxxDto xp) {
        Map<String, Object> resmap = new HashMap<>();
        try {
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

            //首先找出昨天16点后，今天16点前的测序数据
            //查找到所有的实验室和测序仪返回给前端
            WkcxDto wkcxDto = new WkcxDto();
            SimpleDateFormat dayDeal = new SimpleDateFormat("yyyy-MM-dd");
            wkcxDto.setCxkssjstart(dayDeal.format(DateUtils.getDate(new Date(), -1)) + " 16:00:00");
            wkcxDto.setCxkssjend(dayDeal.format(DateUtils.getDate(new Date(), 0)) + " 16:00:00");
            wkcxDto.setJcdws(jcdwList);//查询时加入检测单位限制
            List<Map<String, String>> cyxidsets = dao.getCxyInfo(wkcxDto);//取昨天16点后，今天16点前的实验室和对应的芯片信息的信息
            //用代码将实验室和芯片封装好，并将第一个芯片信息的数据封装给前端
            List<String> sysList = new ArrayList<>();//用于判断实验室是否重复
            JSONArray jsonArray = new JSONArray();
            Map<String, List<JSONObject>> sysMap = new HashMap<>();
            if (cyxidsets != null && cyxidsets.size() > 0) {
                for (Map<String, String> map : cyxidsets) {
                    String xpm = map.get("xpm");//芯片名
                    String xpid = map.get("xpid");//芯片id
                    String sysid = map.get("sysid");//实验室csid
                    String sysmc = map.get("sysmc");//实验室名称
                    if (StringUtil.isNotBlank(xpid) && StringUtil.isNotBlank(sysid)) {
                        List<JSONObject> xpList = new ArrayList<>();
                        JSONObject js = new JSONObject();
                        js.put("chipname", xpm);
                        js.put("chipid", xpid);
                        if (!sysList.contains(sysid)) {//代表是新的实验室
                            sysList.add(sysid);
                            xpList.add(js);
                            sysMap.put(sysmc, xpList);
                        } else {
                            xpList = sysMap.get(sysmc);
                            xpList.add(js);
                        }

                    }
                }
                //将芯片实验室封装好
                //定义一个参数，记录第一个芯片好默认查出来

                List<String> ids=new ArrayList<>();
                for (String sysmc : sysMap.keySet()) {
                    JSONObject sysjs = new JSONObject();
                    sysjs.put("sysmc", sysmc);
                    List<JSONObject> cplist = sysMap.get(sysmc);
                    for(JSONObject cpJson:cplist){
                        String xpid = cpJson.get("chipid").toString();
                        ids.add(xpid);
                    }
                }
                xp.setIds(ids);
                xp.setJcdws(jcdwList);
                xp.setSjhbs(hbmcList);
                List<WkcxDto> wkcxByXpids = dao.getWkcxByXpids(xp);
                for (String sysmc : sysMap.keySet()) {
                    List<String> xpids=new ArrayList<>();
                    JSONObject sysjs = new JSONObject();
                    sysjs.put("sysmc", sysmc);
                    List<JSONObject> cplist = sysMap.get(sysmc);
                    for(JSONObject cpJson:cplist){
                        String xpid = cpJson.get("chipid").toString();
                        xpids.add(xpid);
                    }
                    List<WkcxDto> wkcxDtos=new ArrayList<>();
                    for(WkcxDto wkcxDto_t:wkcxByXpids){
                        if(xpids.contains(wkcxDto_t.getXpid())){
                            wkcxDtos.add(wkcxDto_t);
                        }
                    }
                    sysjs.put("chips", cplist);
                    sysjs.put("wkcx", wkcxDtos);
                    jsonArray.add(sysjs);
                }
            }
            resmap.put("syslist", jsonArray);
            resmap.put("status", "success");
            resmap.put("message", "查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resmap.put("status", "fail");
            resmap.put("message", e.getMessage());
        }
        log.info(resmap.toString());
        return resmap;
    }

    /**
     * 通过芯片id获取列表
     */
    @Override
    public Map<String, Object> getTodayListByxpid(String chipid) {
        Map<String, Object> resmap = new HashMap<>();
        try {
            BioXpxxDto xp = new BioXpxxDto();
            xp.setXpid(chipid);
            List<WkcxDto> wkcxDtos = dao.getWkcxByXpid(xp);//根据芯片找到，每个芯片下的测序数据
            resmap.put("wkcx", wkcxDtos);
            resmap.put("status", "success");
            resmap.put("message", "查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resmap.put("status", "fail");
            resmap.put("message", e.getMessage());
        }
        return resmap;
    }

    /**
     * 获取sample的列表数据
     */
    @Override
    public List<WkcxDto> getPagedWkcx(WkcxDto wkcxDto) {
        return dao.getPagedWkcx(wkcxDto);
    }

    /**
     * 获取sample的列表数据
     */
    @Override
    public List<WkcxDto> getpageWkcx(WkcxDto wkcxDto) {
        return dao.getpageWkcx(wkcxDto);
    }

    @Override
    public List<WkcxDto> libraryInfo(WkcxDto wkcxDto){
        List<String> strings = dao.getTableInfo(wkcxDto);
        wkcxDto.setLrsjs(strings);
        return  dao.getLibraryInfo(wkcxDto);
    }

    @Override
    public Map<String, Object> getQuantityStatistics(WkcxDto wkcxDto) {
        //Map<String,Object>map=new HashMap<>();
        Map<String,Object>paramObjec=new HashMap<>();
        paramObjec.put("starttime",wkcxDto.getStartTime());
        paramObjec.put("endtime",wkcxDto.getEndTime());
//        Map<String,Object>map1=dao.getSampleAndLibraryBytime(paramObjec);
//        Map<String,Object>map2=dao.getClinicalSampleBytime(paramObjec);
//        Map<String,Object>map3=dao.getDevelopmentSampleBytime(paramObjec);
        return null;
    }
    /**
     * 报告导出
     */
    @Override
    public WkcxDto getExport(WkcxDto wkcxDto){
        wkcxDto= dao.getExport(wkcxDto);
        if(("1").equals(wkcxDto.getQxhbbg())){
            wkcxDto.setDetection_type("C");
        }
        if(StringUtil.isNoneBlank(wkcxDto.getSample_id())){
            wkcxDto.setSample_id(wkcxDto.getWkbh()+"+"+wkcxDto.getSample_id());
            wkcxDto.setDetection_type("C");
        }
        else if(!StringUtil.isNoneBlank(wkcxDto.getSample_id())){
            wkcxDto.setSample_id(wkcxDto.getWkbh());
        }
        return wkcxDto;
    }

    /**
     * 一段时间中每天文库测序的totalreads数据
     */
    @Override
    public List<Map<String, String>> getSequenceByTime(WkcxDto wkcxDto) {
        return dao.getSequenceByTime(wkcxDto);
    }

    /**
     * stats报告统计页面--- 报告统计右，统计报告中每种样本类型占比
     */
    @Override
    public List<Map<String, String>> getRatioOfSampleType(WkcxDto wkcxDto) {
        return dao.getRatioOfSampleType(wkcxDto);
    }

    /**
     * stats报告统计页面--- 柱状图，统计报告中每种样本类型中四周关注度类型占比
     */
    @Override
    public List<Map<String, String>> getYblxGzd(WkcxDto wkcxDto) {
        //查找出某时间段的文库测序中含有的样本类型
        //starttime endtime，sjxx录入开始和结束时间
        List<String> yblxList = dao.getYblxType(wkcxDto);
        if (yblxList!=null && yblxList.size()>0){
            wkcxDto.setYblxs(yblxList);
        }
//        List<Map<String,String>> list = dao.getYblxGzd(wkcxDto);
        return null;
    }

    /**
     * 获取某段时间内的年月份
     */
    @Override
    public List<String> getYearMonthTime(WkcxDto wkcxDto) {
        return dao.getYearMonthTime(wkcxDto) ;
    }

    /**
     * 糊查找查mngs明细结果表名
     */
    @Override
    public List<String> getMngsmxjgTableName() {
        return dao.getMngsmxjgTableName();
    }

    /**
     * 获取传入时间范围内需要查找的mngs明细表名时间后缀
     */
    @Override
    public List<String> getTableTimeSuffix(Map<String, List<String>> paramMap) {
        return dao.getTableTimeSuffix(paramMap) ;
    }

    /**
     * 保存合并操作
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateDto(WkcxDto wkcxDto){
        int update = dao.update(wkcxDto);
        return update != 0;
    }

    /**
     * 根据ids查找列表数据
     */
    public List<WkcxDto> getListByIds(WkcxDto wkcxDto){
        return dao.getListByIds(wkcxDto);
    }

    /**
     * 获取分析版本信息
     */
    public List<WkcxDto> getAnalysisParams(){
        return dao.getAnalysisParams();
    }


    public  void syncSampleData(){
        try{
            List<WkcxDto> analysisParams = dao.getAnalysisParams();
            for(int i=1;i<=443;i++){
                WkcxDto wkcxDto=new WkcxDto();
                wkcxDto.setPageSize(500);
                wkcxDto.setPageNumber(i);
                wkcxDto.setPageStart((i-1)*100);
                List<WkcxDto> dtoList = dao.syncData(wkcxDto);
                for(WkcxDto dto:dtoList){
                    if(StringUtil.isNotBlank(dto.getZt())){
                        if("分析完成".equals(dto.getZt())){
                            dto.setZt("01");
                        }else  if("审核完成".equals(dto.getZt())){
                            dto.setZt("02");
                        }else  if("自动复检".equals(dto.getZt())){
                            dto.setZt("04");
                        }else{
                            dto.setZt(null);
                        }
                    }

                    if(StringUtil.isNotBlank(dto.getFxbb())){
                        JSONObject jsonObject = JSONObject.parseObject(dto.getFxbb());
                        JSONObject dateJson = JSONObject.parseObject(dto.getVersion_dates());
                        if("0".equals(dto.getShbbbh())){
                            dto.setShbbbh(null);
                        }else {
                            String as = dto.getShbbbh();
                            String str = jsonObject.getString(as);
                            String date = dateJson.getString(as);
                            if(StringUtil.isNotBlank(str)&&StringUtil.isNotBlank(date)){
                                for (WkcxDto analysis : analysisParams) {
                                    if (str.equals(analysis.getParam_md5())) {
                                        dto.setShbbbh("V" + as + ":" + analysis.getVersion() + "-" + date);
                                    }
                                }
                            }
                        }
                        if (!"0".equals(dto.getLast_result_version())) {
                            String as = dto.getLast_result_version();
                            String str = jsonObject.getString(as);
                            String date = dateJson.getString(as);
                            if(StringUtil.isNotBlank(str)&&StringUtil.isNotBlank(date)){
                                for (WkcxDto analysis : analysisParams) {
                                    if (str.equals(analysis.getParam_md5())) {
                                        dto.setZxbb("V" + as + ":" + analysis.getVersion() + "-" + date);
                                    }
                                }
                            }
                        }
                    }

                    if(StringUtil.isNotBlank(dto.getSffsbg())){
                        if("t".equals(dto.getSffsbg())){
                            dto.setSffsbg("1");
                        }else  if("f".equals(dto.getSffsbg())){
                            dto.setSffsbg("0");
                        }
                    }

                    if(StringUtil.isNotBlank(dto.getQxhbbg())){
                        if("t".equals(dto.getQxhbbg())){
                            dto.setQxhbbg("1");
                        }else  if("f".equals(dto.getQxhbbg())){
                            dto.setQxhbbg("0");
                        }
                    }

                    if(StringUtil.isNotBlank(dto.getFcjc())){
                        if("复测".equals(dto.getFcjc())){
                            dto.setFcjc("1");
                        }else  if("加测".equals(dto.getFcjc())){
                            dto.setFcjc("2");
                        }
                    }

                    if(StringUtil.isNotBlank(dto.getSfxybg())){
                        if("t".equals(dto.getSfxybg())){
                            dto.setSfxybg("1");
                        }else  if("f".equals(dto.getSfxybg())){
                            dto.setSfxybg("0");
                        }
                    }

                    if(StringUtil.isNotBlank(dto.getWkbh())){
                        if(dto.getWkbh().contains("RNA")){
                            dto.setWklx("RNA");
                        }
                        if(dto.getWkbh().contains("DNA")){
                            dto.setWklx("DNA");
                        }
                    }

                    if(StringUtil.isNotBlank(dto.getSjq20())&&StringUtil.isNotBlank(dto.getCleanq20())){
                        if(dto.getCleanq20().equals(dto.getSjq20())){
                            if(StringUtil.isNotBlank(dto.getWkbh())){
                                if(dto.getWkbh().contains("RNA")){
                                    if(StringUtil.isNotBlank(dto.getDna_lib_id())){
                                        dto.setZklx("RNA");
                                        dto.setHbwkcxid(dto.getDna_lib_id());
                                    }
                                }
                                if(dto.getWkbh().contains("DNA")){
                                    if(StringUtil.isNotBlank(dto.getRna_lib_id())){
                                        dto.setZklx("DNA");
                                        dto.setHbwkcxid(dto.getRna_lib_id());
                                    }
                                }
                            }
                        }
                    }

                    if(StringUtil.isNotBlank(dto.getQc())){
                        JSONObject object = JSONObject.parseObject(dto.getQc());
                        if(object.size()>0){
                            if(object.get("adapter")!=null){
                                dto.setAdapter(object.get("adapter").toString());
                            }
                            if(object.get("clean")!=null){
                                dto.setClean(object.get("clean").toString());
                            }
                            if(object.get("dilution_factor")!=null){
                                dto.setDilutionf(object.get("dilution_factor").toString());
                            }
                            if(object.get("con.lib")!=null){
                                dto.setLibccon(object.get("con.lib").toString());
                            }
                            if(object.get("con.dna")!=null){
                                dto.setNuccon(object.get("con.dna").toString());
                            }
                            BigDecimal hu = new BigDecimal(100);
                            if(object.get("cleanreads")!=null&&object.get("clean")!=null&&object.get("barcode_all_match")!=null){
                                if(new BigDecimal(object.get("clean").toString()).compareTo(BigDecimal.ZERO) == 0){
                                    BigDecimal rawreads   =  new BigDecimal(object.get("cleanreads").toString()).multiply(hu).divide(new BigDecimal("1"),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
                                    dto.setRawreads(rawreads.toString());
                                    if(rawreads.compareTo(BigDecimal.ZERO) == 0){
                                        rawreads=new BigDecimal("1");
                                    }
                                    BigDecimal barcodematch = new BigDecimal(object.get("barcode_all_match").toString()).multiply(hu).divide(rawreads,5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
                                    dto.setBarcodematch(barcodematch.toString());

                                }else{
                                    BigDecimal rawreads   =  new BigDecimal(object.get("cleanreads").toString()).multiply(hu).divide(new BigDecimal(object.get("clean").toString()),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
                                    dto.setRawreads(rawreads.toString());
                                    if(rawreads.compareTo(BigDecimal.ZERO) == 0){
                                        rawreads=new BigDecimal("1");
                                    }
                                    BigDecimal barcodematch = new BigDecimal(object.get("barcode_all_match").toString()).multiply(hu).divide(rawreads,5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
                                    dto.setBarcodematch(barcodematch.toString());
                                }
                            }else{
                                BigDecimal rawreads   =  new BigDecimal(dto.getTotalreads()).multiply(hu).divide(new BigDecimal("1"),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
                                dto.setRawreads(rawreads.toString());
                                if(object.get("barcode_all_match")!=null){
                                    BigDecimal barcodematch = new BigDecimal(object.get("barcode_all_match").toString()).multiply(hu).divide(rawreads,5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
                                    dto.setBarcodematch(barcodematch.toString());
                                }
                            }
                            if(object.get("comment")!=null){
                                dto.setComment(object.get("comment").toString());
                            }
                        }
                    }
                }
                dao.insertList(dtoList);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 批量更新
     */
    public int updateList(List<WkcxDto> list){
        return dao.updateList(list);
    }

    /**
     * 模版结果导出
     */
    public List<WkcxDto> getListForExp(WkcxDto wkcxDto) {
        return dao.getListForExp(wkcxDto);
    }
    /**
     * mngs实验室列表
     */
    public List<WkcxDto> getLabList(WkcxDto wkcxDto){
        return dao.getLabList(wkcxDto);
    }

    /**
     * 文库测序数据关联复检申请表
     */
    @Override
    public List<WkcxDto> getpageWkcxLeftFjsq(List<WkcxDto> sampleList) {
        return dao.getpageWkcxLeftFjsq(sampleList);
    }
}

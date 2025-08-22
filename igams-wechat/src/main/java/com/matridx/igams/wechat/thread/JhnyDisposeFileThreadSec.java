package com.matridx.igams.wechat.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.wechat.dao.entities.NyysxxDto;
import com.matridx.igams.wechat.dao.entities.SjnyxDto;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.WzysxxDto;
import com.matridx.igams.wechat.service.svcinterface.INyysxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjnyxService;
import com.matridx.igams.wechat.service.svcinterface.ISjwzxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxWsService;
import com.matridx.igams.wechat.service.svcinterface.IWzysxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class JhnyDisposeFileThreadSec extends Thread{

    private Logger log = LoggerFactory.getLogger(JhnyDisposeFileThread.class);
    private String filePath;
    private String fileName;
    private String jclx;
    private ISjxxWsService sjxxWsService;
    private ISjnyxService sjnyxService;
    private ISjwzxxService sjwzxxService;
    private IFjcfbService fjcfbService;
    private IWzysxxService wzysxxService;

    private INyysxxService nyysxxService;

    public JhnyDisposeFileThreadSec(String filePath,String fileName,ISjxxWsService sjxxWsService,String jclx,ISjwzxxService sjwzxxService,ISjnyxService sjnyxService,IFjcfbService fjcfbService,IWzysxxService wzysxxService,INyysxxService nyysxxService) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.sjxxWsService = sjxxWsService;
        this.jclx = jclx;
        this.sjnyxService = sjnyxService;
        this.sjwzxxService = sjwzxxService;
        this.fjcfbService = fjcfbService;
        this.wzysxxService = wzysxxService;
        this.nyysxxService = nyysxxService;
    }

    @Override
    public void run() {
        try {
            String realPath = ZipUtil.unZipFile(filePath,filePath +"/"+ fileName);
            log.error("realPath:"+realPath);
            String path="/results";
            List<Map<String, String>> pickMap= new ArrayList<>();
            List<Map<String, String>> resultMap= new ArrayList<>();
            getJsonInfo(pickMap,resultMap,realPath, path, "/result.json");
//            dealAnnotInfo(resultMap,realPath, path, "/annot.json");
            List<Map<String, String>> resultNewMap = getSjxxInfo(resultMap);
            List<Map<String, String>> pickNewMap = getSjxxInfo(pickMap);
            List<SjwzxxDto> sjwzxxDtos = new ArrayList<>();
            List<WzysxxDto> wzysxxDtos = new ArrayList<>();
            List<FjcfbDto>  fjcfbDtoList= new ArrayList<>();
            List<String> ids =new ArrayList<>();
            long timestampInSeconds = System.currentTimeMillis() / 1000;
            getSjwzxxList(resultMap, resultNewMap, sjwzxxDtos,wzysxxDtos, fjcfbDtoList, realPath+path,ids,String.valueOf(timestampInSeconds));
            List<SjnyxDto> sjnyxDtos = new ArrayList<>();
            List<NyysxxDto> nyysxxDtos = new ArrayList<>();
            getSjnyxxList(pickMap, resultMap,pickNewMap, sjnyxDtos,nyysxxDtos,String.valueOf(timestampInSeconds));
            if (!CollectionUtils.isEmpty(sjwzxxDtos)){
                SjwzxxDto sjwzxxDto=new SjwzxxDto();
                sjwzxxDto.setIds(ids);
                sjwzxxDto.setJclx(jclx);
                sjwzxxService.deleteBySjidsAndJclx(sjwzxxDto);
                sjwzxxService.insertBysjwzxxDtos(sjwzxxDtos);
            }
            if(!CollectionUtils.isEmpty(wzysxxDtos)){
                wzysxxService.insertDtoList(wzysxxDtos);
            }
            if (!CollectionUtils.isEmpty(sjnyxDtos)){
                SjnyxDto sjnyxDto=new SjnyxDto();
                sjnyxDto.setJclx(jclx);
                sjnyxDto.setIds(ids);
                sjnyxService.deleteBySjidsAndJclx(sjnyxDto);
                sjnyxService.insertBysjnyxDtos(sjnyxDtos);
            }
            if(!CollectionUtils.isEmpty(nyysxxDtos)){
                nyysxxService.insertDtoList(nyysxxDtos);
            }
            if (!CollectionUtils.isEmpty(fjcfbDtoList)){
                FjcfbDto fjcfbDto=new FjcfbDto();
                fjcfbDto.setYwids(ids);
                fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_JHNY_TEMEPLATE.getCode());
                fjcfbService.deleteByYwidsAndYwlx(fjcfbDto);//批量删除原有附件
                fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_JHNY_INFO_TEMEPLATE.getCode());
                fjcfbService.deleteByYwidsAndYwlx(fjcfbDto);//批量删除原有附件
                fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_JHNY_RESULT_TEMEPLATE.getCode());
                fjcfbService.deleteByYwidsAndYwlx(fjcfbDto);//批量删除原有附件
                fjcfbService.batchInsertFjcfb(fjcfbDtoList);
            }
        } catch (Exception e) {
            log.error("结核耐药线程出错:"+e.toString());
        }
    }

    private void getSjnyxxList(List<Map<String, String>> pickMap,List<Map<String, String>> resultMap, List<Map<String, String>> pickNewMap, List<SjnyxDto> sjnyxDtos,List<NyysxxDto> nyysxxDtos,String llh ) {
        for (Map<String, String> map : pickNewMap) {
            SjnyxDto sjnyxDto = new SjnyxDto();
            NyysxxDto nyysxxDto =new NyysxxDto();
            sjnyxDto.setSjid(map.get("sjid"));
            sjnyxDto.setXh(String.valueOf(map.get("xh")));
            sjnyxDto.setJclx(jclx);
            nyysxxDto.setSjid(map.get("sjid"));
            nyysxxDto.setXh(String.valueOf(map.get("xh")));
            nyysxxDto.setJclx(jclx);
            nyysxxDto.setLlh(llh);
            String id = map.get("id");
            for (int i = pickMap.size()-1; i >= 0 ; i--) {
                if (id.equals(pickMap.get(i).get("ID"))){
                    sjnyxDto.setSjnyxid(StringUtil.generateUUID());
                    sjnyxDto.setJson(JSONObject.toJSONString(pickMap.get(i)));
                    sjnyxDto.setSfhb(pickMap.get(i).get("sfhb"));
                    nyysxxDto.setSjnyxid(sjnyxDto.getSjnyxid());
                    nyysxxDto.setJson(JSONObject.toJSONString(pickMap.get(i)));
                    nyysxxDto.setSfhb(pickMap.get(i).get("sfhb"));
//					sjnyxDto.setJy(String.valueOf(stringMap.get("02.突变基因")));
//					sjnyxDto.setJl(String.valueOf(stringMap.get("03.突变结果")));
//					sjnyxDto.setNyjyid(String.valueOf(stringMap.get("04.耐药性")));
//					sjnyxDto.setDs(String.valueOf(stringMap.get("05.突变深度")));
//					sjnyxDto.setXls(String.valueOf(stringMap.get("06.突变频率")));
                    pickMap.remove(i);
                    break;
                }
            }
            sjnyxDtos.add(sjnyxDto);
        }
        if(!CollectionUtils.isEmpty(resultMap)){
            for(Map<String,String> map:resultMap){
                for(Map<String, String> t_map : pickNewMap){
                    if(map.get("ID").equals(t_map.get("id"))){
                        NyysxxDto nyysxxDto =new NyysxxDto();
                        nyysxxDto.setSjid(t_map.get("sjid"));
                        nyysxxDto.setXh("1");
                        nyysxxDto.setJclx(jclx);
                        nyysxxDto.setLlh(llh);
                        nyysxxDto.setBbh("v2.0");
                        nyysxxDto.setSjnyxid(StringUtil.generateUUID());
                        nyysxxDto.setJson(map.get("ck_pick"));
                        nyysxxDtos.add(nyysxxDto);
                        break;
                    }
                }
            }
        }
    }

    private void getSjwzxxList(List<Map<String, String>> resultMap, List<Map<String, String>> resultNewMap, List<SjwzxxDto> sjwzxxDtos,List<WzysxxDto> wzysxxDtos,  List<FjcfbDto> fjcfbDtos, String filePath,List<String> ids,String llh) {
        for (Map<String, String> map : resultNewMap) {
            SjwzxxDto sjwzxxDto = new SjwzxxDto();
            WzysxxDto wzysxxDto = new WzysxxDto();
            sjwzxxDto.setSjwzid(StringUtil.generateUUID());
            sjwzxxDto.setSjid(map.get("sjid"));
            sjwzxxDto.setWzid(map.get("taxid"));
            sjwzxxDto.setWzywm(map.get("wzywm"));
            sjwzxxDto.setWzzwm(map.get("wzzwm"));
            sjwzxxDto.setWzfl(map.get("wzfl"));
            sjwzxxDto.setJglx("possible");
            sjwzxxDto.setXpxx(map.get("id"));
            sjwzxxDto.setXh(String.valueOf(map.get("xh")));
            sjwzxxDto.setJclx(jclx);
            wzysxxDto.setSjwzid(sjwzxxDto.getSjwzid());
            wzysxxDto.setSjid(map.get("sjid"));
            wzysxxDto.setWzid(map.get("taxid"));
            wzysxxDto.setWzywm(map.get("wzywm"));
            wzysxxDto.setWzzwm(map.get("wzzwm"));
            wzysxxDto.setWzfl(map.get("wzfl"));
            wzysxxDto.setJglx("possible");
            wzysxxDto.setXpxx(map.get("id"));
            wzysxxDto.setXh(String.valueOf(map.get("xh")));
            wzysxxDto.setJclx(jclx);
            wzysxxDto.setLlh(llh);
            String id = map.get("id");
            for (int i = resultMap.size()-1; i >= 0 ; i--) {
                if (id.equals(resultMap.get(i).get("ID"))){
                    resultMap.get(i).put("refANI",map.get("refani")!=null?map.get("refani"):"");
                    resultMap.get(i).put("reads",map.get("reads")!=null?map.get("reads"):"");
                    sjwzxxDto.setJson(JSONObject.toJSONString(filteredMap(resultMap.get(i))));
                    wzysxxDto.setJson(JSONObject.toJSONString(filteredMap(resultMap.get(i))));
                    break;
                }
            }
            if(!map.get("nbbm").startsWith("NC") && !map.get("nbbm").startsWith("PC")) {//过滤掉NC和PC开头
                sjwzxxDtos.add(sjwzxxDto);
            }
            wzysxxDtos.add(wzysxxDto);
            if(!ids.toString().contains(map.get("sjid"))){
                ids.add(map.get("sjid"));
                DBEncrypt bpe = new DBEncrypt();
                FjcfbDto fjcfbDto = new FjcfbDto();
                fjcfbDto.setYwid(map.get("sjid"));
                File file = new File(filePath + "/"+map.get("id")+".coverage_depth.png");
                if (!file.exists()) {
                    continue;
                }
                fjcfbDto.setWjlj(bpe.eCode(filePath + "/"+map.get("id")+".coverage_depth.png"));
                fjcfbDto.setXh(String.valueOf(map.get("xh")));
                fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_JHNY_TEMEPLATE.getCode());
                fjcfbDto.setWjm(map.get("id")+".coverage_depth.png");
                fjcfbDto.setZhbj("0");
                fjcfbDto.setFjid(StringUtil.generateUUID());
                fjcfbDtos.add(fjcfbDto);
                //另外一个图片
                FjcfbDto t_fjcfbDto = new FjcfbDto();
                t_fjcfbDto.setYwid(map.get("sjid"));
                File t_file = new File(filePath + "/"+map.get("id")+".png");
                if (!t_file.exists()) {
                    continue;
                }
                t_fjcfbDto.setXh(String.valueOf(map.get("xh")));
                t_fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_JHNY_INFO_TEMEPLATE.getCode());
                t_fjcfbDto.setWjlj(bpe.eCode(filePath + "/"+map.get("id")+".png"));
                t_fjcfbDto.setWjm(map.get("id")+".png");
                t_fjcfbDto.setZhbj("0");
                t_fjcfbDto.setFjid(StringUtil.generateUUID());
                fjcfbDtos.add(t_fjcfbDto);
                //blast文件
                FjcfbDto result_fjcfbDto = new FjcfbDto();
                result_fjcfbDto.setYwid(map.get("sjid"));
                File blast_file = new File(filePath + "/result.json");
                if (!blast_file.exists()) {
                    continue;
                }
                result_fjcfbDto.setXh(String.valueOf(map.get("xh")));
                result_fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_JHNY_RESULT_TEMEPLATE.getCode());
                result_fjcfbDto.setWjlj(bpe.eCode(filePath + "/result.json"));
                result_fjcfbDto.setWjm("result.json");
                result_fjcfbDto.setZhbj("0");
                result_fjcfbDto.setFjid(StringUtil.generateUUID());
                fjcfbDtos.add(result_fjcfbDto);
            }else{
                ids.add(map.get("sjid"));
            }
        }
    }

    public Map<String,String> filteredMap(Map<String, String> resultMap){
        Map<String,String> newResultMap = new HashMap<>();//这里新声明resultMAP进行过滤，不影响后续处理
        newResultMap.putAll(resultMap);
        newResultMap.remove("all_fa");
        newResultMap.remove("clean_fq");
        newResultMap.remove("fragment_qc");
        newResultMap.remove("mtb_cd");
        newResultMap.remove("mtb_fa");
        newResultMap.remove("mtb_png");
        newResultMap.remove("mtb_qc");
        newResultMap.remove("mtb_snpjson");
        newResultMap.remove("mtb_vcf");
        newResultMap.remove("ntm_cd");
        newResultMap.remove("ntm_fa");
        newResultMap.remove("ntm_png");
        newResultMap.remove("ntm_qc");
        newResultMap.remove("ntm_snpjson");
        newResultMap.remove("ntm_vcf");
        newResultMap.remove("object");
        newResultMap.remove("ck_pick");
        return newResultMap;
    }

    private List<Map<String, String>> getSjxxInfo(List<Map<String, String>> resultMap) {
        List<Map<String, String>> mapInfo = new ArrayList<>();
        List<Map<String, String>> wzmap = new ArrayList<>();
        if (!CollectionUtils.isEmpty(resultMap)){
            for (Map<String, String> map : resultMap) {
                try {
                    String id = map.get("ID");
                    if (StringUtil.isBlank(id)){
                        continue;
                    }
                    String[] ids = id.split("-");
                    if(ids.length<=1) {
                        continue;
                    }
                    if (ids[1].length()>6){
                        map.put("nbbm",ids[1]);
                    }else{
                        if (!ids[1].startsWith("NC") && !ids[1].startsWith("PC")){
                            map.put("nbbm",ids[1]+"-"+ids[2]);
                        }else{
                            String nbbm="";
                            //如果为NC或PC，截掉第一位，倒数第一第二位
                            if(ids.length>3){
                                for(int i=0;i<ids.length;i++){
                                    if(i!=0){
                                        if("TBtNGS".equals(ids[i])){
                                            break;
                                        }
                                        //后期NC的后缀不再存在 2025-07-30
                                        if("TA".equals(ids[i])){
                                            nbbm=nbbm+"-"+ids[i];
                                            break;
                                        }
                                        nbbm=nbbm+"-"+ids[i];
                                    }
                                }
                            }
                            map.put("nbbm",StringUtil.isNotBlank(nbbm)?nbbm.substring(1):"");
                        }
                    }
                    List<Map<String,String>> tblist=new ArrayList<>();
                    boolean resultwz=false;//用于判断读取result文件时，判断TB和NTM是否都为空的list，如果为false，放一个taxid为null的map进去
                    resultwz=processTaxonomyData(map, "TB", wzmap, resultwz);
                    resultwz=processTaxonomyData(map, "NTM", wzmap, resultwz);
                    if((map.get("TB")==null && map.get("NTM")==null)||!resultwz) {//处理读取pick文件时，这里用TB和NTM都为null来判断。
                        map.put("taxid",null);
                        map.put("refANI",null);//为了适应原数据回显，往map里再次组装refANI
                        map.put("reads",null);
                        wzmap.add(map);
                        if(map.get("TB")==null && map.get("NTM")==null){
                            continue;
                        }
                    }
                }catch(Exception e) {
                    log.error("getSjxxInfo 循环Map出错 ID:" + map.get("ID") + " 信息：" + e.getMessage());
                }
            }
            mapInfo = sjxxWsService.getSjxxInfo(wzmap);
        }
        return mapInfo;
    }

    private boolean processTaxonomyData(Map<String, String> sourceMap, String fieldName,
                                        List<Map<String, String>> targetList, boolean hasData) {
        if (sourceMap.get(fieldName) != null) {
            JSONArray jsonArray = JSONArray.parseArray(sourceMap.get(fieldName));
            List<Map<String, String>> taxList = (List<Map<String, String>>) JSONArray.parse(jsonArray.toJSONString(), Feature.OrderedField);

            if (!CollectionUtils.isEmpty(taxList)) {
                hasData = true;
                for (Map<String, String> taxEntry : taxList) {
                    Map<String, String> newEntry = new HashMap<>(sourceMap);
                    newEntry.put("taxid", taxEntry.get("taxid"));
                    newEntry.put("refANI", String.valueOf(taxEntry.get("ani")));
                    newEntry.put("reads", String.valueOf(taxEntry.get("reads")));
                    targetList.add(newEntry);
                }
            }
        }
        return hasData;
    }

    /**
     * 解析结果条目为Map
     */
    private Map<String, String> parseResultEntry(Object entry) {
        return JSONObject.parseObject(JSON.toJSONString(entry), new TypeReference<Map<String, String>>() {});
    }

    /**
     * 处理QC相关数据
     */
    private void processQcData(Map<String, String> entryData) {
        Optional.ofNullable(entryData.get("QC"))
                .map(qcStr -> JSON.parseObject(qcStr, new TypeReference<Map<String, String>>() {}))
                .ifPresent(qcMap -> {
                    Stream.of("CleanQ30", "CleanReads", "RawReads","MappingReads")
                            .forEach(field -> entryData.put(field, qcMap.get(field)));
                    // 单独处理cov90到name的映射
                    entryData.put("target", qcMap.get("cov90"));
                    entryData.put("spike", qcMap.get("IC"));
                });
        String anno=entryData.get("annot_json");
        if(StringUtil.isNotBlank(anno)){
            JSONObject cr=JSONObject.parseObject(anno);
            Map<String,String> objec=JSONObject.parseObject(JSON.toJSONString(cr.get("taxon")), Map.class);
            JSONArray NTM = replacekey(JSONArray.parseArray(JSONObject.toJSONString(objec.get("NTM")!=null?objec.get("NTM"):new JSONArray())));
            JSONArray MTBC = replacekey(JSONArray.parseArray(JSONObject.toJSONString(objec.get("MTBC")!=null?objec.get("MTBC"):new JSONArray())));


            entryData.put("TB", MTBC!=null?JSONObject.toJSONString(MTBC):"[]");
            entryData.put("NTM", NTM!=null?JSONObject.toJSONString(NTM):"[]");
        }else{
            entryData.put("TB", "[]");
            entryData.put("NTM", "[]");
        }

    }

    public JSONArray replacekey(JSONArray jsonArray){
        // 替换Species字段为name
        if(jsonArray!=null && jsonArray.size()>0){
            for(int i=0; i<jsonArray.size(); i++){
                JSONObject item = jsonArray.getJSONObject(i);
                if(item.containsKey("Species")){
                    String speciesValue = item.getString("Species");
                    item.remove("Species");
                    item.put("name", speciesValue);
                }
            }
        }
        return jsonArray;
    }
    /**
     * result.json 2.0版本，数据结构变为Map，key为文库ID，value为Map
     * @param realPath
     * @param path
     * @param name
     * @return
     * @throws IOException
     */
    private void getJsonInfo(List<Map<String, String>> pickMap,List<Map<String, String>> resultMap,String realPath, String path,String name) throws IOException {
        Path resultPath = Paths.get(realPath+path+name);
        byte[] data = Files.readAllBytes(resultPath);
        String result = new String(data, "utf-8");
        JSONObject cr=JSONObject.parseObject(result);
        cr.keySet().stream()
                .filter(key -> !"annot_json".equals(key) ) // 过滤条件明确化，将ID为annot_json和ID带YZ的过滤
                .forEach(key -> {
                    Map<String, String> entryData = parseResultEntry(cr.get(key));
                    entryData.put("ID", key);
                    processQcData(entryData); // 提取QC数据处理逻辑
                    resultMap.add(entryData);
                });

        //接下来处理tab文件，将tb和ntm的数据进行组装。
        //通过mtb_snptab找压缩包对应的TB的tab文件，通过ntm_snptab找压缩包对应的NTM的tab文件，进行组装。
        //如果mtb_snptab或ntm_snptab不存在，则不进行组装。
        if(!CollectionUtils.isEmpty(resultMap)){
            List<Map<String,String>> ncList=new ArrayList<>();
            for(Map<String,String> t_map:resultMap){
                List<Map<String,String>> ck_pickMap=new ArrayList<>();
                //获取耐药分析数据文件信息
                List<String> files = new ArrayList<>();
                files.add(t_map.get("mtb_snptab"));
                files.add(t_map.get("ntm_snptab"));
                if(!CollectionUtils.isEmpty(files)){
                    dealTabInfo(pickMap,ck_pickMap,realPath, path,files,ncList,t_map);
                }
                t_map.put("ck_pick",JSONArray.toJSONString(ck_pickMap));
            }
            dealNcInfo(ncList,pickMap);//将背景突变数据组装到resultMap中
        }
    }

    public void dealNcInfo(List<Map<String,String>> ncList,List<Map<String,String>> pickMap){
        if(!CollectionUtils.isEmpty(pickMap)){
            for(Map<String,String> map:pickMap){
                //根据突变结果去ncList中查找对应的背景突变深度
                String tbjg=map.get("突变结果");
                if(StringUtil.isBlank(tbjg)){
                    continue;
                }
                for(Map<String,String> ncmap:ncList){
                    if(tbjg.equals(ncmap.get("突变结果"))){
                        //若原先背景突变为空，则直接set
                        if(StringUtil.isBlank(map.get("背景突变"))){
                            map.put("背景突变",ncmap.get("突变深度"));
                        }else{
                            //若不为空，判断NC中对应的突变深度是否大于原先的背景突变深度，若大于，则set
                            BigDecimal tbsd_num = new BigDecimal(map.get("背景突变"));
                            BigDecimal nctbsd_num = new BigDecimal(ncmap.get("突变深度"));
                            if(nctbsd_num.compareTo(tbsd_num)>0){
                                map.put("背景突变",ncmap.get("突变深度"));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 文库名.tab 文件解析成List<MAP>,并将依据判断规则,对结果数据进行分类
     * @param realPath
     * @param path
     * @param filenames
     * @return
     * @throws IOException
     */
    private void dealTabInfo(List<Map<String,String>> pickMap,List<Map<String, String>> ck_pickMap,String realPath, String path,List<String> filenames,List<Map<String,String>> ncList,Map<String,String> resultMap) throws IOException {
        boolean result=false;//用于判断是否有type01的结果数据，有则为true，没有则为false，若false，添加空的
        String sampleid="";
        for(String name:filenames){
            if(StringUtil.isNotBlank(name)){
                List<Map<String, String>> listmap=new ArrayList<>();;
                try (BufferedReader br = new BufferedReader(new FileReader(realPath+path+"/"+name))) {
                    String line;
                    String[] headers = null;
                    // 读取标题行
                    if ((line = br.readLine()) != null) {
                        headers = line.split("\t");
                    }

                    if (headers == null) {
                        throw new IllegalArgumentException("文件缺少标题行");
                    }

                    // 处理数据行
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split("\t");

                        Map<String, String> entry = new HashMap<>();
                        for (int i = 0; i < headers.length; i++) {
                            try {
                                entry.put(headers[i], values[i]);
                            }catch (Exception e){//若捕获到异常直接跳过
                                continue;
                            }
                        }
                        //同时把相应文件的信息信息也保留起来
                        listmap.add(entry);
                    }
                    //依据判断规则，对结果数据分类
                    if(!CollectionUtils.isEmpty(listmap)){
                        for (Map<String, String> map : listmap) {
                            if(StringUtil.isNotBlank(sampleid))
                                sampleid=map.get("Sample");
                            Map<String,String> t_map=new HashMap<>();
                            //1.判断分支杆菌
                            // MutType匹配Mut,MutFreq>50%,MutAnnot... =第三类,即MutAnnot1不为空，并且MutAnnot1!=Related
                            //2.判断低频突变
                            // MutType匹配Mut,MutFreq<50%,MutAnnot... =第三类,即MutAnnot1不为空，并且MutAnnot1!=Related
                            //3.判断同义突变位点
                            //MutType匹配Mut(不为以上两类的)
                            if(map.get("MutType")!= null && map.get("MutType").contains("Mut")){
                                if(Double.parseDouble(map.get("MutFreq").replace("%",""))>=50
                                && StringUtil.isNotBlank(map.get("MutAnnot2")) && !"Related".equals(map.get("MutAnnot2"))
                                ){
                                    t_map.put("type","tp01");//设置页面显示分类
                                }else if(Double.parseDouble(map.get("MutFreq").replace("%",""))<50
                                        && StringUtil.isNotBlank(map.get("MutAnnot2")) && !"Related".equals(map.get("MutAnnot2"))){
                                    t_map.put("type","tp02");//设置页面显示分类
                                }else{
                                    t_map.put("type","tp03");
                                }
                            }else if(map.get("MutType")!= null &&"Wild".equals(map.get("MutType"))){
                                //4.野生型
                                //MutType=Wild
                                t_map.put("type","tp04");
                            }else if(map.get("MutType")!= null &&"Miss".equals(map.get("MutType"))){
                                //5.未扩增
                                //MutType=Miss
                                t_map.put("type","tp05");
                            }else{
                                t_map.put("type","");//其他不符合不做归类
                            }
                            //同义替换
                            t_map.put("突变基因",StringUtil.isNotBlank(map.get("Gene"))?map.get("Gene"):"");
                            t_map.put("突变结果",StringUtil.isNotBlank(map.get("MutAnnot3"))?map.get("MutAnnot3"):(StringUtil.isNotBlank(map.get("MutAnnot2"))?map.get("MutAnnot2"):""));
                            String MutAnnot3 = map.get("MutAnnot3");
                            // 用于区分是蛋白突变(_p)，还是核苷酸突变(_n 和 _c)。
                            if(StringUtil.isNotBlank(MutAnnot3)){
                                if(MutAnnot3.contains("_p")){
                                    t_map.put("蛋白突变",MutAnnot3);
                                }else if(MutAnnot3.contains("_n")||MutAnnot3.contains("_c")){
                                    t_map.put("核苷酸突变",MutAnnot3);
                                }
                            }
                            String MutAnnot2 = map.get("MutAnnot2");
                            // 拆分药物
                            if(StringUtil.isNotBlank(MutAnnot2)){
                                int k_startIndex = MutAnnot2.indexOf("(");
                                int k_endIndex = MutAnnot2.indexOf(")");
                                String xgyp = MutAnnot2;
                                if(k_startIndex>-1 && k_endIndex>-1){
                                    xgyp = MutAnnot2.substring(k_startIndex+1,k_endIndex);
                                }
                                t_map.put("相关药品",xgyp);
                                //List<String> nyypArr = Arrays.stream(xgyp.split("/")).toList();
                            }
                            t_map.put("耐药性",StringUtil.isNotBlank(map.get("MutAnnot2"))?map.get("MutAnnot2"):"");
                            t_map.put("耐药药品",StringUtil.isNotBlank(map.get("MutAnnot2"))?map.get("MutAnnot2"):"");
                            t_map.put("突变深度",StringUtil.isNotBlank(map.get("MutCount"))?map.get("MutCount"):"");
                            t_map.put("突变频率",StringUtil.isNotBlank(map.get("MutFreq"))?map.get("MutFreq"):"");
                            t_map.put("扩增位置",StringUtil.isNotBlank(map.get("Site"))?map.get("Site"):"");
                            t_map.put("碱基序列",StringUtil.isNotBlank(map.get("Seq"))?map.get("Seq"):"");
                            t_map.put("蛋白序列",StringUtil.isNotBlank(map.get("Pro"))?map.get("Pro"):"");
                            t_map.put("覆盖度",StringUtil.isNotBlank(map.get("Coverage"))?map.get("Coverage"):"");
                            t_map.put("深度",StringUtil.isNotBlank(map.get("Depth"))?map.get("Depth"):"");
                            t_map.put("分类",name.indexOf("ntm")>-1?"ntm":"tb");//区分结核还是非结核
                            if(name.indexOf(".ntm")>-1 && !"[]".equals(resultMap.get("NTM")) && StringUtil.isNotBlank(resultMap.get("NTM")) && resultMap.get("NTM").contains("36809")){
                                t_map.put("sfhb","1");//1代表发送，0代表不发送，0只有审核页面才能设置
                            }else if(name.indexOf(".mtb")>-1 && !"[]".equals(resultMap.get("TB")) && StringUtil.isNotBlank(resultMap.get("TB")) ){
                                t_map.put("sfhb","1");
                            }else{
                                t_map.put("sfhb","2");//2代表审核页面不显示,只有耐药有2
                            }
                            ck_pickMap.add(t_map);//不保留ID
                            t_map.put("ID",map.get("Sample"));
                            if("tp01".equals(t_map.get("type"))){
                                pickMap.add(t_map);//存储筛选后的数据
                                result=true;
                            }
                            if((name.indexOf("NC-")>-1 || name.indexOf("DC-")>-1) && StringUtil.isNotBlank(t_map.get("突变结果"))){//将NC的数据存储到ncList中，用于后续处理
                                ncList.add(t_map);
                            }
                        }
                    }
                    // 输出JSON
//            ObjectMapper mapper = new ObjectMapper();
//            jsonResult = mapper.writerWithDefaultPrettyPrinter()
//                    .writeValueAsString(finalmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if(!result && StringUtil.isNotBlank(sampleid)){
            Map<String,String> t_map=new HashMap<>();
            t_map.put("突变基因","");
            t_map.put("突变结果","");
            t_map.put("耐药性","");
            t_map.put("突变深度","");
            t_map.put("突变频率","");
            t_map.put("ID",sampleid);
            pickMap.add(t_map);
        }
    }
}

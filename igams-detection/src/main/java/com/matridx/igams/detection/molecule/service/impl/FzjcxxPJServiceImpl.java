package com.matridx.igams.detection.molecule.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.YyxxDto;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IYyxxService;
import com.matridx.igams.detection.molecule.dao.entities.FzbbztDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcjgDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxModel;
import com.matridx.igams.detection.molecule.dao.entities.FzzkjgDto;
import com.matridx.igams.detection.molecule.dao.entities.PjjgpdDto;
import com.matridx.igams.detection.molecule.dao.post.IFzbbztDao;
import com.matridx.igams.detection.molecule.dao.post.IFzjcxxPJDao;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzbbztService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcjgPJService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxmPJService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxxPJService;
import com.matridx.igams.detection.molecule.util.CovidReportZipThread;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class FzjcxxPJServiceImpl extends BaseBasicServiceImpl<FzjcxxDto, FzjcxxModel, IFzjcxxPJDao> implements IFzjcxxPJService,IFileImport{
    @Autowired
    RedisUtil redisUtil;
    @Value("${matridx.fileupload.prefix:}")
    private String prefix;
    @Value("${matridx.fileupload.tempPath:}")
    private String tempFilePath;
    @Autowired
    IFzjcjgPJService fzjcjgPJService;
    @Autowired
    private IFzjcxmPJService fzjcxmPjService;
    @Autowired
    private IYyxxService yyxxService;
    @Autowired
    ICommonService commonservice;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IFzbbztDao fzbbztDao;
    @Autowired
    IFzbbztService fzbbztService;
    /**
     * 根据条件下载报告压缩包
     */
    private final Logger log = LoggerFactory.getLogger(FzjcxxServiceImpl.class);
    public Map<String,Object> reportZipDownload(FzjcxxDto fzjcxxDto, HttpServletRequest request){
        // 查询下载报告
        List<String> ids = fzjcxxDto.getIds();
        List<FzjcxxDto> fzjcxxDtos;
        //判断是否选中
        if(ids != null && ids.size() > 0){
            // 根据选择查询报告信息(pdf结果)
            fzjcxxDtos = dao.selectDownloadReportByIds(fzjcxxDto);
        }else{
            // 根据条件查询报告信息(pdf结果)
            fzjcxxDtos = dao.selectDownloadReport(fzjcxxDto);
        }
        return zipDownload(fzjcxxDtos);
    }

    @Override
    public List<FzjcxxDto> checkJcxm(FzjcxxDto fzjcxxDto) {
        return dao.checkJcxm(fzjcxxDto);
    }

    @Override
    public boolean updateSyzt(FzjcxxDto fzjcxxDto) {
        return dao.updateSyzt(fzjcxxDto);
    }
    @Override
    public FzjcxxDto getInfoByNbbh(FzjcxxDto fzjcxxDto) throws BusinessException {
        List<FzjcxxDto> fzjcxxDtos = dao.getInfoByNbbh(fzjcxxDto);
        if (fzjcxxDtos == null || fzjcxxDtos.size() <= 0){
            log.error("没有该条记录！");
            throw new BusinessException("msg","没有该条记录！");
        }
        return fzjcxxDtos.get(0);
    }

    @Override
    public Boolean updateInfoByNbbh(FzjcxxDto fzjcxxDto) {
        return  dao.updateInfoByNbbh(fzjcxxDto) != 0;
    }

    /**
     * 下载压缩报告
     */
    private Map<String, Object> zipDownload(List<FzjcxxDto> fzjcxxDtos){
        Map<String, Object> map = new HashMap<>();
        try {
            // TODO Auto-generated method stub
            // 判断是否大于1000条
            if (fzjcxxDtos != null && fzjcxxDtos.size() > 1000) {
                map.put("error", "超过1000个报告！");
                map.put("status", "fail");
                return map;
            } else if (fzjcxxDtos == null || fzjcxxDtos.size() == 0) {
                map.put("error", "未找到报告信息！");
                map.put("status", "fail");
                return map;
            }
            String key = String.valueOf(System.currentTimeMillis());
            redisUtil.hset("EXP_:_" + key, "Cnt", "0");
            map.put("count", fzjcxxDtos.size());
            map.put("redisKey", key);
            String folderName = "UP" + System.currentTimeMillis();
            String storePath = prefix + tempFilePath + BusTypeEnum.IMP_REPORTZIP.getCode() + "/" + folderName;
            mkDirs(storePath);
            map.put("srcDir", storePath);
            // 开启线程拷贝临时文件
            CovidReportZipExport covidReportZipExport = new CovidReportZipExport();
            covidReportZipExport.init(key, storePath, folderName, fzjcxxDtos, redisUtil);
            CovidReportZipThread covidReportZipThread = new CovidReportZipThread(covidReportZipExport);
            covidReportZipThread.start();
            map.put("status", "success");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return map;
    }
    /**
     * 根据路径创建文件
     */
    private boolean mkDirs(String storePath)
    {
        File file = new File(storePath);
        if (file.isDirectory())
        {
            return true;
        }
        return file.mkdirs();
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean resultModSaveDetectionPJ(FzjcjgDto fzjcjgDto){
        fzjcjgPJService.delDtoListByFzxmid(fzjcjgDto);
        List<FzjcjgDto> fzjcjgDtos = JSON.parseArray(fzjcjgDto.getJcjg_json(), FzjcjgDto.class);
        List<FzjcxxDto> fzjcxxDtos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(fzjcjgDtos)){//处理各标本最终结果
            //boolean sfxt=true;//结果是否一致
            FzjcxxDto fzjcxxDto=new FzjcxxDto();
            for(FzjcjgDto dto:fzjcjgDtos){
                dto.setFzjcjgid(StringUtil.generateUUID());
                fzjcxxDto.setFzjcid(fzjcjgDto.getFzjcid());
                if(StringUtil.isBlank(fzjcxxDto.getJcjgmc())){
                    fzjcxxDto.setJcjgmc(dto.getJcjgmc());
                }else{
                    if(!fzjcxxDto.getJcjgmc().equals(dto.getJcjgmc()))
                        fzjcxxDto.setJcjgmc("阳性");
                }
            }
            fzjcxxDtos.add(fzjcxxDto);
            int count = fzjcjgPJService.insertList(fzjcjgDtos);
            if(count==0){
                return false;
            }
            //修改igams_fzjcxx表检测结果
            dao.updateJcjgmc(fzjcxxDtos);
            //rabbit同步用
            fzjcjgDto.setJglist(fzjcjgDtos);
            fzjcjgDto.setFzjcxxmcs(fzjcxxDtos);
        }
        return true;
    }

    @Override
    public FzjcxxDto getSampleAcceptInfo(FzjcxxDto fzjcxxDto) throws BusinessException {
        List<FzjcxxDto> fzjcxxDtos = getFzjcxxListByYbbh(fzjcxxDto);
        if (CollectionUtils.isEmpty(fzjcxxDtos)){
            log.error("没有该标本编码！");
            throw new BusinessException("msg","没有该标本编码！");
        }
        FzjcxxDto fzjcxxDto_result = fzjcxxDtos.get(0);
        //从标本状态表中查找出标本状态信息
        List<String> bbzts = fzbbztDao.getZtListByFzjcid(fzjcxxDto_result.getFzjcid());
        if (!CollectionUtils.isEmpty(bbzts)){
            fzjcxxDto_result.setPjbbzts(bbzts);
        }else {
            //标本状态无则默认
            List<JcsjDto> bbztlist = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.GENERAL_SAMPLESTATE.getCode());//普检标本状态
            for (JcsjDto jcsj: bbztlist ) {
                if ("1".equals(jcsj.getSfmr())){
                    List<String> mrbbzt = new ArrayList<>();
                    mrbbzt.add(jcsj.getCsid());
                    fzjcxxDto_result.setPjbbzts(mrbbzt);
                    break;
                }
            }
        }
        //检测单位无则默认
        if (StringUtil.isBlank(fzjcxxDto_result.getJcdw())){
            List<JcsjDto> jcdwList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode());
            for (JcsjDto jcdw: jcdwList ) {
                if ("1".equals(jcdw.getSfmr())){
                    fzjcxxDto_result.setJcdw(jcdw.getCsid());
                    break;
                }
            }
        }
        if (StringUtil.isBlank(fzjcxxDto_result.getSyh())){
            //获取实验号
            String syh = generateSyh(fzjcxxDtos);
            fzjcxxDto_result.setSyh(syh);
        }
        if ( StringUtil.isBlank(fzjcxxDto_result.getNbbh())){
            fzjcxxDto_result.setNbbh(getFzjcxxByybbh());
        }
//        if (StringUtil.isBlank(fzjcxxDto_result.getCjsj())){
//            log.error("该用户还未录入！");
//            throw new BusinessException("msg","该用户还未录入！");
//        }
        return fzjcxxDto_result;
    }

    public String generateSyh(List<FzjcxxDto> fzjcxxDtos) {
        if (!CollectionUtils.isEmpty(fzjcxxDtos)){
            FzjcxxDto fzjcxxDto = fzjcxxDtos.get(0);
            /*----------获取流水号规则-------开始----------*/
            //当前规则为年月日+“-”+五位数流水号！范例：20220804-12345
            //实验号替换规则{key:length:isConditionOfSerial}=>{替换关键词:长度:是否为计算流水号的条件}
            String syhgz = "{nf:4:true}{yf:2:true}{ts:2:true}{-:1:true}{lsh:5:false}";
            List<String> gzList = new ArrayList<>();
            gzList.add(fzjcxxDto.getFzjczmxgz());//分子检测子项目规则
            for (String gz : gzList) {
                if (StringUtil.isNotBlank(gz)){
                    syhgz = gz.split(";")[0];
                    break;
                }
            }
            log.error("实验号规则："+ syhgz);
            /*----------实验号规则处理-----开始----------*/
            //1、根据规则获取实验号长度：syhength
            int syhLength = 0;//实验号长度（累加）
            //2、流水号生成的查询条件（里面包含conditionList、startindex、seriallength、deafultserial）：serialMap
            Map<String, Object> serialMap = new HashMap<>();//流水号生成查询条件
            //3、实验号规则中isConditionOfSerial为true的条件：conditionList
            List<Map<String,Object>> conditionList = new ArrayList<>();
            //4、实验号字符串List
            List<Map<String,String>> syhStrList = new ArrayList<>();
            //5、流水号List
            List<Map<String,String>> serialStrList = new ArrayList<>();
            //生成后结果Map
            Map<String, String> resultMap = new HashMap<>();
            //6、循环替换syhgz中个关键词(除有关流水号的内容不替换)
            while (syhgz.contains("{") && syhgz.contains("}")) {
                Map<String,String> syhMap = new HashMap<>();
                int length = 0;//默认关键词长度为0
                boolean isConditionOfSerial = false;//默认 是否加入流水号生成的判断条件 为false
                int startIndex = syhgz.indexOf("{");//获取开始下标
                int endIndex = syhgz.indexOf("}");//获取结束下标
                if (startIndex != 0){
                    // 情况1：替换符前方有字符串；若“{”在不在首位
                    // xxxx-{jcdwdm:2:true}
                    // 截取“xxxx-”存入syhStrList中
                    String valueStr = syhgz.substring(0,startIndex);
                    endIndex = startIndex-1;
                    syhMap.put("key","string");//设置关键词为string
                    syhMap.put("value",valueStr);//设置对应值
                    syhMap.put("index", String.valueOf(syhLength));//设置位置（前面所有字符的长度）
                    length = valueStr.length();
                    syhMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
                }else {
                    // 情况1：替换符前方无字符串；若“{”在首位
                    // {year:4:true}
                    //截取{}内的内容
                    String key = syhgz.substring(startIndex + 1, endIndex);//year:4:true
                    //若截取内容内有“:”则分割；
                    //若截取内容内无“:”，则默认key为截取内容，长度为其map中长度，默认是否加入流水号生成的判断条件为false
                    if (key.contains(":")) {
                        String[] split = key.split(":");//例：[year:4:true]
                        key = split[0];//第一位为关键词key，例：year
                        if (StringUtil.isNotBlank(split[1])){
                            try {
                                length = Integer.parseInt(split[1]);//第二位为对应关键词的长度，例：2
                            } catch (NumberFormatException e) {
                                log.error("实验号规则警告：key:"+key+"的length:"+split[1]+"不是数字");
                            }
                        }
                        //若有第三位，则第三位为是否加入流水号生成的判断条件，例：true
                        //若无第三位，默认为false
                        if (split.length >= 3){
                            isConditionOfSerial = "true".equalsIgnoreCase(split[2]);
                        }
                    }
                    syhMap.put("key",key);//设置关键词
                    //若为流水号，不做处理，
                    if ("lsh".equals(key)){
                        syhMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
                        syhMap.put("value","");//设置值为空（后面处理）
                        syhMap.put("index", String.valueOf(syhLength));//设置位置（前面所有字符的长度）
                        serialStrList.add(syhMap);//存入流水号List中
                    } else {
                        //实验号特殊处理
                        dealSyhKeyMap(key, fzjcxxDto, resultMap);
                        String valueStr = resultMap.get(key);//从infoMap中获取对应值，例：year对应为2023
                        if (valueStr.length() > length && length != 0){
                            valueStr = valueStr.substring(valueStr.length()-length);//将值截取至对应长度。例：截取01的后2位（01为对应值，2为对应值长度）（默认从末尾开始截取长度，若需要从前面开始截取，则需要增加特殊key,并在dealsyhKeyMap方法中处理）
                        }
                        length = valueStr.length();//获取对应值最终长度（针对没设长度的时候）
                        syhMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
                        syhMap.put("value",valueStr);//设置值
                        syhMap.put("index", String.valueOf(syhLength));//设置位置（前面所有字符的长度）
                    }
                    if (isConditionOfSerial){
                        Map<String,Object> map = new HashMap<>();
                        map.put("conditionIndex",syhLength+1);
                        map.put("conditionLength",length);
                        map.put("conditionKey",syhMap.get("value"));
                        conditionList.add(map);
                    }
                }
                syhStrList.add(syhMap);//将截取替换内容添加到list
                syhgz = syhgz.substring(endIndex+1);//截取剩余分规则
                syhLength += length;//实验号长度累加
            }
            //7、若实验号规则最后还有字符串，则进行处理
            Map<String,String> syhMap = new HashMap<>();
            syhMap.put("key","string");//设置关键词为string
            syhMap.put("value",syhgz);//设置对应值
            syhMap.put("index", String.valueOf(syhLength));//设置位置（前面所有字符的长度）
            int length = syhgz.length();
            syhMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
            syhStrList.add(syhMap);//将截取替换内容添加到list
            syhLength += length;//实验号长度累加
            //8、遍历流水号List,循环生成对应的流水号
            for (Map<String, String> syhInfo : serialStrList) {
                serialMap.put("startindex", Integer.parseInt(syhInfo.get("index"))+1);//流水号开始位置
                serialMap.put("seriallength", Integer.parseInt(syhInfo.get("length")));//流水号长度
                serialMap.put("syhLength", syhLength);//实验号长度
                serialMap.put("conditionList",conditionList);//条件List
                //生成获取流水号
                String customSerial = dao.getPjSyhSerial(serialMap);
                syhInfo.put("value",customSerial);
            }
            //拼接list获取完整实验号
            StringBuilder syh = new StringBuilder();
            for (Map<String, String> syhStr : syhStrList) {
                syh.append(syhStr.get("value"));
            }
            return syh.toString();
        }
        return null;
    }

    public void dealSyhKeyMap(String key, FzjcxxDto fzjcxxDto,Map<String,String> resultMap) {
        switch (key){
            case "nf": dealYear(resultMap);break;
            case "yf": dealMonth(resultMap);break;
            case "ts": dealDay(resultMap);break;
            case "-": dealHorizontalBar(resultMap);break;
            case "jcxmdm": dealJcxmdm(resultMap,fzjcxxDto);break;
            case "jczxmdm": dealJczxmdm(resultMap,fzjcxxDto);break;
        }
    }

    private void dealJczxmdm(Map<String, String> resultMap,FzjcxxDto fzjcxxDto) {
        resultMap.put("jczxmdm", fzjcxxDto.getJczxmdm());
    }
    private void dealJcxmdm(Map<String, String> resultMap,FzjcxxDto fzjcxxDto) {
        resultMap.put("jcxmdm", fzjcxxDto.getJcxmdm());
    }

    private void dealHorizontalBar(Map<String, String> resultMap) {
        resultMap.put("-", "-");
    }

    /*
        处理实验号天
    */
    private void dealDay(Map<String, String> resultMap) {
        Calendar currentDate = Calendar.getInstance();
        // 获取当前日
        String nowDay = String.valueOf(currentDate.get(Calendar.DATE));
        nowDay = nowDay.length()>1?nowDay:"0"+nowDay;
        resultMap.put("ts", nowDay);
    }
    /*
        处理实验号月
    */
    private void dealMonth(Map<String, String> resultMap) {
        Calendar currentDate = Calendar.getInstance();
        // 获取当前月份
        String month = String.valueOf(currentDate.get(Calendar.MONTH) + 1);
        month = month.length()>1?month:"0"+month;
        resultMap.put("yf", month);
    }
    /*
       处理实验号年
    */
    private void dealYear(Map<String, String> resultMap) {
        Calendar currentDate = Calendar.getInstance();
        // 获取当前天数
        int year = currentDate.get(Calendar.YEAR);
        resultMap.put("nf", String.valueOf(year));
    }

    @Override
    public List<FzjcxxDto> getFzjcxxBySyh(FzjcxxDto fzjcxxDto) {
        return dao.getFzjcxxBySyh(fzjcxxDto);
    }


    @Override
    public String getPjSyhSerial(FzjcxxDto fzjcxxDto) {
        List<FzjcxxDto> fzjcxxDtos = getFzjcxxListByYbbh(fzjcxxDto);
        return generateSyh(fzjcxxDtos);
    }

    @Override
    public List<FzjcxxDto> getFzjcxxByNbbh(FzjcxxDto fzjcxxDto) {
        return dao.getFzjcxxByNbbh(fzjcxxDto);
    }

    @Override
    public String getFzjcxxByybbh() {
        String yearLast = new SimpleDateFormat("yy", Locale.CHINESE).format(new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);// 设置星期一为一周开始的第一天
        calendar.setTimeInMillis(System.currentTimeMillis());// 获得当前的时间戳
        String weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR) + "";// 获得当前日期属于今年的第几周
        if ("1".equals(weekOfYear) && calendar.get(Calendar.MONTH) == Calendar.DECEMBER){
            calendar.add(Calendar.DATE, -7);
            weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR) + "";// 获得当前日期属于今年的第几周
            weekOfYear = (Integer.parseInt(weekOfYear) + 1) + "";
        }
        String yearandweek = null;
        if (weekOfYear.length() == 1){
            yearandweek = yearLast + "0" + weekOfYear;
        } else if (weekOfYear.length() == 2){
            yearandweek = yearLast + weekOfYear;
        }
        //根据分子检测项目书写
        String prefix =  yearandweek;
        // 查询流水号
        String serial = dao.getNbbhSerial(prefix);
        return prefix + serial;
    }

    @Override
    public boolean saveFzjcxxInfo(FzjcxxDto fzjcxxDto) throws BusinessException {
        boolean success=  dao.saveFzjcxxInfo(fzjcxxDto) !=0;
        if (!success){
            log.error("保存失败！");
            throw new BusinessException("msg","保存失败！");
        }
        return true;
    }




    @Override
    public List<FzjcxxDto> getFzjcxxListByYbbh(FzjcxxDto fzjcxxDto) {
        return dao.getFzjcxxListByYbbh(fzjcxxDto);
    }

    //增加HPV信息
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertHPVDetection(FzjcxxDto fzjcxxDto) {
        fzjcxxDto.setBbztmc(getBbztmc(fzjcxxDto));
        List<JcsjDto> jcxmListall = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM));//所有分子检测项目
        List<JcsjDto> fzjczxmlistall = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_SUBITEM));
        //处理检测项目
        StringBuilder jcxmmc = new StringBuilder();//存储一个检测信息对应的多个检测项目名称，格式xx,xx,xx
        StringBuilder jcxmid = new StringBuilder();
        String[] fzxmlist = fzjcxxDto.getFzxmids();
        List<JcsjDto> zxmlist=new ArrayList<>();
        if(fzxmlist!=null){
            for (String s : fzxmlist) {
                for (JcsjDto jcsjDto_jcxm : jcxmListall) {
                    if (jcsjDto_jcxm.getCsid().equals(s)) {
                        jcxmmc.append(",").append(jcsjDto_jcxm.getCsmc());
                        jcxmid.append(",").append(jcsjDto_jcxm.getCsid());
                    }
                }
            }
        }
        if(fzjcxxDto.getFzjczxmids()!=null && fzjcxxDto.getFzjczxmids().length>0){
            for(int j = 0; j < fzjcxxDto.getFzjczxmids().length; j++){
                for(JcsjDto jcsjDto_zxm:fzjczxmlistall){
                    if(jcsjDto_zxm.getCsid().equals(fzjcxxDto.getFzjczxmids()[j])){
                        zxmlist.add(jcsjDto_zxm);
                    }
                }
            }
        }

        if (StringUtil.isNotBlank(jcxmmc.toString())) {
            fzjcxxDto.setJcxmmc(jcxmmc.substring(1));
        }
        if (StringUtil.isNotBlank(jcxmid.toString())) {
            fzjcxxDto.setJcxmid(jcxmid.substring(1));
        }
        fzjcxxDto.setZt(StatusEnum.CHECK_NO.getCode());
        if(fzjcxxDto.getBbzts()!=null){
            fzjcxxDto.setPjbbzts(Arrays.asList(fzjcxxDto.getBbzts()));
        }
        fzbbztService.insertFzbbzt(fzjcxxDto);
        List <FzjcxmDto> fzjcxmDtos=new ArrayList<>();
        if(fzxmlist!=null){
            for (String fzjcxmid : fzxmlist) {
                // 遍历检测项目一个一个新增到数据库
                if (!CollectionUtils.isEmpty(zxmlist)) {
                    for (JcsjDto jcsjDto : zxmlist) {
                        if (fzjcxmid.equals(jcsjDto.getFcsid())) {
                            FzjcxmDto fzjcxmDto = new FzjcxmDto();
                            fzjcxmDto.setFzxmid(StringUtil.generateUUID());
                            fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
                            fzjcxmDto.setFzjczxmid(jcsjDto.getCsid());
                            fzjcxmDto.setFzjcxmid(fzjcxmid);
                            fzjcxmDto.setLrry(fzjcxxDto.getLrry());
                            fzjcxmDto.setZt(StatusEnum.CHECK_NO.getCode());
                            fzjcxmDtos.add(fzjcxmDto);
                        }
                    }
                }
            }
            fzjcxmPjService.insertListJcxmAndjczxm(fzjcxmDtos);
            fzjcxxDto.setAddFzjcxmDtos(fzjcxmDtos);
            fzjcxxDto.setXms(String.valueOf(fzjcxmDtos.size()));
        }
        dao.insertDto(fzjcxxDto);
        return true;
    }

    /**
     * 修改分子检测信息
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean saveEditHPV(FzjcxxDto fzjcxxDto){
        List<JcsjDto> jcxmListall = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM));//所有分子检测项目
        List<JcsjDto> fzjczxmlistall = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_SUBITEM));
        fzjcxxDto.setBbztmc(getBbztmc(fzjcxxDto));

        String[] fzxmlist = fzjcxxDto.getFzxmids();
        //String[] fzjczxmids = fzjcxxDto.getFzjczxmids();
        StringBuilder jcxmmc = new StringBuilder();
        StringBuilder jcxmid = new StringBuilder();
        //JcsjDto jcsjDto = new JcsjDto();
        List<JcsjDto> zxmlist=new ArrayList<>();
        for (String fzjcxmid : fzxmlist) {
            for (JcsjDto jcsjDto_jcxm : jcxmListall) {
                if (jcsjDto_jcxm.getCsid().equals(fzjcxmid)) {
                    jcxmmc.append(",").append(jcsjDto_jcxm.getCsmc());
                    jcxmid.append(",").append(jcsjDto_jcxm.getCsid());
                }
            }
        }
        if(fzjcxxDto.getFzjczxmids()!=null && fzjcxxDto.getFzjczxmids().length>0){
            for(int j = 0; j < fzjcxxDto.getFzjczxmids().length; j++){
                for(JcsjDto jcsjDto_zxm:fzjczxmlistall){
                    if(jcsjDto_zxm.getCsid().equals(fzjcxxDto.getFzjczxmids()[j])){
                        zxmlist.add(jcsjDto_zxm);
                    }
                }
            }
        }
        if (StringUtil.isNotBlank(jcxmmc.toString())) {
            fzjcxxDto.setJcxmmc(jcxmmc.substring(1));
        }
        if (StringUtil.isNotBlank(jcxmid.toString())) {
            fzjcxxDto.setJcxmid(jcxmid.substring(1));
        }
        if(fzjcxxDto.getBbzts()!=null){
            fzjcxxDto.setPjbbzts(Arrays.asList(fzjcxxDto.getBbzts()));
        }
        fzbbztService.insertFzbbzt(fzjcxxDto);
        // 先删除向目标，再新增项目信息
        FzjcjgDto fzjcjgDto=new FzjcjgDto();
        fzjcjgDto.setFzjcid(fzjcxxDto.getFzjcid());
        List<FzjcjgDto> jglist=fzjcjgPJService.getDtoList(fzjcjgDto);
        //如果已产生检测结果则不修改检测项目
        if(CollectionUtils.isEmpty(jglist)){
            //项目数
            fzjcxxDto.setXms(String.valueOf(zxmlist.size()));
            FzjcxmDto fzjcxmDto_sel = new FzjcxmDto();
            fzjcxmDto_sel.setFzjcid(fzjcxxDto.getFzjcid());
            List<FzjcxmDto> fzjcxmDtos_sel = fzjcxmPjService.getDtoList(fzjcxmDto_sel);
            List<FzjcxmDto> fzjcxmDtos=new ArrayList<>();
            if (!CollectionUtils.isEmpty(fzjcxmDtos_sel)){
                List<FzjcxmDto> lsList = new ArrayList<>(fzjcxmDtos_sel);
                for (JcsjDto jcsjDto : zxmlist) {
                    //剩下的是删除的
                    fzjcxmDtos_sel.removeIf(e->jcsjDto.getCsid().equals(e.getFzjczxmid()));
                }
                for (FzjcxmDto fzjcxmDto : lsList) {
                    //剩下的是新增的
                    zxmlist.removeIf(e->e.getCsid().equals(fzjcxmDto.getFzjczxmid()));
                }
            }
            if (!CollectionUtils.isEmpty(zxmlist)){
                for (JcsjDto jcsjDto : zxmlist) {
                    FzjcxmDto fzjcxmDto = new FzjcxmDto();
                    fzjcxmDto.setFzxmid(StringUtil.generateUUID());
                    fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
                    fzjcxmDto.setFzjczxmid(jcsjDto.getCsid());
                    fzjcxmDto.setFzjcxmid(jcsjDto.getFcsid());
                    fzjcxmDto.setLrry(fzjcxxDto.getLrry());
                    fzjcxmDtos.add(fzjcxmDto);
                }
                fzjcxmPjService.insertListJcxmAndjczxm(fzjcxmDtos);
                fzjcxxDto.setAddFzjcxmDtos(fzjcxmDtos);
            }
            if (!CollectionUtils.isEmpty(fzjcxmDtos_sel)){
                List<String> ids = fzjcxmDtos_sel.stream().map(FzjcxmDto::getFzxmid).collect(Collectors.toList());
                FzjcxmDto fzjcxmDto_del = new FzjcxmDto();
                fzjcxmDto_del.setScry(fzjcxxDto.getXgry());
                fzjcxmDto_del.setIds(ids);
                fzjcxmPjService.delFzjcxmByIds(fzjcxmDto_del);
                fzjcxxDto.setDelIds(ids);
            }
        }
        dao.saveEditHPV(fzjcxxDto);
        List<FzjcxmDto> list=(List<FzjcxmDto>) JSON.parseArray(fzjcxxDto.getJcxm_json(),FzjcxmDto.class);
        if(!CollectionUtils.isEmpty(list)){
            fzjcxmPjService.updateFzjcxmDtos(list);
        }
        return true;
    }
    /**
     * 删除按钮
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delHPVDetection(FzjcxxDto fzjcxxDto){
        dao.delHPVDetection(fzjcxxDto);
        FzjcxmDto fzjcxmDto = new FzjcxmDto();
        fzjcxmDto.setIds(fzjcxxDto.getIds());
        fzjcxmDto.setScry(fzjcxxDto.getScry());
        fzjcxmPjService.delFzjcxmByFzjc(fzjcxmDto);
        return true;
    }
    @Override
    public List<FzjcxxDto> getPagedDetectPJlab(FzjcxxDto fzjcxxDto) {
        //将SQL的yblx_jcsj.csid = fzjcxx.yblx关联，获取yblx_jcsj.csmc yblxmc
        List<JcsjDto> yblxs = new ArrayList<>();
        List<JcsjDto> pjyblx = redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.GENERALSAMPLE_TYPE.getCode());
        List<JcsjDto> wxyblx = redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
        yblxs.addAll(pjyblx);
        yblxs.addAll(wxyblx);
        List<FzjcxxDto> fzjcxxDtos = dao.getPagedDetectPJlab(fzjcxxDto);
        for (FzjcxxDto fzjcxx : fzjcxxDtos){
            for (JcsjDto jc_yblx: yblxs) {
                if (jc_yblx.getCsid().equals(fzjcxx.getYblx()) ){
                    fzjcxx.setYblxmc(jc_yblx.getCsmc());
                    break;
                }
            }
        }
//        return dao.getPagedDetectPJlab(fzjcxxDto);
        return fzjcxxDtos;
    }

    @Override
    public List<FzjcxxDto> getPagedAuditList(FzjcxxDto fzjcxxDto) {
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if(fzjcxxDto.getJclx().equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        List<FzjcxxDto> t_sqList;
        if (fzjcxxDto.getFlag().equals("audit")) {
           t_sqList = dao.getPagedAuditIdList(fzjcxxDto);
           return t_sqList;
        }
        else {
            t_sqList = dao.getPagedAuditIdList(fzjcxxDto);
            if (CollectionUtils.isEmpty(t_sqList)){
                return t_sqList;
            }
            List<FzjcxxDto> sqList = dao.getAuditListByIds(t_sqList);
            commonservice.setSqrxm(sqList);
            return sqList;
        }
    }

    @Override
    public List<FjcfbDto> getReport(FjcfbDto fjcfbDto) {
        return dao.getReport(fjcfbDto);
    }

    @Override
    public List<FzjcxxDto> getFzjcxxListByYwids(List<ShgcDto> shgcList) {
        return dao.getFzjcxxListByYwids(shgcList);
    }

    /**
     * 查询样本编号是不是已经存在
     */
    public Integer getCountByybbh(FzjcxxDto fzjcxxDto)
    {
        return dao.getCountByybbh(fzjcxxDto);
    }
    /**
     * 样本类型转义
     */
    public String escapeYblx(FzjcxxDto fzjcxxDto){
        return dao.escapeYblx(fzjcxxDto);
    }
    /**
     * 接收人员转义
     */
    public String escapeJsry(FzjcxxDto fzjcxxDto){
        return dao.escapeJsry(fzjcxxDto);
    }
    /**
     * 实验人员转义
     */
    public String escapeSyry(FzjcxxDto fzjcxxDto){
        return dao.escapeSyry(fzjcxxDto);
    }
    /**
     * 检测项目转义
     */
    public String escapeJcxm(FzjcxxDto fzjcxxDto){
        return dao.escapeJcxm(fzjcxxDto);
    }
    /**
     * 检测子项目转义
     */
    public String escapeJczxm(FzjcxxDto fzjcxxDto){
        return dao.escapeJczxm(fzjcxxDto);
    }
    /**
     * 检测单位转义
     */
    public String escapeJcdw(FzjcxxDto fzjcxxDto){
        return dao.escapeJcdw(fzjcxxDto);
    }
    /**
     * 科室转义
     */
    public String escapeKs(FzjcxxDto fzjcxxDto){
        return dao.escapeKs(fzjcxxDto);
    }
    /**
     * 采集人员转义
     */
    public String escapeCjry(FzjcxxDto fzjcxxDto){
        return dao.escapeCjry(fzjcxxDto);
    }
    /**
     * 查询实验号
     */
    public String escapeSyh(FzjcxxDto fzjcxxDto){
        return dao.escapeSyh(fzjcxxDto);
    }
    /**
     * 送检单位转义
     */
    public String escapeSjdw(FzjcxxDto fzjcxxDto){
        return dao.escapeSjdw(fzjcxxDto);
    }

    @Override
    public boolean existCheck(String fieldName, String value) {
        return false;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages) throws BusinessException {
        FzjcxxDto fzjcxxDto=(FzjcxxDto)baseModel;
        if (StringUtil.isNotBlank(fzjcxxDto.getJclx())){
            List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
            if(jclxList.size()>0){
                for(JcsjDto jcsjDto : jclxList){
                    if(fzjcxxDto.getJclx().equals(jcsjDto.getCsdm()))
                        fzjcxxDto.setJclx(jcsjDto.getCsid());
                }
            }
        }
        int count=getCountByybbh(fzjcxxDto);
        if (count!=0) {
            log.error("有重复数据");
            throw new BusinessException("msg", "样本编号"+fzjcxxDto.getYbbh()+"重复，请尝试重新导入");//指明哪条数据重复
        }
        else{
            if (StringUtil.isNotBlank(fzjcxxDto.getYbbh())){
                fzjcxxDto.setNbbh(fzjcxxDto.getYbbh());
                fzjcxxDto.setBbzbh(fzjcxxDto.getYbbh());
                fzjcxxDto.setWybh(fzjcxxDto.getYbbh());
            }
            if (StringUtil.isNotBlank(fzjcxxDto.getYblxmc())){//样本类型名称判断整合，当传入的样本类型名称找得到就直接插入、如果找不到就将其他类型的参数id插入。并将填写的其他样本类型
                //插入到其他样本类型中
                String yblx=escapeYblx(fzjcxxDto);
                if (yblx!=null){
                    fzjcxxDto.setYblx(yblx);
                }
                else {
                    List<JcsjDto> yblxlists = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.GENERALSAMPLE_TYPE.getCode());
                    for (JcsjDto jcsj: yblxlists ) {
                        if ("1".equals(jcsj.getCskz1())){
                            fzjcxxDto.setYblx(jcsj.getCsid());
                        }
                    }
                }
            }
            //是否接收根据接收时间来判断
            if(StringUtil.isNotBlank(fzjcxxDto.getJssj())){
                fzjcxxDto.setSfjs("1");
            }
            else {
                fzjcxxDto.setSfjs("0");
            }
            if (StringUtil.isNotBlank(fzjcxxDto.getJsry())){
                String jsry=escapeJsry(fzjcxxDto);
                if (jsry!=null){
                    fzjcxxDto.setJsry(jsry);
                }
            }
        }
        if(StringUtil.isNotBlank(fzjcxxDto.getSysj())){
            fzjcxxDto.setSfsy("1");
        }
        else {
            fzjcxxDto.setSfsy("0");
        }
        if (StringUtil.isNotBlank(fzjcxxDto.getSyry())){
            String syry=escapeSyry(fzjcxxDto);
            if (syry!=null){
                fzjcxxDto.setSyry(syry);
            }
        }
        if (StringUtil.isNotBlank(fzjcxxDto.getJcdwmc())){//必填项
            String jcdw=escapeJcdw(fzjcxxDto);
            if (jcdw!=null){
                fzjcxxDto.setJcdw(jcdw);
            }
            else
                throw new BusinessException("msg", "检测单位"+fzjcxxDto.getJcdwmc()+"不存在");
        }
        if (StringUtil.isNotBlank(fzjcxxDto.getSjdwmc())){
            String sjdw=escapeSjdw(fzjcxxDto);
            if (sjdw!=null){
                fzjcxxDto.setSjdw(sjdw);
            }
            else {
                YyxxDto yyxxDto = new YyxxDto();
                yyxxDto.setCskz1("1");
                List<YyxxDto> yyxxlist = yyxxService.selectHospitalName(yyxxDto);
                if (yyxxlist!=null&&yyxxlist.size()>0){
                    YyxxDto yyxxDto1=yyxxlist.get(0);
                    fzjcxxDto.setSjdw(yyxxDto1.getDwid());
                }
            }
        }
        if (StringUtil.isNotBlank(fzjcxxDto.getKs())){
            String ks=escapeKs(fzjcxxDto);
            if (ks!=null){
                fzjcxxDto.setKs(ks);
            }
            else {
                fzjcxxDto.setQtks(fzjcxxDto.getKs());
                FzjcxxDto fzjcxxDto1=new FzjcxxDto();
                fzjcxxDto1.setKzcs("1");
                List<FzjcxxDto> fzjcxxDtoList=selectAllKs(fzjcxxDto1);
                if (fzjcxxDtoList!=null&&fzjcxxDtoList.size()>0)
                    fzjcxxDto.setKs(fzjcxxDtoList.get(0).getDwid());
            }

        }
        if (StringUtil.isNotBlank(fzjcxxDto.getCjry())){
            String cjry=escapeCjry(fzjcxxDto);
            if (cjry!=null){
                fzjcxxDto.setCjry(cjry);
            }
            else {
                fzjcxxDto.setWbcjry(fzjcxxDto.getCjry());
                fzjcxxDto.setCjry(null);
            }
        }
        if (StringUtil.isNotBlank(fzjcxxDto.getXb())){
            if (fzjcxxDto.getXb().equals("男")){
                fzjcxxDto.setXb("1");
            }
            else if (fzjcxxDto.getXb().equals("女")){
                fzjcxxDto.setXb("2");
            }
        }
        if (StringUtil.isNotBlank(fzjcxxDto.getSyh())){
            if (StringUtil.isNotBlank(escapeSyh(fzjcxxDto)))
                throw new BusinessException("msg","实验号"+fzjcxxDto.getSyh()+"重复");
        }

        fzjcxxDto.setFzjcid(StringUtil.generateUUID());
        fzjcxxDto.setLrry(user.getYhid());
        String jcxmmc=((FzjcxxDto) baseModel).getJcxmmc();
        //List<Map<String,Object>> mapList=new ArrayList<>();
        List<FzjcxmDto> list=new ArrayList<>();
        String[] str_jcxm=jcxmmc.split(";");
        List<JcsjDto> jcxmList = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.MOLECULAR_ITEM);
        List<JcsjDto> jczxmList = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.MOLECULAR_SUBITEM);
        StringBuilder str= new StringBuilder();
        for (String string : str_jcxm) {
            //Map<String,Object> map=new HashMap<>();
            String[] split = string.split(":");
            String jcxm=split[0];
            String jcxmid="";
            for (JcsjDto jcsj: jcxmList ) {
                if (jcxm.equals(jcsj.getCsmc())){
                    jcxmid=jcsj.getCsid();
                    str.append(",").append(jcxm);
                }
            }
            if (StringUtil.isNull(jcxmid)){
                throw new BusinessException("msg","没有"+jcxm+"此检测项目");
            }
            FzjcxxDto fzjcxxDto1=new FzjcxxDto();
            fzjcxxDto1.setJcxm(jcxm);
            int index1=split[1].indexOf("{");
            int index2=split[1].indexOf("}");
            split[1]= split[1].substring(index1+1,index2);
            String[] str_jczxm=split[1].split(",");
            for (String jczxm : str_jczxm){
                FzjcxmDto fzjcxmDto=new FzjcxmDto();
                fzjcxmDto.setFzjcxmid(jcxmid);
                for (JcsjDto jcsj: jczxmList ){
                    if (jczxm.equals(jcsj.getCsmc())){
                        fzjcxmDto.setFzjczxmid(jcsj.getCsid());
                        fzjcxmDto.setFcsid(jcsj.getFcsid());
                    }
                }
                if (StringUtil.isNull(fzjcxmDto.getFzjczxmid())){
                    throw new BusinessException("msg","没有"+jczxm+"此检测子项目");
                }
                if (!jcxmid.equals(fzjcxmDto.getFcsid())){
                    throw new BusinessException("msg",jcxm+"没有"+jczxm+"此检测子项目");
                }
                fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
                fzjcxmDto.setFzxmid(StringUtil.generateUUID());
                list.add(fzjcxmDto);
            }
        }
        boolean isSuccess= (fzjcxmPjService.insertListJcxmAndjczxm(list)) > 0;
        if (!isSuccess){
            throw new BusinessException("msg","插入检测项目失败");
        }
        if (StringUtil.isNotBlank(fzjcxxDto.getBbztmc())){
            List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.GENERAL_SAMPLESTATE.getCode());
            List<FzbbztDto> fzbbztList=new ArrayList<>();
            String bbztList=fzjcxxDto.getBbztmc();
            String[] str_bbzt=bbztList.split(",");
            for (String bbzt:str_bbzt){
                FzbbztDto fzbbztDto=new FzbbztDto();
                if(jclxList.size()>0){
                    for(JcsjDto jcsjDto : jclxList){
                        if(bbzt.equals(jcsjDto.getCsmc()))
                        {
                            fzbbztDto.setFzjcid(fzjcxxDto.getFzjcid());
                            fzbbztDto.setZt(jcsjDto.getCsid());
                            fzbbztList.add(fzbbztDto);
                        }
                    }
                    if (StringUtil.isNull(fzbbztDto.getZt())){
                        throw new BusinessException("msg","没有"+bbzt+"此标本状态");
                    }
                }
            }
            isSuccess=fzbbztDao.insertFzbbzt(fzbbztList);
            if (!isSuccess)
                throw new BusinessException("msg","插入标本状态失败");

        }
        if (StringUtil.isNotBlank(str.toString())) {
            str = new StringBuilder(str.substring(1));
            fzjcxxDto.setJcxmmc(str.toString());
        }
        insertDto(fzjcxxDto);
        return true;
    }

    @Override
    public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
        return null;
    }

    @Override
    public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
        return false;
    }

    @Override
    public boolean checkDefined(List<Map<String, String>> defined) {
        return true;
    }

    /**
     * 普检列表数据
     */
    @Override
    public List<FzjcxxDto> getPagedDtoList(FzjcxxDto fzjcxxDto){
        List<FzjcxxDto> list = dao.getPagedDtoList(fzjcxxDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GENERAL_INSPECTION.getCode(), "zt", "fzjcid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    /**
     * 得到检测结果的列表
     */

    public List<FzjcxxDto> getJg(FzjcxxDto fzjcxxDto){
        return dao.getJg(fzjcxxDto);
    }

    @Override
    public List<FzjcxxDto> selectAllKs(FzjcxxDto fzjcxxDto) {
        return dao.selectAllKs(fzjcxxDto);
    }
    /**
     * 得到标本状态
     */
    @Override
    public FzjcxxDto getBbzts (FzjcxxDto fzjcxxDto){
        StringBuilder bbzt= new StringBuilder();
        StringBuilder bbztmc= new StringBuilder();
        List<FzjcxxDto> fzjcxxDtos=dao.getBbzts(fzjcxxDto);
        for (FzjcxxDto dto : fzjcxxDtos) {
            if (StringUtil.isNotBlank(dto.getZt())) {
                bbzt.append(",").append(dto.getZt());
                bbztmc.append(",").append(dto.getBbztmc());
            }
        }
        if(bbzt.length()>0){
            bbzt = new StringBuilder(bbzt.substring(1));
            fzjcxxDto.setBbzt(bbzt.toString());
        }
        if(bbztmc.length()>0){
            bbztmc = new StringBuilder(bbztmc.substring(1));
            fzjcxxDto.setBbztmc(bbztmc.toString());
        }
        return fzjcxxDto;
    }

    public String getBbztmc(FzjcxxDto fzjcxxDto){
        StringBuilder result= new StringBuilder();
        if(fzjcxxDto.getBbzts()!=null){
            String[] bbzts = fzjcxxDto.getBbzts();
            List<JcsjDto> bbzttotal = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.GENERAL_SAMPLESTATE.getCode());//普检标本状态
            for (String csid : bbzts) {
                for (JcsjDto jcsjDto : bbzttotal) {
                    if (jcsjDto.getCsid().equals(csid)) {
                        result.append(jcsjDto.getCsmc()).append(",");
                    }
                }
            }
            if(result.length()>1){
                result = new StringBuilder(result.substring(0, result.length() - 1));
            }

        }
        return result.toString();
    }

    /**
     * 查询角色检测单位限制
     */
    public List<Map<String, String>> getJsjcdwByjsid(String jsid){
        return dao.getJsjcdwByjsid(jsid);
    }

    /**
     * rabbit接收保存普检信息
     * @param fzjcxxDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean rabbitSaveFzjcxx(FzjcxxDto fzjcxxDto){
        //判断样本是否存在
        FzjcxxDto dto = new FzjcxxDto();
        dto.setFzjcid(fzjcxxDto.getFzjcid());
        FzjcxxDto exsit_dto = dao.getDto(dto);
        if (exsit_dto != null && StringUtil.isNotBlank(exsit_dto.getFzjcid())){
            //存在
            int update = dao.saveEditHPV(fzjcxxDto);
            if (update <= 0){
                return false;
            }
        }else {
            //不存在
            int insert = dao.insertDto(fzjcxxDto);
            if (insert <= 0){
                return false;
            }
        }
        FzjcxmDto fzjcxmDto = new FzjcxmDto();
        fzjcxmDto.setIds(fzjcxxDto.getFzjcid());
        fzjcxmPjService.delFzjcxmByFzjc(fzjcxmDto);
        List<FzjcxmDto> fzjcxmDtos = (List<FzjcxmDto>) JSON.parseArray(fzjcxxDto.getJcxm(), FzjcxmDto.class);
        fzjcxmPjService.insertListJcxmAndjczxm(fzjcxmDtos);
        return true;
    }
    @Override
    public boolean updatePjBgrqAndBgwcs(FzjcxxDto fzjcxxDto) {
        return dao.updatePjBgrqAndBgwcs(fzjcxxDto);
    }

    @Override
    public  void analysisFluFile(Map<String, Object> map) throws IOException {
        JSONArray fjids = JSONArray.parseArray(map.get("fjids").toString());
        String fzjcxmid = map.get("fzjcxmid").toString();
        String fzjczxmid = map.get("fzjczxmid").toString();
        String jclx = map.get("jclx").toString();
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        List<JcsjDto> jcsjDtos = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.GENERAL_RESULT));//所有普检结果
        List<JcsjDto> pjjgList = new ArrayList<>();
        if(jcsjDtos!=null&&!jcsjDtos.isEmpty()){
            for(JcsjDto dto:jcsjDtos){
                if(fzjczxmid.equals(dto.getFcsid())){
                    pjjgList.add(dto);
                }
            }
        }
        if(!CollectionUtils.isEmpty(jclxList)){
            for(JcsjDto dto:jclxList){
                if(jclx.equals(dto.getCsdm())){
                    jclx=dto.getCsid();
                    break;
                }
            }
        }
        //开始循环解析文件
        for(Object fjid:fjids){
            String glid=StringUtil.generateUUID();
            Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+fjid.toString());
            String wjm=(String)mFile.get("wjm"); //文件名
            String wjmhz=wjm.substring(wjm.lastIndexOf("."), wjm.length());
            String wjlj=(String)mFile.get("wjlj");//加密后的文件路径
            Map<String,List<FzjcjgDto>> list=new HashMap<>();
            if(".xlsx".equals(wjmhz)) {
                list = analysisXSSFWorkbook(wjlj, fzjcxmid, fzjczxmid, jclx,pjjgList);
            }else if(".xls".equals(wjmhz)) {
                list = analysisHSSFWorkbook(wjlj,fzjcxmid,fzjczxmid,jclx,pjjgList);
            }
            List<FzzkjgDto> fzzkjgDtos=new ArrayList<>();
            List<FzjcjgDto> fzjcjgDtos=new ArrayList<>();
            if(list!=null && !list.isEmpty()){
                for(Map.Entry<String, List<FzjcjgDto>> entry:list.entrySet()){
                    if(!CollectionUtils.isEmpty(entry.getValue())){
                        List<FzjcjgDto> t_list= (List<FzjcjgDto>) entry.getValue();
                        if(!CollectionUtils.isEmpty(t_list)){
                            dealFzjcjgData(t_list,glid,pjjgList,fzzkjgDtos,fzjcjgDtos);
                        }
                    }
                }
            }
            redisUtil.set("FLU_RESULT_UP:"+fjid, JSONObject.toJSONString(fzjcjgDtos),3600L);
            log.error("FLU_RESULT_UP附件ID :"+fjid+"的解析文件已上传redis");
            redisUtil.set("FLU_QUALITY_CONTROL_RESULT_UP:"+fjid, JSONObject.toJSONString(fzzkjgDtos),3600L);
            log.error("FLU_QUALITY_CONTROL_RESULT_UP附件ID :"+fjid+"的解析文件已上传redis");
        }
    }

    public Map<String,List<FzjcjgDto>> analysisXSSFWorkbook(String wjlj, String fzjcxmid, String fzjczxmid, String jclx, List<JcsjDto> pjjgList) throws IOException {
        Map<String,List<FzjcjgDto>> fzjcjgmap=new HashMap<>();
        List<FzjcjgDto> ptfzjcjgDtos=new ArrayList<>();
        List<FzjcjgDto> qcfzjcjgDtos=new ArrayList<>();
        List<FzjcjgDto> ncfzjcjgDtos=new ArrayList<>();
        fzjcjgmap.put("ptlist",ptfzjcjgDtos);//初始化，存放普通的检测结果数据
        fzjcjgmap.put("qclist",qcfzjcjgDtos);//初始化，存放QC的检测结果数据
        fzjcjgmap.put("nclist",ncfzjcjgDtos);//初始化，存放NC的检测结果数据
        FileInputStream fip = null;
        XSSFWorkbook book = null;
        try {
	    	//1.建立输入流
	        fip = new FileInputStream(wjlj);
	        //2.在输入流中获取Excel工作簿
	        book = new XSSFWorkbook(fip);
	        //3.获取工作簿中要读取的工作表,可以通过工作表工作表的名称来获取
	        XSSFSheet sheet = book.getSheet("Results");
	        
	        if(sheet == null)
	        	return fzjcjgmap;
	        //4.获取工作表中数据的总行数
	        int n = sheet.getPhysicalNumberOfRows();
            Map<String,JcsjDto> m_pjjgList = new HashMap<String,JcsjDto>();
            //先梳理基础数据，只获取相应的检测子项目的结果信息
            if(pjjgList!=null && !CollectionUtils.isEmpty(pjjgList)){
                for(JcsjDto dto:pjjgList){
                    m_pjjgList.put(dto.getCskz2(), dto);
                }
            }
	        FzjcxxDto pre_fzjcxxDto = null;
	        FzjcxmDto pre_fzjcxmDto = null;
            Map<String,String> bbmap=new HashMap<>();
	        //5.循环逐行读取，打印输出
	        for (int i = 1; i <n ; i++) {
	            //读取第i行
	            XSSFRow row = sheet.getRow(i);
	            if(row.getCell(1)!=null&&StringUtil.isNotBlank(row.getCell(1).getStringCellValue())){

	                FzjcjgDto fzjcjgDto=new FzjcjgDto();
	                String syh =row.getCell(1).getStringCellValue();;//内部编号
                    if(StringUtil.isBlank(syh))
                        continue;
                    fzjcjgDto.setSyh(syh);
                    fzjcjgDto.setFzjcjgid(StringUtil.generateUUID());
                    String key=syh+"-"+row.getCell(4).getStringCellValue();
                    if(row.getCell(4)!=null&&StringUtil.isNotBlank(key)){
                        if(StringUtil.isNotBlank(bbmap.get(key))){
                            String value=String.valueOf(Integer.valueOf(bbmap.get(key))+1);
                            bbmap.put(key,value);//记录是通道第几次出现
                        }else{
                            bbmap.put(key,"1");//该标本该通道通道第一次出现
                        }

                        String jgdm = bbmap.get(key)+","+row.getCell(4).getStringCellValue();//普检结果参数代码
                        fzjcjgDto.setJgdm(jgdm);
                        fzjcjgDto.setTd(row.getCell(4).getStringCellValue());
                        if(m_pjjgList!=null ){
                            JcsjDto dto = m_pjjgList.get(jgdm);
                            if(dto!=null) {
                                fzjcjgDto.setJgid(dto.getCsid());
                                fzjcjgDto.setJgmc(dto.getCsmc());
                                fzjcjgDto.setJgcskz1(dto.getCskz2());
                            }
                        }
                    }

                    if(row.getCell(6)!=null){
                        String ctz ="";
                        try{
                            ctz = String.valueOf(row.getCell(6).getNumericCellValue());//CT值
                        }catch (Exception e){
                            fzjcjgDto.setCtz(ctz);
                        }
                        fzjcjgDto.setCtz(ctz);
                    }

	                if("QC".equals(syh)){
                        qcfzjcjgDtos.add(fzjcjgDto);
	                }else if("NC".equals(syh)){
                        ncfzjcjgDtos.add(fzjcjgDto);
                    }else{
	                	FzjcxxDto fzjcxxDto_t = null;
	                	if(pre_fzjcxxDto!=null && syh.equals(pre_fzjcxxDto.getSyh())) {
	                		fzjcxxDto_t = pre_fzjcxxDto;
	                	}else {
	                		FzjcxxDto fzjcxxDto=new FzjcxxDto();
		                    fzjcxxDto.setJclx(jclx);
                            fzjcxxDto.setSyh(syh);
		                    fzjcxxDto_t = dao.getDto(fzjcxxDto);
		                    pre_fzjcxxDto = fzjcxxDto_t;
	                	}
	                    if(fzjcxxDto_t==null){
	                    	continue;
	                    }
                        fzjcjgDto.setFzjcid(fzjcxxDto_t.getFzjcid());
                        fzjcjgDto.setSyh(syh);
                        fzjcjgDto.setSczt("80");
                        
                        FzjcxmDto fzjcxmDto_t = null;
                        if(pre_fzjcxmDto!=null && fzjcxxDto_t.getFzjcid().equals(pre_fzjcxmDto.getFzjcid())
                        		&& fzjcxmid.equals(pre_fzjcxmDto.getFzjcxmid())
                        		&& fzjczxmid.equals(pre_fzjcxmDto.getFzjczxmid())) {
                        	fzjcxmDto_t = pre_fzjcxmDto;
                        }else {
                        	FzjcxmDto fzjcxmDto=new FzjcxmDto();
                            fzjcxmDto.setFzjcid(fzjcxxDto_t.getFzjcid());
                            fzjcxmDto.setFzjcxmid(fzjcxmid);
                            fzjcxmDto.setFzjczxmid(fzjczxmid);
                            fzjcxmDto_t = fzjcxmPjService.getDto(fzjcxmDto);
                            pre_fzjcxmDto = fzjcxmDto_t;
                        }
                        
                        if(fzjcxmDto_t!=null){
                            fzjcjgDto.setFzxmid(fzjcxmDto_t.getFzxmid());
                            fzjcjgDto.setJcxmmc(fzjcxmDto_t.getJcxmmc());
                            fzjcjgDto.setJczxmmc(fzjcxmDto_t.getJczxmmc());
                        }else{
                            fzjcjgDto.setSczt("15");
                        }
                        ptfzjcjgDtos.add(fzjcjgDto);
	                }
	            }
	        }
        }catch(Exception e) {
        	log.error(e.getMessage());
        	throw e;
        }finally {
        	if(book !=null) {
        		book.close();
        	}
        	if(fip != null)
        	{
                //关闭输入流
                fip.close();
        	}
        }
        return fzjcjgmap;
    }

    public Map<String,List<FzjcjgDto>> analysisHSSFWorkbook(String wjlj,String fzjcxmid,String fzjczxmid,String jclx,List<JcsjDto> pjjgList) throws IOException {
        Map<String,List<FzjcjgDto>> fzjcjgmap=new HashMap<>();
        List<FzjcjgDto> ptfzjcjgDtos=new ArrayList<>();
        List<FzjcjgDto> qcfzjcjgDtos=new ArrayList<>();
        List<FzjcjgDto> ncfzjcjgDtos=new ArrayList<>();
        fzjcjgmap.put("ptlist",ptfzjcjgDtos);//初始化，存放普通的检测结果数据
        fzjcjgmap.put("qclist",qcfzjcjgDtos);//初始化，存放QC的检测结果数据
        fzjcjgmap.put("nclist",ncfzjcjgDtos);//初始化，存放NC的检测结果数据
        FileInputStream fip = null;
        HSSFWorkbook book = null;
        try {
	        //1.建立输入流
	        fip = new FileInputStream(wjlj);
	        //2.在输入流中获取Excel工作簿
	        book = new HSSFWorkbook(fip);
	        //3.获取工作簿中要读取的工作表,可以通过工作表工作表的名称来获取
	        HSSFSheet sheet = book.getSheet("Results");
	        if(sheet == null)
	        	return fzjcjgmap;
	        //4.获取工作表中数据的总行数
	        int n = sheet.getPhysicalNumberOfRows();
	        Map<String,JcsjDto> m_pjjgList = new HashMap<String,JcsjDto>();
	        //先梳理基础数据，只获取相应的检测子项目的结果信息
	        if(pjjgList!=null && !CollectionUtils.isEmpty(pjjgList)){
                for(JcsjDto dto:pjjgList){
                    m_pjjgList.put(dto.getCskz2(), dto);
                }
            }
	        FzjcxxDto pre_fzjcxxDto = null;
	        FzjcxmDto pre_fzjcxmDto = null;
            Map<String,String> bbmap=new HashMap<>();
	        //5.循环逐行读取，打印输出
	        for (int i = 8; i <n ; i++) {
	            HSSFRow row = sheet.getRow(i);
	            if(row.getCell(1)!=null&&StringUtil.isNotBlank(row.getCell(1).getStringCellValue())){
	                FzjcjgDto fzjcjgDto=new FzjcjgDto();
	                String syh =row.getCell(1).getStringCellValue();;//内部编号
                    if(StringUtil.isBlank(syh))
                        continue;
                    fzjcjgDto.setSyh(syh);
                    fzjcjgDto.setFzjcjgid(StringUtil.generateUUID());
                    String key=syh+"-"+row.getCell(4).getStringCellValue();
                    if(row.getCell(4)!=null&&StringUtil.isNotBlank(key)){
                        if(StringUtil.isNotBlank(bbmap.get(key))){
                            String value=String.valueOf(Integer.valueOf(bbmap.get(key))+1);
                            bbmap.put(key,value);//记录是通道第几次出现
                        }else{
                            bbmap.put(key,"1");//该标本该通道通道第一次出现
                        }

                        String jgdm = bbmap.get(key)+","+row.getCell(4).getStringCellValue();//普检结果参数代码
                        fzjcjgDto.setJgdm(jgdm);
                        fzjcjgDto.setTd(row.getCell(4).getStringCellValue());
                        if(m_pjjgList!=null ){
                            JcsjDto dto = m_pjjgList.get(jgdm);
                            if(dto!=null) {
                                fzjcjgDto.setJgid(dto.getCsid());
                                fzjcjgDto.setJgmc(dto.getCsmc());
                                fzjcjgDto.setJgcskz1(dto.getCskz2());
                            }
                        }
                    }
                    if(row.getCell(6)!=null){
                        String ctz ="";
                        try{
                            ctz = String.valueOf(row.getCell(6).getNumericCellValue());//CT值
                        }catch (Exception e){

                        }
                        fzjcjgDto.setCtz(ctz);
                    }

	                if("QC".equals(syh)){
                        qcfzjcjgDtos.add(fzjcjgDto);
	                }else if("NC".equals(syh)){
                        ncfzjcjgDtos.add(fzjcjgDto);
                    }else{
	                	FzjcxxDto fzjcxxDto_t = null;
	                	if(pre_fzjcxxDto!=null && syh.equals(pre_fzjcxxDto.getSyh())) {
	                		fzjcxxDto_t = pre_fzjcxxDto;
	                	}else {
	                		FzjcxxDto fzjcxxDto=new FzjcxxDto();
		                    fzjcxxDto.setJclx(jclx);
                            fzjcxxDto.setSyh(syh);
		                    fzjcxxDto_t = dao.getDto(fzjcxxDto);
		                    pre_fzjcxxDto = fzjcxxDto_t;
	                	}
	                    if(fzjcxxDto_t==null){
	                    	continue;
	                    }
                        fzjcjgDto.setFzjcid(fzjcxxDto_t.getFzjcid());
                        //fzjcjgDto.setSyh(syh);
                        fzjcjgDto.setSczt("80");
                        
                        FzjcxmDto fzjcxmDto_t = null;
                        if(pre_fzjcxmDto!=null && fzjcxxDto_t.getFzjcid().equals(pre_fzjcxmDto.getFzjcid())
                        		&& fzjcxmid.equals(pre_fzjcxmDto.getFzjcxmid())
                        		&& fzjczxmid.equals(pre_fzjcxmDto.getFzjczxmid())) {
                        	fzjcxmDto_t = pre_fzjcxmDto;
                        }else {
                        	FzjcxmDto fzjcxmDto=new FzjcxmDto();
                            fzjcxmDto.setFzjcid(fzjcxxDto_t.getFzjcid());
                            fzjcxmDto.setFzjcxmid(fzjcxmid);
                            fzjcxmDto.setFzjczxmid(fzjczxmid);
                            fzjcxmDto_t = fzjcxmPjService.getDto(fzjcxmDto);
                            pre_fzjcxmDto = fzjcxmDto_t;
                        }
                        
                        if(fzjcxmDto_t!=null){
                            fzjcjgDto.setFzxmid(fzjcxmDto_t.getFzxmid());
                            fzjcjgDto.setJcxmmc(fzjcxmDto_t.getJcxmmc());
                            fzjcjgDto.setJczxmmc(fzjcxmDto_t.getJczxmmc());
                        }else{
                            fzjcjgDto.setSczt("15");
                        }
                        ptfzjcjgDtos.add(fzjcjgDto);
	                }
	            }
	        }
        }catch(Exception e) {
        	log.error(e.getMessage());
        	throw e;
        }finally {
        	if(book !=null) {
        		book.close();
        	}
        	if(fip != null)
        	{
                //关闭输入流
                fip.close();
        	}
        }
        return fzjcjgmap;
    }

    public void dealFzjcjgData(List<FzjcjgDto> list,String glid,List<JcsjDto> pjjgList,List<FzzkjgDto> fzzkjgDtos,List<FzjcjgDto> fzjcjgDtos){

        List<JcsjDto> jgList = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.MOLECULAR_DETECTION_RESULT.getCode());
        String negative ="";
        String positive ="";
        if(!CollectionUtils.isEmpty(jgList)){//获取阴阳性结果的csid用于后续结果判断的时候set
            for(JcsjDto dto:jgList){
                if("NEGATIVE".equals(dto.getCsdm())){
                    negative=dto.getCsid();
                }else if("POSITIVE".equals(dto.getCsdm())){
                    positive=dto.getCsid();
                }
            }
        }
        Map<String, List<FzjcjgDto>> listMap = list.stream().collect(Collectors.groupingBy(FzjcjgDto::getSyh));
        if (!CollectionUtils.isEmpty(listMap)){
            Iterator<Map.Entry<String, List<FzjcjgDto>>> entries = listMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String,  List<FzjcjgDto>> entry = entries.next();

                String key=entry.getKey();
                List<FzjcjgDto> resultList = entry.getValue();
                //第一步：阴阳性质控结果判读
                //阳性质控品(QC)	FAM通道（甲流）Ct值≤36	  VIC通道（乙流）Ct值≤36	TEXAS RED通道（内标）Ct值≤42
                //阴性质控品(NC )	FAM通道（甲流）Undetermined或N/A或No Ct	VIC通道（乙流）Undetermined或N/A或No Ct	TEXAS RED通道（内标）Ct值≤42
                if(!CollectionUtils.isEmpty(resultList)){
                    Map<String, PjjgpdDto> ctMap = pjjgList.stream().collect(Collectors.toMap(JcsjDto::getCskz2, e->new PjjgpdDto(0,"","内标".equals(e.getCskz1())?"42":"36",e.getCskz2(),e.getCskz1(),e.getCsid())));
                    for(FzjcjgDto dto:resultList){
                        PjjgpdDto pjjgpdDto = ctMap.get(dto.getJgdm());
                        if (pjjgpdDto!=null){
                            if(StringUtil.isNotBlank(dto.getCtz())){
                                pjjgpdDto.setCt(dto.getCtz());
                            }
                        }
                    }
                    boolean isAllBankNutNb = true; //全部为空但是内标不为空
                    boolean isAllNotBank = true;
                    boolean isAllBank = true;
                    String nbCt = "";//内标Ct
                    String nbPdz = "";//内标判断值
                    for (String td : ctMap.keySet()) {
                        PjjgpdDto pjjgpdDto = ctMap.get(td);
                        if (StringUtil.isNotBlank(pjjgpdDto.getCt())&&!"内标".equals(pjjgpdDto.getPjjgcskz1())){
                            isAllBankNutNb = false;
                        }
                        if (StringUtil.isBlank(pjjgpdDto.getCt())){
                            isAllNotBank = false;
                        }else {
                            isAllBank = false;
                        }
                        if ("内标".equals(pjjgpdDto.getPjjgcskz1())){
                            nbCt = pjjgpdDto.getCt();
                            nbPdz = pjjgpdDto.getPdz();
                        }
                    }
                    if("QC".equals(key) || "NC".equals(key)){
                        for(FzjcjgDto dto:resultList){
                            FzzkjgDto fzzkjgDto=new FzzkjgDto();
                            fzzkjgDto.setFzzkjgid(StringUtil.generateUUID());
                            fzzkjgDto.setGlid(glid);
                            fzzkjgDto.setZkmc(key);
                            fzzkjgDto.setCtz(dto.getCtz());
                            fzzkjgDto.setTd(dto.getTd());
                            PjjgpdDto pjjgpdDto = ctMap.get(dto.getJgdm());
                            fzzkjgDto.setJgid(pjjgpdDto.getPjjgcsid());
                            fzzkjgDtos.add(fzzkjgDto);
                        }

                        //QC 时必须三个通道的CT值存在，否则实验无效，不保留任何数据
                        if("QC".equals(key)&&isAllNotBank){
                            for (String td : ctMap.keySet()) {
                                PjjgpdDto pjjgpdDto = ctMap.get(td);
                                BigDecimal ctBig = new BigDecimal(pjjgpdDto.getCt());
                                BigDecimal pdzBig = new BigDecimal(pjjgpdDto.getPdz());
                                if(ctBig.compareTo(pdzBig)>0){
                                    fzjcjgDtos=new ArrayList<>();
                                    break;
                                }
                            }
                            //NC时FAM和VIC通道必须为空，且内标通道ct必须小于等于42，否则都实验无效
                        }else if("NC".equals(key) && isAllBankNutNb && StringUtil.isNotBlank(nbCt)){
                            BigDecimal ctBig = new BigDecimal(nbCt);
                            BigDecimal pdzBig = new BigDecimal(nbPdz);
                            if (ctBig.compareTo(pdzBig)>0){
                                fzjcjgDtos=new ArrayList<>();
                                break;
                            }
                        }else{
                            fzjcjgDtos=new ArrayList<>();
                            break;
                        }
                    }else {
                        //第二步：标本结果判读
                        //FAM通道   |   VIC通道   |	TEXAS RED通道（内标）  |	结果判断
                        //Undetermined或N/A或No Ct   |	Undetermined或N/A或No Ct  |	Ct≤42  |	甲、乙型流感病毒阴性
                        //Ct≤42   | 	Undetermined或N/A或No Ct	  |   可能有S型曲线，或因高浓度的目的基因竞争抑制导致无S型曲线或No Ct（有无Ct皆可） |	甲型流感病毒阳性
                        //Undetermined或N/A或No Ct |	Ct≤42	|   可能有S型曲线，或因高浓度的目的基因竞争抑制导致无S型曲线或No Ct（有无Ct皆可） |	乙型流感病毒阳性
                        //Ct≤42  |	Ct≤42	|  可能有S型曲线，或因高浓度的目的基因竞争抑制导致无S型曲线或No Ct（有无Ct皆可）   |	甲、乙型流感病毒均阳性
                        //Undetermined或N/A或No Ct   |	Undetermined或N/A或No Ct  |	Undetermined或N/A或No Ct  |	该标本实验结果无效
                        //①若3个通道ct值都为空，则跳过后续处理，该标本实验结果无效
                        if(isAllBank){
                            continue;
                        }
                        for(FzjcjgDto dto:resultList){
                            PjjgpdDto pjjgpdDto = ctMap.get(dto.getJgdm());
                            if (pjjgpdDto!=null){
                                if(!"内标".equals(pjjgpdDto.getPjjgcskz1())){
                                    if(StringUtil.isNotBlank(dto.getCtz())){
                                        BigDecimal ctz=new BigDecimal(dto.getCtz());
                                        //②若FAM或者VIC通道有CT值，判定为低于检出限，报告为阴性，如≤42，则判定为阳性。
                                        if(ctz.compareTo(new BigDecimal("42"))<=0){
                                            dto.setJcjg(positive);
                                        }else{
                                            dto.setJcjg(negative);
                                        }
                                    }else {
                                        dto.setJcjg(negative);
                                    }
                                }
                            }
                        }
                        for(FzjcjgDto dto:resultList){
                            dto.setGlid(glid);
                        }
                        fzjcjgDtos.addAll(resultList);
                    }
                }
            }
        }
    }


    @Override
    public String generateYbbh(FzjcxxDto fzjcxxDto) {
        Map<String, String> infoMap = new HashMap<>();
        if (StringUtil.isNotBlank(fzjcxxDto.getFzjcid())){
            List<Map<String, String>> infoMaps = dao.getconfirmDmInfo(fzjcxxDto);
            if(CollectionUtils.isEmpty(infoMaps))
                return null;
            infoMap = infoMaps.get(0);
        } else if (StringUtil.isNotBlank(fzjcxxDto.getJclx())){
            List<JcsjDto> jcsjDtos = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
            List<JcsjDto> jcdwjcsjDtos = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
            for (JcsjDto jcsjDto : jcsjDtos) {
                if (jcsjDto.getCsdm().equals(fzjcxxDto.getJclx())){
                    infoMap.put("fzjclxcskz1", jcsjDto.getCskz1());
                    infoMap.put("fzjclxcskz2", jcsjDto.getCskz2());
                    break;
                }
            }
            for(JcsjDto jcdw:jcdwjcsjDtos){
                if (jcdw.getCsid().equals(fzjcxxDto.getJcdw())){
                    infoMap.put("jcdwdm", jcdw.getCsdm());
                    break;
                }
            }
        }
        /*----------获取流水号规则-------开始----------*/
        //分子检测类型cskz1
        //样本编号替换规则{key:length:isConditionOfSerial}=>{替换关键词:长度:是否为计算流水号的条件}
        //默认规则如下: 分子检测类型cskz2（2位） + 年份（2位） + 周数（2位） + 流水号（5位）
        String ybbhgz = "{jcdwdm:2:true}{fzjclxcskz2:2:true}{nf:2:true}{zs:2:true}{lsh:5:false}";
        if (StringUtil.isNotBlank(infoMap.get("fzjclxcskz1"))){
            ybbhgz = infoMap.get("fzjclxcskz1");
        }
        log.error("样本编号(新规则)规则："+ ybbhgz);
        /*----------获取流水号规则-------结束----------*/
        /*----------样本编号规则处理-----开始----------*/
        //1、根据规则获取样本编号长度：nbbmLength
        int ybbhLength = 0;//内部编码长度（累加）
        //2、流水号生成的查询条件（里面包含conditionList、startindex、seriallength、deafultserial）：serialMap
        Map<String, Object> serialMap = new HashMap<>();//流水号生成查询条件
        //3、样本编号规则中isConditionOfSerial为true的条件：conditionList
        List<Map<String,Object>> conditionList = new ArrayList<>();
        //4、样本编号字符串List
        List<Map<String,String>> ybbhStrList = new ArrayList<>();
        //5、流水号List
        List<Map<String,String>> serialStrList = new ArrayList<>();
        //6、循环替换ybbhgz中个关键词(除有关流水号的内容不替换)
        while (ybbhgz.contains("{") && ybbhgz.contains("}")) {
            Map<String,String> ybbhMap = new HashMap<>();
            int length = 0;//默认关键词长度为0
            boolean isConditionOfSerial = false;//默认 是否加入流水哈生成的判断条件 为false
            int startIndex = ybbhgz.indexOf("{");//获取开始下标
            int endIndex = ybbhgz.indexOf("}");//获取结束下标
            if (startIndex != 0){
                // 情况1：替换符前方有字符串；若“{”在不在首位
                // xxxx-{jcdwdm:2:true}
                // 截取“xxxx-”存入nbbmStrList中
                String valueStr = ybbhgz.substring(0,startIndex);
                endIndex = startIndex-1;
                ybbhMap.put("key","string");//设置关键词为string
                ybbhMap.put("value",valueStr);//设置对应值
                ybbhMap.put("index", String.valueOf(ybbhLength));//设置位置（前面所有字符的长度）
                length = valueStr.length();
                ybbhMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
            }else {
                // 情况1：替换符前方无字符串；若“{”在首位
                // {jcdwdm:2:true}
                //截取{}内的内容
                String key = ybbhgz.substring(startIndex + 1, endIndex);//jcdwdm:2:true
                //若截取内容内有“:”则分割；
                //若截取内容内无“:”，则默认key为截取内容，长度为其map中长度，默认是否加入流水哈生成的判断条件为false
                if (key.contains(":")) {
                    String[] split = key.split(":");//例：[jcdwdm,4,true]
                    key = split[0];//第一位为关键词key，例：jcdwdm
                    if (StringUtil.isNotBlank(split[1])){
                        try {
                            length = Integer.parseInt(split[1]);//第二位为对应关键词的长度，例：2
                        } catch (NumberFormatException e) {
                            log.error("样本编号(新规则)警告：key:"+key+"的length:"+split[1]+"不是数字");
                        }
                    }
                    //若有第三位，则第三位为是否加入流水哈生成的判断条件，例：true
                    //若无第三位，默认为false
                    if (split.length >= 3){
                        isConditionOfSerial = "true".equalsIgnoreCase(split[2]);
                    }
                }
                ybbhMap.put("key",key);//设置关键词
                //若为流水号，不做处理，
                if (("lsh").equals(key)){
                    ybbhMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
                    ybbhMap.put("value","");//设置值为空（后面处理）
                    ybbhMap.put("index", String.valueOf(ybbhLength));//设置位置（前面所有字符的长度）
                    serialStrList.add(ybbhMap);//存入流水号List中
                } else {
                    //内部编码特殊处理
                    dealBhKeyMap(key, fzjcxxDto, infoMap);
                    String valueStr = infoMap.get(key);//从infoMap中获取对应值，例：jcdwdm对应为01
                    if (StringUtil.isNotBlank(valueStr)){
                        valueStr = valueStr.substring(valueStr.length()-length);//将值截取至对应长度。例：截取01的后2位（01为对应值，2为对应值长度）（默认从末尾开始截取长度，若需要从前面开始截取，则需要增加特殊key,并在dealNbbmKeyMap方法中处理）
                        length = valueStr.length();//获取对应值最终长度（针对没设长度的时候）
                        ybbhMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
                        ybbhMap.put("value",valueStr);//设置值
                        ybbhMap.put("index", String.valueOf(ybbhLength));//设置位置（前面所有字符的长度）
                    }
                }
                if (isConditionOfSerial){
                    Map<String,Object> map = new HashMap<>();
                    map.put("conditionIndex",ybbhLength+1);
                    map.put("conditionLength",length);
                    map.put("conditionKey",ybbhMap.get("value"));
                    conditionList.add(map);
                }
            }
            ybbhStrList.add(ybbhMap);//将截取替换内容添加到list
            ybbhgz = ybbhgz.substring(endIndex+1);//截取剩余分规则
            ybbhLength += length;//内部编码长度累加
        }
        //7、若内部编码规则最后还有字符串，则进行处理
        if(ybbhgz!=null&&ybbhgz.length()>0){
            Map<String,String> ybbhMap = new HashMap<>();
            ybbhMap.put("key","string");//设置关键词为string
            ybbhMap.put("value",ybbhgz);//设置对应值
            ybbhMap.put("index", String.valueOf(ybbhLength));//设置位置（前面所有字符的长度）
            int length = ybbhgz.length();
            ybbhMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
            ybbhStrList.add(ybbhMap);//将截取替换内容添加到list
            ybbhLength += length;//内部编码长度累加
        }
        //8、遍历流水号List,循环生成对应的流水号
        for (Map<String, String> ybbhInfo : serialStrList) {
            serialMap.put("startindex", Integer.parseInt(ybbhInfo.get("index"))+1);//流水号开始位置
            serialMap.put("seriallength", Integer.parseInt(ybbhInfo.get("length")));//流水号长度
            serialMap.put("ybbhLength", ybbhLength);//内部编码长度
            serialMap.put("conditionList",conditionList);//条件List
            serialMap.put("jcdm", infoMap.get("jcdm"));//检测项目cskz1（区分F项目和其他项目）
            //生成获取流水号
            String customSerial = dao.getCustomSerial(serialMap);
            ybbhInfo.put("value",customSerial);
        }
        //拼接list获取完整内部编码
        String ybbh = "";
        for (Map<String, String> ybbhStr : ybbhStrList) {
            ybbh += ybbhStr.get("value");
        }
        return ybbh;
    }


    /**
     * 内部编码相关处理
     * @param key
     * @param fzjcxxDto
     * @param infoMap
     * @return
     */
    public Map<String, String> dealBhKeyMap(String key, FzjcxxDto fzjcxxDto, Map<String, String> infoMap){
        if ("nf".equals(key)){
            // 年份 处理
            dealNf(infoMap);
        }else if ("yf".equals(key)) {
            // 周数 处理
            dealYf(infoMap);
        }else if ("ts".equals(key)) {
            // 周数 处理
            dealTs(infoMap);
        }else if ("zs".equals(key)){
            // 周数 处理
            dealZs(infoMap);
        }
        return infoMap;
    }

    /**
     * 年份处理
     * @param infoMap
     */
    void dealNf(Map<String, String> infoMap){
        String year = new SimpleDateFormat("yyyy", Locale.CHINESE).format(new Date().getTime());
        infoMap.put("nf",year);
    }
    /**
     * 月份处理
     * @param infoMap
     */
    void dealYf(Map<String, String> infoMap){
        Calendar currentDate = Calendar.getInstance();
        // 获取当前月份
        int yf = currentDate.get(Calendar.MONTH) + 1;
        infoMap.put("yf", String.valueOf(yf));

    }
    /**
     * 天数处理
     * @param infoMap
     */
    void dealTs(Map<String, String> infoMap){
        Calendar currentDate = Calendar.getInstance();
        // 获取当前天数
        int ts = currentDate.get(Calendar.DAY_OF_MONTH);
        infoMap.put("ts", String.valueOf(ts));

    }

    /**
     * 年份处理 长度两位
     * @param infoMap
     */
    void dealZs(Map<String, String> infoMap){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);// 设置星期一为一周开始的第一天
        calendar.setTimeInMillis(System.currentTimeMillis());// 获得当前的时间戳
        String weekOfYear = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));// 获得当前日期属于今年的第几周
        if ("1".equals(weekOfYear) && calendar.get(Calendar.MONTH) == 11){
            calendar.add(Calendar.DATE, -7);
            weekOfYear = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)+1);// 获得当前日期属于今年的第几周
        }
        weekOfYear = weekOfYear.length()>1?weekOfYear:"0"+weekOfYear;
        infoMap.put("zs",weekOfYear);
    }
}

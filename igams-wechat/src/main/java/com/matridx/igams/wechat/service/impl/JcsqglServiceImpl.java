package com.matridx.igams.wechat.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.wechat.dao.entities.JcsqglDto;
import com.matridx.igams.wechat.dao.entities.JcsqglModel;
import com.matridx.igams.wechat.dao.entities.JcsqmxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxjgDto;
import com.matridx.igams.wechat.dao.post.IJcsqglDao;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.IJcsqglService;
import com.matridx.igams.wechat.service.svcinterface.IJcsqmxService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxjgService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.RandomUtil;
import com.matridx.springboot.util.file.upload.ZipUtil;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class JcsqglServiceImpl extends BaseBasicServiceImpl<JcsqglDto, JcsqglModel, IJcsqglDao> implements IJcsqglService, IAuditService {

    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    private IJcsqmxService jcsqmxService;
    @Autowired
    ICommonService commonService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.wechat.registerurl:}")
    private String registerurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Value("${matridx.fileupload.releasePath:}")
    private String releasePath;
    @Autowired
    IDdfbsglService ddfbsglService;
    @Autowired
    IDdspglService ddspglService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IShxxService shxxService;
    @Autowired
    ISjxxjgService sjxxjgService;
    @Autowired
    ISjxxService sjxxService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    RedisUtil redisUtil;
    @Value("${matridx.fileupload.tempPath}")
    private String tempFilePath;
    @Value("${matridx.file.exportFilePath}")
    private String exportFilePath;

    @Value("${matridx.wechat.menuurl:}")
    private String menuurl;

    @Autowired
    private IHbqxService hbqxService;

    @Autowired
    private ISjhbxxService sjhbxxService;

    @Autowired
    private IDdxxglService ddxxglService;
    private Logger log = LoggerFactory.getLogger(JcsqglServiceImpl.class);

    /**
     * 新增
     * @param jcsqglDto
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveDetectionApplication(JcsqglDto jcsqglDto) throws BusinessException {
        jcsqglDto.setYbxx(jcsqglDto.getYbxx().length()>1500?jcsqglDto.getYbxx().substring(0,1500):jcsqglDto.getYbxx());
        jcsqglDto.setWkxx(jcsqglDto.getWkxx().length()>1500?jcsqglDto.getWkxx().substring(0,1500):jcsqglDto.getWkxx());
        jcsqglDto.setSqglid(StringUtil.generateUUID());
        if("1".equals(jcsqglDto.getXzbj())){
            jcsqglDto.setZt(StatusEnum.CHECK_PASS.getCode());
        }else{
            jcsqglDto.setZt(StatusEnum.CHECK_NO.getCode());
        }

        if(jcsqglDto.getFjids()!=null && jcsqglDto.getFjids().size() > 0){
            if(!"dingding".equals(jcsqglDto.getFjbcbj())){
                for (int i = 0; i < jcsqglDto.getFjids().size(); i++) {
                    boolean saveFile = fjcfbService.save2RealFile(jcsqglDto.getFjids().get(i),jcsqglDto.getSqglid());
                    if(!saveFile){
                        log.error("附件保存失败");
                        return false;
                    }
                }
            }else{
                {
                    MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                    String fjids = org.apache.commons.lang.StringUtils.join(jcsqglDto.getFjids(), ",");
                    paramMap.add("fjids", fjids);
                    paramMap.add("ywid", jcsqglDto.getSqglid());
                    RestTemplate restTemplate = new RestTemplate();
                    String param;
                    param=restTemplate.postForObject(menuurl +"/wechat/getFileAddress", paramMap, String.class);

                    if(param!=null) {
                        JSONArray parseArray = JSONObject.parseArray(param);
                        for (int i = 0; i < parseArray.size(); i++) {
                            FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
                            fjcfbModel.setYwid(jcsqglDto.getSqglid());
                            // 下载服务器文件到指定文件夹
                            boolean isSuccess = fjcfbService.insert(fjcfbModel);
                            downloadFile(fjcfbModel);
                            if (!isSuccess)
                                return false;
                        }
                    }
                }
            }

        }
        //类型为清单
        if("0".equals(jcsqglDto.getLx())){
            List<JcsqmxDto> list = (List<JcsqmxDto>) JSON.parseArray(jcsqglDto.getSqmx_json(), JcsqmxDto.class);
            if(list!=null&&list.size()>0){
                for(JcsqmxDto jcsqmxDto:list){
                    jcsqmxDto.setSqglid(jcsqglDto.getSqglid());
                    jcsqmxDto.setSqmxid(StringUtil.generateUUID());
                    jcsqmxDto.setLrry(jcsqglDto.getLrry());
                }
                boolean result = jcsqmxService.insertList(list);
                if(!result){
                    log.error("检出申请明细插入失败！");
                    return false;
                }
            }
        }
        int insert = dao.insert(jcsqglDto);
        if(insert==0){
            log.error("检出申请管理插入失败！");
            return false;
        }
        //生成文库信息文件
        generateExcel(jcsqglDto);
        if("1".equals(jcsqglDto.getXzbj())){
            JcsqglDto t_jcsqglDto=dao.getDto(jcsqglDto);
            Thread thread=new Thread(){
                @Override
                public void run() {
                    try {
                        //生成压缩文件
                        List<Map<String,Object>> zipList=new ArrayList<>();//用于存放压缩包里的文件
                        dealZip(t_jcsqglDto,zipList,null);
                        String tempPath = exportFilePath + BusTypeEnum.IMP_DETECTION_APPLICATION_ZIP.getCode() + "/"
                                + "UP" + DateUtils.getCustomFomratCurrentDate("yyyy") + "/"
                                + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/"
                                + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
                        //        //压缩多个文件并下载
                        try(FileOutputStream fos = new FileOutputStream(tempPath+"/"+jcsqglDto.getSqglid()+".zip");
                            ZipOutputStream zos = new ZipOutputStream(fos)) {
                            // 创建 ZipEntry 对象
                            for (Map map:zipList){
                                if (StringUtil.isNotBlank((String) map.get("fileName"))){
                                    ZipEntry zipEntry =  new ZipEntry((String) map.get("fileName"));
                                    zos.putNextEntry(zipEntry);
                                    zos.write((byte[]) map.get("outByte"));
                                    zos.closeEntry();
                                }
                            }
                            redisUtil.hset(jcsqglDto.getSqglid(),"path",tempPath+"/"+jcsqglDto.getSqglid()+".zip",86400);
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            };
            thread.start();
        }

        return true;
    }

    public void pagedataDetectionZipDownload(HttpServletRequest request,HttpServletResponse httpResponse) throws FileNotFoundException {
        String filepath= (String) redisUtil.hget(request.getParameter("sqglid"),"path");
        if(StringUtil.isBlank(filepath)){
            log.error("文件不存在");
            return;
        }
        File file = new File(filepath);
        if(!file.exists()){
            log.error("文件不存在");
            return;
        }
        try(InputStream is = new FileInputStream(file);
            OutputStream os = httpResponse.getOutputStream()) {

            // 下载压缩包
            httpResponse.setContentType("application/zip");
            httpResponse.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(request.getParameter("sqglid")+".zip", "UTF-8"));
            // 传输文件数据
            byte[] buffer = new byte[4096]; // 使用4KB缓冲区
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//        jcsqglDto=dao.getDto(jcsqglDto);
//        List<Map<String,Object>> zipList=new ArrayList<>();//用于存放压缩包里的文件
//        dealZip(jcsqglDto,zipList,null);

//        //压缩多个文件并下载
//        try(ZipOutputStream zipOutputStream = new ZipOutputStream(httpResponse.getOutputStream()); OutputStream out =null) {
//            //下载压缩包
//            httpResponse.setContentType("application/zip");
//            httpResponse.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("检出信息.zip", "UTF-8"));
//            // 创建 ZipEntry 对象
//            for (Map map:zipList){
//                if (StringUtil.isNotBlank((String) map.get("fileName"))){
//                    ZipEntry zipEntry =  new ZipEntry((String) map.get("fileName"));
//                    zipOutputStream.putNextEntry(zipEntry);
//                    zipOutputStream.write((byte[]) map.get("outByte"));
//                }
//            }
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//        }
    }

    public void dealZip(JcsqglDto jcsqglDto,List<Map<String,Object>> zipList,ShgcDto shgcDto) throws BusinessException {
        if("0".equals(jcsqglDto.getLx())&&"JC".equals(jcsqglDto.getSqlxdm())){
            JcsqmxDto jcsqmxDto=new JcsqmxDto();
            jcsqmxDto.setSqglid(jcsqglDto.getSqglid());
            List<JcsqmxDto> dtoList = jcsqmxService.getDtoList(jcsqmxDto);
            if(dtoList!=null&&dtoList.size()>0){
                //异常消息
                StringBuilder exceptionMessage= new StringBuilder();
                String tempPath = exportFilePath + BusTypeEnum.IMP_DETECTION_APPLICATION_ZIP.getCode() + "/"
                        + "UP" + DateUtils.getCustomFomratCurrentDate("yyyy") + "/"
                        + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/"
                        + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
                String filePathId = System.currentTimeMillis() + RandomUtil.getRandomString();
                mkDirs(tempPath +"/" + filePathId);
                for(JcsqmxDto dto:dtoList){
                    SjxxjgDto sjxxjgDto=new SjxxjgDto();
                    sjxxjgDto.setIds(dto.getSjid());
                    List<SjxxjgDto> listByIds = sjxxjgService.getListByIds(sjxxjgDto);
                    if(listByIds!=null&&listByIds.size()>0){
                        for(SjxxjgDto sjxxjgDto1:listByIds){
                            if(StringUtil.isBlank(sjxxjgDto1.getXmdm())){
                                sjxxjgDto1.setXmdm("");
                            }
                        }
                        List<SjxxjgDto> newList=new ArrayList<>();
                        String sjid=listByIds.get(0).getSjid();
                        String xmdm=listByIds.get(0).getXmdm();
                        String excelName=listByIds.get(0).getHzxm()+"-"+listByIds.get(0).getYblxmc()+"-"+listByIds.get(0).getNbbm()+"-"+listByIds.get(0).getBgrq()+"-"+xmdm+".xlsx";

                        for(SjxxjgDto sjxxjgDto1:listByIds){
                            if(sjid.equals(sjxxjgDto1.getSjid())){
                                if(xmdm.equals(sjxxjgDto1.getXmdm())){
                                    extracted(newList, sjxxjgDto1);
                                }else{
                                    if(newList!=null&&newList.size()>0){
                                        zipList.add(generateExcel(excelName,newList,tempPath +"/" + filePathId));
                                    }
                                    newList=new ArrayList<>();
                                    xmdm=sjxxjgDto1.getXmdm();
                                    extracted(newList, sjxxjgDto1);
                                    excelName=sjxxjgDto1.getHzxm()+"-"+sjxxjgDto1.getYblxmc()+"-"+DateUtils.getCustomFomratCurrentDate("yyyyMMdd")+"-"+xmdm+".xlsx";
                                }
                            }else{
                                if(newList!=null&&newList.size()>0){
                                    zipList.add(generateExcel(excelName,newList,tempPath +"/" + filePathId));
                                }
                                newList=new ArrayList<>();
                                sjid=sjxxjgDto1.getSjid();
                                xmdm=sjxxjgDto1.getXmdm();
                                extracted(newList, sjxxjgDto1);
                                excelName=sjxxjgDto1.getHzxm()+"-"+sjxxjgDto1.getYblxmc()+"-"+DateUtils.getCustomFomratCurrentDate("yyyyMMdd")+"-"+xmdm+".xlsx";
                            }
                        }
                        if(!CollectionUtils.isEmpty(newList)){
                            zipList.add(generateExcel(excelName,newList,tempPath +"/" + filePathId));
                        }else{
                            exceptionMessage.append(","+dto.getHzxm());
                        }
                    }
                }
                if(exceptionMessage.length()>0){
                    exceptionMessage.deleteCharAt(0);
                    exceptionMessage.append(" 没有匹配到对应检出报告信息！");
                }
                if(shgcDto!=null){
                    //获取申请人信息
                    User user=new User();
                    user.setYhid(shgcDto.getSqr());
                    user=commonService.getUserInfoById(user);
                    if(user==null)
                        throw new BusinessException("ICOM99019","未获取到申请人信息！");
                    if(org.apache.commons.lang.StringUtils.isBlank(user.getDdid()))
                        throw new BusinessException("ICOM99019","未获取到申请人钉钉ID！");
                    if(org.apache.commons.lang.StringUtils.isBlank(user.getYhm()))
                        throw new BusinessException("ICOM99019","未获取到申请人用户名！");
                    if(zipList.size()>5){
                        //不可以用日期，一天内可能会存在多个检出申请
                        String papersZipName = filePathId +"-检出文件.zip";
                        ZipUtil.toZip(tempPath + "/" + filePathId, tempPath + "/" + papersZipName, true);
                        String mediaId = talkUtil.uploadFileToDingTalk(user.getYhm(),tempPath+"/"+papersZipName, papersZipName);
                        talkUtil.sendFileDyxxMessage(shgcDto.getShlb(),user.getYhid(),user.getYhm(),user.getDdid(),mediaId);
                    }else{
                        for(Map<String,Object> map:zipList){
                            String papersExcelName = (String) map.get("fileName");
                            String mediaId = talkUtil.uploadFileToDingTalk(user.getYhm(),tempPath + "/" + filePathId+"/"+papersExcelName, papersExcelName);
                            talkUtil.sendFileDyxxMessage(shgcDto.getShlb(),user.getYhid(),user.getYhm(),user.getDdid(),mediaId);
                        }
                    }
                    if(zipList.size()==0){
                        talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),user.getYhid(),user.getYhm(),user.getDdid(),"检出申请结果通知",jcsqglDto.getYbxx()+"没有检出数据");
                    }else{
                        talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),user.getYhid(),user.getYhm(),
                                user.getYhid(),
                                xxglService.getMsg("ICOMM_JC00001"),xxglService.getMsg("ICOMM_JC00002", DateUtils.getCustomFomratCurrentDate("HH:mm:ss"),exceptionMessage.toString()));
                    }
                }

            }

        }else if("0".equals(jcsqglDto.getLx())&&("FQ".equals(jcsqglDto.getSqlxdm())||"WZ".equals(jcsqglDto.getSqlxdm()))){//||"WZ".equals(jcsqglDto_t.getSqlxdm())
            JcsqmxDto jcsqmxDto=new JcsqmxDto();
            jcsqmxDto.setSqglid(jcsqglDto.getSqglid());
            List<JcsqmxDto> dtoList = jcsqmxService.getDtoList(jcsqmxDto);
            //List<String> wkbhList=new ArrayList<>();
            StringBuffer wkbhs = new StringBuffer("");
            if(dtoList!=null&&dtoList.size()>0){
                for(JcsqmxDto dto:dtoList){
                    wkbhs.append(dto.getWkbh()).append(",");
                }
                MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                Map<String,String>t_map=new HashMap<>();
                if(shgcDto!=null){
                    //获取申请人信息
                    User user=new User();
                    user.setYhid(shgcDto.getSqr());
                    user=commonService.getUserInfoById(user);
                    if(user==null)
                        throw new BusinessException("ICOM99019","未获取到申请人信息！");
                    if(org.apache.commons.lang.StringUtils.isBlank(user.getDdid()))
                        throw new BusinessException("ICOM99019","未获取到申请人钉钉ID！");
                    if(org.apache.commons.lang.StringUtils.isBlank(user.getYhm()))
                        throw new BusinessException("ICOM99019","未获取到申请人用户名！");

                    t_map.put("shlb",shgcDto.getShlb());
                    t_map.put("yhid",user.getYhid());
                    t_map.put("yhm",user.getYhm());
                    t_map.put("ddid",user.getDdid());
                    t_map.put("username",user.getZsxm());
                }
                //paramMap.add("uid_lst",jcsqglDto.getWkxx());
                paramMap.add("uid_lst",wkbhs);
                String title="Fastq申请通知";
                if ("WZ".equals(jcsqglDto.getSqlxdm())){
                    String wzxx=jcsqglDto.getWzxx();
                    paramMap.add("cn_name",wzxx);
                    title="物种申请通知";
                }
                t_map.put("title",title);
                paramMap.add("params",JSONObject.toJSONString(t_map));
                RestTemplate t_restTemplate = new RestTemplate();
                //发送文件到服务器
                try {
                    String json = t_restTemplate.postForObject("http://bcl.matridx.top/BCL/api/fastq_approval/?token=eba3f34c24943e1c", paramMap, String.class);
                    JSONObject jsonObject=JSON.parseObject(json);
                    if(jsonObject!=null){
                        if(jsonObject.get("detail")!=null&&StringUtil.isNotBlank(jsonObject.get("detail").toString())){
                            if(jsonObject.get("detail").toString().contains("created")){
                                log.error("生信同步到网盘成功"+JSONObject.toJSONString(paramMap));
                            }else{
                                log.error("生信同步到网盘出错"+JSONObject.toJSONString(paramMap));
                            }
                        }else{
                            log.error("生信同步到网盘出错"+JSONObject.toJSONString(paramMap));
                        }
                    }else{
                        log.error("生信同步到网盘出错"+JSONObject.toJSONString(paramMap));
                    }
                }catch (Exception e){
                    log.error("生信同步到网盘出错"+JSONObject.toJSONString(paramMap));
                }
            }
        }
    }

    /**
     * 生成检出文库文件
     * @param jcsqglDto
     */
    public void generateExcel(JcsqglDto jcsqglDto){
        String str = jcsqglDto.getWkxx(); // 假设的输入字符串，注意这里的逗号是中文逗号
        // 使用逗号分割字符串，得到数字数组
        String[] numbers = str.split(",");

        // 创建一个Workbook和Sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("library");
        // 正式文件名
        String releaseFilePath = releasePath + BusTypeEnum.IMP_LIBRARY_JCSQ.getCode() + "/" + "UP"
                + DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP"
                + DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/" + "UP"
                + DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
        // 文件名
        String fileName = System.currentTimeMillis() + RandomUtil.getRandomString() + ".xlsx";
        mkDirs(releaseFilePath);
        // 遍历数字数组，并将它们写入Excel文件的第一列
        for (int i = 0; i < numbers.length; i++) {
            Row row = sheet.createRow(i);
            Cell cell = row.createCell(0);
            cell.setCellValue(numbers[i]); // 假设所有项都是整数
        }

        // 写入文件
        try (FileOutputStream fileOut = new FileOutputStream(releaseFilePath+"/"+fileName)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        DBEncrypt bpe = new DBEncrypt();
        FjcfbDto fjcfbDto = new FjcfbDto();

        String fjid = StringUtil.generateUUID();
        fjcfbDto.setFjid(fjid);
        fjcfbDto.setYwid(jcsqglDto.getSqglid());
        fjcfbDto.setZywid("");
        fjcfbDto.setXh("1");
        fjcfbDto.setYwlx(BusTypeEnum.IMP_LIBRARY_JCSQ.getCode());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String wjm = "检出文库参考"+System.currentTimeMillis()+".xlsx";
        fjcfbDto.setWjm(wjm);
        fjcfbDto.setWjlj(bpe.eCode(releaseFilePath + "/" + fileName));
        fjcfbDto.setFwjlj(bpe.eCode(releaseFilePath));
        fjcfbDto.setFwjm(bpe.eCode(fileName));
        fjcfbDto.setZhbj("0");
        fjcfbService.insert(fjcfbDto);
    }

    /**
     * 修改
     * @param jcsqglDto
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveDetectionApplication(JcsqglDto jcsqglDto){
        if(jcsqglDto.getFjids()!=null && jcsqglDto.getFjids().size() > 0){
                if(!"dingding".equals(jcsqglDto.getFjbcbj())){
                    for (int i = 0; i < jcsqglDto.getFjids().size(); i++) {
                        boolean saveFile = fjcfbService.save2RealFile(jcsqglDto.getFjids().get(i),jcsqglDto.getSqglid());
                        if(!saveFile){
                            log.error("附件保存失败");
                            return false;
                        }
                    }
                }else{
                    {
                        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                        String fjids = org.apache.commons.lang.StringUtils.join(jcsqglDto.getFjids(), ",");
                        paramMap.add("fjids", fjids);
                        paramMap.add("ywid", jcsqglDto.getSqglid());
                        RestTemplate restTemplate = new RestTemplate();
                        String param;
                        param=restTemplate.postForObject(menuurl +"/wechat/getFileAddress", paramMap, String.class);

                        if(param!=null) {
                            JSONArray parseArray = JSONObject.parseArray(param);
                            for (int i = 0; i < parseArray.size(); i++) {
                                FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
                                fjcfbModel.setYwid(jcsqglDto.getSqglid());
                                // 下载服务器文件到指定文件夹
                                boolean isSuccess = fjcfbService.insert(fjcfbModel);
                                downloadFile(fjcfbModel);
                                if (!isSuccess)
                                    return false;
                            }
                        }
                    }
                }
        }
        //类型为清单
        if("0".equals(jcsqglDto.getLx())){
            List<JcsqmxDto> list = (List<JcsqmxDto>) JSON.parseArray(jcsqglDto.getSqmx_json(), JcsqmxDto.class);
            if(list!=null&&list.size()>0){
                JcsqmxDto jcsqmxDto_t=new JcsqmxDto();
                jcsqmxDto_t.setSqglid(jcsqglDto.getSqglid());
                jcsqmxDto_t.setScry(jcsqglDto.getXgry());
                jcsqmxService.delete(jcsqmxDto_t);
                for(JcsqmxDto jcsqmxDto:list){
                    jcsqmxDto.setSqglid(jcsqglDto.getSqglid());
                    jcsqmxDto.setSqmxid(StringUtil.generateUUID());
                    jcsqmxDto.setLrry(jcsqglDto.getLrry());
                }
                boolean result = jcsqmxService.insertList(list);
                if(!result){
                    log.error("检出申请明细插入失败！");
                    return false;
                }
            }
        }
        int update = dao.update(jcsqglDto);
        if(update==0){
            log.error("检出申请管理更新失败！");
            return false;
        }
        return true;
    }

    /**
     * 文件下载
     *
     * @param fjcfbModel
     * @return
     */
    public boolean downloadFile(FjcfbModel fjcfbModel)
    {
        String wjlj = fjcfbModel.getWjlj();
        String fwjlj = fjcfbModel.getFwjlj();
        DBEncrypt crypt = new DBEncrypt();
        String filePath = crypt.dCode(wjlj);
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("wjlj", wjlj);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try
        {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
            RestTemplate t_restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = t_restTemplate.exchange(menuurl + "/wechat/getImportFile", HttpMethod.POST, httpEntity, byte[].class);
            // 校验文件夹目录是否存在，不存在就创建一个目录
            mkDirs(crypt.dCode(fwjlj));
            byte[] result = response.getBody();
            inputStream = new ByteArrayInputStream(result);

            outputStream = new FileOutputStream(new File(filePath));

            int len;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1)
            {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally
        {
            try
            {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }
    /**
     * 删除
     * @param jcsqglDto
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delDetectionApplication(JcsqglDto jcsqglDto){
        JcsqmxDto jcsqmxDto=new JcsqmxDto();
        jcsqmxDto.setIds(jcsqglDto.getIds());
        jcsqmxDto.setScry(jcsqglDto.getScry());
        jcsqmxService.delete(jcsqmxDto);
        int delete = dao.delete(jcsqglDto);
        if(delete==0){
            log.error("检出申请管理删除失败！");
            return false;
        }
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwids(jcsqglDto.getIds());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_DETECTION_APPLICATION_FILE.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto);
        if(null != fjcfbDtos && fjcfbDtos.size() > 0){
            DBEncrypt dbEncrypt = new DBEncrypt();
            for(FjcfbDto dto : fjcfbDtos){
                //删除正式文件
                File file = new File(GlobalString.FILE_STOREPATH_PREFIX+dbEncrypt.dCode(dto.getWjlj()));
                file.delete();
            }
            fjcfbService.deleteByYwidsAndYwlx(fjcfbDto);
        }
        return true;
    }

    /**
     * 审核列表
     * @param jcsqglDto
     * @return
     */
    public List<JcsqglDto> getPagedAuditDetectionApplication(JcsqglDto jcsqglDto){
        // 获取人员ID和履历号
        List<JcsqglDto> t_sbyzList= dao.getPagedAuditDetectionApplication(jcsqglDto);

        if (t_sbyzList == null || t_sbyzList.size() == 0)
            return t_sbyzList;

        List<JcsqglDto> sqList = dao.getAuditListDetectionApplication(t_sbyzList);

        commonService.setSqrxm(sqList);

        return sqList;
    }

    @Override
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        JcsqglDto jcsqglDto = (JcsqglDto)baseModel;
        jcsqglDto.setXgry(operator.getYhid());
        return modSaveDetectionApplication(jcsqglDto);
    }

    @Override
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
//        String token = talkUtil.getToken();
        for (int j=0;j<shgcList.size();j++) {
            JcsqglDto jcsqglDto=new JcsqglDto();
            jcsqglDto.setSqglid(shgcList.get(j).getYwid());
            jcsqglDto.setXgry(operator.getYhid());
            JcsqglDto jcsqglDto_t = getDto(jcsqglDto);
            List<SpgwcyDto> spgwcyDtos = shgcList.get(j).getSpgwcyDtos();
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcList.get(j).getAuditState())) {
                jcsqglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (spgwcyDtos != null && spgwcyDtos.size() > 0) {
                    for (int i = 0; i < spgwcyDtos.size(); i++) {
                        if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcList.get(j).getShlb(),spgwcyDtos.get(i).getYhid(),spgwcyDtos.get(i).getYhm(),
                                    spgwcyDtos.get(i).getYhid(),
                                    xxglService.getMsg("ICOMM_SH00026"),xxglService.getMsg("ICOMM_SH00001",operator.getZsxm(),shgcList.get(j).getShlbmc() ,jcsqglDto_t.getSqlxmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcList.get(j).getAuditState())) {
                jcsqglDto.setZt(StatusEnum.CHECK_PASS.getCode());
                List<Map<String,Object>> zipList=new ArrayList<>();//用于存放压缩包里的文件
                dealZip(jcsqglDto_t,zipList,shgcList.get(j));
                if (spgwcyDtos != null && spgwcyDtos.size() > 0) {
                    int size = spgwcyDtos.size();
                    for (int i = 0; i < size; i++) {
                        if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcList.get(j).getShlb(),spgwcyDtos.get(i).getYhid(),spgwcyDtos.get(i).getYhm(), spgwcyDtos.get(i).getYhid(),
                                    xxglService.getMsg("ICOMM_SH00006"),xxglService.getMsg("ICOMM_SH00016",
                                            operator.getZsxm(),shgcList.get(j).getShlbmc() ,jcsqglDto_t.getSqlxmc(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }else {
                jcsqglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                if (shgcList.get(j).getXlcxh().equals("1")){
					try{
						Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "检出列表申请审批");//获取审批模板ID
						String templateCode=(String) template.get("message");
						//获取申请人信息(合同申请应该为采购部门)
						User user=new User();
                        user.setYhid(operator.getYhid());
						user=commonService.getUserInfoById(user);
						if(user==null)
							throw new BusinessException("ICOM99019","未获取到申请人信息！");
						if(org.apache.commons.lang.StringUtils.isBlank(user.getDdid()))
							throw new BusinessException("ICOM99019","未获取到申请人钉钉ID！");
						String userid=user.getDdid();
						String dept=user.getJgid ();
						Map<String,String> map= new HashMap<>();
						map.put("检出列表申请类型", jcsqglDto_t.getSqlxmc()!=null?jcsqglDto_t.getSqlxmc():"");
						map.put("合作伙伴",StringUtil.isNotBlank(jcsqglDto_t.getHb())?jcsqglDto_t.getHb():(StringUtil.isNotBlank(jcsqglDto_t.getHbxx())?jcsqglDto_t.getHbxx():"暂无"));
						map.put("申请原因",jcsqglDto_t.getSqyy()!=null?jcsqglDto_t.getSqyy():"");
						map.put("需求详细说明",jcsqglDto_t.getXqsm()!=null?jcsqglDto_t.getXqsm():"");
                        map.put("样本信息",jcsqglDto_t.getYbxx()!=null?jcsqglDto_t.getYbxx():"");
                        map.put("文库信息",jcsqglDto_t.getWkxx()!=null?jcsqglDto_t.getWkxx():"");
                        map.put("物种信息",jcsqglDto_t.getWzxx()!=null?jcsqglDto_t.getWzxx():"");
//                        map.put("附件","");
						map.put("所在部门",user.getJgid()!=null?"["+user.getJgid()+"]":"");


						Map<String,Object> t_map=talkUtil.createInstance(operator.getYhm(), templateCode, null, null, userid, dept, map,null,null);
						String str=(String) t_map.get("message");
						String status=(String) t_map.get("status");
						if("success".equals(status)) {
							@SuppressWarnings("unchecked")
							Map<String, Object> result_map = JSON.parseObject(str, Map.class);
							if (("0").equals(String.valueOf(result_map.get("errcode")))) {
								//保存至钉钉分布式管理表(主站)
								RestTemplate t_restTemplate = new RestTemplate();
								MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
								paramMap.add("ddslid", String.valueOf(result_map.get("process_instance_id")));
								paramMap.add("fwqm", urlPrefix);
								paramMap.add("cljg", "1");
								paramMap.add("fwqmc", "杰毅医检");
								paramMap.add("spywlx", shgcList.get(j).getShlb());
								paramMap.add("hddz", applicationurl);
                                paramMap.add("wbcxid", operator.getWbcxid());//存入外部程序id
                                JcsjDto jcsjDto_dd = new JcsjDto();
                                jcsjDto_dd.setCsdm(shgcList.get(j).getAuditType());
                                jcsjDto_dd.setJclb("DINGTALK_AUDTI_CALLBACK_TYPE");
                                jcsjDto_dd = jcsjService.getDtoByCsdmAndJclb(jcsjDto_dd);
                                if(StringUtil.isBlank(jcsjDto_dd.getCsmc()) || StringUtil.isBlank(jcsjDto_dd.getCskz1())) {
                                    throw new BusinessException("msg","请设置"+shgcList.get(j).getShlbmc()+"的钉钉审批回调类型基础数据！");
                                }
                                paramMap.add("ywmc", operator.getZsxm()+"提交的"+jcsjDto_dd.getCsmc());
                                paramMap.add("ywlx", templateCode);//这里存放的就是模板ID
                                paramMap.add("wbcxid", operator.getWbcxid());
//                                //分布式保留一份
								boolean t_result = t_restTemplate.postForObject(applicationurl + "/ws/purchase/saveDistributedMsg", paramMap, boolean.class);
								if (!t_result)
									return false;
								//主站保留一份
								if (StringUtils.isNotBlank(registerurl)) {
									boolean result = t_restTemplate.postForObject(registerurl + "/ws/purchase/saveDistributedMsg", paramMap, boolean.class);
									if (!result)
										return false;
								}
								//若钉钉审批提交成功，则关联审批实例ID
                                jcsqglDto.setDdslid(String.valueOf(result_map.get("process_instance_id")));
							} else {
								throw new BusinessException("msg", "发起钉钉审批失败!错误信息:" + t_map.get("message"));
							}
						}else {
                            throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+t_map.get("message"));
                        }
					} catch (BusinessException e) {
						// TODO: handle exception
						throw new BusinessException("msg",e.getMsg());
					}catch (Exception e) {
						// TODO: handle exception
						throw new BusinessException("msg","异常!异常信息:"+e.toString());
					}
				}else{
                    // 发送钉钉消息
                    if (spgwcyDtos != null && spgwcyDtos.size() > 0 ) {
                        try {
                            int size = spgwcyDtos.size();
                            for (int i = 0; i < size; i++) {
                                if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                                    talkUtil.sendWorkDyxxMessage(shgcList.get(j).getShlb(),shgcList.get(j).getSpgwcyDtos().get(i).getYhid(),shgcList.get(j).getSpgwcyDtos().get(i).getYhm(),
                                            shgcList.get(j).getSpgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00003"),StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SH00001"),
                                                    operator.getZsxm(), shgcList.get(j).getShlbmc(),jcsqglDto_t.getSqlxmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                //发送钉钉消息--取消审核人员
                if(shgcList.get(j).getNo_spgwcyDtos() != null && shgcList.get(j).getNo_spgwcyDtos().size() > 0){
                    int size = shgcList.get(j).getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcList.get(j).getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcList.get(j).getShlb(),shgcList.get(j).getSpgwcyDtos().get(i).getYhid(),shgcList.get(j).getNo_spgwcyDtos().get(i).getYhm(),
                                    shgcList.get(j).getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcList.get(j).getShlbmc() ,jcsqglDto_t.getSqlxmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            dao.update(jcsqglDto);
        }
        return true;
    }

    private void extracted(List<SjxxjgDto> newList, SjxxjgDto dto) {
        if("R".equals(dto.getJclx())){
            if("RNA".equals(dto.getWzfllx())){
                newList.add(dto);
            }
        }else if("D".equals(dto.getJclx())){
            if(!"RNA".equals(dto.getWzfllx())){
                newList.add(dto);
            }
        }else{
            newList.add(dto);
        }
    }

    @Override
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        // TODO Auto-generated method stub
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
//		String token = talkUtil.getToken();
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String sqglid = shgcDto.getYwid();
                JcsqglDto jcsqglDto=new JcsqglDto();
                jcsqglDto.setXgry(operator.getYhid());
                jcsqglDto.setSqglid(sqglid);
                jcsqglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(jcsqglDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String sqglid = shgcDto.getYwid();
                JcsqglDto jcsqglDto=new JcsqglDto();
                jcsqglDto.setXgry(operator.getYhid());
                jcsqglDto.setSqglid(sqglid);
                jcsqglDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(jcsqglDto);
                //OA取消审批的同时组织钉钉审批
				JcsqglDto jcsqglDto_t=dao.getDtoById(sqglid);
				if(jcsqglDto_t!=null && StringUtils.isNotBlank(jcsqglDto_t.getDdslid())) {
					Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhid(), jcsqglDto_t.getDdslid(), "", operator.getDdid());
					//若撤回成功将实例ID至为空
					String success=String.valueOf(cancelResult.get("message"));
					@SuppressWarnings("unchecked")
					Map<String,Object> result_map=JSON.parseObject(success,Map.class);
					Boolean bo1= (boolean) result_map.get("success");
					if(bo1)
						dao.updateDdslidToNull(jcsqglDto_t);
				}
            }
        }
        return true;
    }

    public Map<String,Object> generateExcel(String fileName,List<SjxxjgDto> list,String tempPath) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileOutputStream outputStream;
        HSSFWorkbook wb = null;
        try {
            outputStream = new FileOutputStream(tempPath+"/"+fileName);
            //创建工作sheet
            wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet();

            //第一行
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("TaxID");
            row0.createCell(1).setCellValue("Name");
            row0.createCell(2).setCellValue("ChineseName");
            row0.createCell(3).setCellValue("Rank");
//            if(StringUtil.isNotBlank(list.get(0).getTngs_flg())&&"1".equals(list.get(0).getTngs_flg())){
//                row0.createCell(4).setCellValue("RPM");
//            }else{
//                row0.createCell(4).setCellValue("ReadsAccum");
//            }
            row0.createCell(4).setCellValue("ReadsAccum");
            row0.createCell(5).setCellValue("Ratio(%)");
            row0.createCell(6).setCellValue("Coverage(%)");
            row0.createCell(7).setCellValue("GenusName");
//            if(StringUtil.isNotBlank(list.get(0).getTngs_flg())&&"1".equals(list.get(0).getTngs_flg())){
//                row0.createCell(8).setCellValue("GenusRPM");
//            }else{
//                row0.createCell(8).setCellValue("GenusReadsAccum");
//            }
            row0.createCell(8).setCellValue("GenusReadsAccum");
            row0.createCell(9).setCellValue("SubType");
            //row0.createCell(10).setCellValue("Interest");
            int count=1;
            for(SjxxjgDto sjxxjgDto:list){
                HSSFRow row = sheet.createRow(count);
                row.createCell(0).setCellValue(sjxxjgDto.getJdid()!=null?sjxxjgDto.getJdid():"");
                row.createCell(1).setCellValue(sjxxjgDto.getWzywm()!=null?sjxxjgDto.getWzywm():"");
                row.createCell(2).setCellValue(sjxxjgDto.getWzzwm()!=null?sjxxjgDto.getWzzwm():"");
                row.createCell(3).setCellValue(sjxxjgDto.getFldj()!=null?sjxxjgDto.getFldj():"");
//                if(StringUtil.isNotBlank(sjxxjgDto.getTngs_flg())&&"1".equals(sjxxjgDto.getTngs_flg())){
//                    row.createCell(4).setCellValue(sjxxjgDto.getDwds()!=null?sjxxjgDto.getDwds():"");
//                }else{
//                    row.createCell(4).setCellValue(sjxxjgDto.getZdqs()!=null?sjxxjgDto.getZdqs():"");
//                }
                row.createCell(4).setCellValue(sjxxjgDto.getZdqs()!=null?sjxxjgDto.getZdqs():"");
               // row.createCell(5).setCellValue(sjxxjgDto.getDwds()!=null?new BigDecimal(sjxxjgDto.getDwds()).divide(new BigDecimal("10000")).setScale(6,RoundingMode.HALF_UP) +"%":"");
                row.createCell(5).setCellValue(sjxxjgDto.getXdfd()==null?"": sjxxjgDto.getXdfd()+"%");
                row.createCell(6).setCellValue(sjxxjgDto.getJyzfgd()!=null?sjxxjgDto.getJyzfgd():"");
                row.createCell(7).setCellValue(sjxxjgDto.getSm()!=null?sjxxjgDto.getSm():"");
//                if(StringUtil.isNotBlank(sjxxjgDto.getTngs_flg())&&"1".equals(sjxxjgDto.getTngs_flg())){
//                    row.createCell(8).setCellValue(sjxxjgDto.getFdwds()!=null?sjxxjgDto.getFdwds():"");
//                }else{
//                    row.createCell(8).setCellValue(sjxxjgDto.getSdds()!=null?sjxxjgDto.getSdds():"");
//                }
                row.createCell(8).setCellValue(sjxxjgDto.getSdds()!=null?sjxxjgDto.getSdds():"");
                row.createCell(9).setCellValue(sjxxjgDto.getWzfl()!=null?sjxxjgDto.getWzfl():"");
                //row.createCell(10).setCellValue(sjxxjgDto.getGzd()!=null?sjxxjgDto.getGzd():"");
                count++;
            }
            if (wb != null) {
                // 写入excel文件
                wb.write(out);
                wb.write(outputStream);
                Map<String,Object> excelOut = new HashMap<String, Object>();
                excelOut.put("fileName",fileName);
                excelOut.put("outByte",out.toByteArray());
                return excelOut;
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
            if(wb!=null) {
            	try {
            		wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void generateZip(List<Map<String,Object>> fileList, String fileSavePath,String papersZipName) {
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(fileSavePath+ "/" +papersZipName));
            for (Map<String,Object> map:fileList){
                ZipEntry zipEntry =  new ZipEntry((String) map.get("fileName"));
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write((byte[]) map.get("outByte"));
                zipOutputStream.closeEntry();
            }
            zipOutputStream.flush();
            zipOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) throws BusinessException {
        return null;
    }

    @Override
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) throws BusinessException {
        Map<String, Object> map =new HashMap<>();
        @SuppressWarnings("unchecked")
		List<String> ids = (List<String>)param.get("ywids");
        JcsqglDto jcsqglDto=new JcsqglDto();
        jcsqglDto.setIds(ids);
        List<JcsqglDto> dtoList = dao.getDtoList(jcsqglDto);
        map.put("list",dtoList);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(JcsqglDto dto:dtoList){
                list.add(dto.getSqglid());
            }
        }
        map.put("list",list);
        return map;
    }

    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean callbackAuditDetectionApplication(JSONObject obj, HttpServletRequest request, Map<String,Object> t_map) throws BusinessException{
        String result=obj.getString("result");//正常结束时result为agree，拒绝时result为refuse，审批终止时没这个值，redirect转交
        String type=obj.getString("type");//审批正常结束（同意或拒绝）的type为finish，审批终止的type为terminate
        String processInstanceId=obj.getString("processInstanceId");//审批实例id
        String staffId=obj.getString("staffId");//审批人钉钉ID
        String remark=obj.getString("remark");//审核意见
        String content = obj.getString("content");//评论
        String finishTime=obj.getString("finishTime");//审批完成时间
        String title= obj.getString("title");
        String processCode=obj.getString("processCode");
        String ddspbcbj=request.getParameter("ddspbcbj");
        String wbcxid=obj.getString("wbcxid");
        log.error("回调参数获取---------result:"+result+",type:"+type+",processInstanceId:"+processInstanceId+",staffId:"+staffId+",remark:"+remark+",finishTime"+finishTime);
        //分布式服务器保存钉钉审批信息
        DdfbsglDto ddfbsglDto=new DdfbsglDto();
        ddfbsglDto.setProcessinstanceid(processInstanceId);
        ddfbsglDto=ddfbsglService.getDtoById(processInstanceId);
        DdspglDto ddspglDto=new DdspglDto();
        DdspglDto t_ddspglDto=new DdspglDto();
        t_ddspglDto.setProcessinstanceid(processInstanceId);
        t_ddspglDto.setType("finish");
        t_ddspglDto.setEventtype(DingTalkUtil.BPMS_TASK_CHANGE);
        List<DdspglDto> ddspgllist=ddspglService.getDtoList(t_ddspglDto);
        try {
            if(ddfbsglDto==null)
                throw new BusinessException("message","未获取到相应的钉钉分布式信息！");
            if(org.apache.commons.lang.StringUtils.isNotBlank(ddfbsglDto.getFwqm())) {
                if("1".equals(ddspbcbj)) {
                    ddspglDto=ddspglService.insertInfo(obj);
                }else {
                    //考虑190调190时，因为接收回调接口中已经保存一份消息数据，则这里直接取最新的那条，若没有则添加这条传递过来的消息
                    if(ddspgllist!=null && ddspgllist.size()>0) {
                        ddspglDto=ddspgllist.get(0);
                    }else{
                        ddspglDto=ddspglService.insertInfo(obj);
                    }
                }
            }

            //根据钉钉审批实例ID查询关联请购单
            JcsqglDto jcsqglDto=dao.getDtoByDdslid(processInstanceId);
            //若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if(jcsqglDto!=null) {
                User t_user=new User();
                t_user.setDdid(staffId);
                t_user.setWbcxid(wbcxid);
                //获取审批人信息
                t_user=commonService.getByddwbcxid(t_user);
                if(t_user==null)
                    return false;
                User user=new User();
                user.setYhid(t_user.getYhid());
                user.setZsxm(t_user.getZsxm());
                user.setYhm(t_user.getYhm());
                user.setDdid(t_user.getDdid());
                //获取审批人角色信息
                List<JcsqglDto> dd_sprjs=dao.getSprjsBySprid(t_user.getYhid());
                // 获取当前审核过程
                ShgcDto t_shgcDto = shgcService.getDtoByYwid(jcsqglDto.getSqglid());
                if(t_shgcDto!=null) {
                    // 获取的审核流程列表
                    ShlcDto shlcParam = new ShlcDto();
                    shlcParam.setShid(t_shgcDto.getShid());
                    shlcParam.setGcid(t_shgcDto.getGcid());// 处理旧流程判断用
                    @SuppressWarnings("unused")
                    List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);

                    if (("start").equals(type)) {
                        //更新分布式管理表信息
                        DdfbsglDto t_ddfbsglDto=new DdfbsglDto();
                        t_ddfbsglDto.setProcessinstanceid(processInstanceId);
                        t_ddfbsglDto.setYwlx(processCode);
                        t_ddfbsglDto.setYwmc(title);
                        ddfbsglService.update(t_ddfbsglDto);
                    }
                    if(dd_sprjs!=null && dd_sprjs.size()>0) {
                        //审批正常结束（同意或拒绝）
                        ShxxDto shxxDto=new ShxxDto();
                        shxxDto.setLcxh(t_shgcDto.getXlcxh());
                        shxxDto.setShlb(t_shgcDto.getShlb());
                        shxxDto.setShyj(remark);
                        shxxDto.setYwid(jcsqglDto.getSqglid());
                        shxxDto.setGcid(t_shgcDto.getGcid());
                        if (StringUtil.isNotBlank(finishTime)){
                            shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
                        }
                        String lastlcxh=null;//回退到指定流程

                        if(("finish").equals(type)) {
                            //如果审批通过,同意
                            if(("agree").equals(result)) {
                                log.error("同意------");
                                shxxDto.setSftg("1");
                                if(org.apache.commons.lang3.StringUtils.isBlank(t_shgcDto.getXlcxh()))
                                    throw new BusinessException("ICOM99019","现流程序号为空");
                                lastlcxh=String.valueOf(Integer.parseInt(t_shgcDto.getXlcxh())+1);
                            }
                            //如果审批未通过，拒绝
                            else if(("refuse").equals(result)) {
                                log.error("拒绝------");
                                shxxDto.setSftg("0");
                                shxxDto.setThlcxh(null);
                            }
                            //如果审批被转发
                            else if(("redirect").equals(result)) {
                                String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(finishTime) / 1000));
                                log.error("DingTalkMaterPurchaseAudit(钉钉检出列表申请审批转发提醒)------转发人员:"+t_user.getZsxm()+",人员ID:"+t_user.getYhid()+",申请类型:"+jcsqglDto.getSqlxmc()+",单据ID:"+jcsqglDto.getSqglid()+",转发时间:"+date);
                                return true;
                            }
                            //调用审核方法
                            Map<String, List<String>> map= ToolUtil.reformRequest(request);
                            log.error("map:"+map);
                            List<String> list= new ArrayList<>();
                            list.add(jcsqglDto.getSqglid());
                            map.put("sqglid", list);
                            //若一个用户多个角色导致审核异常时
                            for(int i=0;i<dd_sprjs.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(dd_sprjs.get(i).getSprjsid());
                                    user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
                                    shxxDto.setYwids(new ArrayList<>());
                                    if(("refuse").equals(result)){
                                        shgcService.terminateAudit(shxxDto, user,request,lastlcxh,obj);
                                    }else{
                                        shgcService.doManyBackAudit(shxxDto, user,request,lastlcxh,obj);
                                    }
                                    //更新审批管理信息
                                    ddspglDto.setCljg("1");
                                    ddspglService.updatecljg(ddspglDto);
                                    break;
                                } catch (Exception e) {
                                    log.error("钉钉审批回调-Exception:" + e.getMessage());
                                    t_map.put("ddspglid", ddspglDto.getDdspglid());
                                    t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());

                                    if(i==dd_sprjs.size()-1)
                                        throw new BusinessException("ICOM99019",e.getMessage());
                                }
                            }
                        }
                        //撤销审批
                        else if(("terminate").equals(type)) {
                            shxxDto.setThlcxh(null);
                            shxxDto.setYwglmc(jcsqglDto.getSqglid());
                            JcsqglDto jcsqglDto_t=new JcsqglDto();
                            jcsqglDto_t.setSqglid(jcsqglDto.getSqglid());
                            dao.updateDdslidToNull(jcsqglDto_t);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            //调用审核方法
                            Map<String, List<String>> map=ToolUtil.reformRequest(request);
                            List<String> list= new ArrayList<>();
                            list.add(jcsqglDto.getSqglid());
                            map.put("sqglid", list);
                            for(int i=0;i<dd_sprjs.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(dd_sprjs.get(i).getSprjsid());
                                    user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
                                    shxxDto.setYwids(new ArrayList<>());
                                    shgcService.terminateAudit(shxxDto, user,request,lastlcxh,obj);
//										shgcService.doAudit(shxxDto, user,request);
                                    //更新审批管理信息
                                    ddspglDto.setCljg("1");
                                    ddspglService.updatecljg(ddspglDto);
                                    break;
                                } catch (Exception e) {
                                    t_map.put("ddspglid", ddspglDto.getDdspglid());
                                    t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());

                                    if(i==dd_sprjs.size()-1)
                                        throw new BusinessException("ICOM99019",e.toString());
                                }
                            }
                        }else if(("comment").equals(type)) {
                            //评论时保存评论信息，添加至审核信息表
                            ShgcDto shgc = shgcService.getDtoByYwid(shxxDto.getYwid());//获得已有审核过程
                            ShxxDto shxx = new ShxxDto();
                            String shxxid =StringUtil.generateUUID();
                            shxx.setShxxid(shxxid);
                            shxx.setSqr(shgc.getSqr());
                            shxx.setLcxh(null);
                            shxx.setShid(shgc.getShid());
                            shxx.setSftg("1");
                            shxx.setGcid(shgc.getGcid());
                            shxx.setYwid(shxxDto.getYwid());
                            shxx.setShyj("评论:"+content.substring(0, Math.min(content.length(), 6000)));//这里写死，备注钉钉审批被提交人员撤销了
                            shxx.setLrry(user.getYhid());
                            shxxService.insert(shxx);
                        }
                    }
                }else {
                    if(("comment").equals(type)) {
                        //评论时保存评论信息，添加至审核信息表
                        ShxxDto shxx = new ShxxDto();
                        String shxxid =StringUtil.generateUUID();
                        shxx.setShxxid(shxxid);
                        shxx.setSftg("1");
                        shxx.setYwid(jcsqglDto.getSqglid());
                        shxx.setShlb(AuditTypeEnum.AUDIT_DETECTIONAPPLICATION.getCode());
                        List<ShxxDto> shxxlist=shxxService.getShxxOrderByPass(shxx);
                        if(shxxlist!=null && shxxlist.size()>0) {
                            shxx.setShid(shxxlist.get(0).getShid());
                            shxx.setShyj("评论:"+content.substring(0, Math.min(content.length(), 6000)));//这里写死，备注钉钉审批被提交人员撤销了
                            shxx.setLrry(user.getYhid());
                            shxxService.insert(shxx);
                        }
                    }
                }
            }
        }catch(BusinessException e) {
            log.error("BusinessException:" + e.getMessage());
            throw new BusinessException("ICOM99019",e.getMsg());
        }catch (Exception e) {
            log.error("Exception:" + e.getMessage());
            throw new BusinessException("ICOM99019",e.toString());
        }finally {
            if(ddfbsglDto!=null) {
                if(org.apache.commons.lang3.StringUtils.isNotBlank(ddfbsglDto.getFwqm())) {
                    if("1".equals(ddspbcbj)) {
                        t_map.put("sfbcspgl", "1");//是否返回上一层新增钉钉审批管理信息
                    }
                }
            }
        }
        return true;
    }

    /**
     * 上传
     * @param jcsqglDto
     * @return
     */
    public boolean uploadSaveDetectionApplication(JcsqglDto jcsqglDto){
        if(StringUtil.isBlank(jcsqglDto.getLrry())){
            log.error("未获取到申请人信息");
            return false;
        }
        //获取申请人信息
        User user=new User();
        user.setYhid(jcsqglDto.getLrry());
        user=commonService.getUserInfoById(user);
        if(user==null){
            log.error("未获取到申请人信息");
            return false;
        }

        if(org.apache.commons.lang.StringUtils.isBlank(user.getDdid())){
            log.error("未获取到申请人钉钉ID");
            return false;
        }

        if(org.apache.commons.lang.StringUtils.isBlank(user.getYhm())){
            log.error("未获取到申请人用户名");
            return false;
        }
        if(jcsqglDto.getFjids()!=null && jcsqglDto.getFjids().size() > 0){
            try {
                for (int i = 0; i < jcsqglDto.getFjids().size(); i++) {
                    Map<Object, Object> mFile = redisUtil.hmget("IMP_:_" + jcsqglDto.getFjids().get(i));
                    //如果文件信息不存在，则返回错误
                    if (mFile == null)
                        return false;
                    //文件全路径
                    String wjlj = (String) mFile.get("wjlj");
                    String wjm = (String) mFile.get("wjm");
                    talkUtil.sendWorkMessage(user.getYhm(),
                            user.getYhid(),
                            xxglService.getMsg("ICOMM_JC00001"), xxglService.getMsg("ICOMM_JC00002", DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                    String mediaId = talkUtil.uploadFileToDingTalk(user.getYhm(), wjlj, wjm);
                    talkUtil.sendFileMessage(user.getYhm(), user.getDdid(), mediaId);
                }
            }catch (Exception e){
                log.error(e.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * 获取文库编码
     * @param jcsqglDto
     * @return
     */
    public List<JcsqglDto> getWkbmsByYbbhs(JcsqglDto jcsqglDto){
        return dao.getWkbmsByYbbhs(jcsqglDto);
    }
    /**
     * 获取文库编码
     * @param fjids
     * @param user
     * @return
     */
    public Map<String, Object> analysisFile(String fjids,String xzbj,User user){
        Map<String, Object> map = new HashMap<>();
        String[] split = fjids.split(",");
        List<String> ybbhs=new ArrayList<>();
        for(String fjid:split){
            Map<Object, Object> mFile = redisUtil.hmget("IMP_:_" + fjid);
            if (mFile == null) {
                continue;
            }
            String wjm = (String) mFile.get("wjm");
            if(wjm.endsWith(".xls")||wjm.endsWith(".xlsx")){
                String filePath = (String) mFile.get("wjlj");
                if (StringUtil.isBlank(filePath)) {
                    continue;
                }
                File file = new File(filePath);
                try {
                    Sheet sheet =null;
                    if(wjm.endsWith(".xlsx")) {
                        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(file));
                        int tabIndex =0;
                        sheet = xssfWorkbook.getSheetAt(tabIndex);
                    }else if(wjm.endsWith(".xls")) {
                        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
                        int tabIndex =0;
                        sheet = hssfWorkbook.getSheetAt(tabIndex);
                    }
                    int rows = sheet.getPhysicalNumberOfRows();
                    //找到所有的元素对象
                    for (int i = 0; i < rows; i++) {
                        Row row = sheet.getRow(i);//获取每一行
                        if(row!=null){
                            //获取每个单元格
                            Cell cell = row.getCell(0);
                            if(cell!=null){
                                //设置单元格类型
                                cell.setCellType(CellType.STRING);
                                //获取单元格数据
                                String cellValue = cell.getStringCellValue();
                                ybbhs.add(cellValue);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if(ybbhs!=null&&ybbhs.size()>0){
            ybbhs = ybbhs.stream().distinct().collect(Collectors.toList());
            SjxxDto sjxxDto=new SjxxDto();
            sjxxDto.setYbbhs(ybbhs);
            if("1".equals(xzbj)){
                //伙伴权限
                List<String> hbqxList=hbqxService.getHbidByYhid(user.getYhid());
                if(hbqxList!=null && hbqxList.size()>0) {
                    List<String> hbmcList=sjhbxxService.getHbmcByHbid(hbqxList);
                    sjxxDto.setSjhbs(hbmcList);
                }

                //录入人员是自己得包括当前用户得钉钉和微信
                List<String> lrrys=new ArrayList<>();
                lrrys.add(user.getYhid());
                lrrys.add(user.getDdid());
                List<Map<String, String>> wxyhList=dao.getWxyhByYhid(user.getYhid());
                if(wxyhList!=null&&wxyhList.size()>0){
                    Map<String, String> strMap=wxyhList.get(0);
                    if(StringUtil.isNotBlank(strMap.get("wxid"))){
                        lrrys.add(strMap.get("wxid"));
                    }
                }
                sjxxDto.setLrrys(lrrys);
            }

            //根据sjid去igams_sjbgsm查询wkbh
            List<SjxxDto> dtoList = sjxxService.getDtoList(sjxxDto);
            JcsqglDto jcsqglDto=new JcsqglDto();
            List<String> sjids=new ArrayList<>();
            for(SjxxDto sjxxDto1:dtoList){
                sjids.add(sjxxDto1.getSjid());
            }
            sjids = sjids.stream().distinct().collect(Collectors.toList());
            if(sjids.size()>0){
                jcsqglDto.setYbbhs(sjids);
                List<JcsqglDto> wkbmsByYbbhs = dao.getWkbmsByYbbhs(jcsqglDto);
                if(wkbmsByYbbhs!=null&&wkbmsByYbbhs.size()>0){
                    for(SjxxDto dto:dtoList){
                        for(JcsqglDto jcsqglDto_t:wkbmsByYbbhs){
                            if(dto.getSjid().equals(jcsqglDto_t.getSjid())){
                                if(StringUtil.isBlank(dto.getWkbh())){
                                    dto.setWkbh(jcsqglDto_t.getWkbm());
                                }else{
                                    dto.setWkbh(dto.getWkbh()+","+jcsqglDto_t.getWkbm());
                                }
                            }

                        }
                    }
                }
            }
            map.put("list",dtoList);
        }
        return map;
    }

    @Override
    public Map<String,Object> fastqCallBack(Map<String,String>paramsMap){
        Map<String,Object> map=new HashMap<>();
        map.put("status","success");
        String params=paramsMap.get("params");
        String share_info=paramsMap.get("share_info");
        String failed_uids=paramsMap.get("failed_uids");
        String missing_uids=paramsMap.get("missing_uids");
        if(StringUtil.isNotBlank(failed_uids)||StringUtil.isNotBlank(missing_uids)){
            log.error("fast接口回调--文库上传异常--失败文库："+failed_uids);
            log.error("fast接口回调--文库上传异常--未找到文库："+missing_uids);
            // 发送钉钉消息
            List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.FASTQ_ERROR_TYPE.getCode());
            String info = xxglService.getMsg("ICOMM_FASTERROR001");
            for (DdxxglDto ddxxglDto : ddxxglDtolist) {
                if (StringUtil.isNotBlank(ddxxglDto.getDdid())){
                    talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), "网盘文库错误",
                            StringUtil.replaceMsg(info, failed_uids,missing_uids));
                }
            }
            map.put("status","false");
            map.put("message","fast接口回调--文库上传异常--未找到文库："+missing_uids);
            map.put("message1","fast接口回调--文库上传异常--失败文库："+failed_uids);
            log.error("fast接口回调：生信未返回数据");

            return map;
        }
        if(StringUtil.isNotBlank(params)){
            JSONObject jsonObject=JSONObject.parseObject(params);



            if(jsonObject!=null){
                String shlb=jsonObject.get("shlb").toString();
                String yhid=jsonObject.get("yhid").toString();
                String yhm=jsonObject.get("yhm").toString();
                String ddid=jsonObject.get("ddid").toString();
                String title=jsonObject.get("title").toString();
                try {
                    String info = xxglService.getMsg("ICOMM_FASTRESULT001");
                    if(StringUtil.isNotBlank(share_info)){
                        JSONObject share_info_job=JSONObject.parseObject(share_info);
                        if(share_info_job!=null){
                            String share_link=share_info_job.get("share_link").toString();
                            String share_password=share_info_job.get("share_password").toString();
                            String share_expires=share_info_job.get("share_expires").toString();
                            String share_unit=share_info_job.get("share_unit").toString();
                            talkUtil.sendWorkMessage(yhm, ddid, title,
                                    StringUtil.replaceMsg(info, share_link,
                                            share_password, share_expires, share_unit ));
                        }
                    }

                }catch (Exception e){
                    map.put("status","false");
                    map.put("message",e.getMessage());
                    log.error("fast接口回调--发送钉钉消息："+e.getMessage());
                    talkUtil.sendWorkDyxxMessage(shlb,yhid,yhm,ddid,title,"结果生成失败:请联系管理员");
                }


            }
        }else{
            map.put("status","false");
            map.put("message","生信未返回数据");
            log.error("fast接口回调：生信未返回数据");
        }

        return map;
    }
}

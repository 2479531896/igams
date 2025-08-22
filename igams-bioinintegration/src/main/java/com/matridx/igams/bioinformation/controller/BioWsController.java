package com.matridx.igams.bioinformation.controller;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.bioinformation.dao.entities.MngsmxjgDto;
import com.matridx.igams.bioinformation.dao.entities.ReceiveFileModel;
import com.matridx.igams.bioinformation.dao.entities.WkcxDto;
import com.matridx.igams.bioinformation.service.svcinterface.ICnvFileService;
import com.matridx.igams.bioinformation.service.svcinterface.IMngsFileParsingService;
import com.matridx.igams.bioinformation.service.svcinterface.IMngsmxjgService;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ws")
public class BioWsController extends BaseController {
    @Autowired(required=false)
    private AmqpTemplate amqpTempl;
    @Value("${matridx.rabbit.flg:}")
    private String rabbitFlg;
    @Value("${matridx.rabbit.preflg:}")
    private String preRabbitFlg;
    @Autowired
    ICnvFileService cnvFileService;
    @Value("${matridx.fileupload.releasePath:}")
    private String releasePath;
    @Autowired
    IMngsFileParsingService mngsFileParsingService;
    @Autowired
    IMngsmxjgService mngsmxjgService;
    private final Logger log = LoggerFactory.getLogger(BioWsController.class);
    /**
     * 接收文件并发送Rabbit
     */
    @RequestMapping("/cnv/pagedataFileSave")
    @ResponseBody
    public Map<String, Object> fileSave(ReceiveFileModel receiveFileModel) {
        log.error("pagedataFileSave 开始: Program:" + receiveFileModel.getProgram() );
        Map<String, Object> map = new HashMap<>();
        String releaseFilePath ="";
        if((receiveFileModel.getReport()!=null&&StringUtil.isBlank(receiveFileModel.getProgram()))||(receiveFileModel.getMt_result_file()!=null&&StringUtil.isBlank(receiveFileModel.getProgram()))){
            return map;
        }
        boolean flag=true;

        String datesdf = "";
        String s_chipDate = "";
        String t_chipdata = receiveFileModel.getChip();
        SimpleDateFormat date_Format = new SimpleDateFormat("yyyyMMdd");
        if(t_chipdata.startsWith("20")) {
            if(t_chipdata.length()>=8)
                datesdf = receiveFileModel.getChip().substring(0,8);
            else
                datesdf = receiveFileModel.getChip().substring(0,6) + "01";
        }else{
            datesdf = "20" + receiveFileModel.getChip().substring(0,6);
        }
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            // 设置为非宽容模式，这样可以更严格地解析日期
            simpleDateFormat.setLenient(false);
            Date d_chipdate = simpleDateFormat.parse(datesdf);
            s_chipDate = date_Format.format(d_chipdate);
        }catch (Exception e){
            flag=false;
        }
        if(!flag){
            try{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
                // 设置为非宽容模式，这样可以更严格地解析日期
                simpleDateFormat.setLenient(false);
                Date d_chipdate = simpleDateFormat.parse(datesdf.substring(0,6));
                s_chipDate = date_Format.format(d_chipdate);
            }catch (Exception e){
                flag=false;
            }
        }
        if(!flag){
            log.error(receiveFileModel.getChip()+"芯片名不符合规范");
            return map;
        }
        if(StringUtil.isNotBlank(receiveFileModel.getChip())){
            releaseFilePath = releasePath + BusTypeEnum.IMP_BIOINFORMATION.getCode() +"/UP"+s_chipDate.substring(0,4)+"/UP"+s_chipDate.substring(0,6)+"/UP"+s_chipDate.substring(0,8)+"/"+receiveFileModel.getChip();
        }
        if(StringUtil.isNotBlank(receiveFileModel.getSample())){
            String[] strings = receiveFileModel.getSample().split("-");
            String string = strings[strings.length - 1];
            String date1 = mngsFileParsingService.findDate(strings, string, strings.length - 1);
            if(StringUtil.isBlank(receiveFileModel.getProgram())){
                releaseFilePath = releasePath + BusTypeEnum.IMP_BIOINFORMATION.getCode()+"/UP"+s_chipDate.substring(0,4)+"/UP"+s_chipDate.substring(0,6)+"/UP"+s_chipDate.substring(0,8)+"/"+receiveFileModel.getChip()+"/"+receiveFileModel.getSample();
            }else{
                File bbfile=new File(releasePath + BusTypeEnum.IMP_BIOINFORMATION.getCode()+"/UP"+s_chipDate.substring(0,4)+"/UP"+s_chipDate.substring(0,6)+"/UP"+s_chipDate.substring(0,8)+"/"+receiveFileModel.getChip()+"/"+receiveFileModel.getSample()+"/V1_"+receiveFileModel.getProgram());
                File file =new File( releasePath + BusTypeEnum.IMP_BIOINFORMATION.getCode()+"/UP"+s_chipDate.substring(0,4)+"/UP"+s_chipDate.substring(0,6)+"/UP"+s_chipDate.substring(0,8)+"/"+receiveFileModel.getChip()+"/"+receiveFileModel.getSample());
                int _version=1;
                if(bbfile.isDirectory()){
                    File[] files =file.listFiles();
                    if (files != null) {
                        for(File f:files){
                            String fileName=f.getName();
                            String[] fileNameArr=fileName.split("_");

                            if(fileNameArr.length>1){
                                if(fileNameArr[1].equals(receiveFileModel.getProgram())){
                                    String version=fileNameArr[0].substring(1);
                                    if(Integer.parseInt(version)>_version){
                                        _version=Integer.parseInt(version);
                                    }
                                }

                            }

                        }
                    }
                    releaseFilePath = releasePath+ BusTypeEnum.IMP_BIOINFORMATION.getCode() +"/UP"+s_chipDate.substring(0,4)+"/UP"+s_chipDate.substring(0,6)+"/UP"+s_chipDate.substring(0,8)+"/"+receiveFileModel.getChip()+"/"+receiveFileModel.getSample()+"/V"+(_version+1)+"_"+receiveFileModel.getProgram();
                    receiveFileModel.setProgram("V"+(_version+1)+":"+receiveFileModel.getProgram());
                }else{
                    releaseFilePath = releasePath+ BusTypeEnum.IMP_BIOINFORMATION.getCode() +"/UP"+s_chipDate.substring(0,4)+"/UP"+s_chipDate.substring(0,6)+"/UP"+s_chipDate.substring(0,8)+"/"+receiveFileModel.getChip()+"/"+receiveFileModel.getSample()+"/V1_"+receiveFileModel.getProgram();
                    receiveFileModel.setProgram("V1:"+receiveFileModel.getProgram());
                }
            }
        }

        MultipartFile cnv = receiveFileModel.getCnv();
        if(cnv!=null){
            String path = cnvFileService.saveMultipartFile(cnv, releaseFilePath);
            receiveFileModel.setCnv(null);
            receiveFileModel.setCnvPath(path);
        }
        MultipartFile qc = receiveFileModel.getQc();
        if(qc!=null){
            String path = cnvFileService.saveMultipartFile(qc, releaseFilePath);
            receiveFileModel.setQc(null);
            receiveFileModel.setQcPath(path);
        }
        MultipartFile sex = receiveFileModel.getSex();
        if(sex!=null){
            String path = cnvFileService.saveMultipartFile(sex, releaseFilePath);
            receiveFileModel.setSex(null);
            receiveFileModel.setSexPath(path);
        }
        MultipartFile model_predict = receiveFileModel.getModel_predict();
        if(model_predict!=null){
            String path = cnvFileService.saveMultipartFile(model_predict, releaseFilePath);
            receiveFileModel.setModel_predict(null);
            receiveFileModel.setModel_predictPath(path);
        }
        MultipartFile result = receiveFileModel.getResult();
        if(result!=null){
            String path = cnvFileService.saveMultipartFile(result, releaseFilePath);
            receiveFileModel.setResult(null);
            receiveFileModel.setResultPath(path);
        }
        MultipartFile report = receiveFileModel.getReport();
        if(report!=null){
            String path = cnvFileService.saveMultipartFile(report, releaseFilePath);
            receiveFileModel.setReport(null);
            receiveFileModel.setReportPath(path);
        }
        MultipartFile mt_result_file = receiveFileModel.getMt_result_file();
        if(mt_result_file!=null){
            String path = cnvFileService.saveMultipartFile(mt_result_file, releaseFilePath);
            receiveFileModel.setMt_result_file(null);
            receiveFileModel.setMt_result_filePath(path);
        }
        MultipartFile doctor_ai = receiveFileModel.getDoctor_ai();
        if(doctor_ai!=null){
            String path = cnvFileService.saveMultipartFile(doctor_ai, releaseFilePath);
            receiveFileModel.setDoctor_ai(null);
            receiveFileModel.setDoctor_aiPath(path);
        }
        MultipartFile card_stat = receiveFileModel.getCard_stat();
        if(card_stat!=null){
            String path = cnvFileService.saveMultipartFile(card_stat, releaseFilePath);
            receiveFileModel.setCard_stat(null);
            receiveFileModel.setCard_statPath(path);
        }
        MultipartFile card = receiveFileModel.getCard();
        if(card!=null){
            String path = cnvFileService.saveMultipartFile(card, releaseFilePath);
            receiveFileModel.setCard(null);
            receiveFileModel.setCard_Path(path);
        }
        MultipartFile vfdb_stat = receiveFileModel.getVfdb_stat();
        if(vfdb_stat!=null){
            String path = cnvFileService.saveMultipartFile(vfdb_stat, releaseFilePath);
            receiveFileModel.setVfdb_stat(null);
            receiveFileModel.setVfdb_statPath(path);
        }
        MultipartFile vfdb = receiveFileModel.getVfdb();
        if(vfdb!=null){
            String path = cnvFileService.saveMultipartFile(vfdb, releaseFilePath);
            receiveFileModel.setVfdb(null);
            receiveFileModel.setVfdb_Path(path);
        }
        MultipartFile coverage = receiveFileModel.getCoverage();
        if(coverage!=null){
            String path = cnvFileService.saveMultipartFile(coverage, releaseFilePath);
            receiveFileModel.setCoverage(null);
            receiveFileModel.setCoveragePath(path);
        }
        MultipartFile dash = receiveFileModel.getDash();
        if(dash!=null){
            String path = cnvFileService.saveMultipartFile(dash, releaseFilePath);
            receiveFileModel.setDash(null);
            receiveFileModel.setDashPath(path);
        }
        MultipartFile bg = receiveFileModel.getBg();
        if(bg!=null){
            String path = cnvFileService.saveMultipartFile(bg, releaseFilePath);
            receiveFileModel.setBg(null);
            receiveFileModel.setBgPath(path);
        }
        MultipartFile fx = receiveFileModel.getFx();
        if(fx!=null){
            String path = cnvFileService.saveMultipartFile(fx, releaseFilePath);
            receiveFileModel.setFx(null);
            receiveFileModel.setFxPath(path);
        }
        amqpTempl.convertAndSend("sys.igams", preRabbitFlg+"sys.igams.document.parse"+rabbitFlg, JSON.toJSONString(receiveFileModel));
        return map;
    }

    /**
     * final.report文件下载（仅用于final.report文件下载使用）
     */
    @RequestMapping(value="/file/downloadFinalReport")
    public String downloadFinalReport(WkcxDto wkcxDto,HttpServletResponse response, HttpServletRequest request){
        String[] strings = wkcxDto.getWkbh().split("-");
        String string = strings[strings.length - 1];
        String bbhStr=wkcxDto.getBbh().replace(":","_");
        String date = mngsFileParsingService.findDate(strings, string, strings.length - 1);
        String filePath = releasePath+"UP" +date.substring(0,4)+"/UP"+date.substring(0,4)+date.substring(4,6)+"/UP"+date.substring(0,4)+date.substring(4,6)+date.substring(6)+"/"+wkcxDto.getXpm()+"/"+wkcxDto.getWkbh()+"/"+bbhStr+"/report";
        String wjm = bbhStr+"_final_report.txt";
        File downloadFile = new File(filePath);
        response.setContentLength((int) downloadFile.length());
        String agent = request.getHeader("user-agent");
        //log.error("文件下载 agent=" + agent);
        //指明为下载
        response.setHeader("content-type", "application/octet-stream");
        /*if(wjm != null){
            wjm = URLEncoder.encode(wjm, "utf-8");
        }*/
        if (agent.contains("iPhone") || agent.contains("Trident")){
            //iphone手机 微信内置浏览器 下载
            if (agent.contains("MicroMessenger")|| agent.contains("micromessenger")){
//						byte[] bytes = agent.contains("MSIE") ? wjm.getBytes() : wjm.getBytes("UTF-8");
//						wjm = new String(bytes, "ISO-8859-1");
                wjm = URLEncoder.encode(wjm, StandardCharsets.UTF_8);
                response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", wjm));// 文件名外的双引号处理firefox的空格截断问题
            }else {
                //iphone手机 非微信内置浏览器 下载 或 ie浏览器 下载
                wjm = URLEncoder.encode(wjm, StandardCharsets.UTF_8);
                response.setHeader("Content-Disposition","attachment;filename*=UTF-8''" + wjm);
            }
        }else {
            wjm = URLEncoder.encode(wjm, StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", wjm));// 文件名外的双引号处理firefox的空格截断问题
        }
        log.error("文件下载 准备完成 wjm=" + URLDecoder.decode(wjm, StandardCharsets.UTF_8));

        byte[] buffer = new byte[1024];
        BufferedInputStream bis;
        InputStream iStream;
        OutputStream os; //输出流
        try {
            iStream = new FileInputStream(filePath);
            os = response.getOutputStream();
            bis = new BufferedInputStream(iStream);
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer);
                os.flush();
                i = bis.read(buffer);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("文件下载 写文件异常：" + e);
            return null;
        }
        try {
            bis.close();
            os.flush();
            os.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("文件下载 关闭文件流异常：" + e);
            return null;
        }
        return null;
    }
    @RequestMapping(value="/test/test")
    public void test(HttpServletResponse response) {
        OutputStream out = null;
        HSSFWorkbook wb;
        try {
            //创建工作sheet
            wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("条件致病菌的RPM分析");
            //画图的顶级管理器，一个sheet只能获取一个
//            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            HSSFCellStyle style = wb.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN); //下边框
            style.setBorderLeft(BorderStyle.THIN);//左边框
            style.setBorderTop(BorderStyle.THIN);//上边框
            style.setBorderRight(BorderStyle.THIN);//右边框

//        style.setWrapText(true); //设置内容自动换行

//            CellStyle textStyle = wb.createCellStyle();
//            textStyle.setBorderBottom(BorderStyle.THIN); //下边框
//            textStyle.setBorderLeft(BorderStyle.THIN);//左边框
//            textStyle.setBorderTop(BorderStyle.THIN);//上边框
//            textStyle.setBorderRight(BorderStyle.THIN);//右边框
//            textStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex()); // 是设置前景色不是背景色
//            textStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints((short) 11);
            font.setFontName("宋体");
//            HSSFFont redFont = wb.createFont();
//            redFont.setFontHeightInPoints((short) 11);
//            redFont.setFontName("宋体");
//            redFont.setColor(IndexedColors.RED.getIndex());
//            textStyle.setFont(redFont);
            style.setFont(font);

            //第一行
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(1).setCellValue("中文名");
            row0.createCell(2).setCellValue("拉丁名");
            row0.createCell(3).setCellValue("数量");
            row0.createCell(4).setCellValue("RPM最小值");
            row0.createCell(5).setCellValue("RPM最大值");
            row0.createCell(6).setCellValue("RPM平均数");
            row0.createCell(7).setCellValue("RPM标准差");
            row0.createCell(8).setCellValue("RPM（s+3SD）");
            row0.createCell(9).setCellValue("RPM（s-3SD）");
            row0.createCell(10).setCellValue("推荐范围");
            row0.createCell(11).setCellValue("检出频率");
            for (int i = 1; i < 12; i++) {
                row0.getCell(i).setCellStyle(style);
            }
            MngsmxjgDto mngsmxjgDto=new MngsmxjgDto();
            mngsmxjgDto.setLrsj("202211");
            List<Map<String,String>> list = mngsmxjgService.backgroundDataBase(mngsmxjgDto);
            int count=1;
            for(Map<String,String> map:list){
                HSSFRow row = sheet.createRow(count);
                row.createCell(0).setCellValue(map.get("hzxm").toString());
                row.createCell(1).setCellValue(map.get("wzzwm").toString());
                row.createCell(2).setCellValue(map.get("wzywm").toString());
                row.createCell(3).setCellValue(map.get("gss").toString());
                row.createCell(4).setCellValue(map.get("min").toString());
                row.createCell(5).setCellValue(map.get("max").toString());
                row.createCell(6).setCellValue(map.get("avg").toString());
                row.createCell(7).setCellValue(map.get("stddev").toString());
                row.createCell(8).setCellValue(map.get("z3").toString());
                row.createCell(9).setCellValue(map.get("f3").toString());
                String fw="";
                if(StringUtil.isNotBlank(map.get("f3").toString())){
                    BigDecimal bigDecimal=new BigDecimal(map.get("f3").toString());
                    if(bigDecimal.compareTo(BigDecimal.ZERO) < 0){
                        fw="0-"+map.get("z3").toString();
                    }else{
                        fw=map.get("f3").toString()+"-"+map.get("z3").toString();
                    }
                }
                row.createCell(10).setCellValue(fw);
                row.createCell(11).setCellValue(map.get("bdl").toString());
                for (int i = 0; i < 12; i++) {
                    row.getCell(i).setCellStyle(style);
                }
                count++;
            }

            for (int i = 1; i < 12; i++) {
                sheet.setColumnWidth(i, 5000);
            }
            sheet.setColumnWidth(2, 8000);
            sheet.setColumnWidth(3, 2000);
//            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
//            String path = "/matridx/fileupload/release/2022/08/25/220825_TPNB500525_0459_AHH757BGXM/MDL001-03D2235078L-DNA-C44-20220825-VIP.png";
//            File file = new File(path);
//            log.error("图片路径" + path);
//            bufferImg = ImageIO.read(file);
//            ImageIO.write(bufferImg, "png", byteArrayOut);
//            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 0, 2, (short) 4, 18);
//            patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
            String fileName = "背景数据库.xls";
            log.error("文件下载 准备完成 wjm=" + URLDecoder.decode(fileName, StandardCharsets.UTF_8));
            out = response.getOutputStream();
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(fileName, StandardCharsets.UTF_8)));
            // 写入excel文件
            wb.write(out);
        }catch (Exception e){
            e.printStackTrace();
            log.error("文件下载 异常：" + e);
        }finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

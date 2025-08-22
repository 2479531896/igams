package com.matridx.igams.common.util;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.PictureFloat;
import com.matridx.igams.common.enums.MQTypeEnum_pub;
import com.matridx.igams.common.service.impl.MatridxByteArrayResource;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author hmz
 * @date2022/07/06 version V1.0
 * 文件处理生成公用方法
 **/
public class XWPFUtil {

    //日志记录
    private final Logger log = LoggerFactory.getLogger(XWPFUtil.class);

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
//        map.put("addWatermark",true);
//        GenerateReport(map);
    }

    /**
     * 生成报告文件
     * @param map
     * @param DOC_OK
     * @param FTP_URL
     * @param ToPDF 是否生成为PDF文件
     * @return
     */
    public boolean GenerateReport(Map<String, Object> map, IFjcfbService fjcfbService, AmqpTemplate amqpTempl, String DOC_OK, String FTP_URL, boolean ToPDF){
        ExportWord(map, fjcfbService, amqpTempl, DOC_OK, FTP_URL,ToPDF);
        return true;
    }

    /**
     * 生成报告文件 方法
     * @param map
     * @param DOC_OK
     * @param FTP_URL
     * @param ToPDF
     * @return
     */
    public boolean ExportWord(Map<String, Object> map, IFjcfbService fjcfbService, AmqpTemplate amqpTempl, String DOC_OK, String FTP_URL, boolean ToPDF){
        log.error("开启线程：生成报告--------------开始生成报告！");
        String ywlxToPDF = (String) map.get("ywlx"); // 业务类型
        String ywlx = ywlxToPDF+"_WORD"; // 业务类型
        String wjm=(String) map.get("wjm"); // 文件名
        String prefix=(String) (map.get("prefix")!=null ? map.get("prefix"):""); // 文件名前缀
        String releaseFilePath = (String) map.get("releaseFilePath"); // 正式文件路径
        String tempFilePath = (String) map.get("tempPath"); // 临时文件路径
        String templateFilePath = map.get("wjlj")!=null?map.get("wjlj").toString():"" ; // 模板文件路径
        DBEncrypt dbEncrypt = new DBEncrypt();
        FileInputStream fileInputStream = null;
        XWPFDocument document = null;
        OutputStream output = null;
        try {
            File templateFile = new File(dbEncrypt.dCode(templateFilePath));
            if (StringUtil.isBlank(tempFilePath) || !templateFile.exists()) {
                log.error("报告线程：生成报告--------------模板不存在！请重新确认模板！");
                return false;
            }
            // 把文件放入流中
            fileInputStream = new FileInputStream(templateFile);
            // 读取word文件
            document = new XWPFDocument(fileInputStream);
            //log.error("报告线程：生成报告--------------替换模板中信息：开始");
            replaceTemplate(document, map);
            //log.error("报告线程：生成报告--------------替换模板中信息：结束");
            // 关闭流
            fileInputStream.close();
            //根据日期创建文件夹(临时)
            String storePath = mkDirs(ywlx,tempFilePath);
            //获取文件名后缀
            int index = (wjm.lastIndexOf(".") > 0) ? wjm.lastIndexOf(".") : wjm.length() - 1;
            String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
            String suffix = wjm.substring(index);
            //生成文件名
            String saveName = t_name + suffix;
            output = new FileOutputStream(storePath + "/" + saveName);
            //写入文件
            document.write(output);
            //关闭流
            document.close();
            output.close();
            //保存到正式文件夹并保存到附件存放表中
            String fjid = StringUtil.generateUUID();
            //文件路径
            String pathString = storePath;
            //文件名
            String report_wjm;
            //若有相关规则，根据规则替换文件名
            report_wjm = replaceFileName(map,suffix);
            //分文件名
            String fwjm = saveName;
            //文件全路径
            String wjlj = storePath + "/" + saveName;
            //根据临时文件夹创建正式文件
            String t_path = pathString.substring(prefix.length() + tempFilePath.length());
            //分文件路径
            String real_path = prefix + releaseFilePath + t_path;
            mkDirs(null,real_path);
            //把文件从临时文件夹中移动到正式文件夹中，如果已经存在则覆盖
            CutFile(wjlj,real_path+"/"+fwjm,true);
            //将正式文件夹路径更新至数据库
            DBEncrypt bpe = new DBEncrypt();
            FjcfbDto fjcfbDto = new FjcfbDto();
            fjcfbDto.setFjid(fjid);
            fjcfbDto.setYwid(map.get("ywid").toString());
            fjcfbDto.setZywid("");
            fjcfbDto.setXh("1");
            fjcfbDto.setYwlx(ywlx);
            fjcfbDto.setWjm(report_wjm);
            fjcfbDto.setWjlj(bpe.eCode(real_path+"/"+fwjm));
            fjcfbDto.setFwjlj(bpe.eCode(real_path));
            fjcfbDto.setFwjm(bpe.eCode(fwjm));
            fjcfbDto.setZhbj("0");
            boolean isTrue=fjcfbService.insert(fjcfbDto);
            if(map.get("sendToAli") != null && (Boolean) map.get("sendToAli")) {
                sendFileToAli(map,fjcfbService,fjcfbDto);
            }
            if(isTrue) {
                //刪除之前的word
                fjcfbService.deleteByYwidAndYwlx(fjcfbDto);
            }
            if(ToPDF){
                //转换PDF
                String wjljjm = bpe.dCode(fjcfbDto.getWjlj());
                //连接服务器
                boolean sendFlg = sendWordFile(wjljjm,FTP_URL);
                if(sendFlg) {
                    Map<String,String> pdfMap=new HashMap<>();
                    boolean addWatermark = map.get("addWatermark") != null && (boolean) map.get("addWatermark");
                    boolean free = map.get("free") != null && (boolean) map.get("free");
                    if(free&&addWatermark) {
                        //添加水印
                        pdfMap.put("watermark", "1");
                    }
                    pdfMap.put("wordName", fwjm);
                    pdfMap.put("fwjlj",fjcfbDto.getFwjlj());
                    pdfMap.put("fjid",fjcfbDto.getFjid());
                    pdfMap.put("ywlx",ywlxToPDF);
                    pdfMap.put("ywid",map.get("ywid").toString());
                    pdfMap.put("MQDocOkType",DOC_OK);
                    pdfMap.put("gzlx", (map.get("gzlx")!=null?map.get("gzlx"):"").toString());
                    //删除PDF
                    FjcfbDto fjcfbDto_t=new FjcfbDto();
                    fjcfbDto_t.setYwlx(pdfMap.get("ywlx"));
                    fjcfbDto_t.setYwid(map.get("ywid").toString());
                    fjcfbService.deleteByYwidAndYwlx(fjcfbDto_t);
                    //发送Rabbit消息转换pdf
                    amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(), JSONObject.toJSONString(pdfMap));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (document != null) {
                    document.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Exception e) {
                log.error(e.toString());
            }
        }
        return true;
    }


    /**
     * 根据路径创建文件夹
     * @param ywlx
     * @param realFilePath
     * @return storePath
     */
    protected String mkDirs(String ywlx,String realFilePath) {
        String storePath;
        if(ywlx!=null) {
            //根据日期创建文件夹
            storePath =realFilePath + ywlx +"/"+ "UP"+
                    DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
                    DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
                    DateUtils.getCustomFomratCurrentDate("yyyyMMdd");

        }else {
            storePath=realFilePath;
        }

        File file = new File(storePath);
        if (file.isDirectory()) {
            return storePath;
        }
        if(file.mkdirs())
        {
            return storePath;
        }
        return null;
    }

    /**
     * 从原路径剪切到目标路径
     * @param s_srcFile
     * @param s_distFile
     * @param coverFlag
     * @return flag
     */
    protected boolean CutFile(String s_srcFile,String s_distFile,boolean coverFlag) {
        boolean flag = false;
        //路径如果为空则直接返回错误
        if(StringUtil.isBlank(s_srcFile)|| StringUtil.isBlank(s_distFile)) {
            return false;
        }
        File srcFile = new File(s_srcFile);
        File distFile = new File(s_distFile);
        //文件不存在则直接返回错误
        if(!srcFile.exists()) {
            return false;
        }
        //目标文件已经存在
        if(distFile.exists()) {
            if(coverFlag) {
                srcFile.renameTo(distFile);
                flag = true;
            }
        }else {
            srcFile.renameTo(distFile);
            flag = true;
        }
        return flag;
    }


    /**
     * 发送文件至阿里云
     * @param map
     * @param fjcfbService
     * @param fjcfbModel
     * @return
     */
    public boolean sendFileToAli(Map<String, Object> map, IFjcfbService fjcfbService, FjcfbModel fjcfbModel) {
        //如果为空，直接返回
        if(map.get("menuurl") == null || StringUtil.isBlank(map.get("menuurl").toString())) {
            return true;
        }
        String menuurl = map.get("menuurl").toString();
        // 拷贝文件到微信服务器
        try{
            DBEncrypt dbEncrypt = new DBEncrypt();
            if (fjcfbModel != null){
                String wjlj = fjcfbModel.getWjlj();
                String pathString = dbEncrypt.dCode(wjlj);
                File file = new File(pathString);
                // 文件不存在不做任何操作
                if (!file.exists())
                    return true;

                byte[] bytesArray = new byte[(int) file.length()];

                FileInputStream fis = new FileInputStream(file);
                fis.read(bytesArray); // read file into bytes[]
                fis.close();
                // 需要给文件的名称
                ByteArrayResource contentsAsResource = new ByteArrayResource(bytesArray)
                {
                    @Override
                    public String getFilename()
                    {
                        return file.getName();
                    }
                };
                MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                paramMap.add("file", contentsAsResource);
                paramMap.add("fjcfbModel", fjcfbModel);

                // 设置超时时间
                HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
                httpRequestFactory.setConnectionRequestTimeout(6000);
                httpRequestFactory.setConnectTimeout(6000);
                httpRequestFactory.setReadTimeout(240000);
                RestTemplate t_restTemplate = new RestTemplate(httpRequestFactory);
                // 发送文件到服务器
                String reString = t_restTemplate.postForObject(menuurl + "/wechat/upSaveInspReport", paramMap, String.class);

                if ("OK".equals(reString)){
                    // 更新文件的转换标记为true
                    boolean isSuccess = fjcfbService.updateZhbj(fjcfbModel);
                    if (!isSuccess)
                        return false;
                }
            }
        } catch (Exception e)
        {
            log.error("发送文件至阿里云"+e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 上传Word文件至文件转换服务器
     * @param fileName
     * @param FTP_URL
     * @return
     */
    private boolean sendWordFile(String fileName,String FTP_URL) {
        try {
            File t_file = new File(fileName);
            //文件不存在不做任何操作
            if(!t_file.exists())
                return true;

            byte[] bytesArray = new byte[(int) t_file.length()];

            FileInputStream t_fis = new FileInputStream(t_file);
            t_fis.read(bytesArray); //read file into bytes[]
            t_fis.close();
            //需要给文件的名称
            MatridxByteArrayResource contentsAsResource = new MatridxByteArrayResource(bytesArray);
            contentsAsResource.setFilename(t_file.getName());

            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("file", contentsAsResource);
            RestTemplate t_restTemplate = new RestTemplate();
            //发送文件到服务器
            String reString = t_restTemplate.postForObject("http://" + FTP_URL + ":8756/file/uploadWordFile", paramMap, String.class);
            return "OK".equals(reString);
        }catch(Exception e) {
            e.printStackTrace();
            log.error("上传文件转换服务器失败"+e.getMessage());
            return false;
        }
    }

    /**
     * 文件名替换方法
     * @param map
     * @param suffix 文件名后缀
     * @return fileName 文件名
     */
    public String replaceFileName(Map<String, Object> map,String suffix){
        String fileName;
        Object wjmgzObj = map.get("wjmgz");
        if (wjmgzObj!=null && StringUtil.isNotBlank(wjmgzObj.toString())){
            fileName = wjmgzObj.toString();
            while (fileName.contains("«")) {
                int startIndex = fileName.indexOf("«")+1;
                int endIndex = fileName.indexOf("»");
                String xthnr = fileName.substring(startIndex,endIndex);//需要替换的内容
                Object thnrObj = map.get(xthnr);
                String thnr = "";
                if (thnrObj != null && thnrObj != ""){
                    thnr = thnrObj.toString();
                }
                fileName = fileName.replace("«" + xthnr + "»",thnr);
            }
        }else {
            String xm = map.get("xm").toString();
            if(xm.contains("/")){
                xm="";
            }
            fileName =map.get("sjdwmc") + "_" + xm + "(" + map.get("yblxmc") + ")_"+map.get("ybbh");
        }
        //文件名添加后缀
        fileName = fileName + suffix;
        return fileName;
    }

    /**
     * 替换模板中的内容
     * @param document
     * @param map
     */
    private boolean replaceTemplate(XWPFDocument document,Map<String, Object> map) throws IOException, XmlException {
        /**
         * 替换图片(表格、段落)
         */
        //log.error("生成报告--------------替换图片--开始！");
        replaceImage(document, map);
        //log.error("生成报告--------------替换图片--结束！");

        /**
         * 替换封面信息：封面内容为XML格式
         */
        //log.error("生成报告--------------替换封面--开始！");
        for (int i = 0; i < document.getParagraphs().size(); i++) {
            replaceHead(document.getParagraphs().get(i), map);
        }
        //log.error("生成报告--------------替换封面--结束！");

        /**
         * 4.替换表格中的内容
         */
        //log.error("生成报告--------------替换表格中内容--开始！");
        Iterator<XWPFTable> itTable = document.getTablesIterator();
        while (itTable.hasNext()) {
            XWPFTable table = itTable.next();
            // 替换表中的数据
            replaceTable(table, map);
        }
        //log.error("生成报告--------------替换表格中内容--结束！");

        /**
         * 替换页眉
         */
        //log.error("生成报告--------------替换页眉、页脚--开始！");
        List<XWPFHeader> headerList = document.getHeaderList();
        for (XWPFHeader header : headerList) {
            List<XWPFParagraph> paragraphs = header.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                replaceHeader(paragraph,map);
            }
        }
        /**
         * 替换页脚
         */
        List<XWPFFooter> FooterList = document.getFooterList();
        for (XWPFFooter footer : FooterList) {
            List<XWPFParagraph> paragraphs = footer.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                replaceFooter(paragraph,map);
            }
        }
        //log.error("生成报告--------------替换页眉、页脚--结束！");

        return true;
    }

    /**
     * 替换表格中的图片、以及签名
     * @param document
     * @param map
     */
    private void replaceImage(XWPFDocument document, Map<String,Object> map) throws IOException {
        // 替换表格中的图片
        Iterator<XWPFTable> itTable = document.getTablesIterator();
        while (itTable.hasNext()) {
            XWPFTable table = itTable.next();
            replaceTableImage(table,map);
        }
        // 替换段落中的图片
        List<XWPFParagraph> paragraphImageList = document.getParagraphs();
        for (int i = paragraphImageList.size()-1; i>-1; i--) {
            XWPFParagraph paragraph = paragraphImageList.get(i);
            replaceParagraphImage(paragraph,map);
        }
    }

    /**
     * 替换段落中的图片
     * @param paragraph
     * @param map
     */
    private void replaceParagraphImage(XWPFParagraph paragraph,Map<String,Object> map) throws IOException {
        List<XWPFRun> runs = paragraph.getRuns();
        for (int q = 0; runs != null && q < runs.size(); q++) {
            String oneparaString = runs.get(q).getText(runs.get(q).getTextPosition());
            if (StringUtil.isBlank(oneparaString))
                continue;
            int startTagPos = oneparaString.indexOf("«");
            int endTagPos = oneparaString.indexOf("»");
            if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
                String varString = oneparaString.substring(startTagPos + 1, endTagPos).trim();
                if (StringUtil.isNotBlank(varString)) {
                    //«gzlxdz_FloatPic_size:120_left:300_top:-40»
                    if (varString.contains("_FloatPic")){
                        oneparaString = oneparaString.replace("«" + varString + "»", "");
                        List<PictureFloat> list=new ArrayList<>();
                        String imageObj = varString.split("_")[0];
                        Object imaget = map.get(imageObj);
                        if (imaget!=null) {
                            DBEncrypt dbEncrypt = new DBEncrypt();
                            String imagetFile = dbEncrypt.dCode(String.valueOf(imaget));
                            String[] varStrings = varString.split("_");
                            int size = 120;
                            int left = 300;
                            int top = -60;
                            if (varStrings.length>1){
                                for (String string : varStrings) {
                                    if (StringUtil.isNotBlank(string) && string.indexOf("size:")>-1){
                                        size = Integer.parseInt(string.replace("size:",""));
                                        top = -size/2;
                                    }
                                    if (StringUtil.isNotBlank(string) && string.indexOf("left:")>-1){
                                        left = Integer.parseInt(string.replace("left:",""));
                                    }
                                    if (StringUtil.isNotBlank(string) && string.indexOf("top:")>-1){
                                        top = Integer.parseInt(string.replace("top:",""));
                                    }
                                }
                            }
                            //获取图片长宽，以及长宽的比列
                            PictureFloat imageFloat=new PictureFloat(imagetFile, size, size, left, top);
                            list.add(imageFloat);
                            upsetPictures(runs.get(q),list);
                        }
                        runs.get(q).setText(oneparaString, 0);
                    } else if (varString.contains("Pic")){
                        oneparaString = oneparaString.replace("«" + varString + "»", "");
                        List<PictureFloat> list=new ArrayList<>();
                        Object imaget = map.get(varString);
                        if(imaget!=null) {
                            String imagetFile = map.get(varString).toString();
                            File picture = new File(imagetFile);
                            BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
                            //获取图片长宽，以及长宽的比列
                            int height = sourceImg.getHeight();
                            int width = sourceImg.getWidth();
                            BigDecimal divide = new BigDecimal(width).divide(new BigDecimal(height), 2, RoundingMode.HALF_EVEN);
                            int imgWidth = new BigDecimal(20).multiply(divide).intValue();
                            overlayPictures(runs.get(q),list);
                            PictureFloat imageFloat=new PictureFloat(map.get(varString).toString(), imgWidth, 15, 0, 0);
                            list.add(imageFloat);
                            overlayPictures(runs.get(q),list);
                        }else {
                            //oneparaString = oneparaString.replace("«" + varString + "»", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                        }
                        runs.get(q).setText(oneparaString, 0);
                    }
                }
            }
        }
    }
    public boolean upsetPictures(XWPFRun run, List<PictureFloat> picList){
        // 图片路径
        FileInputStream image = null;
        try {
            if(picList != null && picList.size() > 0) {
                run.setText("",0);
                for (int i = 0; i < picList.size(); i++) {
                    PictureFloat pictureFloat = picList.get(i);
                    image = new FileInputStream(pictureFloat.getWjlj());
                    run.addPicture(image, XWPFDocument.PICTURE_TYPE_PNG, pictureFloat.getWjlj(), Units.toEMU(pictureFloat.getWidth()), Units.toEMU(pictureFloat.getHeight()));
                    if(i > 0) {
                        imageUpsetDeal(run, i, pictureFloat.getWidth(),
                                pictureFloat.getHeight(), pictureFloat.getLeft(), pictureFloat.getTop());
                    }
                    image.close();
                    CTDrawing drawingArray = run.getCTR().getDrawingArray(0);
                    CTGraphicalObject graphic = drawingArray.getInlineArray(0).getGraphic();
                    Random random = new Random();
                    int number = random.nextInt(999)+ 1;
                    CTAnchor anchorWithGraphic = getAnchorWithGraphic(graphic, "Seal" + number,Units.toEMU(pictureFloat.getWidth()), Units.toEMU(pictureFloat.getHeight()),Units.toEMU(pictureFloat.getLeft()), Units.toEMU(pictureFloat.getTop()), false);
                    drawingArray.setAnchorArray(new CTAnchor[]{anchorWithGraphic});
                    drawingArray.removeInline(0);

                }
            }
            return true;
        } catch (Exception e) {
            log.error(e.toString());
        } finally {
            try {
                if(image != null) {
                    image.close();
                }
            } catch (IOException e) {
                log.error(e.toString());
            }
        }
        return false;
    }
    /**图片浮动处理
     * @param run
     * @param i
     * @param width
     * @param height
     * @param left
     * @param top
     * @throws XmlException
     */

    private void imageUpsetDeal(XWPFRun run, int i, int width, int height, int left, int top) throws XmlException {
        // 获取到浮动图片数据
        CTDrawing mid_drawing = run.getCTR().getDrawingArray(i);
        CTGraphicalObject mid_graphicalobject = mid_drawing.getInlineArray(0).getGraphic();
        // 拿到新插入的图片替换添加CTAnchor 设置浮动属性 删除inline属性
        CTAnchor mid_anchor = getAnchorWithGraphic(mid_graphicalobject, "mid",
                Units.toEMU(width), Units.toEMU(height),//图片大小
                Units.toEMU(left), Units.toEMU(top), true);//相对当前段落位置 需要计算段落已有内容的左偏移
        mid_drawing.setAnchorArray(new CTAnchor[]{mid_anchor});//添加浮动属性
        mid_drawing.removeInline(0);//删除行内属性
    }
    /**
     * 替换表格中的图片
     * @param table
     * @param map
     */
    private  void replaceTableImage(XWPFTable table, Map<String, Object> map) throws IOException{
        for (int i = 0; i < table.getRows().size(); i++) {
            List<XWPFTableCell> ListCell = table.getRows().get(i).getTableCells();
            for (XWPFTableCell cell : ListCell) {
                List<XWPFParagraph> cellParList = cell.getParagraphs();
                for (int p = 0; cellParList != null && p < cellParList.size(); p++) {
                    replaceParagraphImage(cellParList.get(p),map);
                }
            }
        }
    }

    /**
     * 图片叠加方法(png格式，第一张为背景图可以传透明图片，从第二张开始浮动)
     * @param run
     * @param imageList
     * @return
     */
    public boolean overlayPictures(XWPFRun run, List<PictureFloat> imageList) {
        // 图片路径
        FileInputStream image = null;
        try {
            if( imageList!=null&&!imageList.isEmpty()) {
                run.setText("",0);
                for (int i = 0; i < imageList.size(); i++) {
                    PictureFloat imageFloat = imageList.get(i);
                    image = new FileInputStream(imageFloat.getWjlj());
                    run.addPicture(image, XWPFDocument.PICTURE_TYPE_PNG, imageFloat.getWjlj(), Units.toEMU(imageFloat.getWidth()), Units.toEMU(imageFloat.getHeight()));
                    if(i > 0) {
                        imageFloatDeal(run, i, imageFloat.getWidth(),
                                imageFloat.getHeight(), imageFloat.getLeft(), imageFloat.getTop());
                    }
                    image.close();
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e.toString());
        } finally {
            try {
                if(image != null){
                    image.close();
                }
            } catch (IOException e) {
                log.error(e.toString());
            }
        }
        return false;
    }

    /**图片浮动处理
     * @param run
     * @param i
     * @param width
     * @param height
     * @param left
     * @param top
     * @throws XmlException
     */
    private void imageFloatDeal(XWPFRun run, int i, int width, int height, int left, int top) throws XmlException {
        // 获取到浮动图片数据
        CTDrawing mid_drawing = run.getCTR().getDrawingArray(i);
        CTGraphicalObject mid_graphicalobject = mid_drawing.getInlineArray(0).getGraphic();
        // 拿到新插入的图片替换添加CTAnchor 设置浮动属性 删除inline属性
        CTAnchor mid_anchor = getAnchorWithGraphic(mid_graphicalobject, "mid",
                Units.toEMU(width), Units.toEMU(height),//图片大小
                Units.toEMU(left), Units.toEMU(top), false);//相对当前段落位置 需要计算段落已有内容的左偏移
        mid_drawing.setAnchorArray(new CTAnchor[]{mid_anchor});//添加浮动属性
        mid_drawing.removeInline(0);//删除行内属性
    }

    /**word添加图片
     * @param ctGraphicalObject 图片数据
     * @param deskFileName      图片描述
     * @param width             宽
     * @param height            高
     * @param leftOffset        水平偏移 left
     * @param topOffset         垂直偏移 top
     * @param behind            文字上方，文字下方
     * @return
     * @throws XmlException
     * @throws Exception
     */
    private CTAnchor getAnchorWithGraphic(CTGraphicalObject ctGraphicalObject, String deskFileName, int width, int height, int leftOffset, int topOffset, boolean behind) throws XmlException {
        String anchorXML = "<wp:anchor xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                + "simplePos=\"0\" relativeHeight=\"0\" behindDoc=\"" + ((behind) ? 1 : 0) + "\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"1\">"
                + "<wp:simplePos x=\"0\" y=\"0\"/>"
                + "<wp:positionH relativeFrom=\"column\">"
                + "<wp:posOffset>" + leftOffset + "</wp:posOffset>"
                + "</wp:positionH>"
                + "<wp:positionV relativeFrom=\"paragraph\">"
                + "<wp:posOffset>" + topOffset + "</wp:posOffset>"
                + "</wp:positionV>"
                + "<wp:extent cx=\"" + width + "\" cy=\"" + height + "\"/>"
                + "<wp:effectExtent l=\"0\" t=\"0\" r=\"0\" b=\"0\"/>"
                + "<wp:wrapNone/>"
                + "<wp:docPr id=\"1\" name=\"Drawing 0\" descr=\"" + deskFileName + "\"/><wp:cNvGraphicFramePr/>"
                + "</wp:anchor>";
        CTDrawing drawing;
        drawing = CTDrawing.Factory.parse(anchorXML);
        CTAnchor anchor = drawing.getAnchorArray(0);
        anchor.setGraphic(ctGraphicalObject);
        return anchor;
    }

    /**
     * 设置页眉
     * @param paragraph
     * @param map
     */
    private void replaceHeader(XWPFParagraph paragraph,Map<String,Object> map){
        List<XWPFRun> runs = paragraph.getRuns();
        for (XWPFRun run : runs) {
            // 获取文本信息
            String text = run.getText(run.getTextPosition());
            if (text == null) {
                continue;
            }
            while (true) {
                int startTagPos = text.indexOf("«");
                int endTagPos = text.indexOf("»");
                // 寻找到需要替换的变量，如果没有找到则到下一个run
                if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
                    String varString = text.substring(startTagPos + 1, endTagPos);
                    Object object_value = map.get(varString);
                    if (object_value instanceof String) {
                        String rep_value = (String) object_value;
                        // 如果找不到相应的替换问题，则替换成空字符串
                        if (StringUtil.isBlank(rep_value)) {
                            text = text.replace("«" + varString + "»", "");
                        } else {
                            text = text.replace("«" + varString + "»", rep_value);
                        }
                        run.setText(text, 0);
                    } else {
                        run.setText(text, 0);
                    }
                } else {
                    break;
                }
            }
        }
    }

    /**
     * 设置页脚
     * @param paragraph
     * @param map
     */
    private void replaceFooter(XWPFParagraph paragraph,Map<String,Object> map){
        List<XWPFRun> runs = paragraph.getRuns();
        for (XWPFRun run : runs) {
            // 获取文本信息
            String text = run.getText(run.getTextPosition());
            if (text == null) {
                continue;
            }
            while (true) {
                int startTagPos = text.indexOf("«");
                int endTagPos = text.indexOf("»");
                // 寻找到需要替换的变量，如果没有找到则到下一个run
                if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
                    String varString = text.substring(startTagPos + 1, endTagPos);
                    Object object_value = map.get(varString);
                    if (object_value instanceof String) {
                        String rep_value = (String) object_value;
                        // 如果找不到相应的替换问题，则替换成空字符串
                        if (StringUtil.isBlank(rep_value)) {
                            text = text.replace("«" + varString + "»", "");
                        } else {
                            text = text.replace("«" + varString + "»", rep_value);
                        }
                        run.setText(text, 0);
                    } else {
                        run.setText(text, 0);
                    }
                } else {
                    break;
                }
            }
        }
    }

    /**
     * 替换头部的信息
     * @param paragraph 当前段落
     * @param map       替换数据
     */
    private void replaceHead(XWPFParagraph paragraph, Map<String, Object> map) throws XmlException {
        for (int i = 0; i < paragraph.getCTP().sizeOfRArray(); i++) {
            XmlObject xml_obj = paragraph.getCTP().getRArray(i);
            String xml_str = xml_obj.toString();
            String t_xml_str = xml_str;
            while (StringUtils.isNotBlank(t_xml_str)) {
                int startTagPos = t_xml_str.indexOf("«");
                int endTagPos = t_xml_str.indexOf("»");
                if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
                    String varString = t_xml_str.substring(startTagPos + 1, endTagPos).trim();
                    Object object_value = map.get(varString);
                    if (object_value instanceof String) {
                        String rep_value = (String) object_value;
                        xml_str = xml_str.replaceAll("«" + varString + "»", rep_value);
                        XmlObject xobj = XmlObject.Factory.parse(xml_str);
                        xml_obj.set(xobj);
                    }
                }else {
                    break;
                }
                t_xml_str = t_xml_str.substring(endTagPos + 1);
            }
        }
    }

    /**
     * 替换表格中的信息
     * @param table
     * @param map
     */
    private void replaceTable(XWPFTable table, Map<String, Object> map) {
        if(table.getRows().isEmpty() ||table.getRows()==null) {
            return;
        }
        XWPFTableRow tmpRow = table.getRows().get(table.getRows().size() - 1);
        // 获取到最后行的第一列 ；
        XWPFTableCell lastcell = tmpRow.getCell(0);
        List<XWPFParagraph> lastcellParList = lastcell.getParagraphs();
        StringBuilder s_cellText = new StringBuilder();
        for (int p = 0; lastcellParList != null && p < lastcellParList.size(); p++) {
            String t_cellText = lastcellParList.get(p).getText();
            if (StringUtil.isNotBlank(t_cellText))
                s_cellText.append(t_cellText);
        }
        String cellText = s_cellText.toString();
        int lastStartTagPos = cellText.indexOf("«");
        // 判断如果为循环表格
        if (lastStartTagPos > -1 && cellText.contains("List.")) {
            // 获取循环表格的变量
            int lastEndTagPos = cellText.indexOf("»");
            // 寻找到需要替换的变量，如果没有找到则到下一个run
            if (lastEndTagPos > 0 && lastEndTagPos > lastStartTagPos) {
                String varString = cellText.substring(lastStartTagPos + 1, lastEndTagPos);
                replaceListTable(table, map, varString);
            }
        } else {
            // 普通表格，则进行替换操作
            // 获取到table 的行数 rows（多）
            for (int i = 0; i < table.getNumberOfRows(); i++) {
                // 拿到当前行的内容
                XWPFTableRow row = table.getRow(i);
                if (row.getCell(0).getText().contains("«tableList.")){
                    //log.error("替换表格中的list");
                    String mapString = row.getCell(0).getText().substring("«tableList.".length(),row.getCell(0).getText().indexOf("_"));
                    Object Listmap = map.get(mapString);
                    if(Listmap instanceof List){
                        List<Map<String,String>> list = (List<Map<String, String>>) Listmap;
                        //去掉行中的tableList.
                        removeSymbol(row,"tableList.");
                        //复制list.size()-1行,复制完后并对上一行进行替换
                        for (int j = 0; j < list.size(); j++) {
                            copy(table,row,i+j+1);
                            boolean isRowBlank = list.get(j).get("isBlank") != null && "true".equals(list.get(j).get("isBlank"));
                            List<XWPFTableCell> cells = row.getTable().getRow(i+j+1).getTableCells();
                            // 循环列（cells 多）
                            for (XWPFTableCell cell : cells) {
                                // 拿到当前列的 段落（多） 注意：poi操作word有bug，段落获取不完整。所以模板中需要直接粘贴，不能手动输入。
                                List<XWPFParagraph> cellParList = cell.getParagraphs();
                                // 循环每个段落
                                for (int p = 0; cellParList != null && p < cellParList.size(); p++) {
                                    List<XWPFRun> runs = cellParList.get(p).getRuns();
                                    for (int r = 0; runs != null && r < runs.size(); r++) {
                                        String text = runs.get(r).getText(runs.get(r).getTextPosition());
                                        if (StringUtil.isNotBlank(text)) {
                                            while (true) {
                                                int startTagPos = text.indexOf("«");
                                                int endTagPos = text.indexOf("»");
                                                if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
                                                    String rep_value = "";
                                                    String varString = text.substring(startTagPos + 1, endTagPos);
                                                    if (!isRowBlank){
                                                        String replaceString = varString.substring((mapString+"_").length());
                                                        if (replaceString.contains("text.")){
                                                            rep_value = replaceString.substring("text.".length());
                                                        }else {
                                                            Object object_value = list.get(j).get(replaceString);
                                                            if (object_value !=null) {
                                                                rep_value = object_value.toString();
                                                            }
                                                        }
                                                    }
                                                    text = text.replace("«" + varString + "»", rep_value);
                                                    runs.get(r).setText(text, 0);
                                                } else {
                                                    break;
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                        table.removeRow(i);
                    }
                }else {
                    // 获取当前行的列数
                    List<XWPFTableCell> cells = row.getTableCells();
                    // 循环列（cells 多）
                    for (XWPFTableCell cell : cells) {
                        // 拿到当前列的 段落（多） 注意：poi操作word有bug，段落获取不完整。所以模板中需要直接粘贴，不能手动输入。
                        List<XWPFParagraph> cellParList = cell.getParagraphs();
                        // 循环每个段落
                        for (int p = 0; cellParList != null && p < cellParList.size(); p++) {
                            replaceParagraph(cellParList.get(p), map);
                        }
                    }
                }
            }
        }

    }

    /**
     * 去掉行替换符中的部分字段.
     * @param row
     * @param symbol
     */
    private  void removeSymbol(XWPFTableRow row,String symbol) {
        List<XWPFTableCell> cells = row.getTableCells();
        for (XWPFTableCell cell : cells) {
            List<XWPFParagraph> parList = cell.getParagraphs();
            for (int i = 0; parList != null && i < parList.size(); i++) {
                List<XWPFRun> runs = parList.get(i).getRuns();
                for (int j = 0; runs != null && j < runs.size(); j++) {
                    String text = runs.get(i).getText(runs.get(i).getTextPosition());
                    if (StringUtil.isNotBlank(text)) {
                        while (true) {
                            int startTagPos = text.indexOf("«");
                            int endTagPos = text.indexOf("»");
                            if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
                                String varString = text.substring(startTagPos + 1, endTagPos);
                                if (varString.contains(symbol)) {
                                    text = text.replace(varString, varString.replace(symbol, ""));
                                    runs.get(i).setText(text, 0);
                                }else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 替换段落
     *
     * @param paragraph
     * @param map
     */
    private void replaceParagraph(XWPFParagraph paragraph, Map<String, Object> map) {
        List<XWPFRun> runs = paragraph.getRuns();
        for (int i = 0; runs != null && i < runs.size(); i++) {
            String text = runs.get(i).getText(runs.get(i).getTextPosition());
            if (StringUtil.isNotBlank(text)) {
                while (true) {
                    int startTagPos = text.indexOf("«");
                    int endTagPos = text.indexOf("»");
                    if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
                        String varString = text.substring(startTagPos + 1, endTagPos);
                        Object object_value = map.get(varString);
                        if (object_value !=null) {
                            String rep_value = object_value.toString();
                            if (StringUtil.isBlank(rep_value)) {
                                text = text.replace("«" + varString + "»", "");
                            }else {
                                text = text.replace("«" + varString + "»", rep_value);
                            }
                            runs.get(i).setText(text, 0);
                        }else {
                            text = text.replace("«" + varString + "»", "");
                            runs.get(i).setText(text, 0);
                        }
                    }else {
                        break;
                    }
                }
            }
        }
    }

    /**
     * 循环多个表格
     * @param table
     * @param map
     * @param varString
     */
    private void replaceListTable(XWPFTable table, Map<String, Object> map, String varString) {
        //获取list的名称，并从map中获取list
        String listName = varString.split("\\.")[0];
        Object o_list = map.get(listName);
        boolean isListFlg = o_list instanceof ArrayList<?>;
        if (o_list == null || !isListFlg){
            return;
        }
        @SuppressWarnings("unchecked")
        List<Object> objectDtoList = (List<Object>) o_list;
        // 如果没有数据，则清除掉空的row，显示为 --
        if (objectDtoList==null||objectDtoList.isEmpty()) {
            return;
        }
        // 获取表格的列数，根据最后一列
        int rowCnt = table.getRows().size();
        addNewRows(table ,objectDtoList);
        // 删除模版行
        table.removeRow(rowCnt - 1);
    }

    /**
     * 不需要添加图片的表格
     * @param table
     * @param objectDtoList
     */
    private void addNewRows(XWPFTable table,List<Object> objectDtoList) {
        XWPFTableRow tmpRow = table.getRows().get(table.getRows().size() - 1);
        List<XWPFTableCell> tmpCells = tmpRow.getTableCells();
        int cellsize = tmpCells.size();
        for (Object o : objectDtoList) {
            XWPFTableRow row = copy(table, tmpRow, table.getRows().size());
            Object objectDto = o;
            try {
                List<XWPFTableCell> cells = row.getTableCells();
                // 插入的行会填充与表格第一行相同的列数
                for (int i = 0; i < cellsize; i++) {
                    // 由于 list的 size 和 List<XWPFTableCell> cells的 size 相等，所以可以直接用一个索引
                    XWPFTableCell cell = cells.get(i);
                    XWPFTableCell tmpCell = tmpCells.get(i);
                    String tmpText = tmpCell.getText();
                    String cellText = cell.getText();
                    if (StringUtil.isNotBlank(tmpText)) {
                        List<XWPFParagraph> paragraphs = cell.getParagraphs();
                        if (paragraphs!=null&&!paragraphs.isEmpty()) {
                            for (XWPFParagraph paragraph : paragraphs) {
                                List<XWPFRun> runs = paragraph.getRuns();
                                if (runs !=null&&!runs.isEmpty()) {
                                    for (XWPFRun run : runs) {
                                        String text = run.getText(run.getTextPosition());
                                        int index = getXwpfParagraphIndex(text);
                                        String s_value;
                                        for (int y = 0; y < index; y++) {
                                            int startTagPos = text.indexOf("«");
                                            int endTagPos = text.indexOf("»");
                                            // 寻找到需要替换的变量，如果没有找到则到下一个run
                                            if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
                                                String tmpVar = text.substring(startTagPos + 1, endTagPos).trim().split("\\.")[1];
                                                String TotmpVar = tmpVar.substring(0, 1).toUpperCase() + tmpVar.substring(1);
                                                // 反射方法 把map的value 插入到数据中
                                                Method t_method = objectDto.getClass().getMethod("get" + TotmpVar);

                                                s_value = (String) t_method.invoke(objectDto);

                                                text = text.replace(text.substring(startTagPos, endTagPos + 1), s_value);
                                            }
                                        }
                                        run.setText(text);
                                    }
                                }
                            }
                        }

                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                log.error(e.toString());
            }
        }

    }

    /**
     * 复制一行
     * @param table
     * @param sourceRow
     * @param rowIndex
     * @return
     */
    public XWPFTableRow copy(XWPFTable table,XWPFTableRow sourceRow,int rowIndex){
        //在表格指定位置新增一行
        XWPFTableRow targetRow = table.insertNewTableRow(rowIndex);
        //复制行属性
        targetRow.getCtRow().setTrPr(sourceRow.getCtRow().getTrPr());
        List<XWPFTableCell> cellList = sourceRow.getTableCells();
        if (null == cellList) {
            return targetRow;
        }
        //复制列及其属性和内容
        XWPFTableCell targetCell;
        for (XWPFTableCell sourceCell : cellList) {
            targetCell = targetRow.addNewTableCell();
            //列属性
            targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
            //段落属性
            if( sourceCell.getParagraphs() !=null&&!sourceCell.getParagraphs().isEmpty()){
                for (int i = 0; i < sourceCell.getParagraphs().size(); i++) {
                    if (targetCell.getParagraphs().size()<sourceCell.getParagraphs().size()){
                        targetCell.addParagraph();
                    }
                    targetCell.getParagraphs().get(i).getCTP().setPPr(sourceCell.getParagraphs().get(i).getCTP().getPPr());
                    if(sourceCell.getParagraphs().get(i).getRuns() !=null&&!sourceCell.getParagraphs().get(i).getRuns().isEmpty()){
                        for (int j = 0; j < sourceCell.getParagraphs().get(i).getRuns().size(); j++) {
                            XWPFRun cellR = targetCell.getParagraphs().get(i).createRun();
                            cellR.setText(sourceCell.getParagraphs().get(i).getRuns().get(j).text());
                            cellR.setBold(sourceCell.getParagraphs().get(i).getRuns().get(j).isBold());
                            cellR.getCTR().setRPr(sourceCell.getParagraphs().get(i).getRuns().get(j).getCTR().getRPr());
                            if (sourceCell.getParagraphs().get(i).getRuns() !=null&& !sourceCell.getParagraphs().get(i).getRuns().isEmpty()){
                                if (StringUtil.isNotBlank(sourceCell.getParagraphs().get(i).getRuns().get(j).getFontFamily())){
                                    cellR.setFontFamily(sourceCell.getParagraphs().get(i).getRuns().get(j).getFontFamily());
                                }
                            }
                        }
                    }else{
                        targetCell.setText(sourceCell.getText());
                    }
                }
            }else{
                targetCell.setText(sourceCell.getText());
            }
        }
        return targetRow;
    }

    /**
     * 判断字符串是否在数组中存在
     * @param strs
     * @param s
     * @return
     */
    public static boolean isHave(String[] strs,String s){
        /*此方法有两个参数，第一个是要查找的字符串数组，第二个是要查找的字符或字符串*/
        for (String str : strs) {
            if (str.contains(s)) {//循环查找字符串数组中的每个字符串中是否包含所有查找的内容
                return true;//查找到了就返回真，不在继续查询
            }
        }
        return false;//没找到返回false
    }

    /**
     * 查看关键字“«” 出现的次数
     * @param text
     * @return
     */
    public int getXwpfParagraphIndex(String text) {
        int count = 0;
        char param = '«';
        for (int i = 0; i < text.length(); i++) {
            char cm = text.charAt(i);
            if (cm == param) {
                count++;
            }
        }
        return count;
    }
}

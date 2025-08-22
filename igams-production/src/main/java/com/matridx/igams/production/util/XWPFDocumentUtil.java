package com.matridx.igams.production.util;

import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.util.CommonWordUtil;
import com.matridx.igams.common.dao.entities.PictureFloat;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class XWPFDocumentUtil {
    private final Logger log = LoggerFactory.getLogger(XWPFDocumentUtil.class);

    public void replaceDocument(Map<String, String> map, IFjcfbService fjcfbService, String FTP_URL, String DOC_OK, AmqpTemplate amqpTempl) {
        Map<String, String> resultMap = new HashMap<>();
        DBEncrypt dbEncrypt = new DBEncrypt();
        FileInputStream inputStream = null;
        XWPFDocument document = null;
        OutputStream output = null;
        try {
            String filePath = dbEncrypt.dCode(map.get("wjlj"));
			CommonWordUtil commonwordutil = new CommonWordUtil();
			commonwordutil.reformWord(filePath);
            File file = new File(filePath);
            if (StringUtil.isBlank(filePath) || !file.exists()) {
                log.error("文件不存在！请重新确认文件！");
                resultMap.put("message", "文件不存在！请重新确认文件！");
                resultMap.put("status", "error");
                return;
            }
            inputStream = new FileInputStream(file);
            document = new XWPFDocument(inputStream);
            log.error("文件替换开始！");
            //处理表格
            Iterator<XWPFTable> itTable = document.getTablesIterator();
            while (itTable.hasNext()) {
                XWPFTable table = itTable.next();
                // 替换表中的数据
                replaceTable(table, map);
            }

             //替换页眉
            List<XWPFHeader> headerList = document.getHeaderList();
            for (XWPFHeader header : headerList) {
                List<XWPFParagraph> paragraphs = header.getParagraphs();
                for (XWPFParagraph paragraph : paragraphs) {
                    replaceHeader(paragraph,map);
                }
                List<XWPFTable> tables = header.getTables();
                for (XWPFTable table : tables) {
                    // 替换表中的数据
                    replaceTable(table, map);
                }
            }
            //替换页脚
            List<XWPFFooter> FooterList = document.getFooterList();
            for (XWPFFooter footer : FooterList) {
                List<XWPFParagraph> paragraphs = footer.getParagraphs();
                for (XWPFParagraph paragraph : paragraphs) {
                    replaceFooter(paragraph,map);
                }
                List<XWPFTable> tables = footer.getTables();
                for (XWPFTable table : tables) {
                    // 替换表中的数据
                    replaceTable(table, map);
                }
            }
            inputStream.close();
            output = new FileOutputStream(dbEncrypt.dCode(map.get("wjlj")));
            document.write(output);

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (output != null)
                    output.close();
                if (document != null)
                    document.close();
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        log.error("文件替换完成！");
        resultMap.put("message", "文件替换完成");
        resultMap.put("status", "success");
    }


    /**
     * 替换表格中的信息
     * table   //当前迭代的表格
     * map //需要替换的数据
     */
    private void replaceTable(XWPFTable table, Map<String, String> map) throws IOException {
        if(CollectionUtils.isEmpty(table.getRows())) {
            return;
        }
        for (int i = 0; i < table.getRows().size(); i++) {
            List<XWPFTableCell> ListCell = table.getRows().get(i).getTableCells();
            for (XWPFTableCell cell : ListCell) {
                List<XWPFParagraph> cellParList = cell.getParagraphs();
                for (int p = 0; cellParList != null && p < cellParList.size(); p++) {
                    List<XWPFRun> runs = cellParList.get(p).getRuns();
                    for (int q = 0; runs != null && q < runs.size(); q++) {
                        String oneparaString = runs.get(q).getText(0);
                        if (StringUtil.isBlank(oneparaString)){
                            continue;
                        }
                        int startTagPos = oneparaString.indexOf("«");
                        int endTagPos = oneparaString.indexOf("»");
                        if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
                            String varString = oneparaString.substring(startTagPos + 1, endTagPos).trim();
                            if (StringUtil.isNotBlank(varString)) {
                                String rep_value = String.valueOf(map.get(varString));
                                if (varString.contains("Pic")){
                                    oneparaString = oneparaString.replace("«" + varString + "»", "");
                                    List<PictureFloat> list=new ArrayList<>();
                                    DBEncrypt dbEncrypt = new DBEncrypt();
                                    String imagetFile = dbEncrypt.dCode(map.get(varString));
                                    File picture = new File(imagetFile);
                                    BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
                                    //获取图片长宽，以及长宽的比列
                                    int height = sourceImg.getHeight();
                                    int width = sourceImg.getWidth();
                                    BigDecimal divide = new BigDecimal(width).divide(new BigDecimal(height), 2, RoundingMode.HALF_EVEN);
                                    int imgWidth = new BigDecimal(20).multiply(divide).intValue();
                                    overlayPictures(runs.get(q),list);
                                    PictureFloat imageFloat=new PictureFloat(dbEncrypt.dCode(map.get(varString)), imgWidth, 20, 0, 0);
                                    list.add(imageFloat);

                                    overlayPictures(runs.get(q),list);
                                }else {
                                    if (StringUtil.isNotBlank(rep_value) && map.get(varString) != null) {
                                        oneparaString = oneparaString.replace("«" + varString + "»", rep_value);
                                    } else {
                                        oneparaString = oneparaString.replace("«" + varString + "»", "");
                                    }
                                }
                                runs.get(q).setText(oneparaString, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 图片叠加方法(png格式，第一张为背景图可以传透明图片，从第二张开始浮动)
     */
    public void overlayPictures(XWPFRun run, List<PictureFloat> imageList) {
        // 图片路径
        FileInputStream image = null;
        try {
            if(!CollectionUtils.isEmpty(imageList)) {
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
        } catch (Exception e) {
            log.error(e.toString());
        } finally {
            try {
                if(image != null)
                    image.close();
            } catch (IOException e) {
                log.error(e.toString());
            }
        }
    }

    /**图片浮动处理
     */
    private void imageFloatDeal(XWPFRun run, int i, int width, int height, int left, int top) throws XmlException {
        // 获取到浮动图片数据
        CTDrawing mid_drawing = run.getCTR().getDrawingArray(i);
        CTGraphicalObject mid_graphicalobject = mid_drawing.getInlineArray(0).getGraphic();
        // 拿到新插入的图片替换添加CTAnchor 设置浮动属性 删除inline属性
        CTAnchor mid_anchor = getAnchorWithGraphic(mid_graphicalobject,
                Units.toEMU(width), Units.toEMU(height),//图片大小
                Units.toEMU(left), Units.toEMU(top));//相对当前段落位置 需要计算段落已有内容的左偏移
        mid_drawing.setAnchorArray(new CTAnchor[]{mid_anchor});//添加浮动属性
        mid_drawing.removeInline(0);//删除行内属性
    }

    /**
     * word添加图片
     *
     * @param ctGraphicalObject 图片数据
     * @param width             宽
     * @param height            高
     * @param leftOffset        水平偏移 left
     * @param topOffset         垂直偏移 top
     */
    private CTAnchor getAnchorWithGraphic(CTGraphicalObject ctGraphicalObject, int width, int height, int leftOffset, int topOffset) throws XmlException {
        String anchorXML = "<wp:anchor xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                + "simplePos=\"0\" relativeHeight=\"0\" behindDoc=\"" + (0) + "\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"1\">"
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
                + "<wp:docPr id=\"1\" name=\"Drawing 0\" descr=\"" + "mid" + "\"/><wp:cNvGraphicFramePr/>"
                + "</wp:anchor>";
        CTDrawing drawing = CTDrawing.Factory.parse(anchorXML);
        CTAnchor anchor = drawing.getAnchorArray(0);
        anchor.setGraphic(ctGraphicalObject);
        return anchor;
    }


    /**
     * 设置页眉
     */
    private void replaceHeader(XWPFParagraph paragraph,Map<String,String> map){
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
                    String object_value = map.get(varString);
                    if (object_value != null) {
                        // 如果找不到相应的替换问题，则替换成空字符串
                        if (StringUtil.isBlank(object_value)) {
                            text = text.replace("«" + varString + "»", "");
                        } else {
                            text = text.replace("«" + varString + "»", object_value);
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
     */
    private void replaceFooter(XWPFParagraph paragraph,Map<String,String> map){
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
                    String object_value = map.get(varString);
                    if (object_value != null) {
                        // 如果找不到相应的替换问题，则替换成空字符串
                        if (StringUtil.isBlank(object_value)) {
                            text = text.replace("«" + varString + "»", "");
                        } else {
                            text = text.replace("«" + varString + "»", object_value);
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
}

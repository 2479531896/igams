package com.matridx.igams.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beyongx.echarts.Option;
import com.beyongx.echarts.charts.Bar;
import com.beyongx.echarts.charts.Line;
import com.beyongx.echarts.charts.Pie;
import com.beyongx.echarts.options.*;
import com.matridx.igams.common.file.Jacob;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/javaEcharts")
public class javaEchartsController extends BaseController {


    private static final Logger logger = LoggerFactory.getLogger(javaEchartsController.class);

//    /**
//     * phantomjs 路径
//     */
//    private static String PHANTOMJS_PATH = "D:\\java\\pjs\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe";
//    /**
//     * echartsJs 路径
//     */
//    private static String ECHARTS_PATH = "E:\\kfhj\\igams-style\\src\\main\\resources\\static\\js\\plugins\\echarts\\echarts-convert.js";

    /**
     * echarts获取图片base64编码URL头
     */
    private static final String BASE64FORMAT = "data:image/png;base64,";

    private List<String> jsonList = new ArrayList<>();
    private String jsrqstart = "";
    private String jsrqend = "";

    @Value("${matridx.fileupload.releasePath:}")
    private String releasePath;

    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;

    @RequestMapping("/javaEcharts/pagedataJavaEchart")
    public void testJavaEchart() {
        int dayOfWeek = DateUtils.getWeek(new Date());
        if (dayOfWeek <= 2) {
            jsrqstart = DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 8), "yyyy-MM-dd");
            jsrqend = DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 2), "yyyy-MM-dd");
        } else {
            jsrqstart = DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 1), "yyyy-MM-dd");
            jsrqend = DateUtils.format(DateUtils.getDate(new Date(), 5 - dayOfWeek), "yyyy-MM-dd");
        }
        getJSON();

        BufferedReader input = null;
        try {

            List<String> urls = new ArrayList<>();
            for (String _url : jsonList) {
                String url = writeFile(_url, releasePath + "/javaEchart/");
                urls.add(url);
            }

            List<String> base64s = new ArrayList<>();
            for (String _url : urls) {
                String base64;
                /*
                  命令格式：
                  phantomjs echarts-convert.js -infile optionURl -width width -height height
                  可选参数：-width width -height height
                  备注：
                  phantomjs添加到环境变量中后可以直接使用，这里防止环境变量配置问题所以直接使用绝对路径
                 */
                logger.error("生成得url:" + _url);
                /*
                  phantomjs 路径
                 */
                String PHANTOMJS_PATH = "/home/centos/phantomjs/phantomjs-2.1.1-linux-x86_64/bin/phantomjs";
                /*
                  echartsJs 路径
                 */
                String ECHARTS_PATH = "/home/centos/phantomjs/echarts/echarts-convert.js";
                String cmd = PHANTOMJS_PATH + " " + ECHARTS_PATH + " -infile " + _url + " -width " + 536 + " -height " + 320;
                logger.error("命令:" + cmd);
                Process process = Runtime.getRuntime().exec(cmd);
                /*
                  获取控制台输出信息
                  通过JS中使用console.log()打印输出base64编码
                  获取进程输入流，进行base64编码获取
                 */
                input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                while ((line = input.readLine()) != null) {
                    logger.error("不是base64" + line);
                    if (line.startsWith(BASE64FORMAT)) {
                        base64 = line.replace(BASE64FORMAT, "");
                        base64s.add(base64);
                        logger.error("js返回数据" + base64);
                        break;
                    }
                }
                logger.error("分割线--------------------------------" + line);
                input.close();
                process.waitFor();
            }
            // 生成word页节点
            XWPFDocument doc = new XWPFDocument();
            // 生成word段落
            XWPFParagraph page = doc.createParagraph();
            page.setAlignment(ParagraphAlignment.CENTER);
            page.setVerticalAlignment(TextAlignment.TOP);
            // 标题部分
            XWPFRun run = page.createRun();
            run.setBold(true); // 设置为粗体
            run.setFontSize(28);
            run.setText("营销数据看板");

            List<String> list = new ArrayList<>();
            list.add("Hi all ，本周数据如下");
            for (String s : list) {
                page = doc.createParagraph();
                page.setAlignment(ParagraphAlignment.LEFT);
                page.setVerticalAlignment(TextAlignment.TOP);
                run = page.createRun(); // 一条数据一个段落
                // 文本段落
                run.setText(String.valueOf(s));
            }
            page = doc.createParagraph();
            page.setAlignment(ParagraphAlignment.LEFT);
            page.setVerticalAlignment(TextAlignment.TOP);
            run = page.createRun(); // 一条数据一个段落
            run.setText("年度平台指标达成率");
            ptzbdcl_nd(doc);
            page = doc.createParagraph();
            page.setAlignment(ParagraphAlignment.LEFT);
            page.setVerticalAlignment(TextAlignment.TOP);
            run = page.createRun(); // 一条数据一个段落
            run.setText("季度平台指标达成率");
            ptzbdcl_jd(doc);
            page = doc.createParagraph();
            page.setAlignment(ParagraphAlignment.LEFT);
            page.setVerticalAlignment(TextAlignment.TOP);
            run = page.createRun(); // 一条数据一个段落
            run.setText("月度平台指标达成率");
            ptzbdcl_yd(doc);
            // 重新生成段落进行图片的绘制
            page = doc.createParagraph();
            run = page.createRun();
            for (String base64 : base64s) {
                byte[] data = Base64.getDecoder().decode(base64.replace("data:image/png;base64,", ""));
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                run.addPicture(bais, XWPFDocument.PICTURE_TYPE_PNG, ".png", Units.toEMU(400), Units.toEMU(240));
            }

            File file = new File(releasePath + "/javaEchart/");
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(new File(file, "text2.docx"));
            doc.write(fos);
            doc.close();
            fos.close();
            Jacob.word2pdf("E:\\matridx\\fileupload\\release\\javaEchart\\text2.docx", "E:\\matridx\\fileupload\\release\\javaEchart\\营销数据看板.pdf");
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        jsonList = new ArrayList<>();
    }


    public void getJSON() {
        qgqs_month();
        qgqs_week();
        fcpqs_mon();
        fcpqs_week();
        ptqs_mon();
        ptqs_week();
        topcos15_mon();
        topdsf15_mon();
        topry15_mon();
        topzx15_mon();
        ptywzb_mon();
        xsqdlxzb_mon();
        xsxzzb_mon();
    }

    /***
     * 全国周趋势
     */
    public void qgqs_week() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("zq", "12");
        paramMap.add("method", "getQgqsSjxxWeek");
        paramMap.add("tj", "week");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("hbfl", "qgqs");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object qgqs = map.get("qgqs");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(qgqs));
        List<String> dataxAxis = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
            if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                if (!dataxAxis.contains(job.get("rq").toString().substring(5, 10))) {
                    dataxAxis.add(job.get("rq").toString().substring(5, 10));
                }
            } else {
                if (!dataxAxis.contains(job.get("rq").toString())) {
                    dataxAxis.add(job.get("rq").toString());
                }
            }
        }
        List<String> dataZx = new ArrayList<>();
        List<String> dataDsf = new ArrayList<>();
        List<String> datahj = new ArrayList<>();
        List<String> dataDls = new ArrayList<>();
        for (var i = 0; i < dataxAxis.size(); i++) {
            int sum = 0;
            for (Object o : jsonArray) {
                JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
                if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                    var date = job.get("rq").toString().substring(5, 10);
                    if (dataxAxis.get(i).equals(date)) {
                        if (job.get("hbflmc") != null && job.get("hbflmc").equals("直销")) {
                            sum += Integer.parseInt(job.get("zxnum") == null ? "0" : job.get("zxnum").toString());
                            dataZx.add(job.get("zxnum") == null ? "0" : job.get("zxnum").toString());
                        } else if (job.get("hbflmc") != null && job.get("hbflmc").equals("第三方")) {
                            dataDsf.add(job.get("dsfnum") == null ? "0" : job.get("dsfnum").toString());
                            sum += Integer.parseInt(job.get("dsfnum") == null ? "0" : job.get("dsfnum").toString());
                        } else if (job.get("hbflmc") != null && job.get("hbflmc").equals("CSO")) {
                            dataDls.add(job.get("dlsnum") == null ? "0" : job.get("dlsnum").toString());
                            sum += Integer.parseInt(job.get("dlsnum") == null ? "0" : job.get("dlsnum").toString());
                        }
                    }
                } else {
                    var date = job.get("rq").toString();
                    if (dataxAxis.get(i).equals(date)) {
                        if (job.get("hbflmc") != null && job.get("hbflmc").equals("直销")) {
                            sum += Integer.parseInt(job.get("zxnum") == null ? "0" : job.get("zxnum").toString());
                            dataZx.add(job.get("zxnum") == null ? "0" : job.get("zxnum").toString());
                        } else if (job.get("hbflmc") != null && job.get("hbflmc").equals("第三方")) {
                            dataDsf.add(job.get("dsfnum") == null ? "0" : job.get("dsfnum").toString());
                            sum += Integer.parseInt(job.get("dsfnum") == null ? "0" : job.get("dsfnum").toString());
                        } else if (job.get("hbflmc") != null && job.get("hbflmc").equals("CSO")) {
                            dataDls.add(job.get("dlsnum") == null ? "0" : job.get("dlsnum").toString());
                            sum += Integer.parseInt(job.get("dlsnum") == null ? "0" : job.get("dlsnum").toString());
                        }
                    }
                }

            }
            if (dataDls.size() < i + 1) {
                dataDls.add("0");
            }
            if (dataZx.size() < i + 1) {
                dataZx.add("0");
            }
            if (dataDsf.size() < i + 1) {
                dataDsf.add("0");
            }
            datahj.add(sum + "");
        }
        int maxY = 0;

        for (var i = 0; i < dataDls.size(); i++) {
            int number = 0;
            if (StringUtil.isNotBlank(dataDls.get(i))) {
                number += Integer.parseInt(dataDls.get(i));
            }
            if (StringUtil.isNotBlank(dataDsf.get(i))) {
                number += Integer.parseInt(dataDsf.get(i));
            }
            if (StringUtil.isNotBlank(dataZx.get(i))) {
                number += Integer.parseInt(dataZx.get(i));
            }
            if (number > maxY) {
                maxY = Integer.parseInt(dataDls.get(i)) + Integer.parseInt(dataDsf.get(i)) + Integer.parseInt(dataZx.get(i));
            }
        }
        maxY = maxY + 4 - (maxY % 4);
        int intval = maxY / 4;
        Option option = new Option();

        Bar bar1 = new Bar();
        bar1.setName("直销");
        bar1.setStack("总量");
        bar1.setBarGap("0");
        Map<String, Object> bar1LabelMap = new HashMap<>();
        Map<String, Object> bar1morMap = new HashMap<>();
        bar1morMap.put("show", true);
        bar1morMap.put("position", "insideBottom");
        bar1LabelMap.put("normal", bar1morMap);
        bar1.setLabel(bar1LabelMap);
        bar1.setData(dataZx.toArray());
        Map<String, Object> bar1ColorMap = new HashMap<>();
        bar1ColorMap.put("color", "rgb(249,174,0)");
        bar1.setItemStyle(bar1ColorMap);
        Bar bar2 = new Bar();
        bar2.setName("第三方");
        bar2.setStack("总量");
        bar2.setBarGap("0");
        Map<String, Object> bar2LabelMap = new HashMap<>();
        Map<String, Object> bar2morMap = new HashMap<>();
        bar2morMap.put("show", true);
        bar2morMap.put("position", "insideBottom");
        bar2LabelMap.put("normal", bar2morMap);
        bar2.setLabel(bar2LabelMap);
        bar2.setData(dataDsf.toArray());
        Map<String, Object> bar2ColorMap = new HashMap<>();
        bar2ColorMap.put("color", "rgb(209, 95, 126)");
        bar2.setItemStyle(bar2ColorMap);
        Bar bar3 = new Bar();
        bar3.setName("CSO");
        bar3.setStack("总量");
        bar3.setBarGap("0");
        Map<String, Object> bar3LabelMap = new HashMap<>();
        Map<String, Object> bar3morMap = new HashMap<>();
        bar3morMap.put("show", true);
        bar3morMap.put("position", "insideBottom");
        bar3LabelMap.put("normal", bar3morMap);
        bar3.setLabel(bar3LabelMap);
        bar3.setData(dataDls.toArray());
        Map<String, Object> bar3ColorMap = new HashMap<>();
        bar3ColorMap.put("color", "#8e0000");
        bar3.setItemStyle(bar3ColorMap);
        Bar bar4 = new Bar();
        bar4.setName("合计");
        bar4.setStack("总量");
        bar4.setBarGap("0");
        Map<String, Object> bar4LabelMap = new HashMap<>();
        Map<String, Object> bar4morMap = new HashMap<>();
        Map<String, Object> bar4TextStyleMap = new HashMap<>();
        bar4TextStyleMap.put("color", "#ff4848");
        bar4TextStyleMap.put("fontSize", "12");
        bar4morMap.put("show", true);
        bar4morMap.put("textStyle", bar4TextStyleMap);
        bar4morMap.put("position", "insideBottom");
        bar4LabelMap.put("normal", bar4morMap);
        bar4.setLabel(bar4LabelMap);
        bar4.setData(datahj.toArray());
        Map<String, Object> bar4ColorMap = new HashMap<>();
        bar4ColorMap.put("color", "rgba(128, 128, 128, 0)");
        bar4.setItemStyle(bar4ColorMap);
        Bar[] chartList = new Bar[]{bar1, bar2, bar3, bar4};
        option.series(chartList);

        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        option.tooltip(tooltip);
        Title title = new Title();
        title.setSubtext("全国周趋势");
        Map<String,Object>textMap=new HashMap<>();
        textMap.put("color","black");
        textMap.put("fontSize","18");
        textMap.put("fontWeight","bold");
        title.setSubtextStyle(textMap);
        option.title(title);
        Grid grid = new Grid();
        grid.setRight(0);
        grid.setTop(90);
        grid.setLeft(10);
        grid.setHeight(200);
        grid.setContainLabel("true");
        option.grid(grid);
        XAxis xAxis = new XAxis();
        xAxis.setType("category");
        xAxis.setData(dataxAxis.toArray());
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("value");
        yAxis.setMax(maxY);
        yAxis.setInterval(intval);
        option.yAxis(yAxis);
        Legend legend = new Legend();
        String[] strArr = new String[]{"直销", "第三方", "CSO"};
        legend.setData(strArr);
        Map<String, Object> textStyleMap = new HashMap<>();
        textStyleMap.put("color", "#3E9EE1");
        legend.setTextStyle(textStyleMap);
        option.legend(legend);
        JSONObject jsonObject1 = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj1 = JSONObject.parseObject(JSON.toJSONString(jsonObject1.get("title")));
        titleObj1.put("x", "left");
        titleObj1.put("y", "5");
        jsonObject1.put("title", titleObj1);
        JSONObject legendObj1 = JSONObject.parseObject(JSON.toJSONString(jsonObject1.get("legend")));
        legendObj1.put("y", "35");
        jsonObject1.put("legend", legendObj1);
        jsonList.add(jsonObject1.toJSONString());
    }

    /***
     * 全国月趋势
     */
    public void qgqs_month() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("zq", "3");
        paramMap.add("method", "getQgqsSjxxMon");
        paramMap.add("tj", "mon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("hbfl", "qgqs");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object qgqs = map.get("qgqs");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(qgqs));
        List<String> dataxAxis = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
            if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                if (!dataxAxis.contains(job.get("rq").toString().substring(5, 10))) {
                    dataxAxis.add(job.get("rq").toString().substring(5, 10));
                }
            } else {
                if (!dataxAxis.contains(job.get("rq").toString())) {
                    dataxAxis.add(job.get("rq").toString());
                }
            }
        }
        List<String> dataZx = new ArrayList<>();
        List<String> dataDsf = new ArrayList<>();
        List<String> datahj = new ArrayList<>();
        List<String> dataDls = new ArrayList<>();
        for (var i = 0; i < dataxAxis.size(); i++) {
            int sum = 0;
            for (Object o : jsonArray) {
                JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
                if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                    var date = job.get("rq").toString().substring(5, 10);
                    if (dataxAxis.get(i).equals(date)) {
                        if (job.get("hbflmc") != null && job.get("hbflmc").equals("直销")) {
                            sum += Integer.parseInt(job.get("zxnum") == null ? "0" : job.get("zxnum").toString());
                            dataZx.add(job.get("zxnum") == null ? "0" : job.get("zxnum").toString());
                        } else if (job.get("hbflmc") != null && job.get("hbflmc").equals("第三方")) {
                            dataDsf.add(job.get("dsfnum") == null ? "0" : job.get("dsfnum").toString());
                            sum += Integer.parseInt(job.get("dsfnum") == null ? "0" : job.get("dsfnum").toString());
                        } else if (job.get("hbflmc") != null && job.get("hbflmc").equals("CSO")) {
                            dataDls.add(job.get("dlsnum") == null ? "0" : job.get("dlsnum").toString());
                            sum += Integer.parseInt(job.get("dlsnum") == null ? "0" : job.get("dlsnum").toString());
                        }
                    }
                } else {
                    var date = job.get("rq").toString();
                    if (dataxAxis.get(i).equals(date)) {
                        if (job.get("hbflmc") != null && job.get("hbflmc").equals("直销")) {
                            sum += Integer.parseInt(job.get("zxnum") == null ? "0" : job.get("zxnum").toString());
                            dataZx.add(job.get("zxnum") == null ? "0" : job.get("zxnum").toString());
                        } else if (job.get("hbflmc") != null && job.get("hbflmc").equals("第三方")) {
                            dataDsf.add(job.get("dsfnum") == null ? "0" : job.get("dsfnum").toString());
                            sum += Integer.parseInt(job.get("dsfnum") == null ? "0" : job.get("dsfnum").toString());
                        } else if (job.get("hbflmc") != null && job.get("hbflmc").equals("CSO")) {
                            dataDls.add(job.get("dlsnum") == null ? "0" : job.get("dlsnum").toString());
                            sum += Integer.parseInt(job.get("dlsnum") == null ? "0" : job.get("dlsnum").toString());
                        }
                    }
                }

            }
            if (dataDls.size() < i + 1) {
                dataDls.add("0");
            }
            if (dataZx.size() < i + 1) {
                dataZx.add("0");
            }
            if (dataDsf.size() < i + 1) {
                dataDsf.add("0");
            }
            datahj.add(sum + "");
        }
        int maxY = 0;

        for (var i = 0; i < dataDls.size(); i++) {
            int number = 0;
            if (StringUtil.isNotBlank(dataDls.get(i))) {
                number += Integer.parseInt(dataDls.get(i));
            }
            if (StringUtil.isNotBlank(dataDsf.get(i))) {
                number += Integer.parseInt(dataDsf.get(i));
            }
            if (StringUtil.isNotBlank(dataZx.get(i))) {
                number += Integer.parseInt(dataZx.get(i));
            }
            if (number > maxY) {
                maxY = Integer.parseInt(dataDls.get(i)) + Integer.parseInt(dataDsf.get(i)) + Integer.parseInt(dataZx.get(i));
            }
        }
        maxY = maxY + 4 - (maxY % 4);
        int intval = maxY / 4;
        Option option = new Option();

        Bar bar1 = new Bar();
        bar1.setName("直销");
        bar1.setStack("总量");
        bar1.setBarGap("0");
        Map<String, Object> bar1LabelMap = new HashMap<>();
        Map<String, Object> bar1morMap = new HashMap<>();
        bar1morMap.put("show", true);
        bar1morMap.put("position", "insideBottom");
        bar1LabelMap.put("normal", bar1morMap);
        bar1.setLabel(bar1LabelMap);
        bar1.setData(dataZx.toArray());
        Map<String, Object> bar1ColorMap = new HashMap<>();
        bar1ColorMap.put("color", "rgb(249,174,0)");
        bar1.setItemStyle(bar1ColorMap);
        Bar bar2 = new Bar();
        bar2.setName("第三方");
        bar2.setStack("总量");
        bar2.setBarGap("0");
        Map<String, Object> bar2LabelMap = new HashMap<>();
        Map<String, Object> bar2morMap = new HashMap<>();
        bar2morMap.put("show", true);
        bar2morMap.put("position", "insideBottom");
        bar2LabelMap.put("normal", bar2morMap);
        bar2.setLabel(bar2LabelMap);
        bar2.setData(dataDsf.toArray());
        Map<String, Object> bar2ColorMap = new HashMap<>();
        bar2ColorMap.put("color", "rgb(209, 95, 126)");
        bar2.setItemStyle(bar2ColorMap);
        Bar bar3 = new Bar();
        bar3.setName("CSO");
        bar3.setStack("总量");
        bar3.setBarGap("0");
        Map<String, Object> bar3LabelMap = new HashMap<>();
        Map<String, Object> bar3morMap = new HashMap<>();
        bar3morMap.put("show", true);
        bar3morMap.put("position", "insideBottom");
        bar3LabelMap.put("normal", bar3morMap);
        bar3.setLabel(bar3LabelMap);
        bar3.setData(dataDls.toArray());
        Map<String, Object> bar3ColorMap = new HashMap<>();
        bar3ColorMap.put("color", "#8e0000");
        bar3.setItemStyle(bar3ColorMap);
        Bar bar4 = new Bar();
        bar4.setName("合计");
        bar4.setStack("总量");
        bar4.setBarGap("0");
        Map<String, Object> bar4LabelMap = new HashMap<>();
        Map<String, Object> bar4morMap = new HashMap<>();
        Map<String, Object> bar4TextStyleMap = new HashMap<>();
        bar4TextStyleMap.put("color", "#ff4848");
        bar4TextStyleMap.put("fontSize", "12");
        bar4morMap.put("show", true);
        bar4morMap.put("textStyle", bar4TextStyleMap);
        bar4morMap.put("position", "insideBottom");
        bar4LabelMap.put("normal", bar4morMap);
        bar4.setLabel(bar4LabelMap);
        bar4.setData(datahj.toArray());
        Map<String, Object> bar4ColorMap = new HashMap<>();
        bar4ColorMap.put("color", "rgba(128, 128, 128, 0)");
        bar4.setItemStyle(bar4ColorMap);
        Bar[] chartList = new Bar[]{bar1, bar2, bar3, bar4};
        option.series(chartList);

        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        option.tooltip(tooltip);
        Title title = new Title();
        title.setSubtext("全国月趋势");
        Map<String,Object>textMap=new HashMap<>();
        textMap.put("color","black");
        textMap.put("fontSize","18");
        textMap.put("fontWeight","bold");
        title.setSubtextStyle(textMap);
        option.title(title);
        Grid grid = new Grid();
        grid.setRight(0);
        grid.setTop(90);
        grid.setLeft(10);
        grid.setHeight(200);
        grid.setContainLabel("true");
        option.grid(grid);
        XAxis xAxis = new XAxis();
        xAxis.setType("category");
        xAxis.setData(dataxAxis.toArray());
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("value");
        yAxis.setMax(maxY);
        yAxis.setInterval(intval);
        option.yAxis(yAxis);
        Legend legend = new Legend();
        String[] strArr = new String[]{"直销", "第三方", "CSO"};
        legend.setData(strArr);
        Map<String, Object> textStyleMap = new HashMap<>();
        textStyleMap.put("color", "#3E9EE1");
        legend.setTextStyle(textStyleMap);
        option.legend(legend);
        JSONObject jsonObject1 = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj1 = JSONObject.parseObject(JSON.toJSONString(jsonObject1.get("title")));
        titleObj1.put("x", "left");
        titleObj1.put("y", "5");
        jsonObject1.put("title", titleObj1);
        JSONObject legendObj1 = JSONObject.parseObject(JSON.toJSONString(jsonObject1.get("legend")));
        legendObj1.put("y", "35");
        jsonObject1.put("legend", legendObj1);
        jsonList.add(jsonObject1.toJSONString());
    }

    /**
     * 分产品月趋势
     */
    public void fcpqs_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("zq", "3");
        paramMap.add("method", "getCpqstjSjxxMon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("tj", "mon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("hbfl", "cpqstj");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("cpqstj");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataxAxis = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
            if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                if (!dataxAxis.contains(job.get("rq").toString().substring(5, 10))) {
                    dataxAxis.add(job.get("rq").toString().substring(5, 10));
                }
            } else {
                if (!dataxAxis.contains(job.get("rq").toString())) {
                    dataxAxis.add(job.get("rq").toString());
                }
            }
        }
        List<String> dataAllNames = new ArrayList<>();
        for (String dataxAxi : dataxAxis) {
            for (Object o : jsonArray) {
                JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
                if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                    var date = job.get("rq").toString().substring(5, 10);
                    if (dataxAxi.equals(date)) {
                        if (job.get("jcxmmc") != null) {
                            if (!dataAllNames.isEmpty()) {
                                var flag = true;
                                for (String dataAllName : dataAllNames) {
                                    if (dataAllName.equals(job.get("jcxmmc"))) {
                                        flag = false;
                                        break;
                                    }
                                }
                                if (flag) {
                                    dataAllNames.add(job.get("jcxmmc").toString());
                                }
                            } else {
                                dataAllNames.add(job.get("jcxmmc").toString());
                            }

                        }
                    }
                } else {
                    var date = job.get("rq").toString();
                    if (dataxAxi.equals(date)) {
                        if (job.get("jcxmmc") != null) {
                            if (!dataAllNames.isEmpty()) {
                                var flag = true;
                                for (String dataAllName : dataAllNames) {
                                    if (dataAllName.equals(job.get("jcxmmc"))) {
                                        flag = false;
                                        break;
                                    }
                                }
                                if (flag) {
                                    dataAllNames.add(job.get("jcxmmc").toString());
                                }
                            } else {
                                dataAllNames.add(job.get("jcxmmc").toString());
                            }
                        }
                    }
                }
            }
        }
        int maxY = 0;
        Option option = new Option();
        for (String dataAllName : dataAllNames) {
            List<String> dataSl = new ArrayList<>();
            Line chart1 = new Line();

            for (String dataxAxi : dataxAxis) {
                var flag = false;
                for (Object o : jsonArray) {

                    JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
                    int sl = job.get("sl") == null ? 0 : Integer.parseInt(job.get("sl").toString());
                    if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                        if (dataAllName.equals(job.get("jcxmmc")) && job.get("rq").toString().substring(5, 10).equals(dataxAxi)) {
                            dataSl.add(sl + "");
                            if (sl > maxY) {
                                maxY = sl;
                            }
                            flag = true;
                            break;
                        }
                    } else {
                        if (dataAllName.equals(job.get("jcxmmc")) && job.get("rq").toString().equals(dataxAxi)) {
                            dataSl.add(sl + "");
                            if (sl > maxY) {
                                maxY = sl;
                            }
                            flag = true;
                            break;
                        }
                    }
                }
                if (!flag) {
                    dataSl.add("0");
                }
            }
            chart1.setName(dataAllName);
            chart1.setType("line");
            chart1.data(dataSl.toArray());
            Map<String, Object> itemStyleMap = new HashMap<>();
            Map<String, Object> normal = new HashMap<>();
            Map<String, Object> label = new HashMap<>();
            Map<String, Object> textStyle = new HashMap<>();
            textStyle.put("fontSize", 12);
            label.put("show", true);
            label.put("position", "top");
            label.put("textStyle", textStyle);
            normal.put("label", label);
            itemStyleMap.put("normal", normal);
            chart1.setItemStyle(itemStyleMap);
            chart1.setSmooth(true);
            option.addSeries(chart1);
        }

        maxY = maxY + dataAllNames.size() - (maxY % (dataAllNames.size()));
        int interval = maxY / (dataAllNames.size());
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        option.tooltip(tooltip);
        Title title = new Title();
        Map<String,Object>textMap=new HashMap<>();
        textMap.put("color","black");
        textMap.put("fontSize","18");
        textMap.put("fontWeight","bold");
        title.setSubtextStyle(textMap);
        title.setSubtext("分产品月趋势");
        option.title(title);

        XAxis xAxis = new XAxis();
        xAxis.setType("category");
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "#d14a61");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        xAxis.setData(dataxAxis.toArray()); //["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"]
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("value");
        yAxis.setMax(maxY);
        yAxis.setInterval(interval);
        Map<String, Object> axisLabel = new HashMap<>();
        axisLabel.put("formatter", "{value}");
        yAxis.setAxisLabel(axisLabel);
        option.yAxis(yAxis);
        Legend legend = new Legend();
        legend.setData(dataAllNames.toArray());
        Map<String, Object> textStyleMap = new HashMap<>();
        textStyleMap.put("color", "#3E9EE1");
        legend.setTextStyle(textStyleMap);
        legend.setData(dataAllNames.toArray());
        option.legend(legend);
        Grid grid = new Grid();
        grid.setRight(0);
        grid.setTop(90);
        grid.setLeft(10);
        grid.setHeight(200);
        grid.setContainLabel("true");
        option.grid(grid);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "-13");
        jsonObject.put("title", titleObj);
        JSONObject legendObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("legend")));
        legendObj.put("y", "12");
        jsonObject.put("legend", legendObj);
        jsonList.add(jsonObject.toJSONString());
    }

    /**
     * 分产品周趋势
     */
    public void fcpqs_week() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("zq", "12");
        paramMap.add("method", "getCpqstjSjxxWeek");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("tj", "week");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("hbfl", "cpqstj");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("cpqstj");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataxAxis = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
            if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                if (!dataxAxis.contains(job.get("rq").toString().substring(5, 10))) {
                    dataxAxis.add(job.get("rq").toString().substring(5, 10));
                }
            } else {
                if (!dataxAxis.contains(job.get("rq").toString())) {
                    dataxAxis.add(job.get("rq").toString());
                }
            }
        }
        List<String> dataAllNames = new ArrayList<>();
        for (String dataxAxi : dataxAxis) {
            for (Object o : jsonArray) {
                JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
                if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                    var date = job.get("rq").toString().substring(5, 10);
                    if (dataxAxi.equals(date)) {
                        if (job.get("jcxmmc") != null) {
                            if (!dataAllNames.isEmpty()) {
                                var flag = true;
                                for (String dataAllName : dataAllNames) {
                                    if (dataAllName.equals(job.get("jcxmmc"))) {
                                        flag = false;
                                        break;
                                    }
                                }
                                if (flag) {
                                    dataAllNames.add(job.get("jcxmmc").toString());
                                }
                            } else {
                                dataAllNames.add(job.get("jcxmmc").toString());
                            }

                        }
                    }
                } else {
                    var date = job.get("rq").toString();
                    if (dataxAxi.equals(date)) {
                        if (job.get("jcxmmc") != null) {
                            if (!dataAllNames.isEmpty()) {
                                var flag = true;
                                for (String dataAllName : dataAllNames) {
                                    if (dataAllName.equals(job.get("jcxmmc"))) {
                                        flag = false;
                                        break;
                                    }
                                }
                                if (flag) {
                                    dataAllNames.add(job.get("jcxmmc").toString());
                                }
                            } else {
                                dataAllNames.add(job.get("jcxmmc").toString());
                            }
                        }
                    }
                }
            }
        }
        int maxY = 0;
        Option option = new Option();
        for (String dataAllName : dataAllNames) {
            List<String> dataSl = new ArrayList<>();
            Line chart1 = new Line();

            for (String dataxAxi : dataxAxis) {
                var flag = false;
                for (Object o : jsonArray) {

                    JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
                    int sl = job.get("sl") == null ? 0 : Integer.parseInt(job.get("sl").toString());
                    if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                        if (dataAllName.equals(job.get("jcxmmc")) && job.get("rq").toString().substring(5, 10).equals(dataxAxi)) {
                            dataSl.add(sl + "");
                            if (sl > maxY) {
                                maxY = sl;
                            }
                            flag = true;
                            break;
                        }
                    } else {
                        if (dataAllName.equals(job.get("jcxmmc")) && job.get("rq").toString().equals(dataxAxi)) {
                            dataSl.add(sl + "");
                            if (sl > maxY) {
                                maxY = sl;
                            }
                            flag = true;
                            break;
                        }
                    }
                }
                if (!flag) {
                    dataSl.add("0");
                }
            }
            chart1.setName(dataAllName);
            chart1.setType("line");
            chart1.data(dataSl.toArray());
            Map<String, Object> itemStyleMap = new HashMap<>();
            Map<String, Object> normal = new HashMap<>();
            Map<String, Object> label = new HashMap<>();
            Map<String, Object> textStyle = new HashMap<>();
            textStyle.put("fontSize", 12);
            label.put("show", true);
            label.put("position", "top");
            label.put("textStyle", textStyle);
            normal.put("label", label);
            itemStyleMap.put("normal", normal);
            chart1.setItemStyle(itemStyleMap);
            chart1.setSmooth(true);
            option.addSeries(chart1);
        }

        maxY = maxY + dataAllNames.size() - (maxY % (dataAllNames.size()));
        int interval = maxY / (dataAllNames.size());
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        option.tooltip(tooltip);
        Title title = new Title();
        Map<String,Object>textMap=new HashMap<>();
        textMap.put("color","black");
        textMap.put("fontSize","18");
        textMap.put("fontWeight","bold");
        title.setSubtextStyle(textMap);
        title.setSubtext("分产品周趋势");
        option.title(title);
        Grid grid = new Grid();
        grid.setTop(90);
        grid.setRight(50);
        option.grid(grid);
        XAxis xAxis = new XAxis();
        xAxis.setType("category");
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "#d14a61");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        xAxis.setData(dataxAxis.toArray()); //["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"]
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("value");
        yAxis.setMax(maxY);
        yAxis.setInterval(interval);
        Map<String, Object> axisLabel = new HashMap<>();
        axisLabel.put("formatter", "{value}");
        yAxis.setAxisLabel(axisLabel);
        option.yAxis(yAxis);
        Legend legend = new Legend();
        legend.setData(dataAllNames.toArray());
        Map<String, Object> textStyleMap = new HashMap<>();
        textStyleMap.put("color", "#3E9EE1");
        legend.setTextStyle(textStyleMap);
        legend.setData(dataAllNames.toArray());
        option.legend(legend);

        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "-13");
        jsonObject.put("title", titleObj);
        JSONObject legendObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("legend")));
        legendObj.put("y", "12");
        jsonObject.put("legend", legendObj);
        jsonList.add(jsonObject.toJSONString());
    }

    /**
     * 平台月趋势
     */
    public void ptqs_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("zq", "3");
        paramMap.add("method", "getPtqstjSjxxMon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("tj", "mon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("hbfl", "cpqstj");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object ptqstj = map.get("ptqstj");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(ptqstj));
        List<String> dataxAxis = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
            if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                if (!dataxAxis.contains(job.get("rq").toString().substring(5, 10))) {
                    dataxAxis.add(job.get("rq").toString().substring(5, 10));
                }
            } else {
                if (!dataxAxis.contains(job.get("rq").toString())) {
                    dataxAxis.add(job.get("rq").toString());
                }
            }
        }
        List<String> dataAllNames = new ArrayList<>();
        for (String dataxAxi : dataxAxis) {
            for (Object o : jsonArray) {
                JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
                if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                    var date = job.get("rq").toString().substring(5, 10);
                    if (dataxAxi.equals(date)) {
                        if (job.get("ptmc") != null) {
                            if (!dataAllNames.isEmpty()) {
                                var flag = true;
                                for (String dataAllName : dataAllNames) {
                                    if (dataAllName.equals(job.get("ptmc"))) {
                                        flag = false;
                                        break;
                                    }
                                }
                                if (flag) {
                                    dataAllNames.add(job.get("ptmc").toString());
                                }
                            } else {
                                dataAllNames.add(job.get("ptmc").toString());
                            }

                        }
                    }
                } else {
                    var date = job.get("rq").toString();
                    if (dataxAxi.equals(date)) {
                        if (job.get("ptmc") != null) {
                            if (!dataAllNames.isEmpty()) {
                                var flag = true;
                                for (String dataAllName : dataAllNames) {
                                    if (dataAllName.equals(job.get("ptmc"))) {
                                        flag = false;
                                        break;
                                    }
                                }
                                if (flag) {
                                    dataAllNames.add(job.get("ptmc").toString());
                                }
                            } else {
                                dataAllNames.add(job.get("ptmc").toString());
                            }
                        }
                    }
                }
            }
        }
        int maxY = 0;
        Option option = new Option();
        for (String dataAllName : dataAllNames) {
            List<String> dataSl = new ArrayList<>();
            Line chart1 = new Line();

            for (String dataxAxi : dataxAxis) {
                var flag = false;
                for (Object o : jsonArray) {

                    JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
                    int sl = job.get("sl") == null ? 0 : Integer.parseInt(job.get("sl").toString());
                    if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                        if (dataAllName.equals(job.get("ptmc")) && job.get("rq").toString().substring(5, 10).equals(dataxAxi)) {
                            dataSl.add(sl + "");
                            if (sl > maxY) {
                                maxY = sl;
                            }
                            flag = true;
                            break;
                        }
                    } else {
                        if (dataAllName.equals(job.get("ptmc")) && job.get("rq").toString().equals(dataxAxi)) {
                            dataSl.add(sl + "");
                            if (sl > maxY) {
                                maxY = sl;
                            }
                            flag = true;
                            break;
                        }
                    }
                }
                if (!flag) {
                    dataSl.add("0");
                }
            }
            chart1.setName(dataAllName);
            chart1.setType("line");
            chart1.data(dataSl.toArray());
            Map<String, Object> itemStyleMap = new HashMap<>();
            Map<String, Object> normal = new HashMap<>();
            Map<String, Object> label = new HashMap<>();
            Map<String, Object> textStyle = new HashMap<>();
            textStyle.put("fontSize", 12);
            label.put("show", true);
            label.put("position", "top");
            label.put("textStyle", textStyle);
            normal.put("label", label);
            itemStyleMap.put("normal", normal);
            chart1.setItemStyle(itemStyleMap);
            chart1.setSmooth(true);
            option.addSeries(chart1);
        }

        maxY = maxY + dataAllNames.size() - (maxY % (dataAllNames.size()));
        int interval = maxY / (dataAllNames.size());
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        option.tooltip(tooltip);
        Title title = new Title();
        title.setSubtext("平台月趋势");
        Map<String,Object>textMap=new HashMap<>();
        textMap.put("color","black");
        textMap.put("fontSize","18");
        textMap.put("fontWeight","bold");
        title.setSubtextStyle(textMap);
        option.title(title);
        Grid grid = new Grid();
        grid.setTop(90);
        grid.setRight(50);
        option.grid(grid);
        XAxis xAxis = new XAxis();
        xAxis.setType("category");
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "#d14a61");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        xAxis.setData(dataxAxis.toArray()); //["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"]
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("value");
        yAxis.setMax(maxY);
        yAxis.setInterval(interval);
        Map<String, Object> axisLabel = new HashMap<>();
        axisLabel.put("formatter", "{value}");
        yAxis.setAxisLabel(axisLabel);
        option.yAxis(yAxis);
        Legend legend = new Legend();
        legend.setData(dataAllNames.toArray());
        Map<String, Object> textStyleMap = new HashMap<>();
        textStyleMap.put("color", "#3E9EE1");
        legend.setTextStyle(textStyleMap);
        legend.setData(dataAllNames.toArray());
        legend.setBottom("0");
        option.legend(legend);

        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "10");
        jsonObject.put("title", titleObj);
        JSONObject legendObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("legend")));
        jsonObject.put("legend", legendObj);
        jsonList.add(jsonObject.toJSONString());
    }

    /**
     * 平台周趋势
     */
    public void ptqs_week() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("zq", "12");
        paramMap.add("method", "getPtqstjSjxxWeek");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("tj", "week");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("ptqstj");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataxAxis = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
            if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                if (!dataxAxis.contains(job.get("rq").toString().substring(5, 10))) {
                    dataxAxis.add(job.get("rq").toString().substring(5, 10));
                }
            } else {
                if (!dataxAxis.contains(job.get("rq").toString())) {
                    dataxAxis.add(job.get("rq").toString());
                }
            }
        }
        List<String> dataAllNames = new ArrayList<>();
        for (String dataxAxi : dataxAxis) {
            for (Object o : jsonArray) {
                JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
                if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                    var date = job.get("rq").toString().substring(5, 10);
                    if (dataxAxi.equals(date)) {
                        if (job.get("ptmc") != null) {
                            if (!dataAllNames.isEmpty()) {
                                var flag = true;
                                for (String dataAllName : dataAllNames) {
                                    if (dataAllName.equals(job.get("ptmc"))) {
                                        flag = false;
                                        break;
                                    }
                                }
                                if (flag) {
                                    dataAllNames.add(job.get("ptmc").toString());
                                }
                            } else {
                                dataAllNames.add(job.get("ptmc").toString());
                            }

                        }
                    }
                } else {
                    var date = job.get("rq").toString();
                    if (dataxAxi.equals(date)) {
                        if (job.get("ptmc") != null) {
                            if (!dataAllNames.isEmpty()) {
                                var flag = true;
                                for (String dataAllName : dataAllNames) {
                                    if (dataAllName.equals(job.get("ptmc"))) {
                                        flag = false;
                                        break;
                                    }
                                }
                                if (flag) {
                                    dataAllNames.add(job.get("ptmc").toString());
                                }
                            } else {
                                dataAllNames.add(job.get("ptmc").toString());
                            }
                        }
                    }
                }
            }
        }
        int maxY = 0;
        Option option = new Option();
        for (String dataAllName : dataAllNames) {
            List<String> dataSl = new ArrayList<>();
            Line chart1 = new Line();

            for (String dataxAxi : dataxAxis) {
                var flag = false;
                for (Object o : jsonArray) {

                    JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
                    int sl = job.get("sl") == null ? 0 : Integer.parseInt(job.get("sl").toString());
                    if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                        if (dataAllName.equals(job.get("ptmc")) && job.get("rq").toString().substring(5, 10).equals(dataxAxi)) {
                            dataSl.add(sl + "");
                            if (sl > maxY) {
                                maxY = sl;
                            }
                            flag = true;
                            break;
                        }
                    } else {
                        if (dataAllName.equals(job.get("ptmc")) && job.get("rq").toString().equals(dataxAxi)) {
                            dataSl.add(sl + "");
                            if (sl > maxY) {
                                maxY = sl;
                            }
                            flag = true;
                            break;
                        }
                    }
                }
                if (!flag) {
                    dataSl.add("0");
                }
            }
            chart1.setName(dataAllName);
            chart1.setType("line");
            chart1.data(dataSl.toArray());
            Map<String, Object> itemStyleMap = new HashMap<>();
            Map<String, Object> normal = new HashMap<>();
            Map<String, Object> label = new HashMap<>();
            Map<String, Object> textStyle = new HashMap<>();
            textStyle.put("fontSize", 12);
            label.put("show", true);
            label.put("position", "top");
            label.put("textStyle", textStyle);
            normal.put("label", label);
            itemStyleMap.put("normal", normal);
            chart1.setItemStyle(itemStyleMap);
            chart1.setSmooth(true);
            option.addSeries(chart1);
        }

        maxY = maxY + dataAllNames.size() - (maxY % (dataAllNames.size()));
        int interval = maxY / (dataAllNames.size());
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        option.tooltip(tooltip);
        Title title = new Title();
        Map<String,Object>textMap=new HashMap<>();
        textMap.put("color","black");
        textMap.put("fontSize","18");
        textMap.put("fontWeight","bold");
        title.setSubtextStyle(textMap);
        title.setSubtext("平台周趋势");
        option.title(title);
        Grid grid = new Grid();
        grid.setTop(90);
        grid.setRight(50);
        option.grid(grid);
        XAxis xAxis = new XAxis();
        xAxis.setType("category");
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "#d14a61");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        xAxis.setData(dataxAxis.toArray()); //["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"]
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("value");
        yAxis.setMax(maxY);
        yAxis.setInterval(interval);
        Map<String, Object> axisLabel = new HashMap<>();
        axisLabel.put("formatter", "{value}");
        yAxis.setAxisLabel(axisLabel);
        option.yAxis(yAxis);
        Legend legend = new Legend();
        legend.setData(dataAllNames.toArray());
        Map<String, Object> textStyleMap = new HashMap<>();
        textStyleMap.put("color", "#3E9EE1");
        legend.setTextStyle(textStyleMap);
        legend.setData(dataAllNames.toArray());
        legend.setBottom("0");
        option.legend(legend);

        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "10");
        jsonObject.put("title", titleObj);
        JSONObject legendObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("legend")));
        jsonObject.put("legend", legendObj);
        jsonList.add(jsonObject.toJSONString());
    }

    /**
     * 第三方top15月度
     */
    public void topdsf15_mon() {

        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getTopDsf20ByMon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("yxxs", "mNGS项目,tNGS");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("topDsf20");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataAxis = new ArrayList<>();
        List<String> dataseries = new ArrayList<>();
        int len = 14;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        title.setSubtext("第三方实验室top15");
        Map<String,Object>textMap=new HashMap<>();
        textMap.put("color","black");
        textMap.put("fontSize","18");
        textMap.put("fontWeight","bold");
        title.setSubtextStyle(textMap);
        option.title(title);
        Grid grid = new Grid();
        grid.setTop(90);
        grid.setRight("4%");
        grid.setLeft("3%");
        grid.setBottom("3%");
        grid.setContainLabel("true");
        option.grid(grid);
        XAxis xAxis = new XAxis();
        xAxis.setType("value");
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "#00b2ee");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("15");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "#00b2ee");
        axisLine_y.put("lineStyle", colorMap_y);
        yAxis.setAxisLine(axisLine_y);
        Map<String, Object> axisTick = new HashMap<>();
        axisTick.put("alignWithLabel", true);
        yAxis.setAxisTick(axisTick);
        Map<String, Object> nameTextStyle = new HashMap<>();
        nameTextStyle.put("fontSize", "10");
        nameTextStyle.put("fontcolor", "#00b2ee");
        yAxis.setNameTextStyle(nameTextStyle);
        option.yAxis(yAxis);
        Bar bar = new Bar();
        bar.setData(dataseries.toArray());
        bar.setName("数量");
        bar.setBarWidth("70%");
        Map<String, Object> itemStyle = new HashMap<>();
        itemStyle.put("color", "#00b2ee");
        bar.setItemStyle(itemStyle);
        Map<String, Object> label = new HashMap<>();
        Map<String, Object> textStyle = new HashMap<>();
        textStyle.put("fontSize", "12");
        label.put("textStyle", textStyle);
        label.put("show", "true");
        label.put("position", "right");
        bar.setLabel(label);
        option.addSeries(bar);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "20");
        jsonObject.put("title", titleObj);
        jsonList.add(jsonObject.toJSONString());
    }

    /**
     * 直销top15月度
     */
    public void topzx15_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getTopZx20ByMon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("yxxs", "mNGS项目,tNGS");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("topZx20");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataAxis = new ArrayList<>();
        List<String> dataseries = new ArrayList<>();
        int len = 14;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Map<String,Object>textMap=new HashMap<>();
        textMap.put("color","black");
        textMap.put("fontSize","18");
        textMap.put("fontWeight","bold");
        title.setSubtextStyle(textMap);
        title.setSubtext("直销top15");
        option.title(title);
        Grid grid = new Grid();
        grid.setTop(90);
        grid.setRight("4%");
        grid.setLeft("3%");
        grid.setBottom("3%");
        grid.setContainLabel("true");
        option.grid(grid);
        XAxis xAxis = new XAxis();
        xAxis.setType("value");
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "rgb(249,174,0)");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("15");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(249,174,0)");
        axisLine_y.put("lineStyle", colorMap_y);
        yAxis.setAxisLine(axisLine_y);
        Map<String, Object> axisTick = new HashMap<>();
        axisTick.put("alignWithLabel", true);
        yAxis.setAxisTick(axisTick);
        Map<String, Object> nameTextStyle = new HashMap<>();
        nameTextStyle.put("fontSize", "10");
        nameTextStyle.put("fontcolor", "rgb(249,174,0)");
        yAxis.setNameTextStyle(nameTextStyle);
        option.yAxis(yAxis);
        Bar bar = new Bar();
        bar.setData(dataseries.toArray());
        bar.setName("数量");
        bar.setBarWidth("70%");
        Map<String, Object> itemStyle = new HashMap<>();
        itemStyle.put("color", "rgb(249,174,0)");
        bar.setItemStyle(itemStyle);
        Map<String, Object> label = new HashMap<>();
        Map<String, Object> textStyle = new HashMap<>();
        textStyle.put("fontSize", "12");
        label.put("textStyle", textStyle);
        label.put("show", "true");
        label.put("position", "right");
        bar.setLabel(label);
        option.addSeries(bar);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "20");
        jsonObject.put("title", titleObj);
        jsonList.add(jsonObject.toJSONString());
    }

    /**
     * COStop15月度
     */
    public void topcos15_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getTopCSO20ByMon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("topCSO20");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataAxis = new ArrayList<>();
        List<String> dataseries = new ArrayList<>();
        int len = 14;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        title.setSubtext("COStop15");
        Map<String,Object>textMap=new HashMap<>();
        textMap.put("color","black");
        textMap.put("fontSize","18");
        textMap.put("fontWeight","bold");
        title.setSubtextStyle(textMap);
        option.title(title);
        Grid grid = new Grid();
        grid.setTop(90);
        grid.setRight("4%");
        grid.setLeft("3%");
        grid.setBottom("3%");
        grid.setContainLabel("true");
        option.grid(grid);
        XAxis xAxis = new XAxis();
        xAxis.setType("value");
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "rgb(209,95,126)");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("15");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(209,95,126)");
        axisLine_y.put("lineStyle", colorMap_y);
        yAxis.setAxisLine(axisLine_y);
        Map<String, Object> axisTick = new HashMap<>();
        axisTick.put("alignWithLabel", true);
        yAxis.setAxisTick(axisTick);
        Map<String, Object> nameTextStyle = new HashMap<>();
        nameTextStyle.put("fontSize", "10");
        nameTextStyle.put("fontcolor", "rgb(209,95,126)");
        yAxis.setNameTextStyle(nameTextStyle);
        option.yAxis(yAxis);
        Bar bar = new Bar();
        bar.setData(dataseries.toArray());
        bar.setName("数量");
        bar.setBarWidth("70%");
        Map<String, Object> itemStyle = new HashMap<>();
        itemStyle.put("color", "rgb(209,95,126)");
        bar.setItemStyle(itemStyle);
        Map<String, Object> label = new HashMap<>();
        Map<String, Object> textStyle = new HashMap<>();
        textStyle.put("fontSize", "12");
        label.put("textStyle", textStyle);
        label.put("show", "true");
        label.put("position", "right");
        bar.setLabel(label);
        option.addSeries(bar);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "20");
        jsonObject.put("title", titleObj);
        jsonList.add(jsonObject.toJSONString());
    }
    /**
     * COStop15月度
     */
    public void tophx15_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getTopHxyxListByMon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("topHxyxList");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataAxis = new ArrayList<>();
        List<String> dataseries = new ArrayList<>();
        int len = 14;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        title.setSubtext("COStop15");
        Map<String,Object>textMap=new HashMap<>();
        textMap.put("color","black");
        textMap.put("fontSize","18");
        textMap.put("fontWeight","bold");
        title.setSubtextStyle(textMap);
        option.title(title);
        Grid grid = new Grid();
        grid.setTop(90);
        grid.setRight("4%");
        grid.setLeft("3%");
        grid.setBottom("3%");
        grid.setContainLabel("true");
        option.grid(grid);
        XAxis xAxis = new XAxis();
        xAxis.setType("value");
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "rgb(209,95,126)");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("15");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(209,95,126)");
        axisLine_y.put("lineStyle", colorMap_y);
        yAxis.setAxisLine(axisLine_y);
        Map<String, Object> axisTick = new HashMap<>();
        axisTick.put("alignWithLabel", true);
        yAxis.setAxisTick(axisTick);
        Map<String, Object> nameTextStyle = new HashMap<>();
        nameTextStyle.put("fontSize", "10");
        nameTextStyle.put("fontcolor", "rgb(209,95,126)");
        yAxis.setNameTextStyle(nameTextStyle);
        option.yAxis(yAxis);
        Bar bar = new Bar();
        bar.setData(dataseries.toArray());
        bar.setName("数量");
        bar.setBarWidth("70%");
        Map<String, Object> itemStyle = new HashMap<>();
        itemStyle.put("color", "rgb(209,95,126)");
        bar.setItemStyle(itemStyle);
        Map<String, Object> label = new HashMap<>();
        Map<String, Object> textStyle = new HashMap<>();
        textStyle.put("fontSize", "12");
        label.put("textStyle", textStyle);
        label.put("show", "true");
        label.put("position", "right");
        bar.setLabel(label);
        option.addSeries(bar);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "20");
        jsonObject.put("title", titleObj);
        jsonList.add(jsonObject.toJSONString());
    }
    /**
     * 入院top15月度
     */
    public void topry15_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getTopRY20ByMon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("topRY20");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataAxis = new ArrayList<>();
        List<String> dataseries = new ArrayList<>();
        int len = 14;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("dwmc") == null ? "" : job.get("dwmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Map<String,Object>textMap=new HashMap<>();
        textMap.put("color","black");
        textMap.put("fontSize","18");
        textMap.put("fontWeight","bold");
        title.setSubtextStyle(textMap);
        title.setSubtext("入院top15");
        option.title(title);
        Grid grid = new Grid();
        grid.setTop(90);
        grid.setRight("4%");
        grid.setLeft("3%");
        grid.setBottom("3%");
        grid.setContainLabel("true");
        option.grid(grid);
        XAxis xAxis = new XAxis();
        xAxis.setType("value");
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "rgb(209,95,126)");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("15");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(209,95,126)");
        axisLine_y.put("lineStyle", colorMap_y);
        yAxis.setAxisLine(axisLine_y);
        Map<String, Object> axisTick = new HashMap<>();
        axisTick.put("alignWithLabel", true);
        yAxis.setAxisTick(axisTick);
        Map<String, Object> nameTextStyle = new HashMap<>();
        nameTextStyle.put("fontSize", "10");
        nameTextStyle.put("fontcolor", "rgb(209,95,126)");
        yAxis.setNameTextStyle(nameTextStyle);
        option.yAxis(yAxis);
        Bar bar = new Bar();
        bar.setData(dataseries.toArray());
        bar.setName("数量");
        bar.setBarWidth("70%");
        Map<String, Object> itemStyle = new HashMap<>();
        itemStyle.put("color", "rgb(209,95,126)");
        bar.setItemStyle(itemStyle);
        Map<String, Object> label = new HashMap<>();
        Map<String, Object> textStyle = new HashMap<>();
        textStyle.put("fontSize", "12");
        label.put("textStyle", textStyle);
        label.put("show", "true");
        label.put("position", "right");
        bar.setLabel(label);
        option.addSeries(bar);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "20");
        jsonObject.put("title", titleObj);
        jsonList.add(jsonObject.toJSONString());
    }

    /***
     * 年度平台指标达成率
     */
    public void ptzbdcl_nd(XWPFDocument document) {

        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getPtzbdclByYear");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("tj", "year");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("ptzbdcl");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        // create table
        XWPFTable table = document.createTable();
        // create first row
        XWPFTableRow tableRowOne = table.getRow(0);
        table.setWidth("100%");
        tableRowOne.getCell(0).setText("平台");
        tableRowOne.addNewTableCell().setText("指标");
        tableRowOne.addNewTableCell().setText("达成率");
        tableRowOne.addNewTableCell().setText("gap");
        tableRowOne.getCell(0).setColor("409EFF");
        tableRowOne.getCell(1).setColor("409EFF");
        tableRowOne.getCell(2).setColor("409EFF");
        tableRowOne.getCell(3).setColor("409EFF");
        // create second row

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            XWPFTableRow tableRow = table.createRow();
            tableRow.getCell(0).setText(job.get("ptmc").toString());
            tableRow.getCell(1).setText(job.get("sz").toString());
            tableRow.getCell(2).setText(job.get("dcl").toString());
            tableRow.getCell(3).setText(job.get("gap").toString());
            if ("全国".equals(job.get("ptmc"))) {
                tableRow.getCell(0).setColor("9BC2E6");
                tableRow.getCell(1).setColor("9BC2E6");
                tableRow.getCell(2).setColor("9BC2E6");
                tableRow.getCell(3).setColor("9BC2E6");
            } else {
                if (i % 2 == 0) {
                    tableRow.getCell(0).setColor("DDEBF7");
                    tableRow.getCell(1).setColor("DDEBF7");
                    tableRow.getCell(2).setColor("DDEBF7");
                    tableRow.getCell(3).setColor("DDEBF7");
                }
            }
        }


    }

    /***
     * 季度平台指标达成率
     */
    public void ptzbdcl_jd(XWPFDocument document) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getPtzbdclByQuarter");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("tj", "mon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("ptzbdcl");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        // create table
        XWPFTable table = document.createTable();
        table.setWidth("100%");
        // create first row
        XWPFTableRow tableRowOne = table.getRow(0);
        tableRowOne.getCell(0).setText("平台");
        tableRowOne.addNewTableCell().setText("指标");
        tableRowOne.addNewTableCell().setText("达成率");
        tableRowOne.addNewTableCell().setText("gap");
        tableRowOne.getCell(0).setColor("409EFF");
        tableRowOne.getCell(1).setColor("409EFF");
        tableRowOne.getCell(2).setColor("409EFF");
        tableRowOne.getCell(3).setColor("409EFF");
        // create second row

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            XWPFTableRow tableRow = table.createRow();
            tableRow.getCell(0).setText(job.get("ptmc").toString());
            tableRow.getCell(1).setText(job.get("sz").toString());
            tableRow.getCell(2).setText(job.get("dcl").toString());
            tableRow.getCell(3).setText(job.get("gap").toString());
            if ("全国".equals(job.get("ptmc"))) {
                tableRow.getCell(0).setColor("9BC2E6");
                tableRow.getCell(1).setColor("9BC2E6");
                tableRow.getCell(2).setColor("9BC2E6");
                tableRow.getCell(3).setColor("9BC2E6");
            } else {
                if (i % 2 == 0) {
                    tableRow.getCell(0).setColor("DDEBF7");
                    tableRow.getCell(1).setColor("DDEBF7");
                    tableRow.getCell(2).setColor("DDEBF7");
                    tableRow.getCell(3).setColor("DDEBF7");
                }
            }
        }

    }

    /***
     * 月度平台指标达成率
     */
    public void ptzbdcl_yd(XWPFDocument document) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getPtzbdclByMon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("tj", "mon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("ptzbdcl");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        // create table
        XWPFTable table = document.createTable();
        table.setWidth("100%");
        // create first row
        XWPFTableRow tableRowOne = table.getRow(0);
        tableRowOne.getCell(0).setText("平台");
        tableRowOne.addNewTableCell().setText("指标");
        tableRowOne.addNewTableCell().setText("达成率");
        tableRowOne.addNewTableCell().setText("gap");
        tableRowOne.getCell(0).setColor("409EFF");
        tableRowOne.getCell(1).setColor("409EFF");
        tableRowOne.getCell(2).setColor("409EFF");
        tableRowOne.getCell(3).setColor("409EFF");
        // create second row

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            XWPFTableRow tableRow = table.createRow();
            tableRow.getCell(0).setText(job.get("ptmc").toString());
            tableRow.getCell(1).setText(job.get("sz").toString());
            tableRow.getCell(2).setText(job.get("dcl").toString());
            tableRow.getCell(3).setText(job.get("gap").toString());
            if ("全国".equals(job.get("ptmc"))) {
                tableRow.getCell(0).setColor("9BC2E6");
                tableRow.getCell(1).setColor("9BC2E6");
                tableRow.getCell(2).setColor("9BC2E6");
                tableRow.getCell(3).setColor("9BC2E6");
            } else {
                if (i % 2 == 0) {
                    tableRow.getCell(0).setColor("DDEBF7");
                    tableRow.getCell(1).setColor("DDEBF7");
                    tableRow.getCell(2).setColor("DDEBF7");
                    tableRow.getCell(3).setColor("DDEBF7");
                }
            }
        }
    }

    /**
     * 销售性质占比
     */
    public void xsxzzb_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getSjqfcsszb");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("tj", "mon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("lrsjStart", jsrqstart.substring(0, 7));
        paramMap.add("lrsjEnd", jsrqend.substring(0, 7));
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("sjqfcsszb");
        List<Map<String, Object>> seriesData = new ArrayList<>();
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        for (Object o : jsonArray) {
            Map<String, Object> dataMap = new HashMap<>();
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
            dataMap.put("value", job.get("num"));
            dataMap.put("name", job.get("sjqfmc"));
            seriesData.add(dataMap);
        }
        Option option = new Option();
        Title title = new Title();
        title.setSubtext("当月平台业务占比");
        Map<String,Object>textMap=new HashMap<>();
        textMap.put("color","black");
        textMap.put("fontSize","18");
        textMap.put("fontWeight","bold");
        title.setSubtextStyle(textMap);
        option.title(title);

        Pie pie = new Pie();
        pie.setData(seriesData.toArray());
        String[] radiusArr = new String[]{"30%", "50%"};
        pie.setRadius(radiusArr);
        String[] centerArr = new String[]{"50%", "55%"};
        pie.setCenter(centerArr);
        pie.setStartAngle("45");
        Map<String, Object> itemMap = new HashMap<>();
        Map<String, Object> normalMap = new HashMap<>();
        Map<String, Object> labelMap = new HashMap<>();
        Map<String, Object> labelLineMap = new HashMap<>();
        labelMap.put("formatter", "{b}：{c}");
        labelLineMap.put("show", "true");
        normalMap.put("labelLine", labelLineMap);
        normalMap.put("label", labelMap);
        itemMap.put("normal", normalMap);
        pie.setItemStyle(itemMap);
        option.addSeries(pie);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "20");
        jsonObject.put("title", titleObj);
        jsonList.add(jsonObject.toJSONString());
    }

    /**
     * 销售渠道类型占比
     */
    public void xsqdlxzb_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getHbflcsszbMon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("tj", "mon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("lrsjStart", jsrqstart.substring(0, 7));
        paramMap.add("lrsjEnd", jsrqend.substring(0, 7));
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("hbflcsszb");
        List<Map<String, Object>> seriesData = new ArrayList<>();
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        for (Object o : jsonArray) {
            Map<String, Object> dataMap = new HashMap<>();
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
            dataMap.put("value", job.get("num"));
            dataMap.put("name", job.get("yxx"));
            seriesData.add(dataMap);
        }
        Option option = new Option();
        Title title = new Title();
        title.setSubtext("当月销售类型占比");
        Map<String,Object>textMap=new HashMap<>();
        textMap.put("color","black");
        textMap.put("fontSize","18");
        textMap.put("fontWeight","bold");
        title.setSubtextStyle(textMap);
        option.title(title);

        Pie pie = new Pie();
        pie.data(seriesData.toArray());
        String[] radiusArr = new String[]{"30%", "50%"};
        pie.setRadius(radiusArr);
        String[] centerArr = new String[]{"50%", "55%"};
        pie.setCenter(centerArr);
        pie.setStartAngle("45");
        pie.setName("个数");
        Map<String, Object> itemMap = new HashMap<>();
        Map<String, Object> normalMap = new HashMap<>();
        Map<String, Object> labelMap = new HashMap<>();
        Map<String, Object> labelLineMap = new HashMap<>();
        labelMap.put("formatter", "{b}：{c}");
        labelLineMap.put("show", "true");
        normalMap.put("labelLine", labelLineMap);
        normalMap.put("label", labelMap);
        itemMap.put("normal", normalMap);
        pie.setItemStyle(itemMap);
        option.addSeries(pie);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "20");
        jsonList.add(jsonObject.toJSONString());
    }

    /**
     * 平台业务占比
     */
    public void ptywzb_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getPtywzbByMon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("tj", "mon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("lrsjStart", jsrqstart.substring(0, 7));
        paramMap.add("lrsjEnd", jsrqend.substring(0, 7));
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("ptywzbtj");
        List<Map<String, Object>> seriesData = new ArrayList<>();
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        for (Object o : jsonArray) {
            Map<String, Object> dataMap = new HashMap<>();
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
            dataMap.put("value", job.get("sl"));
            dataMap.put("name", job.get("ptmc"));
            seriesData.add(dataMap);
        }
        Option option = new Option();
        Title title = new Title();
        title.setSubtext("当月销售性质占比");
        Map<String,Object>textMap=new HashMap<>();
        textMap.put("color","black");
        textMap.put("fontSize","18");
        textMap.put("fontWeight","bold");
        title.setSubtextStyle(textMap);
        option.title(title);

        Pie pie = new Pie();
        pie.setData(seriesData.toArray());
        String[] radiusArr = new String[]{"30%", "50%"};
        pie.setRadius(radiusArr);
        String[] centerArr = new String[]{"50%", "55%"};
        pie.setCenter(centerArr);
        pie.setStartAngle("45");
        pie.setName("个数");
        Map<String, Object> itemMap = new HashMap<>();
        Map<String, Object> normalMap = new HashMap<>();
        Map<String, Object> labelMap = new HashMap<>();
        Map<String, Object> labelLineMap = new HashMap<>();
        labelMap.put("formatter", "{b}：{c}");
        labelLineMap.put("show", "true");
        normalMap.put("labelLine", labelLineMap);
        normalMap.put("label", labelMap);
        itemMap.put("normal", normalMap);
        pie.setItemStyle(itemMap);
        option.addSeries(pie);
        Map<String, Object> itemMap1 = new HashMap<>();
        Map<String, Object> textStyleMap = new HashMap<>();
        textStyleMap.put("fontSize","16");
        itemMap1.put("textStyle",textStyleMap);
        pie.setLabel(itemMap1);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "20");
        jsonList.add(jsonObject.toJSONString());
    }

    public static String writeFile(String options, String tmpPath) {
        String dataPath = tmpPath
                + UUID.randomUUID().toString().substring(0, 8) + ".json";
        try {
            /* 写入Txt文件 */
            File writename = new File(dataPath); // 相对路径，如果没有则要建立一个新的output.txt文件
            if (!writename.exists()) { // 文件不存在则创建文件，先创建目录
                File dir = new File(writename.getParent());
                dir.mkdirs();
                writename.createNewFile(); // 创建新文件
            }
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(options); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataPath;
    }
}

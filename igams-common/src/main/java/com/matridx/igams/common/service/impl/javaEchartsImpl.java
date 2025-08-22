package com.matridx.igams.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beyongx.echarts.Chart;
import com.beyongx.echarts.Option;
import com.beyongx.echarts.charts.Bar;
import com.beyongx.echarts.charts.Line;
import com.beyongx.echarts.charts.Pie;
import com.beyongx.echarts.options.*;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum_pub;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;


import org.apache.poi.xwpf.usermodel.*;
import org.docx4j.Docx4J;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class javaEchartsImpl {
    private static final Logger logger = LoggerFactory.getLogger(javaEchartsImpl.class);


//    private static String PHANTOMJS_PATH = "D:\\java\\pjs\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe";
//    /**
//     * echartsJs 路径
//     */
//    private static String ECHARTS_PATH = "E:\\kfhj\\igams-style\\src\\main\\resources\\static\\js\\plugins\\echarts\\echarts-convert.js";
    /**
     * echarts获取图片base64编码URL头
     */
    private static final String BASE64FORMAT = "data:image/png;base64,";

    @Value("${matridx.ftp.url:}")
    private final String FTP_URL = null;
    @Autowired(required = false)
    private AmqpTemplate amqpTempl;
    /**
     * 文档转换完成OK
     */
    @Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
    private final String DOC_OK = null;

    private List<String> jsonList = new ArrayList<>();
    private String jsrqstart = "";
    private String jsrqend = "";
    private String rq1="";
    private String rq2="";
    private String rq3="";
    @Value("${matridx.fileupload.releasePath:}")
    private String releasePath;

    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;

    @Value("${matridx.fileupload.tempPath}")
    private String tempFilePath;
    @Value("${matridx.fileupload.prefix}")
    private String prefix = "";
    @Value("${matridx.wechat.menuurl:}")
    private String menuurl;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ICommonService commonService;
    @Autowired
    IDdxxglService ddxxglService;
    //@Autowired
    //private DingTalkUtil talkUtil;
    @Autowired
    private IFjcfbService fjcfbService;

    public void creatJavaEchart() {

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
                String url = writeFile(_url, tempFilePath + "/javaEchart/");
                urls.add(url);
            }

            List<String> base64s = new ArrayList<>();
            for (String _url : urls) {
                String base64;
                /**
                 * 命令格式：
                 * phantomjs echarts-convert.js -infile optionURl -width width -height height
                 * 可选参数：-width width -height height
                 * 备注：
                 * phantomjs添加到环境变量中后可以直接使用，这里防止环境变量配置问题所以直接使用绝对路径
                 */
                logger.error("生成得url" + _url);
                /**
                 * phantomjs 路径
                 */
                String PHANTOMJS_PATH = "/home/centos/phantomjs/phantomjs-2.1.1-linux-x86_64/bin/phantomjs";
                /**
                 * echartsJs 路径
                 */
                String ECHARTS_PATH = "/home/centos/phantomjs/echarts/echarts-convert.js";
                String cmd = PHANTOMJS_PATH + " " + ECHARTS_PATH + " -infile " + _url + " -width " + 536 + " -height " + 540;
                logger.error("命令" + cmd);
                Process process = Runtime.getRuntime().exec(cmd);
                /**
                 * 获取控制台输出信息
                 * 通过JS中使用console.log()打印输出base64编码
                 * 获取进程输入流，进行base64编码获取
                 */
                input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                while ((line = input.readLine()) != null) {
                   // logger.error("不是base64" + line);
                    if (line.startsWith(BASE64FORMAT)) {
                        base64 = line.replace(BASE64FORMAT, "");
                        base64s.add(base64);
                       // logger.error("js返回数据" + base64);
                        break;
                    }
                }
                //logger.error("分割线--------------------------------" + line);
                input.close();
                process.waitFor();
            }

            // 重新生成段落进行图片的绘制
            String storePath = releasePath + BusTypeEnum.IMP_MARKETING_SIN.getCode() + "/" + "UP" +
                    DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" +
                    DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/" + "UP" +
                    DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
            String wordName = StringUtil.generateUUID()+".docx";
            File file = new File(storePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file1 = new File(storePath.replace(releasePath,tempFilePath));
            if (!file1.exists()) {
                file1.mkdirs();
            }
            String s = Files.readString(Paths.get("/home/centos/phantomjs/yxkb.xml"));
            //String s = Files.readString(Paths.get("D://yxkb.xml"));
            FileWriter fileWriter;
            logger.error("开始编辑word--------------------------------写入echarts图片");
            for(int i=0;i<base64s.size();i++){
                //byte[] data = Base64.getDecoder().decode(base64s.get(i).replace("data:image/png;base64,", ""));
                s=s.replace("${img"+(i+1)+"}",base64s.get(i));
            }
            s=s.replace("${table1}",tableStr1());
            s=s.replace("${table2}",tableStr2());
            s=s.replace("${table3}",tableStr3());
            s=s.replace("${rq1}",rq1);
            s=s.replace("${rq2}",rq2);
            s=s.replace("${rq3}",rq3);
            fileWriter = new FileWriter(storePath.replace(releasePath,tempFilePath)+"/yxkb.xml", false);
            fileWriter.write(s);
            fileWriter.flush();
            fileWriter.close();

            InputStream is = new FileInputStream(storePath.replace(releasePath,tempFilePath)+"/yxkb.xml");

            WordprocessingMLPackage pkg = Docx4J.load(is);
            Docx4J.save(pkg, new File(storePath+"/"+wordName));
            FjcfbDto fjcfbDto = new FjcfbDto();
            DBEncrypt bpe = new DBEncrypt();
            fjcfbDto.setFjid(StringUtil.generateUUID());
            fjcfbDto.setYwlx(BusTypeEnum.IMP_MARKETING_SIN.getCode());
            fjcfbDto.setYwid(StringUtil.generateUUID());
            fjcfbDto.setWjm(wordName);
            fjcfbDto.setWjlj(bpe.eCode(storePath + "/" + wordName));
            fjcfbDto.setFwjlj(bpe.eCode(storePath));
            fjcfbDto.setFwjm(bpe.eCode(wordName));
            fjcfbDto.setZhbj("0");
            fjcfbService.insert(fjcfbDto);
            Map<String,String> pdfMap=new HashMap<>();
            //转换PDF
            String wjljjm=storePath + "/" + wordName;
            //连接服务器

            sendWordFile(wjljjm,FTP_URL);
            pdfMap.put("wordName",wordName);
            pdfMap.put("fwjlj",fjcfbDto.getFwjlj());
            pdfMap.put("fjid", fjcfbDto.getFjid());
            pdfMap.put("ywlx",BusTypeEnum.IMP_MARKETING_SIN.getCode());
            pdfMap.put("MQDocOkType",DOC_OK);
            //发送Rabbit消息转换pdf
            amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(), JSONObject.toJSONString(pdfMap));


        } catch (Exception e) {
            logger.error("我有错" + e.getMessage());
            e.printStackTrace();
        } finally {

            try {
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("我有错111111" + e.getMessage());
            }
        }
        jsonList = new ArrayList<>();
    }


    public void getJSON() {
        xsxzcssqs_mon();
        xsxzcssqs_mon_bar();
        xsxzzb_mon();

        xsqdlxzb_mon();
        qgqs_week();
        qgqs_month();


        ptqs_week();
        ptywzb_mon();
        ptqs_mon();
        topry15_mon();
        sfksfb_mon();
        sfbbfb_mon();

        jcxmcpzb_mon();

        //fcpqs_week();
        fcpqs_mon();

        topcos15_mon();

        topdsf15_mon();
        topzx15_mon();


        //tophx15_mon();
        tj_topcos15_mon();
        tj_topzx15_mon();
        hj_topzx15_mon();
        hj_topcos15_mon();
        jy_topzx15_mon();
        jy_topcos15_mon();

        yx_topcos15_mon();
        yx_topzx15_mon();
        smszs_topzx15_mon();
        smzs_topcos15_mon();
    }

    /***
     * 全国周趋势
     * @return
     */
    public void qgqs_week() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("zq", "7");
        paramMap.add("method", "getQgqsSjxxMon");
        paramMap.add("tj", "mon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("hbfl", "qgqs");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        Map<String,Object> map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
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
        //List<String> dataAllNames = new ArrayList<>();
        List<String> dataZx = new ArrayList<>();
        List<String> dataDsf = new ArrayList<>();
        List<String> datahj = new ArrayList<>();
        List<String> dataDls = new ArrayList<>();
        List<String> hbzzDls = new ArrayList<String>();//cso的环比增长值数组（环比增长，（本月值-上个月值）/上个月值*100%
        List<String> hbzzDsf = new ArrayList<String>();//第三方的环比增长值数组
        List<String> hbzzZx = new ArrayList<String>();//直销的环比增长值数组
        List<Long> tempHbzz = new ArrayList<Long>();//存直销、cso、第三方的环比百分值，用以计算最大环比数值
        int maxY = 0;
        double maxBL;
        double minBL;
        //int intval = 0;
        int line_intval;
        for (Object o : jsonArray) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
            if (job.get("rq") != null) {
                if (job.get("rq").toString().length() > 8) {
                    if (!dataxAxis.contains(job.get("rq").toString().substring(5, 10))) {
                        dataxAxis.add(job.get("rq").toString().substring(5, 10));
                    }
                } else {
                    if (!dataxAxis.contains(job.get("rq").toString())) {
                        dataxAxis.add(job.get("rq").toString());
                    }
                }
            }

        }
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
        for (var i = 0; i < datahj.size(); i++) {
            //var temp = 0;
            if (i == 0) {
                hbzzDls.add("0");
                hbzzDsf.add("0");
                hbzzZx.add("0");
                tempHbzz.add(0L);
            } else {
                if (dataDls.get(i - 1).equals("0")) {
                    hbzzDls.add("0");
                } else {
                    // hbzzDls.push( Math.round((dataDls[i]-dataDls[i-1])/dataDls[i-1])*100 );
                    // tempHbzz.push( Math.round((dataDls[i]-dataDls[i-1])/dataDls[i-1])*100 );
                    hbzzDls.add(Math.round((Double.parseDouble(dataDls.get(i)) - Double.parseDouble(dataDls.get(i - 1))) / Double.parseDouble(dataDls.get(i - 1)) * 100) + "");
                    tempHbzz.add(Math.round((Double.parseDouble(dataDls.get(i)) - Double.parseDouble(dataDls.get(i - 1))) / Double.parseDouble(dataDls.get(i - 1)) * 100));
                }
                if (dataDsf.get(i - 1).equals("0")) {
                    hbzzDsf.add("0");
                } else {
                    // hbzzDsf.push( Math.round((dataDsf[i]-dataDsf[i-1])/dataDsf[i-1])*100 );
                    // tempHbzz.push( Math.round((dataDsf[i]-dataDsf[i-1])/dataDsf[i-1])*100 );

                    hbzzDsf.add(Math.round((Double.parseDouble(dataDsf.get(i)) - Double.parseDouble(dataDsf.get(i - 1))) / Double.parseDouble(dataDsf.get(i - 1)) * 100) + "");
                    tempHbzz.add(Math.round((Double.parseDouble(dataDsf.get(i)) - Double.parseDouble(dataDsf.get(i - 1))) / Double.parseDouble(dataDsf.get(i - 1)) * 100));
                }
                if (dataZx.get(i - 1).equals("0")) {
                    hbzzZx.add("0");
                } else {
                    // hbzzZx.push(  Math.round((dataZx[i] -dataZx[i-1])/dataZx[i-1])*100 );
                    // tempHbzz.push( Math.round((dataZx[i] -dataZx[i-1])/dataZx[i-1])*100 );
                    hbzzZx.add(Math.round((Double.parseDouble(dataZx.get(i)) - Double.parseDouble(dataZx.get(i - 1))) / Double.parseDouble(dataZx.get(i - 1)) * 100) + "");
                    tempHbzz.add(Math.round((Double.parseDouble(dataZx.get(i)) - Double.parseDouble(dataZx.get(i - 1))) / Double.parseDouble(dataZx.get(i - 1)) * 100));
                }
            }
        }
        //算最大环比值
        Object[] tempArr = tempHbzz.toArray();
        Arrays.sort(tempArr);

        double t_minBL = Double.parseDouble(tempArr[0].toString());
        double t_maxBL = Double.parseDouble(tempArr[tempArr.length - 1].toString());
        minBL = t_minBL;
        maxBL = t_maxBL;
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
                maxY = Integer.parseInt(dataZx.get(i)) + Integer.parseInt(dataDsf.get(i)) + Integer.parseInt(dataDls.get(i));
            }
        }

        maxBL = maxBL + 4 - maxBL % 4;
        line_intval = (int) (maxBL - minBL) / 4;
        maxY = maxY + 4 - (maxY % 4);
        Option option = new Option();

        Line bar1 = new Line();
        bar1.setType("line");
        bar1.setSmooth(true);
        bar1.setName("直销");
        bar1.setData(hbzzZx.toArray());
        Map<String, Object> bar1ColorMap = new HashMap<>();
        Map<String, Object> bar1normalrMap = new HashMap<>();
        Map<String, Object> bar1labelrMap = new HashMap<>();
        bar1labelrMap.put("show", "true");
        bar1labelrMap.put("position", "top");
        bar1labelrMap.put("formatter", "{c}%");
        Map<String, Object> bar1textMap = new HashMap<>();
        bar1textMap.put("fontSize", "12");
        bar1labelrMap.put("textStyle", bar1textMap);
        bar1normalrMap.put("label", bar1labelrMap);
        bar1normalrMap.put("formatter", "{c}%");
        bar1ColorMap.put("color", "rgb(249,174,0)");
        bar1ColorMap.put("normal", bar1normalrMap);
        bar1.setItemStyle(bar1ColorMap);
        Line bar2 = new Line();
        bar2.setType("line");
        bar2.setSmooth(true);
        bar2.setName("第三方");
        bar2.setData(hbzzDsf.toArray());
        Map<String, Object> bar2ColorMap = new HashMap<>();
        bar2ColorMap.put("color", "rgb(209, 95, 126)");
        Map<String, Object> bar2normalrMap = new HashMap<>();
        Map<String, Object> bar2labelrMap = new HashMap<>();
        bar2labelrMap.put("show", "true");
        bar2labelrMap.put("position", "top");
        bar2labelrMap.put("formatter", "{c}%");
        Map<String, Object> bar2textMap = new HashMap<>();
        bar2textMap.put("fontSize", "12");
        bar2labelrMap.put("textStyle", bar2textMap);
        bar2normalrMap.put("label", bar2labelrMap);
        bar2normalrMap.put("formatter", "{c}%");
        bar2ColorMap.put("normal", bar2normalrMap);
        bar2.setItemStyle(bar2ColorMap);
        Line bar3 = new Line();
        bar3.setType("line");
        bar3.setName("CSO");
        bar3.setSmooth(true);
        bar3.setData(hbzzDls.toArray());
        Map<String, Object> bar3ColorMap = new HashMap<>();
        bar3ColorMap.put("color", "#8e0000");
        Map<String, Object> bar3normalrMap = new HashMap<>();
        Map<String, Object> bar3labelrMap = new HashMap<>();
        bar3labelrMap.put("show", "true");
        bar3labelrMap.put("position", "top");
        bar3labelrMap.put("formatter", "{c}%");
        Map<String, Object> bar3textMap = new HashMap<>();
        bar3textMap.put("fontSize", "12");
        bar3labelrMap.put("textStyle", bar3textMap);
        bar3normalrMap.put("label", bar3labelrMap);
        bar3normalrMap.put("formatter", "{c}%");
        bar3ColorMap.put("normal", bar3normalrMap);
        bar3.setItemStyle(bar3ColorMap);

        Line[] chartList = new Line[]{bar1, bar2, bar3};
        option.series(chartList);

        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        option.tooltip(tooltip);
        Title title = new Title();
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("qgqszqs").toString());
        title.setText("全国月趋势(环比)");
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "18");
        textMap.put("fontWeight", "bold");
        title.setSubtextStyle(textMap);
        option.title(title);
        Grid grid = new Grid();
        grid.setRight("4%");
        grid.setTop(80);
        grid.setLeft("3%");
        grid.setHeight(200);
        grid.setContainLabel("true");
        option.grid(grid);
        XAxis xAxis = new XAxis();
        xAxis.setType("category");
        xAxis.setData(dataxAxis.toArray());
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setName("环比");
        yAxis.setType("value");
        yAxis.setMax(maxBL);
        yAxis.setMin(minBL);
        yAxis.setInterval(line_intval);
        option.yAxis(yAxis);
        Legend legend = new Legend();
        String[] strArr = new String[]{"直销", "第三方", "CSO"};
        legend.setData(strArr);
        Map<String, Object> textStyleMap = new HashMap<>();
        textStyleMap.put("color", "#3E9EE1");
        textStyleMap.put("fontSize","24");
        legend.setTextStyle(textStyleMap);
        option.legend(legend);
        JSONObject jsonObject1 = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj1 = JSONObject.parseObject(JSON.toJSONString(jsonObject1.get("title")));
        titleObj1.put("x", "left");
        titleObj1.put("y", "0");
        jsonObject1.put("title", titleObj1);
        JSONObject legendObj1 = JSONObject.parseObject(JSON.toJSONString(jsonObject1.get("legend")));
        legendObj1.put("y", "15");
        legendObj1.put("x", "300");
        jsonObject1.put("legend", legendObj1);

        jsonList.add(jsonObject1.toJSONString());
    }

    /***
     * 全国月趋势
     * @return
     */
    public void qgqs_month() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("zq", "7");
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
        //List<String> dataAllNames = new ArrayList<>();
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
        //bar1ColorMap.put("color", "rgb(249,174,0)");
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
        //bar2ColorMap.put("color", "rgb(209, 95, 126)");
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
        //bar3ColorMap.put("color", "#8e0000");
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
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("qgqszqs").toString());
        title.setText("全国月趋势");
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
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
        textStyleMap.put("fontSize","24");
        legend.setTextStyle(textStyleMap);
        option.legend(legend);
        JSONObject jsonObject1 = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj1 = JSONObject.parseObject(JSON.toJSONString(jsonObject1.get("title")));
        titleObj1.put("x", "left");
        titleObj1.put("y", "5");
        jsonObject1.put("title", titleObj1);
        JSONObject legendObj1 = JSONObject.parseObject(JSON.toJSONString(jsonObject1.get("legend")));
        legendObj1.put("y", "15");
        legendObj1.put("x", "300");
        jsonObject1.put("legend", legendObj1);
        jsonList.add(jsonObject1.toJSONString());
    }

    /**
     * 分产品月趋势
     *
     * @return
     */
    public void fcpqs_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("zq", "7");
        paramMap.add("method", "getCpqstjSjxxMon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("tj", "mon");
        paramMap.add("yxxs", "mNGS项目,Resfirst,tNGS");
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
        List<String> names = new ArrayList<>();
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
                    int sl = job.get("sl") == null ? 0 : Integer.valueOf(job.get("sl").toString());
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
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "18");
        textMap.put("fontWeight", "bold");
        title.setSubtextStyle(textMap);
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("cpqstjzqs").toString());
        title.setText("分产品月趋势");
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
        textStyleMap.put("fontSize","20");
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
        titleObj.put("y", "-6");
        jsonObject.put("title", titleObj);
        JSONObject legendObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("legend")));
        legendObj.put("y", "12");
        legendObj.put("x", "300");
        jsonObject.put("legend", legendObj);
        jsonList.add(jsonObject.toJSONString());
    }

    /**
     * 销售性质测试数折线图
     */
    public void xsxzcssqs_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("zq", "7");
        paramMap.add("method", "getXsxzcssqsMon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("tj", "mon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("xsxzcssqs");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataxAxis = new ArrayList<>();
        List<String> kydata = new ArrayList();
        List<String> rydata = new ArrayList();
        List<String> tjdata = new ArrayList();
        List<String> dsfdata = new ArrayList();
        List<String> zxdata = new ArrayList();
        List<String> csodata = new ArrayList();
        int maxY = 0;
        int intval = 0;
        int num = 0;
        var line_maxY = 0;
        int line_intval;
        int line_minY = 0;
        List<String> line_kydata = new ArrayList();
        List<String> line_rydata = new ArrayList();
        List<String> line_tjdata = new ArrayList();
        List<String> line_dsfdata = new ArrayList();
        List<String> line_zxdata = new ArrayList();
        List<String> line_csodata = new ArrayList();
        if (jsonArray!=null&&!jsonArray.isEmpty()) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(0)));
            dataxAxis.add(job.get("rq").toString());
            for (Object o : jsonArray) {
                JSONObject job1 = JSONObject.parseObject(JSON.toJSONString(o));
                if (job.get("rq").equals(job1.get("rq"))) {
                    if ("科研".equals(job1.get("sjqfmc"))) {
                        kydata.add(job1.get("num").toString());
                        line_kydata.add(job1.get("bl").toString());
                    } else if ("入院".equals(job1.get("sjqfmc"))) {
                        rydata.add(job1.get("num").toString());
                        line_rydata.add(job1.get("bl").toString());
                    } else if ("特检".equals(job1.get("sjqfmc"))) {
                        tjdata.add(job1.get("num").toString());
                        line_tjdata.add(job1.get("bl").toString());
                    } else if ("第三方实验室".equals(job1.get("sjqfmc"))) {
                        dsfdata.add(job1.get("num").toString());
                        line_dsfdata.add(job1.get("bl").toString());
                    } else if ("直销".equals(job1.get("sjqfmc"))) {
                        zxdata.add(job1.get("num").toString());
                        line_zxdata.add(job1.get("bl").toString());
                    } else if ("CSO".equals(job1.get("sjqfmc"))) {
                        csodata.add(job1.get("num").toString());
                        line_csodata.add(job1.get("bl").toString());
                    }
                    num += Integer.parseInt(job1.get("num").toString());
                } else {
                    job = JSONObject.parseObject(JSON.toJSONString(o));
                    dataxAxis.add(job1.get("rq").toString());
                    num = Integer.parseInt(job1.get("num").toString());
                    if ("科研".equals(job1.get("sjqfmc"))) {
                        kydata.add(job1.get("num").toString());
                        line_kydata.add(job1.get("bl").toString());
                    } else if ("入院".equals(job1.get("sjqfmc"))) {
                        rydata.add(job1.get("num").toString());
                        line_rydata.add(job1.get("bl").toString());
                    } else if ("特检".equals(job1.get("sjqfmc"))) {
                        tjdata.add(job1.get("num").toString());
                        line_tjdata.add(job1.get("bl").toString());
                    } else if ("第三方实验室".equals(job1.get("sjqfmc"))) {
                        dsfdata.add(job1.get("num").toString());
                        line_dsfdata.add(job1.get("bl").toString());
                    } else if ("直销".equals(job1.get("sjqfmc"))) {
                        zxdata.add(job1.get("num").toString());
                        line_zxdata.add(job1.get("bl").toString());
                    } else if ("CSO".equals(job1.get("sjqfmc"))) {
                        csodata.add(job1.get("num").toString());
                        line_csodata.add(job1.get("bl").toString());
                    }
                }
                if (maxY < num)
                    maxY = num;
                if (line_maxY < Integer.parseInt(job1.get("bl").toString()))
                    line_maxY = Integer.parseInt(job1.get("bl").toString());
                if (line_minY > Integer.parseInt(job1.get("bl").toString()) && Integer.parseInt(job1.get("bl").toString()) < 0)
                    line_minY = Integer.parseInt(job1.get("bl").toString());
            }
        }

        maxY = maxY + 4 - maxY % 4;
        line_maxY = line_maxY + 4 - line_maxY % 4;
        line_intval = line_maxY / 4;

        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        option.tooltip(tooltip);
        Title title = new Title();
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        title.setSubtextStyle(textMap);
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setText("客户性质测试数趋势");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
        title.setSubtext(zqsjob.get("xsxzcssqszqs").toString());
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
        yAxis.setMax(line_maxY);
        yAxis.setMin(line_minY);
        yAxis.setInterval(line_intval);
        Map<String, Object> axisLabel = new HashMap<>();
        axisLabel.put("formatter", "{value}%");
        yAxis.setAxisLabel(axisLabel);
        option.yAxis(yAxis);
        Legend legend = new Legend();
        String[] lengArr = new String[]{"科研", "入院", "特检","第三方实验室","直销","CSO"};
        Chart[] chats = new Chart[6];
        for (int i = 0; i < lengArr.length; i++) {
            String str = lengArr[i];
            Line line = new Line();
            line.setName(str);
            Map<String, Object> line_item = new HashMap<>();
            Map<String, Object> line_nor = new HashMap<>();
            Map<String, Object> line_lab = new HashMap<>();
            line_lab.put("show", true);
            line_lab.put("position", "top");
            line_lab.put("formatter", "{c}%");
            Map<String, Object> line_text = new HashMap<>();
            line_text.put("fontSize", "18");
            line_lab.put("textStyle", line_text);
            line_nor.put("label", line_lab);
            line_nor.put("formatter", "{c}%");
            line_item.put("normal", line_nor);
            line.setItemStyle(line_item);
            if ("科研".equals(str)) {
                line.setData(line_kydata.toArray());
            } else if ("入院".equals(str)) {
                line.setData(line_rydata.toArray());
            } else if ("特检".equals(str)) {
                line.setData(line_tjdata.toArray());
            }else if ("第三方实验室".equals(str)) {
                line.setData(line_dsfdata.toArray());
            }else if ("直销".equals(str)) {
                line.setData(line_zxdata.toArray());
            }else if ("CSO".equals(str)) {
                line.setData(line_csodata.toArray());
            }
            line.setSmooth(true);
            chats[i] = line;

        }
        option.series(chats);
        legend.setData(lengArr);
        Map<String, Object> textStyleMap = new HashMap<>();
        textStyleMap.put("fontSize","24");
        textStyleMap.put("color", "#3E9EE1");
        legend.setTextStyle(textStyleMap);
        legend.setBottom(0);
        option.legend(legend);
        Grid grid = new Grid();
        grid.setTop(90);
        grid.setRight(50);
        grid.setBottom("20%");
        option.grid(grid);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "-6");
        jsonObject.put("title", titleObj);
        JSONObject legendObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("legend")));
        jsonObject.put("legend", legendObj);
        jsonList.add(jsonObject.toJSONString());
    }

    /**
     * 平台周趋势
     *
     * @return
     */
    public void xsxzcssqs_mon_bar() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("zq", "7");
        paramMap.add("method", "getXsxzcssqsMon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("tj", "mon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("xsxzcssqs");
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
        //List<String> names = new ArrayList<>();
        List<String> dataAllNames;
        String[] strArr=new String[]{"科研","入院","特检","第三方实验室","直销","CSO"};
        dataAllNames=Arrays.asList(strArr);
        int maxY = 0;
        Option option = new Option();
        for (String dataAllName : dataAllNames) {
            List<String> dataSl = new ArrayList<>();
            Line chart1 = new Line();

            for (String dataxAxi : dataxAxis) {
                var flag = false;
                for (Object o : jsonArray) {

                    JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
                    int sl = job.get("num") == null ? 0 : Integer.valueOf(job.get("num").toString());
                    if (job.get("rq") != null && job.get("rq").toString().length() > 8) {
                        if (dataAllName.equals(job.get("sjqfmc")) && job.get("rq").toString().substring(5, 10).equals(dataxAxi)) {
                            dataSl.add(sl + "");

                            flag = true;
                            break;
                        }
                    } else {
                        if (dataAllName.equals(job.get("sjqfmc")) && job.get("rq").toString().equals(dataxAxi)) {
                            dataSl.add(sl + "");
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
            textStyle.put("fontSize", 16);
            label.put("show", true);
            label.put("position", "top");
            label.put("textStyle", textStyle);
            normal.put("label", label);
            itemStyleMap.put("normal", normal);
            chart1.setItemStyle(itemStyleMap);
            chart1.setSmooth(true);
            chart1.setStack("个数");
            option.addSeries(chart1);
        }
        int num = 0;
        if (!jsonArray.isEmpty()) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(0)));
            for (Object o : jsonArray) {
                JSONObject job1 = JSONObject.parseObject(JSON.toJSONString(o));
                if (job.get("rq").equals(job1.get("rq"))) {
                    num += Integer.parseInt(job1.get("num").toString());
                } else {
                    job = JSONObject.parseObject(JSON.toJSONString(o));

                    num = Integer.parseInt(job1.get("num").toString());
                }
                if (maxY < num)
                    maxY = num;

            }
        }
        maxY = maxY + 4 - (maxY % 4);
        int interval = maxY / 4;

        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        option.tooltip(tooltip);
        Title title = new Title();
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "18");
        textMap.put("fontWeight", "bold");
        title.setSubtextStyle(textMap);
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("xsxzcssqszqs").toString());
        title.setText("客户性质测试数趋势");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
        option.title(title);
        Grid grid = new Grid();
        grid.setTop(90);
        grid.setRight(50);
        grid.setBottom("20%");
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
        yAxis.setName("个数");
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
        Map<String,Object>legendMap=new HashMap<>();
        legendMap.put("fontSize","24");
        legend.setTextStyle(legendMap);
        legend.setBottom(0);
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
     * 销售性质柱状
     */
    public void xsxzcssqs_mon_bar1() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("zq", "7");
        paramMap.add("method", "getXsxzcssqsMon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("tj", "mon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("xsxzcssqs");
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
        //List<String> dataAllNames = new ArrayList<>();
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
                        if (job.get("sjqfmc") != null && job.get("sjqfmc").equals("科研")) {
                            sum += Integer.parseInt(job.get("num") == null ? "0" : job.get("num").toString());
                            dataZx.add(job.get("num") == null ? "0" : job.get("num").toString());
                        } else if (job.get("sjqfmc") != null && job.get("sjqfmc").equals("入院")) {
                            dataDsf.add(job.get("num") == null ? "0" : job.get("num").toString());
                            sum += Integer.parseInt(job.get("num") == null ? "0" : job.get("num").toString());
                        } else if (job.get("sjqfmc") != null && job.get("sjqfmc").equals("特检")) {
                            dataDls.add(job.get("num") == null ? "0" : job.get("num").toString());
                            sum += Integer.parseInt(job.get("num") == null ? "0" : job.get("num").toString());
                        }
                    }
                } else {
                    var date = job.get("rq").toString();
                    if (dataxAxis.get(i).equals(date)) {
                        if (job.get("sjqfmc") != null && job.get("sjqfmc").equals("科研")) {
                            sum += Integer.parseInt(job.get("num") == null ? "0" : job.get("num").toString());
                            dataZx.add(job.get("num") == null ? "0" : job.get("num").toString());
                        } else if (job.get("sjqfmc") != null && job.get("sjqfmc").equals("入院")) {
                            dataDsf.add(job.get("num") == null ? "0" : job.get("num").toString());
                            sum += Integer.parseInt(job.get("num") == null ? "0" : job.get("num").toString());
                        } else if (job.get("sjqfmc") != null && job.get("sjqfmc").equals("特检")) {
                            dataDls.add(job.get("num") == null ? "0" : job.get("num").toString());
                            sum += Integer.parseInt(job.get("num") == null ? "0" : job.get("num").toString());
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
        bar1.setName("科研");
        bar1.setStack("个数");
        bar1.setBarGap("0");
        Map<String, Object> bar1LabelMap = new HashMap<>();
        Map<String, Object> bar1morMap = new HashMap<>();
        bar1morMap.put("show", true);
        bar1morMap.put("position", "insideBottom");
        bar1morMap.put("fontSize", "18");
        bar1LabelMap.put("normal", bar1morMap);
        bar1.setLabel(bar1LabelMap);
        bar1.setData(dataZx.toArray());
        Map<String, Object> bar1ColorMap = new HashMap<>();
        //bar1ColorMap.put("color", "rgb(249,174,0)");
        bar1.setItemStyle(bar1ColorMap);
        Bar bar2 = new Bar();
        bar2.setName("入院");
        bar2.setStack("个数");
        bar2.setBarGap("0");
        Map<String, Object> bar2LabelMap = new HashMap<>();
        Map<String, Object> bar2morMap = new HashMap<>();
        bar2morMap.put("show", true);
        bar2morMap.put("position", "insideBottom");
        bar2morMap.put("fontSize", "18");
        bar2LabelMap.put("normal", bar2morMap);

        bar2.setLabel(bar2LabelMap);
        bar2.setData(dataDsf.toArray());
        Map<String, Object> bar2ColorMap = new HashMap<>();
        //bar2ColorMap.put("color", "rgb(209, 95, 126)");
        bar2.setItemStyle(bar2ColorMap);
        Bar bar3 = new Bar();
        bar3.setName("特检");
        bar3.setStack("个数");
        bar3.setBarGap("0");
        Map<String, Object> bar3LabelMap = new HashMap<>();
        Map<String, Object> bar3morMap = new HashMap<>();
        bar3morMap.put("show", true);
        bar3morMap.put("position", "insideBottom");
        bar3morMap.put("fontSize", "18");
        bar3LabelMap.put("normal", bar3morMap);
        bar3.setLabel(bar3LabelMap);
        bar3.setData(dataDls.toArray());
        Map<String, Object> bar3ColorMap = new HashMap<>();
        //bar3ColorMap.put("color", "#8e0000");
        bar3.setItemStyle(bar3ColorMap);
        Bar bar4 = new Bar();
        bar4.setName("合计");
        bar4.setStack("个数");
        bar4.setBarGap("0");
        Map<String, Object> bar4LabelMap = new HashMap<>();
        Map<String, Object> bar4morMap = new HashMap<>();
        Map<String, Object> bar4TextStyleMap = new HashMap<>();
        bar4TextStyleMap.put("color", "#ff4848");
        bar4TextStyleMap.put("fontSize", "16");
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
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setText("客户性质测试数趋势");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "20");
        title.setTextStyle(subMap);
        title.setSubtext(zqsjob.get("xsxzcssqszqs").toString());
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "18");
        textMap.put("fontWeight", "bold");
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
        String[] strArr = new String[]{"科研", "入院", "特检"};
        legend.setData(strArr);
        Map<String, Object> textStyleMap = new HashMap<>();
        textStyleMap.put("color", "#3E9EE1");
        textStyleMap.put("fontSize","24");
        legend.setTextStyle(textStyleMap);
        option.legend(legend);
        JSONObject jsonObject1 = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj1 = JSONObject.parseObject(JSON.toJSONString(jsonObject1.get("title")));
        titleObj1.put("x", "left");
        titleObj1.put("y", "5");
        jsonObject1.put("title", titleObj1);
        JSONObject legendObj1 = JSONObject.parseObject(JSON.toJSONString(jsonObject1.get("legend")));
        legendObj1.put("y", "15");
        legendObj1.put("x", "300");
        jsonObject1.put("legend", legendObj1);
        jsonList.add(jsonObject1.toJSONString());
    }

    /**
     * 分产品周趋势
     *
     * @return
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
        List<String> names = new ArrayList<>();
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
                    int sl = job.get("sl") == null ? 0 : Integer.valueOf(job.get("sl").toString());
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
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "18");
        textMap.put("fontWeight", "bold");
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
        Map<String,Object>legendMap=new HashMap<>();
        legendMap.put("fontSize","24");
        legend.setTextStyle(legendMap);
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
     *
     * @return
     */
    public void ptqs_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("zq", "7");
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
        List<String> names = new ArrayList<>();
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
                    int sl = job.get("sl") == null ? 0 : Integer.valueOf(job.get("sl").toString());
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
            textStyle.put("fontSize", 16);
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
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("ptqstjzqs").toString());
        title.setText("平台月趋势");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        title.setSubtextStyle(textMap);
        option.title(title);
        Grid grid = new Grid();
        grid.setTop(90);
        grid.setRight(50);
        grid.setBottom("20%");
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
        Map<String,Object>legendMap=new HashMap<>();
        legendMap.put("fontSize","24");
        legend.setBottom(0);
        legend.setTextStyle(legendMap);
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
     *
     * @return
     */
    public void ptqs_week() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("zq", "7");
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
        List<String> names = new ArrayList<>();
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
                    int sl = job.get("sl") == null ? 0 : Integer.valueOf(job.get("sl").toString());
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
            textStyle.put("fontSize", 16);
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
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "18");
        textMap.put("fontWeight", "bold");
        title.setSubtextStyle(textMap);
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("ptqstjzqs").toString());
        title.setText("平台周趋势");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
        option.title(title);
        Grid grid = new Grid();
        grid.setTop(90);
        grid.setRight(50);
        grid.setBottom("20%");
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
        Map<String,Object>legendMap=new HashMap<>();
        legendMap.put("fontSize","24");
        legend.setTextStyle(legendMap);
        legend.setBottom(0);
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
     *
     * @return
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
        int len = 9;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        int sum = 0;
        int max = 0;
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
            sum = sum + Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            if (Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString())>max){
                max = Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            }

        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("topDsf20").toString());
        title.setText("第三方top10");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
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
        xAxis.setMax(max*1.3);
        xAxis.setType("value");
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "#00b2ee");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("10");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "#00b2ee");
        axisLine_y.put("lineStyle", colorMap_y);
        Map<String, Object> text_y = new HashMap<>();
        text_y.put("fontSize", "18");
        Map<String, Object> label_y = new HashMap<>();
        label_y.put("textStyle",text_y);
        yAxis.setAxisLabel(label_y);
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
        textStyle.put("fontSize", "18");
        label.put("textStyle", textStyle);
        label.put("show", "true");
        label.put("position", "right");
        label.put("fontSize", "18");
        label.put("formatter", "function (params) {" +
                "var percent =params.data+'(' +Number((params.data / " + sum + ") * 100).toFixed(2) + '%'+')';" +
                "return percent;" +
                "}function");
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
     *
     * @return
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
        int len = 9;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        int sum = 0;
        int max = 0;
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
            sum = sum + Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            if (Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString())>max){
                max = Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            }

        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        title.setSubtextStyle(textMap);
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("topZx20").toString());
        title.setText("直销top15");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
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
        xAxis.setMax(max*1.3);
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "rgb(249,174,0)");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("10");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(249,174,0)");
        axisLine_y.put("lineStyle", colorMap_y);
        Map<String, Object> text_y = new HashMap<>();
        text_y.put("fontSize", "18");
        Map<String, Object> label_y = new HashMap<>();
        label_y.put("textStyle",text_y);
        yAxis.setAxisLabel(label_y);
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
        textStyle.put("fontSize", "18");
        label.put("textStyle", textStyle);
        label.put("show", "true");
        label.put("fontSize", "18");
        label.put("position", "right");
        label.put("formatter", "function (params) {" +
                "var percent =params.data+'(' +Number((params.data / " + sum + ") * 100).toFixed(2) + '%'+')';" +
                "return percent;" +
                "}function");
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
     *
     * @return
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
        int len = 9;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        int sum = 0;
        int max = 0;
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
            sum = sum + Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            if (Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString())>max){
                max = Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            }

        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("topCSO20").toString());
        title.setText("COStop10");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
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
        xAxis.setMax(max*1.3);
        xAxis.setType("value");
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "rgb(209,95,126)");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("10");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(209,95,126)");
        axisLine_y.put("lineStyle", colorMap_y);
        Map<String, Object> text_y = new HashMap<>();
        text_y.put("fontSize", "18");
        Map<String, Object> label_y = new HashMap<>();
        label_y.put("textStyle",text_y);
        yAxis.setAxisLabel(label_y);
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
        textStyle.put("fontSize", "18");
        label.put("textStyle", textStyle);
        label.put("show", "true");
        label.put("fontSize", "18");
        label.put("position", "right");
        label.put("formatter", "function (params) {" +
                "var percent =params.data+'(' +Number((params.data / " + sum + ") * 100).toFixed(2) + '%'+')';" +
                "return percent;" +
                "}function");
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
     * 核心月度
     *
     * @return
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
        int len = 9;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        int sum = 0;
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("dwmc") == null ? "" : job.get("dwmc").toString());
            dataseries.add(job.get("zhnum") == null ? "" : job.get("zhnum").toString());
            sum = sum + Integer.parseInt(job.get("zhnum") == null ? "0" : job.get("zhnum").toString());
        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
        title.setSubtext("核心医院top10");
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
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
        yAxis.setOffset("10");
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
        label.put("formatter", "function (params) {" +
                "var percent =params.data+'(' +Number((params.data / " + sum + ") * 100).toFixed(2) + '%'+')';" +
                "return percent;" +
                "}function");
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
     *
     * @return
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
        int len = 9;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        int sum = 0;
        int max = 0;
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("dwmc") == null ? "" : job.get("dwmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
            sum = sum + Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            if (Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString())>max){
                max = Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            }
        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        title.setSubtextStyle(textMap);
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("topRY20").toString());
        title.setText("入院top10");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
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
        xAxis.setMax(max*1.3);
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "rgb(209,95,126)");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("10");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(209,95,126)");
        axisLine_y.put("lineStyle", colorMap_y);
        Map<String, Object> text_y = new HashMap<>();
        text_y.put("fontSize", "18");
        Map<String, Object> label_y = new HashMap<>();
        label_y.put("textStyle",text_y);
        yAxis.setAxisLabel(label_y);
        yAxis.setAxisLine(axisLine_y);
        Map<String, Object> axisTick = new HashMap<>();
        axisTick.put("alignWithLabel", true);
        yAxis.setAxisTick(axisTick);
        Map<String, Object> nameTextStyle = new HashMap<>();
        nameTextStyle.put("fontSize", "18");
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
        textStyle.put("fontSize", "18");
        label.put("textStyle", textStyle);
        label.put("fontSize", "18");
        label.put("show", "true");
        label.put("position", "right");
        label.put("formatter", "function (params) {" +
                "var percent =params.data+'(' +Number((params.data / " + sum + ") * 100).toFixed(2) + '%'+')';" +
                "return percent;" +
                "}function");
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
     * 平台业务占比
     *
     * @return
     */
    public void sfbbfb_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getChargesDivideByYblxByMon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("tj", "mon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
//        paramMap.add("lrsjStart", jsrqstart.substring(0, 7));
//        paramMap.add("lrsjEnd", jsrqend.substring(0, 7));
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("yblxSfcssList");
        List<Map<String, Object>> seriesData = new ArrayList<>();
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        for (var i = 0; i < (Math.min(jsonArray.size(), 10)); i++) {
            Map<String, Object> dataMap = new HashMap<>();
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataMap.put("value", job.get("cnum"));
            dataMap.put("name", job.get("yblxmc"));
            seriesData.add(dataMap);
        }
        Option option = new Option();
        Title title = new Title();
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("yblxSfcssList").toString());
        title.setText("收费标本分布(直销+代理商)");
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        title.setSubtextStyle(textMap);
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
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
        labelMap.put("fontSize", "18");
        labelMap.put("formatter", "{b} : \n{c} /{d}%");
        labelLineMap.put("show", "true");
        normalMap.put("labelLine", labelLineMap);
        normalMap.put("label", labelMap);
        itemMap.put("normal", normalMap);
        pie.setItemStyle(itemMap);
        option.addSeries(pie);
        Map<String, Object> itemMap1 = new HashMap<>();
        Map<String, Object> textStyleMap = new HashMap<>();
        textStyleMap.put("fontSize", "16");
        itemMap1.put("textStyle", textStyleMap);
        pie.setLabel(itemMap1);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "20");
        jsonList.add(jsonObject.toJSONString());
    }

    /**
     * 平台业务占比
     *
     * @return
     */
    public void sfksfb_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getChargesDivideByKsByMon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("tj", "mon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
//        paramMap.add("lrsjStart", jsrqstart.substring(0, 7));
//        paramMap.add("lrsjEnd", jsrqend.substring(0, 7));
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("ksSfcssList");
        List<Map<String, Object>> seriesData = new ArrayList<>();
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        for (var i = 0; i < (Math.min(jsonArray.size(), 10)); i++) {
            Map<String, Object> dataMap = new HashMap<>();
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataMap.put("value", job.get("cnum"));
            dataMap.put("name", job.get("ksmc"));
            seriesData.add(dataMap);
        }
        Option option = new Option();
        Title title = new Title();
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("ksSfcssList").toString());
        title.setText("收费科室分布(直销+代理商)");
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        title.setSubtextStyle(textMap);
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
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
        labelMap.put("fontSize", "18");
        labelMap.put("formatter", "{b} : \n{c}/{d}%");
        labelLineMap.put("show", "true");
        normalMap.put("labelLine", labelLineMap);
        normalMap.put("label", labelMap);
        itemMap.put("normal", normalMap);
        pie.setItemStyle(itemMap);
        option.addSeries(pie);
        Map<String, Object> itemMap1 = new HashMap<>();
        Map<String, Object> textStyleMap = new HashMap<>();
        textStyleMap.put("fontSize", "16");
        itemMap1.put("textStyle", textStyleMap);
        pie.setLabel(itemMap1);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "20");
        jsonList.add(jsonObject.toJSONString());
    }

    /***
     * 年度平台指标达成率
     * @return
     */
    public void ptzbdcl_nd(XWPFDocument document, XWPFParagraph page) {

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
        // 标题部分
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph page1 = doc.createParagraph();
        XWPFRun run1 = page1.createRun();
        run1.setFontFamily("宋体");
        run1.setText("平台");
        run1.setFontSize(7);
        tableRowOne.getCell(0).setParagraph(page1);
        XWPFParagraph page2 = doc.createParagraph();
        XWPFRun run2 = page2.createRun();
        run2.setFontFamily("宋体");
        run2.setText("测试数/指标");
        run2.setFontSize(7);
        tableRowOne.addNewTableCell().setParagraph(page2);
        XWPFParagraph page3 = doc.createParagraph();
        XWPFRun run3 = page3.createRun();
        run3.setFontFamily("宋体");
        run3.setText("达成率");
        run3.setFontSize(7);
        tableRowOne.addNewTableCell().setParagraph(page3);
        XWPFParagraph page4 = doc.createParagraph();
        XWPFRun run4 = page4.createRun();
        run4.setFontFamily("宋体");
        run4.setText("gap");
        run4.setFontSize(7);
        tableRowOne.addNewTableCell().setParagraph(page4);
        tableRowOne.getCell(0).setColor("409EFF");
        tableRowOne.getCell(1).setColor("409EFF");
        tableRowOne.getCell(2).setColor("409EFF");
        tableRowOne.getCell(3).setColor("409EFF");
        // create second row

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            XWPFTableRow tableRow = table.createRow();
            XWPFParagraph page_1 = doc.createParagraph();
            XWPFRun run_1 = page_1.createRun();
            run_1.setFontFamily("宋体");
            run_1.setText(job.get("ptmc") == null ? "" : job.get("ptmc").toString());
            run_1.setFontSize(7);
            tableRow.getCell(0).setParagraph(page_1);

            XWPFParagraph page_2 = doc.createParagraph();
            XWPFRun run_2 = page_2.createRun();
            run_2.setFontFamily("宋体");
            run_2.setText(job.get("sz") == null ? "" : job.get("sz").toString() + "/" + (job.get("sumnum") == null ? "" : job.get("sumnum").toString()));
            run_2.setFontSize(7);
            tableRow.getCell(1).setParagraph(page_2);


            XWPFParagraph page_3 = doc.createParagraph();
            XWPFRun run_3 = page_3.createRun();
            run_3.setFontFamily("宋体");
            run_3.setText(job.get("gap") == null ? "" : job.get("gap").toString());
            run_3.setFontSize(7);
            tableRow.getCell(2).setParagraph(page_3);


            XWPFParagraph page_4 = doc.createParagraph();
            XWPFRun run_4 = page_4.createRun();
            run_4.setFontFamily("宋体");
            run_4.setText(job.get("ptmc") == null ? "" : job.get("ptmc").toString());
            run_4.setFontSize(7);
            tableRow.getCell(3).setParagraph(page_4);

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
     * @return
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
        // create first row
        XWPFTableRow tableRowOne = table.getRow(0);
        table.setWidth("100%");
        // 标题部分
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph page1 = doc.createParagraph();
        XWPFRun run1 = page1.createRun();
        run1.setFontFamily("宋体");
        run1.setText("平台");
        run1.setFontSize(7);
        tableRowOne.getCell(0).setParagraph(page1);
        XWPFParagraph page2 = doc.createParagraph();
        XWPFRun run2 = page2.createRun();
        run2.setFontFamily("宋体");
        run2.setText("测试数/指标");
        run2.setFontSize(7);
        tableRowOne.addNewTableCell().setParagraph(page2);
        XWPFParagraph page3 = doc.createParagraph();
        XWPFRun run3 = page3.createRun();
        run3.setFontFamily("宋体");
        run3.setText("达成率");
        run3.setFontSize(7);
        tableRowOne.addNewTableCell().setParagraph(page3);
        XWPFParagraph page4 = doc.createParagraph();
        XWPFRun run4 = page4.createRun();
        run4.setFontFamily("宋体");
        run4.setText("gap");
        run4.setFontSize(7);
        tableRowOne.addNewTableCell().setParagraph(page4);
        tableRowOne.getCell(0).setColor("409EFF");
        tableRowOne.getCell(1).setColor("409EFF");
        tableRowOne.getCell(2).setColor("409EFF");
        tableRowOne.getCell(3).setColor("409EFF");
        // create second row

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            XWPFTableRow tableRow = table.createRow();
            XWPFParagraph page_1 = doc.createParagraph();
            XWPFRun run_1 = page_1.createRun();
            run_1.setFontFamily("宋体");
            run_1.setText(job.get("ptmc") == null ? "" : job.get("ptmc").toString());
            run_1.setFontSize(7);
            tableRow.getCell(0).setParagraph(page_1);

            XWPFParagraph page_2 = doc.createParagraph();
            XWPFRun run_2 = page_2.createRun();
            run_2.setFontFamily("宋体");
            run_2.setText(job.get("sz") == null ? "" : job.get("sz").toString() + "/" + (job.get("sumnum") == null ? "" : job.get("sumnum").toString()));
            run_2.setFontSize(7);
            tableRow.getCell(1).setParagraph(page_2);


            XWPFParagraph page_3 = doc.createParagraph();
            XWPFRun run_3 = page_3.createRun();
            run_3.setFontFamily("宋体");
            run_3.setText(job.get("gap") == null ? "" : job.get("gap").toString());
            run_3.setFontSize(7);
            tableRow.getCell(2).setParagraph(page_3);


            XWPFParagraph page_4 = doc.createParagraph();
            XWPFRun run_4 = page_4.createRun();
            run_4.setFontFamily("宋体");
            run_4.setText(job.get("ptmc") == null ? "" : job.get("ptmc").toString());
            run_4.setFontSize(7);
            tableRow.getCell(3).setParagraph(page_4);
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
     * @return
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
        // create first row
        XWPFTableRow tableRowOne = table.getRow(0);
        table.setWidth("100%");
        // 标题部分
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph page1 = doc.createParagraph();
        XWPFRun run1 = page1.createRun();
        run1.setFontFamily("宋体");
        run1.setText("平台");
        run1.setFontSize(7);
        tableRowOne.getCell(0).setParagraph(page1);
        XWPFParagraph page2 = doc.createParagraph();
        XWPFRun run2 = page2.createRun();
        run2.setFontFamily("宋体");
        run2.setText("测试数/指标");
        run2.setFontSize(7);
        tableRowOne.addNewTableCell().setParagraph(page2);
        XWPFParagraph page3 = doc.createParagraph();
        XWPFRun run3 = page3.createRun();
        run3.setFontFamily("宋体");
        run3.setText("达成率");
        run3.setFontSize(7);
        tableRowOne.addNewTableCell().setParagraph(page3);
        XWPFParagraph page4 = doc.createParagraph();
        XWPFRun run4 = page4.createRun();
        run4.setFontFamily("宋体");
        run4.setText("gap");
        run4.setFontSize(7);
        tableRowOne.addNewTableCell().setParagraph(page4);
        tableRowOne.getCell(0).setColor("409EFF");
        tableRowOne.getCell(1).setColor("409EFF");
        tableRowOne.getCell(2).setColor("409EFF");
        tableRowOne.getCell(3).setColor("409EFF");
        // create second row

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            XWPFTableRow tableRow = table.createRow();
            XWPFParagraph page_1 = doc.createParagraph();
            XWPFRun run_1 = page_1.createRun();
            run_1.setFontFamily("宋体");
            run_1.setText(job.get("ptmc") == null ? "" : job.get("ptmc").toString());
            run_1.setFontSize(7);
            tableRow.getCell(0).setParagraph(page_1);

            XWPFParagraph page_2 = doc.createParagraph();
            XWPFRun run_2 = page_2.createRun();
            run_2.setFontFamily("宋体");
            run_2.setText(job.get("sz") == null ? "" : job.get("sz").toString() + "/" + (job.get("sumnum") == null ? "" : job.get("sumnum").toString()));
            run_2.setFontSize(7);
            tableRow.getCell(1).setParagraph(page_2);


            XWPFParagraph page_3 = doc.createParagraph();
            XWPFRun run_3 = page_3.createRun();
            run_3.setFontFamily("宋体");
            run_3.setText(job.get("gap") == null ? "" : job.get("gap").toString());
            run_3.setFontSize(7);
            tableRow.getCell(2).setParagraph(page_3);


            XWPFParagraph page_4 = doc.createParagraph();
            XWPFRun run_4 = page_4.createRun();
            run_4.setFontFamily("宋体");
            run_4.setText(job.get("ptmc") == null ? "" : job.get("ptmc").toString());
            run_4.setFontSize(7);
            tableRow.getCell(3).setParagraph(page_4);
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
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setText("客户性质占比");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
        title.setSubtext(zqsjob.get("sjqfcsszbzqs").toString());
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        title.setSubtextStyle(textMap);
        option.title(title);

        Pie pie = new Pie();
        pie.setData(seriesData.toArray());
        String[] radiusArr = new String[]{"20%", "40%"};
        pie.setRadius(radiusArr);
        String[] centerArr = new String[]{"50%", "55%"};
        pie.setCenter(centerArr);
        pie.setStartAngle("45");
        Map<String, Object> itemMap = new HashMap<>();
        Map<String, Object> normalMap = new HashMap<>();
        Map<String, Object> labelMap = new HashMap<>();
        Map<String, Object> labelLineMap = new HashMap<>();
        labelMap.put("formatter", "{b}\n{d}%/{c}");
        labelMap.put("fontSize", "20");
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
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("hbflcsszbzqs").toString());
        title.setText("当月销售类型占比");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        title.setSubtextStyle(textMap);
        option.title(title);

        Pie pie = new Pie();
        pie.data(seriesData.toArray());
        String[] radiusArr = new String[]{"20%", "40%"};
        pie.setRadius(radiusArr);
        String[] centerArr = new String[]{"50%", "50%"};
        pie.setCenter(centerArr);
        pie.setStartAngle("45");
        pie.setName("个数");
        Map<String, Object> itemMap = new HashMap<>();
        Map<String, Object> normalMap = new HashMap<>();
        Map<String, Object> labelMap = new HashMap<>();
        Map<String, Object> labelLineMap = new HashMap<>();
        labelMap.put("formatter", "{b}\n{d}%/{c}");
        labelLineMap.put("show", "true");
        labelMap.put("fontSize", "20");
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
     *
     * @return
     */
    public void ptywzb_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getPtywzbByQuarter");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("tj", "mon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("zq", "7");
        paramMap.add("jsrqend", jsrqend);
//        paramMap.add("lrsjStart", jsrqstart.substring(0, 7));
//        paramMap.add("lrsjEnd", jsrqend.substring(0, 7));
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
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("ptywzbtjzqs").toString());
        title.setText("平台业务占比");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        title.setSubtextStyle(textMap);
        option.title(title);

        Pie pie = new Pie();
        pie.setData(seriesData.toArray());
        String[] radiusArr = new String[]{"20%", "40%"};
        pie.setRadius(radiusArr);
        String[] centerArr = new String[]{"50%", "55%"};
        pie.setCenter(centerArr);
        pie.setStartAngle("45");
        pie.setName("个数");
        Map<String, Object> itemMap = new HashMap<>();
        Map<String, Object> normalMap = new HashMap<>();
        Map<String, Object> labelMap = new HashMap<>();
        Map<String, Object> labelLineMap = new HashMap<>();
        labelMap.put("formatter", "{b}\n{d}%/{c}");
        labelMap.put("fontSize", "20");
        labelLineMap.put("show", "true");
        normalMap.put("labelLine", labelLineMap);
        normalMap.put("label", labelMap);
        itemMap.put("normal", normalMap);
        pie.setItemStyle(itemMap);
        option.addSeries(pie);
        Map<String, Object> itemMap1 = new HashMap<>();
        Map<String, Object> textStyleMap = new HashMap<>();
        textStyleMap.put("fontSize", "20");
        itemMap1.put("textStyle", textStyleMap);
        pie.setLabel(itemMap1);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "20");
        jsonList.add(jsonObject.toJSONString());
    }

    /**
     * 检测项目产品占比
     */
    public void jcxmcpzb_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getCpywzbByQuarter");
        paramMap.add("yxxs", "mNGS项目,Resfirst,tNGS");
        paramMap.add("tj", "mon");
        paramMap.add("zq", "7");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
//        paramMap.add("lrsjStart", jsrqstart.substring(0, 7));
//        paramMap.add("lrsjEnd", jsrqend.substring(0, 7));
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("cpywzbtj");
        List<Map<String, Object>> seriesData = new ArrayList<>();
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        for (Object o : jsonArray) {
            Map<String, Object> dataMap = new HashMap<>();
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
            dataMap.put("value", job.get("sl"));
            dataMap.put("name", job.get("jcxmmc"));
            seriesData.add(dataMap);
        }
        Option option = new Option();
        Title title = new Title();
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("cpywzbtjzqs").toString());
        title.setText("检测项目产品占比");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        title.setSubtextStyle(textMap);
        option.title(title);

        Pie pie = new Pie();
        pie.setData(seriesData.toArray());
        String[] radiusArr = new String[]{"20%", "40%"};
        pie.setRadius(radiusArr);
        String[] centerArr = new String[]{"50%", "55%"};
        pie.setCenter(centerArr);
        pie.setStartAngle("45");
        Map<String, Object> itemMap = new HashMap<>();
        Map<String, Object> normalMap = new HashMap<>();
        Map<String, Object> labelMap = new HashMap<>();
        Map<String, Object> labelLineMap = new HashMap<>();
        labelMap.put("formatter", "{b}\n{d}%/{c}");
        labelMap.put("fontSize", "20");
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
     * 特检销售发展部直销top15月度
     *
     * @return
     */
    public void tj_topzx15_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getTopZx20ByMon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("ptgss", "A9E2D46E8AB147769983D73F8E629242");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statictis/getweeklymodel_conditionByJsrq", paramMap, Map.class);
        Object cpqstj = map.get("topZx20");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataAxis = new ArrayList<>();
        List<String> dataseries = new ArrayList<>();
        int len = 9;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        int sum = 0;
        int max = 0;
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
            sum = sum + Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            if (Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString())>max){
                max = Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            }

        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        title.setSubtextStyle(textMap);
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("topZx20").toString());
        title.setText("特检销售发展部直销top10");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
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
        xAxis.setMax(max*1.3);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("10");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(249,174,0)");
        axisLine_y.put("lineStyle", colorMap_y);
        Map<String, Object> text_y = new HashMap<>();
        text_y.put("fontSize", "18");
        Map<String, Object> label_y = new HashMap<>();
        label_y.put("textStyle",text_y);
        yAxis.setAxisLabel(label_y);
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
        textStyle.put("fontSize", "16");
        label.put("textStyle", textStyle);
        label.put("fontSize", "16");
        label.put("show", "true");
        label.put("position", "right");
        label.put("formatter", "function (params) {" +
                "var percent =params.data+'(' +Number((params.data / " + sum + ") * 100).toFixed(2) + '%'+')';" +
                "return percent;" +
                "}function");
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
     * 特检销售发展部COStop15月度
     *
     * @return
     */
    public void tj_topcos15_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getTopCSO20ByMon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("ptgss", "A9E2D46E8AB147769983D73F8E629242");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statictis/getweeklymodel_conditionByJsrq", paramMap, Map.class);
        Object cpqstj = map.get("topCSO20");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataAxis = new ArrayList<>();
        List<String> dataseries = new ArrayList<>();
        int len = 9;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        int sum = 0;
        int max = 0;
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
            sum = sum + Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            if (Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString())>max){
                max = Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            }

        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("topCSO20").toString());
        title.setText("特检销售发展部COStop10");
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
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
        xAxis.setMax(max*1.3);
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "rgb(209,95,126)");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("10");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(209,95,126)");
        axisLine_y.put("lineStyle", colorMap_y);
        Map<String, Object> text_y = new HashMap<>();
        text_y.put("fontSize", "18");
        Map<String, Object> label_y = new HashMap<>();
        label_y.put("textStyle",text_y);
        yAxis.setAxisLabel(label_y);
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
        textStyle.put("fontSize", "18");
        label.put("textStyle", textStyle);
        label.put("fontSize", "18");
        label.put("show", "true");
        label.put("position", "right");
        label.put("formatter", "function (params) {" +
                "var percent =params.data+'(' +Number((params.data / " + sum + ") * 100).toFixed(2) + '%'+')';" +
                "return percent;" +
                "}function");
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
     * 汇杰top15月度
     *
     * @return
     */
    public void hj_topzx15_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getTopZx20ByMon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("ptgss", "982C29C1A8294DD29815D73984FF8D45");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statictis/getweeklymodel_conditionByJsrq", paramMap, Map.class);
        Object cpqstj = map.get("topZx20");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataAxis = new ArrayList<>();
        List<String> dataseries = new ArrayList<>();
        int len = 9;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        int sum = 0;
        int max = 0;
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
            sum = sum + Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            if (Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString())>max){
                max = Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            }

        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        title.setSubtextStyle(textMap);
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("topZx20").toString());
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
        title.setText("汇杰直销top10");
        option.title(title);
        Grid grid = new Grid();
        grid.setTop(90);
        grid.setRight("4%");
        grid.setLeft("3%");
        grid.setBottom("3%");
        grid.setContainLabel("true");
        option.grid(grid);
        XAxis xAxis = new XAxis();
        xAxis.setMax(max*1.3);
        xAxis.setType("value");
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "rgb(249,174,0)");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("10");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(249,174,0)");
        axisLine_y.put("lineStyle", colorMap_y);
        Map<String, Object> text_y = new HashMap<>();
        text_y.put("fontSize", "18");
        Map<String, Object> label_y = new HashMap<>();
        label_y.put("textStyle",text_y);
        yAxis.setAxisLabel(label_y);
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
        textStyle.put("fontSize", "16");
        label.put("textStyle", textStyle);
        label.put("fontSize", "16");
        label.put("show", "true");
        label.put("position", "right");
        label.put("formatter", "function (params) {" +
                "var percent =params.data+'(' +Number((params.data / " + sum + ") * 100).toFixed(2) + '%'+')';" +
                "return percent;" +
                "}function");
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
     * 汇杰COStop15月度
     *
     * @return
     */
    public void hj_topcos15_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getTopCSO20ByMon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("ptgss", "982C29C1A8294DD29815D73984FF8D45");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statictis/getweeklymodel_conditionByJsrq", paramMap, Map.class);
        Object cpqstj = map.get("topCSO20");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataAxis = new ArrayList<>();
        List<String> dataseries = new ArrayList<>();
        int len = 9;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        int sum = 0;
        int max = 0;
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
            sum = sum + Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            if (Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString())>max){
                max = Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            }

        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("topCSO20").toString());
        title.setText("汇杰COStop10");
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
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
        xAxis.setMax(max*1.3);
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "rgb(209,95,126)");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("10");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(209,95,126)");
        axisLine_y.put("lineStyle", colorMap_y);
        Map<String, Object> text_y = new HashMap<>();
        text_y.put("fontSize", "18");
        Map<String, Object> label_y = new HashMap<>();
        label_y.put("textStyle",text_y);
        yAxis.setAxisLabel(label_y);
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
        textStyle.put("fontSize", "16");
        label.put("fontSize", "16");
        label.put("textStyle", textStyle);
        label.put("show", "true");
        label.put("position", "right");
        label.put("formatter", "function (params) {" +
                "var percent =params.data+'(' +Number((params.data / " + sum + ") * 100).toFixed(2) + '%'+')';" +
                "return percent;" +
                "}function");
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
     * 聚毅top15月度
     *
     * @return
     */
    public void jy_topzx15_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getTopZx20ByMon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("ptgss", "A0ED520A47324C51ABC8B09EDB287E52");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statictis/getweeklymodel_conditionByJsrq", paramMap, Map.class);
        Object cpqstj = map.get("topZx20");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataAxis = new ArrayList<>();
        List<String> dataseries = new ArrayList<>();
        int len = 9;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        int sum = 0;
        int max = 0;
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
            sum = sum + Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            if (Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString())>max){
                max = Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            }


        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
        title.setSubtextStyle(textMap);
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("topZx20").toString());
        title.setText("聚毅直销top10");
        option.title(title);
        Grid grid = new Grid();
        grid.setTop(90);
        grid.setRight("4%");
        grid.setLeft("3%");
        grid.setBottom("3%");
        grid.setContainLabel("true");
        option.grid(grid);
        XAxis xAxis = new XAxis();
        xAxis.setMax(max*1.3);
        xAxis.setType("value");
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "rgb(249,174,0)");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("10");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(249,174,0)");
        axisLine_y.put("lineStyle", colorMap_y);
        Map<String, Object> text_y = new HashMap<>();
        text_y.put("fontSize", "18");
        Map<String, Object> label_y = new HashMap<>();
        label_y.put("textStyle",text_y);
        yAxis.setAxisLabel(label_y);
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
        textStyle.put("fontSize", "16");
        label.put("textStyle", textStyle);
        label.put("fontSize", "16");
        label.put("show", "true");
        label.put("position", "right");
        label.put("formatter", "function (params) {" +
                "var percent =params.data+'(' +Number((params.data / " + sum + ") * 100).toFixed(2) + '%'+')';" +
                "return percent;" +
                "}function");
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
     * 聚毅COStop15月度
     *
     * @return
     */
    public void jy_topcos15_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getTopCSO20ByMon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("ptgss", "A0ED520A47324C51ABC8B09EDB287E52");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statictis/getweeklymodel_conditionByJsrq", paramMap, Map.class);
        Object cpqstj = map.get("topCSO20");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataAxis = new ArrayList<>();
        List<String> dataseries = new ArrayList<>();
        int len = 9;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        int sum = 0;
        int max = 0;
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
            sum = sum + Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            if (Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString())>max){
                max = Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            }

        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("topCSO20").toString());
        title.setText("聚毅COStop10");
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
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
        xAxis.setMax(max*1.3);
        xAxis.setType("value");
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "rgb(209,95,126)");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("10");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(209,95,126)");
        axisLine_y.put("lineStyle", colorMap_y);
        Map<String, Object> text_y = new HashMap<>();
        text_y.put("fontSize", "18");
        Map<String, Object> label_y = new HashMap<>();
        label_y.put("textStyle",text_y);
        yAxis.setAxisLabel(label_y);
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
        textStyle.put("fontSize", "16");
        label.put("fontSize", "16");
        label.put("textStyle", textStyle);
        label.put("show", "true");
        label.put("position", "right");
        label.put("formatter", "function (params) {" +
                "var percent =params.data+'(' +Number((params.data / " + sum + ") * 100).toFixed(2) + '%'+')';" +
                "return percent;" +
                "}function");
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
     * 毅芯COStop15月度
     *
     * @return
     */
    public void yx_topcos15_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getTopCSO20ByMon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("ptgss", "A886EED5C0BE4EA3B8DFE393FFC6BAC5");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statictis/getweeklymodel_conditionByJsrq", paramMap, Map.class);
        Object cpqstj = map.get("topCSO20");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataAxis = new ArrayList<>();
        List<String> dataseries = new ArrayList<>();
        int len = 9;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        int sum = 0;
        int max = 0;
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
            sum = sum + Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            if (Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString())>max){
                max = Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            }

        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("topCSO20").toString());
        title.setText("毅芯COStop10");
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
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
        xAxis.setMax(max*1.3);
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "rgb(209,95,126)");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("10");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(209,95,126)");
        axisLine_y.put("lineStyle", colorMap_y);
        Map<String, Object> text_y = new HashMap<>();
        text_y.put("fontSize", "18");
        Map<String, Object> label_y = new HashMap<>();
        label_y.put("textStyle",text_y);
        yAxis.setAxisLabel(label_y);
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
        textStyle.put("fontSize", "16");
        label.put("textStyle", textStyle);
        label.put("fontSize", "16");
        label.put("show", "true");
        label.put("position", "right");
        label.put("formatter", "function (params) {" +
                "var percent =params.data+'(' +Number((params.data / " + sum + ") * 100).toFixed(2) + '%'+')';" +
                "return percent;" +
                "}function");
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
     * 毅芯top15月度
     *
     * @return
     */
    public void yx_topzx15_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getTopZx20ByMon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("ptgss", "A886EED5C0BE4EA3B8DFE393FFC6BAC5");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statictis/getweeklymodel_conditionByJsrq", paramMap, Map.class);
        Object cpqstj = map.get("topZx20");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataAxis = new ArrayList<>();
        List<String> dataseries = new ArrayList<>();
        int len = 9;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        int sum = 0;
        int max = 0;
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
            sum = sum + Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            if (Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString())>max){
                max = Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            }

        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
        title.setSubtextStyle(textMap);
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("topZx20").toString());
        title.setText("毅芯直销top10");
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
        xAxis.setMax(max*1.3);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("10");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(249,174,0)");
        axisLine_y.put("lineStyle", colorMap_y);
        Map<String, Object> text_y = new HashMap<>();
        text_y.put("fontSize", "18");
        Map<String, Object> label_y = new HashMap<>();
        label_y.put("textStyle",text_y);
        yAxis.setAxisLabel(label_y);
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
        textStyle.put("fontSize", "16");
        label.put("fontSize", "16");
        label.put("textStyle", textStyle);
        label.put("show", "true");
        label.put("position", "right");
        label.put("formatter", "function (params) {" +
                "var percent =params.data+'(' +Number((params.data / " + sum + ") * 100).toFixed(2) + '%'+')';" +
                "return percent;" +
                "}function");
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
     * 生命指数COStop15月度
     *
     * @return
     */
    public void smzs_topcos15_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getTopCSO20ByMon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("ptgss", "ECA280FE36DF4041A9640871449E2251");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statictis/getweeklymodel_conditionByJsrq", paramMap, Map.class);
        Object cpqstj = map.get("topCSO20");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataAxis = new ArrayList<>();
        List<String> dataseries = new ArrayList<>();
        int len = 9;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        int sum = 0;
        int max = 0;
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
            sum = sum + Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            if (Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString())>max){
                max = Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            }

        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("topCSO20").toString());
        title.setText("生命指数COStop10");
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
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
        xAxis.setMax(max*1.3);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("10");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(209,95,126)");
        axisLine_y.put("lineStyle", colorMap_y);
        Map<String, Object> text_y = new HashMap<>();
        text_y.put("fontSize", "18");
        Map<String, Object> label_y = new HashMap<>();
        label_y.put("textStyle",text_y);
        yAxis.setAxisLabel(label_y);
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
        textStyle.put("fontSize", "16");
        label.put("fontSize", "16");
        label.put("textStyle", textStyle);
        label.put("show", "true");
        label.put("position", "right");
        label.put("formatter", "function (params) {" +
                "var percent =params.data+'(' +Number((params.data / " + sum + ") * 100).toFixed(2) + '%'+')';" +
                "return percent;" +
                "}function");
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
     * 生命指数top15月度
     *
     * @return
     */
    public void smszs_topzx15_mon() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getTopZx20ByMon");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("ptgss", "ECA280FE36DF4041A9640871449E2251");
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statictis/getweeklymodel_conditionByJsrq", paramMap, Map.class);
        Object cpqstj = map.get("topZx20");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        List<String> dataAxis = new ArrayList<>();
        List<String> dataseries = new ArrayList<>();
        int len = 9;
        if (len >= jsonArray.size()) {
            len = jsonArray.size() - 1;
        }
        int sum = 0;
        int max = 0;
        for (int i = len; i >= 0; i--) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(jsonArray.get(i)));
            dataAxis.add(job.get("hbmc") == null ? "" : job.get("hbmc").toString());
            dataseries.add(job.get("sfnum") == null ? "" : job.get("sfnum").toString());
            sum = sum + Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            if (Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString())>max){
                max = Integer.parseInt(job.get("sfnum") == null ? "0" : job.get("sfnum").toString());
            }

        }
        Option option = new Option();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        Map<String, Object> axisPointerMap = new HashMap<>();
        axisPointerMap.put("type", "shadow");
        tooltip.setAxisPointer(axisPointerMap);
        option.tooltip(tooltip);
        Title title = new Title();
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("color", "black");
        textMap.put("fontSize", "24");
        textMap.put("fontWeight", "bold");
        Map<String, Object> subMap = new HashMap<>();
        subMap.put("fontSize", "24");
        title.setTextStyle(subMap);
        title.setSubtextStyle(textMap);
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        title.setSubtext(zqsjob.get("topZx20").toString());
        title.setText("生命指数直销top10");
        option.title(title);
        Grid grid = new Grid();
        grid.setTop(90);
        grid.setRight("4%");
        grid.setLeft("3%");
        grid.setBottom("3%");
        grid.setContainLabel("true");
        option.grid(grid);
        XAxis xAxis = new XAxis();
        xAxis.setMax(max*1.3);
        xAxis.setType("value");
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> colorMap = new HashMap<>();
        colorMap.put("color", "rgb(249,174,0)");
        axisLine.put("lineStyle", colorMap);
        xAxis.setAxisLine(axisLine);
        option.xAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("category");
        yAxis.setOffset("10");
        yAxis.setData(dataAxis.toArray());
        Map<String, Object> axisLine_y = new HashMap<>();
        Map<String, Object> colorMap_y = new HashMap<>();
        colorMap_y.put("color", "rgb(249,174,0)");
        axisLine_y.put("lineStyle", colorMap_y);
        Map<String, Object> text_y = new HashMap<>();
        text_y.put("fontSize", "18");
        Map<String, Object> label_y = new HashMap<>();
        label_y.put("textStyle",text_y);
        yAxis.setAxisLabel(label_y);
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
        textStyle.put("fontSize", "16");
        label.put("fontSize", "16");
        label.put("textStyle", textStyle);
        label.put("show", "true");
        label.put("position", "right");
        label.put("formatter", "function (params) {" +
                "var percent =params.data+'(' +Number((params.data / " + sum + ") * 100).toFixed(2) + '%'+')';" +
                "return percent;" +
                "}function");
        bar.setLabel(label);
        option.addSeries(bar);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(option));
        JSONObject titleObj = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("title")));
        titleObj.put("x", "left");
        titleObj.put("y", "20");
        jsonObject.put("title", titleObj);
        jsonList.add(jsonObject.toJSONString());
    }

    public StringBuffer tableStr1(){
        String str1="<w:tbl>\n" +
                "                        <w:tblPr>\n" +
                "                            <w:tblStyle w:val=\"3\"/>\n" +
                "                            <w:tblpPr w:leftFromText=\"180\" w:rightFromText=\"180\" w:vertAnchor=\"text\" w:horzAnchor=\"page\" w:tblpX=\"755\" w:tblpY=\"202\"/>\n" +
                "                            <w:tblOverlap w:val=\"never\"/>\n" +
                "                            <w:tblW w:w=\"1543\" w:type=\"pct\"/>\n" +
                "                            <w:tblInd w:w=\"0\" w:type=\"dxa\"/>\n" +
                "                            <w:tblBorders>\n" +
                "                                <w:top w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                <w:left w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                <w:bottom w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                <w:right w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                <w:insideH w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                <w:insideV w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                            </w:tblBorders>\n" +
                "                            <w:tblLayout w:type=\"autofit\"/>\n" +
                "                            <w:tblCellMar>\n" +
                "                                <w:top w:w=\"0\" w:type=\"dxa\"/>\n" +
                "                                <w:left w:w=\"10\" w:type=\"dxa\"/>\n" +
                "                                <w:bottom w:w=\"0\" w:type=\"dxa\"/>\n" +
                "                                <w:right w:w=\"10\" w:type=\"dxa\"/>\n" +
                "                            </w:tblCellMar>\n" +
                "                        </w:tblPr>\n" +
                "                        <w:tblGrid>\n" +
                "                            <w:gridCol w:w=\"1158\"/>\n" +
                "                            <w:gridCol w:w=\"966\"/>\n" +
                "                            <w:gridCol w:w=\"563\"/>\n" +
                "                            <w:gridCol w:w=\"549\"/>\n" +
                "                        </w:tblGrid>\n" +
                "                        <w:tr>\n" +
                "                            <w:tblPrEx>\n" +
                "                                <w:tblBorders>\n" +
                "                                    <w:top w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:left w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:bottom w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:right w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:insideH w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:insideV w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                </w:tblBorders>\n" +
                "                                <w:tblCellMar>\n" +
                "                                    <w:top w:w=\"0\" w:type=\"dxa\"/>\n" +
                "                                    <w:left w:w=\"10\" w:type=\"dxa\"/>\n" +
                "                                    <w:bottom w:w=\"0\" w:type=\"dxa\"/>\n" +
                "                                    <w:right w:w=\"10\" w:type=\"dxa\"/>\n" +
                "                                </w:tblCellMar>\n" +
                "                            </w:tblPrEx>";
        String str2="<w:tc>\n" +
                "                                <w:tcPr>\n" +
                "                                    <w:tcW w:w=\"1789\" w:type=\"pct\"/>\n" +
                "                                    <w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"409EFF\"/>\n" +
                "                                </w:tcPr>\n" +
                "                                <w:p>\n" +
                "                                    <w:pPr>\n" +
                "                                       <w:jc w:val=\"center\"/>"+
                "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>\n<w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                    </w:pPr>\n" +
                "                                    <w:r>\n" +
                "                                        <w:rPr>\n" +
                "                                           <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                        <w:t>平台</w:t>\n" +
                "                                    </w:r>\n" +
                "                                </w:p>\n" +
                "                            </w:tc>\n" +
                "                            <w:tc>\n" +
                "                                <w:tcPr>\n" +
                "                                    <w:tcW w:w=\"1492\" w:type=\"pct\"/>\n" +
                "                                    <w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"409EFF\"/>\n" +
                "                                </w:tcPr>\n" +
                "                                <w:p>\n" +
                "                                    <w:pPr>\n" +
                "                                       <w:jc w:val=\"center\"/>"+
                "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>\n<w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                    </w:pPr>\n" +
                "                                    <w:r>\n" +
                "                                        <w:rPr>\n" +
                "                                            <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                        <w:t>m+t/R/目标</w:t>\n" +
                "                                    </w:r>\n" +
                "                                </w:p>\n" +
                "                            </w:tc>\n" +
                "                            <w:tc>\n" +
                "                                <w:tcPr>\n" +
                "                                    <w:tcW w:w=\"869\" w:type=\"pct\"/>\n" +
                "                                    <w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"409EFF\"/>\n" +
                "                                </w:tcPr>\n" +
                "                                <w:p>\n" +
                "                                    <w:pPr>\n" +
                "                                       <w:jc w:val=\"center\"/>"+
                "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                    </w:pPr>\n" +
                "                                    <w:r>\n" +
                "                                        <w:rPr>\n" +
                "                                           <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                        <w:t>达成率</w:t>\n" +
                "                                    </w:r>\n" +
                "                                </w:p>\n" +
                "                            </w:tc>\n" +
                "                            <w:tc>\n" +
                "                                <w:tcPr>\n" +
                "                                    <w:tcW w:w=\"848\" w:type=\"pct\"/>\n" +
                "                                    <w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"409EFF\"/>\n" +
                "                                </w:tcPr>\n" +
                "                                <w:p>\n" +
                "                                    <w:pPr>\n" +
                "                                       <w:jc w:val=\"center\"/>"+
                "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                    </w:pPr>\n" +
                "                                    <w:r>\n" +
                "                                        <w:rPr>\n" +
                "                                           <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                        <w:t>GAP</w:t>\n" +
                "                                    </w:r>\n" +
                "                                </w:p>\n" +
                "                            </w:tc></w:tr>";

        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(str1).append(str2);
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getPtzbdclByYear");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("tj", "year");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        rq1=zqsjob.get("ptzbdcl").toString();
        Object cpqstj = map.get("ptzbdcl");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        String str3="<w:tr>\n" +
                "                            <w:tblPrEx>\n" +
                "                                <w:tblBorders>\n" +
                "                                    <w:top w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:left w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:bottom w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:right w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:insideH w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:insideV w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                </w:tblBorders>\n" +
                "                            </w:tblPrEx>";

        for (Object o : jsonArray) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
            stringBuffer.append(str3);
            String str = "<w:tc>\n" +
                    "                                <w:tcPr>\n" +
                    "                                    <w:tcW w:w=\"1789\" w:type=\"pct\"/>\n" +
                    "                                </w:tcPr>\n" +
                    "                                <w:p>\n" +
                    "                                    <w:pPr>\n<w:jc w:val=\"center\"/>" +
                    "                                        <w:rPr>\n <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                    </w:pPr>\n" +
                    "                                    <w:r>\n" +
                    "                                        <w:rPr>\n" +
                    "                                             <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                        <w:t>" + job.get("ptmc") + "</w:t>\n" +
                    "                                    </w:r>\n" +
                    "                                </w:p>\n" +
                    "                            </w:tc>\n" +
                    "                            <w:tc>\n" +
                    "                                <w:tcPr>\n" +
                    "                                    <w:tcW w:w=\"1492\" w:type=\"pct\"/>\n" +
                    "                                </w:tcPr>\n" +
                    "                                <w:p>\n" +
                    "                                    <w:pPr>\n<w:jc w:val=\"center\"/>" +
                    "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                    </w:pPr>\n" +
                    "                                    <w:r>\n" +
                    "                                        <w:rPr>\n" +
                    "                                            <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                        <w:t>" + job.get("sumnum") + "/" + job.get("rnum") + "/" + job.get("sz") + "</w:t>\n" +
                    "                                    </w:r>\n" +
                    "                                </w:p>\n" +
                    "                            </w:tc>\n" +
                    "                            <w:tc>\n" +
                    "                                <w:tcPr>\n" +
                    "                                    <w:tcW w:w=\"869\" w:type=\"pct\"/>\n" +
                    "                                </w:tcPr>\n" +
                    "                                <w:p>\n" +
                    "                                    <w:pPr>\n<w:jc w:val=\"center\"/>" +
                    "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                    </w:pPr>\n" +
                    "                                    <w:r>\n" +
                    "                                        <w:rPr>\n" +
                    "                                           <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                        <w:t>" + job.get("dcl") + "</w:t>\n" +
                    "                                    </w:r>\n" +
                    "                                </w:p>\n" +
                    "                            </w:tc>\n" +
                    "                            <w:tc>\n" +
                    "                                <w:tcPr>\n" +
                    "                                    <w:tcW w:w=\"848\" w:type=\"pct\"/>\n" +
                    "                                </w:tcPr>\n" +
                    "                                <w:p>\n" +
                    "                                    <w:pPr>\n<w:jc w:val=\"center\"/>" +
                    "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                    </w:pPr>\n" +
                    "                                    <w:r>\n" +
                    "                                        <w:rPr>\n" +
                    "                                           <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                        <w:t>" + job.get("gap") + "</w:t>\n" +
                    "                                    </w:r>\n" +
                    "                                </w:p>\n" +
                    "                            </w:tc></w:tr>";
            stringBuffer.append(str);
        }
        stringBuffer.append("</w:tbl>");
        return stringBuffer;
    }
    public StringBuffer tableStr2(){
        String str1="<w:tbl>\n" +
                "                        <w:tblPr>\n" +
                "                            <w:tblStyle w:val=\"3\"/>\n" +
                "                            <w:tblpPr w:leftFromText=\"180\" w:rightFromText=\"180\" w:vertAnchor=\"text\" w:horzAnchor=\"page\" w:tblpX=\"4318\" w:tblpY=\"200\"/>\n" +
                "                            <w:tblOverlap w:val=\"never\"/>\n" +
                "                            <w:tblW w:w=\"1514\" w:type=\"pct\"/>\n" +
                "                            <w:tblInd w:w=\"0\" w:type=\"dxa\"/>\n" +
                "                            <w:tblBorders>\n" +
                "                                <w:top w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                <w:left w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                <w:bottom w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                <w:right w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                <w:insideH w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                <w:insideV w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                            </w:tblBorders>\n" +
                "                            <w:tblLayout w:type=\"autofit\"/>\n" +
                "                            <w:tblCellMar>\n" +
                "                                <w:top w:w=\"0\" w:type=\"dxa\"/>\n" +
                "                                <w:left w:w=\"10\" w:type=\"dxa\"/>\n" +
                "                                <w:bottom w:w=\"0\" w:type=\"dxa\"/>\n" +
                "                                <w:right w:w=\"10\" w:type=\"dxa\"/>\n" +
                "                            </w:tblCellMar>\n" +
                "                        </w:tblPr>\n" +
                "                        <w:tblGrid>\n" +
                "                            <w:gridCol w:w=\"1023\"/>\n" +
                "                            <w:gridCol w:w=\"1078\"/>\n" +
                "                            <w:gridCol w:w=\"528\"/>\n" +
                "                            <w:gridCol w:w=\"546\"/>\n" +
                "                        </w:tblGrid>\n" +
                "                        <w:tr>\n" +
                "                            <w:tblPrEx>\n" +
                "                                <w:tblBorders>\n" +
                "                                    <w:top w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:left w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:bottom w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:right w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:insideH w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:insideV w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                </w:tblBorders>\n" +
                "                                <w:tblCellMar>\n" +
                "                                    <w:top w:w=\"0\" w:type=\"dxa\"/>\n" +
                "                                    <w:left w:w=\"10\" w:type=\"dxa\"/>\n" +
                "                                    <w:bottom w:w=\"0\" w:type=\"dxa\"/>\n" +
                "                                    <w:right w:w=\"10\" w:type=\"dxa\"/>\n" +
                "                                </w:tblCellMar>\n" +
                "                            </w:tblPrEx>";
        String str2="<w:tc>\n" +
                "                                <w:tcPr>\n" +
                "                                    <w:tcW w:w=\"1612\" w:type=\"pct\"/>\n" +
                "                                    <w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"409EFF\"/>\n" +
                "                                </w:tcPr>\n" +
                "                                <w:p>\n" +
                "                                    <w:pPr>\n" +
                "                                       <w:jc w:val=\"center\"/>"+
                "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>\n<w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                    </w:pPr>\n" +
                "                                    <w:r>\n" +
                "                                        <w:rPr>\n" +
                "                                            <w:rFonts w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>\n<w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                        <w:t>平台</w:t>\n" +
                "                                    </w:r>\n" +
                "                                </w:p>\n" +
                "                            </w:tc>\n" +
                "                            <w:tc>\n" +
                "                                <w:tcPr>\n" +
                "                                    <w:tcW w:w=\"1697\" w:type=\"pct\"/>\n" +
                "                                    <w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"409EFF\"/>\n" +
                "                                </w:tcPr>\n" +
                "                                <w:p>\n" +
                "                                    <w:pPr>\n" +
                "                                       <w:jc w:val=\"center\"/>"+
                "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                    </w:pPr>\n" +
                "                                    <w:r>\n" +
                "                                        <w:rPr>\n" +
                "                                            <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                        <w:t>m+t/R/目标</w:t>\n" +
                "                                    </w:r>\n" +
                "                                </w:p>\n" +
                "                            </w:tc>\n" +
                "                            <w:tc>\n" +
                "                                <w:tcPr>\n" +
                "                                    <w:tcW w:w=\"831\" w:type=\"pct\"/>\n" +
                "                                    <w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"409EFF\"/>\n" +
                "                                </w:tcPr>\n" +
                "                                <w:p>\n" +
                "                                    <w:pPr>\n" +
                "                                       <w:jc w:val=\"center\"/>"+
                "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                    </w:pPr>\n" +
                "                                    <w:r>\n" +
                "                                        <w:rPr>\n" +
                "                                           <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                        <w:t>达成率</w:t>\n" +
                "                                    </w:r>\n" +
                "                                </w:p>\n" +
                "                            </w:tc>\n" +
                "                            <w:tc>\n" +
                "                                <w:tcPr>\n" +
                "                                    <w:tcW w:w=\"859\" w:type=\"pct\"/>\n" +
                "                                    <w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"409EFF\"/>\n" +
                "                                </w:tcPr>\n" +
                "                                <w:p>\n" +
                "                                    <w:pPr>\n" +
                "                                       <w:jc w:val=\"center\"/>"+
                "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                    </w:pPr>\n" +
                "                                    <w:r>\n" +
                "                                        <w:rPr>\n" +
                "                                            <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                        <w:t>GAP</w:t>\n" +
                "                                    </w:r>\n" +
                "                                </w:p>\n" +
                "                            </w:tc></w:tr>";

        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(str1).append(str2);
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getPtzbdclByQuarter");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("tj", "year");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("ptzbdcl");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        rq2=zqsjob.get("ptzbdcl").toString();
        String str3="<w:tr>\n" +
                "                            <w:tblPrEx>\n" +
                "                                <w:tblBorders>\n" +
                "                                    <w:top w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:left w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:bottom w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:right w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:insideH w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:insideV w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                </w:tblBorders>\n" +
                "                            </w:tblPrEx>";

        for (Object o : jsonArray) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
            stringBuffer.append(str3);
            String str = "<w:tc>\n" +
                    "                                <w:tcPr>\n" +
                    "                                    <w:tcW w:w=\"1789\" w:type=\"pct\"/>\n" +
                    "                                </w:tcPr>\n" +
                    "                                <w:p>\n" +
                    "                                    <w:pPr>\n<w:jc w:val=\"center\"/>" +
                    "                                        <w:rPr>\n <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                    </w:pPr>\n" +
                    "                                    <w:r>\n" +
                    "                                        <w:rPr>\n" +
                    "                                             <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                        <w:t>" + job.get("ptmc") + "</w:t>\n" +
                    "                                    </w:r>\n" +
                    "                                </w:p>\n" +
                    "                            </w:tc>\n" +
                    "                            <w:tc>\n" +
                    "                                <w:tcPr>\n" +
                    "                                    <w:tcW w:w=\"1492\" w:type=\"pct\"/>\n" +
                    "                                </w:tcPr>\n" +
                    "                                <w:p>\n" +
                    "                                    <w:pPr>\n<w:jc w:val=\"center\"/>" +
                    "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                    </w:pPr>\n" +
                    "                                    <w:r>\n" +
                    "                                        <w:rPr>\n" +
                    "                                            <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                         <w:t>" + job.get("sumnum") + "/" + job.get("rnum") + "/" + job.get("sz") + "</w:t>\n" +
                    "                                    </w:r>\n" +
                    "                                </w:p>\n" +
                    "                            </w:tc>\n" +
                    "                            <w:tc>\n" +
                    "                                <w:tcPr>\n" +
                    "                                    <w:tcW w:w=\"869\" w:type=\"pct\"/>\n" +
                    "                                </w:tcPr>\n" +
                    "                                <w:p>\n" +
                    "                                    <w:pPr>\n<w:jc w:val=\"center\"/>" +
                    "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                    </w:pPr>\n" +
                    "                                    <w:r>\n" +
                    "                                        <w:rPr>\n" +
                    "                                           <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                        <w:t>" + job.get("dcl") + "</w:t>\n" +
                    "                                    </w:r>\n" +
                    "                                </w:p>\n" +
                    "                            </w:tc>\n" +
                    "                            <w:tc>\n" +
                    "                                <w:tcPr>\n" +
                    "                                    <w:tcW w:w=\"848\" w:type=\"pct\"/>\n" +
                    "                                </w:tcPr>\n" +
                    "                                <w:p>\n" +
                    "                                    <w:pPr>\n<w:jc w:val=\"center\"/>" +
                    "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                    </w:pPr>\n" +
                    "                                    <w:r>\n" +
                    "                                        <w:rPr>\n" +
                    "                                           <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                        <w:t>" + job.get("gap") + "</w:t>\n" +
                    "                                    </w:r>\n" +
                    "                                </w:p>\n" +
                    "                            </w:tc></w:tr>";
            stringBuffer.append(str);
        }
        stringBuffer.append("</w:tbl>");
        return stringBuffer;
    }
    public StringBuffer tableStr3(){
        String str1="<w:tbl>\n" +
                "                        <w:tblPr>\n" +
                "                            <w:tblStyle w:val=\"3\"/>\n" +
                "                            <w:tblpPr w:leftFromText=\"180\" w:rightFromText=\"180\" w:vertAnchor=\"text\" w:horzAnchor=\"page\" w:tblpX=\"7866\" w:tblpY=\"193\"/>\n" +
                "                            <w:tblOverlap w:val=\"never\"/>\n" +
                "                            <w:tblW w:w=\"1468\" w:type=\"pct\"/>\n" +
                "                            <w:tblInd w:w=\"0\" w:type=\"dxa\"/>\n" +
                "                            <w:tblBorders>\n" +
                "                                <w:top w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                <w:left w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                <w:bottom w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                <w:right w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                <w:insideH w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                <w:insideV w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                            </w:tblBorders>\n" +
                "                            <w:tblLayout w:type=\"autofit\"/>\n" +
                "                            <w:tblCellMar>\n" +
                "                                <w:top w:w=\"0\" w:type=\"dxa\"/>\n" +
                "                                <w:left w:w=\"10\" w:type=\"dxa\"/>\n" +
                "                                <w:bottom w:w=\"0\" w:type=\"dxa\"/>\n" +
                "                                <w:right w:w=\"10\" w:type=\"dxa\"/>\n" +
                "                            </w:tblCellMar>\n" +
                "                        </w:tblPr>\n" +
                "                        <w:tblGrid>\n" +
                "                            <w:gridCol w:w=\"1025\"/>\n" +
                "                            <w:gridCol w:w=\"1027\"/>\n" +
                "                            <w:gridCol w:w=\"509\"/>\n" +
                "                            <w:gridCol w:w=\"518\"/>\n" +
                "                        </w:tblGrid>\n" +
                "                        <w:tr>\n" +
                "                            <w:tblPrEx>\n" +
                "                                <w:tblBorders>\n" +
                "                                    <w:top w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:left w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:bottom w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:right w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:insideH w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:insideV w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                </w:tblBorders>\n" +
                "                                <w:tblCellMar>\n" +
                "                                    <w:top w:w=\"0\" w:type=\"dxa\"/>\n" +
                "                                    <w:left w:w=\"10\" w:type=\"dxa\"/>\n" +
                "                                    <w:bottom w:w=\"0\" w:type=\"dxa\"/>\n" +
                "                                    <w:right w:w=\"10\" w:type=\"dxa\"/>\n" +
                "                                </w:tblCellMar>\n" +
                "                            </w:tblPrEx>";
        String str2="<w:tc>\n" +
                "                                <w:tcPr>\n" +
                "                                    <w:tcW w:w=\"1664\" w:type=\"pct\"/>\n" +
                "                                    <w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"409EFF\"/>\n" +
                "                                </w:tcPr>\n" +
                "                                <w:p>\n" +
                "                                    <w:pPr>\n" +
                "                                       <w:jc w:val=\"center\"/>"+
                "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>\n<w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                    </w:pPr>\n" +
                "                                    <w:r>\n" +
                "                                        <w:rPr>\n" +
                "                                            <w:rFonts w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>\n<w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                        <w:t>平台</w:t>\n" +
                "                                    </w:r>\n" +
                "                                </w:p>\n" +
                "                            </w:tc>\n" +
                "                            <w:tc>\n" +
                "                                <w:tcPr>\n" +
                "                                    <w:tcW w:w=\"1667\" w:type=\"pct\"/>\n" +
                "                                    <w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"409EFF\"/>\n" +
                "                                </w:tcPr>\n" +
                "                                <w:p>\n" +
                "                                    <w:pPr>\n" +
                "                                       <w:jc w:val=\"center\"/>"+
                "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                    </w:pPr>\n" +
                "                                    <w:r>\n" +
                "                                        <w:rPr>\n" +
                "                                            <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                        <w:t>m+t/R/目标</w:t>\n" +
                "                                    </w:r>\n" +
                "                                </w:p>\n" +
                "                            </w:tc>\n" +
                "                            <w:tc>\n" +
                "                                <w:tcPr>\n" +
                "                                    <w:tcW w:w=\"826\" w:type=\"pct\"/>\n" +
                "                                    <w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"409EFF\"/>\n" +
                "                                </w:tcPr>\n" +
                "                                <w:p>\n" +
                "                                    <w:pPr>\n" +
                "                                       <w:jc w:val=\"center\"/>"+
                "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                    </w:pPr>\n" +
                "                                    <w:r>\n" +
                "                                        <w:rPr>\n" +
                "                                           <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                        <w:t>达成率</w:t>\n" +
                "                                    </w:r>\n" +
                "                                </w:p>\n" +
                "                            </w:tc>\n" +
                "                            <w:tc>\n" +
                "                                <w:tcPr>\n" +
                "                                    <w:tcW w:w=\"841\" w:type=\"pct\"/>\n" +
                "                                    <w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"409EFF\"/>\n" +
                "                                </w:tcPr>\n" +
                "                                <w:p>\n" +
                "                                    <w:pPr>\n" +
                "                                       <w:jc w:val=\"center\"/>"+
                "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                    </w:pPr>\n" +
                "                                    <w:r>\n" +
                "                                        <w:rPr>\n" +
                "                                            <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/><w:b/>\n" +
                "                                            <w:bCs/>" +
                "                                            <w:sz w:val=\"13\"/>\n" +
                "                                            <w:szCs w:val=\"13\"/>\n" +
                "                                        </w:rPr>\n" +
                "                                        <w:t>GAP</w:t>\n" +
                "                                    </w:r>\n" +
                "                                </w:p>\n" +
                "                            </w:tc></w:tr>";

        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(str1).append(str2);
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        RestTemplate t_restTemplate = new RestTemplate();
        paramMap.add("method", "getPtzbdclByMon");
        paramMap.add("yxxs", "mNGS项目,tNGS");
        paramMap.add("tj", "year");
        paramMap.add("jsrqstart", jsrqstart);
        paramMap.add("jsrqend", jsrqend);
        Map map = t_restTemplate.postForObject(applicationurl + "/ws/statistics/getSjxxStatisByTjAndJsrq", paramMap, Map.class);
        Object cpqstj = map.get("ptzbdcl");
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(cpqstj));
        Object searchData = map.get("searchData");
        JSONObject zqs=JSONObject.parseObject(JSON.toJSONString(searchData));
        JSONObject zqsjob=JSONObject.parseObject(JSON.toJSONString(zqs.get("zqs")));
        rq3=zqsjob.get("ptzbdcl").toString();
        String str3="<w:tr>\n" +
                "                            <w:tblPrEx>\n" +
                "                                <w:tblBorders>\n" +
                "                                    <w:top w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:left w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:bottom w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:right w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:insideH w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                    <w:insideV w:val=\"single\" w:color=\"auto\" w:sz=\"0\" w:space=\"0\"/>\n" +
                "                                </w:tblBorders>\n" +
                "                            </w:tblPrEx>";

        for (Object o : jsonArray) {
            JSONObject job = JSONObject.parseObject(JSON.toJSONString(o));
            stringBuffer.append(str3);
            String str = "<w:tc>\n" +
                    "                                <w:tcPr>\n" +
                    "                                    <w:tcW w:w=\"1789\" w:type=\"pct\"/>\n" +
                    "                                </w:tcPr>\n" +
                    "                                <w:p>\n" +
                    "                                    <w:pPr>\n<w:jc w:val=\"center\"/>" +
                    "                                        <w:rPr>\n <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                    </w:pPr>\n" +
                    "                                    <w:r>\n" +
                    "                                        <w:rPr>\n" +
                    "                                             <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                        <w:t>" + job.get("ptmc") + "</w:t>\n" +
                    "                                    </w:r>\n" +
                    "                                </w:p>\n" +
                    "                            </w:tc>\n" +
                    "                            <w:tc>\n" +
                    "                                <w:tcPr>\n" +
                    "                                    <w:tcW w:w=\"1492\" w:type=\"pct\"/>\n" +
                    "                                </w:tcPr>\n" +
                    "                                <w:p>\n" +
                    "                                    <w:pPr>\n<w:jc w:val=\"center\"/>" +
                    "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                    </w:pPr>\n" +
                    "                                    <w:r>\n" +
                    "                                        <w:rPr>\n" +
                    "                                            <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                         <w:t>" + job.get("sumnum") + "/" + job.get("rnum") + "/" + job.get("sz") + "</w:t>\n" +
                    "                                    </w:r>\n" +
                    "                                </w:p>\n" +
                    "                            </w:tc>\n" +
                    "                            <w:tc>\n" +
                    "                                <w:tcPr>\n" +
                    "                                    <w:tcW w:w=\"869\" w:type=\"pct\"/>\n" +
                    "                                </w:tcPr>\n" +
                    "                                <w:p>\n" +
                    "                                    <w:pPr>\n<w:jc w:val=\"center\"/>" +
                    "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                    </w:pPr>\n" +
                    "                                    <w:r>\n" +
                    "                                        <w:rPr>\n" +
                    "                                           <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                        <w:t>" + job.get("dcl") + "</w:t>\n" +
                    "                                    </w:r>\n" +
                    "                                </w:p>\n" +
                    "                            </w:tc>\n" +
                    "                            <w:tc>\n" +
                    "                                <w:tcPr>\n" +
                    "                                    <w:tcW w:w=\"848\" w:type=\"pct\"/>\n" +
                    "                                </w:tcPr>\n" +
                    "                                <w:p>\n" +
                    "                                    <w:pPr>\n<w:jc w:val=\"center\"/>" +
                    "                                        <w:rPr>\n<w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                    </w:pPr>\n" +
                    "                                    <w:r>\n" +
                    "                                        <w:rPr>\n" +
                    "                                           <w:rFonts w:hint=\"eastAsia\" w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:eastAsia=\"宋体\" w:cs=\"宋体\"/>" +
                    "                                            <w:sz w:val=\"13\"/>\n" +
                    "                                            <w:szCs w:val=\"13\"/>\n" +
                    "                                        </w:rPr>\n" +
                    "                                        <w:t>" + job.get("gap") + "</w:t>\n" +
                    "                                    </w:r>\n" +
                    "                                </w:p>\n" +
                    "                            </w:tc></w:tr>";
            stringBuffer.append(str);
        }
        stringBuffer.append("</w:tbl>");
        return stringBuffer;
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

    /**
     * 上传Word文件
     * @return
     */
    private boolean sendWordFile(String fileName,String FTP_URL) {
        //连接服务器
        //FTPClient ftp=FtpUtil.connect(FTPWORD_PATH, FTP_URL, FTP_PORT, FTP_USER, FTP_PD );
        //上传到服务器word下边的文件夹
        //FtpUtil.upload(new File(wjljjm), ftp);
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
            return false;
        }
    }
}

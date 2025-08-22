package com.matridx.igams.bioinintegration.server.controller;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.bioinformation.dao.entities.ReceiveFileModel;
import com.matridx.igams.bioinformation.service.svcinterface.ICnvFileService;
import com.matridx.igams.bioinformation.service.svcinterface.IMngsFileParsingService;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.springboot.util.base.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ws")
public class BioRabbitWsController extends BaseController {
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

    private final Logger log = LoggerFactory.getLogger(BioRabbitWsController.class);
    /**
     * 接收文件并发送Rabbit
     */
    @RequestMapping("/bioreceive/pagedataFileSave")
    @ResponseBody
    public Map<String, Object> fileSave(ReceiveFileModel receiveFileModel) {
        log.error("/bioreceive/pagedataFileSave 开始:Program:" + receiveFileModel.getProgram());
        Map<String, Object> map = new HashMap<>();
        if((receiveFileModel.getReport()!=null&&StringUtil.isBlank(receiveFileModel.getProgram()))||(receiveFileModel.getMt_result_file()!=null&&StringUtil.isBlank(receiveFileModel.getProgram()))){
            return map;
        }
        boolean flag=true;
        String datesdf = "";
        String s_chipDate = "";
        String releaseFilePath ="";
        if(StringUtil.isNotBlank(receiveFileModel.getChip())){
            String t_chipdata = receiveFileModel.getChip();
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
                s_chipDate = simpleDateFormat.format(d_chipdate);
            }catch (Exception e){
                flag=false;
            }
            if(!flag){
                log.error(receiveFileModel.getChip()+"芯片名不符合规范");
                return map;
            }

            String date = s_chipDate;
            releaseFilePath = releasePath +"/20"+date.substring(0,2)+"/"+date.substring(2,4)+"/"+date.substring(4,6)+"/"+receiveFileModel.getChip();
        }
        if(StringUtil.isNotBlank(receiveFileModel.getSample())){
            String[] strings = receiveFileModel.getSample().split("-");
            String string = strings[strings.length - 1];
            String date = mngsFileParsingService.findDate(strings, string, strings.length - 1);
            releaseFilePath = releasePath +"/"+date.substring(0,4)+"/"+date.substring(4,6)+"/"+date.substring(6)+"/"+receiveFileModel.getChip()+"/"+receiveFileModel.getSample()+"/"+receiveFileModel.getProgram();
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
        MultipartFile card = receiveFileModel.getCard_stat();
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
        amqpTempl.convertAndSend("sys.igams", preRabbitFlg+"sys.igams.document.parse"+rabbitFlg, JSON.toJSONString(receiveFileModel));
        return map;
    }
}

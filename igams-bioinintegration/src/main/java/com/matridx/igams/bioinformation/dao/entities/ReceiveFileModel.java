package com.matridx.igams.bioinformation.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

@Alias(value="ReceiveFileModel")
public class ReceiveFileModel extends BaseModel {
    //版本号
    private String program;
    //文库编号
    private String sample;
    //芯片编号
    private String chip;
    //qc结果文件
    private MultipartFile qc;
    //性别结果文件
    private MultipartFile sex;
    //ONCO  AI结果文件
    private MultipartFile model_predict;
    //CNV结果文件
    private MultipartFile result;
    //final report final.report
    private MultipartFile report;
    //result mtresult.json
    private MultipartFile mt_result_file;
    //<uid>.out
    private MultipartFile doctor_ai;
    //耐药检测结果统计
    private MultipartFile card_stat;
    private MultipartFile card;
    //毒力报告结果
    private MultipartFile vfdb_stat;
    private MultipartFile vfdb;
    //coverage.json
    private MultipartFile coverage;
    //bg.json
    private MultipartFile bg;

    private MultipartFile fx;
    //qc结果文件
    private String qcPath;
    //性别结果文件
    private String sexPath;
    //ONCO  AI结果文件
    private String model_predictPath;
    //CNV结果文件
    private String resultPath;
    //final report final.report
    private String reportPath;
    //result mtresult.json
    private String mt_result_filePath;
    //<uid>.out
    private String doctor_aiPath;
    //耐药检测结果统计
    private String card_statPath;
    private String card_Path;
    //毒力报告结果
    private String vfdb_statPath;
    private String vfdb_Path;
    //coverage.json
    private String coveragePath;
    //dash.json
    private String dashPath;
    //dash.json
    private MultipartFile dash;
    //cnv.json
    private MultipartFile cnv;
    //cnv.json
    private String cnvPath;
    //bg.json
    private String bgPath;

    private String fxPath;

    public MultipartFile getCnv() {
        return cnv;
    }

    public void setCnv(MultipartFile cnv) {
        this.cnv = cnv;
    }

    public String getCnvPath() {
        return cnvPath;
    }

    public void setCnvPath(String cnvPath) {
        this.cnvPath = cnvPath;
    }

    public String getDashPath() {
        return dashPath;
    }

    public void setDashPath(String dashPath) {
        this.dashPath = dashPath;
    }

    public MultipartFile getDash() {
        return dash;
    }

    public void setDash(MultipartFile dash) {
        this.dash = dash;
    }

    public String getQcPath() {
        return qcPath;
    }

    public void setQcPath(String qcPath) {
        this.qcPath = qcPath;
    }

    public String getSexPath() {
        return sexPath;
    }

    public void setSexPath(String sexPath) {
        this.sexPath = sexPath;
    }

    public String getModel_predictPath() {
        return model_predictPath;
    }

    public void setModel_predictPath(String model_predictPath) {
        this.model_predictPath = model_predictPath;
    }

    public String getResultPath() {
        return resultPath;
    }

    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public String getMt_result_filePath() {
        return mt_result_filePath;
    }

    public void setMt_result_filePath(String mt_result_filePath) {
        this.mt_result_filePath = mt_result_filePath;
    }

    public String getDoctor_aiPath() {
        return doctor_aiPath;
    }

    public void setDoctor_aiPath(String doctor_aiPath) {
        this.doctor_aiPath = doctor_aiPath;
    }

    public String getCard_statPath() {
        return card_statPath;
    }

    public void setCard_statPath(String card_statPath) {
        this.card_statPath = card_statPath;
    }

    public String getVfdb_statPath() {
        return vfdb_statPath;
    }

    public void setVfdb_statPath(String vfdb_statPath) {
        this.vfdb_statPath = vfdb_statPath;
    }

    public String getCoveragePath() {
        return coveragePath;
    }

    public void setCoveragePath(String coveragePath) {
        this.coveragePath = coveragePath;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getChip() {
        return chip;
    }

    public void setChip(String chip) {
        this.chip = chip;
    }

    public MultipartFile getQc() {
        return qc;
    }

    public void setQc(MultipartFile qc) {
        this.qc = qc;
    }

    public MultipartFile getSex() {
        return sex;
    }

    public void setSex(MultipartFile sex) {
        this.sex = sex;
    }

    public MultipartFile getModel_predict() {
        return model_predict;
    }

    public void setModel_predict(MultipartFile model_predict) {
        this.model_predict = model_predict;
    }

    public MultipartFile getResult() {
        return result;
    }

    public void setResult(MultipartFile result) {
        this.result = result;
    }

    public MultipartFile getReport() {
        return report;
    }

    public void setReport(MultipartFile report) {
        this.report = report;
    }

    public MultipartFile getMt_result_file() {
        return mt_result_file;
    }

    public void setMt_result_file(MultipartFile mt_result_file) {
        this.mt_result_file = mt_result_file;
    }

    public MultipartFile getDoctor_ai() {
        return doctor_ai;
    }

    public void setDoctor_ai(MultipartFile doctor_ai) {
        this.doctor_ai = doctor_ai;
    }

    public MultipartFile getCard_stat() {
        return card_stat;
    }

    public void setCard_stat(MultipartFile card_stat) {
        this.card_stat = card_stat;
    }

    public MultipartFile getVfdb_stat() {
        return vfdb_stat;
    }

    public void setVfdb_stat(MultipartFile vfdb_stat) {
        this.vfdb_stat = vfdb_stat;
    }

    public MultipartFile getCoverage() {
        return coverage;
    }

    public void setCoverage(MultipartFile coverage) {
        this.coverage = coverage;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public MultipartFile getVfdb() {
        return vfdb;
    }

    public void setVfdb(MultipartFile vfdb) {
        this.vfdb = vfdb;
    }

    public MultipartFile getCard() {
        return card;
    }

    public void setCard(MultipartFile card) {
        this.card = card;
    }

    public String getVfdb_Path() {
        return vfdb_Path;
    }

    public void setVfdb_Path(String vfdb_Path) {
        this.vfdb_Path = vfdb_Path;
    }

    public String getCard_Path() {
        return card_Path;
    }

    public void setCard_Path(String card_Path) {
        this.card_Path = card_Path;
    }

    public String getBgPath() {
        return bgPath;
    }

    public void setBgPath(String bgPath) {
        this.bgPath = bgPath;
    }

    public MultipartFile getBg() {
        return bg;
    }

    public void setBg(MultipartFile bg) {
        this.bg = bg;
    }

    public String getFxPath() {
        return fxPath;
    }

    public void setFxPath(String fxPath) {
        this.fxPath = fxPath;
    }

    public MultipartFile getFx() {
        return fx;
    }

    public void setFx(MultipartFile fx) {
        this.fx = fx;
    }
}

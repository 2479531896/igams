package com.matridx.crf.web.service.impl;


import com.matridx.crf.web.dao.entities.*;
import com.matridx.crf.web.dao.post.NdzxxDao;
import com.matridx.crf.web.service.svcinterface.INdzxxService;
import com.matridx.igams.common.dao.BaseModel;

import com.matridx.igams.common.dao.entities.*;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NdzxxServiceImpl extends BaseBasicServiceImpl<NdzxxDto, NdzxxModel, NdzxxDao> implements INdzxxService, IFileImport {

    @Autowired
    RedisUtil redisUtil;
//    @Autowired
//    INdzxxService ndzxxService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    IJcsjService jcsjService;

    private Logger log = LoggerFactory.getLogger(NdzxxServiceImpl.class);
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, NdzxxDto ndzxxDto)
    {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuffer sqlParam = new StringBuffer();
        for (DcszDto dcszDto : choseList)
        {
            if(dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if(StringUtil.isNotBlank(dcszDto.getSqlzd())){
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        ndzxxDto.setSqlParam(sqlParam.toString());
    }

    /**
     * 选中导出
     *
     * @param params
     * @return
     */
    public List<NdzxxDto> getListForSelectExp(Map<String, Object> params)
    {
        NdzxxDto ndzxxDto = (NdzxxDto) params.get("entryData");
        queryJoinFlagExport(params, ndzxxDto);
        List<NdzxxDto> list = dao.getListForSelectExp(ndzxxDto);
        for(NdzxxDto ndzxxDto_t:list){
            String jwhbzs = ndzxxDto_t.getJwhbz();
            if(jwhbzs!=null) {
                String str[] = jwhbzs.split(",");
                String str_jwhbz = "";
                for (int i = 0; i < str.length; i++) {
                    str[i] = getJwhbzCsmc(str[i]);
                    if (i == str.length - 1) {
                        str_jwhbz += str[i];
                    } else {
                        str_jwhbz += str[i] + ",";
                    }
                }
                ndzxxDto_t.setJwhbz(str_jwhbz);
            }
            String grbws = ndzxxDto_t.getGrbw();
            if(grbws!=null) {
                String[] grbw = grbws.split(",");
                String str_grbw = "";
                for (int i = 0; i < grbw.length; i++) {
                    grbw[i] = getGrbwCsmc(grbw[i]);
                    if (i == grbw.length - 1) {
                        str_grbw += grbw[i];
                    } else {
                        str_grbw += grbw[i] + ",";
                    }
                }
                ndzxxDto_t.setGrbw(str_grbw);
            }
            //性别转义
            if(ndzxxDto_t.getXb()!=null) {
                if (ndzxxDto_t.getXb().equals("1")) {
                    ndzxxDto_t.setXb("男");
                } else if (ndzxxDto_t.getXb().equals("2")) {
                    ndzxxDto_t.setXb("女");
                } else {
                    ndzxxDto_t.setXb("未知");
                }
            }
            //抗菌药物暴露史转义
            if(ndzxxDto_t.getKjywbls()!=null) {
                if (ndzxxDto_t.getKjywbls().equals("0")) {
                    ndzxxDto_t.setKjywbls("前30天");
                } else if (ndzxxDto_t.getKjywbls().equals("1")) {
                    ndzxxDto_t.setKjywbls("前60天");
                } else if (ndzxxDto_t.getKjywbls().equals("2")) {
                    ndzxxDto_t.setKjywbls("前90天");
                } else {
                    ndzxxDto_t.setKjywbls("无");
                }
            }
            //实施抗菌药物降阶梯治疗转义
            if(ndzxxDto_t.getKjywjjtzl()!=null) {
                if (ndzxxDto_t.getKjywjjtzl().equals("1")) {
                    ndzxxDto_t.setKjywjjtzl("是");
                } else {
                    ndzxxDto_t.setKjywjjtzl("否");
                }
            }
            //机械通气转义
            if(ndzxxDto_t.getJxtq()!=null) {
                if (ndzxxDto_t.getJxtq().equals("1")) {
                    ndzxxDto_t.setJxtq("是");
                } else {
                    ndzxxDto_t.setJxtq("否");
                }
            }
            //CRRT转义
            if(ndzxxDto_t.getCrrt()!=null) {
                if (ndzxxDto_t.getCrrt().equals("1")) {
                    ndzxxDto_t.setCrrt("是");
                } else {
                    ndzxxDto_t.setCrrt("否");
                }
            }
            //血管活性药转义
            if(ndzxxDto_t.getXghxy()!=null) {
                if (ndzxxDto_t.getXghxy().equals("1")) {
                    ndzxxDto_t.setXghxy("是");
                } else {
                    ndzxxDto_t.setXghxy("否");
                }
            }
            //送检前是否使用抗菌药物转义
            if(ndzxxDto_t.getSjqsfsykjyw()!=null) {
                if (ndzxxDto_t.getSjqsfsykjyw().equals("1")) {
                    ndzxxDto_t.setSjqsfsykjyw("是");
                } else {
                    ndzxxDto_t.setSjqsfsykjyw("否");
                }
            }
            //mcfdna转义
            if(ndzxxDto_t.getMcfdna()!=null) {
                if(ndzxxDto_t.getMcfdna().equals("1")){
                    ndzxxDto_t.setMcfdna("是");
                }else{
                    ndzxxDto_t.setMcfdna("否");
                }
            }
            //血培养转义
            if(ndzxxDto_t.getXpy()!=null) {
                if (ndzxxDto_t.getXpy().equals("1")) {
                    ndzxxDto_t.setXpy("是");
                } else {
                    ndzxxDto_t.setXpy("否");
                }
            }
            //痰涂片转义
            if(ndzxxDto_t.getTtp()!=null) {
                if (ndzxxDto_t.getTtp().equals("1")) {
                    ndzxxDto_t.setTtp("是");
                } else {
                    ndzxxDto_t.setTtp("否");
                }
            }
            //痰培养转义
            if(ndzxxDto_t.getTpy()!=null) {
                if (ndzxxDto_t.getTpy().equals("1")) {
                    ndzxxDto_t.setTpy("是");
                } else {
                    ndzxxDto_t.setTpy("否");
                }
            }
            //腹水培养转义
            if(ndzxxDto_t.getFspy()!=null) {
                if (ndzxxDto_t.getFspy().equals("1")) {
                    ndzxxDto_t.setFspy("是");
                } else {
                    ndzxxDto_t.setFspy("否");
                }
            }
            //腹水涂片转义
            if(ndzxxDto_t.getFstp()!=null) {
                if (ndzxxDto_t.getFstp().equals("1")) {
                    ndzxxDto_t.setFstp("是");
                } else {
                    ndzxxDto_t.setFstp("否");
                }
            }
            //其它1转义
            if(ndzxxDto_t.getQtf()!=null) {
                if (ndzxxDto_t.getQtf().equals("1")) {
                    ndzxxDto_t.setQtf("是");
                } else {
                    ndzxxDto_t.setQtf("否");
                }
            }
            //其它2转义
            if(ndzxxDto_t.getQtt()!=null) {
                if (ndzxxDto_t.getQtt().equals("1")) {
                    ndzxxDto_t.setQtt("是");
                } else {
                    ndzxxDto_t.setQtt("否");
                }
            }
        }
        return list;
    }

    /**
     * 根据搜索条件获取导出条数
     * @param ndzxxDto
     * @return
     */
    public int getCountForSearchExp(NdzxxDto ndzxxDto,Map<String,Object> params){
        return dao.getCountForSearchExp(ndzxxDto);
    }

    /**
     * 既往合并症导出转义
     * @param jwhbz
     * @return
     */
    public String getJwhbzCsmc(String jwhbz){
        return dao.getJwhbzCsmc(jwhbz);
    }
    /**
     * 感染部位导出转义
     * @param grbw
     * @return
     */
    public String getGrbwCsmc(String grbw){
        return dao.getGrbwCsmc(grbw);
    }
    /**
     * 根据搜索条件分页获取导出信息
     * @param params
     * @return
     */
    public List<NdzxxDto> getListForSearchExp(Map<String,Object> params){
        NdzxxDto ndzxxDto = (NdzxxDto)params.get("entryData");
        queryJoinFlagExport(params,ndzxxDto);
        List<NdzxxDto> list = dao.getListForSearchExp(ndzxxDto);
        for(NdzxxDto ndzxxDto_t:list){
            String jwhbzs = ndzxxDto_t.getJwhbz();
            if(jwhbzs!=null) {
                String str[] = jwhbzs.split(",");
                String str_jwhbz = "";
                for (int i = 0; i < str.length; i++) {
                    str[i] = getJwhbzCsmc(str[i]);
                    if (i == str.length - 1) {
                        str_jwhbz += str[i];
                    } else {
                        str_jwhbz += str[i] + ",";
                    }
                }
                ndzxxDto_t.setJwhbz(str_jwhbz);
            }
            String grbws = ndzxxDto_t.getGrbw();
            if(grbws!=null) {
                String[] grbw = grbws.split(",");
                String str_grbw = "";
                for (int i = 0; i < grbw.length; i++) {
                    grbw[i] = getGrbwCsmc(grbw[i]);
                    if (i == grbw.length - 1) {
                        str_grbw += grbw[i];
                    } else {
                        str_grbw += grbw[i] + ",";
                    }
                }
                ndzxxDto_t.setGrbw(str_grbw);
            }
            //性别转义
            if(ndzxxDto_t.getXb()!=null) {
                if (ndzxxDto_t.getXb().equals("1")) {
                    ndzxxDto_t.setXb("男");
                } else if (ndzxxDto_t.getXb().equals("2")) {
                    ndzxxDto_t.setXb("女");
                } else {
                    ndzxxDto_t.setXb("未知");
                }
            }
            //抗菌药物暴露史转义
            if(ndzxxDto_t.getKjywbls()!=null) {
                if (ndzxxDto_t.getKjywbls().equals("0")) {
                    ndzxxDto_t.setKjywbls("前30天");
                } else if (ndzxxDto_t.getKjywbls().equals("1")) {
                    ndzxxDto_t.setKjywbls("前60天");
                } else if (ndzxxDto_t.getKjywbls().equals("2")) {
                    ndzxxDto_t.setKjywbls("前90天");
                } else {
                    ndzxxDto_t.setKjywbls("无");
                }
            }
            //实施抗菌药物降阶梯治疗转义
            if(ndzxxDto_t.getKjywjjtzl()!=null) {
                if (ndzxxDto_t.getKjywjjtzl().equals("1")) {
                    ndzxxDto_t.setKjywjjtzl("是");
                } else {
                    ndzxxDto_t.setKjywjjtzl("否");
                }
            }
            //机械通气转义
            if(ndzxxDto_t.getJxtq()!=null) {
                if (ndzxxDto_t.getJxtq().equals("1")) {
                    ndzxxDto_t.setJxtq("是");
                } else {
                    ndzxxDto_t.setJxtq("否");
                }
            }
            //CRRT转义
            if(ndzxxDto_t.getCrrt()!=null) {
                if (ndzxxDto_t.getCrrt().equals("1")) {
                    ndzxxDto_t.setCrrt("是");
                } else {
                    ndzxxDto_t.setCrrt("否");
                }
            }
            //血管活性药转义
            if(ndzxxDto_t.getXghxy()!=null) {
                if (ndzxxDto_t.getXghxy().equals("1")) {
                    ndzxxDto_t.setXghxy("是");
                } else {
                    ndzxxDto_t.setXghxy("否");
                }
            }
            //送检前是否使用抗菌药物转义
            if(ndzxxDto_t.getSjqsfsykjyw()!=null) {
                if (ndzxxDto_t.getSjqsfsykjyw().equals("1")) {
                    ndzxxDto_t.setSjqsfsykjyw("是");
                } else {
                    ndzxxDto_t.setSjqsfsykjyw("否");
                }
            }
            //mcfdna转义
            if(ndzxxDto_t.getMcfdna()!=null) {
                if(ndzxxDto_t.getMcfdna().equals("1")){
                    ndzxxDto_t.setMcfdna("是");
                }else{
                    ndzxxDto_t.setMcfdna("否");
                }
            }
            //血培养转义
            if(ndzxxDto_t.getXpy()!=null) {
                if (ndzxxDto_t.getXpy().equals("1")) {
                    ndzxxDto_t.setXpy("是");
                } else {
                    ndzxxDto_t.setXpy("否");
                }
            }
            //痰涂片转义
            if(ndzxxDto_t.getTtp()!=null) {
                if (ndzxxDto_t.getTtp().equals("1")) {
                    ndzxxDto_t.setTtp("是");
                } else {
                    ndzxxDto_t.setTtp("否");
                }
            }
            //痰培养转义
            if(ndzxxDto_t.getTpy()!=null) {
                if (ndzxxDto_t.getTpy().equals("1")) {
                    ndzxxDto_t.setTpy("是");
                } else {
                    ndzxxDto_t.setTpy("否");
                }
            }
            //腹水培养转义
            if(ndzxxDto_t.getFspy()!=null) {
                if (ndzxxDto_t.getFspy().equals("1")) {
                    ndzxxDto_t.setFspy("是");
                } else {
                    ndzxxDto_t.setFspy("否");
                }
            }
            //腹水涂片转义
            if(ndzxxDto_t.getFstp()!=null) {
                if (ndzxxDto_t.getFstp().equals("1")) {
                    ndzxxDto_t.setFstp("是");
                } else {
                    ndzxxDto_t.setFstp("否");
                }
            }
            //其它1转义
            if(ndzxxDto_t.getQtf()!=null) {
                if (ndzxxDto_t.getQtf().equals("1")) {
                    ndzxxDto_t.setQtf("是");
                } else {
                    ndzxxDto_t.setQtf("否");
                }
            }
            //其它2转义
            if(ndzxxDto_t.getQtt()!=null) {
                if (ndzxxDto_t.getQtt().equals("1")) {
                    ndzxxDto_t.setQtt("是");
                } else {
                    ndzxxDto_t.setQtt("否");
                }
            }
        }
        return list;
    }

    /**
     * 获取上传的文件信息
     * @param ndzxxDto
     * @return
     */
    public FjcfbDto getFjcfb(NdzxxDto ndzxxDto){
        return  dao.getFjcfb(ndzxxDto);
    }

    /**
     * 检验结果导入保存
     * @param ndzxxDto
     * @return
     * @throws BusinessException
     */
    public boolean uploadNdzSave(NdzxxDto ndzxxDto) throws BusinessException{
        if(ndzxxDto.getFjids()!=null && ndzxxDto.getFjids().size() > 0){
            for (int i = 0; i < ndzxxDto.getFjids().size(); i++) {
                boolean saveFile = fjcfbService.save2RealFile(ndzxxDto.getFjids().get(i),ndzxxDto.getNdzjlid());
                if(!saveFile)
                    throw new BusinessException("msg","附件保存失败!");
            }
        }
        return true;
    }
    /**
     * 患者信息
     *
     * @param ndzxxDto
     * @return
     */
    public NdzxxDto getHzxx(NdzxxDto ndzxxDto) {
        NdzxxDto ndzxxDto1 = dao.getHzxx(ndzxxDto);
        return ndzxxDto1;
    }

    /**
     * 记录信息
     *
     * @param ndzxxDto
     * @return
     */
    public NdzxxDto getJlxx(NdzxxDto ndzxxDto) {
        NdzxxDto ndzxxDto1 = dao.getJlxx(ndzxxDto);
        return ndzxxDto1;
    }
    /**
     * 动脉血气
     * @param ndzxxDto
     * @return
     */
    public List<NdzdmxqDto> getDmxq(NdzxxDto ndzxxDto){
        return dao.getDmxq(ndzxxDto);
    }
    /**
     * 炎症指标
     * @param ndzxxDto
     * @return
     */
    public List<NdzyzzbDto> getYzzb(NdzxxDto ndzxxDto){
        return dao.getYzzb(ndzxxDto);
    }
    /**
     * 生化
     * @param ndzxxDto
     * @return
     */
    public List<NdzshDto> getSh(NdzxxDto ndzxxDto){
        return dao.getSh(ndzxxDto);
    }
    /**
     * 血常规
     * @param ndzxxDto
     * @return
     */
    public List<NdzxcgDto> getXcg(NdzxxDto ndzxxDto){
        return dao.getXcg(ndzxxDto);

    }
    /**
     * 新增患者信息
     *
     * @param ndzxxDto
     * @return
     */
    public boolean insertHzxx(NdzxxDto ndzxxDto) {
        return dao.insertHzxx(ndzxxDto);
    }

    /**
     * 新增报告信息
     *
     * @param ndzxxDto
     * @return
     */
    public boolean insertReport(NdzxxDto ndzxxDto) {
        return dao.insertReport(ndzxxDto);
    }

    /**
     * 判断插入的数据是否重复
     *
     * @param ndzxxDto
     * @return
     */
    public NdzxxDto getReportRepeat(NdzxxDto ndzxxDto) {
        return dao.getReportRepeat(ndzxxDto);
    }

    /**
     * 判断插入的患者信息是否重复
     *
     * @param ndzxxDto
     * @return
     */
    public NdzxxDto getHzxxRepeat(NdzxxDto ndzxxDto) {
        return dao.getHzxxRepeat(ndzxxDto);
    }

    /**
     * 判断插入的记录信息是否重复
     *
     * @param ndzxxDto
     * @return
     */
    public NdzxxDto getJlxxRepeat(NdzxxDto ndzxxDto) {
        return dao.getJlxxRepeat(ndzxxDto);
    }
    /**
     * 获取医院的参数id
     * @param ndzxxDto
     * @return
     */
    public String getYyCsid(NdzxxDto ndzxxDto){
        return dao.getYyCsid(ndzxxDto);
    }

    /**
     * 获取就诊科室的参数id
     * @param ndzxxDto
     * @return
     */
    public String getJzksCsid(NdzxxDto ndzxxDto){
        return dao.getJzksCsid(ndzxxDto);
    }

    /**
     * 获取病人类别的参数id
     * @param ndzxxDto
     * @return
     */
    public String getBrlbCsid(NdzxxDto ndzxxDto){
        return dao.getBrlbCsid(ndzxxDto);
    }

    /**
     * 获取既往合并症的参数id
     * @param jwhbz
     * @return
     */
    public String getJwhbzCsid(String jwhbz){
        return dao.getJwhbzCsid(jwhbz);
    }

    /**
     * 获取感染部位的参数id
     * @param grbw
     * @return
     */
    public String getGrbwCsid(String grbw){
        return dao.getGrbwCsid(grbw);
    }

    /**
     * 获取出院状态的参数id
     * @param ndzxxDto
     * @return
     */
    public String getCyztCsid(NdzxxDto ndzxxDto){
        return dao.getCyztCsid(ndzxxDto);
    }
    /**
     * 获取出ICU状态的参数id
     * @param ndzxxDto
     * @return
     */
    public String getCicuztCsid(NdzxxDto ndzxxDto){
        return dao.getCicuztCsid(ndzxxDto);
    }
    /**
     * 动脉血气参数信息
     * @return
     */
    public List<JcsjDto> getDmxqJcsj(){
        return dao.getDmxqJcsj();
    }
    /**
     * 炎症指标参数信息
     * @return
     */
    public List<JcsjDto> getYzzbJcsj(){
        return dao.getYzzbJcsj();
    }
    /**
     * 生化参数信息
     * @return
     */
    public List<JcsjDto> getShJcsj(){
        return dao.getShJcsj();
    }
    /**
     * 血常规参数信息
     * @return
     */
    public List<JcsjDto> getXcgJcsj(){
        return dao.getXcgJcsj();
    }
    /**
     * 新增动脉血气
     * @param ndzdmxqDto
     * @return
     */
    public boolean insertDmxq(NdzdmxqDto ndzdmxqDto){
        return dao.insertDmxq(ndzdmxqDto);
    }
    /**
     * 新增炎症指标
     * @param ndzyzzbDto
     * @return
     */
    public boolean insertYzzb(NdzyzzbDto ndzyzzbDto){
        return dao.insertYzzb(ndzyzzbDto);
    }
    /**
     * 新增生化
     * @param ndzshDto
     * @return
     */
    public boolean insertSh(NdzshDto ndzshDto){
        return dao.insertSh(ndzshDto);
    }
    /**
     * 新增血常规
     * @param ndzxcgDto
     * @return
     */
    public boolean insertXcg(NdzxcgDto ndzxcgDto){
        return dao.insertXcg(ndzxcgDto);
    }
    /**
     * 所属医院统计
     * @param ndzxxDto
     * @return
     */
    public List<NdzxxDto> getPagedSsyy(NdzxxDto ndzxxDto){
        return dao.getPagedSsyy(ndzxxDto);
    }

    public Map<String,Object> getnryjbh(NdzxxDto ndzxxDto){
        return dao.getnryjbh(ndzxxDto);
    }

    @Override
    public boolean existCheck(String fieldName, String value) {
        return false;
    }

    // 拼接纳入研究编号
    public void getNrbh(NdzxxDto ndzxxDto) {
        Map<String, Object> map = getnryjbh(ndzxxDto);
        JcsjDto js = new JcsjDto();
        js.setCsid(ndzxxDto.getSsyy());
        JcsjDto jcsjDto = jcsjService.getDto(js);
        String nrbh = jcsjDto.getCsdm();
        boolean isSc = true;
        // 如果本身有纳入编号，并且医院没变动，不修改
    	if (ndzxxDto.getNryjbh() != null && !ndzxxDto.getNryjbh().equals("")) {
			if (ndzxxDto.getNryjbh().length() > 4) {
				if (ndzxxDto.getNryjbh().substring(0, 4).equals(nrbh)) {
					isSc = false;
				}
			}
		}
		if (isSc) {
			BigDecimal xh = new BigDecimal(1);
			if (map != null) {
				if (map.get("nryjbhpx") != null && map.get("nryjbh") != null) {
					xh = ((BigDecimal) map.get("nryjbhpx")).add(new BigDecimal(1));
				}
			}
			ndzxxDto.setNryjbh(nrbh + xh);
			ndzxxDto.setNryjbhpx("" + xh);
		}
           
    }

    @Override
    public boolean insertImportRec(BaseModel baseModel, User user,int index, StringBuffer errorMessage) throws BusinessException {
        // TODO Auto-generated method stub
        NdzxxDto ndzxxDto = (NdzxxDto) baseModel;


        List<JcsjDto> dmxqs = getDmxqJcsj();
        List<JcsjDto> yzzbs = getYzzbJcsj();
        List<JcsjDto> shs = getShJcsj();
        List<JcsjDto> xcgs = getXcgJcsj();


        ndzxxDto.setTjr(user.getYhid());
        //所属医院转义
        ndzxxDto.setSsyy(getYyCsid(ndzxxDto));
        getNrbh(ndzxxDto);
        //亚组转义
        ndzxxDto.setYz(getYzCsid(ndzxxDto));
        //性别转义
        if(ndzxxDto.getXb().equals("男")){
            ndzxxDto.setXb("1");
        }else if(ndzxxDto.getXb().equals("女")){
            ndzxxDto.setXb("2");
        }else{
            ndzxxDto.setXb("未知");
        }
        //就诊科室转义
        ndzxxDto.setJzks(getJzksCsid(ndzxxDto));
        //病人类别转义
        ndzxxDto.setBrlb(getBrlbCsid(ndzxxDto));
        //抗菌药物暴露史转义
        if(ndzxxDto.getKjywbls().equals("前30天")){
            ndzxxDto.setKjywbls("0");
        }else if(ndzxxDto.getKjywbls().equals("前60天")){
            ndzxxDto.setKjywbls("1");
        }else if(ndzxxDto.getKjywbls().equals("前90天")){
            ndzxxDto.setKjywbls("2");
        }else{
            ndzxxDto.setKjywbls("无");
        }
        //既往合并症转义
        String jwhbzs = ndzxxDto.getJwhbz();
        String str[] = jwhbzs.split(",");
        String str_jwhbz="";
        for(int i=0;i<str.length;i++){
           str[i]=getJwhbzCsid(str[i]);
            if(i==str.length-1){
                str_jwhbz+=str[i];
            }else{
                str_jwhbz+=str[i]+",";
            }
        }
        ndzxxDto.setJwhbz(str_jwhbz);

        //感染部位
        String grbws = ndzxxDto.getGrbw();
        String[] grbw = grbws.split(",");
        String str_grbw="";
        for(int i=0;i<grbw.length;i++){
           grbw[i]=getGrbwCsid(grbw[i]);
            if(i==grbw.length-1){
                str_grbw+=grbw[i];
            }else{
                str_grbw+=grbw[i]+",";
            }
        }
        ndzxxDto.setGrbw(str_grbw);
        //实施抗菌药物降阶梯治疗转义
        if(ndzxxDto.getKjywjjtzl().equals("是")){
            ndzxxDto.setKjywjjtzl("1");
        }else{ndzxxDto.setKjywjjtzl("0");}
        //出ICU状态
        ndzxxDto.setCicuzt(getCicuztCsid(ndzxxDto));
        //出院状态
        ndzxxDto.setCyzt(getCyztCsid(ndzxxDto));
        //机械通气转义
        if(ndzxxDto.getJxtq().equals("是")){
            ndzxxDto.setJxtq("1");
        }else{ndzxxDto.setJxtq("0");}
        //CRRT转义
        if(ndzxxDto.getCrrt().equals("是")){
            ndzxxDto.setCrrt("1");
        }else{
            ndzxxDto.setCrrt("0");
        }
        //血管活性药转义
        if(ndzxxDto.getXghxy().equals("是")){
            ndzxxDto.setXghxy("1");
        }else{
            ndzxxDto.setXghxy("0");
        }
        //送检前是否使用抗菌药物转义
        if(ndzxxDto.getSjqsfsykjyw().equals("是")){
            ndzxxDto.setSjqsfsykjyw("1");
        }else{
            ndzxxDto.setSjqsfsykjyw("0");
        }
        //mcfdna转义
        if(ndzxxDto.getMcfdna().equals("是")){
            ndzxxDto.setMcfdna("1");
        }else{
            ndzxxDto.setMcfdna("0");
        }
        //血培养转义
        if(ndzxxDto.getXpy().equals("是")){
            ndzxxDto.setXpy("1");
        }else{
            ndzxxDto.setXpy("0");
        }
        //痰涂片转义
        if(ndzxxDto.getTtp().equals("是")){
            ndzxxDto.setTtp("1");
        }else{
            ndzxxDto.setTtp("0");
        }
        //痰培养转义
        if(ndzxxDto.getTpy().equals("是")){
            ndzxxDto.setTpy("1");
        }else{
            ndzxxDto.setTpy("0");
        }
        //腹水培养转义
        if(ndzxxDto.getFspy().equals("是")){
            ndzxxDto.setFspy("1");
        }else{
            ndzxxDto.setFspy("0");
        }
        //腹水涂片转义
        if(ndzxxDto.getFstp().equals("是")){
            ndzxxDto.setFstp("1");
        }else{
            ndzxxDto.setFstp("0");
        }
        //其它1转义
        if(ndzxxDto.getQtf().equals("是")){
            ndzxxDto.setQtf("1");
        }else{
            ndzxxDto.setQtf("0");
        }
        //其它2转义
        if(ndzxxDto.getQtt().equals("是")){
            ndzxxDto.setQtt("1");
        }else{
            ndzxxDto.setQtt("0");
        }
        //记录第几天转义
        if(ndzxxDto.getJldjt().equals("第一日")){
            ndzxxDto.setJldjt("1");
        }else if(ndzxxDto.getJldjt().equals("第三日")){
            ndzxxDto.setJldjt("3");
        }else if(ndzxxDto.getJldjt().equals("第五日")){
            ndzxxDto.setJldjt("5");
        }else if(ndzxxDto.getJldjt().equals("第七日")){
            ndzxxDto.setJldjt("7");
        }

        if (StringUtils.isNotBlank(ndzxxDto.getHzid())) {
            NdzxxDto ndzxxDto_t = getReportRepeat(ndzxxDto);
            if (ndzxxDto_t != null) {
            	log.error("有重复数据，请尝试重新导入！");
                throw new BusinessException("msg", "有重复数据，请尝试重新导入！");
            }
            NdzxxDto hz_ndzxxDto = getHzxxRepeat(ndzxxDto);
            if (hz_ndzxxDto == null) {
                insertHzxx(ndzxxDto);
                NdzxxDto ndzxxDto_b = new NdzxxDto();
                ndzxxDto_b.setHzid(ndzxxDto.getHzid());
                NdzxxDto jl_ndzxxDto = getJlxxRepeat(ndzxxDto_b);
                if (jl_ndzxxDto == null) {
                    ndzxxDto.setNdzjlid(StringUtil.generateUUID());
                     insertReport(ndzxxDto);
                    for(JcsjDto jcsjDto:dmxqs){
                        NdzdmxqDto ndzdmxqDto=new NdzdmxqDto();
                        ndzdmxqDto.setNdzjlid(ndzxxDto.getNdzjlid());
                        ndzdmxqDto.setJyxm(jcsjDto.getCsid());
                        if(jcsjDto.getCskz1().equals("rs")){
                            ndzdmxqDto.setSjz(ndzxxDto.getRs());
                        }else if(jcsjDto.getCskz1().equals("pao2")){
                            ndzdmxqDto.setSjz(ndzxxDto.getPao2());
                        }else if(jcsjDto.getCskz1().equals("abe")){
                            ndzdmxqDto.setSjz(ndzxxDto.getAbe());
                        }else if(jcsjDto.getCskz1().equals("hco3")){
                            ndzdmxqDto.setSjz(ndzxxDto.getHco3());
                        }else if(jcsjDto.getCskz1().equals("k")){
                            ndzdmxqDto.setSjz(ndzxxDto.getK());
                        }else if(jcsjDto.getCskz1().equals("ph")){
                            ndzdmxqDto.setSjz(ndzxxDto.getPh());
                        }else if(jcsjDto.getCskz1().equals("fio2")){
                            ndzdmxqDto.setSjz(ndzxxDto.getFio2());
                        }else if(jcsjDto.getCskz1().equals("na")){
                            ndzdmxqDto.setSjz(ndzxxDto.getNa());
                        }else if(jcsjDto.getCskz1().equals("ca")){
                            ndzdmxqDto.setSjz(ndzxxDto.getCa());
                        }else if(jcsjDto.getCskz1().equals("paco2")){
                            ndzdmxqDto.setSjz(ndzxxDto.getPaco2());
                        }
                        insertDmxq(ndzdmxqDto);
                    }

                    for(JcsjDto jcsjDto:yzzbs){
                        NdzyzzbDto ndzyzzbDto=new NdzyzzbDto();
                        ndzyzzbDto.setNdzjlid(ndzxxDto.getNdzjlid());
                        ndzyzzbDto.setJyxm(jcsjDto.getCsid());
                        if(jcsjDto.getCskz1().equals("il17")){
                            ndzyzzbDto.setSjz(ndzxxDto.getIl17());
                        }else if(jcsjDto.getCskz1().equals("tnfa")){
                            ndzyzzbDto.setSjz(ndzxxDto.getTnfa());
                        }else if(jcsjDto.getCskz1().equals("il1b")){
                            ndzyzzbDto.setSjz(ndzxxDto.getIl1b());
                        }else if(jcsjDto.getCskz1().equals("il10")){
                            ndzyzzbDto.setSjz(ndzxxDto.getIl10());
                        }else if(jcsjDto.getCskz1().equals("crp")){
                            ndzyzzbDto.setSjz(ndzxxDto.getCrp());
                        }else if(jcsjDto.getCskz1().equals("infy")){
                            ndzyzzbDto.setSjz(ndzxxDto.getInfy());
                        }else if(jcsjDto.getCskz1().equals("c3")){
                            ndzyzzbDto.setSjz(ndzxxDto.getC3());
                        }else if(jcsjDto.getCskz1().equals("c4")){
                            ndzyzzbDto.setSjz(ndzxxDto.getC4());
                        }else if(jcsjDto.getCskz1().equals("cd4th")){
                            ndzyzzbDto.setSjz(ndzxxDto.getCd4th());
                        }else if(jcsjDto.getCskz1().equals("pct")){
                            ndzyzzbDto.setSjz(ndzxxDto.getPct());
                        }else if(jcsjDto.getCskz1().equals("il6")){
                            ndzyzzbDto.setSjz(ndzxxDto.getIl6());
                        }
                        insertYzzb(ndzyzzbDto);
                    }

                    for(JcsjDto jcsjDto:shs){
                        NdzshDto ndzshDto=new NdzshDto();
                        ndzshDto.setNdzjlid(ndzxxDto.getNdzjlid());
                        ndzshDto.setJyxm(jcsjDto.getCsid());
                        if(jcsjDto.getCskz1().equals("nsd")){
                            ndzshDto.setSjz(ndzxxDto.getNsd());
                        }else if(jcsjDto.getCskz1().equals("bdb")){
                            ndzshDto.setSjz(ndzxxDto.getBdb());
                        }else if(jcsjDto.getCskz1().equals("jg")){
                            ndzshDto.setSjz(ndzxxDto.getJg());
                        }else if(jcsjDto.getCskz1().equals("zdhs")){
                            ndzshDto.setSjz(ndzxxDto.getZdhs());
                        }
                        insertSh(ndzshDto);
                    }

                    for(JcsjDto jcsjDto:xcgs){
                        NdzxcgDto ndzxcgDto=new NdzxcgDto();
                        ndzxcgDto.setNdzjlid(ndzxxDto.getNdzjlid());
                        ndzxcgDto.setJyxm(jcsjDto.getCsid());
                        if(jcsjDto.getCskz1().equals("zxlxbjdz")){
                            ndzxcgDto.setSjz(ndzxxDto.getZxxbjdz());
                        }else if(jcsjDto.getCskz1().equals("wbc")){
                            ndzxcgDto.setSjz(ndzxxDto.getWbc());
                        }else if(jcsjDto.getCskz1().equals("lbxbjdz")){
                            ndzxcgDto.setSjz(ndzxxDto.getLbxbjdz());
                        }else if(jcsjDto.getCskz1().equals("plt")){
                            ndzxcgDto.setSjz(ndzxxDto.getPlt());
                        }
                        insertXcg(ndzxcgDto);
                    }
                }
            } else {
                ndzxxDto.setHzid(hz_ndzxxDto.getHzid());
                NdzxxDto ndzxxDto_jlxx =  getJlxxRepeat(ndzxxDto);
                if (ndzxxDto_jlxx == null) {
                    ndzxxDto.setNdzjlid(StringUtil.generateUUID());
                     insertReport(ndzxxDto);
                    for(JcsjDto jcsjDto:dmxqs){
                        NdzdmxqDto ndzdmxqDto=new NdzdmxqDto();
                        ndzdmxqDto.setNdzjlid(ndzxxDto.getNdzjlid());
                        ndzdmxqDto.setJyxm(jcsjDto.getCsid());
                        if(jcsjDto.getCskz1().equals("rs")){
                            ndzdmxqDto.setSjz(ndzxxDto.getRs());
                        }else if(jcsjDto.getCskz1().equals("pao2")){
                            ndzdmxqDto.setSjz(ndzxxDto.getPao2());
                        }else if(jcsjDto.getCskz1().equals("abe")){
                            ndzdmxqDto.setSjz(ndzxxDto.getAbe());
                        }else if(jcsjDto.getCskz1().equals("hco3")){
                            ndzdmxqDto.setSjz(ndzxxDto.getHco3());
                        }else if(jcsjDto.getCskz1().equals("k")){
                            ndzdmxqDto.setSjz(ndzxxDto.getK());
                        }else if(jcsjDto.getCskz1().equals("ph")){
                            ndzdmxqDto.setSjz(ndzxxDto.getPh());
                        }else if(jcsjDto.getCskz1().equals("fio2")){
                            ndzdmxqDto.setSjz(ndzxxDto.getFio2());
                        }else if(jcsjDto.getCskz1().equals("na")){
                            ndzdmxqDto.setSjz(ndzxxDto.getNa());
                        }else if(jcsjDto.getCskz1().equals("ca")){
                            ndzdmxqDto.setSjz(ndzxxDto.getCa());
                        }else if(jcsjDto.getCskz1().equals("paco2")){
                            ndzdmxqDto.setSjz(ndzxxDto.getPaco2());
                        }
                        insertDmxq(ndzdmxqDto);
                    }

                    for(JcsjDto jcsjDto:yzzbs){
                        NdzyzzbDto ndzyzzbDto=new NdzyzzbDto();
                        ndzyzzbDto.setNdzjlid(ndzxxDto.getNdzjlid());
                        ndzyzzbDto.setJyxm(jcsjDto.getCsid());
                        if(jcsjDto.getCskz1().equals("il17")){
                            ndzyzzbDto.setSjz(ndzxxDto.getIl17());
                        }else if(jcsjDto.getCskz1().equals("tnfa")){
                            ndzyzzbDto.setSjz(ndzxxDto.getTnfa());
                        }else if(jcsjDto.getCskz1().equals("il1b")){
                            ndzyzzbDto.setSjz(ndzxxDto.getIl1b());
                        }else if(jcsjDto.getCskz1().equals("il10")){
                            ndzyzzbDto.setSjz(ndzxxDto.getIl10());
                        }else if(jcsjDto.getCskz1().equals("crp")){
                            ndzyzzbDto.setSjz(ndzxxDto.getCrp());
                        }else if(jcsjDto.getCskz1().equals("infy")){
                            ndzyzzbDto.setSjz(ndzxxDto.getInfy());
                        }else if(jcsjDto.getCskz1().equals("c3")){
                            ndzyzzbDto.setSjz(ndzxxDto.getC3());
                        }else if(jcsjDto.getCskz1().equals("c4")){
                            ndzyzzbDto.setSjz(ndzxxDto.getC4());
                        }else if(jcsjDto.getCskz1().equals("cd4th")){
                            ndzyzzbDto.setSjz(ndzxxDto.getCd4th());
                        }else if(jcsjDto.getCskz1().equals("pct")){
                            ndzyzzbDto.setSjz(ndzxxDto.getPct());
                        }else if(jcsjDto.getCskz1().equals("il6")){
                            ndzyzzbDto.setSjz(ndzxxDto.getIl6());
                        }
                        insertYzzb(ndzyzzbDto);
                    }

                    for(JcsjDto jcsjDto:shs){
                        NdzshDto ndzshDto=new NdzshDto();
                        ndzshDto.setNdzjlid(ndzxxDto.getNdzjlid());
                        ndzshDto.setJyxm(jcsjDto.getCsid());
                        if(jcsjDto.getCskz1().equals("nsd")){
                            ndzshDto.setSjz(ndzxxDto.getNsd());
                        }else if(jcsjDto.getCskz1().equals("bdb")){
                            ndzshDto.setSjz(ndzxxDto.getBdb());
                        }else if(jcsjDto.getCskz1().equals("jg")){
                            ndzshDto.setSjz(ndzxxDto.getJg());
                        }else if(jcsjDto.getCskz1().equals("zdhs")){
                            ndzshDto.setSjz(ndzxxDto.getZdhs());
                        }
                        insertSh(ndzshDto);
                    }

                    for(JcsjDto jcsjDto:xcgs){
                        NdzxcgDto ndzxcgDto=new NdzxcgDto();
                        ndzxcgDto.setNdzjlid(ndzxxDto.getNdzjlid());
                        ndzxcgDto.setJyxm(jcsjDto.getCsid());
                        if(jcsjDto.getCskz1().equals("zxlxbjdz")){
                            ndzxcgDto.setSjz(ndzxxDto.getZxxbjdz());
                        }else if(jcsjDto.getCskz1().equals("wbc")){
                            ndzxcgDto.setSjz(ndzxxDto.getWbc());
                        }else if(jcsjDto.getCskz1().equals("lbxbjdz")){
                            ndzxcgDto.setSjz(ndzxxDto.getLbxbjdz());
                        }else if(jcsjDto.getCskz1().equals("plt")){
                            ndzxcgDto.setSjz(ndzxxDto.getPlt());
                        }
                        insertXcg(ndzxcgDto);
                    }
                }
            }
        }
        return true;
    }
    /**
     * 新增转归情况
     * @param ndzxxDto
     * @return
     */
    public boolean insertZgqk(NdzxxDto ndzxxDto){
        return dao.insertZgqk(ndzxxDto);
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
        // TODO Auto-generated method stub

        return true;
    }
    /**
     * 获取亚组的参数id
     * @param ndzxxDto
     * @return
     */
    public String getYzCsid(NdzxxDto ndzxxDto){
        return dao.getYzCsid(ndzxxDto);
    }

    /**
     * 所有所属医院
     * @return
     */
    public List<NdzxxDto> getSsyy(){
        return dao.getSsyy();
    }
    /**
     * 所有亚组
     * @return
     */
    public List<NdzxxDto> getYz(){
        return dao.getYz();
    }
    /**
     * 点击继续存入数据库
     * @param fjcfbDto
     * @return
     */
    public Boolean saveWordReport(FjcfbDto fjcfbDto, User user){
        String fjid = fjcfbDto.getFjid();
        String filepath = String.valueOf(redisUtil.hget("IMP_:_" + fjid, "b_wjlj"));
        List<NdzxxDto> ndzxxDtoList =  formWord(filepath);
        for(int n=0;n<ndzxxDtoList.size();n++){
                NdzxxDto ndzxxDto = ndzxxDtoList.get(n);
                ndzxxDto.setTjr(user.getYhid());
                List<JcsjDto> dmxqs =  getDmxqJcsj();
                List<JcsjDto> yzzbs =  getYzzbJcsj();
                List<JcsjDto> shs =  getShJcsj();
                List<JcsjDto> xcgs =  getXcgJcsj();

            if(n==0){
                    ndzxxDto.setSsyy(fjcfbDto.getSsyy());
                    ndzxxDto.setYz(fjcfbDto.getYz());
                    //所属医院转义
                    ndzxxDto.setSsyy(getYyCsid(ndzxxDto));
                    getNrbh(ndzxxDto);
                    //亚组转义
                    ndzxxDto.setYz(getYzCsid(ndzxxDto));
                    //性别转义
                    if(ndzxxDto.getXb().equals("男")){
                        ndzxxDto.setXb("1");
                    }else if(ndzxxDto.getXb().equals("女")){
                        ndzxxDto.setXb("2");
                    }else{
                        ndzxxDto.setXb("未知");
                    }
                    //就诊科室转义
                    String jzks=getJzksCsid(ndzxxDto);
                    ndzxxDto.setJzks(jzks);
                    //病人类别转义
                    String brlb=getBrlbCsid(ndzxxDto);
                    ndzxxDto.setBrlb(brlb);
                    //抗菌药物暴露史转义
                    if(ndzxxDto.getKjywbls().equals("前30天")){
                        ndzxxDto.setKjywbls("0");
                    }else if(ndzxxDto.getKjywbls().equals("前60天")){
                        ndzxxDto.setKjywbls("1");
                    }else if(ndzxxDto.getKjywbls().equals("前90天")){
                        ndzxxDto.setKjywbls("2");
                    }else{
                        ndzxxDto.setKjywbls("无");
                    }
                    //既往合并症转义
                    String jwhbzs = ndzxxDto.getJwhbz();
                    String str[] = jwhbzs.split(",");
                    String str_jwhbz="";
                    for(int i=0;i<str.length;i++){
                        str[i]=getJwhbzCsid(str[i]);
                        if(i==str.length-1){
                            str_jwhbz+=str[i];
                        }else{
                            str_jwhbz+=str[i]+",";
                        }
                    }
                    ndzxxDto.setJwhbz(str_jwhbz);

                    //感染部位
                    String grbws = ndzxxDto.getGrbw();
                    String[] grbw = grbws.split(",");
                    String str_grbw="";
                    for(int i=0;i<grbw.length;i++){
                        grbw[i]=getGrbwCsid(grbw[i]);
                        if(i==grbw.length-1){
                            str_grbw+=grbw[i];
                        }else{
                            str_grbw+=grbw[i]+",";
                        }
                    }
                    ndzxxDto.setGrbw(str_grbw);
                    //实施抗菌药物降阶梯治疗转义
                    if(ndzxxDto.getKjywjjtzl().equals("是")){
                        ndzxxDto.setKjywjjtzl("1");
                    }else{ndzxxDto.setKjywjjtzl("0");}
                    //出ICU状态
                    ndzxxDto.setCicuzt(getCicuztCsid(ndzxxDto));
                    //出院状态
                    ndzxxDto.setCyzt(getCyztCsid(ndzxxDto));
                    //送检前是否使用抗菌药物转义
                    if(ndzxxDto.getSjqsfsykjyw().equals("是")){
                        ndzxxDto.setSjqsfsykjyw("1");
                    }else{
                        ndzxxDto.setSjqsfsykjyw("0");
                    }
                    //mcfdna转义
                    if(ndzxxDto.getMcfdna().equals("是")){
                        ndzxxDto.setMcfdna("1");
                    }else{
                        ndzxxDto.setMcfdna("0");
                    }
                    //血培养转义
                    if(ndzxxDto.getXpy().equals("是")){
                        ndzxxDto.setXpy("1");
                    }else{
                        ndzxxDto.setXpy("0");
                    }
                    //痰涂片转义
                    if(ndzxxDto.getTtp().equals("是")){
                        ndzxxDto.setTtp("1");
                    }else{
                        ndzxxDto.setTtp("0");
                    }
                    //痰培养转义
                    if(ndzxxDto.getTpy().equals("是")){
                        ndzxxDto.setTpy("1");
                    }else{
                        ndzxxDto.setTpy("0");
                    }
                    //腹水培养转义
                    if(ndzxxDto.getFspy().equals("是")){
                        ndzxxDto.setFspy("1");
                    }else{
                        ndzxxDto.setFspy("0");
                    }
                    //腹水涂片转义
                    if(ndzxxDto.getFstp().equals("是")){
                        ndzxxDto.setFstp("1");
                    }else{
                        ndzxxDto.setFstp("0");
                    }
                    //其它1转义
                    if(ndzxxDto.getQtf().equals("是")){
                        ndzxxDto.setQtf("1");
                    }else{
                        ndzxxDto.setQtf("0");
                    }
                    //其它2转义
                    if(ndzxxDto.getQtt().equals("是")){
                        ndzxxDto.setQtt("1");
                    }else{
                        ndzxxDto.setQtt("0");
                    }
                }
                //机械通气转义
                if(ndzxxDto.getJxtq().equals("是")){
                    ndzxxDto.setJxtq("1");
                }else{ndzxxDto.setJxtq("0");}
                //CRRT转义
                if(ndzxxDto.getCrrt().equals("是")){
                    ndzxxDto.setCrrt("1");
                }else{
                    ndzxxDto.setCrrt("0");
                }
                //血管活性药转义
                if(ndzxxDto.getXghxy().equals("是")){
                    ndzxxDto.setXghxy("1");
                }else{
                    ndzxxDto.setXghxy("0");
                }
                //记录第几天转义
                if(ndzxxDto.getJldjt().equals("第一日")){
                    ndzxxDto.setJldjt("1");
                }else if(ndzxxDto.getJldjt().equals("第三日")){
                    ndzxxDto.setJldjt("3");
                }else if(ndzxxDto.getJldjt().equals("第五日")){
                    ndzxxDto.setJldjt("5");
                }else if(ndzxxDto.getJldjt().equals("第七日")){
                    ndzxxDto.setJldjt("7");
                }
                if(n==0){
                if (StringUtils.isNotBlank(ndzxxDto.getZyh()) && StringUtil.isNotBlank(ndzxxDto.getNryjbh()) && StringUtil.isNotBlank(ndzxxDto.getJldjt())) {
                        NdzxxDto ndzxxDto_t =  getReportRepeat(ndzxxDto);
                        if (ndzxxDto_t != null) {
                            log.error("有重复数据，请尝试重新导入！");
                            return false;
                        }else{
                            ndzxxDto.setNdzjlid(StringUtil.generateUUID());
                            ndzxxDto.setHzid(StringUtil.generateUUID());
                            ndzxxDto.setZgid(StringUtil.generateUUID());
                             insertHzxx(ndzxxDto);
                             insertReport(ndzxxDto);
                            for(JcsjDto jcsjDto:dmxqs){
                                NdzdmxqDto ndzdmxqDto=new NdzdmxqDto();
                                ndzdmxqDto.setNdzjlid(ndzxxDto.getNdzjlid());
                                ndzdmxqDto.setJyxm(jcsjDto.getCsid());
                                if(jcsjDto.getCskz1().equals("rs")){
                                    ndzdmxqDto.setSjz(ndzxxDto.getRs());
                                }else if(jcsjDto.getCskz1().equals("pao2")){
                                    ndzdmxqDto.setSjz(ndzxxDto.getPao2());
                                }else if(jcsjDto.getCskz1().equals("abe")){
                                    ndzdmxqDto.setSjz(ndzxxDto.getAbe());
                                }else if(jcsjDto.getCskz1().equals("hco3")){
                                    ndzdmxqDto.setSjz(ndzxxDto.getHco3());
                                }else if(jcsjDto.getCskz1().equals("k")){
                                    ndzdmxqDto.setSjz(ndzxxDto.getK());
                                }else if(jcsjDto.getCskz1().equals("ph")){
                                    ndzdmxqDto.setSjz(ndzxxDto.getPh());
                                }else if(jcsjDto.getCskz1().equals("fio2")){
                                    ndzdmxqDto.setSjz(ndzxxDto.getFio2());
                                }else if(jcsjDto.getCskz1().equals("na")){
                                    ndzdmxqDto.setSjz(ndzxxDto.getNa());
                                }else if(jcsjDto.getCskz1().equals("ca")){
                                    ndzdmxqDto.setSjz(ndzxxDto.getCa());
                                }else if(jcsjDto.getCskz1().equals("paco2")){
                                    ndzdmxqDto.setSjz(ndzxxDto.getPaco2());
                                }
                                insertDmxq(ndzdmxqDto);
                            }

                            for(JcsjDto jcsjDto:yzzbs){
                                NdzyzzbDto ndzyzzbDto=new NdzyzzbDto();
                                ndzyzzbDto.setNdzjlid(ndzxxDto.getNdzjlid());
                                ndzyzzbDto.setJyxm(jcsjDto.getCsid());
                                if(jcsjDto.getCskz1().equals("il17")){
                                    ndzyzzbDto.setSjz(ndzxxDto.getIl17());
                                }else if(jcsjDto.getCskz1().equals("tnfa")){
                                    ndzyzzbDto.setSjz(ndzxxDto.getTnfa());
                                }else if(jcsjDto.getCskz1().equals("il1b")){
                                    ndzyzzbDto.setSjz(ndzxxDto.getIl1b());
                                }else if(jcsjDto.getCskz1().equals("il10")){
                                    ndzyzzbDto.setSjz(ndzxxDto.getIl10());
                                }else if(jcsjDto.getCskz1().equals("crp")){
                                    ndzyzzbDto.setSjz(ndzxxDto.getCrp());
                                }else if(jcsjDto.getCskz1().equals("infy")){
                                    ndzyzzbDto.setSjz(ndzxxDto.getInfy());
                                }else if(jcsjDto.getCskz1().equals("c3")){
                                    ndzyzzbDto.setSjz(ndzxxDto.getC3());
                                }else if(jcsjDto.getCskz1().equals("c4")){
                                    ndzyzzbDto.setSjz(ndzxxDto.getC4());
                                }else if(jcsjDto.getCskz1().equals("cd4th")){
                                    ndzyzzbDto.setSjz(ndzxxDto.getCd4th());
                                }else if(jcsjDto.getCskz1().equals("pct")){
                                    ndzyzzbDto.setSjz(ndzxxDto.getPct());
                                }else if(jcsjDto.getCskz1().equals("il6")){
                                    ndzyzzbDto.setSjz(ndzxxDto.getIl6());
                                }
                                insertYzzb(ndzyzzbDto);
                            }

                            for(JcsjDto jcsjDto:shs){
                                NdzshDto ndzshDto=new NdzshDto();
                                ndzshDto.setNdzjlid(ndzxxDto.getNdzjlid());
                                ndzshDto.setJyxm(jcsjDto.getCsid());
                                if(jcsjDto.getCskz1().equals("nsd")){
                                    ndzshDto.setSjz(ndzxxDto.getNsd());
                                }else if(jcsjDto.getCskz1().equals("bdb")){
                                    ndzshDto.setSjz(ndzxxDto.getBdb());
                                }else if(jcsjDto.getCskz1().equals("jg")){
                                    ndzshDto.setSjz(ndzxxDto.getJg());
                                }else if(jcsjDto.getCskz1().equals("zdhs")){
                                    ndzshDto.setSjz(ndzxxDto.getZdhs());
                                }
                                insertSh(ndzshDto);
                            }

                            for(JcsjDto jcsjDto:xcgs){
                                NdzxcgDto ndzxcgDto=new NdzxcgDto();
                                ndzxcgDto.setNdzjlid(ndzxxDto.getNdzjlid());
                                ndzxcgDto.setJyxm(jcsjDto.getCsid());
                                if(jcsjDto.getCskz1().equals("zxlxbjdz")){
                                    ndzxcgDto.setSjz(ndzxxDto.getZxxbjdz());
                                }else if(jcsjDto.getCskz1().equals("wbc")){
                                    ndzxcgDto.setSjz(ndzxxDto.getWbc());
                                }else if(jcsjDto.getCskz1().equals("lbxbjdz")){
                                    ndzxcgDto.setSjz(ndzxxDto.getLbxbjdz());
                                }else if(jcsjDto.getCskz1().equals("plt")){
                                    ndzxcgDto.setSjz(ndzxxDto.getPlt());
                                }
                                insertXcg(ndzxcgDto);
                            }
                        }
                    }
                }else{
                    ndzxxDto.setHzid( getHzxxRepeat(ndzxxDto).getHzid());
                    ndzxxDto.setNdzjlid(StringUtil.generateUUID());
                     insertReport(ndzxxDto);
                    for(JcsjDto jcsjDto:dmxqs){
                        NdzdmxqDto ndzdmxqDto=new NdzdmxqDto();
                        ndzdmxqDto.setNdzjlid(ndzxxDto.getNdzjlid());
                        ndzdmxqDto.setJyxm(jcsjDto.getCsid());
                        if(jcsjDto.getCskz1().equals("rs")){
                            ndzdmxqDto.setSjz(ndzxxDto.getRs());
                        }else if(jcsjDto.getCskz1().equals("pao2")){
                            ndzdmxqDto.setSjz(ndzxxDto.getPao2());
                        }else if(jcsjDto.getCskz1().equals("abe")){
                            ndzdmxqDto.setSjz(ndzxxDto.getAbe());
                        }else if(jcsjDto.getCskz1().equals("hco3")){
                            ndzdmxqDto.setSjz(ndzxxDto.getHco3());
                        }else if(jcsjDto.getCskz1().equals("k")){
                            ndzdmxqDto.setSjz(ndzxxDto.getK());
                        }else if(jcsjDto.getCskz1().equals("ph")){
                            ndzdmxqDto.setSjz(ndzxxDto.getPh());
                        }else if(jcsjDto.getCskz1().equals("fio2")){
                            ndzdmxqDto.setSjz(ndzxxDto.getFio2());
                        }else if(jcsjDto.getCskz1().equals("na")){
                            ndzdmxqDto.setSjz(ndzxxDto.getNa());
                        }else if(jcsjDto.getCskz1().equals("ca")){
                            ndzdmxqDto.setSjz(ndzxxDto.getCa());
                        }else if(jcsjDto.getCskz1().equals("paco2")){
                            ndzdmxqDto.setSjz(ndzxxDto.getPaco2());
                        }
                        insertDmxq(ndzdmxqDto);
                    }

                    for(JcsjDto jcsjDto:yzzbs){
                        NdzyzzbDto ndzyzzbDto=new NdzyzzbDto();
                        ndzyzzbDto.setNdzjlid(ndzxxDto.getNdzjlid());
                        ndzyzzbDto.setJyxm(jcsjDto.getCsid());
                        if(jcsjDto.getCskz1().equals("il17")){
                            ndzyzzbDto.setSjz(ndzxxDto.getIl17());
                        }else if(jcsjDto.getCskz1().equals("tnfa")){
                            ndzyzzbDto.setSjz(ndzxxDto.getTnfa());
                        }else if(jcsjDto.getCskz1().equals("il1b")){
                            ndzyzzbDto.setSjz(ndzxxDto.getIl1b());
                        }else if(jcsjDto.getCskz1().equals("il10")){
                            ndzyzzbDto.setSjz(ndzxxDto.getIl10());
                        }else if(jcsjDto.getCskz1().equals("crp")){
                            ndzyzzbDto.setSjz(ndzxxDto.getCrp());
                        }else if(jcsjDto.getCskz1().equals("infy")){
                            ndzyzzbDto.setSjz(ndzxxDto.getInfy());
                        }else if(jcsjDto.getCskz1().equals("c3")){
                            ndzyzzbDto.setSjz(ndzxxDto.getC3());
                        }else if(jcsjDto.getCskz1().equals("c4")){
                            ndzyzzbDto.setSjz(ndzxxDto.getC4());
                        }else if(jcsjDto.getCskz1().equals("cd4th")){
                            ndzyzzbDto.setSjz(ndzxxDto.getCd4th());
                        }else if(jcsjDto.getCskz1().equals("pct")){
                            ndzyzzbDto.setSjz(ndzxxDto.getPct());
                        }else if(jcsjDto.getCskz1().equals("il6")){
                            ndzyzzbDto.setSjz(ndzxxDto.getIl6());
                        }
                        insertYzzb(ndzyzzbDto);
                    }

                    for(JcsjDto jcsjDto:shs){
                        NdzshDto ndzshDto=new NdzshDto();
                        ndzshDto.setNdzjlid(ndzxxDto.getNdzjlid());
                        ndzshDto.setJyxm(jcsjDto.getCsid());
                        if(jcsjDto.getCskz1().equals("nsd")){
                            ndzshDto.setSjz(ndzxxDto.getNsd());
                        }else if(jcsjDto.getCskz1().equals("bdb")){
                            ndzshDto.setSjz(ndzxxDto.getBdb());
                        }else if(jcsjDto.getCskz1().equals("jg")){
                            ndzshDto.setSjz(ndzxxDto.getJg());
                        }else if(jcsjDto.getCskz1().equals("zdhs")){
                            ndzshDto.setSjz(ndzxxDto.getZdhs());
                        }
                        insertSh(ndzshDto);
                    }

                    for(JcsjDto jcsjDto:xcgs){
                        NdzxcgDto ndzxcgDto=new NdzxcgDto();
                        ndzxcgDto.setNdzjlid(ndzxxDto.getNdzjlid());
                        ndzxcgDto.setJyxm(jcsjDto.getCsid());
                        if(jcsjDto.getCskz1().equals("zxlxbjdz")){
                            ndzxcgDto.setSjz(ndzxxDto.getZxxbjdz());
                        }else if(jcsjDto.getCskz1().equals("wbc")){
                            ndzxcgDto.setSjz(ndzxxDto.getWbc());
                        }else if(jcsjDto.getCskz1().equals("lbxbjdz")){
                            ndzxcgDto.setSjz(ndzxxDto.getLbxbjdz());
                        }else if(jcsjDto.getCskz1().equals("plt")){
                            ndzxcgDto.setSjz(ndzxxDto.getPlt());
                        }
                        insertXcg(ndzxcgDto);
                    }
                }


            }
        return true;
    }
    /**
     * 获取上传文件的信息列表
     * @param fjcfbDto
     * @return
     */
    public List<NdzxxDto> getImpList(FjcfbDto fjcfbDto){

        String fjid = fjcfbDto.getFjid();
        try{
                List<NdzxxDto> ndzxxDtoList=null;
                ndzxxDtoList=  formWord(String.valueOf(redisUtil.hget("IMP_:_"+fjid,"b_wjlj")).replaceAll("_back",""));
                return ndzxxDtoList;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 根据附件ID获取导入信息
     * @param fjid
     * @return
     */
    public Map<String, Object> checkImpFileProcess(String fjid){

        Map<String, Object> result = new HashMap<String, Object>();

        String b_wjlj = String.valueOf(redisUtil.hget("IMP_:_" + fjid, "b_wjlj"));


        if(b_wjlj.endsWith(".docx")||b_wjlj.endsWith(".doc")){
            result.put("successmsg","数据检查没有问题，请点击继续按钮继续或点击取消按钮取消上传。若无数据显示，请检查上传文件是否正确");
        } else{
            result.put("errormsg", "导入的文件类型出错，请重新修改再提交");
        }
        return result;
    }

    /**
     * 重新整理模板文件，把变量放到一个段落里，方便后续处理
     * @param filePath
     * @return
     */
    public  List<NdzxxDto> formWord(String filePath) {

        List<NdzxxDto> ndzxxDtoList=new ArrayList<>();
        if(filePath.endsWith(".doc")) {

        }else if(filePath.endsWith(".docx")){
            ndzxxDtoList = reformWord(filePath);;
        }
        return ndzxxDtoList;
    }
    /**
     * 重新整理模板文件，把变量放到一个段落里，方便后续处理
     * @param filePath
     * @return
     */
    public List<NdzxxDto> reformWord(String filePath) {
        NdzxxDto ndzxxDto = new NdzxxDto();
        List<NdzxxDto> ndzxxDtoList=new ArrayList<>();


            File file = new File(filePath);
            if (file.exists()) {



                file.renameTo(new File(filePath + "_back"));

                FileInputStream fileInputStream = null;
                XWPFDocument document = null;

                try {
                    // 把文件放入流
                    fileInputStream = new FileInputStream(filePath + "_back");
                    //读取word文件
                    document = new XWPFDocument(fileInputStream);
                    //先整理未在表格中的段落
                    ndzxxDto = reformOutsideParagraph(document, null);

                    //再整理表格中的段落
                    ndzxxDtoList = reformTableParagraph(document, ndzxxDto);


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        // TODO: handle exception
                        if (document != null)
                            document.close();
                        if (fileInputStream != null)
                            fileInputStream.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        return ndzxxDtoList;
    }



    /**
     * 整理文档中表格外的段落
     * @param document 文档
     * @param cell 表格
     * @return
     */
    public NdzxxDto reformOutsideParagraph(XWPFDocument document, XWPFTableCell cell) {
        NdzxxDto ndzxxDto=new NdzxxDto();
        //先整理未在表格中的段落
        List<XWPFParagraph> graphs = null;

        if(document != null)
            graphs = document.getParagraphs();
        else {
            graphs = cell.getParagraphs();
        }
        for(int i=0;i<graphs.size();i++){
            String text = graphs.get(i).getText().replaceAll(" ", "");
            if(text.indexOf("患者姓名")!=-1){
                String[] split = text.split("：");
                ndzxxDto.setHzxm(split[1]);
            }else if(text.indexOf("住院号")!=-1){
                String[] split = text.split("：");
                ndzxxDto.setZyh(split[1]);
            }else if(text.indexOf("纳入研究编号")!=-1){
                String[] split = text.split("：");
                ndzxxDto.setNryjbh(split[1]);
            }else if(text.indexOf("纳入时间")!=-1){
                String[] split = text.split("：");
                ndzxxDto.setNrsj(split[1]);
            }else if(text.indexOf("临床医师收到报告后是否实施了抗菌药物降阶梯治疗")!=-1){
                String[] split = text.split("疗");
                ndzxxDto.setKjywjjtzl(split[1]);
            }else if(text.indexOf("抗菌药物治疗总疗程")!=-1){
                String[] split = text.split("程");
                ndzxxDto.setKjzlc(split[1]);
            }
        }
        return ndzxxDto;
    }

    /**
     * 整理文档中表格中的段落
     * @param document
     * @return
     */
     public List<NdzxxDto> reformTableParagraph(XWPFDocument document,NdzxxDto ndzxxDto) {

        List<XWPFTable> tables = document.getTables();

         //患者基本信息表信息提取
         XWPFTable xwpfTable = tables.get(1);
         int rcount = xwpfTable.getNumberOfRows();
         for (int i = 0; i < rcount; i++){
             if(i==0) {
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String nl = cells.get(1).getText();
                 ndzxxDto.setNl(nl);
             }else if(i==1){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String xb = cells.get(1).getText();
                 ndzxxDto.setXb(xb);
             }else if(i==2){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String[] split = cells.get(0).getText().split(":");
                 ndzxxDto.setJzks(split[1].replaceAll(" ",""));
             }else if(i==3){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String[] split = cells.get(0).getText().split(":");
                 ndzxxDto.setBrlb(split[1].replaceAll(" ",""));
             }else if(i==5){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String jwhbz = cells.get(0).getText().trim().replaceAll(" ", ",");
                 ndzxxDto.setJwhbz(jwhbz);
             }else if(i==7){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String grbw = cells.get(0).getText().trim().replaceAll(" ", ",");
                 ndzxxDto.setGrbw(grbw);
             }else if(i==8){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String kjywbls = cells.get(0).getText().substring(4, 8);
                 ndzxxDto.setKjywbls(kjywbls);
                 String[] split = cells.get(0).getText().split("：");
                 ndzxxDto.setRysj(split[1]);
             }
         }
         //诊断脓毒症当日信息提取
         XWPFTable xwpfTable_jlone = tables.get(2);
         ndzxxDto.setJldjt("第一日");
         int rcount_jlone = xwpfTable_jlone.getNumberOfRows();
         for (int i = 0; i < rcount_jlone; i++){
             if(i==1) {
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String hrmax = cells.get(1).getText().replace("次/分","");
                 ndzxxDto.setHrmax(hrmax);
                 String mapmax = cells.get(3).getText().replace("mmHg","");
                 ndzxxDto.setMapmax(mapmax);
                 String sapmax = cells.get(5).getText().replace("mmHg","");
                 ndzxxDto.setSapmax(sapmax);
                 String rrmax = cells.get(7).getText().replace("次/分","");
                 ndzxxDto.setRrmax(rrmax);
             }else if(i==2){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String tmax = cells.get(1).getText().replace("℃","");
                 ndzxxDto.setTmax(tmax);
             }else if(i==3){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String jxtq = cells.get(1).getText();
                 ndzxxDto.setJxtq(jxtq);
                 String crrt = cells.get(3).getText();
                 ndzxxDto.setCrrt(crrt);
                 String gcs = cells.get(5).getText();
                 ndzxxDto.setGcs(gcs);
             }else if(i==4){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String xghxy = cells.get(1).getText();
                 ndzxxDto.setXghxy(xghxy);
             }else if(i==6){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String rs = cells.get(1).getText().replace("mmol/L","");
                 ndzxxDto.setRs(rs);
                 String k = cells.get(3).getText().replace("mmol/L","");
                 ndzxxDto.setK(k);
                 String na = cells.get(5).getText().replace("mmol/L","");
                 ndzxxDto.setNa(na);
                 String ca = cells.get(7).getText().replace("mmol/L","");
                 ndzxxDto.setCa(ca);
             }else if(i==7){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String ph = cells.get(1).getText();
                 ndzxxDto.setPh(ph);
                 String paco2 = cells.get(3).getText().replace("mmHg","");
                 ndzxxDto.setPaco2(paco2);
                 String pao2 = cells.get(5).getText().replace("mmHg","");
                 ndzxxDto.setPao2(pao2);
             }else if(i==8){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String abe = cells.get(1).getText().replace("mmol/L","");
                 ndzxxDto.setAbe(abe);
                 String hco3 = cells.get(3).getText().replace("mmol/L","");
                 ndzxxDto.setHco3(hco3);
                 String fio2 = cells.get(5).getText();
                 ndzxxDto.setFio2(fio2);
             }else if(i==10){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String crp = cells.get(1).getText().replace("mg/L","");
                 ndzxxDto.setCrp(crp);
                 String pct = cells.get(3).getText().replace("ng/ml","");
                 ndzxxDto.setPct(pct);
                 String il6 = cells.get(5).getText().replace("pg/ml","");
                 ndzxxDto.setIl6(il6);
                 String il1b = cells.get(7).getText().replace("pg/ml","");
                 ndzxxDto.setIl1b(il1b);
             }else if(i==11){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String il10 = cells.get(1).getText().replace("ng/ml","");
                 ndzxxDto.setIl10(il10);
             }else if(i==13){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String nsd = cells.get(1).getText().replace("mg/L","");
                 ndzxxDto.setNsd(nsd);
                 String bdb = cells.get(3).getText().replace("g/L","");
                 ndzxxDto.setBdb(bdb);
                 String jg = cells.get(5).getText().replace("μmol/L","");
                 ndzxxDto.setJg(jg);
                 String zdhs = cells.get(7).getText().replace("μmol/L","");
                 ndzxxDto.setZdhs(zdhs);
             }else if(i==15){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String wbc = cells.get(1).getText().replace("L","");
                 ndzxxDto.setWbc(wbc);
                 String plt = cells.get(3).getText().replace("L","");
                 ndzxxDto.setPlt(plt);
                 String lcount = cells.get(5).getText().replace("L","");
                 ndzxxDto.setLcount(lcount);
             }else if(i==17){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String sjqsfsykjyw = cells.get(1).getText();
                 ndzxxDto.setSjqsfsykjyw(sjqsfsykjyw);
             }else if(i==19){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String kjywzl = cells.get(0).getText();
                 ndzxxDto.setKjywzl(kjywzl);
             }else if(i==21){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String mcfdna = cells.get(1).getText();
                 ndzxxDto.setMcfdna(mcfdna);
                 String mcfdnajg = cells.get(3).getText();
                 ndzxxDto.setMcfdnajg(mcfdnajg);
             }else if(i==22){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String xpy = cells.get(1).getText();
                 ndzxxDto.setXpy(xpy);
                 String xpyjg = cells.get(3).getText();
                 ndzxxDto.setXpyjg(xpyjg);
             }else if(i==23){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String ttp = cells.get(1).getText();
                 ndzxxDto.setTtp(ttp);
                 String ttpjg = cells.get(3).getText();
                 ndzxxDto.setTtpjg(ttpjg);
             }else if(i==24){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String tpy = cells.get(1).getText();
                 ndzxxDto.setTpy(tpy);
                 String tpyjg = cells.get(3).getText();
                 ndzxxDto.setTpyjg(tpyjg);
             }else if(i==25){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String fstp = cells.get(1).getText();
                 ndzxxDto.setFstp(fstp);
                 String fstpjg = cells.get(3).getText();
                 ndzxxDto.setFstpjg(fstpjg);
             }else if(i==26){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String fspy = cells.get(1).getText();
                 ndzxxDto.setFspy(fspy);
                 String fspyjg = cells.get(3).getText();
                 ndzxxDto.setFspyjg(fspyjg);
             }else if(i==27){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String qtf = cells.get(1).getText();
                 ndzxxDto.setQtf(qtf);
                 String qtfjg = cells.get(3).getText();
                 ndzxxDto.setQtfjg(qtfjg);
             }else if(i==28){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlone.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String qtt = cells.get(1).getText();
                 ndzxxDto.setQtt(qtt);
                 String qttjg = cells.get(3).getText();
                 ndzxxDto.setQttjg(qttjg);
             }
         }
         //诊断脓毒症第三日信息提取
         NdzxxDto ndzxxDto_jlthree=new NdzxxDto();
         ndzxxDto_jlthree.setJldjt("第三日");
         ndzxxDto_jlthree.setHzxm(ndzxxDto.getHzxm());
         ndzxxDto_jlthree.setZyh(ndzxxDto.getZyh());
         ndzxxDto_jlthree.setNryjbh(ndzxxDto.getNryjbh());
         XWPFTable xwpfTable_jlthree = tables.get(3);
         int rcount_jlthree = xwpfTable_jlthree.getNumberOfRows();
         for (int i = 0; i < rcount_jlthree; i++){
             if(i==1) {
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlthree.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String hrmax = cells.get(1).getText().replace("次/分","");
                 ndzxxDto_jlthree.setHrmax(hrmax);
                 String mapmax = cells.get(3).getText().replace("mmHg","");
                 ndzxxDto_jlthree.setMapmax(mapmax);
                 String sapmax = cells.get(5).getText().replace("mmHg","");
                 ndzxxDto_jlthree.setSapmax(sapmax);
                 String rrmax = cells.get(7).getText().replace("次/分","");
                 ndzxxDto_jlthree.setRrmax(rrmax);
             }else if(i==2){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlthree.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String tmax = cells.get(1).getText().replace("℃","");
                 ndzxxDto_jlthree.setTmax(tmax);
             }else if(i==3){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlthree.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String jxtq = cells.get(1).getText();
                 ndzxxDto_jlthree.setJxtq(jxtq);
                 String crrt = cells.get(3).getText();
                 ndzxxDto_jlthree.setCrrt(crrt);
                 String gcs = cells.get(5).getText();
                 ndzxxDto_jlthree.setGcs(gcs);
             }else if(i==4){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlthree.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String xghxy = cells.get(1).getText();
                 ndzxxDto_jlthree.setXghxy(xghxy);
             }else if(i==6){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlthree.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String rs = cells.get(1).getText().replace("mmol/L","");
                 ndzxxDto_jlthree.setRs(rs);
                 String k = cells.get(3).getText().replace("mmol/L","");
                 ndzxxDto_jlthree.setK(k);
                 String na = cells.get(5).getText().replace("mmol/L","");
                 ndzxxDto_jlthree.setNa(na);
                 String ca = cells.get(7).getText().replace("mmol/L","");
                 ndzxxDto_jlthree.setCa(ca);
             }else if(i==7){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlthree.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String ph = cells.get(1).getText();
                 ndzxxDto_jlthree.setPh(ph);
                 String paco2 = cells.get(3).getText().replace("mmHg","");
                 ndzxxDto_jlthree.setPaco2(paco2);
                 String pao2 = cells.get(5).getText().replace("mmHg","");
                 ndzxxDto_jlthree.setPao2(pao2);
             }else if(i==8){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlthree.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String abe = cells.get(1).getText().replace("mmol/L","");
                 ndzxxDto_jlthree.setAbe(abe);
                 String hco3 = cells.get(3).getText().replace("mmol/L","");
                 ndzxxDto_jlthree.setHco3(hco3);
                 String fio2 = cells.get(5).getText();
                 ndzxxDto_jlthree.setFio2(fio2);
             }else if(i==10){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlthree.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String crp = cells.get(1).getText().replace("mg/L","");
                 ndzxxDto_jlthree.setCrp(crp);
                 String pct = cells.get(3).getText().replace("ng/ml","");
                 ndzxxDto_jlthree.setPct(pct);
                 String il6 = cells.get(5).getText().replace("pg/ml","");
                 ndzxxDto_jlthree.setIl6(il6);
                 String il1b = cells.get(7).getText().replace("pg/ml","");
                 ndzxxDto_jlthree.setIl1b(il1b);
             }else if(i==11){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlthree.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String il10 = cells.get(1).getText().replace("ng/ml","");
                 ndzxxDto_jlthree.setIl10(il10);
             }else if(i==13){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlthree.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String nsd = cells.get(1).getText().replace("mg/L","");
                 ndzxxDto_jlthree.setNsd(nsd);
                 String bdb = cells.get(3).getText().replace("g/L","");
                 ndzxxDto_jlthree.setBdb(bdb);
                 String jg = cells.get(5).getText().replace("μmol/L","");
                 ndzxxDto_jlthree.setJg(jg);
                 String zdhs = cells.get(7).getText().replace("μmol/L","");
                 ndzxxDto_jlthree.setZdhs(zdhs);
             }else if(i==15){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlthree.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String wbc = cells.get(1).getText().replace("L","");
                 ndzxxDto_jlthree.setWbc(wbc);
                 String plt = cells.get(3).getText().replace("L","");
                 ndzxxDto_jlthree.setPlt(plt);
                 String lcount = cells.get(5).getText().replace("L","");
                 ndzxxDto_jlthree.setLcount(lcount);
             }
         }
         //诊断脓毒症第五日信息提取
         NdzxxDto ndzxxDto_jlfive=new NdzxxDto();
         ndzxxDto_jlfive.setJldjt("第五日");
         ndzxxDto_jlfive.setHzxm(ndzxxDto.getHzxm());
         ndzxxDto_jlfive.setZyh(ndzxxDto.getZyh());
         ndzxxDto_jlfive.setNryjbh(ndzxxDto.getNryjbh());
         XWPFTable xwpfTable_jlfive = tables.get(4);
         int rcount_jlfive = xwpfTable_jlfive.getNumberOfRows();
         for (int i = 0; i < rcount_jlfive; i++){
             if(i==1) {
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlfive.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String hrmax = cells.get(1).getText().replace("次/分","");
                 ndzxxDto_jlfive.setHrmax(hrmax);
                 String mapmax = cells.get(3).getText().replace("mmHg","");
                 ndzxxDto_jlfive.setMapmax(mapmax);
                 String sapmax = cells.get(5).getText().replace("mmHg","");
                 ndzxxDto_jlfive.setSapmax(sapmax);
                 String rrmax = cells.get(7).getText().replace("次/分","");
                 ndzxxDto_jlfive.setRrmax(rrmax);
             }else if(i==2){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlfive.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String tmax = cells.get(1).getText().replace("℃","");
                 ndzxxDto_jlfive.setTmax(tmax);
             }else if(i==3){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlfive.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String jxtq = cells.get(1).getText();
                 ndzxxDto_jlfive.setJxtq(jxtq);
                 String crrt = cells.get(3).getText();
                 ndzxxDto_jlfive.setCrrt(crrt);
                 String gcs = cells.get(5).getText();
                 ndzxxDto_jlfive.setGcs(gcs);
             }else if(i==4){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlfive.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String xghxy = cells.get(1).getText();
                 ndzxxDto_jlfive.setXghxy(xghxy);
             }else if(i==6){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlfive.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String rs = cells.get(1).getText().replace("mmol/L","");
                 ndzxxDto_jlfive.setRs(rs);
                 String k = cells.get(3).getText().replace("mmol/L","");
                 ndzxxDto_jlfive.setK(k);
                 String na = cells.get(5).getText().replace("mmol/L","");
                 ndzxxDto_jlfive.setNa(na);
                 String ca = cells.get(7).getText().replace("mmol/L","");
                 ndzxxDto_jlfive.setCa(ca);
             }else if(i==7){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlfive.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String ph = cells.get(1).getText();
                 ndzxxDto_jlfive.setPh(ph);
                 String paco2 = cells.get(3).getText().replace("mmHg","");
                 ndzxxDto_jlfive.setPaco2(paco2);
                 String pao2 = cells.get(5).getText().replace("mmHg","");
                 ndzxxDto_jlfive.setPao2(pao2);
             }else if(i==8){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlfive.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String abe = cells.get(1).getText().replace("mmol/L","");
                 ndzxxDto_jlfive.setAbe(abe);
                 String hco3 = cells.get(3).getText().replace("mmol/L","");
                 ndzxxDto_jlfive.setHco3(hco3);
                 String fio2 = cells.get(5).getText();
                 ndzxxDto_jlfive.setFio2(fio2);
             }else if(i==10){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlfive.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String crp = cells.get(1).getText().replace("mg/L","");
                 ndzxxDto_jlfive.setCrp(crp);
                 String pct = cells.get(3).getText().replace("ng/ml","");
                 ndzxxDto_jlfive.setPct(pct);
                 String il6 = cells.get(5).getText().replace("pg/ml","");
                 ndzxxDto_jlfive.setIl6(il6);
                 String il1b = cells.get(7).getText().replace("pg/ml","");
                 ndzxxDto_jlfive.setIl1b(il1b);
             }else if(i==11){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlfive.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String il10 = cells.get(1).getText().replace("ng/ml","");
                 ndzxxDto_jlfive.setIl10(il10);
             }else if(i==13){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlfive.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String nsd = cells.get(1).getText().replace("mg/L","");
                 ndzxxDto_jlfive.setNsd(nsd);
                 String bdb = cells.get(3).getText().replace("g/L","");
                 ndzxxDto_jlfive.setBdb(bdb);
                 String jg = cells.get(5).getText().replace("μmol/L","");
                 ndzxxDto_jlfive.setJg(jg);
                 String zdhs = cells.get(7).getText().replace("μmol/L","");
                 ndzxxDto_jlfive.setZdhs(zdhs);
             }else if(i==15){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlfive.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String wbc = cells.get(1).getText().replace("L","");
                 ndzxxDto_jlfive.setWbc(wbc);
                 String plt = cells.get(3).getText().replace("L","");
                 ndzxxDto_jlfive.setPlt(plt);
                 String lcount = cells.get(5).getText().replace("L","");
                 ndzxxDto_jlfive.setLcount(lcount);
             }
         }
         //诊断脓毒症第七日信息提取
         NdzxxDto ndzxxDto_jlseven=new NdzxxDto();
         ndzxxDto_jlseven.setJldjt("第七日");
         ndzxxDto_jlseven.setHzxm(ndzxxDto.getHzxm());
         ndzxxDto_jlseven.setZyh(ndzxxDto.getZyh());
         ndzxxDto_jlseven.setNryjbh(ndzxxDto.getNryjbh());
         XWPFTable xwpfTable_jlseven = tables.get(5);
         int rcount_jlseven = xwpfTable_jlseven.getNumberOfRows();
         for (int i = 0; i < rcount_jlseven; i++){
             if(i==1) {
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlseven.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String hrmax = cells.get(1).getText().replace("次/分","");
                 ndzxxDto_jlseven.setHrmax(hrmax);
                 String mapmax = cells.get(3).getText().replace("mmHg","");
                 ndzxxDto_jlseven.setMapmax(mapmax);
                 String sapmax = cells.get(5).getText().replace("mmHg","");
                 ndzxxDto_jlseven.setSapmax(sapmax);
                 String rrmax = cells.get(7).getText().replace("次/分","");
                 ndzxxDto_jlseven.setRrmax(rrmax);
             }else if(i==2){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlseven.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String tmax = cells.get(1).getText().replace("℃","");
                 ndzxxDto_jlseven.setTmax(tmax);
             }else if(i==3){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlseven.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String jxtq = cells.get(1).getText();
                 ndzxxDto_jlseven.setJxtq(jxtq);
                 String crrt = cells.get(3).getText();
                 ndzxxDto_jlseven.setCrrt(crrt);
                 String gcs = cells.get(5).getText();
                 ndzxxDto_jlseven.setGcs(gcs);
             }else if(i==4){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlseven.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String xghxy = cells.get(1).getText();
                 ndzxxDto_jlseven.setXghxy(xghxy);
             }else if(i==6){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlseven.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String rs = cells.get(1).getText().replace("mmol/L","");
                 ndzxxDto_jlseven.setRs(rs);
                 String k = cells.get(3).getText().replace("mmol/L","");
                 ndzxxDto_jlseven.setK(k);
                 String na = cells.get(5).getText().replace("mmol/L","");
                 ndzxxDto_jlseven.setNa(na);
                 String ca = cells.get(7).getText().replace("mmol/L","");
                 ndzxxDto_jlseven.setCa(ca);
             }else if(i==7){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlseven.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String ph = cells.get(1).getText();
                 ndzxxDto_jlseven.setPh(ph);
                 String paco2 = cells.get(3).getText().replace("mmHg","");
                 ndzxxDto_jlseven.setPaco2(paco2);
                 String pao2 = cells.get(5).getText().replace("mmHg","");
                 ndzxxDto_jlseven.setPao2(pao2);
             }else if(i==8){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlseven.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String abe = cells.get(1).getText().replace("mmol/L","");
                 ndzxxDto_jlseven.setAbe(abe);
                 String hco3 = cells.get(3).getText().replace("mmol/L","");
                 ndzxxDto_jlseven.setHco3(hco3);
                 String fio2 = cells.get(5).getText();
                 ndzxxDto_jlseven.setFio2(fio2);
             }else if(i==10){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlseven.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String crp = cells.get(1).getText().replace("mg/L","");
                 ndzxxDto_jlseven.setCrp(crp);
                 String pct = cells.get(3).getText().replace("ng/ml","");
                 ndzxxDto_jlseven.setPct(pct);
                 String il6 = cells.get(5).getText().replace("pg/ml","");
                 ndzxxDto_jlseven.setIl6(il6);
                 String il1b = cells.get(7).getText().replace("pg/ml","");
                 ndzxxDto_jlseven.setIl1b(il1b);
             }else if(i==11){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlseven.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String il10 = cells.get(1).getText().replace("ng/ml","");
                 ndzxxDto_jlseven.setIl10(il10);
             }else if(i==13){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlseven.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String nsd = cells.get(1).getText().replace("mg/L","");
                 ndzxxDto_jlseven.setNsd(nsd);
                 String bdb = cells.get(3).getText().replace("g/L","");
                 ndzxxDto_jlseven.setBdb(bdb);
                 String jg = cells.get(5).getText().replace("μmol/L","");
                 ndzxxDto_jlseven.setJg(jg);
                 String zdhs = cells.get(7).getText().replace("μmol/L","");
                 ndzxxDto_jlseven.setZdhs(zdhs);
             }else if(i==15){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_jlseven.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 String wbc = cells.get(1).getText().replace("L","");
                 ndzxxDto_jlseven.setWbc(wbc);
                 String plt = cells.get(3).getText().replace("L","");
                 ndzxxDto_jlseven.setPlt(plt);
                 String lcount = cells.get(5).getText().replace("L","");
                 ndzxxDto_jlseven.setLcount(lcount);
             }
         }
         //转归情况信息提取
         XWPFTable xwpfTable_zgqk = tables.get(6);
         int rcount_zgqk = xwpfTable_zgqk.getNumberOfRows();
         for (int i = 0; i < rcount_zgqk; i++) {
             if (i == 0) {
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_zgqk.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 if(cells.get(0).getText().length()>11){
                     String s = cells.get(0).getText().replaceAll(" ","").substring(11);
                     String xghxytysj = s.substring(0, 10) + " " + s.substring(10);
                     ndzxxDto.setXghxytysj(xghxytysj);
                 }
             }else if(i == 1){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_zgqk.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 if(cells.get(0).getText().length()>9){
                     String s = cells.get(0).getText().replaceAll(" ","").substring(9);
                     String crrttysj = s.substring(0, 10) + " " + s.substring(10);
                     ndzxxDto.setCrrttysj(crrttysj);
                 }
             }else if(i == 2){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_zgqk.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 if(cells.get(0).getText().length()>8){
                     String s = cells.get(0).getText().replaceAll(" ","").substring(8);
                     String hxjtysj = s.substring(0, 10) + " " + s.substring(10);
                     ndzxxDto.setHxjtysj(hxjtysj);
                 }
             }else if(i == 3){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_zgqk.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 if(cells.get(0).getText().length()>7){
                     String s = cells.get(0).getText().replaceAll(" ","").substring(7);
                     String cicusj = s.substring(0, 10) + " " + s.substring(10);
                     ndzxxDto.setCicusj(cicusj);
                 }
             }else if(i == 4){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_zgqk.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 if(cells.get(0).getText().length()>7){
                     String cicuzt = cells.get(0).getText().replaceAll(" ","").substring(7);
                     ndzxxDto.setCicuzt(cicuzt);
                 }
             }else if(i == 5){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_zgqk.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 if(cells.get(0).getText().length()>5){
                     String s = cells.get(0).getText().replaceAll(" ","").substring(5);
                     String cysj = s.substring(0, 10) + " " + s.substring(10);
                    ndzxxDto.setCysj(cysj);
                 }
             }else if(i == 6){
                 //拿到当前行的内容
                 XWPFTableRow row = xwpfTable_zgqk.getRow(i);
                 //获取当前行的列数
                 List<XWPFTableCell> cells = row.getTableCells();
                 if(cells.get(0).getText().length()>5){
                     String cyzt = cells.get(0).getText().replaceAll(" ","").substring(5);
                     ndzxxDto.setCyzt(cyzt);
                 }
             }
         }
         List<NdzxxDto> list=new ArrayList<>();
         list.add(ndzxxDto);
         list.add(ndzxxDto_jlthree);
         list.add(ndzxxDto_jlfive);
         list.add(ndzxxDto_jlseven);
         return list;
    }

}

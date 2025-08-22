package com.matridx.crf.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.crf.web.dao.entities.*;
import com.matridx.crf.web.dao.post.*;
import com.matridx.crf.web.enums.YYSEnum;
import com.matridx.crf.web.service.svcinterface.*;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.post.IJcsjDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class JnsjbgjlServiceImpl extends BaseBasicServiceImpl<JnsjbgjlDto, JnsjbgjlModel, IJnsjbgjlDao> implements IJnsjbgjlService {

    @Autowired
    IJnsjhzxxService jnsjhzxxService;
    @Autowired
    IJnsjcdizlService jnsjcdizlService;
    @Autowired
    IJnsjjcjgService jnsjjcjgService;
    @Autowired
    IJnsjcdiqyysService jnsjcdiqyysService;
    @Autowired
    IJnsjhzxxDao jnsjhzxxDao;
    @Autowired
    IJnsjcdizlDao jnsjcdizlDao;
    @Autowired
    IJnsjjcjgDao jnsjjcjgDao;
    @Autowired
    IJnsjcdiqyysDao jnsjcdiqyysDao;
    @Autowired
    IJcsjDao jcsjDao;
    /**
     * 新增艰难梭菌报告记录
     * @return
     */
    @Override
    @Transactional
    public boolean insertJnsj(JSONObject json) {
        String uuid= StringUtil.generateUUID();
        //艰难梭菌报告记录
        JnsjbgjlDto jnsjbgjlDto =new JnsjbgjlDto();
        jnsjbgjlDto.setJnsjbgid(uuid);
        jnsjbgjlDto.setBlrzdw(json.getString("blrzdw"));
        jnsjbgjlDto.setBlrzbh(json.getString("blrzbh"));
        jnsjbgjlDto.setZyh(json.getString("zyh"));
        jnsjbgjlDto.setKs(json.getString("ks"));
        jnsjbgjlDto.setJzlx(json.getString("jzlx"));
        jnsjbgjlDto.setTbsj(json.getString("tbsj"));
        jnsjbgjlDto.setSffx(json.getString("sffx"));
        jnsjbgjlDto.setFxpl(json.getString("fxpl"));
        jnsjbgjlDto.setSffz(json.getString("sffz"));
        jnsjbgjlDto.setSfyzdxjjc(json.getString("sfyzdxjjc"));
        jnsjbgjlDto.setSfcgz(json.getString("sfcgz"));
        jnsjbgjlDto.setSfxk(json.getString("sfxk"));
        jnsjbgjlDto.setSffz(json.getString("sffz"));
        jnsjbgjlDto.setSfdxy(json.getString("sfdxy"));
        jnsjbgjlDto.setDbxz(json.getString("dbxz"));
        jnsjbgjlDto.setSffz(json.getString("sffz"));
        jnsjbgjlDto.setFyx(json.getString("fyx"));
        jnsjbgjlDto.setFhxb(json.getString("fhxb"));
        jnsjbgjlDto.setFbxb(json.getString("fbxb"));
        jnsjbgjlDto.setFjmyzj(json.getString("fjmyzj"));
        jnsjbgjlDto.setZtdb(json.getString("ztdb"));
        jnsjbgjlDto.setWbc(json.getString("wbc"));
        jnsjbgjlDto.setN(json.getString("n"));
        jnsjbgjlDto.setL(json.getString("l"));
        jnsjbgjlDto.setHb(json.getString("hb"));
        jnsjbgjlDto.setPlt(json.getString("ptl"));
        jnsjbgjlDto.setAlb(json.getString("alb"));
        jnsjbgjlDto.setCk(json.getString("ck"));
        jnsjbgjlDto.setTbil(json.getString("tbil"));
        jnsjbgjlDto.setBun(json.getString("bun"));
        jnsjbgjlDto.setCr(json.getString("cr"));
        jnsjbgjlDto.setEsr(json.getString("esr"));
        jnsjbgjlDto.setCrp(json.getString("crp"));
        jnsjbgjlDto.setPct(json.getString("pct"));
        jnsjbgjlDto.setGwdb(json.getString("gwdb"));
        jnsjbgjlDto.setPlt(json.getString("plt"));
        jnsjbgjlDto.setSfywmxcy(json.getString("sfywmxcy"));
        jnsjbgjlDto.setSfzlcdi(json.getString("sfzlcdi"));
        jnsjbgjlDto.setSfsw(json.getString("sfsw"));
        jnsjbgjlDto.setLrry(json.getString("userId"));
        jnsjbgjlDto.setLrsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
        jnsjbgjlDto.setScbj("0");

        //艰难梭菌CDI治疗
        JSONArray jnsjcdiArr= JSONObject.parseArray(json.getString("zlList"));
        List<JnsjcdizlDto> cdizlList=new ArrayList<>();
        for(int i=0;i<jnsjcdiArr.size();i++){
            JnsjcdizlDto jnsjcdizlDto =new JnsjcdizlDto();
            JSONObject job=(JSONObject)jnsjcdiArr.get(i);
            jnsjcdizlDto.setZlid(StringUtil.generateUUID());
            jnsjcdizlDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizlDto.setZlyw(job.getString("zlyw"));
            jnsjcdizlDto.setDcjl(job.getString("dcjl"));
            jnsjcdizlDto.setSyff(job.getString("syff"));
            jnsjcdizlDto.setKsrq(job.getString("ksrq"));
            jnsjcdizlDto.setJsrq(job.getString("jsrq"));
            jnsjcdizlDto.setScbj("0");
            cdizlList.add(jnsjcdizlDto);
        }
        //检测结果
        JSONArray jcjgList= JSONObject.parseArray(json.getString("jcsjjgList"));
        List<JnsjjcjgDto> jnsjjcjgDtoList=new ArrayList<>();
        for(int i=0;i<jcjgList.size();i++){
            JnsjjcjgDto jnsjjcjgDto =new JnsjjcjgDto();
            JSONObject job=(JSONObject)jcjgList.get(i);
            jnsjjcjgDto.setJnsjjcjgid(StringUtil.generateUUID());
            jnsjjcjgDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjjcjgDto.setJnsjjcsj(job.getString("jnsjjcsj"));
            jnsjjcjgDto.setGdhjg(job.getString("gdhjg"));
            jnsjjcjgDto.setToxinjg(job.getString("toxinjg"));
            jnsjjcjgDto.setDbjnsjpyjg(job.getString("dbjnsjpyjg"));
            jnsjjcjgDto.setJnsjmspyjg(job.getString("jnsjmspyjg"));
            jnsjjcjgDto.setScbj("0");
            jnsjjcjgDtoList.add(jnsjjcjgDto);
        }
        //艰难梭菌CDI之前用药历史

        List<JnsjcdiqyysDto> jnsjcdizqyssDtoList=new ArrayList<>();
        JSONObject nxalKssObj=JSONObject.parseObject(json.getString("nxakssList"));
        JSONArray nxalKssArr= JSONObject.parseArray(nxalKssObj.getString("list"));
        //β内酰胺类抗生素
        for(int i=0;i<nxalKssArr.size();i++){

            JSONObject job=(JSONObject)nxalKssArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                    continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(nxalKssObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //大环内酯类抗生素
        JSONObject dhnzlKssObj=JSONObject.parseObject(json.getString("dhnzlkssList"));
        JSONArray dhnzlKssArr= JSONObject.parseArray(dhnzlKssObj.getString("list"));
        for(int i=0;i<dhnzlKssArr.size();i++){
            JSONObject job=(JSONObject)dhnzlKssArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(dhnzlKssObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //氨基甙类抗生素
        JSONObject ajglKssObj=JSONObject.parseObject(json.getString("ajglkssList"));
        JSONArray ajglKssArr= JSONObject.parseArray(ajglKssObj.getString("list"));
        for(int i=0;i<ajglKssArr.size();i++){
            JSONObject job=(JSONObject)ajglKssArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(ajglKssObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //四环素类抗生素
        JSONObject shslKssObj=JSONObject.parseObject(json.getString("shslkssList"));
        JSONArray shslKssArr= JSONObject.parseArray(shslKssObj.getString("list"));
        for(int i=0;i<shslKssArr.size();i++){
            JSONObject job=(JSONObject)shslKssArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(shslKssObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //林可霉素类抗生素
        JSONObject lkmslKssObj=JSONObject.parseObject(json.getString("lkmslkssList"));
        JSONArray lkmslKssArr= JSONObject.parseArray(lkmslKssObj.getString("list"));
        for(int i=0;i<lkmslKssArr.size();i++){
            JSONObject job=(JSONObject)lkmslKssArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(lkmslKssObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //氯霉素类抗生素
        JSONObject lmslKssObj=JSONObject.parseObject(json.getString("lmslkssList"));
        JSONArray lmslKssArr= JSONObject.parseArray(lmslKssObj.getString("list"));
        for(int i=0;i<lmslKssArr.size();i++){
            JSONObject job=(JSONObject)lmslKssArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(lmslKssObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //万古霉素类
        JSONObject wgmslObj=JSONObject.parseObject(json.getString("wgmslList"));
        JSONArray wgmslArr= JSONObject.parseArray(wgmslObj.getString("list"));
        for(int i=0;i<wgmslArr.size();i++){
            JSONObject job=(JSONObject)wgmslArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(wgmslObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //喹诺酮类药物
        JSONObject kntllObj=JSONObject.parseObject(json.getString("kntllList"));
        JSONArray kntllArr= JSONObject.parseArray(kntllObj.getString("list"));
        for(int i=0;i<kntllArr.size();i++){
            JSONObject job=(JSONObject)kntllArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(kntllObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(kntllObj.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //质子泵抑制剂(抑酸类药物)：
        JSONObject zzbyzjObj=JSONObject.parseObject(json.getString("zzbyzjList"));
        JSONArray zzbyzjArr= JSONObject.parseArray(zzbyzjObj.getString("list"));
        for(int i=0;i<zzbyzjArr.size();i++){
            JSONObject job=(JSONObject)zzbyzjArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(zzbyzjObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //生物碱类：
        JSONObject swjlObj=JSONObject.parseObject(json.getString("swjlList"));
        JSONArray swjlArr= JSONObject.parseArray(swjlObj.getString("list"));
        for(int i=0;i<swjlArr.size();i++){
            JSONObject job=(JSONObject)swjlArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(swjlObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //代谢类
        JSONObject dxlObj=JSONObject.parseObject(json.getString("dxlList"));
        JSONArray dxlArr= JSONObject.parseArray(dxlObj.getString("list"));
        for(int i=0;i<dxlArr.size();i++){
            JSONObject job=(JSONObject)dxlArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(dxlObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //抗生素类
        JSONObject ksslObj=JSONObject.parseObject(json.getString("ksslList"));
        JSONArray ksslArr= JSONObject.parseArray(ksslObj.getString("list"));
        for(int i=0;i<ksslArr.size();i++){
            JSONObject job=(JSONObject)ksslArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(ksslObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //烷化剂类
        JSONObject whjlObj=JSONObject.parseObject(json.getString("whjlList"));
        JSONArray whjlArr= JSONObject.parseArray(whjlObj.getString("list"));
        for(int i=0;i<whjlArr.size();i++){
            JSONObject job=(JSONObject)whjlArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(whjlObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //铂剂类
        JSONObject bjlObj=JSONObject.parseObject(json.getString("bjlList"));
        JSONArray bjlArr= JSONObject.parseArray(bjlObj.getString("list"));
        for(int i=0;i<bjlArr.size();i++){
            JSONObject job=(JSONObject)bjlArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(bjlObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }

        //艰难梭菌患者信息
        JnsjhzxxDto jnsjhzxxDto =new JnsjhzxxDto();
        jnsjhzxxDto.setJnsjhzid(uuid);
        jnsjbgjlDto.setJnsjhzid(jnsjhzxxDto.getJnsjhzid());
        jnsjhzxxDto.setXm(json.getString("xm"));
        jnsjhzxxDto.setXb(json.getString("xb"));
        jnsjhzxxDto.setNl(json.getString("nl"));
        jnsjhzxxDto.setCopd(json.getString("copd"));
        jnsjhzxxDto.setXyxtexzl(json.getString("xyxtexzl"));
        jnsjhzxxDto.setStqgyz(json.getString("stqgyz"));
        jnsjhzxxDto.setCqjsjszl(json.getString("cqjsjszl"));
        jnsjhzxxDto.setZsmyjb(json.getString("zsmyjb"));
        jnsjhzxxDto.setTnb(json.getString("tnb"));
        jnsjhzxxDto.setZlxjb(json.getString("zlxjb"));
        jnsjhzxxDto.setGxb(json.getString("gxb"));
        jnsjhzxxDto.setNxgjb(json.getString("nxgjb"));
        jnsjhzxxDto.setMxsb(json.getString("mxsb"));
        jnsjhzxxDto.setYzxcb(json.getString("yzxcb"));
        jnsjhzxxDto.setSgyqsfyzys(json.getString("sgyqsfyzys"));
        jnsjhzxxDto.setRysj(json.getString("rysj"));
        jnsjhzxxDto.setCysj(json.getString("cysj"));
        jnsjhzxxDto.setZyhf(json.getString("zyhf"));
        jnsjhzxxDto.setCyzd(json.getString("cyzd"));
        jnsjhzxxDto.setLrry(json.getString("userId"));
        jnsjhzxxDto.setLrsj(DateUtils.getCustomFomratCurrentDate("yyyy/MM/dd HH:mm:ss"));
        jnsjhzxxDto.setScbj("0");
        jnsjhzxxService.insertDto(jnsjhzxxDto);
        jnsjcdiqyysService.insertList(jnsjcdizqyssDtoList);
        jnsjjcjgService.insertList(jnsjjcjgDtoList);
        jnsjcdizlService.inserList(cdizlList);
        dao.insertDto(jnsjbgjlDto);
        return true;
    }

    @Override
    @Transactional
    public boolean updateJnsj(JSONObject json) {
        JnsjbgjlDto jnsjbgjlDto =new JnsjbgjlDto();
        jnsjbgjlDto.setJnsjbgid(json.getString("jnsjbgid"));
        jnsjbgjlDto=dao.getDtoById(jnsjbgjlDto);
        jnsjbgjlDto.setBlrzdw(json.getString("blrzdw"));
        jnsjbgjlDto.setBlrzbh(json.getString("blrzbh"));
        jnsjbgjlDto.setZyh(json.getString("zyh"));
        jnsjbgjlDto.setKs(json.getString("ks"));
        jnsjbgjlDto.setJzlx(json.getString("jzlx"));
        jnsjbgjlDto.setTbsj(json.getString("tbsj"));
        jnsjbgjlDto.setFyx(json.getString("fyx"));
        jnsjbgjlDto.setSffx(json.getString("sffx"));
        jnsjbgjlDto.setFxpl(json.getString("fxpl"));
        jnsjbgjlDto.setSffz(json.getString("sffz"));
        jnsjbgjlDto.setSfyzdxjjc(json.getString("sfyzdxjjc"));
        jnsjbgjlDto.setSfcgz(json.getString("sfcgz"));
        jnsjbgjlDto.setSfxk(json.getString("sfxk"));
        jnsjbgjlDto.setSffz(json.getString("sffz"));
        jnsjbgjlDto.setSfdxy(json.getString("sfdxy"));
        jnsjbgjlDto.setDbxz(json.getString("dbxz"));
        jnsjbgjlDto.setSffz(json.getString("sffz"));
        jnsjbgjlDto.setFhxb(json.getString("fhxb"));
        jnsjbgjlDto.setFbxb(json.getString("fbxb"));
        jnsjbgjlDto.setFjmyzj(json.getString("fjmyzj"));
        jnsjbgjlDto.setZtdb(json.getString("ztdb"));
        jnsjbgjlDto.setWbc(json.getString("wbc"));
        jnsjbgjlDto.setN(json.getString("n"));
        jnsjbgjlDto.setL(json.getString("l"));
        jnsjbgjlDto.setHb(json.getString("hb"));
        jnsjbgjlDto.setPlt(json.getString("plt"));
        jnsjbgjlDto.setAlb(json.getString("alb"));
        jnsjbgjlDto.setCk(json.getString("ck"));
        jnsjbgjlDto.setGwdb(json.getString("gwdb"));
        jnsjbgjlDto.setTbil(json.getString("tbil"));
        jnsjbgjlDto.setBun(json.getString("bun"));
        jnsjbgjlDto.setCr(json.getString("cr"));
        jnsjbgjlDto.setEsr(json.getString("esr"));
        jnsjbgjlDto.setCrp(json.getString("crp"));
        jnsjbgjlDto.setPct(json.getString("pct"));
        jnsjbgjlDto.setSfywmxcy(json.getString("sfywmxcy"));
        jnsjbgjlDto.setSfzlcdi(json.getString("sfz;cdi"));
        jnsjbgjlDto.setSfsw(json.getString("sfsw"));
        jnsjbgjlDto.setXgry(json.getString("userId"));
        jnsjbgjlDto.setXgsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
        //艰难梭菌患者信息
        JnsjhzxxDto jnsjhzxxDto =new JnsjhzxxDto();
        jnsjhzxxDto.setJnsjhzid(jnsjbgjlDto.getJnsjhzid());
        jnsjhzxxDto.setXm(json.getString("xm"));
        jnsjhzxxDto.setXb(json.getString("xb"));
        jnsjhzxxDto.setNl(json.getString("nl"));
        jnsjhzxxDto.setCopd(json.getString("copd"));
        jnsjhzxxDto.setXyxtexzl(json.getString("xyxtexzl"));
        jnsjhzxxDto.setStqgyz(json.getString("stqgyz"));
        jnsjhzxxDto.setCqjsjszl(json.getString("cqjsjszl"));
        jnsjhzxxDto.setZsmyjb(json.getString("zsmyjb"));
        jnsjhzxxDto.setTnb(json.getString("tnb"));
        jnsjhzxxDto.setZlxjb(json.getString("zlxjb"));
        jnsjhzxxDto.setGxb(json.getString("gxb"));
        jnsjhzxxDto.setNxgjb(json.getString("nxgjb"));
        jnsjhzxxDto.setMxsb(json.getString("mxsb"));
        jnsjhzxxDto.setYzxcb(json.getString("yzxcb"));
        jnsjhzxxDto.setSgyqsfyzys(json.getString("sgyqsfyzys"));
        jnsjhzxxDto.setRysj(json.getString("rysj"));
        jnsjhzxxDto.setCysj(json.getString("cysj"));
        jnsjhzxxDto.setZyhf(json.getString("zyhf"));
        jnsjhzxxDto.setCyzd(json.getString("cyzd"));
        jnsjhzxxDto.setXgry(json.getString("userId"));
        jnsjhzxxDto.setXgsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
        //艰难梭菌CDI治疗
        JSONArray jnsjcdiArr= JSONObject.parseArray(json.getString("zlList"));
        List<JnsjcdizlDto> cdizlList=new ArrayList<>();
        for(int i=0;i<jnsjcdiArr.size();i++){
            JnsjcdizlDto jnsjcdizlDto =new JnsjcdizlDto();
            JSONObject job=(JSONObject)jnsjcdiArr.get(i);

            jnsjcdizlDto.setZlid(StringUtil.generateUUID());
            jnsjcdizlDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizlDto.setZlyw(job.getString("zlyw"));
            jnsjcdizlDto.setDcjl(job.getString("dcjl"));
            jnsjcdizlDto.setSyff(job.getString("syff"));
            jnsjcdizlDto.setKsrq(job.getString("ksrq"));
            jnsjcdizlDto.setJsrq(job.getString("jsrq"));
            jnsjcdizlDto.setScbj("0");
            cdizlList.add(jnsjcdizlDto);
        }
        //检测结果
        JSONArray jcjgList= JSONObject.parseArray(json.getString("jcsjjgList"));
        List<JnsjjcjgDto> jnsjjcjgDtoList=new ArrayList<>();
        for(int i=0;i<jcjgList.size();i++){
            JnsjjcjgDto jnsjjcjgDto =new JnsjjcjgDto();
            JSONObject job=(JSONObject)jcjgList.get(i);
            jnsjjcjgDto.setJnsjjcjgid(StringUtil.generateUUID());
            jnsjjcjgDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjjcjgDto.setJnsjjcsj(job.getString("jnsjjcsj"));
            jnsjjcjgDto.setGdhjg(job.getString("gdhjg"));
            jnsjjcjgDto.setToxinjg(job.getString("toxinjg"));
            jnsjjcjgDto.setDbjnsjpyjg(job.getString("dbjnsjpyjg"));
            jnsjjcjgDto.setJnsjmspyjg(job.getString("jnsjmspyjg"));
            jnsjjcjgDto.setScbj("0");
            jnsjjcjgDtoList.add(jnsjjcjgDto);
        }
        //艰难梭菌CDI之前用药历史

        List<JnsjcdiqyysDto> jnsjcdizqyssDtoList=new ArrayList<>();
        JSONObject nxalKssObj=JSONObject.parseObject(json.getString("nxakssList"));
        JSONArray nxalKssArr= JSONObject.parseArray(nxalKssObj.getString("list"));
        //β内酰胺类抗生素
        for(int i=0;i<nxalKssArr.size();i++){
            JSONObject job=(JSONObject)nxalKssArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(nxalKssObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //大环内酯类抗生素
        JSONObject dhnzlKssObj=JSONObject.parseObject(json.getString("dhnzlkssList"));
        JSONArray dhnzlKssArr= JSONObject.parseArray(dhnzlKssObj.getString("list"));
        for(int i=0;i<dhnzlKssArr.size();i++){
            JSONObject job=(JSONObject)dhnzlKssArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(dhnzlKssObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //氨基甙类抗生素
        JSONObject ajglKssObj=JSONObject.parseObject(json.getString("ajglkssList"));
        JSONArray ajglKssArr= JSONObject.parseArray(ajglKssObj.getString("list"));
        for(int i=0;i<ajglKssArr.size();i++){
            JSONObject job=(JSONObject)ajglKssArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(ajglKssObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //四环素类抗生素
        JSONObject shslKssObj=JSONObject.parseObject(json.getString("shslkssList"));
        JSONArray shslKssArr= JSONObject.parseArray(shslKssObj.getString("list"));
        for(int i=0;i<shslKssArr.size();i++){
            JSONObject job=(JSONObject)shslKssArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(shslKssObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //林可霉素类抗生素
        JSONObject lkmslKssObj=JSONObject.parseObject(json.getString("lkmslkssList"));
        JSONArray lkmslKssArr= JSONObject.parseArray(lkmslKssObj.getString("list"));
        for(int i=0;i<lkmslKssArr.size();i++){
            JSONObject job=(JSONObject)lkmslKssArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(lkmslKssObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //氯霉素类抗生素
        JSONObject lmslKssObj=JSONObject.parseObject(json.getString("lmslkssList"));
        JSONArray lmslKssArr= JSONObject.parseArray(lmslKssObj.getString("list"));
        for(int i=0;i<lmslKssArr.size();i++){
            JSONObject job=(JSONObject)lmslKssArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(lmslKssObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //万古霉素类
        JSONObject wgmslObj=JSONObject.parseObject(json.getString("wgmslList"));
        JSONArray wgmslArr= JSONObject.parseArray(wgmslObj.getString("list"));
        for(int i=0;i<wgmslArr.size();i++){
            JSONObject job=(JSONObject)wgmslArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(wgmslObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //喹诺酮类药物
        JSONObject kntllObj=JSONObject.parseObject(json.getString("kntllList"));
        JSONArray kntllArr= JSONObject.parseArray(kntllObj.getString("list"));
        for(int i=0;i<kntllArr.size();i++){
            JSONObject job=(JSONObject)kntllArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(kntllObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(kntllObj.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //质子泵抑制剂(抑酸类药物)：
        JSONObject zzbyzjObj=JSONObject.parseObject(json.getString("zzbyzjList"));
        JSONArray zzbyzjArr= JSONObject.parseArray(zzbyzjObj.getString("list"));
        for(int i=0;i<zzbyzjArr.size();i++){
            JSONObject job=(JSONObject)zzbyzjArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(zzbyzjObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //生物碱类：
        JSONObject swjlObj=JSONObject.parseObject(json.getString("swjlList"));
        JSONArray swjlArr= JSONObject.parseArray(swjlObj.getString("list"));
        for(int i=0;i<swjlArr.size();i++){
            JSONObject job=(JSONObject)swjlArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(swjlObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //代谢类
        JSONObject dxlObj=JSONObject.parseObject(json.getString("dxlList"));
        JSONArray dxlArr= JSONObject.parseArray(dxlObj.getString("list"));
        for(int i=0;i<dxlArr.size();i++){
            JSONObject job=(JSONObject)dxlArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(dxlObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //抗生素类
        JSONObject ksslObj=JSONObject.parseObject(json.getString("ksslList"));
        JSONArray ksslArr= JSONObject.parseArray(ksslObj.getString("list"));
        for(int i=0;i<ksslArr.size();i++){
            JSONObject job=(JSONObject)ksslArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(ksslObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //烷化剂类
        JSONObject whjlObj=JSONObject.parseObject(json.getString("whjlList"));
        JSONArray whjlArr= JSONObject.parseArray(whjlObj.getString("list"));
        for(int i=0;i<whjlArr.size();i++){
            JSONObject job=(JSONObject)whjlArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(whjlObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        //铂剂类
        JSONObject bjlObj=JSONObject.parseObject(json.getString("bjlList"));
        JSONArray bjlArr= JSONObject.parseArray(bjlObj.getString("list"));
        for(int i=0;i<bjlArr.size();i++){
            JSONObject job=(JSONObject)bjlArr.get(i);
            if(!StringUtils.isNotBlank(job.getString("ywzlx"))){
                continue;
            }
            JnsjcdiqyysDto jnsjcdizqyssDto=new JnsjcdiqyysDto();
            jnsjcdizqyssDto.setYysid(StringUtil.generateUUID());
            jnsjcdizqyssDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
            jnsjcdizqyssDto.setJl(job.getString("jl"));
            jnsjcdizqyssDto.setYwlx(bjlObj.getString("ywlx"));
            jnsjcdizqyssDto.setYwzlx(job.getString("ywzlx"));
            jnsjcdizqyssDto.setKsrq(job.getString("ksrq"));
            jnsjcdizqyssDto.setTyrq(job.getString("tyrq"));
            jnsjcdizqyssDto.setQtywmc(job.getString("qtywmc"));
            jnsjcdizqyssDto.setScbj("0");
            jnsjcdizqyssDtoList.add(jnsjcdizqyssDto);
        }
        JnsjjcjgDto jnsjjcjgDto =new JnsjjcjgDto();
        jnsjjcjgDto.setScbj("1");
        jnsjjcjgDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
        jnsjjcjgDao.delJnsjjcjg(jnsjjcjgDto);
        JnsjcdizlDto jnsjcdizlDto = new JnsjcdizlDto();
        jnsjcdizlDto.setScbj("1");
        jnsjcdizlDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
        jnsjcdizlDao.delJncjcdizl(jnsjcdizlDto);
        JnsjcdiqyysDto jnsjcdiqyysDto =new JnsjcdiqyysDto();
        jnsjcdiqyysDto.setScbj("1");
        jnsjcdiqyysDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
        jnsjcdiqyysDao.delJncjcdiqyys(jnsjcdiqyysDto);
        jnsjhzxxService.updateDto(jnsjhzxxDto);
        jnsjcdiqyysService.insertList(jnsjcdizqyssDtoList);
        jnsjjcjgService.insertList(jnsjjcjgDtoList);
        jnsjcdizlService.inserList(cdizlList);
        dao.updateDto(jnsjbgjlDto);
        return true;
    }

    @Override
    public Map<String, Object> getDateByBgId(JnsjbgjlDto dto) {
        Map<String, Object> map =null;
        if(StringUtil.isNotBlank(dto.getJnsjbgid())){
            map =new HashMap<>();
            JnsjbgjlDto jnsjbgjlDto = dao.getDto(dto);
            JnsjhzxxDto jnsjhzxxDto=new JnsjhzxxDto();
            jnsjhzxxDto.setJnsjhzid(jnsjbgjlDto.getJnsjhzid());
             jnsjhzxxDto = jnsjhzxxDao.getDtoById(jnsjhzxxDto);
            JnsjcdizlDto cdiParmDto = new JnsjcdizlDto();
            cdiParmDto.setJnsjbgid(dto.getJnsjbgid());
            List<JnsjcdizlDto> jnsjcdizlDtoList = jnsjcdizlDao.getListByBgId(cdiParmDto);
            JnsjjcjgDto jcjgParmDto =new JnsjjcjgDto();
            jcjgParmDto.setJnsjbgid(dto.getJnsjbgid());
            List<JnsjjcjgDto> jnsjjcjgDtoList = jnsjjcjgDao.getDtoList(jcjgParmDto);
            JnsjcdiqyysDto cdiqyysParamDto = new JnsjcdiqyysDto();
            cdiqyysParamDto.setJnsjbgid(dto.getJnsjbgid());
            List<JnsjcdiqyysDto> jnsjcdiqyysDtoList = jnsjcdiqyysDao.getDtoByList(cdiqyysParamDto);
            //患者信息
            map.put("xm",jnsjhzxxDto.getXm());
            map.put("nl",jnsjhzxxDto.getNl());
            map.put("xb",jnsjhzxxDto.getXb());
            map.put("copd",jnsjhzxxDto.getCopd());
            map.put("xyxtexzl",jnsjhzxxDto.getXyxtexzl());
            map.put("stqgyz",jnsjhzxxDto.getStqgyz());
            map.put("cqjsjszl",jnsjhzxxDto.getCqjsjszl());
            map.put("tnb",jnsjhzxxDto.getTnb());
            map.put("zlxjb",jnsjhzxxDto.getZlxjb());
            map.put("zsmyjb",jnsjhzxxDto.getZsmyjb());
            map.put("gxb",jnsjhzxxDto.getGxb());
            map.put("nxgjb",jnsjhzxxDto.getNxgjb());
            map.put("yzxcb",jnsjhzxxDto.getYzxcb());
            map.put("sgyqsfyzys",jnsjhzxxDto.getSgyqsfyzys());
            map.put("rysj",jnsjhzxxDto.getRysj());
            map.put("zyhf",jnsjhzxxDto.getZyhf());
            map.put("mxsb",jnsjhzxxDto.getMxsb());
            map.put("cysj",jnsjhzxxDto.getCysj());
            map.put("cyzd",jnsjhzxxDto.getCyzd());
            map.put("jnsjhzid",jnsjhzxxDto.getJnsjhzid());
            //患者报告
            map.put("blrzdw",jnsjbgjlDto.getBlrzdw());
            map.put("jnsjbgid",jnsjbgjlDto.getJnsjbgid());
            map.put("blrzbh",jnsjbgjlDto.getBlrzbh());
            map.put("tbsj",jnsjbgjlDto.getTbsj());
            map.put("zyh",jnsjbgjlDto.getZyh());
            map.put("ks",jnsjbgjlDto.getKs());
            map.put("jzlx",jnsjbgjlDto.getJzlx());
            map.put("sffx",jnsjbgjlDto.getSffx());
            map.put("fxpl",jnsjbgjlDto.getFxpl());
            map.put("sffz",jnsjbgjlDto.getSffz());
            map.put("sfyzdxjjc",jnsjbgjlDto.getSfyzdxjjc());
            map.put("sfcgz",jnsjbgjlDto.getSfcgz());
            map.put("sfxk",jnsjbgjlDto.getSfxk());
            map.put("sfdxy",jnsjbgjlDto.getSfdxy());
            map.put("dbxz",jnsjbgjlDto.getDbxz());
            map.put("fhxb",jnsjbgjlDto.getFhxb());
            map.put("fbxb",jnsjbgjlDto.getFbxb());
            map.put("fjmyzj",jnsjbgjlDto.getFjmyzj());
            map.put("fyx",jnsjbgjlDto.getFyx());
            map.put("ztdb",jnsjbgjlDto.getZtdb());
            map.put("gwdb",jnsjbgjlDto.getGwdb());
            map.put("wbc",jnsjbgjlDto.getWbc());
            map.put("n",jnsjbgjlDto.getN());
            map.put("l",jnsjbgjlDto.getL());
            map.put("hb",jnsjbgjlDto.getHb());
            map.put("plt",jnsjbgjlDto.getPlt());
            map.put("ck",jnsjbgjlDto.getCk());
            map.put("alb",jnsjbgjlDto.getAlb());
            map.put("tbil",jnsjbgjlDto.getTbil());
            map.put("bun",jnsjbgjlDto.getBun());
            map.put("cr",jnsjbgjlDto.getCr());
            map.put("esr",jnsjbgjlDto.getEsr());
            map.put("crp",jnsjbgjlDto.getCrp());
            map.put("pct",jnsjbgjlDto.getPct());
            map.put("sfywmxcy",jnsjbgjlDto.getSfywmxcy());
            map.put("sfzlcdi",jnsjbgjlDto.getSfzlcdi());
            map.put("sfsw",jnsjbgjlDto.getSfsw());
            //治疗
            map.put("jcsjjgList",jnsjjcjgDtoList);
            //检测结果
            map.put("zlList",jnsjcdizlDtoList);
            //艰难梭菌CDI之前用药历史
            Map<String,Object> nxakssList = new HashMap<>();
            List<Object> selectList =new ArrayList<>();
            nxakssList.put("list",new ArrayList<JnsjcdiqyysDto>());
            nxakssList.put("selectList",selectList);
            nxakssList.put("ywlx",YYSEnum.YYS_NXALKSS.getCode());
            Map<String,Object> dhnzlkssList = new HashMap<>();
            dhnzlkssList.put("list",new ArrayList<JnsjcdiqyysDto>());
            dhnzlkssList.put("selectList",selectList);
            dhnzlkssList.put("list",new ArrayList<JnsjcdiqyysDto>());
            dhnzlkssList.put("ywlx",YYSEnum.YYS_DHNZLKSS.getCode());
            Map<String,Object> ajglkssList = new HashMap<>();
            ajglkssList.put("selectList",selectList);
            ajglkssList.put("list",new ArrayList<JnsjcdiqyysDto>());
            ajglkssList.put("ywlx",YYSEnum.YYS_AJGLKSS.getCode());
            Map<String,Object> shslkssList = new HashMap<>();
            shslkssList.put("selectList",selectList);
            shslkssList.put("list",new ArrayList<JnsjcdiqyysDto>());
            shslkssList.put("ywlx",YYSEnum.YYS_SHSLKSS.getCode());
            Map<String,Object> lkmslkssList = new HashMap<>();
            lkmslkssList.put("selectList",selectList);
            lkmslkssList.put("list",new ArrayList<JnsjcdiqyysDto>());
            lkmslkssList.put("ywlx",YYSEnum.YYS_LKMSLKSS.getCode());
            Map<String,Object> lmslkssList = new HashMap<>();
            lmslkssList.put("selectList",selectList);
            lmslkssList.put("list",new ArrayList<JnsjcdiqyysDto>());
            lmslkssList.put("ywlx",YYSEnum.YYS_LMSLKSS.getCode());
            Map<String,Object> wgmslList = new HashMap<>();
            wgmslList.put("selectList",selectList);
            wgmslList.put("list",new ArrayList<JnsjcdiqyysDto>());
            wgmslList.put("ywlx",YYSEnum.YYS_WGMSLKSS.getCode());
            Map<String,Object> kntllList = new HashMap<>();
            kntllList.put("selectList",selectList);
            kntllList.put("list",new ArrayList<JnsjcdiqyysDto>());
            kntllList.put("ywlx",YYSEnum.YYS_KNTLYW.getCode());
            Map<String,Object> zzbyzjList = new HashMap<>();
            zzbyzjList.put("selectList",selectList);
            zzbyzjList.put("list",new ArrayList<JnsjcdiqyysDto>());
            zzbyzjList.put("ywlx",YYSEnum.YYS_ZZBYZJ.getCode());
            Map<String,Object> swjlList = new HashMap<>();
            swjlList.put("selectList",selectList);
            swjlList.put("list",new ArrayList<JnsjcdiqyysDto>());
            swjlList.put("ywlx",YYSEnum.YYS_XBDXLYW_SWJL.getCode());
            Map<String,Object> dxlList = new HashMap<>();
            dxlList.put("selectList",selectList);
            dxlList.put("list",new ArrayList<JnsjcdiqyysDto>());
            dxlList.put("ywlx",YYSEnum.YYS_XBDXLYW_DXL.getCode());
            Map<String,Object> ksslList = new HashMap<>();
            ksslList.put("selectList",selectList);
            ksslList.put("list",new ArrayList<JnsjcdiqyysDto>());
            ksslList.put("ywlx",YYSEnum.YYS_XBDXLYW_KSSL.getCode());
            Map<String,Object> whjlList = new HashMap<>();
            whjlList.put("selectList",selectList);
            whjlList.put("list",new ArrayList<JnsjcdiqyysDto>());
            whjlList.put("ywlx",YYSEnum.YYS_XBDXLYW_WHJL.getCode());
            Map<String,Object> bjlList = new HashMap<>();
            bjlList.put("selectList",selectList);
            bjlList.put("list",new ArrayList<JnsjcdiqyysDto>());
            bjlList.put("ywlx",YYSEnum.YYS_XBDXLYW_BJL.getCode());
            JcsjDto jcsjDto=new JcsjDto();
            jcsjDto.setJclb("JNSJCDIZQYYS");
            for(JnsjcdiqyysDto jnsjcdiqyysDto:jnsjcdiqyysDtoList){
                String  ywlx=jnsjcdiqyysDto.getYwlx();
                List<JnsjcdiqyysDto> jnsjcdiqyysDto_list=new ArrayList<>();
                if(ywlx.equals(YYSEnum.YYS_NXALKSS.getCode())){
                    if(nxakssList.get("list")!=null){
                        jnsjcdiqyysDto_list=(List<JnsjcdiqyysDto>)nxakssList.get("list");

                    }
                    jnsjcdiqyysDto_list.add(jnsjcdiqyysDto);
                    nxakssList.put("list",jnsjcdiqyysDto_list);
                }else if(ywlx.equals(YYSEnum.YYS_DHNZLKSS.getCode())){
                    if(dhnzlkssList.get("list")!=null){
                        jnsjcdiqyysDto_list=(List<JnsjcdiqyysDto>)dhnzlkssList.get("list");

                    }
                    jnsjcdiqyysDto_list.add(jnsjcdiqyysDto);
                    dhnzlkssList.put("list",jnsjcdiqyysDto_list);
                }else if(ywlx.equals(YYSEnum.YYS_AJGLKSS.getCode())){
                    if(ajglkssList.get("list")!=null){
                        jnsjcdiqyysDto_list=(List<JnsjcdiqyysDto>)ajglkssList.get("list");
                    }
                    jnsjcdiqyysDto_list.add(jnsjcdiqyysDto);
                    ajglkssList.put("list",jnsjcdiqyysDto_list);
                }else if(ywlx.equals(YYSEnum.YYS_SHSLKSS.getCode())){
                    if(shslkssList.get("list")!=null){
                        jnsjcdiqyysDto_list=(List<JnsjcdiqyysDto>)shslkssList.get("list");
                    }
                    jnsjcdiqyysDto_list.add(jnsjcdiqyysDto);
                    shslkssList.put("list",jnsjcdiqyysDto_list);
                }else if(ywlx.equals(YYSEnum.YYS_LKMSLKSS.getCode())){
                    if(lkmslkssList.get("list")!=null){
                        jnsjcdiqyysDto_list=(List<JnsjcdiqyysDto>)lkmslkssList.get("list");
                    }
                    jnsjcdiqyysDto_list.add(jnsjcdiqyysDto);
                    lkmslkssList.put("list",jnsjcdiqyysDto_list);
                }else if(ywlx.equals(YYSEnum.YYS_LMSLKSS.getCode())){
                    if(lmslkssList.get("list")!=null){
                        jnsjcdiqyysDto_list=(List<JnsjcdiqyysDto>)lmslkssList.get("list");
                    }
                    jnsjcdiqyysDto_list.add(jnsjcdiqyysDto);
                    lmslkssList.put("list",jnsjcdiqyysDto_list);
                }else if(ywlx.equals(YYSEnum.YYS_WGMSLKSS.getCode())){
                    if(wgmslList.get("list")!=null){
                        jnsjcdiqyysDto_list=(List<JnsjcdiqyysDto>)wgmslList.get("list");

                    }
                    jnsjcdiqyysDto_list.add(jnsjcdiqyysDto);
                    wgmslList.put("list",jnsjcdiqyysDto_list);
                }else if(ywlx.equals(YYSEnum.YYS_KNTLYW.getCode())){
                    if(kntllList.get("list")!=null){
                        jnsjcdiqyysDto_list=(List<JnsjcdiqyysDto>)kntllList.get("list");
                    }
                    jnsjcdiqyysDto_list.add(jnsjcdiqyysDto);
                    kntllList.put("list",jnsjcdiqyysDto_list);
                }else if(ywlx.equals(YYSEnum.YYS_ZZBYZJ.getCode())){
                    if(zzbyzjList.get("list")!=null){
                        jnsjcdiqyysDto_list=(List<JnsjcdiqyysDto>)zzbyzjList.get("list");

                    }
                    jnsjcdiqyysDto_list.add(jnsjcdiqyysDto);
                    zzbyzjList.put("list",jnsjcdiqyysDto_list);
                }else if(ywlx.equals(YYSEnum.YYS_XBDXLYW_SWJL.getCode())){
                    if(swjlList.get("list")!=null){
                        jnsjcdiqyysDto_list=(List<JnsjcdiqyysDto>)swjlList.get("list");

                    }
                    jnsjcdiqyysDto_list.add(jnsjcdiqyysDto);
                    swjlList.put("list",jnsjcdiqyysDto_list);
                }else if(ywlx.equals(YYSEnum.YYS_XBDXLYW_DXL.getCode())){
                    if(dxlList.get("list")!=null){
                        jnsjcdiqyysDto_list=(List<JnsjcdiqyysDto>)dxlList.get("list");

                    }
                    jnsjcdiqyysDto_list.add(jnsjcdiqyysDto);
                    dxlList.put("list",jnsjcdiqyysDto_list);
                }else if(ywlx.equals(YYSEnum.YYS_XBDXLYW_KSSL.getCode())){
                    if(ksslList.get("list")!=null){
                        jnsjcdiqyysDto_list=(List<JnsjcdiqyysDto>)ksslList.get("list");

                    }
                    jnsjcdiqyysDto_list.add(jnsjcdiqyysDto);
                    ksslList.put("list",jnsjcdiqyysDto_list);
                }else if(ywlx.equals(YYSEnum.YYS_XBDXLYW_WHJL.getCode())){
                    if(whjlList.get("list")!=null){
                        jnsjcdiqyysDto_list=(List<JnsjcdiqyysDto>)whjlList.get("list");

                    }
                    jnsjcdiqyysDto_list.add(jnsjcdiqyysDto);
                    whjlList.put("list",jnsjcdiqyysDto_list);
                }else if(ywlx.equals(YYSEnum.YYS_XBDXLYW_BJL.getCode())){
                    if(bjlList.get("list")!=null){
                        jnsjcdiqyysDto_list=(List<JnsjcdiqyysDto>)bjlList.get("list");

                    }
                    jnsjcdiqyysDto_list.add(jnsjcdiqyysDto);
                    bjlList.put("list",jnsjcdiqyysDto_list);
                }

            }

            map.put("nxakssList",nxakssList);
            map.put("dhnzlkssList",dhnzlkssList);
            map.put("ajglkssList",ajglkssList);
            map.put("shslkssList",shslkssList);
            map.put("lkmslkssList",lkmslkssList);
            map.put("lmslkssList",lmslkssList);
            map.put("wgmslList",wgmslList);
            map.put("kntllList",kntllList);
            map.put("zzbyzjList",zzbyzjList);
            map.put("swjlList",swjlList);
            map.put("dxlList",dxlList);
            map.put("ksslList",ksslList);
            map.put("whjlList",whjlList);
            map.put("bjlList",bjlList);

        }


        return map;
    }

    //查看艰难梭菌报告记录，很多字段0/1/2等在sql里直接转为是/否等
    @Override
    public JnsjbgjlDto getViewDto(JnsjbgjlDto jnsjbgjlDto) {
        return dao.getViewDto(jnsjbgjlDto);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delJnsjInfo(JnsjbgjlDto jnsjbgjlDto) {
        List<String> jnsjhzidList = dao.getJnsjhzids(jnsjbgjlDto);
        Map<String,Object> map = new HashMap<>();
        map.put("list",jnsjhzidList);
        map.put("scry",jnsjbgjlDto.getScry());
        boolean isok = jnsjjcjgService.batchDelete(jnsjbgjlDto.getIds());
        isok = jnsjcdizlService.batchDelete(jnsjbgjlDto.getIds());
        isok = jnsjcdiqyysService.batchDelete(jnsjbgjlDto.getIds());
        isok = dao.deletelistByIds(jnsjbgjlDto);
        isok = jnsjhzxxDao.deletelistByhzids(map);
        return isok;
    }

    /**
     * 根据搜索条件获取导出条数
     * @return
     */
    public int getCountForSearchExp(JnsjbgjlDto jnsjbgjlDto,Map<String,Object> params){
        return dao.getCountForSearchExp(jnsjbgjlDto);
    }

    /**
     * 选中导出
     * @param params
     * @return
     */
    public List<JnsjbgjlDto> getListForSelectExp(Map<String, Object> params){
        JnsjbgjlDto jnsjbgjlDto = (JnsjbgjlDto) params.get("entryData");
        queryJoinFlagExport(params,jnsjbgjlDto);
        List<JnsjbgjlDto> list = dao.getListForSelectExp(jnsjbgjlDto);
        return list;
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String,Object> params, JnsjbgjlDto jnsjbgjlDto){
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuffer sqlParam = new StringBuffer();
        for(DcszDto dcszDto:choseList){
            if(dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if(StringUtil.isNotBlank(dcszDto.getSqlzd())){
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        String sqlcs=sqlParam.toString();
        jnsjbgjlDto.setSqlParam(sqlcs);
    }

    //艰难梭菌报告扩展修改
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean extendUpdateJnsj(JnsjbgjlDto jnsjbgjlDto) {
        boolean isok = false;
        //json转list实体类
        List<JnsjjcjgDto> list = new ArrayList<JnsjjcjgDto>();
        list = JSONObject.parseArray(jnsjbgjlDto.getJnsjjcjg_json(), JnsjjcjgDto.class);
        List<JnsjjcjgDto> reslist = new ArrayList<>();
        //先删除jnsjbgid为xx的所有数据，在一次更新list
        if (list != null && list.size() >0 ){
            for (int i=0; i<list.size(); i++){
                JnsjjcjgDto jnsjjcjgDto = new JnsjjcjgDto();
                jnsjjcjgDto.setJnsjjcjgid(StringUtil.generateUUID());
                jnsjjcjgDto.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
                jnsjjcjgDto.setJnsjjcsj(list.get(i).getJnsjjcsj());
                jnsjjcjgDto.setGdhjg(list.get(i).getGdhjg());
                jnsjjcjgDto.setToxinjg(list.get(i).getToxinjg());
                jnsjjcjgDto.setDbjnsjpyjg(list.get(i).getDbjnsjpyjg());
                jnsjjcjgDto.setJnsjmspyjg(list.get(i).getJnsjmspyjg());
                jnsjjcjgDto.setScbj("0");
                reslist.add(jnsjjcjgDto);
            }
        }
        JnsjjcjgDto jnsjjcjgDto1 = new JnsjjcjgDto();
        jnsjjcjgDto1.setJnsjbgid(jnsjbgjlDto.getJnsjbgid());
        jnsjjcjgDto1.setScbj("1");
        jnsjjcjgDao.delJnsjjcjg(jnsjjcjgDto1);
        jnsjjcjgDao.insertList(reslist);
        dao.extendUpdateJnsj(jnsjbgjlDto);//更新jnsjbgjl的扩展字段
        return true;
    }

    @Override
    public String generateBlrzbh(String cskz1) {
        return dao.generateBlrzbh(cskz1);
    }

}

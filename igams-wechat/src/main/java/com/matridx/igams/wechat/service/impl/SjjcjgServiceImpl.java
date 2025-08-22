package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.dao.post.IShxxDao;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.LczzznglDto;
import com.matridx.igams.wechat.dao.entities.NyjyzsglDto;
import com.matridx.igams.wechat.dao.entities.SjjcjgDto;
import com.matridx.igams.wechat.dao.entities.SjjcjgModel;
import com.matridx.igams.wechat.dao.entities.SjsyglDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.WzglDto;
import com.matridx.igams.wechat.dao.post.ISjjcjgDao;
import com.matridx.igams.wechat.dao.post.ISjxxDao;
import com.matridx.igams.wechat.service.svcinterface.IFjsqService;
import com.matridx.igams.wechat.service.svcinterface.ILczzznglService;
import com.matridx.igams.wechat.service.svcinterface.INyjyzsglService;
import com.matridx.igams.wechat.service.svcinterface.ISjjcjgService;
import com.matridx.igams.wechat.service.svcinterface.ISjsyglService;
import com.matridx.igams.wechat.service.svcinterface.IWzglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SjjcjgServiceImpl extends BaseBasicServiceImpl<SjjcjgDto, SjjcjgModel, ISjjcjgDao> implements ISjjcjgService, IAuditService {
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    private ISjxxDao sjxxService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IWzglService wzglService;
    @Autowired
    ILczzznglService lczzznglService;
    @Autowired
    IShxxDao shxxDao;
    @Autowired
    IFjsqService fjsqService;
    @Autowired
    ISjsyglService sjsyglService;
    @Autowired
    INyjyzsglService nyjyzsglService;
    @Value("${matridx.wechat.applicationurl:}")
    String applicationurl;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    ICommonService commonService;
    Logger logger = LoggerFactory.getLogger(SjjcjgServiceImpl.class);
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean insertList(List<SjjcjgDto> list) {
        return dao.insertList(list);
    }

    @Override
    public List<Map<String,Object>> getSjjcjgList(SjjcjgDto sjjcjgDto) {
        return dao.getSjjcjgList( sjjcjgDto);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        SjjcjgDto sjjcjgDto = (SjjcjgDto) baseModel;
        if (StringUtil.isBlank(sjjcjgDto.getDdbj())){
            List<SjjcjgDto> sjjcjgDtoList = new ArrayList<>();
            if (null != sjjcjgDto.getStrings() && sjjcjgDto.getStrings().size()>0){
                for (String string : sjjcjgDto.getStrings()) {
                    String[] strings = string.split("_");
                    SjjcjgDto sjjcjgDto1 = new SjjcjgDto();
                    sjjcjgDto1.setJcjgid(strings[0]);
                    sjjcjgDto1.setXgry(operator.getYhid());
                    sjjcjgDto1.setJl(strings[1]);
                    sjjcjgDtoList.add(sjjcjgDto1);
                }
                if (null != sjjcjgDtoList && sjjcjgDtoList.size()>0){
                    dao.updateList(sjjcjgDtoList);
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
//        String token = talkUtil.getToken();
        for (ShgcDto shgcDto : shgcList) {
            SjjcjgDto sjjcjgDto = new SjjcjgDto();
            sjjcjgDto.setYwid(shgcDto.getYwid());
            sjjcjgDto.setXgry(operator.getYhid());
            SjxxDto sjxxDto = new SjxxDto();
            sjxxDto.setSjid(shgcDto.getYwid());
            FjcfbDto fjcfbDto = new FjcfbDto();
            fjcfbDto.setYwid(shgcDto.getYwid());
            fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_RFS_TEMEPLATE.getCode());
            List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto);
            if (AuditTypeEnum.AUDIT_RFS_SJ.getCode().equals(shgcDto.getShlb())){
                sjxxDto = sjxxService.getOneFromSJ(sjxxDto);
            }else {
                sjxxDto = sjxxService.getOneFromFJ(sjxxDto);
            }
            boolean hbMessageFlag = commonService.queryAuthMessage(sjxxDto.getHbid(),"INFORM_HB00005");


            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                // 由于合同提交时直接发起了钉钉审批，所以审核不通过时直接采用钉钉的审批拒绝消息即可
                if (AuditTypeEnum.AUDIT_RFS_SJ.getCode().equals(shgcDto.getShlb())){
                    sjjcjgDto.setZt(StatusEnum.CHECK_PASS.getCode());
                }else {
                    sjjcjgDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                    SjsyglDto sjsyglDto=new SjsyglDto();
                    sjsyglDto.setYwid(shgcDto.getYwid());
                    sjsyglDto.setSfjs("0");
                    sjsyglDto.setXgry(operator.getYhid());
                    sjsyglService.updateSfjs(sjsyglDto);
                }
                String RFS_SH000003 = xxglService.getMsg("RFS_SH000003");
                String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
                if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0 ){
                    try{
                        int size = shgcDto.getSpgwcyDtos().size();
                        for(int i=0;i<size;i++){
                            if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00026,StringUtil.replaceMsg(RFS_SH000003,operator.getZsxm(),
                                        shgcDto.getShlbmc() ,sjxxDto.getNbbm(),sjxxDto.getQf(),sjxxDto.getHzxm(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                sjjcjgDto.setZt(StatusEnum.CHECK_PASS.getCode());
                String RFS_SH000002 = xxglService.getMsg("RFS_SH000002");
                String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");

                //发送报告
                sendReport(shgcDto.getYwid(),shgcDto.getShlb(),operator.getYhm());

                if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0){
                    try{
                        int size = shgcDto.getSpgwcyDtos().size();
                        for(int i=0;i<size;i++){
                            if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00006,StringUtil.replaceMsg(RFS_SH000002,operator.getZsxm(),
                                        shgcDto.getShlbmc() ,sjxxDto.getNbbm(),sjxxDto.getQf(),sjxxDto.getHzxm(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }else {
                sjjcjgDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                String RFS_SH000001 = xxglService.getMsg("RFS_SH000001");
                String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
                //如果为复测，则需要根据检测单位发消息
                if (AuditTypeEnum.AUDIT_RFS_FC.getCode().equals(shgcDto.getShlb())){
                    FjsqDto fjsqDto = new FjsqDto();
                    fjsqDto.setFjid(sjxxDto.getSjid());
                    FjsqDto dto = fjsqService.getDto(fjsqDto);
                    //获取审批岗位对应的所有yhm并且过滤yhm为空的数据
                    List<String> yhms = shgcDto.getSpgwcyDtos().stream().filter((e) -> {return StringUtil.isNotBlank(e.getYhm());}).map(SpgwcyDto::getYhm).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(yhms)){
                        Map<String, Object> map = new HashMap<>();
                        map.put("yhms",yhms);
                        map.put("jcdw",dto.getJcdw());
                        //查询有检测单位权限用户
                        List<Map<String,String>> xtyhs = fjsqService.getXtyhByMap(map);
                        if (!CollectionUtils.isEmpty(xtyhs)){
                            try{
                                for (Map<String, String> xtyh : xtyhs) {
                                    String yhid = xtyh.get("yhid");
                                    String yhm = xtyh.get("yhm");
                                    if(StringUtil.isNotBlank(yhm)){
                                        talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),yhid,yhm, yhid, ICOMM_SH00003,StringUtil.replaceMsg(RFS_SH000001,operator.getZsxm(),
                                                shgcDto.getShlbmc() ,(fjcfbDtos!=null&&!fjcfbDtos.isEmpty())?"#16a951":"#ff461f",(fjcfbDtos!=null&&!fjcfbDtos.isEmpty())?"已上传":"未上传",sjxxDto.getNbbm(),sjxxDto.getQf(),sjxxDto.getHzxm(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                                    }
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    if ("1".equals(shgcDto.getXlcxh()) ){
                        if("1".equals(dto.getBgbj()) && hbMessageFlag){
                            fjsqService.sendRecheckMessageSec(dto);
                        }
                        SjsyglDto sjsyglDto=new SjsyglDto();
                        sjsyglDto.setYwid(sjxxDto.getSjid());
                        sjsyglDto.setSfjs("1");
                        sjsyglDto.setJsry(sjxxDto.getLrry());
                        sjsyglDto.setJsrq(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                        sjsyglDto.setXgry(operator.getYhid());
                        sjsyglService.updateSfjs(sjsyglDto);
                    }
                }else{
                    if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0){
                        try{
                            int size = shgcDto.getSpgwcyDtos().size();
                            for(int i=0;i<size;i++){
                                if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
                                    talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00003,StringUtil.replaceMsg(RFS_SH000001,operator.getZsxm(),
                                            shgcDto.getShlbmc() ,(fjcfbDtos!=null&&!fjcfbDtos.isEmpty())?"#16a951":"#ff461f",(fjcfbDtos!=null&&!fjcfbDtos.isEmpty())?"已上传":"未上传",sjxxDto.getNbbm(),sjxxDto.getQf(),sjxxDto.getHzxm(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }

            //发送钉钉消息--取消审核人员
            if(shgcDto.getNo_spgwcyDtos() != null && shgcDto.getNo_spgwcyDtos().size() > 0){
                int size = shgcDto.getNo_spgwcyDtos().size();
                for(int i=0;i<size;i++){
                    if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                        talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(), shgcDto.getNo_spgwcyDtos().get(i).getYhm(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",shgcDto.getShlbmc() ,sjxxDto.getNbbm(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                    }
                }
            }
            if (AuditTypeEnum.AUDIT_RFS_SJ.getCode().equals(shgcDto.getShlb())){
                dao.modSj(sjjcjgDto);
            }else {
                dao.modFc(sjjcjgDto);
            }
        }
        return true;
    }
    public JSONArray dealWzArry(List<WzglDto> wzglDtoList, List<SjjcjgDto> wzDtoList, JSONArray array){
        if (wzglDtoList != null && wzglDtoList.size() > 0){
            for(WzglDto dto:wzglDtoList){
                JSONObject jsonObject_t=new JSONObject();
                jsonObject_t.put("taxid",dto.getTaxid());
                jsonObject_t.put("rank_code",dto.getRank_code());
                jsonObject_t.put("name",dto.getName());
                jsonObject_t.put("cn_name",dto.getCn_name());
                if("Viruses".equals(dto.getSp_type())){
                    jsonObject_t.put("species_category",dto.getBdlb());
                }else{
                    jsonObject_t.put("species_category",dto.getSpecies_category());
                }
                jsonObject_t.put("genus_taxid",dto.getGenus_taxid());
                jsonObject_t.put("genus_name",dto.getGenus_name());
                jsonObject_t.put("genus_cn_name",dto.getGenus_cn_name());
                jsonObject_t.put("special_genus",dto.getSp_type());
                if("Mycoplasma".equals(dto.getSp_type())||"Chlamydia".equals(dto.getSp_type())||"Rckettsia".equals(dto.getSp_type())){
                    jsonObject_t.put("sp_type","MCR");
                }else{
                    jsonObject_t.put("sp_type",dto.getSp_type());
                }
                if(StringUtil.isBlank(dto.getSp_type())){
                    jsonObject_t.put("sp_type","NY");
                }
                jsonObject_t.put("highlight",dto.getHighlight());
                jsonObject_t.put("comment",dto.getComment());
                for (SjjcjgDto sjjcjgDto : wzDtoList) {
                    if (dto.getTaxid().equals(sjjcjgDto.getJcjgdm())){
                        jsonObject_t.put("jglxmc",(StringUtil.isNotBlank(sjjcjgDto.getJlmc())?sjjcjgDto.getJlmc().replace("检测灰区","疑似"):sjjcjgDto.getJlmc()));
                        jsonObject_t.put("ygz",sjjcjgDto.getJgsz());
                        break;
                    }
                }
                array.add(jsonObject_t);
            }
        }
        return array;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        SjjcjgDto sjjcjgDto = new SjjcjgDto();
        if (auditParam.isCancelOpe()) {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                String sczlmxid = shgcDto.getYwid();
                sjjcjgDto.setXgry(operator.getYhid());
                sjjcjgDto.setYwid(sczlmxid);
                sjjcjgDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
            }
        } else {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                String sczlmxid = shgcDto.getYwid();
                sjjcjgDto.setXgry(operator.getYhid());
                sjjcjgDto.setYwid(sczlmxid);
                sjjcjgDto.setZt(StatusEnum.CHECK_NO.getCode());
                if (AuditTypeEnum.AUDIT_RFS_FC.getCode().equals(shgcDto.getShlb())){
                    SjsyglDto sjsyglDto=new SjsyglDto();
                    sjsyglDto.setYwid(sczlmxid);
                    sjsyglDto.setSfjs("0");
                    sjsyglDto.setXgry(operator.getYhid());
                    sjsyglService.updateSfjs(sjsyglDto);
                }
            }
        }
        for (ShgcDto shgcDto : shgcList) {
            if (AuditTypeEnum.AUDIT_RFS_SJ.getCode().equals(shgcDto.getShlb())){
                dao.modSj(sjjcjgDto);
            }else {
                dao.modFc(sjjcjgDto);
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) throws BusinessException {
        return null;
    }

    @Override
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) throws BusinessException {
        Map<String, Object> map =new HashMap<>();
        List<String> ids = (List<String>)param.get("ywids");
        List<String> list=new ArrayList<>();
        FjsqDto fjsqDto = new FjsqDto();
        fjsqDto.setIds(ids);
        List<FjsqDto> dtoList = fjsqService.getDtoList(fjsqDto);
        if(!CollectionUtils.isEmpty(dtoList)){
            for(FjsqDto dto:dtoList){
                ids.add(dto.getFjid());
            }
        }
        SjxxDto sjxxDto = new SjxxDto();
        sjxxDto.setIds(ids);
        List<SjxxDto> sjxxDtos = sjxxService.getDtoList(sjxxDto);
        if(!CollectionUtils.isEmpty(sjxxDtos)){
            for(SjxxDto dto:sjxxDtos){
                ids.add(dto.getSjid());
            }
        }
        map.put("list",list);
        return map;
    }

    /**
     * 获取相关文献List
     */
    public List<SjjcjgDto> getXgwxList(SjjcjgDto sjjcjgDto){
        return dao.getXgwxList(sjjcjgDto);
    }

    @Override
    public Boolean sendReport(String ywid, String shlb,String yhm) throws BusinessException {
        //生成报告
        SjxxDto t_sjxxDto =new SjxxDto();
        if (AuditTypeEnum.AUDIT_RFS_SJ.getCode().equals(shlb)){
            t_sjxxDto.setSjid(ywid);
            t_sjxxDto = sjxxService.getDto(t_sjxxDto);
        }else {
            FjsqDto dtoById = fjsqService.getDtoById(ywid);
            t_sjxxDto.setSjid(dtoById.getSjid());
            t_sjxxDto = sjxxService.getDto(t_sjxxDto);
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("ybbh",t_sjxxDto.getYbbh());
        jsonObject.put("detection_type","F");
        jsonObject.put("status","success");
        jsonObject.put("sample_id","");
        ShxxDto t_shxxDto = new ShxxDto();
        t_shxxDto.setYwid(ywid);
        t_shxxDto.setShlb(shlb);
        List<ShxxDto> shxxList = shxxDao.getShxxOrderByShsj(t_shxxDto);
        if(shxxList!=null&&shxxList.size()>0){
            if (StringUtil.isBlank(yhm)){
                if (shxxList.size()>1){
                    jsonObject.put("jyry",shxxList.get(1).getShryhm());
                    jsonObject.put("shry",shxxList.get(0).getShryhm());
                }
            }else{
                jsonObject.put("jyry",shxxList.get(0).getShryhm());
                jsonObject.put("shry",yhm);
            }
        }

        SjjcjgDto t_sjjcjgDto=new SjjcjgDto();
        t_sjjcjgDto.setYwid(ywid);
        List<SjjcjgDto> dtoList = dao.getDtoList(t_sjjcjgDto);
        List<String> wzids=new ArrayList<>();
        List<SjjcjgDto> pathogenWzList = new ArrayList<>();
        List<SjjcjgDto> possibleWzList = new ArrayList<>();
        List<String> pathogenWzids=new ArrayList<>();
        List<String> possibleWzids=new ArrayList<>();
        JSONArray ny_array= new JSONArray();
        List<String> mcs=new ArrayList<>();
        t_sjjcjgDto.setYblx(t_sjxxDto.getYblx());
        if(dtoList!=null&&dtoList.size()>0){
            for(SjjcjgDto dto:dtoList){
                if("001".equals(dto.getJldm())){//判断阳性物种
                    if(StringUtil.isNotBlank(dto.getJcjgdm())){
                        wzids.add(dto.getJcjgdm());
                        pathogenWzids.add(dto.getJcjgdm());
                        pathogenWzList.add(dto);
                    }
                    if(dto.getJcjgdm().contains("NY")){//耐药物种处理
                        mcs.add(dto.getJcjgdm().substring(3));
                    }
                }
                if("003".equals(dto.getJldm())){//判断疑似物种
                    if(StringUtil.isNotBlank(dto.getJcjgdm())){
                        wzids.add(dto.getJcjgdm());
                        possibleWzids.add(dto.getJcjgdm());
                        possibleWzList.add(dto);
                    }
                }
            }
        }
        //耐药基因注释
        JSONArray array= new JSONArray();
        if(mcs!=null&&mcs.size()>0){
            NyjyzsglDto nyjyzsglDto=new NyjyzsglDto();
            nyjyzsglDto.setMcs(mcs);
            List<NyjyzsglDto> nyjyzsglDtos=nyjyzsglService.getDtoList(nyjyzsglDto);
            for(NyjyzsglDto dto:nyjyzsglDtos){
                JSONObject jsonObject_t=new JSONObject();
                jsonObject_t.put("comment",dto.getZsnr());
                jsonObject_t.put("name",dto.getJyjzmc());
                jsonObject_t.put("id",dto.getNyjyid());
                if(StringUtil.isNotBlank(dto.getXgsj())){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(dto.getXgsj());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long ts = date.getTime();
                    dto.setXgsj(String.valueOf(ts/1000));  // 转化为字符串
                }
                jsonObject_t.put("last_update",dto.getXgsj());
                array.add(jsonObject_t);

                JSONObject jsonObject_ny=new JSONObject();
                jsonObject_ny.put("related_gene",dto.getJyjzmc());
                jsonObject_ny.put("drug_class",dto.getDrugclass());
                jsonObject_ny.put("main_mechanism",dto.getMainmechanism());
                ny_array.add(jsonObject_ny);
            }
        }
        jsonObject.put("drug_resistance_stat",ny_array);
        jsonObject.put("drug_resist_gene",array);//耐药基因注释
        array= new JSONArray();
        if(pathogenWzids!=null&&pathogenWzids.size()>0){
            WzglDto wzglDto=new WzglDto();
            wzglDto.setIds(pathogenWzids);
            List<WzglDto> pathogenList = wzglService.getPathogenList(wzglDto);
            array = dealWzArry(pathogenList,pathogenWzList,array);
        }
        jsonObject.put("pathogen",array);//阳性物种信息
        array= new JSONArray();
        if(possibleWzids!=null&&possibleWzids.size()>0){
            WzglDto wzglDto=new WzglDto();
            wzglDto.setIds(possibleWzids);
            List<WzglDto> possibleList = wzglService.getPathogenList(wzglDto);
            array = dealWzArry(possibleList,possibleWzList,array);
        }
        jsonObject.put("possible",array);//疑似物种信息
        array= new JSONArray();
        if(wzids!=null&&wzids.size()>0){
            t_sjjcjgDto.setIds(wzids);
            List<SjjcjgDto> xgwxList = dao.getXgwxList(t_sjjcjgDto);
            if(xgwxList!=null&&xgwxList.size()>0){
                for(SjjcjgDto dto:xgwxList){
                    JSONObject jsonObject_t=new JSONObject();
                    jsonObject_t.put("id",dto.getId());
                    jsonObject_t.put("author",dto.getAuthor());
                    jsonObject_t.put("title",dto.getTitle());
                    jsonObject_t.put("journal",dto.getJournal());
                    jsonObject_t.put("species_taxid",dto.getSpecies_taxid());
                    jsonObject_t.put("sample_type",dto.getSample_type());
                    jsonObject_t.put("mrwx",dto.getMrwx());
                    array.add(jsonObject_t);
                }
            }
        }
        jsonObject.put("papers",array);//参考文献
        LczzznglDto lczzznglDto = new LczzznglDto();
        List<LczzznglDto> lcznDtoList = lczzznglService.getDtoListByIds(lczzznglDto);
        List<LczzznglDto> lczzznglDtos = new ArrayList<>();
        for(LczzznglDto lczzznglDto_t:lcznDtoList){
            if(StringUtil.isNotBlank(lczzznglDto_t.getSpecies())){
                JSONArray jsonArray = JSON.parseArray(lczzznglDto_t.getSpecies());
                for(int i=0;i<jsonArray.size();i++){
                    boolean flag=false;
                    for(String taxid:wzids){
                        if(taxid.equals(jsonArray.getString(i))){
                            lczzznglDtos.add(lczzznglDto_t);
                            flag=true;
                            break;
                        }
                    }
                    if(flag){
                        break;
                    }
                }
            }
        }
        List<LczzznglDto> dtos = new ArrayList<>();
        for(LczzznglDto lczzznglDto_t:lczzznglDtos){
            if(StringUtil.isBlank(lczzznglDto_t.getYblx())||t_sjjcjgDto.getYblx().equals(lczzznglDto_t.getYblx())){
                dtos.add(lczzznglDto_t);
            }
        }
        array= new JSONArray();
        if(dtos!=null&&dtos.size()>0){
            for(LczzznglDto dto:dtos){
                JSONObject jsonObject_t=new JSONObject();
                jsonObject_t.put("id",dto.getId());
                jsonObject_t.put("detail",dto.getDetail());
                jsonObject_t.put("species",dto.getSpecies());
                jsonObject_t.put("yblx",dto.getYblx());
                if(StringUtil.isNotBlank(dto.getLast_update())){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(dto.getLast_update());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long ts = date.getTime();
                    String res = String.valueOf(ts/1000);  // 转化为字符串
                    dto.setLast_update(res);
                }
                jsonObject_t.put("last_update",dto.getLast_update());
                array.add(jsonObject_t);
            }
        }
        jsonObject.put("guidelines",array);//临床指南信息
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("sample_result", JSON.toJSONString(jsonObject));
        RestTemplate restTemplate = new RestTemplate();
        String url=applicationurl+"/ws/pathogen/receiveInspectionGenerateReportSec?userid="+(StringUtil.isNotBlank(yhm)?yhm:"");
        Map<String, Object> map = restTemplate.postForObject(url, paramMap, Map.class);
        String status = (String)map.get("status");
        if(!"success".equals(status)){
            throw new BusinessException("msg",map.get("errorCode").toString());
        }
        return true;
    }

    @Override
    public List<SjjcjgDto> streamList(List<SjjcjgDto> list) {

        if (list.size() > 0){
            // 使用 Stream 排序
            list.sort(new Comparator<>() {
                          @Override
                          public int compare(SjjcjgDto sjjcjgDto, SjjcjgDto sjjcjgDtoq) {
                              return Double.compare(Double.parseDouble(sjjcjgDtoq.getJgsz()), Double.parseDouble(sjjcjgDto.getJgsz()));
                          }
                      }
            );
        }
        return list;
    }

    @Override
    public List<SjjcjgDto> getWeekList(SjxxDto sjxxDto) {
        List<String> ywidList = dao.getYwidList(sjxxDto);
        List<SjjcjgDto> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(ywidList)){
            SjjcjgDto sjjcjgDto = new SjjcjgDto();
            sjjcjgDto.setIds(ywidList);
            list = dao.getAllList(sjjcjgDto);
        }
        return list;
    }

    /**
     * 批量更新结论
     * @param list
     * @return
     */
    public boolean updatesjjcjgJlList(List<SjjcjgDto> list){
        return dao.updatesjjcjgJlList(list);
    }
}

package com.matridx.igams.hrm.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.google.common.collect.Lists;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DsrwszDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.RedisCommonKeyEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.hrm.dao.entities.*;
import com.matridx.igams.hrm.dao.post.IPxglDao;
import com.matridx.igams.hrm.service.svcinterface.IHbszService;
import com.matridx.igams.hrm.service.svcinterface.IHbszmxService;
import com.matridx.igams.hrm.service.svcinterface.IPxglService;
import com.matridx.igams.hrm.service.svcinterface.IPxglxxService;
import com.matridx.igams.hrm.service.svcinterface.IPxtkglbService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.CronUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.qrcode.QrCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PxglServiceImpl extends BaseBasicServiceImpl<PxglDto, PxglModel, IPxglDao> implements IPxglService,IAuditService{
    @Autowired
    ICommonService commonService;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    IPxtkglbService pxtkglbService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IGzglService gzglService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IHbszService hbszService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IDsrwszService dsrwszService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IHbszmxService hbszmxService;
    @Autowired
    IPxglxxService pxglxxService;
    @Value("${matridx.systemflg.remindtype:}")
    private String remindtype;
    @Autowired(required=false)
    private AmqpTemplate amqpTempl;
    @Value("${matridx.rabbit.flg:}")
    private String rabbitFlg;

    @Value("${matridx.rabbit.preflg:}")
    private String preRabbitFlg;
    @Value("${matridx.fileupload.prefix}")
    private String prefix;
    @Value("${matridx.fileupload.tempPath}")
    private String tempFilePath;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Autowired
    IUserService userService;
    @Autowired
    IXxdyService xxdyService;
    @Autowired
    IJgxxService jgxxService;
    private final Logger log = LoggerFactory.getLogger(PxglServiceImpl.class);

    /**
     * 根据gzidid获取信息
     */
    public PxglDto viewTrainTask(PxglDto pxglDto){
        return dao.viewTrainTask(pxglDto);
    }

    /**
     * 根据pxid获取信息
     */
    public PxglDto viewDetailedList(PxglDto pxglDto){
        return dao.viewDetailedList(pxglDto);
    }

    /**
     * 根据用户Ids拿到去除部分人的list
     */
    public List<User> getPagedDtoListOutByIds(PxglDto pxglDto){
        return dao.getPagedDtoListOutByIds(pxglDto);
    }

    /**
     * 获取用户列表
     */
    public List<User> getPagedDtoListXtyh(PxglDto pxglDto){
        return dao.getPagedDtoListXtyh(pxglDto);
    }

    @Override
    public List<PxglDto> getPagedDtoList(PxglDto pxglDto) {
        List<PxglDto> list = dao.getPagedDtoList(pxglDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_TRAIN.getCode(), "zt", "pxid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMsg());
        }
        return list;
    }

    /**
     * 插入培训信息
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertPxglDto(PxglDto pxglDto) throws BusinessException {
        pxglDto.setPxid(StringUtil.generateUUID());
        List<PxglxxDto> addPxglxxDtos = new ArrayList<>();
        if (StringUtil.isNotBlank(pxglDto.getGlwjids())){
            List<String> glids = JSON.parseArray(pxglDto.getGlwjids(), String.class);
            for (String glwjid : glids) {
                PxglxxDto pxglxxDto = new PxglxxDto();
                pxglxxDto.setPxid(pxglDto.getPxid());
                pxglxxDto.setPxglid(StringUtil.generateUUID());
                pxglxxDto.setGlid(glwjid);
                pxglxxDto.setGllx("0");
                addPxglxxDtos.add(pxglxxDto);
            }
        }
        if (!CollectionUtils.isEmpty(addPxglxxDtos)){
            boolean isSuccess = pxglxxService.insertPxglxxDtos(addPxglxxDtos);
            if(!isSuccess)
                throw new BusinessException("msg","保存培训关联文件失败!");
        }
        if(!CollectionUtils.isEmpty(pxglDto.getFjids())){
            for (int i = 0;i<pxglDto.getFjids().size();i++) {

                boolean saveFile = fjcfbService.save2RealFile(pxglDto.getFjids().get(i), pxglDto.getPxid());
                if(!saveFile)
                    throw new BusinessException("msg","附件保存失败!");
            }
        }
        if(StringUtil.isNotBlank(pxglDto.getSpfjid())){
            String[] split = pxglDto.getSpfjid().split(",");
            for(int i=0;i<split.length;i++){
                boolean saveFile = fjcfbService.saveXhFile(split[i],pxglDto.getPxid(),null,String.valueOf(i+1));
                if(!saveFile)
                    throw new BusinessException("msg","附件保存失败!");
            }
        }
       insert(pxglDto);
        int danxts=0;
        int duoxts=0;
        int jdts=0;
        int tkts=0;
        int pdts=0;
        int cszf=0;
        List<PxtkglbDto> tmmxlist= JSON.parseArray(pxglDto.getTmmx_json(), PxtkglbDto.class);
        if(!CollectionUtils.isEmpty(tmmxlist)){
            for (int i=0;i<tmmxlist.size();i++){
                if("SELECT".equals(tmmxlist.get(i).getTmlx())){
                    danxts+=Integer.parseInt(tmmxlist.get(i).getSl());
                    cszf+=Integer.parseInt(tmmxlist.get(i).getSl())*Integer.parseInt(tmmxlist.get(i).getFz());
                }else if("MULTIPLE".equals(tmmxlist.get(i).getTmlx())){
                    duoxts+=Integer.parseInt(tmmxlist.get(i).getSl());
                    cszf+=Integer.parseInt(tmmxlist.get(i).getSl())*Integer.parseInt(tmmxlist.get(i).getFz());
                }else if("EXPLAIN".equals(tmmxlist.get(i).getTmlx())){
                    jdts+=Integer.parseInt(tmmxlist.get(i).getSl());
                    cszf+=Integer.parseInt(tmmxlist.get(i).getSl())*Integer.parseInt(tmmxlist.get(i).getFz());
                }else if("GAP".equals(tmmxlist.get(i).getTmlx())){
                    tkts+=Integer.parseInt(tmmxlist.get(i).getSl());
                    cszf+=Integer.parseInt(tmmxlist.get(i).getSl())*Integer.parseInt(tmmxlist.get(i).getFz());
                }else if("JUDGE".equals(tmmxlist.get(i).getTmlx())){
                    pdts+=Integer.parseInt(tmmxlist.get(i).getSl());
                    cszf+=Integer.parseInt(tmmxlist.get(i).getSl())*Integer.parseInt(tmmxlist.get(i).getFz());
                }
                tmmxlist.get(i).setXh(String.valueOf(i+1));
                tmmxlist.get(i).setTkid(tmmxlist.get(i).getTkmc());
                tmmxlist.get(i).setPxid(pxglDto.getPxid());
                PxtkglbDto dto = pxtkglbService.getDto(tmmxlist.get(i));
                if(dto!=null){
                    int sl = Integer.parseInt(dto.getSl()) + Integer.parseInt(tmmxlist.get(i).getSl());
                    dto.setSl(String.valueOf(sl));
                    dto.setXgry(pxglDto.getLrry());
                    pxtkglbService.update(dto);
                }else{
                    tmmxlist.get(i).setLrry(pxglDto.getLrry());
                    pxtkglbService.insert(tmmxlist.get(i));
                }
            }
            pxglDto.setDanxts(String.valueOf(danxts));
            pxglDto.setDuoxts(String.valueOf(duoxts));
            pxglDto.setJdts(String.valueOf(jdts));
            pxglDto.setTkts(String.valueOf(tkts));
            pxglDto.setPdts(String.valueOf(pdts));
            pxglDto.setCszf(String.valueOf(cszf));
            pxglDto.setCsts(String.valueOf(danxts+duoxts+jdts+tkts+pdts));

        }
        return update(pxglDto);
    }


    /**
     * 修改培训信息
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updatePxglDto(PxglDto pxglDto) throws BusinessException {
        List<PxglxxDto> addPxglxxDtos = new ArrayList<>();
        PxglxxDto pxglxxDto_del = new PxglxxDto();
        pxglxxDto_del.setPxid(pxglDto.getPxid());
        pxglxxService.delete(pxglxxDto_del);
        if (StringUtil.isNotBlank(pxglDto.getGlwjids())){
            List<String> glids = JSON.parseArray(pxglDto.getGlwjids(), String.class);
            for (String glwjid : glids) {
                PxglxxDto pxglxxDto = new PxglxxDto();
                pxglxxDto.setPxid(pxglDto.getPxid());
                pxglxxDto.setPxglid(StringUtil.generateUUID());
                pxglxxDto.setGlid(glwjid);
                pxglxxDto.setGllx("0");
                addPxglxxDtos.add(pxglxxDto);
            }
        }
        if (!CollectionUtils.isEmpty(addPxglxxDtos)){
            boolean isSuccess = pxglxxService.insertPxglxxDtos(addPxglxxDtos);
            if(!isSuccess)
                throw new BusinessException("msg","保存培训关联文件失败!");
        }
        PxtkglbDto dto_t=new PxtkglbDto();
        dto_t.setPxid(pxglDto.getPxid());
        pxtkglbService.deleteByPxid(dto_t);
        int danxts=0;
        int duoxts=0;
        int jdts=0;
        int cszf=0;
        int tkts=0;
        int pdts=0;
        List<PxtkglbDto> tmmxlist= JSON.parseArray(pxglDto.getTmmx_json(), PxtkglbDto.class);
        if ("0".equals(pxglDto.getSfcs())){
            tmmxlist=new ArrayList<>();
        }
        if(!CollectionUtils.isEmpty(tmmxlist)){
            for (int i=0;i<tmmxlist.size();i++){
                if("SELECT".equals(tmmxlist.get(i).getTmlx())){
                    danxts+=Integer.parseInt(tmmxlist.get(i).getSl());
                    cszf+=Integer.parseInt(tmmxlist.get(i).getSl())*Integer.parseInt(tmmxlist.get(i).getFz());
                }else if("MULTIPLE".equals(tmmxlist.get(i).getTmlx())){
                    duoxts+=Integer.parseInt(tmmxlist.get(i).getSl());
                    cszf+=Integer.parseInt(tmmxlist.get(i).getSl())*Integer.parseInt(tmmxlist.get(i).getFz());
                }else if("EXPLAIN".equals(tmmxlist.get(i).getTmlx())){
                    jdts+=Integer.parseInt(tmmxlist.get(i).getSl());
                    cszf+=Integer.parseInt(tmmxlist.get(i).getSl())*Integer.parseInt(tmmxlist.get(i).getFz());
                }else if("GAP".equals(tmmxlist.get(i).getTmlx())){
                    tkts+=Integer.parseInt(tmmxlist.get(i).getSl());
                    cszf+=Integer.parseInt(tmmxlist.get(i).getSl())*Integer.parseInt(tmmxlist.get(i).getFz());
                }else if("JUDGE".equals(tmmxlist.get(i).getTmlx())){
                    pdts+=Integer.parseInt(tmmxlist.get(i).getSl());
                    cszf+=Integer.parseInt(tmmxlist.get(i).getSl())*Integer.parseInt(tmmxlist.get(i).getFz());
                }
                tmmxlist.get(i).setXh(String.valueOf(i+1));
                tmmxlist.get(i).setTkid(tmmxlist.get(i).getTkmc());
                tmmxlist.get(i).setPxid(pxglDto.getPxid());
                PxtkglbDto dto = pxtkglbService.getDto(tmmxlist.get(i));
                if(dto!=null){
                    int sl = Integer.parseInt(dto.getSl()) + Integer.parseInt(tmmxlist.get(i).getSl());
                    dto.setSl(String.valueOf(sl));
                    dto.setXgry(pxglDto.getLrry());
                    pxtkglbService.update(dto);
                }else{
                    tmmxlist.get(i).setLrry(pxglDto.getLrry());
                    pxtkglbService.insert(tmmxlist.get(i));
                }
            }
            pxglDto.setDanxts(String.valueOf(danxts));
            pxglDto.setDuoxts(String.valueOf(duoxts));
            pxglDto.setJdts(String.valueOf(jdts));
            pxglDto.setTkts(String.valueOf(tkts));
            pxglDto.setPdts(String.valueOf(pdts));
            pxglDto.setCszf(String.valueOf(cszf));
            pxglDto.setCsts(String.valueOf(danxts+duoxts+jdts+tkts+pdts));

        }
        if(pxglDto.getTpfjid()!=null&& !Objects.equals(pxglDto.getTpfjid(), "")){
            boolean saveFile = fjcfbService.save2RealFile(pxglDto.getTpfjid(),pxglDto.getPxid());
            if(!saveFile)
                throw new BusinessException("msg","附件保存失败!");
        }
        if(pxglDto.getSpfjid()!=null&& !Objects.equals(pxglDto.getSpfjid(), "")){
            String[] split = pxglDto.getSpfjid().split(",");
            for(int i=0;i<split.length;i++){
                boolean saveFile = fjcfbService.saveXhFile(split[i],pxglDto.getPxid(),null,String.valueOf(i+1));
                if(!saveFile)
                    throw new BusinessException("msg","附件保存失败!");
            }
        }
        return update(pxglDto);
    }


    /**
     * 递归增加部门
     */
    public void addDepartment(String yhm,Long id,List<Long> bmlist)
    {
        List<Long> list = talkUtil.getNextDepartmentsByYhm(yhm, id);
        if(list!=null){
            bmlist.addAll(list);
            for(Long s:list){
                addDepartment(yhm,s,bmlist);
            }
        }
    }

    @Override
    public List<User> getListByYhms(PxglDto pxglDto) {
        return dao.getListByYhms(pxglDto);
    }

    @Override
    public List<PxglDto> getDtoListByFzr(PxglDto pxglDto) {
        return dao.getDtoListByFzr(pxglDto);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        PxglDto pxglDto = (PxglDto) baseModel;
        pxglDto.setXgry(operator.getYhid());
        if(!CollectionUtils.isEmpty(pxglDto.getFjids())){
            for (int i = 0;i<pxglDto.getFjids().size();i++) {
                boolean saveFile = fjcfbService.save2RealFile(pxglDto.getFjids().get(i), pxglDto.getPxid());
                if(!saveFile){
                    return false;
                }

            }
        }
        if ("1".equals(pxglDto.getSffshb())){
            DsrwszDto dsrwszDto = new DsrwszDto();
            if (StringUtil.isNotBlank(pxglDto.getYhbszid())) {
                HbszDto hbszDto = hbszService.getDtoById(pxglDto.getYhbszid());
                dsrwszDto.setRwid(hbszDto.getRwid());
                dsrwszDto = dsrwszService.getDto(dsrwszDto);
                if (null!=dsrwszDto) {
                    String string = "";
                    dsrwszDto.setCs(string);
                    boolean flag = dsrwszService.updateDsxx(dsrwszDto);
                    if (!flag) {
                        throw new BusinessException("msg", "修改定时任务失败!");
                    }
                }
            }
            if (!pxglDto.getYhbszid().equals(pxglDto.getHbszid())) {
                HbszDto hbszDto1 = hbszService.getDtoById(pxglDto.getHbszid());
                DsrwszDto dsrwszDto1 = new DsrwszDto();
                dsrwszDto1.setRwid(hbszDto1.getRwid());
                dsrwszDto1 = dsrwszService.getDto(dsrwszDto1);
                if (null!=dsrwszDto1) {
                    String string1 = "pxid=" + pxglDto.getPxid();
                    dsrwszDto1.setCs(string1);
                    boolean flag1 = dsrwszService.updateDsxx(dsrwszDto1);
                    if (!flag1) {
                        throw new BusinessException("msg", "修改定时任务失败!");
                    }
                }
            }

        }else {
            if (StringUtil.isNotBlank(pxglDto.getYhbszid())){
                HbszDto hbszDto=hbszService.getDtoById(pxglDto.getYhbszid());
                DsrwszDto dsrwszDto=new DsrwszDto();
                dsrwszDto.setRwid(hbszDto.getRwid());
                dsrwszDto=dsrwszService.getDto(dsrwszDto);
                if (null!=dsrwszDto) {
                    String string = "";
                    dsrwszDto.setCs(string);
                    boolean flag = dsrwszService.updateDsxx(dsrwszDto);
                    if (!flag) {
                        throw new BusinessException("msg", "修改定时任务失败!");
                    }
                }
            }
        }
        if("1".equals(pxglDto.getFlag())){
            return update(pxglDto);
        }else{
            return updatePxglDto(pxglDto);
        }
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
//        String token = talkUtil.getToken();
        String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
        String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
        String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
        String ICOMM_PX00010 = xxglService.getMsg("ICOMM_PX00010");
        String ICOMM_PX00011 = xxglService.getMsg("ICOMM_PX00011");
        String ICOMM_PX00012 = xxglService.getMsg("ICOMM_PX00012");
        for (ShgcDto shgcDto : shgcList) {
            PxglDto pxglDto = new PxglDto();
            pxglDto.setPxid(shgcDto.getYwid());
            pxglDto.setXgry(operator.getYhid());
            PxglDto pxglDto_t = getDtoById(pxglDto.getPxid());
            shgcDto.setSqbm(pxglDto_t.getBm());
            List<SpgwcyDto> spgwcyDtos = commonService.siftJgList(shgcDto.getSpgwcyDtos(),pxglDto_t.getBm());
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                pxglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00026,
                                    StringUtil.replaceMsg(ICOMM_PX00011, operator.getZsxm(), shgcDto.getShlbmc(), pxglDto_t.getPxbt(), pxglDto_t.getPxlb(), pxglDto_t.getSsgsmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                pxglDto.setZt(StatusEnum.CHECK_PASS.getCode());
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    int size = spgwcyDtos.size();
                    for (int i = 0; i < size; i++) {
                        if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00006,
                                    StringUtil. replaceMsg(ICOMM_PX00012, operator.getZsxm(),shgcDto.getShlbmc() ,pxglDto_t.getPxbt(),pxglDto_t.getPxlb(),pxglDto_t.getSsgsmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                if (StringUtil.isNotBlank(pxglDto_t.getHbszid())){
                    DsrwszDto dsrwszDto = new DsrwszDto();
                    HbszDto hbszDto=hbszService.getDtoById(pxglDto_t.getHbszid());
                    dsrwszDto.setRwid(hbszDto.getRwid());
                    dsrwszDto.setRwmc(hbszDto.getHbmc());
                    dsrwszDto.setDsxx(CronUtil.getCron(hbszDto.getFssj()));
                    dsrwszDto.setZxl("pxglServiceImpl");
                    dsrwszDto.setZxff("remindRedPacket");
                    dsrwszDto.setLrry(hbszDto.getLrry());
                    dsrwszDto.setCs("pxid="+pxglDto_t.getPxid()+",xh="+hbszDto.getXh()+",wbcxid="+operator.getWbcxid());
                    dsrwszDto.setRemindtype(remindtype);
                    boolean isSuccess = dsrwszService.insert(dsrwszDto);
                    if (!isSuccess){
                        throw new BusinessException("msg","新增定时任务信息失败");
                    }
                    Map<String,Object>newMap=new HashMap<>();
                    newMap.put("type","add");
                    newMap.put("dsrwid",dsrwszDto.getRwid());
                    amqpTempl.convertAndSend("sys.igams", preRabbitFlg+"sys.igams.task.taskHandle"+rabbitFlg+"1", JSONObject.toJSONString(newMap));
                }
            }else {
                pxglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00003, StringUtil.replaceMsg(ICOMM_PX00010,
                                        operator.getZsxm(), shgcDto.getShlbmc(), pxglDto_t.getPxbt(), pxglDto_t.getPxlb(), pxglDto_t.getSsgsmc(),
                                        DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
                //发送钉钉消息--取消审核人员
                if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())){
                    int size = shgcDto.getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
                            		shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,pxglDto_t.getPxbt(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(pxglDto);
        }
        return true;
    }

    @Override
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        // TODO Auto-generated method stub
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String pxid = shgcDto.getYwid();
                PxglDto pxglDto = new PxglDto();
                pxglDto.setXgry(operator.getYhid());
                pxglDto.setPxid(pxid);
                pxglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(pxglDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String pxid = shgcDto.getYwid();
                PxglDto pxglDto = new PxglDto();
                pxglDto.setXgry(operator.getYhid());
                pxglDto.setPxid(pxid);
                pxglDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(pxglDto);
            }
        }
        return true;
    }

    @Override
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
        return null;
    }

    @Override
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
        Map<String, Object> map =new HashMap<>();
        @SuppressWarnings("unchecked")
        List<String> ids = (List<String>)param.get("ywids");
        PxglDto pxglDto = new PxglDto();
        pxglDto.setIds(ids);
        List<PxglDto> dtoList = dao.getDtoList(pxglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(PxglDto dto:dtoList){
                list.add(dto.getPxid());
            }
        }
        map.put("list",list);
        return map;
    }

    /**
     * 审核列表
     */
    public List<PxglDto> getPagedAuditTrain(PxglDto pxglDto){
        // 获取人员ID和履历号
        List<PxglDto> t_List= dao.getPagedAuditTrain(pxglDto);

        if (CollectionUtils.isEmpty(t_List))
            return t_List;

        List<PxglDto> sqList = dao.getAuditListTrain(t_List);

        commonService.setSqrxm(sqList);

        return sqList;
    }

    /**
     * 获取统计年份
     */
    public List<PxglDto> getYearGroup(){
        return dao.getYearGroup();
    }

    /**
     * 获取培训类别统计
     */
    public List<PxglDto> getTypeStatis(){
        return dao.getTypeStatis();
    }

    /**
     * 获取培训个数统计
     */
    public List<PxglDto> getCountStatis(PxglDto pxglDto){
        return dao.getCountStatis(pxglDto);
    }
	/**
     * 根据选择信息获取导出信息
     */
    public List<PxglDto> getListForSelectExp(Map<String,Object> params){
        PxglDto pxglDto = (PxglDto)params.get("entryData");
        queryJoinFlagExport(params,pxglDto);
        return dao.getListForSelectExp(pxglDto);
    }


    /**
     * 根据搜索条件获取导出条数
     */
    public int getCountForSearchExp(PxglDto pxglDto,Map<String, Object> params) {
        return dao.getCountForSearchExp(pxglDto);
    }
    /**
     * 根据搜索条件分页获取导出信息
     */

    public List<PxglDto> getListForSearchExp(Map<String, Object> params)
    {
        PxglDto pxglDto = (PxglDto)params.get("entryData");
        queryJoinFlagExport(params,pxglDto);

        return dao.getListForSearchExp(pxglDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String,Object> params,PxglDto pxglDto){
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
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
        pxglDto.setSqlParam(sqlcs);
    }

    @Override
    public List<Map<String, Object>> getTrainStausGroup(PxglDto pxglDto) {
        return dao.getTrainStausGroup(pxglDto);
    }

    @Override
    public Integer getTrainStausCount(PxglDto pxglDto) {
        return dao.getTrainStausCount(pxglDto);
    }

    @Override
    public List<Map<String, Object>> getTrainGroupLb(PxglDto pxglDto) {
        return dao.getTrainGroupLb(pxglDto);
    }

    @Override
    public List<String> getTrainGqPxBts(PxglDto pxglDto) {
        return dao.getTrainGqPxBts(pxglDto);
    }

    @Override
    public List<String> getAllUses(PxglDto pxglDto) {
        return dao.getAllUses(pxglDto);
    }

    public void remindDueTasks(Map<String,String> dqts) {
        PxglDto pxglDto=new PxglDto();
        pxglDto.setDqts(dqts.get("dqts"));
//        String token = talkUtil.getToken();
        //小程序访问
        List<PxglDto> pxglDtoList= getDtoList(pxglDto);
        for (PxglDto pxglDto1:pxglDtoList){
            GzglDto gzglDto=new GzglDto();
            gzglDto.setYwid(pxglDto1.getPxid());
            gzglDto.setZt("00");
            List<GzglDto> gzglDtoList=gzglService.getDtoList(gzglDto);
            for (GzglDto gzglDto1:gzglDtoList){
                String external = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/learningpage/videolist/videolist&urlPrefix=" + urlPrefix + "&gzid=" + gzglDto1.getGzid() +"&ywid="+gzglDto1.getYwid()+"&pxbt="+pxglDto1.getPxbt()+"&TabCur="+"0", StandardCharsets.UTF_8);
                List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                btnJsonList.setTitle("小程序");
                btnJsonList.setActionUrl(external);
                btnJsonLists.add(btnJsonList);
                talkUtil.sendCardMessage(gzglDto1.getYhm(), gzglDto1.getDdid(), xxglService.getMsg("ICOMM_PX00003"), StringUtil.replaceMsg(xxglService.getMsg("ICOMM_PX00004"),pxglDto1.getPxbt(),pxglDto1.getPxlb(),pxglDto1.getGqsj(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
            }
        }
    }
	 /**
     * 定时往群里发送红包
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void remindRedPacket(Map<String,String> pxid) {
        String xhStr = pxid.get("xh");
        String wbcxid = pxid.get("wbcxid");
        if (StringUtil.isBlank(wbcxid)){
            wbcxid = "matridxOA";
        }
        PxglDto pxglDto=getDtoById(pxid.get("pxid"));
        HbszDto hbszDto=hbszService.getDtoById(pxglDto.getHbszid());
        List<GzglDto> list=new ArrayList<>();
        GzglDto gzglDto_t=new GzglDto();
        gzglDto_t.setYwid(pxglDto.getPxid());
        List<GzglDto> verification = gzglService.verification(gzglDto_t);
        List<String> userList=getAllUses(pxglDto);
        for (String user:userList){
            GzglDto gzglDto=new GzglDto();
            gzglDto.setGzid(StringUtil.generateUUID());
            gzglDto.setYwid(pxglDto.getPxid());
            gzglDto.setYwmc(pxglDto.getPxbt());
            gzglDto.setZt(StatusEnum.CHECK_NO.getCode());
            gzglDto.setUrlqz(urlPrefix);
            gzglDto.setYwdz("/train/train/pagedataViewTrainDetails?pxid=" +gzglDto.getYwid() );
            gzglDto.setTgbj("0");
            gzglDto.setPxbt(pxglDto.getPxbt());
            gzglDto.setPxlb(pxglDto.getPxlb());
            gzglDto.setFzr(user);
            gzglDto.setGlbj("1");
            list.add(gzglDto);
        }

        for(int i=0;i<list.size();i++){
            for(GzglDto yczDto:verification){
                if(list.get(i).getYwid().equals(yczDto.getYwid())&&list.get(i).getFzr().equals(yczDto.getFzr())){
                    if(i==0){
                        list.remove(i);
                    }else{
                        list.remove(i--);
                    }
                }
                if(CollectionUtils.isEmpty(list)){
                    break;
                }
            }
            if(CollectionUtils.isEmpty(list)){
                break;
            }
        }
        boolean isSuccess = false;
         if(!CollectionUtils.isEmpty(list)){
            List<List<GzglDto>> partition = Lists.partition(list, 100);
            for (List<GzglDto> gzglDtos : partition) {
                isSuccess = gzglService.insertList(gzglDtos);
            }
            if (isSuccess) {
                pxglDto.setGzid(list.get(0).getGzid());
                PxglDto pxglDto_t = this.viewTrainTask(pxglDto);
                //初始化人员信息
                Map<String, Map<String, Object>> ryInfoInit = new HashMap<>();
                Map<String, Map<String, Object>> gzIdInit = new HashMap<>();
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> map_gz = new HashMap<>();
                for (GzglDto gzglDto : list) {
                    GrksglDto grksglDto = new GrksglDto();
                    grksglDto.setKscs(pxglDto_t.getKscs());
                    grksglDto.setRyid(gzglDto.getFzr());
                    grksglDto.setTgbj("0");
                    grksglDto.setPxid(pxid.get("pxid"));
                    map.put(gzglDto.getFzr(),JSON.toJSONString(grksglDto));
                    //gzid fzr
                    GzglDto gzglDto_id = new GzglDto();
                    gzglDto_id.setGzid(gzglDto.getGzid());
                    gzglDto_id.setFzr(gzglDto.getFzr());
                    map_gz.put(gzglDto.getFzr(),JSON.toJSONString(gzglDto_id));
                }
                ryInfoInit.put(RedisCommonKeyEnum.TRAIN_USERPXINFO.getKey() + pxid.get("pxid"),map);
                gzIdInit.put(RedisCommonKeyEnum.TRAIN_TRAINGZID.getKey() + pxid.get("pxid"),map_gz);
                redisUtil.hBatchSet(ryInfoInit, RedisCommonKeyEnum.TRAIN_USERPXINFO.getExpire());
                redisUtil.hBatchSet(gzIdInit, RedisCommonKeyEnum.TRAIN_TRAINGZID.getExpire());
                //由于继承BaseBasicModel过滤字段
                SimplePropertyPreFilter filter = new SimplePropertyPreFilter(PxglDto.class, "pxbt", "pxid", "pxnr", "sfcs", "kscs", "spbj"
                        , "pxjd", "qrry", "qrryid", "csts", "cssc", "cszf", "tzfsbj", "htbj", "spwcbj", "gzid", "tgfs", "ckdabj", "dcckbj", "danxts", "duoxts", "jdts"
                        , "spxz", "hbszid", "dtymlj", "hbfmlj", "color", "gqsj");
                SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
                listFilters[0] = filter;
                redisUtil.set(RedisCommonKeyEnum.TRAIN_TRAINPXINFO.getKey() + pxid.get("pxid"), JSON.toJSONString(pxglDto_t, listFilters, SerializerFeature.DisableCircularReferenceDetect), RedisCommonKeyEnum.TRAIN_TRAINPXINFO.getExpire());
                HbszmxDto hbszmxDto_t = new HbszmxDto();
                hbszmxDto_t.setHbszid(pxglDto_t.getHbszid());
                List<HbszmxDto> hbszmxDtos = hbszmxService.getDtoList(hbszmxDto_t);
                redisUtil.set(RedisCommonKeyEnum.TRAIN_HBSZMX.getKey() + pxglDto.getHbszid(), JSON.toJSONString(hbszmxDtos),RedisCommonKeyEnum.TRAIN_HBSZMX.getExpire());
            }
        }
        if (isSuccess) {
            String pictureUrl=hbszDto.getTpfm();
            UserDto userDto_sel = new UserDto();
            userDto_sel.setWbcxid(wbcxid);
            UserDto userDto = userService.getWbcxDto(userDto_sel);
            DBEncrypt bpe = new DBEncrypt();
            String dingtalkurl = bpe.dCode(userDto.getJumpdingtalkurl());
            String external = dingtalkurl+ "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/learningpage/DragonGraph/DragonGraph&urlPrefix=" + urlPrefix + "&pxid=" + pxid.get("pxid")+"&xh="+(StringUtil.isNotBlank(xhStr)?Integer.parseInt(xhStr):0), StandardCharsets.UTF_8);
            log.error("remindRedPacket-----------external{}",external);
            boolean flag=talkUtil.sendGroupMessageAtTo(hbszDto.getTzxx(), hbszDto.getWebhook(), pictureUrl,external);
            if (flag){
                DsrwszDto dsrwszDto=new DsrwszDto();
                dsrwszDto.setRwid(hbszDto.getRwid());
                dsrwszService.deleteByRwid(dsrwszDto);
                Map<String,Object>newMap=new HashMap<>();
                newMap.put("type","del");
                newMap.put("dsrwid",hbszDto.getRwid());
                amqpTempl.convertAndSend("sys.igams", preRabbitFlg+"sys.igams.task.taskHandle"+rabbitFlg+"1", JSONObject.toJSONString(newMap));
            }
        }
    }

    @Override
    public List<PxglDto> getTrainRecords(PxglDto pxglDto) {
        return dao.getTrainRecords(pxglDto);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean generatesignincodeSaveTrain(PxglDto pxglDto) throws BusinessException {
        pxglDto.setXgry(pxglDto.getLrry());
        boolean isSuccess = update(pxglDto);
        PxglDto pxglDto_sel=getDtoById(pxglDto.getPxid());
        List<GzglDto> dtoTrainSignInPeo = new ArrayList<>();
        List<GzglDto> addGzglDtos = new ArrayList<>();
        //是否删除
        boolean isDel = false;
        if ("1".equals(pxglDto.getSfxzqd())){
            GzglDto gzglDto_sel = new GzglDto();
            gzglDto_sel.setPxid(pxglDto.getPxid());
            dtoTrainSignInPeo = gzglService.getDtoTrainSignInPeo(gzglDto_sel);
            if (StringUtil.isNotBlank(pxglDto.getQdyhids())){
                List<String> yhids = JSON.parseArray(pxglDto.getQdyhids(), String.class);
                if (!CollectionUtils.isEmpty(yhids)){
                    for (String yhid : yhids) {
                        GzglDto gzglDto=new GzglDto();
                        gzglDto.setGzid(StringUtil.generateUUID());
                        gzglDto.setYwid(pxglDto_sel.getPxid());
                        gzglDto.setYwmc(pxglDto_sel.getPxbt());
                        gzglDto.setZt(StatusEnum.CHECK_NO.getCode());
                        gzglDto.setUrlqz(urlPrefix);
                        gzglDto.setYwdz("/train/train/pagedataViewTrainDetails?pxid=" +gzglDto.getYwid() );
                        gzglDto.setTgbj("0");
                        gzglDto.setPxbt(pxglDto_sel.getPxbt());
                        gzglDto.setPxlb(pxglDto_sel.getPxlb());
                        gzglDto.setFzr(yhid);
                        gzglDto.setGlbj("1");
                        gzglDto.setSfqd("0");
                        gzglDto.setLrry(pxglDto.getLrry());
                        addGzglDtos.add(gzglDto);
                    }
                    if (!CollectionUtils.isEmpty(dtoTrainSignInPeo)){
                        List<GzglDto> gzglDtos = new ArrayList<>(dtoTrainSignInPeo);
                        for (GzglDto gzglDto : addGzglDtos) {
                            //剩下的是删除的
                            dtoTrainSignInPeo.removeIf(e->e.getFzr().equals(gzglDto.getFzr()));
                        }
                        for (GzglDto gzglDto : gzglDtos) {
                            //剩下的是新增的
                           addGzglDtos.removeIf(e -> e.getFzr().equals(gzglDto.getFzr()));
                        }
                    }
                    if (!CollectionUtils.isEmpty(dtoTrainSignInPeo)){
                        isDel = true;
                    }
                }else {
                    isDel = true;
                }
            }else {
                isDel = true;
            }
        }
        //else {
        //是否限制改为否后不做删除
        //isDel = true;
        //}
        if (isDel){
            GzglDto gzglDto = new GzglDto();
            gzglDto.setPxid(pxglDto.getPxid());
            gzglDto.setScry(pxglDto.getLrry());
            if (!CollectionUtils.isEmpty(dtoTrainSignInPeo)){
                //要删除的
                gzglDto.setIds(dtoTrainSignInPeo.stream().map(GzglDto::getGzid).distinct().collect(Collectors.toList()));
            }
            gzglService.deleteByTrainSignIn(gzglDto);
        }
        if (!CollectionUtils.isEmpty(addGzglDtos)){
            isSuccess = gzglService.insertList(addGzglDtos);
            if(!isSuccess)
                throw new BusinessException("msg","新增工作记录失败!");
        }
        FjcfbDto fjcfbDto_sel = new FjcfbDto();
        fjcfbDto_sel.setYwlx(BusTypeEnum.IMP_TRAIN_QRCODE.getCode());
        fjcfbDto_sel.setYwid(pxglDto.getPxid());
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto_sel);
        if (CollectionUtils.isEmpty(fjcfbDtos)){
            DBEncrypt bpe = new DBEncrypt();
            String fileName = pxglDto_sel.getPxbt()+".jpg";
            String path = prefix + tempFilePath + BusTypeEnum.IMP_TRAIN_QRCODE.getCode();
            UserDto userDto_sel = new UserDto();
            userDto_sel.setWbcxid(pxglDto.getWbcxid());
            UserDto userDto = userService.getWbcxDto(userDto_sel);
            String dingtalkurl = bpe.dCode(userDto.getJumpdingtalkurl());
            String external = dingtalkurl + "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/learningpage/signInCodePage/signInCodePage&urlPrefix=" + urlPrefix + "&pxid=" + pxglDto.getPxid(), StandardCharsets.UTF_8);
            log.error("培训签到访问路径："+external);
            String filePath = QrCodeUtil.createQrCode(external, path, fileName);
            FjcfbDto fjcfbDto = new FjcfbDto();
            fjcfbDto.setFjid(StringUtil.generateUUID());
            fjcfbDto.setYwid(pxglDto.getPxid());
            fjcfbDto.setXh("1");
            fjcfbDto.setYwlx(BusTypeEnum.IMP_TRAIN_QRCODE.getCode());
            fjcfbDto.setWjm(fileName);
            fjcfbDto.setWjlj(bpe.eCode(filePath));
            fjcfbDto.setFwjlj(bpe.eCode(path));
            fjcfbDto.setFwjm(bpe.eCode(fileName));
            fjcfbDto.setZhbj("0");
            fjcfbDto.setLrry(pxglDto.getLrry());
            isSuccess = fjcfbService.insert(fjcfbDto);
            if(!isSuccess) {
                throw new BusinessException("msg", "保存签到二维码附件失败!");
            }
        }
        return isSuccess;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean signInTrainForDD(PxglDto pxglDto) throws BusinessException{
        PxglDto pxglDto_sel = getDtoById(pxglDto.getPxid());
        String qwwcsj = DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd");
        String lrqwwcsj = DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss");
        //如果是，根据培训id和用户id查工作管理表，如果查到用户，
        GzglDto gzglDto_sel = new GzglDto();
        gzglDto_sel.setYwid(pxglDto.getPxid());
        gzglDto_sel.setFzr(pxglDto.getLrry());
        gzglDto_sel.setSfqd("notnull");
        GzglDto gzglDto = gzglService.getGzInfoByFzrAndYwid(gzglDto_sel);
        if (gzglDto!=null&&"1".equals(gzglDto.getSfqd())){
            throw new BusinessException("msg","您已签到,无需重复签到");
        }
        String jlbh ="";
        if (StringUtil.isNotBlank(pxglDto_sel.getJgkzcs1())){
            String[] strings = qwwcsj.split("-");
            String rq = strings[0]+"-"+strings[1]+strings[2];
            GzglDto gzglDto_t=new GzglDto();
            gzglDto_t.setJlbh(pxglDto_sel.getJgkzcs1() + "-" + rq + "-");
            gzglDto_t.setYwid(pxglDto_sel.getPxid());
            String serial = gzglService.getJlbhSerial(gzglDto_t);
            jlbh=pxglDto_sel.getJgkzcs1() + "-" + rq + "-"+serial;
        }
        //后台查询这个培训的限制签到人员是否为1，
        if ("1".equals(pxglDto_sel.getSfxzqd())){
            if (gzglDto!=null){
                //根据工作管理id修改工作管理表的是否签到为1，
                GzglDto gzglDto_up = new GzglDto();
                gzglDto_up.setGzid(gzglDto.getGzid());
                gzglDto_up.setSfqd("1");
                //期望完成时间为当前时间，记录编号根据期望完成时间和培训部门自动生成
                gzglDto_up.setQwwcsj(lrqwwcsj);
                gzglDto_up.setJlbh(jlbh);
                boolean isSuccess = gzglService.update(gzglDto_up);
                if (!isSuccess){
                    throw new BusinessException("msg","签到失败,请联系管理员!");
                }
            }else {
                //如果查不到用户，返回页面，签到失败！您无需签到！
                throw new BusinessException("msg","签到失败!您无需签到!");
            }
        }else {
            boolean isSuccess = false;
            if (gzglDto!=null){
                //根据工作管理id修改工作管理表的是否签到为1，
                GzglDto gzglDto_up = new GzglDto();
                gzglDto_up.setGzid(gzglDto.getGzid());
                gzglDto_up.setSfqd("1");
                //期望完成时间为当前时间，记录编号根据期望完成时间和培训部门自动生成
                gzglDto_up.setQwwcsj(lrqwwcsj);
                gzglDto_up.setJlbh(jlbh);
                isSuccess = gzglService.update(gzglDto_up);
            }else {
                GzglDto gzglDto_add =new GzglDto();
                gzglDto_add.setGzid(StringUtil.generateUUID());
                gzglDto_add.setYwid(pxglDto_sel.getPxid());
                gzglDto_add.setYwmc(pxglDto_sel.getPxbt());
                gzglDto_add.setZt(StatusEnum.CHECK_NO.getCode());
                gzglDto_add.setUrlqz(urlPrefix);
                gzglDto_add.setYwdz("/train/train/pagedataViewTrainDetails?pxid=" +gzglDto_add.getYwid() );
                gzglDto_add.setTgbj("0");
                gzglDto_add.setPxbt(pxglDto_sel.getPxbt());
                gzglDto_add.setPxlb(pxglDto_sel.getPxlb());
                gzglDto_add.setFzr(pxglDto.getLrry());
                gzglDto_add.setGlbj("1");
                gzglDto_add.setSfqd("1");
                gzglDto_add.setJlbh(jlbh);
                gzglDto_add.setQwwcsj(lrqwwcsj);
                gzglDto_add.setLrry(pxglDto.getLrry());
                isSuccess = gzglService.insertDtobyfzr(gzglDto_add);
            }
            if (!isSuccess){
                throw new BusinessException("msg","签到失败,请联系管理员!");
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modresultSaveTrain(PxglDto pxglDto){
        if (!CollectionUtils.isEmpty(pxglDto.getList())){
            return gzglService.updateGzglDtos(pxglDto.getList());
        }
        return true;
    }
    /*
        培训提醒部门负责人
     */
    public void remindTrainManager(Map<String,String> map){
        //提醒天数
        String txts = map.get("txts");
        if (StringUtil.isBlank(txts)){
            txts = "30";
        }
        //外部程序id
        String wbcxid = map.get("wbcxid");
        if (StringUtil.isBlank(wbcxid)){
            wbcxid = "matridxOA";
        }
        XxdyDto xxdyDto = new XxdyDto();
        xxdyDto.setCskz1("BM");
        List<XxdyDto> dtoList = xxdyService.getDtoList(xxdyDto);
        if (!CollectionUtils.isEmpty(dtoList)){
            List<String> jgids = dtoList.stream().map(XxdyDto::getDyxx).distinct().collect(Collectors.toList());
            JgxxDto jgxxDto = new JgxxDto();
            jgxxDto.setWbcxid(wbcxid);
            List<JgxxDto> jgxxDtos = jgxxService.getJgxxByWbcx(jgxxDto);
            List<Map<String,String>> jgxxMapList = new ArrayList<>();
            for (String jgid : jgids) {
                List<JgxxDto> zjgxxs = new ArrayList<>();
                jgxxService.getJgxxByFjgid(jgid,jgxxDtos,zjgxxs);
                Map<String, String> jgxxMap = new HashMap<>();
                for (JgxxDto dto : jgxxDtos) {
                    if (jgid.equals(dto.getJgid())){
                        zjgxxs.add(dto);
                        jgxxMap.put("bmzgs",dto.getBmzgs());
                        break;
                    }
                }
                jgxxMap.put("zjgids",zjgxxs.stream().map(JgxxDto::getJgid).distinct().collect(Collectors.joining(",")));
                jgxxMap.put("jgid",jgid);
                jgxxMapList.add(jgxxMap);
            }
            GzglDto gzglDto = new GzglDto();
            gzglDto.setMapList(jgxxMapList);
            gzglDto.setTxts(txts);
            gzglDto.setWbcxid(wbcxid);
            List<Map<String,Object>> maps = gzglService.getRemindTrainInfo(gzglDto);
            if (!CollectionUtils.isEmpty(maps)){
                String ICOMM_PXTX001 = xxglService.getMsg("ICOMM_PXTX001");
                String ICOMM_PXTX002 = xxglService.getMsg("ICOMM_PXTX002");
                for (Map<String, Object> resultMap : maps) {
                    //未完成条数
                    int wwcts = Integer.parseInt(String.valueOf(resultMap.get("wwcts")));
                    if (wwcts>0){
                        // 内网访问
                        String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/learningpage/trainRemind/trainRemind&urlPrefix="+urlPrefix+"&jgid="+resultMap.get("jgid")+"&txts="+txts+"&wbcxid="+wbcxid+"&wbfw=1", StandardCharsets.UTF_8);
                        log.error("remindTrainManager--internalbtn={}",internalbtn);
                        //外网访问
                        List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                        OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                        btnJsonList.setTitle("查看");
                        btnJsonList.setActionUrl(internalbtn);
                        btnJsonLists.add(btnJsonList);
                        talkUtil.sendCardMessage(String.valueOf(resultMap.get("yhm")),null,ICOMM_PXTX001,ICOMM_PXTX002,
                                btnJsonLists,"1");
                    }
                }
            }

        }
    }
    /*
        培训提醒统计数据
     */
    @Override
    public Map<String, Object> getTrainRemindStatis(PxglDto pxglDto) {
        Map<String, Object> map = new HashMap<>();
        JgxxDto jgxxDto = new JgxxDto();
        jgxxDto.setWbcxid(pxglDto.getWbcxid());
        List<JgxxDto> jgxxDtos = jgxxService.getJgxxByWbcx(jgxxDto);
        List<Map<String,String>> jgxxMapList = new ArrayList<>();
        List<JgxxDto> zjgxxs = new ArrayList<>();
        jgxxService.getJgxxByFjgid(pxglDto.getJgid(),jgxxDtos,zjgxxs);
        Map<String, String> jgxxMap = new HashMap<>();
        for (JgxxDto dto : jgxxDtos) {
            if (pxglDto.getJgid().equals(dto.getJgid())){
                zjgxxs.add(dto);
                jgxxMap.put("bmzgs",dto.getBmzgs());
                break;
            }
        }
        jgxxMap.put("zjgids",zjgxxs.stream().map(JgxxDto::getJgid).distinct().collect(Collectors.joining(",")));
        jgxxMap.put("jgid",pxglDto.getJgid());
        jgxxMapList.add(jgxxMap);
        GzglDto gzglDto = new GzglDto();
        gzglDto.setMapList(jgxxMapList);
        gzglDto.setTxts(pxglDto.getTxts());
        gzglDto.setWbcxid(pxglDto.getWbcxid());
        List<Map<String,Object>> remindStatis = gzglService.getRemindTrainGroupPeo(gzglDto);
        map.put("remindStatis",remindStatis);
        List<Map<String,Object>> remindList = gzglService.getRemindTrainList(gzglDto);
        map.put("remindList",remindList);
        return map;
    }
}

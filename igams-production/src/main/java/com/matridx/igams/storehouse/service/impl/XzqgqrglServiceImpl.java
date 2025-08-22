package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.DdAuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.IDdfbsglService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.service.svcinterface.IQgglService;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.service.svcinterface.IXzqgfkglService;
import com.matridx.igams.storehouse.service.svcinterface.IXzqgfkmxService;
import com.matridx.igams.storehouse.service.svcinterface.IXzqgqrmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.post.IXzqgqrglDao;
import com.matridx.igams.storehouse.service.svcinterface.IXzqgqrglService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class XzqgqrglServiceImpl extends BaseBasicServiceImpl<XzqgqrglDto, XzqgqrglModel, IXzqgqrglDao> implements IXzqgqrglService, IAuditService {

    @Autowired
    private IXzqgqrmxService xzqgqrmxService;
    @Autowired
    private IXzqgfkglService xzqgfkglService;
    @Autowired
    private IXzqgfkmxService xzqgfkmxService;
    @Autowired
    private IQgmxService qgmxService;
	@Autowired
    ICommonService commonservice;
	@Autowired
    IQgglService qgglService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IDdfbsglService ddfbsglService;
    @Autowired
    IDdspglService ddspglService;
    @Autowired
    IDdxxglService ddxxglService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IShxxService shxxService;
    @Value("${matridx.rabbit.systemreceiveflg:}")
    private String systemreceiveflg;
    @Value("${matridx.systemflg.systemname:}")
    private String systemname;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.wechat.registerurl:}")
    private String registerurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    ICommonService commonService;
    private final Logger log = LoggerFactory.getLogger(XzqgqrglServiceImpl.class);

    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertDto(XzqgqrglDto xzqgqrglDto){
        if(StringUtils.isBlank(xzqgqrglDto.getQrid())){
            xzqgqrglDto.setQrid(StringUtil.generateUUID());
        }
        xzqgqrglDto.setZt(StatusEnum.CHECK_NO.getCode());
        int result=dao.insert(xzqgqrglDto);
        return result > 0;
    }

    /**
     * 处理月结时的数据
     * @param xzqgqrglDto
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean dealMonthBalanceData(XzqgqrglDto xzqgqrglDto) throws BusinessException{
        List<XzqgqrmxDto> xzqgqrmxs= JSON.parseArray(xzqgqrglDto.getQgqrmx_json(), XzqgqrmxDto.class);
        if (CollectionUtils.isEmpty(xzqgqrmxs)){
            throw new BusinessException("msg","明细不能为空!");
        }
        boolean addXzqgqrgl=insertDto(xzqgqrglDto);
        if(!addXzqgqrgl)
            return false;
        xzqgqrglDto.setYwid(xzqgqrglDto.getQrid());
        if(!CollectionUtils.isEmpty(xzqgqrmxs)){
            for (XzqgqrmxDto xzqgqrmx : xzqgqrmxs) {
                xzqgqrmx.setQrid(xzqgqrglDto.getQrid());
                xzqgqrmx.setLrry(xzqgqrglDto.getLrry());
                xzqgqrmx.setQrmxid(StringUtil.generateUUID());
            }
            boolean addmx= xzqgqrmxService.insertDtoList(xzqgqrmxs);
            if(!addmx)
                return false;
            //更新请购明细是否需要入库信息
            xzqgqrmxService.modQgmxsfrk(xzqgqrmxs);
        }
        return true;
    }

    /**
     * 处理公对公的数据
     * @param xzqgqrglDto
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean dealPtpBalanceData(XzqgqrglDto xzqgqrglDto){
        //公对公时直接生成付款单
        XzqgfkglDto xzqgfkglDto=new XzqgfkglDto();
        xzqgfkglDto.setFkdh(xzqgqrglDto.getFkdh());
        xzqgfkglDto.setDzfs(xzqgqrglDto.getDzfs());
        xzqgfkglDto.setZwfkrq(xzqgqrglDto.getZwfkrq());
        xzqgfkglDto.setFkje(xzqgqrglDto.getFkje());
        xzqgfkglDto.setFkf(xzqgqrglDto.getFkf());
        xzqgfkglDto.setFkfs(xzqgqrglDto.getFkfs());
        xzqgfkglDto.setZfdx(xzqgqrglDto.getZfdx());
        xzqgfkglDto.setZffkhh(xzqgqrglDto.getZffkhh());
        xzqgfkglDto.setZffyhzh(xzqgqrglDto.getZffyhzh());
        xzqgfkglDto.setBz(xzqgqrglDto.getBz());
        xzqgfkglDto.setFygs(xzqgqrglDto.getFygs());
        xzqgfkglDto.setFksy(xzqgqrglDto.getFksy());
        xzqgfkglDto.setLrry(xzqgqrglDto.getLrry());
        xzqgfkglDto.setSqr(xzqgqrglDto.getSqr());
        xzqgfkglDto.setSqbm(xzqgqrglDto.getSqbm());
        xzqgfkglDto.setDjcdfs(xzqgqrglDto.getDjcdfs());
        xzqgfkglDto.setFylb(xzqgqrglDto.getFylb());
        boolean addFkgl=xzqgfkglService.insertDto(xzqgfkglDto);
        if(!addFkgl)
            return false;
        xzqgqrglDto.setYwid(xzqgfkglDto.getFkid());
        //保存付款明细
        List<XzqgqrmxDto> xzqgqrmxs= JSON.parseArray(xzqgqrglDto.getQgqrmx_json(), XzqgqrmxDto.class);
        if(!CollectionUtils.isEmpty(xzqgqrmxs)) {
            List<XzqgfkmxDto> xzfkmxDtos=new ArrayList<>();
            for (XzqgqrmxDto xzqgqrmx : xzqgqrmxs) {
                XzqgfkmxDto xzqgfkmxDto = new XzqgfkmxDto();
                xzqgfkmxDto.setFkmxid(StringUtil.generateUUID());
                xzqgfkmxDto.setFkid(xzqgfkglDto.getFkid());
                xzqgfkmxDto.setHwmc(xzqgqrmx.getHwmc());
                xzqgfkmxDto.setHwgg(xzqgqrmx.getHwgg());
                xzqgfkmxDto.setHwjldw(xzqgqrmx.getHwjldw());
                xzqgfkmxDto.setSl(xzqgqrmx.getSl());
                xzqgfkmxDto.setJg(xzqgqrmx.getJg());
                xzqgfkmxDto.setQgid(xzqgqrmx.getQgid());
                xzqgfkmxDto.setQgmxid(xzqgqrmx.getQgmxid());
                xzqgfkmxDto.setLrry(xzqgfkglDto.getLrry());
                xzfkmxDtos.add(xzqgfkmxDto);
            }
            boolean addFkmx=xzqgfkmxService.insertDtoList(xzfkmxDtos);
            if(!addFkmx)
                return false;
        }
        //更新请购是否入库信息
        xzqgqrmxService.modQgmxsfrk(xzqgqrmxs);
        return true;
    }

    /**
     * 自动生成确认单号
     * @return
     */
    @Override
    public String generateConfirmDjh(XzqgqrcglDto xzqgqrcglDto) {
        // TODO Auto-generated method stub
        // 生成规则: QR-2020-1022-01
        String year = DateUtils.getCustomFomratCurrentDate("yyyy");
        String date = DateUtils.getCustomFomratCurrentDate("MMdd");
        String prefix;
        String serial;
        if("MB".equals(xzqgqrcglDto.getDzfsdm())){
            prefix="QR-" + year + "-" + date + "-";
            // 查询流水号
            serial = dao.getDjhSerial(prefix);
        }else{
            prefix="FK-" + year + "-" + date + "-";
            serial = xzqgfkglService.getDjhSerial(prefix);
        }
        return prefix + serial;
    }


     /**
     * 删除行政请购确认单
     * @param xzqgqrglDto
     * @return
     */
    public boolean delConfirmDtos(XzqgqrglDto xzqgqrglDto){
        int result=dao.delete(xzqgqrglDto);
        if(result<=0)
            return false;
        //删除明细
        XzqgqrmxDto xzqgqrmxDto=new XzqgqrmxDto();
        xzqgqrmxDto.setIds(xzqgqrglDto.getIds());
        xzqgqrmxDto.setScry(xzqgqrglDto.getScry());
        boolean delQrmx=xzqgqrmxService.delDtoByQrid(xzqgqrmxDto);
        if(!delQrmx)
            return false;
        //更新请购明细确认标记
        List<XzqgqrmxDto> list=xzqgqrmxService.getDtoList(xzqgqrmxDto);
        if(!CollectionUtils.isEmpty(list)) {
            List<String> qgmxids=new ArrayList<>();
            List<String> qgids=new ArrayList<>();
            for (XzqgqrmxDto dto : list) {
                qgmxids.add(dto.getQgmxid());
                qgids.add(dto.getQgid());
            }
            QgmxDto qgmxDto=new QgmxDto();
            qgmxDto.setIds(qgmxids);
            qgmxDto.setSfqr("0");
            boolean updateQrbj=qgmxService.updateQrbj(qgmxDto);
            if(!updateQrbj)
                return false;
            QgglDto qgglDto=new QgglDto();
            qgglDto.setIds(qgids);
            qgglDto.setSfqr("0");
            return qgglService.updateQrbj(qgglDto);
        }
        return true;
    }

    /**
     * 修改行政请购确认单
     * @param xzqgqrglDto
     * @return
     */
    public boolean modSavePurchaseConfirm(XzqgqrglDto xzqgqrglDto){
        int modQrgl=dao.update(xzqgqrglDto);
        if(modQrgl<=0)
            return false;
        //更新明细
        List<XzqgqrmxDto> xzqgqrmxDtos= JSON.parseArray(xzqgqrglDto.getQgqrmx_json(), XzqgqrmxDto.class);
        XzqgqrmxDto xzqgqrmxDto=new XzqgqrmxDto();
        xzqgqrmxDto.setQrid(xzqgqrglDto.getQrid());
        List<XzqgqrmxDto> xzqgqrmxDtoList=xzqgqrmxService.getDtoList(xzqgqrmxDto);
        List<XzqgqrmxDto> dellist=new ArrayList<>();
        List<XzqgqrmxDto> addlist=new ArrayList<>();
        List<XzqgqrmxDto> updatelist=new ArrayList<>();
        if(!CollectionUtils.isEmpty(xzqgqrmxDtos)) {
            for(XzqgqrmxDto t_xzqgqrmxDto : xzqgqrmxDtos){
                //处理删除的确认明细
                boolean isdel=true;
                if(StringUtils.isNotBlank(t_xzqgqrmxDto.getQrmxid())){
                    if(!CollectionUtils.isEmpty(xzqgqrmxDtoList)) {
                        for(XzqgqrmxDto b_xzqgqrmxDto : xzqgqrmxDtoList){
                            if(b_xzqgqrmxDto.getQrmxid().equals(t_xzqgqrmxDto.getQrmxid())){
                                t_xzqgqrmxDto.setXgry(xzqgqrglDto.getXgry());
                                updatelist.add(t_xzqgqrmxDto);
                                isdel=false;
                            }
                        }
                        if(isdel){
                            t_xzqgqrmxDto.setScry(xzqgqrglDto.getScry());
                            dellist.add(t_xzqgqrmxDto);
                        }
                    }
                }
                //处理新增的确认明细
                if(StringUtils.isBlank(t_xzqgqrmxDto.getQrmxid())){
                    t_xzqgqrmxDto.setQrmxid(StringUtil.generateUUID());
                    t_xzqgqrmxDto.setLrry(xzqgqrglDto.getLrry());
                    t_xzqgqrmxDto.setQrid(xzqgqrglDto.getQrid());
                    addlist.add(t_xzqgqrmxDto);
                }
            }
        }
        if(!CollectionUtils.isEmpty(dellist)){
            xzqgqrmxService.delByList(dellist);
        }
        if(!CollectionUtils.isEmpty(addlist)){
            xzqgqrmxService.insertDtoList(addlist);
        }
        if(!CollectionUtils.isEmpty(updatelist)){
            xzqgqrmxService.updateDtoList(updatelist);
        }
        return true;
    }
	
	 /**
     * 行政请购确认审核列表
     * @param xzqgqrglDto
     * @return
     */
    public List<XzqgqrglDto> getPagedAuditList(XzqgqrglDto xzqgqrglDto){
        //获取人员ID和履历号
        List<XzqgqrglDto> t_sqList = dao.getPagedAuditIdList(xzqgqrglDto);

        if(CollectionUtils.isEmpty(t_sqList))
            return t_sqList;

        List<XzqgqrglDto> sqList = dao.getAuditListByIds(t_sqList);

        commonservice.setSqrxm(sqList);

        return sqList;
    }

    @Override
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
        XzqgqrglDto xzqgqrglDto = (XzqgqrglDto) baseModel;
        xzqgqrglDto.setLrry(operator.getYhid());
        xzqgqrglDto.setXgry(operator.getYhid());
        xzqgqrglDto.setScry(operator.getYhid());
        return modSavePurchaseConfirm(xzqgqrglDto);
    }

    @Override
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        for (ShgcDto shgcDto : shgcList) {
            boolean messageFlag = commonService.queryAuthMessage(operator.getYhid(),shgcDto.getShlb());
            XzqgqrglDto xzqgqrglDto = new XzqgqrglDto();
            xzqgqrglDto.setQrid(shgcDto.getYwid());
            xzqgqrglDto.setXgry(operator.getYhid());
            XzqgqrglDto xzqgqrglDto_t = dao.getDtoById(xzqgqrglDto.getQrid());
            xzqgqrglDto_t.setXgry(operator.getYhid());
            List<SpgwcyDto> spgwcyDtos = shgcDto.getSpgwcyDtos();
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                xzqgqrglDto_t.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos) && messageFlag) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00026"), xxglService.getMsg("ICOMM_SH00001", operator.getZsxm(), shgcDto.getShlbmc(), xzqgqrglDto_t.getQrdh(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                xzqgqrglDto_t.setZt(StatusEnum.CHECK_PASS.getCode());
                XzqgqrmxDto xzqgqrmxDto=new XzqgqrmxDto();
                xzqgqrmxDto.setQrid(xzqgqrglDto.getQrid());
                List<XzqgqrmxDto> list=xzqgqrmxService.getDtoList(xzqgqrmxDto);
                if(!CollectionUtils.isEmpty(list)) {
                    List<String> qgmxids=new ArrayList<>();
                    for (XzqgqrmxDto dto : list) {
                        qgmxids.add(dto.getQgmxid());
                    }
                    QgmxDto qgmxDto=new QgmxDto();
                    qgmxDto.setIds(qgmxids);
                    qgmxDto.setSfqr("1");
                    boolean updateQrbj=qgmxService.updateQrbj(qgmxDto);
                    if(!updateQrbj)
                        return false;
                    //更新确认标记
                    List<XzqgqrmxDto> qrmxlist=xzqgqrmxService.getNeedQrxx(xzqgqrmxDto);
                    if(!CollectionUtils.isEmpty(qrmxlist)) {
                        boolean updateQgglQrbj=xzqgqrmxService.modQgglSfqr(qrmxlist);
                        if(!updateQgglQrbj)
                            return false;
                    }
                }
                if (!CollectionUtils.isEmpty(spgwcyDtos) && messageFlag) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(), spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00006"), xxglService.getMsg("ICOMM_SH00016", operator.getZsxm(), shgcDto.getShlbmc(), xzqgqrglDto_t.getQrdh(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }else {
                xzqgqrglDto_t.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                //发起钉钉审批
                if (!"1".equals(systemreceiveflg)) {
                    if("1".equals(shgcDto.getXlcxh()) && shgcDto.getShxx()==null) {
                        try {
                            // 根据申请部门查询审核流程
                            List<DdxxglDto> ddxxglDtos = ddxxglService.getProcessByJgid(xzqgqrglDto_t.getSqbm(), DdAuditTypeEnum.SP_QG_XQQR_XZ.getCode());
							String spr = null;
							String csr = null;
                            if(!CollectionUtils.isEmpty(ddxxglDtos)) {
                                spr = ddxxglDtos.get(0).getSpr();
                                csr = ddxxglDtos.get(0).getCsr();
                            }
                            if(!StringUtils.isNotBlank(spr))
                                spr="";
                            if(!StringUtils.isNotBlank(csr))
                                csr="";
                            if(StringUtil.isNotBlank(xzqgqrglDto_t.getCsrs())) {
                                String[] split = xzqgqrglDto_t.getCsrs().split(",|，");
                                // 根据抄送人查询钉钉ID
                                List<String> yhids = Arrays.asList(split);
                                List<User> yhxxs = commonservice.getDdidByYhids(yhids);
                                List<String> ddids=new ArrayList<>();
                                if(!CollectionUtils.isEmpty(yhxxs)) {
                                    for(User user : yhxxs) {
                                        if(StringUtils.isBlank(user.getDdid()))
                                            throw new BusinessException("ICOM99019","该用户("+user.getZsxm()+")未获取到钉钉ID！");
                                        ddids.add(user.getDdid());
                                    }
                                }
                                if(!CollectionUtils.isEmpty(ddids)) {
                                    ddids.removeIf(String::isBlank);
                                    csr += "," + String.join(",", ddids);
                                    if(",".equals(csr.substring(0,1))) {
                                        csr=csr.substring(1);
                                    }
                                }
                            }
                            //提交审核至钉钉审批
                            String cc_list = csr; // 抄送人信息
                            String approvers = spr; // 审核人信息
                            Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "行政用品价格审批");//获取审批模板ID
                            String templateCode=(String) template.get("message");
                            //获取申请人信息(合同申请应该为采购部门)
                            User user=new User();
                            user.setYhid(shgcDto.getSqr());
                            user=commonservice.getUserInfoById(user);
                            if(user==null)
                                throw new BusinessException("ICOM99019","未获取到申请人信息！");
                            if(org.apache.commons.lang.StringUtils.isBlank(user.getDdid()))
                                throw new BusinessException("ICOM99019","未获取到申请人钉钉ID！");
                            String userid=user.getDdid();
                            String dept=user.getJgid ();
                            //审批人与申请人做比较，若流程中前面与申请人相同则省略相同的流程
                            String[] t_sprs=spr.split(",");
                            List<String> sprs_list=Arrays.asList(t_sprs);
                            StringBuilder t_str= new StringBuilder();
                            if(!CollectionUtils.isEmpty(sprs_list)) {
                                boolean sflx=true;
                                for (String s : sprs_list) {
                                    if (StringUtils.isNotBlank(userid)) {
                                        if (sflx) {
                                            if (!userid.equals(s)) {
                                                t_str.append(",").append(s);
                                                sflx = false;
                                            }
                                        } else {
                                            t_str.append(",").append(s);
                                        }
                                    }
                                }
                                if(t_str.length()>0)
                                    approvers=t_str.substring(1);
                            }
                            Map<String,String> map=new HashMap<>();
                            XzqgqrmxDto xzqgqrmxDto=new XzqgqrmxDto();
                            xzqgqrmxDto.setQrid(xzqgqrglDto_t.getQrid());
                            List<XzqgqrmxDto> qrmxList=xzqgqrmxService.getDtoList(xzqgqrmxDto);
                            //关联审批单
                            List<String> glspds = new ArrayList<>();
                            if(!CollectionUtils.isEmpty(qrmxList)) {
                                List<String> ids=new ArrayList<>();
                                for(XzqgqrmxDto xzqgqrmxDto_t : qrmxList){
                                    ids.add(xzqgqrmxDto_t.getQgid());
                                }
                                if(!CollectionUtils.isEmpty(ids)) {
                                    QgglDto qgglDto=new QgglDto();
                                    qgglDto.setIds(ids);
                                    List<QgglDto> qggllist=qgglService.getDtoListByIds(qgglDto);
                                    if(!CollectionUtils.isEmpty(qggllist)) {
                                        for (QgglDto dto : qggllist) {
                                            if (StringUtil.isNotBlank(dto.getDdslid())) {
                                                glspds.add(dto.getDdslid());
                                            }
                                        }
                                    }
                                }
                            }
                            map.put("所属公司",systemname);
                            map.put("确认单号",xzqgqrglDto_t.getQrdh());
                            map.put("申请部门",xzqgqrglDto_t.getSqbmmc());
                            map.put("申请人",xzqgqrglDto_t.getSqrmc());
                            map.put("总价（元）",xzqgqrglDto_t.getZje());
                            map.put("关联审批单",glspds.isEmpty() ? "" : JSON.toJSONString(glspds));
                            map.put("供应商是否提供发票","是");
                            map.put("付款方式","/");
                            map.put("收款单位",xzqgqrglDto_t.getZfdx()==null?"/":xzqgqrglDto_t.getZfdx());
                            map.put("收款单位开户行","/");
                            map.put("收款单位开户行账号","/");
                            map.put("说明","详细信息："+applicationurl+urlPrefix+"/ws/production/administration/getPurchaseConfirmMessage?qrid="+xzqgqrglDto_t.getQrid());
                            Map<String,Object> t_map=talkUtil.createInstance(operator.getYhm(), templateCode, approvers, cc_list, userid, dept, map,null,null);
                            log.error("行政请购确认--确认单号："+xzqgqrglDto_t.getQrdh()+",申请部门:"+xzqgqrglDto_t.getSqbmmc()+",总价（元）:"+xzqgqrglDto_t.getZje()+",关联审批单:"+glspds);
                            String str=(String) t_map.get("message");
                            String status=(String) t_map.get("status");
                            if("success".equals(status)) {
                                @SuppressWarnings("unchecked")
                                Map<String,Object> result_map=JSON.parseObject(str,Map.class);
                                if(("0").equals(String.valueOf(result_map.get("errcode")))) {
                                    //保存至钉钉分布式管理表(主站)
                                    RestTemplate t_restTemplate = new RestTemplate();
                                    MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                                    paramMap.add("ddslid", String.valueOf(result_map.get("process_instance_id")));
                                    paramMap.add("fwqm", urlPrefix);
                                    paramMap.add("cljg", "1");
                                    paramMap.add("fwqmc", systemname);
                                    paramMap.add("hddz",applicationurl);
                                    paramMap.add("spywlx",shgcDto.getShlb());
                                  //根据审批类型获取钉钉审批的业务类型，业务名称
                    				JcsjDto jcsjDto_dd = new JcsjDto();
                    				jcsjDto_dd.setCsdm(shgcDto.getAuditType());
                    				jcsjDto_dd.setJclb("DINGTALK_AUDTI_CALLBACK_TYPE");
                    				jcsjDto_dd = jcsjService.getDtoByCsdmAndJclb(jcsjDto_dd);
                    				if(StringUtil.isBlank(jcsjDto_dd.getCsmc()) || StringUtil.isBlank(jcsjDto_dd.getCskz1())) {
                    					throw new BusinessException("msg","请设置"+shgcDto.getShlbmc()+"的钉钉审批回调类型基础数据！");
                    				}
                    				paramMap.add("ywmc", operator.getZsxm()+"提交的"+jcsjDto_dd.getCsmc());
									paramMap.add("ywlx", jcsjDto_dd.getCskz1());
                                    paramMap.add("wbcxid", operator.getWbcxid());
									//分布式保留
									DdfbsglDto ddfbsglDto = new DdfbsglDto();
									ddfbsglDto.setProcessinstanceid(String.valueOf(result_map.get("process_instance_id")));
									ddfbsglDto.setFwqm(urlPrefix);
									ddfbsglDto.setCljg("1");
									ddfbsglDto.setFwqmc(systemname);
									ddfbsglDto.setSpywlx(shgcDto.getShlb());
									ddfbsglDto.setHddz(applicationurl);
									ddfbsglDto.setYwlx(jcsjDto_dd.getCskz1());
									ddfbsglDto.setYwmc(operator.getZsxm()+"提交的"+jcsjDto_dd.getCsmc());
                                    ddfbsglDto.setWbcxid(operator.getWbcxid());
									boolean result_t = ddfbsglService.saveDistributedMsg(ddfbsglDto);
									if(!result_t)
										return false;
                                    //主站保留一份
                                    if(StringUtils.isNotBlank(registerurl)){
                                        boolean result = t_restTemplate.postForObject(registerurl + "/ws/purchase/saveDistributedMsg", paramMap, boolean.class);
                                        if(!result)
                                            return false;
                                    }
                                    //若钉钉审批提交成功，则关联审批实例ID
                                    XzqgqrglDto xzqgqrglDto_a=new XzqgqrglDto();
                                    xzqgqrglDto_a.setDdslid(String.valueOf(result_map.get("process_instance_id")));
                                    xzqgqrglDto_a.setXgry(operator.getYhid());
                                    xzqgqrglDto_a.setQrid(xzqgqrglDto_t.getQrid());
                                    dao.updateDdslid(xzqgqrglDto_a);
                                }else {
                                    throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+t_map.get("message"));
                                }
                            }else {
                                throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+t_map.get("message"));
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                            throw new BusinessException("ICOM99019","出错了!");
                        }
                    }
                }

                //发送钉钉消息--取消审核人员
                if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos()) && messageFlag){
                    int size = shgcDto.getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
                            		shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,xzqgqrglDto_t.getQrdh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(xzqgqrglDto_t);
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
                String qrid = shgcDto.getYwid();
                XzqgqrglDto xzqgqrglDto= new XzqgqrglDto();
                xzqgqrglDto.setXgry(operator.getYhid());
                xzqgqrglDto.setQrid(qrid);
                xzqgqrglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                update(xzqgqrglDto);

            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String qrid = shgcDto.getYwid();
                XzqgqrglDto xzqgqrglDto =new XzqgqrglDto();
                xzqgqrglDto.setXgry(operator.getYhid());
                xzqgqrglDto.setQrid(qrid);
                xzqgqrglDto.setZt(StatusEnum.CHECK_NO.getCode());
                update(xzqgqrglDto);
                //OA取消审批的同时组织钉钉审批
                XzqgqrglDto t_xzqgqrglDto=dao.getDto(xzqgqrglDto);
                if(t_xzqgqrglDto!=null && StringUtils.isNotBlank(t_xzqgqrglDto.getDdslid())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), t_xzqgqrglDto.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map=JSON.parseObject(success,Map.class);
                    boolean bo1= (boolean) result_map.get("success");
                    if(bo1){
                        t_xzqgqrglDto.setDdslid(null);
                        dao.updateDdslid(t_xzqgqrglDto);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
        Map<String, Object> map = new HashMap<>();
        XzqgqrglDto xzqgqrglDto = dao.getDtoById(shgcDto.getYwid());
        map.put("jgid",xzqgqrglDto.getSqbm());
        return map;
    }

    @Override
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
        Map<String, Object> map =new HashMap<>();
        @SuppressWarnings("unchecked")
        List<String> ids = (List<String>)param.get("ywids");
        XzqgqrglDto xzqgqrglDto =new XzqgqrglDto();
        xzqgqrglDto.setIds(ids);
        List<XzqgqrglDto> dtoList = dao.getDtoListByIds(xzqgqrglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(XzqgqrglDto dto:dtoList){
                list.add(dto.getQrid());
            }
        }
        map.put("list",list);
        return map;
    }

    /**
     * 钉钉审批回调
     * @param obj
     * @return
     * @throws BusinessException
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean callbackXzqgqrAduit(JSONObject obj, HttpServletRequest request, Map<String,Object> t_map) throws BusinessException {
        XzqgqrglDto xzqgqrglDto=new XzqgqrglDto();
        String result=obj.getString("result");//正常结束时result为agree，拒绝时result为refuse，审批终止时没这个值，redirect转交
        String type=obj.getString("type");//审批正常结束（同意或拒绝）的type为finish，审批终止的type为terminate
        String processInstanceId=obj.getString("processInstanceId");//审批实例id
        String staffId=obj.getString("staffId");//审批人钉钉ID
        String remark=obj.getString("remark");//审核意见
        String content = obj.getString("content");//评论
        String finishTime=obj.getString("finishTime");//审批完成时间
        String title= obj.getString("title");
        String processCode=obj.getString("processCode");
        String wbcxidString  = obj.getString("wbcxid"); //获取外部程序id
        log.error("回调参数获取---------result:"+result+",type:"+type+",processInstanceId:"+processInstanceId+",staffId:"+staffId+",remark:"+remark+",finishTime"+finishTime+",外部程序id"+wbcxidString);
        //分布式服务器保存钉钉审批信息
        DdfbsglDto ddfbsglDto=new DdfbsglDto();
        ddfbsglDto.setProcessinstanceid(processInstanceId);
        ddfbsglDto=ddfbsglService.getDtoById(processInstanceId);
        DdspglDto ddspglDto=new DdspglDto();
        DdspglDto t_ddspglDto=new DdspglDto();
        t_ddspglDto.setProcessinstanceid(processInstanceId);
        t_ddspglDto.setType("finish");
        t_ddspglDto.setEventtype(DingTalkUtil.BPMS_TASK_CHANGE);
        List<DdspglDto> ddspgllist=ddspglService.getDtoList(t_ddspglDto);
        try {
            if(ddfbsglDto==null)
                throw new BusinessException("message","未获取到相应的钉钉分布式信息！");
            if(StringUtils.isNotBlank(ddfbsglDto.getFwqm())) {                
                if(!CollectionUtils.isEmpty(ddspgllist)) {
                    ddspglDto=ddspgllist.get(0);
                }
            }
            xzqgqrglDto.setDdslid(processInstanceId);
            //根据钉钉审批实例ID查询关联单据
            xzqgqrglDto=dao.getDtoByDdslid(xzqgqrglDto);
            //若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if(xzqgqrglDto!=null) {
                //获取审批人信息
                User user=new User();
                user.setDdid(staffId);
                user.setWbcxid(wbcxidString);
                user = commonservice.getByddwbcxid(user);
                //判断查出得信息是否为空
                if(user==null)
                    return false;
                    
                //获取审批人角色信息
                List<XzqgqrglDto> dd_sprjs=dao.getSprjsBySprid(user.getYhid());
                // 获取当前审核过程
                ShgcDto t_shgcDto = shgcService.getDtoByYwid(xzqgqrglDto.getQrid());
                if(t_shgcDto!=null) {
                    // 获取的审核流程列表
                    ShlcDto shlcParam = new ShlcDto();
                    shlcParam.setShid(t_shgcDto.getShid());
                    shlcParam.setGcid(t_shgcDto.getGcid());// 处理旧流程判断用
                    @SuppressWarnings("unused")
                    List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);

                    if (("start").equals(type)) {
                        //更新分布式管理表信息
                        DdfbsglDto t_ddfbsglDto=new DdfbsglDto();
                        t_ddfbsglDto.setProcessinstanceid(processInstanceId);
                        t_ddfbsglDto.setYwlx(processCode);
                        t_ddfbsglDto.setYwmc(title);
                        ddfbsglService.update(t_ddfbsglDto);
                    }
                    if(!CollectionUtils.isEmpty(dd_sprjs)) {
                        //审批正常结束（同意或拒绝）
                        ShxxDto shxxDto=new ShxxDto();
                        shxxDto.setGcid(t_shgcDto.getGcid());
                        shxxDto.setLcxh(t_shgcDto.getXlcxh());
                        shxxDto.setShlb(t_shgcDto.getShlb());
                        shxxDto.setShyj(remark);
                        shxxDto.setYwid(xzqgqrglDto.getQrid());
                        String lastlcxh=null;//回退到指定流程
                        if (StringUtil.isNotBlank(finishTime)){
                            shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
                        }
                        if(("finish").equals(type)) {
                            //如果审批通过,同意
                            if(("agree").equals(result)) {
                                log.error("同意------");
                                shxxDto.setSftg("1");
                                if(StringUtils.isBlank(t_shgcDto.getXlcxh()))
                                    throw new BusinessException("ICOM99019","现流程序号为空");
                                lastlcxh=String.valueOf(Integer.parseInt(t_shgcDto.getXlcxh())+1);
                            }
                            //如果审批未通过，拒绝
                            else if(("refuse").equals(result)) {
                                log.error("拒绝------");
                                shxxDto.setSftg("0");
                                shxxDto.setLcxh("3");
                                lastlcxh="2";
                                shxxDto.setThlcxh("2");
                            }
                            //如果审批被转发
                            else if(("redirect").equals(result)) {
                                String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(finishTime) / 1000));
                                log.error("DingTalkMaterPurchaseAudit(钉钉物料请购审批转发提醒)------转发人员:"+user.getZsxm()+",人员ID:"+user.getYhid()+",单据号:"+xzqgqrglDto.getQrdh()+",单据ID:"+xzqgqrglDto.getQrid()+",转发时间:"+date);
                                return true;
                            }
                            //调用审核方法
                            Map<String, List<String>> map= ToolUtil.reformRequest(request);
                            log.error("map:"+map);
                            List<String> list=new ArrayList<>();
                            list.add(xzqgqrglDto.getQrid());
                            map.put("qrid", list);
                            //若一个用户多个角色导致审核异常时
                            for(int i=0;i<dd_sprjs.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(dd_sprjs.get(i).getSprjsid());
                                    user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
                                    shxxDto.setYwids(new ArrayList<>());
                                    if(("refuse").equals(result)){
                                        shgcService.DingtalkRecallAudit(shxxDto, user,request,lastlcxh,obj);
                                    }else{
                                        shgcService.doManyBackAudit(shxxDto, user,request,lastlcxh,obj);
                                    }
                                    //更新审批管理信息
                                    ddspglDto.setCljg("1");
                                    ddspglService.updatecljg(ddspglDto);
                                    break;
                                } catch (Exception e) {
                                    log.error("callbackQgglAduit-Exception:" + e.getMessage());
                                    t_map.put("ddspglid", ddspglDto.getDdspglid());
                                    t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());

                                    if(i==dd_sprjs.size()-1)
                                        throw new BusinessException("ICOM99019",e.getMessage());
                                }
                            }
                        }
                        //撤销审批
                        else if(("terminate").equals(type)) {
                            //退回到采购部
                            shxxDto.setThlcxh("2");
                            shxxDto.setYwglmc(xzqgqrglDto.getQrdh());
                            XzqgqrglDto t_xzqgqrDto=new XzqgqrglDto();
                            t_xzqgqrDto.setQrid(xzqgqrglDto.getQrid());
                            t_xzqgqrDto.setDdslid(null);
                            dao.updateDdslid(t_xzqgqrDto);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            //调用审核方法
                            Map<String, List<String>> map=ToolUtil.reformRequest(request);
                            List<String> list=new ArrayList<>();
                            list.add(t_xzqgqrDto.getQrid());
                            map.put("qrid", list);
                            for(int i=0;i<dd_sprjs.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(dd_sprjs.get(i).getSprjsid());
                                    user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
                                    shxxDto.setYwids(new ArrayList<>());
                                    shgcService.terminateAudit(shxxDto, user,request,lastlcxh,obj);
//										shgcService.doAudit(shxxDto, user,request);
                                    //更新审批管理信息
                                    ddspglDto.setCljg("1");
                                    ddspglService.updatecljg(ddspglDto);
                                    break;
                                } catch (Exception e) {
                                    t_map.put("ddspglid", ddspglDto.getDdspglid());
                                    t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());

                                    if(i==dd_sprjs.size()-1)
                                        throw new BusinessException("ICOM99019",e.toString());
                                }
                            }
                        }else if(("comment").equals(type)) {
                            //评论时保存评论信息，添加至审核信息表
                            ShgcDto shgc = shgcService.getDtoByYwid(shxxDto.getYwid());//获得已有审核过程
                            ShxxDto shxx = new ShxxDto();
                            String shxxid =StringUtil.generateUUID();
                            shxx.setShxxid(shxxid);
                            shxx.setSqr(shgc.getSqr());
                            shxx.setLcxh(null);
                            shxx.setShid(shgc.getShid());
                            shxx.setSftg("1");
                            shxx.setGcid(shgc.getGcid());
                            shxx.setYwid(shxxDto.getYwid());
                            shxx.setShyj("评论:"+content.substring(0, Math.min(content.length(), 6000)));//这里写死，备注钉钉审批被提交人员撤销了
                            shxx.setLrry(user.getYhid());
                            shxxService.insert(shxx);
                        }
                    }
                }else {
                    if(("comment").equals(type)) {
                        //评论时保存评论信息，添加至审核信息表
                        ShxxDto shxx = new ShxxDto();
                        String shxxid =StringUtil.generateUUID();
                        shxx.setShxxid(shxxid);
                        shxx.setSftg("1");
                        shxx.setYwid(xzqgqrglDto.getQrid());
                        shxx.setShlb(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
                        List<ShxxDto> shxxlist=shxxService.getShxxOrderByPass(shxx);
                        if(!CollectionUtils.isEmpty(shxxlist)) {
                            shxx.setShid(shxxlist.get(0).getShid());
                            shxx.setShyj("评论:"+content.substring(0, Math.min(content.length(), 6000)));//这里写死，备注钉钉审批被提交人员撤销了
                            shxx.setLrry(user.getYhid());
                            shxxService.insert(shxx);
                        }
                    }
                }
            }
        }catch(BusinessException e) {
            log.error("BusinessException:" + e.getMessage());
            throw new BusinessException("ICOM99019",e.getMsg());
        }catch (Exception e) {
            log.error("Exception:" + e.getMessage());
            throw new BusinessException("ICOM99019",e.toString());
        }
        return true;
    }

    /**
     * 根据qrids获取行政请购确认信息
     * @param xzqgqrglDto
     * @return
     */
    public List<XzqgqrglDto> getDtoListByIds(XzqgqrglDto xzqgqrglDto){
        return dao.getDtoListByIds(xzqgqrglDto);
    }

    /**
     * 确认列表（查询审核状态）
     *
     * @param xzqgqrglDto
     * @return
     */
    @Override
    public List<XzqgqrglDto> getPagedDtoList(XzqgqrglDto xzqgqrglDto) {
        List<XzqgqrglDto> list = dao.getPagedDtoList(xzqgqrglDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_ADMINISTRATIONCONFIRMPURCHASE.getCode(), "zt", "qrid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean finishDto(XzqgqrglDto xzqgqrglDto,User user) {
        //完成，1更新xzgqqrgl的付款完成标记
        dao.updateFkwcbj(xzqgqrglDto);
        //2更新qgmx的fkbj
        XzqgqrmxDto xzqgqrmxDto = new XzqgqrmxDto();
        xzqgqrmxDto.setQrid(xzqgqrglDto.getQrid());
        List<XzqgqrmxDto> xzqgqrxmlist=xzqgqrmxService.getMxlistByQrid(xzqgqrmxDto);
        if(!CollectionUtils.isEmpty(xzqgqrxmlist)) {
            for (XzqgqrmxDto qrmxDto : xzqgqrxmlist) {
                qrmxDto.setXgry(user.getYhid());
            }
            xzqgqrmxService.updateBatchFkbj(xzqgqrxmlist);
        }
        //更新qggl的wcbj
        List<XzqgqrmxDto> qrmxlist=xzqgqrmxService.getNeedXzqgQrxx(xzqgqrmxDto);//取出明细对应的qgid
        if(!CollectionUtils.isEmpty(qrmxlist)) {
            //根据qgid查找每个请购id下的明细数量和明细的fkbj总和是否相等，相等的则更新qggl的完成标记
             List<String> qgids = xzqgqrmxService.getFkwcQgidList(qrmxlist);//找出完成的qgid
             QgglDto qgglDto = new QgglDto();
             qgglDto.setIds(qgids);
             qgglDto.setWcbj("1");
             qgglDto.setXgry(user.getYhid());
             qgglService.updateWcbjs(qgglDto);
        }
        return true;
    }

}

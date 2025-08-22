package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
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
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdfbsglService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.wechat.dao.entities.SjjcxmDto;
import com.matridx.igams.wechat.dao.entities.SjtssqDto;
import com.matridx.igams.wechat.dao.entities.SjtssqModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.post.ISjtssqDao;
import com.matridx.igams.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.igams.wechat.service.svcinterface.ISjtssqService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SjtssqServiceImpl extends BaseBasicServiceImpl<SjtssqDto, SjtssqModel, ISjtssqDao> implements ISjtssqService, IAuditService {

    @Autowired
    ICommonService commonservice;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ISjxxService sjxxService;
    @Value("${matridx.wechat.registerurl:}")
    private String registerurl;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IDdxxglService ddxxglService;
    @Autowired
    IDdspglService ddspglService;
    @Autowired
    IDdfbsglService ddfbsglService;
    @Autowired
    ISjjcxmService sjjcxmService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IShxxService shxxService;
    @Autowired
    DingTalkUtil dingTalkUtil;
    @Autowired(required = false)
    private AmqpTemplate amqpTempl;
    @Autowired
    ICommonService commonService;
    @Autowired
    IJcsjService jcsjService;

    private Logger log = LoggerFactory.getLogger(SjtssqServiceImpl.class);

    /**
     * 申请审核列表
     * @param sjtssqDto
     * @return
     */
    public List<SjtssqDto> getPagedAuditApplication(SjtssqDto sjtssqDto){
        // 获取人员ID和履历号
        List<SjtssqDto> t_sbyzList= dao.getPagedAuditApplication(sjtssqDto);

        if (t_sbyzList == null || t_sbyzList.size() == 0)
            return t_sbyzList;

        List<SjtssqDto> sqList = dao.getAuditListApplication(t_sbyzList);

        commonservice.setSqrxm(sqList);

        return sqList;
    }

    /**
     * 更改处理状态
     * @param sjtssqDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateClbj(SjtssqDto sjtssqDto){
        return dao.updateClbj(sjtssqDto);
    }

    @Override
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        SjtssqDto sjtssqDto=(SjtssqDto)baseModel;
        boolean update = true;
        if(StringUtil.isNotBlank(sjtssqDto.getTssqid())){
           update = update(sjtssqDto);
        }
        return update;
    }

    @Override
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        for (ShgcDto shgcDto : shgcList) {
            SjtssqDto sjtssqDto = new SjtssqDto();
            sjtssqDto.setTssqid(shgcDto.getYwid());
            sjtssqDto.setXgry(operator.getYhid());
            SjtssqDto sjtssqDto_t = getDto(sjtssqDto);
            List<SpgwcyDto> spgwcyDtos = shgcDto.getSpgwcyDtos();
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                sjtssqDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                sjtssqDto.setScbj("1");
                // 发送钉钉消息
                if (spgwcyDtos != null && spgwcyDtos.size() > 0) {
                    for (int i = 0; i < spgwcyDtos.size(); i++) {
                        if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDtos.get(i).getYhid(),spgwcyDtos.get(i).getYhm(),
                            		spgwcyDtos.get(i).getYhid(),
                                    xxglService.getMsg("ICOMM_SH00026"),xxglService.getMsg("ICOMM_SH00001",operator.getZsxm(),shgcDto.getShlbmc() ,sjtssqDto_t.getSqzxmmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                ShxxDto shxxDto = shgcDto.getShxx();
                sjtssqDto.setZt(StatusEnum.CHECK_PASS.getCode());
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sjtssqDto.setTgsj(sdf.format(date));
                SjxxDto sjxxDto=new SjxxDto();
                sjxxDto.setSjid(sjtssqDto_t.getSjid());
                sjxxDto.setSqlx(sjtssqDto_t.getSqxm());
                sjxxDto.setDb(sjtssqDto_t.getDb());
                sjxxDto.setXgry(operator.getYhid());
                if("MD".equals(sjtssqDto_t.getSqzxmdm())){
                    sjxxDto.setSfsf("2");
                }else if("MR".equals(sjtssqDto_t.getSqzxmdm())){
                    sjxxDto.setSfsf("3");
                }else if("MF".equals(sjtssqDto_t.getSqzxmdm()) || "FREE".equals(sjtssqDto_t.getSqxmdm())){
                    sjxxDto.setSfsf("0");
                }else if("VIP".equals(sjtssqDto_t.getSqxmdm())||"PK".equals(sjtssqDto_t.getSqxmdm())){
                    if(StringUtil.isBlank(sjtssqDto_t.getJsrq())){
                        sjxxDto.setSfvip("1");
                    }else{
                        User user=new User();
                        user.setYhid(sjtssqDto_t.getLrry());
                        user=commonService.getUserInfoById(user);
                        if(user!=null){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),user.getYhid(),user.getYhm(), sjtssqDto_t.getDdid(),
                                    xxglService.getMsg("ICOMM_TSSQ00003"),StringUtil.replaceMsg(xxglService.getMsg("ICOMM_TSSQ00004"),
                                            sjtssqDto_t.getSqxmdm(),sjtssqDto_t.getHzxm(),sjtssqDto_t.getYbbh()));
                            shxxDto.setShyj(StringUtil.replaceMsg(xxglService.getMsg("ICOMM_TSSQ00004"),
                                    sjtssqDto_t.getSqxmdm(),sjtssqDto_t.getHzxm(),sjtssqDto_t.getYbbh()));
                        }
                    }
                }
                SjjcxmDto sjjcxmDto=new SjjcxmDto();
                sjjcxmDto.setSjid(sjxxDto.getSjid());
                List<SjjcxmDto> dtoList = sjjcxmService.getDtoList(sjjcxmDto);
                if (StringUtil.isNotBlank(sjxxDto.getSfsf())){
                    for(SjjcxmDto dto:dtoList){
                        if(dto.getJcxmid().equals(sjtssqDto_t.getJcxmid())){
                            if(StringUtil.isNotBlank(dto.getJczxmid())){
                                if(dto.getJczxmid().equals(sjtssqDto_t.getJczxmid())){
                                    dto.setSfsf(sjxxDto.getSfsf());
                                    break;
                                }
                            }else{
                                dto.setSfsf(sjxxDto.getSfsf());
                                break;
                            }
                        }
                    }
                    SjtssqDto t_sjtssqDto=new SjtssqDto();
                    t_sjtssqDto.setSjid(sjxxDto.getSjid());
                    t_sjtssqDto.setZt("80");
                    List<SjtssqDto> sjtssqDtos = dao.getDtoList(t_sjtssqDto);
                    sjtssqDtos.add(sjtssqDto_t);
                    sjjcxmService.readjustPayment(sjxxDto,dtoList,sjtssqDtos);
                    String sfsf_str="";
                    for(SjjcxmDto dto:dtoList) {
                        if (StringUtil.isNotBlank(dto.getSfsf())) {
                            sfsf_str=dto.getSfsf();
                            break;
                        }
                    }
                    if(StringUtil.isNotBlank(sfsf_str)){
                        BigDecimal sfsf=new BigDecimal(sfsf_str);
                        for(SjjcxmDto dto:dtoList) {
                            if (StringUtil.isNotBlank(dto.getSfsf())) {
                                BigDecimal bigDecimal=new BigDecimal(dto.getSfsf());
                                if(sfsf.compareTo(bigDecimal)!=1){
                                    sfsf=bigDecimal;
                                }
                            }
                        }
                        sjxxDto.setSfsf(sfsf.toString());
                    }else{
                        sjxxDto.setSfsf("1");
                    }
                    sjjcxmService.updateSjjcxmDtos(dtoList);
                }
                sjxxDto.setSjjcxms(dtoList);
                boolean update = sjxxService.updateTssq(sjxxDto);
                RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.TSSQ_MOD.getCode() + JSONObject.toJSONString(sjxxDto));
                if(!update){
                    log.error("更新送检信息表失败！");
                }

                if("FREE".equals(sjtssqDto_t.getSqxmdm())){
                    List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.SPECIALAPPLY_TYPE.getCode());
                    if (ddxxglDtolist != null && ddxxglDtolist.size() > 0) {
                        for (int i = 0; i < ddxxglDtolist.size(); i++) {
                            if (StringUtil.isNotBlank(ddxxglDtolist.get(i).getDdid())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),ddxxglDtolist.get(i).getYhid(),ddxxglDtolist.get(i).getYhm(), ddxxglDtolist.get(i).getDdid(),
                                        xxglService.getMsg("ICOMM_SJ00064"),xxglService.getMsg("ICOMM_SJ00065",
                                                sjtssqDto_t.getYbbh(),sjtssqDto_t.getHzxm()));
                            }
                        }
                    }
                }
                if (spgwcyDtos != null && spgwcyDtos.size() > 0) {
                    int size = spgwcyDtos.size();
                    for (int i = 0; i < size; i++) {
                        if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDtos.get(i).getYhid(),spgwcyDtos.get(i).getYhm(),
                            		spgwcyDtos.get(i).getYhid(),
                                    xxglService.getMsg("ICOMM_SH00006"),xxglService.getMsg("ICOMM_SH00016",
                                            operator.getZsxm(),shgcDto.getShlbmc() ,sjtssqDto_t.getSqzxmmc(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }else {
                sjtssqDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                if("1".equals(shgcDto.getXlcxh()) && shgcDto.getShxx()==null) {//提交的时候发起钉钉审批
                    try {
                        Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "销售标本管理审批");//获取审批模板ID
                        String templateCode=(String) template.get("message");
                        //获取申请人信息
                        User user=new User();
                        user.setYhid(shgcDto.getSqr());
                        user=commonservice.getUserInfoById(user);
                        if(user==null)
                            throw new BusinessException("ICOM99019","未获取到申请人信息！");
                        if(StringUtils.isBlank(user.getDdid()))
                            throw new BusinessException("ICOM99019","未获取到申请人钉钉ID！");
                        String userid=user.getDdid();
                        String dept=user.getJgid ();
                        String sqyy="";
                        if(StringUtils.isNotBlank(sjtssqDto_t.getSqyymc()))
                            sqyy=sjtssqDto_t.getSqyymc()+(sjtssqDto_t.getQtsqyy()!=null?":"+sjtssqDto_t.getQtsqyy() : "");
                        Map<String,String> map= new HashMap<>();
                        map.put("患者姓名", sjtssqDto_t.getHzxm()!=null?sjtssqDto_t.getHzxm():"");
                        map.put("标本编号", sjtssqDto_t.getYbbh()!=null?sjtssqDto_t.getYbbh():"");
                        map.put("医院名称", sjtssqDto_t.getDwjc()!=null?sjtssqDto_t.getDwjc():"");
                        map.put("标本类型", sjtssqDto_t.getYblxmc()!=null?sjtssqDto_t.getYblxmc():"");
                        map.put("检测项目", sjtssqDto_t.getJcxmmc()!=null?sjtssqDto_t.getJcxmmc():"");
                        map.put("合作伙伴", sjtssqDto_t.getDb()!=null?sjtssqDto_t.getDb():"");
                        map.put("申请项目", sjtssqDto_t.getSqxmmc());
                        map.put("申请子项目", sjtssqDto_t.getSqzxmmc()!=null?sjtssqDto_t.getSqzxmmc():"");
                        map.put("申请原因", sqyy);
                        map.put("备注", sjtssqDto_t.getBz()!=null?sjtssqDto_t.getBz():"");
                        log.error("钉钉审批信息---患者姓名:"+sjtssqDto_t.getHzxm()+",标本编号:"+sjtssqDto_t.getYbbh()+",医院名称:"+sjtssqDto_t.getDwjc()+",标本类型:"+sjtssqDto_t.getYblxmc()+",检测项目:"+sjtssqDto_t.getJcxmmc()+",合作伙伴:"+sjtssqDto_t.getDb()+",申请项目:"+sjtssqDto_t.getSqxmmc()+",申请子项目:"+sjtssqDto_t.getSqzxmmc()+",申请原因:"+sjtssqDto_t.getSqyymc());
                        log.error("申请人钉钉ID:"+userid+"部门ID:"+dept);
                        Map<String,Object> t_map=talkUtil.createInstance(operator.getYhm(), templateCode, null, null, userid, dept, map,null,null);
                        String str=(String) t_map.get("message");
                        String status=(String) t_map.get("status");
                        if("success".equals(status)) {
                            @SuppressWarnings("unchecked")
                            Map<String,Object> result_map= JSON.parseObject(str,Map.class);
                            if(("0").equals(String.valueOf(result_map.get("errcode")))) {
                                //保存至钉钉分布式管理表(主站)
                                RestTemplate t_restTemplate = new RestTemplate();
                                MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                                paramMap.add("ddslid", String.valueOf(result_map.get("process_instance_id")));
                                paramMap.add("fwqm", urlPrefix);
                                paramMap.add("cljg", "1");
                                paramMap.add("fwqmc", "杰毅医检");
                                paramMap.add("spywlx", shgcDto.getShlb());
                                paramMap.add("hddz",applicationurl);
                                JcsjDto jcsjDto_dd = new JcsjDto();
                                jcsjDto_dd.setCsdm(shgcDto.getAuditType());
                                jcsjDto_dd.setJclb("DINGTALK_AUDTI_CALLBACK_TYPE");
                                jcsjDto_dd = jcsjService.getDtoByCsdmAndJclb(jcsjDto_dd);
                                if(StringUtil.isBlank(jcsjDto_dd.getCsmc()) || StringUtil.isBlank(jcsjDto_dd.getCskz1())) {
                                    throw new BusinessException("msg","请设置"+shgcDto.getShlbmc()+"的钉钉审批回调类型基础数据！");
                                }
                                paramMap.add("ywmc", operator.getZsxm()+"提交的"+jcsjDto_dd.getCsmc());
                                paramMap.add("ywlx", templateCode);//这里存放的就是模板ID
                                paramMap.add("wbcxid", operator.getWbcxid());
//                                //分布式保留一份
                                boolean t_result = t_restTemplate.postForObject( applicationurl+"/ws/purchase/saveDistributedMsg", paramMap, boolean.class);
                                if(!t_result)
                                    return false;
                                //主站保留一份
                                if(StringUtils.isNotBlank(registerurl)){
                                    boolean result = t_restTemplate.postForObject(registerurl + "/ws/purchase/saveDistributedMsg", paramMap, boolean.class);
                                    if(!result)
                                        return false;
                                }
                                //若钉钉审批提交成功，则关联审批实例ID
                                SjtssqDto sjtssqDto_b=new SjtssqDto();
                                sjtssqDto_b.setDdslid(String.valueOf(result_map.get("process_instance_id")));
                                sjtssqDto_b.setXgry(operator.getYhid());
                                sjtssqDto_b.setTssqid(sjtssqDto_t.getTssqid());
                                dao.updateDdslid(sjtssqDto_b);
                            }else {
                                throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+t_map.get("message"));
                            }
                        }else {
                            throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+t_map.get("message"));
                        }
                    } catch (Exception e) {
                        throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+e.getMessage());
                    }
                }

                //发送钉钉消息--取消审核人员
                if(shgcDto.getNo_spgwcyDtos() != null && shgcDto.getNo_spgwcyDtos().size() > 0 ){
                    int size = shgcDto.getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
                            		shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,sjtssqDto_t.getSqzxmmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(sjtssqDto);
        }
        return true;
    }

    @Override
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        if (auditParam.isCancelOpe()) {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                String tssqid = shgcDto.getYwid();
                SjtssqDto sjtssqDto = new SjtssqDto();
                sjtssqDto.setXgry(operator.getYhid());
                sjtssqDto.setTssqid(tssqid);
                sjtssqDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(sjtssqDto);
            }
        } else {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                String tssqid = shgcDto.getYwid();
                SjtssqDto sjtssqDto = new SjtssqDto();
                sjtssqDto.setXgry(operator.getYhid());
                sjtssqDto.setTssqid(tssqid);
                sjtssqDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(sjtssqDto);
                //OA取消审批的同时组织钉钉审批
                SjtssqDto t_sjtssqDto=dao.getDto(sjtssqDto);
                if(t_sjtssqDto!=null && StringUtils.isNotBlank(t_sjtssqDto.getDdslid())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), t_sjtssqDto.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map=JSON.parseObject(success,Map.class);
                    Boolean bo1= (boolean) result_map.get("success");
                    if(bo1)
                        dao.updateDdslidToNull(t_sjtssqDto);
                }
            }
        }
        return true;
    }

    @Override
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) throws BusinessException {
        return null;
    }

    @Override
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) throws BusinessException {
        Map<String, Object> map =new HashMap<>();
        List<String> ids = (List<String>)param.get("ywids");
        SjtssqDto sjtssqDto = new SjtssqDto();
        sjtssqDto.setIds(ids);
        List<SjtssqDto> dtoList = dao.getDtoList(sjtssqDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(SjtssqDto dto:dtoList){
                list.add(dto.getTssqid());
            }
        }
        map.put("list",list);
        return map;
    }

    /**
     * 驳回保存
     * @param sjtssqDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean rejectSaveApplication(SjtssqDto sjtssqDto, User user){
        //撤回钉钉审批
        SjtssqDto dto = getDto(sjtssqDto);
        if(StringUtil.isNotBlank(dto.getDdslid())){
            Map<String, Object> map = dingTalkUtil.cancelDingtalkAudit(user.getYhm(), dto.getDdslid(), sjtssqDto.getBhly(), user.getDdid());
            //若撤回成功将实例ID至为空
            String success=String.valueOf(map.get("message"));
            Map<String,Object> result_map=JSON.parseObject(success,Map.class);
            Boolean bo1= (boolean) result_map.get("success");
            String errcode = String.valueOf(result_map.get("errcode"));
            if("820008".equals(errcode)){
                User user_t=new User();
                user_t.setYhid(sjtssqDto.getLrry());
                user_t=commonService.getUserInfoById(user_t);
                if(user_t!=null){
                    talkUtil.sendWorkMessage(user_t.getYhm(), sjtssqDto.getDdid(),
                            xxglService.getMsg("ICOMM_TSSQ00001"),xxglService.getMsg("ICOMM_TSSQ00002",sjtssqDto.getZsxm(),sjtssqDto.getHzxm(),sjtssqDto.getYbbh(),sjtssqDto.getYblxmc(),sjtssqDto.getSqxmmc(),sjtssqDto.getSqzxmmc(),sjtssqDto.getBhly(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                }
            }
            if(bo1){
                sjtssqDto.setDdslid(null);
                dao.updateDdslid(sjtssqDto);
            }
        }
        sjtssqDto.setClbj("2");
        return update(sjtssqDto);
    }
    /**
     * 获取申请项目字符串
     * @param sjtssqDto
     * @return
     */
    public String getSqxms(SjtssqDto sjtssqDto){
        return dao.getSqxms(sjtssqDto);
    }

    /**
     * 验证项目重复
     * @param sjtssqDto
     * @return
     */
    public SjtssqDto verification(SjtssqDto sjtssqDto){
        return dao.verification(sjtssqDto);
    }

    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean callbackTssqAduit(JSONObject obj, HttpServletRequest request, Map<String,Object> t_map) throws BusinessException{
        String result=obj.getString("result");//正常结束时result为agree，拒绝时result为refuse，审批终止时没这个值，redirect转交
        String type=obj.getString("type");//审批正常结束（同意或拒绝）的type为finish，审批终止的type为terminate
        String processInstanceId=obj.getString("processInstanceId");//审批实例id
        String staffId=obj.getString("staffId");//审批人钉钉ID
        String remark=obj.getString("remark");//审核意见
        String content = obj.getString("content");//评论
        String finishTime=obj.getString("finishTime");//审批完成时间
        String title= obj.getString("title");
        String processCode=obj.getString("processCode");
        String ddspbcbj=request.getParameter("ddspbcbj");
        String wbcxid=obj.getString("wbcxid");
        log.error("回调参数获取---------result:"+result+",type:"+type+",processInstanceId:"+processInstanceId+",staffId:"+staffId+",remark:"+remark+",finishTime"+finishTime);
        //分布式服务器保存钉钉审批信息
        DdfbsglDto ddfbsglDto=new DdfbsglDto();
        ddfbsglDto.setProcessinstanceid(processInstanceId);
        ddfbsglDto=ddfbsglService.getDtoById(processInstanceId);
        DdspglDto ddspglDto=new DdspglDto();
        try {
            if(ddfbsglDto==null)
                throw new BusinessException("message","未获取到相应的钉钉分布式信息！");
            if(StringUtils.isNotBlank(ddfbsglDto.getFwqm())) {
                if("1".equals(ddspbcbj)) {
                    ddspglDto=ddspglService.insertInfo(obj);
                }else {
					//考虑190调190时，因为接收回调接口中已经保存一份消息数据，则这里直接取最新的那条，若没有则添加这条传递过来的消息
                    DdspglDto t_ddspglDto=new DdspglDto();
                    t_ddspglDto.setProcessinstanceid(processInstanceId);
                    t_ddspglDto.setType("finish");
                    t_ddspglDto.setEventtype(DingTalkUtil.BPMS_TASK_CHANGE);
                    List<DdspglDto> ddspgllist=ddspglService.getDtoList(t_ddspglDto);
                    
                    if(ddspgllist!=null && ddspgllist.size()>0) {
                        ddspglDto=ddspgllist.get(0);
                    }else{
                        ddspglDto=ddspglService.insertInfo(obj);
                    }
                }
            }
            SjtssqDto sjtssqDto=new SjtssqDto();
            sjtssqDto.setDdslid(processInstanceId);
            //根据钉钉审批实例ID查询关联请购单
            sjtssqDto=dao.getDtoByDdslid(sjtssqDto);
            //若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if(sjtssqDto!=null) {
                User t_user=new User();
                t_user.setDdid(staffId);
                t_user.setWbcxid(wbcxid);
                //获取审批人信息
                t_user=commonservice.getByddwbcxid(t_user);
                if(t_user==null)
                    return false;
                User user=new User();
                user.setYhid(t_user.getYhid());
                user.setZsxm(t_user.getZsxm());
                user.setYhm(t_user.getYhm());
                user.setDdid(t_user.getDdid());
                //获取审批人角色信息
                List<SjtssqDto> dd_sprjs=dao.getSprjsBySprid(t_user.getYhid());
                // 获取当前审核过程
                ShgcDto t_shgcDto = shgcService.getDtoByYwid(sjtssqDto.getTssqid());
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
                    if(dd_sprjs!=null && dd_sprjs.size()>0) {
                        //审批正常结束（同意或拒绝）
                        ShxxDto shxxDto=new ShxxDto();
                        shxxDto.setLcxh(t_shgcDto.getXlcxh());
                        shxxDto.setShlb(t_shgcDto.getShlb());
                        shxxDto.setShyj(remark);
                        shxxDto.setYwid(sjtssqDto.getTssqid());
                        shxxDto.setGcid(t_shgcDto.getGcid());
                        if (StringUtil.isNotBlank(finishTime)){
                            shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
                        }
                        String lastlcxh=null;//回退到指定流程

                        if(("finish").equals(type)) {
                            //如果审批通过,同意
                            if(("agree").equals(result)) {
                                log.error("同意------");
                                shxxDto.setSftg("1");
                                if(org.apache.commons.lang3.StringUtils.isBlank(t_shgcDto.getXlcxh()))
                                    throw new BusinessException("ICOM99019","现流程序号为空");
                                lastlcxh=String.valueOf(Integer.parseInt(t_shgcDto.getXlcxh())+1);
                            }
                            //如果审批未通过，拒绝
                            else if(("refuse").equals(result)) {
                                log.error("拒绝------");
                                shxxDto.setSftg("0");
                                shxxDto.setThlcxh(null);
                            }
                            //如果审批被转发
                            else if(("redirect").equals(result)) {
                                String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(finishTime) / 1000));
                                log.error("DingTalkMaterPurchaseAudit(钉钉物料请购审批转发提醒)------转发人员:"+t_user.getZsxm()+",人员ID:"+t_user.getYhid()+",标本编号:"+sjtssqDto.getYbbh()+",单据ID:"+sjtssqDto.getTssqid()+",转发时间:"+date);
                                return true;
                            }
                            //调用审核方法
                            Map<String, List<String>> map= ToolUtil.reformRequest(request);
                            log.error("map:"+map);
                            List<String> list= new ArrayList<>();
                            list.add(sjtssqDto.getTssqid());
                            map.put("tssqid", list);
                            //若一个用户多个角色导致审核异常时
                            for(int i=0;i<dd_sprjs.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(dd_sprjs.get(i).getSprjsid());
                                    user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
                                    shxxDto.setYwids(new ArrayList<>());
                                    if(("refuse").equals(result)){
                                        shgcService.terminateAudit(shxxDto, user,request,lastlcxh,obj);
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
                            shxxDto.setThlcxh(null);
                            shxxDto.setYwglmc(sjtssqDto.getYbbh());
                            SjtssqDto t_sjtssqDto = new SjtssqDto();
                            t_sjtssqDto.setTssqid(sjtssqDto.getTssqid());
                            dao.updateDdslidToNull(t_sjtssqDto);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            //调用审核方法
                            Map<String, List<String>> map=ToolUtil.reformRequest(request);
                            List<String> list= new ArrayList<>();
                            list.add(sjtssqDto.getTssqid());
                            map.put("tssqid", list);
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
                        shxx.setYwid(sjtssqDto.getTssqid());
                        if("FREE".equals(sjtssqDto.getSqxmdm())) {
                            shxx.setShlb(AuditTypeEnum.AUDIT_FREESAMPLES.getCode());
                        }else if("VIP".equals(sjtssqDto.getSqxmdm())) {
                            shxx.setShlb(AuditTypeEnum.AUDIT_VIPSAMPLES.getCode());
                        }else if("PK".equals(sjtssqDto.getSqxmdm())) {
                            shxx.setShlb(AuditTypeEnum.AUDIT_PKSAMPLES.getCode());
                        }
                        List<ShxxDto> shxxlist=shxxService.getShxxOrderByPass(shxx);
                        if(shxxlist!=null && shxxlist.size()>0) {
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
        }finally {
            if(ddfbsglDto!=null) {
                if(org.apache.commons.lang3.StringUtils.isNotBlank(ddfbsglDto.getFwqm())) {
                    if("1".equals(ddspbcbj)) {
                        t_map.put("sfbcspgl", "1");//是否返回上一层新增钉钉审批管理信息
                    }
                }
            }
        }
        return true;
    }


    /**
     * 修改
     * @param sjtssqDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateDto(SjtssqDto sjtssqDto){
        int update = dao.update(sjtssqDto);
        return update != 0;
    }

    /**
     * 删除
     * @param sjtssqDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean deleteDto(SjtssqDto sjtssqDto){
        int delete = dao.delete(sjtssqDto);
        return delete != 0;
    }

    /**
     * 新增
     * @param sjtssqDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Map<String,Object> insertSjtssq(SjtssqDto sjtssqDto){
        Map<String,Object> map= new HashMap<>();
        SjtssqDto dto = dao.getDto(sjtssqDto);
        String sqxms = getSqxms(sjtssqDto);
        if(dto!=null){
            if("00".equals(dto.getZt())){
                delete(dto);
            }else{
                map.put("status","fail");
                map.put("message","同一个人不可以多次申请同一个项目!");
                return map;
            }
        }
        sqxms=sqxms+","+sjtssqDto.getSqxmdm();
        if(sqxms.indexOf("VIP")!=-1&&sqxms.indexOf("PK")!=-1){
            map.put("status","fail");
            map.put("message","您之前申请过PK和VIP其一，所以只能继续申请同一个申请项目!");
        }else{
            boolean isSuccess=false;
            sjtssqDto.setTssqid(StringUtil.generateUUID());
            sjtssqDto.setZt(StatusEnum.CHECK_NO.getCode());
            sjtssqDto.setClbj("0");
            int insert = dao.insert(sjtssqDto);
            if(insert>0){
                isSuccess=true;
            }
            map.put("status",isSuccess?"success":"fail");
            map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
            map.put("ywid",sjtssqDto.getTssqid());
            SjtssqDto dtoById = dao.getDto(sjtssqDto);
            if(dtoById!=null){
                if("FREE".equals(dtoById.getSqxmdm())) {
                    map.put("auditType", AuditTypeEnum.AUDIT_FREESAMPLES.getCode());
                }else if("VIP".equals(dtoById.getSqxmdm())) {
                    map.put("auditType", AuditTypeEnum.AUDIT_VIPSAMPLES.getCode());
                }else if("PK".equals(dtoById.getSqxmdm())) {
                    map.put("auditType", AuditTypeEnum.AUDIT_PKSAMPLES.getCode());
                }
            }
        }
        return map;
    }

    /**
     * 导出
     * @param sjtssqDto
     * @return
     */
    @Override
    public int getCountForSearchExp(SjtssqDto sjtssqDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(sjtssqDto);
    }
    /**
     * 根据搜索条件获取导出信息
     * @param sjtssqDto
     * @return
     */
    public List<SjtssqDto> getListForSearchExp(Map<String,Object> params){
        SjtssqDto sjtssqDto = (SjtssqDto)params.get("entryData");
        queryJoinFlagExport(params,sjtssqDto);
        return dao.getListForSearchExp(sjtssqDto);
    }

    /**
     * 根据选择信息获取导出信息
     * @param sjtssqDto
     * @return
     */
    public List<SjtssqDto> getListForSelectExp(Map<String,Object> params){
        SjtssqDto ngsglDto = (SjtssqDto)params.get("entryData");
        queryJoinFlagExport(params,ngsglDto);
        return dao.getListForSelectExp(ngsglDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String,Object> params,SjtssqDto sjtssqDto){
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
        sjtssqDto.setSqlParam(sqlcs);
    }
}

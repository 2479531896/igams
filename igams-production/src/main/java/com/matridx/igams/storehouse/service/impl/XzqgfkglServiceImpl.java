package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.dao.entities.external.ConsumeAmount;
import com.matridx.igams.common.dao.entities.external.Expense;
import com.matridx.igams.common.dao.entities.external.ExpenseRequestDto;
import com.matridx.igams.common.dao.entities.external.PayeeAccount;
import com.matridx.igams.common.dao.entities.external.ReimburseDto;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
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
import com.matridx.igams.common.util.ExternalUtil;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.service.svcinterface.IQgglService;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.storehouse.dao.entities.*;
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
import com.matridx.igams.storehouse.dao.post.IXzqgfkglDao;
import com.matridx.igams.storehouse.dao.post.IXzqgqrglDao;
import com.matridx.igams.storehouse.service.svcinterface.IXzqgfkglService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class XzqgfkglServiceImpl extends BaseBasicServiceImpl<XzqgfkglDto, XzqgfkglModel, IXzqgfkglDao> implements IXzqgfkglService, IAuditService {
    @Autowired
    IXzqgfkmxService xzqgfkmxService;
    //@Autowired(required = false)
    //private AmqpTemplate amqpTempl;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IShxxService shxxService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ICommonService commonService;
    @Autowired
    IDdxxglService ddxxglService;
    @Autowired
    IXzqgqrglDao xzqgqrglDao;
    @Autowired
    IQgglService qgglService;
    @Autowired
    IDdfbsglService ddfbsglService;
    @Autowired
    IDdspglService ddspglService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IXzqgqrmxService xzqgqrmxService;
    @Autowired
    IQgmxService qgmxService;
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
    @Value("${matridx.systemflg.servicecode:}")
    private String servicecode;
    @Value("${matridx.mk.mkflag:}")
    private String mkflag;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    ExternalUtil externalUtil;
    private final Logger log = LoggerFactory.getLogger(XzqgfkglServiceImpl.class);

    /**
     * 获取最大单号
     * @param prefix
     * @return
     */
    public String getDjhSerial(String prefix){
        return dao.getDjhSerial(prefix);
    }

    /**
     * 新增时 处理月结数据
     * @param xzqgfkglDto
     * @return
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean dealMonthData(XzqgfkglDto xzqgfkglDto) throws BusinessException {
        List<XzqgfkmxDto> xzqgfkmxlist= JSON.parseArray(xzqgfkglDto.getFkmxJson(), XzqgfkmxDto.class);
        if (CollectionUtils.isEmpty(xzqgfkmxlist)){
            throw new BusinessException("msg","明细不能为空");
        }
        boolean addXzqgfkgl=insertDto(xzqgfkglDto);
        if(!addXzqgfkgl)
            return false;
        if(!CollectionUtils.isEmpty(xzqgfkmxlist)){
            for (XzqgfkmxDto xzqgfkmxDto : xzqgfkmxlist) {
                xzqgfkmxDto.setFkmxid(StringUtil.generateUUID());
                xzqgfkmxDto.setFkid(xzqgfkglDto.getFkid());
                xzqgfkmxDto.setLrry(xzqgfkglDto.getLrry());
            }
            return xzqgfkmxService.insertDtoList(xzqgfkmxlist);
        }
        return true;
    }

    /**
     * 新增时 处理公对公数据
     * @param xzqgfkglDto
     * @return
     */
    @Override
    public boolean dealPtpData(XzqgfkglDto xzqgfkglDto) {
        boolean addXzqgfkgl=insertDto(xzqgfkglDto);
        if(!addXzqgfkgl)
            return false;
        List<XzqgfkmxDto> xzqgfkmxlist = JSON.parseArray(xzqgfkglDto.getFkmxJson(), XzqgfkmxDto.class);
        if(!CollectionUtils.isEmpty(xzqgfkmxlist)) {
            List<XzqgfkmxDto> xzfkmxDtos=new ArrayList<>();
            for (XzqgfkmxDto dto : xzqgfkmxlist) {
                XzqgfkmxDto xzqgfkmxDto = new XzqgfkmxDto();
                xzqgfkmxDto.setFkmxid(StringUtil.generateUUID());
                xzqgfkmxDto.setFkid(xzqgfkglDto.getFkid());
                xzqgfkmxDto.setHwmc(dto.getHwmc());
                xzqgfkmxDto.setHwgg(dto.getHwgg());
                xzqgfkmxDto.setHwjldw(dto.getHwjldw());
                xzqgfkmxDto.setSl(dto.getSl());
                xzqgfkmxDto.setJg(dto.getJg());
                xzqgfkmxDto.setQgid(dto.getQgid());
                xzqgfkmxDto.setQgmxid(dto.getQgmxid());
                xzqgfkmxDto.setLrry(xzqgfkglDto.getLrry());
                xzfkmxDtos.add(xzqgfkmxDto);
            }
            return xzqgfkmxService.insertDtoList(xzfkmxDtos);
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertDto(XzqgfkglDto xzqgfkglDto){
       if(StringUtils.isBlank(xzqgfkglDto.getFkid())){
           xzqgfkglDto.setFkid(StringUtil.generateUUID());
       }
       xzqgfkglDto.setZt(StatusEnum.CHECK_NO.getCode());
       int result=dao.insert(xzqgfkglDto);
        return result > 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
        XzqgfkglDto xzqgfkglDto = (XzqgfkglDto)baseModel;
        xzqgfkglDto.setXgry(operator.getYhid());
        List<XzqgfkmxDto> xzqgfkmxlist= JSON.parseArray(xzqgfkglDto.getFkmx_json(), XzqgfkmxDto.class);
        dao.update(xzqgfkglDto);
        if(!CollectionUtils.isEmpty(xzqgfkmxlist)) {
            xzqgfkmxService.updateBatch(xzqgfkmxlist);
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        try{
              for(ShgcDto shgcDto : shgcList){
                  boolean messageFlag = commonService.queryAuthMessage(operator.getYhid(),shgcDto.getShlb());
                  XzqgfkglDto xzqgfkglDto_t = dao.getDtoById(shgcDto.getYwid());
                  xzqgfkglDto_t.setXgry(operator.getYhid());
                  //审核退回
                  if(AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())){
                      xzqgfkglDto_t.setZt(StatusEnum.CHECK_UNPASS.getCode());
                  //审核通过
                  }else if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())){
                      XzqgfkmxDto xzqgfkmxDto=new XzqgfkmxDto();
                      xzqgfkmxDto.setFkid(xzqgfkglDto_t.getFkid());
                      List<XzqgfkmxDto> xzqgfkmxList = xzqgfkmxService.getListByFkid(xzqgfkglDto_t.getFkid());
                      if(!CollectionUtils.isEmpty(xzqgfkmxList)) {
                          for(XzqgfkmxDto t_xzqgfkmxDto : xzqgfkmxList){
                              t_xzqgfkmxDto.setXgry(operator.getYhid());
                          }
                          xzqgfkmxService.updateBatchFkbj(xzqgfkmxList);
                      }
                      //更新付款标记
                      List<XzqgfkmxDto> fkmxlist=xzqgfkmxService.getDtoList(xzqgfkmxDto);
                      List<String> qrids=new ArrayList<>();
                      List<String> qgids=new ArrayList<>();
                      if(!CollectionUtils.isEmpty(fkmxlist)) {
                          for(XzqgfkmxDto t_xzqgfkmxDto : fkmxlist){
                        	  if(StringUtil.isNotBlank(t_xzqgfkmxDto.getQrid())) {
                        		  qrids.add(t_xzqgfkmxDto.getQrid());
                        	  }
                        	  if(StringUtil.isNotBlank(t_xzqgfkmxDto.getQgid())){
                                  qgids.add(t_xzqgfkmxDto.getQgid());
                              }
                          }
                          XzqgfkmxDto xzqgfkmxDto_t=new XzqgfkmxDto();
                          xzqgfkmxDto_t.setIds(qgids);
                          List<XzqgfkmxDto> list=xzqgfkmxService.getNeedFkxx(xzqgfkmxDto_t);
                          if(!CollectionUtils.isEmpty(list)) {
                              xzqgfkmxService.modQgglSffk(list);//更新相关请购单付款标记
                          }
                      }
                      XzqgqrmxDto xzqgqrmxDto=new XzqgqrmxDto();                   
                      //更新相关确认单是否付款完成
                      if(!CollectionUtils.isEmpty(qrids)){
                    	  xzqgqrmxDto.setIds(qrids);
                          List<XzqgqrmxDto> fkwcList=xzqgqrmxService.getFkwcDtoList(xzqgqrmxDto);
                          if(!CollectionUtils.isEmpty(fkwcList)) {
                              List<String> t_qrids=new ArrayList<>();
                              for(XzqgqrmxDto t_xzqgqrmxDto : fkwcList){
                                  if("0".equals(t_xzqgqrmxDto.getWfsl()))//若未付款数量为0则更新该确认单付款标记
                                       t_qrids.add(t_xzqgqrmxDto.getQrid());
                              }
                              if(!CollectionUtils.isEmpty(t_qrids)){
                                  XzqgqrglDto xzqgqrglDto=new XzqgqrglDto();
                                  xzqgqrglDto.setIds(t_qrids);
                                  boolean result=xzqgqrglDao.updateFkwcbj(xzqgqrglDto);
                                  if(!result)
                                      return false;
                              }
                          }
                      }
                      //是否同步至每刻数据
                      if ("1".equals(mkflag)&&!CollectionUtils.isEmpty(fkmxlist)) {
                          //组装报销单费用导入数据
                          ExpenseRequestDto expenseRequestDto = new ExpenseRequestDto();
                          expenseRequestDto.setEmployeeId(operator.getYhm());
                          List<Expense> expenseList = new ArrayList<>();
                          for (XzqgfkmxDto dto : fkmxlist) {
                              Expense expense = new Expense();
                              expense.setConsumeAmount(new ConsumeAmount(Double.parseDouble(String.valueOf(new BigDecimal(dto.getJg()).multiply(new BigDecimal(dto.getSl())).setScale(4, RoundingMode.HALF_UP))), "CNY"));
                              //是否对公费用
                              expense.setCorpExpense(true);
                              //行政费用19003；行政采购类别编码见群里附件；采购货款19002；
                              expense.setExpenseTypeBizCode("19003");
                              expense.setCorpType("ALL_RECEIPTS");
                              //到票时间
                              expense.setReceiptDate(new Date().getTime());
                              //支付对象供应商业务编码
                              expense.setTradingPartnerBizCode(xzqgfkglDto_t.getYwbm());
                              //自定义字段 必传参数
                              Map<String, Object> map = new HashMap<>();
                              map.put("CF47", dto.getQgdh());
                              map.put("CF54", xzqgfkglDto_t.getFylbdm());
                              expense.setCustomObject(map);
                              expense.setComments(xzqgfkglDto_t.getBz());
                              expenseList.add(expense);
                          }
                          expenseRequestDto.setExpenseList(expenseList);
                          //导入费用成功后，将返回每刻系统内的唯一费用标识Code，该Code可用于报销单导入时，作为费用expenseCodes的参数使用
                          List<String> expenseCodes = externalUtil.receivExpense(JSON.toJSONString(expenseRequestDto));
                          if (CollectionUtils.isEmpty(expenseCodes)) {
                              throw new BusinessException("msg", "每刻报销单费用导入失败！");
                          }
                          //组装报销单导入数据
                          ReimburseDto reimburseDto = new ReimburseDto();
                          reimburseDto.setFormCode(xzqgfkglDto_t.getFkdh());
                          //单据类型编码
                          reimburseDto.setFormSubTypeBizCode("FT16250966831532077");
                          //申请人工号
                          reimburseDto.setSubmittedUserEmployeeId(operator.getYhm());
                          reimburseDto.setReimburseName(xzqgfkglDto_t.getFksy());
                          //公司抬头业务编码
                          reimburseDto.setLegalEntityBizCode(servicecode);
                          //承担人工号
                          reimburseDto.setCoverUserEmployeeId(operator.getYhm());
                          //用款部门jgid
                          reimburseDto.setCoverDepartmentBizCode(xzqgfkglDto_t.getSqbm());
                          reimburseDto.setRequestDepartment(xzqgfkglDto_t.getSqbmmc());
                          // 自定义字段 最晚支付日期
                          Map<String, Object> customMap = new HashMap<>();
                          try {
                              Map<String, Object> dateMap = new HashMap<>();
                              dateMap.put("currentTime",DateUtils.parse(xzqgfkglDto_t.getZwfkrq()).getTime());
                              customMap.put("CF50",dateMap);
                          } catch (Exception e) {
                              log.error("最晚支付日期转换long失败");
                          }
                          customMap.put("CF54", xzqgfkglDto_t.getFylbdm());
                          reimburseDto.setCustomObject(customMap);
                          PayeeAccount payeeAccount = new PayeeAccount();
                          payeeAccount.setBankAcctName(xzqgfkglDto_t.getZffkhh());
                          payeeAccount.setBankAcctNumber(xzqgfkglDto_t.getZffyhzh());
                          // 支付类型 ALIPAY-支付宝,BANK-银行账户,CASH-现金
                          payeeAccount.setPaymentType("BANK");
                          // 账户性质 个人-PERSONAL,公司-CORP
                          payeeAccount.setAccountType("CORP");
                          reimburseDto.setPayeeAccount(payeeAccount);
                          //导入费用成功后，将返回每刻系统内的唯一费用标识Code，该Code可用于报销单导入时，作为费用expenseCodes的参数使用
                          reimburseDto.setExpenseCodes(expenseCodes);
                          reimburseDto.setTradingPartnerBizCode(xzqgfkglDto_t.getYwbm());
                          reimburseDto.setSubmittedTime(new Date().getTime());
                          reimburseDto.setComments(xzqgfkglDto_t.getBz());
                          //是否校验其他数据 false为校验 true为不校验（只校验重要数据）
                          reimburseDto.setStagingFlag(false);
                          boolean isSuccess = externalUtil.receiveReimburse(JSON.toJSONString(reimburseDto));
                          if (!isSuccess) {
                              throw new BusinessException("msg", "每刻报销单导入失败！");
                          }
                      }
                      xzqgfkglDto_t.setZt(StatusEnum.CHECK_PASS.getCode());
                  //审核中
                  }else{
                      xzqgfkglDto_t.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                      //发起钉钉审批
                      if (!"1".equals(systemreceiveflg)) {
                          if("1".equals(shgcDto.getXlcxh()) && shgcDto.getShxx()==null) {
                              try {
                                  // 根据申请部门查询审核流程
                                  List<DdxxglDto> ddxxglDtos = ddxxglService.getProcessByJgid(xzqgfkglDto_t.getSqbm(), DdAuditTypeEnum.SP_FK_XZ_YJ.getCode());
                                  if("PTP".equals(xzqgfkglDto_t.getDzfsdm()))
                                      ddxxglDtos=ddxxglService.getProcessByJgid(xzqgfkglDto_t.getSqbm(), DdAuditTypeEnum.SP_FK_XZ_GDG.getCode());
                                  String spr=null;
                                  String csr=null;
                                  if(!CollectionUtils.isEmpty(ddxxglDtos)) {
                                      spr = ddxxglDtos.get(0).getSpr();
                                      csr = ddxxglDtos.get(0).getCsr();
                                  }
                                  if(!StringUtils.isNotBlank(spr))
                                      spr="";
                                  if(!StringUtils.isNotBlank(csr))
                                      csr="";
                                  if(StringUtil.isNotBlank(xzqgfkglDto_t.getCsrs())) {
                                      String[] split = xzqgfkglDto_t.getCsrs().split(",|，");
                                      // 根据抄送人查询钉钉ID
                                      List<String> yhids = Arrays.asList(split);
                                      List<User> yhxxs = commonService.getDdidByYhids(yhids);
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
                                  Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "付款申请");//获取审批模板ID
                                  String templateCode=(String) template.get("message");
                                  //获取申请人信息(合同申请应该为采购部门)
                                  User user=new User();
                                  user.setYhid(xzqgfkglDto_t.getSqr());
                                  user=commonService.getUserInfoById(user);
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
                                  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                                  XzqgfkmxDto xzqgfkmxDto=new XzqgfkmxDto();
                                  xzqgfkmxDto.setFkid(xzqgfkglDto_t.getFkid());
                                  List<XzqgfkmxDto> fkmxList=xzqgfkmxService.getDtoList(xzqgfkmxDto);
                                  List<Map<String,String>> mxlist=new ArrayList<>();
                                  //关联审批单,若为月结则关联确认单，若为公对公则关联请购单
                                  List<String> glspds = new ArrayList<>();
                                  if(!CollectionUtils.isEmpty(fkmxList)) {
                                      List<String> ids=new ArrayList<>();
                                      //若为月结，关联确认单
                                      if("MB".equals(xzqgfkglDto_t.getDzfsdm())){
                                          for(XzqgfkmxDto xzqgfkmxDto_t : fkmxList){
                                              ids.add(xzqgfkmxDto_t.getQrid());
                                          }
                                          if(!CollectionUtils.isEmpty(ids)) {
                                              XzqgqrglDto xzqgqrglDto=new XzqgqrglDto();
                                              xzqgqrglDto.setIds(ids);
                                              List<XzqgqrglDto> qrgllist=xzqgqrglDao.getDtoListByIds(xzqgqrglDto);
                                              if(!CollectionUtils.isEmpty(qrgllist)) {
                                                  for (XzqgqrglDto dto : qrgllist) {
                                                      if (StringUtil.isNotBlank(dto.getDdslid())) {
                                                          glspds.add(dto.getDdslid());
                                                      }
                                                  }
                                              }
                                          }
                                      }else{
                                          //若为公对公，则关联请购单
                                          for(XzqgfkmxDto xzqgfkmxDto_t : fkmxList){
                                              ids.add(xzqgfkmxDto_t.getQgid());
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
                                  }
                                  Date zwzfrq=sdf.parse(xzqgfkglDto_t.getZwfkrq());
                                  Map<String,String> a_map=new HashMap<>();
                                  map.put("费用归属",xzqgfkglDto_t.getFygsmc());
                                  List<String> bms = new ArrayList<>();
                                  bms.add(xzqgfkglDto_t.getSqbm());
                                  map.put("用款部门", JSON.toJSONString(bms));
                                  map.put("付款事由",xzqgfkglDto_t.getFksy());
                                  map.put("关联审批单",glspds.isEmpty() ? "" : JSON.toJSONString(glspds));
                                  map.put("付款总额",xzqgfkglDto_t.getFkje());
                                  map.put("单据传达方式",xzqgfkglDto_t.getDjcdfsmc());
                                  map.put("费用类别",xzqgfkglDto_t.getFylbmc());
                                  a_map.put("付款金额（元）",xzqgfkglDto_t.getFkje());//明细不采用，跟总金额保持一致
                                  a_map.put("付款方式",xzqgfkglDto_t.getFkfsmc());
                                  a_map.put("付款方",xzqgfkglDto_t.getFkfmc());
                                  a_map.put("最晚支付日期",sdf.format(zwzfrq));
                                  a_map.put("支付对象",xzqgfkglDto_t.getZfdx());
                                  a_map.put("支付方开户行",xzqgfkglDto_t.getZffkhh());
                                  a_map.put("支付方银行账户",xzqgfkglDto_t.getZffyhzh());
//                                  a_map.put("说明","详细信息:"+applicationurl+urlPrefix+"/ws/payment/getPaymentUrl?htfkid="+htfkqkDto_t.getHtfkid()+"&urlPrefix="+urlPrefix);
                                  mxlist.add(a_map);
                                  Map<String,Object> t_map=talkUtil.createInstance(operator.getYhm(), templateCode, approvers, cc_list, userid, dept, map,mxlist,null);
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
                                          XzqgfkglDto xzqgfkglDto_a=new XzqgfkglDto();
                                          xzqgfkglDto_a.setDdslid(String.valueOf(result_map.get("process_instance_id")));
                                          xzqgfkglDto_a.setXgry(operator.getYhid());
                                          xzqgfkglDto_a.setFkid(xzqgfkglDto_t.getFkid());
                                          dao.updateDdslid(xzqgfkglDto_a);
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
                          try{
                              int size = shgcDto.getNo_spgwcyDtos().size();
                              for(int i=0;i<size;i++){
                                  if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                                      talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
                                    		  shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,xzqgfkglDto_t.getFkdh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                                  }
                              }
                          }catch(Exception e){
                              log.error(e.getMessage());
                          }
                      }
                  }
                  //更新状态
                  update(xzqgfkglDto_t);
            }
            return true;

        } catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String fkid = shgcDto.getYwid();
                XzqgfkglDto xzqgfkglDto= new XzqgfkglDto();
                xzqgfkglDto.setXgry(operator.getYhid());
                xzqgfkglDto.setFkid(fkid);
                xzqgfkglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                update(xzqgfkglDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String fkid = shgcDto.getYwid();
                XzqgfkglDto xzqgfkglDto =new XzqgfkglDto();
                xzqgfkglDto.setXgry(operator.getYhid());
                xzqgfkglDto.setFkid(fkid);
                xzqgfkglDto.setZt(StatusEnum.CHECK_NO.getCode());
                update(xzqgfkglDto);
                //OA取消审批的同时组织钉钉审批
                XzqgfkglDto t_xzqgfkglDto=dao.getDto(xzqgfkglDto);
                if(t_xzqgfkglDto!=null && StringUtils.isNotBlank(t_xzqgfkglDto.getDdslid())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), t_xzqgfkglDto.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map=JSON.parseObject(success,Map.class);
                    boolean bo1= (boolean) result_map.get("success");
                    if(bo1){
                        t_xzqgfkglDto.setDdslid(null);
                        dao.updateDdslid(t_xzqgfkglDto);
                    }
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
        Map<String, Object> map = new HashMap<>();
        XzqgfkglDto xzqgfkglDto = dao.getDtoById(shgcDto.getYwid());
        map.put("jgid",xzqgfkglDto.getSqbm());
        return map;
    }

    @Override
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
        Map<String, Object> map =new HashMap<>();
        @SuppressWarnings("unchecked")
        List<String> ids = (List<String>)param.get("ywids");
        XzqgfkglDto xzqgfkglDto =new XzqgfkglDto();
        xzqgfkglDto.setIds(ids);
        List<XzqgfkglDto> dtoList = dao.getDtoList(xzqgfkglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(XzqgfkglDto dto:dtoList){
                list.add(dto.getFkid());
            }
        }
        map.put("list",list);
        return map;
    }

    /**
     * 行政请购确认审核列表
     * @param xzqgfkglDto
     * @return
     */
    public List<XzqgfkglDto> getPagedAuditList(XzqgfkglDto xzqgfkglDto){
        //获取人员ID和履历号
        List<XzqgfkglDto> t_sqList = dao.getPagedAuditIdList(xzqgfkglDto);

        if(CollectionUtils.isEmpty(t_sqList))
            return t_sqList;

        List<XzqgfkglDto> sqList = dao.getAuditListByIds(t_sqList);

        commonService.setSqrxm(sqList);

        return sqList;
    }

    /**
     * 钉钉审批回调
     * @param obj
     * @return
     * @throws BusinessException
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean callbackXzqgfkglAduit(JSONObject obj, HttpServletRequest request, Map<String,Object> t_map) throws BusinessException {
        XzqgfkglDto xzqgfkglDto=new XzqgfkglDto();
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
            xzqgfkglDto.setDdslid(processInstanceId);
            //根据钉钉审批实例ID查询关联单据
            xzqgfkglDto=dao.getDtoByDdslid(xzqgfkglDto);
            //若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if(xzqgfkglDto!=null) {
                //获取审批人信息
                User user=new User();
                user.setDdid(staffId);
                user.setWbcxid(wbcxidString);
                user = commonService.getByddwbcxid(user);
                //判断查出得信息是否为空
                if(user==null)
                    return false;
                //获取审批人角色信息
                List<XzqgfkglDto> dd_sprjs=dao.getSprjsBySprid(user.getYhid());
                // 获取当前审核过程
                ShgcDto t_shgcDto = shgcService.getDtoByYwid(xzqgfkglDto.getFkid());
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
                        shxxDto.setYwid(xzqgfkglDto.getFkid());
                        if (StringUtil.isNotBlank(finishTime)){
                            shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
                        }
                        String lastlcxh=null;//回退到指定流程
 
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
                                log.error("DingTalkMaterPurchaseAudit(钉钉物料请购审批转发提醒)------转发人员:"+user.getZsxm()+",人员ID:"+user.getYhid()+",单据号:"+xzqgfkglDto.getFkdh()+",单据ID:"+xzqgfkglDto.getFkid()+",转发时间:"+date);
                                return true;
                            }
                            //调用审核方法
                            Map<String, List<String>> map= ToolUtil.reformRequest(request);
                            log.error("map:"+map);
                            List<String> list=new ArrayList<>();
                            list.add(xzqgfkglDto.getFkid());
                            map.put("qgid", list);
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
                            shxxDto.setYwglmc(xzqgfkglDto.getFkdh());
                            XzqgfkglDto t_xzqgfkDto=new XzqgfkglDto();
                            t_xzqgfkDto.setFkid(xzqgfkglDto.getFkid());
                            t_xzqgfkDto.setDdslid(null);
                            dao.updateDdslid(t_xzqgfkDto);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            //调用审核方法
                            Map<String, List<String>> map=ToolUtil.reformRequest(request);
                            List<String> list=new ArrayList<>();
                            list.add(t_xzqgfkDto.getFkid());
                            map.put("fkid", list);
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
                        shxx.setYwid(xzqgfkglDto.getQrid());
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
     * 行政付款列表（查询审核状态）
     *
     * @param xzqgfkglDto
     * @return
     */
    @Override
    public List<XzqgfkglDto> getPagedDtoList(XzqgfkglDto xzqgfkglDto) {
        List<XzqgfkglDto> list = dao.getPagedDtoList(xzqgfkglDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_ADMINPURCHASEPAY.getCode(), "zt", "fkid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_ADMINPURCHASEPAY_PTP.getCode(), "zt", "fkid",
                    new String[]{StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    /**
     * 修改行政请购付款信息
     * @param xzqgfkglDto
     * @return
     */
    public boolean updateAdministationPay(XzqgfkglDto xzqgfkglDto){
        int updateXzqgfkgl=dao.update(xzqgfkglDto);
        if(updateXzqgfkgl<=0)
            return false;
        XzqgfkmxDto xzqgfkmxDto=new XzqgfkmxDto();
        xzqgfkmxDto.setFkid(xzqgfkglDto.getFkid());
        List<XzqgfkmxDto> xzqgfkmxlist= JSON.parseArray(xzqgfkglDto.getFkmxJson(), XzqgfkmxDto.class);
        List<XzqgfkmxDto> yy_xzqgfkmxlist=xzqgfkmxService.getDtoList(xzqgfkmxDto);
        List<XzqgfkmxDto> add_list=new ArrayList<>();
        List<XzqgfkmxDto> mod_list=new ArrayList<>();
        List<XzqgfkmxDto> del_list=new ArrayList<>();
        for(XzqgfkmxDto t_xzqgfkmxDto : xzqgfkmxlist){
            //处理新增明细
            if(StringUtils.isBlank(t_xzqgfkmxDto.getFkmxid())){
                t_xzqgfkmxDto.setLrry(xzqgfkglDto.getXgry());
                add_list.add(t_xzqgfkmxDto);
            }else{
                //处理删除明细
                boolean sfcz=false;
                for(XzqgfkmxDto yy_xzqgfkmxDto : yy_xzqgfkmxlist){
                    if(t_xzqgfkmxDto.getFkmxid().equals(yy_xzqgfkmxDto.getFkmxid())){
                        t_xzqgfkmxDto.setXgry(xzqgfkglDto.getXgry());
                        mod_list.add(t_xzqgfkmxDto);
                        sfcz=true;
                    }
                }
                if(!sfcz){
                    t_xzqgfkmxDto.setScry(xzqgfkglDto.getXgry());
                    del_list.add(t_xzqgfkmxDto);
                }
            }
        }
        if(!CollectionUtils.isEmpty(add_list)){
            boolean addresult=xzqgfkmxService.insertDtoList(add_list);
            if(!addresult)
                return false;
        }
        if(!CollectionUtils.isEmpty(mod_list)){
            boolean modresult=xzqgfkmxService.updateBatch(mod_list);
            if(!modresult)
                return false;
        }
        if(!CollectionUtils.isEmpty(del_list)){
            return xzqgfkmxService.delDtoList(del_list);
        }
        return true;
    }

    /**
     * 删除付款信息
     * @param xzqgfkglDto
     * @return
     */
    public boolean deleteFkxx(XzqgfkglDto xzqgfkglDto){
        int delFkgl=dao.delete(xzqgfkglDto);//删除付款管理
        if(delFkgl<=0)
            return false;
        XzqgfkmxDto xzqgfkmxDto=new XzqgfkmxDto();
        xzqgfkmxDto.setIds(xzqgfkglDto.getIds());
        List<XzqgfkmxDto> mxlist=xzqgfkmxService.getDtoList(xzqgfkmxDto);
        List<String> qgmxids=new ArrayList<>();
        List<String> qgids=new ArrayList<>();
        if(!CollectionUtils.isEmpty(mxlist)) {//删除付款明细
            for(XzqgfkmxDto t_xzqgfkmxDto : mxlist){
                qgmxids.add(t_xzqgfkmxDto.getQgmxid());
                qgids.add(t_xzqgfkmxDto.getQgid());
                t_xzqgfkmxDto.setScry(xzqgfkglDto.getScry());
            }
            boolean delFkmx=xzqgfkmxService.delDtoList(mxlist);
            if(!delFkmx)
                return false;
        }
        //更新付款标记
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

    /**
     * 选中导出
     * @param params
     * @return
     */
    public List<XzqgfkglDto> getListForSelectExp(Map<String, Object> params){
        XzqgfkglDto xzqgfkglDto = (XzqgfkglDto) params.get("entryData");
        queryJoinFlagExport(params,xzqgfkglDto);
        return dao.getListForSelectExp(xzqgfkglDto);
    }

    /**
     * 根据搜索条件获取导出条数
     * @param params
     * @return
     */
    public int getCountForSearchExp(XzqgfkglDto xzqgfkglDto,Map<String,Object> params){
        return dao.getCountForSearchExp(xzqgfkglDto);
    }

    /**
     * 根据搜索条件分页获取导出信息
     * @param params
     * @return
     */
    public List<XzqgfkglDto> getListForSearchExp(Map<String,Object> params){
        XzqgfkglDto xzqgfkglDto = (XzqgfkglDto)params.get("entryData");
        queryJoinFlagExport(params,xzqgfkglDto);
        return dao.getListForSearchExp(xzqgfkglDto);
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String,Object> params, XzqgfkglDto xzqgfkglDto){
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
        xzqgfkglDto.setSqlParam(sqlcs);
    }

}

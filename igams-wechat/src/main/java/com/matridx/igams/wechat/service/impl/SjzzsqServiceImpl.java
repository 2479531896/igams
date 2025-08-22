package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XxglModel;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.EntityTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.enums.UserHabitTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.JingDongUtil;
import com.matridx.igams.common.util.ShunFengUtil;
import com.matridx.igams.wechat.dao.entities.SjkdxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjzzsqDto;
import com.matridx.igams.wechat.dao.entities.SjzzsqModel;
import com.matridx.igams.wechat.dao.entities.YhsyxgjlDto;
import com.matridx.igams.wechat.dao.post.ISjzzsqDao;
import com.matridx.igams.wechat.service.svcinterface.ISjkdxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjzzsqService;
import com.matridx.igams.wechat.service.svcinterface.IYhsyxgjlService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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


@Service
public class SjzzsqServiceImpl extends BaseBasicServiceImpl<SjzzsqDto, SjzzsqModel, ISjzzsqDao> implements ISjzzsqService, IAuditService {

    @Autowired
    ICommonService commonservice;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ISjkdxxService sjkdxxService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ISjxxService sjxxService;
    @Autowired
    IYhsyxgjlService yhsyxgjlService;
    @Autowired
    JingDongUtil jingDongUtil;
    @Autowired
    ShunFengUtil shunFengUtil;
    @Autowired
    IShgcService shgcService;
    @Autowired
    ICommonService commonService;
    private static final Logger logger = LoggerFactory.getLogger(SjzzsqServiceImpl.class);

    /**
     * 获取纸质报告申请列表
     * @param sjzzsqDto
     * @return
     */
    @Override
    public List<SjzzsqDto> getPagedDtoListPaperReportsApply(SjzzsqDto sjzzsqDto){
        return dao.getPagedDtoListPaperReportsApply(sjzzsqDto);
    }

    /**
     * 新增保存纸质报告信息
     * @param sjzzsqDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addSavePaperReportsApply(SjzzsqDto sjzzsqDto,User user,List<String> zzsqids){
        boolean isSuccess=false;
        YhsyxgjlDto yhsyxgjlDto=new YhsyxgjlDto();
        yhsyxgjlDto.setYhid(user.getYhid());
        yhsyxgjlDto.setYwlx(UserHabitTypeEnum.USER_HABIT_SJZZSQ.getCode());
        List<YhsyxgjlDto> dtoList = yhsyxgjlService.getDtoList(yhsyxgjlDto);
        if(dtoList.size()>=3){
            boolean flg=true;
            for(YhsyxgjlDto dto:dtoList){
                if(dto.getZdz1().equals(sjzzsqDto.getSjr())&&dto.getZdz2().equals(sjzzsqDto.getDh())){
                    dto.setZdz1(sjzzsqDto.getSjr());
                    dto.setZdz2(sjzzsqDto.getDh());
                    dto.setZdz3(sjzzsqDto.getDz());
                    yhsyxgjlService.update(dto);
                    flg=false;
                    break;
                }
            }
            if(flg){
                YhsyxgjlDto yhsyxgjlDto_t = dtoList.get(2);
                yhsyxgjlDto_t.setZdz1(sjzzsqDto.getSjr());
                yhsyxgjlDto_t.setZdz2(sjzzsqDto.getDh());
                yhsyxgjlDto_t.setZdz3(sjzzsqDto.getDz());
                yhsyxgjlService.update(yhsyxgjlDto_t);
            }
        }else{
            if(dtoList!=null&&dtoList.size()>0){
                boolean flg=true;
                for(YhsyxgjlDto dto:dtoList){
                    if(dto.getZdz1().equals(sjzzsqDto.getSjr())&&dto.getZdz1().equals(sjzzsqDto.getSjr())&&dto.getZdz2().equals(sjzzsqDto.getDh())){
                        dto.setZdz1(sjzzsqDto.getSjr());
                        dto.setZdz2(sjzzsqDto.getDh());
                        dto.setZdz3(sjzzsqDto.getDz());
                        yhsyxgjlService.update(dto);
                        flg=false;
                        break;
                    }
                }
                if(flg){
                    YhsyxgjlDto yhsyxgjlDto_t = new YhsyxgjlDto();
                    yhsyxgjlDto_t.setSyxgjlid(StringUtil.generateUUID());
                    yhsyxgjlDto_t.setYhid(user.getYhid());
                    yhsyxgjlDto_t.setYwlx(UserHabitTypeEnum.USER_HABIT_SJZZSQ.getCode());
                    yhsyxgjlDto_t.setZdm1("sjr");
                    yhsyxgjlDto_t.setZdz1(sjzzsqDto.getSjr());
                    yhsyxgjlDto_t.setZdm2("dh");
                    yhsyxgjlDto_t.setZdz2(sjzzsqDto.getDh());
                    yhsyxgjlDto_t.setZdm3("dz");
                    yhsyxgjlDto_t.setZdz3(sjzzsqDto.getDz());
                    yhsyxgjlService.insert(yhsyxgjlDto_t);
                }
            }else{
                YhsyxgjlDto yhsyxgjlDto_t = new YhsyxgjlDto();
                yhsyxgjlDto_t.setSyxgjlid(StringUtil.generateUUID());
                yhsyxgjlDto_t.setYhid(user.getYhid());
                yhsyxgjlDto_t.setYwlx(UserHabitTypeEnum.USER_HABIT_SJZZSQ.getCode());
                yhsyxgjlDto_t.setZdm1("sjr");
                yhsyxgjlDto_t.setZdz1(sjzzsqDto.getSjr());
                yhsyxgjlDto_t.setZdm2("dh");
                yhsyxgjlDto_t.setZdz2(sjzzsqDto.getDh());
                yhsyxgjlDto_t.setZdm3("dz");
                yhsyxgjlDto_t.setZdz3(sjzzsqDto.getDz());
                yhsyxgjlService.insert(yhsyxgjlDto_t);
            }
        }
        if(sjzzsqDto.getIds()!=null&&sjzzsqDto.getIds().size()>0){
            List<SjzzsqDto> sjzzsqDtos=new ArrayList<>();
            for(String sjid:sjzzsqDto.getIds()){
                SjzzsqDto sjzzsqDto_t=new SjzzsqDto();
                String zzsqid = StringUtil.generateUUID();
                sjzzsqDto_t.setZzsqid(zzsqid);
                sjzzsqDto_t.setZt(StatusEnum.CHECK_NO.getCode());
                sjzzsqDto_t.setSjid(sjid);
                sjzzsqDto_t.setSjr(sjzzsqDto.getSjr());
                sjzzsqDto_t.setDh(sjzzsqDto.getDh());
                sjzzsqDto_t.setDz(sjzzsqDto.getDz());
                sjzzsqDto_t.setFpsj(sjzzsqDto.getFpsj());
                sjzzsqDto_t.setBz(sjzzsqDto.getBz());
                sjzzsqDto_t.setFs(sjzzsqDto.getFs());
                sjzzsqDto_t.setLrry(user.getYhid());
                sjzzsqDtos.add(sjzzsqDto_t);
                zzsqids.add(zzsqid);
            }
            if(sjzzsqDtos!=null&&sjzzsqDtos.size()>0){
                isSuccess=dao.insertList(sjzzsqDtos);
            }
        }else{
            if (StringUtil.isBlank(sjzzsqDto.getSjid())){
                return false;
            }
            String zzsqid = StringUtil.generateUUID();
            sjzzsqDto.setZzsqid(zzsqid);
            sjzzsqDto.setZt(StatusEnum.CHECK_NO.getCode());
            isSuccess=dao.addSavePaperReportsApply(sjzzsqDto);
        }
        return isSuccess;
    }

    /**
     * 修改保存纸质报告信息
     * @param sjzzsqDto
     * @return
     */
    public boolean modSavePaperReportsApply(SjzzsqDto sjzzsqDto){
        int result=dao.update(sjzzsqDto);
        return result > 0;
    }

    /**
     * 审核列表
     * @param sjzzsqDto
     * @return
     */
    public List<SjzzsqDto> getPagedAuditPaperReports(SjzzsqDto sjzzsqDto){
        // 获取人员ID和履历号
        List<SjzzsqDto> t_sbyzList= dao.getPagedAuditPaperReports(sjzzsqDto);

        if (t_sbyzList == null || t_sbyzList.size() == 0)
            return t_sbyzList;

        List<SjzzsqDto> sqList = dao.getAuditListPaperReports(t_sbyzList);

        commonservice.setSqrxm(sqList);

        return sqList;
    }

    /**
     * 获取sjid字符串
     * @param sjzzsqDto
     * @return
     */
    public String getSjidsByIds(SjzzsqDto sjzzsqDto){
        return dao.getSjidsByIds(sjzzsqDto);
    }

    /**
     * 获取状态为审核中的sjid字符串
     * @param sjzzsqDto
     * @return
     */
    public String getSjidsByZt(SjzzsqDto sjzzsqDto){
        return dao.getSjidsByZt(sjzzsqDto);
    }

    /**
     * 锁定
     * @param sjzzsqDto
     * @return
     */
    public boolean lock(SjzzsqDto sjzzsqDto){
        return dao.lock(sjzzsqDto);
    }

    /**
     * 获取批量审核全部数据
     * @param shgcList
     * @return
     */
    public String getStringByYwids(List<ShgcDto> shgcList){
        return dao.getStringByYwids(shgcList);
    }

    /**
     * 取消快递订单，根据单号的前缀是‘SF’还是‘JD’来判断调用京东/顺丰的取消订单
     * @return
     */
    @Override
    public Map<String,Object> cancelOrder(SjzzsqDto sjzzsqDto,User user) {
        boolean isOk;
        Map<String,Object> result = new HashMap<>();
        //通过包号获取该包号下所有的纸质申请数据，循环审核后退一步
        List<SjzzsqDto> sjzzsqDtos = dao.getDtoListByBh(sjzzsqDto);
        for (SjzzsqDto sjzz: sjzzsqDtos) {
            ShxxDto shxxDto = dao.getAuditDto(sjzz);
            logger.error("cancelOrder取消订单的审核信息："+JSON.toJSONString(shxxDto));
            try {
                //传入委托相关参数
                DataPermission.addWtParam(shxxDto);
                isOk = shgcService.doCancleAudit(shxxDto, user);
                if (!isOk){
                    String msg = xxglService.getModelById("ICOM99020").getXxnr();
                    result.put("status", isOk?"success":"fail");
                    result.put("message", msg);
                    return result;
                }
                result.put("status", "success");
                result.put("message", xxglService.getModelById("ICOM00001").getXxnr());
            }catch (BusinessException e) {
                String mess = e.getMsg();
                XxglModel xxglModel = xxglService.getModelById(e.getMsgId());
                String idMsg = xxglModel==null?"":xxglModel.getXxnr();
                result.put("status", "fail");
                result.put("message", idMsg + (StringUtil.isNotBlank(idMsg) && StringUtil.isNotBlank(mess) ? "<br/>" : "")
                        + (StringUtil.isNotBlank(mess) ? mess : ""));
                return result;
            }catch (Exception e) {
                logger.error(e.getMessage());
                result.put("status", "fail");
                result.put("message", xxglService.getModelById("ICOM99021").getXxnr());
                return result;
            }
        }
        //=======================调用京东/顺丰接口取消订单====================
        //第一次后退时候更新单号为null，再后退无需再取消单号
        if (StringUtil.isNotBlank(sjzzsqDto.getMailno())){
            result = cancelMailno(sjzzsqDto, user);//调顺丰京东接口取消订单
        }
        return result;
    }

    private Map<String, Object> cancelMailno(SjzzsqDto sjzzsqDto, User user) {
        Map<String, Object> result = new HashMap<>();
        //根据mailno前缀是SF还是JD区分是取消顺丰快递还是京东快递
        if ("SF".equals(sjzzsqDto.getMailno().substring(0,2))){
            try {
                String sjmailno = dao.getSjmailnoByMailno(sjzzsqDto);
                String ress = shunFengUtil.cancelOrder(sjmailno);
//                String ress = shunFengUtil.cancelOrder("322E3B12EDF048F2AD4B5FC8198DDDB8");
                //成功取消{"apiErrorMsg":"","apiResponseID":"000180FE5ACBE23FCCA91641910DA83F","apiResultCode":"A1000","apiResultData":"{\"success\":true,\"errorCode\":\"S0000\",\"errorMsg\":null,\"msgData\":{\"orderId\":\"322E3B12EDF048F2AD4B5FC8198DDDB8\",\"waybillNoInfoList\":[{\"waybillType\":1,\"waybillNo\":\"SF7444439560563\"}],\"resStatus\":2,\"extraInfoList\":null}}"}
                //已消单{ "apiErrorMsg": "", "apiResponseID": "000180FE6087F63FD7B981DC1AF6CC3F", "apiResultCode": "A1000", "apiResultData": "{\"success\":false,\"errorCode\":\"8037\",\"errorMsg\":\"已消单\",\"msgData\":null}" }
                logger.error("--顺丰物流取消订单接口返回信息--："+ress);
                JSONObject jsonObject = JSONObject.parseObject(ress);
                if (jsonObject.getJSONObject("apiResultData").getBoolean("success")){
                    SjkdxxDto sjkdxxDto = new SjkdxxDto();
                    sjkdxxDto.setMailno(sjzzsqDto.getMailno());
                    sjkdxxDto.setSfqx("1");
                    sjkdxxService.updateByMailno(sjkdxxDto);//更改取消状态为已取消，快递单号置为空
                    result.put("status", "success");
                    result.put("message", xxglService.getModelById("ICOM00001").getXxnr());
                    return result;
                }else {
                    result.put("status", "fail");
                    result.put("message", "调用顺丰取消接口返回错误码："+jsonObject.getJSONObject("apiResultData").getString("errorCode")+"，返回错误信息："+jsonObject.getJSONObject("apiResultData").getString("errorMsg"));
                    return result;
                }
//                return true;
            }catch (Exception e){
                logger.error("cancelOrder方法中取消顺丰快递单号报错，快递单号为："+sjzzsqDto.getMailno()+"，错误信息为："+e.getMessage());
                result.put("status", "fail");
                result.put("message", e.getMessage());
                return result;
//                return false;
            }
        }else if ("JD".equals(sjzzsqDto.getMailno().substring(0,2))){
            //取消操作人，运单号
            try {
                String res = jingDongUtil.cancelOrder(user.getZsxm(), sjzzsqDto.getMailno());
                logger.error("--京东物流取消订单接口返回信息--："+res);
                JSONObject jsonObject = JSONObject.parseObject(res);
                if ("1".equals(jsonObject.getJSONObject("result").getString("stateCode"))){
                    SjkdxxDto sjkdxxDto = new SjkdxxDto();
                    sjkdxxDto.setMailno(sjzzsqDto.getMailno());
                    sjkdxxDto.setSfqx("1");
                    sjkdxxService.updateByMailno(sjkdxxDto);//更改取消状态为已取消，快递单号置为空
                    result.put("status", "success");
                    result.put("message", xxglService.getModelById("ICOM00001").getXxnr());
                    return result;
                }else {
                    result.put("status", "fail");
                    result.put("message", "调用京东取消接口返回错误码："+jsonObject.getJSONObject("result").getString("stateCode")+"，返回错误信息："+jsonObject.getJSONObject("result").getString("stateMessage")+"，requestionId："+jsonObject.getJSONObject("result").getString("requestionId"));
                    return result;
                }
//                return true;
            }catch (Exception e){
                logger.error("cancelOrder方法中取消京东快递单号报错，快递单号为："+sjzzsqDto.getMailno()+"错误信息为："+e.getMessage());
                result.put("status", "fail");
                result.put("message", e.getMessage());
                return result;
//                return false;
            }
            //String res = "{\"code\":\"0\",\"msg\":\"{\\\"response\\\":{\\\"content\\\":{\\\"requestionId\\\":77054535,\\\"stateCode\\\":1,\\\"stateMessage\\\":\\\"运单取消拦截成功\\\"}, \\\"code\\\":0}}\",\"result\":{\"requestionId\":77054535,\"stateCode\":1,\"stateMessage\":\"运单取消拦截成功\"}}";
        }else {
            logger.info("cancelOrder方法中，快递单号："+ sjzzsqDto.getMailno()+"前缀既不是京东又不是顺丰，取消订单失败");
            result.put("status", "fail");
            result.put("message","快递单号前缀既不是京东又不是顺丰，取消订单失败,请确认快递单号是否可用");
            return result;
//            return false;
        }
    }

    @Override
    public Map<String, Object> createOrder(SjzzsqDto sjzzsqDto, User user) {
        Map<String,String> map = new HashMap<>();
        Map<String,Object> resmap = new HashMap<>();
        map.put("xgry",sjzzsqDto.getXgry());
        map.put("oldmailno",sjzzsqDto.getMailno());
        String sjmailno = sjkdxxService.generateMailno();
        JcsjDto jc_kdlx = redisUtil.hgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SD_TYPE,sjzzsqDto.getKdlx());
        if ("SF".equals(jc_kdlx.getCsdm())){
            try{
                String SFInfo=shunFengUtil.SfCreateOrder(sjmailno, sjzzsqDto.getSjr(), sjzzsqDto.getDz(), sjzzsqDto.getDh());
//                String SFInfo="{\"apiErrorMsg\":\"\",\"apiResponseID\":\"000180FDDD5BA63FDE61E7FE00E8323F\",\"apiResultCode\":\"A1000\",\"apiResultData\":\"{\\\"success\\\":true,\\\"errorCode\\\":\\\"S0000\\\",\\\"errorMsg\\\":null,\\\"msgData\\\":{\\\"orderId\\\":\\\"322E3B12EDF048F2AD4B5FC8198DDDB8\\\",\\\"originCode\\\":\\\"\\\",\\\"destCode\\\":\\\"\\\",\\\"filterResult\\\":2,\\\"remark\\\":\\\"\\\",\\\"url\\\":null,\\\"paymentLink\\\":null,\\\"isUpstairs\\\":null,\\\"isSpecialWarehouseService\\\":null,\\\"mappingMark\\\":null,\\\"agentMailno\\\":null,\\\"returnExtraInfoList\\\":null,\\\"waybillNoInfoList\\\":[{\\\"waybillType\\\":1,\\\"waybillNo\\\":\\\"SF7444439560563\\\",\\\"boxNo\\\":null,\\\"length\\\":null,\\\"width\\\":null,\\\"height\\\":null,\\\"weight\\\":null,\\\"volume\\\":null}],\\\"routeLabelInfo\\\":[{\\\"code\\\":\\\"1000\\\",\\\"routeLabelData\\\":{\\\"waybillNo\\\":\\\"SF7444439560563\\\",\\\"sourceTransferCode\\\":null,\\\"sourceCityCode\\\":\\\"\\\",\\\"sourceDeptCode\\\":null,\\\"sourceTeamCode\\\":null,\\\"destCityCode\\\":\\\"\\\",\\\"destDeptCode\\\":null,\\\"destDeptCodeMapping\\\":null,\\\"destTeamCode\\\":null,\\\"destTeamCodeMapping\\\":null,\\\"destTransferCode\\\":null,\\\"destRouteLabel\\\":\\\"\\\",\\\"proName\\\":null,\\\"cargoTypeCode\\\":\\\"C201\\\",\\\"limitTypeCode\\\":\\\"T4\\\",\\\"expressTypeCode\\\":\\\"B1\\\",\\\"codingMapping\\\":null,\\\"codingMappingOut\\\":null,\\\"xbFlag\\\":null,\\\"printFlag\\\":null,\\\"twoDimensionCode\\\":\\\"MMM={'k1':'','k2':'','k3':'','k4':'T4','k5':'SF7444439560563','k6':'','k7':''}\\\",\\\"proCode\\\":\\\"T4\\\",\\\"printIcon\\\":null,\\\"abFlag\\\":null,\\\"destPortCode\\\":null,\\\"destCountry\\\":null,\\\"destPostCode\\\":null,\\\"goodsValueTotal\\\":null,\\\"currencySymbol\\\":null,\\\"cusBatch\\\":null,\\\"goodsNumber\\\":null,\\\"errMsg\\\":null,\\\"checkCode\\\":null,\\\"proIcon\\\":null,\\\"fileIcon\\\":null,\\\"fbaIcon\\\":null,\\\"icsmIcon\\\":null,\\\"destGisDeptCode\\\":null,\\\"newIcon\\\":null,\\\"sendAreaCode\\\":null,\\\"destinationStationCode\\\":null,\\\"sxLabelDestCode\\\":null,\\\"sxDestTransferCode\\\":null,\\\"sxCompany\\\":null,\\\"newAbFlag\\\":null,\\\"destAddrKeyWord\\\":null,\\\"rongType\\\":null,\\\"waybillIconList\\\":null},\\\"message\\\":null}],\\\"contactInfoList\\\":null,\\\"sendStartTm\\\":null,\\\"customerRights\\\":null,\\\"expressTypeId\\\":null}}\"}";
                logger.error("--顺丰物流下单接口返回信息--："+SFInfo);
                JSONObject jsonObject = JSONObject.parseObject(SFInfo);
//                JSONObject apiResultData = jsonObject.getJSONObject("apiResultData");
                if(jsonObject.getJSONObject("apiResultData").getBoolean("success")){
                    //下单成功
                    String waybillNo = jsonObject.getJSONObject("apiResultData").getJSONObject("msgData").getJSONArray("waybillNoInfoList").getJSONObject(0).getString("waybillNo");
                    map.put("newmailno",waybillNo);
                    map.put("sjmailno",sjmailno);
                    map.put("sfqx","0");
                    map.put("kdzt","0");
                    sjkdxxService.updateMailnoByOld(map);//将旧的mailno为oldmailno的数据改为mailno为newmailno
                    resmap.put("status",true);
                    resmap.put("message","重下单成功");
                }else{
                    logger.error("顺丰重下单报错"+SFInfo);
                    resmap.put("status",false);
                    resmap.put("message",SFInfo);
                    System.out.println("顺丰重下单报错"+SFInfo);
                }
            }catch (Exception e){
                logger.error("createOrder顺丰方法报错"+e.getMessage());
                resmap.put("status",false);
                resmap.put("message",e.getMessage());
            }
        }else if("JD".equals(jc_kdlx.getCsdm())){
            try {
               String JDInfo = jingDongUtil.receiveOrderInfo(sjmailno,sjzzsqDto.getSjr(),sjzzsqDto.getDz(),sjzzsqDto.getDh(),null);
//                String JDInfo ="{\"code\":\"0\",\"msg\":\"{\\\"response\\\":{\\\"content\\\":{\\\"deliveryId\\\":\\\"JDVB16033803683\\\",\\\"expressOperationMode\\\":3,\\\"needRetry\\\":false,\\\"preSortResult\\\":{\\\"aging\\\":4,\\\"agingName\\\":\\\"次晨达\\\",\\\"collectionAddress\\\":\\\"\\\",\\\"coverCode\\\":\\\"T\\\",\\\"distributeCode\\\":\\\"233\\\",\\\"isHideContractNumbers\\\":1,\\\"isHideName\\\":1,\\\"qrcodeUrl\\\":\\\"https://mp.weixin.qq.com/a/~bdyFWnK5nG7Ly5w-xXbAYg~~\\\",\\\"road\\\":\\\"0\\\",\\\"siteId\\\":447520,\\\"siteName\\\":\\\"杭州仁和营业部\\\",\\\"siteType\\\":4,\\\"slideNo\\\":\\\"31\\\",\\\"sourceCrossCode\\\":\\\"31\\\",\\\"sourceSortCenterId\\\":822956,\\\"sourceSortCenterName\\\":\\\"杭州东洲分拣中心\\\",\\\"sourceTabletrolleyCode\\\":\\\"233C \\\",\\\"targetCrossCode\\\":\\\"31\\\",\\\"targetSortCenterId\\\":761804,\\\"targetSortCenterName\\\":\\\"杭州富阳分拣中心\\\",\\\"targetTabletrolleyCode\\\":\\\"Y39\\\"},\\\"promiseTimeType\\\":2,\\\"resultCode\\\":100,\\\"resultMessage\\\":\\\"成功\\\",\\\"transType\\\":1}, \\\"code\\\":0}}\",\"result\":{\"deliveryId\":\"JDVB16033803683\",\"expressOperationMode\":3,\"needRetry\":false,\"preSortResult\":{\"aging\":4,\"agingName\":\"次晨达\",\"collectionAddress\":\"\",\"coverCode\":\"T\",\"distributeCode\":\"233\",\"isHideContractNumbers\":1,\"isHideName\":1,\"qrcodeUrl\":\"https://mp.weixin.qq.com/a/~bdyFWnK5nG7Ly5w-xXbAYg~~\",\"road\":\"0\",\"siteId\":447520,\"siteName\":\"杭州仁和营业部\",\"siteType\":4,\"slideNo\":\"31\",\"sourceCrossCode\":\"31\",\"sourceSortCenterId\":822956,\"sourceSortCenterName\":\"杭州东洲分拣中心\",\"sourceTabletrolleyCode\":\"233C \",\"targetSortCenterId\":761804,\"targetSortCenterName\":\"杭州富阳分拣中心\",\"targetTabletrolleyCode\":\"Y39\"},\"promiseTimeType\":2,\"resultCode\":100,\"resultMessage\":\"成功\",\"transType\":1}}";
                logger.error("--京东物流下单接口返回信息--："+JDInfo);
                JSONObject jsonObject = JSONObject.parseObject(JDInfo);
                JSONObject result = jsonObject.getJSONObject("result");
                if("100".equals(jsonObject.getJSONObject("result").getString("resultCode"))){//调用下单接口返回码为100表示成功调用
                    String deliveryId = result.getString("deliveryId");
                    map.put("newmailno",deliveryId);
                    map.put("sjmailno",sjmailno);
                    map.put("sfqx","0");
                    map.put("kdzt","0");
                    sjkdxxService.updateMailnoByOld(map);//将旧的mailno为oldmailno的数据改为mailno为newmailno
                    resmap.put("status",true);
                    resmap.put("message","重下单成功");
                }else {
                    logger.error("京东重下单报错"+JDInfo);
                    resmap.put("status",false);
                    resmap.put("message",JDInfo);
                    System.out.println("京东重下单报错"+JDInfo);
                }
            }catch (Exception e){
                logger.error("createOrder京东方法报错"+e.getMessage());
                resmap.put("status",false);
                resmap.put("message",e.getMessage());
            }
        }else {
            logger.info("createOrder方法中，重下单时候没有快递单号或者单号"+sjzzsqDto.getMailno()+"前缀既不是京东又不是顺丰");
            resmap.put("status",false);
            resmap.put("message","数据快递单号缺失，不可重下单");
        }
        return resmap;
    }

    @Override
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        SjzzsqDto sjzzsqDto=(SjzzsqDto)baseModel;
        boolean flag =true;
        if(StringUtil.isNotBlank(sjzzsqDto.getKdlx())){
            SjkdxxDto sjkdxxDto=new SjkdxxDto();
            sjkdxxDto.setYwid(sjzzsqDto.getZzsqid());
            sjkdxxDto.setYwlx(EntityTypeEnum.ENTITY_SJZZSQDTO.getCode());
            SjkdxxDto dto = sjkdxxService.getDto(sjkdxxDto);
            //判断sjkdxx表中是否存在此ywid,存在则更新，不存在则新增
            if(dto!=null){
                dto.setKdlx(sjzzsqDto.getKdlx());
                dto.setJdlx(sjzzsqDto.getJdlx());
                flag=sjkdxxService.update(dto);
            }else{
                sjkdxxDto.setSjkdid(StringUtil.generateUUID());
                sjkdxxDto.setKdlx(sjzzsqDto.getKdlx());
                sjkdxxDto.setJdlx(sjzzsqDto.getJdlx());
                flag=sjkdxxService.insert(sjkdxxDto);
            }
        }
        return flag;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
//        String token = talkUtil.getToken();
        for (int j=0;j<shgcList.size();j++) {
            SjzzsqDto sjzzsqDto = new SjzzsqDto();
            sjzzsqDto.setZzsqid(shgcList.get(j).getYwid());
            sjzzsqDto.setXgry(operator.getYhid());
            SjzzsqDto sjzzsqDto_t = getDto(sjzzsqDto);
            List<SpgwcyDto> spgwcyDtos = shgcList.get(j).getSpgwcyDtos();
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcList.get(j).getAuditState())) {
                sjzzsqDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (spgwcyDtos != null && spgwcyDtos.size() > 0 ) {
                    for (int i = 0; i < spgwcyDtos.size(); i++) {
                        if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcList.get(j).getShlb(),spgwcyDtos.get(i).getYhid(),spgwcyDtos.get(i).getYhm(),
                            		spgwcyDtos.get(i).getYhid(),
                                    xxglService.getMsg("ICOMM_SH00026"),xxglService.getMsg("ICOMM_SH00001",operator.getZsxm(),shgcList.get(j).getShlbmc() ,sjzzsqDto_t.getYbbh(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcList.get(j).getAuditState())) {
                sjzzsqDto.setZt(StatusEnum.CHECK_PASS.getCode());
                SjxxDto sjxxDto=new SjxxDto();
                sjxxDto.setSjid(sjzzsqDto_t.getSjid());
                SjxxDto sjxxDto_t = sjxxService.getDto(sjxxDto);
                if(sjxxDto_t!=null){
                    if(StringUtil.isBlank(sjxxDto_t.getBgrq())){
                        throw new BusinessException("msg","标本编号为："+sjxxDto_t.getYbbh()+"的信息未出报告，无法审核通过！");
                    }
                }
                if (j == shgcList.size()-1){
                    String str = getStringByYwids(shgcList);
                    String[] split = str.split(",");
                    String dhs = getDhByYwids(shgcList);
                    String[] dh = dhs.split(",");
                    if(split.length>1||dh.length>1){
                        throw new BusinessException("msg","所选的记录中存在地址或者电话不一样的数据，无法审核通过！");
                    }else {
                        List<String> ids=new ArrayList<>();
                        for(ShgcDto shgcDto:shgcList){
                            ids.add(shgcDto.getYwid());
                        }
                        SjkdxxDto sjkdxxDto=new SjkdxxDto();
                        sjkdxxDto.setIds(ids);
                        sjkdxxDto.setYwlx(EntityTypeEnum.ENTITY_SJZZSQDTO.getCode());
                        List<SjkdxxDto> list = sjkdxxService.getDtoList(sjkdxxDto);
                        if(list!=null&&list.size()>0){
                            String s = sjkdxxService.generateMailno();
                            if("JD".equals(list.get(0).getKdlxdm())){
//                                String JDInfo = jingDongUtil.receiveOrderInfo(sjzzsqDto_t.getSjkdid(),sjzzsqDto_t.getSjr(),sjzzsqDto_t.getDz(),sjzzsqDto_t.getDh());
                                String JDInfo = jingDongUtil.receiveOrderInfo(s,sjzzsqDto_t.getSjr(),sjzzsqDto_t.getDz(),sjzzsqDto_t.getDh(),sjzzsqDto_t.getJdlx());
                                logger.error("--京东物流下单接口返回信息--："+JDInfo);
                                JSONObject jsonObject = JSONObject.parseObject(JDInfo);
                        
                            	//throw new BusinessException("msg","京东物流下单失败！");
						 		JSONObject msg = jsonObject.getJSONObject("result");
                                String deliveryId = msg.getString("deliveryId");
                                if(StringUtil.isNotBlank(deliveryId)){
                                    for(SjkdxxDto dto:list){
                                        dto.setMailno(deliveryId);
                                        dto.setKdzt("0");
                                        dto.setSjmailno(s);
                                        dto.setSfqx("0");
                                    }
                                }else{
                                    throw new BusinessException("msg","京东物流下单失败！接口返回信息:"+JDInfo);
                                }
                            }else if("SF".equals(list.get(0).getKdlxdm())){
                                try {
//                                    SFInfo=shunFengUtil.SfCreateOrder(sjzzsqDto_t.getSjkdid(),sjzzsqDto_t.getSjr(),sjzzsqDto_t.getDz(),sjzzsqDto_t.getDh());
                                    String SFInfo=shunFengUtil.SfCreateOrder(s,sjzzsqDto_t.getSjr(),sjzzsqDto_t.getDz(),sjzzsqDto_t.getDh());
                                    logger.error("--顺丰物流下单接口返回信息--："+SFInfo);
                                    JSONObject jsonObject = JSONObject.parseObject(SFInfo);
                                    JSONObject apiResultData = jsonObject.getJSONObject("apiResultData");
                                    JSONObject msgData = apiResultData.getJSONObject("msgData");
                                    JSONArray routeLabelInfo = msgData.getJSONArray("routeLabelInfo");
                                    JSONObject jsonObject_t = routeLabelInfo.getJSONObject(0);
                                    JSONObject routeLabelData = jsonObject_t.getJSONObject("routeLabelData");
                                    String SFid = routeLabelData.getString("waybillNo");

                                	//throw new BusinessException("msg","顺丰物流下单失败！");
                                 	if(StringUtil.isNotBlank(SFid)){
                                        for(SjkdxxDto dto:list){
                                            dto.setMailno(SFid);
                                            dto.setKdzt("0");
                                            dto.setSjmailno(s);
                                            dto.setSfqx("0");
                                        }
                                    }else{
                                        throw new BusinessException("msg","顺丰物流下单失败！接口返回信息:"+SFInfo);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            sjkdxxService.updateList(list);
                        }

                        if (spgwcyDtos != null && spgwcyDtos.size() > 0) {
                            int size = spgwcyDtos.size();
                            for (int i = 0; i < size; i++) {
                                if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                                    talkUtil.sendWorkDyxxMessage(shgcList.get(j).getShlb(),spgwcyDtos.get(i).getYhid(),spgwcyDtos.get(i).getYhm(), spgwcyDtos.get(i).getYhid(),
                                            xxglService.getMsg("ICOMM_SH00006"),xxglService.getMsg("ICOMM_SH00016",
                                                    operator.getZsxm(),shgcList.get(j).getShlbmc() ,sjzzsqDto_t.getYbbh(),
                                                    DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                                }
                            }
                        }
                    }
                }
            }else {
                sjzzsqDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                // 发送钉钉消息
                if (spgwcyDtos != null && spgwcyDtos.size() > 0) {
                    try {
                        int size = spgwcyDtos.size();
                        for (int i = 0; i < size; i++) {
                            if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                                //小程序访问
                                String internalbtn="page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/freeList/applyChecksh/applyChecksh",StandardCharsets.UTF_8);
//                                +URLEncoder.encode("?type=1&ywzd=zzsqid&ywid="+
//                                        sjzzsqDto_t.getZzsqid()+"&auditType="+ AuditTypeEnum.AUDIT_PAPERREPORTAPPLY.getCode()+"&urlPrefix="+urlPrefix+"&userid="+user_t.getDdid()+"&username="+user_t.getZsxm())
                                List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                                OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                                btnJsonList.setTitle("小程序");
                                btnJsonList.setActionUrl(internalbtn);
                                btnJsonLists.add(btnJsonList);
                                talkUtil.sendCardDyxxMessage(shgcList.get(j).getShlb(),shgcList.get(j).getSpgwcyDtos().get(i).getYhid(),shgcList.get(j).getSpgwcyDtos().get(i).getYhm(),
                                		shgcList.get(j).getSpgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00003"),StringUtil.replaceMsg(xxglService.getMsg("ICOMM_ZZSQ00001"),
                                        operator.getZsxm(), shgcList.get(j).getShlbmc(),sjzzsqDto_t.getHzxm(),sjzzsqDto_t.getYbbh(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists,"1");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //发送钉钉消息--取消审核人员
                if(shgcList.get(j).getNo_spgwcyDtos() != null && shgcList.get(j).getNo_spgwcyDtos().size() > 0){
                    int size = shgcList.get(j).getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcList.get(j).getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcList.get(j).getShlb(),shgcList.get(j).getNo_spgwcyDtos().get(i).getYhid(),shgcList.get(j).getNo_spgwcyDtos().get(i).getYhm(),
                            		shgcList.get(j).getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcList.get(j).getShlbmc() ,sjzzsqDto_t.getYbbh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            dao.update(sjzzsqDto);
        }
        return true;
    }

    @Override
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        // TODO Auto-generated method stub
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                SjzzsqDto sjzzsqDto = new SjzzsqDto();
                sjzzsqDto.setZzsqid(shgcDto.getYwid());
                sjzzsqDto.setXgry(operator.getYhid());
                sjzzsqDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(sjzzsqDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                SjzzsqDto sjzzsqDto = new SjzzsqDto();
                sjzzsqDto.setZzsqid(shgcDto.getYwid());
                sjzzsqDto.setXgry(operator.getYhid());
                sjzzsqDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(sjzzsqDto);
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
        SjzzsqDto sjzzsqDto = new SjzzsqDto();
        sjzzsqDto.setIds(ids);
        List<SjzzsqDto> dtoList = dao.getDtoList(sjzzsqDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(SjzzsqDto dto:dtoList){
                list.add(dto.getZzsqid());
            }
        }
        map.put("list",list);
        return map;
    }

    /**
     * 获取电话全部数据
     * @param shgcList
     * @return
     */
    public String getDhByYwids(List<ShgcDto> shgcList){
        return dao.getDhByYwids(shgcList);
    }

    /**
     * 取消锁定
     * @param sjzzsqDto
     * @return
     */
    public boolean cancelLock(SjzzsqDto sjzzsqDto){
        return dao.cancelLock(sjzzsqDto);
    }

    /**
     * 获取所选数据包号
     * @param sjzzsqDto
     * @return
     */
    public String getBhsByIds(SjzzsqDto sjzzsqDto){
        return dao.getBhsByIds(sjzzsqDto);
    }

    private void queryJoinFlagExport(Map<String, Object> params, SjzzsqDto sjzzsqDto)
    {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuffer sqlParam = new StringBuffer();
        for (DcszDto dcszDto : choseList)
        {
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;
            if(dcszDto.getDczd().equalsIgnoreCase("YBBH") || dcszDto.getDczd().equalsIgnoreCase("NBBM")|| dcszDto.getDczd().equalsIgnoreCase("HZXM")) {
                sjzzsqDto.setSjxx_flg("Y");
            }


            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd()))
            {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        sjzzsqDto.setSqlParam(sqlParam.toString());
    }


    /**
     * 自动生成包号
     * @return
     */
    public String generateBh(User user) {
        String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
        String prefix = "-" + user.getYhm() + "-" + date;
        // 查询流水号
        String serial = dao.getBhSerial(prefix);
        return serial+prefix;
    }

    /**
     * 打包
     * @param sjzzsqDto
     * @return
     */
    public boolean pack(SjzzsqDto sjzzsqDto){
        return dao.pack(sjzzsqDto);
    }
    /**
     * 打包
     * @param sjzzsqDto
     * @return
     */
    public boolean cancelPack(SjzzsqDto sjzzsqDto){
        return dao.cancelPack(sjzzsqDto);
    }

    @Override
    public boolean updateZtByIds(SjzzsqDto sjzzsqDto) {
        int num = dao.updateZtByIds(sjzzsqDto);
        return num > 0;
    }

    /**
     * 选中导出
     * @param params
     * @return
     */
    public List<SjzzsqDto> getListForSelectExp(Map<String, Object> params){
        SjzzsqDto sjzzsqDto = (SjzzsqDto) params.get("entryData");
        queryJoinFlagExport(params,sjzzsqDto);
        return dao.getListForSelectExp(sjzzsqDto);
    }

    /**
     * 根据搜索条件获取导出条数
     * @param sjzzsqDto
     * @return
     */
    public int getCountForSearchExp(SjzzsqDto sjzzsqDto,Map<String,Object> params){
        return dao.getCountForSearchExp(sjzzsqDto);
    }

    /**
     * 根据搜索条件分页获取导出信息
     * @param params
     * @return
     */
    public List<SjzzsqDto> getListForSearchExp(Map<String,Object> params){
        SjzzsqDto sjzzsqDto = (SjzzsqDto)params.get("entryData");
        queryJoinFlagExport(params,sjzzsqDto);
        return dao.getListForSearchExp(sjzzsqDto);
    }
    /**
     * 获取送检报告列表
     * @param sjzzsqDto
     * @return
     */
    public List<SjzzsqDto> getPageReportList(SjzzsqDto sjzzsqDto){
        return dao.getPageReportList(sjzzsqDto);
    }

    /**
     * 通过sjid获取纸质申请数据
     * @param sjzzsqDto
     * @return
     */
    @Override
    public List<SjzzsqDto> getSjzzsqBySjid(SjzzsqDto sjzzsqDto) {
        return dao.getSjzzsqBySjid(sjzzsqDto);
    }
}

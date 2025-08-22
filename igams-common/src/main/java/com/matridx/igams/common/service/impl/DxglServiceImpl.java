package com.matridx.igams.common.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.matridx.igams.common.dao.entities.DxglDto;
import com.matridx.igams.common.dao.entities.DxglModel;
import com.matridx.igams.common.dao.post.IDxglDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDxglService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class DxglServiceImpl extends BaseBasicServiceImpl<DxglDto, DxglModel, IDxglDao> implements IDxglService{

    @Autowired
    IXxglService xxglService;
    @Value("${matridx.aliyunSms.accessKeyId:}")
    private String accessKeyId;
    @Value("${matridx.aliyunSms.accessSecret:}")
    private String accessSecret;
    final Logger logger = LoggerFactory.getLogger(DxglServiceImpl.class);

    /**
     * 发送手机短信(验证码)
     */
    @SuppressWarnings("deprecation")
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean sendSms(String phoneNumbers, String signName, String templateCode, String TemplateParam) {

        // TODO Auto-generated method stub
        DBEncrypt crypt = new DBEncrypt();
        DefaultProfile profile = DefaultProfile.getProfile("RegionId", crypt.dCode(accessKeyId), crypt.dCode(accessSecret));
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //无需替换
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //必填:短信签名-可在短信控制台中找到，你在签名管理里的内容
        request.putQueryParameter("SignName", signName);
        //必填:待发送手机号
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        //必填:短信模板-可在短信控制台中找到，你模板管理里的模板编号
        request.putQueryParameter("TemplateCode", templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"您的验证码为${code}"时,此处的值为
        request.putQueryParameter("TemplateParam", "{\"code\":\""+TemplateParam+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 发送手机短信
     * @param templateParam 组装后的消息 例：{\"zsxm\":\"张三\",\"yyrq\":\"2021-12-16\"}
     */
    @SuppressWarnings("deprecation")
	public boolean sendSmsMsg(String phoneNumbers, String signName, String templateCode, String templateParam) {
        // TODO Auto-generated method stub
        DBEncrypt crypt = new DBEncrypt();
        DefaultProfile profile = DefaultProfile.getProfile("RegionId", crypt.dCode(accessKeyId), crypt.dCode(accessSecret));
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //无需替换
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //必填:短信签名-可在短信控制台中找到，你在签名管理里的内容
        request.putQueryParameter("SignName", signName);
        //必填:待发送手机号
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        //必填:短信模板-可在短信控制台中找到，你模板管理里的模板编号
        request.putQueryParameter("TemplateCode", templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"您的验证码为${code}"时,此处的值为
        request.putQueryParameter("TemplateParam", templateParam);
        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.error(response.getData());
        } catch (ClientException e) {
            logger.error(e.toString());
        }
        return true;
    }

    /**
     * 根据手机号获取当天短信管理信息
     */
    public DxglDto getDxglBySjhToday(DxglDto dxglDto){
        return dao.getDxglBySjhToday(dxglDto);
    }

    /**
     * 根据手机号获取所有短信管理信息
     */
    public DxglDto getDxglBySjh(DxglDto dxglDto){
        return dao.getDxglBySjh(dxglDto);
    }

    /**
     * 发送短信验证码（调用发送短信验证码接口）
     * @param sjh 手机号
     * @param yzm 验证码
     * @param signName 配置文件 signName
     * @param templateCode 配置文件 templateCode
     * @param time 发送间隔时间 单位秒
     * @param fscs 限制每天发送次数
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public Map<String,Object> sendCode(String sjh, String yzm, String signName, String templateCode,String time,String fscs){
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess;
        if (!checkPhone(sjh)){
            map.put("status","fail");
            map.put("message","手机号错误，发送失败！");
            return map;
        }
        // TODO Auto-generated method stub
        boolean sendOrNot = true;
        DBEncrypt dbEncrypt = new DBEncrypt();
        DxglDto dxglDto = new DxglDto();
        dxglDto.setSjh(dbEncrypt.eCode(sjh));
        Date date = new Date();
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //根据手机号查询当日发送情况
        dxglDto = getDxglBySjhToday(dxglDto);
        //若当日有发送，判断发送间隔是否超过time
        if (dxglDto!=null){
            //获取当前时间与上次发送时间的差，
            Date dateFssj = new Date();
            try {
                dateFssj=simpleDateFormat.parse(dxglDto.getFssj());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar caFssj = Calendar.getInstance();
            Calendar caNow = Calendar.getInstance();
            caFssj.setTime(dateFssj);
            caNow.setTime(date);
            long distanceMin =( caNow.getTimeInMillis()- caFssj.getTimeInMillis())/(1000);
            if (Long.parseLong(time) >distanceMin){
                sendOrNot = false;
                map.put("status","fail");
                map.put("message","发送时间间隔过短，请稍后发送！");
            }else if (fscs.equals(dxglDto.getFscs())){
                //若 当天发送次数超过限定发送次数，则不发送
                sendOrNot = false;
                map.put("status","fail");
                map.put("message","当前手机已超过发送次数！");
            }
        }else {
            //若无当天的发送数据，设发送次数为0
            dxglDto = new DxglDto();
            dxglDto.setFscs("0");
        }
        if (sendOrNot){
            logger.error("当前发送的验证码为："+yzm);
            isSuccess = sendSms(sjh, signName, templateCode, yzm);
            if (isSuccess){
                dxglDto.setYzm(yzm);
                dxglDto.setFscs(new BigDecimal(dxglDto.getFscs()).add(new BigDecimal("1")).toString());
                dxglDto.setSjh(dbEncrypt.eCode(sjh));
                DxglDto HaveDxglOrNot = getDxglBySjh(dxglDto);
                if (HaveDxglOrNot!=null){
                    //若该手机号存在，则更新
                    isSuccess = updateDxgl(dxglDto);
                }else {
                    //若手机号不存在，新增
                    isSuccess = insertDxgl(dxglDto);
                }
            }
            map.put("status",isSuccess?"success":"fail");
            map.put("message",isSuccess?"发送成功！":"发送失败！");
        }
        return map;
    }

    /**
     * 检查验证码
     * @param sjh 手机号
     * @param yzm 验证码
     * @param time 验证码过期时间
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public Map<String,Object> checkCode(String sjh, String yzm, String time){
        Map<String, Object> map = new HashMap<>();
        DBEncrypt dbEncrypt = new DBEncrypt();
        DxglDto dxglDto = new DxglDto();
        dxglDto.setSjh(dbEncrypt.eCode(sjh));
        dxglDto = getDxglBySjh(dxglDto);
        //获取当前时间与上次发送时间的差，
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Date dateFssj = new Date();
        try {
            dateFssj=simpleDateFormat.parse(dxglDto.getFssj());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar caFssj = Calendar.getInstance();
        Calendar caNow = Calendar.getInstance();
        caFssj.setTime(dateFssj);
        caNow.setTime(date);
        long distanceMin =( caNow.getTimeInMillis()- caFssj.getTimeInMillis())/(1000);
        if (Long.parseLong(time) <distanceMin){
            map.put("status","fail");
            map.put("message","验证码已过期，请重新获取！");
        }else {
            if (StringUtil.isNotBlank(yzm) && yzm.equals(dxglDto.getYzm())){
                map.put("status","success");
                map.put("message","验证码正确！");
            }else {
                map.put("status","fail");
                map.put("message","验证码错误！");
            }
        }
        return map;
    }

    /**
     * 更新短信管理信息
     */
    public boolean updateDxgl(DxglDto dxglDto){
        return dao.updateDxgl(dxglDto);
    }

    /**
     * 新增短信管理信息
     */
    public boolean insertDxgl(DxglDto dxglDto){
        return dao.insertDxgl(dxglDto);
    }

    /**
     * 检查手机号码是否正确
     */
    public static boolean checkPhone(String phone){
        Pattern p = Pattern.compile("^(?:(?:0\\d{2,3}-\\d{7,8})|\\d{7,8}|1\\d{10})$");
        return p.matcher(phone).matches();
    }

    /**
     * 发送短信消息（调用发送短信预约信息接口）
     * @param sjh 手机号
     * @param templateParam 消息内容Map
     * @param signName 配置文件 signName
     * @param templateCode 配置文件 templateCode
     */
    public Map<String,Object> sendMsgDetection(String sjh,String templateParam, String signName, String templateCode){
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = false;
        // TODO Auto-generated method stub
        String phoneNum;
        try {
            DBEncrypt dbEncrypt = new DBEncrypt();
            phoneNum = dbEncrypt.dCode(sjh);
            logger.error("发送手机号："+phoneNum+" 发送templateParam："+templateParam);
            isSuccess = sendSmsMsg(phoneNum, signName, templateCode, templateParam);
        } catch (Exception e) {
            logger.error("手机号解密失败！"+ e);
        }
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?"发送成功！":"发送失败！");
        return map;
    }

    /**
     * 组装templateParam
     */
    public String packagingTemplateParam(List<Map<String,String>> msgList){
        StringBuilder templateParam = new StringBuilder();
        if (msgList!=null&&!msgList.isEmpty()){
            for (Map<String, String> stringStringMap : msgList) {
                templateParam.append(",\"").append(stringStringMap.get("key")).append("\":\"").append(stringStringMap.get("value")).append("\"");
            }
        }
        if (templateParam.length()>0){
            templateParam = new StringBuilder("{" + templateParam.substring(1) + "}");
        }else {
            templateParam = new StringBuilder("{" + templateParam + "}");
        }
        return templateParam.toString();
    }

    /**
     * 发送短信消息（替换消息内容调用发送短信接口）
     * @param phoneNum 手机号
     * @param msgMap 消息内容
     * @param signName 配置文件 signName
     * @param templateCode 配置文件 templateCode
     */
    public Map<String,Object> sendReplaceMsg(String phoneNum,String msgId, Map<String,Object> msgMap, String signName, String templateCode){
        String templateParam = xxglService.getReplaceMsg(msgId, msgMap);
        return sendMsgDetection(phoneNum, templateParam, signName, templateCode);
    }

}

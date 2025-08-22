package com.matridx.igams.experiment.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.post.IXtszDao;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.util.ImgQrCodeUtil;
import com.matridx.igams.experiment.dao.entities.ZdhYhxxDto;
import com.matridx.igams.experiment.dao.entities.ZdhYhxxModel;
import com.matridx.igams.experiment.dao.post.IZdhYhxxDao;
import com.matridx.igams.experiment.service.svcinterface.IZdhYhxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ZdhYhxxServiceImpl extends BaseBasicServiceImpl<ZdhYhxxDto, ZdhYhxxModel, IZdhYhxxDao> implements IZdhYhxxService, IAuditService {

    private final Logger log = LoggerFactory.getLogger(ZdhYhxxServiceImpl.class);

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IXtszDao xtszDao;
    @Autowired
    ICommonService commonService;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.wechat.menuurl:}")
    private String menuurl;
    @Value("${matridx.fileupload.tempPath}")
    private String tempFilePath;
    @Value("${matridx.loginCentre.url:}")
    private String loginCentreUrl;


    /**
     * @param qRCodeSettingId 基础数据ID
     * @return String 二维码本地路径
     */
    @Override
    public String qrCodeGenerate(String qRCodeSettingId) {
        if (StringUtil.isBlank(qRCodeSettingId)) {
            return null;
        }
        JcsjDto jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.QRCODE_SETTING.getCode(), qRCodeSettingId);
        if (StringUtil.isBlank(jcsjDto.getCskz2())) {
            return null;
        }
        Map<String, Object> map = JSON.parseObject(jcsjDto.getCskz2());
        if (map == null) {
            return null;
        }
        //设置二维码内容
        Object o_jumpUrl = map.get("JumpUrl");
        String qrCodeContent = applicationurl;
        if (o_jumpUrl == null) {
            qrCodeContent += "/ws/qrCode/qrCodeAnalysis?id=";
        } else {
            qrCodeContent += o_jumpUrl.toString();
        }
        qrCodeContent += qRCodeSettingId;
        //设置二维码尺寸
        int qrCodeSize;
        Object o_QRCodeSize = map.get("QRCodeSize");
        if (o_QRCodeSize != null) {
            qrCodeSize = Integer.parseInt(o_QRCodeSize.toString());
        } else {
            qrCodeSize = 200;
        }
        //设置二维码Logo
        String logoPath = null;

        Object o_LogoPath = map.get("LogoPath");
        if (o_LogoPath != null) {
            logoPath = o_LogoPath.toString();
        } else {
            FjcfbDto fjcfbDto = new FjcfbDto();
            fjcfbDto.setYwid(qRCodeSettingId);
            fjcfbDto.setYwlx(BusTypeEnum.IMP_QRCODE_LOGO.getCode());
            List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
            if (!CollectionUtils.isEmpty(fjcfbDtos)) {
                DBEncrypt dbEncrypt = new DBEncrypt();
                logoPath = dbEncrypt.dCode(fjcfbDtos.get(0).getWjlj());
            }
        }
        String qrCodePath = mkDirs(BusTypeEnum.IMP_QRCODE_IMG.getCode(), tempFilePath);
        ImgQrCodeUtil imgQrCodeUtil = new ImgQrCodeUtil();
        try {
            String result = imgQrCodeUtil.creatQrcodeImg(qrCodeContent, logoPath, null, qrCodePath, qrCodeSize, qrCodeSize, null, null, Color.BLUE);
            log.error("generateQRCode-result:" + result);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 解析二维码
     *
     * @param wechatid
     * @param qRCodeSettingId
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Map<String, Object> qrCodeAnalysis(String qRCodeSettingId, String wechatid, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtil.isNotBlank(wechatid)) {
            // 1、存在微信ID
            // 判断是否存在绑定账户
            // 若存在，则跳转至绑定账户页面
            // 若不存在，则跳转提示绑定账户页面（绑定页面所需扫码功能参数须在85端）
            //查询相关账号状态
            ZdhYhxxDto dto = new ZdhYhxxDto();
            dto.setWechatid(wechatid);
            ZdhYhxxDto zdhYhxxDto = getDto(dto);
            if (zdhYhxxDto != null) {
                if ("80".equals(zdhYhxxDto.getZt())) {
                    //若账号存在，且审核通过
                    result.put("message", "正在跳转实验室界面中...");
                    result.put("status", "success");
                    // 跳转指定页面

                    JcsjDto jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.QRCODE_SETTING.getCode(), qRCodeSettingId);
                    if (StringUtil.isBlank(jcsjDto.getCskz1())) {
                        return null;
                    }
                    Map<String, Object> cskz1map = JSON.parseObject(jcsjDto.getCskz1());
                    if (cskz1map == null || cskz1map.get("AutomationLaboratoryUrl") == null) {
                        return null;
                    }
                    String redirectUrl = cskz1map.get("AutomationLaboratoryUrl").toString();
                    redirectUrl += "?wechatid=" + wechatid;
                    if(StringUtil.isNotBlank(zdhYhxxDto.getYhm()) && StringUtil.isNotBlank(zdhYhxxDto.getMm())){
                        redirectUrl += "&yhm=" + zdhYhxxDto.getYhm() + "&mm=" + zdhYhxxDto.getMm();
                    }
                    result.put("redirectUrl", redirectUrl);
                    return result;
                } else if ("15".equals(zdhYhxxDto.getZt())) {
                    //若账号存在，且审核不通过
                    result.put("message", "账号\n【" + wechatid + "】\n审核未通过！\n请联系管理员！");
                    result.put("status", "warn");
                    return result;
                } else {
                    //若账号存在，且正在审核中
                    result.put("message", "账号\n【" + wechatid + "】\n已提交申请...\n请联系管理员进行审核！");;
                    result.put("status", "waiting");
                    return result;
                }
            } else if (StringUtil.isNotBlank(qRCodeSettingId)) {
                //若账号不存在，创建账号并提交审核
                dto.setYhid(StringUtil.generateUUID());
                dto.setZt("10");
                dto.setJcsjid(qRCodeSettingId);
                dto.setScbj("0");
                insertDto(dto);
                //提交审批流程
                ShgcDto shgcDto = new ShgcDto();
                shgcDto.setExtend_1(JSONObject.toJSONString(new String[]{dto.getYhid()}));
                shgcDto.setShlb("AUDIT_ZDHYHHX");
                User user = new User();
                user.setYhid(dto.getYhid());
                user.setZsxm(dto.getWechatid());
                String message = "账号\n【" + wechatid + "】\n正在审核中...\n请联系管理员！";
                try {
                    shgcService.checkAndCommit(shgcDto, user);
                } catch (BusinessException e) {
                    log.error(e.toString());
                    message += e.getMessage();
                } catch (Exception e) {
                    log.error("commCommitAudit(ShgcDto shgcDto): Ywids= " + (shgcDto.getYwids() != null ? shgcDto.getYwids().toString() : "") + " Extend_1= " + JSON.toJSONString(shgcDto.getExtend_1()));
                    log.error(e.toString());
                    message += e.getMessage();
                }
                result.put("message", message);
                result.put("status", "waiting");
                return result;
            } else {
                result.put("message", "账号\n【" + wechatid + "】\n还未申请相应实验室\n请扫码申请！");;
                result.put("status", "waiting");
                return result;
            }
        } else if (StringUtil.isNotBlank(qRCodeSettingId)) {
            //2、不存在微信ID，但存在qRCodeSettingId(基础数据csid)
            String redirectUrl = dealWechaidNotExist(qRCodeSettingId);
            result.put("redirectUrl", redirectUrl);
            return result;
        }
        return result;
    }

    /**
     * 处理wechatid未传的情况
     *
     * @param qRCodeSettingId
     * @return
     */
    private String dealWechaidNotExist(String qRCodeSettingId) {
        JcsjDto jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.QRCODE_SETTING.getCode(), qRCodeSettingId);
        if (StringUtil.isBlank(jcsjDto.getCskz1())) {
            return null;
        }
        Map<String, Object> cskz1map = JSON.parseObject(jcsjDto.getCskz1());
        if (cskz1map == null || cskz1map.get("WechatInfoGetUrl") == null) {
            return null;
        }
        /*
         * jcsjDto.getCskz1()
         * {
         *  "WechatInfoGetUrl":"/wechat/getWechatId",           //获取微信ID的接口（85端）
         *  "WechatMPCode":"MDINSPECT",                         //获取微信ID的公众号代码
         *  "WechatInfoGetBackUrl":"/ws/qrCode/qrCodeAnalysis"  //获取微信用户信息后，回调的地址（86端）
         * }
         *
         * 拼接后的wechatInfoGetUrl = {WechatInfoGetUrl}?wechatInfoGetBackUrl={WechatInfoGetBackUrl}&wechatMPCode={WechatMPCode}&id={qRCodeSettingId}
         */
        //获取appid
        //85端获取微信用户信息地址
        String wechatInfoGetUrl = menuurl + (cskz1map.get("WechatInfoGetUrl") == null ? "/wechat/getWechatId" : cskz1map.get("WechatInfoGetUrl").toString());
        //获取微信用户信息后，回调86端的地址
        String wechatInfoGetBackUrl = applicationurl + (cskz1map.get("WechatInfoGetBackUrl") == null ? "/ws/qrCode/qrCodeAnalysis" : cskz1map.get("WechatInfoGetBackUrl").toString());
        wechatInfoGetUrl = wechatInfoGetUrl + "?wechatInfoGetBackUrl=" + URLEncoder.encode(wechatInfoGetBackUrl);
        if (cskz1map.get("WechatMPCode") != null) {
            wechatInfoGetUrl += ("&wechatMPCode=" + cskz1map.get("WechatMPCode").toString());
        }
        wechatInfoGetUrl += ("&id=" + qRCodeSettingId);
        //拼接跳转地址
        return wechatInfoGetUrl;
    }

    /**
     * 根据路径创建文件
     *
     * @param ywlx
     * @param realFilePath
     * @return
     */
    protected String mkDirs(String ywlx, String realFilePath) {
        String storePath;
        if (ywlx != null) {
            //根据日期创建文件夹
            storePath = realFilePath + ywlx + "/" + "UP" +
                    DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" +
                    DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/" + "UP" +
                    DateUtils.getCustomFomratCurrentDate("yyyyMMdd");

        } else {
            storePath = realFilePath;
        }

        File file = new File(storePath);
        if (file.isDirectory()) {
            return storePath;
        }
        if (file.mkdirs()) {
            return storePath;
        }
        return null;
    }


    @Override
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        return true;
    }

    @Override
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        for (ShgcDto shgcDto : shgcList) {
            ZdhYhxxDto zdhYhxxDto = new ZdhYhxxDto();
            zdhYhxxDto.setYhid(shgcDto.getYwid());
            zdhYhxxDto.setXgry(operator.getYhid());
            ZdhYhxxDto zdhYhxxDto_t = getDto(zdhYhxxDto);
//            List<SpgwcyDto> spgwcyDtos = shgcDto.getSpgwcyDtos();
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                // 审核退回
                zdhYhxxDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                updateDto(zdhYhxxDto);
            }else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                // 审核通过
                zdhYhxxDto_t.setZt(StatusEnum.CHECK_PASS.getCode());
                updateDto(zdhYhxxDto_t);
            } else {
                // 审核提交
                zdhYhxxDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                updateDto(zdhYhxxDto);
            }
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
                String yhid = shgcDto.getYwid();
                ZdhYhxxDto zdhYhxxDto = new ZdhYhxxDto();
                zdhYhxxDto.setYhid(yhid);
                zdhYhxxDto.setXgry(operator.getYhid());
                zdhYhxxDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                updateDto(zdhYhxxDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String yhid = shgcDto.getYwid();
                ZdhYhxxDto zdhYhxxDto = new ZdhYhxxDto();
                zdhYhxxDto.setYhid(yhid);
                zdhYhxxDto.setXgry(operator.getYhid());
                zdhYhxxDto.setZt(StatusEnum.CHECK_NO.getCode());
                updateDto(zdhYhxxDto);
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
        @SuppressWarnings("unchecked")
        List<String> ids = (List<String>)param.get("ywids");
        map.put("list",ids);
        return map;
    }
}

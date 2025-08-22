package com.matridx.igams.wechat.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DdAuditTypeEnum;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum_pub;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdfbsglService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.wechat.dao.entities.HbsfbzDto;
import com.matridx.igams.wechat.dao.entities.HtyhzcDto;
import com.matridx.igams.wechat.dao.entities.KhhtglDto;
import com.matridx.igams.wechat.dao.entities.KhyhzcbzDto;
import com.matridx.igams.wechat.dao.entities.SjjcxmDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SwhtglDto;
import com.matridx.igams.wechat.dao.entities.SwhtglModel;
import com.matridx.igams.wechat.dao.entities.SwhtmxDto;
import com.matridx.igams.wechat.dao.entities.SwkhglDto;
import com.matridx.igams.wechat.dao.entities.SwyszkDto;
import com.matridx.igams.wechat.dao.post.ISwhtglDao;
import com.matridx.igams.wechat.service.svcinterface.IHbsfbzService;
import com.matridx.igams.wechat.service.svcinterface.IHtyhzcService;
import com.matridx.igams.wechat.service.svcinterface.IKhhtglService;
import com.matridx.igams.wechat.service.svcinterface.IKhyhzcbzService;
import com.matridx.igams.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISwhtglService;
import com.matridx.igams.wechat.service.svcinterface.ISwhtmxService;
import com.matridx.igams.wechat.service.svcinterface.ISwkhglService;
import com.matridx.igams.wechat.service.svcinterface.ISwyszkService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.lang3.StringUtils;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商务合同管理(IgamsSwhtgl)表服务实现类
 *
 * @author makejava
 * @since 2023-04-04 13:52:16
 */
@Service
public class SwhtglServiceImpl extends BaseBasicServiceImpl<SwhtglDto, SwhtglModel, ISwhtglDao> implements ISwhtglService, IAuditService {
    @Autowired
    private ISwhtmxService swhtmxService;
    @Autowired
    private IKhhtglService khhtglService;
    @Autowired
    private ISwkhglService swkhglService;
    @Autowired(required=false)
    private AmqpTemplate amqpTempl;
    @Autowired
    IDdspglService ddspglService;
    @Autowired
    ICommonService commonService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IShxxService shxxService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IDdfbsglService ddfbsglService;
    @Autowired
    IHbsfbzService hbsfbzService;
    @Autowired
    DingTalkUtil talkUtil;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.wechat.registerurl:}")
    private String registerurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    ICommonService commonservice;
    @Autowired
    private IXtszService xtszService;
    @Autowired
    IDdxxglService ddxxglService;
    @Autowired
    ISwyszkService swyszkService;
	@Autowired
    private RedisUtil redisUtil;
    @Autowired
    ISjjcxmService sjjcxmService;
    @Autowired
    ISjxxService sjxxService;
    @Autowired
    IHtyhzcService htyhzcService;
    @Autowired
    IKhyhzcbzService khyhzcbzService;
    /**
     * 文档转换完成OK
     */
    @Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
    private String DOC_OK = null;


    private Logger log = LoggerFactory.getLogger(SwhtglServiceImpl.class);

    @Override
    public List<SwhtglDto> getRepeatDtoList(SwhtglDto swhtglDto) {
        return dao.getRepeatDtoList(swhtglDto);
    }

    /**
     * 根据前缀生成合同编号
     */
    public String getContractNumber(String cskz){
        String date = DateUtils.getCustomFomratCurrentDate("yyMMdd");
        String prefix = cskz + "-B-" + date + "-";
        // 查询流水号
        String serial = dao.getContractNumber(prefix);
        return prefix+serial;
    }

    @Override
    public List<SwhtglDto> getPagedAuditList(SwhtglDto swhtglDto) {
        List<SwhtglDto> list = dao.getPagedAuditList(swhtglDto);
        if (CollectionUtils.isEmpty(list))
            return list;
        List<SwhtglDto> sqList = dao.getAuditListRecheck(list);
        commonservice.setSqrxm(sqList);
        return sqList;
    }

    @Override
    public List<SwhtglDto> getPagedDtoList(SwhtglDto swhtglDto) {
        List<SwhtglDto> list = dao.getPagedDtoList(swhtglDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_BUSINESS_CONTRACT.getCode(), "zt", "htid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
    /**
     * 根据客户获取合同信息
     * @param
     * @return
     */
    @Override
    public List<SwhtglDto> getAllInfo(SwhtglDto swhtglDto){
        List<SwhtglDto> dtoList = dao.getKhhtglInfo(swhtglDto);
        if (!CollectionUtils.isEmpty(dtoList)){
            SwhtmxDto swhtmxDto = new SwhtmxDto();
            swhtmxDto.setKhid(swhtglDto.getQddx());
            List<SwhtmxDto> list = swhtmxService.getAllList(swhtmxDto);
            List<String> strings = new ArrayList<>();
            for (SwhtglDto dto : dtoList) {
                strings.add(dto.getHtid());
            }

            FjcfbDto fjcfbDto = new FjcfbDto();
            fjcfbDto.setYwids(strings);
            fjcfbDto.setYwlx(BusTypeEnum.IMP_CHAPTER_CONTRACT.getCode());
            List<FjcfbDto> fjcfbDtoList = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto);
            for (SwhtglDto dto : dtoList) {
                List<SwhtmxDto> dtoList1 = new ArrayList<>();
                List<FjcfbDto> dtoList2 = new ArrayList<>();
                dto.setHtmxList(dtoList1);
                dto.setFjcfbDtos(dtoList2);
                if (!CollectionUtils.isEmpty(list)){
                    for (int i = list.size()-1; i >=0 ; i--) {
                        if (dto.getHtid().equals(list.get(i).getHtid())){
                            dto.getHtmxList().add(list.get(i));
                            list.remove(i);
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(fjcfbDtoList)){
                    for (int i = fjcfbDtoList.size()-1; i >=0 ; i--) {
                        if (dto.getHtid().equals(fjcfbDtoList.get(i).getYwid())){
                            dto.getFjcfbDtos().add(fjcfbDtoList.get(i));
                            fjcfbDtoList.remove(i);
                        }
                    }
                }
            }
        }
        return dtoList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean insertInfo(SwhtglDto swhtglDto) throws BusinessException {
        Boolean success = dao.insert(swhtglDto)!=0;
        if (!success)
            throw new BusinessException("msg","保存主表信息失败！");
        if (!CollectionUtils.isEmpty(swhtglDto.getKhids())){
            List<KhhtglDto> list = new ArrayList<>();
            for (String khid : swhtglDto.getKhids()) {
                KhhtglDto khhtglDto = new KhhtglDto();
                khhtglDto.setHtid(swhtglDto.getHtid());
                khhtglDto.setKhid(khid);
                list.add(khhtglDto);
            }
            success = khhtglService.insertList(list);
            if (!success)
                throw new BusinessException("msg","保存客户合同信息失败！");
        }else{
            KhhtglDto khhtglDto = new KhhtglDto();
            khhtglDto.setHtid(swhtglDto.getHtid());
            khhtglDto.setKhid(swhtglDto.getQddx());
            success = khhtglService.insert(khhtglDto);
            if (!success)
                throw new BusinessException("msg","保存客户合同信息失败！");
        }
	  	if (StringUtil.isNotBlank(swhtglDto.getHkzq())){
            SwkhglDto swkhglDto = new SwkhglDto();
            swkhglDto.setKhid(swhtglDto.getQddx());
            swkhglDto.setHkzq(swhtglDto.getHkzq());
            success = swkhglService.update(swkhglDto);
            if (!success)
                throw new BusinessException("msg","更新客户回款周期失败！");
        }
        if (StringUtil.isNotBlank(swhtglDto.getJson())){
            List<SwhtmxDto> dtoList;
            try {
                dtoList = (List<SwhtmxDto>) JSON.parseArray(swhtglDto.getJson(),SwhtmxDto.class);
            } catch (Exception e) {
                throw new BusinessException("msg","传输明细信息格式错误！");
            }
            if (!CollectionUtils.isEmpty(dtoList)) {
                for (SwhtmxDto swhtmxDto : dtoList) {
                    swhtmxDto.setLrry(swhtglDto.getLrry());
                    swhtmxDto.setHtid(swhtglDto.getHtid());
                    swhtmxDto.setHtmxid(StringUtil.generateUUID());
                }
                success = swhtmxService.insertList(dtoList);
                if (!success)
                    throw new BusinessException("msg","保存明细信息失败！");
            }
        }
        if(StringUtil.isNotBlank(swhtglDto.getYhzcjson())){
            List<HtyhzcDto> htyhzcDtoList;
            try{
                htyhzcDtoList=(List<HtyhzcDto>) JSON.parseArray(swhtglDto.getYhzcjson(),HtyhzcDto.class);
            }catch(Exception e){
                throw new BusinessException("msg","传输优惠政策明细信息格式错误！");
            }
            if (!CollectionUtils.isEmpty(htyhzcDtoList)) {
                for (HtyhzcDto htyhzcDto : htyhzcDtoList) {
                    htyhzcDto.setHtid(swhtglDto.getHtid());
                    htyhzcDto.setLrry(swhtglDto.getLrry());
                    htyhzcDto.setHtyhzcid(StringUtil.generateUUID());
                }
                success = htyhzcService.insertList(htyhzcDtoList);
                if (!success)
                    throw new BusinessException("msg","保存优惠政策明细信息失败！");
            }
        }

        //文件复制到正式文件夹，插入信息至正式表
        if(swhtglDto.getFjids()!=null && swhtglDto.getFjids().size() > 0){
            for (int i = 0; i < swhtglDto.getFjids().size(); i++) {
                success = fjcfbService.save2RealFile(swhtglDto.getFjids().get(i),swhtglDto.getHtid());
                if(!success)
                    throw new BusinessException("msg","附件保存失败!");
            }
        }
        if(!CollectionUtils.isEmpty(swhtglDto.getChapterFjids())){
            for (int i = 0; i < swhtglDto.getChapterFjids().size(); i++) {
                success = fjcfbService.save2RealFile(swhtglDto.getChapterFjids().get(i),swhtglDto.getHtid());
                if(!success)
                    throw new BusinessException("msg","附件保存失败!");
            }
        }
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(swhtglDto.getHtid());
        List<FjcfbDto> dtoList = fjcfbService.getDtoList(fjcfbDto);
        if(!CollectionUtils.isEmpty(dtoList)){
            for(FjcfbDto fjcfbDto_t:dtoList){
                if(fjcfbDto_t.getWjm().indexOf(".doc")!=-1||fjcfbDto_t.getWjm().indexOf(".docx")!=-1||fjcfbDto_t.getWjm().indexOf(".xls")!=-1||fjcfbDto_t.getWjm().indexOf(".xlsx")!=-1){
                    log.error("文件： "+fjcfbDto_t.getWjm()+" 即将进行PDF转换");
                    DBEncrypt p = new DBEncrypt();
                    String wjljjm=p.dCode(fjcfbDto_t.getWjlj());
                    boolean sendFlg=fjcfbService.sendWordFile(wjljjm);
                    if(sendFlg) {
                        Map<String,String> map= new HashMap<>();
                        String fwjm=p.dCode(fjcfbDto_t.getFwjm());
                        map.put("wordName", fwjm);
                        map.put("fwjlj",fjcfbDto_t.getFwjlj());
                        map.put("fjid",fjcfbDto_t.getFjid());
                        map.put("ywlx",fjcfbDto_t.getYwlx());
                        map.put("MQDocOkType",DOC_OK);
                        //发送Rabbit消息转换pdf
                        amqpTempl.convertAndSend("doc2pdf_exchange",MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),JSONObject.toJSONString(map));
                    }
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean updateInfo(SwhtglDto swhtglDto) throws BusinessException {
        SwhtglDto dtoById = dao.getDtoById(swhtglDto.getHtid());
        if ((!swhtglDto.getHtksrq().equals(dtoById.getHtksrq())||!swhtglDto.getHtjsrq().equals(dtoById.getHtjsrq()))&&StatusEnum.CHECK_PASS.getCode().equals(dtoById.getZt())){
            XtszDto xtszDto = xtszService.getDtoById("business.contract.slippage");
            int yqsj = Integer.parseInt(xtszDto.getSzz());
            LocalDate today = LocalDate.now();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate htjsrqL = LocalDate.parse(swhtglDto.getHtjsrq(), df);
            long rqc = ChronoUnit.DAYS.between(today, htjsrqL);
            List<SwhtglDto> swhtglDtos = new ArrayList<>();
            SwhtglDto swhtglDto_mod = new SwhtglDto();
            swhtglDto_mod.setHtid(swhtglDto.getHtid());
            swhtglDto_mod.setXgry(swhtglDto.getXgry());
            // 当当前日期在结束日期和逾期周期之间设置为即将逾期
            if (rqc<=yqsj&&rqc>0){
                swhtglDto_mod.setHtzt("30");
                swhtglDto_mod.setSfcl("2");
            }else if (rqc<=0){
                swhtglDto_mod.setHtzt("40");
            }else {
                swhtglDto_mod.setHtzt("10");
            }
            swhtglDtos.add(swhtglDto_mod);
            if (!dao.updateSwhtglDtos(swhtglDtos)){
                throw new BusinessException("msg","修改商务合同合同状态失败！");
            }
        }
        Boolean success = dao.update(swhtglDto)!=0;
        if (!success)
            throw new BusinessException("msg","修改主表信息失败！");
	    if (StringUtil.isNotBlank(swhtglDto.getHkzq())){
            SwkhglDto swkhglDto = new SwkhglDto();
            swkhglDto.setKhid(swhtglDto.getQddx());
            swkhglDto.setHkzq(swhtglDto.getHkzq());
            success = swkhglService.update(swkhglDto);
            if (!success)
                throw new BusinessException("msg","更新客户回款周期失败！");
        }
        if (StringUtil.isNotBlank(swhtglDto.getJson())){
            List<SwhtmxDto> dtoList;
            try {
                dtoList = (List<SwhtmxDto>) JSON.parseArray(swhtglDto.getJson(),SwhtmxDto.class);
            } catch (Exception e) {
                throw new BusinessException("msg","传输明细信息格式错误！");
            }

            SwhtmxDto swhtmxDto = new SwhtmxDto();
            swhtmxDto.setHtid(swhtglDto.getHtid());
            List<SwhtmxDto> ys_list = swhtmxService.getDtoList(swhtmxDto);
            List<String> del_list = new ArrayList<>();
            List<SwhtmxDto> mod_list = new ArrayList<>();
            List<SwhtmxDto> add_list = new ArrayList<>();
            //页面传回来的数据先拿到mxid为空的数据为新增，并移除
            if (!CollectionUtils.isEmpty(dtoList)) {
                for (int i = dtoList.size() - 1; i >= 0; i--) {
                    if (StringUtil.isBlank(dtoList.get(i).getHtmxid())) {
                        dtoList.get(i).setLrry(swhtglDto.getXgry());
                        dtoList.get(i).setHtid(swhtglDto.getHtid());
                        dtoList.get(i).setHtmxid(StringUtil.generateUUID());
                        add_list.add(dtoList.get(i));
                        dtoList.remove(i);
                    }
                }
                for (SwhtmxDto dto : dtoList) {
                    //拿到mxid不为为空的数据，为修改的数据
                    dto.setXgry(swhtglDto.getXgry());
                    dto.setHtid(swhtglDto.getHtid());
                    mod_list.add(dto);
                    //用数据库的数据跟页面数据对比，如果同时存在则从原始数据移除，原始数据剩下的即为要删除的数据
                    for (int i = ys_list.size() - 1; i >= 0; i--) {
                        if (dto.getHtmxid().equals(ys_list.get(i).getHtmxid())) {
                            ys_list.remove(i);
                        }
                    }
                }
            }
            for (SwhtmxDto dto : ys_list) {
                del_list.add(dto.getHtmxid());
            }
            if (!CollectionUtils.isEmpty(add_list)){
                success = swhtmxService.insertList(add_list);
                if (!success)
                    throw new BusinessException("msg","修改明细信息失败！");
            }
            if (!CollectionUtils.isEmpty(mod_list)){
                success = swhtmxService.updateList(mod_list);
                if (!success)
                    throw new BusinessException("msg","修改明细信息失败！");
            }
            if (!CollectionUtils.isEmpty(del_list)){
                SwhtmxDto dto = new SwhtmxDto();
                dto.setMxids(del_list);
                dto.setScry(swhtglDto.getXgry());
                success = swhtmxService.delete(dto);
                if (!success)
                    throw new BusinessException("msg","修改明细信息失败！");
            }
        }
        //优惠政策明细处理
        if (StringUtil.isNotBlank(swhtglDto.getYhzcjson())){
            List<HtyhzcDto> yhzcList;
            try {
                yhzcList = (List<HtyhzcDto>) JSON.parseArray(swhtglDto.getYhzcjson(),HtyhzcDto.class);
            } catch (Exception e) {
                throw new BusinessException("msg","传输优惠政策明细信息格式错误！");
            }

            HtyhzcDto htyhzcDto = new HtyhzcDto();
            htyhzcDto.setHtid(swhtglDto.getHtid());
            List<HtyhzcDto> ys_list = htyhzcService.getDtoList(htyhzcDto);
            List<String> del_list = new ArrayList<>();
            List<HtyhzcDto> mod_list = new ArrayList<>();
            List<HtyhzcDto> add_list = new ArrayList<>();
            //页面传回来的数据先拿到mxid为空的数据为新增，并移除
            if (!CollectionUtils.isEmpty(yhzcList)) {
                for (int i = yhzcList.size() - 1; i >= 0; i--) {
                    if (StringUtil.isBlank(yhzcList.get(i).getHtyhzcid())) {
                        yhzcList.get(i).setLrry(swhtglDto.getXgry());
                        yhzcList.get(i).setHtid(swhtglDto.getHtid());
                        yhzcList.get(i).setHtyhzcid(StringUtil.generateUUID());
                        add_list.add(yhzcList.get(i));
                        yhzcList.remove(i);
                    }
                }
                for (HtyhzcDto dto : yhzcList) {
                    //拿到mxid不为为空的数据，为修改的数据
                    dto.setXgry(swhtglDto.getXgry());
                    dto.setHtid(swhtglDto.getHtid());
                    mod_list.add(dto);
                    //用数据库的数据跟页面数据对比，如果同时存在则从原始数据移除，原始数据剩下的即为要删除的数据
                    for (int i = ys_list.size() - 1; i >= 0; i--) {
                        if (dto.getHtyhzcid().equals(ys_list.get(i).getHtyhzcid())) {
                            ys_list.remove(i);
                        }
                    }
                }
            }
            for (HtyhzcDto dto : ys_list) {
                del_list.add(dto.getHtyhzcid());
            }
            if (!CollectionUtils.isEmpty(add_list)){
                success = htyhzcService.insertList(add_list);
                if (!success)
                    throw new BusinessException("msg","修改优惠政策明细信息失败！");
            }
            if (!CollectionUtils.isEmpty(mod_list)){
                success = htyhzcService.updateList(mod_list);
                if (!success)
                    throw new BusinessException("msg","修改优惠政策明细信息失败！");
            }
            if (!CollectionUtils.isEmpty(del_list)){
                HtyhzcDto dto = new HtyhzcDto();
                dto.setIds(del_list);
                dto.setScry(swhtglDto.getXgry());
                success = htyhzcService.delete(dto);
                if (!success)
                    throw new BusinessException("msg","修改明细信息失败！");
            }
        }
        //文件复制到正式文件夹，插入信息至正式表
        if(swhtglDto.getFjids()!=null && swhtglDto.getFjids().size() > 0){
            for (int i = 0; i < swhtglDto.getFjids().size(); i++) {
                success = fjcfbService.save2RealFile(swhtglDto.getFjids().get(i),swhtglDto.getHtid());
                if(!success)
                    throw new BusinessException("msg","附件保存失败!");
            }
        }
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(swhtglDto.getHtid());
        List<FjcfbDto> dtoList = fjcfbService.getDtoList(fjcfbDto);
        if(!CollectionUtils.isEmpty(dtoList)){
            for(FjcfbDto fjcfbDto_t:dtoList){
                if(fjcfbDto_t.getWjm().indexOf(".doc")!=-1||fjcfbDto_t.getWjm().indexOf(".docx")!=-1||fjcfbDto_t.getWjm().indexOf(".xls")!=-1||fjcfbDto_t.getWjm().indexOf(".xlsx")!=-1){
                    log.error("文件： "+fjcfbDto_t.getWjm()+" 即将进行PDF转换");
                    DBEncrypt p = new DBEncrypt();
                    String wjljjm=p.dCode(fjcfbDto_t.getWjlj());
                    boolean sendFlg=fjcfbService.sendWordFile(wjljjm);
                    if(sendFlg) {
                        Map<String,String> map= new HashMap<>();
                        String fwjm=p.dCode(fjcfbDto_t.getFwjm());
                        map.put("wordName", fwjm);
                        map.put("fwjlj",fjcfbDto_t.getFwjlj());
                        map.put("fjid",fjcfbDto_t.getFjid());
                        map.put("ywlx",fjcfbDto_t.getYwlx());
                        map.put("MQDocOkType",DOC_OK);
                        //发送Rabbit消息转换pdf
                        amqpTempl.convertAndSend("doc2pdf_exchange",MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),JSONObject.toJSONString(map));
                    }
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean delInfo(SwhtglDto swhtglDto) throws BusinessException {
        boolean success = dao.delete(swhtglDto)!=0;
        if (!success)
            throw new BusinessException("msg","删除主表信息失败！");
        SwhtmxDto dto = new SwhtmxDto();
        dto.setIds(swhtglDto.getIds());
        dto.setScry(swhtglDto.getScry());
        swhtmxService.delete(dto);
        KhhtglDto khhtglDto = new KhhtglDto();
        khhtglDto.setIds(swhtglDto.getIds());
        success = khhtglService.delete(khhtglDto);
        if (!success)
            throw new BusinessException("msg","删除客户关联信息失败！");
        return true;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean formalSaveContract(SwhtglDto swhtglDto) throws BusinessException {
        SwhtglDto dto = new SwhtglDto();
        dto.setXgry(swhtglDto.getXgry());
        dto.setHtid(swhtglDto.getHtid());
        dto.setGzzt("1");
        dto.setHtzt("10");
        boolean success = dao.update(dto) != 0;
        if(!success)
            throw new BusinessException("msg","盖章状态更新失败!");
        //文件复制到正式文件夹，插入信息至正式表
        if(swhtglDto.getChapterFjids()!=null && swhtglDto.getChapterFjids().size() > 0){
            for (int i = 0; i < swhtglDto.getChapterFjids().size(); i++) {
                success = fjcfbService.save2RealFile(swhtglDto.getChapterFjids().get(i),swhtglDto.getHtid());
                if(!success)
                    throw new BusinessException("msg","附件保存失败!");
            }
        }else{
            throw new BusinessException("msg","附件不存在!");
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean discontinuecontractSave(SwhtglDto swhtglDto) {
        SwhtglDto dto = new SwhtglDto();
        dto.setXgry(swhtglDto.getXgry());
        dto.setHtid(swhtglDto.getHtid());
        dto.setHtzt("50");
        dto.setZzyy(swhtglDto.getZzyy());
        return dao.update(dto) != 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean replenishcontractSaveContract(SwhtglDto swhtglDto) throws BusinessException {
        KhhtglDto khhtglDto = new KhhtglDto();
        khhtglDto.setHtid(swhtglDto.getHtid());
        Boolean success = khhtglService.delete(khhtglDto);
        if(!success)
            throw new BusinessException("msg","客户合同信息更新失败!");
        if (!CollectionUtils.isEmpty(swhtglDto.getKhids())){
            List<KhhtglDto> list = new ArrayList<>();
            for (String khid : swhtglDto.getKhids()) {
                KhhtglDto dto = new KhhtglDto();
                dto.setHtid(swhtglDto.getHtid());
                dto.setKhid(khid);
                list.add(dto);
            }
            success = khhtglService.insertList(list);
            if (!success)
                throw new BusinessException("msg","客户合同信息更新失败！");
        }
        if (!CollectionUtils.isEmpty(swhtglDto.getChapterFjids())){
            for (int i = 0; i < swhtglDto.getChapterFjids().size(); i++) {
                success = fjcfbService.save2RealFile(swhtglDto.getChapterFjids().get(i),swhtglDto.getHtid());
                if(!success)
                    throw new BusinessException("msg","附件保存失败!");
            }
        }
        return true;
    }
    @Override
    public String getHtzt(SwhtglDto swhtglDto){
        XtszDto dtoById = xtszService.getDtoById("business.contract.slippage");
        LocalDate today = LocalDate.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int yqsj = Integer.parseInt(dtoById.getSzz());
        LocalDate htjsrq = LocalDate.parse(swhtglDto.getHtjsrq(), df);
        long rqc = ChronoUnit.DAYS.between(today, htjsrq);
        if (rqc < 0L){
            return "40";
        }else if (rqc > yqsj){
            return "10";
        }else{
            return "30";
        }
    }




    public List<Map<String,Object>> getListForSelectExp(Map<String, Object> params) {
        Map<String, Object> map = (Map<String, Object>) params.get("entryData");
        queryJoinFlagExport(params,map);
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        for (DcszDto dcszDto : choseList) {
            if ("2".equals(dcszDto.getFlpx())){
                map.put("bs","1");
                break;
            }
        }
        List<JcsjDto> jcsjDtos=redisUtil.lgetDto("All_matridx_jcsj:"+BasicDataTypeEnum.DETECT_TYPE.getCode());
        if(jcsjDtos!=null&&jcsjDtos.size()>0){
            for(JcsjDto dto:jcsjDtos){
                String csmc=dto.getCsmc();
                dto.setCsmc(csmc.replaceAll("-","").replaceAll("\\+","").replaceAll("\\.","").replaceAll("0","").replaceAll("\\(","").replaceAll("\\)","").replaceAll(" ","").replaceAll("™",""));
            }
        }
        map.put("jcsjDtos",jcsjDtos);
        return dao.getListForSelectExp(map);
    }


    public List<Map<String,Object>> getListForSearchExp(Map<String, Object> params) {
        Map<String, Object> map = (Map<String, Object>) params.get("entryData");
        queryJoinFlagExport(params,map);
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        for (DcszDto dcszDto : choseList) {
            if ("2".equals(dcszDto.getFlpx())){
                map.put("bs","1");
                break;
            }
        }
        List<JcsjDto> jcsjDtos=redisUtil.lgetDto("All_matridx_jcsj:"+BasicDataTypeEnum.DETECT_TYPE.getCode());
        if(jcsjDtos!=null&&jcsjDtos.size()>0){
            for(JcsjDto dto:jcsjDtos){
                String csmc=dto.getCsmc();
                dto.setCsmc(csmc.replaceAll("-","").replaceAll("\\+","").replaceAll("\\.","").replaceAll("0","").replaceAll("\\(","").replaceAll("\\)","").replaceAll(" ","").replaceAll("™",""));
            }
        }
        map.put("jcsjDtos",jcsjDtos);
        return dao.getListForSearchExp(map);
    }


    public int getCountForSearchExp(Map<String,Object> map, Map<String, Object> params) {
        return dao.getCountForSearchExp(map);
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, Map<String,Object> map)
    {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuffer sqlParam = new StringBuffer();
        for (DcszDto dcszDto : choseList)
        {
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;



            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd()))
            {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        map.put("sqlParam",sqlParam.toString());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        SwhtglDto swhtglDto = (SwhtglDto) baseModel;
        swhtglDto.setXgry(operator.getYhid());
        boolean isSuccess = true;
        if (StringUtil.isNotBlank(swhtglDto.getDdbj())){
            isSuccess = updateInfo(swhtglDto);
        }
        return isSuccess;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        for (ShgcDto shgcDto : shgcList) {
            SwhtglDto swhtglDto = new SwhtglDto();
            swhtglDto.setHtid(shgcDto.getYwid());
            swhtglDto.setXgry(operator.getYhid());
            SwhtglDto dtoById = dao.getDtoById(swhtglDto.getHtid());
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                // 由于合同提交时直接发起了钉钉审批，所以审核不通过时直接采用钉钉的审批拒绝消息即可
                swhtglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                //OA取消审批的同时组织钉钉审批
                if(dtoById!=null && StringUtils.isNotBlank(dtoById.getDdslid()) && AuditTypeEnum.AUDIT_BUSINESS_CONTRACT.getCode().equals(shgcDto.getShlb())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), dtoById.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map=JSON.parseObject(success,Map.class);
                    Boolean bo1= (boolean) result_map.get("success");
                    if(bo1)
                        swhtglDto.setDdslid("");
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                swhtglDto.setZt(StatusEnum.CHECK_PASS.getCode());
                //更新伙伴收费标准，同步85端，这里直接调用定时任务方法
                SwhtmxDto swhtmxDto=new SwhtmxDto();
                swhtmxDto.setHtid(swhtglDto.getHtid());
                insertOrUpdate(swhtmxDto);
                //根据开始时间和结束时间更新历史标本应付金额（考虑合同滞后实际标本已开始送的情况）
                //查询标本信息通过接收日期，伙伴来限制
                List<SjjcxmDto> sjjcxmDtos=dao.getUpListBbHt(dtoById);
                //更新检测项目表
                if(!CollectionUtils.isEmpty(sjjcxmDtos))
                    sjjcxmService.updateSjjcxmDtos(sjjcxmDtos);
                //更新送检表
                List<SjxxDto> sjxxDtos=dao.getSjUpListByHt(dtoById);
                if(!CollectionUtils.isEmpty(sjxxDtos))
                    sjxxService.updateFkje(sjxxDtos);
                //发送rabbit同步85端
                RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.YFJE_MODLIST.getCode() + JSONObject.toJSONString(sjjcxmDtos));
                RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.FKJE_MODLIST.getCode() + JSONObject.toJSONString(sjxxDtos));
            }else {
                swhtglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                if("1".equals(shgcDto.getXlcxh()) && shgcDto.getShxx()==null) {//提交的时候发起钉钉审批
                    try {
                        Map<String,Object> template;
                        if ("PERSONAL_FILING".equals(dtoById.getHtfldm())) {
                            template = talkUtil.getTemplateCode(operator.getYhm(), "合作伙伴备案");//获取审批模板ID
                        }else{
                            template = talkUtil.getTemplateCode(operator.getYhm(), "营销合同");//获取审批模板ID
                        }

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
                        Map<String,String> map= new HashMap<>();
                        if ("PERSONAL_FILING".equals(dtoById.getHtfldm())) {
                            map.put("个人代理商名称", dtoById.getKhmc());
                            map.put("代理商联系人", dtoById.getLxr());
                            map.put("代理商联系电话", dtoById.getLxdh());
                            map.put("授权区域", dtoById.getSqqy());
                            map.put("代理样本量归属人", dtoById.getFzr());
                        }else{
                            map.put("合同主体", dtoById.getQdztmc());
                            map.put("合同风险程度", dtoById.getHtfxmc());
                            map.put("销售类别", dtoById.getXslbmc());
                            map.put("用章类型", dtoById.getYzlxmc());
                            map.put("合同名称", dtoById.getHtmc());
                            map.put("合同编号", dtoById.getHtbh());
                            map.put("客户名称", dtoById.getKhmc());
                            map.put("合同金额（元）", dtoById.getJe());
                            map.put("合同份数", dtoById.getHtfs());
                            map.put("合同邮寄信息", dtoById.getHtyjxx());
                            map.put("备注", StringUtil.isNotBlank(dtoById.getBz())?dtoById.getBz():"");
                        }
                        map.put("优惠政策", StringUtil.isNotBlank(dtoById.getYhzc())?dtoById.getYhzc():"");
                        map.put("其他内容",applicationurl+urlPrefix+"/ws/contract/getBusinessContractUrl?htid="+dtoById.getHtid());
                        log.error("个人代理商名称:"+dtoById.getKhmc()+"代理商联系人:"+ dtoById.getLxr()+"代理商联系电话:"+dtoById.getLxdh()+"授权区域:"+dtoById.getSqqy()+"代理样本量归属人:"+
                                dtoById.getFzr()+"优惠政策:"+ map.get("优惠政策")+"其他内容:"+applicationurl+urlPrefix+"/ws/contract/getBusinessContractUrl?htid="+dtoById.getHtid()
                        );

                        List<DdxxglDto> dtoList = ddxxglService.getDingtalkAuditDep(DdAuditTypeEnum.SW_HT.getCode());
                        if (CollectionUtils.isEmpty(dtoList))
                            throw new BusinessException("msg","未配置钉钉审批流程!");
                        String ddids = "";
                        if (StringUtil.isNotBlank(dtoById.getSpr())){
                            List<String> strings = new ArrayList<>();
                            Collections.addAll(strings, dtoById.getSpr().split(","));
                            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(strings)){
                                User user1 = new User();
                                user1.setIds(strings);
                                List<User> list = commonService.getListByIds(user1);
                                for (User user2 : list) {
                                    if (StringUtil.isNotBlank(user2.getDdid())){
                                        ddids+=","+user2.getDdid();
                                    }
                                }
                            }
                        }
                        Map<String,Object> t_map=talkUtil.createInstance(operator.getYhm(), templateCode,StringUtil.isNotBlank(ddids)? ddids.substring(1)+","+dtoList.get(0).getSpr():dtoList.get(0).getSpr(), null, userid, dept, map,null,null);
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
                                paramMap.add("wbcxid", operator.getWbcxid());//存入外部程序id

    //                                //分布式保留一份
                                boolean t_result = t_restTemplate.postForObject( applicationurl+"/ws/purchase/saveDistributedMsg", paramMap, boolean.class);
                                if(!t_result)
                                    return false;
                                //主站保留一份
                                if(org.apache.commons.lang.StringUtils.isNotBlank(registerurl)){
                                    boolean result = t_restTemplate.postForObject(registerurl + "/ws/purchase/saveDistributedMsg", paramMap, boolean.class);
                                    if(!result)
                                        return false;
                                }
                                swhtglDto.setDdslid(String.valueOf(result_map.get("process_instance_id")));
                            }else {
                                throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+t_map.get("message"));
                            }
                        }else {
                            throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+t_map.get("message"));
                        }

                    } catch (BusinessException e) {
                        // TODO: handle exception
                        throw new BusinessException("msg",e.getMsg());
                    }catch (Exception e) {
                        // TODO: handle exception
                        throw new BusinessException("msg","异常!异常信息:"+e.toString());
                    }
                }
            }
            dao.update(swhtglDto);
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        for (ShgcDto shgcDto : shgcList) {
            SwhtglDto swhtglDto = new SwhtglDto();
            String htid = shgcDto.getYwid();
            swhtglDto.setXgry(operator.getYhid());
            swhtglDto.setHtid(htid);
            if (auditParam.isCancelOpe()) {
                swhtglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
            } else {
                swhtglDto.setZt(StatusEnum.CHECK_NO.getCode());
                //OA取消审批的同时组织钉钉审批
                SwhtglDto dtoById = dao.getDtoById(swhtglDto.getHtid());
                if(dtoById!=null && StringUtils.isNotBlank(dtoById.getDdslid()) && AuditTypeEnum.AUDIT_BUSINESS_CONTRACT.getCode().equals(shgcDto.getShlb())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), dtoById.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map=JSON.parseObject(success,Map.class);
                    Boolean bo1= (boolean) result_map.get("success");
                    if(bo1)
                        swhtglDto.setDdslid("");
                }
            }
            dao.update(swhtglDto);
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) throws BusinessException {
        return null;
    }

    @Override
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) throws BusinessException {
        Map<String, Object> map =new HashMap<>();
        List<String> ids = (List<String>)param.get("ywids");
        SwhtglDto swhtglDto = new SwhtglDto();
        swhtglDto.setIds(ids);
        List<SwhtglDto> dtoList = dao.getDtoList(swhtglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(SwhtglDto dto:dtoList){
                list.add(dto.getHtid());
            }
        }
        map.put("list",list);
        return map;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean callbackContractAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException {
        SwhtglDto swhtglDto=new SwhtglDto();
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
        String wbcxidString  = obj.getString("wbcxid"); //获取外部程序id

        log.error("回调参数获取---------result:"+result+",type:"+type+",processInstanceId:"+processInstanceId+",staffId:"+staffId+",remark:"+remark+",finishTime"+finishTime);
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
//			int a=1/0;
            if(ddfbsglDto==null)
                throw new BusinessException("message","未获取到相应的钉钉分布式信息！");
            if(org.apache.commons.lang3.StringUtils.isNotBlank(ddfbsglDto.getFwqm())  || !applicationurl.equals(registerurl)) {
                if("1".equals(ddspbcbj)) {
                    ddspglDto=ddspglService.insertInfo(obj);
                }else {
                    if(ddspgllist!=null && ddspgllist.size()>0) {
                        ddspglDto=ddspgllist.get(0);
                    }else{
                        ddspglDto=ddspglService.insertInfo(obj);
                    }
                }
            }
            swhtglDto.setDdslid(processInstanceId);
            //根据钉钉审批实例ID查询关联请购单
            swhtglDto=dao.getDtoByDdslid(swhtglDto);
            //若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if(swhtglDto!=null) {
                //获取审批人信息
                User user=new User();
                user.setDdid(staffId);
                user.setWbcxid(wbcxidString);
                user = commonService.getByddwbcxid(user);
                //判断查出得信息是否为空
                if(user==null)
                    return false;
                //获取审批人角色信息
                List<SwhtglDto> dd_sprjs=dao.getSprjsBySprid(user.getYhid());
                // 获取当前审核过程
                ShgcDto t_shgcDto = shgcService.getDtoByYwid(swhtglDto.getHtid());
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
                        shxxDto.setYwid(swhtglDto.getHtid());
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
                                log.error("DingTalkMaterPurchaseAudit(钉钉物料请购审批转发提醒)------转发人员:"+user.getZsxm()+",人员ID:"+user.getYhid()+",合同编号:"+swhtglDto.getHtbh()+",合同ID:"+swhtglDto.getHtid()+",转发时间:"+date);
                                return true;
                            }
                            //调用审核方法
                            Map<String, List<String>> map= ToolUtil.reformRequest(request);
                            log.error("map:"+map);
                            List<String> list= new ArrayList<>();
                            list.add(swhtglDto.getHtid());
                            map.put("htid", list);
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
                                    log.error("callbackAduit-Exception:" + e.getMessage());
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
                            shxxDto.setYwglmc(swhtglDto.getHtbh());
                            SwhtglDto swhtglDto1 = new SwhtglDto();
                            swhtglDto1.setHtid(swhtglDto.getHtid());
                            swhtglDto1.setDdslid("");
                            dao.update(swhtglDto1);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            //调用审核方法
                            Map<String, List<String>> map=ToolUtil.reformRequest(request);
                            List<String> list= new ArrayList<>();
                            list.add(swhtglDto.getHtid());
                            map.put("htid", list);
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
                        shxx.setYwid(swhtglDto.getHtid());
                        shxx.setShlb(AuditTypeEnum.AUDIT_BUSINESS_CONTRACT.getCode());
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
	
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void reminderOverdueContract() throws Exception {
        XtszDto dtoById = xtszService.getDtoById("business.contract.slippage");
        if (dtoById!=null){
            String nowdate = DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd");
            LocalDate today = LocalDate.now();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            int yqsj = Integer.parseInt(dtoById.getSzz());
            SwhtglDto swhtglDto = new SwhtglDto();
            List<String> htzts = new ArrayList<>();
            htzts.add("10");
            htzts.add("30");
            swhtglDto.setHtzts(htzts);
            List<SwhtglDto> dtoList = dao.getDtoList(swhtglDto);
            if (!CollectionUtils.isEmpty(dtoList)){
                //发送满足逾期条件消息
                List<SwhtglDto> jyqSwhtglDtos = new ArrayList<>();
                //修改合同状态
                List<SwhtglDto> modSwhtglDtos = new ArrayList<>();
                for (SwhtglDto dto : dtoList) {
                    if (StringUtil.isBlank(dto.getHtjsrq())){
                        continue;
                    }
                    LocalDate htjsrq = LocalDate.parse(dto.getHtjsrq(), df);
                    long rqc = ChronoUnit.DAYS.between(today, htjsrq);
                    //状态是10且是否履行是1  并且日期差为系统设置值
                    if ("1".equals(dto.getSflx())&&"10".equals(dto.getHtzt())&&rqc<=yqsj&&rqc>0){
                        SwhtglDto swhtglDto_up = new SwhtglDto();
                        swhtglDto_up.setHtid(dto.getHtid());
                        swhtglDto_up.setHtzt("30");
                        swhtglDto_up.setXgry("admin");
                        swhtglDto_up.setSfcl("2");
                        jyqSwhtglDtos.add(dto);
                        modSwhtglDtos.add(swhtglDto_up);
                    }
                    if ("30".equals(dto.getHtzt())&&rqc<=0){
                        SwhtglDto swhtglDto_up = new SwhtglDto();
                        swhtglDto_up.setHtid(dto.getHtid());
                        swhtglDto_up.setXgry("admin");
                        swhtglDto_up.setHtzt("40");
                        modSwhtglDtos.add(swhtglDto_up);
                    }
                }
                if (!CollectionUtils.isEmpty(modSwhtglDtos)){
                    boolean isSuccess = dao.updateSwhtglDtos(modSwhtglDtos);
                    if (!isSuccess){
                        throw new BusinessException("msg","修改合同状态失败！");
                    }
                }

                if (!CollectionUtils.isEmpty(jyqSwhtglDtos)){
                    String ICOMM_HTTX00001 = xxglService.getMsg("ICOMM_HTTX00001");
                    String ICOMM_HTTX00002 = xxglService.getMsg("ICOMM_HTTX00002");
                    List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(DingMessageType.OVERDUE_CONTRACT_WARNING.getCode());
                    for (SwhtglDto jyqSwhtglDto : jyqSwhtglDtos) {
                        if(!CollectionUtils.isEmpty(ddxxglDtos)) {
                            for (DdxxglDto ddxxglDto : ddxxglDtos) {
                                talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getYhid(),ICOMM_HTTX00001,StringUtil.replaceMsg(ICOMM_HTTX00002
                                        ,jyqSwhtglDto.getHtbh(),jyqSwhtglDto.getKhmc() ,jyqSwhtglDto.getHtjsrq().substring(0,4) ,jyqSwhtglDto.getHtjsrq().substring(5,7) ,jyqSwhtglDto.getHtjsrq().substring(8,10)));
                            }
                        }
                    }
                }
            }
            //获取一下未结清的应收账款信息，判断一下当前日期是否超过开票日期+回款周期，若超过则将应付中的是否逾期更新为1
            List<SwyszkDto> outstandingData = swyszkService.getOutstandingData();
            if (outstandingData!=null&&outstandingData.size()>0){
                List<String> ids=new ArrayList<>();
                for(SwyszkDto dto:outstandingData){
                    if(StringUtil.isNotBlank(dto.getKprq())&&StringUtil.isNotBlank(dto.getHkzq())){
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                        Date date=format.parse(dto.getKprq());
                        Calendar calendar=Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.DATE, Integer.parseInt(dto.getHkzq()));// 添加天数
                        Date kprq=calendar.getTime();
                        Date time=new Date();
                        if(kprq.compareTo(time) < 0){
                            ids.add(dto.getYszkid());
                        }
                    }
                }
                if(ids!=null&&ids.size()>0){
                    SwyszkDto swyszkDto=new SwyszkDto();
                    swyszkDto.setIds(ids);
                    swyszkDto.setXgry("admin");
                    swyszkDto.setSfyq("1");
                    boolean isSuccess = swyszkService.updateSfyq(swyszkDto);
                    if (!isSuccess){
                        throw new BusinessException("msg","修改是否逾期字段失败！");
                    }
                }
            }
        }else {
            log.error("reminderOverdueContract--系统设置未设置");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void modHbsfInfo(){
        SwhtmxDto swhtmxDto=new SwhtmxDto();
        swhtmxDto.setCxbj("1");
        swhtmxDto.setZt(StatusEnum.CHECK_PASS.getCode());
        insertOrUpdate(swhtmxDto);
    }

    public void insertOrUpdate(SwhtmxDto tswhtmxDto){
        //查询需要更新伙伴收费标准的数据
        List<SwhtmxDto> dtos = swhtmxService.getHbsfInfo(tswhtmxDto);
        if (!CollectionUtils.isEmpty(dtos)){
            List<HbsfbzDto> list_one = new ArrayList<>();//path为1的代表我需要去处理更新或新增的,这里用HbsfbzDto方便rabbit同步
            List<HbsfbzDto> list_add=new ArrayList<>();//path为1中若收费标准为空的代表没有关联到伙伴收费标准信息，也就是需要新增的数据
            List<String> htids_two = new ArrayList<>();//path为2的代表我要去处理将其合同明细sfyx更新为0的
            List<String> htids=new ArrayList<>();//获取sflx=0的合同ID
            for(SwhtmxDto swhtmxDto :dtos){
                if("1".equals(swhtmxDto.getPath())){
                    if (StringUtil.isNotBlank(swhtmxDto.getHtmxid())){
                        HbsfbzDto hbsfbzDto = new HbsfbzDto();
                        hbsfbzDto.setHbid(swhtmxDto.getHbid());
                        hbsfbzDto.setXm(swhtmxDto.getJcxmid());
                        hbsfbzDto.setZxm(swhtmxDto.getJczxmid());
                        hbsfbzDto.setSfbz(swhtmxDto.getJg());
                        hbsfbzDto.setKsrq(swhtmxDto.getHtksrq());
                        hbsfbzDto.setJsrq(swhtmxDto.getHtjsrq());
                        hbsfbzDto.setLrsj(swhtmxDto.getLrsj());
                        hbsfbzDto.setHtmxid(swhtmxDto.getHtmxid());
                        if(StringUtil.isBlank(swhtmxDto.getSfbz())){
                            hbsfbzDto.setInsertFlag("1");
                        }
                        hbsfbzDto.setHbsfbzid(StringUtil.generateUUID());
                        list_one.add(hbsfbzDto);
                        if(StringUtil.isBlank(swhtmxDto.getSfbz()))
                            list_add.add(hbsfbzDto);
                    }
                    htids.add(swhtmxDto.getHtid());//path为1的都可以将合同的是否履行修改为1，是否有效修改为1
                }else{
                    htids_two.add(swhtmxDto.getHtid());
                }
            }
            //执行更新
            //这里由于包含需要update和insert的数据，所以先执行update，然后再通过not in 的形式insert
            hbsfbzService.updateSfbzByHt(tswhtmxDto);
            //执行新增
            if(!CollectionUtils.isEmpty(list_add)){
                hbsfbzService.insertsfbz(list_add);
            }
            if (!CollectionUtils.isEmpty(list_one)){
                //同步数据
                amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.MOD_PARTNER_TOLL.getCode(),  JSONObject.toJSONString(list_one));
            }
            if(!CollectionUtils.isEmpty(htids)) {
                //修改合同状态
                SwhtglDto swhtglDto = new SwhtglDto();
                swhtglDto.setIds(htids);
                swhtglDto.setSflx("1");
                swhtglDto.setXgry("admin");
                dao.update(swhtglDto);
                SwhtmxDto t_swhtmxDto = new SwhtmxDto();
                t_swhtmxDto.setIds(htids);
                t_swhtmxDto.setSfyx("1");
                swhtmxService.update(t_swhtmxDto);
            }
            //修改合同明细sfyx为无效
            if(!CollectionUtils.isEmpty(htids_two)){
                SwhtmxDto swhtmxDto = new SwhtmxDto();
                swhtmxDto.setIds(htids_two);
                swhtmxDto.setSfyx("0");
                swhtmxService.update(swhtmxDto);
            }
        }
        HtyhzcDto htyhzcDto=new HtyhzcDto();
        htyhzcDto.setZt(tswhtmxDto.getZt());
        List<HtyhzcDto> htyhzcDtos=htyhzcService.getYhzcInfo(htyhzcDto);
        if(!CollectionUtils.isEmpty(htyhzcDtos)){
            List<KhyhzcbzDto> add_khyhzcbzDtos=new ArrayList<>();
            List<KhyhzcbzDto> mod_khyhzcbzDtos=new ArrayList<>();
            List<String> notdelids=new ArrayList<>();//不删除的khyhzcbzids
            List<String> delhtids=new ArrayList<>();//不删除的khyhzcbzids
            for(HtyhzcDto htyhzcDto1:htyhzcDtos){
                KhyhzcbzDto khyhzcbzDto=new KhyhzcbzDto();
                khyhzcbzDto.setKhid(htyhzcDto1.getKhid());
                khyhzcbzDto.setYhfl(htyhzcDto1.getYhfl());
                khyhzcbzDto.setYhzfl(htyhzcDto1.getYhzfl());
                khyhzcbzDto.setYhqsfw(htyhzcDto1.getYhqsfw());
                khyhzcbzDto.setYhjsfw(htyhzcDto1.getYhjsfw());
                khyhzcbzDto.setYh(htyhzcDto1.getYh());
                khyhzcbzDto.setHtyhzcid(htyhzcDto1.getHtyhzcid());
                khyhzcbzDto.setHtid(htyhzcDto1.getHtid());
                khyhzcbzDto.setYhxs(htyhzcDto1.getYhxs());
                if (StringUtil.isBlank(htyhzcDto1.getKhyhzcbzid())){
                    //客户优惠政策标准ID为空的直接插入
                    khyhzcbzDto.setKhyhzcbzid(StringUtil.generateUUID());
                    add_khyhzcbzDtos.add(khyhzcbzDto);
                }else{
                    //能关联上客户优惠政策标准id的直接更新
                    khyhzcbzDto.setKhyhzcbzid(htyhzcDto1.getKhyhzcbzid());
                    mod_khyhzcbzDtos.add(khyhzcbzDto);
                }
                notdelids.add(khyhzcbzDto.getKhyhzcbzid());
                delhtids.add(khyhzcbzDto.getHtid());
            }

            if(!CollectionUtils.isEmpty(notdelids) && !CollectionUtils.isEmpty(delhtids)){//删除
                KhyhzcbzDto khyhzcbzDto=new KhyhzcbzDto();
                khyhzcbzDto.setIds(notdelids);
                khyhzcbzDto.setHtids(delhtids);
                khyhzcbzService.delInfo(khyhzcbzDto);
            }
            if(!CollectionUtils.isEmpty(add_khyhzcbzDtos)){
                khyhzcbzService.insertList(add_khyhzcbzDtos);
            }
            if(!CollectionUtils.isEmpty(mod_khyhzcbzDtos)){
                khyhzcbzService.updateList(mod_khyhzcbzDtos);
            }
        }
    }

    /**
     * 客户合同管理查询合同信息
     * @param
     * @return
     */
    public List<SwhtglDto> getKhhtglInfo(SwhtglDto swhtglDto){
        return dao.getKhhtglInfo(swhtglDto);
    }

    /**
     * 处理合同信息
     */
    public Boolean processContract(SwhtglDto swhtglDto){
        return dao.updateSfcl(swhtglDto);
    }

    /**
     * 修改状态
     */
    public boolean updateZt(SwhtglDto swhtglDto){
        return dao.updateZt(swhtglDto);
    }

    /**
     * 更新收费标准
     * @param swhtglDto
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateSfbz(SwhtglDto swhtglDto){
        return dao.updateSfbz(swhtglDto);
    }
}

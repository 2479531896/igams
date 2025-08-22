package com.matridx.igams.sample.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.sample.dao.entities.*;
import com.matridx.igams.sample.dao.post.IYbllglDao;
import com.matridx.igams.sample.service.svcinterface.ISbglService;
import com.matridx.igams.sample.service.svcinterface.IYbkcxxService;
import com.matridx.igams.sample.service.svcinterface.IYbllglService;
import com.matridx.igams.sample.service.svcinterface.IYbllmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 *@date 2022年06月13日17:08
 *
 */
@Service
public class YbllglServiceImpl extends BaseBasicServiceImpl<YbllglDto, YbllglModel, IYbllglDao> implements IYbllglService, IAuditService {

    private final Logger logger = LoggerFactory.getLogger(YbllglServiceImpl.class);
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ICommonService commonService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IYbllmxService ybllmxService;
    @Lazy
    @Autowired
    IYbkcxxService ybkcxxService;
    @Autowired
    ISbglService sbglService;
    /**
     * 自动生成领料单号
     */
    public String generateLldh(YbllglDto ybllglDto) {
        // TODO Auto-generated method stub
        // 生成规则: LL-20201022-01 LL-年份日期-流水号 。
        String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
        String prefix = "LL" + "-" + date + "-";
        // 查询流水号
        String serial = dao.getLldhSerial(prefix);
        return prefix + serial;
    }
    /**
     * 校验领料单号是否重复
     */
    public boolean getDtoByLldh(YbllglDto ybllglDto) {
        YbllglDto ybllglDto_t = dao.getDtoByLldh(ybllglDto);
        return ybllglDto_t==null;
    }
    // 查询状态
    @Override
    public YbllglDto getZtById(YbllglDto ybllglDto) {
        return dao.getZtById(ybllglDto);
    }

    @Override
    public boolean updateFlr(YbllglDto ybllglDto) {
        return dao.updateFlr(ybllglDto);
    }

    @Override
    public List<YbllglDto> getPagedAuditPick(YbllglDto ybllglDto) {
        // 获取人员ID和履历号
        List<YbllglDto> t_sbyzList = dao.getPagedAuditPick(ybllglDto);
        if (t_sbyzList == null || t_sbyzList.isEmpty())
            return t_sbyzList;

        List<YbllglDto> sqList = dao.getAuditListPick(t_sbyzList);

        commonService.setSqrxm(sqList);

        return sqList;
    }

    @Override
    public List<YbllglDto> getDtoByIds(YbllglDto ybllglDto) {
        return dao.getDtoByIds(ybllglDto);
    }
    /**
     * 样本领料修改保存或提交或审核
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveYbll(YbllglDto ybllglDto, User user) throws BusinessException {
        //通过领料明细里没删除的去删除
        List<YbllmxDto> yblldelmxs = JSON.parseArray(ybllglDto.getDelllmxids(), YbllmxDto.class);
        List<String> llmxids = new ArrayList<>();
        for (YbllmxDto yblldelmx : yblldelmxs) {
            llmxids.add(yblldelmx.getLlmxid());
        }
        YbllmxDto ybllmxDto_y = new YbllmxDto();
        ybllmxDto_y.setIds(llmxids);
        ybllmxDto_y.setLlid(ybllglDto.getLlid());
        //有可能一个都没删除,那执行结果一定为false所以不加判断
        ybllmxService.deleteByLlmxids(ybllmxDto_y);
        ybllmxService.updateYdbjByLlmxids(ybllmxDto_y);

        ybllglDto.setXgry(user.getYhid());
        boolean isSuccess = this.update(ybllglDto);
        if (!isSuccess){
            throw new BusinessException("msg", "保存失败!");
        }
        //添加的样本领料明细
        List<YbllglDto> yblladdmxs = JSON.parseArray(ybllglDto.getAddmxkcids(), YbllglDto.class);
        //样本库存
        YbkcxxDto ybkcxxDto = new YbkcxxDto();
        List<String> ybkcidlist = new ArrayList<>();
        //组装样本领料明细
        List<YbllmxDto> ybllmxDtos = new ArrayList<>();
        if (yblladdmxs!=null){
            for (YbllglDto dto : yblladdmxs) {
                YbllmxDto ybllmxDto = new YbllmxDto();
                if (StringUtil.isNotBlank(dto.getYbkcid())){
                    ybllmxDto.setLlmxid(StringUtil.generateUUID());
                    ybllmxDto.setLlid(ybllglDto.getLlid());
                    ybllmxDto.setYbkcid(dto.getYbkcid());
                    ybllmxDtos.add(ybllmxDto);
                    ybllmxDto.setLrry(user.getYhid());
                    ybkcidlist.add(ybllmxDto.getYbkcid());
                }
            }
            if (!ybllmxDtos.isEmpty()){
                boolean isSuccesstwo = ybllmxService.insertYbllmxDtos(ybllmxDtos);
                if (!isSuccesstwo){
                    throw new BusinessException("msg", "保存失败!");
                }
            }
            ybkcxxDto.setIds(ybkcidlist);
            //修改对应的预定标记
            if (ybkcxxDto.getIds()!=null&& !ybkcxxDto.getIds().isEmpty()){
                boolean isSuccessThree = ybkcxxService.updateYdbj(ybkcxxDto);
                if (!isSuccessThree){
                    throw new BusinessException("msg", "保存失败!");
                }
            }
        }
        return true;
    }
    /**
     * 样本领料出库
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean deliveryYbll(YbllglDto ybllglDto, User user) throws BusinessException {
        //发料人
        boolean isSuccess = this.updateFlr(ybllglDto);
        if (!isSuccess){
            throw new BusinessException("msg", "样本领料出库失败!");
        }
        List<YbllglDto> ybllglDtos = JSON.parseArray(ybllglDto.getYbllmx_json(), YbllglDto.class);
        List<String> ybllkcids = new ArrayList<>();

        for (YbllglDto ybllglDto_t : ybllglDtos) {
            ybllkcids.add(ybllglDto_t.getYbkcid());
        }
        YbkcxxDto ybkcxxDto_t = new YbkcxxDto();
        ybkcxxDto_t.setIds(ybllkcids);
        List<YbkcxxDto> dtoList = ybkcxxService.getDtoList(ybkcxxDto_t);
        List<SbglDto> sbglDtos = new ArrayList<>();
        for (YbkcxxDto ybkcxxDto : dtoList) {
            SbglDto sbglDto_sub = new SbglDto();
            sbglDto_sub.setSubcfs("1");
            sbglDto_sub.setSbid(ybkcxxDto.getHzid());
            sbglDtos.add(sbglDto_sub);
        }
        List<SbglDto> modSbglDtos = sbglDtos.stream().collect(Collectors.groupingBy(SbglDto::getSbid
                        //分组求和
                        , Collectors.mapping(e -> new BigDecimal(String.valueOf(e.getSubcfs())), Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))))
                //转为list
                .entrySet().stream().map(e -> new SbglDto(e.getKey(),"0", String.valueOf(e.getValue()))).collect(Collectors.toList());
        isSuccess = sbglService.updateYcfsDtos(modSbglDtos);
        if (!isSuccess){
            throw new BusinessException("msg", "样本领料出库失败!");
        }
        //删除对应库存
        YbkcxxDto ybkcxxDto = new YbkcxxDto();
        ybkcxxDto.setIds(ybllkcids);
        ybkcxxDto.setScry(user.getYhid());
        isSuccess = ybkcxxService.deleteListForCK(ybkcxxDto);
        if (!isSuccess){
            throw new BusinessException("msg", "样本领料出库失败!");
        }
        return true;
    }
    /**
     * 删除或废弃样本领料信息
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delYbll(YbllglDto ybllglDto, User user) throws BusinessException {
        //删除对应审核过程
        shgcService.deleteByYwids(ybllglDto.getIds());

        ybllglDto.setScry(user.getYhid());
        List<YbllglDto> ybllglDtos = this.getYbkcidByLlids(ybllglDto);
        List<String> kcids = new ArrayList<>();
        //出库的库存ids
        List<String> ckKcids = new ArrayList<>();
        for (YbllglDto dto : ybllglDtos) {
            kcids.add(dto.getYbkcid());
            if (StringUtil.isNotBlank(dto.getFlr())){
                ckKcids.add(dto.getYbkcid());
            }
        }
        YbkcxxDto ybkcxxDto_yd = new YbkcxxDto();
        ybkcxxDto_yd.setIds(kcids);
        // 预定标记改为0
        boolean isSuccess = ybkcxxService.updateYdbjForDelete(ybkcxxDto_yd);
        if (!isSuccess){
            throw new BusinessException("msg", "样本领料删除失败!");
        }
        if (!CollectionUtils.isEmpty(ckKcids)){
            YbkcxxDto ybkcxxDto = new YbkcxxDto();
            ybkcxxDto.setIds(ckKcids);
            //删除标记改为0
            isSuccess = ybkcxxService.updateScbjForDelete(ybkcxxDto);
            if (!isSuccess){
                throw new BusinessException("msg", "样本领料删除失败!");
            }
            //获取样本库存信息
            List<YbkcxxDto> dtoList = ybkcxxService.getDtoList(ybkcxxDto);
            List<SbglDto> sbglDtos = new ArrayList<>();
            for (YbkcxxDto dto : dtoList) {
                SbglDto sbglDto_add = new SbglDto();
                sbglDto_add.setAddcfs("1");
                sbglDto_add.setSbid(dto.getHzid());
                sbglDtos.add(sbglDto_add);
            }
            List<SbglDto> modSbglDtos = sbglDtos.stream().collect(Collectors.groupingBy(SbglDto::getSbid
                            //分组求和
                            , Collectors.mapping(e -> new BigDecimal(String.valueOf(e.getAddcfs())), Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))))
                    //转为list
                    .entrySet().stream().map(e -> new SbglDto(e.getKey(), String.valueOf(e.getValue()),"0")).collect(Collectors.toList());
            isSuccess = sbglService.updateYcfsDtos(modSbglDtos);
            if (!isSuccess){
                throw new BusinessException("msg", "样本领料删除失败!");
            }
        }
        // 删除对应领料管理
        isSuccess = this.deleteByLlids(ybllglDto);
        if (!isSuccess){
            throw new BusinessException("msg", "样本领料删除失败!");
        }
        YbllmxDto ybllmxDto = new YbllmxDto();
        ybllmxDto.setIds(ybllglDto.getIds());
        // 删除对应领料明细
        isSuccess = ybllmxService.deleteByLlids(ybllmxDto);
        if (!isSuccess){
            throw new BusinessException("msg", "样本领料删除失败!");
        }
        return true;
    }

    @Override
    public boolean deleteByLlids(YbllglDto ybllglDto) {
        return dao.deleteByLlids(ybllglDto);
    }

    @Override
    public List<YbllglDto> getYbkcidByLlids(YbllglDto ybllglDto) {
       return dao.getYbkcidByLlids(ybllglDto);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        YbllglDto ybllglDto = (YbllglDto)baseModel;
        if (!ybllglDto.getXgqlldh().equals(ybllglDto.getLldh())){
            // 校验领料单号是否重复
            boolean result = this.getDtoByLldh(ybllglDto);
            if (!result) {
                throw new BusinessException("msg","领料单号不允许重复!");
            }
        }
        return this.modSaveYbll(ybllglDto, operator);
    }

    @Override
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
        String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
//        String token = talkUtil.getToken();
        for (ShgcDto shgcDto : shgcList) {
            YbllglDto ybllglDto = new YbllglDto();
            ybllglDto.setLlid(shgcDto.getYwid());
            ybllglDto.setXgry(operator.getYhid());
            YbllglDto ybllglDto_t = getDto(ybllglDto);
            List<SpgwcyDto> spgwcyDtos = shgcDto.getSpgwcyDtos();
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                ybllglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (spgwcyDtos != null && !spgwcyDtos.isEmpty()) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), ICOMM_SH00026,
                                    xxglService.getMsg("ICOMM_SH00086", operator.getZsxm(), ybllglDto_t.getLldh(), ybllglDto_t.getSqrq(), ybllglDto_t.getLlrmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                ybllglDto.setZt(StatusEnum.CHECK_PASS.getCode());
                if (spgwcyDtos != null && !spgwcyDtos.isEmpty()) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), ICOMM_SH00006,
                                    xxglService.getMsg("ICOMM_SH00087", ybllglDto_t.getLldh(), ybllglDto_t.getLlrmc(), ybllglDto_t.getBmmc(), ybllglDto_t.getSqrq())
                            );
                        }
                    }
                }
            }else {
                ybllglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                // 发送钉钉消息
                if (spgwcyDtos != null && !spgwcyDtos.isEmpty()) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                        spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00003"), xxglService.getMsg("ICOMM_SH00086", operator.getZsxm(), ybllglDto_t.getLldh(), ybllglDto_t.getSqrq(), ybllglDto_t.getLlrmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                            }
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
                //发送钉钉消息--取消审核人员
                if(shgcDto.getNo_spgwcyDtos() != null && !shgcDto.getNo_spgwcyDtos().isEmpty()){
                    int size = shgcDto.getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
                            		shgcDto.getNo_spgwcyDtos().get(i).getYhid(),xxglService.getMsg("ICOMM_SH00004"), xxglService.getMsg("ICOMM_SH00086", operator.getZsxm(), ybllglDto_t.getLldh(), ybllglDto_t.getSqrq(), ybllglDto_t.getLlrmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(ybllglDto);
        }
        return true;
    }

    @Override
    public List<YbllglDto> getPagedDtoList(YbllglDto ybllglDto) {
        List<YbllglDto> list = dao.getPagedDtoList(ybllglDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_SAMPLE.getCode(), "zt", "llid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
        }
        return list;
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
                String llid = shgcDto.getYwid();
                YbllglDto ybllglDto = new YbllglDto();
                ybllglDto.setLlid(llid);
                ybllglDto.setXgry(operator.getYhid());
                ybllglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(ybllglDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String llid = shgcDto.getYwid();
                YbllglDto ybllglDto = new YbllglDto();
                ybllglDto.setLlid(llid);
                ybllglDto.setXgry(operator.getYhid());
                ybllglDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(ybllglDto);
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
        List<String> ids = (List<String>)param.get("ywids");
        YbllglDto ybllglDto = new YbllglDto();
        ybllglDto.setIds(ids);
        List<YbllglDto> dtoList = dao.getDtoByIds(ybllglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(YbllglDto dto:dtoList){
                list.add(dto.getLlid());
            }
        }
        map.put("list",list);
        return map;
    }
}

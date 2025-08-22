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
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.sample.dao.entities.JzkcxxDto;
import com.matridx.igams.sample.dao.entities.JzllglDto;
import com.matridx.igams.sample.dao.entities.JzllglModel;
import com.matridx.igams.sample.dao.entities.JzllmxDto;
import com.matridx.igams.sample.dao.post.IJzllglDao;
import com.matridx.igams.sample.service.svcinterface.IJzkcxxService;
import com.matridx.igams.sample.service.svcinterface.IJzllcService;
import com.matridx.igams.sample.service.svcinterface.IJzllglService;
import com.matridx.igams.sample.service.svcinterface.IJzllmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code @author:JYK}
 */
@Service
public class JzllglServiceImpl extends BaseBasicServiceImpl<JzllglDto, JzllglModel, IJzllglDao> implements IJzllglService, IAuditService {
    private final Logger logger = LoggerFactory.getLogger(JzllglServiceImpl.class);
    @Autowired
    ICommonService commonService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IJzllmxService jzllmxService;
    @Autowired
    IJzkcxxService jzkcxxService;
    @Autowired
    IJzllcService jzllcService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Override
    public List<JzllglDto> getPagedAuditPick(JzllglDto jzllglDto) {
        // 获取人员ID和履历号
        List<JzllglDto> t_sbyzList = dao.getPagedAuditPick(jzllglDto);
        if (t_sbyzList == null || t_sbyzList.isEmpty())
            return t_sbyzList;

        List<JzllglDto> sqList = dao.getAuditListPick(t_sbyzList);

        commonService.setSqrxm(sqList);

        return sqList;
    }

    @Override
    public List<JzllglDto> getDtoByIds(JzllglDto jzllglDto) {
        return dao.getDtoByIds(jzllglDto);
    }

    @Override
    public List<JzllglDto> getPagedDtoList(JzllglDto jzllglDto) {
        List<JzllglDto> list = dao.getPagedDtoList(jzllglDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GERM_PICK.getCode(), "zt", "llid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
        }
        return list;
    }

    @Override
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
        JzllglDto jzllglDto = (JzllglDto)baseModel;
        jzllglDto.setXgry(operator.getYhid());
        return modSaveGermPicking(jzllglDto);
    }

    @Override
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
//        String token = talkUtil.getToken();
        for (ShgcDto shgcDto : shgcList) {
            JzllglDto jzllglDto = new JzllglDto();
            jzllglDto.setLlid(shgcDto.getYwid());
            jzllglDto.setXgry(operator.getYhid());
            JzllglDto jzllglDto_t = getDto(jzllglDto);
            List<SpgwcyDto> spgwcyDtos = shgcDto.getSpgwcyDtos();
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                jzllglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (spgwcyDtos != null && !spgwcyDtos.isEmpty()) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(),
                                    xxglService.getMsg("ICOMM_SH00026"), xxglService.getMsg("ICOMM_JZLL0001", operator.getZsxm(), shgcDto.getShlbmc(), jzllglDto_t.getLldh(), jzllglDto_t.getLlrmc(), jzllglDto_t.getBmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                jzllglDto.setZt(StatusEnum.CHECK_PASS.getCode());
                if (spgwcyDtos != null && !spgwcyDtos.isEmpty()) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(),
                                    xxglService.getMsg("ICOMM_SH00006"), xxglService.getMsg("ICOMM_JZLL0002",
                                            operator.getZsxm(), shgcDto.getShlbmc(), jzllglDto_t.getLldh(), jzllglDto_t.getLlrmc(), jzllglDto_t.getBmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }else {
                jzllglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                // 发送钉钉消息
                if (spgwcyDtos != null && !spgwcyDtos.isEmpty()) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                        spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00003"), xxglService.getMsg("ICOMM_JZLL0003", operator.getZsxm(), shgcDto.getShlbmc(), jzllglDto_t.getLldh(), jzllglDto_t.getLlrmc(), jzllglDto_t.getBmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
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
                            		shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_JZLL0004",operator.getZsxm(),shgcDto.getShlbmc(),jzllglDto_t.getLldh(),jzllglDto_t.getLlrmc(),jzllglDto_t.getBmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(jzllglDto);
        }
        return true;
    }

    @Override
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        if (auditParam.isCancelOpe()) {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                String llid = shgcDto.getYwid();
                JzllglDto jzllglDto = new JzllglDto();
                jzllglDto.setXgry(operator.getYhid());
                jzllglDto.setLlid(llid);
                jzllglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(jzllglDto);
            }
        } else {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                String llid = shgcDto.getYwid();
                JzllglDto jzllglDto = new JzllglDto();
                jzllglDto.setXgry(operator.getYhid());
                jzllglDto.setLlid(llid);
                jzllglDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(jzllglDto);
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
        JzllglDto jzllglDto = new JzllglDto();
        jzllglDto.setIds(ids);
        List<JzllglDto> dtoList = dao.getDtoByIds(jzllglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(JzllglDto dto:dtoList){
                list.add(dto.getLlid());
            }
        }
        map.put("list",list);
        return map;
    }

    /**
     * 自动生成领料单号
     */
    public String generateLldh() {
        // TODO Auto-generated method stub
        // 生成规则: LL-20201022-01 LL-年份日期-流水号 。
        String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
        String prefix = "LL" + "-" + date + "-";
        // 查询流水号
        String serial = dao.getDjhSerial(prefix);
        return prefix + serial;
    }

    /**
     * 新增保存
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addSaveGermPicking(JzllglDto jzllglDto){
        jzllglDto.setLlid(StringUtil.generateUUID());
        jzllglDto.setZt(StatusEnum.CHECK_NO.getCode());
        int insert = dao.insert(jzllglDto);
        if(insert==0){
            return false;
        }
        List<JzllmxDto> jzllmxDtos = JSON.parseArray(jzllglDto.getLlmx_json(), JzllmxDto.class);
        List<JzkcxxDto> jzkcxxDtos=new ArrayList<>();
        for(JzllmxDto dto:jzllmxDtos){
            dto.setLlmxid(StringUtil.generateUUID());
            dto.setLlid(jzllglDto.getLlid());
            dto.setLrry(jzllglDto.getLrry());
            JzkcxxDto jzkcxxDto=new JzkcxxDto();
            jzkcxxDto.setJzkcid(dto.getJzkcid());
            BigDecimal yds=new BigDecimal(dto.getYds());
            BigDecimal qlsl=new BigDecimal(dto.getQlsl());
            jzkcxxDto.setYds(String.valueOf(yds.add(qlsl)));
            jzkcxxDto.setXgry(jzllglDto.getLrry());
            jzkcxxDtos.add(jzkcxxDto);
        }
        boolean isSuccess = jzllmxService.insertList(jzllmxDtos);
        if(!isSuccess){
            return false;
        }
        int update = jzkcxxService.updateList(jzkcxxDtos);
        if(update==0){
            return false;
        }
        jzllcService.deleteByRyid(jzllglDto.getLrry());
        return true;
    }

    /**
     * 修改保存
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean modSaveGermPicking(JzllglDto jzllglDto){
        int update = dao.update(jzllglDto);
        if(update==0){
            return false;
        }

        //将预定数还原到原本数值
        List<JzllmxDto> dtoListByllid = jzllmxService.getDtoListByllid(jzllglDto.getLlid());
        List<JzkcxxDto> list=new ArrayList<>();
        for(JzllmxDto dto:dtoListByllid){
            JzkcxxDto jzkcxxDto=new JzkcxxDto();
            jzkcxxDto.setYds(dto.getYds());
            jzkcxxDto.setJzkcid(dto.getJzkcid());
            jzkcxxDto.setXgry(jzllglDto.getLrry());
            list.add(jzkcxxDto);
        }
        int updateList = jzkcxxService.updateList(list);
        if(updateList==0){
            return false;
        }
        //将领料明细进行先删后增
        boolean isSuccess = jzllmxService.deleteById(jzllglDto.getLlid());
        if(!isSuccess){
            return false;
        }
        List<JzllmxDto> jzllmxDtos = JSON.parseArray(jzllglDto.getLlmx_json(), JzllmxDto.class);
        List<JzkcxxDto> jzkcxxDtos=new ArrayList<>();
        for(JzllmxDto dto:jzllmxDtos){
            dto.setLlmxid(StringUtil.generateUUID());
            dto.setLlid(jzllglDto.getLlid());
            dto.setLrry(jzllglDto.getXgry());
            JzkcxxDto jzkcxxDto=new JzkcxxDto();
            jzkcxxDto.setJzkcid(dto.getJzkcid());
            BigDecimal yds=new BigDecimal(dto.getYds());
            BigDecimal qlsl=new BigDecimal(dto.getQlsl());
            jzkcxxDto.setYds(String.valueOf(yds.add(qlsl)));
            jzkcxxDto.setXgry(jzllglDto.getXgry());
            jzkcxxDtos.add(jzkcxxDto);
        }
        isSuccess = jzllmxService.insertList(jzllmxDtos);
        if(!isSuccess){
            return false;
        }
        updateList = jzkcxxService.updateList(jzkcxxDtos);
        return updateList != 0;
    }

    /**
     * 出库保存
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deliverySaveGermPicking(JzllglDto jzllglDto){
        List<JzllmxDto> jzllmxDtos = JSON.parseArray(jzllglDto.getCkmx_json(), JzllmxDto.class);
        List<JzkcxxDto> jzkcxxDtos=new ArrayList<>();
        for(JzllmxDto dto:jzllmxDtos){
            dto.setXgry(jzllglDto.getXgry());
            JzkcxxDto jzkcxxDto=new JzkcxxDto();
            jzkcxxDto.setJzkcid(dto.getJzkcid());
            if( StringUtil.isNotBlank(dto.getCksl()) && StringUtil.isNotBlank(dto.getKcl()) ){
                BigDecimal cksl=new BigDecimal(dto.getCksl());
                BigDecimal kcl=new BigDecimal(dto.getKcl());
                jzkcxxDto.setKcl(String.valueOf(kcl.subtract(cksl)));
            }
            jzkcxxDto.setYds(dto.getYds());
            jzkcxxDto.setXgry(jzllglDto.getXgry());
            jzkcxxDtos.add(jzkcxxDto);
        }
        boolean isSuccess = jzllmxService.updateList(jzllmxDtos);
        if(!isSuccess){
            return false;
        }
        int updateList = jzkcxxService.updateList(jzkcxxDtos);
        if(updateList==0){
            return false;
        }
        jzllglDto.setCkzt("1");
        int update = dao.update(jzllglDto);
        return update != 0;
    }

    /**
     * 删除
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delGermPicking(JzllglDto jzllglDto){
        List<String> ids = jzllglDto.getIds();
        List<String> tgids = jzllglDto.getTgids();
        JzllmxDto jzllmxDto=new JzllmxDto();
        if(ids!=null&& !ids.isEmpty()){
            jzllmxDto.setIds(ids);
            List<JzllmxDto> dtoList = jzllmxService.getDtoList(jzllmxDto);
            if(dtoList!=null&& !dtoList.isEmpty()){
                List<JzkcxxDto> jzkcxxDtos=new ArrayList<>();
                for(JzllmxDto dto:dtoList){
                    JzkcxxDto jzkcxxDto=new JzkcxxDto();
                    jzkcxxDto.setJzkcid(dto.getJzkcid());
                    BigDecimal yds=new BigDecimal(dto.getYds());
                    BigDecimal qlsl=new BigDecimal(dto.getQlsl());
                    jzkcxxDto.setYds(String.valueOf(yds.subtract(qlsl)));
                    jzkcxxDto.setXgry(jzllglDto.getScry());
                    jzkcxxDtos.add(jzkcxxDto);
                }
                int updateList = jzkcxxService.updateList(jzkcxxDtos);
                if(updateList==0){
                    return false;
                }
            }
        }
        if(tgids!=null&& !tgids.isEmpty()){
            jzllmxDto.setIds(tgids);
            List<JzllmxDto> dtoList = jzllmxService.getDtoList(jzllmxDto);
            if(dtoList!=null&& !dtoList.isEmpty()){
                List<JzkcxxDto> jzkcxxDtos=new ArrayList<>();
                for(JzllmxDto dto:dtoList){
                    JzkcxxDto jzkcxxDto=new JzkcxxDto();
                    jzkcxxDto.setJzkcid(dto.getJzkcid());
                    if(StringUtil.isNotBlank(dto.getCksl())){
                        BigDecimal cksl=new BigDecimal(dto.getCksl());
                        BigDecimal kcl=new BigDecimal(dto.getKcl());
                        jzkcxxDto.setKcl(String.valueOf(kcl.add(cksl)));
                    }else{
                        BigDecimal yds=new BigDecimal(dto.getYds());
                        BigDecimal qlsl=new BigDecimal(dto.getQlsl());
                        jzkcxxDto.setYds(String.valueOf(yds.subtract(qlsl)));
                    }
                    jzkcxxDto.setXgry(jzllglDto.getScry());
                    jzkcxxDtos.add(jzkcxxDto);
                }
                int updateList = jzkcxxService.updateList(jzkcxxDtos);
                if(updateList==0){
                    return false;
                }
            }
        }

        if (ids != null&&tgids != null) {
            ids.addAll(tgids);
        }
        jzllmxDto.setIds(ids);
        boolean delete = jzllmxService.delete(jzllmxDto);
        if(!delete){
            return false;
        }
        jzllglDto.setIds(ids);
        int count = dao.delete(jzllglDto);
        return count != 0;
    }
}

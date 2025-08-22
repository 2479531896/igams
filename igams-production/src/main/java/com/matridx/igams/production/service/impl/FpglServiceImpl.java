package com.matridx.igams.production.service.impl;


import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.*;
import com.matridx.igams.production.dao.post.IFpglDao;
import com.matridx.igams.production.service.svcinterface.IFpglService;
import com.matridx.igams.production.service.svcinterface.IFpmxService;
import com.matridx.igams.production.service.svcinterface.IHtglService;
import com.matridx.igams.production.service.svcinterface.IHtmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
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

@Service
public class FpglServiceImpl extends BaseBasicServiceImpl<FpglDto, FpglModel, IFpglDao> implements IFpglService  , IAuditService {

    @Autowired
    IFpmxService fpmxService;
    @Autowired
    ICommonService commonservice;
    @Autowired
    IXxglService xxglService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IHtmxService htmxService;
    @Autowired
    IHtglService htglService;
    @Autowired
    IFpglDao fpglDao;
    private final Logger log = LoggerFactory.getLogger(FpglServiceImpl.class);
    /**
     * 新增
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertDto(FpglDto fpglDto){
        boolean isSuccess = insert(fpglDto);
        List<FpmxDto> fpmxDtoList = JSON.parseArray(fpglDto.getFpmx_json(), FpmxDto.class);
        if(!CollectionUtils.isEmpty(fpmxDtoList)){
            for(FpmxDto dto:fpmxDtoList){
                dto.setFpmxid(StringUtil.generateUUID());
                dto.setFpid(fpglDto.getFpid());
                dto.setFpsl(dto.getSl());
                dto.setFpje(dto.getHjje());
                dto.setLrry(fpglDto.getLrry());
            }
            isSuccess=fpmxService.insertList(fpmxDtoList);
            if(isSuccess){
                FpmxDto fpmxDto=new FpmxDto();
                fpmxDto.setFpid(fpglDto.getFpid());
               if(!CollectionUtils.isEmpty(fpmxDtoList)){
                    List<String> ids=new ArrayList<>();
                    List<String> htids=new ArrayList<>();
                    for(FpmxDto dto:fpmxDtoList){
                        String sum = fpmxService.getSumById(dto.getHtmxid());
                        Double zsl=Double.parseDouble(sum);
                        HtmxDto htmxDto = htmxService.getDtoById(dto.getHtmxid());
                        if(!htids.contains(htmxDto.getHtid())){
                            htids.add(htmxDto.getHtid());
                        }
                        Double sl=Double.parseDouble(htmxDto.getSl());
                        if(sl-zsl==0){
                            htmxDto.setSffpwh("1");
                            ids.add(dto.getHtmxid());
                        }
                    }
                    if(!CollectionUtils.isEmpty(ids)){
                        HtmxDto htmxDto=new HtmxDto();
                        htmxDto.setIds(ids);
                        htmxDto.setSffpwh("1");
                        htmxService.updateSffpwh(htmxDto);
                    }
                    List<FpmxDto> sumGroupByHtid = fpmxService.getSumGroupByHtid(fpmxDto);
                    for(String s:htids){
                        BigDecimal zje=new BigDecimal("0");
                        for(FpmxDto dto:sumGroupByHtid){
                            if(dto.getHtid().equals(s)){
                                zje=new BigDecimal(dto.getSum());
                            }
                        }
                        boolean fkflg=false;
                        boolean fpflg=false;
                        HtmxDto htmxDto_t=new HtmxDto();
                        htmxDto_t.setHtid(s);
                        htmxDto_t.setSffpwh("0");
                        List<HtmxDto> list = htmxService.getListForInvoice(htmxDto_t);
                        HtglDto dtoById = htglService.getDtoById(s);
                        HtglDto htglDto=new HtglDto();
                        if(dtoById!=null){
                            if(StringUtil.isNotBlank(dtoById.getWfje())){
                                BigDecimal wfje=new BigDecimal(dtoById.getWfje());
                                if(wfje.compareTo(BigDecimal.ZERO)!=1){
                                    fkflg=true;
                                }
                            }
                            if(StringUtil.isBlank(dtoById.getFpje())){
                                htglDto.setFpje(String.valueOf(zje));
                            }else{
                                BigDecimal fpje=new BigDecimal(dtoById.getFpje());
                                htglDto.setFpje(String.valueOf(fpje.add(zje)));
                            }
                        }
                        htglDto.setHtid(s);
                        htglDto.setXgry(fpglDto.getLrry());
                        if(!CollectionUtils.isEmpty(list)){
                            htglDto.setSffpwh("2");
                        }else{
                            fpflg=true;
                            htglDto.setSffpwh("1");
                        }
                        if(fpflg&&fkflg){
                            htglDto.setWcbj("1");
                        }
                        htglService.update(htglDto);
                    }
                }
            }
        }
        return isSuccess;
    }


    /**
     * 修改
     * @param fpglDto
     
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateDto(FpglDto fpglDto){
        boolean isSuccess = update(fpglDto);
        List<FpmxDto> fpmxDtoList = JSON.parseArray(fpglDto.getFpmx_json(), FpmxDto.class);
        if(!CollectionUtils.isEmpty(fpmxDtoList)){
            FpmxDto fpmxDto=new FpmxDto();
            fpmxDto.setFpid(fpglDto.getFpid());
            List<FpmxDto> dtoList = fpmxService.getDtoList(fpmxDto);
            if(!CollectionUtils.isEmpty(dtoList)){
                List<String> ids=new ArrayList<>();
                List<String> htids=new ArrayList<>();
                for(FpmxDto dto:dtoList){
                    ids.add(dto.getHtmxid());
                    HtmxDto htmxDto = htmxService.getDtoById(dto.getHtmxid());
                    if(!htids.contains(htmxDto.getHtid())){
                        htids.add(htmxDto.getHtid());
                    }
                }
                HtmxDto htmxDto = new HtmxDto();
                htmxDto.setIds(ids);
                htmxDto.setSffpwh("0");
                htmxService.updateSffpwh(htmxDto);
                List<FpmxDto> sumGroupByHtid = fpmxService.getSumGroupByHtid(fpmxDto);
                for(String str:htids){
                    BigDecimal zje=new BigDecimal("0");
                    for(FpmxDto dto:sumGroupByHtid){
                        if(dto.getHtid().equals(str)){
                            zje=new BigDecimal(dto.getSum());
                        }
                    }
                    boolean fkflg=false;
                    boolean fpflg=false;
                    HtmxDto htmxDto_t=new HtmxDto();
                    htmxDto_t.setHtid(str);
                    htmxDto_t.setSffpwh("0");
                    List<HtmxDto> list = htmxService.getListForInvoice(htmxDto_t);
                    HtglDto dtoById = htglService.getDtoById(str);
                    HtglDto htglDto=new HtglDto();
                    if(dtoById!=null){
                        if(StringUtil.isNotBlank(dtoById.getWfje())){
                            BigDecimal wfje=new BigDecimal(dtoById.getWfje());
                            if(wfje.compareTo(BigDecimal.ZERO)!=1){
                                fkflg=true;
                            }
                        }
                        if(StringUtil.isNotBlank(dtoById.getFpje())){
                            BigDecimal fpje=new BigDecimal(dtoById.getFpje());
                            htglDto.setFpje(String.valueOf(fpje.subtract(zje)));
                        }
                    }
                    htglDto.setHtid(str);
                    htglDto.setXgry(fpglDto.getLrry());
                    if(!CollectionUtils.isEmpty(list)){
                        HtmxDto htmxDto_A=new HtmxDto();
                        htmxDto_A.setHtid(str);
                        List<HtmxDto> list_t = htmxService.getListForInvoice(htmxDto_A);
                        if(list.size()==list_t.size()){
                            htglDto.setSffpwh("0");
                        }else{
                            htglDto.setSffpwh("2");
                        }
                    }else{
                        fpflg=true;
                        htglDto.setSffpwh("1");
                    }
                    if(fpflg&&fkflg){
                        htglDto.setWcbj("1");
                    }else{
                        htglDto.setWcbj("0");
                    }
                    htglService.update(htglDto);
                }
            }
            fpmxService.deleteByFpid(fpmxDto);
            for(FpmxDto dto:fpmxDtoList){
                dto.setFpmxid(StringUtil.generateUUID());
                dto.setFpid(fpglDto.getFpid());
                dto.setFpsl(dto.getSl());
                dto.setFpje(dto.getHjje());
                dto.setLrry(fpglDto.getLrry());
            }
            isSuccess=fpmxService.insertList(fpmxDtoList);
            if(isSuccess){
                FpmxDto fpmxDto1=new FpmxDto();
                fpmxDto1.setFpid(fpglDto.getFpid());
                if(!CollectionUtils.isEmpty(fpmxDtoList)){
                    List<String> ids_t=new ArrayList<>();
                    List<String> htids=new ArrayList<>();
                    for(FpmxDto dto:fpmxDtoList){
                        String sum = fpmxService.getSumById(dto.getHtmxid());
                        Double zsl=Double.parseDouble(sum);
                        HtmxDto htmxDto2 = htmxService.getDtoById(dto.getHtmxid());
                        if(!htids.contains(htmxDto2.getHtid())){
                            htids.add(htmxDto2.getHtid());
                        }
                        Double sl=Double.parseDouble(htmxDto2.getSl());
                        if(sl-zsl==0){
                            ids_t.add(dto.getHtmxid());
                        }
                    }
                    if(!CollectionUtils.isEmpty(ids_t)){
                        HtmxDto htmxDto3=new HtmxDto();
                        htmxDto3.setIds(ids_t);
                        htmxDto3.setSffpwh("1");
                        htmxService.updateSffpwh(htmxDto3);
                    }
                    List<FpmxDto> sumGroupByHtid_t = fpmxService.getSumGroupByHtid(fpmxDto1);
                    for(String s:htids){
                        BigDecimal zje=new BigDecimal("0");
                        for(FpmxDto dto:sumGroupByHtid_t){
                            if(dto.getHtid().equals(s)){
                                zje=new BigDecimal(dto.getSum());
                            }
                        }
                        boolean fkflg=false;
                        boolean fpflg=false;
                        HtmxDto htmxDto_t=new HtmxDto();
                        htmxDto_t.setHtid(s);
                        htmxDto_t.setSffpwh("0");
                        List<HtmxDto> list = htmxService.getListForInvoice(htmxDto_t);
                        HtglDto dtoById = htglService.getDtoById(s);
                        HtglDto htglDto=new HtglDto();
                        if(dtoById!=null){
                            if(StringUtil.isNotBlank(dtoById.getWfje())){
                                BigDecimal wfje=new BigDecimal(dtoById.getWfje());
                                if(wfje.compareTo(BigDecimal.ZERO)!=1){
                                    fkflg=true;
                                }
                            }
                            if(StringUtil.isBlank(dtoById.getFpje())){
                                htglDto.setFpje(String.valueOf(zje));
                            }else{
                                BigDecimal fpje=new BigDecimal(dtoById.getFpje());
                                htglDto.setFpje(String.valueOf(fpje.add(zje)));
                            }
                        }
                        htglDto.setHtid(s);
                        htglDto.setXgry(fpglDto.getLrry());
                        if(!CollectionUtils.isEmpty(list)){
                            htglDto.setSffpwh("2");
                        }else{
                            fpflg=true;
                            htglDto.setSffpwh("1");
                        }
                        if(fpflg&&fkflg){
                            htglDto.setWcbj("1");
                        }
                        htglService.update(htglDto);
                    }
                }
            }
        }
        return isSuccess;
    }

    /**
     * 删除
     * @param fpglDto
     
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean deleteDto(FpglDto fpglDto,User user){
        FpmxDto fpmxDto=new FpmxDto();
        boolean isSuccess=false;
        if(!CollectionUtils.isEmpty(fpglDto.getIds())){
            fpmxDto.setIds(fpglDto.getIds());
            List<FpmxDto> dtoList = fpmxService.getDtoList(fpmxDto);
            if(!CollectionUtils.isEmpty(dtoList)){
                List<String> ids=new ArrayList<>();
                List<String> htids=new ArrayList<>();
                for(FpmxDto dto:dtoList){
                    ids.add(dto.getHtmxid());
                    HtmxDto htmxDto = htmxService.getDtoById(dto.getHtmxid());
                    if(!htids.contains(htmxDto.getHtid())){
                        htids.add(htmxDto.getHtid());
                    }
                }
                HtmxDto htmxDto = new HtmxDto();
                htmxDto.setIds(ids);
                htmxDto.setSffpwh("0");
                htmxService.updateSffpwh(htmxDto);
                List<FpmxDto> sumGroupByHtid = fpmxService.getSumGroupByHtid(fpmxDto);
                for(String str:htids){
                    BigDecimal zje=new BigDecimal("0");
                    for(FpmxDto dto:sumGroupByHtid){
                        if(dto.getHtid().equals(str)){
                            zje=new BigDecimal(dto.getSum());
                        }
                    }
                    boolean fkflg=false;
                    boolean fpflg=false;
                    HtmxDto htmxDto_t=new HtmxDto();
                    htmxDto_t.setHtid(str);
                    htmxDto_t.setSffpwh("0");
                    List<HtmxDto> list = htmxService.getListForInvoice(htmxDto_t);
                    HtglDto dtoById = htglService.getDtoById(str);
                    HtglDto htglDto=new HtglDto();
                    if(dtoById!=null){
                        if(StringUtil.isNotBlank(dtoById.getWfje())){
                            BigDecimal wfje=new BigDecimal(dtoById.getWfje());
                            if(wfje.compareTo(BigDecimal.ZERO)!=1){
                                fkflg=true;
                            }
                        }
                        if(StringUtil.isNotBlank(dtoById.getFpje())){
                            BigDecimal fpje=new BigDecimal(dtoById.getFpje());
                            htglDto.setFpje(String.valueOf(fpje.subtract(zje)));
                        }
                    }
                    htglDto.setHtid(str);
                    htglDto.setXgry(fpglDto.getLrry());
                    if(!CollectionUtils.isEmpty(list)){
                        HtmxDto htmxDto_A=new HtmxDto();
                        htmxDto_A.setHtid(str);
                        List<HtmxDto> list_t = htmxService.getListForInvoice(htmxDto_A);
                        if(list.size()==list_t.size()){
                            htglDto.setSffpwh("0");
                        }else{
                            htglDto.setSffpwh("2");
                        }
                    }else{
                        fpflg=true;
                        htglDto.setSffpwh("1");
                    }
                    if(fpflg&&fkflg){
                        htglDto.setWcbj("1");
                    }else{
                        htglDto.setWcbj("0");
                    }
                    htglService.update(htglDto);
                }
            }
            fpglDto.setScry(user.getYhid());
            fpmxDto.setScry(fpglDto.getScry());
            fpmxService.delete(fpmxDto);
            int num = fpglDao.delete(fpglDto);
            if(num>0){
                isSuccess=true;
            }
        }
        return isSuccess;
    }

    /**
     * 验证发票代码和发票号
     * @param fpglDto
     
     */
    public List<FpglDto> verifyDmAndFph(FpglDto fpglDto){
        return dao.verifyDmAndFph(fpglDto);
    }

    /**
     * 审核列表
     * @param fpglDto
     
     */
    public List<FpglDto> getPagedAuditInvoice(FpglDto fpglDto){
        // 获取人员ID和履历号
        List<FpglDto> t_sbyzList= dao.getPagedAuditInvoice(fpglDto);

        if(CollectionUtils.isEmpty(t_sbyzList))
            return t_sbyzList;

        List<FpglDto> sqList = dao.getAuditListInvoice(t_sbyzList);

        commonservice.setSqrxm(sqList);

        return sqList;
    }

    @Override
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
        return true;
    }

    @Override
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
//        String token = talkUtil.getToken();
        for (ShgcDto shgcDto : shgcList) {
            FpglDto fpglDto = new FpglDto();
            fpglDto.setFpid(shgcDto.getYwid());
            fpglDto.setXgry(operator.getYhid());
            FpglDto fpglDto_t = getDto(fpglDto);
            List<SpgwcyDto> spgwcyDtos = shgcDto.getSpgwcyDtos();
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                fpglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                fpglDto.setScbj("1");
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(),
                                    xxglService.getMsg("ICOMM_SH00026"), xxglService.getMsg("ICOMM_SH00001", operator.getZsxm(), shgcDto.getShlbmc(), fpglDto_t.getFpzlmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                fpglDto.setZt(StatusEnum.CHECK_PASS.getCode());
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(),
                                    xxglService.getMsg("ICOMM_SH00006"), xxglService.getMsg("ICOMM_SH00016",
                                            operator.getZsxm(), shgcDto.getShlbmc(), fpglDto_t.getFpzlmc(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }else {
                fpglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(), spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                        spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00003"), xxglService.getMsg("ICOMM_SH00001", operator.getZsxm(), shgcDto.getShlbmc(), fpglDto_t.getFpzlmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
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
                            		shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,fpglDto_t.getFpzlmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(fpglDto);
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
                String fpid = shgcDto.getYwid();
                FpglDto fpglDto = new FpglDto();
                fpglDto.setFpid(fpid);
                fpglDto.setXgry(operator.getYhid());
                fpglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(fpglDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String fpid = shgcDto.getYwid();
                FpglDto fpglDto = new FpglDto();
                fpglDto.setFpid(fpid);
                fpglDto.setXgry(operator.getYhid());
                fpglDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(fpglDto);
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
        FpglDto fpglDto = new FpglDto();
        fpglDto.setIds(ids);
        List<FpglDto> dtoList = dao.getDtoList(fpglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(FpglDto dto:dtoList){
                list.add(dto.getFpid());
            }
        }
        map.put("list",list);
        return map;
    }

}

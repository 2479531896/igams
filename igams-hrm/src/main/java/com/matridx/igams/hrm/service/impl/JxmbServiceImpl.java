package com.matridx.igams.hrm.service.impl;
import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
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
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.hrm.dao.entities.*;
import com.matridx.igams.hrm.dao.post.IJxmbDao;
import com.matridx.igams.hrm.service.svcinterface.ICsszService;
import com.matridx.igams.hrm.service.svcinterface.IGrjxService;
import com.matridx.igams.hrm.service.svcinterface.IJxmbService;
import com.matridx.igams.hrm.service.svcinterface.IJxmbmxService;
import com.matridx.igams.hrm.service.svcinterface.IMbszService;
import com.matridx.igams.hrm.service.svcinterface.IQzszService;
import com.matridx.igams.hrm.service.svcinterface.ISyfwService;
import com.matridx.igams.hrm.service.svcinterface.IYghmcService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author:JYK
 */
@Service
public class JxmbServiceImpl extends BaseBasicServiceImpl<JxmbDto, JxmbModel, IJxmbDao> implements IJxmbService, IAuditService , IFileImport{
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ICommonService commonService;
    @Autowired
    ICsszService csszService;
    @Autowired
    IMbszService mbszService;
    @Autowired
    IJxmbmxService jxmbmxService;
    @Autowired
    ISyfwService syfwService;
    @Autowired
    IQzszService qzszService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IYghmcService yghmcService;
    @Lazy
    @Autowired
    IGrjxService grjxService;
    private final Logger log = LoggerFactory.getLogger(JxmbServiceImpl.class);
    /**
     * 绩效模板列表（查询审核状态）
     */
    @Override
    public List<JxmbDto> getPagedDtoList(JxmbDto jxmbDto) {
        List<JxmbDto> list = dao.getPagedDtoList(jxmbDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_PERFORMANCE_TEMPLATE.getCode(), "zt", "jxmbid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMsg());
        }
        return list;
    }
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveTemplate(JxmbDto jxmbDto) throws BusinessException {
        if ("0".equals(jxmbDto.getLx())&&"tj".equals(jxmbDto.getSignal())){
            jxmbDto.setZt("80");
        }else
            jxmbDto.setZt("00");
        if ("tj".equals(jxmbDto.getSignal())){
            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            jxmbDto.setTjsj(dateFormat.format(date));
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        CsszDto csszDto=csszService.getDtoByKhlx(jxmbDto.getKhlx());
        if (StringUtil.isNotBlank(csszDto.getFsxz()) && Double.parseDouble(csszDto.getFsxz()) != Double.parseDouble(jxmbDto.getZf())){
            throw new BusinessException("该绩效类型的分数限制为"+csszDto.getFsxz()+"分，您当前绩效目标的分数与限制分数不一致，请修改绩效目标分数！");
        }
        String sxrq;
        String yxq;
        Calendar calendar = Calendar.getInstance();
        long syrqL;
        try {
            calendar.setTime(DateUtils.parse(jxmbDto.getSyrq()));
        } catch (Exception e) {
            throw new BusinessException("使用日期转换失败!");
        }
        long kssj;
        int khzqcskz2 = Integer.parseInt(csszDto.getKhzqcskz2());
        //每周七天
        int khzqWeekcskz2 = new BigDecimal(csszDto.getKhzqcskz2()).multiply(new BigDecimal(7)).intValue();
        if ("WW".equals(csszDto.getKhzqcskz1())) {
            calendar.set(Calendar.DAY_OF_WEEK,calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
            calendar.add(Calendar.DATE,1);
            syrqL = calendar.getTime().getTime();
            kssj = calendar.getTime().getTime();
            calendar.add(Calendar.DAY_OF_WEEK,khzqWeekcskz2);
        } else if ("YY".equals(csszDto.getKhzqcskz1())) {
            calendar.set(Calendar.DAY_OF_YEAR,calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
            syrqL = calendar.getTime().getTime();
            kssj = calendar.getTime().getTime();
            calendar.add(Calendar.YEAR,khzqcskz2);
        } else if ("MM".equals(csszDto.getKhzqcskz1())) {
            calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            syrqL = calendar.getTime().getTime();
            kssj = calendar.getTime().getTime();
            calendar.add(Calendar.MONTH,khzqcskz2);
        } else {
            throw new BusinessException("考核周期参数扩展1不合法!");
        }
        long jssj =  calendar.getTime().getTime();
        int yc = 0;
        while (!(syrqL>=kssj&&syrqL<=jssj)){
            kssj = jssj;
            if ("WW".equals(csszDto.getKhzqcskz1())) {
                calendar.add(Calendar.DATE,khzqWeekcskz2);
            } else if ("YY".equals(csszDto.getKhzqcskz1())) {
                calendar.add(Calendar.YEAR,khzqcskz2);
            } else if ("MM".equals(csszDto.getKhzqcskz1())) {
                calendar.add(Calendar.MONTH,khzqcskz2);
            }
            jssj = calendar.getTime().getTime();
            if (yc++==100){
                throw new BusinessException("初始设置异常请联系管理员核对!");
            }
        }

        Calendar yxqIn = Calendar.getInstance();
        yxqIn.setTime(new Date(kssj));
        int yxzq = Integer.parseInt(csszDto.getYxzq());
        if ("WW".equals(csszDto.getKhzqcskz1())) {
            yxqIn.add(Calendar.DAY_OF_WEEK,khzqWeekcskz2*yxzq);
        } else if ("YY".equals(csszDto.getKhzqcskz1())) {
            yxqIn.add(Calendar.YEAR,khzqcskz2*yxzq);
        } else if ("MM".equals(csszDto.getKhzqcskz1())) {
            yxqIn.add(Calendar.MONTH,khzqcskz2*yxzq);
        }
        yxqIn.add(Calendar.DATE,-1);
        yxq = format.format(yxqIn.getTime());
        sxrq = format.format(kssj);
        jxmbDto.setJxmbid(StringUtil.generateUUID());
        boolean isSuccess=insert(jxmbDto);
        if (isSuccess){
            if(StringUtil.isNotBlank(sxrq)&&StringUtil.isNotBlank(yxq)){
                MbszDto mbszDto=new MbszDto();
                jxmbDto.setMbshid(csszDto.getMbshid());
                mbszDto.setMbszid(StringUtil.generateUUID());
                mbszDto.setMbid(jxmbDto.getJxmbid());
                mbszDto.setKhzq(csszDto.getKhzq());
                mbszDto.setXffrq(csszDto.getXffrq());
                mbszDto.setFfkhy(csszDto.getFfkhy());
                mbszDto.setXjzrq(csszDto.getXjzrq());
                mbszDto.setJzkhy(csszDto.getJzkhy());
                mbszDto.setXjxtx(csszDto.getXjxtx());
                mbszDto.setZdtj(csszDto.getZdtj());
                mbszDto.setMbjb(csszDto.getMbjb());
                mbszDto.setZp(csszDto.getZp());
                mbszDto.setMbshid(csszDto.getMbshid());
                mbszDto.setJxtzsj(csszDto.getJxtzsj());
                mbszDto.setMbtzsj(csszDto.getMbtzsj());
                mbszDto.setKhlx(csszDto.getKhlx());
                mbszDto.setJxshid(csszDto.getJxshid());
                mbszDto.setMblx(csszDto.getMblx());
                mbszDto.setMbzlx(csszDto.getMbzlx());
                mbszDto.setLrry(jxmbDto.getLrry());
                mbszDto.setFsxz(csszDto.getFsxz());
                // //生效期
                mbszDto.setSxrq(sxrq);
                // //失效期
                mbszDto.setYxq(yxq);
                isSuccess=mbszService.insert(mbszDto);
                jxmbDto.setMbshid(mbszDto.getMbshid());
                QzszDto qzszDto = new QzszDto();
                qzszDto.setCsszid(csszDto.getCsszid());
                List<QzszDto> qzszDtos = qzszService.getDtoList(qzszDto);
                for (QzszDto dto : qzszDtos) {
                    dto.setQzszid(StringUtil.generateUUID());
                    dto.setCsszid(null);
                    dto.setJxmbid(jxmbDto.getJxmbid());
                    dto.setMbszid(mbszDto.getMbszid());
                }
                if (!CollectionUtils.isEmpty(qzszDtos)){
                    qzszService.insertQzszDtos(qzszDtos);
                }
            }else {
                throw new BusinessException("message","引用初始设置生成有效期异常，请联系管理员核对!");
            }
        }else {
            throw new BusinessException("message","保存绩效模板失败");
        }
        if (isSuccess){
            //保存绩效明细
            List<JxmbmxDto> dtos = JSON.parseArray(jxmbDto.getJxmbmx_json(), JxmbmxDto.class);
            List<JxmbmxDto> jxmbmxDtoList=new ArrayList<>();
                if (!CollectionUtils.isEmpty(dtos)) {
                    for (JxmbmxDto dto : dtos) {
                        String jxmbmxid = StringUtil.generateUUID();
                        dto.setJxmbmxid(jxmbmxid);
                        dto.setJxmbid(jxmbDto.getJxmbid());
                        jxmbmxDtoList.add(dto);
                        dto.setLrry(jxmbDto.getLrry());
                        if (!CollectionUtils.isEmpty(dto.getChildren())) {
                            getJxmbmxTreeList(dto.getChildren(), jxmbmxid, jxmbmxDtoList, jxmbDto.getJxmbid(), jxmbDto.getLrry());
                        }
                    }
                if (!CollectionUtils.isEmpty(jxmbmxDtoList)) {
                    for (int i=0;i<jxmbmxDtoList.size();i++){
                        int xh=i+1;
                        jxmbmxDtoList.get(i).setXh(String.valueOf(xh));
                    }
                    isSuccess = jxmbmxService.insertMbmxList(jxmbmxDtoList);
                }
            }
        }else {
            throw new BusinessException("message","保存模板设置失败");
        }
        if (isSuccess){
            if ("1".equals(jxmbDto.getLx())){
                SyfwDto syfwDto=new SyfwDto();
                syfwDto.setSyfwid(StringUtil.generateUUID());
                syfwDto.setJxmbid(jxmbDto.getJxmbid());
                syfwDto.setYhid(jxmbDto.getLrry());
                syfwDto.setZt("0");
                isSuccess=syfwService.insert(syfwDto);
                if (!isSuccess){
                    throw new BusinessException("message","保存适用范围失败");
                }
            }
        }else {
            throw new BusinessException("messsage","保存模板明细失败");
        }
        return isSuccess;
    }
    public void getJxmbmxTreeList(List<JxmbmxDto> list,String sjid,List<JxmbmxDto> jxmbmxDtoList,String jxmbid,String ryid){
        for (JxmbmxDto jxmbmxDto : list) {
            String jxmbmxid = StringUtil.generateUUID();
            jxmbmxDto.setJxmbmxid(jxmbmxid);
            jxmbmxDto.setJxmbid(jxmbid);
            jxmbmxDto.setZbsjid(sjid);
            jxmbmxDto.setLrry(ryid);
            jxmbmxDtoList.add(jxmbmxDto);
            if (!CollectionUtils.isEmpty(jxmbmxDto.getChildren())) {
                getJxmbmxTreeList(jxmbmxDto.getChildren(), jxmbmxid, jxmbmxDtoList, jxmbid, ryid);
            }
        }
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveTemplate(JxmbDto jxmbDto) throws BusinessException {
        MbszDto mbszDto_check = mbszService.getDtoById(jxmbDto.getJxmbid());
        if (StringUtil.isNotBlank(mbszDto_check.getFsxz()) && Double.parseDouble(mbszDto_check.getFsxz()) != Double.parseDouble(jxmbDto.getZf())){
            throw new BusinessException("该绩效类型的分数限制为"+mbszDto_check.getFsxz()+"分，您当前绩效目标的分数与限制分数不一致，请修改绩效目标分数！");
        }
        if ("0".equals(jxmbDto.getLx())&&"tj".equals(jxmbDto.getSignal())){
            jxmbDto.setZt("80");
        }
        if ("tj".equals(jxmbDto.getSignal())){
            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            jxmbDto.setTjsj(dateFormat.format(date));
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        CsszDto csszDto=csszService.getDtoByKhlx(mbszDto_check.getKhlx());
        String sxrq;
        String yxq;
        Calendar calendar = Calendar.getInstance();
        long syrqL;
        try {
            calendar.setTime(DateUtils.parse(jxmbDto.getSyrq()));
        } catch (Exception e) {
            throw new BusinessException("使用日期转换失败!");
        }
        long kssj;
        int khzqcskz2 = Integer.parseInt(csszDto.getKhzqcskz2());
        //每周七天
        int khzqWeekcskz2 = new BigDecimal(csszDto.getKhzqcskz2()).multiply(new BigDecimal(7)).intValue();
        if ("WW".equals(csszDto.getKhzqcskz1())) {
            calendar.set(Calendar.DAY_OF_WEEK,calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
            calendar.add(Calendar.DATE,1);
            syrqL = calendar.getTime().getTime();
            kssj = calendar.getTime().getTime();
            calendar.add(Calendar.DAY_OF_WEEK,khzqWeekcskz2);
        } else if ("YY".equals(csszDto.getKhzqcskz1())) {
            calendar.set(Calendar.DAY_OF_YEAR,calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
            syrqL = calendar.getTime().getTime();
            kssj = calendar.getTime().getTime();
            calendar.add(Calendar.YEAR,khzqcskz2);
        } else if ("MM".equals(csszDto.getKhzqcskz1())) {
            calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            syrqL = calendar.getTime().getTime();
            kssj = calendar.getTime().getTime();
            calendar.add(Calendar.MONTH,khzqcskz2);
        } else {
            throw new BusinessException("考核周期参数扩展1不合法!");
        }
        long jssj =  calendar.getTime().getTime();
        int yc = 0;
        while (!(syrqL>=kssj&&syrqL<=jssj)){
            kssj = jssj;
            if ("WW".equals(csszDto.getKhzqcskz1())) {
                calendar.add(Calendar.DATE,khzqWeekcskz2);
            } else if ("YY".equals(csszDto.getKhzqcskz1())) {
                calendar.add(Calendar.YEAR,khzqcskz2);
            } else if ("MM".equals(csszDto.getKhzqcskz1())) {
                calendar.add(Calendar.MONTH,khzqcskz2);
            }
            jssj = calendar.getTime().getTime();
            if (yc++==100){
                throw new BusinessException("初始设置异常请联系管理员核对!");
            }
        }

        Calendar yxqIn = Calendar.getInstance();
        yxqIn.setTime(new Date(kssj));
        int yxzq = Integer.parseInt(csszDto.getYxzq());
        if ("WW".equals(csszDto.getKhzqcskz1())) {
            yxqIn.add(Calendar.DAY_OF_WEEK,khzqWeekcskz2*yxzq);
        } else if ("YY".equals(csszDto.getKhzqcskz1())) {
            yxqIn.add(Calendar.YEAR,khzqcskz2*yxzq);
        } else if ("MM".equals(csszDto.getKhzqcskz1())) {
            yxqIn.add(Calendar.MONTH,khzqcskz2*yxzq);
        }
        yxqIn.add(Calendar.DATE,-1);
        yxq = format.format(yxqIn.getTime());
        sxrq = format.format(kssj);
        jxmbDto.setSxrq(sxrq);
        jxmbDto.setYxq(yxq);
        MbszDto mbszDto = new MbszDto();
        mbszDto.setMbid(jxmbDto.getJxmbid());
        mbszDto.setSxrq(sxrq);
        mbszDto.setYxq(yxq);
        mbszDto.setXgry(jxmbDto.getXgry());
        boolean isSuccess = mbszService.updateSxrqAndYxq(mbszDto);
        if (!isSuccess){
            throw new BusinessException("msg","修改生效日期和有效期失败！");
        }
        isSuccess=dao.updatePageEvent(jxmbDto);
        if (isSuccess)
        {
            //修改绩效明细
            List<JxmbmxDto> dtos = JSON.parseArray(jxmbDto.getJxmbmx_json(), JxmbmxDto.class);
            isSuccess=jxmbmxService.deleteByJxmbid(jxmbDto.getJxmbid());
            if (isSuccess){
                if (!CollectionUtils.isEmpty(dtos)) {
                    List<JxmbmxDto> jxmbmxDtoList = new ArrayList<>();
                    for (JxmbmxDto dto : dtos) {
                        String jxmbmxid = StringUtil.generateUUID();
                        dto.setJxmbmxid(jxmbmxid);
                        dto.setJxmbid(jxmbDto.getJxmbid());
                        dto.setLrry(jxmbDto.getXgry());
                        jxmbmxDtoList.add(dto);
                        if (!CollectionUtils.isEmpty(dto.getChildren())) {
                            getJxmbmxTreeList(dto.getChildren(), jxmbmxid, jxmbmxDtoList, jxmbDto.getJxmbid(), jxmbDto.getXgry());
                        }
                    }
                    if (!CollectionUtils.isEmpty(jxmbmxDtoList)) {
                        for (int i=0;i<jxmbmxDtoList.size();i++){
                            int xh=i+1;
                            jxmbmxDtoList.get(i).setXh(String.valueOf(xh));
                        }
                        isSuccess = jxmbmxService.insertMbmxList(jxmbmxDtoList);
                    }
                }
            }
            else {
                throw new BusinessException("msg","更新绩效模板明细失败");
            }
        }else {
            throw new BusinessException("msg","更新绩效模板失败");
        }
        return isSuccess;

    }


    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delete(JxmbDto jxmbDto) throws BusinessException {
        if (dao.delete(jxmbDto)>0){
            JxmbmxDto jxmbmxDto=new JxmbmxDto();
            jxmbmxDto.setScry(jxmbDto.getScry());
            jxmbmxDto.setIds(jxmbDto.getIds());
            if (!jxmbmxService.delete(jxmbmxDto)){
                throw new BusinessException("message","删除绩效模板明细失败");
            }
            SyfwDto syfwDto=new SyfwDto();
            syfwDto.setIds(jxmbDto.getIds());
            syfwService.deleteByJxmbids(syfwDto);
            MbszDto mbszDto=new MbszDto();
            mbszDto.setIds(jxmbDto.getIds());
            mbszDto.setScry(jxmbDto.getScry());
            if (!mbszService.deleteBymbid(mbszDto)){
                throw new BusinessException("message","删除模板设置失败");
            }
        }else {
            throw new BusinessException("message","删除绩效模板失败");
        }QzszDto qzszDto=new QzszDto();
        qzszDto.setJxmbids(jxmbDto.getIds());
        qzszService.deleteByJxmbds(qzszDto);
        return true;
    }

    @Override
    public List<JxmbDto> getAllMbJgxx(JxmbDto jxmbDto) {
        return dao.getAllMbJgxx(jxmbDto);
    }

    @Override
    public List<JxmbDto> getCreatByRq(List<JxmbDto> jxmbDtos) {
        return dao.getCreatByRq(jxmbDtos);
    }

    /**
     * 导出
     */
    public int getCountForSearchExp(JxmbDto jxmbDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(jxmbDto);
    }

    /**
     * 根据搜索条件获取导出信息
     */
    public List<JxmbDto> getListForSearchExp(Map<String, Object> params) {
        JxmbDto jxmbDto = (JxmbDto) params.get("entryData");
        queryJoinFlagExport(params, jxmbDto);
        return dao.getListForSearchExp(jxmbDto);
    }

    /**
     * 根据选择信息获取导出信息
     */
    public List<JxmbDto> getListForSelectExp(Map<String, Object> params) {
        JxmbDto jxmbDto = (JxmbDto) params.get("entryData");
        queryJoinFlagExport(params, jxmbDto);
        return dao.getListForSelectExp(jxmbDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, JxmbDto jxmbDto) {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
        for (DcszDto dcszDto : choseList) {
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        String sqlcs = sqlParam.toString();
        jxmbDto.setSqlParam(sqlcs);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
        JxmbDto jxmbDto = (JxmbDto) baseModel;
        jxmbDto.setXgry(operator.getYhid());
        boolean isSuccess = update(jxmbDto);
        if (isSuccess) {
            //修改绩效明细
            List<JxmbmxDto> dtos = JSON.parseArray(jxmbDto.getJxmbmx_json(), JxmbmxDto.class);
            isSuccess = jxmbmxService.deleteByJxmbid(jxmbDto.getJxmbid());
            if (isSuccess) {
                if (!CollectionUtils.isEmpty(dtos)) {
                    List<JxmbmxDto> jxmbmxDtoList = new ArrayList<>();
                    for (JxmbmxDto dto : dtos) {
                        String jxmbmxid = StringUtil.generateUUID();
                        dto.setJxmbmxid(jxmbmxid);
                        dto.setJxmbid(jxmbDto.getJxmbid());
                        dto.setLrry(jxmbDto.getXgry());
                        jxmbmxDtoList.add(dto);
                        if (!CollectionUtils.isEmpty(dto.getChildren())) {
                            getJxmbmxTreeList(dto.getChildren(), jxmbmxid, jxmbmxDtoList, jxmbDto.getJxmbid(), jxmbDto.getXgry());
                        }
                    }
                    if (!CollectionUtils.isEmpty(jxmbmxDtoList)) {
                        for (int i=0;i<jxmbmxDtoList.size();i++){
                            int xh=i+1;
                            jxmbmxDtoList.get(i).setXh(String.valueOf(xh));
                        }
                        isSuccess = jxmbmxService.insertMbmxList(jxmbmxDtoList);
                    }
                }
            }
        }
        return isSuccess;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList==null||shgcList.isEmpty()){
            return true;
        }
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
        String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
        String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
        for (ShgcDto shgcDto : shgcList) {
            JxmbDto jxmbDto = new JxmbDto();
            jxmbDto.setJxmbid(shgcDto.getYwid());
            jxmbDto.setXgry(operator.getYhid());
            JxmbDto jxmbDto_t = getDtoById(jxmbDto.getJxmbid());
            shgcDto.setSqbm(jxmbDto_t.getJgid());
            if ("80".equals(jxmbDto_t.getZt())){
                shgcDto.setAuditState(AuditStateEnum.AUDITED);
                jxmbDto.setTjsj(simpleDateFormat.format(date));
            }
            List<SpgwcyDto> spgwcyDtos = commonService.siftJgList(shgcDto.getSpgwcyDtos(),jxmbDto_t.getJgid());
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                String ICOMM_JXMB00002 = xxglService.getMsg("ICOMM_JXMB00002");
                jxmbDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(), spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00026,
                                    StringUtil.replaceMsg(ICOMM_JXMB00002, operator.getZsxm(), shgcDto.getShlbmc(), jxmbDto_t.getMbmc(), jxmbDto_t.getKhzqmc(), shgcDto.getShxx_shyj(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                boolean isSuccess = grjxService.checkDistributePerformance(jxmbDto_t);
                if (!isSuccess){
                    throw new BusinessException("msg","发放绩效失败！");
                }
                String ICOMM_JXMB00003 = xxglService.getMsg("ICOMM_JXMB00003");
                jxmbDto.setZt(StatusEnum.CHECK_PASS.getCode());
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    int size = spgwcyDtos.size();
                    for (int i = 0; i < size; i++) {
                        if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),spgwcyDtos.get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00006,
                                    StringUtil. replaceMsg(ICOMM_JXMB00003, operator.getZsxm(),shgcDto.getShlbmc(),jxmbDto_t.getMbmc(),jxmbDto_t.getKhzqmc() , DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                if ("1".equals(jxmbDto_t.getMbtzsj())){
                    YghmcDto dtoById = yghmcService.getDtoByYhId(operator.getYhid());
                    if (dtoById!=null&&StringUtil.isNotBlank(dtoById.getZszg())){
                        String ICOMM_JXMB00004 = xxglService.getMsg("ICOMM_JXMB00004");
                        String bt = jxmbDto_t.getLrrymc()+"申请的模板已审核通过!";
                        talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),dtoById.getYhid(),null, dtoById.getZszg(), bt,
                                StringUtil. replaceMsg(ICOMM_JXMB00004, bt,jxmbDto_t.getMbmc(), jxmbDto_t.getKhzqmc(),DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss")));
                    }
                }
            }else {
                String ICOMM_JXMB00001 = xxglService.getMsg("ICOMM_JXMB00001");
                jxmbDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                if ("1".equals(shgcDto.getXlcxh())){
                    jxmbDto.setTjsj(date.toString());
                }
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00003, StringUtil.replaceMsg(ICOMM_JXMB00001,
                                        operator.getZsxm(), shgcDto.getShlbmc(), jxmbDto_t.getMbmc(), jxmbDto_t.getKhzqmc(),
                                        DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
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
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,jxmbDto_t.getMbmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(jxmbDto);
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        // TODO Auto-generated method stub
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        boolean isSuccess=true;
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String jxmbid = shgcDto.getYwid();
                JxmbDto jxmbDto = new JxmbDto();
                jxmbDto.setXgry(operator.getYhid());
                jxmbDto.setJxmbid(jxmbid);
                jxmbDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
               isSuccess = update(jxmbDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String jxmbid = shgcDto.getYwid();
                JxmbDto jxmbDto = new JxmbDto();
                jxmbDto.setXgry(operator.getYhid());
                jxmbDto.setJxmbid(jxmbid);
                jxmbDto.setZt(StatusEnum.CHECK_NO.getCode());
                 isSuccess = update(jxmbDto);
            }
        }
        return isSuccess;
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
        JxmbDto jxmbDto = new JxmbDto();
        jxmbDto.setIds(ids);
        List<JxmbDto> dtoList = dao.getDtoList(jxmbDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(JxmbDto dto:dtoList){
                list.add(dto.getJxmbid());
            }
        }
        map.put("list",list);
        return map;
    }

    /**
     * 获取待办数据
     */
    public List<JxmbDto> getToDoList(JxmbDto jxmbDto){
        return dao.getToDoList(jxmbDto);
    }

    /**
     * 审核列表数据（不分页）
     */
    public List<JxmbDto> getAuditListData(JxmbDto jxmbDto){
        // 获取人员ID和履历号
        List<JxmbDto> t_List= dao.getPagedAuditData(jxmbDto);

        if (CollectionUtils.isEmpty(t_List))
            return t_List;

        List<JxmbDto> sqList = dao.getAuditList(t_List);

        commonService.setSqrxm(sqList);

        return sqList;
    }

    @Override
    public boolean existCheck(String fieldName, String value) {
        return false;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages) {
        // JxmbmxDto jxmbmxDto=(JxmbmxDto)baseModel;
        // jxmbmxDto.setScbj("0");
        // if (StringUtil.isNotBlank(jxmbmxDto.getJxmbid())) {
        //     JxmbDto jxmbDto = new JxmbDto();
        //     JxmbDto jxmbDto_t =getDtoById(jxmbmxDto.getJxmbid());
        //     if (jxmbDto_t==null){
        //         jxmbDto.setScbj("0");
        //         jxmbDto.setJxmbid(jxmbmxDto.getJxmbid());
        //         jxmbDto.setLrry(user.getYhid());
        //         jxmbDto.setMbmc(jxmbmxDto.getMbmc());
        //         jxmbDto.setBm(user.getJgid());
        //         jxmbDto.setLx("1");
        //         jxmbDto.setZt(StatusEnum.CHECK_NO.getCode());
        //         jxmbDto.setZf(jxmbmxDto.getZf());
        //         jxmbDto.setSyrq(jxmbmxDto.getSyrq());
        //         List<JcsjDto> khlxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSESSMENT_TYPE.getCode());
        //         if(!CollectionUtils.isEmpty(khlxs)){
        //             for(JcsjDto jcsjDto : khlxs){
        //                 if (jcsjDto.getCsmc().equals(jxmbmxDto.getKhlxmc())){
        //                     jxmbDto.setKhlx(jcsjDto.getCsid());
        //                 }
        //             }
        //         }
        //         CsszDto csszDto=csszService.getDtoByKhlx(jxmbDto.getKhlx());
        //         if(csszDto!=null){
        //             MbszDto mbszDto=new MbszDto();
        //             jxmbDto.setMbshid(csszDto.getMbshid());
        //             mbszDto.setMbszid(StringUtil.generateUUID());
        //             mbszDto.setMbid(jxmbDto.getJxmbid());
        //             mbszDto.setKhzq(csszDto.getKhzq());
        //             mbszDto.setFfrq(csszDto.getXffrq());
        //             mbszDto.setFfkhy(csszDto.getFfkhy());
        //             mbszDto.setJzrq(csszDto.getXjzrq());
        //             mbszDto.setJzkhy(csszDto.getJzkhy());
        //             mbszDto.setJxtx(csszDto.getXjxtx());
        //             mbszDto.setZdtj(csszDto.getZdtj());
        //             mbszDto.setMbjb(csszDto.getMbjb());
        //             mbszDto.setZp(csszDto.getZp());
        //             mbszDto.setMbshid(csszDto.getMbshid());
        //             mbszDto.setJxtzsj(csszDto.getJxtzsj());
        //             mbszDto.setMbtzsj(csszDto.getMbtzsj());
        //             mbszDto.setKhlx(csszDto.getKhlx());
        //             mbszDto.setJxshid(csszDto.getJxshid());
        //             mbszDto.setMblx(csszDto.getMblx());
        //             mbszDto.setMbzlx(csszDto.getMbzlx());
        //             //生效期
        //             mbszDto.setSxrq(csszDto.getSxrq());
        //             //失效期
        //             mbszDto.setYxq(csszDto.getYxq());
        //             mbszService.insert(mbszDto);
        //             jxmbDto.setMbshid(mbszDto.getMbshid());
        //             QzszDto qzszDto = new QzszDto();
        //             qzszDto.setCsszid(csszDto.getCsszid());
        //             insert(jxmbDto);
        //             List<QzszDto> qzszDtos = qzszService.getDtoList(qzszDto);
        //             for (int i=0;i<qzszDtos.size();i++){
        //                 qzszDtos.get(i).setQzszid(StringUtil.generateUUID());
        //                 qzszDtos.get(i).setCsszid(null);
        //                 qzszDtos.get(i).setJxmbid(jxmbDto.getJxmbid());
        //                 qzszDtos.get(i).setMbszid(mbszDto.getMbszid());
        //             }
        //             if (!CollectionUtils.isEmpty(qzszDtos)){
        //                 qzszService.insertQzszDtos(qzszDtos);
        //             }
        //         }
        //     }else {
        //         jxmbmxDto.setJxmbid(jxmbmxDto.getJxmbid());
        //         String jxmbmxid = StringUtil.generateUUID();
        //         jxmbmxDto.setJxmbmxid(jxmbmxid);
        //         jxmbmxDto.setLrry(user.getYhid());
        //         if (StringUtil.isNotBlank(jxmbmxDto.getZbsjmc())){
        //             JxmbmxDto jxmbmxDto_sel = new JxmbmxDto();
        //             jxmbmxDto_sel.setJxmbid(jxmbmxDto.getJxmbid());
        //             jxmbmxDto_sel.setKhzb(jxmbmxDto.getZbsjmc());
        //             JxmbmxDto dto = jxmbmxService.getDtoByKhzbAndJxmbid(jxmbmxDto_sel);
        //             if (dto!=null){
        //                 jxmbmxDto.setZbsjid(dto.getJxmbmxid());
        //             }
        //         }
        //         jxmbmxService.insert(jxmbmxDto);
        //     }
        // }
        return true;
    }

    @Override
    public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
        return null;
    }

    @Override
    public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
        return false;
    }

    @Override
    public boolean checkDefined(List<Map<String, String>> defined) {
        return true;
    }

    @Override
    public Map<String,Object> getPerformanceTargetStatistics(YghmcDto yghmcDto){
        return dao.getPerformanceTargetStatistics(yghmcDto);
    }
    @Override
    public List<Map<String, Object>> getPerformanceTargetStatisticsView(YghmcDto yghmcDto){
        return dao.getPerformanceTargetStatisticsView(yghmcDto);
    }
}

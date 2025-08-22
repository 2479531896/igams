package com.matridx.igams.storehouse.service.impl;
import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.service.svcinterface.IQgglService;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.dao.post.IXzrkglDao;
import com.matridx.igams.storehouse.service.svcinterface.IXzkcxxService;
import com.matridx.igams.storehouse.service.svcinterface.IXzrkglService;
import com.matridx.igams.storehouse.service.svcinterface.IXzrkmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class XzrkglServiceImpl extends BaseBasicServiceImpl<XzrkglDto, XzrkglModel, IXzrkglDao> implements IXzrkglService, IAuditService {

    @Autowired
    ICommonService commonservice;
    @Autowired
    IXzrkmxService xzrkmxService;

    @Autowired
    DingTalkUtil talkUtil;

    @Autowired
    IXxglService xxglService;
    @Autowired
    IQgmxService qgmxService;
    @Autowired
    IXzkcxxService xzkcxxService;
    @Autowired
    private IQgglService qgglService;
    private final Logger log = LoggerFactory.getLogger(XzrkglServiceImpl.class);

    /**
     * 行政入库审核列表
     * @param xzrkglDto
     * @return
     */
    public List<XzrkglDto> getPagedAuditPutInAdmin(XzrkglDto xzrkglDto){
        // 获取人员ID和履历号
        List<XzrkglDto> t_sbyzList= dao.getPagedAuditPutInAdmin(xzrkglDto);

        if(CollectionUtils.isEmpty(t_sbyzList))
            return t_sbyzList;

        List<XzrkglDto> sqList = dao.getAuditListPutInAdmin(t_sbyzList);

        commonservice.setSqrxm(sqList);

        return sqList;
    }

    @Override
    public String generateDjh(XzrkglDto xzrkglDto) {
        // 生成规则: LL-20201022-01 LL-年份日期-流水号 。
        String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
        String prefix = "RK" + "-" + date + "-";
        // 查询流水号
        String serial = dao.getDjhSerial(prefix);
        return prefix + serial;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delAdministrationStorage(XzrkglDto xzrkglDto) throws BusinessException {
        List<XzrkglDto> xzrkglDtos = dao.getDtoByXzrkids(xzrkglDto);
        List<QgmxDto> qgmxDtos = new ArrayList<>();
        List<XzkcxxDto> xzkcxxDtos = new ArrayList<>();
        List<String> qgmxids=new ArrayList<>();
        Map<String, Double> map = new HashMap<>();
        for (XzrkglDto xzrkglDto_t : xzrkglDtos) {
            map.put(xzrkglDto_t.getXzkcid(),Double.parseDouble(xzrkglDto_t.getXzkcl()));
            qgmxids.add(xzrkglDto_t.getQgmxid());
        }
        for (XzrkglDto dto : xzrkglDtos) {
            double kcl = Double.parseDouble(dto.getKcl());
            for (String xzkcid : map.keySet()) {
                if (xzkcid.equals(dto.getXzkcid())){
                    if (map.get(xzkcid)<kcl){
                        throw new BusinessException("msg","物料名称:"+dto.getHwmc()+",标准:"+dto.getHwbz()+",库存量不足或删除过多导致库存量不足!");
                    }
                    map.put(xzkcid,map.get(xzkcid)-kcl);
                }
            }
            if ("QG".equals(dto.getRklbdm())){
                QgmxDto qgmxDto = new QgmxDto();
                qgmxDto.setQgmxid(dto.getQgmxid());
                qgmxDto.setRksldel("1");
                qgmxDto.setRksl(dto.getRksl());
                qgmxDtos.add(qgmxDto);
                XzkcxxDto xzkcxxDto = new XzkcxxDto();
                xzkcxxDto.setXzkcid(dto.getXzkcid());
                xzkcxxDto.setKcl(dto.getKcl());
                xzkcxxDto.setUpdateFlag("subtract");
                xzkcxxDtos.add(xzkcxxDto);
            }else if ("QT".equals(dto.getRklbdm())){
                XzkcxxDto xzkcxxDto = new XzkcxxDto();
                xzkcxxDto.setXzkcid(dto.getXzkcid());
                xzkcxxDto.setKcl(dto.getKcl());
                xzkcxxDto.setUpdateFlag("subtract");
                xzkcxxDtos.add(xzkcxxDto);
            }else {
                throw new BusinessException("msg","入库类别异常!");
            }
        }
        if (!CollectionUtils.isEmpty(xzkcxxDtos)){
            boolean isSuccess = xzkcxxService.updateList(xzkcxxDtos);
            if (!isSuccess){
                throw new BusinessException("msg","同步数量失败!");
            }
        }
        if (!CollectionUtils.isEmpty(qgmxDtos)){
            boolean isSuccess = qgmxService.updateRkslByIds(qgmxDtos);
            if (!isSuccess){
                throw new BusinessException("msg","同步数量失败!");
            }
        }
        int delete = dao.delete(xzrkglDto);
        if (delete<1){
            throw new BusinessException("msg","删除入库信息失败!");
        }
        if(!CollectionUtils.isEmpty(qgmxids)) {
            List<QgglDto> qgglDtoList=qgglService.getXzRkslByQgmxids(qgmxids);
            if (!CollectionUtils.isEmpty(qgglDtoList)) {
                List<QgglDto> qgglDtoListAllInRk = new ArrayList<>();
                List<QgglDto> qgglDtoListNotAllInRk = new ArrayList<>();
                for (QgglDto qgglDto : qgglDtoList) {
                    if (Double.parseDouble(qgglDto.getSl()) == 0) {
                        qgglDto.setRkzt("01");
                        qgglDtoListAllInRk.add(qgglDto);
                    } else {
                        qgglDtoListNotAllInRk.add(qgglDto);
                        qgglDto.setRkzt("02");
                    }
                }
                if (!CollectionUtils.isEmpty(qgglDtoListAllInRk)) {
                    boolean isSuccess = qgglService.updateRkztList(qgglDtoListAllInRk);
                    if (!isSuccess) {
                        throw new BusinessException("mag", "更新请购管理表入库状态失败！");
                    }
                }
                if (!CollectionUtils.isEmpty(qgglDtoListNotAllInRk)) {
                    boolean isSuccess = qgglService.updateRkztList(qgglDtoListNotAllInRk);
                    if (!isSuccess) {
                        throw new BusinessException("mag", "更新请购管理表入库状态失败！");
                    }
                }
            }
        }
        XzrkmxDto xzrkmxDto = new XzrkmxDto();
        xzrkmxDto.setIds(xzrkglDto.getIds());
        boolean isSuccess = xzrkmxService.delete(xzrkmxDto);
        if (!isSuccess){
            throw new BusinessException("msg","删除入库明细失败!");
        }
        return true;
    }

    @Override
    public XzrkglDto getDtoByRkdh(XzrkglDto xzrkglDto) {
        return dao.getDtoByRkdh(xzrkglDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addSaveInStore(XzrkglDto xzrkglDto) throws BusinessException {
        boolean result=dao.insert(xzrkglDto) != 0;
        if(!result) {
            throw new BusinessException("msg","入库信息保存失败!");
        }
        //保存货物领料信息
        List<XzrkmxDto> xzrkmxDtoList= JSON.parseArray(xzrkglDto.getQgmx_json(),XzrkmxDto.class);
        if(!CollectionUtils.isEmpty(xzrkmxDtoList)) {
            List<QgmxDto> qgmxDtos=new ArrayList<>();
            for (XzrkmxDto xzrkmxDto:xzrkmxDtoList) {
                xzrkmxDto.setXzrkid(xzrkglDto.getXzrkid());
                xzrkmxDto.setXzrkmxid(StringUtil.generateUUID());
                xzrkmxDto.setLrry(xzrkglDto.getLrry());
                QgmxDto qgmxDto=new QgmxDto();
                qgmxDto.setQgmxid(xzrkmxDto.getQgmxid());
                BigDecimal yrksl=new BigDecimal(xzrkmxDto.getYrksl());
                BigDecimal rksl=new BigDecimal(xzrkmxDto.getRksl());
                qgmxDto.setRksl(String.valueOf(yrksl.add(rksl)));
                qgmxDtos.add(qgmxDto);
            }

            result = xzrkmxService.insertList(xzrkmxDtoList);
            if(!result) {
                throw new BusinessException("msg","入库明细信息保存失败!");
            }

            List<XzrkmxDto> xzkcList = xzrkmxService.getXzkcList(xzrkglDto.getXzrkid());
            List<XzkcxxDto> updatelist=new ArrayList<>();
            List<XzkcxxDto> addlist=new ArrayList<>();
            for(XzrkmxDto dto:xzkcList){
                if(StringUtil.isNotBlank(dto.getXzkcid())){
                    XzkcxxDto xzkcxxDto=new XzkcxxDto();
                    xzkcxxDto.setXzkcid(dto.getXzkcid());
                    xzkcxxDto.setKcl(dto.getRksl());
                    xzkcxxDto.setUpdateFlag("add");
                    updatelist.add(xzkcxxDto);
                }else{
                    XzkcxxDto xzkcxxDto=new XzkcxxDto();
                    xzkcxxDto.setXzkcid(StringUtil.generateUUID());
                    xzkcxxDto.setKcl(dto.getRksl());
                    xzkcxxDto.setKwid(dto.getKwid());
                    xzkcxxDto.setHwmc(dto.getHwmc());
                    xzkcxxDto.setHwbz(dto.getHwbz());
                    dto.setXzkcid(xzkcxxDto.getXzkcid());
                    addlist.add(xzkcxxDto);
                }
            }
            result = qgmxService.updateQgmx(qgmxDtos);
            List<QgglDto> qgglDtoList=qgglService.getPurchaseByQgmxids(qgmxDtos);
            List<QgglDto> qgglDtoListAllInRk=new ArrayList<>();
            List<QgglDto> qgglDtoListNotAllInRk=new ArrayList<>();
            for (QgglDto qgglDto:qgglDtoList) {
                if (StringUtil.isNotBlank(qgglDto.getRksl()) && StringUtil.isNotBlank(qgglDto.getSl())) {
                    if (Double.parseDouble(qgglDto.getRksl()) == Double.parseDouble(qgglDto.getSl())) {
                        qgglDto.setRkzt("03");
                        qgglDtoListAllInRk.add(qgglDto);
                    } else {
                        qgglDtoListNotAllInRk.add(qgglDto);
                        qgglDto.setRkzt("02");
                    }
                }
            }
            if (!CollectionUtils.isEmpty(qgglDtoListAllInRk)){
                boolean isSuccess=qgglService.updateRkztList(qgglDtoListAllInRk);
                if (!isSuccess) {
                    throw new  BusinessException("mag","更新请购管理表入库状态失败！");
                }
            }
            if (!CollectionUtils.isEmpty(qgglDtoListNotAllInRk)){
                boolean isSuccess=qgglService.updateRkztList(qgglDtoListNotAllInRk);
                if (!isSuccess) {
                    throw new  BusinessException("mag","更新请购管理表入库状态失败！");
                }
            }
            if(!result) {
                throw new BusinessException("msg","更新请购明细失败!");
            }
            if(!CollectionUtils.isEmpty(updatelist)){
                result = xzkcxxService.updateList(updatelist);
                if(!result) {
                    throw new BusinessException("msg","行政库存信息更新失败!");
                }
            }
            if(!CollectionUtils.isEmpty(addlist)){
                result = xzkcxxService.insertList(addlist);
                if(!result) {
                    throw new BusinessException("msg","行政库存信息更新失败!");
                }

            }
            result = xzrkmxService.updateList(xzkcList);
            if(!result) {
                throw new BusinessException("msg","行政库存信息更新失败!");
            }
        }
        return true;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean modSaveAdministrationStorage(XzrkglDto xzrkglDto) throws BusinessException {
        boolean result=dao.updatePageEvent(xzrkglDto);
        if(!result) {
            throw new BusinessException("msg","入库信息修改保存失败!");
        }
        //保存货物领料信息
        List<XzrkmxDto> xzrkmxDtoList= JSON.parseArray(xzrkglDto.getQgmx_json(),XzrkmxDto.class);
        List<XzrkmxDto> xzrkmxDtoList_t= JSON.parseArray(xzrkglDto.getQgmx_yjson(),XzrkmxDto.class);
        for (XzrkmxDto value : xzrkmxDtoList) {
            for (int j = 0; j < xzrkmxDtoList_t.size(); j++) {
                if (StringUtil.isNotBlank(value.getXzrkmxid())) {
                    if (StringUtil.isNotBlank(xzrkmxDtoList_t.get(j).getXzrkmxid()) && StringUtil.isNotBlank(value.getXzrkmxid()) && xzrkmxDtoList_t.get(j).getXzrkmxid().equals(value.getXzrkmxid())) {
                        xzrkmxDtoList_t.remove(xzrkmxDtoList_t.get(j));
                        break;
                    }
                }
            }
        }
        List<XzrkmxDto> xzrkmxDtoList1=new ArrayList<>();
        if (!CollectionUtils.isEmpty(xzrkmxDtoList)){
            for (int i=0;i<xzrkmxDtoList.size();i++){
                if (StringUtil.isBlank(xzrkmxDtoList.get(i).getXzrkmxid()))
                {
                    xzrkmxDtoList1.add(xzrkmxDtoList.get(i));
                    xzrkmxDtoList.remove(xzrkmxDtoList.get(i));
                }
            }
        }
        List<XzkcxxDto> xzkcxxDtos=new ArrayList<>();
        List<QgmxDto> qgmxDtoList=new ArrayList<>();
        if (!CollectionUtils.isEmpty(xzrkmxDtoList_t)){
            for (XzrkmxDto xzrkmxDto:xzrkmxDtoList_t){
                xzrkmxDto.setScbj("1");
                XzkcxxDto xzkcxxDto=new XzkcxxDto();
                xzkcxxDto.setXzkcid(xzrkmxDto.getXzkcid());
                xzkcxxDto.setKcl(xzrkmxDto.getXzrksl());
                xzkcxxDto.setUpdateFlag("subtract");
                xzkcxxDtos.add(xzkcxxDto);
                QgmxDto qgmxDto=new QgmxDto();
                qgmxDto.setQgmxid(xzrkmxDto.getQgmxid());
                BigDecimal yrksl=new BigDecimal(xzrkmxDto.getYrksl());
                BigDecimal rksl=new BigDecimal(xzrkmxDto.getRksl());
                qgmxDto.setRksl(String.valueOf(yrksl.subtract(rksl)));
                qgmxDtoList.add(qgmxDto);
            }
            xzrkmxService.updateList(xzrkmxDtoList_t);
            xzkcxxService.updateList(xzkcxxDtos);
            result = qgmxService.updateQgmx(qgmxDtoList);
            if (!result){
                throw new BusinessException("msg","更新请购明细失败!");
            }
        }
        List<XzkcxxDto> xzkcxxDtosNew=new ArrayList<>();
        List<QgmxDto> qgmxDtos=new ArrayList<>();
        if(!CollectionUtils.isEmpty(xzrkmxDtoList1)){
            for (XzrkmxDto xzrkmxDto:xzrkmxDtoList1) {
                xzrkmxDto.setXzrkid(xzrkglDto.getXzrkid());
                xzrkmxDto.setXzrkmxid(StringUtil.generateUUID());
                xzrkmxDto.setLrry(xzrkglDto.getLrry());
                xzrkmxDto.setKcl(xzrkmxDto.getRksl());
                XzkcxxDto xzkcxxDto1=new XzkcxxDto();
                xzkcxxDto1.setXzkcid(StringUtil.generateUUID());
                xzkcxxDto1.setKcl(xzrkmxDto.getRksl());
                xzkcxxDto1.setHwmc(xzrkmxDto.getHwmc());
                xzkcxxDto1.setHwbz(xzrkmxDto.getHwbz());
                xzkcxxDto1.setKwid(xzrkglDto.getKwid());
                xzkcxxDto1.setXzrkmxid(xzrkmxDto.getXzrkmxid());
                xzkcxxDtosNew.add(xzkcxxDto1);
                xzrkmxDto.setXzkcid(xzkcxxDto1.getXzkcid());
                QgmxDto qgmxDto=new QgmxDto();
                qgmxDto.setQgmxid(xzrkmxDto.getQgmxid());
                BigDecimal yrksl=new BigDecimal(xzrkmxDto.getYrksl());
                BigDecimal rksl=new BigDecimal(xzrkmxDto.getRksl());
                qgmxDto.setRksl(String.valueOf(yrksl.add(rksl)));
                qgmxDtos.add(qgmxDto);
            }
            result = qgmxService.updateQgmx(qgmxDtos);
            if (!result){
                throw new BusinessException("msg","更新请购明细失败!");
            }
        }
        List<XzkcxxDto> xzkcxxDtosupdate=new ArrayList<>();
        XzkcxxDto xzkcxxDtox=new XzkcxxDto();
        if (!CollectionUtils.isEmpty(xzkcxxDtosNew)){
            List<XzkcxxDto> xzkcxxDtoList=xzkcxxService.getDtoList(xzkcxxDtox);
            for (int i=xzkcxxDtoList.size()-1;i>=0;i--){
                for (int j=xzkcxxDtosNew.size()-1;j>=0;j--){
                    if (xzkcxxDtosNew.get(j).getHwbz().equals(xzkcxxDtoList.get(i).getHwbz())&& xzkcxxDtosNew.get(j).getKwid().equals(xzkcxxDtoList.get(i).getKwid())&&
                            xzkcxxDtosNew.get(j).getHwmc().equals(xzkcxxDtoList.get(i).getHwmc())){
                        for (XzrkmxDto xzrkmxDto:xzrkmxDtoList1){
                            if (xzrkmxDto.getXzrkmxid().equals(xzkcxxDtosNew.get(j).getXzrkmxid())){
                                xzrkmxDto.setXzkcid(xzkcxxDtoList.get(i).getXzkcid());
                            }
                        }
                        xzkcxxDtoList.get(i).setKcl(xzkcxxDtosNew.get(j).getKcl());
                        xzkcxxDtosupdate.add(xzkcxxDtoList.get(i));
                        xzkcxxDtosNew.remove(xzkcxxDtosNew.get(j));
                        break;
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(xzkcxxDtosupdate)){
            for (XzkcxxDto xzkcxxDto1:xzkcxxDtosupdate){
                xzkcxxDto1.setUpdateFlag("add");
            }
            xzkcxxService.updateList(xzkcxxDtosupdate);
        }if (!CollectionUtils.isEmpty(xzkcxxDtosNew)){
            xzkcxxService.insertList(xzkcxxDtosNew);
        }
        if (!CollectionUtils.isEmpty(xzrkmxDtoList1)){
            result = xzrkmxService.insertList(xzrkmxDtoList1);
        }
        if(!result) {
            throw new BusinessException("msg","入库明细信息保存失败!");
        }
        List<XzkcxxDto> xzkcxxDtos1=new ArrayList<>();
        List<QgmxDto> qgmxDtoList1=new ArrayList<>();
        if (xzrkglDto.getYkwid().equals(xzrkglDto.getKwid())){
            if (!CollectionUtils.isEmpty(xzrkmxDtoList)){
                for (XzrkmxDto xzrkmxDto:xzrkmxDtoList){
                    BigDecimal xzrksl=new BigDecimal(xzrkmxDto.getXzrksl());
                    BigDecimal rksl=new BigDecimal(xzrkmxDto.getRksl());
                    BigDecimal kcl=new BigDecimal(xzrkmxDto.getKcl());
                    BigDecimal kcl_t=kcl.subtract(xzrksl);
                    xzrkmxDto.setKcl(String.valueOf(kcl_t.add(rksl)));
                    XzkcxxDto xzkcxxDto=new XzkcxxDto();
                    xzkcxxDto.setXzkcid(xzrkmxDto.getXzkcid());
                    BigDecimal xzkcl=new BigDecimal(xzrkmxDto.getXzkcl());
                    BigDecimal xzkcl_t=xzkcl.subtract(xzrksl);
                    xzkcxxDto.setKcl(String.valueOf(xzkcl_t.add(rksl)));
                    xzkcxxDtos1.add(xzkcxxDto);
                    QgmxDto qgmxDto=new QgmxDto();
                    qgmxDto.setQgmxid(xzrkmxDto.getQgmxid());
                    BigDecimal yrksl=new BigDecimal(xzrkmxDto.getYrksl());
                    qgmxDto.setRksl(String.valueOf(yrksl.add(rksl).subtract(xzrksl)));
                    qgmxDtoList1.add(qgmxDto);
                }
                xzkcxxService.updateListKclById(xzkcxxDtos1);
                xzrkmxService.updateList(xzrkmxDtoList);
                result = qgmxService.updateQgmx(qgmxDtoList1);
                if (!result){
                    throw new BusinessException("msg","更新请购明细失败!");
                }
            }
        }else {
            if (!CollectionUtils.isEmpty(xzrkmxDtoList)) {
                for (XzrkmxDto xzrkmxDto : xzrkmxDtoList) {
                    BigDecimal xzrksl = new BigDecimal(xzrkmxDto.getXzrksl());
                    BigDecimal rksl = new BigDecimal(xzrkmxDto.getRksl());
                    BigDecimal kcl = new BigDecimal(xzrkmxDto.getKcl());
                    BigDecimal kcl_t = kcl.subtract(xzrksl);
                    xzrkmxDto.setKcl(String.valueOf(kcl_t.add(rksl)));
                    XzkcxxDto xzkcxxDto = new XzkcxxDto();
                    xzkcxxDto.setXzkcid(xzrkmxDto.getXzkcid());
                    BigDecimal xzkcl = new BigDecimal(xzrkmxDto.getXzkcl());
                    BigDecimal xzkcl_t = xzkcl.subtract(xzrksl);
                    xzkcxxDto.setKcl(String.valueOf(xzkcl_t));
                    xzkcxxDto.setKwid(xzrkglDto.getKwid());
                    xzkcxxDto.setHwmc(xzrkmxDto.getHwmc());
                    xzkcxxDto.setHwbz(xzrkmxDto.getHwbz());
                    xzkcxxDto.setXzrkmxid(xzrkmxDto.getXzrkmxid());
                    xzkcxxDto.setRksl(xzrkmxDto.getRksl());
                    xzkcxxDto.setYkcl(String.valueOf(xzkcl_t.add(rksl)));
                    xzkcxxDtos1.add(xzkcxxDto);
                    QgmxDto qgmxDto=new QgmxDto();
                    qgmxDto.setQgmxid(xzrkmxDto.getQgmxid());
                    BigDecimal yrksl=new BigDecimal(xzrkmxDto.getYrksl());
                    qgmxDto.setRksl(String.valueOf(yrksl.add(rksl).subtract(xzrksl)));
                    qgmxDtoList1.add(qgmxDto);
                }
                result = qgmxService.updateQgmx(qgmxDtoList1);
                if (!result){
                    throw new BusinessException("msg","更新请购明细失败!");
                }
                XzkcxxDto xzkcxxDto=new XzkcxxDto();
                List<XzkcxxDto> xzkcxxDtoList=xzkcxxService.getDtoList(xzkcxxDto);
                List<XzkcxxDto> xzkcxxDtoList1=new ArrayList<>();
                List<XzkcxxDto> xzkcxxDtoList2=new ArrayList<>();
                for (XzkcxxDto dto : xzkcxxDtoList) {
                    for (int i = xzkcxxDtos1.size() - 1; i >= 0; i--) {
                        if (xzkcxxDtos1.get(i).getHwmc().equals(dto.getHwmc()) && xzkcxxDtos1.get(i).getKwid().equals(dto.getKwid())
                                && xzkcxxDtos1.get(i).getHwbz().equals(dto.getHwbz())) {
                            dto.setKcl(xzkcxxDtos1.get(i).getRksl());
                            xzkcxxDtoList1.add(dto);
                            xzkcxxDtoList2.add(xzkcxxDtos1.get(i));
                            XzkcxxDto xzkcxxDto1 = xzkcxxDtos1.get(i);
                            xzkcxxDtos1.remove(xzkcxxDtos1.get(i));
                            for (XzrkmxDto xzrkmxDto : xzrkmxDtoList) {
                                if (xzkcxxDto1.getXzrkmxid().equals(xzrkmxDto.getXzrkmxid())) {
                                    xzrkmxDto.setXzkcid(dto.getXzkcid());
                                }
                            }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(xzkcxxDtoList1)){
                    for (XzkcxxDto xzkcxxDto1:xzkcxxDtoList1){
                        xzkcxxDto1.setUpdateFlag("add");
                    }
                    xzkcxxService.updateList(xzkcxxDtoList1);
                }
                if (!CollectionUtils.isEmpty(xzkcxxDtoList2)){
                    for (XzkcxxDto xzkcxxDto1:xzkcxxDtoList2){
                        xzkcxxDto1.setKwid(xzrkglDto.getYkwid());
                    }
                    xzkcxxService.updateListKclById(xzkcxxDtoList2);
                }
                if (!CollectionUtils.isEmpty(xzkcxxDtos1)){
                    for (XzkcxxDto xzkcxxDto1:xzkcxxDtos1){
                        xzkcxxDto1.setKwid(xzrkglDto.getYkwid());
                    }
                    xzkcxxService.updateListKclById(xzkcxxDtos1);
                    for (XzkcxxDto xzkcxxDto_t: xzkcxxDtos1){
                        xzkcxxDto_t.setKcl(xzkcxxDto_t.getYkcl());
                        xzkcxxDto_t.setKwid(xzrkglDto.getKwid());
                        xzkcxxDto_t.setXzkcid(StringUtil.generateUUID());
                        for (XzrkmxDto xzrkmxDto:xzrkmxDtoList){
                            if (xzkcxxDto_t.getXzrkmxid().equals(xzrkmxDto.getXzrkmxid())){
                                xzrkmxDto.setXzkcid(xzkcxxDto_t.getXzkcid());
                            }
                        }
                    }
                    xzkcxxService.insertList(xzkcxxDtos1);
                }
                if (!CollectionUtils.isEmpty(xzkcxxDtos1)){
                    for (XzkcxxDto xzkcxxDto1:xzkcxxDtos1){
                        xzkcxxDto1.setXzkcid(StringUtil.generateUUID());
                        for (XzrkmxDto xzrkmxDto:xzrkmxDtoList_t){
                            if (xzkcxxDto.getXzrkmxid().equals(xzkcxxDto1.getXzrkmxid())){
                                xzrkmxDto.setXzkcid(xzkcxxDto1.getXzkcid());
                            }
                        }
                    }
                }
                xzrkmxService.updateList(xzrkmxDtoList);
            }

        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean modSaveAdministrationOtherStorage(XzrkglDto xzrkglDto) throws BusinessException {
        boolean result=dao.updatePageEvent(xzrkglDto);
        if(!result) {
            throw new BusinessException("msg","入库信息修改保存失败!");
        }
        //保存货物领料信息
        List<XzrkmxDto> xzrkmxDtoList= JSON.parseArray(xzrkglDto.getQgmx_json(),XzrkmxDto.class);
        List<XzrkmxDto> xzrkmxDtoList_t= JSON.parseArray(xzrkglDto.getQgmx_yjson(),XzrkmxDto.class);
        for (XzrkmxDto value : xzrkmxDtoList) {
            for (int j = 0; j < xzrkmxDtoList_t.size(); j++) {
                if (StringUtil.isNotBlank(value.getXzrkmxid())) {
                    if (StringUtil.isNotBlank(xzrkmxDtoList_t.get(j).getXzrkmxid()) && StringUtil.isNotBlank(value.getXzrkmxid()) && xzrkmxDtoList_t.get(j).getXzrkmxid().equals(value.getXzrkmxid())) {
                        xzrkmxDtoList_t.remove(xzrkmxDtoList_t.get(j));
                        break;
                    }
                }
            }
        }
        List<XzrkmxDto> xzrkmxDtoList1=new ArrayList<>();
        if (!CollectionUtils.isEmpty(xzrkmxDtoList)){
            for (int i=0;i<xzrkmxDtoList.size();i++){
                if (StringUtil.isBlank(xzrkmxDtoList.get(i).getXzrkmxid()))
                {
                    xzrkmxDtoList1.add(xzrkmxDtoList.get(i));
                    xzrkmxDtoList.remove(xzrkmxDtoList.get(i));
                }
            }
        }
        List<XzkcxxDto> xzkcxxDtos=new ArrayList<>();
        if (!CollectionUtils.isEmpty(xzrkmxDtoList_t)){
            for (XzrkmxDto xzrkmxDto:xzrkmxDtoList_t){
                xzrkmxDto.setScbj("1");
                XzkcxxDto xzkcxxDto=new XzkcxxDto();
                xzkcxxDto.setXzkcid(xzrkmxDto.getXzkcid());
                xzkcxxDto.setKcl(xzrkmxDto.getXzrksl());
                xzkcxxDto.setUpdateFlag("subtract");
                xzkcxxDtos.add(xzkcxxDto);
            }
            xzrkmxService.updateList(xzrkmxDtoList_t);
            xzkcxxService.updateList(xzkcxxDtos);
        }
        List<XzkcxxDto> xzkcxxDtosNew=new ArrayList<>();
        if(!CollectionUtils.isEmpty(xzrkmxDtoList1)){
            for (XzrkmxDto xzrkmxDto:xzrkmxDtoList1) {
                xzrkmxDto.setXzrkid(xzrkglDto.getXzrkid());
                xzrkmxDto.setXzrkmxid(StringUtil.generateUUID());
                xzrkmxDto.setLrry(xzrkglDto.getLrry());
                xzrkmxDto.setKcl(xzrkmxDto.getRksl());
                XzkcxxDto xzkcxxDto1=new XzkcxxDto();
                xzkcxxDto1.setXzkcid(StringUtil.generateUUID());
                xzkcxxDto1.setKcl(xzrkmxDto.getRksl());
                xzkcxxDto1.setHwmc(xzrkmxDto.getHwmc());
                xzkcxxDto1.setHwbz(xzrkmxDto.getHwbz());
                xzkcxxDto1.setKwid(xzrkglDto.getKwid());
                xzkcxxDto1.setXzrkmxid(xzrkmxDto.getXzrkmxid());
                xzkcxxDtosNew.add(xzkcxxDto1);
                xzrkmxDto.setXzkcid(xzkcxxDto1.getXzkcid());
            }
        }
        List<XzkcxxDto> xzkcxxDtosupdate=new ArrayList<>();
        XzkcxxDto xzkcxxDtox=new XzkcxxDto();
        if (!CollectionUtils.isEmpty(xzkcxxDtosNew)){
            List<XzkcxxDto> xzkcxxDtoList=xzkcxxService.getDtoList(xzkcxxDtox);
            for (int i=xzkcxxDtoList.size()-1;i>=0;i--){
                for (int j=xzkcxxDtosNew.size()-1;j>=0;j--){
                    if (xzkcxxDtosNew.get(j).getHwbz().equals(xzkcxxDtoList.get(i).getHwbz())&& xzkcxxDtosNew.get(j).getKwid().equals(xzkcxxDtoList.get(i).getKwid())&&
                            xzkcxxDtosNew.get(j).getHwmc().equals(xzkcxxDtoList.get(i).getHwmc())){
                        for (XzrkmxDto xzrkmxDto:xzrkmxDtoList1){
                            if (xzrkmxDto.getXzrkmxid().equals(xzkcxxDtosNew.get(j).getXzrkmxid())){
                                xzrkmxDto.setXzkcid(xzkcxxDtoList.get(i).getXzkcid());
                            }
                        }
                        xzkcxxDtoList.get(i).setKcl(xzkcxxDtosNew.get(j).getKcl());
                        xzkcxxDtosupdate.add(xzkcxxDtoList.get(i));
                        xzkcxxDtosNew.remove(xzkcxxDtosNew.get(j));
                        break;
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(xzkcxxDtosupdate)){
            for (XzkcxxDto xzkcxxDto1:xzkcxxDtosupdate){
                xzkcxxDto1.setUpdateFlag("add");
            }
            xzkcxxService.updateList(xzkcxxDtosupdate);
        }if (!CollectionUtils.isEmpty(xzkcxxDtosNew)){
            xzkcxxService.insertList(xzkcxxDtosNew);
        }
        if (!CollectionUtils.isEmpty(xzrkmxDtoList1)){
            result = xzrkmxService.insertList(xzrkmxDtoList1);
        }
        if(!result) {
            throw new BusinessException("msg","入库明细信息保存失败!");
        }
        List<XzkcxxDto> xzkcxxDtos1=new ArrayList<>();
//        if (xzrkglDto.getYkwid().equals(xzrkglDto.getKwid())){
//            if (!CollectionUtils.isEmpty(xzrkmxDtoList)){
//                for (XzrkmxDto xzrkmxDto:xzrkmxDtoList){
//                    BigDecimal xzrksl=new BigDecimal(xzrkmxDto.getXzrksl());
//                    BigDecimal rksl=new BigDecimal(xzrkmxDto.getRksl());
//                    BigDecimal kcl=new BigDecimal(xzrkmxDto.getKcl());
//                    BigDecimal kcl_t=kcl.subtract(xzrksl);
//                    xzrkmxDto.setKcl(String.valueOf(kcl_t.add(rksl)));
//                    XzkcxxDto xzkcxxDto=new XzkcxxDto();
//                    xzkcxxDto.setXzkcid(xzrkmxDto.getXzkcid());
//                    xzkcxxDto.setHwmc(xzrkmxDto.getHwmc());
//                    xzkcxxDto.setHwbz(xzrkmxDto.getHwbz());
//                    BigDecimal xzkcl=new BigDecimal(xzrkmxDto.getXzkcl());
//                    BigDecimal xzkcl_t=xzkcl.subtract(xzrksl);
//                    xzkcxxDto.setKcl(String.valueOf(xzkcl_t.add(rksl)));
//                    xzkcxxDtos1.add(xzkcxxDto);
//                }
//                List<XzkcxxDto> xzkcxxDtoOther=new ArrayList<>();
//                XzkcxxDto xzkcxxDto=new XzkcxxDto();
//                if (!CollectionUtils.isEmpty(xzkcxxDtos1)){
//                    List<XzkcxxDto> xzkcxxDtoList=xzkcxxService.getDtoList(xzkcxxDto);
//                    for (int i=xzkcxxDtoList.size()-1;i>=0;i--){
//                        for (int j=xzkcxxDtos1.size()-1;j>=0;j--){
//                            if (xzkcxxDtos1.get(j).getHwbz().equals(xzkcxxDtoList.get(i).getHwbz())&& xzkcxxDtos1.get(j).getKwid().equals(xzkcxxDtoList.get(i).getKwid())&&
//                                    xzkcxxDtos1.get(j).getHwmc().equals(xzkcxxDtoList.get(i).getHwmc())){
//                                for (XzrkmxDto xzrkmxDto:xzrkmxDtoList1){
//                                    if (xzrkmxDto.getXzrkmxid().equals(xzkcxxDtos1.get(j).getXzrkmxid())){
//                                        xzrkmxDto.setXzkcid(xzkcxxDtoList.get(i).getXzkcid());
//                                    }
//                                }
//                                xzkcxxDtoList.get(i).setKcl(xzkcxxDtos1.get(j).getKcl());
//                                xzkcxxDtoOther.add(xzkcxxDtoList.get(i));
//                                xzkcxxDtos1.remove(xzkcxxDtos1.get(j));
//                                break;
//                            }
//                        }
//                    }
//                }
//                if (!CollectionUtils.isEmpty(xzkcxxDtoOther)){
//                    for (XzkcxxDto xzkcxxDto1:xzkcxxDtoOther){
//                        xzkcxxDto1.setUpdateFlag("add");
//                    }
//                    xzkcxxService.updateList(xzkcxxDtoOther);
//                }
//                xzkcxxService.updateListKclById(xzkcxxDtos1);
//                xzrkmxService.updateList(xzrkmxDtoList);
//            }
//        }else {
            if (!CollectionUtils.isEmpty(xzrkmxDtoList)) {
                for (XzrkmxDto xzrkmxDto : xzrkmxDtoList) {
                    BigDecimal xzrksl = new BigDecimal(xzrkmxDto.getXzrksl());
                    BigDecimal rksl = new BigDecimal(xzrkmxDto.getRksl());
                    BigDecimal kcl = new BigDecimal(xzrkmxDto.getKcl());
                    BigDecimal kcl_t = kcl.subtract(xzrksl);
                    xzrkmxDto.setKcl(String.valueOf(kcl_t.add(rksl)));
                    XzkcxxDto xzkcxxDto = new XzkcxxDto();
                    xzkcxxDto.setXzkcid(xzrkmxDto.getXzkcid());
                    BigDecimal xzkcl = new BigDecimal(xzrkmxDto.getXzkcl());
                    BigDecimal xzkcl_t = xzkcl.subtract(xzrksl);
                    xzkcxxDto.setKcl(String.valueOf(xzkcl_t));
                    xzkcxxDto.setKwid(xzrkglDto.getKwid());
                    xzkcxxDto.setHwmc(xzrkmxDto.getHwmc());
                    xzkcxxDto.setHwbz(xzrkmxDto.getHwbz());
                    xzkcxxDto.setXzrkmxid(xzrkmxDto.getXzrkmxid());
                    xzkcxxDto.setRksl(xzrkmxDto.getRksl());
                    xzkcxxDto.setYkcl(String.valueOf(xzkcl_t.add(rksl)));
                    xzkcxxDto.setDbsl(String.valueOf(xzkcl_t));
                    xzkcxxDtos1.add(xzkcxxDto);
                }

                XzkcxxDto xzkcxxDto=new XzkcxxDto();
                List<XzkcxxDto> xzkcxxDtoList=xzkcxxService.getDtoList(xzkcxxDto);
                List<XzkcxxDto> xzkcxxDtoList1=new ArrayList<>();
                List<XzkcxxDto> xzkcxxDtoList2=new ArrayList<>();
                for (XzkcxxDto dto : xzkcxxDtoList) {
                    for (int i = xzkcxxDtos1.size() - 1; i >= 0; i--) {
                        if (xzkcxxDtos1.get(i).getHwmc().equals(dto.getHwmc()) && xzkcxxDtos1.get(i).getKwid().equals(dto.getKwid())
                                && xzkcxxDtos1.get(i).getHwbz().equals(dto.getHwbz())) {
                            dto.setKcl(xzkcxxDtos1.get(i).getRksl());
                            xzkcxxDtoList1.add(dto);
                            xzkcxxDtoList2.add(xzkcxxDtos1.get(i));
                            XzkcxxDto xzkcxxDto1 = xzkcxxDtos1.get(i);
                            xzkcxxDtos1.remove(xzkcxxDtos1.get(i));
                            for (XzrkmxDto xzrkmxDto : xzrkmxDtoList) {
                                if (xzkcxxDto1.getXzrkmxid().equals(xzrkmxDto.getXzrkmxid())) {
                                    xzrkmxDto.setXzkcid(dto.getXzkcid());
                                }
                            }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(xzkcxxDtoList1)){
                    for (XzkcxxDto xzkcxxDto1:xzkcxxDtoList1){
                        xzkcxxDto1.setUpdateFlag("add");
                    }
                    xzkcxxService.updateList(xzkcxxDtoList1);
                }
                if (!CollectionUtils.isEmpty(xzkcxxDtoList2)){
                    for (XzkcxxDto xzkcxxDto1:xzkcxxDtoList2){
                        xzkcxxDto1.setKwid(xzrkglDto.getYkwid());
                        xzkcxxDto1.setKcl(xzkcxxDto1.getDbsl());
                        xzkcxxDto1.setHwmc(null);
                        xzkcxxDto1.setHwbz(null);
                    }
                    xzkcxxService.updateListKclById(xzkcxxDtoList2);
                }
                if (!CollectionUtils.isEmpty(xzkcxxDtos1)){
                    for (XzkcxxDto xzkcxxDto1:xzkcxxDtos1){
                        xzkcxxDto1.setKwid(xzrkglDto.getYkwid());
                    }
                    xzkcxxService.updateListKclById(xzkcxxDtos1);
                    for (XzkcxxDto xzkcxxDto_t: xzkcxxDtos1){
                        xzkcxxDto_t.setKcl(xzkcxxDto_t.getYkcl());
                        xzkcxxDto_t.setKwid(xzrkglDto.getKwid());
                        xzkcxxDto_t.setXzkcid(StringUtil.generateUUID());
                        for (XzrkmxDto xzrkmxDto:xzrkmxDtoList){
                            if (xzkcxxDto_t.getXzrkmxid().equals(xzrkmxDto.getXzrkmxid())){
                                xzrkmxDto.setXzkcid(xzkcxxDto_t.getXzkcid());
                            }
                        }
                    }
                    xzkcxxService.insertList(xzkcxxDtos1);
                }
                if (!CollectionUtils.isEmpty(xzkcxxDtos1)){
                    for (XzkcxxDto xzkcxxDto1:xzkcxxDtos1){
                        xzkcxxDto1.setXzkcid(StringUtil.generateUUID());
                        for (XzrkmxDto xzrkmxDto:xzrkmxDtoList_t){
                            if (xzkcxxDto.getXzrkmxid().equals(xzkcxxDto1.getXzrkmxid())){
                                xzrkmxDto.setXzkcid(xzkcxxDto1.getXzkcid());
                            }
                        }
                    }
                }
                xzrkmxService.updateList(xzrkmxDtoList);
            }

//        }
        return true;
    }

    /**
     * 其他入库信息
     * @param
     * @return
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addSaveInStoreOther(XzrkglDto xzrkglDto) throws BusinessException{
        boolean result=dao.insert(xzrkglDto) != 0;
        if(!result) {
            throw new BusinessException("msg","入库信息保存失败!");
        }
        //保存货物领料信息
        List<XzrkmxDto> xzrkmxDtoList= JSON.parseArray(xzrkglDto.getQgmx_json(),XzrkmxDto.class);
        if(!CollectionUtils.isEmpty(xzrkmxDtoList)) {
            for (XzrkmxDto xzrkmxDto:xzrkmxDtoList) {
                xzrkmxDto.setXzrkid(xzrkglDto.getXzrkid());
                xzrkmxDto.setXzrkmxid(StringUtil.generateUUID());
                xzrkmxDto.setKcl(xzrkmxDto.getRksl());
                xzrkmxDto.setLrry(xzrkglDto.getLrry());
            }
            result = xzrkmxService.insertList(xzrkmxDtoList);
            if(!result) {
                throw new BusinessException("msg","入库明细信息保存失败!");
            }

            List<XzrkmxDto> xzkcList = xzrkmxService.getXzkcList(xzrkglDto.getXzrkid());
            List<XzkcxxDto> updatelist=new ArrayList<>();
            List<XzkcxxDto> addlist=new ArrayList<>();
            for(XzrkmxDto dto:xzkcList){
                if(StringUtil.isNotBlank(dto.getRkkcid())){
                    XzkcxxDto xzkcxxDto=new XzkcxxDto();
                    xzkcxxDto.setXzkcid(dto.getRkkcid());
                    xzkcxxDto.setKcl(dto.getRksl());
                    xzkcxxDto.setUpdateFlag("add");
                    updatelist.add(xzkcxxDto);
                }else{
                    XzkcxxDto xzkcxxDto=new XzkcxxDto();
                    xzkcxxDto.setXzkcid(StringUtil.generateUUID());
                    xzkcxxDto.setKcl(dto.getRksl());
                    xzkcxxDto.setKwid(dto.getKwid());
                    xzkcxxDto.setHwmc(dto.getRkhwmc());
                    xzkcxxDto.setHwbz(dto.getRkhwbz());
                    dto.setXzkcid(xzkcxxDto.getXzkcid());
                    addlist.add(xzkcxxDto);
                }
            }
            if(!CollectionUtils.isEmpty(updatelist)){
                result = xzkcxxService.updateList(updatelist);
                if(!result) {
                    throw new BusinessException("msg","行政库存信息更新失败!");
                }
            }
            if(!CollectionUtils.isEmpty(addlist)){
                result = xzkcxxService.insertList(addlist);
                if(!result) {
                    throw new BusinessException("msg","行政库存信息更新失败!");
                }

            }
            result = xzrkmxService.updateList(xzkcList);
            if(!result) {
                throw new BusinessException("msg","行政库存信息更新失败!");
            }

        }
        return true;
    }

    @Override
    public XzrkglDto getDtoByXzrkid(String xzrkid) {
        return dao.getDtoByXzrkid(xzrkid);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
        XzrkglDto xzrkglDto = (XzrkglDto) baseModel;
        xzrkglDto.setXgry(operator.getYhid());
        update(xzrkglDto);
        List<XzrkmxDto> xzrkmxDtoList= JSON.parseArray(xzrkglDto.getQgmx_json(),XzrkmxDto.class);
        for (XzrkmxDto xzrkmxDto:xzrkmxDtoList) {
            xzrkmxDto.setXzrkid(xzrkglDto.getXzrkid());
            xzrkmxDto.setXgry(operator.getYhid());
            xzrkmxService.updateAdministrationStockByXzrkmxid(xzrkmxDto);
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
//        String token = talkUtil.getToken();
        String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
        String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
        String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
        String ICOMM_SH00075 = xxglService.getMsg("ICOMM_SH00075");
        String ICOMM_SH00076 = xxglService.getMsg("ICOMM_SH00076");
        String ICOMM_SH00077 = xxglService.getMsg("ICOMM_SH00077");
        for (ShgcDto shgcDto : shgcList) {
            XzrkglDto xzrkglDto = new XzrkglDto();
            xzrkglDto.setXzrkid(shgcDto.getYwid());
            xzrkglDto.setXgry(operator.getYhid());
            XzrkglDto xzrkglDto_t = getDtoById(xzrkglDto.getXzrkid());
            List<SpgwcyDto> spgwcyDtos = shgcDto.getSpgwcyDtos();
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                xzrkglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), ICOMM_SH00026,
                                    StringUtil.replaceMsg(ICOMM_SH00077, operator.getZsxm(), shgcDto.getShlbmc(), xzrkglDto_t.getRkdh(), xzrkglDto_t.getRkrymc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                xzrkglDto.setZt(StatusEnum.CHECK_PASS.getCode());
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    int size = spgwcyDtos.size();
                    for (int i = 0; i < size; i++) {
                        if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
                            		shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00006,
                                    StringUtil. replaceMsg(ICOMM_SH00076, operator.getZsxm(),shgcDto.getShlbmc() ,xzrkglDto_t.getRkdh(),xzrkglDto_t.getRkrymc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }


            }else {
                xzrkglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                        spgwcyDto.getYhid(), ICOMM_SH00003, StringUtil.replaceMsg(ICOMM_SH00075,
                                                operator.getZsxm(), shgcDto.getShlbmc(), xzrkglDto_t.getRkdh(), xzrkglDto_t.getRkrymc(),
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
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
                            		shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,xzrkglDto_t.getRkdh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(xzrkglDto);
        }
        return true;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
		return true;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
        return null;
    }

    @Override
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
        Map<String, Object> map =new HashMap<>();
        @SuppressWarnings("unchecked")
        List<String> ids = (List<String>)param.get("ywids");
        XzrkglDto xzrkglDto = new XzrkglDto();
        xzrkglDto.setIds(ids);
        List<XzrkglDto> dtoList = dao.getDtoList(xzrkglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(XzrkglDto dto:dtoList){
                list.add(dto.getXzrkid());
            }
        }
        map.put("list",list);
        return map;
    }
}

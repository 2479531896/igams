package com.matridx.igams.sample.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.sample.dao.entities.SbglDto;
import com.matridx.igams.sample.dao.entities.YbdbDto;
import com.matridx.igams.sample.dao.entities.YbdbModel;
import com.matridx.igams.sample.dao.entities.YbdbmxDto;
import com.matridx.igams.sample.dao.entities.YbdbmxModel;
import com.matridx.igams.sample.dao.entities.YbkcxxDto;
import com.matridx.igams.sample.dao.post.IYbdbDao;
import com.matridx.igams.sample.service.svcinterface.ISbglService;
import com.matridx.igams.sample.service.svcinterface.IYbdbService;
import com.matridx.igams.sample.service.svcinterface.IYbdbmxService;
import com.matridx.igams.sample.service.svcinterface.IYbkcxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class YbdbSerivceImpl extends BaseBasicServiceImpl<YbdbDto, YbdbModel, IYbdbDao> implements IYbdbService, IAuditService {

    @Autowired
    IYbdbmxService ybdbmxService;
    @Autowired
    ISbglService sbglService;
    @Autowired
    IYbkcxxService ybkcxxService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ICommonService commonService;
    private final Logger log = LoggerFactory.getLogger(YbdbSerivceImpl.class);

    /**
     * 样本调拨保存
     * @param ybdbDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean saveSampleAllot(YbdbDto ybdbDto) throws BusinessException {
        List<YbdbmxDto> old_ybdbmxDtos = new ArrayList<>();
        if (StringUtil.isBlank(ybdbDto.getYbdbid())){
            ybdbDto.setYbdbid(StringUtil.generateUUID());
            //1、样本调拨表保存
            int insert = dao.insert(ybdbDto);
            if (insert <= 0){
                return false;
            }
        } else {
            int update = dao.update(ybdbDto);
            if (update <= 0){
                return false;
            }
            YbdbmxDto ybdbmxDto_t = new YbdbmxDto();
            ybdbmxDto_t.setYbdbid(ybdbDto.getYbdbid());
            old_ybdbmxDtos = ybdbmxService.getDbmxDtos(ybdbmxDto_t);
        }
        //2、样本调拨明细表保存
        List<YbdbmxDto> ybdbmxDtos = ybdbDto.getYbdbmxDtos();
        List<YbdbmxDto> add_ybdbmxDtos = new ArrayList<>();
        List<YbdbmxDto> upd_ybdbmxDtos = new ArrayList<>();
        List<YbdbmxDto> del_ybdbmxDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(old_ybdbmxDtos)){
            for (int i = ybdbmxDtos.size() - 1; i >= 0; i--) {
                for (int j = old_ybdbmxDtos.size() - 1; j >= 0; j--) {
                    //若序号和调出盒子一致，则为不变样本调拨明细
                    if (ybdbmxDtos.get(i).getDchz().equals(old_ybdbmxDtos.get(j).getDchz()) && ybdbmxDtos.get(i).getXh().equals(old_ybdbmxDtos.get(j).getXh())){
                        upd_ybdbmxDtos.add(ybdbmxDtos.get(i));
                        old_ybdbmxDtos.remove(old_ybdbmxDtos.get(j));
                        ybdbmxDtos.remove(ybdbmxDtos.get(i));
                        break;
                    }
                }
            }
            add_ybdbmxDtos = ybdbmxDtos;
            del_ybdbmxDtos = old_ybdbmxDtos;
        }else {
            add_ybdbmxDtos.addAll(ybdbmxDtos);
        }
        //3、根据盒子的sbid查询其存放数，并set到ybs中
        //删除部分
        if (!CollectionUtils.isEmpty(del_ybdbmxDtos)){
            List<String> del_sbids = del_ybdbmxDtos.stream().map( ybdbmxDto -> ybdbmxDto.getDchz()).collect(Collectors.toList());
            YbdbmxDto ybdbmxDto = new YbdbmxDto();
            ybdbmxDto.setHzs(del_sbids);
            ybdbmxDto.setScry(ybdbDto.getXgry());
            boolean isSuccess = ybdbmxService.delete(ybdbmxDto);
            if (isSuccess){
                YbkcxxDto ybkcxxDto = new YbkcxxDto();
                ybkcxxDto.setHzids(del_sbids);
                ybkcxxDto.setYdbj("0");
                ybkcxxDto.setXgry(ybdbDto.getXgry());
                isSuccess = ybkcxxService.updateYdbjByHzids(ybkcxxDto);
                if (!isSuccess){
                    throw new BusinessException("样本库存信息更新失败!");
                }
            } else {
                throw new BusinessException("样本调拨明细保存失败!");
            }
        }
        //新增部分
        if (!CollectionUtils.isEmpty(add_ybdbmxDtos)){
            List<String> add_sbids = add_ybdbmxDtos.stream().map( ybdbmxDto -> ybdbmxDto.getDchz()).collect(Collectors.toList());
            SbglDto add_sbglDto = new SbglDto();
            add_sbglDto.setIds(add_sbids);
            List<SbglDto> add_sbglDtos = sbglService.getDeviceInfoBySbids(add_sbglDto);
            List<YbdbmxModel> ybdbmxModels = new ArrayList<>();
            for (YbdbmxDto ybdbmxDto : add_ybdbmxDtos) {
                YbdbmxModel ybdbmxModel = new YbdbmxModel();
                ybdbmxModel.setDbmxid(StringUtil.generateUUID());
                ybdbmxModel.setYbdbid(ybdbDto.getYbdbid());
                ybdbmxModel.setXh(ybdbmxDto.getXh());
                ybdbmxModel.setDchz(ybdbmxDto.getDchz());
                ybdbmxModel.setLrry(StringUtil.isNotBlank(ybdbDto.getLrry())?ybdbDto.getLrry():ybdbDto.getXgry());
                for (int i = add_sbglDtos.size() - 1; i >= 0; i--) {
                    if (add_sbglDtos.get(i).getSbid().equals(ybdbmxDto.getDchz())){
                        ybdbmxModel.setYbs(add_sbglDtos.get(i).getYcfs());
                        add_sbglDtos.remove(i);
                        break;
                    }
                }
                ybdbmxModels.add(ybdbmxModel);
            }
            boolean isSuccess = ybdbmxService.batchInsert(ybdbmxModels);
            if (isSuccess){
                YbkcxxDto ybkcxxDto = new YbkcxxDto();
                ybkcxxDto.setHzids(add_sbids);
                ybkcxxDto.setYdbj("2");
                ybkcxxDto.setXgry(StringUtil.isNotBlank(ybdbDto.getLrry())?ybdbDto.getLrry():ybdbDto.getXgry());
                isSuccess = ybkcxxService.updateYdbjByHzids(ybkcxxDto);
                if (!isSuccess){
                    throw new BusinessException("样本库存信息更新失败!");
                }
            } else {
                throw new BusinessException("样本调拨明细保存失败!");
            }
        }
        return true;
    }


    /**
     * 样本调拨删除
     * @param ybdbDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delSampleAllot(YbdbDto ybdbDto) throws BusinessException {
        YbdbmxDto ybdbmxDto = new YbdbmxDto();
        ybdbmxDto.setYbdbids(ybdbDto.getIds());
        List<String> erroList = ybdbmxService.checkCanDelete(ybdbmxDto);
        if (!CollectionUtils.isEmpty(erroList)){
            throw new BusinessException(erroList.get(0));
        }
        /*  1.根据调入盒子修改igams_ybkcxx表的删除标记为1，删除人员为当前登陆用户，删除时间为当前时间。
            2.根据调出盒子修改igams_ybkcxx表的预定标记，预定标记改为0（正常），修改人员为当前登陆用户，修改时间为当前时间。
            3.根据调入盒子修改igams_sbgl表的已存放数，已存放数=已存放数-igams_dbmx表的样本数
            4.根据调出盒子修改igams_sbgl表的已存放数，已存放数=已存放数+调拨明细的样本数。
        */
        ybdbDto.setXgry(ybdbDto.getScry());
        ybdbDto.setScbj("1");
        ybdbDto.setYdbj("0");
        boolean result = ybkcxxService.deleteBydbmx(ybdbDto);
        if(!result){
            throw new BusinessException("修改审核通过数据失败！");
        }
        //根据调出盒子id和预定标记=2（处理中）修改igams_ybkcxx表的预定标记，预定标记改为0（正常），修改人员为当前登陆用户，修改时间为当前时间
        ybdbDto.setYdbj("0");
        ybkcxxService.updateYbkcxx(ybdbDto);

        //调拨信息删除
        int ybdbdel = dao.delByYbdbid(ybdbDto);
        if (ybdbdel<=0){
            throw new BusinessException("删除调拨管理表数据失败！");
        }
        boolean isSuccess = ybdbmxService.delete(ybdbmxDto);
        if (!isSuccess){
            throw new BusinessException("删除调拨明细表数据失败！");
        }

        return true;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        YbdbDto ybdbDto = (YbdbDto) baseModel;
        ybdbDto.setXgry(operator.getYhid());
        if (StringUtil.isNotBlank(ybdbDto.getHzInfo_json())){
            List<YbdbmxDto> ybdbmxDtos = JSONArray.parseArray(ybdbDto.getHzInfo_json(), YbdbmxDto.class);
            ybdbDto.setYbdbmxDtos(ybdbmxDtos);
        }
        //特殊审核
        if ("commonAudit".equals(ybdbDto.getFormAction())){
            return commonAuditSaveAllot(ybdbDto);
        }else {
            return saveSampleAllot(ybdbDto);
        }

    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private boolean commonAuditSaveAllot(YbdbDto ybdbDto) throws BusinessException {
        dao.update(ybdbDto);
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        try {
            for (ShgcDto shgcDto : shgcList) {
                String ybdbid = shgcDto.getYwid();
                YbdbDto ybdbDto = new YbdbDto();
                ybdbDto.setYbdbid(ybdbid);
                ybdbDto.setXgry(operator.getYhid());
                YbdbDto ybdbDto_sel = dao.getDto(ybdbDto);
                List<SpgwcyDto> spgwcyDtos = commonService.siftJcdwList(shgcDto.getSpgwcyDtos(), ybdbDto_sel.getDrccdw());
                String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
                String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
                if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                    // 审核退回
                    ybdbDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                    // 发送钉钉消息
                    if (!CollectionUtils.isEmpty(spgwcyDtos) ) {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                        spgwcyDto.getYhid(), ICOMM_SH00026,
                                        xxglService.getMsg("ICOMM_YBDB001", operator.getZsxm(), shgcDto.getShlbmc(), ybdbDto_sel.getDcccdwmc(),
                                                ybdbDto_sel.getDrccdwmc(),
                                                ybdbDto_sel.getDbrmc(),
                                                ybdbDto_sel.getDbrq()));
                            }
                        }
                    }
                } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                    ybdbDto.setZt(StatusEnum.CHECK_PASS.getCode());
                    // 审核通过
                    YbdbmxDto ybdbmxDto = new YbdbmxDto();
                    ybdbmxDto.setYbdbid(ybdbid);
                    List<YbdbmxDto> dbmxDtos = ybdbmxService.getDbmxDtos(ybdbmxDto);
                    //⑤.新增数据至igams_ybkcxx,可以根据调出盒子查igams_ybkcxx表根据盒子id关联样本调拨明细表查出List
                    YbkcxxDto ybkcxxDto_sel = new YbkcxxDto();
                    ybkcxxDto_sel.setYdbj("2");
                    ybkcxxDto_sel.setHzids(dbmxDtos.stream().map(YbdbmxDto::getDchz).collect(Collectors.toList()));
                    List<YbkcxxDto> ybkcxxDtos = ybkcxxService.getDtoList(ybkcxxDto_sel);
                    //②.根据调出盒子id修改igams_ybkcxx表的预定标记，预定标记改为3（销毁）。
                    YbkcxxDto ybkcxxDto = new YbkcxxDto();
                    ybkcxxDto.setYbdbid(ybdbid);
                    ybkcxxDto.setYdbj("3");
                    ybkcxxService.updateByDbmx(ybkcxxDto);

                    //③.根据调出盒子id修改igasm_sbgl表的ycfs,ycfs=0.
                    SbglDto sbglDto_dc = new SbglDto();
                    sbglDto_dc.setIds(dbmxDtos.stream().map(YbdbmxDto::getDchz).collect(Collectors.toList()));
                    sbglDto_dc.setXgry(operator.getYhid());
                    sbglDto_dc.setYcfs("0");
                    sbglService.updateYcfsForMods(sbglDto_dc);
                    //④.根据调入盒子id修改igams_sbgl表的ycfs,ycfs=样本调拨明细表的ybs.
//                    List<SbglDto> modSbglDtos = new ArrayList<>();
//                    for (YbdbmxDto dbmxDto : dbmxDtos) {
//                        SbglDto sbglDto_dr = new SbglDto();
//                        sbglDto_dr.setSbid(dbmxDto.getDrhz());
//                        sbglDto_dr.setXgry(operator.getYhid());
//                        sbglDto_dr.setYcfs(dbmxDto.getYbs());
//                        modSbglDtos.add(sbglDto_dr);
//                    }
//                    sbglService.batchUpdateYcfs(modSbglDtos);
                    List<SbglDto> addSbglDtos = new ArrayList<>();
                    //冰箱
                    SbglDto sbglDto = new SbglDto();
                    sbglDto.setSbh(ybdbDto_sel.getDcbxsbh());
                    sbglDto.setWz(ybdbDto_sel.getDcbxmc());
                    sbglDto.setSblx("bx");
                    sbglDto.setJcdw(ybdbDto_sel.getDrccdw());
                    sbglDto = sbglService.getDto(sbglDto);
                    String bxid= StringUtil.generateUUID();
                    if(sbglDto==null){
                        SbglDto sbglDtoBx = new SbglDto();
                        sbglDtoBx.setSbid(bxid);
                        sbglDtoBx.setSblx("bx");
                        sbglDtoBx.setSbh(ybdbDto_sel.getDcbxsbh());
                        sbglDtoBx.setPx(ybdbDto_sel.getBxpx());
                        sbglDtoBx.setLrry(operator.getYhid());
                        sbglDtoBx.setWz(ybdbDto_sel.getDcbxmc());
                        sbglDtoBx.setJcdw(ybdbDto_sel.getDrccdw());
                        addSbglDtos.add(sbglDtoBx);
                    }else{
                        bxid = sbglDto.getSbid();
                    }
                    //抽屉
                    SbglDto sbglDtoCtQ = new SbglDto();
                    sbglDtoCtQ.setSbh(ybdbDto_sel.getDcctmc());
                    sbglDtoCtQ.setWz(ybdbDto_sel.getDcctwz());
                    sbglDtoCtQ.setSblx("ct");
                    sbglDtoCtQ.setBxJcdw(ybdbDto_sel.getDrccdw());
                    sbglDtoCtQ = sbglService.getDto(sbglDtoCtQ);
                    String ctid = StringUtil.generateUUID();
                    if(sbglDtoCtQ==null){
                        SbglDto sbglDtoCt = new SbglDto();
                        sbglDtoCt.setSbid(ctid);
                        sbglDtoCt.setFsbid(bxid);
                        sbglDtoCt.setSblx("ct");
                        sbglDtoCt.setSbh(ybdbDto_sel.getDcctmc());
                        sbglDtoCt.setPx(ybdbDto_sel.getCtpx());
                        sbglDtoCt.setLrry(operator.getYhid());
                        sbglDtoCt.setWz(ybdbDto_sel.getDcctwz());
                        addSbglDtos.add(sbglDtoCt);
                    }else{
                        ctid = sbglDtoCtQ.getSbid();
                    }
                    //调出调入对应
                    Map<String, String> dyMap = new HashMap<>();
                    List<YbdbmxDto> ybdbmxDtos = new ArrayList<>();
                    for (YbdbmxDto dbmxDto : dbmxDtos) {
                        String hzid= StringUtil.generateUUID();
                        //盒子
                        SbglDto sbglDtoHz = new SbglDto();
                        sbglDtoHz.setSbid(hzid);
                        sbglDtoHz.setFsbid(ctid);
                        sbglDtoHz.setSblx("hz");
                        sbglDtoHz.setSbh(dbmxDto.getDchzsbh());
                        sbglDtoHz.setPx(dbmxDto.getHzpx());
                        sbglDtoHz.setLrry(operator.getYhid());
                        sbglDtoHz.setWz(dbmxDto.getDchzmc());
                        sbglDtoHz.setCfs(dbmxDto.getCfs());
                        sbglDtoHz.setYcfs(dbmxDto.getYcfs());
                        addSbglDtos.add(sbglDtoHz);
                        YbdbmxDto ybdbmxDtoT = new YbdbmxDto();
                        ybdbmxDtoT.setDbmxid(dbmxDto.getDbmxid());
                        ybdbmxDtoT.setDrhz(hzid);
                        ybdbmxDtoT.setXgry(operator.getYhid());
                        ybdbmxDtos.add(ybdbmxDtoT);
                        dyMap.put(dbmxDto.getDchz(),dbmxDto.getDrhz());
                        dyMap.put(dbmxDto.getDcct(),dbmxDto.getDrct());
                        dyMap.put(dbmxDto.getDcbx(),dbmxDto.getDrbx());
                        dyMap.put(dbmxDto.getDcccdw(),dbmxDto.getDrccdw());
                        dyMap.put("dbmxid_"+dbmxDto.getDchz(),dbmxDto.getDbmxid());
                        dyMap.put("drhzbh_"+dbmxDto.getDchz(),dbmxDto.getDchzsbh());
                        dyMap.put("dchz"+dbmxDto.getDchz(),hzid);
                    }
                    boolean result = sbglService.insetList(addSbglDtos);
                    if(!result){
                        throw new BusinessException("新增设备失败!");
                    }
                    result = ybdbmxService.updateYbdbmxDtos(ybdbmxDtos);
                    if(!result){
                        throw new BusinessException("更新调入盒子失败!");
                    }
                    List<YbkcxxDto> addYbkcxxDtos = new ArrayList<>();
                    // （查样本库存信息表所有字段和调拨明细id）,
                    //		将ybkcid改为新生成的UUID,检测单位改为调入检测单位，冰箱改为调入冰箱，抽屉改为调入抽屉，
                    //		录入人员为当前登陆用户，录入时间为当前时间，删除标记为0，
                    //		预定标记为0，样本库存信息表增加字段调拨明细id,将List新增到igams_ybkcxx表。
                    for (YbkcxxDto dto : ybkcxxDtos) {
                        YbkcxxDto ybkcxxDto_add = new YbkcxxDto();
                        ybkcxxDto_add.setYbkcid(StringUtil.generateUUID());
                        ybkcxxDto_add.setSjid(dto.getSjid());
                        ybkcxxDto_add.setJcdw(dyMap.get(dto.getJcdw()));
                        ybkcxxDto_add.setBxid(bxid);
                        ybkcxxDto_add.setChtid(ctid);
                        ybkcxxDto_add.setHzid(dyMap.get("dchz"+dto.getHzid()));
                        ybkcxxDto_add.setYblrlx(dto.getYblrlx());
                        ybkcxxDto_add.setTj(dto.getTj());
                        ybkcxxDto_add.setLrry(operator.getYhid());
                        ybkcxxDto_add.setWz(dto.getWz());
                        ybkcxxDto_add.setDbmxid(dyMap.get("dbmxid_"+dto.getHzid()));
                        //⑥.样本库存信息表增加盒子编号，根据调出盒子id，修改样本库存信息表的盒子编号，盒子编号为设备管理表的盒子编号。
                        ybkcxxDto_add.setHzbh(dyMap.get("drhzbh_"+dto.getHzid()));
                        addYbkcxxDtos.add(ybkcxxDto_add);
                    }
                    ybkcxxService.insertYbkcxxDtos(addYbkcxxDtos);
                    if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                        spgwcyDto.getYhid(), ICOMM_SH00006,
                                        xxglService.getMsg("ICOMM_YBDB002", operator.getZsxm(), shgcDto.getShlbmc(), ybdbDto_sel.getDcccdwmc(),
                                                ybdbDto_sel.getDrccdwmc(),
                                                ybdbDto_sel.getDbrmc(),
                                                ybdbDto_sel.getDbrq()));
                            }
                        }
                    }
                } else {
                    //审核中
                    ybdbDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                    if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                        spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00003"),
                                        xxglService.getMsg("ICOMM_YBDB003", operator.getZsxm(), shgcDto.getShlbmc(), ybdbDto_sel.getDcccdwmc(),
                                                ybdbDto_sel.getDrccdwmc(),
                                                ybdbDto_sel.getDbrmc(),
                                                ybdbDto_sel.getDbrq()));
                            }
                        }
                    }
                    //发送钉钉消息--取消审核人员
                    if (shgcDto.getNo_spgwcyDtos() != null && shgcDto.getNo_spgwcyDtos().size() > 0) {
                        try {
                            int size = shgcDto.getNo_spgwcyDtos().size();
                            for (int i = 0; i < size; i++) {
                                if (StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())) {
                                    talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
                                            shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"), xxglService.getMsg("ICOMM_SH00005", operator.getZsxm(), shgcDto.getShlbmc(),"样本调拨申请", DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                                }
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                }
                //更新状态
                dao.updateZt(ybdbDto);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String ybdbid = shgcDto.getYwid();
                YbdbDto ybdbDto = new YbdbDto();
                ybdbDto.setYbdbid(ybdbid);
                ybdbDto.setXgry(operator.getYhid());
                ybdbDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.updateZt(ybdbDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String ybdbid = shgcDto.getYwid();
                YbdbDto ybdbDto = new YbdbDto();
                ybdbDto.setYbdbid(ybdbid);
                ybdbDto.setXgry(operator.getYhid());
                ybdbDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.updateZt(ybdbDto);
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
        YbdbDto ybdbDto=new YbdbDto();
        ybdbDto.setIds(ids);
        List<YbdbDto> dtoList = dao.getDtoList(ybdbDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(YbdbDto dto:dtoList){
                list.add(dto.getYbdbid());
            }
        }
        map.put("list",list);
        return map;
    }

    @Override
    public List<YbdbDto> getPagedAuditSampleAllot(YbdbDto ybdbDto) {
        List<YbdbDto> t_ybdbList = dao.getPagedAuditSampleAllot(ybdbDto);
        if(CollectionUtils.isEmpty(t_ybdbList))
            return t_ybdbList;
        List<YbdbDto> sqList = dao.getAuditListSampleAllot(t_ybdbList);
        commonService.setSqrxm(sqList);
        return sqList;
    }
}

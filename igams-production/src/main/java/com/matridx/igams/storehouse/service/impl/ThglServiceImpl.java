package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.dao.post.IDhxxDao;
import com.matridx.igams.storehouse.dao.post.IHwxxDao;
import com.matridx.igams.storehouse.dao.post.IThglDao;
import com.matridx.igams.storehouse.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Service
public class ThglServiceImpl extends BaseBasicServiceImpl<ThglDto, ThglModel, IThglDao> implements IThglService, IAuditService {

    @Autowired
    IThmxService thmxService;
    @Autowired
    IFhmxService fhmxService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ICommonService commonService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IDhxxDao dhxxDao;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IHwxxDao hwxxDao;
    @Autowired
    IRdRecordService rdRecordService;
    @Autowired
    ICkglService ckglService;
    @Autowired
    ICkmxService ckmxService;
    @Autowired
    IHwxxService hwxxService;
    @Autowired
    IXsmxService xsmxService;
    @Value("${matridx.rabbit.systemreceiveflg:}")
    private String systemreceiveflg;
    @Value("${sqlserver.matridxds.flag:}")
    private String matridxdsflag;
    private final Logger log = LoggerFactory.getLogger(ThglServiceImpl.class);

    /**
     * 自动生成退货单号
     * @return
     */
    public String generateThdh() {
        // TODO Auto-generated method stub
        // 生成规则: LL-20201022-01 LL-年份日期-流水号 。
        String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
        String prefix = "TH" + "-" + date + "-";
        // 查询流水号
        String serial = dao.getThdhSerial(prefix);
        return prefix + serial;
    }
    /**
     * 校验领料单号是否重复
     * @param thglDto
     * @return
     */
    public boolean getDtoByThdh(ThglDto thglDto) {
        ThglDto ThglDto_t = dao.getDtoByThdh(thglDto);
        return ThglDto_t==null;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveReturnManagements(ThglDto thglDto) throws BusinessException  {
        int update = dao.update(thglDto);
        if (update <1){
            throw new BusinessException("msg","修改退货信息失败！");
        }
        List<FhmxDto> fhmxDtos = new ArrayList<>();
        List<ThmxDto> xgqthmxDtos = JSON.parseArray(thglDto.getXgqthmx_json(), ThmxDto.class);
        List<ThmxDto> thmxDtos = JSON.parseArray(thglDto.getThmx_json(), ThmxDto.class);
        List<HwxxDto> upHwxxDtos = new ArrayList<>();
        List<HwxxDto> addHwxxDtos = new ArrayList<>();
        List<ThmxDto> addThmxDtos = new ArrayList<>();
        List<String> delhwids = new ArrayList<>();
        List<String> delthmxids = new ArrayList<>();
        //初始化发货明细的退货数量
        for (ThmxDto xgqthmxDto : xgqthmxDtos) {
            FhmxDto fhmxDto = new FhmxDto();
            fhmxDto.setThslbj("0");
            fhmxDto.setThsl(xgqthmxDto.getThsl());
            fhmxDto.setFhmxid(xgqthmxDto.getFhmxid());
            fhmxDtos.add(fhmxDto);
        }
        for (ThmxDto thmxDto : thmxDtos) {
            //剩下的就是删除的
            xgqthmxDtos.removeIf(next -> next.getThmxid().equals(thmxDto.getThmxid()));
            //空的新增
            if (StringUtil.isBlank(thmxDto.getThmxid())){
                HwxxDto hwxxDto = new HwxxDto();
                String hwid = StringUtil.generateUUID();
                hwxxDto.setHwid(hwid);
                hwxxDto.setCkid(thmxDto.getCk());
                hwxxDto.setDhsl(thmxDto.getThsl());
                hwxxDto.setWlid(thmxDto.getWlid());
                hwxxDto.setYxq(thmxDto.getYxq());
                hwxxDto.setScph(thmxDto.getScph());
                hwxxDto.setScrq(thmxDto.getScrq());
                hwxxDto.setKwbh(thmxDto.getKw());
                hwxxDto.setZsh(thmxDto.getZsh());
                hwxxDto.setLrry(thglDto.getXgry());
                hwxxDto.setCskw(thmxDto.getKw());
                hwxxDto.setZt(GoodsStatusEnum.GODDS_CHECK.getCode());
                addHwxxDtos.add(hwxxDto);

                thmxDto.setThmxid(StringUtil.generateUUID());
                thmxDto.setThid(thglDto.getThid());
                thmxDto.setHwid(hwid);
                thmxDto.setLrry(thglDto.getXgry());
                addThmxDtos.add(thmxDto);
            }else {
                //有的修改
                HwxxDto hwxxDto = new HwxxDto();
                hwxxDto.setDhsl(thmxDto.getThsl());
                hwxxDto.setHwid(thmxDto.getHwid());
                hwxxDto.setCkid(thmxDto.getCk());
                hwxxDto.setKwbh(thmxDto.getKw());
                upHwxxDtos.add(hwxxDto);
            }
            FhmxDto fhmxDto = new FhmxDto();
            fhmxDto.setFhmxid(thmxDto.getFhmxid());
            fhmxDto.setThslbj("1");
            fhmxDto.setThsl(thmxDto.getThsl());
            fhmxDtos.add(fhmxDto);
        }
        if (!CollectionUtils.isEmpty(xgqthmxDtos)) {
            for (ThmxDto xgqthmxDto : xgqthmxDtos) {
                delthmxids.add(xgqthmxDto.getThmxid());
                delhwids.add(xgqthmxDto.getHwid());
            }
        }
        if (!CollectionUtils.isEmpty(delthmxids)){
            ThmxDto thmxDto = new ThmxDto();
            thmxDto.setIds(delthmxids);
            thmxDto.setScry(thglDto.getXgry());
            boolean delthmx = thmxService.deleteByThmxids(thmxDto);
            if (!delthmx){
                throw new BusinessException("msg","删除退货明细失败！");
            }
            HwxxDto hwxxDto = new HwxxDto();
            hwxxDto.setIds(delhwids);
            hwxxDto.setScry(thglDto.getXgry());
            boolean delhwxx = hwxxDao.deleteHwxxDtos(hwxxDto);
            if (!delhwxx){
                throw new BusinessException("msg","删除货物信息失败！");
            }
        }
        if (!CollectionUtils.isEmpty(addThmxDtos)){
            boolean result = thmxService.insertThmxList(addThmxDtos);
            if (!result){
                throw new BusinessException("msg","新增退货明细失败！");
            }
            boolean inhwxxs = hwxxDao.insertHwxxList(addHwxxDtos);
            if (!inhwxxs){
                throw new BusinessException("msg","新增货物信息失败");
            }
        }
        if (!CollectionUtils.isEmpty(thmxDtos)){
            boolean isSuccess = thmxService.updateThmxDtos(thmxDtos);
            if (!isSuccess){
                throw new BusinessException("msg","修改退货明细失败！");
            }
        }
        if (!CollectionUtils.isEmpty(fhmxDtos)){
            boolean isSuccess = fhmxService.updateThsls(fhmxDtos);
            if (!isSuccess){
                throw new BusinessException("msg","同步退货数量失败！");
            }
        }
        if (!CollectionUtils.isEmpty(upHwxxDtos)){
            boolean updhsl = hwxxDao.updateHwxxs(upHwxxDtos);
            if (!updhsl){
                throw new BusinessException("msg","同步货物信息失败！");
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delReturnManagement(ThglDto thglDto) throws BusinessException {
        ThmxDto thmxDto = new ThmxDto();
        thmxDto.setThids(thglDto.getIds());
        List<ThmxDto> thmxDtos = thmxService.getDtoListByIds(thmxDto);
        List<String> dhids = new ArrayList<>();
        for (ThmxDto dto : thmxDtos) {
            if (StringUtil.isNotBlank(dto.getDhjyid())){
                throw new BusinessException("msg","退货单: "+dto.getThdh()+"的物料已质检(单号："+dto.getJydh()+")，请先删除检验单");
            }
            if (StringUtil.isNotBlank(dto.getDhid())){
                dhids.add(dto.getDhid());
            }
        }

        int delth = dao.delete(thglDto);
        if (delth<1){
            throw new BusinessException("msg","删除退货信息失败！");
        }
        if (!CollectionUtils.isEmpty(dhids)){
            DhxxDto dhxxDto = new DhxxDto();
            dhxxDto.setIds(dhids);
            dhxxDto.setScry(thglDto.getScry());
            int delete = dhxxDao.delete(dhxxDto);
            if (delete<1){
                throw new BusinessException("msg","删除对应到货信息失败！");
            }
        }
        List<FhmxDto> fhmxDtos = new ArrayList<>();
        List<String> hwids = new ArrayList<>();
        for (ThmxDto dto : thmxDtos) {
            FhmxDto fhmxDto = new FhmxDto();
            fhmxDto.setFhmxid(dto.getFhmxid());
            fhmxDto.setThsl(dto.getThsl());
            fhmxDto.setThslbj("0");
            fhmxDtos.add(fhmxDto);
            hwids.add(dto.getHwid());
        }
        boolean isSuccess = fhmxService.updateThsls(fhmxDtos);
        if (!isSuccess){
            throw new BusinessException("msg","同步退货数量失败！");
        }
        HwxxDto hwxxDto = new HwxxDto();
        hwxxDto.setIds(hwids);
        hwxxDto.setScry(thglDto.getScry());
        boolean delhw = hwxxDao.deleteHwxxDtos(hwxxDto);
        if (!delhw){
            throw new BusinessException("msg","删除货物信息失败！");
        }
        thmxDto.setScry(thglDto.getScry());
        boolean delthmx = thmxService.delete(thmxDto);
        if (!delthmx){
            throw new BusinessException("msg","删除退货明细信息失败！");
        }
        shgcService.deleteByYwids(thglDto.getIds());
        return true;
    }

    /**
     * 新增保存退货信息
     * @param thglDto
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveReturnManagements(ThglDto thglDto) throws BusinessException {
        String thid = StringUtil.generateUUID();
        thglDto.setThid(thid);
        int insertThgl = dao.insert(thglDto);
        if (insertThgl <1){
            throw new BusinessException("msg","新增退货信息失败！");
        }
        List<ThmxDto> thmxDtos = JSON.parseArray(thglDto.getThmx_json(), ThmxDto.class);
        List<FhmxDto> fhmxDtos = new ArrayList<>();
        List<HwxxDto> hwxxDtos = new ArrayList<>();
        for (ThmxDto thmxDto : thmxDtos) {
            HwxxDto hwxxDto = new HwxxDto();
            String hwid = StringUtil.generateUUID();
            hwxxDto.setHwid(hwid);
            hwxxDto.setCkid(thmxDto.getCk());
            hwxxDto.setDhsl(thmxDto.getThsl());
            hwxxDto.setWlid(thmxDto.getWlid());
            hwxxDto.setYxq(thmxDto.getYxq());
            hwxxDto.setScph(thmxDto.getScph());
            hwxxDto.setScrq(thmxDto.getScrq());
            hwxxDto.setKwbh(thmxDto.getKw());
            hwxxDto.setZsh(thmxDto.getZsh());
            hwxxDto.setLrry(thglDto.getLrry());
            hwxxDto.setCskw(thmxDto.getKw());
            hwxxDto.setZt(GoodsStatusEnum.GODDS_CHECK.getCode());
            hwxxDtos.add(hwxxDto);

            thmxDto.setThmxid(StringUtil.generateUUID());
            thmxDto.setThid(thid);
            thmxDto.setHwid(hwid);
            thmxDto.setLrry(thglDto.getLrry());

            FhmxDto fhmxDto = new FhmxDto();
            fhmxDto.setFhmxid(thmxDto.getFhmxid());
            fhmxDto.setThslbj("1");
            fhmxDto.setThsl(thmxDto.getThsl());
            fhmxDtos.add(fhmxDto);
        }
        boolean result = thmxService.insertThmxList(thmxDtos);
        if (!result){
            throw new BusinessException("msg","新增退货明细失败！");
        }
        boolean inhwxxs = hwxxDao.insertHwxxList(hwxxDtos);
        if (!inhwxxs){
            throw new BusinessException("msg","新增货物信息失败");
        }
        boolean updateThsls = fhmxService.updateThsls(fhmxDtos);
        if (!updateThsls){
            throw new BusinessException("msg","同步退货数量失败！");
        }
        return true;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        ThglDto thglDto = (ThglDto)baseModel;
        update(thglDto);
        boolean isSuccess=this.getDtoByThdh(thglDto);
        if(!isSuccess&&!thglDto.getXgqthdh().equals(thglDto.getThdh())) {
            throw new BusinessException("msg","退货单号不允许重复!");
        }
        return this.modSaveReturnManagements(thglDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
        String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
//        String token = talkUtil.getToken();
        for (ShgcDto shgcDto : shgcList) {
            ThglDto thglDto = new ThglDto();
            thglDto.setThid(shgcDto.getYwid());
            thglDto.setXgry(operator.getYhid());
            ThglDto thglDto_t = getDto(thglDto);
            ThmxDto thmxDto = new ThmxDto();
            thmxDto.setThid(shgcDto.getYwid());
            List<ThmxDto> dtoList = thmxService.getDtoList(thmxDto);
            List<SpgwcyDto> spgwcyDtos = shgcDto.getSpgwcyDtos();
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                thglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), ICOMM_SH00026,
                                    xxglService.getMsg("ICOMM_SH00122", operator.getZsxm(), shgcDto.getShlbmc(), thglDto_t.getThdh(),
                                            thglDto_t.getJsrmc(),
                                            thglDto_t.getXsbmmc(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                thglDto_t.setXgry(operator.getYhid());
                thglDto_t.setZsxm(operator.getZsxm());
                //同过hwid 查找hwxx表数据
                HwxxDto hwxxDto_t = new HwxxDto();
                List<String> hwids= new ArrayList<>();
                for (ThmxDto thmx:dtoList) {
                    hwids.add(thmx.getHwid());
                }
                hwxxDto_t.setIds(hwids);
                List<HwxxDto> hwxxDtos_t = hwxxService.getCkInfo(hwxxDto_t);
                JcsjDto jcsjDto_t = new JcsjDto();
                jcsjDto_t.setJclb("DELIVERY_TYPE");
                jcsjDto_t.setCsdm("XSCK");
                jcsjDto_t = jcsjService.getDto(jcsjDto_t);
                if(jcsjDto_t==null) {
                    throw new BusinessException("msg","未找到出库类型销售出库！");
                }
                //存出库数据
                List<CkglDto> ckglDtos = new ArrayList<>();
                //存出库明细表数据
                List<CkmxDto> ckmxDtos = new ArrayList<>();
                //生成出库单 生成规则: CK-20201022-01 LL-年份日期-流水号 。
                String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
                String prefix = "CK" + "-" + date + "-";
                String serial = ckglService.generateDjh(prefix);
                int serial_int = Integer.parseInt(serial)-1;
                for (HwxxDto hwxxDto : hwxxDtos_t) {
                    CkglDto ckglDto = new CkglDto();
                    ckglDto.setCkid(StringUtil.generateUUID()); //出库id
                    ckglDto.setBm(thglDto_t.getXsbm()); //部门
                    ckglDto.setFlr(thglDto_t.getJsr()); //经手人
                    ckglDto.setCk(hwxxDto.getCkid()); //仓库id
                    serial_int = serial_int +1;
                    String ckdh="00"+ serial_int;
                    ckglDto.setCkdh(prefix+ckdh.substring(ckdh.length()-3)); //出库单号
                    ckglDto.setCklb(jcsjDto_t.getCsid()); //出库类别
                    ckglDto.setZt("80"); //状态
                    ckglDto.setLrry(thglDto_t.getXgry()); //录入人员
                    ckglDto.setCkrq(thglDto_t.getDjrq()); //出库日期
                    ckglDtos.add(ckglDto);
                    for (ThmxDto thmxDto_t:dtoList) {
                        if(thmxDto_t.getCk().equals(hwxxDto.getCkid())) {
                            //组装出库明细数据
                            CkmxDto ckmx = new CkmxDto();
                            ckmx.setCkid(ckglDto.getCkid()); //出库id
                            ckmx.setCkmxid(StringUtil.generateUUID()); //生成明细id
                            thmxDto_t.setCkid(ckglDto.getCkid());
                            thmxDto_t.setCkmxid(ckmx.getCkmxid()); //发货明细存入关联id
                            ckmx.setHwid(thmxDto_t.getHwid()); //货物id
                            ckmx.setCksl(thmxDto_t.getThsl()); //出库数量
                            ckmx.setLrry(thglDto_t.getXgry()); //录入人员
                            ckmxDtos.add(ckmx);
                        }
                    }
                }
                boolean success = ckglService.insertCkgls(ckglDtos);
                if (!success){
                    throw new BusinessException("msg","添加出库数据出错！");
                }

                success = ckmxService.insertckmxs(ckmxDtos);
                if (!success){
                    throw new BusinessException("msg","添加出库明细数据出错！");
                }
                if(!"1".equals(systemreceiveflg) && StringUtil.isNotBlank(matridxdsflag)){
                    if (!rdRecordService.determine_Entry(thglDto_t.getDjrq())){
                        throw new BusinessException("ICOM99019","该月份已结账，不允许再录入U8数据，请修改单据日期!");
                    }
                    Map<String, Object> map = rdRecordService.addU8ThData(thglDto_t,dtoList);
                    @SuppressWarnings("unchecked")
                    List<ThmxDto> thmxDtoList = (List<ThmxDto>) map.get("thmxDtos");
                    @SuppressWarnings("unchecked")
                    List<CkglDto> ckgls = (List<CkglDto>) map.get("ckglDtos");
                    ThglDto thgl = (ThglDto) map.get("thglDto");
                    thgl.setXgry(thglDto_t.getXgry());
                    success = update(thgl);
                    if(!success) {
                        throw new BusinessException("msg","更新退货关联id失败！");
                    }
                    success = ckglService.updateCkgls(ckgls);
                    if(!success) {
                        throw new BusinessException("msg","更新出库关联id失败！");
                    }
                    success = thmxService.updateThmxDtos(thmxDtoList);
                    if(!success) {
                        throw new BusinessException("msg","更新退货明细关联id失败！");
                    }
                }

                List<XsmxDto> xsmxDtos = new ArrayList<>();
                for (ThmxDto dto : dtoList) {
                    XsmxDto xsmxDto = new XsmxDto();
                    xsmxDto.setXsmxid(dto.getXsmxid());
                    xsmxDto.setYfhsl(dto.getThsl());
                    xsmxDto.setFhbj("0");
                    xsmxDtos.add(xsmxDto);
                }
                success = xsmxService.updateXsmxList(xsmxDtos);
                if (!success){
                    throw new BusinessException("msg","同步销售明细已发货数量失败！");
                }
                //生成到货单自动提交
                //仓库ids
                List<String> ckids = new ArrayList<>();
                List<String> dhids = new ArrayList<>();
                String year = DateUtils.getCustomFomratCurrentDate("yyyy");
                String date_t = DateUtils.getCustomFomratCurrentDate("MMdd");
                String dhrq = DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd");
                String prefix_t = "DH" + "-" + year + "-" + date_t + "-";
                JcsjDto jcsjDto = new JcsjDto();
                jcsjDto.setJclb(BasicDataTypeEnum.ARRIVAL_CATEGORY.getCode());
                jcsjDto.setCsdm("TH");
                jcsjDto=jcsjService.getDto(jcsjDto);
                // 查询流水号
                int serial_t = Integer.parseInt(dhxxDao.getDhdhSerial(prefix_t));
                List<DhxxDto> dhxxDtos = new ArrayList<>();
                List<HwxxDto> hwxxDtos = new ArrayList<>();
                for (ThmxDto dto : dtoList) {
                    if (!ckids.contains(dto.getCk())){
                        ckids.add(dto.getCk());
                        DhxxDto dhxxDto = new DhxxDto();
                        String dhid = StringUtil.generateUUID();
                        dhxxDto.setDhid(dhid);
                        dhxxDto.setDhdh(Integer.toString(serial_t).length()==1?prefix_t+"0"+serial_t:prefix_t+serial_t);
                        serial_t++;
                        dhxxDto.setDhrq(dhrq);
                        dhxxDto.setBm(operator.getJgid());
                        dhxxDto.setDhlx(jcsjDto.getCsid());
                        dhxxDto.setCkid(dto.getCk());
                        dhids.add(dhid);
                        dhxxDtos.add(dhxxDto);
                        for (ThmxDto thmxDto_t : dtoList) {
                            if (thmxDto_t.getCk().equals(dto.getCk())){
                                HwxxDto hwxxDto = new HwxxDto();
                                hwxxDto.setHwid(thmxDto_t.getHwid());
                                hwxxDto.setDhid(dhid);
                                hwxxDto.setDhbz(thmxDto_t.getBz());
                                hwxxDtos.add(hwxxDto);
                            }
                        }
                    }
                }
                boolean indhxxs = dhxxDao.insertDhxxDtos(dhxxDtos);
                if (!indhxxs){
                    throw new BusinessException("msg","新增到货信息失败");
                }
                boolean uphwxxs = hwxxDao.updateHwxxs(hwxxDtos);
                if (!uphwxxs){
                    throw new BusinessException("msg","修改货物信息失败");
                }
                ShgcDto shgcDto_t = new ShgcDto();
                shgcDto_t.setExtend_1(JSONObject.toJSONString(dhids.toArray()));
                shgcDto_t.setShlb(AuditTypeEnum.AUDIT_GOODS_ARRIVAL.getCode());
                shgcService.checkAndCommit(shgcDto_t,operator);
                thglDto.setZt(StatusEnum.CHECK_PASS.getCode());
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), ICOMM_SH00006,
                                    xxglService.getMsg("ICOMM_SH00121", operator.getZsxm(), shgcDto.getShlbmc(), thglDto_t.getThdh(),
                                            thglDto_t.getJsrmc(),
                                            thglDto_t.getXsbmmc(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }else {
                thglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                        spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00003"),
                                        xxglService.getMsg("ICOMM_SH00120"
                                                , operator.getZsxm(), shgcDto.getShlbmc(), thglDto_t.getThdh(),
                                                thglDto_t.getJsrmc(),
                                                thglDto_t.getXsbmmc(),
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
                            		shgcDto.getNo_spgwcyDtos().get(i).getYhid(),xxglService.getMsg("ICOMM_SH00004"), xxglService.getMsg("ICOMM_SH00005",shgcDto.getShlbmc() ,thglDto_t.getThdh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(thglDto);
        }
        return true;
    }

    @Override
    public List<ThglDto> getPagedDtoList(ThglDto thglDto) {
        List<ThglDto> list = dao.getPagedDtoList(thglDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.RETURN_GOODS.getCode(), "zt", "thid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        // TODO Auto-generated method stub
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String thid = shgcDto.getYwid();
                ThglDto thglDto = new ThglDto();
                thglDto.setThid(thid);
                thglDto.setXgry(operator.getYhid());
                thglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(thglDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String thid = shgcDto.getYwid();
                ThglDto thglDto = new ThglDto();
                thglDto.setThid(thid);
                thglDto.setXgry(operator.getYhid());
                thglDto.setZt(StatusEnum.CHECK_NO.getCode());
                update(thglDto);
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
        ThglDto thglDto = new ThglDto();
        thglDto.setIds(ids);
        List<ThglDto> dtoList = dao.getDtoList(thglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(ThglDto dto:dtoList){
                list.add(dto.getThid());
            }
        }
        map.put("list",list);
        return map;
    }

    @Override
    public List<ThglDto> getPagedAuditReturnManagement(ThglDto thglDto) {
        // 获取人员ID和履历号
        List<ThglDto> t_sbyzList = dao.getPagedAuditReturnManagement(thglDto);
        if(CollectionUtils.isEmpty(t_sbyzList))
            return t_sbyzList;

        List<ThglDto> sqList = dao.getAuditListReturnManagement(t_sbyzList);

        commonService.setSqrxm(sqList);

        return sqList;
    }
}
